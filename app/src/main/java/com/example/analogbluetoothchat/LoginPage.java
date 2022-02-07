package com.example.analogbluetoothchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    Button createButton;
    TextView resetText;
    EditText emailInput,passInput;
    Button loginButton;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getSupportActionBar().hide();

        createButton = (Button)findViewById(R.id.createButtonId);
        resetText = (TextView)findViewById(R.id.textView2);
        emailInput = (EditText)findViewById(R.id.emailId);
        passInput = (EditText)findViewById(R.id.passwordId);
        loginButton = (Button)findViewById(R.id.containedButton);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        resetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i = new Intent(LoginPage.this,resetPass.class);
                startActivity(i);
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i = new Intent(LoginPage.this,signUp.class);
                startActivity(i);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = emailInput.getText().toString().trim();
                String Pass = passInput.getText().toString().trim();

                if(Email.isEmpty())
                {
                    emailInput.setError("Provide your registered email address!");
                    emailInput.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    emailInput.setError("Enter your valid email address!");
                    emailInput.requestFocus();
                    return;
                }
                if(Pass.isEmpty())
                {
                    passInput.setError("Provide your password!");
                    passInput.requestFocus();
                    return;
                }
                if(Pass.length() < 6)
                {
                    passInput.setError("Password must have at least 6 characters!");
                    passInput.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(Email, Pass)
                        .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginPage.this, "Logged In", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(LoginPage.this,onlineChatPage.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginPage.this, "Login not successful", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });



            }
        });
    }
}