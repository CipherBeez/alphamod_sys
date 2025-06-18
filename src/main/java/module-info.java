module lk.ijse.alpha {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires com.google.protobuf;


    opens lk.ijse.alpha.controller to javafx.fxml;
    opens lk.ijse.alpha.dto.Tm to javafx.base;
    opens lk.ijse.alpha.dto to javafx.base;

    exports lk.ijse.alpha;
}