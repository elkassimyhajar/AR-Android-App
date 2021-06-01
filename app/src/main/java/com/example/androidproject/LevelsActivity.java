package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myclassroomproject.R;

public class LevelsActivity extends AppCompatActivity implements View.OnClickListener {

    public CardView card1, card2, card3;
    private String selectedTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        // Get the selected topic
        Bundle bundle = getIntent().getExtras();
        selectedTopic = bundle.getString("topic");

        card1 = (CardView) findViewById(R.id.c1);
        card2 = (CardView) findViewById(R.id.c2);
        card3 = (CardView) findViewById(R.id.c3);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent i;
        switch(v.getId()) {
            case R.id.c1 :
                if(selectedTopic.equals("animals")) {
                    i = new Intent(this, FirstLevelActivity.class);
                    startActivity(i);
                }
                else if(selectedTopic.equals("veggies")) {
                    i = new Intent(this, SecondLevelActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.c2 :
                if(selectedTopic.equals("animals")) {
                    i = new Intent(this, ThirdLevelActivity.class);
                    startActivity(i);
                }
                else if(selectedTopic.equals("veggies")) {
                    //
                }
                break;
            case R.id.c3 :
                if(selectedTopic.equals("animals")) {
                    //
                }
                else if(selectedTopic.equals("veggies")) {
                    //
                }
                break;
        }
    }
}