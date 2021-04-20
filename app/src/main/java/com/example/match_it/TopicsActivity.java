package com.example.match_it;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private Button goButton;
    private RelativeLayout relativeLayout;
    private ImageView helpImage;

    private Adapter adapter;
    private List<Etablissement> etablissements;

    private Integer[] colors = null;
    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    private MediaPlayer mediaPlayer;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        etablissements = new ArrayList<>();
        etablissements.add(new Etablissement(R.drawable.abc, "Alphabets", R.raw.alphabets));
        etablissements.add(new Etablissement(R.drawable.numbers, "Numbers", R.raw.numbers));
        etablissements.add(new Etablissement(R.drawable.shapes, "Shapes", R.raw.shapes));
        etablissements.add(new Etablissement(R.drawable.animals, "Animals", R.raw.animals));
        etablissements.add(new Etablissement(R.drawable.fruits, "Fruits", R.raw.fruits));
        etablissements.add(new Etablissement(R.drawable.vegetables, "Vegetables", R.raw.vegetables));

        adapter = new Adapter(etablissements, this);

        this.colors = new Integer[]{
                getResources().getColor(R.color.topic_color1, getTheme()),
                getResources().getColor(R.color.topic_color2, getTheme()),
                getResources().getColor(R.color.topic_color3, getTheme()),
                getResources().getColor(R.color.topic_color4, getTheme()),
                getResources().getColor(R.color.topic_color5, getTheme()),
                getResources().getColor(R.color.topic_color6, getTheme())
        };

        //animate helpImage and remove it from the view when the animation ends
        this.helpImage = findViewById(R.id.helpImage);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.right_left);
        this.helpImage.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                ((ViewManager) helpImage.getParent()).removeView(helpImage);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        this.goButton = findViewById(R.id.goButton);

        this.relativeLayout = findViewById(R.id.relativeLayout);

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener(){
            boolean firstTime = true;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //change the bg color of the relativeLayout
                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    relativeLayout.setBackgroundColor(
                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }
                else {
                    relativeLayout.setBackgroundColor(colors[colors.length - 1]);
                }
            }
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    if (firstTime) {
                        firstTime=false;
                        //play the help message
                        mediaPlayer = MediaPlayer.create(TopicsActivity.this, R.raw.help);
                        mediaPlayer.start();
                        //play the name of the first topic once the help message ends
                        mediaPlayer.setOnCompletionListener(mp -> {
                            mediaPlayer = MediaPlayer.create(TopicsActivity.this, etablissements.get(position).getSound());
                            mediaPlayer.start();
                        });
                    }
                    else {
                        //play the name of the first topic
                        mediaPlayer = MediaPlayer.create(TopicsActivity.this, etablissements.get(position).getSound());
                        mediaPlayer.start();
                    }
                } else {
                    //play the sound of the topic name
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(TopicsActivity.this, etablissements.get(position).getSound());
                    mediaPlayer.start();
                }

                goButton.setOnClickListener(
                        v -> {
                            //play click sound effect
                            mediaPlayer.release();
                            mediaPlayer = MediaPlayer.create(TopicsActivity.this, R.raw.playbuttonclick);
                            mediaPlayer.start();
                            //save the selected topic as a shared preference
                            preferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("topic", etablissements.get(position).getName());
                            Log.i("topic",etablissements.get(position).getName() );
                            editor.apply();
                            //start next activity
                            startActivity(new Intent(TopicsActivity.this, LoadingActivity.class));
                        }
                );
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
        pageChangeListener.onPageSelected(0);
        viewPager.addOnPageChangeListener(pageChangeListener);
    }
}
