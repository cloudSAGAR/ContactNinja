package com.contactninja.Broadcast_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.contactninja.Auth.LoginActivity;
import com.contactninja.MainActivity;
import com.contactninja.R;

public class Brodcsast_Tankyou extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brodcsast_tankyou);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        }, 2000);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();

    }
}