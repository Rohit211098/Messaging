package com.example.messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button logout;
    FloatingActionButton startChat;
    private FirebaseAuth auth;
    FirebaseUser mUserId;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersDb = db.child("Users");
    DatabaseReference conversationsDb,lastMessageDb;
    List<UsersModel> usersModels = new ArrayList<>();
    List<ChatModel> lastChat = new ArrayList<>();
    RecyclerView recyclerView;
    MainAdapter adapter;
    DatabaseReference user;
    ValueEventListener lastMessageListener,userListener;
    ChildEventListener conversationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout =findViewById(R.id.button_logout);
        recyclerView = findViewById(R.id.main_recycle);
        auth = FirebaseAuth.getInstance();
        mUserId = auth.getCurrentUser();
        startChat = findViewById(R.id.FAB_start_conversation);

        conversationsDb = db.child("Conversation").child(mUserId.getUid());


        getUsers();

        setupRecycler();

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


    private void getUsers(){

//        conversationsDb.addChildEventListener();


        conversationListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getValue() != null){

                    final String key = dataSnapshot.getKey();
                    user = usersDb.child(key);
                    lastMessageDb = conversationsDb.child(key).child("LastMessage");
                    lastMessageListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map data = (HashMap) dataSnapshot.getValue();

                            if (key.matches(data.get("Sender").toString())&&lastChat.size() == dataSnapshot.getChildrenCount()){
                                ChatModel chatModel = new ChatModel(data.get("Sender").toString(),data.get("Receiver").toString(),data.get("Message").toString(),Long.valueOf(data.get("TimeStamp").toString()),true);
                                chatModel.setDisplayUserId(key);
                                for (ChatModel i : lastChat){
                                    if (i.getDisplayUserId().matches(data.get("Sender").toString())){
                                    int index = lastChat.indexOf(i);
                                    lastChat.set(index,chatModel);
                                    break;
                                }
                            }

                                adapter.notifyDataSetChanged();

                            }else {
                                ChatModel chatModel = new ChatModel(data.get("Sender").toString(),data.get("Receiver").toString(),data.get("Message").toString(),Long.valueOf(data.get("TimeStamp").toString()),true);
                                chatModel.setDisplayUserId(key);
                                lastChat.add(chatModel);
                                adapter.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };


                    lastMessageDb.addValueEventListener(lastMessageListener);



                    userListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Map data = (HashMap) dataSnapshot.getValue();


                            usersModels.add(new UsersModel(data.get("Name").toString(),data.get("ProfileImage").toString(),key));

                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };



                    user.addListenerForSingleValueEvent(userListener);





                }else {
                    Log.e("tag","+++++++++++nonono");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }





    private void setupRecycler(){
        adapter = new MainAdapter(usersModels,getApplicationContext(),lastChat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void logout(){
        auth.signOut();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();



    }

    @Override
    protected void onStop() {
        super.onStop();
        if (lastMessageListener != null){
            lastMessageDb.removeEventListener(lastMessageListener);
        }

        conversationsDb.removeEventListener(conversationListener);

        usersModels.clear();



    }

    @Override
    protected void onStart() {
        super.onStart();
        lastChat.clear();
        conversationsDb.addChildEventListener(conversationListener);

    }

}
