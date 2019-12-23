package com.babuland.babuland.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScheduledQuiz_stop extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        FirebaseUser mUser;
        DatabaseReference mDatabase;
        String userId;

        mUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null) {
            userId = mUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

            mDatabase.child("status_quz").setValue("inactive");
        }
    }
}
