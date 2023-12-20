import Data.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {

        database db = new database();
        HashMap<String, Client> allClients = db.getAllClients();
        HashMap<String, ChatRoom> allChatrooms = db.getAllChatrooms();

//        for (String client: allClients.keySet()) {
//            String key = client.toString();
//            String value = allClients.get(client).toString();
//            System.out.println(key + " " + value);
//        }
//
//        for (String chatroom: allChatrooms.keySet()) {
//            String key = chatroom.toString();
//            String value = allChatrooms.get(chatroom).toString();
//            System.out.println(key + " " + value);
//        }
//        ArrayList<String> allUser = new ArrayList<>();
//        for(String user : allClients.keySet()) {
//            allUser.add(user);
//        }

//        ServerSocket serverSocket = new ServerSocket(8080);
//        do {
//            try {
//                Socket ss = serverSocket.accept(); //asynchronous
//
//                OutputStream os = ss.getOutputStream();
//                ObjectOutputStream oos = new ObjectOutputStream(os);
//                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
//
//                InputStream is = ss.getInputStream();
//                ObjectInputStream ois = new ObjectInputStream(is);
//                BufferedReader br = new BufferedReader(new InputStreamReader(is));
//
//                oos.writeObject(allUser);
//
//                String request = br.readLine();
//
//            } catch (UnknownHostException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }while(true);
//        Server serverSocket = new Server(8080,db);

        NewServer newServer1 = new NewServer(db,8080);
        newServer1.start();
    }
}


