package com.example.trucksharingapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.trucksharingapp1.data.DatabaseHelper;
import com.example.trucksharingapp1.data.DatabaseHelper2;
import com.example.trucksharingapp1.model.Order;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class AddActivity2 extends AppCompatActivity {
    EditText goodText, vehicleText, weightText, widthText, lengthText, heightText;
    RadioButton Furniture, Drygoods, Food, Building, truck, van, refrigeratedTruck, miniTruck;
    Button orderBtn;
    String username, receivername, date, time, location;
    DatabaseHelper2 db2;
    DatabaseHelper db;
    byte[] image;
    long delay;

    public static final String NOTIFICATION_CHANNEL_ID = "10002" ;
    private final static String default_notification_channel_id = "default" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add2);
        initWidgets();

        //Intent to get the data from previous creation page
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        receivername = intent.getStringExtra("receivername");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        location = intent.getStringExtra("location");
        delay = intent.getLongExtra("calendar", 0);


        // Create database helper to utilize databases.
        db2 = new DatabaseHelper2(this);
        db = new DatabaseHelper(this);

        //Get image from user database
        image = db.fetchImage(username);

        //order button insert order into database
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goodtype = goodText.getText().toString();
                String weight = weightText.getText().toString();
                String width = widthText.getText().toString();
                String length = lengthText.getText().toString();
                String height = heightText.getText().toString();
                String vehicletype = vehicleText.getText().toString();
                long result = db2.insertOrder(new Order(username, receivername, date, time, location, goodtype, weight, width, length, height, vehicletype, image));
                if (result > 0)
                {
                    scheduleNotification(date , delay) ;
                    Toast.makeText(AddActivity2.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                    Intent addIntent = new Intent(AddActivity2.this, HomeActivity.class);
                    addIntent.putExtra("username", username);
                    startActivity(addIntent);
                    finish();
                }
                else {
                    Toast.makeText(AddActivity2.this, "Registration error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Initialize
    private void initWidgets() {
        goodText = findViewById(R.id.goodText);
        Furniture = findViewById(R.id.Furniture);
        Drygoods = findViewById(R.id.Drygoods);
        Food = findViewById(R.id.Food);
        Building = findViewById(R.id.Building);
        vehicleText = findViewById(R.id.vehicleText);
        truck = findViewById(R.id.truck);
        van = findViewById(R.id.van);
        refrigeratedTruck = findViewById(R.id.refrigeratedTruck);
        miniTruck = findViewById(R.id.miniTruck);

        weightText = findViewById(R.id.weightText);
        widthText = findViewById(R.id.widthText);
        lengthText = findViewById(R.id.lengthText);
        heightText = findViewById(R.id.heightText);
        orderBtn = findViewById(R.id.orderBtn);

    }

    //Function to handle 2 radio widgets.
    public void radioTapped(View view) {
        switch (view.getId()) {
            case R.id.Furniture:
                goodText.setText(Furniture.getText());
                break;
            case R.id.Drygoods:
                goodText.setText(Drygoods.getText());
                break;
            case R.id.Food:
                goodText.setText(Food.getText());
                break;
            case R.id.Building:
                goodText.setText(Building.getText());
                break;
            case R.id.truck:
                vehicleText.setText(truck.getText());
                break;
            case R.id.van:
                vehicleText.setText(van.getText());
                break;
            case R.id.refrigeratedTruck:
                vehicleText.setText(refrigeratedTruck.getText());
                break;
            case R.id.miniTruck:
                vehicleText.setText(miniTruck.getText());
                break;
            default:
                break;
        }
    }

    //Function to schedule notification
    private void scheduleNotification (String content , long delay) {
        Intent notificationIntent = new Intent( getApplicationContext(), MyNotificationPublisher.class ) ;
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID , 0 ) ;
        Log.d("Content sent", content);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION , content) ;
        PendingIntent pendingIntent = PendingIntent.getBroadcast( this, 0 , notificationIntent , PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE ) ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager. RTC_WAKEUP , delay , pendingIntent);
        }
        else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager. RTC_WAKEUP , delay , pendingIntent);
        }
        else {
            alarmManager.set(AlarmManager. RTC_WAKEUP , delay , pendingIntent);
        }
        Boolean something =alarmManager.canScheduleExactAlarms();
        Log.d("Alarm", String.valueOf(something));
    }
}