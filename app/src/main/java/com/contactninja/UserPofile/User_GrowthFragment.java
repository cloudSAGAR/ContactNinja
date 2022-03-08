package com.contactninja.UserPofile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.contactninja.Model.UserData.AffiliateInfo;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.SessionManager;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.Calendar;
import java.util.Locale;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class User_GrowthFragment extends Fragment implements View.OnClickListener {
    TextView button_Affiliate_Report;
    ValueLineChart mBarChart;
    private long mLastClickTime = 0;
    Calendar calendar;
    CalendarView calendarView;
    ImageView prevMonth,nextMonth;
    TextView currentMonth;
    private Calendar _calendar;
    private int month, year;
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";
    public User_GrowthFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user_growth, container, false);
        IntentUI(view);
        setdata();
        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);

        setGridCellAdapterToDate(month, year);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 9);
        calendar.set(Calendar.YEAR, 2012);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.YEAR, 1);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String msg = "Selected date Day: " + i2 + " Month : " + (i1 + 1) + " Year " + i;
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void setdata() {
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());

      AffiliateInfo affiliateInfo = user_data.getUser().getAffiliateInfo();
        if(affiliateInfo.getLevel1().size()!=0||
                affiliateInfo.getLevel2().size()!=0||
                affiliateInfo.getLevel3().size()!=0||
                affiliateInfo.getLevel4().size()!=0||
                affiliateInfo.getLevel5().size()!=0){
            button_Affiliate_Report.setVisibility(View.VISIBLE);
        }else {
            button_Affiliate_Report.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void IntentUI(View view) {
        mBarChart = view.findViewById(R.id.cubiclinechart);
        mBarChart.setOutlineAmbientShadowColor(Color.YELLOW);
        mBarChart.startAnimation();
        loadData();
        calendarView = view.findViewById(R.id.calendarView);
        prevMonth=view.findViewById(R.id.prevMonth);
        currentMonth=view.findViewById(R.id.currentMonth);
        nextMonth=view.findViewById(R.id.nextMonth);
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
        button_Affiliate_Report=view.findViewById(R.id.button_Affiliate_Report);
        button_Affiliate_Report.setOnClickListener(this);

    }

    private void loadData() {


        ValueLineSeries series = new ValueLineSeries();
        series.setColor(Color.parseColor("#79D2DE"));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(3.2f));
        series.addPoint(new ValueLinePoint(2.6f));
        series.addPoint(new ValueLinePoint(5.0f));
        series.addPoint(new ValueLinePoint(3.5f));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(0.4f));
        series.addPoint(new ValueLinePoint(3.4f));
        series.addPoint(new ValueLinePoint(2.5f));
        series.addPoint(new ValueLinePoint(1.4f));
        series.addPoint(new ValueLinePoint(4.4f));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(3.2f));
        series.addPoint(new ValueLinePoint(2.6f));
        series.addPoint(new ValueLinePoint(5.0f));
        series.addPoint(new ValueLinePoint(3.5f));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(0.4f));
        series.addPoint(new ValueLinePoint(3.4f));
        series.addPoint(new ValueLinePoint(2.5f));
        series.addPoint(new ValueLinePoint(1.0f));
        series.addPoint(new ValueLinePoint(4.4f));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(3.2f));
        series.addPoint(new ValueLinePoint(2.6f));
        series.addPoint(new ValueLinePoint(5.0f));
        series.addPoint(new ValueLinePoint(3.5f));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(0.4f));
        series.addPoint(new ValueLinePoint(3.4f));
        series.addPoint(new ValueLinePoint(2.5f));
        series.addPoint(new ValueLinePoint(1.0f));
        series.addPoint(new ValueLinePoint(4.2f));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(3.6f));
        series.addPoint(new ValueLinePoint(1.0f));
        series.addPoint(new ValueLinePoint(2.5f));
        series.addPoint(new ValueLinePoint(2.0f));
        series.addPoint(new ValueLinePoint(1.4f));
        mBarChart.addSeries(series);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_Affiliate_Report:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(getActivity(),Affiliate_Report_LavelActivity.class));
                break;
            case R.id.prevMonth:
                if (month <= 1)
                {
                    month = 12;
                    year--;
                }
                else
                {
                    month--;
                }
                setGridCellAdapterToDate(month, year);
                break;
            case R.id.nextMonth:
                if (month > 11)
                {
                    month = 1;
                    year++;
                }
                else
                {
                    month++;
                }
                setGridCellAdapterToDate(month, year);
                break;
        }
    }



    private void setGridCellAdapterToDate(int month, int year)
    {
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
    }
}