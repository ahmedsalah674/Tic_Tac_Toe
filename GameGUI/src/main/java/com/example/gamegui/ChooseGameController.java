package com.example.gamegui;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

//import java.io.IOException;
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
    @FXML private Button playOffline;

    static ObservableList<Player> List = FXCollections.observableArrayList();
    public ChooseGameController() {
        Main.sendMessage("getPlayersRequest","Client");
    }
    public ChooseGameController(ArrayList<Player> players) {
        List.clear();
        List.addAll(players);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BackgroundImage myBI= new BackgroundImage(new Image("https://cdn.pixabay.com/photo/2015/11/15/18/42/white-1044659_960_720.jpg",600,400,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        rootPane.setBackground(new Background(myBI));
        name.setCellValueFactory(new PropertyValueFactory<>("userName"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        invite.setCellValueFactory(new PropertyValueFactory<>("invitePlayer"));
        playOffline.setCursor(Cursor.HAND);
        Main.sendMessage("getPlayersRequest","Client");
        myTable.setItems(List);
    }
    public void playOffline(){
//        System.out.println("play offline");
        Main.changeSceneName("ChooseDifficultyGui.fxml");
    }
}
