package com.contactninja.Bzcard.Media.Video;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.Model.Bzcard_Fields_Model;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")

public class Video_LinkAdd_Activity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    RelativeLayout mMainLayout;
    ImageView iv_back;
    EditText edt_link_youtube;
    TextView btn_Link, txt_invalid_link;
    Bzcard_Fields_Model.BZ_media_information information;
    private BroadcastReceiver mNetworkReceiver;
    private long mLastClickTime = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_linkadd);
        mNetworkReceiver = new ConnectivityReceiver();
        initUI();
        
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            information = (Bzcard_Fields_Model.BZ_media_information) getIntent().getSerializableExtra("MyClass");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        edt_link_youtube = findViewById(R.id.edt_link_youtube);
        txt_invalid_link = findViewById(R.id.txt_invalid_link);
        btn_Link = findViewById(R.id.btn_Link);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        btn_Link.setOnClickListener(this);
        
        
        edt_link_youtube.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                
                if (Global.isValidURL(s.toString()) == true) {
                    txt_invalid_link.setVisibility(View.GONE);
                } else {
                    txt_invalid_link.setVisibility(View.VISIBLE);
                }
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            
            }
        });
        
        
    }
    
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Video_LinkAdd_Activity.this, mMainLayout);
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
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_Link:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                String link = edt_link_youtube.getText().toString().trim();
                if (Vlidaction()) {
                    Intent intent = new Intent(getApplicationContext(), Add_Video_Activity.class);
                    intent.putExtra("record", link);
                    intent.putExtra("MyClass", information);
                    startActivity(intent);
                }
                
                break;
        }
    }
    
    private boolean Vlidaction() {
        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
        if (!edt_link_youtube.getText().toString().isEmpty() && edt_link_youtube.getText().toString().matches(pattern)) {
            txt_invalid_link.setVisibility(View.GONE);
            return true;
        } else {
            txt_invalid_link.setVisibility(View.VISIBLE);
        }
        if (Global.isValidURL(edt_link_youtube.getText().toString()) == true) {
            txt_invalid_link.setVisibility(View.GONE);
            return true;
        } else {
            txt_invalid_link.setVisibility(View.VISIBLE);
        }
        return false;
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}