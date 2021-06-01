package com.example.myclassroomproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IntermediateDashboard extends AppCompatActivity implements View.OnClickListener{
    private ImageView img;
    CardView learn, play, practice;
    String theme;
    private List<Lesson> lessons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate_dashboard);

        learn = (CardView) findViewById(R.id.learn);
        play = (CardView) findViewById(R.id.play);
        practice = (CardView) findViewById(R.id.practice);

        learn.setOnClickListener(this);
        practice.setOnClickListener(this);
        play.setOnClickListener(this);

        lessons = new ArrayList<>();

        Intent i = getIntent();
        theme = i.getStringExtra("theme");

        img = (ImageView) findViewById(R.id.themeImg);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipImage();
            }
        });
        if (theme.equals("numbers")){
            img.setImageResource(R.drawable.numbers);

            //flipImage();
        }else if (theme.equals("alphabet")){
            img.setImageResource(R.drawable.alphabet);

            flipImage();
        }else if (theme.equals("fruits")){
            img.setImageResource(R.drawable.fruits);

            flipImage();
        }else if (theme.equals("veggies")){
            img.setImageResource(R.drawable.veggies);

            flipImage();
        }else if (theme.equals("shapes")){
            img.setImageResource(R.drawable.shapes);

            flipImage();
        }else if (theme.equals("animals")){
            img.setImageResource(R.drawable.animals);

            flipImage();
        }
        //System.out.println("theme : "+theme);

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.learn:
                i = new Intent(this, LessonflipperActivity.class);
                i.putExtra("theme", theme);
                startActivity(i);
                break;
            case R.id.play:
                break;
            case R.id.practice:
                i = new Intent(this, WordFillActivity.class);
                startActivity(i);
                break;
        }
    }
    void flipImage(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        img.startAnimation(animation);
    }


}