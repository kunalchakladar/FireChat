package com.example.kunal.firechat;

import android.content.Intent;
import android.os.*;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ChatActivity extends AppCompatActivity {

    private FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference msgRef = mDatabase.getReference().child("message");
    private static final String TAG = "ChatActivity";
    Message map = new Message();
    String chat_friend;
    String chat_friend_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText mMessage = (EditText) findViewById(R.id.msg);
        ListView msgList = (ListView) findViewById(R.id.msgList);
        final ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        msgList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Intent intent = getIntent();
        chat_friend = intent.getStringExtra("chat_friend");
        chat_friend_uid = intent.getStringExtra("chat_friend_uid");
        toolbar.setTitle(chat_friend);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ChatActivity.this, "Tapped", Toast.LENGTH_SHORT).show();

                map.setMsg(mMessage.getText().toString());
                map.setSender(mFirebaseUser.getUid());
                map.setReciever(chat_friend_uid);
                msgRef.push().setValue(map);
                mMessage.setText("");
            }
        });

        msgRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String message;
               // Log.d(TAG, "onChildAdded: " + dataSnapshot.getValue());
              //  Log.d(TAG, "onChildAdded: " + dataSnapshot.getKey());
                Message allMsg;
                allMsg = dataSnapshot.getValue(Message.class);
                message = allMsg.getMsg();
                Log.d(TAG, "onChildAdded: Message: " +message);
                Log.d(TAG, "onChildAdded: Reciever: " +allMsg.getReciever());
                Log.d(TAG, "onChildAdded: Sender: " +allMsg.getSender());

                if((allMsg.getReciever().equals(chat_friend_uid) || allMsg.getReciever().equals(mFirebaseUser.getUid()))
                        && (allMsg.getSender().equals(chat_friend_uid) || allMsg.getSender().equals(mFirebaseUser.getUid()))) {

                        arrayList.add(message);

                }
                Log.d(TAG, "onChildAdded: " + arrayList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
