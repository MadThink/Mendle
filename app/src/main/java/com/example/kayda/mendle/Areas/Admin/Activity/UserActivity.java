package com.example.kayda.mendle.Areas.Admin.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.kayda.mendle.App_Start.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {

    private CircleImageView setupImage;
    private Uri mainImageURI = null;

    private String user_id;

    private boolean isChanged = false;

    private EditText setupName;
    private Button setupBtn;
    private ProgressBar setupProgress;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.kayda.mendle.R.layout.activity_user);

        Toolbar setupToolbar = findViewById(com.example.kayda.mendle.R.id.toolbar2);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Account Setup");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);


        setupImage = findViewById(com.example.kayda.mendle.R.id.circle_image_view);
        setupName = findViewById(com.example.kayda.mendle.R.id.edit_name);
        setupBtn = findViewById(com.example.kayda.mendle.R.id.setup_button);
        setupProgress = findViewById(com.example.kayda.mendle.R.id.progressBar2);

        setupProgress.setVisibility(View.VISIBLE);
        setupBtn.setEnabled(false);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(UserActivity.this, dataSnapshot.toString(), Toast.LENGTH_LONG).show();
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                //String thumb_nail= dataSnapshot.child("thumb nail").getValue().toString();

                setupName.setText(name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_value = setupName.getText().toString();
                mDatabase.child("Users").child("name").setValue(name_value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Intent activity= new Intent(UserActivity.this,MainActivity.class);
                            startActivity(activity);
                            finish();

                        } else {

                        }
                    }
                });
            }
        });

     setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_name = setupName.getText().toString();

                if (!TextUtils.isEmpty(user_name) && mainImageURI != null) {

                    setupProgress.setVisibility(View.VISIBLE);

                    if (isChanged) {

                        user_id = firebaseAuth.getCurrentUser().getUid();

                        StorageReference image_path = storageReference.child("profile_images").child(user_id + ".jpg");
                        image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {

                                    storeFirestore(task, user_name);

                                } else {

                                    String error = task.getException().getMessage();
                                    Toast.makeText(UserActivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();

                                    setupProgress.setVisibility(View.INVISIBLE);

                                }
                            }
                        });

                    } else {

                        storeFirestore(null, user_name);

                    }

                }

            }

        });

        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(UserActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(UserActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }

            }

        });


    }

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String user_name) {

        Uri download_uri;

        if(task != null) {

            download_uri = task.getResult().getDownloadUrl();

        } else {

            download_uri = mainImageURI;

        }

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", user_name);
        userMap.put("image", download_uri.toString());

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(UserActivity.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(UserActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(UserActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                }

                setupProgress.setVisibility(View.INVISIBLE);

            }
        });


    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(UserActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

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