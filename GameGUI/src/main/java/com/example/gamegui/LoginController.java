package com.example.gamegui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML public  Pane rootPane;
    @FXML private TextField user_name;
    @FXML private TextField user_pass;
    @FXML private Button login_btn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_btn.setDefaultButton(true);
        user_name.setText("ahmed");//delete it
        user_pass.setText("12345");
    }
    public  void login() throws IOException {
        Main.sendMessage("loginRequest:"+user_name.getText()+":"+user_pass.getText());
        System.out.println("login in loginController");
    }
    public  void signup() throws IOException {
        System.out.println("signup in loginController");
        Main.sendMessage("signUpRequest:"+user_name.getText()+":"+user_pass.getText());
    }



}
