package com.example.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.babuland1.MainActivity;
import com.example.babuland1.R;
import com.example.babuland1.fragment.FirstFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Qr_cameraopenerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView Scaner;
    FirstFragment homefragment;
    private SharedPreferences sharedPreferences;
    private static final String qrAuthcode="qrkey";
    private static final  String myref="myref";
    public String data;
    String varifyqrcode;
    public String qrvalidation;
    private DatabaseReference mDatabaseref;

    public qrSendata listener;


    public interface qrSendata{
        void validationofdata(String data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        cameraPermision();
        Scaner=new ZXingScannerView(Qr_cameraopenerActivity.this);
        setContentView(Scaner);
        //homefragment = new FirstFragment();



    }

    private void cameraPermision() {
        String []permission = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),permission[0])==PackageManager.PERMISSION_GRANTED &&

        ContextCompat.checkSelfPermission(this.getApplicationContext(),permission[1])==PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(),permission[2])==PackageManager.PERMISSION_GRANTED
        ){

        }else{
            ActivityCompat.requestPermissions(Qr_cameraopenerActivity.this,permission,1);
        }
    }

    @Override
    public void handleResult(Result result) {

/*
        FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();
        final FirstFragment firstFragment = new FirstFragment();
*/


        data=result.getText();
      if(data!=null){
          FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
          final String userId = mCurrentUser.getUid();
          mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
         mDatabaseref.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(dataSnapshot.getValue()!=null){
                     varifyqrcode=dataSnapshot.child("qrdata").getValue().toString();
                     if(varifyqrcode.equals(data)){
                         Toast.makeText(Qr_cameraopenerActivity.this, "scan successful", Toast.LENGTH_SHORT).show();
                         Intent i = getIntent();
                         String btn_identity=i.getStringExtra("identity");
                         startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra("dataofqr",varifyqrcode).putExtra("identity",btn_identity));
                         //onBackPressed();
                     }else {
                         FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                         String userId = mCurrentUser.getUid();

                         mDatabaseref = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
                         mDatabaseref.child("status").setValue("false").addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 if(task.isSuccessful()){
                                     Toast.makeText(Qr_cameraopenerActivity.this, "its not babuland qr code please try again", Toast.LENGTH_SHORT).show();
                                     onBackPressed();
                                 }
                             }
                         });

                     }
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 //onBackPressed();
             }
         });



      }
        //Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        /*replacefragment(homefragment);*/

    }


    @Override
    protected void onPause() {
        super.onPause();
     // Scaner.stopCamera();
        Scaner.startCamera();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Scaner.setResultHandler(this);
        Scaner.startCamera();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        cameraPermision();
    }
}
