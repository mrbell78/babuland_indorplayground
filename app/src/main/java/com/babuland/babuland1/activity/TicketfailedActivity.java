package com.babuland.babuland1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.babuland.babuland1.R;

public class TicketfailedActivity extends AppCompatActivity {

    TextView tv_orderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketfailed);

        tv_orderid=findViewById(R.id.orderid);

        Intent intent =getIntent();

        int orderid=intent.getIntExtra("orderid",0);

        tv_orderid.setText("452315781434tsl44");
    }
}
