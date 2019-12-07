package com.project.translator;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements Serializable {

    RecyclerView chatList;
    List<String> chats,chattimes;
    List<Boolean> chatside;
    EditText message;
    ImageButton backButton,settings;
    FloatingActionButton send;
    ImageView profilepic;
    TextView username;
    ChatMessageAdapter chatMessageAdapter;
    FirebaseAuth mAuth;
    FirebaseUser senderuser;
    User recieveruser;
    DatabaseReference dbRef,dbRef2;
    ChatStore userentry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TranslateViewModel viewModel =
                ViewModelProviders.of(this).get(TranslateViewModel.class);
        viewModel.downloadLanguage(new TranslateViewModel.Language("en"));
        viewModel.downloadLanguage(new TranslateViewModel.Language("hi"));
        chatList = findViewById(R.id.chatList);
        chatList.setLayoutManager(new LinearLayoutManager(this));
        chats = new ArrayList<String>();
        chattimes = new ArrayList<String>();
        chatside =new ArrayList<Boolean>();
        userentry = new ChatStore();
        backButton = findViewById(R.id.backButton);
        settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.language_selector);
                dialog.setTitle("Choose Language");
//                sourceLangSelector = dialog.findViewById(R.id.sourceLanguage);
//                targetLangSelector = dialog.findViewById(R.id.targetLanguage);
////                ArrayAdapter<new FirebaseTranslateLanguage.getAllLanguages()> adapter = new ArrayAdapter<FirebaseTranslateLanguage>(this);
////                sourceLangSelector.setAdapter(adapter);
////                targetLangSelector.setAdapter(adapter);
////                sourceLangSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                    @Override
////                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                        sourceLang = adapter.getItem(position);
////                    }
////                });
                dialog.show();
                Button select = dialog.findViewById(R.id.select);
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        message = findViewById(R.id.sourceText);
        send = findViewById(R.id.sendButton);
        profilepic=findViewById(R.id.profile_pic);
        Intent input = (Intent) getIntent();
        recieveruser = (User) input.getSerializableExtra("Currentuser");
        mAuth = FirebaseAuth.getInstance();
        senderuser= mAuth.getCurrentUser();
        username = findViewById(R.id.username);
        username.setText(recieveruser.getName());
        Glide.with(this).load(Uri.parse(recieveruser.getPhotoUrL())).into(profilepic);
        chatMessageAdapter = new ChatMessageAdapter(this,chats,chattimes,chatside);
        chatList.setAdapter(chatMessageAdapter);
        userentry.key=senderuser.getUid()+""+recieveruser.getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
       dbRef = database.getReference().child("chats").child(userentry.key);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat df = new SimpleDateFormat("hh::mm a");
                String time = df.format(new Date()).toString();
                translate(message.getText().toString(),time,true,false);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Toast.makeText(MainActivity.this, "Time"+dataSnapshot.child("time").getValue()+",Message"+dataSnapshot.child("message").getValue(), Toast.LENGTH_SHORT).show();
                translate(dataSnapshot.child("message").getValue().toString(),dataSnapshot.child("time").getValue().toString(),true,true);
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
        String receiverkey = recieveruser.getUid()+""+senderuser.getUid();
        dbRef2 = database.getReference().child("chats").child(receiverkey);
        dbRef2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Toast.makeText(MainActivity.this, "Time"+dataSnapshot.child("time").getValue()+",Message"+dataSnapshot.child("message").getValue(), Toast.LENGTH_SHORT).show();
                translate(dataSnapshot.child("message").getValue().toString(),dataSnapshot.child("time").getValue().toString(),false,true);
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
    private void translate(String string, String time,boolean currentchatside,boolean fromDatabase) {
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(FirebaseTranslateLanguage.HI)
                        .build();
        final FirebaseTranslator LanguageTranslator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);
        LanguageTranslator.translate(string)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String s) {
//                                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
//                                translatedString[0] = s;
                                chats.add(s);
                                 chattimes.add(time);
                                 chatside.add(currentchatside);
                                 if(fromDatabase==false) {
                                     userentry.chats.add(s);
                                     String tempkey = dbRef.push().getKey();
                                     dbRef.child(tempkey).child("time").setValue(time);
                                     dbRef.child(tempkey).child("message").setValue(string);
                                 }
                                chatMessageAdapter.notifyDataSetChanged();
//                                finish();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Translation failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
//        return translatedString[0];
    }
}
