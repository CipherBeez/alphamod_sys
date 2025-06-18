module lk.ijse.krishannew {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires com.google.protobuf;


    opens lk.ijse.krishannew.controller to javafx.fxml;
    opens lk.ijse.krishannew.dto.Tm to javafx.base;
    opens lk.ijse.krishannew.dto to javafx.base;

    exports lk.ijse.krishannew;
}