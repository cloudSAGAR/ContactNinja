package com.contactninja.Auth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.R;
import com.contactninja.Utils.SessionManager;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class SplashActivity extends AppCompatActivity {
    SessionManager securityManager;
    Handler  mHandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        securityManager= new SessionManager(SplashActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Runnable mRunnable = () -> {
            securityManager.checkLogin();
            finish();
            
        };
        int SPLASH_DISPLAY_LENGTH = 2000;
        mHandler.postDelayed(mRunnable, SPLASH_DISPLAY_LENGTH);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}