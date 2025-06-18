package lk.ijse.alpha.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import lk.ijse.alpha.dto.Tm.OrderCartTM;

public class OrderPageController {

    public Label lblOrderId;
    public Label orderDate;
    public TextField txtItemId;
    public ComboBox cmbItemName;
    public TextField txtAddToCartQty;
    public Label lblItemQty;
    public Label lblItemPrice;

    public TableView<OrderCartTM> tblOrder;
    public TableColumn<OrderCartTM , String> colItemId;
    public TableColumn<OrderCartTM , String> colItemName;
    public TableColumn<OrderCartTM , Integer> colQty;
    public TableColumn<OrderCartTM , Double> colPrice;
    public TableColumn<OrderCartTM , Double> colTotal;
    public TableColumn<? , ?> colAction;


    private final ObservableList<OrderCartTM> cartData = FXCollections.observableArrayList();

    public void btnAddToCartOnAction(ActionEvent event) {
    }

    public void OnClickTable(MouseEvent mouseEvent) {
    }

    public void btnResetOnAction(ActionEvent event) {
    }

    public void btnPlaceOrderOnAAction(ActionEvent event) {
    }
}
