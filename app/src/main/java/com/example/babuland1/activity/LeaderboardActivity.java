package com.example.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.babuland1.model.Model_leaderboard;
import com.example.babuland1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderboardActivity extends AppCompatActivity {

    private FirebaseRecyclerOptions<Model_leaderboard> options;
    private FirebaseRecyclerAdapter<Model_leaderboard,UserViewholder> fadapter;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private String mUserid;
    private RecyclerView recyclerView;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        mToolbar=findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        //db=new DbHelper_freeTicket(this);
        getSupportActionBar().setTitle("Leader board");

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserid=mUser.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Leaderboard");
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


    @Override
    protected void onStart() {
        super.onStart();

        options = new FirebaseRecyclerOptions.Builder<Model_leaderboard>().setQuery(mDatabase,Model_leaderboard.class).build();

        fadapter= new FirebaseRecyclerAdapter<Model_leaderboard, UserViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewholder userViewholder, int i, @NonNull Model_leaderboard model_leaderboard) {


                Picasso.with(LeaderboardActivity.this).load(model_leaderboard.getImage()).into(userViewholder.imageView);
                userViewholder.tv_name.setText(model_leaderboard.getName());

            }

            @NonNull
            @Override
            public UserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.firebaserecycleradapter_leaderboard,parent,false);
                return new UserViewholder(view);
            }
        };

        fadapter.startListening();
        recyclerView.setAdapter(fadapter);
    }

    public static class UserViewholder extends  RecyclerView.ViewHolder{


        CircleImageView imageView;
        TextView tv_name;
        ImageView img_status;
        public UserViewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_profile);
            tv_name=itemView.findViewById(R.id.text_profile);
            img_status=itemView.findViewById(R.id.imgstatus);


        }
    }
}
