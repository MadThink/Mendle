package com.example.kayda.mendle;

import android.app.ActionBar;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class OtherUsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private FirebaseFirestore mFirestore;
    private static final String TAG="FireLog";
    private UsersListAdapter usersListAdapter;
    private List<Users> usersList;
    private Button buttonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_users);

       // mToolbar=(Toolbar)findViewById(R.id.users_appBar);
        //getSupportActionBar().setTitle(R.string.all_users);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // setSupportActionBar(mToolbar);


        mFirestore = FirebaseFirestore.getInstance();
        //buttonProfile=(Button) findViewById(R.id.open_profile);

        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        usersList=new ArrayList<>();
        usersListAdapter=new UsersListAdapter(getApplicationContext(),usersList);

        mRecyclerView =(RecyclerView) findViewById(R.id.users_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(usersListAdapter);

        mFirestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e!=null)
                {
                    Log.d(TAG,"Error:"+e.getMessage());
                }

                for(DocumentChange doc:documentSnapshots.getDocumentChanges()){

                    if(doc.getType()==DocumentChange.Type.ADDED){
                        String userId=doc.getDocument().getId();
                        Users users =doc.getDocument().toObject(Users.class).withId(userId);
                        usersList.add(users);
                        usersListAdapter.notifyDataSetChanged();
                    }

                }
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