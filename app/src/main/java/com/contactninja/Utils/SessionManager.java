package com.contactninja.Utils;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.contactninja.Auth.AppIntroActivity;
import com.contactninja.Auth.LoginActivity;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.GroupListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.SignModel;
import com.contactninja.Model.UserData.SignResponseModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.contactninja.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private static final String PREF_NAME = "jainaPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_APPITRO = "Isintro";
    public static   SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private final Context _context;

    public static final String KEY_User = "User";
    public static final String KEY_Token = "token";
    public static final String Plan_type ="plan_type";
    public static final String Login_type ="login_type";
    public static final String Sign_Model ="sign_model";
    public static final String Contect_Name="contect_name";
    public static final String Contect_Type="contect_type";
    public static final String Add_Contect_Detail="contect_detail";
    public static final String Fcm_Token="fcm_token";
    private static final String IS_Email_Update = "IsEmailUpdate";
    private static final String IS_Payment_Type_Select= "Ispaymenttypeselect";

    public static final String GroupListData ="grouplistdata";

    public static final String Group_Model ="group_model";

    public static final String Contect_Model ="contect_model";

    public static final String Contect_flag="contect_flag";

    public static final String Refresh_token="refresh_token";
    public static final String csv_token="csv_token";

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


    public void Payment_Type_Select() {
        editor.putBoolean(IS_Payment_Type_Select, true);
        editor.commit();
    }

    public void Email_Update() {
        editor.putBoolean(IS_Email_Update, true);
        editor.commit();
    }


    public boolean isEmail_Update() {
        return pref.getBoolean(IS_Email_Update, false);
    }

    public boolean isPayment_Type_Select() {
        return pref.getBoolean(IS_Payment_Type_Select, false);
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




    public  String getRefresh_token() {

        String type= pref.getString(Refresh_token, "");
        return  type;

    }

    public  void setRefresh_token(String efresh_token) {
        editor.putString(Refresh_token, efresh_token);
        editor.commit();
    }


    public  boolean getCsv_token() {
        return  pref.getBoolean(csv_token, false);
    }

    public  void setCsv_token() {
        editor.putBoolean(csv_token, true);
        editor.commit();
    }


    public  String getContect_flag(Context context) {

        String type= pref.getString(Contect_flag, "");
        return  type;

    }

    public static void setContect_flag(String plantype) {
        editor.putString(Contect_flag, plantype);
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





    public  String getFcm_Token(Context context) {

        String type= pref.getString(Fcm_Token, "");
        return  type;

    }

    public  void setFcm_Token(String fcm_token) {
        editor.putString(Fcm_Token, fcm_token);
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

 /*   public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_Token, pref.getString(KEY_Token, ""));
        return user;
    }*/



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

    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
    public boolean isAppIntroIn() {
        return pref.getBoolean(IS_APPITRO, false);
    }

    public  String getlogin_type(Context context) {

        String type= pref.getString(Login_type, "");
        return  type;

    }

    public  void setlogin_type(String plantype) {
        editor.putString(Login_type, plantype);
        editor.commit();
    }

    public static SignResponseModel getGetUserdata(Context context) {
        Gson gson = new Gson();
        String json = pref.getString(Sign_Model, "");
        Type type = new TypeToken<SignResponseModel>() {
        }.getType();
        SignResponseModel signModel = gson.fromJson(json, type);
        if (signModel == null) {
            signModel = new SignResponseModel();
        }
        return signModel;
    }

    public static void setUserdata(Context context, SignResponseModel signModel) {
        Gson gson = new Gson();
        String json = gson.toJson(signModel);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Sign_Model, json);
        Log.e("Sessioin data",new Gson().toJson(signModel));
        editor.apply();
    }










    public static ContectListData.Contact getOneCotect_deatil(Context context) {
        Gson gson = new Gson();
        String json = pref.getString(Contect_Model, "");
        Type type = new TypeToken<ContectListData.Contact>() {
        }.getType();
        ContectListData.Contact  contect= gson.fromJson(json, type);
        if (contect == null) {
            contect = new ContectListData.Contact();
        }
        return contect;
    }

    public static void setOneCotect_deatil(Context context, ContectListData.Contact contact) {
        Gson gson = new Gson();
        String json = gson.toJson(contact);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Contect_Model, json);
        /*Log.e("Sessioin data",new Gson().toJson(contact));*/
        editor.apply();
    }


    public static Grouplist.Group getGroupData(Context context) {
        Gson gson = new Gson();
        String json = pref.getString(Group_Model, "");
        Type type = new TypeToken<Grouplist.Group>() {
        }.getType();
        Grouplist.Group  group= gson.fromJson(json, type);
        if (group == null) {
            group = new Grouplist.Group();
        }
        return group;
    }

    public static void setGroupData(Context context,  Grouplist.Group group) {
        Gson gson = new Gson();
        String json = gson.toJson(group);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Group_Model, json);
        Log.e("Sessioin data",new Gson().toJson(group));
        editor.apply();
    }


    public static List<ContectListData.Contact> getGroupList(Context context) {

        Gson gson = new Gson();
        String json = pref.getString(GroupListData, null);
        Type type = new TypeToken<ArrayList<ContectListData.Contact>>() {
        }.getType();
        List<ContectListData.Contact> GroupList = gson.fromJson(json, type);
        if (GroupList == null) {
            GroupList = new ArrayList<>();
        }
        return GroupList;

    }

    public static void setGroupList(Context context, List<ContectListData.Contact> groupModel) {
        Gson gson = new Gson();
        String json = gson.toJson(groupModel);
        editor.putString(GroupListData, json);
        Log.e("Sessioin data",new Gson().toJson(groupModel));
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