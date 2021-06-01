package com.example.myclassroomproject;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    public CardView numbers, alphabet, animals, shapes, fruits, veggies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        numbers = (CardView)findViewById(R.id.inumbers);
        alphabet = (CardView)findViewById(R.id.ialphabet);
        animals = (CardView)findViewById(R.id.ianimals);
        shapes = (CardView)findViewById(R.id.ishapes);
        fruits = (CardView)findViewById(R.id.ifruits);
        veggies = (CardView)findViewById(R.id.iveggies);

        numbers.setOnClickListener(this);
        alphabet.setOnClickListener(this);
        animals.setOnClickListener(this);
        shapes.setOnClickListener(this);
        fruits.setOnClickListener(this);
        veggies.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, IntermediateDashboard.class);

        View sharedView = v.findViewWithTag("themeImage");
        String transitionName = getString(R.string.themeName);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, sharedView, transitionName);

        i.putExtra("theme", v.getTag().toString());
        startActivity(i, options.toBundle());
        /*switch (v.getId()){
            case R.id.ialphabet:
                i = new Intent(this, alphabet.class);
                startActivity(i);
                break;
            case R.id.ianimals:
                i = new Intent(this, animals.class);
                startActivity(i);
                break;
            case R.id.ifruits:
                i = new Intent(this, fruits.class);
                startActivity(i);
                break;
            case R.id.ishapes:
                i = new Intent(this, shapes.class);
                startActivity(i);
                break;
            case R.id.iveggies:
                i = new Intent(this, veggies.class);
                startActivity(i);
                break;
            case R.id.inumbers:
                i = new Intent(this, numbers.class);
                startActivity(i);
                break;
        }*/
    }
}