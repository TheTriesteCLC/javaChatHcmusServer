//package Data;
//
//import java.io.*;
//import java.net.Socket;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.Objects;
//
//public class ClientThread extends Thread{
//    private database db;
//    ClientSocket clientSocket;
//    NewServer server;
//    public static final String getUsernameReq = "GET userNameList all";
//    public static final String getClientData = "GET clientData all";
//    public static final String getChatRoomData = "GET chatroomData all";
//    public static final String sendMessage = "POST Message";
//    public ClientThread(NewServer server, Socket clientSocket, database db) {
//        this.server = server;
//        this.db = db;
//        try {
//            this.clientSocket = new ClientSocket();
//            this.clientSocket.setSocket(clientSocket);
//
//            OutputStream os = clientSocket.getOutputStream();
//            this.clientSocket.setObjectSender(new ObjectOutputStream(os));
//            this.clientSocket.setSender(new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8)));
//
//            InputStream is = clientSocket.getInputStream();
//            this.clientSocket.setObjectReceiver(new ObjectInputStream(is));
//            this.clientSocket.setReceiver(new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)));
//
//            this.clientSocket.setPort(clientSocket.getPort());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    @Override
//    public void run() {
//        try {
//            String receiveReq;
//            do {
//                receiveReq = this.clientSocket.getReceiver().readLine();
//                if (receiveReq == null || Objects.equals(receiveReq, "CLOSE CONNECTTION"))
//                    throw new IOException();
//
//
//                System.out.println(receiveReq);
//
//                this.clientSocket.getObjectSender().reset();
//                this.clientSocket.getSender().flush();
//
//                if (Objects.equals(receiveReq, getUsernameReq)) {
//                    ArrayList<String> allUser = new ArrayList<>();
//                    for (String user : this.server.getDb().getAllClients().keySet()) {
//                        allUser.add(user);
//                    }
//                    this.clientSocket.getObjectSender().writeUnshared(allUser);
//
//                } else if (receiveReq.contains("GET clientData ")) {
//                    String currClient = receiveReq.split("GET clientData ")[1];
//                    Client clientFound = this.server.getDb().getAllClients().get(currClient);
//                    this.clientSocket.getObjectSender().writeUnshared(clientFound);
//
//                } else if (receiveReq.contains("GET chatroomData ")) {
//                    String chatroomName = receiveReq.split("GET chatroomData ")[1];
//                    ChatRoom chatroomFound = this.server.getDb().getAllChatrooms().get(chatroomName);
//                    this.clientSocket.getObjectSender().writeUnshared(chatroomFound);
//
//                } else if (receiveReq.contains("POST Message")) {
//                    Message newMessage = (Message) this.clientSocket.getObjectReceiver().readObject();
//                    if (newMessage != null) {
//                        String chatroomType = this.server.getDb().getAllChatrooms().get(newMessage.getChatroom()).getType();
//                        ArrayList<Client> chatroomClients = this.server.getDb().getAllChatrooms().get(newMessage.getChatroom()).getClients();
//                        ArrayList<Message> chatroomMsg = this.server.getDb().getAllChatrooms().get(newMessage.getChatroom()).getMessages();
//                        chatroomMsg.add(newMessage);
//                        ChatRoom newChatRoom = new ChatRoom(newMessage.getChatroom(), chatroomType, chatroomMsg, chatroomClients);
//
//                        this.server.getDb().getAllChatrooms().remove(newChatRoom.getRoomName());
//                        this.server.getDb().getAllChatrooms().put(newChatRoom.getRoomName(), newChatRoom);
//                    }
//                }
//            }while(true);
//        } catch (IOException e) {
////            if(this.server.getServerSocket().isClosed() || this.clientSocket.getUserName() == null) {
////                this.server.
////            }
//            throw new RuntimeException(e);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
