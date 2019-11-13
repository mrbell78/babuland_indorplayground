package com.example.babuland1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.babuland1.MainActivity;
import com.example.babuland1.R;
import com.example.babuland1.adapter.CustomAdapter_review;

public class ReviewActivity extends AppCompatActivity {


    int []img={R.drawable.bathroom,R.drawable.foodquality,R.drawable.pga,R.drawable.clean};
    String []name;
    RecyclerView recyclerView;
    Button btn_confirmReview;
    float toylet=0;
    float foodquality=0;
    float pgaservice = 0;
    float hyginic = 0;
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


        btn_confirmReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ReviewActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });


    }
}
