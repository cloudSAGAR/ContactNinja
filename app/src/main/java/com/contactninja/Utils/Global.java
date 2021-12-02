package com.contactninja.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.TextUtils;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint({"SimpleDateFormat", "StaticFieldLeak"})
public class Global extends Application   {
    private static final long MIN_CLICK_INTERVAL = 2000; //in millis
    private static long lastClickTime = 0;
    private static Global mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        mInstance = this;

    }
    public static synchronized Global getInstance () {
        return mInstance;
    }
    /*
     * any value not null or blank check
     * */
    public static boolean IsNotNull (Object object) {
        return object != null && !object.equals("null") && !object.equals("");
    }

    public static void bsck (Context context, View drawerLayout) {
        Snackbar snackbar;
        snackbar = Snackbar.make(drawerLayout, context.getString(R.string.back_button), 5000);
        snackbar.show();
    }



    public static boolean emailValidator (String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }



    public static void list_Show_Hide (RecyclerView recyclerView, TextView textView, boolean isVisibalRecyclerView) {
        if (isVisibalRecyclerView) {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public static void View_Show_Hide (View view, boolean isVisibal) {
        if (isVisibal) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    public static void hideKeyboard (Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        if (Global.IsNotNull(activity.getCurrentFocus())) {
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    @SuppressLint("NewApi")
    public static void Messageshow (Context context, View frameLayout, String message, boolean success) {
        Snackbar snackbar;
        View sbview;

        snackbar = Snackbar.make(frameLayout, message, 5000);
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


    public static CircularProgressDrawable setplaceholder (Context context) {
        CircularProgressDrawable circularProgressDrawable;
        circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }




    public static boolean buttonOneclick () {
        boolean click;
        long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
            lastClickTime = currentTime;
           click=true;
        }else {
            click = false;
        }

    return click;
    }

    public static boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
    public static void checkConnectivity (Activity activity, View mMainLayout) {
        boolean Connectivity = false;
        Snackbar snackbar;
        snackbar =
                Snackbar.make(
                        mMainLayout,
                        activity.getString(R.string.noInternet),
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(activity.getResources().getColor(R.color.red));

        boolean finalConnectivity = Connectivity;
        snackbar.setAction(activity.getString(R.string.tryAgain), view -> {
            if (finalConnectivity) {
                snackbar.dismiss();
            } else {
                checkConnectivity(activity,mMainLayout);
            }
        });
        if (ConnectivityReceiver.isConnected()) {
            Connectivity = true;
            snackbar.dismiss();
        } else {
            snackbar.show();
        }
    }


    public static String getToken(Activity activity){

        SessionManager sessionManager=new SessionManager(activity);
        String token=sessionManager.getGetUserdata(activity).getTokenType()+" "+sessionManager.getGetUserdata(activity).getAccessToken();
        return token;
    }


}
