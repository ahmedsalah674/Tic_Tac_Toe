package handler;

import client.Client;

import java.util.Arrays;

public class RequestHandler {
    public static void handleRequest(String request,Client clientInstance) {
//        request="Client:getPlayersRequest:ahmed"
        String[] requestParts = request.split(":");
        String[] requestHandle = Arrays.copyOfRange(requestParts, 1, requestParts.length);
        switch (requestParts[0]) {
//            case "Game" -> GameHandler.handleRequest(requestHandle,clientInstance);
//            case "Client" -> ClientHandler.handleRequest(requestHandle,clientInstance);
            default -> System.out.println("Unexpected value in RequestHandler-handleRequest(): " + requestParts[0]);
        }
    }
}