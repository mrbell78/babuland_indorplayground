package com.example.babuland1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babuland1.R;
import com.google.android.gms.tasks.OnCompleteListener;
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

import com.sslwireless.sslcommerzlibrary.model.initializer.CustomerInfoInitializer;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization;
import com.sslwireless.sslcommerzlibrary.model.response.TransactionInfoModel;
import com.sslwireless.sslcommerzlibrary.model.util.CurrencyType;
import com.sslwireless.sslcommerzlibrary.model.util.SdkType;
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz;
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.TransactionResponseListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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
    private  static String  DEFAULT_USERNAME;
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

     String timestapm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        mstorage= FirebaseStorage.getInstance().getReference();
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        mProgress=new ProgressDialog(this);
        if(mUser!=null){
            userId=mUser.getUid();
            mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(userId);
            getphone(mDatabase);
        }else{
            Log.d(TAG, "onCreate: ----------------------------------------------------------------------------firebase user ---------------------------------------------------"+userId);
        }

        Intent intent = getIntent();
        infant_ticket = intent.getIntExtra("infant",0);
        kids_ticket = intent.getIntExtra("kids",0);
        gardian_ticket = intent.getIntExtra("gardian",0);
        socks_ticket = intent.getIntExtra("socks",0);
        branchname=intent.getStringExtra("radiovalue_string");
        DEFAULT_USERNAME=branchname;

        if(branchname.equals("mirpur")){
            DEFAULT_USERNAME="bland";
            Log.d(TAG, "onCreate: branch name--------------------------------- "+DEFAULT_USERNAME);
        }else if(branchname.equals("wari")){
            DEFAULT_USERNAME="wari";
            Log.d(TAG, "onCreate: branch name--------------------------------- "+DEFAULT_USERNAME);
        }else if(branchname.equals("uttara")){
            DEFAULT_USERNAME="uttara";
            Log.d(TAG, "onCreate: branch name--------------------------------- "+DEFAULT_USERNAME);
        }
        Log.d(TAG, "onCreate: -------------------------------------infat ticket num-----------------------"+infant_ticket);
        Log.d(TAG, "onCreate: -------------------------------------kids ticket num-----------------------"+kids_ticket);
        Log.d(TAG, "onCreate: -------------------------------------infat ticket num-----------------------"+gardian_ticket);
        Log.d(TAG, "onCreate: -------------------------------------infat ticket num-----------------------"+socks_ticket);

        Log.d(TAG, "onCreate: dbname  ------------------------"+DEFAULT_USERNAME);

        //pruduct id event start mirpur


        infant_ticket_product_id = intent.getIntExtra("infant_id",0);
        kids_ticket_product_id = intent.getIntExtra("kids_id",0);
        gardian_ticket_product_id = intent.getIntExtra("gardian_id",0);
        socks_ticket_product_id = intent.getIntExtra("socks_id",0);
        //branchname=intent.getStringExtra("radiovalue_string");


        // product id event end

        //vieworginal start



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
/*
                mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
               getphone(mDatabase);*/
                Log.d(TAG, "onClick: ---------------------------------------phone "+phone);
                if(!phone.equals("default") && phone!=null){

                    if(Total!=0){
                        startPayment();
                    }else {

                        Toast.makeText(PaymentActivity.this, "Transection failed. no payable amount ", Toast.LENGTH_SHORT).show();

                    }


                }else {

                    Toast.makeText(PaymentActivity.this, "Phone number required", Toast.LENGTH_SHORT).show();
                    dialogbox_phonenumber();

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

        transectionSuccess();
    }

    private void transectionSuccess() {



        try {
            this.connection = createConnection();
            Toast.makeText(this, "database connected", Toast.LENGTH_SHORT).show();
            Statement stmt=connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();


            if(branchname.equals("mirpur")){

                stmt.executeUpdate("INSERT INTO TICKET_ORDERS (ORDER_ID,CUSTOMER_ID,ORDER_TOTAL,ORDER_TIMESTAMP,USER_NAME,RECEIVED_AMOUNT,PAYMENT_TYPE,PHONE) " + "VALUES (null,null,null,systimestamp,'NUR',"+Total+",'Card',"+phone+")");

                ResultSet productid_gardianl=stmt.executeQuery(" SELECT  PRODUCT_ID from  DEMO_PRODUCT_INFO WHERE PRODUCT_NAME='PARENT'");
                while(productid_gardianl.next())
                    productid_gardian=productid_gardianl.getInt(1);

            }else if(branchname.equals("wari")){

                stmt.executeUpdate("INSERT INTO TICKET_ORDERS (ORDER_ID,CUSTOMER_ID,ORDER_TOTAL,ORDER_TIMESTAMP,USER_NAME,RECEIVE_AMOUNT,PAYMENT_TYPE,PHONE_NUMBER) " + "VALUES (null,null,null,systimestamp,'NUR',"+Total+",'Card',"+phone+")");

                ResultSet productid_gardianl=stmt.executeQuery(" SELECT  PRODUCT_ID from  DEMO_PRODUCT_INFO WHERE PRODUCT_NAME='GUARDIAN'");
                while(productid_gardianl.next())
                    productid_gardian=productid_gardianl.getInt(1);
            }
            else if(branchname.equals("uttara")){

                            stmt.executeUpdate("INSERT INTO TICKET_ORDERS (ORDER_ID,CUSTOMER_ID,ORDER_TOTAL,ORDER_TIMESTAMP,USER_NAME,RECEIVE_AMOUNT,PAYMENT_TYPE,PHONE_NUMBER) " + "VALUES (null,null,null,systimestamp,'NUR',"+Total+",'Card',"+phone+")");

                        ResultSet productid_gardianl=stmt.executeQuery(" SELECT  PRODUCT_ID from  DEMO_PRODUCT_INFO WHERE PRODUCT_NAME='GUARDIAN'");
                        while(productid_gardianl.next())
                            productid_gardian=productid_gardianl.getInt(1);
                        }


            ResultSet rs=stmt.executeQuery(" select  MAX(ORDER_ID) from  TICKET_ORDERS ");

            while(rs.next()) {
                stringBuffer.append( rs.getString(1)+"\n");
                orderid_maxvalue = rs.getInt(1);
                //
            }
            ResultSet productid_in=stmt.executeQuery(" SELECT  PRODUCT_ID from  DEMO_PRODUCT_INFO WHERE PRODUCT_NAME='INFANT'");
            while(productid_in.next())
                productid_infant=productid_in.getInt(1);

            ResultSet productid_kidsl=stmt.executeQuery(" SELECT  PRODUCT_ID from  DEMO_PRODUCT_INFO WHERE PRODUCT_NAME  ='CHILDREN'");
            while(productid_kidsl.next())
                productid_kids=productid_kidsl.getInt(1);



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
                    .putExtra("orderid",orderid_maxvalue).putExtra("transectionid",gatawayname).putExtra("phone",phone));
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


    }

    @Override
    public void transactionFail(String s) {

    }

    @Override
    public void merchantValidationError(String s) {

    }


    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {

        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {


        return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);

    }


    private  String getphone(DatabaseReference mDatabase){





        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("phone").exists()){


                    phone=dataSnapshot.child("phone").getValue().toString();
                    Log.d(TAG, "onDataChange: -------------------------phone "+phone);


                }else {
                    Toast.makeText(PaymentActivity.this, "db problem", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return phone;
    }


    public void dialogbox_phonenumber(){
        AlertDialog.Builder albuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.phonenumber_dialog,null);
        albuilder.setView(view);
        final AlertDialog dialog = albuilder.create();
        if(!isFinishing()){
            dialog.show();
        }

        Button btn_continiue = view.findViewById(R.id.btn_continiue);
        final EditText edtphone=view.findViewById(R.id.idPhone);

        btn_continiue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone=edtphone.getText().toString();

                if(phone.length()<10){
                    Toast.makeText(PaymentActivity.this, "Please Enter valid phonenumber", Toast.LENGTH_SHORT).show();
                }else if(phone!=null && phone.length()>10){

                    mUser= FirebaseAuth.getInstance().getCurrentUser();
                    String mUserid=mUser.getUid();
                    mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child(mUserid);
                    mDatabase.child("phone").setValue(phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                dialog.dismiss();
                                Toast.makeText(PaymentActivity.this, "Thank you", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }
        });
    }


}
