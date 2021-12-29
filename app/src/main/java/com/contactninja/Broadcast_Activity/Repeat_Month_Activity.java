package com.contactninja.Broadcast_Activity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

import com.contactninja.R;
import com.contactninja.Setting.WebActivity;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Repeat_Month_Activity extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{
    ImageView iv_back, iv_Setting;
    TextView save_button, date_spinner, tv_day;
    String type = "", week_txt = "";
    Spinner day_spinner,day_spinner1,month_spinner1,secon_month_spinner1;

    List<String> select_string = new ArrayList<>();
    String select_day = "", end_date="",day_txt="",second_day="";
    private int mYear, mMonth, mDay, mHour, mMinute;
    RadioButton radio_day,radio_every;
    String r_day="",r_monthe="";
    private BroadcastReceiver mNetworkReceiver;
    ConstraintLayout mMainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_month);
        mNetworkReceiver = new ConnectivityReceiver();

        IntentUI();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        type = bundle.getString("type", "");

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.broadcast_week));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_spinner.setAdapter(adapter);
        day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                week_txt = String.valueOf(day_spinner.getSelectedItem());
                Log.e("Text is ", week_txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        ArrayAdapter<CharSequence> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.broadcast_day));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_spinner1.setAdapter(adapter1);
        day_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                day_txt = String.valueOf(day_spinner1.getSelectedItem());
                Log.e("Text is ", day_txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






        ArrayAdapter<CharSequence> m_adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.broadcast_week_txt));
        m_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_spinner1.setAdapter(m_adapter1);
        month_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                day_txt = String.valueOf(month_spinner1.getSelectedItem());
                Log.e("Text is ", day_txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        date_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Repeat_Month_Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date_spinner.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });



        ArrayAdapter<CharSequence> secon_month_adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.broadcast_day_txt));
        secon_month_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secon_month_spinner1.setAdapter(secon_month_adapter1);
        secon_month_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                second_day = String.valueOf(secon_month_spinner1.getSelectedItem());
                Log.e("Text is ", second_day);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

      /*  tv_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog_For_Home();
            }
        });*/


        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radio_day.isChecked())
                {

                    r_day=radio_day.getText().toString();
                }
                if (radio_every.isChecked())
                {
                    r_monthe=radio_every.getText().toString();

                }
                end_date = date_spinner.getText().toString();
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                String type = bundle.getString("type", "");
                Intent Time_Selcet_Activity = new Intent(getApplicationContext(), Broadcast_time_select.class);
                Time_Selcet_Activity.putExtra("start_date", "");
                Time_Selcet_Activity.putExtra("end_date", end_date);
                Time_Selcet_Activity.putExtra("type", type);
                Time_Selcet_Activity.putExtra("repeat", week_txt);
                Time_Selcet_Activity.putExtra("num_day", second_day);
                Time_Selcet_Activity.putExtra("m_day",r_day);
                Time_Selcet_Activity.putExtra("m_month",r_monthe);
                Time_Selcet_Activity.putExtra("m_first",day_txt);
                startActivity(Time_Selcet_Activity);
            }
        });

    }

    private void IntentUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText("Next");
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.GONE);
        day_spinner = findViewById(R.id.day_spinner);
        date_spinner = findViewById(R.id.date_spinner);
        tv_day = findViewById(R.id.tv_day);
        day_spinner1=findViewById(R.id.day_spinner1);
        month_spinner1=findViewById(R.id.month_spinner1);
        secon_month_spinner1=findViewById(R.id.secon_month_spinner1);
        radio_day=findViewById(R.id.radio_day);
        radio_every=findViewById(R.id.radio_every);
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Repeat_Month_Activity.this, mMainLayout);
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


