package com.babuland.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.babuland.babuland1.MainActivity;
import com.babuland.babuland1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name, email,phone,password,confirmpssword;
    private Button btn_createAccount;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseref;
    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        viewinitialoization();
       firebaseInitialization();

       btn_createAccount.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String lname=name.getText().toString();
               String lemail=email.getText().toString();
               String lphone=phone.getText().toString();
               String lpassword=password.getText().toString();
               String lconfirmpass=confirmpssword.getText().toString();

               if(TextUtils.isEmpty(lname))
                   name.setError("Enter your name please......");
              else if(TextUtils.isEmpty(lemail))
                   email.setError("your email is missing");
               else if(TextUtils.isEmpty(lphone))
                   phone.setError("please enter yoiur phone number");
               else if(TextUtils.isEmpty(lpassword))
                   password.setError("enter password");
               else if(!lpassword.equals(lconfirmpass))
               {
                   Toast.makeText(RegistrationActivity.this, "password didnt match", Toast.LENGTH_SHORT).show();
               }else {
                   createAccount(lname,lemail,lphone,lpassword);
               }


           }
       });

    }

    private void createAccount(final String name, final String email, final String phone, final String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String userId= mCurrentUser.getUid();
                    mDatabaseref= FirebaseDatabase.getInstance().getReference().child("User").child(userId);
                    Map<String,String> userfield = new HashMap<>();

                    userfield.put("name",name);
                    userfield.put("email",email);
                    userfield.put("phone",phone);
                    userfield.put("password",password);
                    userfield.put("image","default");
                    userfield.put("thumb_nail","default");
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
                    userfield.put("progressvalue","2");
                    mDatabaseref.setValue(userfield);
                    Toast.makeText(RegistrationActivity.this, "registration successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void firebaseInitialization() {

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
       //  String userId=currentUser.getUid();

    }

    private void viewinitialoization() {

        name=findViewById(R.id.register_name);
        email=findViewById(R.id.register_email);
        phone=findViewById(R.id.register_phonenumber);
        password=findViewById(R.id.register_password);
        confirmpssword=findViewById(R.id.register_confirm_password);
        btn_createAccount=findViewById(R.id.Register_createaccount);
    }


}
