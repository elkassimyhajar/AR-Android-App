package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

import com.example.myclassroomproject.R;

public class GameMainActivity extends AppCompatActivity {

    private MediaPlayer mp;
    private Button btn_about, btn_play;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

//        mp = MediaPlayer.create(getApplicationContext(), R.raw.music);
//        mp.setLooping(true);
//        mp.start();

        btn_play = (Button) findViewById(R.id.btn_play);

        btn_play.setOnClickListener(v -> {
            Intent intent = new Intent(GameMainActivity.this, LevelsActivity.class);
            intent.putExtra("topic", getIntent().getExtras().getString("topic"));
            startActivity(intent);
        });

        btn_about = (Button) findViewById(R.id.btn_about);

        btn_about.setOnClickListener(v -> {
            dialog = new Dialog(GameMainActivity.this);
            dialog.setContentView(R.layout.popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

    }
}