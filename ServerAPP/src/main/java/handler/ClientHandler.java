package handler;
import client.Client;
import client.Player;
import client.User;
import com.example.serverapp.Main;
import server.Server;
import java.io.IOException;
import java.util.ArrayList;
public class ClientHandler {
//    private static Client usedClientForHandler;
    public static void handleRequest(String[] requestParts, Client copyClient) {
//        usedClientForHandler = copyClient;
//        System.out.println(" handleRequest() request-> " + request);
//        String[] requestParts = request.split(":");
        switch (requestParts[0]) {
            case "loginRequest" -> handleLogin(requestParts,copyClient);
            case "signUpRequest" -> handleSignUp(requestParts,copyClient);
            case "getPlayersRequest" -> handleGetPlayers(requestParts,copyClient);
            case "getFriendsRequest" -> handleGetFriends(requestParts, copyClient);
            case "gamePlayRequest" -> handleGamePlay(requestParts);
            case "gameSaveRequest" -> handleGameSave(requestParts);
            case "logoutRequest" -> handleLogout(requestParts,copyClient);
            case "inviteRequest" -> handleInvite(requestParts, copyClient);
            case "inviteResponse" -> handleInviteResponse(requestParts);
            case "closeMERequest" -> handleCloseMERequest(requestParts);
            case "updateScoreRequest" -> handleUpdateScoreRequest(requestParts);
            case "playAgainRequest" -> handlePlayAgain(requestParts);
            case "removeOtherPlayerRequest" -> handleRemoveOtherPlayer(requestParts);
            default -> System.out.println("Unexpected value for request: " + String.join(":",requestParts));
        }
    }

    private static void handleLogin(String[] requestParts, Client copyClient) {
        System.out.println("login handler function: " + requestParts[0]);
            copyClient.clientUser.setPlayer(requestParts[1], requestParts[2]);
            boolean isLogin = copyClient.clientUser.playerDate.login();
           String result = "loginResponse:" +isLogin+":"+requestParts[1];
//        }
        if(isLogin&&!Server.clientVector.isEmpty()){
            for (Client counter:Server.clientVector) {
                ClientHandler.handleRequest(new String[]{"getPlayersRequest"}, counter);
            }
        }
        if(isLogin)
        {
            System.out.println("----------------------------"+true);
            Main.changeSceneName("ServerGui.fxml");
        }
        copyClient.sendResponseMessage(result,"Client");
    }

    private static void handleSignUp(String[] requestParts,Client copyClient) {
        System.out.println("handleSignUp handler function: " + requestParts[0]);
        copyClient.clientUser= new User(copyClient.ClientSocket);
        copyClient.clientUser.setPlayer(requestParts[1], requestParts[2]);
            String result = "signUpResponse:" + copyClient.clientUser.playerDate.signUp()+":"+requestParts[1];
//            usedClientForHandler.sendResponseMessage(result);
        copyClient.sendResponseMessage(result,"Client");
    }

    private static void handleGetPlayers(String[] requestParts,Client copyClient) {
        System.out.println("handleGetPlayers handler function: " + requestParts[0]);
        String stringPlayersData = "getPlayersResponse:";
        if (copyClient != null && copyClient.clientUser != null && copyClient.clientUser.playerDate != null) {
            ArrayList<Player> players = PlayersBOT(requestParts,copyClient);
            stringPlayersData += playersToString(players);
            copyClient.sendResponseMessage(stringPlayersData,"Client");
        }
    }

    private static void handleGetFriends(String[] requestParts, Client copyClient) {
        System.out.println("handleGetFriends handler function: " + requestParts[0]);
        String stringFriendsData = "getFriendsResponse:";
        if (copyClient != null && copyClient.clientUser != null && copyClient.clientUser.playerDate != null) {
            ArrayList<Player> friends = PlayersBOT(requestParts,copyClient);
            stringFriendsData += playersToString(friends);
            copyClient.sendResponseMessage(stringFriendsData,"Client");
        }
    }
    private static String playersToString(ArrayList<Player> players) {
        StringBuilder stringPlayersData = new StringBuilder();
        for (Player playerLooper : players) {
            String playerData = playerLooper.getUserName() + "-" + playerLooper.getStatus() + "-" + playerLooper.getScore() + ":";
            stringPlayersData.append(playerData);
        }
        return stringPlayersData.toString();
    }

    private static ArrayList<Player> PlayersBOT(String[] requestParts,Client copyClient) {
        System.out.println("PlayersBOT handler function: " + requestParts[0]);
        return switch (requestParts[0]) {
            case "getFriendsRequest" -> copyClient.clientUser.playerDate.getFriends();
            case "getPlayersRequest" -> copyClient.clientUser.playerDate.getOnlinePlayers();
            default -> null;
        };
    }
    private static void handleGamePlay(String[] responseParts) {
        System.out.println("handleGamePlay handler function: " + responseParts[0]);
//        "gamePlayRequest:Main.playerUserName:Main.otherPlayerUserName:X:+move[1]_move[2]
        Client player = Server.getUserByUserName(responseParts[1]);
        Client otherPlayer = Server.getUserByUserName(responseParts[2]);
        if(player!=null && otherPlayer!=null) {
            String message="gamePlayResponse:"+responseParts[3]+":"+responseParts[4];
            otherPlayer.sendResponseMessage(message,"Client");
        }
    }
    private static void handleGameSave(String[] requestParts) {
        System.out.println("handleGameSave handler function: " + requestParts[0]);
    }

    private static void handleInvite(String[] requestParts, Client copyClient) {
        if(copyClient.clientUser!=null&&copyClient.clientUser.playerDate!=null);
            copyClient.clientUser.playerDate.setInGame();
        Client result = Server.getUserByUserName(requestParts[1]);
        if (result != null)
            result.clientUser.playerDate.setInGame();
            result.sendResponseMessage("inviteMessageRequest:" + copyClient.clientUser.playerDate.getUserName(),"Client");
        Main.changeSceneName("ServerGui.fxml");
        Server.sendMessageForAll("getPlayersRequest", "Client");
        System.out.println("handleInvite handler function: " + requestParts[0]);
    }

    private static void handleInviteResponse(String[] requestParts) {
        Client result = Server.getUserByUserName(requestParts[1]);
        if (result != null)
            result.sendResponseMessage("inviteResponse:" + requestParts[2],"Client");
        System.out.println("handleInviteResponse(): " + requestParts[0]);
    }

    private static void handleLogout(String[] requestParts, Client copyClient) {
        System.out.println("handleLogout handler function: " + requestParts[0]);
        System.out.println("handleLogout:" + copyClient.clientUser.playerDate.logout());
        if(!Server.clientVector.isEmpty()){
            System.out.println("!Server.clientVector.isEmpty");
            for (Client counter:Server.clientVector) {
                ClientHandler.handleRequest(new String[]{"getPlayersRequest"}, counter);
            }
        }
        Main.changeSceneName("ServerGui.fxml");
    }

    private static void handleCloseMERequest(String[] requestParts) {
        Client result = Server.getUserByUserName(requestParts[1]);
        if(result!=null) {
            try {
                result.printStream.close();
                result.dataInputStream.close();
                result.ClientSocket.close();
                result.stop();
                Server.clientVector.remove(result);
            } catch (IOException e) {
                System.out.println("can't close this com.example.serverapp.client");
            }
        }
    }

    private static void handleUpdateScoreRequest(String[] requestParts) {
        System.out.println("handleUpdateScoreRequest  request->"+requestParts[0]);
        Client result = Server.getUserByUserName(requestParts[1]);
        if(result!=null){
                result.clientUser.playerDate.addScore(Integer.parseInt(requestParts[2]));
        }
    }

    private static void handlePlayAgain(String[] requestParts) {
        System.out.println("handlePlayAgain request->"+requestParts[0]);
        Client result = Server.getUserByUserName(requestParts[1]);
        if(result!=null){
            result.sendResponseMessage("playAgainResponse","Client") ;
        }
    }

    private static void handleRemoveOtherPlayer(String[] requestParts) {
        System.out.println("handleRemoveOtherPlayer request->"+requestParts[0]);
        Client result = Server.getUserByUserName(requestParts[1]);
        if(result!=null){
//            "removeOtherPlayer:"+otherPlayerUserName
            result.sendResponseMessage("removeOtherPlayerResponse","Client") ;
        }
    }

}

// gui will press button then on action method will send request fot server and wait for respond
// server will receive request send it here then call database and get result
// convert it to string and get it back as response message
// gui will receive response message send it to handler convert it to object then show data on gui