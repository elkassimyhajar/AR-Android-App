package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class Introductory extends AppCompatActivity {

    ImageView logo, splashImg;
    TextView appName;
    LottieAnimationView lottieAnimationView;

    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);

        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.app_name);
        splashImg = findViewById(R.id.bg);
        lottieAnimationView = findViewById(R.id.lottie);

        splashImg.animate().translationY(-1600).setDuration(2000).setStartDelay(4000);
        logo.animate().translationY(1400).setDuration(2000).setStartDelay(4000);
        appName.animate().translationY(1400).setDuration(2000).setStartDelay(4000);
        lottieAnimationView.animate().translationY(1400).setDuration(2000).setStartDelay(4000);

        //splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}