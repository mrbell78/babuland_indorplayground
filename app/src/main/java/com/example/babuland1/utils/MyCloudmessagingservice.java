package com.example.babuland1.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.babuland1.activity.AccountSettingsActivity;
import com.example.babuland1.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyCloudmessagingservice extends FirebaseMessagingService {

    public static final String FIREBASER_TOKEN="myfcmtoken";



    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        storetokenfromservice(s);
        getApplicationContext().sendBroadcast(new Intent(FIREBASER_TOKEN ));
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("from", "onMessageReceived: --from "+ remoteMessage.getFrom());
        Log.d("notification", "onMessageReceived: --body "+ remoteMessage.getNotification().getBody());

        showNotification(remoteMessage.getFrom(),remoteMessage.getNotification().getBody());

    }



    private void storetokenfromservice(String s) {

        SharedprefManager.getInstance(getApplicationContext()).storeToken(s);
    }

    public void showNotification(String from,String body){
        NotificationManagerCompat notificationManagerCompat =  NotificationManagerCompat.from(this);


        Intent specialintent = new Intent(this, AccountSettingsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,specialintent,0);


        Notification notification = new NotificationCompat.Builder(this, Mynotification.CHANEL_1_ID)
                .setSmallIcon(R.drawable.babulandlogo)
                .setContentTitle(from)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.babulandlogo))
                .build();

        notificationManagerCompat.notify(1,notification);
    }
}
