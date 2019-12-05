package com.babuland.babuland1.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.babuland.babuland1.activity.BabulandpointsActivity;
import com.babuland.babuland1.activity.FooditemActivity;
import com.babuland.babuland1.activity.LoginActivity;
import com.babuland.babuland1.activity.PaymentActivity;
import com.babuland.babuland1.adapter.CustomAdapter;
import com.babuland.babuland1.adapter.Viewpageradapter_homeslider;
import com.babuland.babuland1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class ForthFragment extends Fragment  implements View.OnClickListener {

    RecyclerView recyclerView;
    CustomAdapter adapter;
    String[] heading;
    String []content;
    int []imgrclr={R.drawable.testpic1,R.drawable.souppic,R.drawable.testpic3,R.drawable.choclet_cake1,R.drawable.thai_soup,R.drawable.jouce2,R.drawable.jouce2};
    LinearLayout btn_buyticket;
    int delay=2500,priod=3000;



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

    private TextView[] indexes;
    private ViewPager mViewpager_home;
    private Viewpageradapter_homeslider viewpageradapter;
    private LinearLayout dotlayout;

    private RadioGroup radioGroup;
    private RadioButton rdio_text;

    private int selected;

    private String radiovalue;


    private LinearLayout babulandpoints_liniarid;
    private TextView tv_babulandpoints;

    FirebaseUser mUser;
    DatabaseReference mDatabase;
    String userId;
    LinearLayout fooditem;


    public ForthFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view=inflater.inflate(R.layout.fragment_forth, container, false);



      return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fooditem=getActivity().findViewById(R.id.fooditem);
        babulandpoints_liniarid=getActivity().findViewById(R.id.babulandpoint_liniarid);
        tv_babulandpoints=getActivity().findViewById(R.id.babulandpoint_tvid);

         babulandpoints_liniarid.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(getActivity(), BabulandpointsActivity.class));
             }
         });


        mUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null) {
            userId = mUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.child("BabulandPoints").exists()) {
                        Integer databaspoint = dataSnapshot.child("BabulandPoints").getValue(Integer.class);

                        tv_babulandpoints.setText("My Points "+ Integer.toString(databaspoint));
                                            }
                    else {

                        //Toast.makeText(getActivity(), "failed to pull data", Toast.LENGTH_SHORT).show();
                                            }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        //viewpager adpter event


        mViewpager_home=getActivity().findViewById(R.id.forth_viewpager);
        viewpageradapter=new Viewpageradapter_homeslider(getContext());
        mViewpager_home.setAdapter(viewpageradapter);


        //addDotindicator(0);
        mViewpager_home.addOnPageChangeListener(viewlistener);

        Timer timer =  new Timer();

        timer.scheduleAtFixedRate(new Mytimertask_home(),delay,priod);

        //recyclerView event

        /*recyclerView=getActivity().findViewById(R.id.recyclerview);
        heading=getResources().getStringArray(R.array.heading);
        content=getResources().getStringArray(R.array.contetn);



        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager =new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        adapter=new CustomAdapter(getContext(),imgrclr,heading,content);
        recyclerView.setAdapter(adapter);
*/
        //normal button event for buy ticket
        btn_buyticket=getActivity().findViewById(R.id.byTicket);
        btn_buyticket.setOnClickListener(this);

        fooditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), FooditemActivity.class));
            }
        });
    }

/*    private void addDotindicator(int position) {

        indexes=new TextView[7];


        for(int i=0; i<indexes.length;i++){
            indexes[i]=new TextView(getContext());
            indexes[i].setText(Html.fromHtml("&#8226"));
            indexes[i].setTextSize(35);
            indexes[i].setTextColor(getResources().getColor(R.color.transparentWhite));

        }
        if(indexes.length>0){
            indexes[position].setTextColor(Color.BLACK);
        }


    }*/


    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        }

        @Override
        public void onPageSelected(int position) {

           // addDotindicator(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.byTicket:
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

    @Override
    public void onStart() {
        super.onStart();
        delay=2500;
        priod=3000;


    }

    private void dialogbox() {

        final AlertDialog.Builder albuilder = new AlertDialog.Builder(getContext());
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
                        Toast.makeText(getContext(), "mirpur is selected", Toast.LENGTH_SHORT).show();
                        radiovalue="mirpur";
                        break;
                    case R.id.radiowari:

                        Toast.makeText(getContext(), "wari  is selected", Toast.LENGTH_SHORT).show();
                        radiovalue="wari";
                        break;
                    case R.id.radioutt:
                        Toast.makeText(getContext(), "uttara is selected", Toast.LENGTH_SHORT).show();
                        radiovalue="uttara";
                        break;
                }
            }
        });

            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(radiovalue!=null){

                        Intent intent = new Intent(getActivity(), PaymentActivity.class);

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
                        Toast.makeText(getContext(), "please select a branch first", Toast.LENGTH_SHORT).show();
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


    public class Mytimertask_home extends TimerTask {

        @Override
        public void run() {

             if(getActivity()!=null){
                 getActivity().runOnUiThread(new Runnable() {
                     @Override
                     public void run() {

                         if(mViewpager_home.getCurrentItem()==0){
                             mViewpager_home.setCurrentItem(1);
                         }else if(mViewpager_home.getCurrentItem()==1)
                             mViewpager_home.setCurrentItem(2);
                         else if(mViewpager_home.getCurrentItem()==2)
                             mViewpager_home.setCurrentItem(3);
                         else if(mViewpager_home.getCurrentItem()==3)
                             mViewpager_home.setCurrentItem(4);
                         else if(mViewpager_home.getCurrentItem()==4)
                             mViewpager_home.setCurrentItem(5);
                         else if(mViewpager_home.getCurrentItem()==5)
                             mViewpager_home.setCurrentItem(6);

                         else if(mViewpager_home.getCurrentItem()==6)
                         {
                             SystemClock.sleep(100);
                             mViewpager_home.setCurrentItem(0,true);
                         }




                    /*if(mViewpager.getCurrentItem()==6){
                        mViewpager.setCurrentItem(0,true);
                    }*/
                     }
                 });

             }
        }
    }

    private void sendTologin() {
        startActivity(new Intent(getContext(), LoginActivity.class));
        //getContext().finish();
    }



}
