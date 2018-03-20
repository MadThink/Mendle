package com.example.kayda.mendle;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mProfileImage;
    private TextView mProfileName;
    private Button mSendRequestButton;
    private Button mDeclineRequestButton;
    private String current_state;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrent_user;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mFriendsDatabase;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id=getIntent().getStringExtra("user_id");

        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendsDatabase= FirebaseDatabase.getInstance().getReference().child("FriendRequest");
        mCurrent_user=FirebaseAuth.getInstance().getCurrentUser();

        mProfileImage=(ImageView) findViewById(R.id.profile_image);
        mProfileName=(TextView)findViewById(R.id.profile_display_name);
        mSendRequestButton=(Button)findViewById(R.id.profile_send_rqs_btn);

        current_state="not friends";

        mProgress=new ProgressDialog(this);
        mProgress.setTitle("Loading User Data");
        mProgress.setMessage("Please wait while we load the user data");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String display_name=dataSnapshot.child("name").getValue().toString();
                String display_image=dataSnapshot.child("image").getValue().toString();

                mProfileName.setText(display_name);

                RequestOptions placeholderRequest = new RequestOptions();
                placeholderRequest.placeholder(R.drawable.ic_account_circle_black_24dp);

                Glide.with(ProfileActivity.this).setDefaultRequestOptions(placeholderRequest).load(display_image).into(mProfileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        mSendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_state.equals("not friends")){

                    mFriendsDatabase.child(mCurrent_user.getUid()).child(user_id).child("request_type")
                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                mFriendsDatabase.child(user_id).child(mCurrent_user.getUid()).child("request_type")
                                        .setValue("recieved").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ProfileActivity.this,"Friend Request Sent",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(ProfileActivity.this,"Failed Sending Request",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
