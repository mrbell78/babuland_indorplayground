package com.babuland.babuland.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.babuland.babuland.R;
import com.babuland.babuland.model.Model_ticket;
import com.babuland.babuland.utils.DbHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class MyeTicketActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    DbHelper db;
    ImageView imageView;
    String value ;
    Cursor cursor;
    TextView tv;
    List<Model_ticket> list;
    RecyclerView recyclerView;
    DatabaseReference mDatabase,mTicketdb;
    FirebaseUser mUser,ticketuser;
    String userId,ticketuserid;
    String status;
    LinearLayout myticketlinerid;
    Button btn_buyticketid;

    private FirebaseRecyclerOptions<Model_ticket> options;
    private FirebaseRecyclerAdapter<Model_ticket, MyeTicketActivity.UserViewholder> fadapter;

    public static final String DEFAULT_DRIVER="oracle.jdbc.driver.OracleDriver";
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@itlimpex.ddns.net:2121:xe";
    private static final String DEFAULT_USERNAME = "bland";
    private static final String DEFAULT_PASSWORD = "servicepack3";

    private Connection connection;

    private RadioGroup radioGroup;
    private String radiovalue;

    private ImageButton btn_minus_infant;
    private ImageButton  btn_minus_kids;
    private ImageButton  btn_minus_gardian;
    private ImageButton  btn_minus_socks;

    private ImageButton btn_add_infant;
    private ImageButton btn_add_kids;
    private ImageButton btn_add_gardian;
    private ImageButton btn_add_socks;


    private TextView tv_minus_infat;
    private TextView tv_minus_kids;
    private TextView tv_minus_gardian;
    private TextView tv_minus_socks;

    private int Count_infat=0;
    private int Count_kids=0;
    private int Count_gardian=0;
    private int Count_socks=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mye_ticket);
         mToolbar=findViewById(R.id.toolbar);
        myticketlinerid=findViewById(R.id.myticketlinerid);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My eTicket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btn_buyticketid=findViewById(R.id.btn_buyticketid);
        btn_buyticketid.setOnClickListener(this);

        ticketuser=FirebaseAuth.getInstance().getCurrentUser();
        ticketuserid=ticketuser.getUid();
        mTicketdb=FirebaseDatabase.getInstance().getReference().child("Buyticket").child(ticketuserid);

        mTicketdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(MyeTicketActivity.this, "number of ticket "+ dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                if(dataSnapshot.getChildrenCount()>0){
                    myticketlinerid.setVisibility(View.INVISIBLE);
                }else if(dataSnapshot.getChildrenCount()==0){
                    myticketlinerid.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.btn_buyticketid:
                    dialogbox();
                    break;
            case R.id.btn_minus_infant:
                if(Count_infat>0){
                    Count_infat--;
                    tv_minus_infat.setText(Integer.toString(Count_infat));
                }else {
                    Count_gardian=0;
                    //tv_minus_infat.setText(Integer.toString(Count_infat));
                }
                break;
            case R.id.btn_minus_kids:
                if(Count_kids>0){
                    Count_kids--;
                    tv_minus_kids.setText(Integer.toString(Count_kids));
                }else {
                    Count_gardian=0;
                    //tv_minus_kids.setText(Integer.toString(Count_kids));
                }
                break;

            case R.id.btn_minus_gardian:
                if(Count_gardian>0){
                    Count_gardian--;
                    tv_minus_gardian.setText(Integer.toString(Count_gardian));
                }else {
                    Count_gardian=0;
                    //tv_minus_gardian.setText(Integer.toString(Count_gardian));
                }
                break;

            case R.id.btn_minus_soks:
                if(Count_socks>0){
                    Count_socks--;
                    tv_minus_socks.setText(Integer.toString(Count_socks));
                }else {
                    Count_gardian=0;
                    //tv_minus_socks.setText(Integer.toString(Count_socks));
                }

                break;

            case R.id.btn_add_infant:
                Count_infat++;
                tv_minus_infat.setText(Integer.toString(Count_infat));
                break;
            case R.id.btn_add_kids:
                Count_kids++;
                tv_minus_kids.setText(Integer.toString(Count_kids));
                break;

            case R.id.btn_add_gardian:
                Count_gardian++;
                tv_minus_gardian.setText(Integer.toString(Count_gardian));

                break;
            case R.id.btn_add_socks:
                Count_socks++;
                tv_minus_socks.setText(Integer.toString(Count_socks));
                break;

            default:return;
        }

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


    private void dialogbox() {

        final AlertDialog.Builder albuilder = new AlertDialog.Builder(MyeTicketActivity.this);
        View view = getLayoutInflater().inflate(R.layout.customdialogbox,null);

        Button btn_confirm=view.findViewById(R.id.confirm_ticket_dialog);

        radioGroup=view.findViewById(R.id.radioallgrp);

        dialogdata(view);

        albuilder.setView(view);
        final AlertDialog dialog = albuilder.create();
        dialog.show();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radiomirpur:
                        Toast.makeText(MyeTicketActivity.this, "mirpur is selected", Toast.LENGTH_SHORT).show();
                        radiovalue="mirpur";
                        break;
                    case R.id.radiowari:

                        Toast.makeText(MyeTicketActivity.this, "wari  is selected", Toast.LENGTH_SHORT).show();
                        radiovalue="wari";
                        break;
                    case R.id.radioutt:
                        Toast.makeText(MyeTicketActivity.this, "uttara is selected", Toast.LENGTH_SHORT).show();
                        radiovalue="uttara";
                        break;
                }
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(radiovalue!=null){

                    Intent intent = new Intent(MyeTicketActivity.this, PaymentActivity.class);

                    intent.putExtra("infant",Count_infat);
                    intent.putExtra("kids",Count_kids);
                    intent.putExtra("gardian",Count_gardian);
                    intent.putExtra("socks",Count_socks);
                    intent.putExtra("radiovalue_string",radiovalue);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    dialog.dismiss();

                    Count_infat=0;
                    Count_kids=0;
                    Count_gardian=0;
                    Count_socks=0;
                    radiovalue=null;
                }else{
                    Toast.makeText(MyeTicketActivity.this, "please select a branch first", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void dialogdata(View view) {

        btn_minus_infant=view.findViewById(R.id.btn_minus_infant);
        btn_minus_kids=view.findViewById(R.id.btn_minus_kids);
        btn_minus_gardian=view.findViewById(R.id.btn_minus_gardian);
        btn_minus_socks=view.findViewById(R.id.btn_minus_soks);


        btn_add_infant=view.findViewById(R.id.btn_add_infant);
        btn_add_kids=view.findViewById(R.id.btn_add_kids);
        btn_add_gardian=view.findViewById(R.id.btn_add_gardian);
        btn_add_socks=view.findViewById(R.id.btn_add_socks);


        tv_minus_infat=view.findViewById(R.id.tv_count_infant);
        tv_minus_kids=view.findViewById(R.id.tv_count_kids);
        tv_minus_gardian=view.findViewById(R.id.tv_count_gardian);
        tv_minus_socks=view.findViewById(R.id.tv_count_socks);



        btn_minus_infant.setOnClickListener(this);
        btn_minus_kids.setOnClickListener(this);
        btn_minus_gardian.setOnClickListener(this);
        btn_minus_socks.setOnClickListener(this);


        btn_add_infant.setOnClickListener(this);
        btn_add_kids.setOnClickListener(this);
        btn_add_gardian.setOnClickListener(this);
        btn_add_socks.setOnClickListener(this);


        radioGroup=view.findViewById(R.id.radioallgrp);


        /*switch (view.getId()){
            case R.id.btn_minus_infant:
                if(Count_infat>0){
                    Count_infat--;
                    tv_minus_infat.setText(Integer.toString(Count_infat));
                }else {
                    Count_gardian=0;
                    //tv_minus_infat.setText(Integer.toString(Count_infat));
                }
                break;
            case R.id.btn_minus_kids:
                if(Count_kids>0){
                    Count_kids--;
                    tv_minus_kids.setText(Integer.toString(Count_kids));
                }else {
                    Count_gardian=0;
                    //tv_minus_kids.setText(Integer.toString(Count_kids));
                }
                break;

            case R.id.btn_minus_gardian:
                if(Count_gardian>0){
                    Count_gardian--;
                    tv_minus_gardian.setText(Integer.toString(Count_gardian));
                }else {
                    Count_gardian=0;
                    //tv_minus_gardian.setText(Integer.toString(Count_gardian));
                }
                break;

            case R.id.btn_minus_soks:
                if(Count_socks>0){
                    Count_socks--;
                    tv_minus_socks.setText(Integer.toString(Count_socks));
                }else {
                    Count_gardian=0;
                    //tv_minus_socks.setText(Integer.toString(Count_socks));
                }

                break;

            case R.id.btn_add_infant:
                Count_infat++;
                tv_minus_infat.setText(Integer.toString(Count_infat));
                break;
            case R.id.btn_add_kids:
                tv_minus_kids.setText(Integer.toString(Count_kids));
                Count_kids++;
                break;

            case R.id.btn_add_gardian:
                Count_gardian++;
                tv_minus_gardian.setText(Integer.toString(Count_gardian));

                break;
            case R.id.btn_add_socks:
                Count_socks++;
                tv_minus_socks.setText(Integer.toString(Count_socks));
                break;
            default:
                return;
        }*/





    }



}
