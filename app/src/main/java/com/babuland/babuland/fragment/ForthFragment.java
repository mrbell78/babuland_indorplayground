package com.babuland.babuland.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.babuland.babuland.R;
import com.babuland.babuland.activity.BabulandpointsActivity;
import com.babuland.babuland.activity.FooditemActivity;
import com.babuland.babuland.activity.LoginActivity;
import com.babuland.babuland.activity.PaymentActivity;
import com.babuland.babuland.activity.TicketfromListviewActivity;
import com.babuland.babuland.adapter.CustomAdapter;
import com.babuland.babuland.adapter.Viewpageradapter_homeslider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.babuland.babuland.activity.PaymentActivity.DEFAULT_DRIVER;

public class ForthFragment extends Fragment implements View.OnClickListener {

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

    private ProgressDialog mdialog;
    private DatePickerDialog.OnDateSetListener mDatesetListener;

    private LinearLayout babulandpoints_liniarid;
    private TextView tv_babulandpoints;

    EditText edt_name,edt_number,childnumber_view,spousename_view,email_view;
    TextView dob_view;
    Spinner spinner_gender,pre_branch_view;
    FirebaseUser mUser;
    DatabaseReference mDatabase;
    String userId;
    LinearLayout fooditem;
    ArrayAdapter<CharSequence> adapter_gender,adapter_branch;

    private TextView tv_buyticket;
    private String name,phone,dob,gender,spousename,email,pre_branch,childnumber,childname;


    Boolean status_free=false;

    private static final String DEFAULT_URL = "jdbc:oracle:thin:@itlimpex.ddns.net:2121:xe";
    private  static String  DEFAULT_USERNAME="bland";
    private static final String DEFAULT_PASSWORD = "servicepack3";

    private Connection connection;

    FirebaseUser mCurrentUser;
    DatabaseReference Freeticketdb;




    public ForthFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
       if(mCurrentUser!=null){

           Freeticketdb=FirebaseDatabase.getInstance().getReference().child("Freeticket").child(mCurrentUser.getUid());

       }


        View view=inflater.inflate(R.layout.fragment_forth, container, false);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fooditem=getActivity().findViewById(R.id.fooditem);
        babulandpoints_liniarid=getActivity().findViewById(R.id.babulandpoint_liniarid);
        tv_babulandpoints=getActivity().findViewById(R.id.babulandpoint_tvid);

        adapter_gender = ArrayAdapter.createFromResource(getActivity(),R.array.gender,android.R.layout.simple_list_item_1);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapter_branch=ArrayAdapter.createFromResource(getActivity(),R.array.branch_prefered,android.R.layout.simple_list_item_1);
        adapter_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();

        StrictMode.setThreadPolicy(policy);

        tv_buyticket=getActivity().findViewById(R.id.buyticket_tv);
        mdialog=new ProgressDialog(getActivity());

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

                        String avilfreeticket= dataSnapshot.child("availfreeTicket").getValue().toString();

                        if(avilfreeticket.equals("avail_free")){
                            tv_buyticket.setText("Get Free Ticket");
                            status_free=true;

                        }else if(avilfreeticket.equals("avail_expired")){
                            tv_buyticket.setText("Buy Ticket");
                            status_free=false;
                        }

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

    private void searchfromapexdb(Connection connection,String phone_local) throws SQLException {

        Statement stmt=connection.createStatement();
        Log.d("phone", "searchfromapexdb: -------------- "+phone_local);

        ResultSet resultSet=stmt.executeQuery(" SELECT  CHILD_NAME,GENDER,DOB,EDU_CLASS,SCHOOL,FULLNAME,SPOUSE_NAME,MOBILE_NUMBER from REG_DATA WHERE  to_char(mobile_number)= '"+phone_local+"'");
        while(resultSet.next()){
            name=resultSet.getString("FULLNAME");
            Log.d("nametgag", "searchfromapexdb: ----------------- "+name);


            phone=resultSet.getString("MOBILE_NUMBER");
            dob=resultSet.getString("DOB");
            spousename=resultSet.getString("SPOUSE_NAME");
            //email=resultSet.getString("CHILD_NAME");
            //pre_branch=resultSet.getString("CHILD_NAME");
            childname=resultSet.getString("CHILD_NAME");
            // childnumber=resultSet.getString("CHILD_NAME");
            gender=resultSet.getString("GENDER");

            Log.d("resultvalue", "searchfromapexdb: ----------------resultcode "+resultSet.next());

        }

        Log.d("name_apex", "searchfromapexdb: fullname  "+name);
        Log.d("name_apex", "searchfromapexdb: name  "+childname);


        if(TextUtils.isEmpty(name) || childname==null){
            Toast.makeText(getContext(), "new user", Toast.LENGTH_SHORT).show();
            edt_name.setError("Enter Name");
            email_view.setError("Enter email");
            spousename_view.setError("Enter spouse name");
            dob_view.setError("fill up  this filed");
            phone=edt_number.getText().toString();
           // edt_number.setError("please enter valid number");


        }else if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(childname)){


            Toast.makeText(getContext(), "existing user", Toast.LENGTH_SHORT).show();
            edt_name.setText(name);
            edt_number.setText(phone);
            spousename_view.setText(spousename);
            email_view.setError("Enter email");
            dob_view.setText(dob);
            //pre_branch_view.setPrompt(pre_branch);

        }

        //EditText edt_name,edt_number,childnumber_view,spousename_view,email_view;







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
                if(tv_buyticket.getText().toString().equals("Buy Ticket"))
                    dialogbox();
                else
                    dailogbox_getfreeTicket();



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


    private void dailogbox_getfreeTicket(){

        AlertDialog.Builder albuilder = new AlertDialog.Builder(getContext());
        final View view = getLayoutInflater().inflate(R.layout.freeticket_getinformation,null);

        albuilder.setView(view);
        final AlertDialog dialog = albuilder.create();
        dialog.show();

        TextView tv_info= view.findViewById(R.id.vnumber_getinfo);


        final CardView cardView;
        cardView=view.findViewById(R.id.chil_edtview);


        tv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                try {
                    if(edt_number.getText().toString()!=null && edt_number.getText().toString().length()==11){
                        Connection connection = createConnection();
                        searchfromapexdb(connection,edt_number.getText().toString());

                        ViewGroup.MarginLayoutParams layoutParams =
                                (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();

                        layoutParams.setMargins(0,10,0,15);
                        cardView.requestLayout();

                        cardView.setVisibility(View.VISIBLE);
                        TranslateAnimation animation = new TranslateAnimation(0,0,cardView.getHeight(),0);
                        animation.setDuration(500);
                        cardView.startAnimation(animation);

                        // Toast.makeText(getContext(), edt_number.getText().toString().length(), Toast.LENGTH_SHORT).show();
                    }
                    else if(edt_number.getText().toString().length()>=11){
                        Toast.makeText(getContext(), "try without +88", Toast.LENGTH_SHORT).show();
                        edt_number.setError("try without +88");
                    }else if(edt_number.getText().toString().length()<=11){
                        Toast.makeText(getContext(), "Enter valid number", Toast.LENGTH_SHORT).show();
                        edt_number.setError("Enter valid number");
                    }



                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Log.d("dberror", "onClick: --------------dberror "+e.getMessage());
                } catch (SQLException e) {
                    e.printStackTrace();
                    Log.d("dberror", "onClick: --------------dberror "+e.getMessage());
                }

            }
        });

        edt_name=view.findViewById(R.id.nameid);
        edt_number=view.findViewById(R.id.number);

        childnumber_view=view.findViewById(R.id.child_number);
        dob_view=view.findViewById(R.id.tv_dateofBirdth_user);
        spinner_gender=view.findViewById(R.id.spiner_gender);
        spousename_view=view.findViewById(R.id.spouseid);
        email_view=view.findViewById(R.id.emailid);


        name = edt_name.getText().toString();
        phone=edt_number.getText().toString();

        spousename=spousename_view.getText().toString();

        email=email_view.getText().toString();
        childnumber=childnumber_view.getText().toString();
        mdialog.setTitle("Profile Loading");
        mdialog.setMessage("Please wait........");
        setinfo_dialog(edt_name,edt_number,childnumber_view,dob_view,spinner_gender,spousename_view,email_view,childnumber_view);


        dob_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int day  = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog  = new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Dialog_MinWidth,mDatesetListener,year,month,day);
                dialog.getWindow();
                dialog.show();
            }
        });

        mDatesetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1=i1+1;
                dob=i2+"/"+i1+"/"+i;
                dob_view.setText(dob);

            }
        };

        spinner_gender.setAdapter(adapter_gender);
        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
/*
      pre_branch_view.setAdapter(adapter_branch);
      pre_branch_view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

              pre_branch=parent.getItemAtPosition(position).toString();
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });*/





        Button btn_saveinfo_free=view.findViewById(R.id.btn_saveChange);

        btn_saveinfo_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                mdialog.setTitle("Uploading");
                mdialog.setMessage("Please wait.......:)");

                mUser= FirebaseAuth.getInstance().getCurrentUser();
                if(mUser!=null) {
                    userId = mUser.getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);





                    if(!TextUtils.isEmpty(edt_name.getText().toString()) && !TextUtils.isEmpty(edt_number.getText().toString()) && !TextUtils.isEmpty(dob) &&

                            !TextUtils.isEmpty(gender) && !TextUtils.isEmpty(spousename_view.getText().toString()) && !TextUtils.isEmpty(email_view.getText().toString())

                    ){


                        Map freeticket_getdata = new HashMap();

                        freeticket_getdata.put("name",edt_name.getText().toString());
                        freeticket_getdata.put("phone",edt_number.getText().toString());
                        freeticket_getdata.put("availfreeTicket","avail_expired");
                        freeticket_getdata.put("dateofbirdth",dob);
                        freeticket_getdata.put("gender",gender);
                        freeticket_getdata.put("spousename",spousename_view.getText().toString());
                        freeticket_getdata.put("email",email_view.getText().toString());
                        freeticket_getdata.put("pre_branch",pre_branch);
                        freeticket_getdata.put("childrenName",childnumber);

                        mDatabase.updateChildren(freeticket_getdata).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                String currentTime_rdm = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


                                Map imagedatawiththum=  new HashMap();
                                imagedatawiththum.put("status","Free ticket");
                                imagedatawiththum.put("phone",phone);
                                imagedatawiththum.put("total_amount",0);
                                imagedatawiththum.put("branch","any");
                                imagedatawiththum.put("time","One time");
                                imagedatawiththum.put("orderid",0);
                                String childname=phone+currentTime_rdm;


                                Freeticketdb.child(childname).updateChildren(imagedatawiththum).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        mdialog.dismiss();
                                        dialog.dismiss();
                                        startActivity(new Intent(getContext(), TicketfromListviewActivity.class).putExtra("orderid", Integer.parseInt(phone)));

                                    }
                                });


                            }
                        });
                    }else {
                        Toast.makeText(getContext(), "You must have to complete all the info box", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void setinfo_dialog(final EditText edt_name, final EditText edt_number, final EditText childnumber_view, final TextView dob_view, final Spinner spinner_gender, final EditText spousename_view, EditText email_view, EditText childnumber_view1) {


        mUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null) {
            userId = mUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        //name
                        String name=dataSnapshot.child("name").getValue().toString();
                        edt_name.setText(name);
                        //number
                        String number=dataSnapshot.child("phone").getValue().toString();
                        edt_number.setText(number);
                        //child number
                        String childnumber=dataSnapshot.child("childrenName").getValue().toString();
                        childnumber_view.setText(childnumber);

                        //dob
                        String dob=dataSnapshot.child("dateofbirdth").getValue().toString();
                        dob_view.setText(dob);

                        //spouse name
                        String spousename=dataSnapshot.child("spousename").getValue().toString();
                        spousename_view.setText(spousename);

                        // gender
                        String genderlocal = dataSnapshot.child("gender").getValue().toString();
                        gender=genderlocal;



                        mdialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mdialog.dismiss();
                }
            });

        }

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




    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {

        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {


        return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);

    }


}
