package com.example.match_it;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView textView;
    private Button playButton;
    private MediaPlayer mediaPlayer;
    private Animation bounce_anim;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.textView = findViewById(R.id.gameName);
        // show game name letter by letter
        Thread thread = new Thread() {
            int i;
            String text = "Match it!";
            @Override
            public void run() {
                try {
                    for (i = 0; i < text.length(); i++) { // use your variable text.leght()
                        Thread.sleep(500);
                        runOnUiThread(() -> textView.setText(text.substring(0, i)));
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();

        this.playButton = findViewById(R.id.playButton);
        //animate play button
        bounce_anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        this.playButton.startAnimation(bounce_anim);
        this.playButton.setOnClickListener(v -> {
            mediaPlayer = MediaPlayer.create(HomeActivity.this, R.raw.playbuttonclick);
            mediaPlayer.start();
            startActivity(new Intent(HomeActivity.this, TopicsActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.playButton.startAnimation(bounce_anim);
    }
}
