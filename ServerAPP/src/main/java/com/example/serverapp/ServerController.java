package com.example.serverapp;
import client.Client;
import client.Player;
import client.User;
import handler.RequestHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
public class ServerController  implements Initializable {

    public static class PlayerGui {
        private String userName;
        private String status;
        private int score;

        public PlayerGui(){}
        public static ArrayList<PlayerGui> convertPlayers(ArrayList<Player> players){
            ArrayList<PlayerGui> playersGui=new ArrayList<>();
            if(players!=null)
                for (Player thisPlayer:players)
                    playersGui.add(new PlayerGui(thisPlayer));
            return playersGui;
        }

        public PlayerGui(Player player) {
            userName=player.getUserName();
            setStatus(player.getStatus());
            score=player.getScore();
        }

//        public String getUserName() {
//            return userName;
//        }
//
//        public String getStatus() {
//            return status;
//        }
//
//        public int getScore() {
//            return score;
//        }
//
//        public void setScore(int score) {
//            this.score = score;
//        }

        public void setStatus(int status) {
            switch (status) {
                case 0 -> this.status = "offline";
                case 1 -> this.status = "online";
                case 2 -> this.status = "inGame";
            }
        }
    }
    @FXML private TableView <PlayerGui> myTable;
    @FXML private TableColumn<PlayerGui, String> name;
    @FXML private TableColumn<PlayerGui, Integer> score;
    @FXML private TableColumn<PlayerGui, String> status;
    @FXML private Button StartServerBtn;
    @FXML private TextArea serverLogs;
//    private static ArrayList<TextArea> logs;
    public static ObservableList<PlayerGui> List = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serverLogs.setStyle("-fx-background-radius: 5px;-fx-padding: 3px");
        serverLogs.setCursor(Cursor.DEFAULT);
//        serverLogs.setDisable(true);
        serverLogs.setEditable(false);
//        logs=new ArrayList<>(Arrays.asList(serverLogs));
//        System.out.println("initialize----------------"+Main.running);
        StartServerBtn.setStyle("-fx-background-color: red; -fx-text-fill: white");
        name.setCellValueFactory(new PropertyValueFactory<>("userName"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        StartServerBtn.setDefaultButton(true);
        StartServerBtn.setCursor(Cursor.HAND);
        if(Main.running){
            Player serverPlayer = new Player();
            ArrayList<PlayerGui> players= PlayerGui.convertPlayers(serverPlayer.getPlayers());
            if(players!=null) {
                List.clear();
                List.addAll(players);
                myTable.setItems(List);
            }
            StartServerBtn.setText("Running");
            StartServerBtn.setStyle("-fx-background-color: green; -fx-text-fill: white");
        }
    }
    public void controlServer()
    {
        if(!Main.running) {
            Player serverPlayer = new Player();
            ArrayList<PlayerGui> players= PlayerGui.convertPlayers(serverPlayer.getPlayers());
                if(players!=null) {
                    List.addAll(players);
                    myTable.setItems(List);
                }
            StartServerBtn.setText("Running");
            StartServerBtn.setStyle("-fx-background-color: green; -fx-text-fill: white");
            Main.running=true;
            RequestHandler.handleRequest("Server:unlockedAll", new Client(new User(new Socket())));
        }
        else{
            {
                List.clear();
                myTable.setItems(List);
                StartServerBtn.setText("Start Server");
                StartServerBtn.setStyle("-fx-background-color: red; -fx-text-fill: white");
                Main.running=false;
                RequestHandler.handleRequest("Server:lockedAll", new Client(new User(new Socket())));
            }
        }
    }
//    public static void addLog(String log){
//        logs.get(0).appendText(log+"\n");
//    }
}
