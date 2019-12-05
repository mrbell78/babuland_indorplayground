package com.babuland.babuland1.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedprefManager {

    private static final String SHAREDPRE_NAME="fcmdemo";
    private static final String KEY_ACCESS_TOKEN="token";

    private static Context context;
    private static SharedprefManager manager;

    public SharedprefManager(Context context) {

        this.context=context;
    }

    public static synchronized SharedprefManager getInstance(Context context){
        if(manager==null)
            manager=new SharedprefManager(context);

        return manager;
    }

    public boolean storeToken(String token){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_NAME,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN,token);
        editor.apply();
        return true;

    }


    public String getToken(){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPRE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN,null);

    }
}
