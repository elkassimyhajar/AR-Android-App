package com.example.myclassroomproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EndWordFillActivity extends AppCompatActivity {

    Button playagain;
    TextView score, home;
    EndWordFillActivity instance;
    int note, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_word_fill);

        playagain = (Button) findViewById(R.id.playagain);
        score = (TextView) findViewById(R.id.score);
        home = (TextView) findViewById(R.id.home);
        instance = this;
        playagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(instance, WordFillActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(instance, DashboardActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        note = getIntent().getIntExtra("score", 0);
        total = getIntent().getIntExtra("size", 0);

        score.setText("Votre score est : "+note+" / "+total);

    }
}