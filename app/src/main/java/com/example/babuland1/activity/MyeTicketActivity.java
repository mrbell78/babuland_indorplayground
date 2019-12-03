package com.example.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.babuland1.model.Model_ticket;
import com.example.babuland1.R;

import com.example.babuland1.utils.DbHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Connection;
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

    private FirebaseRecyclerOptions<Model_ticket> options;
    private FirebaseRecyclerAdapter<Model_ticket, MyeTicketActivity.UserViewholder> fadapter;

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
        getSupportActionBar().setTitle("My eTicket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(mUser!=null){
            userId=mUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Buyticket").child(userId);

            Log.d("userid", "onCreate: userid "+userId);



        }else{
            Log.d("user", "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }

        list=new ArrayList<>();



         //showdatafromsqdb();
         //showdatafromapexdb();

      // TicketAdapter adapter = new TicketAdapter(this,list);

        recyclerView=findViewById(R.id.ticketrecylerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





    }

/*
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
*/


    @Override
    protected void onStart() {
        super.onStart();


        mUser= FirebaseAuth.getInstance().getCurrentUser();
        userId=mUser.getUid();
        Log.d("userid", "onStart: -----userid "+ userId);
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Buyticket").child(userId);
        options=new FirebaseRecyclerOptions.Builder<Model_ticket>().setQuery(mDatabase,Model_ticket.class).build();
        fadapter= new FirebaseRecyclerAdapter<Model_ticket, UserViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewholder userViewholder, int i, @NonNull final Model_ticket model_ticket) {

                userViewholder.tv_total.setText(Integer.toString(model_ticket.getTotal_amount()));
                userViewholder.status.setText(model_ticket.getStatus());

                //userViewholder.imageView.setImageResource(R.drawable.ticket);

                final String itemkey = getRef(i).getKey();

                Log.d("itemkey", "onBindViewHolder: user childkey= "+ itemkey);

                userViewholder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(),TicketfromListviewActivity.class).putExtra("itemkey",itemkey)
                        .putExtra("branch_adp",model_ticket.getBranch()).putExtra("total",model_ticket.getTotal_amount())
                                .putExtra("validity",model_ticket.getStatus()).putExtra("time",model_ticket.getTime()).putExtra("orderid",model_ticket.getOrderid())
                        );
                    }
                });





            }

            @NonNull
            @Override
            public UserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticketview,parent,false);

                return  new UserViewholder(view);
            }
        };

        fadapter.startListening();
        recyclerView.setAdapter(fadapter);
    }

    public static class UserViewholder extends  RecyclerView.ViewHolder{


        ImageView imageView;
        TextView tv_total;
        TextView status;
        RelativeLayout layout;
        public UserViewholder(@NonNull View itemView) {
            super(itemView);


            imageView=itemView.findViewById(R.id.img_ticketlogo);
            tv_total=itemView.findViewById(R.id.amountid);
            status=itemView.findViewById(R.id.statusid);
            layout=itemView.findViewById(R.id.relativeListener);
        }
    }

}
