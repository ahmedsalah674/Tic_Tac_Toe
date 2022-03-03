package handler;

import com.example.gamegui.Main;
import com.example.gamegui.OnlineGameController;

public class ChatHandler {
    public static void handleResponse(String[] responseParts) {
//        System.out.println(" handleRequest() response-> " + responseParts[0]);
        switch (responseParts[0]) {
//            case "sendMessageRequest" -> handleSendMessage(responseParts);
            case "receiveMessage" -> handleReceiveMessage(responseParts);
            default -> System.out.println("Unexpected value for request: " + String.join(":",responseParts));
        }
    }

    private static void handleReceiveMessage(String[] responseParts) {
        OnlineGameController.addToChatBox(Main.otherPlayerUserName +": "+responseParts[1]+"\n");
    }
}
