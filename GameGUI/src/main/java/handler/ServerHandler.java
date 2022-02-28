package handler;

import com.example.gamegui.Main;

public class ServerHandler {
    public static void handleRequest(String[] responseParts) {
        System.out.println(" handleRequest() response-> " + responseParts[0]);
//        String[] responseParts = response.split(":");
        switch (responseParts[0]) {
            case "locked" -> handleLocked();
            case "unlocked" -> handleUnlocked();
            case "serverClosed"->handleServerClosed();
            default -> System.out.println("Unexpected value for request: " + String.join(":",responseParts));
        }
    }

    private static void handleServerClosed() {
        Main.changeSceneName("failed.fxml");
        Main.MainThread.stop();
    }
    
    private static void handleLocked(){
        Main.changeSceneName("failed.fxml");
    }
    private static void handleUnlocked(){
        Main.changeSceneName("LoginGui.fxml");
    }
}
