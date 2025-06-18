package lk.ijse.krishannew.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class OrderPageController {

    public Label lblOrderId;
    public Label orderDate;
    public TextField txtItemId;
    public ComboBox cmbItemName;
    public TextField txtAddToCartQty;
    public Label lblItemQty;
    public Label lblItemPrice;

    public TableView<OrderCartTm> tblOrder;
    public TableColumn<OrderCartTm , String> colItemId;
    public TableColumn<OrderCartTm , String> colItemName;
    public TableColumn<OrderCartTm , Integer> colQty;
    public TableColumn<OrderCartTm , Double> colPrice;
    public TableColumn<OrderCartTm , Double> colTotal;
    public TableColumn<? , ?> colAction;


    private final ObservableList<OrderCartTm> cartData = FXCollections.observableArrayList();

    public void btnAddToCartOnAction(ActionEvent event) {
    }

    public void OnClickTable(MouseEvent mouseEvent) {
    }

    public void btnResetOnAction(ActionEvent event) {
    }

    public void btnPlaceOrderOnAAction(ActionEvent event) {
    }
}
