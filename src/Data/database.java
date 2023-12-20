package Data;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class database {
    private HashMap<String, Client> allClients;
    private HashMap<String, ChatRoom> allChatrooms;
    public static String pathInfo = "./src/chatroomInfo";
    public static String pathMessage = "./src/chatroomMessage";
    public static String pathFile = "./src/chatroomFile";
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
        System.out.println("Loaded old chatroom data.");
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
    public static long getFileLengthByte(String pathFile) {
        File file = new File(pathFile);
        return file.length();
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
                this.allChatrooms.put(chatroom,new ChatRoom(chatroom,(chatroom.contains("---") ? "private" : "group"), chatRoomClients));
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
    private void writeChatroomInfo(String chatroom) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(pathInfo + "/" + chatroom + ".txt"))) {
            int i = 0;
            ArrayList<Client> clients = this.allChatrooms.get(chatroom).getClients();

            while(i < clients.size())
            {
                String clientName = clients.get(i).getUsername();
                writer.write(clientName);
                if(i < clients.size() - 1) {
                    writer.write("\n");
                }
                i++;
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeChatroom(String chatroom) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(pathMessage + "/" + chatroom + ".txt"))) {
            int i = 0;
            ArrayList<Message> messsages = this.allChatrooms.get(chatroom).getMessages();

            while(i < messsages.size())
            {
                String msgStr = messsages.get(i).getSender() + ": " + messsages.get(i).getContent();
                writer.write(msgStr);
                if(i < messsages.size() - 1) {
                    writer.write("\n");
                }
                i++;
            }
            writer.close();
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
    public void addNewMessage(Message newMessage) {
        String chatroomType = this.allChatrooms.get(newMessage.getChatroom()).getType();
        ArrayList<Client> chatroomClients = this.allChatrooms.get(newMessage.getChatroom()).getClients();
        ArrayList<Message> chatroomMsg = this.allChatrooms.get(newMessage.getChatroom()).getMessages();
        chatroomMsg.add(newMessage);
        ChatRoom newChatRoom = new ChatRoom(newMessage.getChatroom(), chatroomType, chatroomMsg,chatroomClients);

        this.allChatrooms.put(newChatRoom.getRoomName(),newChatRoom);
    }
    public void addNewClient(Client newClient) {

        for(Client client : this.allClients.values()) {
            if(!Objects.equals(client, newClient.getUsername())) {
                String generatedChatroomName = String.format("%s---%s",client.getUsername(),newClient.getUsername());
                newClient.addChatroom(generatedChatroomName);
                Client putClient = client;
                putClient.addChatroom(generatedChatroomName);
                this.allClients.put(putClient.getUsername(),putClient);

                ArrayList<Client> generatedChatroomClients = new ArrayList<>();
                generatedChatroomClients.add(newClient);
                generatedChatroomClients.add(client);
                ChatRoom generatedChatroom = new ChatRoom(generatedChatroomName,"private",generatedChatroomClients);
                this.allChatrooms.put(generatedChatroomName,generatedChatroom);
            }
        }
        this.allClients.put(newClient.getUsername(),newClient);
    }

    public boolean createNewChatroom(String newChatroomName, ArrayList<String> addingClients) {
        if(this.allChatrooms.containsKey(newChatroomName)) {
            return false;
        }
        ArrayList<Client> foundClients = new ArrayList<>();
        for(String client : addingClients) {
            if(this.allClients.containsKey(client)) {
                foundClients.add(this.allClients.get(client));

                Client updateClient = this.allClients.get(client);
                updateClient.addChatroom(newChatroomName);
                this.allClients.put(updateClient.getUsername(), updateClient);
            }
        }
        ChatRoom newChatroom = new ChatRoom(newChatroomName,"group",foundClients);
        this.allChatrooms.put(newChatroomName, newChatroom);
        return true;
    }
    public void addnewFile(String fileName, long fileLength, DataInputStream dataReceiver) {
        try {
            int bytes = 0;
            FileOutputStream fileOutputStream
                    = new FileOutputStream(pathFile + "/" + fileName);


            byte[] buffer = new byte[4 * 1024];
            while (fileLength > 0
                    && (bytes = dataReceiver.read(buffer, 0, (int)Math.min(buffer.length, fileLength))) != -1) {

                fileOutputStream.write(buffer, 0, bytes);
                fileLength -= bytes;
            }

            System.out.println(fileName + " is saved.");
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteMessage(String chatroomName, int messageIndex) {
        String chatroomType = this.allChatrooms.get(chatroomName).getType();
        ArrayList<Client> chatroomClients = this.allChatrooms.get(chatroomName).getClients();
        ArrayList<Message> chatroomMsg = this.allChatrooms.get(chatroomName).getMessages();
        chatroomMsg.remove(messageIndex);
        ChatRoom newChatRoom = new ChatRoom(chatroomName, chatroomType, chatroomMsg,chatroomClients);

        this.allChatrooms.put(newChatRoom.getRoomName(),newChatRoom);
    }

    private HashMap<String,String> getFileInfoString() {
        HashMap<String, String> fileInfoString = new HashMap<>();

        for(String key : this.allChatrooms.keySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            for(Client client : this.allChatrooms.get(key).getClients()) {
                stringBuilder.append(client.getUsername());
                stringBuilder.append("\n");
            }

            fileInfoString.put(key,stringBuilder.toString());
        }
        return fileInfoString;
    }
    private HashMap<String,String> getFileMessageString() {
        HashMap<String, String> fileMessageString = new HashMap<>();

        for(String key : this.allChatrooms.keySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            for(Message msg : this.allChatrooms.get(key).getMessages()) {
                stringBuilder.append(msg.getSender() + ": " + msg.getContent());
                stringBuilder.append("\n");
            }

            fileMessageString.put(key,stringBuilder.toString());
        }
        return fileMessageString;
    }
    public void savingData(){
        File fileInfo = new File(pathInfo);
        File fileMessage = new File(pathMessage);

        for(ChatRoom chatroom : this.allChatrooms.values()) {
            writeChatroomInfo(chatroom.getRoomName());
            writeChatroom(chatroom.getRoomName());
        }
    }
}
