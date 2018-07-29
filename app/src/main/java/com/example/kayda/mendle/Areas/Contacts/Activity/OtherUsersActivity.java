package com.example.kayda.mendle.Areas.Contacts.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.example.kayda.mendle.R;
import com.example.kayda.mendle.Areas.Admin.Models.Users;
import com.example.kayda.mendle.Areas.Admin.Adapters.UsersListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class OtherUsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private DatabaseReference mUsersDatabase;
    private static final String TAG="FireLog";
    private UsersListAdapter usersListAdapter;
    private List<Users> usersList;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_users);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList=new ArrayList<>();
        usersListAdapter=new UsersListAdapter(getApplicationContext(),usersList);

        mRecyclerView =(RecyclerView) findViewById(R.id.users_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(usersListAdapter);

        mUsersDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Users user = data.getValue(Users.class);
                    user.userId = data.getKey();
                    Boolean set = true;

                    for(Users u : usersList){
                        if(u.userId.equals(user.userId)){
                            set = false;
                        }
                    }
                        if(set && !user.userId.equals(mCurrentUser.getUid())) {
                            usersList.add(user);
                            usersListAdapter.notifyDataSetChanged();
                        }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       /* buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newUser= new Intent(OtherUsersActivity.this,ProfileActivity.class);
                startActivity(newUser);
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

}