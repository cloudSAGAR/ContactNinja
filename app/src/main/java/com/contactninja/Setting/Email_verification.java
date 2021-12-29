package com.contactninja.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.contactninja.R;

import java.util.Objects;

public class Email_verification extends AppCompatActivity {
    WebView webEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        webEmail = findViewById(R.id.webEmail);
        webEmail.clearCache(true);
        webEmail.getSettings().setJavaScriptEnabled(true);
        webEmail.loadUrl("https://app.contactninja.org/email_api/callback.php");
        webEmail.setHorizontalScrollBarEnabled(false);
        webEmail.setWebViewClient(new HelloWebViewClient());

    }
    private class HelloWebViewClient extends WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {

            String hostURL = url.substring(url.lastIndexOf("/") + 1, url.length());
            if (hostURL.equals("stripesuccess")) {
                //Continue connect to stripe
            } else {

               // finish();
            }
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }
    }
}