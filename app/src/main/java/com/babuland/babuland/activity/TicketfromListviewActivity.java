package com.babuland.babuland.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.babuland.babuland.R;
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

public class TicketfromListviewActivity extends AppCompatActivity {

    ImageView qrimage;
    TextView tv_total,tv_time,tv_orderid,tv_status,tv_branchname;
    int infant,kids,gardian,socks,orderid,totalamount;
    String branchname;
    String transectionid;
    QRGEncoder qrgEncoder;
    String result;
    Bitmap bitmap;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";

    DatabaseReference ticketchilddb;
    FirebaseUser mUser;
    String usrId ;


    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketfrom_listview);

        mUser= FirebaseAuth.getInstance().getCurrentUser();
        usrId = mUser.getUid();



        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Ticket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        qrimage=findViewById(R.id.imgqr);
        tv_orderid=findViewById(R.id.orderid_id);
        tv_time=findViewById(R.id.timeid);
        tv_status=findViewById(R.id.statusid);
        tv_total=findViewById(R.id.total);
        tv_branchname=findViewById(R.id.branchid);



        Intent intent = getIntent();

        int qrcodevalue = intent.getIntExtra("orderid",0);



        String time = intent.getStringExtra("time");
        String status= intent.getStringExtra("validity");
        int total =intent.getIntExtra("total",0);
        String branchname = intent.getStringExtra("branch_adp");
        String userkey_childname = intent.getStringExtra("itemkey");

        Log.d("childid", "onCreate: ..................useritem key or child key   "+usrId);


        ticketchilddb= FirebaseDatabase.getInstance().getReference().child("Buyticket").child(usrId).child(userkey_childname);

        ticketchilddb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String ticketstatus = dataSnapshot.child("status").getValue().toString();

                    if(ticketstatus.equals("used")){
                        startActivity(new Intent(getApplicationContext(),MyeTicketActivity.class));
                        Toast.makeText(TicketfromListviewActivity.this, "Scan Successful", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        qrcodegenaretorfromadapter(usrId+userkey_childname);


        tv_status.setText(status);
        tv_time.setText(time);
        tv_orderid.setText(Integer.toString(qrcodevalue));
        tv_total.setText(Integer.toString(total));
        tv_branchname.setText(branchname);
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
            try {
                bitmap= qrgEncoder.encodeAsBitmap();
                qrimage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            boolean save;

            try {
                /*save = QRGSaver.save(savePath, imagename, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                result = save ? "Saved" : "Image Not Saved";*/
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
