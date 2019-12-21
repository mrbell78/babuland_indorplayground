package com.babuland.babuland.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.babuland.babuland.adapter.CustomAdapter;
import com.babuland.babuland.R;

public class FooditemActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    CustomAdapter adapter;
    String[] heading;
    String []content;
    int []imgrclr={R.drawable.testpic1,R.drawable.souppic,R.drawable.testpic3,R.drawable.choclet_cake1,R.drawable.thai_soup,R.drawable.jouce2,R.drawable.jouce2};


    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooditem);



        mToolbar=findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        //db=new DbHelper_freeTicket(this);
        getSupportActionBar().setTitle("Babuland Food Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.fooditem_recylerview);

        heading=getResources().getStringArray(R.array.heading);
        content=getResources().getStringArray(R.array.contetn);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter=new CustomAdapter(this,imgrclr,heading,content);
        recyclerView.setAdapter(adapter);
    }
}
