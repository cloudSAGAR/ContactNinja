package com.contactninja.Broadcast.Broadcast_Schedule;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;

public class Broadcast_to_repeat extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    ImageView iv_back, iv_Setting;
    TextView save_button;
    Spinner day_spinner,time_spinner;
    String day_txt="",time_txt="";
    private BroadcastReceiver mNetworkReceiver;
    ConstraintLayout mMainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_to_repeat);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();

        setSpinner_data();
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (day_txt.equals(""))
                {

                }
               else if (day_txt.equals("Daily"))
                {
                    Intent intent=new Intent(getApplicationContext(),Broadcast_date_select.class);
                    intent.putExtra("type",day_txt);
                    startActivity(intent);
                   // finish();
                }
                else if (day_txt.equals("Weekly"))
                {
                    Intent intent=new Intent(getApplicationContext(),Repeat_weekly_Activity.class);
                    intent.putExtra("type",day_txt);
                    startActivity(intent);

                }
                else if (day_txt.equals("Monthly"))
                {
                    Intent intent=new Intent(getApplicationContext(),Repeat_Month_Activity.class);
                    intent.putExtra("type",day_txt);
                    startActivity(intent);

                }
                else if (day_txt.equals("Does Not Repeat"))
                {
                    Intent Time_Selcet_Activity=new Intent(getApplicationContext(),Broadcast_time_select.class);
                    Time_Selcet_Activity.putExtra("start_date","");
                    Time_Selcet_Activity.putExtra("end_date","");
                    Time_Selcet_Activity.putExtra("type","");
                    Time_Selcet_Activity.putExtra("repeat","");
                    Time_Selcet_Activity.putExtra("num_day","");
                    Time_Selcet_Activity.putExtra("m_day","");
                    Time_Selcet_Activity.putExtra("m_month","");
                    Time_Selcet_Activity.putExtra("m_first","");
                    startActivity(Time_Selcet_Activity);
                }
                else {

                }

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setSpinner_data() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.broadcast_repeat_list));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_spinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> TimeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.broadcste_time_list));
        TimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_spinner.setAdapter(TimeAdapter);

        day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                day_txt=String.valueOf(day_spinner.getSelectedItem());
                   Log.e("Text is ",day_txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time_txt=String.valueOf(time_spinner.getSelectedItem());
                Log.e("Text is ",time_txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void IntentUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button=findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText("Next");
        iv_Setting =findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.GONE);
        day_spinner=findViewById(R.id.day_spinner);
        time_spinner=findViewById(R.id.time_spinner);

    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Broadcast_to_repeat.this, mMainLayout);
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