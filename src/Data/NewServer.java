package Data;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewServer extends Thread {
    private database db;
    private ServerSocket serverSocket;
    private int socketPort;
    public NewServer(database db, int socketPort){
        this.socketPort = socketPort;
        this.db = db;
        try {
            this.serverSocket = new ServerSocket(socketPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public database getDb() {
        return this.db;
    }
    public ServerSocket getServerSocket() {
        return  this.serverSocket;
    }

    @Override
    public void run() {
        try {
            do{
                try {
                    Socket s = this.serverSocket.accept();

                    ClientSocket newClientSocket = new ClientSocket(s, db);
                    System.out.println("New client entered server.");

                    String receiveReq;
                    newClientSocket.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }while(true);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
