package com.contactninja.Manual_email_text.List_And_show;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.contactninja.ARE.ARE_ToolItem_Bold;
import com.contactninja.ARE.ARE_ToolItem_Italic;
import com.contactninja.ARE.ARE_ToolItem_Link;
import com.contactninja.ARE.ARE_ToolItem_Underline;
import com.contactninja.Interface.TemplateClick;
import com.contactninja.Interface.TextClick;
import com.contactninja.MainActivity;
import com.contactninja.Manual_email_text.Email_Tankyou;
import com.contactninja.Manual_email_text.Manual_Email_TaskActivity_;
import com.contactninja.Manual_email_text.Manual_Shooz_Time_Date_Activity;
import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.HastagList;
import com.contactninja.Model.ManualTaskDetailsModel;
import com.contactninja.Model.TemplateList;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UserLinkedList;
import com.contactninja.Model.UservalidateModel;
import com.contactninja.Model.ZoomExists;
import com.contactninja.R;
import com.contactninja.Setting.Verification_web;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Item_List_Email_Detail_activty extends AppCompatActivity implements View.OnClickListener,
        ConnectivityReceiver.ConnectivityReceiverListener, TextClick, TemplateClick {
    public static final int PICKFILE_RESULT_CODE = 1;
    static ManualTaskDetailsModel.ManualDetails manualDetails;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back, iv_body, iv_toolbar_manu1;
    TextView bt_done, tv_taskname, tv_stap;
    RelativeLayout mMainLayout;
    static  EditText ev_from, ev_to, ev_subject;
    static AREditText edit_compose;
    EditText ev_titale;
    ImageView iv_more;
    BottomSheetDialog bottomSheetDialog;
    TemplateAdepter templateAdepter;
    RecyclerView rv_direct_list;
    PicUpTextAdepter picUpTextAdepter;
    List<HastagList.TemplateText> templateTextList = new ArrayList<>();
    List<TemplateList.Template> templateList = new ArrayList<>();
    String filePath;
    BottomSheetDialog bottomSheetDialog_templateList;
    TemplateClick templateClick;
    Integer id = 0;
    String email = "", task_name = "", from_ac = "", from_ac_id = "";
    BottomSheetDialog bottomSheetDialog_templateList1;
    int defult_id, temaplet_id = 0;
    List<UserLinkedList.UserLinkedGmail> select_userLinkedGmailList = new ArrayList<>();
    List<UserLinkedList.UserLinkedGmail> userLinkedGmailList = new ArrayList<>();
    TextView tv_status, tv_date, tv_use_tamplet;
    TextView tv_bold, tv_ital, tv_uline;
    LinearLayout layout_a, lay_seq_stap, lay_taskname;
    List<BZcardListModel.Bizcard> bizcardList = new ArrayList<>();
    CardListAdepter cardListAdepter;
    private BroadcastReceiver mNetworkReceiver;
    private int amountOfItemsSelected = 0;
    private int FirstTime = 0;
    private long mLastClickTime = 0;
    private IARE_Toolbar mToolbar;
    LinearLayout bottombar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail_activty);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);

        IntentUI();
        initToolbar();
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            id = bundle.getInt("record_id");
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (Global.isNetworkAvailable(Item_List_Email_Detail_activty.this, MainActivity.mMainLayout)) {
                loadingDialog.showLoadingDialog();
                Api_Details();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        templateClick = this;


        tv_bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Spannable spannableString = new SpannableStringBuilder(edit_compose.getText());
                spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                        edit_compose.getSelectionStart(),
                        edit_compose.getSelectionEnd(),
                        0);

                edit_compose.setText(spannableString);
                Log.e("Text is a", Html.toHtml(edit_compose.getText()));
            }
        });


        tv_ital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Spannable spannableString = new SpannableStringBuilder(edit_compose.getText());
                spannableString.setSpan(new StyleSpan(Typeface.ITALIC),
                        edit_compose.getSelectionStart(),
                        edit_compose.getSelectionEnd(),
                        0);

                edit_compose.setText(spannableString);
            }
        });

        tv_uline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Spannable spannableString = new SpannableStringBuilder(edit_compose.getText());
                spannableString.setSpan(new UnderlineSpan(),
                        edit_compose.getSelectionStart(),
                        edit_compose.getSelectionEnd(),
                        0);

                edit_compose.setText(spannableString.toString());
            }
        });

    }

    void Api_Details() throws JSONException {
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("record_id", id);

        obj.add("data", paramObject);
        retrofitCalls.Mail_Activiy_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {


                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    if (response.body().getHttp_status() == 200) {

                        Type listType = new TypeToken<ManualTaskDetailsModel>() {
                        }.getType();
                        ManualTaskDetailsModel manualTaskDetailsModel = new Gson().fromJson(headerString, listType);
                        manualDetails = manualTaskDetailsModel.get_0();
                        setData();
                        Mail_listDetails();
                        try {
                            if (Global.isNetworkAvailable(Item_List_Email_Detail_activty.this, mMainLayout)) {
                                Hastag_list();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (Global.isNetworkAvailable(Item_List_Email_Detail_activty.this, MainActivity.mMainLayout)) {
                                BZCard_list();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void Mail_listDetails() {

        userLinkedGmailList = sessionManager.getUserLinkedGmail(getApplicationContext());
        Log.e("Size is", "" + new Gson().toJson(userLinkedGmailList));
       if (userLinkedGmailList.size() == 1) {
            iv_more.setVisibility(View.GONE);
        } else {
            iv_more.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < userLinkedGmailList.size(); i++) {
            if (userLinkedGmailList.get(i).getId().toString().equals(manualDetails.getSentTblId().toString())) {
                ev_from.setText(userLinkedGmailList.get(i).getUserEmail());
                defult_id = userLinkedGmailList.get(i).getId();
                select_userLinkedGmailList.add(userLinkedGmailList.get(i));
                from_ac = userLinkedGmailList.get(i).getType();
                from_ac_id = String.valueOf(userLinkedGmailList.get(i).getId());
            }
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Item_List_Email_Detail_activty.this, mMainLayout);
    }

    @SuppressLint("ObsoleteSdkInt")
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

    private void setData() {
        //    ev_from.setText(manualDetails.getContactMasterFirstname()+" "+manualDetails.getContactMasterLastname());
        ev_to.setText(manualDetails.getEmail());
        ev_subject.setText(manualDetails.getContentHeader());
        edit_compose.setText(Html.fromHtml(manualDetails.getContentBody()));
        ev_titale.setText(manualDetails.getTaskName());
        tv_taskname.setText(manualDetails.getTaskName());
        //  try {
        //   String time =Global.getDate(manualDetails.getStartTime());
        String FullDate = manualDetails.getDate() + " " + manualDetails.getTime();
        String formateChnage = Global.formateChange(FullDate);
        tv_date.setText(formateChnage);
        compareDates(manualDetails.getDate(), tv_status, manualDetails);


       /* } catch (ParseException e) {
            e.printStackTrace();
        }*/
        if (!Global.IsNotNull(manualDetails.getSeqId()) || manualDetails.getSeqId() != 0) {
            lay_seq_stap.setVisibility(View.VISIBLE);
            lay_taskname.setVisibility(View.GONE);
            tv_stap.setText("Step#" + manualDetails.getStep_no() + " Manual email -");

        } else {
            lay_seq_stap.setVisibility(View.GONE);
            lay_taskname.setVisibility(View.VISIBLE);
        }

    }

    private void initToolbar() {
        mToolbar = this.findViewById(R.id.areToolbar);
        IARE_ToolItem bold = new ARE_ToolItem_Bold();
        IARE_ToolItem italic = new ARE_ToolItem_Italic();
        IARE_ToolItem underline = new ARE_ToolItem_Underline();
        IARE_ToolItem link = new ARE_ToolItem_Link();
        mToolbar.addToolbarItem(bold);
        mToolbar.addToolbarItem(italic);
        mToolbar.addToolbarItem(underline);
        mToolbar.addToolbarItem(link);
        edit_compose.setToolbar(mToolbar);
    }
    private void IntentUI() {
        bottombar=findViewById(R.id.bottombar);
        tv_stap = findViewById(R.id.tv_stap);
        tv_taskname = findViewById(R.id.tv_taskname);
        lay_seq_stap = findViewById(R.id.lay_seq_stap);
        lay_taskname = findViewById(R.id.lay_taskname);
        layout_a = findViewById(R.id.layout_a);
        tv_uline = findViewById(R.id.tv_uline);
        tv_bold = findViewById(R.id.tv_bold);
        tv_ital = findViewById(R.id.tv_ital);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        iv_toolbar_manu1 = findViewById(R.id.iv_toolbar_manu1);
        iv_toolbar_manu1.setOnClickListener(this);
        iv_toolbar_manu1.setVisibility(View.VISIBLE);
        mMainLayout = findViewById(R.id.mMainLayout);
        bt_done = findViewById(R.id.bt_done);
        bt_done.setOnClickListener(this);

        ev_from = findViewById(R.id.ev_from);
        ev_to = findViewById(R.id.ev_to);
        ev_subject = findViewById(R.id.ev_subject);
        edit_compose = findViewById(R.id.edit_compose);
        iv_body = findViewById(R.id.iv_body);
        ev_titale = findViewById(R.id.ev_titale);
        iv_more = findViewById(R.id.iv_more);
        tv_use_tamplet = findViewById(R.id.tv_use_tamplet);

        tv_use_tamplet.setOnClickListener(this);
        rv_direct_list = findViewById(R.id.rv_direct_list);
        iv_more = findViewById(R.id.iv_more);
        iv_more.setOnClickListener(this);
        tv_status = findViewById(R.id.tv_status);
        tv_date = findViewById(R.id.tv_date);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_manu1:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Select_to_update();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_use_tamplet:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                bouttomSheet();
                break;
            case R.id.iv_more:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Email_bouttomSheet();
                break;
            case R.id.bt_done:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                try {
                    SMS_execute(edit_compose.getHtml().toString(), manualDetails.getProspectId(),
                            manualDetails.getEmail(), String.valueOf(manualDetails.getId()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    private void Select_to_update() {
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.solo_item_update, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Item_List_Email_Detail_activty.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        LinearLayout layout_Mark = bottomSheetDialog.findViewById(R.id.layout_Mark);
        LinearLayout layout_Paused = bottomSheetDialog.findViewById(R.id.layout_Paused);
        LinearLayout layout_snooze = bottomSheetDialog.findViewById(R.id.layout_snooze);
        LinearLayout layout_delete = bottomSheetDialog.findViewById(R.id.layout_delete);


        // select sting static
        String[] selet_item = getResources().getStringArray(R.array.manual_Select);
        layout_Mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

                showAlertDialogButtonClicked(getResources().getString(R.string.manual_aleart_finished),
                        getResources().getString(R.string.manual_aleart_finished_des), selet_item[0]);
            }
        });
        layout_Paused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

                showAlertDialogButtonClicked(getResources().getString(R.string.manual_aleart_paused),
                        getResources().getString(R.string.manual_aleart_paused_des), selet_item[1]);
            }
        });

        layout_snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                showAlertDialogButtonClicked(getResources().getString(R.string.manual_aleart_snooze),
                        getResources().getString(R.string.manual_aleart_snooze_des), selet_item[2]);
            }
        });
        layout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                showAlertDialogButtonClicked(getResources().getString(R.string.manual_aleart_delete),
                        getResources().getString(R.string.manual_aleart_delete_des), selet_item[3]);
            }
        });


        bottomSheetDialog.show();
    }


    public void showAlertDialogButtonClicked(String title, String dis, String type) {

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
        tv_title.setText(title);
        tv_sub_titale.setText(dis);
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
                dialog.dismiss();
                try {

                    switch (type) {
                        case "PAUSED":
                            SMSAPI(type);
                            break;
                        case "SNOOZE":
                            Intent intent = new Intent(getApplicationContext(), Manual_Shooz_Time_Date_Activity.class);
                            intent.putExtra("id", manualDetails.getId());
                            intent.putExtra("prospect_id", manualDetails.getProspectId());
                            intent.putExtra("seq_task_id", manualDetails.getSeqTaskId());
                            intent.putExtra("record_id",id);
                            intent.putExtra("Type", "EMAIL");
                            startActivity(intent);
                            finish();
                            break;
                        case "DELETE":
                            SMSAPI(type);
                            break;
                        case "MARK":
                            SMSAPI(type);
                            break;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        dialog.show();
    }

    private void SMSAPI(String type) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("team_id", 1);
        paramObject.put("organization_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("record_id", manualDetails.getId());
        switch (type) {
            case "PAUSED":
                paramObject.put("status", "PAUSED");
                break;
            case "DELETE":
                paramObject.put("status", "DELETED");
                break;
            case "MARK":
                paramObject.put("status", "FINISHED");
                break;
        }
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is", new Gson().toJson(gsonObject));


        retrofitCalls.manual_task_store(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {
                    loadingDialog.cancelLoading();
                    finish();

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
        JsonObject obj = new JsonObject();

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("record_id", record_id);
        paramObject.addProperty("type", "EMAIL");
        paramObject.addProperty("from_ac", from_ac);
        paramObject.addProperty("from_ac_id", from_ac_id);
        paramObject.addProperty("email_recipients", email);
        paramObject.addProperty("content_body", text);
        paramObject.addProperty("content_header", ev_subject.getText().toString());
        obj.add("data", paramObject);

        retrofitCalls.Email_execute(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();

                if (response.body().getHttp_status() == 200) {
                    Intent intent = new Intent(getApplicationContext(), Email_Tankyou.class);
                    intent.putExtra("s_name", "add");
                    startActivity(intent);
                    finish();
                }
                else if (response.body().getHttp_status()==403)
                {
                    Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.plan_validation),false);
                }
                else if (response.body().getHttp_status() == 404) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), false);
                } else {
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), false);
                }

            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void Hastag_list() throws JSONException {
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Item_List_Email_Detail_activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Hastag_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Item_List_Email_Detail_activty.this), Global.Device, new RetrofitCallback() {
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


                    HastagList.TemplateText text = new HastagList.TemplateText();
                    text.setFile(R.drawable.ic_a);
                    text.setSelect(false);
                    templateTextList.add(0, text);

                    HastagList.TemplateText text1 = new HastagList.TemplateText();
                    text1.setFile(R.drawable.ic_card_blank);
                    text1.setSelect(false);
                    templateTextList.add(1, text1);

                    HastagList.TemplateText text2 = new HastagList.TemplateText();
                    text2.setFile(R.drawable.ic_video);
                    text2.setSelect(false);
                    templateTextList.add(2, text2);


                    HastagList.TemplateText templateText = new HastagList.TemplateText();
                    templateText.setDescription("Placeholders #");
                    templateText.setSelect(true);
                    templateTextList.add(3, templateText);


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
        picUpTextAdepter = new PicUpTextAdepter(Item_List_Email_Detail_activty.this, templateTextList, this);
        rv_direct_list.setAdapter(picUpTextAdepter);
    }


    private void broadcast_manu() {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.mail_bottom_sheet, null);
        bottomSheetDialog = new BottomSheetDialog(Item_List_Email_Detail_activty.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        LinearLayout lay_sendnow = bottomSheetDialog.findViewById(R.id.lay_sendnow);
        LinearLayout lay_schedule = bottomSheetDialog.findViewById(R.id.lay_schedule);
        lay_sendnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    EmailAPI(ev_subject.getText().toString(), edit_compose.getHtml().toString(), id, email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        lay_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Manual_Email_TaskActivity_.class);
                intent.putExtra("subject", ev_subject.getText().toString());
                intent.putExtra("body", edit_compose.getHtml().toString());
                intent.putExtra("id", id);
                intent.putExtra("email", email);
                intent.putExtra("gid", String.valueOf(select_userLinkedGmailList.get(0).getId()));
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

    private void bouttomSheet() {
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.template_list_dialog_item, null);
        bottomSheetDialog_templateList = new BottomSheetDialog(Item_List_Email_Detail_activty.this, R.style.CoffeeDialog);
        bottomSheetDialog_templateList.setContentView(mView);
        LinearLayout layout_list_template = bottomSheetDialog_templateList.findViewById(R.id.layout_list_template);
        layout_list_template.setVisibility(View.VISIBLE);
        TextView tv_error = bottomSheetDialog_templateList.findViewById(R.id.tv_error);
        RecyclerView templet_list = bottomSheetDialog_templateList.findViewById(R.id.templet_list);
        templet_list.setVisibility(View.VISIBLE);
        try {
            if (Global.isNetworkAvailable(Item_List_Email_Detail_activty.this, MainActivity.mMainLayout)) {
                Template_list(templet_list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        tv_error.setVisibility(View.GONE);

        bottomSheetDialog_templateList.show();
    }

    private void Template_list(RecyclerView templet_list) throws JSONException {
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Item_List_Email_Detail_activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("type", "EMAIL");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("perPage", 10000000);
        paramObject.addProperty("page", 1);
        obj.add("data", paramObject);
        retrofitCalls.Template_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Item_List_Email_Detail_activty.this), Global.Device, new RetrofitCallback() {
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
                template1.setTemplateName("Save current as template");
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
        String curenttext = edit_compose.getText().toString();
        String Newtext = curenttext +" "+ s +" ";
        edit_compose.setText(Newtext);
        edit_compose.setSelection(edit_compose.getText().length());
    }

    @Override
    public void OnClick(TemplateList.Template template) {
        ev_subject.setText(template.getContentHeader());
        edit_compose.setText(template.getContentBody());
        edit_compose.setSelection(edit_compose.getText().length());
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
                    Global.Messageshow(getApplicationContext(), c_layout, "Enter template name ", false);
                } else {
                    try {
                        if (Global.isNetworkAvailable(Item_List_Email_Detail_activty.this, MainActivity.mMainLayout)) {
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

    private boolean isValidation(String name, AlertDialog dialog) {

        if (name.equals("")) {
            dialog.dismiss();
            bottomSheetDialog_templateList.dismiss();
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.template_name), false);
        } else if (ev_subject.getText().toString().equals("")) {
            dialog.dismiss();
            bottomSheetDialog_templateList.dismiss();
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.template_subject), false);
        } else if (edit_compose.getText().toString().equals("")) {
            bottomSheetDialog_templateList.dismiss();
            dialog.dismiss();
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.template_dody), false);
        } else {
            return true;
        }

        return false;
    }

    private void CreateTemplate(String template_name, AlertDialog dialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Item_List_Email_Detail_activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("template_name", template_name);
        paramObject.addProperty("content_header", ev_subject.getText().toString().trim());
        String template_slug = template_name.toUpperCase().replace(" ", "_");
        paramObject.addProperty("template_slug", template_slug);
        paramObject.addProperty("content_body", edit_compose.getHtml().toString().trim());
        paramObject.addProperty("type", "EMAIL");

        obj.add("data", paramObject);
        retrofitCalls.CreateTemplate(sessionManager, obj, loadingDialog, token, Global.getVersionname(Item_List_Email_Detail_activty.this), Global.Device, new RetrofitCallback() {
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
                        Global.Messageshow(getApplicationContext(), mMainLayout,
                                user_model.getTemplate_slug().get(0).toString().replace("slug", "name"), false);
                    }
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }


        });


    }

    private void Email_bouttomSheet() {
        final View mView = getLayoutInflater().inflate(R.layout.email_bottom_sheet, null);
        bottomSheetDialog_templateList1 = new BottomSheetDialog(Item_List_Email_Detail_activty.this, R.style.CoffeeDialog);
        bottomSheetDialog_templateList1.setContentView(mView);
        TextView tv_done = bottomSheetDialog_templateList1.findViewById(R.id.tv_done);
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
                    from_ac = select_userLinkedGmailList.get(0).getType();
                    from_ac_id = String.valueOf(select_userLinkedGmailList.get(0).getId());
                    ev_from.setText(select_userLinkedGmailList.get(0).getUserEmail());
                }
            }
        });

        bottomSheetDialog_templateList1.show();
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


    private void EmailAPI(String subject, String text, int id, String email) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        JSONObject obj = new JSONObject();

        JSONObject paramObject = new JSONObject();

        paramObject.put("type", SessionManager.getCampaign_type(getApplicationContext()));
        paramObject.put("team_id", 1);
        paramObject.put("organization_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("manage_by", SessionManager.getCampaign_type_name(getApplicationContext()));
        paramObject.put("time", Global.getCurrentTime());
        paramObject.put("date", Global.getCurrentDate());
        paramObject.put("assign_to", user_data.getUser().getId());

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 1; i++) {
            JSONObject paramObject1 = new JSONObject();
            paramObject1.put("prospect_id", id);
            paramObject1.put("email", email);
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

        paramObject.put("content_header", ev_subject.getText().toString());
        paramObject.put("content_body", edit_compose.getHtml().toString());
        paramObject.put("from_ac", from_ac);
        paramObject.put("from_ac_id", from_ac_id);
        obj.put("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is", new Gson().toJson(gsonObject));


        retrofitCalls.manual_task_store(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(Item_List_Email_Detail_activty.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {
                    //  loadingDialog.cancelLoading();
                    loadingDialog.cancelLoading();
                    Intent intent = new Intent(getApplicationContext(), Email_Tankyou.class);
                    intent.putExtra("s_name", "add");
                    startActivity(intent);
                    finish();

                }
                else if (response.body().getHttp_status()==403)
                {
                    loadingDialog.cancelLoading();
                    Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.plan_validation),false);
                }
                else {
                    loadingDialog.cancelLoading();
                }


            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }

    private void Email_execute(String subject, String text, int id, String email, String record_id) throws JSONException {

        //  loadingDialog.showLoadingDialog();
        Log.e("Defuilt id", String.valueOf(defult_id));

        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        JsonObject obj = new JsonObject();

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("content_body", text);
        paramObject.addProperty("content_header", subject);
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("prospect_id", id);
        paramObject.addProperty("record_id", record_id);
        paramObject.addProperty("type", "EMAIL");
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_ggmail_id", select_userLinkedGmailList.get(0).getId());
        paramObject.addProperty("email_recipients", email);
        obj.add("data", paramObject);

        retrofitCalls.Email_execute(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(Item_List_Email_Detail_activty.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                bottomSheetDialog.cancel();
                if (response.body().getHttp_status() == 200) {
                    loadingDialog.cancelLoading();
                    Intent intent = new Intent(getApplicationContext(), Email_Tankyou.class);
                    intent.putExtra("s_name", "add");
                    startActivity(intent);
                    finish();
                }
                else if (response.body().getHttp_status()==403)
                {
                    loadingDialog.cancelLoading();
                    Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.plan_validation),false);
                }
                else if (response.body().getHttp_status() == 406) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage().toString(), false);
                    loadingDialog.cancelLoading();
                } else {
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage().toString(), false);
                    loadingDialog.cancelLoading();
                    /* finish();*/
                }

                Log.e("Main Response is", new Gson().toJson(response.body()));

            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
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

            int resID = mContext.getResources().getIdentifier("my_" + bizcard.getCardName()
                    .replace(" ", "_").toLowerCase(), "drawable", mContext.getPackageName());
            if (resID != 0) {
                Glide.with(mContext.getApplicationContext()).load(resID).into(holder.iv_card);
            }

            holder.layout_select_image.setOnClickListener(v -> {
                String newUrl="",oldUrl="",Newtext="";
                for(int i=0;i<bizcardList.size();i++){
                    if(bizcardList.get(i).isScelect()){
                        oldUrl=Global.bzcard_share+bizcardList.get(i).getId_encoded();
                        bizcardList.get(i).setScelect(false);
                        break;
                    }
                }
                bizcard.setScelect(true);
                newUrl=Global.bzcard_share+bizcard.getId_encoded();

                String curenttext = edit_compose.getText().toString();
                if(!oldUrl.equals("")&& !oldUrl.equals(newUrl)){
                    String changeurl=curenttext.replace(oldUrl,newUrl);
                    Newtext = changeurl;
                }else {
                    Newtext = curenttext+" "+newUrl;
                }
                edit_compose.setText(Newtext);
                edit_compose.setSelection(edit_compose.getText().length());

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
                    if (holder.tv_item.getText().toString().equals("Save current as template")) {
                        showAlertDialogButtonClicked(view);
                    } else {
                        temaplet_id = item.getId();
                        interfaceClick.OnClick(item);
                        for(int i=0;i<bizcardList.size();i++){
                            if(bizcardList.get(i).isScelect()){
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
                holder.im_file.setImageDrawable(mCtx.getResources().getDrawable(item.getFile()));
                holder.line_view.setVisibility(View.GONE);
            } else {
                holder.im_file.setVisibility(View.GONE);
                holder.tv_item.setVisibility(View.VISIBLE);
            }
            if (position == 0) {
                holder.line_view.setVisibility(View.GONE);
            } else if (position == 1) {
                holder.line_view.setVisibility(View.GONE);
            }
            holder.im_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 0) {

                        if (bottombar.getVisibility()==View.VISIBLE)
                        {
                            bottombar.setVisibility(View.GONE);

                        }
                        else {
                            bottombar.setVisibility(View.VISIBLE);

                        }
                    }
                   else if (position == 1) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        final View mView = getLayoutInflater().inflate(R.layout.bzcart_list_dialog_item, null);
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Item_List_Email_Detail_activty.this,
                                R.style.DialogStyle);
                        bottomSheetDialog.setContentView(mView);
                        RecyclerView rv_image_card = bottomSheetDialog.findViewById(R.id.rv_image_card);

                        rv_image_card.setLayoutManager(new LinearLayoutManager(Item_List_Email_Detail_activty.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        rv_image_card.setHasFixedSize(true);
                        cardListAdepter = new CardListAdepter(Item_List_Email_Detail_activty.this, bizcardList,
                                 bottomSheetDialog, interfaceClick);
                        rv_image_card.setAdapter(cardListAdepter);


                        bottomSheetDialog.show();
                    } else if (position == 2) {
                        try {
                            if (Global.isNetworkAvailable(Item_List_Email_Detail_activty.this, MainActivity.mMainLayout)) {
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
    void BZCard_list() throws JSONException {

       // loadingDialog.showLoadingDialog();

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Item_List_Email_Detail_activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.BZcard_User_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Item_List_Email_Detail_activty.this), Global.Device, new RetrofitCallback() {
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
    public static void compareDates(String onlyDate, TextView tv_status, ManualTaskDetailsModel.ManualDetails item) {
        try {

            Date date1 = Global.defoult_date_formate.parse(Global.getCurrentDate());
            Date date2 = Global.defoult_date_formate.parse(onlyDate);


            if (date1.after(date2)) {
                if (item.getStatus().equals("NOT_STARTED")) {
                    tv_status.setText("Due");
                    tv_status.setTextColor(Color.parseColor("#EC5454"));
                } else {
                    tv_status.setText(Global.setFirstLetter(item.getStatus()));
                    tv_status.setTextColor(Color.parseColor("#ABABAB"));
                }
            } else if (date1.before(date2)) {
                if (item.getStatus().equals("NOT_STARTED")) {
                    tv_status.setText("Upcoming");
                    tv_status.setTextColor(Color.parseColor("#2DA602"));
                } else {
                    tv_status.setText(Global.setFirstLetter(item.getStatus()));
                    tv_status.setTextColor(Color.parseColor("#ABABAB"));
                }

            } else if (date1.equals(date2)) {
                if (item.getStatus().equals("NOT_STARTED")) {
                    tv_status.setText("Today");
                } else {
                    tv_status.setText(Global.setFirstLetter(item.getStatus()));
                }
                tv_status.setTextColor(Color.parseColor("#ABABAB"));
            }

            System.out.println();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    class EmailListAdepter extends RecyclerView.Adapter<EmailListAdepter.viewholder> {

        public Context mCtx;
        List<UserLinkedList.UserLinkedGmail> userLinkedGmailList;

        public EmailListAdepter(Context applicationContext, List<UserLinkedList.UserLinkedGmail> userLinkedGmailList) {
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

            holder.tv_item.setText(userLinkedGmailList.get(position).getUserEmail());
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

    void Zoom_Api(Context mCtx) throws JSONException {

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Item_List_Email_Detail_activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("user_tmz_id", signResponseModel.getUser().getUserTimezone().get(0).getValue());
        obj.add("data", paramObject);
        retrofitCalls.zoomIntegrationExists(sessionManager, obj, loadingDialog, token, Global.getVersionname(Item_List_Email_Detail_activty.this), Global.Device, new RetrofitCallback() {
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

    private void broadcast_manu_zoom(Context mCtx) {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.zoom_layout, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Item_List_Email_Detail_activty.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        LinearLayout la_date = bottomSheetDialog.findViewById(R.id.la_date);
        TextView tv_date = bottomSheetDialog.findViewById(R.id.tv_date);
        LinearLayout la_time = bottomSheetDialog.findViewById(R.id.la_time);
        LinearLayout layout_Duration = bottomSheetDialog.findViewById(R.id.layout_Duration);
        TextView tv_time = bottomSheetDialog.findViewById(R.id.tv_time);
        TextView txt_time = bottomSheetDialog.findViewById(R.id.txt_time);
        TextView tc_time_zone = bottomSheetDialog.findViewById(R.id.tc_time_zone);
        TextView tv_done = bottomSheetDialog.findViewById(R.id.tv_done);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        tv_date.setText(formattedDate);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateandTime = sdf.format(new Date());
        tv_time.setText(currentDateandTime);
        la_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenBob(tv_date);
            }
        });
        layout_Duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*select Duration ti zoom */
                Duration_bouttomSheet(mCtx, txt_time);
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
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                /*Create a Zoom Meeting*/
                if (!txt_time.getText().toString().equals("")) {
                    Create_Zoom(tv_date, tv_time, txt_time);
                    bottomSheetDialog.cancel();
                } else {
                    Global.Messageshow(mCtx, mMainLayout, mCtx.getString(R.string.select_meeting), false);
                }


            }
        });
        bottomSheetDialog.show();

    }

    private void Create_Zoom(TextView tv_date, TextView tv_time, TextView txt_time) {
        String Starttime = tv_date.getText().toString() + 'T' + tv_time.getText().toString() + ":00";
        String duration = txt_time.getText().toString();
        String mystring = duration;
        String arr[] = mystring.split(" ", 2);
        String firstWord = arr[0];

        try {
            if (Global.isNetworkAvailable(Item_List_Email_Detail_activty.this, MainActivity.mMainLayout)) {
                /*Create a Zoom Meeting*/
                Zoom_create(Starttime, firstWord);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void Zoom_create(String starttime, String duration) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Item_List_Email_Detail_activty.this);
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
        retrofitCalls.ZoomCreate(sessionManager, obj, loadingDialog, token, Global.getVersionname(Item_List_Email_Detail_activty.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ZoomExists>() {
                    }.getType();
                    ZoomExists zoomExists = new Gson().fromJson(headerString, listType);

                    String curenttext = edit_compose.getText().toString();
                    String Newtext = curenttext +" "+ " \n " + zoomExists.getZoom_meeting_link_with_password();
                    edit_compose.setText(Html.fromHtml(Newtext));
                    edit_compose.setSelection(edit_compose.getText().length());

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }

    private void Duration_bouttomSheet(Context mCtx, TextView txt_time) {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.duration_item_update, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Item_List_Email_Detail_activty.this, R.style.CoffeeDialog);
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

    private int mYear, mMonth, mDay, mHour, mMinute;

    public void OpenBob(TextView tv_date) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Item_List_Email_Detail_activty.this,
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
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String stime = "";
                if (selectedHour + 1 < 10) {
                    stime = "0" + (selectedHour);
                } else {
                    stime = String.valueOf(selectedHour);
                }


                String sminite = "";
                if (selectedMinute < 10) {
                    sminite = "0" + selectedMinute;
                } else {
                    sminite = String.valueOf(selectedMinute);
                }
                tv_time.setText(stime + ":" + sminite);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}