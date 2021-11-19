package com.intricare.test.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.intricare.test.Auth.PlanTyep.PlanType_Screen;
import com.intricare.test.R;

public class Phone_email_verificationActivity extends AppCompatActivity implements View.OnClickListener {
    TextView btn_getStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_email_verification);
        initUI();
    }

    private void initUI() {
        btn_getStarted=findViewById(R.id.btn_getStarted);
        btn_getStarted.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_getStarted:

                startActivity(new Intent(getApplicationContext(), PlanType_Screen.class));
                finish();
                break;
        }
    }
}