package com.example.messaging;


import java.util.Calendar;
import java.util.Date;

public class ChatModel {
    private String sender, receiver,message,displayUserId;
    private  long timeStamp;
    private  Boolean isCurrentUser;
    private Date date ;
    private Calendar calendar = Calendar.getInstance();



    public Boolean getCurrentUser() {
        return isCurrentUser;
    }



    public ChatModel(String sender, String receiver, String message, long timeStamp, Boolean isCurrentUser) {
        this.sender = sender;
        this.receiver = receiver;
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

    public String getReceiver() {
        return receiver;
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

        return day+" "+getMonthInString(month)+" "+Year;
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

    public  Date getDateObj(){
        return this.date;

    }

    public int diffInDate(){
        Date date1 = new Date(System.currentTimeMillis());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        int year1 = calendar1.get(Calendar.YEAR);
        int returnValue;

        if ((year1 - year) == 0){
            if ((day1 -day ) == 0)
                returnValue = 0;
            else if((day1 -day ) ==1)
                returnValue = 1;
            else
                returnValue =2;
        }else {
            return 2;
        }

        return returnValue;
    }




    private String getMonthInString(int month){
        String monthString;

        switch (month+1) {
            case 1:
                monthString = "January";
                break;
            case 2:
                monthString = "February";
                break;
            case 3:  monthString = "March";
                break;
            case 4:  monthString = "April";
                break;
            case 5:  monthString = "May";
                break;
            case 6:  monthString = "June";
                break;
            case 7:  monthString = "July";
                break;
            case 8:  monthString = "August";
                break;
            case 9:  monthString = "September";
                break;
            case 10: monthString = "October";
                break;
            case 11: monthString = "November";
                break;
            case 12: monthString = "December";
                break;
            default: monthString = "Invalid month";
                break;
        }
        return monthString;

    }
}
