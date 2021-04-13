package com.example.myclassroomproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

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
        Intent i;

        switch (v.getId()){
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
        }
    }
}