package com.contactninja.Manual_email_text;

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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import org.json.JSONArray;
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
public class Manual_Text_Sheduled_Activity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    TextView tc_time_zone;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back;
    TextView save_button;
    LinearLayout la_date, la_time, linearLayout;
    TextView tv_date, tv_time;
    String id, text, p_number;
    ConstraintLayout mMainLayout;
    String task_name = "", from_ac = "", from_ac_id = "", temaplet_id = "";
    String main_date = "";
    int m_hour = 0;
    int m_minute = 0;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private BroadcastReceiver mNetworkReceiver;
    private long mLastClickTime = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_task_sheduled);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("id");
        text = bundle.getString("text");
        p_number = bundle.getString("number");
        
        
        temaplet_id = bundle.getString("tem_id");
        task_name = bundle.getString("task_name");
        from_ac = bundle.getString("from_ac");
        from_ac_id = bundle.getString("from_ac_id");
        
        String formateChnage = Global_Time.DateFormateMonth(Global_Time.getCurrentDate());
        tv_date.setText(formateChnage);
        main_date = Global_Time.getCurrentDate();
        
        tv_time.setText(Global_Time.getCurrentTime());
    }
    
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Manual_Text_Sheduled_Activity.this, mMainLayout);
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
    
    private void IntentUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        linearLayout = findViewById(R.id.linearLayout);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText(getResources().getString(R.string.Done));
        tc_time_zone = findViewById(R.id.tc_time_zone);
        la_date = findViewById(R.id.la_date);
        tv_date = findViewById(R.id.tv_date);
        la_time = findViewById(R.id.la_time);
        tv_time = findViewById(R.id.tv_time);
        la_date.setOnClickListener(this);
        la_time.setOnClickListener(this);
        Calendar cal = Calendar.getInstance();
        TimeZone tz1 = cal.getTimeZone();
        Calendar calendar = Calendar.getInstance(tz1,
                Locale.getDefault());
        
        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        String time_zone = TimeZone.getDefault().getID();
        //  tc_time_zone.setText("Time Zone("+localTime+"-"+time_zone);
        
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        if (Global.IsNotNull(user_data.getUser().getUserTimezone().get(0))) {
            tc_time_zone.setText("Time Zone(" + user_data.getUser().getUserTimezone().get(0).getText().toString() + ")");
        }
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
                if (tv_date.getText().toString().equals("")) {
                    Global.Messageshow(getApplicationContext(), linearLayout, getResources().getString(R.string.add_date), false);
                } else if (tv_time.getText().toString().equals("")) {
                    Global.Messageshow(getApplicationContext(), linearLayout, getResources().getString(R.string.add_time), false);
                    
                } else {
                    try {
                        if (Global_Time.checkTime_isvalid(getApplicationContext(),tv_time.getText().toString().trim(),tv_date.getText().toString().trim())) {
                            try {
                                SMSAPI(text, Integer.parseInt(id), p_number);
                                
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
    
    public void onTimer() {
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
        
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(Manual_Text_Sheduled_Activity.this,
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
                        
                        
                        String formateChnage = Global_Time.DateFormateMonth(year + "-" + sMonth + "-" + sdate);
                        tv_date.setText(formateChnage);
                        main_date = year + "-" + sMonth + "-" + sdate;
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (1000 * 60 * 60));
        
        datePickerDialog.show();
        
    }
    
    private void SMSAPI(String text, int id, String email) throws JSONException {
        
        Log.e("Phone Number", email);
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        JSONObject obj = new JSONObject();
        
        JSONObject paramObject = new JSONObject();
        
        paramObject.put("type", SessionManager.getCampaign_type(getApplicationContext()));
        paramObject.put("team_id", "1");
        paramObject.put("organization_id", "1");
        paramObject.put("user_id", user_id);
        paramObject.put("manage_by", SessionManager.getCampaign_type_name(getApplicationContext()));
        try {
            paramObject.put("time", Global_Time.time_12_to_24(tv_time.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramObject.put("date", main_date);
        paramObject.put("assign_to", user_id);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 1; i++) {
            JSONObject paramObject1 = new JSONObject();
            paramObject1.put("prospect_id", id);
            paramObject1.put("mobile", email);
            jsonArray.put(paramObject1);
            break;
        }
        JSONArray contact_group_ids = new JSONArray();
        contact_group_ids.put("");
        paramObject.put("contact_group_ids", contact_group_ids);
        paramObject.put("prospect_id", jsonArray);
        paramObject.put("task_name", task_name);
        if (temaplet_id.equals("")) {
            paramObject.put("template_id", "");
        } else {
            paramObject.put("template_id", temaplet_id);
        }
        paramObject.put("content_body", text);
        paramObject.put("from_ac", from_ac);
        paramObject.put("from_ac_id", from_ac_id);
        obj.put("data", paramObject);
        
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        retrofitCalls.manual_task_store(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {
                    loadingDialog.cancelLoading();
                    String jsonRawData = new Gson().toJson(response.body());
                    
                    
                    Intent intent = new Intent(getApplicationContext(), Tankyou_after_scheduled_task_Activity.class);
                    intent.putExtra("s_name", "final");
                    startActivity(intent);
                    finish();
                    
                } else if (response.body().getHttp_status() == 403) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage().toString(), false);
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
}