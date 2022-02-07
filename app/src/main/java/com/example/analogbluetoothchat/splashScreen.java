package com.example.analogbluetoothchat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;

public class splashScreen extends AppCompatActivity {

    TextView txtWel,txtConnect;
    //ConstraintLayout constraintLayout;
    Animation txtAnimation,layoutAnimation;
    SpinKitView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        hideNavigationBar();

        txtAnimation = AnimationUtils.loadAnimation(splashScreen.this,R.anim.fall_down);
        layoutAnimation = AnimationUtils.loadAnimation(splashScreen.this,R.anim.bottom_to_top);

        txtWel = (TextView)findViewById(R.id.welcome);
        txtConnect = (TextView)findViewById(R.id.connect);
        progressBar = (SpinKitView) findViewById(R.id.spin_kit);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txtWel.setVisibility(View.VISIBLE);
                txtConnect.setVisibility(View.VISIBLE);

                txtWel.setAnimation(txtAnimation);
                txtConnect.setAnimation(txtAnimation);
            }
        },300);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                Intent i = new Intent(splashScreen.this,toMultiplePage.class);
                startActivity(i);

            }
        },3000);
    }
    private void hideNavigationBar()
    {
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );

    }
}