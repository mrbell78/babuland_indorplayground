package com.babuland.babuland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.babuland.babuland.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BabulandpointsActivity extends AppCompatActivity {


    DatabaseReference mDatabase;
    FirebaseUser mUser;
    String mUserid;

    TextView tv_babulandpoints,tv_accountprogress,tv_getprogress;
    ProgressBar bland_progress,account_progress;

    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babulandpoints);


        mToolbar=findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        //db=new DbHelper_freeTicket(this);
        getSupportActionBar().setTitle("Redeem Points");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_babulandpoints=findViewById(R.id.tv_blandpoints);
        bland_progress=findViewById(R.id.myprogressbar_points);
        account_progress=findViewById(R.id.myprogressbar);
        tv_accountprogress=findViewById(R.id.tv_percentage);
        tv_getprogress=findViewById(R.id.tv_getprogress);


        tv_getprogress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AccountSettingsActivity.class));

            }
        });

        mUser= FirebaseAuth.getInstance().getCurrentUser();
        mUserid=mUser.getUid();
        if(mUserid!=null){


            mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(mUserid);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("BabulandPoints").exists()){

                        int points = dataSnapshot.child("BabulandPoints").getValue(Integer.class);

                        int picup=dataSnapshot.child("pic_up").getValue(Integer.class);
                        int name_up=dataSnapshot.child("name_up").getValue(Integer.class);
                        int number_up = dataSnapshot.child("number_up").getValue(Integer.class);
                        int dob_up = dataSnapshot.child("dob_up").getValue(Integer.class);
                        int mail_up= dataSnapshot.child("mail_up").getValue(Integer.class);
                        int address_up =dataSnapshot.child("address_up").getValue(Integer.class);

                        int total_progress = picup+name_up+number_up+dob_up+mail_up+address_up;


                        if(points>=0){
                            tv_babulandpoints.setText(Integer.toString(points)+"Points");
                            bland_progress.setProgress(points);
                        }


                        account_progress.setProgress(total_progress);
                        tv_accountprogress.setText(Integer.toString(total_progress)+"%");


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
