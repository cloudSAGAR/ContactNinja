package com.contactninja.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.contactninja.R;

public class WebActivity extends AppCompatActivity {
    WebView wv_url;
    String webURL = "";
    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initUI();
        webURL = getIntent().getStringExtra("WebUrl");
        if (webURL != null) {
            wv_url.loadUrl(webURL);
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        wv_url = findViewById(R.id.wv_url);
        wv_url.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = wv_url.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
}