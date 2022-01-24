package com.contactninja.Broadcast.Broadcast_Schedule;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Broadcast_time_select extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    ImageView iv_back, iv_Setting;
    TextView save_button,selected_date;
    String time="";
    TimePicker timePicker;
    ConstraintLayout mMainLayout;
    String start_date="",end_date="",type="",repeat="",num_day="",r_day="",r_monthe="",day_txt="";
    private BroadcastReceiver mNetworkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_time_select);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        start_date=bundle.getString("start_date","");
        end_date=bundle.getString("end_date","");
        type=bundle.getString("type","");
        repeat=bundle.getString("repeat","");
        num_day=bundle.getString("num_day","");
        r_day=bundle.getString("m_day","");
        r_monthe=bundle.getString("m_month","");
        day_txt=bundle.getString("m_first","");



        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if (time.equals(""))
             {
                 Global.Messageshow(getApplicationContext(),mMainLayout,"Add Time",false);
             }
             else {

                 Intent Time_Selcet_Activity=new Intent(getApplicationContext(),Broadcast_Final_Activity.class);
                 Time_Selcet_Activity.putExtra("start_date",start_date);
                 Time_Selcet_Activity.putExtra("end_date",end_date);
                 Time_Selcet_Activity.putExtra("time",time);
                 Time_Selcet_Activity.putExtra("type",type);
                 Time_Selcet_Activity.putExtra("repeat",repeat);
                 Time_Selcet_Activity.putExtra("num_day",num_day);
                 Time_Selcet_Activity.putExtra("m_day",r_day);
                 Time_Selcet_Activity.putExtra("m_month",r_monthe);
                 Time_Selcet_Activity.putExtra("m_first",day_txt);
                 //startActivity(new Intent(getApplicationContext(), Broadcst_Activty.class));
                 startActivity(Time_Selcet_Activity);
             }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        timePicker = (TimePicker) findViewById(R.id.simpleTimePicker);
        timePicker.setIs24HourView(false); // used to display AM/PM mode
        // perform set on time changed listener event
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay>12) {
                    time=hourOfDay + " : " + minute+" PM";
                } else if(hourOfDay==12) {
                    time=hourOfDay + " : " + minute+" PM";
                } else if(hourOfDay<12) {
                    if(hourOfDay!=0) {
                        time=hourOfDay + " : " + minute+" AM";
                    } else {
                        time=hourOfDay + " : " + minute+" AM";
                    }
                }

            }
        });
    }

    private void IntentUI() {
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button=findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText("Next");
        iv_Setting =findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.GONE);
        selected_date=findViewById(R.id.selected_date);
        timePicker=findViewById(R.id.simpleTimePicker);
        mMainLayout=findViewById(R.id.mMainLayout);

    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Broadcast_time_select.this, mMainLayout);
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