package com.contactninja.Setting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contactninja.R;
import com.contactninja.Utils.SessionManager;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_version_name;
    ImageView iv_back;
    LinearLayout layout_logout,layout_resetPassword;
    SessionManager sessionManager;
    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sessionManager=new SessionManager(getApplicationContext());
        intentView();

        //version
        versionCode();
    }

    @SuppressLint("SetTextI18n")
    private void versionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tv_version_name.setText("Version "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void intentView() {
        tv_version_name=findViewById(R.id.tv_version_name);
        layout_resetPassword=findViewById(R.id.layout_resetPassword);
        layout_logout=findViewById(R.id.layout_logout);
        layout_logout.setOnClickListener(this);
        layout_resetPassword.setOnClickListener(this);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@SuppressLint("UnknownNullness") View v) {
        switch (v.getId()){
            case R.id.layout_logout:
                showAlertDialogButtonClicked();
                break;
                case R.id.layout_resetPassword:
                startActivity(new Intent(getApplicationContext(),ResetActivity.class));
                break;
        }
    }
    public void showAlertDialogButtonClicked() {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, R.style.MyDialogStyle);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.logout_dialog, null);
        builder.setView(customLayout);
        AlertDialog dialog
                = builder.create();

        Button btn_ok=customLayout.findViewById(R.id.btn_ok);
        Button btn_cancel=customLayout.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager=new SessionManager(getApplicationContext());
                sessionManager.logoutUser();
                finish();
            }
        });
        dialog.show();
    }
}