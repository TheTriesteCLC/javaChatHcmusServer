package Data;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private String sender;
    private String content;
    private String chatroom;
    public Message(String sender, String content, String chatroom) {
        this.sender = sender;
        this.content = content;
        this.chatroom = chatroom;
    }
    public String getSender() {
        return this.sender;
    }
    public String getContent() {
        return this.content;
    }
    public String getChatroom() {
        return chatroom;
    }
    public String toString() {
        return this.sender + ": " + this.content + "+to+" + this.chatroom;
    }
}
