package com.example.match_it.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.match_it.R;
import com.google.ar.core.ArCoreApk;

import java.util.Objects;

public class LoadingActivity extends Activity {

    private RelativeLayout layout;
    private TextView textView;
    /*private Button playButton;
    private MediaPlayer mediaPlayer;
    private Animation bounce_anim;*/

    Thread thread;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Get the selected topic
        Bundle bundle = getIntent().getExtras();
        String selectedTopic = bundle.getString("topic");

        // Personalize the bg of the layout
        this.layout = findViewById(R.id.loadingRelativeLayout);
        if (selectedTopic.equals("Alphabets")) {
            layout.setBackground(getDrawable(R.drawable.letters_bg));
        }
        else if (selectedTopic.equals("Shapes")) {
            layout.setBackground(getDrawable(R.drawable.shapes_bg));
        }

        this.textView = findViewById(R.id.gameName);

        Intent intent = new Intent(LoadingActivity.this, LevelsActivity.class);
        intent.putExtra("topic", selectedTopic);

        //checkSystemSupport(this);

        // show game name letter by letter
        // then, start GameActivity
        thread = new Thread() {
            int i;
            final String gameName = getString(R.string.game_name) + " ...";
            @Override
            public void run() {
                try {
                    for (i = 0; i < gameName.length(); i++) {
                        Thread.sleep(250);
                        runOnUiThread(() -> {
                            textView.setText(gameName.substring(0, i));
                        });
                    }
                    Thread.sleep(250);
                    runOnUiThread(() -> startActivity(intent));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

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

    @Override
    protected void onPause() {
        super.onPause();
        thread.interrupt();
        finish();
    }

    /*private void checkSystemSupport(Activity activity) {
        final ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(activity);
        if (availability.isTransient()) {
            // re-query at 5Hz while we check compatibility.
            new Handler().postDelayed(() -> checkSystemSupport(activity), 200);
        }
        if(availability.isUnsupported()) {
            Toast.makeText(activity, "This device is not supported.", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }*/
}
