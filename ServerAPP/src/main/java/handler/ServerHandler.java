package handler;

import client.Client;
import com.example.serverapp.Main;
import server.Server;

import java.io.IOException;

public class ServerHandler {
    public static void handleRequest(String[] requestParts, Client copyClient) {
        switch (requestParts[0]) {
            case "locked" -> handleLocked(copyClient);
            case "closeMERequest" -> handleCloseMERequest(copyClient);
            case "connectAgain" ->handleConnectAgain(copyClient);
            case "lockedAll" ->handleLockedAll();
            case "unlockedAll" ->handleUnlockedAll();
            case "checkConnection" ->handleCheckConnection(copyClient);

            default -> System.out.println("Unexpected value for request: " + String.join(":", requestParts));
        }
    }


    public static void handleLocked(Client copyClient) {
        if (copyClient.clientUser != null && copyClient.clientUser.playerDate != null)
            copyClient.clientUser.playerDate.logout();
        copyClient.sendResponseMessage("locked", "Server");
    }

    private static void handleCloseMERequest(Client copyClient) {
        try {
            copyClient.printStream.close();
            copyClient.dataInputStream.close();
            copyClient.ClientSocket.close();
            copyClient.stop();
            Server.clientVector.remove(copyClient);
        } catch (IOException e) {
            System.out.println("can't close this client");
        }
    }

    private static void handleConnectAgain(Client copyClient) {
        if(Main.running)
            copyClient.sendResponseMessage("unlocked", "Server");
        else
            copyClient.sendResponseMessage("locked", "Server");
    }

    private static void handleLockedAll() {
//        System.err.println(Server.clientVector.isEmpty());
        for (Client clientCounter:Server.clientVector) {
            handleLocked(clientCounter);
//            System.out.println(ClientCounter.clientUser.playerDate.getUserName());
        }
    }
    private static void handleUnlockedAll() {
//        System.err.println(Server.clientVector.isEmpty());
        for (Client clientCounter:Server.clientVector) {
            clientCounter.sendResponseMessage("unlocked", "Server");
        }
    }
    private static void handleCheckConnection(Client copyClient) {
        if(Main.running)
            copyClient.sendResponseMessage("unlocked", "Server");
        else
            copyClient.sendResponseMessage("locked", "Server");
    }

}
