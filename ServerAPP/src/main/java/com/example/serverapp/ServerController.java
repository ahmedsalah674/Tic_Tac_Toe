package com.example.serverapp;
import client.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

        public String getUserName() {
            return userName;
        }

        public String getStatus() {
            return status;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

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
    static ObservableList<PlayerGui> List = FXCollections.observableArrayList();
    boolean serverStatus;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serverStatus=false;
        StartServerBtn.setStyle("-fx-background-color: red; -fx-text-fill: white");
        name.setCellValueFactory(new PropertyValueFactory<>("userName"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
    }
//    public void updateList(){
//
//    }
    public void controlServer()
    {
        if(!serverStatus) {
            Player serverPlayer = new Player();
            ArrayList<PlayerGui> players= PlayerGui.convertPlayers(serverPlayer.getPlayers());
                if(players!=null) {
                    List.addAll(players);
                    myTable.setItems(List);
                }
            StartServerBtn.setText("Running");
            StartServerBtn.setStyle("-fx-background-color: green; -fx-text-fill: white");
            serverStatus=true;
        }
        else{
            {
                List.clear();
                myTable.setItems(List);
                StartServerBtn.setText("Start Server");
                StartServerBtn.setStyle("-fx-background-color: red; -fx-text-fill: white");
                serverStatus=false;
            }
        }
    }
}
