package com.contactninja.Fragment.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contactninja.R;

import java.util.Calendar;
import java.util.Locale;

public class Broadcast_Fragment extends Fragment implements View.OnClickListener {
    Calendar calendar;
    CalendarView calendarView;
    ImageView prevMonth,nextMonth;
    TextView currentMonth;
    private Calendar _calendar;
    private int month, year;
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_affiliate__groth_, container, false);
        IntentUI(view);

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

    private void IntentUI(View view) {

        calendarView = view.findViewById(R.id.calendarView);
        prevMonth=view.findViewById(R.id.prevMonth);
        currentMonth=view.findViewById(R.id.currentMonth);
        nextMonth=view.findViewById(R.id.nextMonth);
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
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