package com.example.messaging;

import android.view.View;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChatModel {
    String sender,reciver,message,displayUserId;

    long timeStamp;
    Boolean isCurrentUser;
    Date date ;
    Calendar calendar = Calendar.getInstance();



    public Boolean getCurrentUser() {
        return isCurrentUser;
    }

    public ChatModel(String sender, String reciver, String message, long timeStamp, Boolean isCurrentUser) {
        this.sender = sender;
        this.reciver = reciver;
        this.message = message;
        this.timeStamp = timeStamp;
        this.isCurrentUser = isCurrentUser;
        date= new Date(timeStamp);
        calendar.setTime(date);
    }

    public void setDisplayUserId(String displayUserId) {
        this.displayUserId = displayUserId;
    }

    public String getDisplayUserId() {
        return displayUserId;
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

    public String getDate(){
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int month = calendar.get(Calendar.MONTH);
        int Year = calendar.get(Calendar.YEAR);

        return day+"/"+month+"/"+Year;
    }

    public String getTime(){
        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);

        int am_pm = calendar.get(Calendar.AM_PM);
        String am;
        if (am_pm == 0)
            am = "AM";
        else
            am = "PM";

        if ( minutes<10){
            return hour+":0"+minutes+" "+am;
        }

        return hour+":"+minutes+" "+am;
    }
}
