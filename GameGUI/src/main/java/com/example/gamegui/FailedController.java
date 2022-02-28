package com.example.gamegui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class FailedController implements Initializable {
    @FXML Button tryAgain;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tryAgain.setCursor(Cursor.HAND);
        tryAgain.setDefaultButton(true);
    }
    public void connectAgain(){
        if (Main.clientSocket!=null){
            Main.sendMessage("connectAgain", "Server");
        }
        else{
            if (Main.connectToServer()) {
                Main.changeSceneName("LoginGui.fxml");
            } else
                Main.changeSceneName("failed.fxml");
        }
        Main.otherPlayerUserName=null;
    }
}
