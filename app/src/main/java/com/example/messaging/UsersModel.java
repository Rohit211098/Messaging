package com.example.messaging;

public class UsersModel implements Comparable <UsersModel>{
    String name;
    String imageURL;
    ChatModel lastChat;


    public void setLastChat(ChatModel lastChat) {
        this.lastChat = lastChat;
    }

    public ChatModel getLastChat() {
        return lastChat;
    }

    public String getUserId() {
        return userId;
    }

    public UsersModel(String name, String imageURL, String userId) {
        this.name = name;
        this.imageURL = imageURL;
        this.userId = userId;
    }

    String userId;

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }



    @Override
    public int compareTo(UsersModel o) {
        return String.valueOf(this.lastChat.getTimeStamp()).compareTo(String.valueOf(o.lastChat.getTimeStamp()));
    }
}
