package com.babuland.babuland.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.babuland.babuland.R;

public class TicketfailedActivity extends AppCompatActivity {

    TextView tv_orderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketfailed);

        tv_orderid=findViewById(R.id.orderid);

        Intent intent =getIntent();

        int orderid=intent.getIntExtra("orderid",0);

        tv_orderid.setText(orderid);
    }
}
