package com.contactninja.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.contactninja.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Global extends Application {
    public  static SimpleDateFormat defoult_date_time_formate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public  static SimpleDateFormat defoult_date_formate = new SimpleDateFormat("yyyy-MM-dd");
    public  static SimpleDateFormat defoult_month_formate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");

    public static final String Device = "APP_ANDR";
    private static final long MIN_CLICK_INTERVAL = 2000; //in millis
    public static String AppVersion = "";
    public static String about = "https://contactninja.us/about/";
    public static String Email_auth = "https://app.contactninja.org/email_api/callback.php";
    public static int count = 1;
    private static long lastClickTime = 0;
    private static Global mInstance;
    private static Snackbar snackbar;


    public static String imei=Build.MANUFACTURER+" "+Build.MODEL+" "+Build.VERSION.RELEASE;

    public static void openEmailAuth(Activity activity) {
        Uri uri = Uri.parse(Global.Email_auth); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    public static String getCurrentTime() {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        return currentTime;
    }

    public static String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }
    public static String getCurrentTimeandDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }
    public static String getVersionname(Activity activity) {
        String version = "";
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            version = pInfo.versionName;
            AppVersion = version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String setFirstLetter(String status) {
        String upperString="";
        if(Global.IsNotNull(status)||!status.equals("")){
            upperString = status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
        }
        return upperString;
    }

    public static synchronized Global getInstance() {
        return mInstance;
    }

    /*
     * any value not null or blank check
     * */
    public static boolean IsNotNull(Object object) {
        return object != null && !object.equals("null") && !object.equals("");
    }

    public static void bsck(Context context, View drawerLayout) {
        Snackbar snackbar;
        snackbar = Snackbar.make(drawerLayout, context.getString(R.string.back_button), 5000);
        snackbar.show();
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "[a-zA-Z0-9+._-]+@[a-z]+\\.+[a-z]+";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void list_Show_Hide(RecyclerView recyclerView, TextView textView, boolean isVisibalRecyclerView) {
        if (isVisibalRecyclerView) {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public static void View_Show_Hide(View view, boolean isVisibal) {
        if (isVisibal) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        if (Global.IsNotNull(activity.getCurrentFocus())) {
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void ShowKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        if (Global.IsNotNull(activity.getCurrentFocus())) {
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        }
    }

    @SuppressLint("NewApi")
    public static void Messageshow(Context context, View frameLayout, String message, boolean success) {
        Snackbar snackbar;
        View sbview;

        snackbar = Snackbar.make(frameLayout, message, 0);
        sbview = snackbar.getView();

        TextView textView = sbview.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        if (success) {
            sbview.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
        } else {
            sbview.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        }
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }

    public static CircularProgressDrawable setplaceholder(Context context) {
        CircularProgressDrawable circularProgressDrawable;
        circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(1f);
        circularProgressDrawable.setCenterRadius(15f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }

    public static boolean buttonOneclick() {
        boolean click;
        long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
            lastClickTime = currentTime;
            click = true;
        } else {
            click = false;
        }

        return click;
    }

    public static boolean isPasswordValidMethod(String password) {

        boolean isValid = false;

        // ^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$
        // ^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$

        String expression = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        CharSequence inputStr = password;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    public static void checkConnectivity(Activity activity, View mMainLayout) {
        boolean finalConnectivity = false;

        snackbar =
                Snackbar.make(
                        mMainLayout,
                        activity.getString(R.string.noInternet),
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(activity.getResources().getColor(R.color.red));

        snackbar.setAction(activity.getString(R.string.tryAgain), view -> {
            checkConnectivity(activity, mMainLayout);
        });
        if (ConnectivityReceiver.isConnected()) {
            snackbar.dismiss();
        } else {
            snackbar.show();
        }
    }

    public static boolean isNetworkAvailable(Activity context, View view) {
        boolean isAvailable;
        ConnectivityManager connectivityMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
        isAvailable = networkInfo != null && networkInfo.isConnected();
        if (isAvailable) {
            return true;
        } else {
            Global.checkConnectivity(context, view);
            return false;
        }
    }

    public static String getToken(SessionManager sessionManager) {
        String token = sessionManager.getAccess_token();
        Log.e("token", token);
        return token;
    }

    public static String formateChange(String dateTime) {
       Date oneWayTripDate=null;
       String tripDate="";
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        try {
            oneWayTripDate = input.parse(dateTime);                 // parse input
            tripDate= output.format(oneWayTripDate);    // format output
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tripDate;
    }

    public static int Countrycode_Country(Activity activity, String email_number) {
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(activity.TELEPHONY_SERVICE);
        String country = tm.getNetworkCountryIso();
        int countryCode = 0;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(activity);
        try {
            // phone must begin with '+'
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(email_number, country.toUpperCase());
            countryCode = numberProto.getCountryCode();
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        return countryCode;
    }
    public static int Countrycode(Activity activity, String email_number) {
        int countryCode = 0;

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(activity);
        try {
            // phone must begin with '+'
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(email_number, "");
            countryCode = numberProto.getCountryCode();
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        return countryCode;
    }

    /*
        public static String getcontectexits(SessionManager sessionManager){

            String token=sessionManager.getcontectexits();
            return token;
        }
    */
   /* public static String getDate(Integer time) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(Long.valueOf(time) * 1000);
        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy, hh:mm");
        String date1 = format1.format(date);

        return String.valueOf(date1);
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        mInstance = this;

    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
