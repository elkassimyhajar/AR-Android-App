package com.example.myclassroomproject;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class numbers extends AppCompatActivity implements View.OnClickListener {

    public CardView audio;
    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);

        audio = (CardView) findViewById(R.id.audio);
        audio.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.one);
        }
        player.start();
    }
}