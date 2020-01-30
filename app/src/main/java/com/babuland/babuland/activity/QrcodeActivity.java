package com.babuland.babuland.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.babuland.babuland.R;
import com.babuland.babuland.utils.DbHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrcodeActivity extends AppCompatActivity {

    Bitmap bitmap;
    FirebaseUser mUser;
    String userId;
    DatabaseReference mDatabase;
    String name;
    ImageView imageView;
    //String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
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
    static Uri qrimageuri;
    String result;

    byte imageInByte[];
    StorageReference mStorage;

    DatabaseReference Buyticketref;

    ProgressDialog mDialog;

    String passengerID;
    DatabaseReference ref;
    QRGEncoder qrgEncoder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);




        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("eTicket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDialog=new ProgressDialog(this);
        mDialog.setTitle("Ticket on processing");
        mDialog.setMessage("Please wait");
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
            Buyticketref=FirebaseDatabase.getInstance().getReference().child("Buyticket").child(userId);
        }else{
            Log.d("user", "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }




       /* mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                     name= dataSnapshot.child("name").getValue().toString();
                    //end----------------------------------
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        tv_name.setText(name);
        tv_infant.setText(Integer.toString(infant));
        tv_kids.setText(Integer.toString(kids));
        tv_gardian.setText(Integer.toString(gardian));
        tv_socks.setText(Integer.toString(socks));
        tv_branchname.setText(branchname);
        tv_orderid.setText(Integer.toString(orderid));
        tv_gateway.setText(transectionid);
        tv_totalamount.setText(Integer.toString(totalamount));


        //start--------------------------------

        Intent intent = getIntent();
        int orderid_adapter = intent.getIntExtra("orderid",0);

        uploadticket_info(Integer.toString(orderid_adapter));
        mDialog.show();

       /* Log.d("orderid_adapter", "onCreate: ------------------------------------- orderidadapter "+orderid_adapter);
        if(orderid_adapter!=0 )
            qrcodegenaretorfromadapter(String.valueOf(orderid_adapter));
        else
            qrcodegenaretor(String.valueOf(orderid));*/






        //Log.d("qrimage", "onComplete: ------------------------------------------- "+imageInByte);


}

    private void qrcodegenaretor(String name) {

        if( transectionid.length()>0 && totalamount!=0){

            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height =point.y;
            int smallerDimension= width<height?width:height;
            smallerDimension=smallerDimension*3/4;

            //String totalvalue="Total price="+Integer.toString(totalamount)+"\nBranch Name: "+branchname+"\n"+"Userid "+userId;
            qrgEncoder = new QRGEncoder(name, null, QRGContents.Type.TEXT, smallerDimension);

            boolean save;

            try {
                bitmap= qrgEncoder.encodeAsBitmap();
                imageView.setImageBitmap(bitmap);
               // covertBitmaptobyte(bitmap);
                mDialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void qrcodegenaretorfromadapter(String orderid){

        if( orderid!=null){

            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height =point.y;
            int smallerDimension= width<height?width:height;
            smallerDimension=smallerDimension*3/4;

            //String totalvalue="Total price="+Integer.toString(totalamount)+"\nBranch Name: "+branchname+"\n"+"Userid "+userId;
            qrgEncoder = new QRGEncoder(orderid, null, QRGContents.Type.TEXT, smallerDimension);

            boolean save;

            try {
                bitmap= qrgEncoder.encodeAsBitmap();
                imageView.setImageBitmap(bitmap);
                //covertBitmaptobyte(bitmap);
                mDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

/*    private void covertBitmaptobyte(Bitmap bitmap) {

        //imageView.setImageBitmap(bitmap);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        imageInByte = stream.toByteArray();

        final StorageReference qrimage = mStorage.child("Allqrimage").child(userId+imagename+".jpg");
        UploadTask uploadTask =  qrimage.putBytes(imageInByte);



        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    qrimage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Uri downloaduri=uri;
                            qrimageuri=downloaduri;

                            if(task.isSuccessful()){
                                uploadticket_info(downloaduri.toString());
                            }
                        }
                    });
                    Log.d("special", "onComplete: ------------------------------------------- "+qrimageuri);
                }

            }
        });

    }*/

 /*   private void uploadticket(final String toString) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss. aa");
        String formattedDate = dateFormat.format(new Date()).toString();
        Intent intent =getIntent();
        String phone = intent.getStringExtra("phone");
        Integer totalamount = intent.getIntExtra("totalammount",0);
        String Branch = intent.getStringExtra("branchname");
        final Integer orderid = intent.getIntExtra("orderid",0);


        Log.d("uri", "onSuccess: -------------------------------------- "+toString);
        Map imagedatawiththum=  new HashMap();
        imagedatawiththum.put("status","valid");
        imagedatawiththum.put("phone",phone);
        imagedatawiththum.put("total_amount",totalamount);
        imagedatawiththum.put("branch",Branch);
        imagedatawiththum.put("time",formattedDate);
        imagedatawiththum.put("orderid",orderid);
        db.insertdata(formattedDate,totalamount,orderid,"valid",branchname);

         //status value will come frome firebase
        String childname=phone+formattedDate;


        Buyticketref.child(childname).updateChildren(imagedatawiththum).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    mDialog.dismiss();
                    //Picasso.with(QrcodeActivity.this).load(toString).into(imageView);


                }
            }
        });

    }*/

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


    private  void uploadticket_info(final String imageuri){

        mUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null){
            userId=mUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
            Buyticketref=FirebaseDatabase.getInstance().getReference().child("Buyticket").child(userId);



            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh.mm.ss. aa");
            String formattedDate = dateFormat.format(new Date()).toString();

            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            Intent intent =getIntent();
            String phone = intent.getStringExtra("phone");
            Integer totalamount = intent.getIntExtra("totalammount",0);
            String Branch = intent.getStringExtra("branchname");
            final Integer orderid = intent.getIntExtra("orderid",0);

            Map imagedatawiththum=  new HashMap();
            imagedatawiththum.put("status","not used");
            imagedatawiththum.put("phone",phone);
            imagedatawiththum.put("total_amount",totalamount);
            imagedatawiththum.put("branch",Branch);
            imagedatawiththum.put("time",formattedDate);
            imagedatawiththum.put("orderid",orderid);
            String childname=phone+currentTime;
            Buyticketref.child(childname).updateChildren(imagedatawiththum).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        //Picasso.with(getApplicationContext()).load(imageuri).into(imageView);
                       qrcodegenaretorfromadapter(Integer.toString(orderid));
                        mDialog.dismiss();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("reason", "onFailure: ----------------------------error "+e.getMessage());
                }
            });

            /*qrcodegenaretorfromadapter(Integer.toString(orderid));
            mDialog.dismiss();*/

        }else{
            Log.d("user", "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }


    }
}
