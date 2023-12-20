package Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Client implements Serializable {
    private String username;
    private ArrayList<String> chatRooms;
    public Client(String username) {
        this.username = username;
        this.chatRooms = new ArrayList<>();
    }
    public Client(String username, ArrayList<String> chatRooms) {
        this.username = username;
        this.chatRooms = chatRooms;
    }
    public void addChatroom(String newChatroom) {
        this.chatRooms.add(newChatroom);
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getChatRooms() {
        return chatRooms;
    }
    public String toString() {
        return String.format("%s --- %s",this.username,String.join(", ",this.chatRooms));
    }
}
