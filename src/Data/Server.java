package Data;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Server {
    private database db;
    public ServerSocket serverSocket;
    public String socketIP;
    public int socketPort;
    public ObjectOutputStream oos;
    public BufferedWriter bw;

    public ObjectInputStream ois;
    public BufferedReader br;

    public List<Client> connectedClient;
    public static final String getUsernameReq = "GET userNameList all";
    public static  final String createNewUser = "POST newClient";
    public static final String getClientData = "GET clientData all";
    public static final String getChatRoomData = "GET chatroomData all";
    public static final String sendMessage = "POST Message";
    public Server(int socketPort, database db)  {
        this.db = db;
        try {
            this.serverSocket = new ServerSocket(socketPort);

            do {
                try {
                    Socket ss = serverSocket.accept(); //asynchronous

                    OutputStream os = ss.getOutputStream();
                    this.oos = new ObjectOutputStream(os);
                    this.bw = new BufferedWriter(new OutputStreamWriter(os));

                    InputStream is = ss.getInputStream();
                    this.ois = new ObjectInputStream(is);
                    this.br = new BufferedReader(new InputStreamReader(is));

                    String receiveReq;
                    do {
                        receiveReq = br.readLine();
//                        System.out.println(receiveReq);

                        oos.reset();
                        bw.flush();

                        if(Objects.equals(receiveReq, "CLOSE CONNECTTION")) {
                            System.out.println("Client has left !");
                            break;

                        }else if(Objects.equals(receiveReq, getUsernameReq)) {
                            ArrayList<String> allUser = new ArrayList<>();
                            for(String user : this.db.getAllClients().keySet()) {
                                allUser.add(user);
                            }
                            oos.writeUnshared(allUser);

                        }else if(receiveReq.contains("GET clientData ")) {
                            String currClient = receiveReq.split("GET clientData ")[1];
                            Client clientFound = this.db.getAllClients().get(currClient);
                            oos.writeUnshared(clientFound);

                        }else if(receiveReq.contains("GET chatroomData ")) {
                            String chatroomName = receiveReq.split("GET chatroomData ")[1];
                            ChatRoom chatroomFound = this.db.getAllChatrooms().get(chatroomName);
                            oos.writeUnshared(chatroomFound);

                        }else if(receiveReq.contains("POST Message")) {
                            Message newMessage = (Message) this.ois.readObject();
                            if(newMessage != null) {
                                this.db.addNewMessage(newMessage);
                            }
                        }else if(receiveReq.contains("POST newClient")) {
                            Client newClient = (Client) this.ois.readObject();
                            if(newClient != null) {
                                this.db.addNewClient(newClient);
                            }
                        }
                    }while(true);
                    bw.close();
                    br.close();

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }while(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public database getDb() {
        return db;
    }
}
