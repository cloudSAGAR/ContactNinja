package com.intricare.test.Utils;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.intricare.test.Auth.LoginActivity;
import com.intricare.test.MainActivity;

import java.util.HashMap;

public class SessionManager {
    private static final String PREF_NAME = "ContactNinjaPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context _context;

    /*
     *
     * user login to get data
     * */
    public static final String KEY_User = "User";
    public static final String KEY_Token = "token";


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


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, MainActivity.class);
            _context.startActivity(i);
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

}