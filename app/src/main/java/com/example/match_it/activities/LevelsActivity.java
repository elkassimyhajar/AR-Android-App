package com.example.match_it.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclassroomproject.R;
import com.example.match_it.utils.LevelDecoration;
import com.example.match_it.utils.LevelEstablishment;
import com.example.match_it.utils.LevelsAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class LevelsActivity extends Activity {

    private static boolean mustFinish = false;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchit_levels);

        // Get the selected topic
        Bundle bundle = getIntent().getExtras();
        String selectedTopic = bundle.getString("topic");

        // Get the reference of RecyclerView
        RecyclerView rv = findViewById(R.id.levels_rv);

        // Personalize backgrounds
        ConstraintLayout layout = findViewById(R.id.levels_layout);
        if (selectedTopic.equals("alphabet")) {
            layout.setBackground(getDrawable(R.color.topic_color1));
            rv.setBackground(getDrawable(R.color.topic_color1));
        }
        else if (selectedTopic.equals("shapes")) {
            layout.setBackground(getDrawable(R.color.topic_color3));
            rv.setBackground(getDrawable(R.color.topic_color3));
        }

        // Get the last unlocked level
        SharedPreferences sharedpreferences = getSharedPreferences("matchItPrefs", Context.MODE_PRIVATE);
        int lastUnlockedLevel = sharedpreferences.getInt(selectedTopic+"level", 0);

        ArrayList<LevelEstablishment> levels = new ArrayList<>( Arrays.asList(
                new LevelEstablishment(" LEVEL 1 ", R.drawable.ic_baseline_lock_24, 0, this),
                new LevelEstablishment(" LEVEL 2 ", R.drawable.ic_baseline_lock_24, 1, this),
                new LevelEstablishment(" LEVEL 3 ", R.drawable.ic_baseline_lock_24, 2, this),
                new LevelEstablishment(" LEVEL 4 ", R.drawable.ic_baseline_lock_24, 3, this)
        ));

        for (int i = 0; i < lastUnlockedLevel + 1; i++) {
            levels.get(i).setImg(R.drawable.ic_baseline_lock_open_24);
        }

        // Set a LinearLayoutManager with default vertical orientation
        rv.setLayoutManager(new LinearLayoutManager(this));
        // Call the constructor of LevelsAdapter to send the reference and data to Adapter
        rv.setAdapter(new LevelsAdapter(levels, selectedTopic, lastUnlockedLevel));
        int sidePadding = getResources().getDimensionPixelSize(R.dimen.sidePadding);
        int topPadding = getResources().getDimensionPixelSize(R.dimen.topPadding);
        rv.addItemDecoration(new LevelDecoration(sidePadding, topPadding));

    }

    @Override
    protected void onResume() {
        if(mustFinish) {
            mustFinish = false;
            finish();
        }
        super.onResume();
    }

    public static void setMustFinish(boolean b) {
        mustFinish = b;
    }
}
