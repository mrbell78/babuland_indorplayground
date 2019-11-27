package com.example.babuland1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.babuland1.R;
import com.example.babuland1.adapter.TicketAdapter;
import com.example.babuland1.model.Model_ticket;
import com.example.babuland1.utils.DbHelper;
import com.example.babuland1.utils.DbHelper_freeTicket;

import java.util.ArrayList;
import java.util.List;

public class MyFreeTicketActivity extends AppCompatActivity {


    Toolbar mToolbar;
    DbHelper_freeTicket db;
    ImageView imageView;
    String value ;
    Cursor cursor;
    TextView tv;
    List<Model_ticket> list;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_free_ticket);

        mToolbar=findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        db=new DbHelper_freeTicket(this);
        getSupportActionBar().setTitle("My Free Ticket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.ticketrecylerview_free);

        list=new ArrayList<>();
        int count = db.count_free();



        showdatafromsqdb();
        //showdatafromapexdb();

        TicketAdapter adapter = new TicketAdapter(this,list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private void showdatafromsqdb() {


        cursor=db.pulldata_free();




        if(cursor!=null){

            if(cursor.moveToFirst()){
                do{
                    final Model_ticket model_ticket = new Model_ticket();
                    model_ticket.setTime(cursor.getString(1));
                    model_ticket.setTotal(cursor.getInt(2));
                    model_ticket.setOrderid(cursor.getInt(3));
                    model_ticket.setStatus(cursor.getString(4));
                    model_ticket.setBranchname(cursor.getString(5));

                    list.add(model_ticket);
                }while (cursor.moveToNext());
            }
        }
    }
}
