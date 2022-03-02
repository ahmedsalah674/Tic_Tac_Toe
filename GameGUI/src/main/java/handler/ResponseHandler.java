package handler;

import java.util.Arrays;

public class ResponseHandler {
    public static void handleResponse(String response) {
//        System.out.println(" handleResponse() response-> " + response);
        String[] responseParts = response.split(":");
//        System.out.println(String.join( "_______",responseParts));
        String[] responseArray = Arrays.copyOfRange(responseParts, 1, responseParts.length);
        switch (responseParts[0]) {
            case "Client" -> ClientHandler.handleRequest(responseArray);
            case "Server" -> ServerHandler.handleRequest(responseArray);
            case "Game"   -> GameHandler.handleResponse(responseArray);
            default -> System.out.println("Unexpected value for request: " + response);
        }
    }
}
