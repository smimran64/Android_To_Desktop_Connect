module com.example.desktopdatareceiver {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // FXML controllers
    opens com.example.desktopdatareceiver.controller to javafx.fxml;

    // TableView model reflection
    opens com.example.desktopdatareceiver.model to javafx.base;


    exports app;
}
