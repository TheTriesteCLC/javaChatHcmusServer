//package Data;
//
//import Data.Message;
//
//import java.io.*;
//import java.lang.reflect.Array;
//import java.util.*;
//
//public class Processing {
//    private String chatroomName;
//    private ArrayList<Client> clients;
//    private ArrayList<Message> messages;
//    public Processing(String chatroomName) {
//        this.chatroomName = chatroomName;
//        this.clients = new ArrayList<Client>();
//        this.messages = new ArrayList<Message>();
//
//        String pathInfo = "./src/chatroomInfo/" + this.chatroomName + ".txt";
//        String pathMessages = "./src/chatroomMessage/" + this.chatroomName + ".txt";
//
//        if(this.chatroomName == null) {
//            //xu ly chatroom ko ton tai
//        }
//        else {
//
//        }
//    }
//    public static int getFileLength(String filePath)
//    {
//        int lines = 0;
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            while (reader.readLine() != null) lines++;
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return lines;
//    }
//    private void loadingChatInfo(String pathInfo)
//    {
//        try(BufferedReader reader = new BufferedReader(new FileReader(pathInfo))) {
//            int i = 0;
//            int chatFilelength = getFileLength(pathInfo);
//
//            while(i < chatFilelength)
//            {
//                String username = reader.readLine();
//
//                Client newClient = new Client(username,)
//                chatFileContent.add(message);
//                i++;
//            }
//            this.userMessages.put(chatRoomName, chatFileContent);
//            reader.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
////    public void writeExistedUser()
////    {
////        try(BufferedWriter writer = new BufferedWriter(
////                new OutputStreamWriter(new FileOutputStream(this.historyFileName, false)))) {
////            int i = 0;
////            writer.write("History search");
////            while(i < this.searchHistory.length)
////            {
////                writer.write("\n");
////                writer.write(this.searchHistory[i]);
////                i++;
////            }
////            writer.close();
////        } catch (FileNotFoundException e) {
////            throw new RuntimeException(e);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
////    }
////    public void setSearchHistory(String[] searchHistory)
////    {
////        this.searchHistory = searchHistory;
////    }
////    public HashMap<String, String[]> getCloneDictionary() {
////        return (HashMap<String, String[]>) this.slangDictionary.clone();
////    }
////    public String[] getHistory() {
////        return this.searchHistory;
////    }
//
////    public String[] getExistedUser() {
////        return existedUser;
////    }
//}
