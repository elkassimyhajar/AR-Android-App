package com.example.myclassroomproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.match_it.activities.LoadingActivity;

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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.learn:
                i = new Intent(this, LessonflipperActivity.class);
                i.putExtra("theme", theme);
                startActivity(i);
                break;
            case R.id.play:
                if (theme.equals("alphabet") || theme.equals("shapes")) {
                    Log.d("__Theme__", theme);
                    //pass the name of the selected theme to the next activity
                    i = new Intent(this, LoadingActivity.class);
                    i.putExtra("topic", theme);
                    //start the next activity
                    startActivity(i);
                } else if (theme.equals("animals") || theme.equals("veggies")) {
                    Log.d("__Theme__", theme);
                }
                break;
            case R.id.practice:
                i = new Intent(this, WordFillActivity.class);
                startActivity(i);
                break;
        }
    }

    private void flipImage(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        img.startAnimation(animation);
    }

}