package com.contactninja.Main_Broadcast.List_And_show;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contactninja.Campaign.List_itm.Campaign_Final_Start;
import com.contactninja.Campaign.List_itm.Campaign_List_Activity;
import com.contactninja.Campaign.List_itm.Campaign_Viewcontect;
import com.contactninja.MainActivity;
import com.contactninja.Manual_email_text.Text_And_Email_Auto_Manual;
import com.contactninja.Model.BroadcastActivityListModel;
import com.contactninja.Model.CampaignTask_overview;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Broadcaste_Activity extends AppCompatActivity   implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener{
    ImageView iv_back, iv_Setting,image_icon,image_step,iv_toolbar_manu_vertical;
    TextView save_button;
    LinearLayout main_layout;
    SessionManager sessionManager;
    int sequence_id,seq_task_id;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    TextView tv_email,tv_sms,tv_contect,tv_pending,tv_contec_reach,tv_camp_name;
    private long mLastClickTime=0;
    private BroadcastReceiver mNetworkReceiver;
    BroadcastActivityListModel.Broadcast broadcasteda;
    TextView tv_contect1,tv_exposure,tv_status,tv_title,
            tv_date,tv_repete_type,ev_subject,tv_detail;
    LinearLayout layout_email_subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcaste);
        mNetworkReceiver = new ConnectivityReceiver();
        broadcasteda=SessionManager.getBroadcate_List_Detail(this);
        Log.e("Broadcaste Data",new Gson().toJson(broadcasteda));
        IntentUI();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        if (broadcasteda.getType().equals("SMS"))
        {
            image_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_sms_mini));
            tv_status.setText(broadcasteda.getBroadcastName());
            image_step.setImageDrawable(getResources().getDrawable(R.drawable.ic_sms_mini));
            layout_email_subject.setVisibility(View.GONE);
        }
        else {
            layout_email_subject.setVisibility(View.VISIBLE);
            image_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_email));
            image_step.setImageDrawable(getResources().getDrawable(R.drawable.ic_email));
            tv_status.setText(broadcasteda.getBroadcastName());
        }
        if (broadcasteda.getStatus().equals("A"))
        {
            tv_title.setText(broadcasteda.getType());
            tv_title.setTextColor(getResources().getColor(R.color.text_green));
        }
        else if (broadcasteda.getStatus().equals("I"))
        {
            tv_title.setText(broadcasteda.getType());
            tv_title.setTextColor(getResources().getColor(R.color.red));
        }
        else if (broadcasteda.getStatus().equals("P"))
        {
            tv_title.setText(broadcasteda.getType());
            tv_title.setTextColor(getResources().getColor(R.color.tv_push_color));
        }

        tv_date.setText(broadcasteda.getStartDate()+" @ "+broadcasteda.getStartTime());

        if (broadcasteda.getRecurringType().equals("D"))
        {
            tv_repete_type.setText("Daily");
        }
        else if (broadcasteda.getRecurringType().equals("W"))
        {
            tv_repete_type.setText("Weekly");
        }
        else if (broadcasteda.getRecurringType().equals("M"))
        {
            tv_repete_type.setText("Monthly");
        }
        ev_subject.setText(broadcasteda.getContentHeader());
        tv_detail.setText(broadcasteda.getContentBody());

    }
    private void IntentUI() {
        iv_toolbar_manu_vertical=findViewById(R.id.iv_toolbar_manu_vertical);
        iv_toolbar_manu_vertical.setOnClickListener(this);
        iv_toolbar_manu_vertical.setVisibility(View.VISIBLE);

        layout_email_subject=findViewById(R.id.layout_email_subject);
        tv_detail=findViewById(R.id.tv_detail);
        ev_subject=findViewById(R.id.ev_subject);
        tv_repete_type=findViewById(R.id.tv_repete_type);
        tv_date=findViewById(R.id.tv_date);
        tv_title=findViewById(R.id.tv_title);
        image_step=findViewById(R.id.image_step);
        tv_status=findViewById(R.id.tv_status);
        image_icon=findViewById(R.id.image_icon);
        tv_exposure=findViewById(R.id.tv_exposure);
        tv_contect1=findViewById(R.id.tv_contect1);
        main_layout = findViewById(R.id.main_layout);
        tv_camp_name = findViewById(R.id.tv_camp_name);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.GONE);
        iv_back.setOnClickListener(this);
        save_button.setOnClickListener(this);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText(getString(R.string.view_contect));
        save_button.setTextColor(getResources().getColor(R.color.purple_200));
        tv_email=findViewById(R.id.tv_email);
        tv_sms=findViewById(R.id.tv_sms);
        tv_contect=findViewById(R.id.tv_contect);
        tv_pending=findViewById(R.id.tv_pending);
        tv_contec_reach=findViewById(R.id.tv_contec_reach);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                break;
            case R.id.iv_toolbar_manu_vertical:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                broadcast_manu();
                break;
        }
    }

    private void broadcast_manu() {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.brodcaste_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Broadcaste_Activity.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        TextView selected_campaign = bottomSheetDialog.findViewById(R.id.selected_campaign);
        TextView selected_broadcast = bottomSheetDialog.findViewById(R.id.selected_broadcast);
        TextView selected_task = bottomSheetDialog.findViewById(R.id.selected_task);
        selected_broadcast.setText("Pause Broadcast ");
        selected_task.setText("Edit Broadcast");
        selected_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                bottomSheetDialog.dismiss();
            }
        });

        selected_broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                bottomSheetDialog.dismiss();
            }
        });
        selected_campaign.setVisibility(View.GONE);
        bottomSheetDialog.show();

    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Broadcaste_Activity.this, main_layout);
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
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}