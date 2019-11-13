package com.example.babuland1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.babuland1.R;

public class MyeTicketActivity extends AppCompatActivity {

    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mye_ticket);
         mToolbar=findViewById(R.id.toolbar);

         setSupportActionBar(mToolbar);

         getSupportActionBar().setTitle("My eTicket");
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
