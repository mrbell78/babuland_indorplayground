package com.babuland.babuland.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.babuland.babuland.MainActivity;
import com.babuland.babuland.R;
import com.babuland.babuland.utils.ScheduledQuiz_stop;
import com.babuland.babuland.utils.Scheduling_quiz;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;



public class LoginActivity extends AppCompatActivity {

    private Button btn_continiue, btn_varyfycode, btn_loginfacebook;
    private LoginButton loginButton;
    private EditText edt_number;
    private String codesent;
    private TextView txtSignupbt;
    private CallbackManager mCallbackManager;
    private Toolbar mToolbar;
    private String phonenumber;
    private DatabaseReference mDatabaseref,admindatabase;
    private SharedPreferences sharedPreferences;
    private FirebaseUser mUser;
    private ProgressDialog mdialog;


    public static final String myref="myrep";
    public static final String namekey="nur";
    public static final String imagekey="bell";
    public static final String emailkey="email";

    String number;


    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";

    Integer startTime;
    Integer endTime;
    String quiznumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
       // accessToken = AccessToken.getCurrentAccessToken();
        setContentView(R.layout.activity_login);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.completewhite)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btn_continiue = findViewById(R.id.phone_continiue);
        edt_number = findViewById(R.id.edt_numberholder);
        btn_varyfycode = findViewById(R.id.phone_ok);
        //btn_loginfacebook=findViewById(R.id.btn_facebboklogin);
        loginButton = findViewById(R.id.btn_facebboklogin);
       // txtSignupbt = findViewById(R.id.login_signup);
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mdialog=new ProgressDialog(this);
        mdialog.setTitle("Login");
        mdialog.setMessage("Please wait.......:)");


/*
        admindatabase=FirebaseDatabase.getInstance().getReference().child("Admin");

        Map<String ,String > adminpanel = new HashMap<>();
        adminpanel.put("quiznumber","1");
        adminpanel.put("editmode","no");
        admindatabase.setValue(adminpanel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    admindatabase.child("Qtimestart").setValue(21);
                    admindatabase.child("Qtimeend").setValue(23);


                    admindatabase=FirebaseDatabase.getInstance().getReference().child("Admin");
                    admindatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("quiznumber").exists() && dataSnapshot.child("Qtimestart").exists()

                                    && dataSnapshot.child("Qtimeend").exists()
                            ){
                                startTime=dataSnapshot.child("Qtimestart").getValue(Integer.class);
                                endTime=dataSnapshot.child("Qtimeend").getValue(Integer.class);
                                quiznumber = dataSnapshot.child("quiznumber").getValue().toString();


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }
        });*/





           // number=edt_number.getText().toString();
            btn_continiue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: -------------------number length "+ edt_number.getText().toString().length());

                    if(!TextUtils.isEmpty(edt_number.getText().toString()) && edt_number.getText().toString().length()==11){
                        sendVarificationcode();

                        edt_number.setText("");
                        mdialog.show();
                    }

                    else if(TextUtils.isEmpty(edt_number.getText().toString())) {

                        edt_number.setError("number is empty!!");
                        Toast.makeText(LoginActivity.this, "Please Enter valid number", Toast.LENGTH_SHORT).show();
                    }else if (edt_number.getText().toString().length()<=10 || edt_number.getText().toString().length()>=11 || edt_number.getText().toString().length()!=11 ){
                        edt_number.setError("enter 11 digit number without +88");
                        Toast.makeText(LoginActivity.this, "enter 11 digit number without +88", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        btn_varyfycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code =edt_number.getText().toString();
                varyfycode(code);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithfb();
                printKeyHash(LoginActivity.this);
            }
        });

      /*  txtSignupbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });*/

  /*      mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                codesent = s;
               // edt_number.setText(codesent);
                Toast.makeText(LoginActivity.this, "code sent", Toast.LENGTH_SHORT).show();
            }
        };*/


    }

    private void loginWithfb() {

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        //loginButton = findViewById(R.id.login_withfb);

        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));
      /*  LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });*/
    loginButton.setReadPermissions("email", "public_profile");
        //loginButton.setPermissions("email","public_profile");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("error_facebook", "facebook:onError", error);
                // ...
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            updateUI();
        }
    }

    private void updateUI() {


            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {

                String fullname = profile.getName();
                String profileimage = profile.getProfilePictureUri(300, 300).toString();
                String uniqid = profile.getId().toString();

                FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = mCurrentUser.getUid();
                mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);


                Map<String, String> userfield = new HashMap<>();
                userfield.put("name", fullname);
                userfield.put("email", "");
                userfield.put("phone", "");
                userfield.put("password", "default");
                userfield.put("image", profileimage);
                userfield.put("thumb_nail", "default");
                userfield.put("gender","");
                userfield.put("dateofbirdth","Date of Birth");
                userfield.put("address","");
                userfield.put("qrdata","nurhossen");
                userfield.put("count1","avail");
                userfield.put("count2","avail");
                userfield.put("count3","avail");
                userfield.put("count4","avail");
                userfield.put("count5","avail");
                userfield.put("count6","avail");
                userfield.put("count7","avail");
                userfield.put("count8","avail");
                userfield.put("count9","avail");
                userfield.put("count10","avail");
                userfield.put("comment", "not comment yet");
                userfield.put("userid",uniqid);
                userfield.put("availfreeTicket","avail_free");
                userfield.put("childrenName","Child name");
                userfield.put("spousename","");
                userfield.put("status_quz","inactive");
                userfield.put("alrmstatus","false");


                mDatabaseref.setValue(userfield).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String userId_tkrdm = CurrentUser.getUid();
                        mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId_tkrdm);
                        mDatabaseref.child("ticket_redeem").setValue(0);
                        mDatabaseref.child("pic_up").setValue(30);
                        mDatabaseref.child("name_up").setValue(0);
                        mDatabaseref.child("number_up").setValue(0);
                        mDatabaseref.child("dob_up").setValue(0);
                        mDatabaseref.child("mail_up").setValue(0);
                        mDatabaseref.child("address_up").setValue(0);
                        mDatabaseref.child("BabulandPoints").setValue(20);
                        mDatabaseref.child("nof_purchase_time").setValue(0);
                        mDatabaseref.child("progressvalue_stamp").setValue(0);
                        int startquiz;
                        int endquiz;
                        if(startTime!=null && endTime!=null){

                            startquiz=startTime;
                            endquiz =endTime;

                        }else {
                            startquiz=21;
                            endquiz=23;
                        }


                        Log.d(TAG, "onDataChange: -----------------------starttime "+startquiz);
                        Log.d(TAG, "onDataChange: ---------------------------endtime " + endquiz);
                        Log.d(TAG, "onDataChange: ------------------------------quiznumber "+quiznumber);

                        Calendar calendar_stop= Calendar.getInstance();
                        calendar_stop.set(Calendar.HOUR_OF_DAY,23);
                        calendar_stop.set(Calendar.MINUTE,59);
                        calendar_stop.set(Calendar.SECOND,0);

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY,21);
                        calendar.set(Calendar.MINUTE,1);
                        calendar.set(Calendar.SECOND,0);



                        scheduleNotification(getNotification("Dont miss out todays quiz"),100,calendar);
                        scheduliedquiz_stop(calendar_stop);
                    }
                });

                Log.d(TAG, "updateUI: -------------------------------------------------------------------name----------------------------------------" + fullname);
                startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("fullname", fullname).putExtra("profileimage", profileimage).putExtra("r_home","allow"));
                mdialog.dismiss();
                finish();
        }
    }

    private void scheduliedquiz_stop(Calendar calendar_stop) {
        Toast.makeText(this, "scheduled stop notification is called tata", Toast.LENGTH_SHORT).show();

        Intent notificationIntent = new Intent( this, ScheduledQuiz_stop. class ) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        //long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManagerr = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManagerr != null;

        alarmManagerr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar_stop.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void varyfycode(String s) {

        //String code = edt_number.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, s);
        signInWithPhoneAuthCredential(credential);
    }

    private void sendVarificationcode() {


        phonenumber = "+88"+edt_number.getText().toString();
        Log.d(TAG, "sendVarificationcode: -------------------------------------phone number---------------------------------------" + phonenumber);
        if (phonenumber.isEmpty()) {
            edt_number.setError("phone number is required!!");
            return;
        } else if (phonenumber.length() < 10) {
            edt_number.setError("Please enter valid phone number");
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber, 60, TimeUnit.SECONDS, this, mCallback);
    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if(code!=null){
                varyfycode(code);
                edt_number.setText(code);
                mdialog.dismiss();
            }


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            mdialog.dismiss();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            //super.onCodeSent(s, forceResendingToken);

            codesent=s;
            Toast.makeText(LoginActivity.this, "code sent", Toast.LENGTH_SHORT).show();
            btn_continiue.setVisibility(View.INVISIBLE);
            btn_varyfycode.setVisibility(View.VISIBLE);
            edt_number.setHint("Enter pin");
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = mCurrentUser.getUid();
                            mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
                           mDatabaseref.orderByChild("phone").equalTo(phonenumber).addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   if(dataSnapshot.getValue()!=null)
                                   {
                                       Toast.makeText(LoginActivity.this, "old user", Toast.LENGTH_SHORT).show();
                                       startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                       mdialog.dismiss();
                                       finish();
                                   }else{
                                       Toast.makeText(LoginActivity.this, "new user", Toast.LENGTH_SHORT).show();
                                       Map<String, String> userfield = new HashMap<>();
                                       userfield.put("name", "");
                                       userfield.put("email", "");
                                       userfield.put("phone", phonenumber);
                                       userfield.put("password", "default");
                                       userfield.put("image", "default");
                                       userfield.put("thumb_nail", "default");
                                       userfield.put("gender","");
                                       userfield.put("dateofbirdth","Date of Birth");
                                       userfield.put("address","");
                                       userfield.put("qrdata","nurhossen");
                                       userfield.put("count1","avail");
                                       userfield.put("count2","avail");
                                       userfield.put("count3","avail");
                                       userfield.put("count4","avail");
                                       userfield.put("count5","avail");
                                       userfield.put("count6","avail");
                                       userfield.put("count7","avail");
                                       userfield.put("count8","avail");
                                       userfield.put("count9","avail");
                                       userfield.put("count10","avail");
                                       userfield.put("availfreeTicket","avail_free");
                                       userfield.put("childrenName","Child name");
                                       userfield.put("comment", "not comment yet");
                                       userfield.put("spousename","");
                                       userfield.put("status_quz","inactive");
                                       userfield.put("alrmstatus","false");

                                       mDatabaseref.setValue(userfield).addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {


                                               if(task.isSuccessful()){
                                                   mDatabaseref.child("ticket_redeem").setValue(0);
                                                   mDatabaseref.child("pic_up").setValue(30);
                                                   mDatabaseref.child("name_up").setValue(0);
                                                   mDatabaseref.child("number_up").setValue(0);
                                                   mDatabaseref.child("dob_up").setValue(0);
                                                   mDatabaseref.child("mail_up").setValue(0);
                                                   mDatabaseref.child("address_up").setValue(0);
                                                   mDatabaseref.child("BabulandPoints").setValue(10);
                                                   mDatabaseref.child("nof_purchase_time").setValue(0);
                                                   mDatabaseref.child("progressvalue_stamp").setValue(0);

                                                   int startquiz;
                                                   int endquiz;
                                                   if(startTime!=null && endTime!=null){

                                                       startquiz=startTime;
                                                       endquiz =endTime;

                                                   }else {
                                                       startquiz=21;
                                                       endquiz=23;
                                                   }

                                                   Calendar calendar_stop= Calendar.getInstance();
                                                   calendar_stop.set(Calendar.HOUR_OF_DAY,23);
                                                   calendar_stop.set(Calendar.MINUTE,59);
                                                   calendar_stop.set(Calendar.SECOND,0);

                                                   Calendar calendar = Calendar.getInstance();
                                                   calendar.set(Calendar.HOUR_OF_DAY,21);
                                                   calendar.set(Calendar.MINUTE,0);
                                                   calendar.set(Calendar.SECOND,5);

                                                   scheduleNotification(getNotification("Dont miss out todays quiz"),100,calendar);
                                                   scheduliedquiz_stop(calendar_stop);
                                               }
                                               startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                               mdialog.dismiss();
                                               finish();
                                           }
                                       });


                                   }
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {
                                   mdialog.dismiss();

                               }
                           });
                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(LoginActivity.this, "incorrenct code", Toast.LENGTH_SHORT).show();
                                mdialog.dismiss();
                            }
                        }
                    }
                });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                            boolean isNewuser=  task.getResult().getAdditionalUserInfo().isNewUser();
                            if(isNewuser){
                                updateUI();
                                Toast.makeText(LoginActivity.this, "new User", Toast.LENGTH_SHORT).show();
                            }else if(isNewuser==false){
                                Toast.makeText(LoginActivity.this, "old user", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "logedin failed", Toast.LENGTH_SHORT).show();
                            //updateUI(null);\
                            mdialog.dismiss();
                        }

                        // ...
                    }
                });
    }




    private void scheduleNotification (Notification notification , int delay, Calendar calendar) {


        Toast.makeText(this, "scheduled notification is called for quiz", Toast.LENGTH_SHORT).show();
        Intent notificationIntent = new Intent( this, Scheduling_quiz. class ) ;
        notificationIntent.putExtra(Scheduling_quiz. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(Scheduling_quiz. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        //long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
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
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        builder.setSound(soundUri);

        Notification notification = builder.build();

        return notification ;
    }



    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }


}
