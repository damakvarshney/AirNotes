package com.application.airnotes;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class CustomMessagingService extends FirebaseMessagingService {
    NotificationManager notificationManager;
    Notification notification;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification()!=null){
            String title = remoteMessage.getNotification().getTitle();
            String messageBody = remoteMessage.getNotification().getBody();
            generateNotification(title,messageBody);

            Log.e("Notification","Messaging :"+title+messageBody );
        }
    }

    public void generateNotification(String noteTitle,String noteMsg){
        Intent intent = new Intent(this,main_screen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0,intent,PendingIntent.FLAG_UPDATE_CURRENT );
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        //Building a channel
        if(Build.VERSION.SDK_INT>=26){
            String channelId = "com.application.airnotes";
            String channelName = "FCMDemo";

            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationChannel.setLightColor(Color.BLUE);

            assert notificationManager!=null;

            notificationManager.createNotificationChannel(notificationChannel);
            Context context;
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
            notificationBuilder.setContentIntent(pendingIntent).setContentTitle(noteTitle).setOngoing(true).setSmallIcon(R.drawable.navigation_icon).setColor(Color.BLUE).setAutoCancel(true).setTicker(noteMsg).setSound(alarmSound).setDefaults(Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS).setContentText(noteMsg);

            notification = notificationBuilder.build();
        }else {

            Notification.Builder nb  = new Notification.Builder(this);
            nb.setContentIntent(pendingIntent).setContentTitle(noteTitle).setOngoing(true).setSmallIcon(R.drawable.navigation_icon).setAutoCancel(true).setTicker(noteMsg).setSound(alarmSound).setDefaults(Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS).setContentText(noteMsg).build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
        }

        //Sending notification
        if(Build.VERSION.SDK_INT>=26){
            startForeground(0,notification);
        }else {
            notificationManager.notify(0,notification);
        }
    }
}
