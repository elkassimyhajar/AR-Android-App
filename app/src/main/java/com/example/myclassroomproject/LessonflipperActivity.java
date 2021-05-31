package com.example.myclassroomproject;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.Serializable;
import java.util.ArrayList;

public class LessonflipperActivity extends AppCompatActivity {
    private final String path = "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/";
    String arPath;
    CardView prev, next, audio, ar;
    ViewFlipper flipper;
    ImageView img;
    TextView txt;
    MediaPlayer player;
    LessonflipperActivity lf;
    ArrayList<Lesson> lessons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessonflipper);

        lf= this;
        //get theme and populate array
        String theme = getIntent().getStringExtra("theme");

        if (theme.equals("numbers")){

        }else if (theme.equals("alphabet")){
            lessons.add(new Lesson(R.drawable.a, "A  a", R.raw.a, "A.gltf"));
            lessons.add(new Lesson(R.drawable.b, "B  b", R.raw.b, "B.gltf"));
            lessons.add(new Lesson(R.drawable.c, "C  c", R.raw.c, "C.gltf"));
            lessons.add(new Lesson(R.drawable.d, "D  d", R.raw.d, "D.gltf"));
        }else if (theme.equals("fruits")){

        }else if (theme.equals("veggies")){

        }else if (theme.equals("shapes")){

        }else if (theme.equals("animals")){

        }

        flipper = (ViewFlipper) findViewById(R.id.flipper);

        setFlipperContent();
    }


    private void setFlipperContent() {

        int size = lessons.size();

        for (int i = 0; i < size; i++) {
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.lesson_template, null);

            flipper.addView(view);

            //next and previous buttons
            next = (CardView) view.findViewById(R.id.next);
            prev = (CardView) view.findViewById(R.id.prev);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipper.showNext();
                }
            });
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipper.showPrevious();
                }
            });

            //add content of layout
            img = (ImageView) view.findViewById(R.id.lessonpic);
            img.setImageResource(lessons.get(i).getImage());
            txt = (TextView) view.findViewById(R.id.lessontext);
            txt.setText(lessons.get(i).getName());

            //add sound
            audio = (CardView) view.findViewById(R.id.audio);
            audio.setTag(lessons.get(i).getSound());
            audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player = MediaPlayer.create(lf, (int) v.getTag());
                    player.start();
                }
            });

            //add Ar
            ar = (CardView) view.findViewById(R.id.aricon);
            arPath = path + lessons.get(i).getArPath();
            ar.setTag(arPath);
            ar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(lf, ArActivity.class);
                    intent.putExtra("path", (String) v.getTag());
                    startActivity(intent);
                }
            });

        }
    }


}