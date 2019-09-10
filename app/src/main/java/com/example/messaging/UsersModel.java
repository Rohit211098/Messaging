package com.example.messaging;

public class UsersModel {
    String name;
    String imageURL;

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

}
