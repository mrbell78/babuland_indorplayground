package com.example.babuland1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.babuland1.R;
import com.example.babuland1.adapter.CustomAdapter;

public class FooditemActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    CustomAdapter adapter;
    String[] heading;
    String []content;
    int []imgrclr={R.drawable.testpic1,R.drawable.souppic,R.drawable.testpic3,R.drawable.choclet_cake1,R.drawable.thai_soup,R.drawable.jouce2,R.drawable.vr_pic};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooditem);

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
