package com.example.trucksharingapp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.trucksharingapp1.data.DatabaseHelper2;
import com.example.trucksharingapp1.model.Order;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, DeliveryRecyclerAdapter.OnRowClickListener {
    ImageButton deliveryBtn;
    RecyclerView truckRecyclerView;
    String username;
    DeliveryRecyclerAdapter deliveryRecyclerAdapter;
    List<Order> orderList =new ArrayList<>();
    DatabaseHelper2 db2;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Get username
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        //Database
        db2 = new DatabaseHelper2(HomeActivity.this);
        orderList = db2.fetchAllOrders();

        //Delivery Recycler View set up
        truckRecyclerView = findViewById(R.id.truckRecyclerView);
        deliveryRecyclerAdapter = new DeliveryRecyclerAdapter(orderList, this, this);
        truckRecyclerView.setAdapter(deliveryRecyclerAdapter);

        layoutManager = new LinearLayoutManager(this);
        truckRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(truckRecyclerView.VERTICAL);



        //Button to create orders
        deliveryBtn = findViewById(R.id.deliveryBtn);
        deliveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(HomeActivity.this, AddActivity.class);
                addIntent.putExtra("username", username);
                startActivity(addIntent);
            }
        });
    }

    //Popup menu
    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    //Handle click on menu items
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item1:
                orderList = db2.fetchAllOrders();
                deliveryRecyclerAdapter = new DeliveryRecyclerAdapter(orderList, this, this);
                truckRecyclerView.setAdapter(deliveryRecyclerAdapter);
                return true;
            case R.id.item2:
                return false;
            case R.id.item3:
                orderList = db2.fetchSelectedOrders(username);
                deliveryRecyclerAdapter = new DeliveryRecyclerAdapter(orderList, this, this);
                truckRecyclerView.setAdapter(deliveryRecyclerAdapter);
                return true;
            default:
                return false;

        }
    }

    //Handle detail fragment for each item in RecyclerView
    @Override
    public void onItemClick(int position) {
        Fragment fragment;
        fragment = new DetailFragment();
        sendDatatoFragment(position, fragment);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment, "reset")
                .addToBackStack("reset").commit();

    }

    //Send data to display in Fragment
    public void sendDatatoFragment(int i, Fragment fragment) {
        //Send information
        Bundle bundle = new Bundle();
        Order order = orderList.get(i);
        bundle.putString("username", order.getUsername());
        bundle.putString("time", order.getTime());
        bundle.putString("receivername", order.getReceivername());
        bundle.putString("goodtype", order.getGoodtype());
        bundle.putString("vehicletype", order.getVehicletype());
        bundle.putString("weight", order.getWeight());
        bundle.putString("width", order.getWidth());
        bundle.putString("length", order.getLength());
        bundle.putString("height", order.getHeight());
        bundle.putByteArray("image", order.getImage());


        fragment.setArguments(bundle);
    }




}