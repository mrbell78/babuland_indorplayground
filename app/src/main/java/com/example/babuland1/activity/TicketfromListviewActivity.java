package com.example.babuland1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babuland1.R;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketfrom_listview);

        qrimage=findViewById(R.id.imgqr);
        tv_orderid=findViewById(R.id.orderid_id);
        tv_time=findViewById(R.id.timeid);
        tv_status=findViewById(R.id.statusid);
        tv_total=findViewById(R.id.total);
        tv_branchname=findViewById(R.id.branchid);



        Intent intent = getIntent();

        int qrcodevalue = intent.getIntExtra("orderid",0);
        qrcodegenaretorfromadapter(Integer.toString(qrcodevalue));


        String time = intent.getStringExtra("time");
        String status= intent.getStringExtra("status");
        int total =intent.getIntExtra("total",0);
        String branchname = intent.getStringExtra("branch");


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