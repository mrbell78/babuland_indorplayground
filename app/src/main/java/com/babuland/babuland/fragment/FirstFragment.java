package com.babuland.babuland.fragment;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.babuland.babuland.MainActivity;
import com.babuland.babuland.R;
import com.babuland.babuland.activity.MyFreeTicketActivity;
import com.babuland.babuland.activity.Qr_cameraopenerActivity;
import com.babuland.babuland.utils.DbHelper_freeTicket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment implements Qr_cameraopenerActivity.qrSendata {


    CircleImageView profileimage;
    TextView name;
    TextView number;
    TextView qrResult;
    private String namestring;
    private String image;
    private ImageView qrCodeScaner;
    private DatabaseReference mDatabaseref;
    private FirebaseUser mCurrentUser;
    private String userId;
    public TextView tv_scan1,tv_scan2,tv_scan3,tv_scan4,tv_scan5,tv_scan6,tv_scan7,tv_scan8,tv_scan9,tv_scan10,tv_scan11,tv_scan12,tv_scan13,tv_scan14,tv_scan15;
    public ImageView img_scan1,img_scan2,img_scan3,img_scan4,img_scan5,img_scan6,img_scan7,img_scan8,img_scan9,img_scan10,img_scan11,img_scan12,img_scan13,img_scan14,img_scan15;
    public CardView cd1,cd2,cd3,cd4,cd5,cd6,cd7,cd8,cd9,cd10,cd11,cd12,cd13,cd14,cd15;
    private SharedPreferences sharedPreferences;
    private static final String TAG = "FirstFragment";
    private int i=0;
    private ProgressDialog mdialog;
    private String qrcode_id;
    private String ticket1,ticket2,ticket3,ticket4,ticket5,ticket6,ticket7,ticket8,ticket9,ticket10;
    private Qr_cameraopenerActivity data;
    private  String datafromqar;
    private LinearLayout tv_win,pre_win;


    private ProgressBar v;
    private ObjectAnimator animator;
    private homefragmentlistener listener;
    private int progressvalue;
    private MediaPlayer mp,mpp;
    private LinearLayout layout_rdm;
    private TextView tv_percentage;
    private String finladatafromqr;
    private String identity;
    private TextView btn_later;
    private Boolean status;
    private TextView tv_ticketrdm;
    private Button btn_redmticket;
    private Activity activity=getActivity();

    private Date currentTime;
    private String imagename;
    private Integer count_ticketrdm=0;
    DbHelper_freeTicket db;

    DatabaseReference Freeticketdb;

    SimpleDateFormat dateFormat;
    String formattedDate;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public void validationofdata(String data) {
       img_scan2.setEnabled(false);
       cd2.setCardBackgroundColor(Color.parseColor("#d3d3d3"));
    }


    public interface homefragmentlistener{
        void Inputsent(String qrdata);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_first,container,false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        db=new DbHelper_freeTicket(getContext());
        status=true;
        btn_later=view.findViewById(R.id.btn_later);
        dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.S aa");

        btn_redmticket=view.findViewById(R.id.btn_redeemtk);
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        Freeticketdb=FirebaseDatabase.getInstance().getReference().child("Freeticket").child(mCurrentUser.getUid());


        if(status.equals(true)){
            btn_redmticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mDatabaseref.child("count1").setValue("avail");
                    mDatabaseref.child("count2").setValue("avail");
                    mDatabaseref.child("count3").setValue("avail");
                    mDatabaseref.child("count4").setValue("avail");
                    mDatabaseref.child("count5").setValue("avail");
                    mDatabaseref.child("count6").setValue("avail");
                    mDatabaseref.child("progressvalue_stamp").setValue(0) ;



                    mDatabaseref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            count_ticketrdm =  dataSnapshot.child("ticket_redeem").getValue(Integer.class) ;
                            mDatabaseref.child("ticket_redeem").setValue(count_ticketrdm=count_ticketrdm+1);
                            formattedDate = dateFormat.format(new Date()).toString();
                            //db.insertdata_free(formattedDate,0,123456789+count_ticketrdm,"free","any");

                            String currentTime_rdm = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            String phone = dataSnapshot.child("phone").getValue().toString();
                            // db.insertdata_free(formattedDate,0,123456789+count_ticketrdm,"free","any");


                            Map imagedatawiththum=  new HashMap();
                            imagedatawiththum.put("status","Free ticket");
                            imagedatawiththum.put("phone",phone);
                            imagedatawiththum.put("total_amount",0);
                            imagedatawiththum.put("branch","any");
                            imagedatawiththum.put("time",formattedDate);
                            imagedatawiththum.put("orderid",0);
                            String childname=phone+currentTime_rdm;


                            Freeticketdb.child(childname).updateChildren(imagedatawiththum).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        tv_ticketrdm.setText(Integer.toString(count_ticketrdm)+" free ticket");
                                        mdialog.dismiss();

                                        /*Intent intent = new Intent(getContext(), TicketfromListviewActivity.class);

                                        intent.putExtra("orderid",123456789+count_ticketrdm);
                                        intent.putExtra("time",formattedDate);
                                        intent.putExtra("total",0);
                                        intent.putExtra("status","Free ticket");
                                        intent.putExtra("branch","any");
                                        getContext().startActivity(intent);*/
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Log.d(TAG, "onClick: ---------------------------------------------current date "+ formattedDate);
                /*QrcodeActivity activity = new QrcodeActivity();
                activity.qrcodegenaretorfromadapter(String.valueOf(403)+"free"+"any");*/
                    v.setProgress(0);
                    pre_win.setVisibility(View.VISIBLE);
                    layout_rdm.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            });
        }



    if(status.equals(true)){
        btn_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mDatabaseref.child("count1").setValue("avail");
                mDatabaseref.child("count2").setValue("avail");
                mDatabaseref.child("count3").setValue("avail");
                mDatabaseref.child("count4").setValue("avail");
                mDatabaseref.child("count5").setValue("avail");
                mDatabaseref.child("count6").setValue("avail");
                mDatabaseref.child("progressvalue_stamp").setValue(0) ;

                mDatabaseref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        count_ticketrdm =  dataSnapshot.child("ticket_redeem").getValue(Integer.class) ;
                        mDatabaseref.child("ticket_redeem").setValue(count_ticketrdm=count_ticketrdm+1);
                        formattedDate = dateFormat.format(new Date()).toString();
                        String currentTime_later = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                        String phone = dataSnapshot.child("phone").getValue().toString();
                       // db.insertdata_free(formattedDate,0,123456789+count_ticketrdm,"free","any");


                        Map imagedatawiththum=  new HashMap();
                        imagedatawiththum.put("status","Free ticket");
                        imagedatawiththum.put("phone",phone);
                        imagedatawiththum.put("total_amount",0);
                        imagedatawiththum.put("branch","any");
                        imagedatawiththum.put("time",formattedDate);
                        imagedatawiththum.put("orderid",123456789+count_ticketrdm);
                        String childname=phone+currentTime_later;

                        Freeticketdb.child(childname).updateChildren(imagedatawiththum).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    tv_ticketrdm.setText(Integer.toString(count_ticketrdm)+" free ticket");
                                    mdialog.dismiss();
                                }
                            }
                        });





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                v.setProgress(0);
                pre_win.setVisibility(View.VISIBLE);
                layout_rdm.setVisibility(View.INVISIBLE);
                startActivity(new Intent(getActivity(),MainActivity.class));

            }
        });
    }


       return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        profileimage=getActivity().findViewById(R.id.first_cirimage);
        tv_percentage=getActivity().findViewById(R.id.tv_percentage);
        name=getActivity().findViewById(R.id.name_id);
        number=getActivity().findViewById(R.id.number_id);

        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        tv_ticketrdm=getActivity().findViewById(R.id.tv_ticketrdm);




        //stapm tv initialization
        tv_scan1=getActivity().findViewById(R.id.tv_scan1);
        tv_scan2=getActivity().findViewById(R.id.tv_scan2);
        tv_scan3=getActivity().findViewById(R.id.tv_scan3);
        tv_scan4=getActivity().findViewById(R.id.tv_scan4);
        tv_scan5=getActivity().findViewById(R.id.tv_scan5);
        tv_scan6=getActivity().findViewById(R.id.tv_scan6);
       /* tv_scan7=view.findViewById(R.id.tv_scan7);
        tv_scan8=view.findViewById(R.id.tv_scan8);
        tv_scan9=view.findViewById(R.id.tv_scan9);
        tv_scan10=view.findViewById(R.id.tv_scan10);
        tv_scan11=view.findViewById(R.id.tv_scan11);
        tv_scan12=view.findViewById(R.id.tv_scan12);
        tv_scan13=view.findViewById(R.id.tv_scan13);
        tv_scan14=view.findViewById(R.id.tv_scan14);
        tv_scan15=view.findViewById(R.id.tv_scan15);*/

        //stamp img initilization
        img_scan1=getActivity().findViewById(R.id.img_scan1);
        img_scan2=getActivity().findViewById(R.id.img_scan2);
        img_scan3=getActivity().findViewById(R.id.img_scan3);
        img_scan4=getActivity().findViewById(R.id.img_scan4);
        img_scan5=getActivity().findViewById(R.id.img_scan5);
        img_scan6=getActivity() .findViewById(R.id.img_scan6);
   /*     img_scan7=view.findViewById(R.id.img_scan7);
        img_scan8=view.findViewById(R.id.img_scan8);
        img_scan9=view.findViewById(R.id.img_scan9);
        img_scan10=view.findViewById(R.id.img_scan10);
        img_scan11=view.findViewById(R.id.img_scan12);
        img_scan12=view.findViewById(R.id.img_scan12);
        img_scan13=view.findViewById(R.id.img_scan13);
        img_scan14=view.findViewById(R.id.img_scan14);
        img_scan15=view.findViewById(R.id.img_scan15);*/






        // cardview intilization

        cd1=getActivity().findViewById(R.id.cd1);
        cd2=getActivity().findViewById(R.id.cd2);
        cd3=getActivity().findViewById(R.id.cd3);
        cd4=getActivity().findViewById(R.id.cd4);
        cd5=getActivity().findViewById(R.id.cd5);
        cd6=getActivity().findViewById(R.id.cd6);
  /*      cd7=view.findViewById(R.id.cd7);
        cd8=view.findViewById(R.id.cd8);
        cd9=view.findViewById(R.id.cd9);
        cd10=view.findViewById(R.id.cd10);
        cd11=view.findViewById(R.id.cd11);
        cd12=view.findViewById(R.id.cd12);
        cd13=view.findViewById(R.id.cd13);
        cd14=view.findViewById(R.id.cd14);
        cd15=view.findViewById(R.id.cd15);
*/


        //layout initilization

        layout_rdm=getActivity().findViewById(R.id.layout_message);
        pre_win=getActivity().findViewById(R.id.layout_win_preshow);


        //progress dialog
        mdialog=new ProgressDialog(getContext().getApplicationContext());
        mdialog.setTitle("Scaning qr code");
        mdialog.setMessage("please wait.................");

        /* mDatabaseref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String qrcode= dataSnapshot.child("qrdata").getValue().toString();
                    if(qrcode.equals("nurhossen")){
                        img_scan1.setEnabled(false);
                        img_scan1.setBackgroundColor(Color.parseColor("#d3d3d3"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/



        // data=new Qr_cameraopenerActivity();
        v=getActivity().findViewById(R.id.myprogressbar);

        if(mCurrentUser!=null){

            userId= mCurrentUser.getUid();
            mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
            mDatabaseref.keepSynced(true);
            mDatabaseref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        if (isAdded()) {
                            Picasso.with(getContext()).load(image).into(profileimage);
                        }
                        String namel = dataSnapshot.child("name").getValue().toString();
                        name.setText(namel);
                        String phonel = dataSnapshot.child("phone").getValue().toString();
                        number.setText(phonel);

                        qrcode_id = dataSnapshot.child("qrdata").getValue().toString();

                        ticket1 = dataSnapshot.child("count1").getValue().toString();
                        Log.d(TAG, "onDataChange: -----------------------------------ticket1 value-------------------------------------------" + ticket1);
                        Log.d(TAG, "onDataChange: -------------------------------------qrcode id ---------------------------------------------" + qrcode_id);
                        if (ticket1.equals("avail") && qrcode_id.equals("nurhossen")) {

                            img_scan1.setEnabled(true);
                            img_scan2.setEnabled(false);
                            img_scan3.setEnabled(false);
                            img_scan4.setEnabled(false);
                            img_scan5.setEnabled(false);
                            img_scan6.setEnabled(false);

                            cd1.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));

                        } else if (ticket1.equals("over")) {
                            img_scan1.setEnabled(false);
                            img_scan2.setEnabled(true);


                            img_scan3.setEnabled(false);
                            img_scan4.setEnabled(false);
                            img_scan5.setEnabled(false);
                            img_scan6.setEnabled(false);
                            cd1.setBackgroundColor(Color.parseColor("#d3d3d3"));

                            ticket2 = dataSnapshot.child("count2").getValue().toString();

                            if (ticket2.equals("avail") && qrcode_id.equals("nurhossen")) {
                                img_scan1.setEnabled(false);
                                img_scan2.setEnabled(true);

                                img_scan3.setEnabled(false);
                                img_scan4.setEnabled(false);
                                img_scan5.setEnabled(false);
                                img_scan6.setEnabled(false);
                                cd2.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            } else if (ticket2.equals("over")) {
                                img_scan1.setEnabled(false);
                                img_scan2.setEnabled(false);

                                img_scan3.setEnabled(true);
                                img_scan4.setEnabled(false);
                                img_scan5.setEnabled(false);
                                img_scan6.setEnabled(false);
                                cd2.setCardBackgroundColor(Color.parseColor("#d3d3d3"));

                                ticket3 = dataSnapshot.child("count3").getValue().toString();

                                if (ticket3.equals("avail") && qrcode_id.equals("nurhossen")) {
                                    img_scan1.setEnabled(false);
                                    img_scan2.setEnabled(false);


                                    img_scan3.setEnabled(true);
                                    img_scan4.setEnabled(false);
                                    img_scan5.setEnabled(false);
                                    img_scan6.setEnabled(false);
                                    cd3.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                } else if (ticket3.equals("over")) {
                                    img_scan1.setEnabled(false);
                                    img_scan2.setEnabled(false);


                                    img_scan3.setEnabled(false);
                                    img_scan4.setEnabled(true);
                                    img_scan5.setEnabled(false);
                                    img_scan6.setEnabled(false);
                                    cd3.setCardBackgroundColor(Color.parseColor("#d3d3d3"));

                                    ticket4 = dataSnapshot.child("count4").getValue().toString();

                                    if (ticket4.equals("avail") && qrcode_id.equals("nurhossen")) {
                                        img_scan1.setEnabled(false);
                                        img_scan2.setEnabled(false);


                                        img_scan3.setEnabled(false);
                                        img_scan4.setEnabled(true);
                                        img_scan5.setEnabled(false);
                                        img_scan6.setEnabled(false);
                                        cd4.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    } else if (ticket4.equals("over")) {
                                        img_scan1.setEnabled(false);
                                        img_scan2.setEnabled(false);


                                        img_scan3.setEnabled(false);
                                        img_scan4.setEnabled(false);
                                        img_scan5.setEnabled(true);
                                        img_scan6.setEnabled(false);
                                        cd4.setCardBackgroundColor(Color.parseColor("#d3d3d3"));

                                        ticket5 = dataSnapshot.child("count5").getValue().toString();

                                        if (ticket5.equals("avail") && qrcode_id.equals("nurhossen")) {
                                            img_scan1.setEnabled(false);
                                            img_scan2.setEnabled(false);


                                            img_scan3.setEnabled(false);
                                            img_scan4.setEnabled(false);
                                            img_scan5.setEnabled(true);
                                            img_scan6.setEnabled(false);
                                            cd5.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        } else if (ticket5.equals("over")) {
                                            img_scan1.setEnabled(false);
                                            img_scan2.setEnabled(false);


                                            img_scan3.setEnabled(false);
                                            img_scan4.setEnabled(false);
                                            img_scan5.setEnabled(false);
                                            img_scan6.setEnabled(true);
                                            cd5.setCardBackgroundColor(Color.parseColor("#d3d3d3"));

                                            ticket6 = dataSnapshot.child("count6").getValue().toString();

                                            if (ticket6.equals("avail") && qrcode_id.equals("nurhossen")) {
                                                img_scan1.setEnabled(false);
                                                img_scan2.setEnabled(false);
                                                img_scan3.setEnabled(false);
                                                img_scan4.setEnabled(false);
                                                img_scan5.setEnabled(false);
                                                img_scan6.setEnabled(true);
                                                cd6.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                            } else if (ticket6.equals("over")) {
                                                img_scan1.setEnabled(false);
                                                img_scan2.setEnabled(false);
                                                img_scan3.setEnabled(false);
                                                img_scan4.setEnabled(false);
                                                img_scan5.setEnabled(false);
                                                img_scan6.setEnabled(false);
                                                cd6.setCardBackgroundColor(Color.parseColor("#d3d3d3"));

                                            }
                                        }


                                    }


                                }
                            }

                        }

                        count_ticketrdm =  dataSnapshot.child("ticket_redeem").getValue(Integer.class);
                        if (count_ticketrdm != 0){
                        tv_ticketrdm.setText(Integer.toString(count_ticketrdm) + "free ticket");
                        tv_ticketrdm.setVisibility(View.VISIBLE);

                        tv_ticketrdm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(getActivity().getApplicationContext(), MyFreeTicketActivity.class));
                            }
                        });
                    }
                        progressvalue=dataSnapshot.child("progressvalue_stamp").getValue(Integer.class);
                        if(progressvalue==17){
                            v.setVisibility(View.VISIBLE);
                            v.setProgress(17);
                            tv_percentage.setText("17%");
                        }
                        else if(progressvalue==34){
                            v.setVisibility(View.VISIBLE);
                            v.setProgress(34);
                            tv_percentage.setText("34%");
                        }
                        else if(progressvalue==51){
                            v.setVisibility(View.VISIBLE);
                            v.setProgress(51);
                            tv_percentage.setText("50%");
                        }
                        else if(progressvalue==68){
                            v.setVisibility(View.VISIBLE);
                            v.setProgress(68);
                            tv_percentage.setText("68%");
                        }
                        else if(progressvalue==85){
                            v.setVisibility(View.VISIBLE);
                            v.setProgress(85);
                            tv_percentage.setText("85%");
                        }
                        else if(progressvalue==100){
                            v.setVisibility(View.VISIBLE);
                            v.setProgress(100);
                            tv_percentage.setText("100%");
                            layout_rdm.setVisibility(View.VISIBLE);
                            pre_win.setVisibility(View.INVISIBLE);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


     /*   img_scan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),Qr_cameraopenerActivity.class).putExtra("identity","btn1"));


            }
        });*/


/*

        img_scan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),Qr_cameraopenerActivity.class).putExtra("identity","btn2"));

            }
        });
*/



     /*   img_scan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),Qr_cameraopenerActivity.class).putExtra("identity","btn3"));

            }
        });
*/

/*
        img_scan4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),Qr_cameraopenerActivity.class).putExtra("identity","btn4"));

            }
        });*/



      /*  img_scan5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),Qr_cameraopenerActivity.class).putExtra("identity","btn5"));


            }
        });
*/

/*

        img_scan6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),Qr_cameraopenerActivity.class).putExtra("identity","btn6"));



            }
        });
*/

        //LONG PRESS BUTTON ACTION OF SCANING START

        img_scan1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),Qr_cameraopenerActivity.class).putExtra("identity","btn1"));
                return true;
            }
        });

        img_scan2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                startActivity(new Intent(getActivity().getApplicationContext(),Qr_cameraopenerActivity.class).putExtra("identity","btn2"));

                return true;
            }
        });

        img_scan3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(),Qr_cameraopenerActivity.class).putExtra("identity","btn3"));

                return true;
            }
        });


        img_scan4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                startActivity(new Intent(getActivity().getApplicationContext(),Qr_cameraopenerActivity.class).putExtra("identity","btn4"));

                return true;
            }
        });

        img_scan5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                startActivity(new Intent(getActivity().getApplicationContext(),Qr_cameraopenerActivity.class).putExtra("identity","btn5").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                return true;
            }
        });


        img_scan6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                startActivity(new Intent(getActivity().getApplicationContext(),Qr_cameraopenerActivity.class).putExtra("identity","btn6").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                return true;
            }
        });
        //LONG PRESS BUTTON ACTION OF SCANING END



    }




    @Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);

        Bundle bundle=getArguments();
       if(bundle!=null){



           finladatafromqr=bundle.getString("data");
           identity=bundle.getString("identity");
           Log.d(TAG, "onAttach: -----------------------------------------------------------------iam from onAttach method-----------------------------------------------"+finladatafromqr);
           Log.d(TAG, "onAttach: -----------------------------------------------------------------iam from onAttach method identity value-----------------------------------------------"+identity);

               if(finladatafromqr.equals("nurhossen") && identity.equals("btn1")){
                   FirebaseUser mCurentUser =FirebaseAuth.getInstance().getCurrentUser();

                   String userId= mCurentUser.getUid();
                   mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
                   mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
                   mDatabaseref.child("count1").setValue("over").addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           Toast.makeText(getContext(), "load success", Toast.LENGTH_SHORT).show();
                           img_scan1.setEnabled(false);
                           cd1.setCardBackgroundColor(Color.parseColor("#d3d3d3"));

                           mDatabaseref.child("progressvalue_stamp").setValue(17);
                           v.setProgress(17);
                           tv_percentage.setText("17%");
                           finladatafromqr=null;

                       }
                   });
               }


           if(finladatafromqr.equals("nurhossen") && identity.equals("btn2")){
               FirebaseUser mCurentUser =FirebaseAuth.getInstance().getCurrentUser();

               String userId= mCurentUser.getUid();
               mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
               mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
               mDatabaseref.child("count2").setValue("over").addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       Toast.makeText(getContext(), "load success", Toast.LENGTH_SHORT).show();
                       img_scan2.setEnabled(false);
                       cd2.setCardBackgroundColor(Color.parseColor("#d3d3d3"));

                       mDatabaseref.child("progressvalue_stamp").setValue(34);
                       v.setProgress(34);
                       tv_percentage.setText("34%");
                       finladatafromqr=null;



                   }
               });
           }


           if(finladatafromqr.equals("nurhossen") && identity.equals("btn3")){
               FirebaseUser mCurentUser =FirebaseAuth.getInstance().getCurrentUser();

               String userId= mCurentUser.getUid();
               mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
               mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
               mDatabaseref.child("count3").setValue("over").addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       Toast.makeText(getContext(), "load success", Toast.LENGTH_SHORT).show();
                       img_scan3.setEnabled(false);
                       cd3.setCardBackgroundColor(Color.parseColor("#d3d3d3"));

                       mDatabaseref.child("progressvalue_stamp").setValue(51);
                       v.setProgress(51);
                       tv_percentage.setText("50%");
                       finladatafromqr=null;
                   }
               });
           }

           if(finladatafromqr.equals("nurhossen") && identity.equals("btn4")){
               FirebaseUser mCurentUser =FirebaseAuth.getInstance().getCurrentUser();

               String userId= mCurentUser.getUid();
               mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
               mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
               mDatabaseref.child("count4").setValue("over").addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       Toast.makeText(getContext(), "load success", Toast.LENGTH_SHORT).show();
                       img_scan4.setEnabled(false);
                       cd4.setCardBackgroundColor(Color.parseColor("#d3d3d3"));

                       mDatabaseref.child("progressvalue_stamp").setValue(68);
                       v.setProgress(68);
                       tv_percentage.setText("68%");
                       finladatafromqr=null;
                   }
               });
           }

           if(finladatafromqr.equals("nurhossen") && identity.equals("btn5")){
               FirebaseUser mCurentUser =FirebaseAuth.getInstance().getCurrentUser();

               String userId= mCurentUser.getUid();
               mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
               mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
               mDatabaseref.child("count5").setValue("over").addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       Toast.makeText(getContext(), "load success", Toast.LENGTH_SHORT).show();
                       img_scan5.setEnabled(false);
                       cd5.setCardBackgroundColor(Color.parseColor("#d3d3d3"));

                       mDatabaseref.child("progressvalue_stamp").setValue(85);
                       v.setProgress(85);
                       tv_percentage.setText("85%");
                       finladatafromqr=null;
                   }
               });
           }


           if (finladatafromqr.equals("nurhossen") && identity.equals("btn6")) {
               FirebaseUser mCurentUser = FirebaseAuth.getInstance().getCurrentUser();
               String userId = mCurentUser.getUid();
               //mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
               mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
               mDatabaseref.child("count6").setValue("over").addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {

                       //Toast.makeText(getContext(), "load success", Toast.LENGTH_SHORT).show();
                       img_scan6.setEnabled(false);
                       cd6.setCardBackgroundColor(Color.parseColor("#d3d3d3"));
                       mDatabaseref.child("progressvalue_stamp").setValue(100);
                       mDatabaseref.child("progressvalue_stamp").setValue(100);
                       v.setProgress(100);
                       tv_percentage.setText("100%");
                       finladatafromqr = null;
                       mp = MediaPlayer.create(getContext(), R.raw.yahoo_2);
                       mp.start();
                       mpp = MediaPlayer.create(getContext(), R.raw.fireworks);
                       mpp.getDuration();
                       mpp.start();
                       pre_win.setVisibility(View.INVISIBLE);
                       layout_rdm.setVisibility(View.VISIBLE);
                       status = true;
                       Toast.makeText(getContext(), "totaltime " + mpp.getDuration(), Toast.LENGTH_SHORT).show();
                       Log.d(TAG, "onComplete: ---------------------------------------------------total animation sound time(mil)-----------------------------------------------" + mpp.getDuration());
                      finladatafromqr=null;
                      identity="btn9";
                   }
               });

           }




       }
    }


}
