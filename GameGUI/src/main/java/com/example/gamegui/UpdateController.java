package com.example.gamegui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {
    @FXML Text resultMessage;
//    @FXML Image resultImage;
    @FXML ImageView backImage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backImage.setCursor(Cursor.HAND);
        resultMessage.setText("""
                             I think he is afraid!!
                But don't worry bro I'm here for you,\s
              I will punish him take 5 points from him\040""".indent(7));
    }
    public void back(){
            Main.changeSceneName("ChooseGameGui.fxml");
    }
}
