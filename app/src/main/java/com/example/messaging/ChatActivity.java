package com.example.messaging;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ChatActivity extends AppCompatActivity {

    String otherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        otherUser = getIntent().getExtras().getString("otherUser");


    }
}
