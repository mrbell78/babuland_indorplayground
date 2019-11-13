package com.example.babuland1.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babuland1.R;
import com.example.babuland1.activity.AccountSettingsActivity;
import com.example.babuland1.activity.QuizresultActivity;
import com.example.babuland1.utils.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class Thirdragment extends Fragment {




    Button btn_a,btn_b,btn_c,btn_d;
    TextView tv_question,tv_time;
    DatabaseReference mdatabase;
    int total=1;
    int correct=0;
    int wrong =0;

    Button btn_next;
    LinearLayout linearLayoutshake;
    Animation animation;

   ;


    private static final long START_TIME_INMILIS=20000;
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

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_a=getActivity().findViewById(R.id.buttona);
        btn_b=getActivity().findViewById(R.id.buttonb);
        btn_c=getActivity().findViewById(R.id.buttonc);
        btn_d=getActivity().findViewById(R.id.buttond);

        btn_next=getActivity().findViewById(R.id.next);
        tv_question=getActivity().findViewById(R.id.questionid);
        tv_time=getActivity().findViewById(R.id.tv_time);
         animation = AnimationUtils.loadAnimation(getActivity(),R.anim.shake);

        Updatequestion();
        startCountdown();


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Updatequestion();
                restartCountdown();
                btn_a.setEnabled(true);
                btn_b.setEnabled(true);
                btn_c.setEnabled(true);
                btn_d.setEnabled(true);
            }
        });
    }



    private void Updatequestion() {


        btn_a.setBackgroundColor(Color.parseColor("#F57C00"));
        btn_b.setBackgroundColor(Color.parseColor("#F57C00"));
        btn_c.setBackgroundColor(Color.parseColor("#F57C00"));
        btn_d.setBackgroundColor(Color.parseColor("#F57C00"));


        if(total>7){
            //open result activity
            startActivity(new Intent(getActivity(), QuizresultActivity.class).putExtra("total",total).putExtra("correctans",correct));
            total=1;
        }else{

            mdatabase= FirebaseDatabase.getInstance().getReference().child("Quiz").child("Questions").child(String.valueOf(total));
            total++;
            mdatabase.keepSynced(true);
            mdatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
                                restartCountdown();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;
                                        Updatequestion();
                                    }
                                },1000);


                            }else {
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
                                        Updatequestion();
                                    }
                                },1000);
                            }
                        }
                    });


                    btn_b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            btn_b.setBackgroundColor(Color.parseColor("#22bb33"));
                            restartCountdown();
                            if(btn_b.getText().toString().equals(question.getAnswer())){

                                MediaPlayer mediaPlayer= MediaPlayer.create(getActivity(),R.raw.correctans);
                                mediaPlayer.start();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;



                                        Updatequestion();

                                    }
                                },1000);
                            }else {

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


                                        Updatequestion();
                                    }
                                },1000);


                            }
                        }
                    });

                    btn_c.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            restartCountdown();
                            if(btn_c.getText().toString().equals(question.getAnswer())){
                                btn_c.setBackgroundColor(Color.parseColor("#22bb33"));
                                MediaPlayer mediaPlayer= MediaPlayer.create(getActivity(),R.raw.correctans);
                                mediaPlayer.start();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            correct++;

                                                            Updatequestion();
                                                        }
                                                    },
                                        1000);
                            }else {
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


                                        Updatequestion();
                                    }

                                },1000);

                            }
                        }
                    });


                    btn_d.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            restartCountdown();
                            if(btn_d.getText().toString().equals(question.getAnswer())){
                                btn_d.setBackgroundColor(Color.parseColor("#22bb33"));
                                MediaPlayer mediaPlayer= MediaPlayer.create(getActivity(),R.raw.correctans);
                                mediaPlayer.start();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;



                                        Updatequestion();


                                    }
                                },1000);
                            }else {
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
                                        Updatequestion();
                                    }
                                },1000);
                            }
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    public void startCountdown(){

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
    }

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

    }


    private void UpdateCountdowntext() {

        int minitues = (int) ((mTimerinleft_inmils/1000)/60);
        int seconds = (int) ((mTimerinleft_inmils/1000)%60);

        if(seconds<5){

            tv_time.setTextColor(Color.parseColor("#bb2124"));
        }else {
            tv_time.setTextColor(Color.parseColor("#43A047"));
        }
        String timerformated= String.format(Locale.getDefault(),"%02d:%02d",minitues,seconds);

        tv_time.setText(timerformated);
    }
}
