package com.example.messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewConversation extends AppCompatActivity {

    private DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    List<UsersModel> usersModel  = new ArrayList<>();
    RecyclerView recyclerView;
    NewConversationAdapter newConversationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversation);
        recyclerView = findViewById(R.id.new_conversation_recycle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("New Chat");



        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()){

                    Map userMap = (HashMap) user.getValue();
                    if (!user.getKey().contentEquals(mAuth.getUid())){



                        Log.e("tag",""+user.getKey()+"++++++++"+mAuth.getUid());
                        usersModel.add(new UsersModel(userMap.get("Name").toString(),userMap.get("ProfileImage").toString(),user.getKey()));
                    }

                }
                newConversationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        newConversationAdapter = new NewConversationAdapter(usersModel,getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newConversationAdapter);

    }
}
