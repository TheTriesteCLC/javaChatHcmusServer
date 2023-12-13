import Data.ChatRoom;
import Data.Client;
import Data.database;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        database db = new database();
        HashMap<String, Client> allClients = db.getAllClients();
        HashMap<String, ChatRoom> allChatrooms = db.getAllChatrooms();

        for (String client: allClients.keySet()) {
            String key = client.toString();
            String value = allClients.get(client).toString();
            System.out.println(key + " " + value);
        }

        for (String chatroom: allChatrooms.keySet()) {
            String key = chatroom.toString();
            String value = allChatrooms.get(chatroom).toString();
            System.out.println(key + " " + value);
        }
    }
}