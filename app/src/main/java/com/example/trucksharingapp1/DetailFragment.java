package com.example.trucksharingapp1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    ImageView image;
    TextView userdetailView, timedetailView, receivedetailView, dropoffView;
    TextView gooddesView, gooddetailView, vehicledetailView;
    TextView weightdetailView, widthdetailView, lengthdetailView, heightdetailView;
    Button getBtn;
    byte[] imagearray;
    String username, time, receivername, goodtype, vehicletype, weight, width, length, height;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        image = view.findViewById(R.id.imageView2);
        userdetailView = view.findViewById(R.id.userdetailView);
        timedetailView = view.findViewById(R.id.timedetailView);
        receivedetailView = view.findViewById(R.id.receivedetailView);
        dropoffView = view.findViewById(R.id.dropoffView);
        gooddesView = view.findViewById(R.id.gooddesView);
        gooddetailView = view.findViewById(R.id.gooddetailView);
        vehicledetailView = view.findViewById(R.id.vehicledetailView);
        weightdetailView = view.findViewById(R.id.weightdetailView);
        widthdetailView = view.findViewById(R.id.widthdetailView);
        lengthdetailView = view.findViewById(R.id.lengthdetailView);
        heightdetailView = view.findViewById(R.id.heightdetailView);
        getBtn = view.findViewById(R.id.getBtn);

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(getActivity(), MapActivity.class);
                mapIntent.putExtra("username",username);
                startActivity(mapIntent);
            }
        });

        //Get information
        Bundle bundle = this.getArguments();
        imagearray = bundle.getByteArray("image");
        username = bundle.getString("username");
        time = bundle.getString("time");
        //int dropoffTime = Integer.valueOf(time) + 3;
        receivername = bundle.getString("receivername");
        goodtype = bundle.getString("goodtype");
        vehicletype = bundle.getString("vehicletype");
        weight = bundle.getString("weight");
        width = bundle.getString("width");
        length = bundle.getString("length");
        height = bundle.getString("height");

        //Display information
        if (imagearray != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagearray, 0, imagearray.length);
            image.setImageBitmap(bitmap);
        }
        else {
            image.setImageResource(R.drawable.ic_launcher_background);
        }

        userdetailView.setText("From sender " + username);
        timedetailView.setText("Pick up time " + time);
        receivedetailView.setText("To receiver " + receivername);
        dropoffView.setText("Drop off time " + time);
        gooddetailView.setText("Good type: \n " + goodtype);
        vehicledetailView.setText("Vehicle type: \n " + vehicletype);
        weightdetailView.setText("Weight: \n" + weight + " kg");
        widthdetailView.setText("Width: \n" + width + "m");
        lengthdetailView.setText("Length: \n" + length + "m");
        heightdetailView.setText("Height \n" + height + "m");




        return view;
    }
}