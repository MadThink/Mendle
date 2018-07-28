package com.example.kayda.mendle.Areas.Profiles.Activity;

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
import com.example.kayda.mendle.Areas.Requests.Models.Request;
import com.example.kayda.mendle.R;
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

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mProfileImageView;
    private TextView mProfileNameView;
    private String mProfileImage;
    private String mProfileName;
    private Button mSendRequestButton;
    private Button mDeclineRequestButton;
    private String current_state;
    private Boolean request_sent;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrent_user;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mRequestsDatabase;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id=getIntent().getStringExtra("user_id");

        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mRequestsDatabase= FirebaseDatabase.getInstance().getReference();
        mCurrent_user=FirebaseAuth.getInstance().getCurrentUser();

        mProfileImageView=(ImageView) findViewById(R.id.profile_image);
        mProfileNameView=(TextView)findViewById(R.id.profile_display_name);
        mSendRequestButton=(Button)findViewById(R.id.profile_send_rqs_btn);


        current_state="not friends";
        request_sent = mUsersDatabase.child("Users")
                .child(mCurrent_user.getUid())
                .child("Requests")
                .child(user_id)
                .child("RequestStatus")
                .toString()
                .equals("sent");

        mProgress=new ProgressDialog(this);
        mProgress.setTitle("Loading User Data");
        mProgress.setMessage("Please wait while we load the user data");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mProfileName=dataSnapshot.child("name").getValue().toString();
                mProfileImage=dataSnapshot.child("image").getValue().toString();

                mProfileNameView.setText(mProfileName);

                RequestOptions placeholderRequest = new RequestOptions();
                placeholderRequest.placeholder(R.drawable.ic_account_circle_black_24dp);

                Glide.with(getApplicationContext()).setDefaultRequestOptions(placeholderRequest).load(mProfileImage).into(mProfileImageView);
                mProgress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mSendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_state.equals("not friends") && !request_sent){

                    Request newRequest = new Request(user_id, mProfileImage, mProfileName);
                    newRequest.RequestStatus = "sent";

                    Map<String, Object> requestValues = newRequest.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();


                    childUpdates.put("/Users/" + mCurrent_user.getUid() + "/" + "Requests" + "/" + user_id, requestValues);
                    childUpdates.put("/Requests/" + mCurrent_user.getUid() + "/" + user_id, requestValues);

                    mRequestsDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mRequestsDatabase.child("Users").child(user_id).child("Requests").child(mCurrent_user.getUid())
                                        .child("RequestStatus").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ProfileActivity.this, "Friend Request Sent!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                mRequestsDatabase.child("Users").child(mCurrent_user.getUid()).child("Requests").child(user_id)
                                        .child("RequestStatus").setValue("failed");
                                Toast.makeText(ProfileActivity.this, "Sorry. Your request failed... :(", Toast.LENGTH_SHORT);
                            }
                        }
                    });


//                    mRequestsDatabase.child(mCurrent_user.getUid()).child("Requests").child(user_id).child("requestStatus")
//                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//
//                                mRequestsDatabase.child(user_id).child(mCurrent_user.getUid()).child("requestStatus")
//                                        .setValue("recieved").addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Toast.makeText(ProfileActivity.this,"Friend Request Sent",Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }else{
//                                Toast.makeText(ProfileActivity.this,"Something went wrong... :(",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

                }
            }
        });

    }
}
