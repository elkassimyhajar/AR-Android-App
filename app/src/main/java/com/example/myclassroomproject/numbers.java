package com.example.myclassroomproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.google.ar.sceneform.ux.ArFragment;

public class numbers extends AppCompatActivity implements View.OnClickListener {

    public CardView audio, aricon;
    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);

        audio = (CardView) findViewById(R.id.audio);
        aricon = (CardView) findViewById(R.id.aricon);

        audio.setOnClickListener(this);
        aricon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.audio:
                if (player == null) {
                    player = MediaPlayer.create(this, R.raw.one);
                }
                player.start();
                break;
            case R.id.aricon:
                Intent i = new Intent(this, ArActivity.class);
                i.putExtra("object", "duck");
                startActivity(i);
                break;
        }

    }
}