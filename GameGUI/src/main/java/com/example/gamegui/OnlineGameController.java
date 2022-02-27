package com.example.gamegui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class OnlineGameController implements Initializable {
    @FXML private Button button_0_0;
    @FXML private Button button_0_1;
    @FXML private Button button_0_2;
    @FXML private Button button_1_0;
    @FXML private Button button_1_1;
    @FXML private Button button_1_2;
    @FXML private Button button_2_0;
    @FXML private Button button_2_1;
    @FXML private Button button_2_2;
    //    @FXML private Button playAgain;
    @FXML private Label txt;
    private static char[][] board = new char[3][3];
    static ArrayList<Button> buttons;
    private static boolean myTurn;

    public static void setMyTurn(boolean myTurn) {
        OnlineGameController.myTurn = myTurn;
    }
    private static boolean gameOver;
    private static char playerShape;
    private int tilesLeft = 9;

    public static boolean isGameOver() {
        return gameOver;
    }

    public OnlineGameController() {}
    public OnlineGameController(char playerShape, boolean Turn) {
        myTurn = Turn;
        setPlayerShape(playerShape);
    }
    public OnlineGameController(String movePositions, char moveShape) {
        gameLogicFire(getButton("button_" + movePositions), moveShape);
        myTurn = true;
    }
    public void setPlayerShape(char playerShape) {
        if (playerShape == 'O' || playerShape == 'X')
            OnlineGameController.playerShape = playerShape;
    }
    public void back(){
        if(Main.otherPlayerUserName!=null&&!OnlineGameController.isGameOver()) {
            System.out.println("in back() in main and user is " + Main.playerUserName);
            Main.sendMessage("removeOtherPlayerRequest:"+Main.otherPlayerUserName);
            Main.sendMessage("updateScoreRequest:"+Main.otherPlayerUserName+":"+ 15);
            Main.changeSceneName("ChooseGameGui.fxml");
            Main.otherPlayerUserName=null;
        }
    }
    public static void changeGameOver(boolean value){ gameOver=value; }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button_0_0, button_0_1, button_0_2, button_1_0,
                button_1_1, button_1_2, button_2_0, button_2_1, button_2_2));
        buttons.forEach(button -> {
            setupButton(button);
            button.setFocusTraversable(false);
        });
        gameOver=false;
    }
    public static void resetBord(){
        board=new char[3][3];
    }
    public void playAgain(){
        if(gameOver){
            Main.sendMessage("playAgainRequest:" + Main.otherPlayerUserName);
            Main.changeSceneName("OnlineGameGui.fxml");
            resetBord();
            myTurn = true;
            gameOver = false;
        }
    }
    public void setupButton(Button playedButton) {
        playedButton.setOnMouseClicked(mouseEvent -> {
            System.out.println("here in mouseEvent myTurn "+ myTurn );
            System.out.println("here in mouseEvent  gameOver"+ gameOver );
            if (myTurn && !gameOver) {
                gameLogicFire(playedButton, playerShape);
                String[] move = playedButton.getId().split("_");
                Main.sendMessage("gamePlayRequest:" + Main.playerUserName + ":" + Main.otherPlayerUserName +
                        ":" + playerShape + ":" + move[1] + "_" + move[2]);
                myTurn = false;
            }
        });
    }

    public void gameLogicFire(Button playedButton, char gameShape) {
        tilesLeft--;
        Platform.runLater(() -> {
            playedButton.setText(String.valueOf(gameShape));
            playedButton.setDisable(true);
        });
        updateBoard(playedButton, gameShape);
        checkIfGameIsOver();
        DisabledAll();
    }
    public void DisabledAll(){
        if (gameOver)
            for (Button b:buttons)
                b.setDisable(true);
    }
    public Button getButton(String buttonID) {
        for (Button cuButton : buttons)
            if (cuButton.getId().equals(buttonID))
                return cuButton;
        return null;
    }

    private void updateBoard(Button playedButton, char gameShape) {
        String[] move = playedButton.getId().split("_");
        board[Integer.parseInt(move[1])][Integer.parseInt(move[2])] = gameShape;
    }
    public void saveGame(){
        //save game implementation
    }

    public void checkIfGameIsOver() {
        for (int a = 0; a < 8; a++) {
            String line = switch (a) {
                case 0 -> String.valueOf(board[0][0]) + board[0][1] + board[0][2];
                case 1 -> String.valueOf(board[1][0]) + board[1][1] + board[1][2];
                case 2 -> String.valueOf(board[2][0]) + board[2][1] + board[2][2];

                case 3 -> String.valueOf(board[0][0]) + board[1][1] + board[2][2];
                case 4 -> String.valueOf(board[0][2]) + board[1][1] + board[2][0];

                case 5 -> String.valueOf(board[0][0]) + board[1][0] + board[2][0];
                case 6 -> String.valueOf(board[0][1]) + board[1][1] + board[2][1];
                case 7 -> String.valueOf(board[0][2]) + board[1][2] + board[2][2];
                default -> null;
            };
            //X winner
            if (line.equals("XXX")) {
//                Platform.runLater(() -> {
                if(txt==null) {
                    txt = new Label();
                }
                txt.setText("X won!");
//                });
                gameOver = true;
                if (playerShape=='X') Main.sendMessage("updateScoreRequest:"+Main.playerUserName+":"+10);
            }
            //O winner
            else if (line.equals("OOO")) {
//                Platform.runLater(() -> {
                if(txt==null) {
                    txt = new Label();
                }
                txt.setText("O won!");
//                });
                gameOver = true;
                if (playerShape=='O') Main.sendMessage("updateScoreRequest:"+Main.playerUserName+":"+10);
            }
            //Tie
            if (tilesLeft == 0) {
//                Platform.runLater(() -> {
//                    txt.setText("Oh NO!! .... Its A Tie!");
//                });
                gameOver = true;
            }
        }
    }
}
