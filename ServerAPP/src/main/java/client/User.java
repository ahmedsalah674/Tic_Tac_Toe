package client;

import client.Player;

import java.net.Socket;
public class User {
    public Player playerDate;
    //    private Player otherPlayer;
    Socket clientSocket;
    public User(Socket CS) {
        clientSocket = CS;
    }
    public void setPlayer(String userName, String password) {
        playerDate = new Player(userName, password);
    }
//    public void setOtherPlayer(String otherPlayerName) {
//        otherPlayer = new Player(otherPlayerName);
//    }

}
