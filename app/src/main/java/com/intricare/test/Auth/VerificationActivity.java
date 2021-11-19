package com.intricare.test.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.intricare.test.Auth.PlanTyep.PlanType_Screen;
import com.intricare.test.R;
import com.intricare.test.Utils.OTP_Receiver;

import java.util.ArrayList;


public class VerificationActivity extends AppCompatActivity {

    PinView otp_pinview;
    TextView verfiy_button,resend_txt,tc_wrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        EnableRuntimePermission();
        IntentUI();
        verfiy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Phone_email_verificationActivity.class));
                finish();
            }
        });
        new OTP_Receiver().setEditText(otp_pinview);

    }

    private void IntentUI() {
        otp_pinview=findViewById(R.id.otp_pinview);
        verfiy_button=findViewById(R.id.verfiy_button);
        resend_txt=findViewById(R.id.resend_txt);
        tc_wrong=findViewById(R.id.tc_wrong);
    }

    public void EnableRuntimePermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.RECEIVE_SMS)
                .check();

        startActivity(new Intent(getApplicationContext(), VerificationActivity.class));

    }

}