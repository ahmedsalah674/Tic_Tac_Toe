package server;

import client.Client;
import client.User;
import com.example.serverapp.Main;
import handler.RequestHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server extends Thread {
    public static ServerSocket serverSocket;
//    public static boolean running;
    public static Vector<Client> clientVector = new Vector<>();
    public Server() {
        try {
            serverSocket = new ServerSocket(5005);
            start();
            Main.running=true;
        } catch (Exception e) {
            System.out.println("inside private Server constructor" + e);
        }
    }

    public static Client getUserByUserName(String userName){
        Client wantedUser = null;
        for (Client thisClint:clientVector)
            if(thisClint.clientUser!=null&&thisClint.clientUser.playerDate!=null&&thisClint.clientUser.playerDate.getUserName().equals(userName))
                wantedUser = thisClint;
        return wantedUser;
    }
    @Override
    public void run() {
            while (!serverSocket.isClosed()) {
                try {
//                System.out.println("i'm still here");
//                if(){
                    Socket newClientSocket = serverSocket.accept();
                    User newUser = new User(newClientSocket);
                    Client newClient = new Client(newUser);
                    clientVector.add(newClient);
//                }
            }catch (Exception e) {
                    System.out.println("inside run() Server function " + e);
                }
            }
    }
    public static void sendMessageForAll(String message,String messageType){
        for (Client clientCounter:clientVector) {
                RequestHandler.handleRequest(messageType+":"+message,clientCounter);
        }
    }
}
