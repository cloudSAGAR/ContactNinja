package com.contactninja.Manual_email_and_sms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.contactninja.R;

public class Manual_Sms_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_sms);
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}