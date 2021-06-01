package com.example.myclassroomproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class WordFillActivity extends AppCompatActivity {

    private Map<String, String[]> wordShuffles;
    TextView textScreen, textQuestion, textTitle;
    Animation smallbigforth;
    ImageView image;
    TextView pageNumber;
    private int presCounter = 0;
    private int maxPresCounter;
    private ViewFlipper flipper;
    private int score = 0;
    ArrayList<Lesson> lessons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_fill);

        smallbigforth = AnimationUtils.loadAnimation(this, R.anim.smallbigforth);

        //instantiate views to be flipped
        lessons.add(new Lesson(R.drawable.duck, "DUCK", new String[]{"D", "U", "C", "K", "T", "Y", "H", "P"}));
        lessons.add(new Lesson(R.drawable.cat, "CAT", new String[]{"A", "T", "C", "K", "O", "D", "N", "V"}));
        lessons.add(new Lesson(R.drawable.cube, "CUBE", new String[]{"S", "U", "C", "Q", "A", "B", "E", "N"}));

        flipper = (ViewFlipper) findViewById(R.id.wordfillflipper);

        setFlipperContent();
        //for (Map.Entry<String, String[]> element : wordShuffles.entrySet()) {
        //    String word = element.getKey();
        //    element.setValue(shuffleArray(element.getValue()));


        //}

    }

    private void setFlipperContent() {

            int size = lessons.size();

            for (int i = 0; i < size; i++) {
                LayoutInflater inflater = (LayoutInflater) this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_word_game_template, null);

                //shuffle letters
                int lettersShuffled = 0;
                String[] hints = lessons.get(i).getNameShuffled();
                for (String letter : hints) {
                    if (lettersShuffled >= hints.length/2)
                        //Toast.makeText(WordFillActivity.this, ""+element.getValue().length, Toast.LENGTH_SHORT).show();
                        addView(((LinearLayout) view.findViewById(R.id.layoutParent2)), letter, lessons.get(i).getName(), ((EditText) view.findViewById(R.id.editText)));
                    else
                        addView(((LinearLayout) view.findViewById(R.id.layoutParent1)), letter, lessons.get(i).getName(), ((EditText) view.findViewById(R.id.editText)));

                    lettersShuffled++;

                }
                image = view.findViewById(R.id.wordpic);
                image.setImageResource(lessons.get(i).getImage());

                pageNumber = view.findViewById(R.id.npage);
                pageNumber.setText((i+1)+" / "+size);
                flipper.addView(view);

            }
    }


    private String[] shuffleArray(String[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }


    private void addView(LinearLayout viewParent, final String text, String wordAnswer, final EditText editText) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        linearLayoutParams.rightMargin = 30;

        final TextView textView = new TextView(this);

        textView.setLayoutParams(linearLayoutParams);
        textView.setBackground(this.getResources().getDrawable(R.drawable.bgedit));
        textView.setTextColor(this.getResources().getColor(R.color.white));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(32);


        //textQuestion = (TextView) findViewById(R.id.textQuestion);
        //textScreen = (TextView) findViewById(R.id.textScreen);
        //textTitle = (TextView) findViewById(R.id.textTitle);


        textView.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(presCounter < wordAnswer.length()) {
                    if (presCounter == 0)
                        editText.setText("");

                    editText.setText(editText.getText().toString() + text);
                    textView.startAnimation(smallbigforth);
                    textView.animate().alpha(0).setDuration(300);
                    presCounter++;

                    if (presCounter == wordAnswer.length()) {
                        doValidate(wordAnswer);
                        if (flipper.getDisplayedChild() < lessons.size()-1) {
                            flipper.showNext();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                        else {
                            Intent intent = new Intent(WordFillActivity.this, EndWordFillActivity.class);
                            intent.putExtra("score", score);
                            intent.putExtra("size", lessons.size());
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                }
            }
        });


        viewParent.addView(textView);


    }


    private void doValidate(String answer) {
        presCounter = 0;

        EditText editText = findViewById(R.id.editText);
        LinearLayout linearLayout = findViewById(R.id.layoutParent1);

        if(editText.getText().toString().equals(answer)) {
            Toast.makeText(WordFillActivity.this, "Correct", Toast.LENGTH_SHORT).show();

            //Intent a = new Intent(WordFillActivity.this, EndWordFillActivity.class);
            //startActivity(a);
            score++;
            editText.setText("");
        } else {
            Toast.makeText(WordFillActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
            editText.setText("");
        }

        /*wordShuffles.replace(answer, shuffleArray(wordShuffles.get(answer)));
        linearLayout.removeAllViews();
        for (String key : wordShuffles.get(answer)) {
            addView(linearLayout, key, answer, editText);
        }*/

    }

}