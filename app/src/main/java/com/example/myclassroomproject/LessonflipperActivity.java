package com.example.myclassroomproject;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class LessonflipperActivity extends AppCompatActivity {
    private final String path = "https://github.com/elkassimyhajar/3D-Gltf-Samples/raw/main/Samples/";
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
            lessons.add(new Lesson(R.drawable.one, "One", R.raw.one, "one.gltf"));
            lessons.add(new Lesson(R.drawable.three, "Three", R.raw.three, "three.gltf"));
        }else if (theme.equals("alphabet")){
            lessons.add(new Lesson(R.drawable.a, "A  a", R.raw.a, "letterA.gltf"));
            lessons.add(new Lesson(R.drawable.b, "B  b", R.raw.b, "letterB.gltf"));
            lessons.add(new Lesson(R.drawable.c, "C  c", R.raw.c, "letterC.gltf"));
            lessons.add(new Lesson(R.drawable.d, "D  d", R.raw.d, "letterD.gltf"));
        }else if (theme.equals("fruits")){
            lessons.add(new Lesson(R.drawable.apple, "Apple", R.raw.apple, "apple.gltf"));
            lessons.add(new Lesson(R.drawable.banana, "Banana", R.raw.banana, "banana.gltf"));
        }else if (theme.equals("veggies")){

        }else if (theme.equals("shapes")){
            lessons.add(new Lesson(R.drawable.square, "Square", R.raw.square, "square_red.gltf"));
            lessons.add(new Lesson(R.drawable.circle, "Circle", R.raw.circle, "circle_blue.gltf"));
            lessons.add(new Lesson(R.drawable.triangle, "Triangle", R.raw.triangle, "triangle_yellow.gltf"));
        }else if (theme.equals("animals")){
            lessons.add(new Lesson(R.drawable.fox, "Fox", R.raw.fox, "Fox.gltf"));
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