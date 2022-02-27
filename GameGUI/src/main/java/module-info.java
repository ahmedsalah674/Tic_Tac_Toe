module com.example.gamegui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gamegui to javafx.fxml;
    exports com.example.gamegui;
}