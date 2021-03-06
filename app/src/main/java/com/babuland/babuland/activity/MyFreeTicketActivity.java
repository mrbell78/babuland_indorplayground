package com.babuland.babuland.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.babuland.babuland.R;
import com.babuland.babuland.model.Model_ticket;
import com.babuland.babuland.utils.DbHelper_freeTicket;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    DatabaseReference mDatabase,mTicketdb;
    FirebaseUser mUser,ticketuser_free;
    String userId,ticketuserid_free;
    LinearLayout freeticketid;


    private FirebaseRecyclerOptions<Model_ticket> options;
    private FirebaseRecyclerAdapter<Model_ticket, MyFreeTicketActivity.UserViewholder> fadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_free_ticket);

        mToolbar=findViewById(R.id.toolbar);
        freeticketid=findViewById(R.id.freeticketid);
        setSupportActionBar(mToolbar);
        //db=new DbHelper_freeTicket(this);
        getSupportActionBar().setTitle("My Free Ticket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ticketuser_free=FirebaseAuth.getInstance().getCurrentUser();
        ticketuserid_free=ticketuser_free.getUid();
        mTicketdb=FirebaseDatabase.getInstance().getReference().child("Freeticket").child(ticketuserid_free);

        mTicketdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(MyFreeTicketActivity.this, "number of ticket "+ dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                if(dataSnapshot.getChildrenCount()>0){
                    freeticketid.setVisibility(View.INVISIBLE);
                }else if(dataSnapshot.getChildrenCount()==0){
                    freeticketid.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerView=findViewById(R.id.ticketrecylerview_free);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*list=new ArrayList<>();
        int count = db.count_free();
*/


        //showdatafromsqdb();
        //showdatafromapexdb();

       /* TicketAdapter adapter = new TicketAdapter(this,list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
*/
    }

    private void showdatafromsqdb() {


        cursor=db.pulldata_free();




    /*    if(cursor!=null){

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
        }*/
    }



    @Override
    protected void onStart() {
        super.onStart();


        mUser= FirebaseAuth.getInstance().getCurrentUser();
        userId=mUser.getUid();
        Log.d("userid", "onStart: -----userid "+ userId);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Freeticket").child(userId);
        options=new FirebaseRecyclerOptions.Builder<Model_ticket>().setQuery(mDatabase,Model_ticket.class).build();
        fadapter= new FirebaseRecyclerAdapter<Model_ticket, MyFreeTicketActivity.UserViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyFreeTicketActivity.UserViewholder userViewholder, int i, @NonNull final Model_ticket model_ticket) {

                userViewholder.tv_total.setText(Integer.toString(model_ticket.getTotal_amount()));
                userViewholder.status.setText(model_ticket.getTime());

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
            public MyFreeTicketActivity.UserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticketview,parent,false);

                return  new MyFreeTicketActivity.UserViewholder(view);
            }
        };
        Toast.makeText(this, "Number of ticket"+fadapter.getItemCount(), Toast.LENGTH_SHORT).show();
        if(fadapter.getItemCount()>=0){
            freeticketid.setVisibility(View.INVISIBLE);

        }else {
            freeticketid.setVisibility(View.VISIBLE);
        }
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
