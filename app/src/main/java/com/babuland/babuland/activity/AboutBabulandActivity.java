package com.babuland.babuland.activity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.babuland.babuland.R;
import com.novoda.merlin.Merlin;

public class AboutBabulandActivity extends AppCompatActivity {

    WebView webView;
    Boolean netstatus=false;
    Merlin merlin;
    boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_babuland2);
        webView=findViewById(R.id.wevbiew_aboutbabuland);


        merlin = new Merlin.Builder().withConnectableCallbacks().build(this);

        WebSettings webSettings = webView.getSettings();
        webSettings.setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
        webSettings.setAppCachePath(this.getCacheDir().getAbsolutePath());
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);




        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

        }
        else
            connected = false;

        if (!connected) { // loading offline
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webView.loadUrl("http://babuland.com/");
        webSettings.setJavaScriptEnabled(true);















    }


    @Override
    protected void onResume() {
        super.onResume();
        merlin.bind();
    }
    @Override
    protected void onPause() {
        merlin.unbind();
        super.onPause();
    }
}
