package com.contactninja.Main_Broadcast;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Model.Broadcate_save_data;
import com.contactninja.Model.Broadcste_Coman_Model;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.Global_Time;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables")
public class Recuring_email_broadcast_activity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    TextView  tv_day, tv_daylist;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    String main_date="";
    int m_hour = 0;
    int m_minute = 0;
    ImageView iv_back, iv_time, iv_date, iv_down_arrow,iv_big_logo,iv_small_logo;
    TextView save_button, tv_day_txt, tv_occurs_weekly;
    LinearLayout la_date, la_time, linearLayout, layout_rec,
            layout_day, layout_daily_selction,
            layout_weekly,layout_occurs_weekly, layout_month, layout_month_selction, layout_selcond, layout_day_selction;
    TextView tv_date, tv_time, tv_titele, tv_recurrence, tv_month, tv_day_selction;
    ConstraintLayout mMainLayout;
    BottomSheetDialog bottomSheetDialog_Recurrence,
            bottomSheetDialog_day, bottomSheetDialog_day_text, bottomSheetDialog_monthday, bottomSheetDialog_second,
            bottomSheetDialog_day_second;
    TextView tv_info, tv_second;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private BroadcastReceiver mNetworkReceiver;
    private long mLastClickTime = 0;
    View view_day;
    ImageView iv_unselected, iv_selected, iv_every_selcted, iv_every_unselcted;
    Broadcate_save_data broadcate_save_data = new Broadcate_save_data();
    String occurs_monthly = "Day", day_list_id = "1", second_id = "1", day_section_id = "1";


    String[] Select_Recurrence ;
    String[] Select_day;
    String[] Days_Of_Week;
    String[] Days_31;
    String[] Week_Of;


    List<Broadcste_Coman_Model> list_Recurrence = new ArrayList<>();
    List<Broadcste_Coman_Model> list_day_15 = new ArrayList<>();
    List<Broadcste_Coman_Model> list_week_12 = new ArrayList<>();
    List<Broadcste_Coman_Model> list_month_3 = new ArrayList<>();
    List<Broadcste_Coman_Model> list_DayOfWeek = new ArrayList<>();
    List<Broadcste_Coman_Model> list_Day_31 = new ArrayList<>();
    List<Broadcste_Coman_Model> list_Week_Of = new ArrayList<>();
    List<Broadcste_Coman_Model> list_DayOfWeek_Month = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuring_email_broadcast);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);

        Select_Recurrence =getResources().getStringArray(R.array.Select_Recurrence);
        Select_day = getResources().getStringArray(R.array.Select_day);
        Days_Of_Week = getResources().getStringArray(R.array.broadcast_days);
        Days_31 = getResources().getStringArray(R.array.broadcast_day);
        Week_Of = getResources().getStringArray(R.array.broadcast_week_txt);

        IntentUI();

        /*crate static list for first time */
        ststicListCreate();


        SessionManager.getGroupList(getApplicationContext());
        if (SessionManager.getCampaign_type(getApplicationContext()).equals("SMS")) {
            tv_titele.setText(getResources().getString(R.string.broadcast_text));
        } else {
            tv_titele.setText(getResources().getString(R.string.broadcast_email));
        }
        broadcate_save_data = SessionManager.getBroadcate_save_data(getApplicationContext());
        String formateChnage = Global_Time.DateFormateMonth(Global_Time.getCurrentDate());
        tv_date.setText(formateChnage);
        main_date=Global_Time.getCurrentDate();
        tv_time.setText(Global_Time.getCurrentTime());


        if (SessionManager.getBroadcast_flag(getApplicationContext()).equals("edit")) {
            broadcate_save_data = SessionManager.getBroadcate_save_data(getApplicationContext());
            tv_time.setText(broadcate_save_data.getTime());
            String formateChnage1 = Global_Time.DateFormateMonth(broadcate_save_data.getDate());
            tv_date.setText(formateChnage1);
            main_date=broadcate_save_data.getDate();;


            tv_recurrence.setText(broadcate_save_data.getRecurrence());
            tv_recurrence.setTextColor(getResources().getColor(R.color.text_reg));
            tv_day.setText(broadcate_save_data.getRepeat_every());


            switch (broadcate_save_data.getRecurrence()) {
                case "Daily":
                    layout_month.setVisibility(View.GONE);
                    tv_info.setVisibility(View.GONE);
                    layout_daily_selction.setVisibility(View.VISIBLE);
                    tv_day_txt.setText("Day");
                    view_day.setVisibility(View.VISIBLE);
                    layout_occurs_weekly.setVisibility(View.GONE);
                    break;
                case "Weekly":

                    layout_month.setVisibility(View.GONE);
                    tv_info.setVisibility(View.GONE);
                    layout_daily_selction.setVisibility(View.VISIBLE);
                    tv_day_txt.setText("Week");
                    view_day.setVisibility(View.VISIBLE);
                    layout_occurs_weekly.setVisibility(View.VISIBLE);

                    /*set day first selset */
                    Select_day_of_week();


                    break;
                case "Monthly":
                    tv_daylist.setText("");
                    layout_month.setVisibility(View.VISIBLE);
                    tv_info.setVisibility(View.GONE);
                    layout_daily_selction.setVisibility(View.VISIBLE);
                    tv_day_txt.setText("Month");
                    view_day.setVisibility(View.VISIBLE);
                    layout_occurs_weekly.setVisibility(View.GONE);


                    try {
                        if (broadcate_save_data.getOccurs_monthly().equals("Day")) {
                            iv_selected.setVisibility(View.VISIBLE);
                            iv_every_selcted.setVisibility(View.GONE);
                            iv_every_unselcted.setVisibility(View.VISIBLE);
                            tv_month.setText(broadcate_save_data.getDay_of_month());
                        }
                        else {
                            second_id = broadcate_save_data.getEvery_second();
                            if (!second_id.equals(""))
                            {
                                String week_of=Week_Of[Integer.parseInt(second_id)-1];
                                tv_second.setText(week_of);

                            }

                            day_section_id = broadcate_save_data.getEvery_day();

                            if (!day_section_id.equals(""))
                            {
                                String week_day=Days_Of_Week[Integer.parseInt(day_section_id)];
                                tv_day_selction.setText(week_day);
                            }



                            iv_every_selcted.setVisibility(View.VISIBLE);
                            iv_selected.setVisibility(View.GONE);
                            iv_unselected.setVisibility(View.VISIBLE);
                        }
                    }
                    catch (Exception e)
                    {

                    }


               //     iv_every_unselcted.setVisibility(View.VISIBLE);



                    break;
                default:
                    layout_month.setVisibility(View.GONE);
                    layout_occurs_weekly.setVisibility(View.GONE);
                    layout_daily_selction.setVisibility(View.GONE);
                    view_day.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void Select_day_of_week() {
        String[] elements = broadcate_save_data.getOccurs_weekly().split(",");
        List<String> fixedLenghtList = Arrays.asList(elements);
        List<Broadcste_Coman_Model> list_DayOfWeek_new = new ArrayList<>();

        for (int i = 0; i < list_DayOfWeek.size(); i++) {
            Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
            broadcste_coman_model.setData(list_DayOfWeek.get(i).getData());
             broadcste_coman_model.setNum(list_DayOfWeek.get(i).getNum());
             broadcste_coman_model.setPhoneSelect(list_DayOfWeek.get(i).isPhoneSelect());

            for(int j=0;j<fixedLenghtList.size();j++){
                if(fixedLenghtList.get(j).equals(list_DayOfWeek.get(i).getNum())){
                    broadcste_coman_model.setPhoneSelect(true);
                    break;
                }
            }

            list_DayOfWeek_new.add(broadcste_coman_model);
        }
        list_DayOfWeek=list_DayOfWeek_new;
        SetSelected();

    }

    private void ststicListCreate() {
        /* select day ,week ,month list */
        Select_Day_month_week();

        /* select day 15*/
        Select_Day_15();

        /* select week 12*/
        Select_Week_12();

        /* select Monthly 3*/
        Select_Monthly_3();

        /*select Day of week*/
        Select_day_week();

        /*select Day 31*/
        Select_day_31();

        /*select Week */
        Select_Week();

        /*select Day of week Month*/
        Select_day_week_Month();

    }

    private void Select_day_week_Month() {
        int count =0;
        for (int i = 0; i < Days_Of_Week.length; i++) {
            Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
            broadcste_coman_model.setData(Days_Of_Week[i]);
            if(Days_Of_Week[i].equals(Days_Of_Week[0])){
                broadcste_coman_model.setPhoneSelect(true);
            }
            else {
                broadcste_coman_model.setPhoneSelect(false);
            }
            broadcste_coman_model.setNum(String.valueOf(count));
            count++;
            list_DayOfWeek_Month.add(broadcste_coman_model);
            Log.e("Day is",new Gson().toJson(list_DayOfWeek_Month));
        }
    }

    private void Select_Week() {
        int count=1;
        for (int i = 0; i < Week_Of.length; i++) {
            Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
            if (i == 0) {
                broadcste_coman_model.setPhoneSelect(true);
            } else {
                broadcste_coman_model.setPhoneSelect(false);
            }
            broadcste_coman_model.setData(Week_Of[i]);
            broadcste_coman_model.setNum(String.valueOf(count));
            list_Week_Of.add(broadcste_coman_model);
            count++;
        }
    }

    private void Select_day_31 () {
        for (int i = 0; i < Days_31.length; i++) {
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                if (i == 0) {
                    broadcste_coman_model.setPhoneSelect(true);
                } else {
                    broadcste_coman_model.setPhoneSelect(false);
                }
                broadcste_coman_model.setNum(Days_31[i]);
                list_Day_31.add(broadcste_coman_model);
        }
    }

    private void Select_day_week() {

        int count =0;
        for (int i = 0; i < Days_Of_Week.length; i++) {
            Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
            broadcste_coman_model.setData(Days_Of_Week[i]);
            broadcste_coman_model.setNum(String.valueOf(count));
            count++;
            list_DayOfWeek.add(broadcste_coman_model);
        }

    }


    private void Select_Monthly_3() {
        for (int i = 0; i < 3; i++) {
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setNum(Select_day[i]);
                if (i == 0) {
                    broadcste_coman_model.setPhoneSelect(true);
                } else {
                    broadcste_coman_model.setPhoneSelect(false);
                }
                list_month_3.add(broadcste_coman_model);
        }
    }

    private void Select_Week_12() {

        for (int i = 0; i < 12; i++) {
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setNum(Select_day[i]);
                if (i == 0) {
                    broadcste_coman_model.setPhoneSelect(true);
                } else {
                    broadcste_coman_model.setPhoneSelect(false);
                }
                list_week_12.add(broadcste_coman_model);
        }
    }

    private void Select_Day_15() {

        for (int i = 0; i < 15; i++) {
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setNum(Select_day[i]);
                if (i == 0) {
                    broadcste_coman_model.setPhoneSelect(true);
                } else {
                    broadcste_coman_model.setPhoneSelect(false);
                }
                list_day_15.add(broadcste_coman_model);
        }
    }

    private void Select_Day_month_week() {
        String[] recursion_array = getResources().getStringArray(R.array.Select_Recurrence);
        for (int i = 0; i < recursion_array.length; i++) {
                Broadcste_Coman_Model broadcste_coman_model = new Broadcste_Coman_Model();
                broadcste_coman_model.setData(recursion_array[i]);
                broadcste_coman_model.setPhoneSelect(false);
                list_Recurrence.add(broadcste_coman_model);
        }
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
        tv_day_selction = findViewById(R.id.tv_day_selction);
        layout_day_selction = findViewById(R.id.layout_day_selction);
        layout_day_selction.setOnClickListener(this);
        tv_second = findViewById(R.id.tv_second);
        layout_selcond = findViewById(R.id.layout_selcond);
        layout_selcond.setOnClickListener(this);
        tv_month = findViewById(R.id.tv_month);
        layout_month_selction = findViewById(R.id.layout_month_selction);
        layout_month_selction.setOnClickListener(this);
        iv_selected = findViewById(R.id.iv_selected);
        iv_unselected = findViewById(R.id.iv_unselected);
        iv_every_unselcted = findViewById(R.id.iv_every_unselcted);
        iv_every_selcted = findViewById(R.id.iv_every_selcted);

        iv_selected.setOnClickListener(this);
        iv_unselected.setOnClickListener(this);
        iv_every_unselcted.setOnClickListener(this);
        iv_every_selcted.setOnClickListener(this);


        layout_month = findViewById(R.id.layout_month);
        layout_month.setVisibility(View.GONE);
        tv_daylist = findViewById(R.id.tv_daylist);
        layout_occurs_weekly = findViewById(R.id.layout_occurs_weekly);
        layout_weekly = findViewById(R.id.layout_weekly);
        layout_occurs_weekly.setVisibility(View.GONE);
        layout_weekly.setOnClickListener(this);
        tv_occurs_weekly = findViewById(R.id.tv_occurs_weekly);
        view_day = findViewById(R.id.view_day);
        view_day.setVisibility(View.GONE);
        tv_day_txt = findViewById(R.id.tv_day_txt);
        layout_daily_selction = findViewById(R.id.layout_daily_selction);
        layout_daily_selction.setVisibility(View.GONE);
        layout_day = findViewById(R.id.layout_day);
        tv_day = findViewById(R.id.tv_day);
        layout_day.setOnClickListener(this);
        tv_info = findViewById(R.id.tv_info);
        iv_small_logo = findViewById(R.id.iv_small_logo);
        iv_big_logo = findViewById(R.id.iv_big_logo);
        tv_recurrence = findViewById(R.id.tv_recurrence);
        tv_recurrence.setTextColor(getResources().getColor(R.color.text_gray));
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.layout_day:
                Day_bouttomSheet();
                break;
            case R.id.layout_weekly:
                Day_text_bouttomSheet();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                String recurring_type= tv_recurrence.getText().toString();
                if (recurring_type.equals("Recurrence")) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.Select_Recurrence), false);
                } else {
                    broadcate_save_data.setDate(main_date);
                    try {
                        broadcate_save_data.setTime(Global_Time.time_12_to_24(tv_time.getText().toString().trim()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    broadcate_save_data.setRecurrence(tv_recurrence.getText().toString());
                    broadcate_save_data.setRepeat_every(tv_day.getText().toString());
                    broadcate_save_data.setOccurs_weekly(day_list_id);
                    broadcate_save_data.setOccurs_monthly(occurs_monthly);
                    broadcate_save_data.setDay_of_month(tv_month.getText().toString());
                    broadcate_save_data.setEvery_second(second_id);
                    broadcate_save_data.setEvery_day(day_section_id);
                    SessionManager.setBroadcate_save_data(getApplicationContext(), broadcate_save_data);
                    SessionManager.setBroadcast_flag("edit");
                    Intent broad_caste = new Intent(getApplicationContext(), Broadcast_Name_Activity.class);
                    broad_caste.putExtra("Activty","Recurrence");
                    startActivity(broad_caste);
                    finish();
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

            case R.id.layout_rec:
                Recurrence_bouttomSheet();
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
                occurs_monthly = "Day";
                layout_month_selction.setEnabled(true);
                layout_selcond.setEnabled(false);
                layout_day_selction.setEnabled(false);
                iv_unselected.setVisibility(View.GONE);
                iv_selected.setVisibility(View.VISIBLE);
                iv_every_selcted.setVisibility(View.GONE);
                iv_every_unselcted.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_every_unselcted:
                occurs_monthly = "Every";
                layout_selcond.setEnabled(true);
                layout_day_selction.setEnabled(true);
                iv_every_selcted.setVisibility(View.VISIBLE);
                iv_every_unselcted.setVisibility(View.GONE);
                iv_unselected.setVisibility(View.VISIBLE);
                iv_selected.setVisibility(View.GONE);
                break;
            case R.id.iv_every_selcted:
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
                Second_Day_bouttomSheet();
                break;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void Day_text_bouttomSheet() {

        final View mView = getLayoutInflater().inflate(R.layout.broadcast_bottom_sheet, null);
        bottomSheetDialog_day_text = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog_day_text.setContentView(mView);

        RecyclerView rv_Days_Of_Week = bottomSheetDialog_day_text.findViewById(R.id.number_list);
        rv_Days_Of_Week.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

     //   number_list.setItemViewCacheSize(50000);

        Day_SelctionListAdepter emailListAdepter = new Day_SelctionListAdepter(getApplicationContext(), list_DayOfWeek);
        rv_Days_Of_Week.setAdapter(emailListAdepter);

        bottomSheetDialog_day_text.show();
    }

    private void Second_bouttomSheet() {

        final View mView = getLayoutInflater().inflate(R.layout.broadcast_bottom_sheet, null);
        bottomSheetDialog_second = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog_second.setContentView(mView);
        RecyclerView number_list = bottomSheetDialog_second.findViewById(R.id.number_list);
        number_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        SecondListAdepter emailListAdepter = new SecondListAdepter(getApplicationContext(), list_Week_Of);
        number_list.setAdapter(emailListAdepter);
        bottomSheetDialog_second.show();
    }

    private void Second_Day_bouttomSheet() {

        final View mView = getLayoutInflater().inflate(R.layout.broadcast_bottom_sheet, null);
        bottomSheetDialog_day_second = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog_day_second.setContentView(mView);
        RecyclerView number_list = bottomSheetDialog_day_second.findViewById(R.id.number_list);
        number_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Day_Second_ListAdepter emailListAdepter = new Day_Second_ListAdepter(getApplicationContext(), list_DayOfWeek_Month);
        number_list.setAdapter(emailListAdepter);
        bottomSheetDialog_day_second.show();
    }

    private void Day_bouttomSheet() {

        final View mView = getLayoutInflater().inflate(R.layout.broadcast_bottom_sheet, null);
        bottomSheetDialog_day = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog_day.setContentView(mView);

        TextView tv_txt = bottomSheetDialog_day.findViewById(R.id.tv_txt);
        tv_txt.setText("");
        RecyclerView number_list = bottomSheetDialog_day.findViewById(R.id.number_list);
        number_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        List<Broadcste_Coman_Model> list_day=new ArrayList<>();
        if (tv_recurrence.getText().toString().equals(Select_Recurrence[0])) {
            list_day.clear();
            list_day=list_day_15;
        } else if (tv_recurrence.getText().toString().equals(Select_Recurrence[1])) {
            list_day.clear();
            list_day=list_week_12;
        } else if (tv_recurrence.getText().toString().equals(Select_Recurrence[2])) {
            list_day.clear();
            list_day=list_month_3;
        }
        DayListAdepter   dayListAdepter = new DayListAdepter(getApplicationContext(),list_day);
        number_list.setAdapter(dayListAdepter);

        bottomSheetDialog_day.show();
    }

    private void monthDay_bouttomSheet() {

        final View mView = getLayoutInflater().inflate(R.layout.broadcast_bottom_sheet, null);
        bottomSheetDialog_monthday = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog_monthday.setContentView(mView);

        RecyclerView number_list = bottomSheetDialog_monthday.findViewById(R.id.number_list);
        number_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        MonthDayListAdepter emailListAdepter = new MonthDayListAdepter(getApplicationContext(), list_Day_31);
        number_list.setAdapter(emailListAdepter);
        bottomSheetDialog_monthday.show();
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
                    timeSet = "PM";
                } else if (m_hour == 0) {
                    m_hour += 12;
                    timeSet = "AM";
                } else if (m_hour == 12) {
                    timeSet = "PM";
                } else {
                    timeSet = "AM";
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

                        String formateChnage = Global_Time.DateFormateMonth(year+"-"+sMonth+"-"+sdate);
                        tv_date.setText(formateChnage);
                        main_date=year + "-" + sMonth + "-" + sdate;

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (1000 * 60 * 60));

        datePickerDialog.show();

    }

    private void Recurrence_bouttomSheet() {

        final View mView = getLayoutInflater().inflate(R.layout.broadcast_bottom_sheet, null);
        bottomSheetDialog_Recurrence = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog_Recurrence.setContentView(mView);
        //  TextView tv_done = bottomSheetDialog_templateList1.findViewById(R.id.tv_done);
        TextView tv_txt = bottomSheetDialog_Recurrence.findViewById(R.id.tv_txt);
        tv_txt.setText("");
        RecyclerView number_list = bottomSheetDialog_Recurrence.findViewById(R.id.number_list);
        number_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        RecurrenceAdepter recurrenceAdepter = new RecurrenceAdepter(getApplicationContext(), list_Recurrence);
        number_list.setAdapter(recurrenceAdepter);

        bottomSheetDialog_Recurrence.show();
    }

    class RecurrenceAdepter extends RecyclerView.Adapter<RecurrenceAdepter.viewholder> {

        public Context mCtx;
        List<Broadcste_Coman_Model> list_Recurrence;


        public RecurrenceAdepter(Context applicationContext, List<Broadcste_Coman_Model> list_Recurrence) {
            this.mCtx = applicationContext;
            this.list_Recurrence = list_Recurrence;
        }


        @NonNull
        @Override
        public RecurrenceAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.broadcast_select_layout, parent, false);
            return new RecurrenceAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecurrenceAdepter.viewholder holder, int position) {
            Broadcste_Coman_Model item = list_Recurrence.get(position);
            holder.tv_phone.setText(item.getData());
            if (item.isPhoneSelect()) {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }
            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < list_Recurrence.size(); i++) {
                        if (list_Recurrence.get(i).isPhoneSelect()) {
                            list_Recurrence.get(i).setPhoneSelect(false);
                            break;
                        }
                    }
                    item.setPhoneSelect(true);
                    tv_recurrence.setText(item.getData());
                    tv_recurrence.setTextColor(mCtx.getResources().getColor(R.color.text_reg));
                    /* inisilize to day */
                    Day_list_set();

                    if (item.getData().equals(Select_Recurrence[0])) {
                        layout_daily_selction.setVisibility(View.VISIBLE);
                        layout_occurs_weekly.setVisibility(View.GONE);
                        view_day.setVisibility(View.VISIBLE);
                        layout_month.setVisibility(View.GONE);
                        tv_info.setVisibility(View.GONE);
                        iv_big_logo.setVisibility(View.GONE);
                        iv_small_logo.setVisibility(View.VISIBLE);
                        tv_day_txt.setText("Day");

                    } else if (item.getData().equals(Select_Recurrence[1])) {
                        layout_daily_selction.setVisibility(View.VISIBLE);
                        layout_month.setVisibility(View.GONE);
                        tv_info.setVisibility(View.GONE);
                        iv_big_logo.setVisibility(View.GONE);
                        iv_small_logo.setVisibility(View.VISIBLE);
                        tv_day_txt.setText("Week");
                        view_day.setVisibility(View.VISIBLE);
                        layout_occurs_weekly.setVisibility(View.VISIBLE);
                        SetSelected();
                    } else if (item.getData().equals(Select_Recurrence[2])) {
                        layout_daily_selction.setVisibility(View.VISIBLE);
                        layout_month.setVisibility(View.VISIBLE);
                        tv_info.setVisibility(View.GONE);
                        iv_small_logo.setVisibility(View.VISIBLE);
                        iv_big_logo.setVisibility(View.GONE);
                        tv_day_txt.setText("Month");
                        view_day.setVisibility(View.VISIBLE);
                        layout_occurs_weekly.setVisibility(View.GONE);
                    }
                    bottomSheetDialog_Recurrence.cancel();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list_Recurrence.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_phone;
            ImageView iv_selected, iv_unselected;
            LinearLayout layout_item;

            public viewholder(View view) {
                super(view);
                iv_selected = view.findViewById(R.id.iv_selected);
                layout_item = view.findViewById(R.id.layout_item);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                tv_phone = view.findViewById(R.id.tv_phone);
            }
        }
    }

    private void Day_list_set() {
        if (tv_recurrence.getText().toString().equals(Select_Recurrence[0])) {
            for (int i = 0; i < list_day_15.size(); i++) {
                if (list_day_15.get(i).isPhoneSelect()) {
                    tv_day.setText(list_day_15.get(i).getNum());
                    break;
                }
            }
        } else if (tv_recurrence.getText().toString().equals(Select_Recurrence[1])) {
            for (int i = 0; i < list_week_12.size(); i++) {
                if (list_week_12.get(i).isPhoneSelect()) {
                    tv_day.setText(list_week_12.get(i).getNum());
                    break;
                }
            }
        } else if (tv_recurrence.getText().toString().equals(Select_Recurrence[2])) {
            for (int i = 0; i < list_month_3.size(); i++) {
                if (list_month_3.get(i).isPhoneSelect()) {
                    tv_day.setText(list_month_3.get(i).getNum());
                    break;
                }
            }
        }
    }


    class Day_SelctionListAdepter extends RecyclerView.Adapter<Day_SelctionListAdepter.viewholder> {

        public Context mCtx;
        List<Broadcste_Coman_Model> list_DayOfWeek;


        public Day_SelctionListAdepter(Context applicationContext, List<Broadcste_Coman_Model> list_DayOfWeek) {
            this.mCtx = applicationContext;
            this.list_DayOfWeek = list_DayOfWeek;

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
            Broadcste_Coman_Model item = list_DayOfWeek.get(position);
            holder.tv_phone.setText(item.getData());

            if(item.isPhoneSelect()){
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            }else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }


            holder.layout_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.isPhoneSelect()) {
                        item.setPhoneSelect(false);
                        holder.iv_selected.setVisibility(View.GONE);
                        holder.iv_unselected.setVisibility(View.VISIBLE);
                    } else {
                        item.setPhoneSelect(true);
                        holder.iv_selected.setVisibility(View.VISIBLE);
                        holder.iv_unselected.setVisibility(View.GONE);
                    }
                    SetSelected();
                    notifyDataSetChanged();
                }
            });
        }



        @Override
        public int getItemCount() {
            return list_DayOfWeek.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_phone;
            ImageView iv_selected, iv_unselected;
            LinearLayout layout_item, layout_select;

            public viewholder(View view) {
                super(view);
                iv_selected = view.findViewById(R.id.iv_selected);
                layout_item = view.findViewById(R.id.layout_item);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                tv_phone = view.findViewById(R.id.tv_phone);
                layout_select = view.findViewById(R.id.layout_select);
            }
        }
    }
    private void SetSelected () {
        String day_list = "";
        day_list_id="";
        for (int i = 0; i < list_DayOfWeek.size(); i++) {
            if (list_DayOfWeek.get(i).isPhoneSelect() == true) {
                if (day_list_id.equals("")) {
                    day_list_id = list_DayOfWeek.get(i).getNum();
                    day_list = list_DayOfWeek.get(i).getData().substring(0,3);
                } else {
                    day_list_id = day_list_id + "," + list_DayOfWeek.get(i).getNum();
                    day_list = day_list + ", " + list_DayOfWeek.get(i).getData().substring(0,3);
                }
            }
            tv_daylist.setText(day_list);
        }
    }

    class DayListAdepter extends RecyclerView.Adapter<DayListAdepter.viewholder> {

        public Context mCtx;
        List<Broadcste_Coman_Model> list_day;


        public DayListAdepter(Context applicationContext, List<Broadcste_Coman_Model> list_day ) {
            this.mCtx = applicationContext;
            this.list_day = list_day;
        }


        @NonNull
        @Override
        public DayListAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.broadcast_select_layout, parent, false);
            return new DayListAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DayListAdepter.viewholder holder, int position) {
            Broadcste_Coman_Model item = list_day.get(position);
            holder.tv_phone.setText(item.getNum());
            if (item.isPhoneSelect()) {
                tv_day.setText(item.getNum());
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }

            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < list_day.size(); i++) {
                        if (list_day.get(i).isPhoneSelect()) {
                            list_day.get(i).setPhoneSelect(false);
                            break;
                        }
                    }
                    item.setPhoneSelect(true);
                    tv_day.setText(item.getNum());
                  //  tv_info.setVisibility(View.GONE);
                    bottomSheetDialog_day.cancel();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list_day.size();
        }



        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_phone;
            ImageView iv_selected, iv_unselected;
            LinearLayout layout_item;

            public viewholder(View view) {
                super(view);
                iv_selected = view.findViewById(R.id.iv_selected);
                layout_item = view.findViewById(R.id.layout_item);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                tv_phone = view.findViewById(R.id.tv_phone);
            }
        }
    }

    class MonthDayListAdepter extends RecyclerView.Adapter<MonthDayListAdepter.viewholder> {

        public Context mCtx;
        List<Broadcste_Coman_Model> list_Day;


        public MonthDayListAdepter(Context applicationContext, List<Broadcste_Coman_Model> list_Day) {
            this.mCtx = applicationContext;
            this.list_Day = list_Day;
        }


        @NonNull
        @Override
        public MonthDayListAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.broadcast_select_layout, parent, false);
            return new MonthDayListAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MonthDayListAdepter.viewholder holder, int position) {
            Broadcste_Coman_Model item = list_Day.get(position);
            holder.tv_phone.setText(item.getNum());
            if (item.isPhoneSelect()) {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }

            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < list_Day.size(); i++) {
                        if (list_Day.get(i).isPhoneSelect()) {
                            list_Day.get(i).setPhoneSelect(false);
                            break;
                        }
                    }
                    item.setPhoneSelect(true);

                    holder.iv_selected.setVisibility(View.VISIBLE);
                    holder.iv_unselected.setVisibility(View.GONE);

                    tv_month.setText(item.getNum());
                  //  tv_info.setVisibility(View.GONE);
                    bottomSheetDialog_monthday.cancel();
                }
            });


        }

        @Override
        public int getItemCount() {
            return list_Day.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_phone;
            ImageView iv_selected, iv_unselected;
            LinearLayout layout_item;

            public viewholder(View view) {
                super(view);
                iv_selected = view.findViewById(R.id.iv_selected);
                layout_item = view.findViewById(R.id.layout_item);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                tv_phone = view.findViewById(R.id.tv_phone);
            }
        }
    }

    class SecondListAdepter extends RecyclerView.Adapter<SecondListAdepter.viewholder> {

        public Context mCtx;
        List<Broadcste_Coman_Model> list_Week_Of;


        public SecondListAdepter(Context applicationContext, List<Broadcste_Coman_Model> list_Week_Of) {
            this.mCtx = applicationContext;
            this.list_Week_Of = list_Week_Of;
        }


        @NonNull
        @Override
        public SecondListAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.broadcast_select_layout, parent, false);
            return new SecondListAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SecondListAdepter.viewholder holder, int position) {
            Broadcste_Coman_Model item = list_Week_Of.get(position);
            holder.tv_phone.setText(item.getData());
            if (item.isPhoneSelect()) {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }

            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < list_Week_Of.size(); i++) {
                        if (list_Week_Of.get(i).isPhoneSelect()) {
                            list_Week_Of.get(i).setPhoneSelect(false);
                            break;
                        }
                    }
                    item.setPhoneSelect(true);

                    holder.iv_selected.setVisibility(View.VISIBLE);
                    holder.iv_unselected.setVisibility(View.GONE);


                    tv_second.setText(item.getData());
                    //tv_info.setVisibility(View.GONE);
                    bottomSheetDialog_second.cancel();
                    second_id = item.getNum();

                }
            });


        }

        @Override
        public int getItemCount() {
            return list_Week_Of.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_phone;
            ImageView iv_selected, iv_unselected;
            LinearLayout layout_item;

            public viewholder(View view) {
                super(view);
                iv_selected = view.findViewById(R.id.iv_selected);
                layout_item = view.findViewById(R.id.layout_item);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                tv_phone = view.findViewById(R.id.tv_phone);
            }
        }
    }

    class Day_Second_ListAdepter extends RecyclerView.Adapter<Day_Second_ListAdepter.viewholder> {

        public Context mCtx;
        List<Broadcste_Coman_Model> list_DayOfWeek;


        public Day_Second_ListAdepter(Context applicationContext, List<Broadcste_Coman_Model> list_DayOfWeek) {
            this.mCtx = applicationContext;
            this.list_DayOfWeek = list_DayOfWeek;
        }


        @NonNull
        @Override
        public Day_Second_ListAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.broadcast_select_layout, parent, false);
            return new Day_Second_ListAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Day_Second_ListAdepter.viewholder holder, int position) {
            Broadcste_Coman_Model item = list_DayOfWeek.get(position);
            holder.tv_phone.setText(item.getData());
            if (item.isPhoneSelect()) {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }

            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < list_DayOfWeek.size(); i++) {
                        if (list_DayOfWeek.get(i).isPhoneSelect()) {
                            list_DayOfWeek.get(i).setPhoneSelect(false);
                            break;
                        }
                    }
                    item.setPhoneSelect(true);


                    holder.iv_selected.setVisibility(View.VISIBLE);
                    holder.iv_unselected.setVisibility(View.GONE);

                    tv_day_selction.setText(item.getData());
                  //  tv_info.setVisibility(View.GONE);
                    bottomSheetDialog_day_second.cancel();
                    day_section_id = item.getNum();
                }
            });

        }

        @Override
        public int getItemCount() {
            return list_DayOfWeek.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_phone;
            ImageView iv_selected, iv_unselected;
            LinearLayout layout_item;

            public viewholder(View view) {
                super(view);
                iv_selected = view.findViewById(R.id.iv_selected);
                layout_item = view.findViewById(R.id.layout_item);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                tv_phone = view.findViewById(R.id.tv_phone);
            }
        }
    }

}