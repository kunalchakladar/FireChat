package com.example.kunal.firechat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private TextView textView;
    private Button logout;
    private ListView friendListView;
    private ArrayList<String> friendArrayList, uIDs;
    private ArrayAdapter<String> adapter;
    private static final String TAG = "FriendsActivity";

    private FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = mDatabase.getReference().child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        textView = (TextView) findViewById(R.id.textView2);
        logout = (Button) findViewById(R.id.logout);
        friendListView = (ListView) findViewById(R.id.friendList);
        friendArrayList = new ArrayList<>();
        uIDs = new ArrayList<>();
        adapter = new ArrayAdapter<String>(FriendsActivity.this, android.R.layout.simple_list_item_1, friendArrayList);

        //textView.setText("Your friends, " + mFirebaseUser.getEmail());
        friendListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        friendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

              //  Toast.makeText(FriendsActivity.this, friendArrayList.get(i) + uIDs.get(i+1) + " tapped", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);
                intent.putExtra("chat_friend", friendArrayList.get(i));
                intent.putExtra("chat_friend_uid", uIDs.get(i+1));
                startActivity(intent);

            }
        });

        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String userID = mFirebaseUser.getUid();
                Log.d(TAG, "onChildAdded: userID " +userID);
                String key = dataSnapshot.getKey();
                Log.d(TAG, "onChildAdded: " + key.toString());
                String names = dataSnapshot.getValue(String.class);

                if(!userID.equals(key)) {

                    friendArrayList.add(names);

                }else {

                    textView.setText("Welcome " + names);

                }
                uIDs.add(key);

                Log.d(TAG, "onChildAdded: Friends : " + friendArrayList);
                Log.d(TAG, "onChildAdded: FriendUIDs : " + uIDs);

//                for (int i = 0; i < uIDs.size(); ++i){
//
//                    if(uIDs.get(i) == key){
//                        uIDs.remove(i);
//                        friendArrayList.remove(i);
//                    }
//
//                }
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(FriendsActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

    }
}
