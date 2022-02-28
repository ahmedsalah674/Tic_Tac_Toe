package com.example.gamegui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChooseDifficultyController implements Initializable {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BackgroundImage myBI= new BackgroundImage(new Image("https://cdn.pixabay.com/photo/2016/10/28/12/12/tic-tac-toe-1777880_960_720.jpg",600,400,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        rootPane.setBackground(new Background(myBI));
        btn1.setCursor(Cursor.HAND);
        btn2.setCursor(Cursor.HAND);
    }
    public void easy() throws IOException {
        GameController.setDifficulty(0);
        //System.out.println("play offline");
        AnchorPane pane = FXMLLoader.load(getClass().getResource("GameGui.fxml"));
        rootPane.getChildren().setAll(pane);
    }
    public void hard() throws IOException {
        GameController.setDifficulty(1);
        //System.out.println("play offline");
        AnchorPane pane = FXMLLoader.load(getClass().getResource("GameGui.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}