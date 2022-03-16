package com.contactninja.Setting;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contactninja.MainActivity;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.ZoomExists;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;

import retrofit2.Response;

public class ZoomActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private BroadcastReceiver mNetworkReceiver;
    RelativeLayout mMainLayout;
    public static TextView tv_email,btn_Disconnect;
    String Email="";
    private long mLastClickTime=0;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(ZoomActivity.this);
        retrofitCalls = new RetrofitCalls(ZoomActivity.this);
        sessionManager = new SessionManager(getApplicationContext());
        initUI();

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            Email = bundle.getString("Email");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(Email.equals("")){
            Intent intent=new Intent(getApplicationContext(),Verification_web.class);
            intent.putExtra("Activtiy","zoom");
            intent.putExtra("ZoomActivity","ZoomActivity");
            startActivity(intent);
        }else {
            tv_email.setText(Email);
        }
        if(tv_email.getText().toString().equals("")){
            finish();
        }

    }

    private void initUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        tv_email = findViewById(R.id.tv_email);
        btn_Disconnect = findViewById(R.id.btn_Disconnect);
        btn_Disconnect.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(ZoomActivity.this, mMainLayout);
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
        switch (v.getId()){
            case R.id.btn_Disconnect:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                try {
                    if(Global.isNetworkAvailable(ZoomActivity.this, MainActivity.mMainLayout)) {
                        /*Disconnect Zoom Account*/
                        Zoom_Disconnect();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break ;
            case  R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    void Zoom_Disconnect() throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel= SessionManager.getGetUserdata(ZoomActivity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.ZoomDisconnect(sessionManager,obj, loadingDialog, token,Global.getVersionname(ZoomActivity.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ZoomExists>() {
                    }.getType();
                    ZoomExists zoomExists=new Gson().fromJson(headerString, listType);
                    if(zoomExists.getRevokedTokens()){
                        Intent intent=new Intent(getApplicationContext(),SettingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }
}