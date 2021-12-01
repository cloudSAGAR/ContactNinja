package com.contactninja.Utils;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.contactninja.Auth.AppIntroActivity;
import com.contactninja.Auth.LoginActivity;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.SignModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.contactninja.MainActivity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class SessionManager {
    private static final String PREF_NAME = "jainaPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_APPITRO = "Isintro";
    public static   SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context _context;

    public static final String KEY_User = "User";
    public static final String KEY_Token = "token";
    public static final String Plan_type ="plan_type";
    public static final String Login_type ="login_type";
    public static final String Sign_Model ="sign_model";
    public static final String Contect_Name="contect_name";
    public static final String Contect_Type="contect_type";

    public static final String Add_Contect_Detail="contect_detail";
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


    public  String getContect_Name(Context context) {

        String type= pref.getString(Contect_Name, "");
        return  type;

    }

    public  void setContect_Name(String plantype) {
        editor.putString(Contect_Name, plantype);
        editor.commit();
    }



    public  String getContect_Type(Context context) {

        String type= pref.getString(Contect_Type, "");
        return  type;

    }

    public  void setContect_Type(String plantype) {
        editor.putString(Contect_Type, plantype);
        editor.commit();
    }


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
        Intent i = new Intent(_context, LoginActivity.class);
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

    public static AddcontectModel getAdd_Contect_Detail(Context context) {
        Gson gson = new Gson();
        String json = pref.getString(Add_Contect_Detail, "");
        Type type = new TypeToken<AddcontectModel>() {
        }.getType();
        AddcontectModel signModel = gson.fromJson(json, type);
        if (signModel == null) {
            signModel = new AddcontectModel();
        }
        return signModel;
    }

    public static void setAdd_Contect_Detail(Context context,AddcontectModel add_model) {
        Gson gson = new Gson();
        String json = gson.toJson(add_model);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Add_Contect_Detail, json);
        editor.apply();
    }


}