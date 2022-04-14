package com.contactninja.Manual_email_text;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.contactninja.Interface.TemplateClick;
import com.contactninja.Interface.TextClick;
import com.contactninja.MainActivity;
import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.ContecModel;
import com.contactninja.Model.HastagList;
import com.contactninja.Model.TemplateList;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UservalidateModel;
import com.contactninja.Model.ZoomExists;
import com.contactninja.R;
import com.contactninja.Setting.Verification_web;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.Global_Time;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Manual_Text_Send_Activty extends AppCompatActivity implements View.OnClickListener, TextClick,
                                                                                   TemplateClick, ConnectivityReceiver.ConnectivityReceiverListener {
    public static final int PICKFILE_RESULT_CODE = 1;
    static EditText edit_template, ev_task_title, ev_from;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    RelativeLayout mMainLayout;
    List<BZcardListModel.Bizcard> bizcardList = new ArrayList<>();
    CardListAdepter cardListAdepter;
    ImageView iv_back;
    TextView save_button, tv_use_tamplet;
    TemplateAdepter templateAdepter;
    ImageView iv_submit;
    RecyclerView rv_direct_list;
    PicUpTextAdepter picUpTextAdepter;
    List<HastagList.TemplateText> templateTextList = new ArrayList<>();
    List<TemplateList.Template> templateList = new ArrayList<>();
    String filePath;
    BottomSheetDialog bottomSheetDialog_templateList;
    TemplateClick templateClick;
    String p_number = "", id = "", task_name = "", from_ac = "", from_ac_id = "";
    BottomSheetDialog bottomSheetDialog_templateList1;
    ImageView iv_more;
    int defult_id, temaplet_id = 0;
    List<ContecModel.PhoneDatum> select_userLinkedGmailList = new ArrayList<>();
    List<ContecModel.PhoneDatum> userLinkedGmailList = new ArrayList<>();
    ImageView iv_down;
    boolean zoom_flag = false;
    int m_hour = 0;
    int m_minute = 0;
    private int amountOfItemsSelected = 0;
    private BroadcastReceiver mNetworkReceiver;
    private long mLastClickTime = 0;
    private int mYear, mMonth, mDay, mHour, mMinute;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_sms_send_activty);
        
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        templateClick = this;
        
        IntentUI();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        p_number = bundle.getString("number");
        id = String.valueOf(bundle.getInt("id"));
        String type = bundle.getString("type");
        task_name = bundle.getString("task_name");
        ev_task_title.setText(task_name);
        ev_from.setEnabled(false);
        
        try {
            Zoom_Api_check_zoom_account(getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (Global.isNetworkAvailable(Manual_Text_Send_Activty.this, mMainLayout)) {
                Hastag_list();
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        try {
            if (Global.isNetworkAvailable(Manual_Text_Send_Activty.this, mMainLayout)) {
                Contect_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (Global.isNetworkAvailable(Manual_Text_Send_Activty.this, MainActivity.mMainLayout)) {
                BZCard_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    private void IntentUI() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.GONE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText(getResources().getString(R.string.Next));
        tv_use_tamplet = findViewById(R.id.tv_use_tamplet);
        tv_use_tamplet.setOnClickListener(this);
        rv_direct_list = findViewById(R.id.rv_direct_list);
        mMainLayout = findViewById(R.id.mMainLayout);
        edit_template = findViewById(R.id.ev_txt);
        edit_template.requestFocus();
        iv_submit = findViewById(R.id.iv_submit);
        iv_submit.setOnClickListener(this);
        ev_task_title = findViewById(R.id.ev_task_title);
        ev_from = findViewById(R.id.ev_from);
        iv_down = findViewById(R.id.iv_down);
        iv_down.setOnClickListener(this);
        
        
    }
    
    private void Contect_list() throws JSONException {
        
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Manual_Text_Send_Activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("q", "");
        paramObject.addProperty("status", "");
        paramObject.addProperty("orderBy", "");
        paramObject.addProperty("order", "");
        paramObject.addProperty("perPage", "10");
        paramObject.addProperty("page", "1");
        obj.add("data", paramObject);
        retrofitCalls.Contect_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Manual_Text_Send_Activty.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    userLinkedGmailList.clear();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ContecModel>() {
                    }.getType();
                    ContecModel userLinkedGmail = new Gson().fromJson(headerString, listType);
                    userLinkedGmailList = userLinkedGmail.getPhoneData();
                    
                    ContecModel.PhoneDatum phoneDatum = new ContecModel.PhoneDatum();
                    phoneDatum.setId(0);
                    phoneDatum.setIsDefault(1);
                    phoneDatum.setPhoneNumber("System Assigned");
                    userLinkedGmailList.add(userLinkedGmailList.size(), phoneDatum);
                    Collections.reverse(userLinkedGmailList);
                    Log.e("Size is", "" + new Gson().toJson(userLinkedGmailList));
                    if (userLinkedGmailList.size() == 1) {
                        iv_down.setVisibility(View.GONE);
                    } else if (userLinkedGmailList.size() == 1) {
                        iv_down.setVisibility(View.GONE);
                    } else {
                        iv_down.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < userLinkedGmailList.size(); i++) {
                        if (userLinkedGmailList.get(i).getIsDefault().toString().equals("1")) {
                            ev_from.setText(userLinkedGmailList.get(i).getPhoneNumber());
                            defult_id = userLinkedGmailList.get(i).getId();
                            select_userLinkedGmailList.add(userLinkedGmailList.get(i));
                            from_ac = "USERSMS";
                            from_ac_id = String.valueOf(userLinkedGmailList.get(i).getId());
                            
                            
                        }
                    }
                    Log.e("List Is", new Gson().toJson(userLinkedGmailList));
                } else {
                    ContecModel.PhoneDatum phoneDatum = new ContecModel.PhoneDatum();
                    phoneDatum.setId(0);
                    phoneDatum.setIsDefault(1);
                    phoneDatum.setPhoneNumber("System Assigned");
                    userLinkedGmailList.add(userLinkedGmailList.size(), phoneDatum);
                    
                    ev_from.setText(userLinkedGmailList.get(0).getPhoneNumber());
                    defult_id = userLinkedGmailList.get(0).getId();
                    select_userLinkedGmailList.add(userLinkedGmailList.get(0));
                    from_ac = "USERSMS";
                    from_ac_id = String.valueOf(userLinkedGmailList.get(0).getId());
                    
                    //Global.openEmailAuth(from_ac.this);
                    // startActivity(new Intent(getApplicationContext(), Email_verification.class));
                }
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
        
        
    }
    
    private void Hastag_list() throws JSONException {
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Manual_Text_Send_Activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Hastag_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Manual_Text_Send_Activty.this), Global.Device, new RetrofitCallback() {
            @SuppressLint("SyntheticAccessor")
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    templateTextList.clear();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<HastagList>() {
                    }.getType();
                    HastagList hastagList = new Gson().fromJson(headerString, listType);
                    templateTextList = hastagList.getHashtag();


                /*    HastagList.TemplateText text = new HastagList.TemplateText();
                    text.setFile(R.drawable.ic_a);
                    text.setSelect(false);
                    templateTextList.add(0, text);

                    HastagList.TemplateText text1 = new HastagList.TemplateText();
                    text1.setFile(R.drawable.ic_file);
                    text1.setSelect(false);
                    templateTextList.add(1, text1);*/
                    
                    
                    HastagList.TemplateText text1 = new HastagList.TemplateText();
                    text1.setFile(R.drawable.ic_card_blank);
                    text1.setSelect(false);
                    templateTextList.add(0, text1);
                    
                    
                    HastagList.TemplateText text2 = new HastagList.TemplateText();
                    if (zoom_flag == true) {
                        text2.setFile(R.drawable.ic_video);
                    } else {
                        text2.setFile(R.drawable.ic_video_5);
                    }
                    
                    text2.setSelect(false);
                    templateTextList.add(1, text2);
                    
                    
                    HastagList.TemplateText templateText = new HastagList.TemplateText();
                    templateText.setDescription("Placeholders #");
                    templateText.setSelect(true);
                    templateTextList.add(2, templateText);
                    
                    Listset(templateTextList);
                    
                    //   sessionManager.setUserdata(getApplicationContext(),data);
                }
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
            
            
        });
        
        
    }
    
    private void Listset(List<HastagList.TemplateText> templateTextList) {
        rv_direct_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_direct_list.setHasFixedSize(true);
        picUpTextAdepter = new PicUpTextAdepter(Manual_Text_Send_Activty.this, templateTextList, this);
        rv_direct_list.setAdapter(picUpTextAdepter);
    }
    
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Manual_Text_Send_Activty.this, mMainLayout);
    }
    
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                showAlertDialogButtonClicked();
                break;
            case R.id.iv_submit:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                //  Log.e("Text is",edit_template.getText().toString());
                if (edit_template.getText().toString().equals("")) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.add_message), false);
                } else {
                    if (SessionManager.getEmail_screen_name(getApplicationContext()).equals("only_sms")) {
                       /* try {
                            SMSAPI(edit_template.getText().toString(), Integer.parseInt(id), p_number);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
*/
                        
                        broadcast_manu();
                    } else {
                        broadcast_manu();
                    }
                    
                }
                
                break;
            case R.id.tv_use_tamplet:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                bouttomSheet();
                break;
            case R.id.iv_down:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Email_bouttomSheet();
                break;
            
            
        }
    }
    
    private void Email_bouttomSheet() {
        final View mView = getLayoutInflater().inflate(R.layout.email_bottom_sheet, null);
        bottomSheetDialog_templateList1 = new BottomSheetDialog(Manual_Text_Send_Activty.this, R.style.CoffeeDialog);
        bottomSheetDialog_templateList1.setContentView(mView);
        TextView tv_done = bottomSheetDialog_templateList1.findViewById(R.id.tv_done);
        TextView tv_txt = bottomSheetDialog_templateList1.findViewById(R.id.tv_txt);
        tv_txt.setText(getResources().getString(R.string.please_select_sender));
        RecyclerView email_list = bottomSheetDialog_templateList1.findViewById(R.id.email_list);
        
        
        for (int i = 0; i < userLinkedGmailList.size(); i++) {
            if (userLinkedGmailList.get(i).getIsDefault() == 1) {
                select_userLinkedGmailList.clear();
                userLinkedGmailList.get(i).setEmailSelect(true);
                select_userLinkedGmailList.add(userLinkedGmailList.get(i));
                break;
            }
        }
        
        email_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        EmailListAdepter emailListAdepter = new EmailListAdepter(getApplicationContext(), userLinkedGmailList);
        email_list.setAdapter(emailListAdepter);
        email_list.setVisibility(View.VISIBLE);
        
        
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog_templateList1.cancel();
                if (select_userLinkedGmailList.size() != 0) {
                    from_ac = "USERSMS";
                    from_ac_id = String.valueOf(select_userLinkedGmailList.get(0).getId());
                    ev_from.setText(select_userLinkedGmailList.get(0).getPhoneNumber());
                }
            }
        });
        
        bottomSheetDialog_templateList1.show();
    }
    
    public void showAlertDialogButtonClicked() {
        
        // Create an alert builder
        androidx.appcompat.app.AlertDialog.Builder builder
                = new androidx.appcompat.app.AlertDialog.Builder(this, R.style.MyDialogStyle);
        
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.logout_dialog, null);
        builder.setView(customLayout);
        androidx.appcompat.app.AlertDialog dialog
                = builder.create();
        
        TextView tv_title = customLayout.findViewById(R.id.tv_title);
        TextView tv_sub_titale = customLayout.findViewById(R.id.tv_sub_titale);
        TextView tv_ok = customLayout.findViewById(R.id.tv_ok);
        tv_title.setText("Are You Sure ?");
        tv_sub_titale.setText("Are you sure that you would like to back home ?");
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
                
            }
        });
        dialog.show();
    }
    
    private void broadcast_manu() {
        
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.mail_bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Manual_Text_Send_Activty.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        LinearLayout lay_sendnow = bottomSheetDialog.findViewById(R.id.lay_sendnow);
        LinearLayout lay_schedule = bottomSheetDialog.findViewById(R.id.lay_schedule);
        lay_sendnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SMSAPI(edit_template.getText().toString(), Integer.parseInt(id), p_number);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                
            }
        });
        lay_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                Intent intent = new Intent(getApplicationContext(), Manual_Text_Sheduled_Activity.class);
                intent.putExtra("text", edit_template.getText().toString());
                intent.putExtra("id", id);
                intent.putExtra("number", p_number);
                intent.putExtra("tem_id", String.valueOf(temaplet_id));
                intent.putExtra("task_name", task_name);
                intent.putExtra("from_ac", from_ac);
                intent.putExtra("from_ac_id", from_ac_id);
                startActivity(intent);
                finish();
                
                
            }
        });
        bottomSheetDialog.show();
        
    }
    
    private void SMSAPI(String text, int id, String email) throws JSONException {
        
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        JSONObject obj = new JSONObject();
        
        JSONObject paramObject = new JSONObject();
        
        paramObject.put("type", SessionManager.getCampaign_type(getApplicationContext()));
        paramObject.put("team_id", "1");
        paramObject.put("organization_id", "1");
        paramObject.put("user_id", user_id);
        paramObject.put("manage_by", SessionManager.getCampaign_type_name(getApplicationContext()));
        try {
            paramObject.put("time", Global_Time.time_12_to_24(Global_Time.getCurrentTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramObject.put("date", Global_Time.getCurrentDate());
        paramObject.put("assign_to", user_id);
        //   paramObject.put("task_description", text);
        
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 1; i++) {
            JSONObject paramObject1 = new JSONObject();
            paramObject1.put("prospect_id", id);
            paramObject1.put("mobile", email);
            jsonArray.put(paramObject1);
            break;
        }
        JSONArray contact_group_ids = new JSONArray();
        contact_group_ids.put("");
        paramObject.put("contact_group_ids", contact_group_ids);
        paramObject.put("prospect_id", jsonArray);
        paramObject.put("record_id", "");
        paramObject.put("task_name", task_name);
        if (temaplet_id == 0) {
            paramObject.put("template_id", "");
            
        } else {
            paramObject.put("template_id", temaplet_id);
        }
        //paramObject.put("content_header","");
        paramObject.put("content_body", edit_template.getText().toString());
        paramObject.put("from_ac", from_ac);
        paramObject.put("from_ac_id", from_ac_id);
        obj.put("data", paramObject);
        
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is", new Gson().toJson(gsonObject));
        
        
        retrofitCalls.manual_task_store(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {
                    loadingDialog.cancelLoading();
                    loadingDialog.cancelLoading();
                    Intent intent = new Intent(getApplicationContext(), Tankyou_after_scheduled_task_Activity.class);
                    intent.putExtra("s_name", "add");
                    startActivity(intent);
                    finish();
                    
                } else if (response.body().getHttp_status() == 403) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage().toString(), false);
                } else {
                    loadingDialog.cancelLoading();
                }
                
                
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }
    
    private void SMS_execute(String text, int id, String email, String record_id) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        JsonObject obj = new JsonObject();
        
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("content_body", text);
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("prospect_id", id);
        paramObject.addProperty("record_id", record_id);
        paramObject.addProperty("type", "SMS");
        paramObject.addProperty("team_id", "1");
        obj.add("data", paramObject);
        
        retrofitCalls.Email_execute(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                edit_template.setText("");
                loadingDialog.cancelLoading();
                Intent intent = new Intent(getApplicationContext(), Tankyou_after_scheduled_task_Activity.class);
                intent.putExtra("s_name", "add");
                startActivity(intent);
                finish();
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }
    
    private void bouttomSheet() {
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.template_list_dialog_item, null);
        bottomSheetDialog_templateList = new BottomSheetDialog(Manual_Text_Send_Activty.this, R.style.CoffeeDialog);
        bottomSheetDialog_templateList.setContentView(mView);
        LinearLayout layout_list_template = bottomSheetDialog_templateList.findViewById(R.id.layout_list_template);
        layout_list_template.setVisibility(View.VISIBLE);
        TextView tv_error = bottomSheetDialog_templateList.findViewById(R.id.tv_error);
        RecyclerView templet_list = bottomSheetDialog_templateList.findViewById(R.id.templet_list);
        templet_list.setVisibility(View.VISIBLE);
        try {
            if (Global.isNetworkAvailable(Manual_Text_Send_Activty.this, MainActivity.mMainLayout)) {
                Template_list(templet_list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        
        tv_error.setVisibility(View.GONE);
        
        bottomSheetDialog_templateList.show();
    }
    
    private void Template_list(RecyclerView templet_list) throws JSONException {
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Manual_Text_Send_Activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("type", "SMS");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("perPage", 10000000);
        paramObject.addProperty("page", 1);
        obj.add("data", paramObject);
        retrofitCalls.Template_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Manual_Text_Send_Activty.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                templateList.clear();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<TemplateList>() {
                    }.getType();
                    TemplateList list = new Gson().fromJson(headerString, listType);
                    templateList = list.getTemplate();
                }
                
                TemplateList.Template template1 = new TemplateList.Template();
                template1.setTemplateName(getResources().getString(R.string.Save_current_as_template));
                template1.setSelect(true);
                templateList.add(templateList.size(), template1);
                
                
                templet_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                templateAdepter = new TemplateAdepter(getApplicationContext(), templateList, templateClick);
                templet_list.setAdapter(templateAdepter);
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
            
            
        });
        
        
    }
    
    public void OnClick(@SuppressLint("UnknownNullness") String s) {
        String curenttext = edit_template.getText().toString();
        String Newtext = curenttext + " " + s + " ";
        edit_template.setText(Newtext);
        edit_template.setSelection(edit_template.getText().length());
    }
    
    @Override
    public void OnClick(TemplateList.Template template) {
        edit_template.setText(template.getContentBody());
        edit_template.setSelection(edit_template.getText().length());
        bottomSheetDialog_templateList.dismiss();
    }
    
    public void showAlertDialogButtonClicked(View view) {
        
        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, R.style.BottomSheetDialog);
        final View customLayout
                = getLayoutInflater()
                          .inflate(
                                  R.layout.add_titale_for_templet,
                                  null);
        builder.setView(customLayout);
        CoordinatorLayout c_layout = customLayout.findViewById(R.id.c_layout);
        EditText editText = customLayout.findViewById(R.id.editText);
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
        TextView tv_add = customLayout.findViewById(R.id.tv_add);
        AlertDialog dialog
                = builder.create();
        
        dialog.show();
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (editText.getText().toString().equals("")) {
                    Global.Messageshow(getApplicationContext(), c_layout, getResources().getString(R.string.add_tamplate), false);
                } else {
                    try {
                        if (Global.isNetworkAvailable(Manual_Text_Send_Activty.this, MainActivity.mMainLayout)) {
                            if (isValidation(editText.getText().toString().trim(), dialog)) {
                                CreateTemplate(editText.getText().toString().trim(), dialog);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        
    }
    
    private void CreateTemplate(String template_name, AlertDialog dialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Manual_Text_Send_Activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("template_name", template_name);
        String template_slug = template_name.toUpperCase().replace(" ", "_");
        paramObject.addProperty("template_slug", template_slug);
        paramObject.addProperty("content_body", edit_template.getText().toString().trim());
        paramObject.addProperty("type", "SMS");
        
        obj.add("data", paramObject);
        retrofitCalls.CreateTemplate(sessionManager, obj, loadingDialog, token, Global.getVersionname(Manual_Text_Send_Activty.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Global.Messageshow(getApplicationContext(), mMainLayout,
                            response.body().getMessage(), true);
                    dialog.dismiss();
                    bottomSheetDialog_templateList.dismiss();
                    
                } else {
                    bottomSheetDialog_templateList.dismiss();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    UservalidateModel user_model = new Gson().fromJson(headerString, listType);
                    if (user_model.getTemplate_slug() != null) {
                       try {
                           Global.Messageshow(getApplicationContext(), mMainLayout,
                                   user_model.getTemplate_slug().get(0).toString().replace("slug", "name"), false);

                       }catch (Exception e)
                       {
                       }
                       }
                }
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
            
            
        });
        
        
    }
    
    private boolean isValidation(String name, AlertDialog dialog) {
        
        if (name.equals("")) {
            dialog.dismiss();
            bottomSheetDialog_templateList.dismiss();
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.template_name), false);
        } else if (edit_template.getText().toString().equals("")) {
            bottomSheetDialog_templateList.dismiss();
            dialog.dismiss();
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.template_dody), false);
        } else {
            return true;
        }
        
        return false;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE) {
            if (resultCode == -1) {
                Uri fileUri = data.getData();
                filePath = fileUri.getPath();
                Log.e("File Pathe uis ", filePath);
                
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        showAlertDialogButtonClicked();
        //super.onBackPressed();
    }
    
    void BZCard_list() throws JSONException {
        
        // loadingDialog.showLoadingDialog();
        
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Manual_Text_Send_Activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.BZcard_User_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Manual_Text_Send_Activty.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    bizcardList.clear();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<BZcardListModel>() {
                    }.getType();
                    BZcardListModel bZcardListModel = new Gson().fromJson(headerString, listType);
                    
                    bizcardList = bZcardListModel.getBizcardList_user();
                    
                    
                }
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
        
        
    }
    
    void Zoom_Api(Context mCtx) throws JSONException {
        
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Manual_Text_Send_Activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("user_tmz_id", signResponseModel.getUser().getUserTimezone().get(0).getValue());
        obj.add("data", paramObject);
        retrofitCalls.zoomIntegrationExists(sessionManager, obj, loadingDialog, token, Global.getVersionname(Manual_Text_Send_Activty.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ZoomExists>() {
                    }.getType();
                    ZoomExists zoomExists = new Gson().fromJson(headerString, listType);
                    if (zoomExists.getUserExists()) {
                        broadcast_manu_zoom(mCtx);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Verification_web.class);
                        intent.putExtra("Activtiy", "zoom");
                        startActivity(intent);
                    }
                }
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
        
        
    }
    
    void Zoom_Api_check_zoom_account(Context mCtx) throws JSONException {
        
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Manual_Text_Send_Activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("user_tmz_id", signResponseModel.getUser().getUserTimezone().get(0).getValue());
        obj.add("data", paramObject);
        retrofitCalls.zoomIntegrationExists(sessionManager, obj, loadingDialog, token, Global.getVersionname(Manual_Text_Send_Activty.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ZoomExists>() {
                    }.getType();
                    ZoomExists zoomExists = new Gson().fromJson(headerString, listType);
                    if (zoomExists.getUserExists()) {
                        zoom_flag = true;
                    } else {
                        zoom_flag = false;
                    }
                }
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
        
        
    }
    
    private void broadcast_manu_zoom(Context mCtx) {
        
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.zoom_layout, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Manual_Text_Send_Activty.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        LinearLayout la_date = bottomSheetDialog.findViewById(R.id.la_date);
        TextView tv_date = bottomSheetDialog.findViewById(R.id.tv_date);
        LinearLayout la_time = bottomSheetDialog.findViewById(R.id.la_time);
        LinearLayout layout_Duration = bottomSheetDialog.findViewById(R.id.layout_Duration);
        TextView tv_time = bottomSheetDialog.findViewById(R.id.tv_time);
        TextView txt_time = bottomSheetDialog.findViewById(R.id.txt_time);
        TextView tc_time_zone = bottomSheetDialog.findViewById(R.id.tc_time_zone);
        TextView tv_done = bottomSheetDialog.findViewById(R.id.tv_done);
        tv_date.setText(Global_Time.getCurrentDate());
        
        tv_time.setText(Global_Time.getCurrentTime());
        la_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenBob(tv_date);
            }
        });
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        tc_time_zone.setText("Time Zone(" + user_data.getUser().getUserTimezone().get(0).getText().toString() + ")");
        la_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTimer(tv_time);
            }
        });
        layout_Duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*select Duration ti zoom */
                Duration_bouttomSheet(mCtx, txt_time);
            }
        });
        
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                /*Create a Zoom Meeting*/
                if (!txt_time.getText().toString().equals("")) {
                    try {
                        if (Global_Time.checkTime_isvalid(getApplicationContext(),tv_time.getText().toString().trim(),tv_date.getText().toString().trim())) {
                            Create_Zoom(tv_date, tv_time, txt_time);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Global.Messageshow(mCtx, mMainLayout, mCtx.getString(R.string.select_meeting), false);
                }
                bottomSheetDialog.cancel();
                
                
            }
        });
        bottomSheetDialog.show();
        
    }
    
    private void Create_Zoom(TextView tv_date, TextView tv_time, TextView txt_time) {
        String Starttime = "";
        try {
            Starttime = tv_date.getText().toString() + 'T' + Global_Time.time_12_to_24(tv_time.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String duration = txt_time.getText().toString();
        String mystring = duration;
        String arr[] = mystring.split(" ", 2);
        String firstWord = arr[0];
        
        try {
            if (Global.isNetworkAvailable(Manual_Text_Send_Activty.this, MainActivity.mMainLayout)) {
                /*Create a Zoom Meeting*/
                Zoom_create(Starttime, firstWord);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    void Zoom_create(String starttime, String duration) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Manual_Text_Send_Activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("meeting_name", "contact Ninja with");
        paramObject.addProperty("start_time", starttime);
        paramObject.addProperty("duration", duration);
        paramObject.addProperty("description", "");
        paramObject.addProperty("timezone", signResponseModel.getUser().getUserTimezone().get(0).getTzname());
        obj.add("data", paramObject);
        retrofitCalls.ZoomCreate(sessionManager, obj, loadingDialog, token, Global.getVersionname(Manual_Text_Send_Activty.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ZoomExists>() {
                    }.getType();
                    ZoomExists zoomExists = new Gson().fromJson(headerString, listType);
                    
                    
                    String curenttext = edit_template.getText().toString();
                    String Newtext = curenttext + " " + " \n " + zoomExists.getZoom_meeting_link_with_password();
                    edit_template.setText(Newtext);
                    edit_template.setSelection(edit_template.getText().length());
                    
                }
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.reconnect_zoom), false);
                loadingDialog.cancelLoading();
            }
        });
        
        
    }
    
    private void Duration_bouttomSheet(Context mCtx, TextView txt_time) {
        
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.duration_item_update, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Manual_Text_Send_Activty.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        RadioButton red_5 = bottomSheetDialog.findViewById(R.id.red_5);
        RadioButton red_15 = bottomSheetDialog.findViewById(R.id.red_15);
        RadioButton red_30 = bottomSheetDialog.findViewById(R.id.red_30);
        RadioButton red_45 = bottomSheetDialog.findViewById(R.id.red_45);
        RadioButton red_60 = bottomSheetDialog.findViewById(R.id.red_60);
        
        bottomSheetDialog.show();
        
        if (txt_time.getText().toString().equals(mCtx.getResources().getString(R.string.m_5))) {
            red_5.setChecked(true);
        } else if (txt_time.getText().toString().equals(mCtx.getResources().getString(R.string.m_15))) {
            red_15.setChecked(true);
        } else if (txt_time.getText().toString().equals(mCtx.getResources().getString(R.string.m_30))) {
            red_30.setChecked(true);
        } else if (txt_time.getText().toString().equals(mCtx.getResources().getString(R.string.m_45))) {
            red_45.setChecked(true);
        } else if (txt_time.getText().toString().equals(mCtx.getResources().getString(R.string.m_60))) {
            red_60.setChecked(true);
        }
        
        red_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txt_time.setText(mCtx.getResources().getString(R.string.m_5));
                    txt_time.setVisibility(View.VISIBLE);
                    bottomSheetDialog.dismiss();
                }
            }
        });
        red_15.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txt_time.setText(mCtx.getResources().getString(R.string.m_15));
                    txt_time.setVisibility(View.VISIBLE);
                    bottomSheetDialog.dismiss();
                }
            }
        });
        red_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txt_time.setText(mCtx.getResources().getString(R.string.m_30));
                    txt_time.setVisibility(View.VISIBLE);
                    bottomSheetDialog.dismiss();
                }
            }
        });
        red_45.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txt_time.setText(mCtx.getResources().getString(R.string.m_45));
                    txt_time.setVisibility(View.VISIBLE);
                    bottomSheetDialog.dismiss();
                }
            }
        });
        red_60.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txt_time.setText(mCtx.getResources().getString(R.string.m_60));
                    txt_time.setVisibility(View.VISIBLE);
                    bottomSheetDialog.dismiss();
                }
            }
        });
        
    }
    
    public void OpenBob(TextView tv_date) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(Manual_Text_Send_Activty.this,
                new DatePickerDialog.OnDateSetListener() {
                    
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        
                        
                        String sMonth = "";
                        if (monthOfYear + 1 < 10) {
                            sMonth = "0" + (monthOfYear + 1);
                        } else {
                            sMonth = String.valueOf(monthOfYear + 1);
                        }
                        
                        
                        String sdate = "";
                        if (dayOfMonth < 10) {
                            sdate = "0" + dayOfMonth;
                        } else {
                            sdate = String.valueOf(dayOfMonth);
                        }
                        
                        
                        tv_date.setText(year + "-" + sMonth + "-" + sdate);
                        
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (1000 * 60 * 60));
        
        datePickerDialog.show();
        
    }
    
    public void onTimer(TextView tv_time) {
        Calendar mcurrentTime = Calendar.getInstance();
        m_hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        m_minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    
                    m_hour = selectedHour;
                    m_minute = selectedMinute;
                    String timeSet = "";
                    if (m_hour > 12) {
                        m_hour -= 12;
                        timeSet = getResources().getString(R.string.PM);
                    } else if (m_hour == 0) {
                        m_hour += 12;
                        timeSet = getResources().getString(R.string.AM);
                    } else if (m_hour == 12) {
                        timeSet = getResources().getString(R.string.PM);
                    } else {
                        timeSet = getResources().getString(R.string.AM);
                    }
                
                    String min = "";
                    if (m_minute < 10)
                        min = "0" + m_minute;
                    else
                        min = String.valueOf(m_minute);
                
                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(m_hour).append(':')
                                           .append(min).append(" ").append(timeSet).toString();
                    tv_time.setText(aTime);
                    
            }
        }, m_hour, m_minute, false);//Yes 24 hour time
        mTimePicker.setTitle(getResources().getString(R.string.Select_Time));
        mTimePicker.show();
    }
    
    static class CardListAdepter extends RecyclerView.Adapter<CardListAdepter.cardListData> {
        
        Activity mContext;
        List<BZcardListModel.Bizcard> bizcardList;
        BottomSheetDialog bottomSheetDialog;
        TextClick interfaceClick;
        
        public CardListAdepter(Activity activity, List<BZcardListModel.Bizcard> bizcardList,
                               BottomSheetDialog bottomSheetDialog, TextClick interfaceClick) {
            this.mContext = activity;
            this.bizcardList = bizcardList;
            this.bottomSheetDialog = bottomSheetDialog;
            this.interfaceClick = interfaceClick;
        }
        
        
        @NonNull
        @Override
        public CardListAdepter.cardListData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
            return new CardListAdepter.cardListData(view);
        }
        
        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
        @Override
        public void onBindViewHolder(@NonNull CardListAdepter.cardListData holder, int position) {
            BZcardListModel.Bizcard bizcard = this.bizcardList.get(position);
            switch (bizcard.getCardName()) {
                case "bz_card_1":
                    Glide.with(mContext.getApplicationContext()).load(Global.card_s3_link + "bzstore1.png").into(holder.iv_card);
                    break;
                case "bz_card_2":
                    Glide.with(mContext.getApplicationContext()).load(Global.card_s3_link + "bzstore2.png").into(holder.iv_card);
                    break;
                case "bz_card_3":
                    Glide.with(mContext.getApplicationContext()).load(Global.card_s3_link + "bzstore3.png").into(holder.iv_card);
                    break;
                case "bz_card_4":
                    Glide.with(mContext.getApplicationContext()).load(Global.card_s3_link + "bzstore4.png").into(holder.iv_card);
                    break;
                case "bz_card_5":
                    Glide.with(mContext.getApplicationContext()).load(Global.card_s3_link + "bzstore5.png").into(holder.iv_card);
                    break;
                case "bz_card_6":
                    Glide.with(mContext.getApplicationContext()).load(Global.card_s3_link + "bzstore5.png").into(holder.iv_card);
                    break;
            }
           /* int resID = mContext.getResources().getIdentifier("my_" + bizcard.getCardName()
                    .replace(" ", "_").toLowerCase(), "drawable", mContext.getPackageName());
            if (resID != 0) {
                Glide.with(mContext.getApplicationContext()).load(resID).into(holder.iv_card);
            }*/
            
            holder.layout_select_image.setOnClickListener(v -> {
                String newUrl = "", oldUrl = "", Newtext = "";
                for (int i = 0; i < bizcardList.size(); i++) {
                    if (bizcardList.get(i).isScelect()) {
                        oldUrl = Global.bzcard_share + bizcardList.get(i).getId_encoded();
                        bizcardList.get(i).setScelect(false);
                        break;
                    }
                }
                bizcard.setScelect(true);
                newUrl = Global.bzcard_share + bizcard.getId_encoded();
                
                String curenttext = edit_template.getText().toString();
                if (!oldUrl.equals("") && !oldUrl.equals(newUrl)) {
                    String changeurl = curenttext.replace(oldUrl, newUrl);
                    Newtext = changeurl;
                } else {
                    Newtext = curenttext + newUrl;
                }
                edit_template.setText(Newtext);
                edit_template.setSelection(edit_template.getText().length());
                
                bottomSheetDialog.dismiss();
                notifyDataSetChanged();
            });
            if (bizcard.isScelect()) {
                holder.layout_select_image.setBackgroundResource(R.drawable.shape_10_blue);
            } else {
                holder.layout_select_image.setBackground(null);
            }
        }
        
        
        @Override
        public int getItemCount() {
            return bizcardList.size();
        }
        
        public static class cardListData extends RecyclerView.ViewHolder {
            
            ImageView iv_card;
            LinearLayout layout_select_image;
            
            public cardListData(@NonNull View itemView) {
                super(itemView);
                iv_card = itemView.findViewById(R.id.iv_card);
                layout_select_image = itemView.findViewById(R.id.layout_select_image);
            }
        }
        
        
    }
    
    class TemplateAdepter extends RecyclerView.Adapter<TemplateAdepter.viewholder> {
        
        public Context mCtx;
        List<TemplateList.Template> templateTextList1;
        TemplateClick interfaceClick;
        
        public TemplateAdepter(Context applicationContext, List<TemplateList.Template> templateTextList1, TemplateClick interfaceClick) {
            this.mCtx = applicationContext;
            this.templateTextList1 = templateTextList1;
            this.interfaceClick = interfaceClick;
        }
        
        @NonNull
        @Override
        public TemplateAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.select_templet_layout, parent, false);
            return new TemplateAdepter.viewholder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull TemplateAdepter.viewholder holder, int position) {
            TemplateList.Template item = templateTextList1.get(position);
            holder.tv_item.setText(item.getTemplateName());
            //holder.tv_item.setBackgroundResource(R.drawable.shape_unselect_back);
            holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.text_reg));
            
            if (item.isSelect()) {
                holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.purple_200));
                holder.line_view.setVisibility(View.VISIBLE);
            } else {
                holder.line_view.setVisibility(View.GONE);
            }
            
            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.tv_item.getText().toString().equals(getResources().getString(R.string.Save_current_as_template))) {
                        showAlertDialogButtonClicked(view);
                    } else {
                        temaplet_id = item.getId();
                        interfaceClick.OnClick(item);
                        for (int i = 0; i < bizcardList.size(); i++) {
                            if (bizcardList.get(i).isScelect()) {
                                bizcardList.get(i).setScelect(false);
                                break;
                            }
                        }
                        
                    }
                }
            });
            
            
        }
        
        @Override
        public int getItemCount() {
            return templateTextList1.size();
        }
        
        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item;
            View line_view;
            
            public viewholder(View view) {
                super(view);
                tv_item = view.findViewById(R.id.selected_broadcast);
                line_view = view.findViewById(R.id.line_view);
            }
        }
    }
    
    class PicUpTextAdepter extends RecyclerView.Adapter<PicUpTextAdepter.viewholder> {
        
        public Activity mCtx;
        List<HastagList.TemplateText> templateTextList;
        TextClick interfaceClick;
        
        public PicUpTextAdepter(Activity applicationContext, List<HastagList.TemplateText> templateTextList, TextClick interfaceClick) {
            this.mCtx = applicationContext;
            this.templateTextList = templateTextList;
            this.interfaceClick = interfaceClick;
        }
        
        @NonNull
        @Override
        public PicUpTextAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.template_text_selecte, parent, false);
            return new PicUpTextAdepter.viewholder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull PicUpTextAdepter.viewholder holder, int position) {
            HastagList.TemplateText item = templateTextList.get(position);
            holder.tv_item.setText(Global.setFirstLetter(item.getDescription()));
            holder.tv_item.setBackgroundResource(R.drawable.shape_unselect_back);
            holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.text_reg));
            if (item.getFile() != 0) {
                holder.im_file.setVisibility(View.VISIBLE);
                holder.tv_item.setVisibility(View.GONE);
                holder.im_file.setImageDrawable(mCtx.getDrawable(item.getFile()));
                holder.line_view.setVisibility(View.GONE);
                
            } else {
                
                holder.im_file.setVisibility(View.GONE);
                holder.tv_item.setVisibility(View.VISIBLE);
                
            }
            if (position == 0) {
                
                holder.line_view.setVisibility(View.GONE);
                
            } else if (position == 1) {
                holder.line_view.setVisibility(View.GONE);
            } else {
            
            }
            holder.im_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 0) {
                        
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        final View mView = getLayoutInflater().inflate(R.layout.bzcart_list_dialog_item, null);
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Manual_Text_Send_Activty.this,
                                R.style.DialogStyle);
                        bottomSheetDialog.setContentView(mView);
                        RecyclerView rv_image_card = bottomSheetDialog.findViewById(R.id.rv_image_card);
                        LinearLayout lay_no_list = bottomSheetDialog.findViewById(R.id.lay_no_list);
                        
                        if (bizcardList.size() != 0) {
                            rv_image_card.setVisibility(View.VISIBLE);
                            lay_no_list.setVisibility(View.GONE);
                        } else {
                            rv_image_card.setVisibility(View.GONE);
                            lay_no_list.setVisibility(View.VISIBLE);
                        }
                        rv_image_card.setLayoutManager(new LinearLayoutManager(Manual_Text_Send_Activty.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        rv_image_card.setHasFixedSize(true);
                        cardListAdepter = new CardListAdepter(Manual_Text_Send_Activty.this, bizcardList,
                                bottomSheetDialog, interfaceClick);
                        rv_image_card.setAdapter(cardListAdepter);
                        
                        
                        bottomSheetDialog.show();
                    } else if (position == 1) {
                        try {
                            if (Global.isNetworkAvailable(Manual_Text_Send_Activty.this, MainActivity.mMainLayout)) {
                                /*Check if user has records in Zoom Oauth table*/
                                Zoom_Api(mCtx);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    
                }
            });
            if (item.isSelect()) {
                holder.tv_item.setBackground(null);
                holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.text_reg));
                holder.line_view.setVisibility(View.VISIBLE);
            } else {
                holder.line_view.setVisibility(View.GONE);
            }
            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.isSelect()) {
                        Handler handler = new Handler();
                        Runnable r = new Runnable() {
                            @SuppressLint("NotifyDataSetChanged")
                            public void run() {
                                notifyDataSetChanged();
                            }
                        };
                        handler.postDelayed(r, 1000);
                        holder.tv_item.setBackgroundResource(R.drawable.shape_5_blue);
                        holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.white));
                        interfaceClick.OnClick(item.getHashtag());
                    }
                }
            });
            
            if (item.isSelect()) {
                holder.tv_item.setBackground(null);
                holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.text_reg));
                holder.line_view.setVisibility(View.VISIBLE);
            } else {
                holder.line_view.setVisibility(View.GONE);
            }
            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.isSelect()) {
                        Handler handler = new Handler();
                        Runnable r = new Runnable() {
                            @SuppressLint("NotifyDataSetChanged")
                            public void run() {
                                notifyDataSetChanged();
                            }
                        };
                        handler.postDelayed(r, 1000);
                        holder.tv_item.setBackgroundResource(R.drawable.shape_5_blue);
                        holder.tv_item.setTextColor(mCtx.getResources().getColor(R.color.white));
                        interfaceClick.OnClick(item.getHashtag());
                    }
                }
            });
            
            
        }
        
        @Override
        public int getItemCount() {
            return templateTextList.size();
        }
        
        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item;
            View line_view;
            ImageView im_file;
            
            public viewholder(View view) {
                super(view);
                tv_item = view.findViewById(R.id.tv_item);
                line_view = view.findViewById(R.id.line_view);
                im_file = view.findViewById(R.id.im_file);
            }
        }
    }
    
    class EmailListAdepter extends RecyclerView.Adapter<EmailListAdepter.viewholder> {
        
        public Context mCtx;
        List<ContecModel.PhoneDatum> userLinkedGmailList;
        
        public EmailListAdepter(Context applicationContext, List<ContecModel.PhoneDatum> userLinkedGmailList) {
            this.mCtx = applicationContext;
            this.userLinkedGmailList = userLinkedGmailList;
        }
        
        @NonNull
        @Override
        public EmailListAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.email_select_layout, parent, false);
            return new EmailListAdepter.viewholder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull EmailListAdepter.viewholder holder, int position) {
            
            holder.tv_item.setText(userLinkedGmailList.get(position).getPhoneNumber());
            if (holder.iv_selected.isSelected() == false) {
                holder.iv_unselected.setVisibility(View.VISIBLE);
                holder.iv_selected.setVisibility(View.GONE);
                
                amountOfItemsSelected++;
            } else if (holder.iv_selected.isSelected() == true) {
                holder.iv_unselected.setVisibility(View.GONE);
                holder.iv_selected.setVisibility(View.VISIBLE);
                amountOfItemsSelected--;
            }
            
            if (amountOfItemsSelected > 1) {
            
            }
            if (userLinkedGmailList.get(position).getIsDefault().toString().equals("1")) {
                holder.iv_dufult.setVisibility(View.VISIBLE);
            } else {
                holder.iv_dufult.setVisibility(View.GONE);
            }
            
            
            if (userLinkedGmailList.get(position).isEmailSelect()) {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }
            
            
            holder.layout_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < userLinkedGmailList.size(); i++) {
                        if (userLinkedGmailList.get(i).isEmailSelect()) {
                            userLinkedGmailList.get(i).setEmailSelect(false);
                            break;
                        }
                    }
                    userLinkedGmailList.get(position).setEmailSelect(true);
                    
                    select_userLinkedGmailList.clear();
                    holder.iv_selected.setVisibility(View.VISIBLE);
                    holder.iv_unselected.setVisibility(View.GONE);
                    select_userLinkedGmailList.add(userLinkedGmailList.get(position));
                    notifyDataSetChanged();
                }
            });
            
            
        }
        
        @Override
        public int getItemCount() {
            return userLinkedGmailList.size();
        }
        
        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item;
            View line_view;
            ImageView iv_dufult, iv_selected, iv_unselected;
            LinearLayout layout_select;
            
            public viewholder(View view) {
                super(view);
                tv_item = view.findViewById(R.id.tv_item);
                line_view = view.findViewById(R.id.line_view);
                iv_dufult = view.findViewById(R.id.iv_dufult);
                iv_selected = view.findViewById(R.id.iv_selected);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                layout_select = view.findViewById(R.id.layout_select);
            }
        }
    }
}