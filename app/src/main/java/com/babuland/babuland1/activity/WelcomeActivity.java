package com.babuland.babuland1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babuland.babuland1.adapter.Viewpageradapter;
import com.babuland.babuland1.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    private TextView[] indexes;
    private ViewPager mViewpager;
    private Viewpageradapter viewpageradapter;
    private LinearLayout dotlayout;
    private Button btn_login;
    private static final String TAG = "WelcomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mViewpager=findViewById(R.id.viewpager);
        viewpageradapter=new Viewpageradapter(this);

        mViewpager.setAdapter(viewpageradapter);

        dotlayout=findViewById(R.id.dot);

        btn_login=findViewById(R.id.login_wecome);

        addDotindicator(0);
        mViewpager.addOnPageChangeListener(viewlistener);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));

            }
        });

        Timer timer =  new Timer();

        timer.scheduleAtFixedRate(new WelcomeActivity.Mytimertask(),2500,3000);
    }

    private void addDotindicator(int position) {

        indexes=new TextView[5];

        dotlayout.removeAllViews();
        for(int i=0; i<indexes.length;i++){
            indexes[i]=new TextView(this);
            indexes[i].setText(Html.fromHtml("&#8226"));
            indexes[i].setTextSize(35);
            indexes[i].setTextColor(getResources().getColor(R.color.transparentWhite));
            dotlayout.addView(indexes[i]);
        }
        if(indexes.length>0){
                indexes[position].setTextColor(Color.BLACK);
        }


    }

    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        }

        @Override
        public void onPageSelected(int position) {

            addDotindicator(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public class Mytimertask extends TimerTask {

        @Override
        public void run() {
            WelcomeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mViewpager.getCurrentItem()==0){
                        mViewpager.setCurrentItem(1);
                    }else if(mViewpager.getCurrentItem()==1)
                        mViewpager.setCurrentItem(2);
                    else if(mViewpager.getCurrentItem()==2)
                        mViewpager.setCurrentItem(3);
                    else if(mViewpager.getCurrentItem()==3)
                        mViewpager.setCurrentItem(4);
                    else if(mViewpager.getCurrentItem()==4)
                        mViewpager.setCurrentItem(5);
                    Log.d(TAG, "run: --------------------------------------------------------------------getCurrentposition-------------------------"+mViewpager.getCurrentItem());

                    if(mViewpager.getCurrentItem()==4){
                        mViewpager.setCurrentItem(0,true);
                    }
                }
            });
        }
    }

}
