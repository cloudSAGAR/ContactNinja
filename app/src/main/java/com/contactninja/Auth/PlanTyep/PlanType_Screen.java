package com.contactninja.Auth.PlanTyep;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class PlanType_Screen extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener  {

    LinearLayout layout_free_card, layout_bz_card, layout_master, layout_contect;
    int flag = 0;
    private BroadcastReceiver mNetworkReceiver;
    ConstraintLayout mMainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_type_screen);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();

    }

    private void IntentUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        layout_free_card = findViewById(R.id.layout_free_card);
        layout_bz_card = findViewById(R.id.layout_bz_card);
        layout_master = findViewById(R.id.layout_master);
        layout_contect = findViewById(R.id.layout_contect);

        layout_free_card.setOnClickListener(this);
        layout_bz_card.setOnClickListener(this);
        layout_master.setOnClickListener(this);
        layout_contect.setOnClickListener(this);

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), Plan_Detail_Screen.class);
        switch (v.getId()) {
            case R.id.layout_free_card:
                /**
                 * This is free card */
                intent.putExtra("flag", 1);
                intent.putExtra("plan_product_id", "0");
                startActivity(intent);
                finish();
                break;
            case R.id.layout_bz_card:
                /**
                 * This is 9.95 card */
                intent.putExtra("flag", 2);
                intent.putExtra("plan_product_id", getResources().getString(R.string.plan_9));
                startActivity(intent);
                finish();
                break;
            case R.id.layout_master:
                /**
                 * This is 39.95 card */
                intent.putExtra("flag", 3);
                intent.putExtra("plan_product_id", getResources().getString(R.string.plan_39));
                startActivity(intent);
                finish();
                break;
            case R.id.layout_contect:
                /**
                 * This is 69.95 card */
                intent.putExtra("flag", 4);
                intent.putExtra("plan_product_id", getResources().getString(R.string.plan_69));
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(PlanType_Screen.this, mMainLayout);
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