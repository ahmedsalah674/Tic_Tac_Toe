package com.example.gamegui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
//import javafx.scene.image.Image;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultController implements Initializable {
    @FXML Button PlayAgain;
    @FXML Text resultMessage;
    @FXML ImageView backImage;
    @FXML Image resultImage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PlayAgain.setCursor(Cursor.HAND);
        PlayAgain.setDefaultButton(true);
        backImage.setCursor(Cursor.HAND);
        if(Main.otherPlayerUserName!=null) {
            resultMessage.setText(OnlineGameController.message);
            if(OnlineGameController.winner==OnlineGameController.playerShape)
                resultMessage.setLayoutX(90);
            else if(OnlineGameController.winner=='T')
                resultMessage.setLayoutX(200);
            else
                resultMessage.setLayoutX(110);

        }
    }
    public void PlayAgain(){
        if(Main.otherPlayerUserName!=null)
            OnlineGameController.playAgain();
        else
            Main.changeSceneName("GameGui.fxml");
    }
    public void back(){
        if(Main.otherPlayerUserName!=null) Main.leaveGame();
        else
            Main.changeSceneName("ChooseGameGui.fxml");
    }
}
