package handler;

import com.example.gamegui.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.Optional;

public class ClientHandler {
    public static void handleRequest(String[] responseParts) {
//        System.out.println(" handleRequest() response-> " + responseParts[0]);
//        String[] responseParts = response.split(":");
        switch (responseParts[0]) {
            case "loginResponse" -> handleLogin(responseParts);
            case "signUpResponse" -> handleSignUp(responseParts);
            case "getPlayersResponse" -> handleGetPlayers(responseParts);
            case "getFriendsResponse" -> handleGetFriends(responseParts);
            case "inviteResponse" -> handleInviteResponse(responseParts);
            case "inviteMessageRequest" -> handleInviteMessageRequest(responseParts);
            case "gamePlayResponse" -> handleGamePlay(responseParts);
            case "playAgainResponse" -> handlePlayAgain(responseParts);
            case "leaveGameResponse" -> removeOtherPlayer();
            case "updateScoreResponse" -> handleUpdateScore();
            default -> System.out.println("Unexpected value for request: " + String.join(":",responseParts));
        }
    }
    private static void handleUpdateScore(){
        Main.changeSceneName("updateScore.fxml");
//        Platform.runLater(() -> {
////            System.out.println("in handleRemoveOtherPlayer() and runLater() and user is " + Main.playerUserName);
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("opponent surrendered!!");
//            alert.setHeaderText("congrats your opponent surrendered you win this game and win 5 points as bonus");
//            alert.setResizable(false);
//            alert.showAndWait();
//        });//replace it with show alert
    }

    private static void showAlert(Alert.AlertType type, String title, String header) {
//        "information", "warning", "error", "confirmation"
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setResizable(false);
            alert.setHeaderText(header);
            alert.showAndWait();
        });
    }

    private static void handleLogin(String[] responseParts) {
//        System.out.println("login handler response function: " + responseParts[0]);
        if (responseParts.length >= 2 && responseParts[1].equals("true")) {
            Main.playerUserName = responseParts[2];
            Main.changeSceneName("ChooseGameGui.fxml");
//            System.out.println("logged in ");
            Platform.runLater(() -> {Main.s.setTitle(responseParts[2]);});
        }
        else {
            showAlert(Alert.AlertType.ERROR,"Login Error","Failed Login TryAgain");
        }
    }

    private static void handleSignUp(String[] responseParts) {
//        System.out.println("SignUp handler response function: " + responseParts[0]);
        if(responseParts[1].equals("true")) {
            showAlert(Alert.AlertType.INFORMATION, "SignUp Successfully", "SignUp Successfully You Can Login Now");
            Main.playerUserName = responseParts[2];
            System.out.println("signed up");
        }
        else
            showAlert(Alert.AlertType.ERROR,"Sign up Error","Failed Signup Change This UserName And TryAgain");

    }

    private static void handleGetPlayers(String[] responseParts) {
        if (responseParts.length >= 1) {
            ArrayList<Player> players = stringToPlayers(responseParts);
            new ChooseGameController(players);
        }
    }

    private static void handleGetFriends(String[] responseParts) {
//        System.out.println("getFriends response handler function: " + responseParts[0]);
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
            Main.sendMessage("getGameRequest:"+Main.playerUserName+":"+Main.otherPlayerUserName, "Game");
        } else {
            Main.otherPlayerUserName = null;
            Main.sendMessage("leaveGameRequest", "Client");
            Main.changeSceneName("ChooseGameGui.fxml");
            OnlineGameController.resetGame();
        }
        Main.haveInvite=false;
    }

    private static void handleInviteMessageRequest(String[] responseParts) {
//        System.out.println("handleInviteMessageRequest()");
        if (responseParts.length > 1) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("invite Message");
                alert.setHeaderText(responseParts[1] + " has invited you to play with him");
                alert.setResizable(false);
                Optional<ButtonType> result = alert.showAndWait();
                ButtonType button = result.orElse(ButtonType.CANCEL);
                if (button == ButtonType.OK) {
                    Main.otherPlayerUserName = responseParts[1];
                    Main.sendMessage("inviteResponse:" + responseParts[1] + ":true","Client");
                    Main.changeSceneName("OnlineGameGui.fxml");
                    new OnlineGameController('X', false);
                    Main.sendMessage("getGameRequest:"+Main.playerUserName+":"+Main.otherPlayerUserName,"Game");
                } else {
                    Main.sendMessage("leaveGameRequest:"+":"+responseParts[1], "Client");
                    Main.otherPlayerUserName = null;
                    Main.sendMessage("inviteResponse:" + responseParts[1] + ":false","Client");
                }
            });
        }
    }

    public static void handleGamePlay(String[] responseParts) {
//        "gamePlayResponse:X:+0_1
        OnlineGameController.setMove(responseParts[2], responseParts[1].charAt(0));
        OnlineGameController.setMyTurn(true);
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
                System.err.println("ClientHandler-setTimeout ()"+e);
            }
        }).start();
    }
    private static void handlePlayAgain(String[] requestParts) {
//        System.out.println("handlePlayAgain handler function: " + requestParts[0]);
        Main.changeSceneName("OnlineGameGui.fxml");
        OnlineGameController.resetGame();
        OnlineGameController.setMyTurn(false);
    }
    private static void removeOtherPlayer(){
        Main.changeSceneName("ChooseGameGui.fxml");
        Main.otherPlayerUserName=null;
    }

//    private static void handleShowAlert(String[] responseParts) {
////        "showAlertResponse:saveGame"
//        System.out.println("handleShowAlert handler function: " + responseParts[1]);
//        Platform.runLater(() -> {
//            System.out.println("in handleShowAlert() and runLater() and user is " + Main.playerUserName);
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            getAlertMessage(responseParts[1],alert);
//            alert.setResizable(false);
//            alert.showAndWait();
//        });
//    }

//    private static void getAlertMessage(String responseParts,Alert alert){
//        switch (responseParts){
//            case "saveGame":
//                alert.setTitle("Save Game!!");
//                alert.setHeaderText("sorry but your opponent want to save this Game and continue in anther time");
//                break;
//            case "surrendered":
//                alert.setTitle("opponent surrendered!!");
//                alert.setHeaderText("congrats your opponent surrendered you win this game and win 5 points as bonus");
//                break;
//        }
//    }
}
