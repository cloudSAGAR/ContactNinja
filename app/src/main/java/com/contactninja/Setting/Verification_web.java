package com.contactninja.Setting;

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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.MainActivity;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UserLinkedList;
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
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Verification_web extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    WebView webEmail;
    LinearLayout mMainLayout;

    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    String create="",Activtiy_back="",ZoomAct="";
    private BroadcastReceiver mNetworkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_web);

        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        mMainLayout = findViewById(R.id.mMainLayout);
        webEmail = findViewById(R.id.webEmail);
        try {
            Intent intent=getIntent();
            Bundle bundle=intent.getExtras();
            create=bundle.getString("create");
            Activtiy_back=bundle.getString("Activtiy");
            ZoomAct=bundle.getString("ZoomActivity");
        }catch (Exception e){
            e.printStackTrace();
        }

        if(Global.IsNotNull(Activtiy_back)&&Activtiy_back.equals("zoom")){
            //zoom verification
            try {
                if(Global.isNetworkAvailable(Verification_web.this, MainActivity.mMainLayout)) {
                    /*For Zoom Oauth link*/
                    Zoom_Api();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            //Email verification
            if(Global.IsNotNull(create)||create.equals("create")){
                try {
                    webEmail.clearCache(true);
                    webEmail.clearHistory();
                    webEmail.loadUrl(Global.Email_auth);
                    webEmail.getSettings().setJavaScriptEnabled(true);
                    webEmail.getSettings().setUserAgentString("contactninja");
                    webEmail.setHorizontalScrollBarEnabled(false);
                    webEmail.setWebViewClient(new HelloWebViewClient());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }else {
                try {
                    if(Global.isNetworkAvailable(Verification_web.this,mMainLayout)){
                        Mail_list();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Verification_web.this, mMainLayout);
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

    private void GoogleAuth(String val2) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(getApplicationContext());
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("email_address", val2);
        paramObject.addProperty("is_default", "1");
        obj.add("data", paramObject);
        retrofitCalls.Gmailauth_update(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(Verification_web.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        if (response.body().getHttp_status() == 200) {
                            webEmail.clearHistory();
                            webEmail.clearFormData();
                            webEmail.clearCache(true);

                            try {
                                if(Global.isNetworkAvailable(Verification_web.this,mMainLayout)){
                                    Mail_list();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            loadingDialog.cancelLoading();
                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }
    private void Mail_list() throws JSONException {

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Verification_web.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("include_smtp","1");
        obj.add("data", paramObject);
        retrofitCalls.Mail_list(sessionManager, obj, loadingDialog, token,Global.getVersionname(Verification_web.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UserLinkedList>() {
                    }.getType();
                    UserLinkedList userLinkedGmail = new Gson().fromJson(headerString, listType);
                    List<UserLinkedList.UserLinkedGmail> setUserLinkedGmail = userLinkedGmail.getUserLinkedGmail();
                    sessionManager.setUserLinkedGmail(getApplicationContext(),setUserLinkedGmail);
                    finish();
                }else {
                    webEmail.clearCache(true);
                    webEmail.clearHistory();
                    webEmail.loadUrl(Global.Email_auth);
                    webEmail.getSettings().setJavaScriptEnabled(true);
                    webEmail.getSettings().setUserAgentString("contactninja");
                    webEmail.setHorizontalScrollBarEnabled(false);
                    webEmail.setWebViewClient(new HelloWebViewClient());
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }

    private class HelloWebViewClient extends WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {

            if(Global.IsNotNull(Activtiy_back)&&Activtiy_back.equals("zoom")){

                String hostURL = url.substring(url.lastIndexOf("?") + 1, url.length());
                String code = url.substring(url.lastIndexOf("=") + 1, url.length());

                String first4char = hostURL.substring(0,5);

                // decode
                if (first4char.equals("code=")) {

                    try {
                        if(Global.isNetworkAvailable(Verification_web.this, MainActivity.mMainLayout)) {
                            /*Zoom Tokens Generation*/
                            Zoom_helpZoomOauth(code);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    webView.loadUrl(url);
                }

            }else {
                String hostURL = url.substring(url.lastIndexOf("/") + 1, url.length());

                String AccessURL = url.substring(url.lastIndexOf("/")- 2, url.length());
                String[] bits = AccessURL.split("/");
                String access = bits[bits.length-2];
                String val2 = "";
                if (access.equals("1")) {
                    try {
                        byte[] tmp2 = Base64.decode(hostURL, Base64.DEFAULT);
                        val2 = new String(tmp2, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }


            /*    // decode
                String substring = hostURL.substring(Math.max(hostURL.length() - 2, 0));
                if (substring.equals("==")) {
                    try {
                        byte[] tmp2 = Base64.decode(hostURL, Base64.DEFAULT);
                        val2 = new String(tmp2, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
*/
                if (Global.emailValidator(val2)) {
                    try {
                        if (Global.isNetworkAvailable(Verification_web.this, mMainLayout)) {
                                GoogleAuth(val2);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    webView.loadUrl(url);
                }
            }


            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }
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






    void Zoom_Api() throws JSONException {

        SignResponseModel signResponseModel= SessionManager.getGetUserdata(Verification_web.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.zoomAuthApp(sessionManager,obj, loadingDialog, token,Global.getVersionname(Verification_web.this),Global.Device, new RetrofitCallback() {
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
                        webEmail.clearCache(true);
                        webEmail.clearHistory();
                        webEmail.loadUrl(zoomExists.getZoom_url());
                        webEmail.getSettings().setJavaScriptEnabled(true);
                        webEmail.getSettings().setUserAgentString("contactninja");
                        webEmail.setHorizontalScrollBarEnabled(false);
                        webEmail.setWebViewClient(new HelloWebViewClient());
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


    void Zoom_helpZoomOauth(String code) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel= SessionManager.getGetUserdata(Verification_web.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("code",code);
        obj.add("data", paramObject);
        retrofitCalls.helpZoomOauth(sessionManager,obj, loadingDialog, token,Global.getVersionname(Verification_web.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ZoomExists>() {
                    }.getType();
                    ZoomExists zoomExists=new Gson().fromJson(headerString, listType);
                    ZoomExists.Zoom zoom=zoomExists.getData();
                    if(zoom.getReturn_status()==200){
                        webEmail.clearHistory();
                        webEmail.clearFormData();
                        webEmail.clearCache(true);
                        if(Global.IsNotNull(ZoomAct)&&ZoomAct.equals("ZoomActivity")){
                            ZoomActivity.tv_email.setText(zoom.getUserMeResultEmail());
                        }
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