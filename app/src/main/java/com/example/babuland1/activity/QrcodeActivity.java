package com.example.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.babuland1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class QrcodeActivity extends AppCompatActivity {

    Bitmap bitmap;
    FirebaseUser mUser;
    String userId;
    DatabaseReference mDatabase;
    String name;
    ImageView imageView;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Intent intent = getIntent();
        final int inputValue = intent.getIntExtra("totalammount",0);
        final String branchname= intent.getStringExtra("branchname");
        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("eTicket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView=findViewById(R.id.imgqr);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null){
            userId=mUser.getUid();
            mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(userId);
        }else{
            Log.d("user", "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                     name= dataSnapshot.child("name").getValue().toString();

                     //start--------------------------------
                    if( name.length()>0 && inputValue!=0){

                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point point = new Point();
                        display.getSize(point);
                        int width = point.x;
                        int height =point.y;
                        int smallerDimension= width<height?width:height;
                        smallerDimension=smallerDimension*3/4;

                        String totalvalue="name :"+name+"\n"+"Total price="+Integer.toString(inputValue)+"\nBranch Name: "+branchname+"\n"+"Userid "+userId;
                        QRGEncoder qrgEncoder = new QRGEncoder(totalvalue, null, QRGContents.Type.TEXT, smallerDimension);
                        try {
                            // Getting QR-Code as Bitmap
                            bitmap = qrgEncoder.encodeAsBitmap();
                            // Setting Bitmap to ImageView
                            imageView.setImageBitmap(bitmap);

                            QRGSaver.save(savePath, "qrcode", bitmap, QRGContents.ImageType.IMAGE_JPEG);

                        } catch (WriterException e) {
                            Log.v("error", e.toString());
                        }
                        String result;
                        boolean save;
                        try {
                            save = QRGSaver.save(savePath, "totalprice", bitmap, QRGContents.ImageType.IMAGE_JPEG);
                            result = save ? "Image Saved" : "Image Not Saved";
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    //end----------------------------------
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
}
