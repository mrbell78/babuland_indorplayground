package com.babuland.babuland.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.babuland.babuland.R;

public class AboutBabulandActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_babuland2);
        webView=findViewById(R.id.wevbiew_aboutbabuland);

        webView.loadUrl("https://www.facebook.com/babulandbd/");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
}
