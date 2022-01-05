package com.contactninja.Setting;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.contactninja.AddContect.EmailSend_Activity;
import com.contactninja.Auth.SignupActivity;
import com.contactninja.MainActivity;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.TemplateList;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UserLinkedList;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.DatabaseClient;
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
import java.util.List;

import retrofit2.Response;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    TextView tv_version_name;
    ImageView iv_back;
    LinearLayout layout_logout, layout_resetPassword, layout_template, layout_Current_plan, layout_about, layout_mail_box;
    SessionManager sessionManager;
    RelativeLayout mMainLayout;
    private BroadcastReceiver mNetworkReceiver;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog=new LoadingDialog(SettingActivity.this);
        retrofitCalls = new RetrofitCalls(SettingActivity.this);
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
        layout_mail_box = findViewById(R.id.layout_mail_box);
        tv_version_name = findViewById(R.id.tv_version_name);
        layout_Current_plan = findViewById(R.id.layout_Current_plan);
        layout_about = findViewById(R.id.layout_about);
        layout_template = findViewById(R.id.layout_template);
        layout_resetPassword = findViewById(R.id.layout_resetPassword);
        layout_logout = findViewById(R.id.layout_logout);
        layout_mail_box.setOnClickListener(this);
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

            case R.id.layout_mail_box:


                try {
                    if(Global.isNetworkAvailable(SettingActivity.this, MainActivity.mMainLayout)) {
                        Mail_list();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }
    private void Mail_list() throws JSONException {

        SignResponseModel signResponseModel= SessionManager.getGetUserdata(SettingActivity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Mail_list(sessionManager,obj, loadingDialog, token,Global.getVersionname(SettingActivity.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getStatus() == 200) {
                    /*is a list to email show*/
                    startActivity(new Intent(getApplicationContext(), EmailListActivity.class));
                }else {

                    /*is a email permission link open */
                    //Global.openEmailAuth(SettingActivity.this);
                    startActivity(new Intent(getApplicationContext(),Email_verification.class));
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


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
                delete();
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.logoutUser();
                finish();
            }
        });
        dialog.show();
    }

    public void delete() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .RemoveData();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.e("Delete Task", "Yes");
                super.onPostExecute(aVoid);

            }
        }

        DeleteTask ut = new DeleteTask();
        ut.execute();
    }
}