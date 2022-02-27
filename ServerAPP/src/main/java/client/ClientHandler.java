package client;
import server.Server;
import java.io.IOException;
import java.util.ArrayList;
public class ClientHandler {
    private static Client usedClientForHandler;
    public static void handleRequest(String request, Client copyClient) {
        usedClientForHandler = copyClient;
        System.out.println(" handleRequest() request-> " + request);
        String[] requestParts = request.split(":");
        switch (requestParts[0]) {
            case "loginRequest" -> handleLogin(requestParts);
            case "signUpRequest" -> handleSignUp(requestParts);
            case "getPlayersRequest" -> handleGetPlayers(requestParts);
            case "getFriendsRequest" -> handleGetFriends(requestParts);
            case "gamePlayRequest" -> handleGamePlay(requestParts);
            case "gameSaveRequest" -> handleGameSave(requestParts);
            case "logoutRequest" -> handleLogout(requestParts);
            case "inviteRequest" -> handleInvite(requestParts);
            case "inviteResponse" -> handleInviteResponse(requestParts);
            case "closeMERequest" -> handleCloseMERequest(requestParts);
            case "updateScoreRequest" -> handleUpdateScoreRequest(requestParts);
            case "playAgainRequest" -> handlePlayAgain(requestParts);
            case "removeOtherPlayerRequest" -> handleRemoveOtherPlayer(requestParts);
            default -> System.out.println("Unexpected value for request: " + request);
        }
    }

    private static void handleLogin(String[] requestParts) {
        System.out.println("login handler function: " + requestParts[0]);
//        String result = "loginResponse:false";
//        if (requestParts.length >= 3) {
            usedClientForHandler.clientUser.setPlayer(requestParts[1], requestParts[2]);
            boolean isLogin = usedClientForHandler.clientUser.playerDate.login();
           String result = "loginResponse:" +isLogin+":"+requestParts[1];
//        }
        if(isLogin&&!Server.clientVector.isEmpty()){
            for (Client counter:Server.clientVector) {
                ClientHandler.handleRequest("getPlayersRequest", counter);
            }
        }
        usedClientForHandler.sendResponseMessage(result);
    }

    private static void handleSignUp(String[] requestParts) {
        System.out.println("handleSignUp handler function: " + requestParts[0]);
            User newUser = new User(usedClientForHandler.ClientSocket);
            usedClientForHandler.clientUser=newUser;
            usedClientForHandler.clientUser.setPlayer(requestParts[1], requestParts[2]);
            String result = "signUpResponse:" + usedClientForHandler.clientUser.playerDate.signUp()+":"+requestParts[1];
            usedClientForHandler.sendResponseMessage(result);
    }

    private static void handleGetPlayers(String[] requestParts) {
        System.out.println("handleGetPlayers handler function: " + requestParts[0]);
        String stringPlayersData = "getPlayersResponse:";
        if (usedClientForHandler != null && usedClientForHandler.clientUser != null && usedClientForHandler.clientUser.playerDate != null) {
            ArrayList<Player> players = PlayersBOT(requestParts);
            stringPlayersData += playersToString(players);
            usedClientForHandler.sendResponseMessage(stringPlayersData);
        }
    }

    private static void handleGetFriends(String[] requestParts) {
        System.out.println("handleGetFriends handler function: " + requestParts[0]);
        String stringFriendsData = "getFriendsResponse:";
        if (usedClientForHandler != null && usedClientForHandler.clientUser != null && usedClientForHandler.clientUser.playerDate != null) {
            ArrayList<Player> friends = PlayersBOT(requestParts);
            stringFriendsData += playersToString(friends);
            usedClientForHandler.sendResponseMessage(stringFriendsData);
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

    private static ArrayList<Player> PlayersBOT(String[] requestParts) {
        System.out.println("PlayersBOT handler function: " + requestParts[0]);
        return switch (requestParts[0]) {
            case "getFriendsRequest" -> usedClientForHandler.clientUser.playerDate.getFriends();
            case "getPlayersRequest" -> usedClientForHandler.clientUser.playerDate.getOnlinePlayers();
            default -> null;
        };
    }
    private static void handleGamePlay(String[] responseParts) {
        System.out.println("handleGamePlay handler function: " + responseParts[0]);
//        "gamePlayRequest:Main.playerUserName:Main.otherPlayerUserName:X:+move[1]_move[2]
        Client player = Server.getUserByUserName(responseParts[1]);
        Client otherPlayer = Server.getUserByUserName(responseParts[2]);
        if(player!=null && otherPlayer!=null) {
            otherPlayer.sendResponseMessage("gamePlayResponse:"+responseParts[3]+":"+responseParts[4]);
        }
    }
    private static void handleGameSave(String[] requestParts) {
        System.out.println("handleGameSave handler function: " + requestParts[0]);
    }

    private static void handleInvite(String[] requestParts) {
        Client result = Server.getUserByUserName(requestParts[1]);
        if (result != null)
            result.sendResponseMessage("inviteMessageRequest:" + usedClientForHandler.clientUser.playerDate.getUserName());
        System.out.println("handleInvite handler function: " + requestParts[0]);
    }

    private static void handleInviteResponse(String[] requestParts) {
        Client result = Server.getUserByUserName(requestParts[1]);
        if (result != null)
            result.sendResponseMessage("inviteResponse:" + requestParts[2]);
        System.out.println("handleInviteResponse(): " + requestParts[0]);
    }

    private static void handleLogout(String[] requestParts) {
        System.out.println("handleLogout handler function: " + requestParts[0]);
        System.out.println("handleLogout:" + usedClientForHandler.clientUser.playerDate.logout());
        if(!Server.clientVector.isEmpty()){
            System.out.println("!Server.clientVector.isEmpty");
            for (Client counter:Server.clientVector) {
                ClientHandler.handleRequest("getPlayersRequest", counter);
            }
        }
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
            result.sendResponseMessage("playAgainResponse") ;
        }
    }

    private static void handleRemoveOtherPlayer(String[] requestParts) {
        System.out.println("handleRemoveOtherPlayer request->"+requestParts[0]);
        Client result = Server.getUserByUserName(requestParts[1]);
        if(result!=null){
//            "removeOtherPlayer:"+otherPlayerUserName
            result.sendResponseMessage("removeOtherPlayerResponse") ;
        }
    }

}

// gui will press button then on action method will send request fot server and wait for respond
// server will receive request send it here then call database and get result
// convert it to string and get it back as response message
// gui will receive response message send it to handler convert it to object then show data on gui