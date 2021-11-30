package com.contactninja.Utils;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.contactninja.Auth.AppIntroActivity;
import com.contactninja.Auth.LoginActivity;
import com.contactninja.Model.SignModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.contactninja.MainActivity;

import java.lang.reflect.Type;
import java.util.HashMap;

public class SessionManager {
    private static final String PREF_NAME = "jainaPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_APPITRO = "Isintro";
    public static   SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context _context;

    /*
     *
     * user login to get data
     * */
    public static final String KEY_User = "User";
    public static final String KEY_Token = "token";
    public static final String Plan_type ="plan_type";
    public static final String Login_type ="login_type";
    public static final String Sign_Model ="sign_model";


    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void login() {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }
    public void appIntro() {
        editor.putBoolean(IS_APPITRO, false);
        editor.commit();
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
          if (this.isAppIntroIn()) {
              Intent i = new Intent(_context, AppIntroActivity.class);
              _context.startActivity(i);
           }else {
              Intent i = new Intent(_context, LoginActivity.class);
              _context.startActivity(i);
          }

        } else {
            Intent i = new Intent(_context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_Token, pref.getString(KEY_Token, ""));
        return user;
    }


    /**
     * Clear session details
     */
    public void logoutUser() {
        NotificationManager notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
    public boolean isAppIntroIn() {
        return pref.getBoolean(IS_APPITRO, true);
    }

    public  String getlogin_type(Context context) {

        String type= pref.getString(Login_type, "");
        return  type;

    }

    public  void setlogin_type(String plantype) {
        editor.putString(Login_type, plantype);
        editor.commit();
    }

    public static SignModel getGetUserdata(Context context) {
        Gson gson = new Gson();
        String json = pref.getString(Sign_Model, "");
        Type type = new TypeToken<SignModel>() {
        }.getType();
        SignModel signModel = gson.fromJson(json, type);
        if (signModel == null) {
            signModel = new SignModel();
        }
        return signModel;
    }

    public static void setUserdata(Context context, SignModel signModel) {
        Gson gson = new Gson();
        String json = gson.toJson(signModel);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Sign_Model, json);
        editor.apply();
    }

}