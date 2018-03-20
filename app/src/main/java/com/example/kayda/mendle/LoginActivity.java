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

public class LoginActivity extends AppCompatActivity {

    private EditText emailLogin;
    private EditText passLogin;
    private Button buttonLogin;
    private Button newAccountLogin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        emailLogin=(EditText) findViewById(R.id.reg_email);
        passLogin=(EditText) findViewById(R.id.reg_password);
        buttonLogin=(Button) findViewById(R.id.new_account_reg_button);
        newAccountLogin=(Button) findViewById(R.id.login_new_account);
        progressBarLogin=(ProgressBar) findViewById(R.id.progressBar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String loginEmail=emailLogin.getText().toString();
                String loginPass=passLogin.getText().toString();

                if(!TextUtils.isEmpty(loginEmail)&&!TextUtils.isEmpty(loginPass)){
                    progressBarLogin.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(loginEmail,loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                sendToMain();

                            } else {

                                String errormessage=task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,"Error: " +errormessage,Toast.LENGTH_LONG).show();
                            }



                        }
                    });
                }
            }
        });

        newAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent= new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regIntent);

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

    private void sendToMain() {

        Intent intent= new Intent(LoginActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
