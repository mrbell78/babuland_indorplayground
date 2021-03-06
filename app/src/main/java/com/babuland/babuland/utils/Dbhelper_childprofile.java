package com.babuland.babuland.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbhelper_childprofile extends SQLiteOpenHelper {





    private static final String DATABASE_NAME = "Childprofile.db";
    private static final int DATABASE_VERSION = 1;
    Context context;
    SQLiteDatabase db;
    ContentResolver mContentResolver;

    public final static String TABLE_NAME = "childprofile_firebase";
    public final static String IMAGE_NAME = "name";
    public final static String ClomnID= "id";





    public Dbhelper_childprofile(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" CREATE TABLE  " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,  title TEXT,name TEXT, firebaseindex INTEGER )");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean insertdata_childpro(String title, String name, int firebaseindex){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name",name);
        contentValues.put("title",title);
        contentValues.put("firebaseindex",firebaseindex);


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


    public Cursor pulldata_childpro(){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * From "+TABLE_NAME,null);
        return cursor;
    }
    public int count_childpro(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * From "+TABLE_NAME,null);
        int count = cursor.getCount();
        return count;
    }

}
