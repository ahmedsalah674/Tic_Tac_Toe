package com.example.serverapp;

import client.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.Server;

import java.io.IOException;

public class Main extends Application {
    private static Server ourServer;
    public static void main(String[] args) {
        ourServer = new Server();
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ServerGui.fxml"));
        Scene scene = new Scene(root, 601, 405);
        primaryStage.setTitle("TicTacToe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop(){
        if (!Server.clientVector.isEmpty()){
            for (Client clientCounter: Server.clientVector) {
                clientCounter.clientUser.playerDate.logout();
//                clientCounter.sendResponseMessage(); //tell him server is closed
                try {
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