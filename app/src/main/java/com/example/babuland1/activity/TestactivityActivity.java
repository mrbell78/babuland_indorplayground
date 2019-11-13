package com.example.babuland1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babuland1.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestactivityActivity extends AppCompatActivity {

    TextView productid_infant,productid_kids,productid_gardian,productid_socks;
    TextView quantity_infant,quantity_kids,quantity_gardian,quantity_socks;
    TextView branchname;
    TextView total;
    int orderid_maxvalue;


    public static final String DEFAULT_DRIVER="oracle.jdbc.driver.OracleDriver";
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@itlimpex.ddns.net:2121:xe";
    private static final String DEFAULT_USERNAME = "bland";
    private static final String DEFAULT_PASSWORD = "servicepack3";

    private Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testactivity);

        productid_infant=findViewById(R.id.productid_infant);
        productid_kids=findViewById(R.id.productid_kids);
        productid_gardian=findViewById(R.id.productid_gardian);
        productid_socks=findViewById(R.id.productid_socks);



        quantity_infant=findViewById(R.id.quantity_infant);
        quantity_kids=findViewById(R.id.quantity_kids);
        quantity_gardian=findViewById(R.id.quantity_gardian);
        quantity_socks=findViewById(R.id.quantity_socks);

        branchname=findViewById(R.id.branchname);

        total=findViewById(R.id.total);


        Intent intent = getIntent();

        int productid_infantl = intent.getIntExtra("infant_id",0);
        int productid_kidsl = intent.getIntExtra("kids_id",0);
        int productid_gardianl = intent.getIntExtra("gardian_id",0);
        int productid_socksl = intent.getIntExtra("socks_id",0);



        productid_infant.setText(Integer.toString(productid_infantl));
        productid_kids.setText(Integer.toString(productid_kidsl));
        productid_gardian.setText(Integer.toString(productid_gardianl));
        productid_socks.setText(Integer.toString(productid_socksl));



        int quantity_infantl=intent.getIntExtra("infant_quantity",0);
        int quantity_kidsl=intent.getIntExtra("kids_quantity",0);
        int quantity_gardianl=intent.getIntExtra("gardian_quantity",0);
        int quantity_socksl=intent.getIntExtra("socks_quantity",0);


        quantity_infant.setText(Integer.toString(quantity_infantl));
        quantity_kids.setText(Integer.toString(quantity_kidsl));
        quantity_gardian.setText(Integer.toString(quantity_gardianl));
        quantity_socks.setText(Integer.toString(quantity_socksl));

        String branchnamel = intent.getStringExtra("branchname");
        branchname.setText(branchnamel);


        int totall=intent.getIntExtra("total",0);

        total.setText(Integer.toString(totall));


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            this.connection= createConnection();
            Toast.makeText(this, "database connected", Toast.LENGTH_SHORT).show();
            Statement stmt=connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();


            stmt.executeUpdate("INSERT INTO TICKET_ORDERS (ORDER_ID,CUSTOMER_ID,ORDER_TOTAL,ORDER_TIMESTAMP,USER_NAME,RECEIVED_AMOUNT,PAYMENT_TYPE) " + "VALUES (null,null,null,systimestamp,'NUR',500,'Card')");

            ResultSet rs=stmt.executeQuery(" select  MAX(ORDER_ID) from  TICKET_ORDERS ");

            while(rs.next()) {
                stringBuffer.append( rs.getString(1)+"\n");
                orderid_maxvalue = rs.getInt(1);



            }
            Log.d("ordermaxvalue", "onCreate: ---------------------------------------------------ordermaxvalue="+orderid_maxvalue);

            stmt.executeUpdate("INSERT INTO TICKET_ORDER_ITEMS (ORDER_ITEM_ID,ORDER_ID,PRODUCT_ID,UNIT_PRICE,QUANTITY) " + "VALUES (null,"+orderid_maxvalue+",247,300,3)");


            //orderid=Integer.parseInt(stringBuffer.toString());
            Toast.makeText(this, "orderid is ="+orderid_maxvalue, Toast.LENGTH_SHORT).show();
            connection.close();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("errorsql", "onCreate: ------------------------------------------------------"+e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("sql exception", "onCreate: ----------------------------------------------------database error---------------------------"+e.getMessage());
            Toast.makeText(this, "error  "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {

        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }
}
