package com.example.trucksharingapp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trucksharingapp1.data.DatabaseHelper;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton, signupButton;
    DatabaseHelper db;
    OneTimeWorkRequest backgroundWorkRequest;
    PeriodicWorkRequest checkWorkRequest;
    Boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize
        usernameEditText = findViewById(R.id.usereditText);
        passwordEditText = findViewById(R.id.passeditText);
        loginButton = findViewById(R.id.loginBtn);
        signupButton = findViewById(R.id.signupBtn);

        //Create database
        db = new DatabaseHelper(this);

        //Create Periodic Work Request and One Time Work Request
        checkWorkRequest = new PeriodicWorkRequest.Builder(BackgroundWorker.class,
                15, TimeUnit.MINUTES, 5, TimeUnit.MINUTES).addTag("intervalcheck").build();

        backgroundWorkRequest = new OneTimeWorkRequest.Builder(BackgroundWorker.class).addTag("onecheck").build();

        //Enqueue Periodic Work and handle Periodic Work every 15 minutes
        WorkManager.getInstance().enqueueUniquePeriodicWork("intervalcheck",
                ExistingPeriodicWorkPolicy.REPLACE ,checkWorkRequest);

        WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(checkWorkRequest.getId())
                .observe(MainActivity.this, info -> {
                        openRatingDialog();
                });



        //Login function, allow login if username and password correct, and user has access to internet
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance(getApplicationContext()).beginUniqueWork("onecheck", ExistingWorkPolicy.REPLACE, backgroundWorkRequest).enqueue();
                WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(backgroundWorkRequest.getId())
                        .observe(MainActivity.this, info -> {
                            if (info != null && info.getState().isFinished()) {
                                state = info.getOutputData().getBoolean("state",
                                        false);
                                if (state) {
                                    boolean result = db.fetchUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                                    if (result) {
                                        Toast.makeText(MainActivity.this, "Successfully logged in ", Toast.LENGTH_SHORT).show();
                                        Intent signupIntent = new Intent(MainActivity.this, HomeActivity.class);
                                        signupIntent.putExtra("username", usernameEditText.getText().toString());
                                        startActivity(signupIntent);
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "The user does not exist.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    openDialog();
                                }
                            }
                        });
            }
        });

        // Button for signing up
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signupIntent);
            }
        });

    }

    //Dialog for work request
    public void openDialog() {
        PopupDialog popupDialog = new PopupDialog();
        popupDialog.show(getSupportFragmentManager(), "reminder dialog");
    }

    public void openRatingDialog() {
        RatingDialog ratingDialog = new RatingDialog();
        ratingDialog.show(getSupportFragmentManager(), "rating dialog");
    }
}