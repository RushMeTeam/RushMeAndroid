package com.example.prests1.rushmeandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

// This class creates the Splash Screen and runs it with a small animation.
public class SplashActivity extends AppCompatActivity {

    private ImageView logo;
    // The amount of time to delay the Splash Screen
    // Note: Match the value in anim/appsplashanimation.xml as well
    private static int splashTimeOut=1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo=(ImageView)findViewById(R.id.logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },splashTimeOut);

        // Loads in the animation
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.appsplashanimation);
        logo.startAnimation(myanim);
    }
}