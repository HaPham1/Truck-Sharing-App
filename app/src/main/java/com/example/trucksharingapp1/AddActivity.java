package com.example.trucksharingapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

public class AddActivity extends AppCompatActivity {
    CalendarView calendarView;
    TextView dateView;
    Button nextBtn;
    EditText receiveName, timeText, locationText;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");


        dateView = findViewById(R.id.dateView);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        nextBtn = findViewById(R.id.nxtBtn);

        receiveName = findViewById(R.id.receiveName);
        timeText = findViewById(R.id.timeText);
        locationText = findViewById(R.id.locationText);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = i2 +"/" + (i1 + 1) + "/" + i;
                dateView.setText(date);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(AddActivity.this, AddActivity2.class);
                addIntent.putExtra("username", username);
                addIntent.putExtra("receivername", receiveName.getText().toString());
                addIntent.putExtra("date", dateView.getText().toString());
                addIntent.putExtra("time", timeText.getText().toString());
                addIntent.putExtra("location", locationText.getText().toString());
                startActivity(addIntent);

            }
        });
    }
}