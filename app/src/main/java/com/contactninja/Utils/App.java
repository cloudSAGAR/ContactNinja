package com.contactninja.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;

public class App extends MultiDexApplication {
    Activity activity;
    public static boolean isFirstTime = true;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        //set true when upload apk on playstore
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }

}
