package com.example.kayda.mendle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailreg;
    private EditText passreg;
    private Button buttonreg;
    private Button loginreg;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressBar progressBarReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth=FirebaseAuth.getInstance();

        emailreg=(EditText) findViewById(R.id.reg_email);
        passreg=(EditText) findViewById(R.id.reg_password);
        buttonreg=(Button) findViewById(R.id.new_account_reg_button);
        loginreg=(Button) findViewById(R.id.login_reg);
        progressBarReg=(ProgressBar) findViewById(R.id.progressBar);

        buttonreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String regEmail=emailreg.getText().toString();
                String regPass=passreg.getText().toString();

                if(!TextUtils.isEmpty(regEmail)&&!TextUtils.isEmpty(regPass)){
                    progressBarReg.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(regEmail,regPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                               if(task.isSuccessful()) {
                                   Intent logIntent = new Intent(RegisterActivity.this, UserActivity.class);
                                   logIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                   startActivity(logIntent);
                                   finish();
                               } else {

                                String errormessage=task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this,"Error: " +errormessage,Toast.LENGTH_LONG).show();

                            }
                        }
                    });
            }
            }
        });

        loginreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logIntent= new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(logIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            sendToMain();
        }
    }

        private void sendToMain(){
            Intent intent= new Intent(RegisterActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

}
