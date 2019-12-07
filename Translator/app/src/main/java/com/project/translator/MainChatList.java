package com.project.translator;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainChatList extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    List<User> userList;
    FirebaseAuth mAuth;
    FirebaseUser user;
    MainListAdapter mainListAdapter;
//    FirebaseRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat_list);
        recyclerView = findViewById(R.id.userlist);
        userList = new ArrayList<User>();
        mAuth = FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
         databaseReference=FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {
                    User value = dataSnapshot1.getValue(User.class);
//                    Toast.makeText(MainChatList.this, ""+value.getUid(), Toast.LENGTH_SHORT).show();
                    User output = new User(value);
//                    Toast.makeText(MainChatList.this, "" + output.getEmail() + "," + user.getEmail(), Toast.LENGTH_SHORT).show();
                    if(!output.getEmail().equals(user.getEmail()))
                        userList.add(output);
//                    Toast.makeText(MainChatList.this, ""+output.getUid(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainListAdapter = new MainListAdapter(userList,this);
        recyclerView.setAdapter(mainListAdapter);
    }

}
