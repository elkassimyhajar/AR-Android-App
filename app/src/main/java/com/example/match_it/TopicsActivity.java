package com.example.match_it;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class TopicsActivity extends AppCompatActivity {

    ViewPager viewPager;
    Button button;
    RelativeLayout relativeLayout;
    ImageView helpView;

    Adapter adapter;
    List<Etablissement> etablissements;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        etablissements = new ArrayList<>();
        etablissements.add(new Etablissement(R.drawable.abc, "Alphabets", R.raw.alphabets));
        etablissements.add(new Etablissement(R.drawable.numbers, "Numbers", R.raw.numbers));
        etablissements.add(new Etablissement(R.drawable.animals, "Animals", R.raw.animals));
        etablissements.add(new Etablissement(R.drawable.fruits, "Fruits", R.raw.fruits));
        etablissements.add(new Etablissement(R.drawable.vegetables, "Vegetables", R.raw.vegetables));
        adapter = new Adapter(etablissements, this);

        this.colors = new Integer[]{
                getResources().getColor(R.color.topic_color5, getTheme()),
                getResources().getColor(R.color.topic_color2, getTheme()),
                getResources().getColor(R.color.topic_color3, getTheme()),
                getResources().getColor(R.color.topic_color4, getTheme()),
                getResources().getColor(R.color.topic_color1, getTheme())
        };

        this.helpView = findViewById(R.id.helpView);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.right_left);
        this.helpView.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((ViewManager)helpView.getParent()).removeView(helpView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



        this.button = findViewById(R.id.goButton);
        this.relativeLayout = findViewById(R.id.relativeLayout);

        this.viewPager = findViewById(R.id.viewPager);
        this.viewPager.setAdapter(adapter);
        //this.viewPager.setPadding(200, 100, 200, 100);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    relativeLayout.setBackgroundColor(
                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                    viewPager.setBackgroundColor(
                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }
                else {
                    relativeLayout.setBackgroundColor(colors[colors.length - 1]);
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {
                mediaPlayer = MediaPlayer.create(TopicsActivity.this, etablissements.get(position).getSound());
                mediaPlayer.start();
                button.setOnClickListener(
                        v -> startActivity(new Intent(TopicsActivity.this, GameActivity.class))
                );
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
