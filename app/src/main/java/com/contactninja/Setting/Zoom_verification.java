package com.contactninja.Setting;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import retrofit2.Response;

public class Zoom_verification extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private BroadcastReceiver mNetworkReceiver;

    WebView webzoom;

    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_verification);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        initUI();

        try {
            if(Global.isNetworkAvailable(Zoom_verification.this, MainActivity.mMainLayout)) {
                /*For Zoom Oauth link*/
                Zoom_Api();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        webzoom = findViewById(R.id.webzoom);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Zoom_verification.this, webzoom);
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
    void Zoom_Api() throws JSONException {

        SignResponseModel signResponseModel= SessionManager.getGetUserdata(Zoom_verification.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.zoomAuthApp(sessionManager,obj, loadingDialog, token,Global.getVersionname(Zoom_verification.this),Global.Device, new RetrofitCallback() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ZoomExists>() {
                    }.getType();
                    ZoomExists zoomExists=new Gson().fromJson(headerString, listType);


                    try {
                        webzoom.clearCache(true);
                        webzoom.clearHistory();
                        webzoom.loadUrl("https://zoom.us/oauth/authorize?client_id=xv6fTpPkSaj5FzmhBTDlA&response_type=code&redirect_uri=https://app.contactninja.org/zoomReturnDev");
                        webzoom.getSettings().setJavaScriptEnabled(true);
                        webzoom.setWebViewClient(new HelloWebViewClient());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }


    class HelloWebViewClient extends WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            String hostURL = url.substring(url.lastIndexOf("/") + 1, url.length());

            String AccessURL = url.substring(url.lastIndexOf("/")- 2, url.length());
            String[] bits = AccessURL.split("/");
            String access = bits[bits.length-2];

            String val2 = "";
            // decode
            String substring = hostURL.substring(Math.max(hostURL.length() - 2, 0));
            if (substring.equals("code=")) {
              /*  try {
                    if (Global.isNetworkAvailable(Zoom_verification.this, mMainLayout)) {
                        if(access.equals("1")){
                            Zoom_helpZoomOauth();
                        }else {
                            showAlertDialogButtonClicked(val2);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }
    }
    void Zoom_helpZoomOauth() throws JSONException {

        SignResponseModel signResponseModel= SessionManager.getGetUserdata(Zoom_verification.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("user_tmz_id",signResponseModel.getUser().getUserTimezone().get(0).getValue());
        obj.add("data", paramObject);
        retrofitCalls.zoomIntegrationExists(sessionManager,obj, loadingDialog, token,Global.getVersionname(Zoom_verification.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ZoomExists>() {
                    }.getType();
                    ZoomExists zoomExists=new Gson().fromJson(headerString, listType);
                    if(zoomExists.getUserExists()){
                        Intent intent=new Intent(getApplicationContext(),ZoomActivity.class);
                        intent.putExtra("Email",zoomExists.getZoomUserEmail());
                        startActivity(intent);
                    }else {
                        startActivity(new Intent(getApplicationContext(), Zoom_verification.class));
                    }
                }else {
                    startActivity(new Intent(getApplicationContext(), Zoom_verification.class));
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }

    public void showAlertDialogButtonClicked(String val2) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, R.style.BottomSheetDialog);
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.item_aleart_email_access,
                        null);
        builder.setView(customLayout);
        TextView tv_add = customLayout.findViewById(R.id.tv_add);
        TextView tv_aleartMessage = customLayout.findViewById(R.id.tv_aleartMessage);
        tv_aleartMessage.setText(val2);
        AlertDialog dialog
                = builder.create();

        dialog.show();
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

                dialog.dismiss();
            }
        });


    }
}