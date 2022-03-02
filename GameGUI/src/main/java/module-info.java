module com.example.gamegui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.gamegui to javafx.fxml;
    exports com.example.gamegui;
    exports handler;
    opens handler to javafx.fxml;
}