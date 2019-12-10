package com.babuland.babuland1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;



import com.babuland.babuland1.activity.AccountSettingsActivity;
import com.babuland.babuland1.activity.MyFreeTicketActivity;
import com.babuland.babuland1.activity.MyeTicketActivity;
import com.babuland.babuland1.activity.Qr_cameraopenerActivity;
import com.babuland.babuland1.activity.ReviewActivity;
import com.babuland.babuland1.activity.WelcomeActivity;
import com.babuland.babuland1.fragment.FirstFragment;
import com.babuland.babuland1.fragment.ForthFragment;
import com.babuland.babuland1.fragment.Secondragment;
import com.babuland.babuland1.fragment.Thirdragment;
import com.babuland.babuland1.utils.BroadcastService;
import com.babuland.babuland1.utils.DbHelper;

import com.babuland.babuland1.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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


    DbHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        mToolbar=(Toolbar)findViewById(R.id.rederingtoolbarmain);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        bottomNavigationView=findViewById(R.id.manin_bottomnavigationview);
        frameLayout=findViewById(R.id.main_framlayout);
        navigationView=findViewById(R.id.navigatgationview);
        navigationView.setNavigationItemSelectedListener(this);


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
        header_proimg=navHeaderView.findViewById(R.id.header_proimg);




        mUser=FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null){
            userId=mUser.getUid();
            mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(userId);


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
                        Picasso.with(MainActivity.this).load(imageuri).into(header_proimg);
                        header_proimg.setOnClickListener(new View.OnClickListener() {
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
           if(currentUser==null){
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

            case R.id.setting:
                sendToAccountSettings();
                return true;
            default:
                return false;

        }
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
            case R.id.review:
                startActivity(new Intent(getApplicationContext(), ReviewActivity.class));
                break;
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
                default:
                    bottomNavigationView.setSelectedItemId(R.id.home);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
       return true;
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
    private void dialogbox() {

        AlertDialog.Builder albuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.custom_dialogbox_main,null);

        albuilder.setView(view);
        final AlertDialog dialog = albuilder.create();
        dialog.show();

        Button btn_ok=view.findViewById(R.id.btn_dialogbox_main_ok);
        Button btn_cancel=view.findViewById(R.id.btn_dialogbox_main_cancel);
        final EditText edt_comment=view.findViewById(R.id.edt_comment);
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

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        Log.i(TAG, "Registered broacast receiver");
    }



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
