package Data;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class ClientSocket extends Thread{
    private database db;
    private int port;
    private Socket socket;
    private BufferedReader receiver;
    private ObjectInputStream objectReceiver;
    private DataInputStream dataReceiver;
    private BufferedWriter sender;
    private ObjectOutputStream objectSender;
    private DataOutputStream dataSender;
    public static String createNewClient = "POST newClient";
    public static final String getUsernameReq = "GET userNameList all";
    public static final String getClientData = "GET clientData all";
    public static final String getChatRoomData = "GET chatroomData all";
    public static final String sendMessage = "POST Message";
    public static final String createNewChatroom = "POST newChatroom";
    public static final String sendNewFile = "POST newFile";
    public static final String getFile = "GET fileData";
    public static final String deleteMessage = "DELETE Message";

    public ClientSocket(Socket socket, database db) {
        this.socket = socket;
        this.db = db;
        this.port = socket.getPort();

        try {
            OutputStream os = socket.getOutputStream();
            this.objectSender = new ObjectOutputStream(os);
            this.sender = new BufferedWriter(new OutputStreamWriter(os));
            this.dataSender = new DataOutputStream(os);

            InputStream is = socket.getInputStream();
            this.objectReceiver = new ObjectInputStream(is);
            this.receiver = new BufferedReader(new InputStreamReader(is));
            this.dataReceiver = new DataInputStream(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
        try {
            String receiveReq;
            do {
                receiveReq = this.receiver.readLine();
                System.out.println(receiveReq);

                this.objectSender.reset();
//                this.objectReceiver.reset();
                this.sender.flush();

                if(Objects.equals(receiveReq, "CLOSE CONNECTTION")) {
                    System.out.println("Client has left !");
                    break;

                }else if(Objects.equals(receiveReq, getUsernameReq)) {
                    ArrayList<String> allUser = new ArrayList<>();
                    for(String user : this.db.getAllClients().keySet()) {
                        allUser.add(user);
                    }
                    this.objectSender.writeUnshared(allUser);

                }else if(receiveReq.contains(createNewClient)) {
                    Client newClient = (Client) this.objectReceiver.readObject();
                    if(newClient != null) {
                        this.db.addNewClient(newClient);
                    }
                }
                else if(receiveReq.contains("GET clientData ")) {
                    String currClient = receiveReq.split("GET clientData ")[1];
                    Client clientFound = this.db.getAllClients().get(currClient);
                    this.objectSender.writeUnshared(clientFound);

                }else if(receiveReq.contains("GET chatroomData ")) {
                    String chatroomName = receiveReq.split("GET chatroomData ")[1];
                    ChatRoom chatroomFound = this.db.getAllChatrooms().get(chatroomName);
                    this.objectSender.writeUnshared(chatroomFound);

                }else if(receiveReq.contains(sendMessage)) {
                    Message newMessage = (Message) this.objectReceiver.readObject();
                    if(newMessage != null) {
                        this.db.addNewMessage(newMessage);
                    }
                }else if(receiveReq.contains(createNewChatroom)) {
                    String newChatroomName = receiveReq.split(createNewChatroom + " ")[1];
                    ArrayList<String> addingClients = (ArrayList<String>) this.objectReceiver.readObject();
                    if(newChatroomName != null && addingClients != null) {
                         boolean canCreate = this.db.createNewChatroom(newChatroomName, addingClients);

                         String reply = canCreate ? "Create " + newChatroomName : "Fail " + newChatroomName;
                         this.sender.write(reply);
                         this.sender.newLine();
                         this.sender.flush();
                    }
                }else if(receiveReq.contains(sendNewFile)) {
                    String fileData = receiveReq.split(sendNewFile + " ")[1];
                    String fileName = fileData.split(" ")[0];
                    long fileLength = Long.parseLong(fileData.split(" ")[1]);

                    this.db.addnewFile(fileName,fileLength,this.dataReceiver);
                }else if(receiveReq.contains(getFile)) {
                    String fileName = receiveReq.split(getFile + " ")[1];
                    long fileLength = database.getFileLengthByte(database.pathFile + "/" + fileName);

                    String reply = fileName + " " + fileLength;
                    this.sender.write(reply);
                    this.sender.newLine();
                    this.sender.flush();

                    FileInputStream fileInputStream = new FileInputStream(database.pathFile + "/" + fileName);

                    int bytes = 0;
                    byte[] buffer = new byte[4 * 1024];
                    while ((bytes = fileInputStream.read(buffer)) != -1) {

                        this.dataSender.write(buffer, 0, bytes);
                        this.dataSender.flush();
                    }
                    fileInputStream.close();
                }else if(receiveReq.contains(deleteMessage)) {
                    String delInfo = receiveReq.split(deleteMessage + " ")[1];
                    String chatroomName = delInfo.split(" ")[0];
                    int messageIndex = Integer.parseInt(delInfo.split(" ")[1]);

                    this.db.deleteMessage(chatroomName,messageIndex);
                }
            }while(true);
        } catch (IOException e) {
            System.out.println("A client is leaving");
            this.db.savingData();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public int getPort() {
        return port;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getReceiver() {
        return receiver;
    }

    public BufferedWriter getSender() {
        return sender;
    }

    public ObjectInputStream getObjectReceiver() {
        return objectReceiver;
    }

    public ObjectOutputStream getObjectSender() {
        return objectSender;
    }
}
