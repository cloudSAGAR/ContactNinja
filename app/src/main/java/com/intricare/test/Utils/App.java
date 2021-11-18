package com.intricare.test.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.intricare.test.R;

public class App extends MultiDexApplication {
    Activity activity;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        //set true when upload apk on playstore
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

    }

}
