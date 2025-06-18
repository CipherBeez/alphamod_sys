package lk.ijse.alpha.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

public class DashboardPageController {
    public AnchorPane ancDashboard;

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

    public void btnGoToItemPageOnAction(ActionEvent event) {
        navigateTo("/view/ItemView.fxml");
    }

    public void btnGoToSupplierPageOnAction(ActionEvent event) {
        navigateTo("/view/SupplierPage.fxml");
    }

    public void btnVisitOrderPage(ActionEvent actionEvent) {
        navigateTo("/view/OrderPage.fxml");
    }
}
