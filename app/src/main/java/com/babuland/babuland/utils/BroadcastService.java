package com.babuland.babuland.utils;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class BroadcastService extends Service {

    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "com.babuland.babuland.utils.countdown_br";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;
    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "Starting timer...");


        cdt = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Log.i(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                bi.putExtra("countdown", millisUntilFinished/1000);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BroadcastService.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("timer", (int) (millisUntilFinished/1000));
                editor.apply();
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "Timer finished");
                sendBroadcast(bi);
            }
        };

        cdt.start();
    }

    @Override
    public void onDestroy() {
        //cdt.cancel();
        Log.i(TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
