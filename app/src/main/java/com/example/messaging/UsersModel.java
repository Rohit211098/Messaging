package com.example.messaging;

import android.os.Parcel;
import android.os.Parcelable;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class UsersModel implements Comparable <UsersModel>,SearchSuggestion{
    private String name;
    private String imageURL;
    private ChatModel lastChat;
    private String userId;


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public UsersModel createFromParcel(Parcel in) {
            return new UsersModel(in);
        }

        public UsersModel[] newArray(int size) {
            return new UsersModel[size];
        }
    };


    public UsersModel(Parcel in){
        this.name = in.readString();
        this.imageURL = in.readString();
        this.userId =  in.readString();
    }


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

    @Override
    public String getBody() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.imageURL);
        dest.writeString(this.userId);
    }
}
