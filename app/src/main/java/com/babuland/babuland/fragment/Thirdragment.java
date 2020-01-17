package com.babuland.babuland.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.babuland.babuland.R;
import com.babuland.babuland.activity.LeaderboardActivity;
import com.babuland.babuland.utils.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class Thirdragment extends Fragment {


    SharedPreferences prefs;


    Button btn_a,btn_b,btn_c,btn_d;
    TextView tv_question,tv_time,tv_correctans,tv_anserstatus;
    DatabaseReference mdatabase;
    DatabaseReference Leaderboard;

    DatabaseReference Userinfodb;

    DatabaseReference quizparticipant;
    DatabaseReference admindatabase;

    int total=0;
    int correct=0;
    int wrong =0;

    Button btn_next;
    LinearLayout linearLayoutshake;
    Animation animation;
    FirebaseUser mUser;
    String mUserid;

     String name;
    String image;
    String key;

    String quzname;
    String answer;
    String staus_quiz;
    String quiznumber;


   ;


    private static final long START_TIME_INMILIS=3*60*60*1000;
    TextView timer;
    Button btn_start,btn_restart;

    private CountDownTimer mcountDownTimer;

    private Boolean mTimerrunning=false;

    private long mTimerinleft_inmils= START_TIME_INMILIS;
    public Thirdragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_thirdragment, container, false);

        linearLayoutshake=view.findViewById(R.id.linearlayout_shake);

        tv_question=view.findViewById(R.id.questionid);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


         //prefs = PreferenceManager.getDefaultSharedPreferences(getContext());


        // use a default value using new Date()


        btn_a=getActivity().findViewById(R.id.buttona);
        btn_b=getActivity().findViewById(R.id.buttonb);
        btn_c=getActivity().findViewById(R.id.buttonc);
        btn_d=getActivity().findViewById(R.id.buttond);


       // tv_correctans=getActivity().findViewById(R.id.tv_Correctans);
        tv_anserstatus=getActivity().findViewById(R.id.anserstatus);

        btn_next=getActivity().findViewById(R.id.next);
        tv_question=getActivity().findViewById(R.id.questionid);
        tv_time=getActivity().findViewById(R.id.tv_time);
         animation = AnimationUtils.loadAnimation(getActivity(),R.anim.shake);
         mUser= FirebaseAuth.getInstance().getCurrentUser();
         mUserid=mUser.getUid();

         Userinfodb=FirebaseDatabase.getInstance().getReference().child("User").child(mUserid);
         //mdatabase= FirebaseDatabase.getInstance().getReference().child("Quiz").child("Questions");


         quizparticipant=FirebaseDatabase.getInstance().getReference().child("Leaderboard").child(mUserid);
        if(isAdded()) {


            quizparticipant.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("name").exists()) {
                        key = dataSnapshot.getKey();
                        quzname = dataSnapshot.child("name").getValue().toString();
                        answer = dataSnapshot.child("answer").getValue().toString();

                        Log.d("keyu", "Updatequestion: -------------------key for check " + key);
                        Log.d("quizname", "Updatequestion: -------------------quzname for check " + quzname);

                        if (key != null && quzname != null) {
                       /*  btn_a.setEnabled(false);
                         btn_a.setBackgroundColor(Color.parseColor("#D3D3D3"));
                         btn_b.setEnabled(false);
                         btn_b.setBackgroundColor(Color.parseColor("#D3D3D3"));
                         btn_c.setEnabled(false);
                         btn_c.setBackgroundColor(Color.parseColor("#D3D3D3"));
                         btn_d.setEnabled(false);
                         btn_d.setBackgroundColor(Color.parseColor("#D3D3D3"));*/

                            tv_time.setVisibility(View.INVISIBLE);

                            // tv_anserstatus.setText("You answered this question & its correct");
                            //tv_correctans.setText("Correct Answer  " + answer);
                           // tv_correctans.setVisibility(View.VISIBLE);
                            //tv_anserstatus.setVisibility(View.VISIBLE);

                        } else {

                            btn_a.setBackgroundColor(Color.parseColor("#F57C00"));
                            btn_b.setBackgroundColor(Color.parseColor("#F57C00"));
                            btn_c.setBackgroundColor(Color.parseColor("#F57C00"));
                            btn_d.setBackgroundColor(Color.parseColor("#F57C00"));

                            btn_a.setEnabled(true);
                            btn_b.setEnabled(true);
                            btn_c.setEnabled(true);
                            btn_d.setEnabled(true);

                            tv_time.setVisibility(View.VISIBLE);
                            //tv_correctans.setVisibility(View.INVISIBLE);
                            tv_anserstatus.setVisibility(View.INVISIBLE);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            Userinfodb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        name = dataSnapshot.child("name").getValue().toString();
                        image = dataSnapshot.child("image").getValue().toString();
                        staus_quiz = dataSnapshot.child("status_quz").getValue().toString();

                        if (staus_quiz.equals("active")) {
                            btn_a.setBackgroundColor(Color.parseColor("#F57C00"));
                            btn_b.setBackgroundColor(Color.parseColor("#F57C00"));
                            btn_c.setBackgroundColor(Color.parseColor("#F57C00"));
                            btn_d.setBackgroundColor(Color.parseColor("#F57C00"));

                            btn_a.setEnabled(true);
                            btn_b.setEnabled(true);
                            btn_c.setEnabled(true);
                            btn_d.setEnabled(true);

                            Updatequestion();
                            //startCountdown();
                            tv_anserstatus.setVisibility(View.VISIBLE);
                            tv_anserstatus.setText("Quiz will End at 11:00pm");

                        } else {

                            btn_a.setEnabled(false);
                            btn_a.setBackgroundColor(Color.parseColor("#D3D3D3"));
                            btn_b.setEnabled(false);
                            btn_b.setBackgroundColor(Color.parseColor("#D3D3D3"));
                            btn_c.setEnabled(false);
                            btn_c.setBackgroundColor(Color.parseColor("#D3D3D3"));
                            btn_d.setEnabled(false);
                            btn_d.setBackgroundColor(Color.parseColor("#D3D3D3"));


                            admindatabase = FirebaseDatabase.getInstance().getReference().child("Admin");
                            admindatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("update_quiznumber").exists()) {
                                        String answeredquiznumber = dataSnapshot.child("update_quiznumber").getValue().toString();
                                        String willquizanser = dataSnapshot.child("quiznumber").getValue().toString();
                                        //quiznumber_semiglobal[1] =quiznumber_local;
                                        //getquiznumber_pre_exicute(quiznumber_semiglobal);

                                        // allwork(quiznumber_semiglobal);
                                        Log.d("anseredquiz", "onDataChange: ------------answered question " + answeredquiznumber);
                                        if (!willquizanser.equals("1")) {
                                            mdatabase = FirebaseDatabase.getInstance().getReference().child("Quiz").child("Questions").child(answeredquiznumber);

                                            anseredquiz(mdatabase);
                                        }


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            //tv_correctans.setVisibility(View.VISIBLE);
                            tv_anserstatus.setVisibility(View.VISIBLE);
                            tv_time.setVisibility(View.INVISIBLE);
                            tv_anserstatus.setText("Today Quiz will start at 9:00pm");
                            //tv_correctans.setText("Correct answer " + answer);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }




        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LeaderboardActivity.class));

            }
        });

        /*Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,21);
        calendar.set(Calendar.MINUTE,10);
        calendar.set(Calendar.SECOND,5);
        Intent scheduling_active = new Intent( getContext(), Scheduled_Fragment. class ) ;
        scheduling_active.putExtra("status","sleep") ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( getContext(), 0 , scheduling_active , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        //long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
*/
    }

    private void anseredquiz(DatabaseReference mdatabase) {

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Question question =  dataSnapshot.getValue(Question.class);
                    tv_question.setText(question.getQuestions());
                    btn_a.setText(question.getOption1());
                    btn_b.setText(question.getOption2());
                    btn_c.setText(question.getOption3());
                    btn_d.setText(question.getOption4());


                    if(btn_a.getText().toString().equals(question.getAnswer())){
                        btn_a.setBackgroundColor(Color.parseColor("#22BB33"));
                        btn_b.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        btn_c.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        btn_d.setBackgroundColor(Color.parseColor("#D3D3D3"));
                    }

                    else if(btn_b.getText().toString().equals(question.getAnswer())){

                        btn_a.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        btn_b.setBackgroundColor(Color.parseColor("#22BB33"));
                        btn_c.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        btn_d.setBackgroundColor(Color.parseColor("#D3D3D3"));
                    }

                    else if(btn_c.getText().toString().equals(question.getAnswer())){
                        btn_a.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        btn_b.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        btn_c.setBackgroundColor(Color.parseColor("#22BB33"));
                        btn_d.setBackgroundColor(Color.parseColor("#D3D3D3"));
                    }
                    else if(btn_d.getText().toString().equals(question.getAnswer())){
                        btn_a.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        btn_b.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        btn_c.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        btn_d.setBackgroundColor(Color.parseColor("#22BB33"));
                    }

                   // tv_correctans.setText("Correct answer "+question.getAnswer());
                }else {
                    Log.d("question data", "onDataChange: ----------------question data not exist ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void Updatequestion() {

        admindatabase=FirebaseDatabase.getInstance().getReference().child("Admin");
        if(isAdded()){


            admindatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("quiznumber").exists()){
                        String quiznumber_semiglobal=dataSnapshot.child("quiznumber").getValue().toString();
                        //quiznumber_semiglobal[1] =quiznumber_local;
                        //Log.d("quiznumber", "onDataChange: ---------quiznumber_thirdfragment--------" +quiznumber_semiglobal);
                        if(quiznumber_semiglobal!=null){

                            mdatabase= FirebaseDatabase.getInstance().getReference().child("Quiz").child("Questions").child(quiznumber_semiglobal);
                            mdatabase.keepSynced(true);

                            allwork(mdatabase);
                        }else {
                            Updatequestion();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



    }


    private void allwork(DatabaseReference mdatabase){




        Log.d("quiznumber", "onDataChange: ---------quiznumber_thirdfragment--------" +quiznumber);

        //total++;
        if(isAdded()){


            mdatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {



                    if(dataSnapshot.exists()){
                        Toast.makeText(getContext(), "database exist", Toast.LENGTH_SHORT).show();

                        final Question question =  dataSnapshot.getValue(Question.class);
                        tv_question.setText(question.getQuestions());
                        btn_a.setText(question.getOption1());
                        btn_b.setText(question.getOption2());
                        btn_c.setText(question.getOption3());
                        btn_d.setText(question.getOption4());
                        btn_a.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                if(btn_a.getText().toString().equals(question.getAnswer())){
                                    btn_a.setBackgroundColor(Color.parseColor("#22bb33"));
                                    MediaPlayer mediaPlayer= MediaPlayer.create(getActivity(),R.raw.correctans);
                                    mediaPlayer.start();
                                    //restartCountdown();


                                    correct=1;

                              /*  Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {


                                        Updatequestion();
                                    }
                                },1000);*/

                                    if(correct!=0){

                                        btn_a.setEnabled(false);
                                        btn_a.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        btn_b.setEnabled(false);
                                        btn_b.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        btn_c.setEnabled(false);
                                        btn_c.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        btn_d.setEnabled(false);
                                        btn_d.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        //tv_correctans.setVisibility(View.VISIBLE);
                                        tv_anserstatus.setVisibility(View.VISIBLE);
                                        tv_time.setVisibility(View.INVISIBLE);


                                        tv_anserstatus.setText("You answered this question & its correct");
                                       // tv_correctans.setText("Correct Answer  "+answer);
                                        //tv_correctans.setVisibility(View.VISIBLE);
                                        tv_anserstatus.setVisibility(View.VISIBLE);

                                        FirebaseUser mUserfrg;
                                        DatabaseReference mDatabase;
                                        String userId;
                                        mUserfrg= FirebaseAuth.getInstance().getCurrentUser();
                                        if(mUserfrg!=null) {
                                            userId = mUserfrg.getUid();
                                            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                                            mDatabase.child("status_quz").setValue("inactive");
                                        }


                                        Leaderboard=FirebaseDatabase.getInstance().getReference().child("Leaderboard").child(mUserid);
                                        Map participant = new HashMap();

                                        participant.put("name",name);
                                        participant.put("image",image);
                                        participant.put("answer",question.getAnswer());
                                        Leaderboard.setValue(participant).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getContext(), "Correct answer", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }


                                }else {
                                    correct=0;
                                    MediaPlayer mediaPlayer= MediaPlayer.create(getActivity(),R.raw.wrongans);
                                    mediaPlayer.start();
                                    Toast.makeText(getContext(), "Incorrect answer ", Toast.LENGTH_SHORT).show();
                                    wrong=wrong+1;
                                    btn_a.setBackgroundColor(Color.RED);
                                    linearLayoutshake.startAnimation(animation);

                                    if(btn_b.getText().toString().equals(question.getAnswer())){
                                        btn_b.setBackgroundColor(Color.GREEN);
                                    }else if(btn_c.getText().toString().equals(question.getAnswer())){
                                        btn_c.setBackgroundColor(Color.GREEN);
                                    }else if(btn_d.getText().toString().equals(question.getAnswer())){
                                        btn_d.setBackgroundColor(Color.GREEN);
                                    }

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            btn_a.setBackgroundColor(Color.parseColor("#F57C00"));
                                            btn_b.setBackgroundColor(Color.parseColor("#F57C00"));
                                            btn_c.setBackgroundColor(Color.parseColor("#F57C00"));
                                            btn_d.setBackgroundColor(Color.parseColor("#F57C00"));

                                            FirebaseUser mUserfrg;
                                            DatabaseReference mDatabase;
                                            String userId;
                                            mUserfrg= FirebaseAuth.getInstance().getCurrentUser();
                                            if(mUserfrg!=null) {
                                                userId = mUserfrg.getUid();
                                                mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                                                mDatabase.child("status_quz").setValue("inactive");


                                                //tv_correctans.setText("Correct Answer  "+question.getAnswer());
                                              //  tv_correctans.setVisibility(View.VISIBLE);
                                                //prefs.edit().putString("wrongforCorrectans",question.getAnswer()).apply();

                                            }
                                            //Updatequestion();
                                        }
                                    },1000);
                                }
                            }
                        });


                        btn_b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                btn_b.setBackgroundColor(Color.parseColor("#22bb33"));
                                //restartCountdown();
                                if(btn_b.getText().toString().equals(question.getAnswer())){

                                    MediaPlayer mediaPlayer= MediaPlayer.create(getActivity(),R.raw.correctans);
                                    mediaPlayer.start();

                            /*    Userinfodb.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){


                                            name = dataSnapshot.child("name").getValue().toString();
                                            image=dataSnapshot.child("image").getValue().toString();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });*/


                                    correct=1;
                           /*     Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {







                                       // Updatequestion();

                                    }
                                },1000);*/

                                    if(correct!=0){


                                        btn_a.setEnabled(false);
                                        btn_a.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        btn_b.setEnabled(false);
                                        btn_b.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        btn_c.setEnabled(false);
                                        btn_c.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        btn_d.setEnabled(false);
                                        btn_d.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        //tv_correctans.setVisibility(View.VISIBLE);
                                        tv_anserstatus.setVisibility(View.VISIBLE);
                                        tv_time.setVisibility(View.INVISIBLE);

                                        tv_anserstatus.setText("You answered this question & its correct");
                                       // tv_correctans.setText("Correct Answer  "+answer);
                                        //tv_correctans.setVisibility(View.VISIBLE);
                                        tv_anserstatus.setVisibility(View.VISIBLE);


                                        FirebaseUser mUserfrg;
                                        DatabaseReference mDatabase;
                                        String userId;
                                        mUserfrg= FirebaseAuth.getInstance().getCurrentUser();
                                        if(mUserfrg!=null) {
                                            userId = mUserfrg.getUid();
                                            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                                            mDatabase.child("status_quz").setValue("inactive");
                                        }

                                        Log.d("tagiduser", "run: --------------------------------------------muserid "+mUserid);

                                        Leaderboard=FirebaseDatabase.getInstance().getReference().child("Leaderboard").child(mUserid);
                                        Map participant = new HashMap();

                                        participant.put("name",name);
                                        participant.put("image",image);
                                        participant.put("answer",question.getAnswer());
                                        Leaderboard.setValue(participant).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getContext(), "Correct answer", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }else {
                                    correct=0;

                                    MediaPlayer mediaPlayer= MediaPlayer.create(getActivity(),R.raw.wrongans);
                                    mediaPlayer.start();
                                    linearLayoutshake.startAnimation(animation);
                                    wrong=wrong+1;
                                    btn_b.setBackgroundColor(Color.RED);
                                    if(btn_a.getText().toString().equals(question.getAnswer())){
                                        btn_a.setBackgroundColor(Color.GREEN);
                                    }else if(btn_c.getText().toString().equals(question.getAnswer())){
                                        btn_c.setBackgroundColor(Color.GREEN);
                                    }else if(btn_d.getText().toString().equals(question.getAnswer())){
                                        btn_d.setBackgroundColor(Color.GREEN);
                                    }

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            btn_a.setBackgroundColor(Color.parseColor("#F57C00"));
                                            btn_b.setBackgroundColor(Color.parseColor("#F57C00"));
                                            btn_c.setBackgroundColor(Color.parseColor("#F57C00"));
                                            btn_d.setBackgroundColor(Color.parseColor("#F57C00"));

                                            FirebaseUser mUserfrg;
                                            DatabaseReference mDatabase;
                                            String userId;
                                            mUserfrg= FirebaseAuth.getInstance().getCurrentUser();
                                            if(mUserfrg!=null) {
                                                userId = mUserfrg.getUid();
                                                mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                                                mDatabase.child("status_quz").setValue("inactive");
                                            }


                                           // tv_correctans.setText("Correct Answer  "+question.getAnswer());
                                            //tv_correctans.setVisibility(View.VISIBLE);
                                            // prefs.edit().putString("wrongforCorrectans",question.getAnswer()).apply();

                                            Updatequestion();
                                        }
                                    },1000);


                                }
                            }
                        });

                        btn_c.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //restartCountdown();
                                if(btn_c.getText().toString().equals(question.getAnswer())){
                                    btn_c.setBackgroundColor(Color.parseColor("#22bb33"));
                                    MediaPlayer mediaPlayer= MediaPlayer.create(getActivity(),R.raw.correctans);
                                    mediaPlayer.start();

/*


                                Userinfodb.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){


                                            name = dataSnapshot.child("name").getValue().toString();
                                            image=dataSnapshot.child("image").getValue().toString();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
*/

                                    correct=1;
                                /*Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {




                                                            //Updatequestion();
                                                        }
                                                    },
                                        1000);*/


                                    if(correct!=0){

                                        tv_anserstatus.setText("You answered this question & its correct");
                                        //tv_correctans.setText("Correct Answer  "+answer);
                                        //tv_correctans.setVisibility(View.VISIBLE);
                                        tv_anserstatus.setVisibility(View.VISIBLE);


                                        btn_a.setEnabled(false);
                                        btn_a.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        btn_b.setEnabled(false);
                                        btn_b.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        btn_c.setEnabled(false);
                                        btn_c.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        btn_d.setEnabled(false);
                                        btn_d.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        //tv_correctans.setVisibility(View.VISIBLE);
                                        tv_anserstatus.setVisibility(View.VISIBLE);
                                        tv_time.setVisibility(View.INVISIBLE);

                                        FirebaseUser mUserfrg;
                                        DatabaseReference mDatabase;
                                        String userId;
                                        mUserfrg= FirebaseAuth.getInstance().getCurrentUser();
                                        if(mUserfrg!=null) {
                                            userId = mUserfrg.getUid();
                                            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                                            mDatabase.child("status_quz").setValue("inactive");
                                        }



                                        Log.d("tagiduser", "run: --------------------------------------------muserid "+mUserid);
                                        Leaderboard=FirebaseDatabase.getInstance().getReference().child("Leaderboard").child(mUserid);
                                        Map participant = new HashMap();

                                        participant.put("name",name);
                                        participant.put("image",image);
                                        participant.put("answer",question.getAnswer());
                                        Leaderboard.setValue(participant).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getContext(), "Correct answer", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }



                                }else {
                                    correct=0;
                                    MediaPlayer mediaPlayer= MediaPlayer.create(getActivity(),R.raw.wrongans);
                                    mediaPlayer.start();
                                    linearLayoutshake.startAnimation(animation);
                                    wrong++;
                                    btn_c.setBackgroundColor(Color.RED);
                                    if(btn_b.getText().toString().equals(question.getAnswer())){
                                        btn_b.setBackgroundColor(Color.GREEN);
                                    }else if(btn_a.getText().toString().equals(question.getAnswer())){
                                        btn_a.setBackgroundColor(Color.GREEN);
                                    }else if(btn_d.getText().toString().equals(question.getAnswer())){
                                        btn_d.setBackgroundColor(Color.GREEN);
                                    }

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            btn_a.setBackgroundColor(Color.parseColor("#F57C00"));
                                            btn_b.setBackgroundColor(Color.parseColor("#F57C00"));
                                            btn_c.setBackgroundColor(Color.parseColor("#F57C00"));
                                            btn_d.setBackgroundColor(Color.parseColor("#F57C00"));

                                            FirebaseUser mUserfrg;
                                            DatabaseReference mDatabase;
                                            String userId;
                                            mUserfrg= FirebaseAuth.getInstance().getCurrentUser();
                                            if(mUserfrg!=null) {
                                                userId = mUserfrg.getUid();
                                                mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                                                mDatabase.child("status_quz").setValue("inactive");

                                               // tv_correctans.setText("Correct Answer  "+question.getAnswer());
                                               // tv_correctans.setVisibility(View.VISIBLE);
                                                //prefs.edit().putString("wrongforCorrectans",question.getAnswer()).apply();
                                            }


                                            //Updatequestion();
                                        }

                                    },1000);

                                }
                            }
                        });


                        btn_d.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //restartCountdown();
                                if(btn_d.getText().toString().equals(question.getAnswer())){
                                    btn_d.setBackgroundColor(Color.parseColor("#22bb33"));
                                    MediaPlayer mediaPlayer= MediaPlayer.create(getActivity(),R.raw.correctans);
                                    mediaPlayer.start();
/*
                                Userinfodb.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){


                                            name = dataSnapshot.child("name").getValue().toString();
                                            image=dataSnapshot.child("image").getValue().toString();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });*/
                                    correct=1;
                            /*    Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {



                                       // Updatequestion();
                                    }
                                },1000);*/

                                    if(correct!=0){

                                        tv_anserstatus.setText("You answered this question & its correct");
                                        //tv_correctans.setText("Correct Answer  "+answer);
                                        //tv_correctans.setVisibility(View.VISIBLE);
                                        tv_anserstatus.setVisibility(View.VISIBLE);

                                        btn_a.setEnabled(false);
                                        btn_a.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        btn_b.setEnabled(false);
                                        btn_b.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        btn_c.setEnabled(false);
                                        btn_c.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        btn_d.setEnabled(false);
                                        btn_d.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                        //tv_correctans.setVisibility(View.VISIBLE);
                                        tv_anserstatus.setVisibility(View.VISIBLE);
                                        tv_time.setVisibility(View.INVISIBLE);

                                        FirebaseUser mUserfrg;
                                        DatabaseReference mDatabase;
                                        String userId;
                                        mUserfrg= FirebaseAuth.getInstance().getCurrentUser();
                                        if(mUserfrg!=null) {
                                            userId = mUserfrg.getUid();
                                            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                                            mDatabase.child("status_quz").setValue("inactive");
                                        }

                                        Leaderboard=FirebaseDatabase.getInstance().getReference().child("Leaderboard").child(mUserid);
                                        Map participant = new HashMap();

                                        participant.put("name",name);
                                        participant.put("image",image);
                                        participant.put("answer",question.getAnswer());

                                        Leaderboard.setValue(participant).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getContext(), "Correct answer", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                }else {
                                    correct=0;
                                    MediaPlayer mediaPlayer= MediaPlayer.create(getActivity(),R.raw.wrongans);
                                    mediaPlayer.start();
                                    linearLayoutshake.startAnimation(animation);
                                    wrong++;
                                    btn_d.setBackgroundColor(Color.RED);
                                    if(btn_b.getText().toString().equals(question.getAnswer())){
                                        btn_b.setBackgroundColor(Color.GREEN);
                                    }else if(btn_c.getText().toString().equals(question.getAnswer())){
                                        btn_c.setBackgroundColor(Color.GREEN);
                                    }else if(btn_a.getText().toString().equals(question.getAnswer())){
                                        btn_a.setBackgroundColor(Color.GREEN);
                                    }

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            btn_a.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                            btn_b.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                            btn_c.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                            btn_d.setBackgroundColor(Color.parseColor("#D3D3D3"));

                                            FirebaseUser mUserfrg;
                                            DatabaseReference mDatabase;
                                            String userId;
                                            mUserfrg= FirebaseAuth.getInstance().getCurrentUser();
                                            if(mUserfrg!=null) {
                                                userId = mUserfrg.getUid();
                                                mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                                                mDatabase.child("status_quz").setValue("inactive");
                                                //tv_correctans.setText("Correct Answer  "+question.getAnswer());
                                                //tv_correctans.setVisibility(View.VISIBLE);
                                                //prefs.edit().putString("wrongforCorrectans",question.getAnswer()).apply();
                                            }
                                            //Updatequestion();
                                        }
                                    },1000);
                                }
                            }
                        });

                    }else {
                        Log.d("dsp", "onDataChange: ..........data snapshort dont exist ");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }


  /*  public void startCountdown(){

        mcountDownTimer = new CountDownTimer(mTimerinleft_inmils,100) {
            @Override
            public void onTick(long l) {

                mTimerinleft_inmils=l;
                UpdateCountdowntext();
            }

            @Override
            public void onFinish() {
                btn_a.setEnabled(false);
                btn_b.setEnabled(false);
                btn_c.setEnabled(false);
                btn_d.setEnabled(false);

                btn_a.setBackgroundColor(Color.parseColor("#D3D3D3"));
                btn_b.setBackgroundColor(Color.parseColor("#D3D3D3"));
                btn_c.setBackgroundColor(Color.parseColor("#D3D3D3"));
                btn_d.setBackgroundColor(Color.parseColor("#D3D3D3"));
                mTimerrunning=false;
            }
        }.start();
    }*/
/*
    public  void restartCountdown(){

        mcountDownTimer.cancel();
        mTimerinleft_inmils=START_TIME_INMILIS;

        mcountDownTimer= new CountDownTimer(mTimerinleft_inmils,100) {
            @Override
            public void onTick(long l) {
                mTimerinleft_inmils=l;
                UpdateCountdowntext();
            }

            @Override
            public void onFinish() {

                btn_a.setEnabled(false);
                btn_b.setEnabled(false);
                btn_c.setEnabled(false);
                btn_d.setEnabled(false);
                btn_a.setBackgroundColor(Color.parseColor("#D3D3D3"));
                btn_b.setBackgroundColor(Color.parseColor("#D3D3D3"));
                btn_c.setBackgroundColor(Color.parseColor("#D3D3D3"));
                btn_d.setBackgroundColor(Color.parseColor("#D3D3D3"));
                mTimerrunning=false;


            }
        }.start();
        mTimerrunning=true;

    }*/


  /*  private void UpdateCountdowntext() {

        *//*int minitues = (int) ((mTimerinleft_inmils/1000)/60);
        int seconds = (int) ((mTimerinleft_inmils/1000)%60);*//*

       // long secondss= TimeUnit.MICROSECONDS.toSeconds(mTimerinleft_inmils);

        int secondss = (int) (mTimerinleft_inmils / 1000) % 60 ;
        int minutes = (int) ((mTimerinleft_inmils / (1000*60)) % 60);
        int hours   = (int) ((mTimerinleft_inmils / (1000*60*60)) % 24);


        if(secondss<5){

            tv_time.setTextColor(Color.parseColor("#bb2124"));
        }else {
            tv_time.setTextColor(Color.parseColor("#43A047"));
        }
        String timerformated= String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,secondss);

        tv_time.setText(timerformated);
    }*/



    private void getquiznumber_pre_exicute(String quiznumber){


        mdatabase= FirebaseDatabase.getInstance().getReference().child("Quiz").child("Questions").child(quiznumber);
        //  total++;
        mdatabase.keepSynced(true);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    Question questions = dataSnapshot.getValue(Question.class);
                    tv_question.setText(questions.getQuestions());
                    btn_a.setText(questions.getOption1());
                    btn_b.setText(questions.getOption2());
                    btn_c.setText(questions.getOption3());
                    btn_d.setText(questions.getOption4());


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
