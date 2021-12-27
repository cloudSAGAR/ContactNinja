package com.contactninja.Setting;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;

public class TemplateActivity extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener{
    ImageView iv_back;
    LinearLayout demo_layout,add_new_Template;
    LinearLayout mMainLayout1;
    TextView tv_create;
    private BroadcastReceiver mNetworkReceiver;
    LinearLayout mMainLayout;
    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();

    }

    private void IntentUI() {
        mMainLayout=findViewById(R.id.mMainLayout);
        mMainLayout1=findViewById(R.id.mMainLayout1);
        add_new_Template=findViewById(R.id.add_new_Template);
        add_new_Template.setOnClickListener(this);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        tv_create=findViewById(R.id.tv_create);
        tv_create.setText(getResources().getString(R.string.cratetemplate));
        demo_layout=findViewById(R.id.demo_layout);
        demo_layout.setOnClickListener(this);
        demo_layout.setVisibility(View.VISIBLE);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@SuppressLint("UnknownNullness") View v) {
        switch (v.getId()){
            case R.id.demo_layout:
                demo_layout.setVisibility(View.GONE);
                mMainLayout1.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
                case R.id.add_new_Template:
               startActivity(new Intent(getApplicationContext(),TemplateCreateActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(TemplateActivity.this, mMainLayout);
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
}