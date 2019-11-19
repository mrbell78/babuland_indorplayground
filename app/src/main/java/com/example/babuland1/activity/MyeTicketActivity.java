package com.example.babuland1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.babuland1.R;
import com.example.babuland1.utils.DbHelper;
import com.squareup.picasso.Picasso;

public class MyeTicketActivity extends AppCompatActivity {

    Toolbar mToolbar;
    DbHelper db;
    ImageView imageView;
    String value ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mye_ticket);
         mToolbar=findViewById(R.id.toolbar);

         setSupportActionBar(mToolbar);
         db=new DbHelper(this);
         getSupportActionBar().setTitle("My eTicket");
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         Intent intent = getIntent();

         String name = intent.getStringExtra("name");

         imageView=findViewById(R.id.imageview);

        Picasso.with(getApplicationContext()).load(name).into(imageView);
    }
}
