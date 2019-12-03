package com.example.babuland1.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by delaroy on 9/8/17.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "qrimage_child.db";
    private static final int DATABASE_VERSION = 1;
    Context context;
    SQLiteDatabase db;
    ContentResolver mContentResolver;

    public final static String TABLE_NAME = "qr_child";
    public final static String IMAGE_NAME = "name";
    public final static String ClomnID= "id";
    public final static String Totalamount= "total";




    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" CREATE TABLE  " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,  name TEXT, total integer not null,orderid INTEGER not null,status TEXT,branchname TEXT )");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean insertdata(String name, int total,int orderid,String status,String branchname){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name",name);
        contentValues.put("total",total);
        contentValues.put("orderid",orderid);
        contentValues.put("status",status);
        contentValues.put("branchname",branchname);

        long result =  db.insert(TABLE_NAME,null,contentValues);

        if(result==-1){
            return false;
        }else {
            return true;
        }

    }
/*
    public byte[] getBitmapName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] select ={COLUMN_DATA};

        qb.setTables(TABLE_NAME);

        Cursor cursor = qb.query(db,select,"Name= ?",new String[]{name},null,null,null);

        byte[] result = null;
        if(cursor.moveToFirst()){
            do{
                result=cursor.getBlob(cursor.getColumnIndex(COLUMN_DATA));
            }while (cursor.moveToNext());


        }
        return result;
    }*/


 public Cursor pulldata(){

     SQLiteDatabase db = this.getWritableDatabase();

     Cursor cursor = db.rawQuery("select * From "+TABLE_NAME,null);
     return cursor;
 }
 public int count(){

     SQLiteDatabase db = this.getWritableDatabase();
     Cursor cursor = db.rawQuery("select * From "+TABLE_NAME,null);
     int count = cursor.getCount();
     return count;
 }

}

