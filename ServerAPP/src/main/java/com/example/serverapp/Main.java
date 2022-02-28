package com.example.serverapp;

import client.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.Server;

import java.io.IOException;

public class Main extends Application {
    public static boolean running;
    private static Server ourServer;
    public static Stage serverScene;
    public static void main(String[] args) {
        ourServer = new Server();
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.running=false;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ServerGui.fxml"));
            Scene scene = new Scene(root, 601, 405);
            serverScene=primaryStage;
            serverScene.setTitle("TicTacToe Server");
            serverScene.setScene(scene);
            serverScene.setResizable(false);
            serverScene.show();
        } catch (IOException e) {
            System.out.println("fxml not found");
        }
    }
    public static void changeSceneName(String sceneName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    System.err.println(sceneName);
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource(sceneName)),601, 405);
                    serverScene.setScene(scene);
                } catch (IOException e) {
                    System.out.println("this scene not found");
                }
            }
        });
    }
    @Override
    public void stop(){
        if (!Server.clientVector.isEmpty()){
            for (Client clientCounter: Server.clientVector) {
                if(clientCounter.clientUser!=null&& clientCounter.clientUser.playerDate!=null)
                    clientCounter.clientUser.playerDate.logout();
                try {
                    clientCounter.sendResponseMessage("serverClosed","Server");
                    clientCounter.printStream.close();
                    clientCounter.dataInputStream.close();
                    clientCounter.ClientSocket.close();
                }catch (IOException e){
                    System.out.println("inside stop() in server "+e);
                }
            }
        }
        try {
            Server.serverSocket.close();
            ourServer.stop();
        }catch (IOException e){
            System.out.println("server not opened yet");
        }

    }
}