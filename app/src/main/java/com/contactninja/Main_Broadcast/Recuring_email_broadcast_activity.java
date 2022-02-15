package com.contactninja.Main_Broadcast;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.contactninja.Model.Broadcate_save_data;
import com.contactninja.Model.Broadcste_Coman_Model;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
public class Recuring_email_broadcast_activity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    TextView tc_time_zone, tv_day,tv_daylist;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;

    ImageView iv_back, iv_time, iv_date, iv_down_arrow;
    TextView save_button,tv_day_txt,tv_occurs_weekly;
    LinearLayout la_date, la_time, linearLayout, layout_rec,
            layout_day,layout_daily_selction,
            layout_occurs_weekly,layout_month,layout_month_selction
            ,layout_selcond,layout_day_selction;
    TextView tv_date, tv_time, tv_titele, tv_recurrence,tv_month,tv_day_selction;
    ConstraintLayout mMainLayout;
    BottomSheetDialog bottomSheetDialog_templateList1,
            bottomSheetDialog_day,bottomSheetDialog_day_text
            ,bottomSheetDialog_monthday,bottomSheetDialog_second,
            bottomSheetDialog_day_second;
    TextView tv_info,tv_second;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private BroadcastReceiver mNetworkReceiver;
    private long mLastClickTime = 0;
    View view_day;
    ImageView iv_unselected,iv_selected,iv_every_selcted,iv_every_unselcted;
    Broadcate_save_data broadcate_save_data=new Broadcate_save_data();
    String occurs_monthly="Day",day_list_id="1",second_id="1",day_section_id="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuring_email_broadcast);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();
        if (SessionManager.getCampaign_type(getApplicationContext()).equals("SMS")) {
            tv_titele.setText(getResources().getString(R.string.broadcast_text));
        } else {
            tv_titele.setText(getResources().getString(R.string.broadcast_email));
        }
        broadcate_save_data=SessionManager.getBroadcate_save_data(getApplicationContext());
        Log.e("Save Data is",new Gson().toJson(broadcate_save_data));

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        tv_date.setText(formattedDate);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateandTime = sdf.format(new Date());
        tv_time.setText(currentDateandTime);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Recuring_email_broadcast_activity.this, mMainLayout);
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
        tv_day_selction=findViewById(R.id.tv_day_selction);
        layout_day_selction=findViewById(R.id.layout_day_selction);
        layout_day_selction.setOnClickListener(this);
        tv_second=findViewById(R.id.tv_second);
        layout_selcond=findViewById(R.id.layout_selcond);
        layout_selcond.setOnClickListener(this);
        tv_month=findViewById(R.id.tv_month);
        layout_month_selction=findViewById(R.id.layout_month_selction);
        layout_month_selction.setOnClickListener(this);
        iv_selected=findViewById(R.id.iv_selected);
        iv_unselected=findViewById(R.id.iv_unselected);
        iv_every_unselcted=findViewById(R.id.iv_every_unselcted);
        iv_every_selcted=findViewById(R.id.iv_every_selcted);

        iv_selected.setOnClickListener(this);
        iv_unselected.setOnClickListener(this);
        iv_every_unselcted.setOnClickListener(this);
        iv_every_selcted.setOnClickListener(this);



        layout_month=findViewById(R.id.layout_month);
        layout_month.setVisibility(View.GONE);
        tv_daylist=findViewById(R.id.tv_daylist);
        layout_occurs_weekly=findViewById(R.id.layout_occurs_weekly);
        layout_occurs_weekly.setVisibility(View.GONE);
        layout_occurs_weekly.setOnClickListener(this);
        tv_occurs_weekly=findViewById(R.id.tv_occurs_weekly);
        view_day=findViewById(R.id.view_day);
        view_day.setVisibility(View.GONE);
        tv_day_txt=findViewById(R.id.tv_day_txt);
        layout_daily_selction=findViewById(R.id.layout_daily_selction);
        layout_daily_selction.setVisibility(View.GONE);
        layout_day = findViewById(R.id.layout_day);
        tv_day = findViewById(R.id.tv_day);
        layout_day.setOnClickListener(this);
        tv_info = findViewById(R.id.tv_info);
        tv_recurrence = findViewById(R.id.tv_recurrence);
        layout_rec = findViewById(R.id.layout_rec);
        layout_rec.setOnClickListener(this);
        iv_down_arrow = findViewById(R.id.iv_down_arrow);
        iv_date = findViewById(R.id.iv_date);
        iv_date.setOnClickListener(this);
        iv_time = findViewById(R.id.iv_time);
        iv_time.setOnClickListener(this);
        tv_titele = findViewById(R.id.tv_titele);
        mMainLayout = findViewById(R.id.mMainLayout);
        linearLayout = findViewById(R.id.linearLayout);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Done");
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
        tc_time_zone.setText("Time Zone(" + user_data.getUser().getUserTimezone().get(0).getText().toString() + ")");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.layout_day:
                Day_bouttomSheet();
                break;
            case R.id.layout_occurs_weekly:
                Day_text_bouttomSheet();
                break;

            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

               if (tv_recurrence.getText().toString().equals("Recurrence"))
               {
                   Global.Messageshow(getApplicationContext(),mMainLayout,"Select Recurrence",false);
               }
               else {
                   broadcate_save_data.setDate(tv_date.getText().toString());
                   broadcate_save_data.setTime(tv_time.getText().toString());
                   broadcate_save_data.setRecurrence(tv_recurrence.getText().toString());
                   broadcate_save_data.setRepeat_every(tv_day.getText().toString());
                   broadcate_save_data.setOccurs_weekly(day_list_id);
                   broadcate_save_data.setOccurs_monthly(occurs_monthly);
                   broadcate_save_data.setDay_of_month(tv_month.getText().toString());
                   broadcate_save_data.setEvery_second(second_id);
                   broadcate_save_data.setEvery_day(day_section_id);
                   SessionManager.setBroadcate_save_data(getApplicationContext(),broadcate_save_data);
                   Intent broad_caste=new Intent(getApplicationContext(),Broadcast_Name_Activity.class);
                   startActivity(broad_caste);
                   finish();
               }


                break;
            case R.id.iv_date:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                OpenBob();
                break;
            case R.id.iv_time:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                onTimer();
                break;

            case R.id.layout_rec:
                Phone_bouttomSheet();
                break;
            case R.id.iv_selected:
                layout_month_selction.setEnabled(false);
                layout_selcond.setEnabled(false);
                layout_day_selction.setEnabled(false);
                iv_unselected.setVisibility(View.VISIBLE);
                iv_every_unselcted.setVisibility(View.VISIBLE);
                iv_selected.setVisibility(View.GONE);
                iv_every_selcted.setVisibility(View.GONE);
                break;

            case R.id.iv_unselected:
                occurs_monthly="Day";
                layout_month_selction.setEnabled(true);
                layout_selcond.setEnabled(false);
                layout_day_selction.setEnabled(false);
                iv_unselected.setVisibility(View.GONE);
                iv_selected.setVisibility(View.VISIBLE);
                iv_every_selcted.setVisibility(View.GONE);
                iv_every_unselcted.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_every_unselcted:
                occurs_monthly="Every";
                layout_selcond.setEnabled(true);
                layout_selcond.setEnabled(false);
                layout_day_selction.setEnabled(true);
                iv_every_selcted.setVisibility(View.VISIBLE);
                iv_every_unselcted.setVisibility(View.GONE);
                iv_unselected.setVisibility(View.VISIBLE);
                iv_selected.setVisibility(View.GONE);
                break;
            case R.id.iv_every_selcted:
                layout_selcond.setEnabled(false);
                layout_selcond.setEnabled(false);
                layout_day_selction.setEnabled(false);
                iv_unselected.setVisibility(View.VISIBLE);
                iv_every_unselcted.setVisibility(View.VISIBLE);
                iv_selected.setVisibility(View.GONE);
                iv_every_selcted.setVisibility(View.GONE);
                break;
            case R.id.layout_month_selction:
                monthDay_bouttomSheet();
                break;
            case R.id.layout_selcond:
                Second_bouttomSheet();
                break;
            case R.id.layout_day_selction:
                Log.e("Click","Done");
                Second_Day_bouttomSheet();
                break;



        }
    }


    private void Day_text_bouttomSheet() {

        final View mView = getLayoutInflater().inflate(R.layout.email_bottom_sheet, null);
        bottomSheetDialog_day_text = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog_day_text.setContentView(mView);
        TextView tv_done = bottomSheetDialog_day_text.findViewById(R.id.tv_done);
        TextView tv_txt = bottomSheetDialog_day_text.findViewById(R.id.tv_txt);
         tv_txt.setText("");
        RecyclerView email_list = bottomSheetDialog_day_text.findViewById(R.id.email_list);
        email_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<Broadcste_Coman_Model> broadcste_coman_models = new ArrayList<>();
        String[] recursion_array = getResources().getStringArray(R.array.broadcast_days);

        for (int i = 0; i < recursion_array.length; i++) {
                int count=i+1;
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setData(recursion_array[i]);
                broadcste_coman_model.setNum(String.valueOf(count));
                broadcste_coman_model.setPhoneSelect(false);
                broadcste_coman_models.add(broadcste_coman_model);



        }
        email_list.setItemViewCacheSize(5000);

        Day_SelctionListAdepter emailListAdepter = new Day_SelctionListAdepter(getApplicationContext(), broadcste_coman_models, tv_done);
        email_list.setItemViewCacheSize(5000);
        email_list.setAdapter(emailListAdepter);
        email_list.setVisibility(View.VISIBLE);
        bottomSheetDialog_day_text.show();
    }
    private void Second_bouttomSheet() {

        final View mView = getLayoutInflater().inflate(R.layout.email_bottom_sheet, null);
        bottomSheetDialog_second = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog_second.setContentView(mView);
        TextView tv_done = bottomSheetDialog_second.findViewById(R.id.tv_done);
        TextView tv_txt = bottomSheetDialog_second.findViewById(R.id.tv_txt);
          tv_txt.setText("");
        RecyclerView email_list = bottomSheetDialog_second.findViewById(R.id.email_list);
        email_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<Broadcste_Coman_Model> broadcste_coman_models = new ArrayList<>();
        String[] recursion_array = getResources().getStringArray(R.array.broadcast_week_txt);

        for (int i = 0; i < recursion_array.length; i++) {
            if (i == 0) {
                int count=i+1;
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setData(recursion_array[i]);
                broadcste_coman_model.setNum(String.valueOf(count));
                broadcste_coman_model.setPhoneSelect(true);
                broadcste_coman_models.add(broadcste_coman_model);

            } else {
                int count=i+1;
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setData(recursion_array[i]);
                broadcste_coman_model.setNum(String.valueOf(count));
                broadcste_coman_model.setPhoneSelect(false);
                broadcste_coman_models.add(broadcste_coman_model);

            }


        }
        SecondListAdepter emailListAdepter = new SecondListAdepter(getApplicationContext(), broadcste_coman_models, tv_done);
        email_list.setAdapter(emailListAdepter);
        email_list.setVisibility(View.VISIBLE);
        bottomSheetDialog_second.show();
    }

    private void Second_Day_bouttomSheet() {

        final View mView = getLayoutInflater().inflate(R.layout.email_bottom_sheet, null);
        bottomSheetDialog_day_second = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog_day_second.setContentView(mView);
        TextView tv_done = bottomSheetDialog_day_second.findViewById(R.id.tv_done);
        TextView tv_txt = bottomSheetDialog_day_second.findViewById(R.id.tv_txt);
        tv_txt.setText("");
        RecyclerView email_list = bottomSheetDialog_day_second.findViewById(R.id.email_list);
        email_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<Broadcste_Coman_Model> broadcste_coman_models = new ArrayList<>();
        String[] recursion_array = getResources().getStringArray(R.array.broadcast_day_txt);

        for (int i = 0; i < recursion_array.length; i++) {
            if (i == 0) {
                int count=i+1;

                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setData(recursion_array[i]);
                broadcste_coman_model.setNum(String.valueOf(count));
                broadcste_coman_model.setPhoneSelect(true);
                broadcste_coman_models.add(broadcste_coman_model);

            } else {
                int count=i+1;
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setData(recursion_array[i]);
                broadcste_coman_model.setPhoneSelect(false);
                broadcste_coman_model.setNum(String.valueOf(count));

                broadcste_coman_models.add(broadcste_coman_model);

            }


        }
        Day_Second_ListAdepter emailListAdepter = new Day_Second_ListAdepter(getApplicationContext(), broadcste_coman_models, tv_done);
        email_list.setAdapter(emailListAdepter);
        email_list.setVisibility(View.VISIBLE);
        bottomSheetDialog_day_second.show();
    }

    private void Day_bouttomSheet() {

        final View mView = getLayoutInflater().inflate(R.layout.email_bottom_sheet, null);
        bottomSheetDialog_day = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog_day.setContentView(mView);
        TextView tv_done = bottomSheetDialog_day.findViewById(R.id.tv_done);
        TextView tv_txt = bottomSheetDialog_day.findViewById(R.id.tv_txt);
        tv_txt.setText("");
        RecyclerView email_list = bottomSheetDialog_day.findViewById(R.id.email_list);
        email_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<Broadcste_Coman_Model> broadcste_coman_models = new ArrayList<>();
        String[] recursion_array = getResources().getStringArray(R.array.Select_day);
        int count=0;
        if (tv_recurrence.getText().toString().equals("Daily"))
        {

            count=15;
        }
        else if (tv_recurrence.getText().toString().equals("Weekly"))
        {
            count=12;
        }
        else if (tv_recurrence.getText().toString().equals("Monthly"))
        {
            count=3;
        }
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setNum(recursion_array[i]);
                broadcste_coman_model.setPhoneSelect(true);
                broadcste_coman_models.add(broadcste_coman_model);

            } else {
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setNum(recursion_array[i]);
                broadcste_coman_model.setPhoneSelect(false);
                broadcste_coman_models.add(broadcste_coman_model);

            }


        }
        DayListAdepter emailListAdepter = new DayListAdepter(getApplicationContext(), broadcste_coman_models, tv_done);
        email_list.setAdapter(emailListAdepter);
        email_list.setVisibility(View.VISIBLE);
        bottomSheetDialog_day.show();
    }

    private void monthDay_bouttomSheet() {

        final View mView = getLayoutInflater().inflate(R.layout.email_bottom_sheet, null);
        bottomSheetDialog_monthday = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog_monthday.setContentView(mView);
        TextView tv_done = bottomSheetDialog_monthday.findViewById(R.id.tv_done);
        TextView tv_txt = bottomSheetDialog_monthday.findViewById(R.id.tv_txt);
        tv_txt.setText("");
        RecyclerView email_list = bottomSheetDialog_monthday.findViewById(R.id.email_list);
        email_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<Broadcste_Coman_Model> broadcste_coman_models = new ArrayList<>();
        String[] recursion_array = getResources().getStringArray(R.array.broadcast_day);

        for (int i = 0; i < recursion_array.length; i++) {
            if (i == 0) {
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setNum(recursion_array[i]);
                broadcste_coman_model.setPhoneSelect(true);
                broadcste_coman_models.add(broadcste_coman_model);

            } else {
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setNum(recursion_array[i]);
                broadcste_coman_model.setPhoneSelect(false);
                broadcste_coman_models.add(broadcste_coman_model);

            }


        }
        MonthDayListAdepter emailListAdepter = new MonthDayListAdepter(getApplicationContext(), broadcste_coman_models, tv_done);
        email_list.setAdapter(emailListAdepter);
        email_list.setVisibility(View.VISIBLE);
        bottomSheetDialog_monthday.show();
    }

    public void onTimer() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
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
                tv_time.setText(stime + ":" + sminite);
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(Recuring_email_broadcast_activity.this,
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
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (1000 * 60 * 60));

        datePickerDialog.show();

    }

    private void Phone_bouttomSheet() {

        final View mView = getLayoutInflater().inflate(R.layout.email_bottom_sheet, null);
        bottomSheetDialog_templateList1 = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog_templateList1.setContentView(mView);
        TextView tv_done = bottomSheetDialog_templateList1.findViewById(R.id.tv_done);
        TextView tv_txt = bottomSheetDialog_templateList1.findViewById(R.id.tv_txt);
        tv_txt.setText("");
        RecyclerView email_list = bottomSheetDialog_templateList1.findViewById(R.id.email_list);
        email_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        List<Broadcste_Coman_Model> broadcste_coman_models = new ArrayList<>();
        String[] recursion_array = getResources().getStringArray(R.array.Select_Recurrence);
        for (int i = 0; i < recursion_array.length; i++) {
            if (i == 0) {
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setData(recursion_array[i]);
                broadcste_coman_model.setPhoneSelect(true);
                broadcste_coman_models.add(broadcste_coman_model);

            } else {
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setData(recursion_array[i]);
                broadcste_coman_model.setPhoneSelect(false);
                broadcste_coman_models.add(broadcste_coman_model);

            }


        }
        PhoneListAdepter emailListAdepter = new PhoneListAdepter(getApplicationContext(), broadcste_coman_models, tv_done);
        email_list.setAdapter(emailListAdepter);
        email_list.setVisibility(View.VISIBLE);


        bottomSheetDialog_templateList1.show();
    }

    class PhoneListAdepter extends RecyclerView.Adapter<PhoneListAdepter.viewholder> {

        public Context mCtx;
        List<Broadcste_Coman_Model> userLinkedGmailList;
        List<Broadcste_Coman_Model> contacts;
        int s_position;
        TextView tv_done;


        public PhoneListAdepter(Context applicationContext, List<Broadcste_Coman_Model> userLinkedGmailList, TextView tv_done) {
            this.mCtx = applicationContext;
            this.userLinkedGmailList = userLinkedGmailList;
            this.s_position = s_position;
            this.tv_done = tv_done;
        }


        @NonNull
        @Override
        public PhoneListAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.email_select_layout, parent, false);
            return new PhoneListAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhoneListAdepter.viewholder holder, int position) {
            holder.tv_item.setVisibility(View.GONE);
            holder.iv_dufult.setVisibility(View.GONE);
            holder.tv_phone.setText(userLinkedGmailList.get(position).getData());
            holder.tv_phone.setVisibility(View.VISIBLE);
            holder.tv_phone.setTextColor(Color.parseColor("#4A4A4A"));
            if (userLinkedGmailList.get(position).isPhoneSelect()) {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }

            holder.layout_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < userLinkedGmailList.size(); i++) {
                        if (userLinkedGmailList.get(i).isPhoneSelect()) {
                            userLinkedGmailList.get(i).setPhoneSelect(false);
                            break;
                        }
                    }
                    userLinkedGmailList.get(position).setPhoneSelect(true);


                    tv_done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            tv_recurrence.setText(userLinkedGmailList.get(position).getData());
                            if (userLinkedGmailList.get(position).getData().equals("Daily")) {
                                layout_month.setVisibility(View.GONE);
                                tv_info.setVisibility(View.GONE);
                                layout_daily_selction.setVisibility(View.VISIBLE);
                                tv_day_txt.setText("Day");
                                view_day.setVisibility(View.VISIBLE);
                                layout_occurs_weekly.setVisibility(View.GONE);
                            }
                            else if (userLinkedGmailList.get(position).getData().equals("Weekly")) {

                                layout_month.setVisibility(View.GONE);
                                tv_info.setVisibility(View.GONE);
                                layout_daily_selction.setVisibility(View.VISIBLE);
                                tv_day_txt.setText("Week");
                                view_day.setVisibility(View.VISIBLE);
                                layout_occurs_weekly.setVisibility(View.VISIBLE);
                            }
                            else if (userLinkedGmailList.get(position).getData().equals("Monthly")) {
                                tv_daylist.setText("");
                                layout_month.setVisibility(View.VISIBLE);
                                tv_info.setVisibility(View.GONE);
                                layout_daily_selction.setVisibility(View.VISIBLE);
                                tv_day_txt.setText("Month");
                                view_day.setVisibility(View.VISIBLE);
                                layout_occurs_weekly.setVisibility(View.GONE);
                            }
                            else {
                                layout_month.setVisibility(View.GONE);
                                layout_occurs_weekly.setVisibility(View.GONE);
                                layout_daily_selction.setVisibility(View.GONE);
                                view_day.setVisibility(View.GONE);
                            }
                            bottomSheetDialog_templateList1.cancel();
                        }
                    });


                    holder.iv_selected.setVisibility(View.VISIBLE);
                    holder.iv_unselected.setVisibility(View.GONE);
                    notifyItemChanged(position);
                    notifyDataSetChanged();
                }
            });


        }

        @Override
        public int getItemCount() {
            return userLinkedGmailList.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item, tv_phone;
            View line_view;
            ImageView iv_dufult, iv_selected, iv_unselected;
            LinearLayout layout_select;

            public viewholder(View view) {
                super(view);
                tv_item = view.findViewById(R.id.tv_item);
                line_view = view.findViewById(R.id.line_view);
                iv_dufult = view.findViewById(R.id.iv_dufult);
                iv_selected = view.findViewById(R.id.iv_selected);
                layout_select = view.findViewById(R.id.layout_select);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                tv_phone = view.findViewById(R.id.tv_phone);
            }
        }
    }



    class Day_SelctionListAdepter extends RecyclerView.Adapter<Day_SelctionListAdepter.viewholder> {

        public Context mCtx;
        List<Broadcste_Coman_Model> userLinkedGmailList;
        List<Broadcste_Coman_Model> contacts;
        int s_position;
        TextView tv_done;


        public Day_SelctionListAdepter(Context applicationContext, List<Broadcste_Coman_Model> userLinkedGmailList, TextView tv_done) {
            this.mCtx = applicationContext;
            this.userLinkedGmailList = userLinkedGmailList;
            this.s_position = s_position;
            this.tv_done = tv_done;
        }


        @NonNull
        @Override
        public Day_SelctionListAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.day_select_layout, parent, false);
            return new Day_SelctionListAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Day_SelctionListAdepter.viewholder holder, int position) {
            holder.tv_item.setVisibility(View.GONE);
            holder.iv_dufult.setVisibility(View.GONE);
            holder.tv_phone.setText(userLinkedGmailList.get(position).getData());
            holder.tv_phone.setVisibility(View.VISIBLE);
            holder.tv_phone.setTextColor(Color.parseColor("#4A4A4A"));
      /*      if (userLinkedGmailList.get(position).isPhoneSelect()) {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }*/

            holder.iv_unselected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   /* for (int i = 0; i < userLinkedGmailList.size(); i++) {
                        if (userLinkedGmailList.get(i).isPhoneSelect()) {
                            userLinkedGmailList.get(i).setPhoneSelect(false);
                            break;
                        }
                    }*/

                    userLinkedGmailList.get(position).setPhoneSelect(true);
                    holder.iv_selected.setVisibility(View.VISIBLE);
                    holder.iv_unselected.setVisibility(View.GONE);
                    notifyItemChanged(position);

                }
            });

            holder.iv_selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                  /*  for (int i = 0; i < userLinkedGmailList.size(); i++) {
                        if (userLinkedGmailList.get(i).isPhoneSelect()) {
                            userLinkedGmailList.get(i).setPhoneSelect(false);
                            break;
                        }
                    }*/
                    userLinkedGmailList.get(position).setPhoneSelect(false);
                    holder.iv_selected.setVisibility(View.GONE);
                    holder.iv_unselected.setVisibility(View.VISIBLE);
                    notifyItemChanged(position);

                }
            });

            tv_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String day_list="";

                    for (int i=0;i<userLinkedGmailList.size();i++)
                    {
                        if (userLinkedGmailList.get(i).isPhoneSelect()==true)
                        {
                            if (day_list.equals(""))
                            {
                                day_list=userLinkedGmailList.get(i).getData();

                            }
                            else {
                                day_list=day_list+","+userLinkedGmailList.get(i).getData();

                            }
                            if (day_list_id.equals(""))
                            {
                                day_list_id=userLinkedGmailList.get(i).getNum();

                            }
                            else {
                                day_list_id=day_list_id+","+userLinkedGmailList.get(i).getNum();

                            }

                        }
                        tv_daylist.setText(day_list);
                    }
                    bottomSheetDialog_day_text.cancel();
                }
            });


        }

        @Override
        public int getItemCount() {
            return userLinkedGmailList.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item, tv_phone;
            View line_view;
            ImageView iv_dufult, iv_selected, iv_unselected;
            LinearLayout layout_select;

            public viewholder(View view) {
                super(view);
                tv_item = view.findViewById(R.id.tv_item);
                line_view = view.findViewById(R.id.line_view);
                iv_dufult = view.findViewById(R.id.iv_dufult);
                iv_selected = view.findViewById(R.id.iv_selected);
                layout_select = view.findViewById(R.id.layout_select);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                tv_phone = view.findViewById(R.id.tv_phone);
            }
        }
    }


    class DayListAdepter extends RecyclerView.Adapter<DayListAdepter.viewholder> {

        public Context mCtx;
        List<Broadcste_Coman_Model> userLinkedGmailList;
        List<Broadcste_Coman_Model> contacts;
        int s_position;
        TextView tv_done;


        public DayListAdepter(Context applicationContext, List<Broadcste_Coman_Model> userLinkedGmailList, TextView tv_done) {
            this.mCtx = applicationContext;
            this.userLinkedGmailList = userLinkedGmailList;
            this.s_position = s_position;
            this.tv_done = tv_done;
        }


        @NonNull
        @Override
        public DayListAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.email_select_layout, parent, false);
            return new DayListAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DayListAdepter.viewholder holder, int position) {
            holder.tv_item.setVisibility(View.GONE);
            holder.iv_dufult.setVisibility(View.GONE);
            holder.tv_phone.setText(userLinkedGmailList.get(position).getNum());
            holder.tv_phone.setVisibility(View.VISIBLE);
            holder.tv_phone.setTextColor(Color.parseColor("#4A4A4A"));
            if (userLinkedGmailList.get(position).isPhoneSelect()) {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }

            holder.layout_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < userLinkedGmailList.size(); i++) {
                        if (userLinkedGmailList.get(i).isPhoneSelect()) {
                            userLinkedGmailList.get(i).setPhoneSelect(false);
                            break;
                        }
                    }
                    userLinkedGmailList.get(position).setPhoneSelect(true);


                    tv_done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            tv_day.setText(userLinkedGmailList.get(position).getNum());
                            tv_info.setVisibility(View.GONE);
                            bottomSheetDialog_day.cancel();
                        }
                    });


                    holder.iv_selected.setVisibility(View.VISIBLE);
                    holder.iv_unselected.setVisibility(View.GONE);
                    notifyItemChanged(position);
                    notifyDataSetChanged();
                }
            });


        }

        @Override
        public int getItemCount() {
            return userLinkedGmailList.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item, tv_phone;
            View line_view;
            ImageView iv_dufult, iv_selected, iv_unselected;
            LinearLayout layout_select;

            public viewholder(View view) {
                super(view);
                tv_item = view.findViewById(R.id.tv_item);
                line_view = view.findViewById(R.id.line_view);
                iv_dufult = view.findViewById(R.id.iv_dufult);
                iv_selected = view.findViewById(R.id.iv_selected);
                layout_select = view.findViewById(R.id.layout_select);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                tv_phone = view.findViewById(R.id.tv_phone);
            }
        }
    }

    class MonthDayListAdepter extends RecyclerView.Adapter<MonthDayListAdepter.viewholder> {

        public Context mCtx;
        List<Broadcste_Coman_Model> userLinkedGmailList;
        List<Broadcste_Coman_Model> contacts;
        int s_position;
        TextView tv_done;


        public MonthDayListAdepter(Context applicationContext, List<Broadcste_Coman_Model> userLinkedGmailList, TextView tv_done) {
            this.mCtx = applicationContext;
            this.userLinkedGmailList = userLinkedGmailList;
            this.s_position = s_position;
            this.tv_done = tv_done;
        }


        @NonNull
        @Override
        public MonthDayListAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.email_select_layout, parent, false);
            return new MonthDayListAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MonthDayListAdepter.viewholder holder, int position) {
            holder.tv_item.setVisibility(View.GONE);
            holder.iv_dufult.setVisibility(View.GONE);
            holder.tv_phone.setText(userLinkedGmailList.get(position).getNum());
            holder.tv_phone.setVisibility(View.VISIBLE);
            holder.tv_phone.setTextColor(Color.parseColor("#4A4A4A"));
            if (userLinkedGmailList.get(position).isPhoneSelect()) {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }

            holder.layout_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < userLinkedGmailList.size(); i++) {
                        if (userLinkedGmailList.get(i).isPhoneSelect()) {
                            userLinkedGmailList.get(i).setPhoneSelect(false);
                            break;
                        }
                    }
                    userLinkedGmailList.get(position).setPhoneSelect(true);


                    tv_done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            tv_month.setText(userLinkedGmailList.get(position).getNum());
                            tv_info.setVisibility(View.GONE);
                            bottomSheetDialog_monthday.cancel();
                        }
                    });


                    holder.iv_selected.setVisibility(View.VISIBLE);
                    holder.iv_unselected.setVisibility(View.GONE);
                    notifyItemChanged(position);
                    notifyDataSetChanged();
                }
            });


        }

        @Override
        public int getItemCount() {
            return userLinkedGmailList.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item, tv_phone;
            View line_view;
            ImageView iv_dufult, iv_selected, iv_unselected;
            LinearLayout layout_select;

            public viewholder(View view) {
                super(view);
                tv_item = view.findViewById(R.id.tv_item);
                line_view = view.findViewById(R.id.line_view);
                iv_dufult = view.findViewById(R.id.iv_dufult);
                iv_selected = view.findViewById(R.id.iv_selected);
                layout_select = view.findViewById(R.id.layout_select);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                tv_phone = view.findViewById(R.id.tv_phone);
            }
        }
    }

    class SecondListAdepter extends RecyclerView.Adapter<SecondListAdepter.viewholder> {

        public Context mCtx;
        List<Broadcste_Coman_Model> userLinkedGmailList;
        List<Broadcste_Coman_Model> contacts;
        int s_position;
        TextView tv_done;


        public SecondListAdepter(Context applicationContext, List<Broadcste_Coman_Model> userLinkedGmailList, TextView tv_done) {
            this.mCtx = applicationContext;
            this.userLinkedGmailList = userLinkedGmailList;
            this.s_position = s_position;
            this.tv_done = tv_done;
        }


        @NonNull
        @Override
        public SecondListAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.email_select_layout, parent, false);
            return new SecondListAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SecondListAdepter.viewholder holder, int position) {
            holder.tv_item.setVisibility(View.GONE);
            holder.iv_dufult.setVisibility(View.GONE);
            holder.tv_phone.setText(userLinkedGmailList.get(position).getData());
            holder.tv_phone.setVisibility(View.VISIBLE);
            holder.tv_phone.setTextColor(Color.parseColor("#4A4A4A"));
            if (userLinkedGmailList.get(position).isPhoneSelect()) {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }

            holder.layout_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < userLinkedGmailList.size(); i++) {
                        if (userLinkedGmailList.get(i).isPhoneSelect()) {
                            userLinkedGmailList.get(i).setPhoneSelect(false);
                            break;
                        }
                    }
                    userLinkedGmailList.get(position).setPhoneSelect(true);


                    tv_done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            tv_second.setText(userLinkedGmailList.get(position).getData());
                            tv_info.setVisibility(View.GONE);
                            bottomSheetDialog_second.cancel();
                            second_id=userLinkedGmailList.get(position).getNum();
                        }
                    });


                    holder.iv_selected.setVisibility(View.VISIBLE);
                    holder.iv_unselected.setVisibility(View.GONE);
                    notifyItemChanged(position);
                    notifyDataSetChanged();
                }
            });


        }

        @Override
        public int getItemCount() {
            return userLinkedGmailList.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item, tv_phone;
            View line_view;
            ImageView iv_dufult, iv_selected, iv_unselected;
            LinearLayout layout_select;

            public viewholder(View view) {
                super(view);
                tv_item = view.findViewById(R.id.tv_item);
                line_view = view.findViewById(R.id.line_view);
                iv_dufult = view.findViewById(R.id.iv_dufult);
                iv_selected = view.findViewById(R.id.iv_selected);
                layout_select = view.findViewById(R.id.layout_select);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                tv_phone = view.findViewById(R.id.tv_phone);
            }
        }
    }

    class Day_Second_ListAdepter extends RecyclerView.Adapter<Day_Second_ListAdepter.viewholder> {

        public Context mCtx;
        List<Broadcste_Coman_Model> userLinkedGmailList;
        List<Broadcste_Coman_Model> contacts;
        int s_position;
        TextView tv_done;


        public Day_Second_ListAdepter(Context applicationContext, List<Broadcste_Coman_Model> userLinkedGmailList, TextView tv_done) {
            this.mCtx = applicationContext;
            this.userLinkedGmailList = userLinkedGmailList;
            this.s_position = s_position;
            this.tv_done = tv_done;
        }


        @NonNull
        @Override
        public Day_Second_ListAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.email_select_layout, parent, false);
            return new Day_Second_ListAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Day_Second_ListAdepter.viewholder holder, int position) {
            holder.tv_item.setVisibility(View.GONE);
            holder.iv_dufult.setVisibility(View.GONE);
            holder.tv_phone.setText(userLinkedGmailList.get(position).getData());
            holder.tv_phone.setVisibility(View.VISIBLE);
            holder.tv_phone.setTextColor(Color.parseColor("#4A4A4A"));
            if (userLinkedGmailList.get(position).isPhoneSelect()) {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }

            holder.layout_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < userLinkedGmailList.size(); i++) {
                        if (userLinkedGmailList.get(i).isPhoneSelect()) {
                            userLinkedGmailList.get(i).setPhoneSelect(false);
                            break;
                        }
                    }
                    userLinkedGmailList.get(position).setPhoneSelect(true);


                    tv_done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            tv_day_selction.setText(userLinkedGmailList.get(position).getData());
                            tv_info.setVisibility(View.GONE);
                            bottomSheetDialog_day_second.cancel();
                            day_section_id=userLinkedGmailList.get(position).getNum();
                        }
                    });


                    holder.iv_selected.setVisibility(View.VISIBLE);
                    holder.iv_unselected.setVisibility(View.GONE);
                    notifyItemChanged(position);
                    notifyDataSetChanged();
                }
            });


        }

        @Override
        public int getItemCount() {
            return userLinkedGmailList.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item, tv_phone;
            View line_view;
            ImageView iv_dufult, iv_selected, iv_unselected;
            LinearLayout layout_select;

            public viewholder(View view) {
                super(view);
                tv_item = view.findViewById(R.id.tv_item);
                line_view = view.findViewById(R.id.line_view);
                iv_dufult = view.findViewById(R.id.iv_dufult);
                iv_selected = view.findViewById(R.id.iv_selected);
                layout_select = view.findViewById(R.id.layout_select);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                tv_phone = view.findViewById(R.id.tv_phone);
            }
        }
    }

}