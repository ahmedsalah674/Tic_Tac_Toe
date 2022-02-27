package com.example.gamegui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.Optional;

public class ClientHandler {
    public static void handleRequest(String response) {
        System.out.println(" handleRequest() response-> " + response);
        String[] responseParts = response.split(":");
        switch (responseParts[0]) {
            case "loginResponse" -> handleLogin(responseParts);
            case "signUpResponse" -> handleSignUp(responseParts);
            case "getPlayersResponse" -> handleGetPlayers(responseParts);
            case "getFriendsResponse" -> handleGetFriends(responseParts);
            case "inviteResponse" -> handleInviteResponse(responseParts);
            case "inviteMessageRequest" -> handleInviteMessageRequest(responseParts);
            case "gamePlayResponse" -> handleGamePlay(responseParts);
            case "playAgainResponse" -> handlePlayAgain(responseParts);
            case "removeOtherPlayerResponse" -> handleRemoveOtherPlayer(responseParts);
//            case "logoutResponse" -> handleLogout(responseParts);
            default -> System.out.println("Unexpected value for request: " + response);
        }
    }

    private static void handleLogin(String[] responseParts) {
        System.out.println("login handler response function: " + responseParts[0]);
        if (responseParts.length >= 2 && responseParts[1].equals("true")) {
            Main.playerUserName = responseParts[2];
            Main.changeSceneName("ChooseGameGui.fxml");
            System.out.println("logged in ");
        }
    }

    private static void handleSignUp(String[] responseParts) {
        System.out.println("SignUp handler response function: " + responseParts[0]);
//        if (responseParts.length >= 2 && responseParts[1].equals("true")) {
            Platform.runLater(() -> {
                System.out.println("in handleSignUp() and runLater() and user is " + Main.playerUserName);
                Alert alert = new Alert(Alert.AlertType.ERROR);
           if(responseParts[1].equals("true")) {
               alert.setTitle("SignUp Successfully");
               alert.setHeaderText("SignUp Successfully");
           } else {
               alert.setTitle("SignUp Failed");
               alert.setHeaderText("Sign Up Failed .... User already exists!");
           }
                alert.setResizable(false);
                alert.showAndWait();});
        Main.playerUserName=responseParts[2];
        System.out.println("signed up");
//        }
    }

    private static void handleGetPlayers(String[] responseParts) {
        if (responseParts.length >= 1) {
            ArrayList<Player> players = stringToPlayers(responseParts);
            new ChooseGameController(players);
        }
    }

    private static void handleGetFriends(String[] responseParts) {
        System.out.println("getFriends response handler function: " + responseParts[0]);
        ArrayList<Player> players = stringToPlayers(responseParts);
        new ChooseGameController(players);
    }

    private static ArrayList<Player> stringToPlayers(String[] responseParts) {
        ArrayList<Player> players = new ArrayList<>();
        int i = 0;
        for (String player : responseParts) {
            String[] playerData = player.split("-");
            if (i != 0 && playerData.length == 3) {
                players.add(new Player(playerData[0], playerData[1], playerData[2]));
            }
            i++;
        }
        return players;
    }

    private static void handleInviteResponse(String[] responseParts) {
        if (responseParts.length > 1 && responseParts[1].equals("true")) {
            Main.changeSceneName("OnlineGameGui.fxml");
            new OnlineGameController('O', true);
        } else {
            Main.otherPlayerUserName = null;
            Main.changeSceneName("ChooseGameGui.fxml");
        }
    }

    private static void handleInviteMessageRequest(String[] responseParts) {
        System.out.println("handleInviteMessageRequest()");
        if (responseParts.length > 1) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("invite Message");
                alert.setHeaderText(responseParts[1] + " has invited you to play with him");
                alert.setResizable(false);
//                        setTimeout(() -> System.out.println("test"), 7000,alert);
                Optional<ButtonType> result = alert.showAndWait();
                ButtonType button = result.orElse(ButtonType.CANCEL);
                if (button == ButtonType.OK) {
                    Main.otherPlayerUserName = responseParts[1];
                    Main.sendMessage("inviteResponse:" + responseParts[1] + ":true");
                    Main.changeSceneName("OnlineGameGui.fxml");
                    new OnlineGameController('X', false);
                } else {
                    Main.otherPlayerUserName = null;
                    Main.sendMessage("inviteResponse:" + responseParts[1] + ":false");
                }
            });
        }
    }

    public static void handleGamePlay(String[] responseParts) {
//        "gamePlayResponse:X:+0_1
        new OnlineGameController(responseParts[2], responseParts[1].charAt(0));
    }
    public static void setTimeout(Runnable runnable, int delay, Alert alert) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                try {
                    alert.close();
                } catch (Exception e) {
                    System.out.println("alert not found");
                }
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }
    private static void handlePlayAgain(String[] requestParts) {
        System.out.println("handlePlayAgain handler function: " + requestParts[0]);
        Main.changeSceneName("OnlineGameGui.fxml");
        OnlineGameController.changeGameOver(false);
        OnlineGameController.setMyTurn(false);
        OnlineGameController.resetBord();
    }

    private static void handleRemoveOtherPlayer(String[] responseParts) {
        System.out.println("handleRemoveOtherPlayer handler function: " + responseParts[0]);
        Platform.runLater(() -> {
            System.out.println("in handleRemoveOtherPlayer() and runLater() and user is " + Main.playerUserName);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("opponent surrendered!!");
            alert.setHeaderText("congrats your opponent surrendered you win this game and win 5 points as bonus");
            alert.setResizable(false);
            alert.showAndWait();
//            Optional<ButtonType> result =
//            ButtonType button = result.orElse(ButtonType.CANCEL);
//            if (button == ButtonType.OK) {
//                Main.otherPlayerUserName = responseParts[1];
//                Main.sendMessage("inviteResponse:" + responseParts[1] + ":true");
//                Main.changeSceneName("OnlineGameGui.fxml");
//                new OnlineGameController('X', false);
//            } else {
//                Main.otherPlayerUserName = null;
//                Main.sendMessage("inviteResponse:" + responseParts[1] + ":false");
//            }
        });
        Main.changeSceneName("ChooseGameGui.fxml");
        Main.otherPlayerUserName=null;
    }
//
//    private static void handleGameSave(String[] requestParts) {
//        System.out.println("handleGameSave handler function: " + requestParts[0]);
//    }
//    private static void handleLogout(String[] requestParts) {
//        System.out.println("handleGameSave handler function: " + requestParts[0]);
////        usedClientForHandler.sendResponseMessage("logoutResponse:"+usedClientForHandler.clientUser.playerDate.logout());
//    }
}
