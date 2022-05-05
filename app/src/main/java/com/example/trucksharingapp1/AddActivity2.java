package com.example.trucksharingapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.trucksharingapp1.data.DatabaseHelper;
import com.example.trucksharingapp1.data.DatabaseHelper2;
import com.example.trucksharingapp1.model.Order;

public class AddActivity2 extends AppCompatActivity {
    EditText goodText, vehicleText, weightText, widthText, lengthText, heightText;
    RadioButton Furniture, Drygoods, Food, Building, truck, van, refrigeratedTruck, miniTruck;
    Button orderBtn;
    String username, receivername, date, time, location;
    DatabaseHelper2 db2;
    DatabaseHelper db;
    byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add2);
        initWidgets();

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        receivername = intent.getStringExtra("receivername");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        location = intent.getStringExtra("location");

        db2 = new DatabaseHelper2(this);
        db = new DatabaseHelper(this);

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String goodtype = goodText.getText().toString();
                String weight = weightText.getText().toString();
                String width = widthText.getText().toString();
                String length = lengthText.getText().toString();
                String height = heightText.getText().toString();
                String vehicletype = vehicleText.getText().toString();
                image = db.fetchImage(username);
                long result = db2.insertOrder(new Order(username, receivername, date, time, location, goodtype, weight, width, length, height, vehicletype, image));
                if (result > 0)
                {
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
}