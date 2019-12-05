package com.babuland.babuland1.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Myapp extends Application {

    public static final String CHANEL_ID="notificationChanel";

    @Override
    public void onCreate() {
        super.onCreate();

        CretenotificationChanel();

    }

    private void CretenotificationChanel() {

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                NotificationChannel chanel = new NotificationChannel(CHANEL_ID,"pushnotification", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(chanel);
            }
    }
}
