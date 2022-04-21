package com.contactninja.Utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.contactninja.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint({"SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled", "Registered"})
public class Global_Time extends Application {
    static String yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
    static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static SimpleDateFormat defoult_date_time_formate = new SimpleDateFormat(yyyy_MM_dd_hh_mm_ss);
    static String yyyy_MM_dd = "yyyy-MM-dd";
    public static SimpleDateFormat defoult_date_formate = new SimpleDateFormat(yyyy_MM_dd);
    static String dd_MMM_yyyy_hh_mm_ss = "dd-MMM-yyyy hh:mm:ss";
    public static SimpleDateFormat defoult_month_formate = new SimpleDateFormat(dd_MMM_yyyy_hh_mm_ss);
    static String dd_MMM_yyyy_hh_mmaaa = "dd-MMM-yyyy hh:mmaaa";
    static String dd_MMM_yyyy = "dd-MMM-yyyy";
    static String HH_mm_ss = "HH:mm:ss";
    static String HH_mm = "HH:mm";
    static String HH = "HH";
    static String mm = "mm";
    static String hh_mm_a = "hh:mm a";
    private static Global_Time mInstance;
    
    public static String getCurrentTime() {
        String currentTime = new SimpleDateFormat(hh_mm_a, Locale.getDefault()).format(new Date());
        return currentTime;
    }
    
    public static String getCurrentDate() {
        DateFormat df = new SimpleDateFormat(yyyy_MM_dd);
        String date = df.format(Calendar.getInstance().getTime());
        if(date==null){
            date="";
        }
        return date;
    }
    
    public static String getCurrentTimeandDate_24() {
        DateFormat df = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
        String date = df.format(Calendar.getInstance().getTime());
        if(date==null){
            date="";
        }
        return date;
    }
    
    
    public static synchronized Global_Time getInstance() {
        return mInstance;
    }
    
    
    public static String formateChange(String dateTime) {
        Date oneWayTripDate = null;
        String tripDate = "";
        SimpleDateFormat input = new SimpleDateFormat(yyyy_MM_dd_hh_mm_ss);
        SimpleDateFormat output = new SimpleDateFormat(dd_MMM_yyyy_hh_mmaaa);
        try {
            oneWayTripDate = input.parse(dateTime);                 // parse input
            tripDate = output.format(oneWayTripDate);    // format output
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tripDate;
    }
    
    
    public static String DateFormateMonth(String dateTime) {
        Date oneWayTripDate = null;
        String tripDate = "";
        SimpleDateFormat input = new SimpleDateFormat(yyyy_MM_dd);
        SimpleDateFormat output = new SimpleDateFormat(dd_MMM_yyyy);
        try {
            oneWayTripDate = input.parse(dateTime);                 // parse input
            tripDate = output.format(oneWayTripDate);    // format output
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tripDate;
    }
    
    public static String TimeFormateAMPM(String Time) {
        Date oneWayTripDate = null;
        String tripDate = "";
        SimpleDateFormat input = new SimpleDateFormat(HH_mm_ss);
        SimpleDateFormat output = new SimpleDateFormat(hh_mm_a);
        try {
            oneWayTripDate = input.parse(Time);                 // parse input
            tripDate = output.format(oneWayTripDate);    // format output
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tripDate;
    }
    
    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = yyyy_MM_dd_hh_mm_ss;
        String outputPattern = dd_MMM_yyyy;
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        
        Date date = null;
        String str = "";
        
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    
    public static String parsetime(String time) {
        String inputPattern = yyyy_MM_dd_hh_mm_ss;
        String outputPattern = hh_mm_a;
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        
        Date date = null;
        String str = "";
        
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    
    public static String time_12_to_24(String time) throws Exception {
        SimpleDateFormat displayFormat = new SimpleDateFormat(HH_mm);
        SimpleDateFormat parseFormat = new SimpleDateFormat(hh_mm_a);
        Date date = parseFormat.parse(time);
        return displayFormat.format(date);
    }
    
    public static boolean checkTime_isvalid(Context context, String select_time, String select_date) throws Exception {
        boolean check = false;
        
        if (Global_Time.DateFormateMonth(Global_Time.getCurrentDate()).equals(
               select_date
        )) {
            SimpleDateFormat displayFormat = new SimpleDateFormat(HH_mm);
            SimpleDateFormat parseFormat = new SimpleDateFormat(hh_mm_a);
            Date date = parseFormat.parse(select_time);
            String time_24=displayFormat.format(date);
            String[] separated = time_24.split(":");
            int h= Integer.parseInt(separated[0]);
            int m= Integer.parseInt(separated[1]);
            
            Calendar datetime = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, h);
            datetime.set(Calendar.MINUTE, m);
            if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                check = true;
            } else {
                check = false;
                Toast.makeText(context, context.getResources().getString(R.string.Invalid_Time), Toast.LENGTH_LONG).show();
            }
        } else {
            check = true;
        }
    
        return check;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}

