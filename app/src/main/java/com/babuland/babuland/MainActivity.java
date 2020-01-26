package com.babuland.babuland;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.babuland.babuland.activity.AboutBabulandActivity;
import com.babuland.babuland.activity.AccountSettingsActivity;
import com.babuland.babuland.activity.MyFreeTicketActivity;
import com.babuland.babuland.activity.MyeTicketActivity;
import com.babuland.babuland.activity.Qr_cameraopenerActivity;
import com.babuland.babuland.activity.ReviewActivity;
import com.babuland.babuland.activity.WelcomeActivity;
import com.babuland.babuland.fragment.FirstFragment;
import com.babuland.babuland.fragment.ForthFragment;
import com.babuland.babuland.fragment.Secondragment;
import com.babuland.babuland.fragment.Thirdragment;
import com.babuland.babuland.utils.BroadcastService;
import com.babuland.babuland.utils.DbHelper;
import com.babuland.babuland.utils.ScheduledQuiz_stop;
import com.babuland.babuland.utils.Scheduling_quiz;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements Qr_cameraopenerActivity.qrSendata, FirstFragment.homefragmentlistener, NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth mAuth;
    private FirebaseUser muser;
    private Toolbar mToolbar;
    private TextView tv_displayname;
    private CircleImageView profileimage;
    private String  []name_reviewitem;
    private String  name;
    private String image;
    private static final String TAG = "MainActivity";
    FirstFragment homefragment;
    Secondragment explorefragment;
    Thirdragment accountfragment;
    ForthFragment offersfragment;
    private String identity;
    SupportMapFragment mapFragment;
    String name_fromfirebase;
    String phone;
    String imageuri;
    int ticket_pTime;

    public String finaldatafromcamera;


    private CountDownTimer mcountDownTimer;
    private static final long START_TIME_INMILIS=10000;
    private long mTimerinleft_inmils= START_TIME_INMILIS;
    DrawerLayout drawerLayout;



    ActionBarDrawerToggle mToggle;
    private BottomNavigationView bottomNavigationView;

    private FrameLayout frameLayout;


    private  NavigationView navigationView;

    int  value;
    int firsttime;

    DatabaseReference admindatabase,mDatabase_q,leaderboard_database;
    Integer startTime,endTime,startminitue,endminitue;
    String quiznumber;
    String concent;

    DbHelper helper;
    FirebaseUser mUserr;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        mToolbar=(Toolbar)findViewById(R.id.rederingtoolbarmain);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        bottomNavigationView=findViewById(R.id.manin_bottomnavigationview);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        frameLayout=findViewById(R.id.main_framlayout);
        navigationView=findViewById(R.id.navigatgationview);
        navigationView.setNavigationItemSelectedListener(this);




        admindatabase=FirebaseDatabase.getInstance().getReference().child("Admin");
        mDatabase_q=FirebaseDatabase.getInstance().getReference().child("User");

        admindatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("editmode").exists()){

                    concent=dataSnapshot.child("editmode").getValue().toString();





                    Log.d(TAG, "onCreate: ---------concent value "+concent);
                    Log.d(TAG, "onCreate: ------------before edit statrt "+ startTime);
                    Log.d(TAG, "onCreate: ------------before edit end "+ endTime);



                    if(concent.equals("yes") ){

                        Toast.makeText(MainActivity.this, "concetvalue "+ concent, Toast.LENGTH_SHORT).show();
                        mUserr=FirebaseAuth.getInstance().getCurrentUser();
                        final String userid = mUserr.getUid();
                        mDatabase_q.child(userid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){

                                    final String useralrm = dataSnapshot.child("alrmstatus").getValue().toString();
                                    Log.d(TAG, "onDataChange: --------------------------alrmvalue fetch");

                                    final String activiequiz=dataSnapshot.child("status_quz").getValue().toString();


                                    if(useralrm.equals("false")){
                                        admindatabase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                Log.d(TAG, "onDataChange: ----------------------------alrmvalue status "+ useralrm);


                                                if(dataSnapshot.child("Qtimestart").exists() &&  dataSnapshot.child("Qtimeend").exists()){

                                                    Log.d(TAG, "onDataChange: ---------------inside setalrm");

                                                    startTime=dataSnapshot.child("Qtimestart").getValue(Integer.class);
                                                    endTime=dataSnapshot.child("Qtimeend").getValue(Integer.class);
                                                    startminitue=dataSnapshot.child("Qstartmnt").getValue(Integer.class);
                                                    endminitue=dataSnapshot.child("Qendmnt").getValue(Integer.class);



                                                    Log.d(TAG, "onCreate: ------------in main activity statrt "+ startTime);
                                                    Log.d(TAG, "onCreate: ------------in main activity end "+ endTime);



                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.set(Calendar.HOUR_OF_DAY,startTime);
                                                    calendar.set(Calendar.MINUTE,startminitue);
                                                    calendar.set(Calendar.SECOND,5);


                                                    Calendar calendar_stop= Calendar.getInstance();
                                                    calendar_stop.set(Calendar.HOUR_OF_DAY,endTime);
                                                    calendar_stop.set(Calendar.MINUTE,endminitue);
                                                    calendar_stop.setTimeInMillis(System.currentTimeMillis());
                                                    calendar_stop.set(Calendar.SECOND,10);


                                                    Log.d(TAG, "onDataChange: .............starttime "+startTime);
                                                    Log.d(TAG, "onDataChange: .............startminitue "+startminitue);
                                                    Log.d(TAG, "onDataChange: .............endtime "+endTime);
                                                    Log.d(TAG, "onDataChange: .............endminute "+endminitue);

                                                    if(activiequiz.equals("active")){

                                                        scheduliedquiz_stop(calendar_stop);

                                                    }else if(activiequiz.equals("inactive")){

                                                        scheduleNotification(getNotification("Dont miss out todays quiz"),100,calendar);

                                                    }



                                                    //admindatabase.child("editmode").setValue("no");

                                                    mUserr=FirebaseAuth.getInstance().getCurrentUser();
                                                     String userid = mUserr.getUid();
                                                    mDatabase_q.child(userid).child("alrmstatus").setValue("true");

                                                }else {
                                                    Log.d(TAG, "onDataChange: ---------no data found");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }


                                }else {
                                    Log.d(TAG, "onDataChange: -------------------alarm status not found");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                Log.d(TAG, "onCancelled: ----------------if condition not satisfied bcz this error "+ databaseError.getMessage());

                            }
                        });





                    }else {
                        Log.d(TAG, "onDataChange: ---------------yes not found");
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Intent intent = getIntent();

        name=intent.getStringExtra("fullname");
        Log.d(TAG, "onCreate: --------------------------------------------------name---------------------------------------"+name);
         image = intent.getStringExtra("profileimage");

        homefragment=new FirstFragment();
        explorefragment= new Secondragment();
        accountfragment=new Thirdragment();
        offersfragment=new ForthFragment();


        //header profile settings start

        navHeaderView=  navigationView.inflateHeaderView(R.layout.header_layout);
        header_name =  navHeaderView.findViewById(R.id.header_textview_name);
        header_number =  navHeaderView.findViewById(R.id.header_textview_phone);
        headerlayout=navHeaderView.findViewById(R.id.header_fullid);


        header_proimg=navHeaderView.findViewById(R.id.header_proimg);

        mUser=FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null){
            userId=mUser.getUid();
            mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(userId);
            mDatabase.keepSynced(true);



            //data fetch start

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        name_fromfirebase=dataSnapshot.child("name").getValue().toString();
                        phone=dataSnapshot.child("phone").getValue().toString();
                        imageuri = dataSnapshot.child("image").getValue().toString();
                        //ticket_pTime=dataSnapshot.child("nof_purchase_time").getValue(Integer.class);
                        if(ticket_pTime!=0){
                            firsttime=1;
                            Log.d(TAG, "onDataChange: -----------------------------------------------service started again "+ isMyServiceRunning(BroadcastService.class));
                            startService(new Intent(MainActivity.this, BroadcastService.class));
                        }
                        header_name.setText(name_fromfirebase);
                        header_number.setText(phone);
                        Picasso.with(MainActivity.this).load(imageuri).placeholder(R.drawable.defaultpic).error(R.drawable.defaultpic).into(header_proimg);
                        headerlayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getApplicationContext(),AccountSettingsActivity.class));
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            //data fetch end




        }else{
            Log.d(TAG, "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }



        // header profile seting end


        Intent i =getIntent();
        finaldatafromcamera=i.getStringExtra("dataofqr");
        identity=i.getStringExtra("identity");

        //Toast.makeText(this, "inMainactivity "+finaldatafromcamera, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate: -------------------------------------------------------------------qrdata from camera in main activity ---------------------------------------------------------------"+finaldatafromcamera);
        if(finaldatafromcamera!=null ){
            Bundle bundle = new Bundle();
            bundle.putString("data",finaldatafromcamera);
            bundle.putString("identity",identity);
            homefragment.setArguments(bundle);

        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

              switch (menuItem.getItemId()){
                  case R.id.offer:
                      replacefragment(homefragment);
                      return true;
                  case R.id.explore:
                      replacefragment(explorefragment);
                      return true;
                  case R.id.quiz:
                      replacefragment(accountfragment);
                      return true;
                  case R.id.home:
                      replacefragment(offersfragment);
                      return true;
                      default: return false;
              }
          }
      });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.home);

        }
        String quizfragment = getIntent().getStringExtra("quizfragment");
        if(quizfragment!=null){

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if(quizfragment.equals("qauiz")){

                /*Thirdragment quizfrg = new Thirdragment();
                fragmentTransaction.replace(R.id.hudai,quizfrg);
                fragmentTransaction.commit();*/

                bottomNavigationView.setSelectedItemId(R.id.quiz);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout= findViewById(R.id.drawerlayout);
        mToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
    }

    private void replacefragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_framlayout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {

        super.onStart();
           FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
           //String userid_local = currentUser.getUid();
          // String userid=currentUser.getUid();
           if(currentUser==null || currentUser.getUid()==null){
               //sendToAccountSettings();
               //finish();
               //Toast.makeText(this, "u can stay here for developoing purpose", Toast.LENGTH_SHORT).show();
               sendTologin();
               Log.d(TAG, "onStart: ================user_locla "+ currentUser);
           }


    }

    private void sendTologin() {
        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(mToggle.onOptionsItemSelected(item)){
            drawerLayout.setVisibility(View.VISIBLE);
            return true;
        }
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                return true;
            case R.id.aboutus:
                sendTobabuland();
                return true;

            case R.id.setting:
                sendToAccountSettings();
                return true;
            default:
                return false;

        }
    }

    private void sendTobabuland() {
        MediaPlayer mp =  MediaPlayer.create(this,R.raw.notification_babuland);
        mp.start();
        startActivity(new Intent(getApplicationContext(), AboutBabulandActivity.class));
    }

    private void sendToMyeTicket() {
        startActivity(new Intent(getApplicationContext(), MyeTicketActivity.class));

    }

    private void referFriend() {


        Log.e("main", "create link ");

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.babuland.com/"))
                .setDynamicLinkDomain("babulandrefer78.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                //.setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();
//click -- link -- google play store -- inistalled/ or not  ----
        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e("main", "  Long refer "+ dynamicLink.getUri());
        //   https://referearnpro.page.link?apn=blueappsoftware.referearnpro&link=https%3A%2F%2Fwww.blueappsoftware.com%2F
        // apn  ibi link

        createReferlink("kamal123", "prod456");
    }

    private void createReferlink(String custid, String prodid) {


        // manuall link
        String sharelinktext  = "https://babulandrefer78.page.link/?"+
                "link=http://www.babuland.com/myrefer.php?custid="+custid +"-"+prodid+
                "&apn="+ getPackageName()+
                "&st="+"My Refer Link"+
                "&sd="+"Reward Coins 20"+
                "&si="+"https://www.blueappsoftware.com/logo-1.png";

        Log.e("mainactivity", "sharelink - "+sharelinktext);
        Log.d("sharedlink", "createReferlink: -----------------------------------------------------------"+sharelinktext);
        // shorten the link
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                //.setLongLink(dynamicLink.getUri())    // enable it if using firebase method dynamicLink
                .setLongLink(Uri.parse(sharelinktext))  // manually
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            //Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.e("main ", "short link "+ shortLink.toString());
                            //share app dialog
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT,  shortLink.toString());
                            intent.setType("text/plain");
                            startActivity(intent);

                }
                        else {
                            // Error
                            // ...
                            Log.e("main", " error "+task.getException() );

                        }
                    }
                });
    }

    private void sendToAccountSettings() {
        startActivity(new Intent(getApplicationContext(), AccountSettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }


    private void logout() {

        mAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Toast.makeText(this, "you signed out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
        finish();
    }


    @Override
    public void validationofdata(String data) {

      /* if(data!=null){
           homefragment.UpdateEdittext(data);
       }
*/
    }
    @Override
    public void Inputsent(String qrdata) {
        Toast.makeText(this, "what i should i ned to do .. i dont know that", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.branch:
                bottomNavigationView.setSelectedItemId(R.id.explore);
                Toast.makeText(this, "branch is selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.comment:
               dialogbox();
               break;
            case R.id.eticket:
                sendToMyeTicket();
                return true;
            case R.id.eticket_free:
                sendToMyFreeTicket();
                return true;
            case R.id.logout:
                logout();
                break;
            case R.id.refer:
                referFriend();
                break;
            case R.id.cservice:
                openDialar();
                break;
                default:
                    bottomNavigationView.setSelectedItemId(R.id.home);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
       return true;
    }

    private void openDialar() {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:+8801313 428 423"));
        startActivity(intent);
    }

    private void sendToMyFreeTicket() {
        startActivity(new Intent(getApplicationContext(), MyFreeTicketActivity.class));
    }

    private void shareapp() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "http://babuland.com";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    public void dialogbox() {

        AlertDialog.Builder albuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.custom_dialogbox_main,null);

        albuilder.setView(view);
        final AlertDialog dialog = albuilder.create();
        dialog.show();

        Button btn_ok=view.findViewById(R.id.btn_dialogbox_main_ok);
        Button btn_cancel=view.findViewById(R.id.btn_dialogbox_main_cancel);
        final Button btn_washroom=view.findViewById(R.id.btn_washroom);
        Button btn_pga=view.findViewById(R.id.btn_pgaservice);
        Button btn_food=view.findViewById(R.id.foodquality);
        Button btn_hygin=view.findViewById(R.id.hygin);
        Button btn_others=view.findViewById(R.id.other);
        final LinearLayout llayout = view.findViewById(R.id.noteview);

        final EditText edt_comment=view.findViewById(R.id.edt_comment);

        btn_washroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                params.setMargins(0, 15, 0, 0);
                llayout.setLayoutParams(params);
/*
                llayout.setVisibility(View.VISIBLE);
                llayout.setAlpha(0.0f);

// Start the animation
                llayout.animate()
                        .translationY(llayout.getHeight())
                        .alpha(1.5f)
                        .setListener(null);*/


                llayout.setVisibility(View.VISIBLE);
                TranslateAnimation animation = new TranslateAnimation(0,0,llayout.getHeight(),0);
                animation.setDuration(500);
                llayout.startAnimation(animation);
                edt_comment.setHint("Will you please highlights the issues regarding wash room");
            }
        });
        btn_pga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 15, 0, 0);
                llayout.setLayoutParams(params);
                llayout.setVisibility(View.VISIBLE);
                TranslateAnimation animation = new TranslateAnimation(0,0,llayout.getHeight(),0);
                animation.setDuration(500);
                llayout.startAnimation(animation);
                edt_comment.setHint("Please tell us your experience with pga");
            }
        });
        btn_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 15, 0, 0);
                llayout.setLayoutParams(params);
                llayout.setVisibility(View.VISIBLE);
                TranslateAnimation animation = new TranslateAnimation(0,0,llayout.getHeight(),0);
                animation.setDuration(500);
                llayout.startAnimation(animation);
                edt_comment.setHint("what was our food quality? U can tell us your choice");
            }
        });
        btn_hygin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 15, 0, 0);
                llayout.setLayoutParams(params);
                llayout.setVisibility(View.VISIBLE);
                TranslateAnimation animation = new TranslateAnimation(0,0,llayout.getHeight(),0);
                animation.setDuration(500);
                llayout.startAnimation(animation);
                edt_comment.setHint("Sanitation issue? Please tell us");
            }
        });

        btn_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 15, 0, 0);
                llayout.setLayoutParams(params);
                llayout.setVisibility(View.VISIBLE);
                TranslateAnimation animation = new TranslateAnimation(0,0,llayout.getHeight(),0);
                animation.setDuration(500);
                llayout.startAnimation(animation);
                edt_comment.setHint("Say anything about us");
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment =edt_comment.getText().toString();
                if(!TextUtils.isEmpty(comment)) {
                    mUser=FirebaseAuth.getInstance().getCurrentUser();
                    userId=mUser.getUid();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

                    String key = mDatabase.child("User").push().getKey();
                    mDatabase.child("comment").setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                llayout.setVisibility(View.INVISIBLE);
                            }else{
                                Toast.makeText(MainActivity.this, "some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else if(TextUtils.isEmpty(comment)){
                        Toast.makeText(MainActivity.this, "Comment is empty", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Toast.makeText(MainActivity.this, "comment canceled", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
            }
        });

    }

    private void dialogbox_review(){
        AlertDialog.Builder albuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.reviewus_popup,null);
        albuilder.setView(view);
        final AlertDialog dialog = albuilder.create();
       if(!isFinishing()){
           dialog.show();
       }

        RatingBar ratingBar = view.findViewById(R.id.ratingbar_reviewmain);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                startActivity(new Intent(getApplicationContext(),ReviewActivity.class));
                finish();
                dialog.dismiss();
            }
        });
    }


//header profile setting

    ImageView header_proimg;
    TextView header_name;
    TextView header_number;
    LinearLayout headerlayout;

    private FirebaseUser mUser;
    private String userId;
    private DatabaseReference mDatabase;
    View navHeaderView;

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           // updateGUI(intent); // or whatever method used to update your GUI fields



            //int value =  intent.getIntExtra("countdown",0);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
              value = preferences.getInt("timer",0);
            Log.d(TAG, "onReceive: ------------------------------------value recived==="+value);
            if(value==0){

                dialogbox_review();
            }

        }
    };

   /* @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        Log.i(TAG, "Registered broacast receiver");
    }*/



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }*/


    private void scheduleNotification (Notification notification , int delay,Calendar calendar) {



        Intent notificationIntent = new Intent( this, Scheduling_quiz. class ) ;
        notificationIntent.putExtra(Scheduling_quiz. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(Scheduling_quiz. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent.FLAG_UPDATE_CURRENT) ;
        //long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        //alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent);

    }

    private Notification getNotification (String content) {

        Uri soundUri =  Uri.parse(String.valueOf(R.raw.notification_babuland));

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("quizfragment","qauiz");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentIntent(pendingIntent);
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable.babuland ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        builder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.babulandlogo));
        builder.setSound(soundUri);

        Notification notification = builder.build();

        return notification ;
    }

    private void scheduliedquiz_stop(final Calendar calendar_stop) {

        final Intent notificationIntent = new Intent( this, ScheduledQuiz_stop. class ) ;
        DatabaseReference admindatabaseforquestion;


        admindatabaseforquestion=FirebaseDatabase.getInstance().getReference().child("Admin");

        admindatabaseforquestion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("quiznumber").exists()){

                 String question_online = dataSnapshot.child("quiznumber").getValue().toString();
                 notificationIntent.putExtra("quiznumber",question_online);
                    PendingIntent pendingIntent = PendingIntent. getBroadcast ( MainActivity.this, 0 , notificationIntent ,PendingIntent.FLAG_UPDATE_CURRENT ) ;
                    //long futureInMillis = SystemClock. elapsedRealtime () + delay ;
                    AlarmManager alarmManagerr = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
                    assert alarmManagerr != null;
                    alarmManagerr.setRepeating(AlarmManager.RTC_WAKEUP, calendar_stop.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

}
