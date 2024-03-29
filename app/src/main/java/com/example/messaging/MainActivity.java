package com.example.messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    FloatingActionButton startChat;
    private FirebaseAuth auth;
    FirebaseUser mUserId;
    DatabaseReference db ;
    DatabaseReference usersDb ;
    DatabaseReference conversationsDb,lastMessageDb;
    List<UsersModel> usersModels = new ArrayList<>();
    List<ChatModel> lastChat = new ArrayList<>();
    List<UsersModel> userList = new ArrayList<>();
    RecyclerView recyclerView;
    MainAdapter adapter;
    DatabaseReference user;
    ValueEventListener lastMessageListener,userListener;
    ChildEventListener conversationListener;
    FloatingSearchView searchView ;
    List<UsersModel> temp = new ArrayList<>();
    private String mNewString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        db = FirebaseDatabase.getInstance().getReference();
        usersDb = db.child("Users");
        setContentView(R.layout.activity_main);
        searchView = findViewById(R.id.floating_search_view);
        recyclerView = findViewById(R.id.main_recycle);


        auth = FirebaseAuth.getInstance();
        mUserId = auth.getCurrentUser();
        startChat = findViewById(R.id.FAB_start_conversation);

        conversationsDb = db.child("Conversation").child(mUserId.getUid());




        searchView.bringToFront();


        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {


                //pass them on to the search view
                mNewString = newQuery;
                searchView.showProgress();
                if (newQuery.contentEquals("")){
                    adapter.changeList(userList);
                    searchView.hideProgress();
                }else {

                    temp = filter(newQuery);
                    adapter.changeList(temp);
                    searchView.hideProgress();
                }

//                searchView.swapSuggestions(filter(newQuery));
            }
        });



        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                adapter.changeList(temp);
            }

            @Override
            public void onFocusCleared() {
                if (mNewString == null || mNewString.contentEquals("")){
                    adapter.changeList(userList);
                }
            }
        });

        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.logout){
                    logout();
                }

            }
        });



        getUsers();

        setupRecycler();

        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NewConversation.class);
                startActivity(intent);
            }
        });






    }
    ChatModel chatModelM;
    UsersModel usersModelM;

    private List<UsersModel> filter(String filter){
        List<UsersModel> temp = new ArrayList<>();

        for (UsersModel i : userList){
            if (i.getName().toLowerCase().contains(filter.toLowerCase())){
                temp.add(i);
            }
        }

        return temp;

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
                            if (dataSnapshot.getValue() != null){

                                Map data = (HashMap) dataSnapshot.getValue();

                                if (key.matches(data.get("Sender").toString())&&userList.size() == dataSnapshot.getChildrenCount()){
                                    chatModelM = new ChatModel(data.get("Sender").toString(),data.get("Receiver").toString(),data.get("Message").toString(),Long.valueOf(data.get("TimeStamp").toString()),true);
                                    chatModelM.setDisplayUserId(key);


                                    for (UsersModel i : userList){
                                        if (i.getLastChat().getDisplayUserId().matches(data.get("Sender").toString())){
                                            i.setLastChat(chatModelM);
                                            Collections.sort(userList,Collections.<UsersModel>reverseOrder());
                                            adapter.notifyDataSetChanged();
                                            break;
                                        }


                                    }



                                }else {
                                    chatModelM = new ChatModel(data.get("Sender").toString(),data.get("Receiver").toString(),data.get("Message").toString(),Long.valueOf(data.get("TimeStamp").toString()),true);
                                    chatModelM.setDisplayUserId(key);
//                                lastChat.add(chatModel);
                                    attachLastChat(1);


                                }

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

                            if (dataSnapshot.getValue() != null){
                                Map data = (HashMap) dataSnapshot.getValue();

                                if (data.containsKey("ProfileImage")){
                                    usersModelM = new UsersModel(data.get("Name").toString(),data.get("ProfileImage").toString(),key);
                                }else {
                                    usersModelM = new UsersModel(data.get("Name").toString(),null,key);

                                }


//                            usersModels.add(usersModelM);
                                attachLastChat(2);

//                            adapter.notifyDataSetChanged();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };



                    user.addListenerForSingleValueEvent(userListener);





                }else {
                    Log.e("tag","no data in database snapshot");
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

    private Boolean chatB= false,userB=false;

    private void attachLastChat(int i){
        if (i==1)
            chatB = true;
        else if (i == 2)
            userB = true;

        if ( (chatB && userB)){
            usersModelM.setLastChat(chatModelM);
            userList.add(usersModelM);
            chatB = false;
            userB= false;
            Collections.sort(userList,Collections.<UsersModel>reverseOrder());
            adapter.notifyDataSetChanged();
        }


    }





    private void setupRecycler(){
        adapter = new MainAdapter(userList,getApplicationContext());
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
        userList.clear();
        conversationsDb.addChildEventListener(conversationListener);

    }


}
