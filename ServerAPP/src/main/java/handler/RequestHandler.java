package handler;

import client.Client;
import com.example.serverapp.Main;

import java.util.Arrays;

public class RequestHandler {
    public static void handleRequest(String request,Client clientInstance) {
//        request="Client:getPlayersRequest:ahmed"
        String[] requestParts = request.split(":");
        String[] requestHandle = Arrays.copyOfRange(requestParts, 1, requestParts.length);
        if(Main.running || requestParts[0].equals("Server") ) {
            switch (requestParts[0]) {
                case "Server"->ServerHandler.handleRequest(requestHandle, clientInstance);
                case "Client"->ClientHandler.handleRequest(requestHandle, clientInstance);
                case "Game" -> GameHandler.handleRequest(requestHandle,clientInstance);
                case "Chat" -> ChatHandler.handleRequest(requestHandle,clientInstance);
                default -> System.out.println("Unexpected value in RequestHandler-handleRequest(): " + requestParts[0]);
            }
        }
        else
            RequestHandler.handleRequest("Server:locked",clientInstance);


    }

}