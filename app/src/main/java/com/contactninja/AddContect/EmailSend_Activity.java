package com.contactninja.AddContect;


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
import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.HastagList;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables")
public class EmailSend_Activity extends AppCompatActivity implements View.OnClickListener, TextClick,
        TemplateClick, ConnectivityReceiver.ConnectivityReceiverListener {
    public static final int PICKFILE_RESULT_CODE = 1;
    SessionManager sessionManager;
    BottomSheetDialog bottomSheetDialog;
    List<BZcardListModel.Bizcard> bizcardList=new ArrayList<>();
    CardListAdepter cardListAdepter;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back;
    TextView save_button, tv_use_tamplet;
    CoordinatorLayout mMainLayout;
    TemplateAdepter templateAdepter;
    RecyclerView rv_direct_list;
    PicUpTextAdepter picUpTextAdepter;
    List<HastagList.TemplateText> templateTextList = new ArrayList<>();
    List<TemplateList.Template> templateList = new ArrayList<>();
    String filePath;
    BottomSheetDialog bottomSheetDialog_templateList;
    TemplateClick templateClick;

    static EditText  ev_subject, ev_to, ev_from, ev_titale;
    String email = "", id = "", task_name = "", from_ac = "", from_ac_id = "";
    BottomSheetDialog bottomSheetDialog_templateList1;
    ImageView iv_more;
    int defult_id, temaplet_id = 0, FirstTime = 0;
    List<UserLinkedList.UserLinkedGmail> select_userLinkedGmailList = new ArrayList<>();
    List<UserLinkedList.UserLinkedGmail> userLinkedGmailList = new ArrayList<>();
    private int amountOfItemsSelected = 0;
    private BroadcastReceiver mNetworkReceiver;
    private long mLastClickTime=0;
    private IARE_Toolbar mToolbar;
    LinearLayout bottombar;
    static AREditText edit_template;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_send);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        templateClick = EmailSend_Activity.this;

        IntentUI();
        initToolbar();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email = bundle.getString("email");
        id = bundle.getString("id");
        task_name = bundle.getString("task_name");
        ev_titale.setText(task_name);

        ev_to.setText(email);

        try {
            if (Global.isNetworkAvailable(EmailSend_Activity.this, mMainLayout)) {
                Hastag_list();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (Global.isNetworkAvailable(EmailSend_Activity.this, MainActivity.mMainLayout)) {
                BZCard_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
        edit_template.setToolbar(mToolbar);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Mail_listDetails();
    }

    private void Mail_listDetails() {
        userLinkedGmailList = sessionManager.getUserLinkedGmail(getApplicationContext());
        if (userLinkedGmailList.size() == 0) {
            iv_more.setVisibility(View.GONE);
            if (FirstTime == 0) {
                FirstTime = 1;
                startActivity(new Intent(getApplicationContext(), Verification_web.class));
            }
        } else if (userLinkedGmailList.size() == 1) {
            iv_more.setVisibility(View.GONE);
        } else {
            iv_more.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < userLinkedGmailList.size(); i++) {
            if (userLinkedGmailList.get(i).getIsDefault().toString().equals("1")) {
                ev_from.setText(userLinkedGmailList.get(i).getUserEmail());
                defult_id = userLinkedGmailList.get(i).getId();
                select_userLinkedGmailList.add(userLinkedGmailList.get(i));
                from_ac = userLinkedGmailList.get(i).getType();
                from_ac_id = String.valueOf(userLinkedGmailList.get(i).getId());

            }
        }
    }

    private void IntentUI() {
        bottombar=findViewById(R.id.bottombar);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Next");
        mMainLayout = findViewById(R.id.mMainLayout);
        tv_use_tamplet = findViewById(R.id.tv_use_tamplet);
        tv_use_tamplet.setOnClickListener(this);
        rv_direct_list = findViewById(R.id.rv_direct_list);
        edit_template = findViewById(R.id.edit_compose);
        ev_subject = findViewById(R.id.ev_subject);
        ev_to = findViewById(R.id.ev_to);
        ev_from = findViewById(R.id.ev_from);
        iv_more = findViewById(R.id.iv_more);
        iv_more.setOnClickListener(this);
        ev_titale = findViewById(R.id.ev_titale);


    }

    private void Hastag_list() throws JSONException {
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(EmailSend_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Hastag_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(EmailSend_Activity.this), Global.Device, new RetrofitCallback() {
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
        picUpTextAdepter = new PicUpTextAdepter(EmailSend_Activity.this, templateTextList, this);
        rv_direct_list.setAdapter(picUpTextAdepter);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(EmailSend_Activity.this, mMainLayout);
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
                finish();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (ev_subject.getText().toString().equals("")) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, "Add Subject", false);
                } else if (edit_template.getText().toString().equals("")) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, getString(R.string.ComposeEmail), false);

                } else {

                    broadcast_manu();
                    /*try {

                        EmailAPI(ev_subject.getText().toString(), edit_template.getText().toString(), Integer.parseInt(id), email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }

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

        }
    }

    private void broadcast_manu() {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.mail_bottom_sheet, null);
        bottomSheetDialog = new BottomSheetDialog(EmailSend_Activity.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        LinearLayout lay_sendnow = bottomSheetDialog.findViewById(R.id.lay_sendnow);
        LinearLayout lay_schedule = bottomSheetDialog.findViewById(R.id.lay_schedule);
        lay_sendnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (Global.isNetworkAvailable(EmailSend_Activity.this, mMainLayout)) {
                        EmailAPI(ev_subject.getText().toString(), edit_template.getHtml().toString(), Integer.parseInt(id), email);
                    }
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
                intent.putExtra("body", edit_template.getHtml().toString());
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
        bottomSheetDialog_templateList = new BottomSheetDialog(EmailSend_Activity.this, R.style.CoffeeDialog);
        bottomSheetDialog_templateList.setContentView(mView);
        LinearLayout layout_list_template = bottomSheetDialog_templateList.findViewById(R.id.layout_list_template);
        layout_list_template.setVisibility(View.VISIBLE);
        TextView tv_error = bottomSheetDialog_templateList.findViewById(R.id.tv_error);
        RecyclerView templet_list = bottomSheetDialog_templateList.findViewById(R.id.templet_list);
        templet_list.setVisibility(View.VISIBLE);


        try {
            if (Global.isNetworkAvailable(EmailSend_Activity.this, MainActivity.mMainLayout)) {
                Template_list(templet_list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        tv_error.setVisibility(View.GONE);

        bottomSheetDialog_templateList.show();
    }

    private void Template_list(RecyclerView templet_list) throws JSONException {
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(EmailSend_Activity.this);
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
        retrofitCalls.Template_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(EmailSend_Activity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                templateList.clear();
                loadingDialog.cancelLoading();
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
                templateAdepter = new TemplateAdepter(getApplicationContext(), templateList,
                        templateClick);
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
        String Newtext = curenttext + s;
        edit_template.setText(Newtext);
        edit_template.setSelection(edit_template.getText().length());
    }

    @Override
    public void OnClick(TemplateList.Template template) {
        ev_subject.setText(template.getContentHeader());
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
                    Global.Messageshow(getApplicationContext(), c_layout, "Enter template name ", false);
                } else {
                    try {
                        if (Global.isNetworkAvailable(EmailSend_Activity.this, MainActivity.mMainLayout)) {
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
        } else if (edit_template.getText().toString().equals("")) {
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
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(EmailSend_Activity.this);
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
        paramObject.addProperty("content_body", edit_template.getText().toString().trim());
        paramObject.addProperty("type", "EMAIL");

        obj.add("data", paramObject);
        retrofitCalls.CreateTemplate(sessionManager, obj, loadingDialog, token, Global.getVersionname(EmailSend_Activity.this), Global.Device, new RetrofitCallback() {
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
        bottomSheetDialog_templateList1 = new BottomSheetDialog(EmailSend_Activity.this, R.style.CoffeeDialog);
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
           /* JSONArray contect_array = new JSONArray();
            contect_array.put(email);
            paramObject1.put("email_mobile", contect_array);*/
            jsonArray.put(paramObject1);
            break;
        }
        JSONArray contact_group_ids = new JSONArray();
        contact_group_ids.put("");
        paramObject.put("contact_group_ids", contact_group_ids);
        paramObject.put("prospect_id", jsonArray);
        paramObject.put("record_id", "");
        paramObject.put("task_name", ev_titale.getText().toString());
        if (temaplet_id == 0) {
            paramObject.put("template_id", "");

        } else {
            paramObject.put("template_id", temaplet_id);
        }

        paramObject.put("content_header", ev_subject.getText().toString());
        paramObject.put("content_body", edit_template.getHtml().toString());
        paramObject.put("from_ac", from_ac);
        paramObject.put("from_ac_id", from_ac_id);
        obj.put("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is", new Gson().toJson(gsonObject));


        retrofitCalls.manual_task_store(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(EmailSend_Activity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {

                    loadingDialog.cancelLoading();
                    Intent intent = new Intent(getApplicationContext(), Email_Tankyou.class);
                    intent.putExtra("s_name", "add");
                    startActivity(intent);
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

        retrofitCalls.Email_execute(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(EmailSend_Activity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                Log.e("Responsse", new Gson().toJson(response.body()));
                if (response.body().getHttp_status() == 200) {
                    loadingDialog.cancelLoading();
                    finish();
                } else if (response.body().getHttp_status() == 406) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage().toString(), false);
                    loadingDialog.cancelLoading();
                } else {
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage().toString(), false);
                    loadingDialog.cancelLoading();
                    /* finish();*/
                }

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

                String curenttext = edit_template.getText().toString();
                if(!oldUrl.equals("")&& !oldUrl.equals(newUrl)){
                    String changeurl=curenttext.replace(oldUrl,newUrl);
                    Newtext = changeurl;
                }else {
                    Newtext = curenttext+newUrl;
                }
                edit_template.setText(Newtext);
                edit_template.setSelection(edit_template.getText().length());

                bottomSheetDialog.dismiss();
                notifyDataSetChanged();
            });
            if (bizcard.isScelect()) {
                holder.layout_select_image.setBackgroundResource(R.drawable.shape_5_blue);
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
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EmailSend_Activity.this,
                                R.style.DialogStyle);
                        bottomSheetDialog.setContentView(mView);
                        RecyclerView rv_image_card = bottomSheetDialog.findViewById(R.id.rv_image_card);



                        rv_image_card.setLayoutManager(new LinearLayoutManager(EmailSend_Activity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        rv_image_card.setHasFixedSize(true);
                        cardListAdepter = new CardListAdepter(EmailSend_Activity.this, bizcardList,
                                 bottomSheetDialog, interfaceClick);
                        rv_image_card.setAdapter(cardListAdepter);


                        bottomSheetDialog.show();
                    }
                    else if (position==2)
                    {
                        try {
                            if(Global.isNetworkAvailable(EmailSend_Activity.this, MainActivity.mMainLayout)) {
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

      //  loadingDialog.showLoadingDialog();

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(EmailSend_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.BZcard_User_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(EmailSend_Activity.this), Global.Device, new RetrofitCallback() {
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

        SignResponseModel signResponseModel= SessionManager.getGetUserdata(EmailSend_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("user_tmz_id",signResponseModel.getUser().getUserTimezone().get(0).getValue());
        obj.add("data", paramObject);
        retrofitCalls.zoomIntegrationExists(sessionManager,obj, loadingDialog, token,Global.getVersionname(EmailSend_Activity.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ZoomExists>() {
                    }.getType();
                    ZoomExists zoomExists=new Gson().fromJson(headerString, listType);
                    if(zoomExists.getUserExists()){
                        broadcast_manu_zoom(mCtx);
                    }else {
                        Intent intent=new Intent(getApplicationContext(),Verification_web.class);
                        intent.putExtra("Activtiy","zoom");
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
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EmailSend_Activity.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        LinearLayout la_date=bottomSheetDialog.findViewById(R.id.la_date);
        TextView tv_date=bottomSheetDialog.findViewById(R.id.tv_date);
        LinearLayout la_time=bottomSheetDialog.findViewById(R.id.la_time);
        LinearLayout layout_Duration=bottomSheetDialog.findViewById(R.id.layout_Duration);
        TextView tv_time=bottomSheetDialog.findViewById(R.id.tv_time);
        TextView txt_time=bottomSheetDialog.findViewById(R.id.txt_time);
        TextView tc_time_zone=bottomSheetDialog.findViewById(R.id.tc_time_zone);
        TextView tv_done=bottomSheetDialog.findViewById(R.id.tv_done);
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
                Duration_bouttomSheet(mCtx,txt_time);
            }
        });
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        tc_time_zone.setText("Time Zone("+user_data.getUser().getUserTimezone().get(0).getText().toString()+")");
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
                if(!txt_time.getText().toString().equals("")){
                    Create_Zoom(tv_date,tv_time,txt_time);
                    bottomSheetDialog.cancel();

                }else {
                    Global.Messageshow(mCtx,mMainLayout,mCtx.getString(R.string.select_meeting),false);
                }


            }
        });
        bottomSheetDialog.show();

    }

    private void Create_Zoom(TextView tv_date, TextView tv_time, TextView txt_time) {
        String Starttime= tv_date.getText().toString()+'T'+tv_time.getText().toString()+":00";
        String duration = txt_time.getText().toString();
        String mystring = duration;
        String arr[] = mystring.split(" ", 2);
        String firstWord = arr[0];

        try {
            if(Global.isNetworkAvailable(EmailSend_Activity.this, MainActivity.mMainLayout)) {
                /*Create a Zoom Meeting*/
                Zoom_create(Starttime,firstWord);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void Zoom_create(String starttime, String duration) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel= SessionManager.getGetUserdata(EmailSend_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("meeting_name","contact Ninja with");
        paramObject.addProperty("start_time",starttime);
        paramObject.addProperty("duration",duration);
        paramObject.addProperty("description","");
        paramObject.addProperty("timezone",signResponseModel.getUser().getUserTimezone().get(0).getTzname());
        obj.add("data", paramObject);
        retrofitCalls.ZoomCreate(sessionManager,obj, loadingDialog, token,Global.getVersionname(EmailSend_Activity.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success (Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ZoomExists>() {
                    }.getType();
                    ZoomExists zoomExists=new Gson().fromJson(headerString, listType);

                    String curenttext = edit_template.getText().toString();
                    String Newtext = curenttext + " \n "+ zoomExists.getZoom_meeting_link_with_password();
                    edit_template.setText(Newtext);
                    edit_template.setSelection(edit_template.getText().length());

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }
    private void Duration_bouttomSheet(Context mCtx, TextView txt_time ) {

       @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.duration_item_update, null);
       BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EmailSend_Activity.this, R.style.CoffeeDialog);
       bottomSheetDialog.setContentView(mView);
       RadioButton red_5 = bottomSheetDialog.findViewById(R.id.red_5);
       RadioButton red_15 = bottomSheetDialog.findViewById(R.id.red_15);
       RadioButton red_30 = bottomSheetDialog.findViewById(R.id.red_30);
       RadioButton red_45 = bottomSheetDialog.findViewById(R.id.red_45);
       RadioButton red_60 = bottomSheetDialog.findViewById(R.id.red_60);

       bottomSheetDialog.show();

       if(txt_time.getText().toString().equals(mCtx.getResources().getString(R.string.m_5))){
           red_5.setChecked(true);
       }else   if(txt_time.getText().toString().equals(mCtx.getResources().getString(R.string.m_15))){
           red_15.setChecked(true);
       } else   if(txt_time.getText().toString().equals(mCtx.getResources().getString(R.string.m_30))){
           red_30.setChecked(true);
       } else   if(txt_time.getText().toString().equals(mCtx.getResources().getString(R.string.m_45))){
           red_45.setChecked(true);
       } else   if(txt_time.getText().toString().equals(mCtx.getResources().getString(R.string.m_60))){
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(EmailSend_Activity.this,
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

    public void onTimer(TextView tv_time)
    {
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
                tv_time.setText( stime + ":" + sminite);            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

}