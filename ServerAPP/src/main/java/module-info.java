module com.example.serverapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.serverapp to javafx.fxml;
    exports com.example.serverapp;
    exports client;
    opens client to javafx.fxml;
    exports handler;
    opens handler to javafx.fxml;
}