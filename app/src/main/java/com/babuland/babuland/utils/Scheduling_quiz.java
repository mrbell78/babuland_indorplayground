package com.babuland.babuland.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.babuland.babuland.MainActivity.NOTIFICATION_CHANNEL_ID;

    public class Scheduling_quiz extends BroadcastReceiver {

        public static String NOTIFICATION_ID = "notification-id" ;
        public static String NOTIFICATION = "notification" ;
        @Override
        public void onReceive(Context context, Intent intent) {


            FirebaseUser mUser;
            DatabaseReference mDatabase,leaderboard_database;
            String userId;

            leaderboard_database=FirebaseDatabase.getInstance().getReference().child("Leaderboard");
            leaderboard_database.removeValue();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
            Notification notification = intent.getParcelableExtra( NOTIFICATION ) ;
            if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
                int importance = NotificationManager. IMPORTANCE_HIGH ;
                NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME" , importance) ;
                assert notificationManager != null;
                notificationManager.createNotificationChannel(notificationChannel) ;
            }
            int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;
            assert notificationManager != null;
            notificationManager.notify(id , notification) ;

            mUser= FirebaseAuth.getInstance().getCurrentUser();
            if(mUser!=null) {
                userId = mUser.getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                mDatabase.child("status_quz").setValue("active");
            }


        }

    }
