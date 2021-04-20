package com.example.match_it;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private TextView textView;
    /*private Button playButton;
    private MediaPlayer mediaPlayer;
    private Animation bounce_anim;*/

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //get the selected topic from SharedPreferences
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String selectedTopic = preferences.getString("topic", "NOT FOUND");

        //personalize the bg of the layout
        this.layout = findViewById(R.id.loadingRelativeLayout);
        if (selectedTopic.equals("Alphabets")) {
            layout.setBackground(getDrawable(R.drawable.letters_bg));
        }
        else if (selectedTopic.equals("Shapes")) {
            layout.setBackground(getDrawable(R.drawable.shapes_bg));
        }

        this.textView = findViewById(R.id.gameName);
        // show game name letter by letter
        Thread thread = new Thread() {
            int i;
            final String gameName = getString(R.string.game_name) + " ...";
            @Override
            public void run() {
                try {
                    for (i = 0; i < gameName.length(); i++) {
                        Thread.sleep(500);
                        runOnUiThread(() -> textView.setText(gameName.substring(0, i)));
                    }
                    Thread.sleep(500);
                    runOnUiThread(() -> startActivity(new Intent(LoadingActivity.this, GameActivity.class)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        /*this.playButton = findViewById(R.id.playButton);
        //animate play button
        bounce_anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bounce);
        this.playButton.startAnimation(bounce_anim);
        this.playButton.setOnClickListener(v -> {
            mediaPlayer = MediaPlayer.create(LoadingActivity.this, R.raw.playbuttonclick);
            mediaPlayer.start();
            startActivity(new Intent(LoadingActivity.this, TopicsActivity.class));
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        //this.playButton.startAnimation(bounce_anim);
    }
}
