package com.example.gamegui;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChooseGameController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML private TableView<Player> myTable;
    @FXML private TableColumn<Player, String> name;
    @FXML private TableColumn<Player, String> score;
    @FXML private TableColumn<Player, String> status;
    @FXML private TableColumn<Player, String> invite;
//    @FXML private Button b1;

    static ObservableList<Player> List = FXCollections.observableArrayList();
    public ChooseGameController() {
        Main.sendMessage("getPlayersRequest");
    }
    public ChooseGameController(ArrayList<Player> players) {
        List.clear();
        List.addAll(players);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<>("userName"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        invite.setCellValueFactory(new PropertyValueFactory<>("invitePlayer"));
        Main.sendMessage("getPlayersRequest");
        myTable.setItems(List);
    }
    public void playOffline() throws IOException {
        System.out.println("play offline");
        AnchorPane pane = FXMLLoader.load(getClass().getResource("GameGui.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
