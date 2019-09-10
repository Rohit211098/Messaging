package com.example.messaging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button logout;
    FloatingActionButton startChat;
    private FirebaseAuth auth;
    FirebaseUser mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout =findViewById(R.id.button_logout);
        auth = FirebaseAuth.getInstance();
        mUserId = auth.getCurrentUser();
        startChat = findViewById(R.id.FAB_start_conversation);



        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NewConversation.class);
                startActivity(intent);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            logout();
            }
        });
    }

    private void logout(){
        auth.signOut();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();


    }
}
