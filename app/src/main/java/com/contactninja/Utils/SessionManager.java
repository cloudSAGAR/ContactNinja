package com.contactninja.Utils;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.contactninja.Auth.AppIntroActivity;
import com.contactninja.Auth.LoginActivity;
import com.contactninja.Contect.Contact;
import com.contactninja.MainActivity;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.BroadcastActivityListModel;
import com.contactninja.Model.BroadcastActivityModel;
import com.contactninja.Model.Broadcast_Data;
import com.contactninja.Model.Broadcate_save_data;
import com.contactninja.Model.Bzcard_Fields_Model;
import com.contactninja.Model.CampaignTask;
import com.contactninja.Model.CampaignTask_overview;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.ManualTaskModel;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UserLinkedList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("UnknownNullness")
public class SessionManager {
    public static final String KEY_User = "User";
    public static final String KEY_Token = "token";
    public static final String Plan_type = "plan_type";
    public static final String Login_type = "login_type";
    public static final String Sign_Model = "sign_model";
    public static final String Task_Model = "task_model";
    public static final String Contect_Name = "contect_name";
    public static final String Contect_Type = "contect_type";
    public static final String Add_Contect_Detail = "contect_detail";
    public static final String Fcm_Token = "fcm_token";
    public static final String GroupListData = "grouplistdata";
    public static final String UserLinkedGmailList = "userLinkedGmailList";
    public static final String Group_Model = "group_model";
    public static final String Contect_Model = "contect_model";
    public static final String Contect_flag = "contect_flag";
    public static final String Campign_flag = "campign_flag";
    public static final String Refresh_token = "refresh_token";
    public static final String Access_token = "access_token";
    public static final String csv_token = "csv_token";
    public static final String Contect_List = "contectlist";
    //public static final String Company_List = "company_List";
    public static final String brodcaste_Contect_List = "brod_contectlist";
    public static final String brodcaste_group = "brod_group";
    public static final String Broadcast_Data_save = " broadcast_data";
    public static final String Contectexits_token = "contect_token";
    public static final String campaign_type = "campaign_type";
    public static final String campaign_type_name = "campaign_type_name";
    public static final String campaign_day = "campaign_day";
    public static final String campaign_minute = "campaign_minute";
    public static final String Campaign_overview = "campign_overview";
    private static final String PREF_NAME = "jainaPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_APPITRO = "Isintro";
    private static final String IS_Email_Update = "IsEmailUpdate";
    private static final String IS_Payment_Type_Select = "Ispaymenttypeselect";
    public static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private final Context _context;
    private static final String ManualTaskmodel="ManualTaskmodel";
    private  static final String email_screen_name="email_screen_name";
    private static final String message_number="message_number";

    private static final String message_id="message_id";
    private static final String message_type="message_type";

    public static final String Broadcaste_save_data = "broadcaste_save_data";

    public static final String Broadcast_flag="broadcast_flag";
    public static final String Broadcaste_Detail="Broadcaste_detail";
    public static final String Broadcast_contect="Broadcast_contect";
    public static final String Bzcard="Bzcard";
    public static final String Add_newcontect="add_new_contect";
    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static String getCampaign_type(Context context) {

        String type = pref.getString(campaign_type, "");
        return type;

    }

    public static void setCampaign_type(String campaign_type1) {
        editor.putString(campaign_type, campaign_type1);
        editor.commit();
    }

    public static String getCampaign_type_name(Context context) {

        String type = pref.getString(campaign_type_name, "");
        return type;

    }

    public static void setCampaign_type_name(String campaign_type_name1) {
        editor.putString(campaign_type_name, campaign_type_name1);
        editor.commit();
    }


    public static String getEmail_screen_name(Context context) {

        String type = pref.getString(email_screen_name, "");
        return type;

    }

    public static void setEmail_screen_name(String email_screen_name1) {
        editor.putString(email_screen_name, email_screen_name1);
        editor.commit();
    }



    public static String getMessage_number(Context context) {

        String type = pref.getString(message_number, "");
        return type;

    }

    public static void setMessage_number(String email_screen_name1) {
        editor.putString(message_number, email_screen_name1);
        editor.commit();
    }



    public static String getMessage_id(Context context) {

        String type = pref.getString(message_id, "");
        return type;

    }

    public static void setMessage_id(String email_screen_name1) {
        editor.putString(message_id, email_screen_name1);
        editor.commit();
    }


    public static String getMessage_tyep(Context context) {

        String type = pref.getString(message_type, "");
        return type;

    }

    public static void setMessage_type(String email_screen_name1) {
        editor.putString(message_type, email_screen_name1);
        editor.commit();
    }




    public static String getcontectexits() {

        String type = pref.getString(Contectexits_token, "0");
        return type;

    }

    public static String getContect_flag(Context context) {

        String type = pref.getString(Contect_flag, "");
        return type;

    }



    public static void setContect_flag(String plantype) {
        editor.putString(Contect_flag, plantype);
        editor.commit();
    }



    public static String getBroadcast_flag(Context context) {

        String type = pref.getString(Broadcast_flag, "");
        return type;

    }



    public static void setBroadcast_flag(String flag) {
        editor.putString(Broadcast_flag, flag);
        editor.commit();
    }






    public static String getCampign_flag(Context context) {

        String type = pref.getString(Campign_flag, "");
        return type;

    }

    public static void setCampign_flag(String plantype) {
        editor.putString(Campign_flag, plantype);
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
        Log.e("Sessioin data", new Gson().toJson(signModel));
        editor.apply();
    }

    public static ContectListData.Contact getOneCotect_deatil(Context context) {
        Gson gson = new Gson();
        String json = pref.getString(Contect_Model, "");
        Type type = new TypeToken<ContectListData.Contact>() {
        }.getType();
        ContectListData.Contact contect = gson.fromJson(json, type);
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
        Grouplist.Group group = gson.fromJson(json, type);
        if (group == null) {
            group = new Grouplist.Group();
        }
        return group;
    }

    public static void setGroupData(Context context, Grouplist.Group group) {
        Gson gson = new Gson();
        String json = gson.toJson(group);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Group_Model, json);
        Log.e("Sessioin data", new Gson().toJson(group));
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
        Log.e("Sessioin data", new Gson().toJson(groupModel));
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

    public static void setAdd_Contect_Detail(Context context, AddcontectModel add_model) {
        Gson gson = new Gson();
        String json = gson.toJson(add_model);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Add_Contect_Detail, json);
        editor.apply();
    }

    public static List<ContectListData> getContectList(Context context) {

        Gson gson = new Gson();
        String json = pref.getString(Contect_List, null);
        Type type = new TypeToken<ArrayList<ContectListData>>() {
        }.getType();
        List<ContectListData> contectList = gson.fromJson(json, type);
        if (contectList == null) {
            contectList = new ArrayList<>();
        }
        return contectList;

    }

    public static void setContectList(Context context, List<ContectListData> groupModel) {
        Gson gson = new Gson();
        String json = gson.toJson(groupModel);
        editor.putString(Contect_List, json);
        Log.e("Sessioin data", new Gson().toJson(groupModel));
        editor.apply();
    }


    /*public static List<CompanyModel.Company> getCompanylist(Context context) {

        Gson gson = new Gson();
        String json = pref.getString(Company_List, null);
        Type type = new TypeToken<ArrayList<CompanyModel.Company>>() {
        }.getType();
        List<CompanyModel.Company> contectList = gson.fromJson(json, type);
        if (contectList == null) {
            contectList = new ArrayList<>();
        }
        return contectList;

    }

    public static void setCompanylist(Context context, List<CompanyModel.Company> companyModels) {
        Gson gson = new Gson();
        String json = gson.toJson(companyModels);
        editor.putString(Company_List, json);
        Log.e("companyModels data", new Gson().toJson(companyModels));
        editor.apply();
    }*/

    public static List<ContectListData.Contact> getContectList_broadcste(Context context) {

        Gson gson = new Gson();
        String json = pref.getString(brodcaste_Contect_List, null);
        Type type = new TypeToken<ArrayList<ContectListData.Contact>>() {
        }.getType();
        List<ContectListData.Contact> contectList = gson.fromJson(json, type);
        if (contectList == null) {
            contectList = new ArrayList<>();
        }
        return contectList;

    }

    public static void setContectList_broadcste(Context context, List<ContectListData.Contact> groupModel) {
        Gson gson = new Gson();
        String json = gson.toJson(groupModel);
        editor.putString(brodcaste_Contect_List, json);
        editor.apply();
    }

    public static List<Grouplist.Group> getgroup_broadcste(Context context) {

        Gson gson = new Gson();
        String json = pref.getString(brodcaste_group, null);
        Type type = new TypeToken<ArrayList<Grouplist.Group>>() {
        }.getType();
        List<Grouplist.Group> contectList = gson.fromJson(json, type);
        if (contectList == null) {
            contectList = new ArrayList<>();
        }
        return contectList;

    }

    public static void setgroup_broadcste(Context context, List<Grouplist.Group> groupModel) {
        Gson gson = new Gson();
        String json = gson.toJson(groupModel);
        editor.putString(brodcaste_group, json);
        editor.apply();
    }

    public static Broadcast_Data getAdd_Broadcast_Data(Context context) {
        Gson gson = new Gson();
        String json = pref.getString(Broadcast_Data_save, "");
        Type type = new TypeToken<Broadcast_Data>() {
        }.getType();
        Broadcast_Data signModel = gson.fromJson(json, type);
        if (signModel == null) {
            signModel = new Broadcast_Data();
        }
        return signModel;
    }

    public static void setAdd_Broadcast_Data(Broadcast_Data add_model) {
        Gson gson = new Gson();
        String json = gson.toJson(add_model);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Broadcast_Data_save, json);
        editor.apply();
    }

    public static void setTask(Context context, List<CampaignTask> groupModel) {
        Gson gson = new Gson();
        String json = gson.toJson(groupModel);
        editor.putString(Task_Model, json);
        editor.apply();
    }

    public static List<CampaignTask> getTask(Context context) {

        Gson gson = new Gson();
        String json = pref.getString(Task_Model, null);
        Type type = new TypeToken<ArrayList<CampaignTask>>() {
        }.getType();
        List<CampaignTask> contectList = gson.fromJson(json, type);
        if (contectList == null) {
            contectList = new ArrayList<>();
        }
        return contectList;

    }

 /*   public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_Token, pref.getString(KEY_Token, ""));
        return user;
    }*/

    public static String getCampaign_Day(Context context) {

        String type = pref.getString(campaign_day, "");
        return type;

    }

    public static void setCampaign_Day(String campaign_day1) {
        editor.putString(campaign_day, campaign_day1);
        editor.commit();
    }

    public static String getCampaign_minute(Context context) {

        String type = pref.getString(campaign_minute, "");
        return type;

    }

    public static void setCampaign_minute(String campaign_minte1) {
        editor.putString(campaign_minute, campaign_minte1);
        editor.commit();
    }

    public static CampaignTask_overview getCampaign_data(Context context) {
        Gson gson = new Gson();
        String json = pref.getString(Campaign_overview, "");
        Type type = new TypeToken<CampaignTask_overview>() {
        }.getType();
        CampaignTask_overview signModel = gson.fromJson(json, type);
        if (signModel == null) {
            signModel = new CampaignTask_overview();
        }
        return signModel;
    }

    public static void setCampaign_data(CampaignTask_overview add_model) {
        Gson gson = new Gson();
        String json = gson.toJson(add_model);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Campaign_overview, json);
        editor.apply();
    }






    public static ManualTaskModel getManualTaskModel(Context context) {
        Gson gson = new Gson();
        String json = pref.getString(ManualTaskmodel, "");
        Type type = new TypeToken<ManualTaskModel>() {
        }.getType();
        ManualTaskModel signModel = gson.fromJson(json, type);
        if (signModel == null) {
            signModel = new ManualTaskModel();
        }
        return signModel;
    }

    public static void setManualTaskModel(ManualTaskModel add_model) {
        Gson gson = new Gson();
        String json = gson.toJson(add_model);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ManualTaskmodel, json);
        editor.apply();
    }



    public void login() {
        editor.putBoolean(IS_LOGIN, false);
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

    public String getContect_Name(Context context) {

        String type = pref.getString(Contect_Name, "");
        return type;

    }

    public void setContect_Name(String plantype) {
        editor.putString(Contect_Name, plantype);
        editor.commit();
    }

    public String getRefresh_token() {

        String type = pref.getString(Refresh_token, "");
        return type;

    }

    public void setRefresh_token(String efresh_token) {
        editor.putString(Refresh_token, efresh_token);
        editor.commit();
    }

    public String getAccess_token() {

        String type = pref.getString(Access_token, "");
        return type;

    }

    public void setAccess_token(String access_token) {
        editor.putString(Access_token, access_token);
        editor.commit();
    }

    public void setcontectexits(String efresh_token) {
        editor.putString(Contectexits_token, efresh_token);
        editor.commit();
    }

    public boolean getCsv_token() {
        return pref.getBoolean(csv_token, false);
    }

    public void setCsv_token() {
        editor.putBoolean(csv_token, true);
        editor.commit();
    }

    public String getContect_Type(Context context) {

        String type = pref.getString(Contect_Type, "");
        return type;

    }

    public void setContect_Type(String plantype) {
        editor.putString(Contect_Type, plantype);
        editor.commit();
    }

    public static Broadcate_save_data getBroadcate_save_data(Context context) {
        Gson gson = new Gson();
        String json = pref.getString(Broadcaste_save_data, "");
        Type type = new TypeToken<Broadcate_save_data>() {
        }.getType();
        Broadcate_save_data signModel = gson.fromJson(json, type);
        if (signModel == null) {
            signModel = new Broadcate_save_data();
        }
        return signModel;
    }

    public static void setBroadcate_save_data(Context context, Broadcate_save_data add_model) {
        Gson gson = new Gson();
        String json = gson.toJson(add_model);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Broadcaste_save_data, json);
        editor.apply();
    }


    public static BroadcastActivityListModel.Broadcast getBroadcate_List_Detail(Context context) {
        Gson gson = new Gson();
        String json = pref.getString(Broadcaste_Detail, "");
        Type type = new TypeToken<BroadcastActivityListModel.Broadcast>() {
        }.getType();
        BroadcastActivityListModel.Broadcast signModel = gson.fromJson(json, type);
        if (signModel == null) {
            signModel = new BroadcastActivityListModel.Broadcast();
        }
        return signModel;
    }

    public static void setBroadcate_List_Detail(Context context, BroadcastActivityListModel.Broadcast add_model) {
        Gson gson = new Gson();
        String json = gson.toJson(add_model);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Broadcaste_Detail, json);
        editor.apply();
    }


    public static BZcardListModel.Bizcard getBzcard(Context context) {
        Gson gson = new Gson();
        String json = pref.getString(Bzcard, "");
        Type type = new TypeToken<BZcardListModel.Bizcard>() {
        }.getType();
        BZcardListModel.Bizcard signModel = gson.fromJson(json, type);
        if (signModel == null) {
            signModel = new BZcardListModel.Bizcard();
        }
        return signModel;
    }

    public static void setBzcard(Context context, BZcardListModel.Bizcard bzcard_model) {
        Gson gson = new Gson();
        String json = gson.toJson(bzcard_model);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Bzcard, json);
        editor.apply();
    }






/*    public static CampaignTask getTask(Context context) {
        Gson gson = new Gson();
        String json = pref.getString(Task_Model, "");
        Type type = new TypeToken<CampaignTask>() {
        }.getType();
        CampaignTask signModel = gson.fromJson(json, type);
        if (signModel == null) {
            signModel = new CampaignTask();
        }
        return signModel;
    }

    public static void setTask(Context context, CampaignTask signModel) {
        Gson gson = new Gson();
        String json = gson.toJson(signModel);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Task_Model, json);
        Log.e("Sessioin data",new Gson().toJson(signModel));
        editor.apply();
    }*/

    public String getFcm_Token(Context context) {

        String type = pref.getString(Fcm_Token, "");
        return type;

    }

    public void setFcm_Token(String fcm_token) {
        editor.putString(Fcm_Token, fcm_token);
        editor.commit();
    }

    public void checkLogin() {
        // Check login status
        if (isLoggedIn()) {
            if (isAppIntroIn()) {
                Intent i = new Intent(_context, AppIntroActivity.class);
                _context.startActivity(i);
            } else {
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
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, true);
    }

    public boolean isAppIntroIn() {
        return pref.getBoolean(IS_APPITRO, true);
    }

    public String getlogin_type(Context context) {

        String type = pref.getString(Login_type, "");
        return type;

    }

    public void setlogin_type(String plantype) {
        editor.putString(Login_type, plantype);
        editor.commit();
    }



    public static List<UserLinkedList.UserLinkedGmail> getUserLinkedGmail(Context context) {

        Gson gson = new Gson();
        String json = pref.getString(UserLinkedGmailList, null);
        Type type = new TypeToken<ArrayList<UserLinkedList.UserLinkedGmail>>() {
        }.getType();
        List<UserLinkedList.UserLinkedGmail> userLinkedGmailList = gson.fromJson(json, type);
        if (userLinkedGmailList == null) {
            userLinkedGmailList = new ArrayList<>();
        }
        return userLinkedGmailList;

    }

    public static void setUserLinkedGmail(Context context, List<UserLinkedList.UserLinkedGmail> userLinkedGmailList) {
        Gson gson = new Gson();
        String json = gson.toJson(userLinkedGmailList);
        editor.putString(UserLinkedGmailList, json);
        Log.e("Sessioin data", new Gson().toJson(userLinkedGmailList));
        editor.apply();
    }





    public static List<BroadcastActivityModel.BroadcastProspect> getBroadcast_Contect(Context context) {

        Gson gson = new Gson();
        String json = pref.getString(Broadcast_contect, null);
        Type type = new TypeToken<ArrayList<BroadcastActivityModel.BroadcastProspect>>() {
        }.getType();
        List<BroadcastActivityModel.BroadcastProspect> userLinkedGmailList = gson.fromJson(json, type);
        if (userLinkedGmailList == null) {
            userLinkedGmailList = new ArrayList<>();
        }
        return userLinkedGmailList;

    }

    public static void setBroadcast_Contect(Context context, List<BroadcastActivityModel.BroadcastProspect> userLinkedGmailList) {
        Gson gson = new Gson();
        String json = gson.toJson(userLinkedGmailList);
        editor.putString(Broadcast_contect, json);
        Log.e("Sessioin data", new Gson().toJson(userLinkedGmailList));
        editor.apply();
    }


    public static List<Contact> getnewContect(Context context) {

        Gson gson = new Gson();
        String json = pref.getString(Add_newcontect, null);
        Type type = new TypeToken<ArrayList<Contact>>() {
        }.getType();
        List<Contact> userLinkedGmailList = gson.fromJson(json, type);
        if (userLinkedGmailList == null) {
            userLinkedGmailList = new ArrayList<>();
        }
        return userLinkedGmailList;

    }

    public static void setnewContect(Context context, List<Contact> userLinkedGmailList) {
        Gson gson = new Gson();
        String json = gson.toJson(userLinkedGmailList);
        editor.putString(Add_newcontect, json);
        Log.e("Sessioin data", new Gson().toJson(userLinkedGmailList));
        editor.apply();
    }

}