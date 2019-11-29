package com.example.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babuland1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ChildprofileeditActivity extends AppCompatActivity {



    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private String mUserid;

    private String status;

    TextView tv_childname,tv_childdob,tv_child_class,tv_childschool,tv_childgender;
    ImageView img_childpropic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        img_childpropic=findViewById(R.id.babypropicid);

        tv_childname=findViewById(R.id.tv_childname);
        tv_childdob=findViewById(R.id.childdob);
        tv_child_class=findViewById(R.id.childclass);
        tv_childschool=findViewById(R.id.childschool);
        tv_childgender=findViewById(R.id.child_gender);

        Intent intent = getIntent();
        status=intent.getStringExtra("userid");
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        mUserid=mUser.getUid();

        if(status!=null){
            mDatabase= FirebaseDatabase.getInstance().getReference().child("Childdb").child(mUserid).child(status);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){

                        String imguri = dataSnapshot.child("child_image").getValue().toString();
                        Picasso.with(getApplicationContext()).load(imguri).into(img_childpropic);

                        String name = dataSnapshot.child("child_name").getValue().toString();
                        tv_childname.setText(name);

                        String gender = dataSnapshot.child("child_gender").getValue().toString();
                        tv_childgender.setText(gender);


                        String dob= dataSnapshot.child("dob").getValue().toString();
                        tv_childdob.setText(dob);


                        String child_class = dataSnapshot.child("class").getValue().toString();
                        tv_child_class.setText(child_class);

                        String childschool = dataSnapshot.child("school").getValue().toString();
                        tv_childschool.setText(childschool);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }




    }
}
