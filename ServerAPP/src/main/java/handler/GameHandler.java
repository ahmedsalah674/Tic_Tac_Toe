package handler;

import client.Client;
import game.Game;
import server.Server;

public class GameHandler {
    public static void handleRequest(String[] requestParts, Client clientInstance) {
//        System.out.println(" GameHandler Class-handleRequest() request-> " + requestParts[0]);
        switch (requestParts[0]) {
            case "createGameRequest"   -> handleCreateGameRequest(requestParts);
            case "saveThisGameRequest" -> handleSaveThisGame(requestParts,clientInstance);
            case "canselSaveRequest"   -> handleCanselSaveRequest(clientInstance);
            case "getGameRequest"      -> handleGetGameRequest(requestParts,clientInstance);
            case "removeGameRequest" -> handleRemoveGame(requestParts);
            case "reloadGameRequest" -> handleReloadGameRequest(requestParts);
            default -> System.out.println("Unexpected value for request: " + requestParts[0]);
        }
    }

    private static void handleRemoveGame(String[] requestParts) {
//        System.out.println(Game.removeGame(Integer.parseInt(requestParts[1])));
        Game.removeGame(Integer.parseInt(requestParts[1]));
    }
    private static void handleReloadGameRequest(String[] requestParts) {
        Client result = Server.getUserByUserName(requestParts[2]);
        Game exitGame=new Game(requestParts[1],requestParts[2]);
        exitGame=exitGame.getGameByPlayersName();
        if (!exitGame.isEmpty() && result!=null)
            result.sendResponseMessage("loadGameResponse:"+exitGame, "Game");
        
    }

    private static void handleGetGameRequest(String[] requestParts, Client clientInstance) {
//        getGameRequest:ahmed:salah
        Game exitGame=new Game(requestParts[1],requestParts[2]);
        exitGame=exitGame.getGameByPlayersName();
        if(!exitGame.isEmpty())
            clientInstance.sendResponseMessage("getGameResponse:"+exitGame,"Game");
        else
            clientInstance.sendResponseMessage("getGameResponse:null","Game");
    }

    private static void handleCanselSaveRequest(Client clientInstance) {
        clientInstance.sendResponseMessage("canselSaveResponse:", "Game");
    }

    public static void handleCreateGameRequest(String[] requestParts){
        //createGameRequest:otherPlayer
        Client result = Server.getUserByUserName(requestParts[1]);
        if(result!=null)
            result.sendResponseMessage("createGameRequest:", "Game");
    }
    public static void handleSaveThisGame(String[] requestParts,Client clientInstance ){
        //saveThisGameRequest:ahmed:ahmedS:ahmed:[1-0-0-O, 2-0-1-X, 3-1-0-O]
        Client result;
        if(clientInstance.clientUser.playerDate.getUserName().equals(requestParts[1]))
            result=Server.getUserByUserName(requestParts[2]);
        else
            result=Server.getUserByUserName(requestParts[1]);
        Game thisGame=new Game(requestParts[1],requestParts[2],requestParts[3],requestParts[4]);
        boolean gameSaved = thisGame.createGame();
        clientInstance.sendResponseMessage("saveThisGameResponse:"+gameSaved,"Game" );
        if(result!=null)
            clientInstance.sendResponseMessage("saveThisGameResponse:"+gameSaved,"Game" );
    }
}
