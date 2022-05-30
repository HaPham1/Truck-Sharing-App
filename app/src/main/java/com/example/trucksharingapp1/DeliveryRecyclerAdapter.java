package com.example.trucksharingapp1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trucksharingapp1.model.Order;

import java.util.List;

//Recycler View for list of orders
public class DeliveryRecyclerAdapter extends RecyclerView.Adapter<DeliveryRecyclerAdapter.DeliveryAdapterHolder> {

    private List<Order> orderList;
    private Context context;
    private OnRowClickListener listener;


    public DeliveryRecyclerAdapter(List<Order> orderList, Context context, OnRowClickListener clickListener) {
        this.orderList = orderList;
        this.context = context;
        this.listener = clickListener;
    }

    public interface OnRowClickListener {
        void onItemClick(int position);
    }


    @NonNull
    @Override
    public DeliveryRecyclerAdapter.DeliveryAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.property_delivery, parent, false);
        return new DeliveryAdapterHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryRecyclerAdapter.DeliveryAdapterHolder holder, int position) {
        // set the Texts
        Order order = orderList.get(position);
        holder.vehicleText.setText(order.getVehicletype());
        holder.goodText.setText(order.getGoodtype());
        holder.timeText.setText(order.getTime());

        // Get image byte array then display by converting to bitmap
        byte[] image = order.getImage();
        if (image != null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.userImage.setImageBitmap(bitmap);
        }
        else{
            holder.userImage.setImageResource(R.drawable.ic_launcher_background);
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class DeliveryAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView userImage;
        public OnRowClickListener onRowClickListener;
        public TextView vehicleText, goodText, timeText;
        ImageButton imageButton;

        public DeliveryAdapterHolder(@NonNull View itemView, OnRowClickListener onRowClickListener) {
            super(itemView);

            //Initialize
            userImage = itemView.findViewById(R.id.imageView);
            vehicleText = itemView.findViewById(R.id.vehicleView);
            goodText = itemView.findViewById(R.id.goodView);
            timeText = itemView.findViewById(R.id.timeView);
            imageButton = itemView.findViewById(R.id.imageButton);

            //Share button function
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String Body = "Share this truck";
                    String Sub = "http://shareablelink.example.com";
                    intent.putExtra(Intent.EXTRA_TEXT, Body);
                    intent.putExtra(Intent.EXTRA_TEXT, Sub);
                    context.startActivity(Intent.createChooser(intent, "Share via"));
                }
            });


            this.onRowClickListener = onRowClickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onRowClickListener.onItemClick(getAdapterPosition());
        }
    }

}
