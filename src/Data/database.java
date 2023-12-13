package Data;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class database {
    private HashMap<String, Client> allClients;
    private HashMap<String, ChatRoom> allChatrooms;
    private static String pathInfo = "./src/chatroomInfo";
    private static String pathMessage = "./src/chatroomMessage";
    public database() {
        this.allClients = new HashMap<String,Client>();
        this.allChatrooms = new HashMap<String, ChatRoom>();

        File fileInfo = new File(pathInfo);
        File fileMessage = new File(pathMessage);
        String[] infoList = fileInfo.list();
        String[] messageList = fileMessage.list();
        for(String info : infoList) {
            String chatroomName = info.split(".txt")[0];
            loadChatroom(chatroomName);
        }
        for(String tmp : messageList) System.out.println(tmp);
    }
    public static int getFileLength(String filePath)
    {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.readLine() != null) lines++;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    public ArrayList<Client> loadingChatroomInfo(String chatroom) {
        try(BufferedReader reader = new BufferedReader(new FileReader(pathInfo + "/" + chatroom + ".txt"))) {
            int i = 0;
            int chatFilelength = getFileLength(pathInfo + "/" + chatroom + ".txt");
            ArrayList<Client> chatRoomClients = new ArrayList<Client>();

            while(i < chatFilelength)
            {
                String username = reader.readLine();

                if(this.allClients.containsKey(username)) {
                    this.allClients.get(username).addChatroom(chatroom);
                }else {
                    Client readClient = new Client(username,new ArrayList<String>(List.of(new String[]{chatroom})));
                    this.allClients.put(readClient.getUsername(),readClient);
                }
                chatRoomClients.add(this.allClients.get(username));

                i++;
            }
            reader.close();
            return chatRoomClients;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void loadChatroom(String chatroom) {
        try(BufferedReader reader = new BufferedReader(new FileReader(pathMessage + "/" + chatroom + ".txt"))) {
            int i = 0;
            int chatFilelength = getFileLength(pathMessage + "/" + chatroom + ".txt");

            ArrayList<Client> chatRoomClients = loadingChatroomInfo(chatroom);
            if(chatRoomClients == null)
            {
                //xu ly null
            }
            else {
                this.allChatrooms.put(chatroom,new ChatRoom(chatroom,(chatroom.contains("$") ? "private" : "group"), chatRoomClients));
                while(i < chatFilelength)
                {
                    String message = reader.readLine();
                    String sender = message.split(": ")[0];
                    String content = message.split(": ")[1];
                    String reveiceChatroom = chatroom;

                    Message readMessage = new Message(sender,content,reveiceChatroom);

                    this.allChatrooms.get(chatroom).addMessage(readMessage);
                    i++;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Client> getAllClients() {
        return allClients;
    }

    public HashMap<String, ChatRoom> getAllChatrooms() {
        return allChatrooms;
    }
}
