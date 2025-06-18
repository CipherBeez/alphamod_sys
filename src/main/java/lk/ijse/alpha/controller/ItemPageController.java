package lk.ijse.alpha.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.alpha.dto.ItemDto;
import lk.ijse.alpha.dto.Tm.ItemTm;
import lk.ijse.alpha.model.ItemModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemPageController implements Initializable {

    public Label lblItemId;
    public TextField txtName;
    public TextField txtQuantity;
    public TextField txtBuyingPrice;
    public TextField txtSellingPrice;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;

    public TableView<ItemTm> tblItem;
    public TableColumn<ItemTm , String > colId;
    public TableColumn<ItemTm , String > colName;
    public TableColumn<ItemTm , Integer > colQuantity;
    public TableColumn<ItemTm , Double > colBuyPrice;
    public TableColumn<ItemTm , Double > colSellPrice;
    public TableColumn<ItemTm , Double > colTotal;

    private final ItemModel itemModel = new ItemModel();

    public TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colBuyPrice.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
        colSellPrice.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        try {
            resetPage();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR , "Not Items saved in system").show();
        }
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        tblItem.setItems(FXCollections.observableArrayList(
                itemModel.getAllItem().stream().map(itemDto -> new ItemTm(
                        itemDto.getItemId(),
                        itemDto.getItemName(),
                        itemDto.getQuantity(),
                        itemDto.getBuyPrice(),
                        itemDto.getSellPrice(),
                        itemDto.getTotal()
                )).toList()
        ));
    }

    private void resetPage() {
        try {
            loadTableData();
            loadNextId();

            btnSave.setDisable(false);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            txtName.setText(null);
            txtQuantity.setText(null);
            txtBuyingPrice.setText(null);
            txtSellingPrice.setText(null);

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR , "Not Items saved in system").show();
        }
    }
    
    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = itemModel.getNextItemId();
        lblItemId.setText(nextId);
    }

    public void btnSaveOnAction(ActionEvent event) {
        String itemId = lblItemId.getText();
        String itemName = txtName.getText();
        int quantity = Integer.parseInt(txtQuantity.getText());
        double buyPrice = Double.parseDouble(txtBuyingPrice.getText());
        double sellPrice = Double.parseDouble(txtSellingPrice.getText());
        double total = quantity * buyPrice;


        ItemDto itemDto = new ItemDto(
                itemId,
                itemName,
                quantity,
                buyPrice,
                sellPrice,
                total
        );
        
        try {
            boolean isSaved = itemModel.saveItem(itemDto);
            if (isSaved) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION , "Item Saved Successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR , "Failed to save Item").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save Item").show();
        }
    }

    public void btnUpdateOnAction(ActionEvent event) {
        String itemId = lblItemId.getText();
        String itemName = txtName.getText();
        String quantity = txtQuantity.getText();
        String buyingPrice = txtBuyingPrice.getText();
        String sellingPrice = txtSellingPrice.getText();
        String  total = String.valueOf(Double.parseDouble(quantity) * Double.parseDouble(buyingPrice));

        ItemDto itemDto = new ItemDto(
                itemId,
                itemName,
                Integer.parseInt(quantity),
                Double.parseDouble(buyingPrice),
                Double.parseDouble(sellingPrice),
                Double.parseDouble(total)
        );

        try {
            boolean isUpdated = itemModel.updateItem(itemDto);
            if (isUpdated) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION , "Item updated Successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR , "Failed to update Item").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to update Item ").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this item?",
                ButtonType.YES, ButtonType.NO
                );
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            String itemId = lblItemId.getText();
            try {
                boolean isDeleted = itemModel.deleteItem(itemId);
                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION , "Item deleted Successfully").show();
                } else {
                    new Alert(Alert.AlertType.ERROR , "Failed to delete Item").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to delete Item").show();
            }
        }
    }

    public void btnResetOnAction(ActionEvent event) {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        ItemTm selectedItem = tblItem.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            lblItemId.setText(selectedItem.getItemId());
            txtName.setText(selectedItem.getItemName());
            txtQuantity.setText(String.valueOf(selectedItem.getQuantity()));
            txtBuyingPrice.setText(String.valueOf(selectedItem.getBuyPrice()));
            txtSellingPrice.setText(String.valueOf(selectedItem.getSellPrice()));

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }


    public void search(KeyEvent event) {
        String searchText = searchField.getText();
        try {
            if (searchText.isEmpty()) {
                loadTableData();
            } else {
                tblItem.setItems(FXCollections.observableArrayList(
                        itemModel.searchItem(searchText).stream().map(itemDto -> new ItemTm(
                                itemDto.getItemId(),
                                itemDto.getItemName(),
                                itemDto.getQuantity(),
                                itemDto.getBuyPrice(),
                                itemDto.getSellPrice(),
                                itemDto.getTotal()
                        )).toList()
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to search items").show();
        }
    }
}
