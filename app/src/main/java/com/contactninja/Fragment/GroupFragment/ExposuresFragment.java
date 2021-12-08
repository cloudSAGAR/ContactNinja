package com.contactninja.Fragment.GroupFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.contactninja.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class ExposuresFragment extends Fragment implements View.OnClickListener {


    ImageView icon_left, ic_right;
    TextView tv_date;
    Calendar calendar = Calendar.getInstance();
    Calendar calendarMy = Calendar.getInstance();
    int month;
    int year;
    int current_month;
    int current_year;
    LinearLayout layout_right, layout_left;
    String[] months;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_exposures2, container, false);
        IntentUI(view);

        months = new DateFormatSymbols().getMonths();
        for (String month : months) {
            Log.e("month = ", month);
        }

        Calendar c = Calendar.getInstance();
        String month1 = months[c.get(Calendar.MONTH)];
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        current_month = month;
        current_year = year;
        int date1 = c.get(Calendar.DATE);
        tv_date.setText(" " + month1 + " " + "" + year);
        layout_right.setOnClickListener(this);
        layout_left.setOnClickListener(this);
        return view;
    }

    public void disableButton() {

        // condition for checking first month of last year
        if (calendar.get(Calendar.YEAR) != (calendarMy.get(Calendar.YEAR)) && calendarMy.get(Calendar.MONTH) == 0) {
            //disable left button
            layout_right.setOnClickListener(null);

            //  Log.e("Left Key","Yes");
        }
        // condition for checking current month
        else if (calendarMy.get(Calendar.YEAR) == (calendarMy.get(Calendar.YEAR)) && calendar.get(Calendar.MONTH) == calendarMy.get(Calendar.MONTH)) {
            // layout_left.setOnClickListener(null);
            //disable right button
            // Log.e("Right Key","Yes");
        } else {
            //Log.e("Enable Key","Yes");
            // enable both button
            layout_left.setOnClickListener(this);
            layout_right.setOnClickListener(this);
        }


    }

    private void IntentUI(View content_view) {
        icon_left = content_view.findViewById(R.id.icon_left);
        tv_date = content_view.findViewById(R.id.tv_date);
        ic_right = content_view.findViewById(R.id.ic_right);
        layout_right = content_view.findViewById(R.id.layout_right);
        layout_left = content_view.findViewById(R.id.layout_left);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_left:
                if (month <= 1) {//checking the month
                    month = 12;
                    year--;
                } else {
                    month--;
                }

                //update current time
                calendarMy.set(Calendar.MONTH, month);
                calendarMy.set(Calendar.YEAR, year);
                for (int i = 0; i < months.length; i++) {
                    if (i == month) {
                        tv_date.setText(" " + months[i] + " " + year + " ");
                        Log.e("Data left", " " + months[i] + " " + year + " ");
                    }
                }
                disableButton();
                break;
            case R.id.layout_right:

                Log.e("If print", "Yes");
                if (month > 11) {//checking the month
                    month = 1;
                    year++;
                } else {
                    month++;
                }

                //update current time
                calendarMy.set(Calendar.MONTH, month);
                calendarMy.set(Calendar.YEAR, year);
                for (int i = 0; i < months.length; i++) {
                    if (i == month) {
                        Log.e("Data right", " " + months[i] + " " + year + " ");
                        tv_date.setText(" " + months[i] + " " + year + " ");
                    }
                }

                disableButton();
                break;
        }

    }
}