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

import java.util.Calendar;


public class AddActivity extends AppCompatActivity {
    CalendarView calendarView;
    TextView dateView;
    Button nextBtn;
    EditText receiveName, timeText, locationText;
    String username;

    public static final String NOTIFICATION_CHANNEL_ID = "10002" ;
    final Calendar myCalendar = Calendar. getInstance () ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //Get username from previous intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        //Initialize
        dateView = findViewById(R.id.dateView);
        calendarView = findViewById(R.id.calendarView);
        nextBtn = findViewById(R.id.nxtBtn);
        receiveName = findViewById(R.id.receiveName);
        timeText = findViewById(R.id.timeText);
        locationText = findViewById(R.id.locationText);

        // Function to set text by clicking on calender view, also set calendar
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = i2 +"/" + (i1 + 1) + "/" + i;
                dateView.setText(date);
                myCalendar .set(Calendar. YEAR , i) ;
                myCalendar .set(Calendar. MONTH , i1) ;
                myCalendar .set(Calendar. DAY_OF_MONTH , i2) ;
            }
        });

        // Next function, send data to next order page for insertion into database.
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(AddActivity.this, AddActivity2.class);
                addIntent.putExtra("username", username);
                addIntent.putExtra("receivername", receiveName.getText().toString());
                addIntent.putExtra("date", dateView.getText().toString());
                addIntent.putExtra("time", timeText.getText().toString());
                addIntent.putExtra("location", locationText.getText().toString());
                addIntent.putExtra("calendar", myCalendar.getTimeInMillis());
                startActivity(addIntent);

            }
        });
    }




}