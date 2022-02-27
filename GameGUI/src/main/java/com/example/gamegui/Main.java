package com.example.gamegui;

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

    public static void changeSceneName(String sceneName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource(sceneName)));
                    Main.s.setScene(scene);
                } catch (IOException e) {
                    System.out.println("this scene not found");
                }
            }
        });
    }

    public static void sendMessage(String message) {
        if (clientPrintStream != null)
            clientPrintStream.println(message);
        else
            System.out.println("inside sendMessage function clientPrintStream is null");
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginGui.fxml"));
            Scene scene = new Scene(root);
            s = primaryStage;
            s.setTitle("TicTacToe");
            s.setScene(scene);
            s.show();
        } catch (IOException e) {
            System.out.println("fxml not found");
        }

    }

    @Override
    public void stop() {
        if(playerUserName!=null) {
            if(otherPlayerUserName!=null&&!OnlineGameController.isGameOver()) {
                System.out.println("in Stop() in main and user is " + playerUserName);
                Main.sendMessage("removeOtherPlayerRequest:"+otherPlayerUserName);
                Main.sendMessage("updateScoreRequest:"+Main.otherPlayerUserName+":"+ 15);
            }
            sendMessage("logoutRequest");
        }
        sendMessage("closeMERequest:"+playerUserName);
        try{
            if(clientSocket!=null) {
                clientSocket.close();
                MainThread.stop();
            }
        }catch (IOException e){
            System.out.println("already closed");
        }
    }

    static class mainThread extends Thread {
        public void run() {
            try {
                while (true) {
                    System.out.println("here in main thread");
                    String line = clientDataInputStream.readLine();
                    if (line != null) {
                        System.out.println("line is -> " + line);
                        ClientHandler.handleRequest(line);
                    }
                }
            } catch (IOException ex) {
                System.out.println("server not run ");
            }
        }
    }

    public static void main(String[] args) {
        try {
            clientSocket = new Socket("127.0.0.1", 5005);
            clientDataInputStream = new DataInputStream(clientSocket.getInputStream());
            clientPrintStream = new PrintStream(clientSocket.getOutputStream());
            MainThread = new mainThread();
            MainThread.start();
        } catch (IOException e) {
            System.out.println("clientSocket");
            System.out.println("server is off");
//            Main.changeSceneName("failed.fxml");
        }

        launch(args);

    }
}