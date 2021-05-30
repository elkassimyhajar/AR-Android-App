package com.example.match_it.activities;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.ViewManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.match_it.utils.TopicEstablishment;
import com.example.match_it.R;
import com.example.match_it.utils.TopicsAdapter;

import java.util.ArrayList;
import java.util.List;

public class TopicsActivity extends AppCompatActivity {

    private Button goButton;
    private RelativeLayout relativeLayout;
    private ImageView helpImage;

    private TopicsAdapter topicsAdapter;
    private List<TopicEstablishment> topicEstablishments;

    private Integer[] colors = null;
    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        topicEstablishments = new ArrayList<>();
        topicEstablishments.add(new TopicEstablishment(R.drawable.abc, "Alphabets", R.raw.alphabets));
        topicEstablishments.add(new TopicEstablishment(R.drawable.numbers, "Numbers", R.raw.numbers));
        topicEstablishments.add(new TopicEstablishment(R.drawable.shapes, "Shapes", R.raw.shapes));
        topicEstablishments.add(new TopicEstablishment(R.drawable.animals, "Animals", R.raw.animals));
        topicEstablishments.add(new TopicEstablishment(R.drawable.fruits, "Fruits", R.raw.fruits));
        topicEstablishments.add(new TopicEstablishment(R.drawable.vegetables, "Vegetables", R.raw.vegetables));

        topicsAdapter = new TopicsAdapter(topicEstablishments, this);

        this.colors = new Integer[]{
                getResources().getColor(R.color.topic_color1, getTheme()),
                getResources().getColor(R.color.topic_color2, getTheme()),
                getResources().getColor(R.color.topic_color3, getTheme()),
                getResources().getColor(R.color.topic_color4, getTheme()),
                getResources().getColor(R.color.topic_color5, getTheme()),
                getResources().getColor(R.color.topic_color6, getTheme())
        };

        this.goButton = findViewById(R.id.goButton);

        this.relativeLayout = findViewById(R.id.relativeLayout);

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
                relativeLayout.postDelayed(() -> ((ViewManager) helpImage.getParent()).removeView(helpImage), 500);
                //((ViewManager) helpImage.getParent()).removeView(helpImage);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });



        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(topicsAdapter);

        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener(){
            boolean firstTime = true;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //change the bg color of the relativeLayout
                if (position < (topicsAdapter.getCount() -1) && position < (colors.length - 1)) {
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
                            mediaPlayer = MediaPlayer.create(TopicsActivity.this, topicEstablishments.get(position).getSound());
                            mediaPlayer.start();
                        });
                    }
                    else {
                        //play the name of the first topic
                        mediaPlayer = MediaPlayer.create(TopicsActivity.this, topicEstablishments.get(position).getSound());
                        mediaPlayer.start();
                    }
                } else {
                    //play the sound of the topic name
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(TopicsActivity.this, topicEstablishments.get(position).getSound());
                    mediaPlayer.start();
                }

                goButton.setOnClickListener(
                        v -> {
                            //play click sound effect
                            mediaPlayer.release();
                            mediaPlayer = MediaPlayer.create(TopicsActivity.this, R.raw.playbuttonclick);
                            mediaPlayer.start();
                            mediaPlayer.setOnCompletionListener(mp -> {
                                //pass the name of the selected topic to the next activity
                                Intent intent = new Intent(TopicsActivity.this, LoadingActivity.class);
                                intent.putExtra("topic", topicEstablishments.get(position).getName());
                                //start the next activity
                                startActivity(intent);
                            });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null)
            mediaPlayer.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.release();
        if (mediaPlayer != null)
            mediaPlayer.release();
    }
}
