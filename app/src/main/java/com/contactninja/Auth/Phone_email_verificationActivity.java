package com.contactninja.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.Auth.PlanTyep.PlanType_Screen;
import com.contactninja.R;
import com.contactninja.Utils.SessionManager;

public class Phone_email_verificationActivity extends AppCompatActivity implements View.OnClickListener {
    TextView btn_getStarted;
    SessionManager sessionManager;
    LinearLayout layout_phonenumber,layout_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_email_verification);
        initUI();
        check_login_type();
    }

    private void check_login_type() {

        String login_type=sessionManager.getlogin_type(this);
        Log.e("Login Type ",login_type);
        if (login_type.equals("PHONE"))
        {
            layout_email.setVisibility(View.VISIBLE);
            layout_phonenumber.setVisibility(View.GONE);
        }
        else {
            layout_email.setVisibility(View.GONE);

            layout_phonenumber.setVisibility(View.VISIBLE);
        }
    }

    private void initUI() {
        btn_getStarted=findViewById(R.id.btn_getStarted);
        btn_getStarted.setOnClickListener(this);
        sessionManager=new SessionManager(this);
        layout_phonenumber=findViewById(R.id.layout_phonenumber);
        layout_email=findViewById(R.id.layout_email);
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