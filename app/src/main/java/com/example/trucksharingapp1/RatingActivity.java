package com.example.trucksharingapp1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity {
    private ImageView ratingImage;
    private RatingBar ratingBar;
    private EditText feedbackMultiLine;
    private TextView rateText;
    private ImageButton micBtn;
    private Button rateBtn;
    private static int RESULT_SPEECH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        //Initialize
        ratingImage = findViewById(R.id.ratingImage);
        ratingBar = findViewById(R.id.ratingBar);
        feedbackMultiLine = findViewById(R.id.feedbackMultiLine);
        micBtn = findViewById(R.id.micBtn);
        rateBtn = findViewById(R.id.rateBtn);
        rateText = findViewById(R.id.rateText);
        ratingImage.setImageResource(R.drawable.rate);

        //Function for rate button
        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
            }
        });

        //Function for Speech-to-Text button
        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent speakIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speakIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speakIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                try {
                    startActivityForResult(speakIntent, RESULT_SPEECH);
                }
                catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


        //Settings for rating bar
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating == 0 || rating == 0.5 || rating == 1)
                {
                    ratingImage.setImageResource(R.drawable.catangry);
                    rateText.setText("Very Dissatisfied");
                }
                else if(rating == 2 || rating == 1.5)
                {
                    ratingImage.setImageResource(R.drawable.catsad);
                    rateText.setText("Dissatisfied");
                }
                else if(rating == 3 || rating == 2.5)
                {
                    ratingImage.setImageResource(R.drawable.catneutral);
                    rateText.setText("OK");
                }
                else if(rating == 4 || rating == 3.5)
                {
                    ratingImage.setImageResource(R.drawable.cathappy);
                    rateText.setText("Satisfied");
                }
                else if(rating == 5 || rating == 4.5)
                {
                    ratingImage.setImageResource(R.drawable.catlove);
                    rateText.setText("Very Satisfied");
                }
                else
                {

                }
            }
        });
    }
    //Handle result from Speech to text
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    feedbackMultiLine.setText(text.get(0));
                }
                break;
        }
    }
}