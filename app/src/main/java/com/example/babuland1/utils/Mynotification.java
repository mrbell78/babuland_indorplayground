package com.example.babuland1.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Mynotification extends Application {

    public static final String CHANEL_1_ID = "chanel1";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChanel();
    }

    private void createNotificationChanel() {

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

                NotificationChannel chanel_1 = new NotificationChannel(CHANEL_1_ID,"Chanel 1", NotificationManager.IMPORTANCE_HIGH);

                chanel_1.setDescription("Babuland");
                chanel_1.canShowBadge();


                NotificationManager manager = getSystemService(NotificationManager.class);

                manager.createNotificationChannel(chanel_1);
            }



    }
}
