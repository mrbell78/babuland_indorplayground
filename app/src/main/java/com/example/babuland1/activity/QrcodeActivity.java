package com.example.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babuland1.R;
import com.example.babuland1.utils.DbHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    TextView tv_branchname,tv_name,tv_infant,tv_kids,tv_gardian,tv_socks,tv_orderid,tv_gateway,tv_totalamount;

    String branchname;
    String transectionid;
    int infant,kids,gardian,socks,orderid,totalamount;
    String Totalamount;
    int i=0;

    Date currentTime;
    String imagename;

    DbHelper db;
    Uri qrimageuri;
    String result;

    byte imageInByte[];
    StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("eTicket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializietextview();
        getvalueformpymentactivity();
        //settextview();
        db=new DbHelper(this);

        mStorage= FirebaseStorage.getInstance().getReference();

        currentTime = Calendar.getInstance().getTime();

        imagename =currentTime.toString();


        imageView=findViewById(R.id.imgqr);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null){
            userId=mUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
        }else{
            Log.d("user", "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                     name= dataSnapshot.child("name").getValue().toString();

                     //start--------------------------------
                    if( name.length()>0 && totalamount!=0){

                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point point = new Point();
                        display.getSize(point);
                        int width = point.x;
                        int height =point.y;
                        int smallerDimension= width<height?width:height;
                        smallerDimension=smallerDimension*3/4;

                        String totalvalue="name :"+name+"\n"+"Total price="+Integer.toString(totalamount)+"\nBranch Name: "+branchname+"\n"+"Userid "+userId;
                        QRGEncoder qrgEncoder = new QRGEncoder(totalvalue, null, QRGContents.Type.TEXT, smallerDimension);


                        boolean save;

                        try {

                            bitmap= qrgEncoder.encodeAsBitmap();
                            imageView.setImageBitmap(bitmap);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                             imageInByte = stream.toByteArray();

                            save = QRGSaver.save(savePath, imagename, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                            result = save ? "Saved" : "Image Not Saved";
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    tv_name.setText(name);
                    tv_infant.setText(Integer.toString(infant));
                    tv_kids.setText(Integer.toString(kids));
                    tv_gardian.setText(Integer.toString(gardian));
                    tv_socks.setText(Integer.toString(socks));
                    tv_branchname.setText(branchname);
                    tv_orderid.setText(Integer.toString(orderid));
                    tv_gateway.setText(transectionid);
                    tv_totalamount.setText(Integer.toString(totalamount));

                    //end----------------------------------
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(result.equals("Saved")){
            final StorageReference qrimage = mStorage.child("Allqrimage").child(userId+imagename+".jpg");
            UploadTask uploadTask =  qrimage.putBytes(imageInByte);

        }

}

    private void settextview() {

        tv_name.setText(name);
        tv_infant.setText(Integer.toString(infant));
        tv_kids.setText(Integer.toString(kids));
        tv_gardian.setText(Integer.toString(gardian));
        tv_socks.setText(Integer.toString(socks));
        tv_branchname.setText(branchname);
        tv_orderid.setText(Integer.toString(orderid));
        tv_gateway.setText(transectionid);
        tv_totalamount.setText(Integer.toString(totalamount));
    }

    private void getvalueformpymentactivity() {
        Intent intent =getIntent();
        infant=intent.getIntExtra("infant",0);
        kids=intent.getIntExtra("kids",0);
        gardian=intent.getIntExtra("gardian",0);
        socks=intent.getIntExtra("socks",0);
        orderid=intent.getIntExtra("orderid",0);
        branchname=intent.getStringExtra("branchname");
        transectionid=intent.getStringExtra("transectionid");
        totalamount=intent.getIntExtra("totalammount",0);
    }

    private void initializietextview() {


        tv_branchname=findViewById(R.id.branchname);
        tv_name=findViewById(R.id.username);
        tv_infant=findViewById(R.id.infant);
        tv_kids=findViewById(R.id.kids);
        tv_gardian=findViewById(R.id.gardian);
        tv_socks=findViewById(R.id.socks);
        tv_orderid=findViewById(R.id.orderid);
        tv_totalamount=findViewById(R.id.totalamount);
        tv_gateway=findViewById(R.id.gateway);
    }
}
