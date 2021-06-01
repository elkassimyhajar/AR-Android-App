package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

public class LevelsActivity extends AppCompatActivity implements View.OnClickListener {

    public CardView card1, card2, card3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        card1 = (CardView) findViewById(R.id.c1);
        card2 = (CardView) findViewById(R.id.c2);
        card3 = (CardView) findViewById(R.id.c3);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch(v.getId()) {
            case R.id.c1 :
                i = new Intent(this, FirstLevelActivity.class);
                startActivity(i);
                break;
            case R.id.c2 :
                i = new Intent(this, SecondLevelActivity.class);
                startActivity(i);
                break;
            case R.id.c3 :
                i = new Intent(this, ThirdLevelActivity.class);
                startActivity(i);
                break;
        }
    }
}