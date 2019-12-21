package com.babuland.babuland.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Posticketing extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="DEMO_TICKETING";
    public static final String TABLE_NAME="TICKET_ORDERS";
    public static final String  col1="ID";
    public static final String  col2="ORDER_TOTAL";
    public static final String  col3="ORDER_TIMESTAMP";
    public static final String  col4="USER_NAME";
    public static final String  col5="CUSTOMER_ID";
    public static final String  col6="RECEIVED_AMOUNT";
    public static final String  col7="PAYMENT_TYPE";
    public static final String  col8="PHONE";
    public static final String  col9="DISCOUNT";
    public static final String  col10="BANK_ID";

    public Posticketing(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, ORDER_TOTAL DECIMAL(8.2)  ,ORDER_TIMESTAMP DATE, USER_NAME TEXT, CUSTOMER_ID INTEGER,RECEIVED_AMOUNT TEXT,PAYMENT_TYPE TEXT,PHONE TEXT,DISCOUNT DECIMAL,BANK_ID INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
