package com.example.gamegui;
import handler.ResponseHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Main extends Application {
    public static Stage s;
    public static Socket clientSocket;
    public static DataInputStream clientDataInputStream;
    public static PrintStream clientPrintStream;
    public static mainThread MainThread;
    public static String playerUserName;
    public static String otherPlayerUserName;
    public static boolean haveInvite;
    public static void changeSceneName(String sceneName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
//                    System.err.println(sceneName);
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource(sceneName)));
                    Main.s.setScene(scene);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
    public static boolean sendMessage(String message,String responseType) {
        if (clientPrintStream != null) {
            clientPrintStream.println(responseType + ":" + message);
            return true;
        }
        else
            System.out.println("inside Main-sendMessage() clientPrintStream is null");
        return false;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root;
            if(connectToServer())
                root = FXMLLoader.load(getClass().getResource("LoginGui.fxml"));
            else
                root = FXMLLoader.load(getClass().getResource("failed.fxml"));
            Scene scene = new Scene(root);
            s = primaryStage;
            s.setScene(scene);
            s.setResizable(false);
            s.setTitle("TicTacToe");
            s.show();

        } catch (IOException e) {
            System.out.println("fxml not found");
        }

    }

    public static void leaveGame(){
        if(Main.otherPlayerUserName!=null)
        {
            if(!OnlineGameController.isGameOver()){
                Main.sendMessage("updateScoreRequest:"+Main.otherPlayerUserName+":"+ 15+":true","Client");
            }
            Main.sendMessage("inviteResponse:" + Main.otherPlayerUserName + ":false","Client");
            Main.sendMessage("leaveGameRequest:"+Main.otherPlayerUserName,"Client");
            Main.changeSceneName("ChooseGameGui.fxml");
            Main.otherPlayerUserName=null;
            OnlineGameController.resetGame();
        }
    }

    @Override
    public void stop() {
        if(playerUserName!=null) {
            leaveGame();
            sendMessage("logoutRequest","Client");
        }
        sendMessage("closeMERequest:"+playerUserName,"Server");
        try{
            if(clientSocket!=null) {
                clientSocket.close();
                try {

                    MainThread.stop();
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        }catch (IOException e){
            System.out.println("already closed");
        }
    }

    public static class mainThread extends Thread {
        public void run() {
            try {
                while (true) {
                    System.out.println("here in main thread");
                    String line = clientDataInputStream.readLine();
                    if (line != null) {
                        System.out.println("line is -> " + line);
                        ResponseHandler.handleResponse(line);
                        //                        ClientHandler.handleRequest(line);
                    }
                }
            } catch (IOException ex) {
                System.out.println("server not run ");
            }
        }
    }


    public static void main(String[] args) {
//        connectToServer();
        launch(args);
    }
    public static boolean connectToServer(){
        try {
            clientSocket = new Socket("127.0.0.1", 5005);
            clientDataInputStream = new DataInputStream(clientSocket.getInputStream());
            clientPrintStream = new PrintStream(clientSocket.getOutputStream());
            MainThread = new mainThread();
            MainThread.start();
            sendMessage("checkConnection", "Server");
            return true;
        } catch (IOException e) {
            System.out.println("clientSocket");
            System.out.println("server is off");
            return false;
        }
    }
}