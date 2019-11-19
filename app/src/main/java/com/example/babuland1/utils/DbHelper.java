package com.example.babuland1.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import static android.provider.BaseColumns._ID;

/**
 * Created by delaroy on 9/8/17.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "SaveBitmap.db";
    private static final int DATABASE_VERSION = 2;
    Context context;
    SQLiteDatabase db;
    ContentResolver mContentResolver;

    public final static String TABLE_NAME = "Bitmaatmp";
    public final static String COLUMN_DATA = "Data";
    public final static String IMAGE_NAME = "Name";
    public final static String ClomnID= "id";

    String statement = "create table "+ TABLE_NAME + "("+ClomnID+" integer primary key autoincrement, "+ IMAGE_NAME + "text not null,"+COLUMN_DATA+"BLOB"+ ")";




    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mContentResolver = context.getContentResolver();

        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(statement);

    }

    public void addToDb(String name,byte[] image){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(IMAGE_NAME,name);
        cv.put(COLUMN_DATA,image);
        db.insert( TABLE_NAME, null, cv );
        //Toast.makeText(context, "data upload successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }



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
    }

}

