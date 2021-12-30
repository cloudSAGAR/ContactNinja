package com.contactninja.Broadcast.Broadcast_Schedule;

import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.archit.calendardaterangepicker.customviews.CalendarListener;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Broadcast_date_select extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    ImageView iv_back, iv_Setting;
    TextView save_button;
    DateRangeCalendarView calendar;
    private static final String TAG = Broadcast_date_select.class.getSimpleName();

    private BroadcastReceiver mNetworkReceiver;
    ConstraintLayout mMainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_date_select);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        calendar.setCalendarListener(calendarListener);
        final Calendar startMonth = Calendar.getInstance();
        startMonth.set(2010, Calendar.DECEMBER, 30);
        final Calendar endMonth = (Calendar) startMonth.clone();
        endMonth.add(Calendar.MONTH, 5);




       // Log.d(TAG, "Start month: " + startMonth.getTime().toString() + " :: End month: " + endMonth.getTime().toString());
        calendar.setVisibleMonthRange(startMonth, endMonth);
        final Calendar startDateSelectable = (Calendar) startMonth.clone();
        startDateSelectable.add(Calendar.DATE, 20);
        final Calendar endDateSelectable = (Calendar) endMonth.clone();
        endDateSelectable.add(Calendar.DATE, -20);
        Log.e(TAG, "startDateSelectable: " + startDateSelectable.getTime().toString() + " :: endDateSelectable: " + endDateSelectable.getTime().toString());
        calendar.setSelectableDateRange(startDateSelectable, endDateSelectable);
        final Calendar startSelectedDate = (Calendar) startDateSelectable.clone();
        startSelectedDate.add(Calendar.DATE, 10);
        final Calendar endSelectedDate = (Calendar) endDateSelectable.clone();
        endSelectedDate.add(Calendar.DATE, -10);
        Log.e(TAG, "startSelectedDate: " + startSelectedDate.toString() + " :: endSelectedDate: " + endSelectedDate.toString());
        calendar.setSelectedDateRange(startSelectedDate, endSelectedDate);
        final Calendar current = (Calendar) startMonth.clone();
        current.add(Calendar.MONTH, 1);
        calendar.setCurrentMonth(current);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String formatedDate = sdf.format(startSelectedDate.getTime());
        Log.e("start Date",formatedDate);


        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String formatedDate1 = sdf1.format(endSelectedDate.getTime());
        Log.e("End Date",formatedDate1);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=getIntent();
                Bundle bundle=intent.getExtras();
                String type=bundle.getString("type","");
                Intent Time_Selcet_Activity=new Intent(getApplicationContext(),Broadcast_time_select.class);
                Time_Selcet_Activity.putExtra("start_date",formatedDate.toString());
                Time_Selcet_Activity.putExtra("end_date",formatedDate1.toString());
                Time_Selcet_Activity.putExtra("type",type);
                Time_Selcet_Activity.putExtra("repeat","");
                Time_Selcet_Activity.putExtra("num_day","");
                Time_Selcet_Activity.putExtra("m_day","");
                Time_Selcet_Activity.putExtra("m_month","");
                Time_Selcet_Activity.putExtra("m_first","");
                startActivity(Time_Selcet_Activity);
                //finish();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        calendar = findViewById(R.id.cdrvCalendar);
    }
    private final CalendarListener calendarListener = new CalendarListener() {
        @Override
        public void onFirstDateSelected(@NonNull final Calendar startDate) {
           // Toast.makeText(Broadcast_date_select.this, "Start Date: " + startDate.getTime().toString(), Toast.LENGTH_SHORT).show();
           /* Log.d(TAG, "Selected dates: Start: " + printDate(calendar.getStartDate()) +
                    " End:" + printDate(calendar.getEndDate()));*/
        }

        @Override
        public void onDateRangeSelected(@NonNull final Calendar startDate, @NonNull final Calendar endDate) {
          //  Toast.makeText(Broadcast_date_select.this, "Start Date: " + startDate.getTime().toString() + " End date: " + endDate.getTime().toString(), Toast.LENGTH_SHORT).show();
/*            Log.d(TAG, "Selected dates: Start: " + printDate(calendar.getStartDate()) +
                    " End:" + printDate(calendar.getEndDate()));*/
        }
    };
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Broadcast_date_select.this, mMainLayout);
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