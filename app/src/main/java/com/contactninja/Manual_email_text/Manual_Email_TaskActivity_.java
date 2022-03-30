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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
public class Manual_Email_TaskActivity_ extends AppCompatActivity implements View.OnClickListener,ConnectivityReceiver.ConnectivityReceiverListener  {

    TextView tc_time_zone;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back;
    TextView save_button;
    LinearLayout la_date,la_time,linearLayout;
    TextView tv_date,tv_time;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String subject,body,id,email,gid;
    private BroadcastReceiver mNetworkReceiver;
    ConstraintLayout mMainLayout;
    String task_name="",from_ac="",from_ac_id="",temaplet_id="";
    private long mLastClickTime=0;
    String main_date="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_email_task);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        subject=bundle.getString("subject");
        body=bundle.getString("body");
        id=bundle.getString("id");
        email=bundle.getString("email");
        gid=bundle.getString("gid");

        temaplet_id=bundle.getString("tem_id");
        task_name=bundle.getString("task_name");
        from_ac=bundle.getString("from_ac");
        from_ac_id= bundle.getString("from_ac_id");

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        String formateChnage = Global.DateFormateMonth(formattedDate);
        tv_date.setText(formateChnage);
        main_date=formattedDate;;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateandTime = sdf.format(new Date());
        tv_time.setText(currentDateandTime);
        //tv_time.setText(String.valueOf(currentTime.getTime()));

    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Manual_Email_TaskActivity_.this, mMainLayout);
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
       // tc_time_zone.setText("Time Zone("+localTime+"-"+time_zone);

        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        tc_time_zone.setText("Time Zone("+user_data.getUser().getUserTimezone().get(0).getText().toString()+")");

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
                if (tv_date.getText().toString().equals(""))
                {
                    Global.Messageshow(getApplicationContext(),linearLayout,"Add Date ",false);
                }
                else if (tv_time.getText().toString().equals(""))
                {
                    Global.Messageshow(getApplicationContext(),linearLayout,"Add Time",false);

                }
                else {
                    try {

                        EmailAPI(subject, body, Integer.parseInt(id), email);
                    } catch (JSONException e) {
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


    private void EmailAPI(String subject, String text, int id, String email) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        JSONObject obj = new JSONObject();

        JSONObject paramObject = new JSONObject();

        paramObject.put("type", SessionManager.getCampaign_type(getApplicationContext()));
        paramObject.put("team_id", 1);
        paramObject.put("organization_id", 1);
        paramObject.put("user_id", user_id);
        paramObject.put("manage_by", SessionManager.getCampaign_type_name(getApplicationContext()));
        paramObject.put("time", tv_time.getText().toString());
        paramObject.put("date", main_date);
        paramObject.put("assign_to", user_id);


        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 1; i++) {
            JSONObject paramObject1 = new JSONObject();
            paramObject1.put("prospect_id", id);
            paramObject1.put("email", email);
           /* JSONArray contect_array = new JSONArray();
            contect_array.put(email);
            paramObject1.put("email_mobile", contect_array);*/
            jsonArray.put(paramObject1);
            break;
        }
        JSONArray contact_group_ids = new JSONArray();
        contact_group_ids.put("");
        paramObject.put("contact_group_ids", contact_group_ids);
        paramObject.put("prospect_id", jsonArray);
        paramObject.put("record_id","");
        paramObject.put("task_name",task_name);
        if (temaplet_id.equals(""))
        {
            paramObject.put("template_id","");

        }
        else {
            paramObject.put("template_id",temaplet_id);
        }

        paramObject.put("content_header",subject);
        paramObject.put("content_body",body);
        paramObject.put("from_ac",from_ac);
        paramObject.put("from_ac_id",from_ac_id);
        obj.put("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is", new Gson().toJson(gsonObject));


        retrofitCalls.manual_task_store(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager),Global.getVersionname(Manual_Email_TaskActivity_.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {

                        loadingDialog.cancelLoading();
                        Intent intent=new Intent(getApplicationContext(),Email_Tankyou.class);
                        intent.putExtra("s_name","final");
                        startActivity(intent);
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

    private void Email_execute(String subject, String text, int id, String email, String record_id) throws JSONException {
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        JsonObject obj = new JsonObject();

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("content_body", text);
        paramObject.addProperty("content_header", subject);
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("prospect_id", id);
        paramObject.addProperty("record_id", record_id);
        paramObject.addProperty("type", "EMAIL");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_ggmail_id", gid);
        paramObject.addProperty("email_recipients", email);
        obj.add("data", paramObject);

        retrofitCalls.Email_execute(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),Global.getVersionname(Manual_Email_TaskActivity_.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                Log.e("Main Response is",new Gson().toJson(response.body()));

                if (response.body().getHttp_status()==200)
                {
                    loadingDialog.cancelLoading();
                    Intent intent=new Intent(getApplicationContext(),Email_Tankyou.class);
                    intent.putExtra("s_name","final");
                    startActivity(intent);
                    finish();
                }
                else if (response.body().getHttp_status()==406)
                {
                    Global.Messageshow(getApplicationContext(),linearLayout,response.body().getMessage().toString(),false);
                    loadingDialog.cancelLoading();
                }
                else{
                    Global.Messageshow(getApplicationContext(),linearLayout,response.body().getMessage().toString(),false);
                    loadingDialog.cancelLoading();
                    /* finish();*/
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
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
              Log.e("selectedHour", String.valueOf(selectedHour));
              Log.e("selectedminite", String.valueOf(selectedMinute));

                String stime = "";
                if (selectedHour + 1 < 10) {
                    stime = "0" + (selectedHour);
                } else {
                    stime = String.valueOf(selectedHour);
                }


                String sminite = "";
                if (selectedMinute < 10) {
                    sminite = "0" + selectedMinute;
                } else {
                    sminite = String.valueOf(selectedMinute);
                }
                tv_time.setText( stime + ":" + sminite);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void OpenBob() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Manual_Email_TaskActivity_.this,
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


                        String formateChnage = Global.DateFormateMonth(year+"-"+sMonth+"-"+sdate);
                        tv_date.setText(formateChnage);
                        main_date=year + "-" + sMonth + "-" + sdate;

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (1000 * 60 * 60));

        datePickerDialog.show();

    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}