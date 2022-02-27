package com.example.gamegui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class Player {
    private String userName;
    private String password;
    private Button invitePlayer;
    private String status;
    private String score;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Button getInvitePlayer() {
        return invitePlayer;
    }

    public void setInvitePlayer(Button invitePlayer) {
        this.invitePlayer = invitePlayer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        switch (status) {
            case "0" -> this.status = "offline";
            case "1" -> this.status = "online";
            case "2" -> this.status = "inGame";
        }
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
    Player(String n, String st, String sc) {
        this.userName = n;
        setStatus(st);
        this.score = sc;
        this.invitePlayer = new Button("Invite");
        this.invitePlayer.setStyle("-fx-border-color: #000000; -fx-border-width: 2px; fx-text-fill: red; -fx-background-color: #FFFFFF");
        this.invitePlayer.setId(userName);
        this.invitePlayer.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (Main.otherPlayerUserName != null) {
                    Main.sendMessage("inviteResponse:false");
                }
                Main.sendMessage("inviteRequest:" + invitePlayer.getId());
                Main.otherPlayerUserName = invitePlayer.getId();
            }
        });
    }

    public void logout() {
        Main.sendMessage("logoutRequest");
    }

    public void login() {
        Main.sendMessage("loginRequest:" + userName + ":" + password);
    }
//    public static void setTimeout(Runnable runnable, int delay){
//        new Thread(() -> {
//            try {
//                Main.sendMessage("inviteRequest:"+invitePlayer.getId());
//                Thread.sleep(delay);
//                runnable.run();
//            }
//            catch (Exception e){
//                System.err.println(e);
//            }
//        }).start();
//    }
}
