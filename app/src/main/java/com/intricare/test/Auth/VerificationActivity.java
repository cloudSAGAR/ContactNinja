package com.intricare.test.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.intricare.test.Auth.PlanTyep.PlanType_Screen;
import com.intricare.test.R;


public class VerificationActivity extends AppCompatActivity {

    PinView otp_pinview;
    TextView verfiy_button,resend_txt,tc_wrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        IntentUI();
        verfiy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), PlanType_Screen.class));
            }
        });

    }

    private void IntentUI() {
        otp_pinview=findViewById(R.id.otp_pinview);
        verfiy_button=findViewById(R.id.verfiy_button);
        resend_txt=findViewById(R.id.resend_txt);
        tc_wrong=findViewById(R.id.tc_wrong);
    }
}