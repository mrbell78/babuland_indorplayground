package com.example.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babuland1.R;
import com.example.babuland1.adapter.TicketAdapter;
import com.example.babuland1.model.Model_ticket;
import com.example.babuland1.utils.DbHelper;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class MyeTicketActivity extends AppCompatActivity {

    Toolbar mToolbar;
    DbHelper db;
    ImageView imageView;
    String value ;
    Cursor cursor;
    TextView tv;
    List<Model_ticket> list;
    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    FirebaseUser mUser;
    String userId;
    String status;

    public static final String DEFAULT_DRIVER="oracle.jdbc.driver.OracleDriver";
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@itlimpex.ddns.net:2121:xe";
    private static final String DEFAULT_USERNAME = "bland";
    private static final String DEFAULT_PASSWORD = "servicepack3";

    private Connection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mye_ticket);
         mToolbar=findViewById(R.id.toolbar);

         setSupportActionBar(mToolbar);
         db=new DbHelper(this);
         getSupportActionBar().setTitle("My eTicket");
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(mUser!=null){
            userId=mUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);



        }else{
            Log.d("user", "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }

        list=new ArrayList<>();
         int count = db.count();


         showdatafromsqdb();
         //showdatafromapexdb();

       TicketAdapter adapter = new TicketAdapter(this,list);

       recyclerView.setLayoutManager(new LinearLayoutManager(this));
       recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        Log.d("count", "onCreate: ------------------------------total data count "+ count);



    }

    private void showdatafromsqdb() {


        cursor=db.pulldata();
        recyclerView=findViewById(R.id.ticketrecylerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
