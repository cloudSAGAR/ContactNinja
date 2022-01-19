package com.contactninja.Setting;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.AddContect.EmailSend_Activity;
import com.contactninja.MainActivity;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Email_verification extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    WebView webEmail;
    LinearLayout mMainLayout;

    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;

    private BroadcastReceiver mNetworkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);


        mMainLayout = findViewById(R.id.mMainLayout);
        webEmail = findViewById(R.id.webEmail);
        webEmail.clearCache(true);
        webEmail.clearHistory();
        webEmail.loadUrl(Global.Email_auth);
        webEmail.getSettings().setJavaScriptEnabled(true);
        webEmail.getSettings().setUserAgentString("contactninja");
        webEmail.setHorizontalScrollBarEnabled(false);
        webEmail.setWebViewClient(new HelloWebViewClient());

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Email_verification.this, mMainLayout);
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
                Global.getVersionname(Email_verification.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        if (response.body().getStatus() == 200) {
                            loadingDialog.cancelLoading();
                            webEmail.clearHistory();
                            webEmail.clearFormData();
                            webEmail.clearCache(true);

                            finish();

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

    private class HelloWebViewClient extends WebViewClient {


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
            if (substring.equals("==")) {
                try {
                    byte[] tmp2 = Base64.decode(hostURL, Base64.DEFAULT);
                    val2 = new String(tmp2, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (Global.emailValidator(val2)) {

               try {
                   if (Global.isNetworkAvailable(Email_verification.this, mMainLayout)) {
                       if(access.equals("1")){
                           GoogleAuth(val2);
                       }else {
                           showAlertDialogButtonClicked(val2);
                       }
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }

            } else {
                webView.loadUrl(url);
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
}