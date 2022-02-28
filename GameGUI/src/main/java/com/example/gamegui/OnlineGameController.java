package com.example.gamegui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
//    static ArrayList<Text> userNames;
    private static boolean myTurn;
    private static char winner;
    public static void setMyTurn(boolean myTurn) {
        OnlineGameController.myTurn = myTurn;
    }
    private static boolean gameOver;
    private static char playerShape;
    private static int tilesLeft = 9;
    public static String title;
    public static String message;
    public static boolean isGameOver() {
        return gameOver;
    }

    public OnlineGameController() {}
    public OnlineGameController(char playerShape, boolean Turn) {
        myTurn = Turn;
        setPlayerShape(playerShape);
    }
    public static void setMove(String movePositions, char moveShape)
    {
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
            Main.sendMessage("removeOtherPlayerRequest:"+Main.otherPlayerUserName,"Client");
            Main.sendMessage("updateScoreRequest:"+Main.otherPlayerUserName+":"+ 15,"Client");
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
        tilesLeft=9;
    }
    public void playAgain(){
        if(gameOver){
            Main.sendMessage("playAgainRequest:" + Main.otherPlayerUserName,"Client");
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
                        ":" + playerShape + ":" + move[1] + "_" + move[2],"Client");
                myTurn = false;
            }
        });
    }

    public static void gameLogicFire(Button playedButton, char gameShape) {
        tilesLeft--;
        Platform.runLater(() -> {
            playedButton.setText(String.valueOf(gameShape));
            playedButton.setDisable(true);
        });
        updateBoard(playedButton, gameShape);
        checkIfGameIsOver();
        DisabledAll();
    }
    public static void DisabledAll(){
        if (gameOver) {
            for (Button b : buttons)
                b.setDisable(true);
            setMessageValueAndShow();
        }
    }
    public static Button getButton(String buttonID) {
        for (Button cuButton : buttons)
            if (cuButton.getId().equals(buttonID))
                return cuButton;
        return null;
    }

    private static void updateBoard(Button playedButton, char gameShape) {
        String[] move = playedButton.getId().split("_");
        board[Integer.parseInt(move[1])][Integer.parseInt(move[2])] = gameShape;
    }
    public void saveGame(){
        //save game implementation
    }

    public static void checkIfGameIsOver() {
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
                winner='X';
                gameOver = true;
                if (playerShape=='X') Main.sendMessage("updateScoreRequest:"+Main.playerUserName+":"+10,"Client");
            }
            //O winner
            else if (line.equals("OOO")) {
                winner='O';
                gameOver = true;
                if (playerShape=='O') Main.sendMessage("updateScoreRequest:"+Main.playerUserName+":"+10,"Client");
            }
            //Tie
            if (tilesLeft <=0) {
                winner='T';
                gameOver = true;
            }
        }
    }
    private static void showAlert(String title, String message){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(message);
            alert.setResizable(false);
            alert.showAndWait();
        });
    }
    public static void setMessageValue(){
        if(gameOver==true){
            if(playerShape==winner) {
                title = "We have a Winner here:\" ";
                message = "Wonderful "+Main.playerUserName+" You Win this game GG :) ";
            }else if(winner=='T'){
                title = "Tie :| ";
                message="Oh no!! .... its a Tie!!";
            }
            else {
                title = "We have a loser here:\" ";
                message="Sorry "+ Main.playerUserName +" but you lost this game:\" ";
            }
        }
    }
    public static void setMessageValueAndShow(){
        setMessageValue();
        showAlert(title, message);
    }

}
