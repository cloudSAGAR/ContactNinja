package com.contactninja.Utils;

import android.annotation.SuppressLint;
import android.app.Application;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint({"SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled", "Registered"})
public class Global_Time extends Application {
    static String yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
    public static SimpleDateFormat defoult_date_time_formate = new SimpleDateFormat(yyyy_MM_dd_hh_mm_ss);
    static String yyyy_MM_dd = "yyyy-MM-dd";
    public static SimpleDateFormat defoult_date_formate = new SimpleDateFormat(yyyy_MM_dd);
    static String dd_MMM_yyyy_hh_mm_ss = "dd-MMM-yyyy hh:mm:ss";
    public static SimpleDateFormat defoult_month_formate = new SimpleDateFormat(dd_MMM_yyyy_hh_mm_ss);
    static String dd_MMM_yyyy_hh_mmaaa = "dd-MMM-yyyy hh:mmaaa";
    static String dd_MMM_yyyy = "dd-MMM-yyyy";
    static String HH_mm_aaa = "HH:mm aaa";
    static String HH_mm_ss = "HH:mm:ss";
    static String hh_mm_a = "hh:mm a";
    private static Global_Time mInstance;
    
    public static String getCurrentTime() {
        String currentTime = new SimpleDateFormat(HH_mm_aaa, Locale.getDefault()).format(new Date());
        return currentTime;
    }
    
    public static String getCurrentDate() {
        DateFormat df = new SimpleDateFormat(yyyy_MM_dd);
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }
    
    public static String getCurrentTimeandDate() {
        DateFormat df = new SimpleDateFormat(yyyy_MM_dd_hh_mm_ss);
        String date = df.format(Calendar.getInstance().getTime());
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
        SimpleDateFormat output = new SimpleDateFormat(HH_mm_aaa);
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
        SimpleDateFormat displayFormat = new SimpleDateFormat(HH_mm_ss);
        SimpleDateFormat parseFormat = new SimpleDateFormat(hh_mm_a);
        Date date = parseFormat.parse(time);
        return displayFormat.format(date);
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}

