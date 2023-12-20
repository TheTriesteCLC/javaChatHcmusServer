package Data;

import Data.Message;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatRoom implements Serializable {
    private String roomName;
    private String type;
    private ArrayList<Message> messages;
    private ArrayList<Client> clients;
    public ChatRoom(String roomName, String type , ArrayList<Client> clients) {
        this.roomName = roomName;
        this.type = type;
        this.messages = new ArrayList<Message>();
        this.clients = clients;
    }
    public ChatRoom(String roomName, String type, ArrayList<Message> messages , ArrayList<Client> clients) {
        this.roomName = roomName;
        this.type = type;
        this.messages = messages;
        this.clients = clients;
    }

    public void addMessage(Message newMess) {
        this.messages.add(newMess);
    }
    public void deleteMessage(int index) {this.messages.remove(index);}

    public String getRoomName() {
        return roomName;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public String toString() {
        ArrayList<String> mess = new ArrayList<>();
        ArrayList<String> client = new ArrayList<>();
        for(Message tmp : this.messages) {
            mess.add(tmp.toString());
        }
        for(Client tmp : this.clients) {
            client.add(tmp.toString());
        }
        return String.format("%s --- %s --- %s --- %s",this.roomName, this.type, String.join(", ",mess),String.join(", ",client));
    }
}
