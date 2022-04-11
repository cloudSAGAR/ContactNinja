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

import com.contactninja.MainActivity;
import com.contactninja.Model.Timezon;
import com.contactninja.Model.UserData.SignResponseModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class PlanType_Screen extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener  {

    LinearLayout layout_free_card, layout_bz_card, layout_master, layout_contect;
    int flag = 0;
    private BroadcastReceiver mNetworkReceiver;
    ConstraintLayout mMainLayout;
    SignResponseModel user_data;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    String token_api = "", organization_id = "", team_id = "";
    Integer user_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_type_screen);
        mNetworkReceiver = new ConnectivityReceiver();
        sessionManager = new SessionManager(this);
        loadingDialog = new LoadingDialog(this);
        retrofitCalls = new RetrofitCalls(getApplicationContext());
        IntentUI();

        user_data = SessionManager.getGetUserdata(getApplicationContext());
        user_id = user_data.getUser().getId();
        organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        token_api = Global.getToken(sessionManager);



    }
    //TimeZone Call
    private void Timezone(String id, int i, String i1) throws JSONException {
        loadingDialog.showLoadingDialog();
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);
        retrofitCalls.Timezone(sessionManager, obj, loadingDialog, token_api, Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {


                Gson gson = new Gson();
                String headerString = gson.toJson(response.body().getData());
                Type listType = new TypeToken<ArrayList<Timezon.TimezonData>>() {
                }.getType();
                List<Timezon.TimezonData> timezonDataList = new Gson().fromJson(headerString, listType);
                for (int i = 0; i < timezonDataList.size(); i++) {
                    if (id.equals(timezonDataList.get(i).getTzname())) {
                        Working_hour(timezonDataList.get(i).getValue(),i,i1);
                        break;
                    }
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
            }
        });
    }
    //Working Hourse Call
    private void Working_hour(Integer integer, int i, String value) {
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("timezone_id", integer);
        paramObject.addProperty("is_default", "1");
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);
        String version_name = Global.getVersionname(this);
        retrofitCalls.Working_hour(sessionManager, obj, loadingDialog, token_api, version_name, Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                Intent intent = new Intent(getApplicationContext(), Plan_Detail_Screen.class);
                intent.putExtra("flag", i);
                intent.putExtra("plan_product_id", value);
                startActivity(intent);
              //  finish();
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });

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

        TimeZone tz = TimeZone.getDefault();
        switch (v.getId()) {
            case R.id.layout_free_card:
                /**
                 * This is free card */


                if (!Global.IsNotNull(user_data.getUser().getWorkingHoursList()) || user_data.getUser().getWorkingHoursList().size() == 0) {
                    try {
                        if (Global.isNetworkAvailable(this, MainActivity.mMainLayout)) {
                            Timezone(tz.getID(),1,"0");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Intent intent = new Intent(getApplicationContext(), Plan_Detail_Screen.class);
                    intent.putExtra("flag", 1);
                    intent.putExtra("plan_product_id", "0");
                    startActivity(intent);
                   // finish();
                }

                break;
            case R.id.layout_bz_card:
                /**
                 * This is 9.95 card */

                if (!Global.IsNotNull(user_data.getUser().getWorkingHoursList()) || user_data.getUser().getWorkingHoursList().size() == 0) {
                    try {
                        if (Global.isNetworkAvailable(this, MainActivity.mMainLayout)) {
                            Timezone(tz.getID(),2, getResources().getString(R.string.plan_9));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Intent intent = new Intent(getApplicationContext(), Plan_Detail_Screen.class);
                    intent.putExtra("flag", 2);
                    intent.putExtra("plan_product_id",  getResources().getString(R.string.plan_9));
                    startActivity(intent);
                  //  finish();
                }

                break;
            case R.id.layout_master:
                /**
                 * This is 39.95 card */
                if (!Global.IsNotNull(user_data.getUser().getWorkingHoursList()) || user_data.getUser().getWorkingHoursList().size() == 0) {
                    try {
                        if (Global.isNetworkAvailable(this, MainActivity.mMainLayout)) {
                            Timezone(tz.getID(),3, getResources().getString(R.string.plan_39));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Intent intent = new Intent(getApplicationContext(), Plan_Detail_Screen.class);
                    intent.putExtra("flag", 3);
                    intent.putExtra("plan_product_id",  getResources().getString(R.string.plan_39));
                    startActivity(intent);
                //    finish();
                }

                break;
            case R.id.layout_contect:
                /**
                 * This is 69.95 card */
                if (!Global.IsNotNull(user_data.getUser().getWorkingHoursList()) || user_data.getUser().getWorkingHoursList().size() == 0) {
                    try {
                        if (Global.isNetworkAvailable(this, MainActivity.mMainLayout)) {
                            Timezone(tz.getID(),4, getResources().getString(R.string.plan_69));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Intent intent = new Intent(getApplicationContext(), Plan_Detail_Screen.class);
                    intent.putExtra("flag", 4);
                    intent.putExtra("plan_product_id",  getResources().getString(R.string.plan_69));
                    startActivity(intent);
                   // finish();
                }

                break;
        }


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(PlanType_Screen.this, mMainLayout);
    }

    @SuppressLint("ObsoleteSdkInt")
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