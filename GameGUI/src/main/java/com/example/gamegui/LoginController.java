package com.example.gamegui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML public Pane rootPane;
    @FXML private TextField user_name;
    @FXML private TextField user_pass;
    @FXML private Button login_btn;
    @FXML private Button signup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_btn.setDefaultButton(true);
        login_btn.setCursor(Cursor.HAND);
        signup.setCursor(Cursor.HAND);
//        user_name.setText("ahmed");//delete it
//        user_pass.setText("12345");
    }

    private boolean checkField() {
        return !user_name.getText().isEmpty() && !user_pass.getText().isEmpty();
    }

    private void showAlert(Alert.AlertType type, String title, String header) {
//        "information", "warning", "error", "confirmation"
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setResizable(false);
            alert.setHeaderText(header);
            alert.showAndWait();
        });
    }

    public void login() throws IOException {
        if (checkField()) {
            Main.sendMessage("loginRequest:" + user_name.getText() + ":" + user_pass.getText(), "Client");
            System.out.println("login in loginController");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login ERROR", "Please Fill All Fields Then Press Login or Enter");
        }
    }

    public void signup() throws IOException {
        System.out.println("signup in loginController");
        if (checkField()) {
            Main.sendMessage("signUpRequest:" + user_name.getText() + ":" + user_pass.getText(), "Client");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login ERROR", "Please Fill All Fields Then Press Signup");
        }
        //        if(Main.playerUserName!=null)
//            Main.s.setTitle(Main.playerUserName);
    }


}
