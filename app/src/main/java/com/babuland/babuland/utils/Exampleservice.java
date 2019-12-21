package com.babuland.babuland.utils;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class Exampleservice extends JobIntentService {
    int minitues=0;
    int seconds=0;

    private CountDownTimer mcountDownTimer;
    private static final long START_TIME_INMILIS=10000;
    private long mTimerinleft_inmils= START_TIME_INMILIS;
    private static final String TAG = "Exampleservice";

    public static void enqueueWork(Context context, Intent work){
        enqueueWork(context,Exampleservice.class,123,work);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ---------------------------on create is called");
        mcountDownTimer=new CountDownTimer(mTimerinleft_inmils,1000) {
            @Override
            public void onTick(long l) {
                mTimerinleft_inmils=l;
                Updatetext();
                Log.d(TAG, "onTick: -------------------timer is running in srvice "+seconds);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork: --------------------------------------service is runing ");
        Log.d(TAG, "onHandleWork: "+mTimerinleft_inmils);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: --------------------------------------------service is finished");
    }

    @Override
    public boolean onStopCurrentWork() {
        return super.onStopCurrentWork();

    }
    public void Updatetext(){
         minitues = (int) ((mTimerinleft_inmils/1000)/60);
         seconds = (int) ((mTimerinleft_inmils/1000)%60);
    }
}
