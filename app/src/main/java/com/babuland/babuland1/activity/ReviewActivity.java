package com.babuland.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.babuland.babuland1.MainActivity;
import com.babuland.babuland1.adapter.CustomAdapter_review;
import com.babuland.babuland1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewActivity extends AppCompatActivity {


    int []img={R.drawable.foodquality,R.drawable.foodquality,R.drawable.pga,R.drawable.clean};
    String []name;
    RecyclerView recyclerView;
    Button btn_confirmReview;
    float toylet=0;
    float foodquality=0;
    float pgaservice = 0;
    float hyginic = 0;
    FirebaseUser mUser;
    DatabaseReference mDatabase;
    String userId;
    int nof_ptime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        btn_confirmReview=findViewById(R.id.btn_confirmm_review);
       name=getResources().getStringArray(R.array.name);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CustomAdapter_review adapter  = new CustomAdapter_review(this,img,name);
        recyclerView.setAdapter(adapter);


        mUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null){
            userId=mUser.getUid();
            mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(userId);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    nof_ptime=dataSnapshot.child("nof_purchase_time").getValue(Integer.class);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            Log.d("databse put/pull value", "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }

        btn_confirmReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nof_ptime=nof_ptime-1;
                mDatabase.child("nof_purchase_time").setValue(nof_ptime).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ReviewActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });

            }
        });


    }
}
