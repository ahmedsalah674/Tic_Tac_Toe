package handler;

import client.Client;
import server.Server;

public class ChatHandler  {
    public static void handleRequest(String[] requestParts, Client clientInstance) {
//        System.out.println(" GameHandler Class-handleRequest() request-> " + requestParts[0]);
        switch (requestParts[0]) {
            case "sendMessageRequest"->handleSendMessage(requestParts);
            default -> System.out.println("Unexpected value for request: " + requestParts[0]);
        }
    }

    private static void handleSendMessage(String[] requestParts) {
        //sendMessageRequest:ahmed:message
        Client result = Server.getUserByUserName(requestParts[1]);
        if(result!=null)
            result.sendResponseMessage("receiveMessage:"+requestParts[2],"Chat");
    }
}
