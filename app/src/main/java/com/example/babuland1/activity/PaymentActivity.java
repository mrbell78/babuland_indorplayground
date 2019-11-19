package com.example.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babuland1.MainActivity;
import com.example.babuland1.R;
import com.google.android.gms.tasks.OnCompleteListener;
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

import com.squareup.picasso.Picasso;
import com.sslwireless.sslcommerzlibrary.model.initializer.CustomerInfoInitializer;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization;
import com.sslwireless.sslcommerzlibrary.model.response.TransactionInfoModel;
import com.sslwireless.sslcommerzlibrary.model.util.CurrencyType;
import com.sslwireless.sslcommerzlibrary.model.util.SdkType;
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz;
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.TransactionResponseListener;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.babuland1.activity.TestactivityActivity.createConnection;

public class PaymentActivity extends AppCompatActivity  implements TransactionResponseListener {

    int infant_ticket;
    int kids_ticket;
    int gardian_ticket;
    int socks_ticket;
    int infant_ticket_product_id;
    int kids_ticket_product_id;
    int gardian_ticket_product_id;
    int socks_ticket_product_id;

    int price_infant=150;
    int price_kids=300;
    int price_gardian=100;
    int price_socks=30;
    FirebaseUser mUser;
    DatabaseReference mDatabase;
    String userId;
    int Total;
    StorageReference mstorage;
    Uri donwloaduri;

    TextView tv_infant,tv_kids,tv_gardian,tv_socks,tv_total,tv_branchname;
    Button btn_pay;
    String branchname;
    TextView vieworginal;
    private static final String TAG = "PaymentActivity";

    SQLiteOpenHelper helper;
    SQLiteDatabase db;

    String name;
    String phone;
    String imageuri;
    //for insert data in db
    public static final String DEFAULT_DRIVER="oracle.jdbc.driver.OracleDriver";
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@itlimpex.ddns.net:2121:xe";
    private static final String DEFAULT_USERNAME = "bland";
    private static final String DEFAULT_PASSWORD = "servicepack3";

    private Connection connection;

    int orderid_maxvalue;
    int productid_infant;
    int productid_kids;
    int productid_gardian;
    int productid_socks;
    int point;
    int databaspoint;

    String gatawayname;
    boolean pointflag=false;

    ProgressDialog mProgress;
    SSLCommerzInitialization sslCommerzInitialization ;

     CustomerInfoInitializer customerInfoInitializer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        vieworginal=findViewById(R.id.vieworiginal);
        mstorage= FirebaseStorage.getInstance().getReference();
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        mProgress=new ProgressDialog(this);
        if(mUser!=null){
            userId=mUser.getUid();
            mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(userId);
        }else{
            Log.d(TAG, "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }

        Intent intent = getIntent();
        infant_ticket = intent.getIntExtra("infant",0);
        kids_ticket = intent.getIntExtra("kids",0);
        gardian_ticket = intent.getIntExtra("gardian",0);
        socks_ticket = intent.getIntExtra("socks",0);
        branchname=intent.getStringExtra("radiovalue_string");
        Log.d(TAG, "onCreate: -------------------------------------infat ticket num-----------------------"+infant_ticket);
        Log.d(TAG, "onCreate: -------------------------------------infat ticket num-----------------------"+kids_ticket);
        Log.d(TAG, "onCreate: -------------------------------------infat ticket num-----------------------"+gardian_ticket);
        Log.d(TAG, "onCreate: -------------------------------------infat ticket num-----------------------"+socks_ticket);


        //pruduct id event start mirpur


        infant_ticket_product_id = intent.getIntExtra("infant_id",0);
        kids_ticket_product_id = intent.getIntExtra("kids_id",0);
        gardian_ticket_product_id = intent.getIntExtra("gardian_id",0);
        socks_ticket_product_id = intent.getIntExtra("socks_id",0);
        branchname=intent.getStringExtra("radiovalue_string");

        // product id event end

        //vieworginal start


        vieworginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TestactivityActivity.class);
                intent.putExtra("infant_id",infant_ticket_product_id);
                intent.putExtra("kids_id",kids_ticket_product_id);
                intent.putExtra("gardian_id",gardian_ticket_product_id);
                intent.putExtra("socks_id",socks_ticket_product_id);

                intent.putExtra("infant_quantity",infant_ticket);
                intent.putExtra("kids_quantity",kids_ticket);
                intent.putExtra("gardian_quantity",gardian_ticket);
                intent.putExtra("socks_quantity",socks_ticket);

                intent.putExtra("infant_unitprice",price_infant);
                intent.putExtra("kids_unitprice",price_kids);
                intent.putExtra("gardian_unitprice",price_gardian);
                intent.putExtra("socks_unitprice",price_socks);

                intent.putExtra("branchname",branchname);

                intent.putExtra("total",Total);

                startActivity(intent);
            }
        });
        //vieworginal end
        //Arethmetic event


        tv_infant=findViewById(R.id.invoice_infant);
        tv_kids=findViewById(R.id.invoice_Kids);
        tv_gardian=findViewById(R.id.invoice_gardian);
        tv_socks=findViewById(R.id.invoice_socks);
        tv_total=findViewById(R.id.invoice_total);
        btn_pay=findViewById(R.id.payment);
        tv_branchname=findViewById(R.id.branchname);
        tv_branchname.setText(branchname);



        tv_infant.setText(Integer.toString(price_infant)+"X"+Integer.toString(infant_ticket)+"="+Integer.toString(price_infant*infant_ticket)+"Tk");
        tv_kids.setText(Integer.toString(price_kids)+"X"+Integer.toString(kids_ticket)+"="+Integer.toString(price_kids*kids_ticket)+"Tk");
        tv_gardian.setText(Integer.toString(price_gardian)+"X"+Integer.toString(gardian_ticket)+"="+Integer.toString(price_gardian*gardian_ticket)+"Tk");
        tv_socks.setText(Integer.toString(price_socks)+"X"+Integer.toString(socks_ticket)+"="+Integer.toString(price_socks*socks_ticket)+"Tk");


        Total=price_infant*infant_ticket+price_kids*kids_ticket+price_gardian*gardian_ticket+price_socks*socks_ticket;
        tv_total.setText(Integer.toString(Total)+"Tk");
        babuLandpoints();

        btn_pay.setText("Pay    "+Integer.toString(Total)+" Tk");

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Total!=0){
                    startPayment();
                }else {
                    Toast.makeText(PaymentActivity.this, "Transection failed. no payable amount", Toast.LENGTH_SHORT).show();
                }
            }
        });


        sslCommerzInitialization = new SSLCommerzInitialization("babul5dc25b039d6e8",
                "babul5dc25b039d6e8@ssl", Total, CurrencyType.BDT,
                "123456789098765", "food", SdkType.TESTBOX);

        customerInfoInitializer = new CustomerInfoInitializer("reza", "rubel12131103078@gmail.com",
                "mirpur12", "dhaka", "1216", "Bangladesh", "01762957451");




    }

    private void startPayment() {

        IntegrateSSLCommerz
                .getInstance(PaymentActivity.this)
                .addSSLCommerzInitialization(sslCommerzInitialization)
                .addCustomerInfoInitializer(customerInfoInitializer)
                .buildApiCall( PaymentActivity.this);


    }

    @Override
    public void transactionSuccess(TransactionInfoModel transactionInfoModel) {

        gatawayname=transactionInfoModel.getBankTranId();
        point=(Total*10)/100;

        mUser=FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null) {
            userId = mUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

            databaspoint=databaspoint+point;
        mDatabase.child("BabulandPoints").setValue(databaspoint).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PaymentActivity.this, "your point added", Toast.LENGTH_SHORT).show();
                    mDatabase.child("nof_purchase_time").setValue(1);
                    mDatabase.child("branch_name").setValue(branchname);
                }
            }
        });

    }else{
        Log.d(TAG, "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
    }


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            this.connection= createConnection();
            Toast.makeText(this, "database connected", Toast.LENGTH_SHORT).show();
            Statement stmt=connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();


            stmt.executeUpdate("INSERT INTO TICKET_ORDERS (ORDER_ID,CUSTOMER_ID,ORDER_TOTAL,ORDER_TIMESTAMP,USER_NAME,RECEIVED_AMOUNT,PAYMENT_TYPE) " + "VALUES (null,null,null,systimestamp,'NUR',"+Total+",'Card')");

            ResultSet rs=stmt.executeQuery(" select  MAX(ORDER_ID) from  TICKET_ORDERS ");

            while(rs.next()) {
                stringBuffer.append( rs.getString(1)+"\n");
                orderid_maxvalue = rs.getInt(1);
                //
            }
            ResultSet productid_in=stmt.executeQuery(" SELECT  PRODUCT_ID from  DEMO_PRODUCT_INFO WHERE PRODUCT_NAME='INFANT'");
            while(productid_in.next())
                productid_infant=productid_in.getInt(1);

            ResultSet productid_kidsl=stmt.executeQuery(" SELECT  PRODUCT_ID from  DEMO_PRODUCT_INFO WHERE PRODUCT_NAME='CHILDREN'");
            while(productid_kidsl.next())
                productid_kids=productid_kidsl.getInt(1);

            ResultSet productid_gardianl=stmt.executeQuery(" SELECT  PRODUCT_ID from  DEMO_PRODUCT_INFO WHERE PRODUCT_NAME='PARENT'");
            while(productid_gardianl.next())
                productid_gardian=productid_gardianl.getInt(1);

            ResultSet productid_socksl=stmt.executeQuery(" SELECT  PRODUCT_ID from  DEMO_PRODUCT_INFO WHERE PRODUCT_NAME='SOCKS'");
            while(productid_socksl.next())
                productid_socks=productid_socksl.getInt(1);


            Log.d("ordermaxvalue", "onCreate: ---------------------------------------------------ordermaxvalue="+orderid_maxvalue);
            Log.d("productid", "onCreate: ---------------------------------------------------productid="+productid_infant);
            Log.d("productid", "onCreate: ---------------------------------------------------productid="+productid_kids);
            Log.d("productid", "onCreate: ---------------------------------------------------productid="+productid_gardian);
            Log.d("productid", "onCreate: ---------------------------------------------------productid="+productid_socks);
            if(infant_ticket!=0)
                stmt.executeUpdate("INSERT INTO TICKET_ORDER_ITEMS (ORDER_ITEM_ID,ORDER_ID,PRODUCT_ID,UNIT_PRICE,QUANTITY) " + "VALUES (null,"+orderid_maxvalue+","+productid_infant+",null,"+infant_ticket+")");

            if(kids_ticket!=0)
                stmt.executeUpdate("INSERT INTO TICKET_ORDER_ITEMS (ORDER_ITEM_ID,ORDER_ID,PRODUCT_ID,UNIT_PRICE,QUANTITY) " + "VALUES (null,"+orderid_maxvalue+","+productid_kids+",null,"+kids_ticket+")");

            if(gardian_ticket!=0)
                stmt.executeUpdate("INSERT INTO TICKET" +"_ORDER_ITEMS (ORDER_ITEM_ID,ORDER_ID,PRODUCT_ID,UNIT_PRICE,QUANTITY) " + "VALUES (null,"+orderid_maxvalue+","+productid_gardian+",null,"+gardian_ticket+")");

            if(socks_ticket!=0)
                stmt.executeUpdate("INSERT INTO TICKET_ORDER_ITEMS (ORDER_ITEM_ID,ORDER_ID,PRODUCT_ID,UNIT_PRICE,QUANTITY) " + "VALUES (null,"+orderid_maxvalue+","+productid_socks+",null,"+socks_ticket+")");


            //orderid=Integer.parseInt(stringBuffer.toString());
            Toast.makeText(this, "orderid is ="+orderid_maxvalue, Toast.LENGTH_SHORT).show();
            connection.close();

            pointflag=true;

            startActivity(new Intent(getApplicationContext(),QrcodeActivity.class).putExtra("totalammount",Total).putExtra("branchname",branchname)
            .putExtra("infant",infant_ticket).putExtra("kids",kids_ticket).putExtra("gardian",gardian_ticket).putExtra("socks",socks_ticket)
            .putExtra("orderid",orderid_maxvalue).putExtra("transectionid",gatawayname));
            finish();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("errorsql", "onCreate: ------------------------------------------------------"+e.getMessage());
            startActivity(new Intent(getApplicationContext(),TicketfailedActivity.class).putExtra("orderid",orderid_maxvalue));
            finish();

        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("sql exception", "onCreate: ----------------------------------------------------database error---------------------------"+e.getMessage());
            Toast.makeText(this, "error  "+e.getMessage(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),TicketfailedActivity.class).putExtra("orderid",orderid_maxvalue));
        }
    }

    private void babuLandpoints() {


        mProgress.setTitle("Processing");
        mProgress.setMessage("Please wait");
        mProgress.show();
         point =  (Total*10)/100;

        mUser=FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null) {
            userId = mUser.getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.child("BabulandPoints").exists()) {
                        databaspoint = dataSnapshot.child("BabulandPoints").getValue(Integer.class);
                        Log.d(TAG, "onDataChange: -------------------------------------babulandpoints "+databaspoint);
                        Toast.makeText(PaymentActivity.this, "datapull success", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    }else {
                        databaspoint=0;
                        Toast.makeText(PaymentActivity.this, "failed to pull data", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
            /*point=point+databaspoint;
            Log.d(TAG, "transactionSuccess: ----------------------------------------------babuland point "+ point);

            mDatabase.child("BabulandPoints").setValue(point).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        mProgress.dismiss();
                    }
                }
            });

        }else{
            Log.d(TAG, "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }


        Toast.makeText(this, "your total point is "+ point, Toast.LENGTH_SHORT).show();*/


    }

    @Override
    public void transactionFail(String s) {

    }

    @Override
    public void merchantValidationError(String s) {

    }


/*
    @Override
    public void onPaymentSuccess(String s) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            this.connection= createConnection();
            Toast.makeText(this, "database connected", Toast.LENGTH_SHORT).show();
            Statement stmt=connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();


            stmt.executeUpdate("INSERT INTO TICKET_ORDERS (ORDER_ID,CUSTOMER_ID,ORDER_TOTAL,ORDER_TIMESTAMP,USER_NAME,RECEIVED_AMOUNT,PAYMENT_TYPE) " + "VALUES (null,null,null,systimestamp,'NUR',"+Total+",'Card')");

            ResultSet rs=stmt.executeQuery(" select  MAX(ORDER_ID) from  TICKET_ORDERS ");

            while(rs.next()) {
                stringBuffer.append( rs.getString(1)+"\n");
                orderid_maxvalue = rs.getInt(1);
              //



            }
            ResultSet productid_in=stmt.executeQuery(" SELECT  PRODUCT_ID from  DEMO_PRODUCT_INFO WHERE PRODUCT_NAME='INFANT'");
            while(productid_in.next())
                productid_infant=productid_in.getInt(1);

            ResultSet productid_kidsl=stmt.executeQuery(" SELECT  PRODUCT_ID from  DEMO_PRODUCT_INFO WHERE PRODUCT_NAME='CHILDREN'");
            while(productid_kidsl.next())
                productid_kids=productid_kidsl.getInt(1);

            ResultSet productid_gardianl=stmt.executeQuery(" SELECT  PRODUCT_ID from  DEMO_PRODUCT_INFO WHERE PRODUCT_NAME='PARENT'");
            while(productid_gardianl.next())
                productid_gardian=productid_gardianl.getInt(1);

            ResultSet productid_socksl=stmt.executeQuery(" SELECT  PRODUCT_ID from  DEMO_PRODUCT_INFO WHERE PRODUCT_NAME='SOCKS'");
            while(productid_socksl.next())
                productid_socks=productid_socksl.getInt(1);


            Log.d("ordermaxvalue", "onCreate: ---------------------------------------------------ordermaxvalue="+orderid_maxvalue);
            Log.d("productid", "onCreate: ---------------------------------------------------productid="+productid_infant);
            Log.d("productid", "onCreate: ---------------------------------------------------productid="+productid_kids);
            Log.d("productid", "onCreate: ---------------------------------------------------productid="+productid_gardian);
            Log.d("productid", "onCreate: ---------------------------------------------------productid="+productid_socks);
            if(infant_ticket!=0)
                stmt.executeUpdate("INSERT INTO TICKET_ORDER_ITEMS (ORDER_ITEM_ID,ORDER_ID,PRODUCT_ID,UNIT_PRICE,QUANTITY) " + "VALUES (null,"+orderid_maxvalue+","+productid_infant+",null,"+infant_ticket+")");

            if(kids_ticket!=0)
                stmt.executeUpdate("INSERT INTO TICKET_ORDER_ITEMS (ORDER_ITEM_ID,ORDER_ID,PRODUCT_ID,UNIT_PRICE,QUANTITY) " + "VALUES (null,"+orderid_maxvalue+","+productid_kids+",null,"+kids_ticket+")");

            if(gardian_ticket!=0)
                stmt.executeUpdate("INSERT INTO TICKET" +"_ORDER_ITEMS (ORDER_ITEM_ID,ORDER_ID,PRODUCT_ID,UNIT_PRICE,QUANTITY) " + "VALUES (null,"+orderid_maxvalue+","+productid_gardian+",null,"+gardian_ticket+")");

            if(socks_ticket!=0)
                stmt.executeUpdate("INSERT INTO TICKET_ORDER_ITEMS (ORDER_ITEM_ID,ORDER_ID,PRODUCT_ID,UNIT_PRICE,QUANTITY) " + "VALUES (null,"+orderid_maxvalue+","+productid_socks+",null,"+socks_ticket+")");


            //orderid=Integer.parseInt(stringBuffer.toString());
            Toast.makeText(this, "orderid is ="+orderid_maxvalue, Toast.LENGTH_SHORT).show();
            connection.close();

            startActivity(new Intent(getApplicationContext(),QrcodeActivity.class).putExtra("totalammount",Total).putExtra("branchname",branchname));
            finish();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("errorsql", "onCreate: ------------------------------------------------------"+e.getMessage());
            startActivity(new Intent(getApplicationContext(),TicketfailedActivity.class).putExtra("orderid",orderid_maxvalue));
            finish();

        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("sql exception", "onCreate: ----------------------------------------------------database error---------------------------"+e.getMessage());
            Toast.makeText(this, "error  "+e.getMessage(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),TicketfailedActivity.class).putExtra("orderid",orderid_maxvalue));
        }
    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(this, "Paymetn failed", Toast.LENGTH_SHORT).show();
    }


    public void startPayment() {
        *//**
         * Instantiate Checkout
         *//*
        Checkout checkout = new Checkout();

        *//**
         * Set your logo here
         *//*
        //checkout.setImage(R.drawable.logo);

        *//**
         * Reference to current activity
         *//*
        final Activity activity = this;

        *//**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         *//*
        try {
            JSONObject options = new JSONObject();

            *//**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             *//*
            options.put("name", "Techriz");

            *//**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             *//*
            options.put("description", "Test Order");
            //0options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");

            *//**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             *//*
            options.put("amount", "5000");

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("error", "Error in starting Razorpay Checkout", e);
            Log.d(TAG, "startPayment: -------------------------------------------error: "+e);
        }
    }

    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {

        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }*/
}
