package com.contactninja.Setting;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener  {
    TextView tv_version_name;
    ImageView iv_back;
    LinearLayout layout_logout, layout_resetPassword, layout_template, layout_Current_plan, layout_about;
    SessionManager sessionManager;
    RelativeLayout mMainLayout;
    private BroadcastReceiver mNetworkReceiver;

    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mNetworkReceiver = new ConnectivityReceiver();
        sessionManager = new SessionManager(getApplicationContext());
        intentView();

        //version
        versionCode();
    }

    @SuppressLint("SetTextI18n")
    private void versionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tv_version_name.setText("Version " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void intentView() {
        mMainLayout = findViewById(R.id.mMainLayout);
        tv_version_name = findViewById(R.id.tv_version_name);
        layout_Current_plan = findViewById(R.id.layout_Current_plan);
        layout_about = findViewById(R.id.layout_about);
        layout_template = findViewById(R.id.layout_template);
        layout_resetPassword = findViewById(R.id.layout_resetPassword);
        layout_logout = findViewById(R.id.layout_logout);
        layout_template.setOnClickListener(this);
        layout_logout.setOnClickListener(this);
        layout_Current_plan.setOnClickListener(this);
        layout_resetPassword.setOnClickListener(this);
        layout_about.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
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
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(SettingActivity.this, mMainLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@SuppressLint("UnknownNullness") View v) {
        switch (v.getId()) {
            case R.id.layout_logout:
                showAlertDialogButtonClicked();
                break;
            case R.id.layout_resetPassword:
                startActivity(new Intent(getApplicationContext(), ResetActivity.class));
                break;
            case R.id.layout_template:
                startActivity(new Intent(getApplicationContext(), TemplateActivity.class));
                break;
            case R.id.layout_Current_plan:
                startActivity(new Intent(getApplicationContext(), CurrentPlanActivity.class));
                break;
            case R.id.layout_about:
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                intent.putExtra("WebUrl", Global.about);
                startActivity(intent);
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

        TextView tv_ok = customLayout.findViewById(R.id.tv_ok);
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.logoutUser();
                finish();
            }
        });
        dialog.show();
    }
}