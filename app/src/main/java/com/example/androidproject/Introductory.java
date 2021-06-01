package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import com.example.myclassroomproject.R;


public class Introductory extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;

        Button btStart;
        ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);

        progressDialog = new ProgressDialog(Introductory.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        //splash screen
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), GameMainActivity.class);
            intent.putExtra("topic", getIntent().getExtras().getString("topic"));
            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);

    }

    @Override
    public void onBackPressed() {
        progressDialog.dismiss();
    }
}