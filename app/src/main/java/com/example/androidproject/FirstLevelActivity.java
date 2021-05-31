package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class FirstLevelActivity extends AppCompatActivity {

    public ImageView img1, img2, img3, img4, img5;
    public TextView counterTxt;
    public int counter;

    MediaPlayer sound, soundSuccess, soundWin;

    private ViewPager2 viewPager2;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_level);

        counterTxt = (TextView) findViewById(R.id.counterTxt);
        counter();

        //ViewPager

        viewPager2 = findViewById(R.id.viewPagerImageSlider);

        //Here i'm preparing list of images from drawable
        //We can get it from API as well

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.raccoon_img));
        sliderItems.add(new SliderItem(R.drawable.snake_img));
        sliderItems.add(new SliderItem(R.drawable.hedgehog_img));
        sliderItems.add(new SliderItem(R.drawable.owl_img));
        sliderItems.add(new SliderItem(R.drawable.woodpecker_img));

        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        ///Body images
        img1 = (ImageView) findViewById(R.id.raccoon);
        img2 = (ImageView) findViewById(R.id.snake);
        img3 = (ImageView) findViewById(R.id.hedgehog);
        img4 = (ImageView) findViewById(R.id.owl);
        img5 = (ImageView) findViewById(R.id.woodpecker);

    }

    public void counter() {
        counter = 5;
        counterTxt.setText(Integer.toString(counter));
    }

    public void minusCounter() {
        soundSuccess = MediaPlayer.create(getApplicationContext(), R.raw.success_win_sound_effect);
        sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                soundSuccess.start();
            }
        });

        counter--;
        counterTxt.setText(Integer.toString(counter));

        soundWin = MediaPlayer.create(getApplicationContext(), R.raw.winning_sound_effect);
        if(counter == 0) {

            //Show toast
            Toast toasty = Toasty.success(this, "Good Job", Toast.LENGTH_SHORT);
            toasty.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toasty.show();

            sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    soundWin.start();
                }
            });

            soundWin.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Intent intent = new Intent(getApplicationContext(), LevelsActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public void raccoonClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.raccoon);
        sound.start();
        img1.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void snakeClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.snake);
        sound.start();
        img2.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void hedgehogClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.hedgehog);
        sound.start();
        img3.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void owlClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.owl);
        sound.start();
        img4.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void woodpeckerClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.woodpecker);
        sound.start();
        img5.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void birdClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.bird);
        sound.start();
    }

    public void gazelleClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.gazelle);
        sound.start();
    }

    public void hippoClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.hippopotamus);
        sound.start();
    }

    public void giraffeClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.giraffe);
        sound.start();
    }
}