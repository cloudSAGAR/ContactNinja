package com.contactninja.Manual_email_text;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.contactninja.Manual_email_text.List_And_show.Item_List_Email_Detail_activty;
import com.contactninja.Manual_email_text.List_And_show.Item_List_Text_Detail_Activty;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.Global_Time;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Response;
@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Manual_Shooz_Time_Date_Activity extends AppCompatActivity implements View.OnClickListener,ConnectivityReceiver.ConnectivityReceiverListener{
    private BroadcastReceiver mNetworkReceiver;
    LinearLayout mMainLayout;


    TextView tc_time_zone;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back;
    TextView save_button;
    LinearLayout la_date,la_time,linearLayout;
    TextView tv_date,tv_time;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String id,text,p_number,Type;
    private long mLastClickTime=0;
    int prospect_id=0,seq_task_id=0,record_id=0;
    String main_date="";
    int m_hour = 0;
    int m_minute = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_shooz_time_date);
        mNetworkReceiver = new ConnectivityReceiver();
        mMainLayout = findViewById(R.id.mMainLayout);


        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();
        try {
            Intent intent=getIntent();
            Bundle bundle=intent.getExtras();
            id=bundle.getString("id");
            Type=bundle.getString("Type");
            prospect_id=bundle.getInt("prospect_id");
            seq_task_id=bundle.getInt("seq_task_id");
            record_id=bundle.getInt("record_id");
        }catch (Exception e){
            e.printStackTrace();
        }
        tv_date.setText(Global_Time.getCurrentDate());

        String formateChnage = Global_Time.DateFormateMonth(Global_Time.getCurrentDate());
        tv_date.setText(formateChnage);
        main_date=Global_Time.getCurrentDate();

        Calendar mcurrentTime = Calendar.getInstance();
        mcurrentTime.add(Calendar.MINUTE, 2);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aaa");
        String currentDateandTime = sdf.format(mcurrentTime.getTime());
        tv_time.setText(currentDateandTime);
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Manual_Shooz_Time_Date_Activity.this, mMainLayout);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = null;
        if(Type.equals("EMAIL")){
            intent = new Intent(getApplicationContext(), Item_List_Email_Detail_activty.class);
        }else {
            intent = new Intent(getApplicationContext(), Item_List_Text_Detail_Activty.class);
        }
        intent.putExtra("record_id",record_id);
        startActivity(intent);
        finish();
    }

    private void IntentUI() {
        mMainLayout=findViewById(R.id.mMainLayout);
        linearLayout=findViewById(R.id.linearLayout);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Done");
        tc_time_zone=findViewById(R.id.tc_time_zone);
        la_date=findViewById(R.id.la_date);
        tv_date=findViewById(R.id.tv_date);
        la_time=findViewById(R.id.la_time);
        tv_time=findViewById(R.id.tv_time);
        la_date.setOnClickListener(this);
        la_time.setOnClickListener(this);
        Calendar cal = Calendar.getInstance();
        TimeZone tz1 = cal.getTimeZone();
        Calendar calendar = Calendar.getInstance(tz1,
                Locale.getDefault());

        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        String time_zone= TimeZone.getDefault().getID();
        //  tc_time_zone.setText("Time Zone("+localTime+"-"+time_zone);

        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        tc_time_zone.setText("Time Zone("+user_data.getUser().getUserTimezone().get(0).getText().toString()+")");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
              onBackPressed();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (tv_date.getText().toString().equals(""))
                {

                    Global.Messageshow(getApplicationContext(),linearLayout,getResources().getString(R.string.add_date),false);
                }
                else if (tv_time.getText().toString().equals(""))
                {
                    Global.Messageshow(getApplicationContext(),linearLayout,getResources().getString(R.string.add_time),false);

                }
                else {
                    try {
                        if (Global_Time.checkTime_isvalid(getApplicationContext(),tv_time.getText().toString().trim(),tv_date.getText().toString().trim())) {
                            
                            try {
                                SMSAPI();
        
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.la_date:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                OpenBob();
                break;
            case R.id.la_time:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                onTimer();
                break;


        }
    }
    private void SMSAPI() throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("team_id", "1");
        paramObject.put("organization_id", "1");
        paramObject.put("user_id", user_id);
        paramObject.put("prospect_id", prospect_id);
        paramObject.put("seq_task_id", seq_task_id);
        paramObject.put("status", "SNOOZE");
        try {
            paramObject.put("time", Global_Time.time_12_to_24(tv_time.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramObject.put("date", main_date);
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is", new Gson().toJson(gsonObject));


        retrofitCalls.manual_task_store_snooze(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {
                    loadingDialog.cancelLoading();
                    finish();
                }
                else if (response.body().getHttp_status()==403)
                {
                    Global.Messageshow(getApplicationContext(),mMainLayout,response.body().getMessage().toString(),false);
                }
                else {
                    loadingDialog.cancelLoading();
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }
    public void onTimer()
    {
        Calendar mcurrentTime = Calendar.getInstance();
        m_hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        m_minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    
                    m_hour = selectedHour;
                    m_minute = selectedMinute;
                    String timeSet = "";
                    if (m_hour > 12) {
                        m_hour -= 12;
                        timeSet = getResources().getString(R.string.PM);
                    } else if (m_hour == 0) {
                        m_hour += 12;
                        timeSet = getResources().getString(R.string.AM);
                    } else if (m_hour == 12) {
                        timeSet = getResources().getString(R.string.PM);
                    } else {
                        timeSet = getResources().getString(R.string.AM);
                    }
                
                    String min = "";
                    if (m_minute < 10)
                        min = "0" + m_minute;
                    else
                        min = String.valueOf(m_minute);
                
                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(m_hour).append(':')
                                           .append(min).append(" ").append(timeSet).toString();
                    tv_time.setText(aTime);
                    
            }
        }, m_hour, m_minute, false);//Yes 24 hour time
        mTimePicker.setTitle(getResources().getString(R.string.Select_Time));
        mTimePicker.show();
    }

    public void OpenBob() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Manual_Shooz_Time_Date_Activity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        String sMonth = "";
                        if (monthOfYear + 1 < 10) {
                            sMonth = "0" + (monthOfYear + 1);
                        } else {
                            sMonth = String.valueOf(monthOfYear + 1);
                        }


                        String sdate = "";
                        if (dayOfMonth < 10) {
                            sdate = "0" + dayOfMonth;
                        } else {
                            sdate = String.valueOf(dayOfMonth);
                        }

                        String formateChnage = Global_Time.DateFormateMonth(year+"-"+sMonth+"-"+sdate);
                        tv_date.setText(formateChnage);
                        main_date=year + "-" + sMonth + "-" + sdate;
                     //   tv_date.setText(year + "-" + sMonth + "-" + sdate);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (1000 * 60 * 60));

        datePickerDialog.show();

    }
}