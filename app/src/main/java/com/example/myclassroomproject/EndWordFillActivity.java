package com.example.myclassroomproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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