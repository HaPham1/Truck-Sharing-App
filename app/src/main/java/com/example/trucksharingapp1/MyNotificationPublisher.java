package com.example.trucksharingapp1;

import static com.example.trucksharingapp1.AddActivity.NOTIFICATION_CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyNotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String NOTIFICATION = "notification" ;
    public void onReceive (Context context , Intent intent) {

        //Get content from pending intent
        String content = intent.getStringExtra(NOTIFICATION);
        int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;
        //check the content
        Log.d("content received", content);

        //Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "default" ) ;
        builder.setContentTitle( "Your Delivery Date" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;

        //set notification channel and manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME2" , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert notificationManager != null;

        //notify
        notificationManager.notify(id , builder.build()) ;
    }
}

