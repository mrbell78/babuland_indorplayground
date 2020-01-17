package com.babuland.babuland.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScheduledQuiz_stop extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        FirebaseUser mUser;
        final DatabaseReference mDatabase,admindatabase,leaderboard_database;
        String userId;


        mUser= FirebaseAuth.getInstance().getCurrentUser();
        leaderboard_database=FirebaseDatabase.getInstance().getReference().child("Leaderboard");
        leaderboard_database.removeValue();
        if(mUser!=null) {
            userId = mUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

            mDatabase.child("status_quz").setValue("inactive");
            admindatabase=FirebaseDatabase.getInstance().getReference().child("Admin");

            admindatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String value = dataSnapshot.child("quiznumber").getValue().toString();
                        DatabaseReference mdatabase_broadcast;

                        if(value.equals("1")){
                            value="2";
                            mdatabase_broadcast= FirebaseDatabase.getInstance().getReference().child("Admin");
                            mdatabase_broadcast.child("quiznumber").setValue(value);
                            mdatabase_broadcast.child("update_quiznumber").setValue("1");
                        }else if(value.equals("2")){
                            value="3";
                            mdatabase_broadcast= FirebaseDatabase.getInstance().getReference().child("Admin");
                            mdatabase_broadcast.child("quiznumber").setValue(value);
                            mdatabase_broadcast.child("update_quiznumber").setValue("2");
                        }

                        else if(value.equals("3")){
                            value="4";
                            mdatabase_broadcast= FirebaseDatabase.getInstance().getReference().child("Admin");
                            mdatabase_broadcast.child("quiznumber").setValue(value);
                            mdatabase_broadcast.child("update_quiznumber").setValue("3");
                        }

                        else if(value.equals("4")){
                            value="5";
                            mdatabase_broadcast= FirebaseDatabase.getInstance().getReference().child("Admin");
                            mdatabase_broadcast.child("quiznumber").setValue(value);
                            mdatabase_broadcast.child("update_quiznumber").setValue("4");
                        }
                        else if(value.equals("5")){
                            value="6";
                            mdatabase_broadcast= FirebaseDatabase.getInstance().getReference().child("Admin");
                            mdatabase_broadcast.child("quiznumber").setValue(value);
                            mdatabase_broadcast.child("update_quiznumber").setValue("5");
                        }
                        else if(value.equals("6")){
                            value="7";

                            mdatabase_broadcast= FirebaseDatabase.getInstance().getReference().child("Admin");
                            mdatabase_broadcast.child("quiznumber").setValue(value);
                            mdatabase_broadcast.child("update_quiznumber").setValue("6");
                        }
                        else if(value.equals("7")){
                            value="8";

                            mdatabase_broadcast= FirebaseDatabase.getInstance().getReference().child("Admin");
                            mdatabase_broadcast.child("quiznumber").setValue(value);
                            mdatabase_broadcast.child("update_quiznumber").setValue("7");
                        }
                        else if(value.equals("8")){
                            value="9";

                            mdatabase_broadcast= FirebaseDatabase.getInstance().getReference().child("Admin");
                            mdatabase_broadcast.child("quiznumber").setValue(value);
                            mdatabase_broadcast.child("update_quiznumber").setValue("8");
                        }
                        else if(value.equals("9")){
                            value="10";

                            mdatabase_broadcast= FirebaseDatabase.getInstance().getReference().child("Admin");
                            mdatabase_broadcast.child("quiznumber").setValue(value);
                            mdatabase_broadcast.child("update_quiznumber").setValue("9");
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
