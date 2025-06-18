package lk.ijse.alpha.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.alpha.dto.CartDto;
import lk.ijse.alpha.dto.ItemDto;
import lk.ijse.alpha.dto.OrderDto;
import lk.ijse.alpha.dto.Tm.CartTm;
import lk.ijse.alpha.model.ItemModel;
import lk.ijse.alpha.model.OrderModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderPageController implements Initializable {

    public Label lblOrderId;
    public Label orderDate;
    public ComboBox<String> cmbItemName;
    public TextField txtAddToCartQty;
    public Label lblItemQty;
    public Label lblItemPrice;
    public Label lblFinalTotalAmount;
    public Label lblProfit;
    public Label lblTotalItemsSold;

    public TableView<CartTm> tblOrder;
    public TableColumn<CartTm, String> colItemId;
    public TableColumn<CartTm, String> colItemName;
    public TableColumn<CartTm, Integer> colQty;
    public TableColumn<CartTm, Double> colPrice;
    public TableColumn<CartTm, Double> colTotal;
    public TableColumn<?, ?> colAction;

    private final OrderModel orderModel = new OrderModel();
    private final ItemModel itemModel = new ItemModel();

    private final ObservableList<CartTm> cartData = FXCollections.observableArrayList();
    public AnchorPane ancDashboard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValues();

        try {
            refreshPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load data....").show();
        }
    }

    private void setCellValues() {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("cartQty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));

        tblOrder.setItems(cartData);
    }

    private void refreshPage() throws Exception {
        lblOrderId.setText(orderModel.getNextOrderId());
        orderDate.setText(LocalDate.now().toString());
        loadItemIds();
    }

    private void loadItemIds() throws SQLException {
        ArrayList<String> itemNameLists = itemModel.getItemNames();
        cmbItemName.setItems(FXCollections.observableArrayList(itemNameLists));
    }

    public void btnAddToCartOnAction(ActionEvent event) {
        String selectedItemId = cmbItemName.getValue();
        String cartQtyString = txtAddToCartQty.getText();
        String itemName = cmbItemName.getValue();

        if (selectedItemId == null || cartQtyString == null || !cartQtyString.matches("\\d+")) {
            new Alert(Alert.AlertType.WARNING, "Select item and enter valid quantity").show();
            return;
        }

        int cartQty = Integer.parseInt(cartQtyString);
        int stockQty = Integer.parseInt(lblItemQty.getText());

        if (stockQty < cartQty) {
            new Alert(Alert.AlertType.WARNING, "Insufficient stock").show();
            return;
        }

        double unitPrice = Double.parseDouble(lblItemPrice.getText());
        double total = cartQty * unitPrice;

        for (CartTm cartTm : cartData) {
            if (cartTm.getItemId().equals(selectedItemId)) {
                int newQty = cartTm.getCartQty() + cartQty;
                if (newQty > stockQty) {
                    new Alert(Alert.AlertType.WARNING, "Not enough stock").show();
                    return;
                }
                cartTm.setCartQty(newQty);
                cartTm.setTotal(newQty * unitPrice);
                tblOrder.refresh();
                txtAddToCartQty.clear();
                updateFinalDetails();
                return;
            }
        }

        Button btnRemove = new Button("Remove");
        CartTm tm = new CartTm(selectedItemId, itemName, cartQty, unitPrice, total, btnRemove);

        btnRemove.setOnAction(e -> {
            cartData.remove(tm);
            tblOrder.refresh();
            updateFinalDetails();
        });

        cartData.add(tm);
        txtAddToCartQty.clear();
        updateFinalDetails();
    }

    private void updateFinalDetails() {
        double totalAmount = 0.0;
        int totalItems = 0;

        for (CartTm tm : cartData) {
            totalAmount += tm.getTotal();
            totalItems += tm.getCartQty();
        }

        lblFinalTotalAmount.setText(String.format("%.2f", totalAmount));
        lblTotalItemsSold.setText(String.valueOf(totalItems));
    }

    public void btnPlaceOrderOnAAction(ActionEvent event) {
        if (cartData.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Cart is empty").show();
            return;
        }

        String orderId = lblOrderId.getText();
        String date = orderDate.getText();

        ArrayList<CartDto> cartList = new ArrayList<>();
        for (CartTm tm : cartData) {
            CartDto dto = new CartDto(
                    orderId,
                    tm.getItemId(),
                    tm.getItemName(),
                    tm.getCartQty(),
                    tm.getUnitPrice(),
                    tm.getTotal(),
                    tm.getBtnRemove()
            );
            cartList.add(dto);
        }

        OrderDto orderDto = new OrderDto(orderId, date);

        try {
            boolean isPlaced = orderModel.placeOrder(orderDto, cartList);
            if (isPlaced) {
                cartData.clear();
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Order placed successfully").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to place order").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error occurred while placing order").show();
        }
    }

    public void cmbItemNameSelected(ActionEvent event) throws SQLException {
        String itemName = cmbItemName.getValue();
        ItemDto itemDto = itemModel.findIdByName(itemName);
        if (itemDto != null) {
            lblItemQty.setText(String.valueOf(itemDto.getQuantity()));
            lblItemPrice.setText(String.valueOf(itemDto.getSellPrice()));
        } else {
            lblItemQty.setText("");
            lblItemPrice.setText("");
        }
    }

    public void btnDeleteOnAction(ActionEvent event) {
        // implement if needed
    }

    public void OnClickTable(MouseEvent mouseEvent) {
        // implement if needed
    }

    public void btnResetOnAction(ActionEvent event) {
        cartData.clear();
        tblOrder.refresh();
    }

    public void gotoDashboard(MouseEvent mouseEvent) {
        // implement navigation
        navigateTo("/view/DashboardPage.fxml");
    }
    private void navigateTo(String path) {
        try {
            ancDashboard.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancDashboard.widthProperty());
            anchorPane.prefHeightProperty().bind(ancDashboard.heightProperty());

            ancDashboard.getChildren().add(anchorPane);

        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }
}
