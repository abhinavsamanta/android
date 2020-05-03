package com.example.firebasenoteapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG ="NOTIFICATION";

    NotificationManager notificationManager;
    Notification notification;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification()!=null){
            String title =remoteMessage.getNotification().getTitle();
            String message=remoteMessage.getNotification().getBody();
            generatenote(title,message);
        //    Toast.makeText(this, "notificationreceived", Toast.LENGTH_SHORT).show();

            Log.e(TAG, "onMessageReceived: is "+title+" "+message );
        }
    }

    public void generatenote(String notetile,String notemsg){

        Intent intent=new Intent(MessagingService.this,MainActivity.class);

        PendingIntent pendingIntent=PendingIntent.getActivity(MessagingService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmsound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if(Build.VERSION.SDK_INT>=26){

            String channelId="com.example.firebasenoteapp";
            String channelName="FCMDemo";

            NotificationChannel notificationChannel=new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            assert notificationManager!=null;
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationCompat.Builder notificationbuilder=new NotificationCompat.Builder(MessagingService.this,channelId);
            notificationbuilder.setOngoing(true).setSmallIcon(R.mipmap.ic_launcher).setColor(Color.BLUE)
                    .setAutoCancel(true).setTicker(notemsg).setContentTitle(notetile).setContentText(notemsg)
                    .setDefaults(Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS)
                    .setContentIntent(pendingIntent);

            notification=notificationbuilder.build();

        }else{
            Notification.Builder nb=new Notification.Builder(MessagingService.this);
            nb.setSmallIcon(R.mipmap.ic_launcher).setContentText(notemsg).setContentTitle(notetile)
                    .setSound(alarmsound).setContentIntent(pendingIntent);
                    nb.build();

                    notification=nb.getNotification();
                    notification.flags=Notification.FLAG_AUTO_CANCEL;


        }

        if(Build.VERSION.SDK_INT>=26){
            startForeground(0,notification);
        }else {
            notificationManager.notify(0,notification);
        }



    }
}
