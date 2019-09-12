package com.example.messaging;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Messaging extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
