package com.example.messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
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

public class ChatActivity extends AppCompatActivity {

    String otherUser;
    EditText mMessage;
    Button send;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabaseRef ;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    Boolean isPreviousConversation ;
    String currentUser = mAuth.getUid();
    List<ChatModel> chatModels = new ArrayList<>();
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        otherUser = getIntent().getExtras().getString("otherUser");
        mMessage = findViewById(R.id.chat_editText_message);
        send = findViewById(R.id.chat_button_send);
        recyclerView = findViewById(R.id.chat_recycler);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mMessage.getText().toString();
                sendMessage(message);
            }
        });


        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Conversations").child(mAuth.getUid());

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    for (DataSnapshot item :dataSnapshot.getChildren()){
                        if (item.getKey().contentEquals(otherUser) ){
                            isPreviousConversation = false;
                        }else {
                            isPreviousConversation = true;
                        }
                    }
                }
            }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getChat();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

        conversationRefCurrent.push().updateChildren(data);
        conversationRefOther.push().updateChildren(data);


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

                chatModels.add(new ChatModel(data.get("Sender").toString(),data.get("Receiver").toString(),data.get("Message").toString(),Long.valueOf(data.get("TimeStamp").toString()),isCurrentUser));
                chatAdapter.notifyDataSetChanged();

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
