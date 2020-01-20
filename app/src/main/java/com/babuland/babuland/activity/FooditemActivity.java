package com.babuland.babuland.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.babuland.babuland.R;
import com.babuland.babuland.adapter.CustomAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FooditemActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    CustomAdapter adapter;
    String[] heading;
    String []content;
    int []imgrclr={R.drawable.chicken_cashwnut_salad,R.drawable.chicken_pizza,R.drawable.thai_soup_thick,R.drawable.cream_of_mushroom,R.drawable.chicken_tender,R.drawable.chicken_lolipop};


    Toolbar mToolbar;

    public static final String DEFAULT_DRIVER="oracle.jdbc.driver.OracleDriver";
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@itlimpex.ddns.net:2121:xe";
    private  static String  DEFAULT_USERNAME;
    private static final String DEFAULT_PASSWORD = "servicepack3";
    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooditem);



        mToolbar=findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        //db=new DbHelper_freeTicket(this);
        getSupportActionBar().setTitle("Babuland Food Items");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            this.connection = createConnection();


            Toast.makeText(this, "database connected", Toast.LENGTH_SHORT).show();
            Statement stmt=connection.createStatement();

            ResultSet fodditem = stmt.executeQuery("select PRODUCT_NAME,CATEGORY,PRODUCT_AVAIL,LIST_PRICE,BUYING_PRICE,VENDOR from DEMO_PRODUCT_INFO");
            while (fodditem.next()){
                String foodname = fodditem.getString("PRODUCT_NAME");
                String pirce= fodditem.getString("LIST_PRICE");
                Log.d("foodname", "verify_user: .................food name " + foodname+"price"+pirce);

            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        recyclerView=findViewById(R.id.fooditem_recylerview);

        heading=getResources().getStringArray(R.array.heading);
        content=getResources().getStringArray(R.array.contetn);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter=new CustomAdapter(this,imgrclr,heading,content);
        recyclerView.setAdapter(adapter);
    }



    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {

        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {


        return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);

    }
}
