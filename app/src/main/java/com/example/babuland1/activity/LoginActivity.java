package com.example.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babuland1.MainActivity;
import com.example.babuland1.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
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
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

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
    private DatabaseReference mDatabaseref;
    private SharedPreferences sharedPreferences;
    private FirebaseUser mUser;
    private ProgressDialog mdialog;


    public static final String myref="myrep";
    public static final String namekey="nur";
    public static final String imagekey="bell";
    public static final String emailkey="email";


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        txtSignupbt = findViewById(R.id.login_signup);
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mdialog=new ProgressDialog(this);
        mdialog.setTitle("Login");
        mdialog.setMessage("Please wait.....a second... :)");



        btn_continiue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVarificationcode();
                edt_number.setText("");
                btn_continiue.setVisibility(View.INVISIBLE);
                btn_varyfycode.setVisibility(View.VISIBLE);
                mdialog.show();


          }
        });

        btn_varyfycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                varyfycode();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithfb();
            }
        });

        txtSignupbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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
        };


    }

    private void loginWithfb() {

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        //loginButton = findViewById(R.id.login_withfb);
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
                Log.d(TAG, "facebook:onError", error);
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
                userfield.put("email", "default");
                userfield.put("phone", "default");
                userfield.put("password", "default");
                userfield.put("image", profileimage);
                userfield.put("thumb_nail", "default");
                userfield.put("gender","default");
                userfield.put("dateofbirdth","default");
                userfield.put("address","default");

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
                mDatabaseref.setValue(userfield).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String userId_tkrdm = CurrentUser.getUid();
                        mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId_tkrdm);
                        mDatabaseref.child("ticket_redeem").setValue(0);
                        mDatabaseref.child("progressvalue").setValue(30);
                        mDatabaseref.child("BabulandPoints").setValue(0);
                        mDatabaseref.child("nof_purchase_time").setValue(0);
                        mDatabaseref.child("progressvalue_stamp").setValue(0);
                    }
                });

                Log.d(TAG, "updateUI: -------------------------------------------------------------------name----------------------------------------" + fullname);
                startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("fullname", fullname).putExtra("profileimage", profileimage).putExtra("r_home","allow"));
                mdialog.dismiss();
                finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void varyfycode() {

        String code = edt_number.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, code);
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
                                       userfield.put("name", "default");
                                       userfield.put("email", "default");
                                       userfield.put("phone", phonenumber);
                                       userfield.put("password", "default");
                                       userfield.put("image", "default");
                                       userfield.put("thumb_nail", "default");
                                       userfield.put("gender","default");
                                       userfield.put("dateofbirdth","default");
                                       userfield.put("address","default");
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

                                       mDatabaseref.setValue(userfield).addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {


                                               if(task.isSuccessful()){
                                                   mDatabaseref.child("ticket_redeem").setValue(0);
                                                   mDatabaseref.child("progressvalue").setValue(20);
                                                   mDatabaseref.child("BabulandPoints").setValue(0);
                                                   mDatabaseref.child("nof_purchase_time").setValue(0);
                                                   mDatabaseref.child("progressvalue_stamp").setValue(0);
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

}
