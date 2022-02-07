package com.example.analogbluetoothchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class signUp extends AppCompatActivity {

    ImageButton backButton;
    EditText inputUser,inputEmail,inputPass;
    Button registerButton;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        backButton = (ImageButton)findViewById(R.id.imageButton);
        inputUser = (EditText)findViewById(R.id.userId);
        inputEmail = (EditText)findViewById(R.id.emailId);
        inputPass = (EditText)findViewById(R.id.passwordId);
        registerButton = (Button)findViewById(R.id.containedButton);
        progressBar = (ProgressBar)findViewById(R.id.progressBarId);

        mAuth = FirebaseAuth.getInstance();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(signUp.this,LoginPage.class);
                startActivity(i);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String pass = inputPass.getText().toString().trim();
                String user = inputUser.getText().toString().trim();
                if(user.isEmpty())
                {
                   inputUser.setError("Please provide your user name!");
                   inputUser.requestFocus();
                   return;
                }
                if(email.isEmpty())
                {
                    inputEmail.setError("Please provide a valid email address!");
                    inputEmail.requestFocus();
                    return;
                }
                if(pass.isEmpty())
                {
                    inputPass.setError("Please set a strong password!");
                    inputPass.requestFocus();
                    return;
                }
                if(pass.length() < 6)
                {
                    inputPass.setError("Password must have at least 6 characters!");
                    inputPass.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(signUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(signUp.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(signUp.this,onlineChatPage.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(signUp.this, "User is already registered", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(signUp.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
            }
        });
    }

   /* @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }*/
}