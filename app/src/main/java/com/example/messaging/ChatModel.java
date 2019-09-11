package com.example.messaging;

public class ChatModel {
    String sender,reciver,message;
    long timeStamp;
    Boolean isCurrentUser;


    public Boolean getCurrentUser() {
        return isCurrentUser;
    }

    public ChatModel(String sender, String reciver, String message, long timeStamp, Boolean isCurrentUser) {
        this.sender = sender;
        this.reciver = reciver;
        this.message = message;
        this.timeStamp = timeStamp;
        this.isCurrentUser = isCurrentUser;
    }




    public String getSender() {
        return sender;
    }

    public String getReciver() {
        return reciver;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
