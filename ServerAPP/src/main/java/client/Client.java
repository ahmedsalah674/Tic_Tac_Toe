package client;
import com.example.serverapp.Main;
import handler.ClientHandler;
import handler.RequestHandler;
import server.Server;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Client extends Thread {
    public Socket ClientSocket;
    public DataInputStream dataInputStream;
    public PrintStream printStream;
    public User clientUser;

    public Client(User newUser) {
        this.ClientSocket = newUser.clientSocket;
        try {
            dataInputStream = new DataInputStream(ClientSocket.getInputStream());
            printStream = new PrintStream(ClientSocket.getOutputStream());
            clientUser = newUser;
            start();
            System.out.println("i'm here in Client constructor");
        } catch (IOException e) {
            System.out.println("inside Client constructor: " + e);
        }
    }
    // write function on clint side
    public boolean sendResponseMessage(String responseMessage,String responseType){
        if(this.printStream!=null) {
//            System.out.println(responseMessage+"++++++"+    this.getId()+"------"+this.clientUser.playerDate.getUserName());
            this.printStream.println(responseType + ":" + responseMessage);
            return true;
        }
        else
            System.out.println("inside sendResponseMessage() printStream is null");
        return false;
    }
    public void run() {
        while (true) {
            try {
                System.out.println("i'm here in com.example.serverapp.client run()");
                String request = dataInputStream.readLine();
                if (request != null)
                        RequestHandler.handleRequest(request, this);

            } catch (IOException ex) {
                this.stop();
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                printStream.close();
                Server.clientVector.remove(this);
            }
        }
    }

}

