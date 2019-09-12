package com.example.messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    String otherUser,image,otherName;
    EditText mMessage;
    Button send;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabaseRef ;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    Boolean isPreviousConversation =false ;
    String currentUser = mAuth.getUid();
    List<ChatModel> chatModels = new ArrayList<>();
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    DatabaseReference userDB = db.child("Users");
    Toolbar toolbar ;
    ImageView profile,back;
    TextView name;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_chat);
        otherUser = getIntent().getExtras().getString("otherUser");
        otherName = getIntent().getExtras().getString("name");
        image = getIntent().getExtras().getString("image");
        mMessage = findViewById(R.id.chat_editText_message);
        send = findViewById(R.id.chat_button_send);
        recyclerView = findViewById(R.id.chat_recycler);
        toolbar =findViewById(R.id.toolbar);

        profile = findViewById(R.id.app_bar_chat_image);
        name = findViewById(R.id.app_bar_chat_name);
        back = findViewById(R.id.imageView_back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Glide.with(this).load(image).into(profile);
        name.setText(otherName);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mMessage.getText().toString();
                sendMessage(message);
            }
        });
        Log.e("tsg","********************************");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Conversation").child(currentUser);

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("tsg","********************************"+dataSnapshot.getValue());
                if(dataSnapshot.getValue() != null){
//                    for (DataSnapshot item :dataSnapshot.getChildren()){
//                        if (item.getKey().contentEquals(otherUser) ){
//                            isPreviousConversation = false;
//                            Log.e("tsg","false"+item.getKey());
//
//                        }else {
//                            isPreviousConversation = true;
//                            Log.e("tsg","true");
//                        }
//                    }
                    if (dataSnapshot.getChildrenCount() > 0 ){
                        isPreviousConversation = true;
                    }else {
                        isPreviousConversation = false;
                    }
                }else {
                    Log.e("tsg","false");
                }
            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getChat();


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(chatModels,getApplicationContext());
        recyclerView.setAdapter(chatAdapter);



    }

    private void sendMessage(String message){
        DatabaseReference conversationRefCurrent = db.child("Conversation").child(mAuth.getUid()).child(otherUser);
        DatabaseReference conversationRefOther =db.child("Conversation").child(otherUser).child(mAuth.getUid());


        Map data = new HashMap();
        data.put("Sender",currentUser);
        data.put("Receiver",otherUser);
        data.put("Message",message);
        data.put("TimeStamp",System.currentTimeMillis());
//        if (!isPreviousConversation){
//            DatabaseReference converRefCurrent = userDB.child(currentUser).child("Conversation");
//            DatabaseReference converRefOther = userDB.child(otherUser).child("Conversation");
//            Map infoCurrent = new HashMap();
//            infoCurrent.put(currentUser,true);
//            Map infoOther = new HashMap();
//            infoOther.put(otherUser,true);
//
//            converRefCurrent.updateChildren(infoOther);
//            converRefOther.updateChildren(infoCurrent);
//            Log.e("tsg","********************************");
//
//        }
        Log.e("tsg",".............................");

        conversationRefCurrent.push().updateChildren(data);
        conversationRefOther.push().updateChildren(data);
        conversationRefCurrent.child("LastMessage").updateChildren(data);
        conversationRefOther.child("LastMessage").updateChildren(data);


    }

    private void  getChat(){
        DatabaseReference conversationRefCurrent = db.child("Conversation").child(mAuth.getUid()).child(otherUser);



        conversationRefCurrent.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Map data = (HashMap) dataSnapshot.getValue();
                Boolean isCurrentUser = false;

                if (data.get("Sender").toString().contentEquals(currentUser)){
                    isCurrentUser = true;
                }

                if(!dataSnapshot.getKey().matches("LastMessage")){
                    chatModels.add(new ChatModel(data.get("Sender").toString(),data.get("Receiver").toString(),data.get("Message").toString(),Long.valueOf(data.get("TimeStamp").toString()),isCurrentUser));
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(chatModels.size()- 1);
                }


                Log.e("tsg",""+dataSnapshot.getValue()+"++++++++++++"+s);

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
        });
    }


}
