package com.contactninja.Manual_email_text;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Email_Tankyou extends AppCompatActivity  implements ConnectivityReceiver.ConnectivityReceiverListener{
    TextView tv_sub_titale;
    String s_name="";
    private BroadcastReceiver mNetworkReceiver;
    LinearLayout mMainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_tankyou);
        mNetworkReceiver = new ConnectivityReceiver();
        mMainLayout = findViewById(R.id.mMainLayout);
        tv_sub_titale=findViewById(R.id.tv_sub_titale);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        s_name=bundle.getString("s_name","");

            if (s_name.equals("final"))
            {
                tv_sub_titale.setText("Your task \n has been Schedule!");
            }
            else {
                tv_sub_titale.setText("Your task \n has been added!");
            }


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        }, 2000);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();

    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Email_Tankyou.this, mMainLayout);
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