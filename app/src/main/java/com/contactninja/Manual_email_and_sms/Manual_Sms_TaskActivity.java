package com.contactninja.Manual_email_and_sms;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
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

public class Manual_Sms_TaskActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tc_time_zone;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back;
    TextView save_button;
    LinearLayout la_date,la_time,linearLayout;
    TextView tv_date,tv_time;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String id,text,p_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_sms_task);
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        id=bundle.getString("id");
        text=bundle.getString("text");
        p_number=bundle.getString("number");


    }

    private void IntentUI() {
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
        tc_time_zone.setText("Time Zone("+localTime+"-"+time_zone);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:
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
                        SMSAPI(text, Integer.parseInt(id), p_number);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.la_date:
                OpenBob();
                break;
            case R.id.la_time:
                onTimer();
                break;


        }
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
                tv_time.setText( selectedHour + ":" + selectedMinute);
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(Manual_Sms_TaskActivity.this,
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


                        tv_date.setText(year + "-" + sMonth + "-" + sdate);

                    }
                }, mYear, mMonth, mDay);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60));

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

        paramObject.put("type", "SMS");
        paramObject.put("team_id", "1");
        paramObject.put("organization_id", "1");
        paramObject.put("user_id", user_id);
        paramObject.put("manage_by", "MANUAL");
        paramObject.put("time", tv_time.getText().toString());
        paramObject.put("date", tv_date.getText().toString());
        paramObject.put("assign_to", user_id);
        paramObject.put("task_description", text);

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 1; i++) {
            JSONObject paramObject1 = new JSONObject();
            paramObject1.put("prospect_id", id);
            paramObject1.put("mobile", email);
           /* JSONArray contect_array = new JSONArray();
            contect_array.put(email);
            paramObject1.put("mobile", contect_array);*/
            jsonArray.put(paramObject1);
            break;
        }
        JSONArray contact_group_ids = new JSONArray();
        contact_group_ids.put("");
        paramObject.put("contact_group_ids", contact_group_ids);
        paramObject.put("prospect_id", jsonArray);

        obj.put("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is", new Gson().toJson(gsonObject));


        retrofitCalls.manual_task_store(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager),Global.getVersionname(this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {
                    loadingDialog.cancelLoading();
                    String jsonRawData = new Gson().toJson(response.body());

                    try {
                        JSONObject jsonObject = new JSONObject(jsonRawData);
                        JSONObject jsonDailyObject = jsonObject.getJSONObject("data");
                        JSONObject jsonDailyObject1 = jsonDailyObject.getJSONObject("0");
                        String _newid = jsonDailyObject1.getString("id");
                        Log.e("_newid", _newid);
                        SMS_execute(text, id, email, _newid);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

    private void SMS_execute(String text, int id, String email, String record_id) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        JsonObject obj = new JsonObject();

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("content_body", text);
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("prospect_id", id);
        paramObject.addProperty("record_id", record_id);
        paramObject.addProperty("type", "SMS");
        paramObject.addProperty("team_id", "1");
        obj.add("data", paramObject);

        retrofitCalls.Email_execute(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this),Global.Device,new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                Intent intent=new Intent(getApplicationContext(),Email_Tankyou.class);
                intent.putExtra("s_name","final");
                startActivity(intent);
                finish();
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}