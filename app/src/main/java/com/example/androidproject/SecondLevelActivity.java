package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myclassroomproject.R;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SecondLevelActivity extends AppCompatActivity {

    public ImageView img1, img2, img3, img4, img5, img6, img7, img77, img8, img9, img99, img10;
    public TextView counterTxt;
    public int counter;

    MediaPlayer sound, soundSuccess, soundWin;

    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_level);


        counterTxt = (TextView) findViewById(R.id.counterTxt);
        counter();

        //ViewPager

        viewPager2 = findViewById(R.id.viewPagerImageSlider);

        //Here i'm preparing list of images from drawable
        //We can get it from API as well

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.broccoli_img));
        sliderItems.add(new SliderItem(R.drawable.fennel_img));
        sliderItems.add(new SliderItem(R.drawable.sweet_potato_img));
        sliderItems.add(new SliderItem(R.drawable.red_chillies_img));
        sliderItems.add(new SliderItem(R.drawable.spinach_img));
        sliderItems.add(new SliderItem(R.drawable.zucchini_img));
        sliderItems.add(new SliderItem(R.drawable.pea_img));
        sliderItems.add(new SliderItem(R.drawable.carrots_img));
        sliderItems.add(new SliderItem(R.drawable.eggplant_img));
        sliderItems.add(new SliderItem(R.drawable.artichoke_img));

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
        img1 = (ImageView) findViewById(R.id.broccoli);
        img2 = (ImageView) findViewById(R.id.fennel);
        img3 = (ImageView) findViewById(R.id.sweet_potato);
        img4 = (ImageView) findViewById(R.id.red_chillies);
        img5 = (ImageView) findViewById(R.id.spinach);
        img6 = (ImageView) findViewById(R.id.zucchini);
        img7 = (ImageView) findViewById(R.id.pea1);
        img77 = (ImageView) findViewById(R.id.pea2);
        img8 = (ImageView) findViewById(R.id.carrots);
        img9 = (ImageView) findViewById(R.id.eggplant1);
        img99 = (ImageView) findViewById(R.id.eggplant2);
        img10 = (ImageView) findViewById(R.id.artichoke);

    }

    public void counter() {
        counter = 10;
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

    public void bellPepperClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.bell_pepper);
        sound.start();
    }

    public void tomatoClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.tomato);
        sound.start();
    }

    public void redChilliesClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.red_chillies);
        sound.start();
        img4.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void potatoClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.potato);
        sound.start();
    }

    public void lemonClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.lemon);
        sound.start();
    }

    public void onionClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.onion);
        sound.start();
    }

    public void spinachClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.spinach);
        sound.start();
        img5.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void broccoliClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.broccoli);
        sound.start();
        img1.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void cauliflowerClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.cauliflower);
        sound.start();
    }

    public void artichokeClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.artichoke);
        sound.start();
        img10.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void cabbageClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.cabbage);
        sound.start();
    }

    public void pumpkinClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.pumpkin);
        sound.start();
    }

    public void peaClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.pea);
        sound.start();
        img7.setVisibility(View.INVISIBLE);
        img77.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void zucchiniClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.zucchini);
        sound.start();
        img6.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void sweetPotatoClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.sweet_potato);
        sound.start();
        img3.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void fennelClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.fennel);
        sound.start();
        img2.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void beetrootClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.beetroot);
        sound.start();
    }

    public void carrotsClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.carrots);
        sound.start();
        img8.setVisibility(View.INVISIBLE);
        minusCounter();
    }

    public void eggplantClicked(View view) {
        sound = MediaPlayer.create(getApplicationContext(), R.raw.eggplant);
        sound.start();
        img9.setVisibility(View.INVISIBLE);
        img99.setVisibility(View.INVISIBLE);
        minusCounter();
    }
}