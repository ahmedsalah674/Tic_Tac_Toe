package com.example.gamegui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML private Button button1;
    @FXML private Button button2;
    @FXML private Button button3;
    @FXML private Button button4;
    @FXML private Button button5;
    @FXML private Button button6;
    @FXML private Button button7;
    @FXML private Button button8;
    @FXML private Button button9;
    @FXML private Label txt;
    private int board[][] = new int[3][3];
    private int movesLeft = 9;
    private  boolean gameOver = false;
    ArrayList<Button> buttons;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));
        buttons.forEach(button ->{
            setupCursor(button);
            setupButton(button);
            button.setFocusTraversable(false);
        });
    }
    @FXML
    void reset(ActionEvent event) {
        movesLeft = 9;
        gameOver = false;
        for (int[] row: board)
            Arrays.fill(row, 0);
        buttons.forEach(this::resetButton);
        txt.setText("");
    }
    public void back(){
        Main.changeSceneName("ChooseGameGui.fxml");
    }
    public void resetButton(Button button){
        button.setDisable(false);
        button.setText("");
    }
    private void setupCursor(Button button){
        button.setOnMouseEntered(mouseEvent -> {
            button.setCursor(Cursor.HAND);
        });
    }
    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            button.setDisable(true);
            button.setStyle("-fx-text-fill: black");
            button.setText("X");
            updateBoard(button);
            checkIfGameIsOver();
            movesLeft--;
            if(movesLeft > 1 && !gameOver) {
                AImove();
                movesLeft--;
                checkIfGameIsOver();
            }
        });
    }

    private void AImove(){
        int x = 0;
        int y = 0;
        Random rand = new Random();
        do{
            x = rand.nextInt(3);
            y = rand.nextInt(3);
        }while(board[x][y] != 0);
        board[x][y] = 2;

        if(x == 0 && y == 0)
        {
            button1.setStyle("-fx-text-fill: red");
            button1.setText("O");
            button1.setDisable(true);
        }
        else if(x == 0 && y == 1){
            button2.setStyle("-fx-text-fill: red");
            button2.setText("O");
            button2.setDisable(true);
        }
        else if(x == 0 && y == 2){
            button3.setStyle("-fx-text-fill: red");
            button3.setText("O");
            button3.setDisable(true);
        }
        else if(x == 1 && y == 0){
            button4.setStyle("-fx-text-fill: red");
            button4.setText("O");
            button4.setDisable(true);
        }
        else if(x == 1 && y == 1){
            button5.setStyle("-fx-text-fill: red");
            button5.setText("O");
            button5.setDisable(true);
        }
        else if(x == 1 && y == 2){
            button6.setStyle("-fx-text-fill: red");
            button6.setText("O");
            button6.setDisable(true);
        }
        else if(x == 2 && y == 0){
            button7.setStyle("-fx-text-fill: red");
            button7.setText("O");
            button7.setDisable(true);
        }
        else if(x == 2 && y == 1){
            button8.setStyle("-fx-text-fill: red");
            button8.setText("O");
            button8.setDisable(true);
        }
        else if(x == 2 && y == 2){
            button9.setStyle("-fx-text-fill: red");
            button9.setText("O");
            button9.setDisable(true);
        }
    }
    private void updateBoard(Button b){
        switch (b.getId()){
            case "button1"->board[0][0] = 1;
            case "button2"->board[0][1] = 1;
            case "button3"->board[0][2] = 1;
            case "button4"->board[1][0] = 1;
            case "button5"->board[1][1] = 1;
            case "button6"->board[1][2] = 1;
            case "button7"->board[2][0] = 1;
            case "button8"->board[2][1] = 1;
            case "button9"->board[2][2] = 1;
        }
    }
    public void checkIfGameIsOver(){
        for (int a = 0; a < 8; a++) {
            String line = switch (a) {
                case 0 -> button1.getText() + button2.getText() + button3.getText();
                case 1 -> button4.getText() + button5.getText() + button6.getText();
                case 2 -> button7.getText() + button8.getText() + button9.getText();
                case 3 -> button1.getText() + button5.getText() + button9.getText();
                case 4 -> button3.getText() + button5.getText() + button7.getText();
                case 5 -> button1.getText() + button4.getText() + button7.getText();
                case 6 -> button2.getText() + button5.getText() + button8.getText();
                case 7 -> button3.getText() + button6.getText() + button9.getText();
                default -> null;
            };

            //X winner
            if (line.equals("XXX")) {
                txt.setText("X won!");
                buttons.forEach(button -> button.setDisable(true));
                gameOver = true;

            }

            //O winner
            else if (line.equals("OOO")) {
                txt.setText("O won!");
                buttons.forEach(button -> button.setDisable(true));
                gameOver = true;
            }

            //Tie
            if(movesLeft == 0)
            {

                txt.setText("Oh NO!! .... Its A Tie!");
                gameOver = true;
            }
        }
    }

}