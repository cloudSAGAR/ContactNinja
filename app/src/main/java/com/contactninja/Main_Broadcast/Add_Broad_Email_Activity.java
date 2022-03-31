package com.contactninja.Main_Broadcast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinalwb.are.AREditText;
import com.chinalwb.are.styles.toolbar.IARE_Toolbar;
import com.chinalwb.are.styles.toolitems.IARE_ToolItem;
import com.contactninja.ARE.ARE_ToolItem_Bold;
import com.contactninja.ARE.ARE_ToolItem_Italic;
import com.contactninja.ARE.ARE_ToolItem_Link;
import com.contactninja.ARE.ARE_ToolItem_Underline;
import com.contactninja.Campaign.Campaign_Overview;
import com.contactninja.Interface.TemplateClick;
import com.contactninja.Interface.TextClick;
import com.contactninja.MainActivity;
import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.Broadcate_save_data;
import com.contactninja.Model.CampaignTask;
import com.contactninja.Model.HastagList;
import com.contactninja.Model.TemplateList;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UserLinkedList;
import com.contactninja.Model.UservalidateModel;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables")
public class Add_Broad_Email_Activity extends AppCompatActivity implements View.OnClickListener, TextClick,
        TemplateClick, ConnectivityReceiver.ConnectivityReceiverListener {
    public static final int PICKFILE_RESULT_CODE = 1;
    static CoordinatorLayout mMainLayout;
    public String template_id_is = "";
    ImageView iv_back;
    List<BZcardListModel.Bizcard> bizcardList = new ArrayList<>();
    CardListAdepter cardListAdepter;
    TextView save_button, tv_use_tamplet;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    static EditText ev_subject;
    LinearLayout top_layout;
    TemplateAdepter templateAdepter;
    RecyclerView rv_direct_list;
    PicUpTextAdepter picUpTextAdepter;
    List<HastagList.TemplateText> templateTextList = new ArrayList<>();
    List<TemplateList.Template> templateList = new ArrayList<>();
    String filePath, from_ac = "", from_ac_id = "";
    BottomSheetDialog bottomSheetDialog_templateList;
    TemplateClick templateClick;
    String step_no = "1", time = "09:00", sequence_id = "", seq_task_id = "";
    int minite = 00, day = 1;
    TextView add_new_contect;
    EditText ev_from;
    ImageView iv_more;
    BottomSheetDialog bottomSheetDialog_templateList1;
    int defult_id;
    List<UserLinkedList.UserLinkedGmail> select_userLinkedGmailList = new ArrayList<>();
    List<UserLinkedList.UserLinkedGmail> userLinkedGmailList = new ArrayList<>();
    private BroadcastReceiver mNetworkReceiver;
    private int amountOfItemsSelected = 0;
    private int FirstTime = 0;
    private long mLastClickTime=0;
    Broadcate_save_data broadcate_save_data=new Broadcate_save_data();
    private IARE_Toolbar mToolbar;
    LinearLayout bottombar;
    static AREditText edit_template;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_email);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        templateClick = Add_Broad_Email_Activity.this;


        IntentUI();
        initToolbar();
        try {
            if (Global.isNetworkAvailable(Add_Broad_Email_Activity.this, MainActivity.mMainLayout)) {
                Hastag_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (Global.isNetworkAvailable(Add_Broad_Email_Activity.this, MainActivity.mMainLayout)) {
                BZCard_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (SessionManager.getBroadcast_flag(getApplicationContext()).equals("edit"))
        {
            broadcate_save_data=SessionManager.getBroadcate_save_data(getApplicationContext());
            ev_subject.setText(broadcate_save_data.getContent_header());
            edit_template.setText(Html.fromHtml(broadcate_save_data.getContent_body()));
            from_ac=broadcate_save_data.getFrom_ac();
            from_ac_id=broadcate_save_data.getFrom_ac_id();
            template_id_is=broadcate_save_data.getTemplate_id();
        }

        edit_template.requestFocus();
        edit_template.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                top_layout.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Intent inten = getIntent();
        Bundle bundle = inten.getExtras();
        String flag = bundle.getString("flag");
        if (SessionManager.getTask(getApplicationContext()).size() == 0) {
            String step_id = String.valueOf(SessionManager.getTask(getApplicationContext()).size() + 1);
            String stpe_tyep = SessionManager.getCampaign_type_name(getApplicationContext());
            add_new_contect.setText("Step#" + step_id + "(" + stpe_tyep + " " + SessionManager.getCampaign_type(getApplicationContext()) + ")");
        } else {
            List<CampaignTask> step = SessionManager.getTask(getApplicationContext());

            int step_id = step.get(0).getStepNo() + 1;
            String stpe_tyep = SessionManager.getCampaign_type_name(getApplicationContext());
            add_new_contect.setText("Step#" + step_id + "(" + stpe_tyep + " " + SessionManager.getCampaign_type(getApplicationContext()) + ")");

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Mail_listDetails();
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
    private void Mail_listDetails() {
        userLinkedGmailList = sessionManager.getUserLinkedGmail(getApplicationContext());
        if (userLinkedGmailList.size() == 0) {
            if (FirstTime == 0) {
                FirstTime = 1;
                startActivity(new Intent(getApplicationContext(), Verification_web.class));
            }
            iv_more.setVisibility(View.GONE);
        } else if (userLinkedGmailList.size() == 1) {
            iv_more.setVisibility(View.GONE);
        } else {
            iv_more.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < userLinkedGmailList.size(); i++) {

            Intent inten = getIntent();
            Bundle bundle = inten.getExtras();
            String flag = bundle.getString("flag");
            if (flag.equals("edit")) {
                if (userLinkedGmailList.get(i).getId().toString().equals(bundle.getString("from_ac_id"))) {
                    ev_from.setText(userLinkedGmailList.get(i).getUserEmail());
                    defult_id = userLinkedGmailList.get(i).getId();
                    select_userLinkedGmailList.add(userLinkedGmailList.get(i));
                    from_ac = userLinkedGmailList.get(i).getType();
                    from_ac_id = String.valueOf(userLinkedGmailList.get(i).getId());

                }
            } else {
                if (userLinkedGmailList.get(i).getIsDefault().toString().equals("1")) {
                    ev_from.setText(userLinkedGmailList.get(i).getUserEmail());
                    defult_id = userLinkedGmailList.get(i).getId();
                    select_userLinkedGmailList.add(userLinkedGmailList.get(i));
                    from_ac = userLinkedGmailList.get(i).getType();
                    from_ac_id = String.valueOf(userLinkedGmailList.get(i).getId());

                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        finish();

        super.onBackPressed();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Add_Broad_Email_Activity.this, mMainLayout);
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


    private void Hastag_list() throws JSONException {

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Add_Broad_Email_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Hastag_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Add_Broad_Email_Activity.this), Global.Device, new RetrofitCallback() {
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




                    HastagList.TemplateText templateText = new HastagList.TemplateText();
                    templateText.setDescription("Placeholders #");
                    templateText.setSelect(true);
                    templateTextList.add(2, templateText);


                    Listset(templateTextList);

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
        picUpTextAdepter = new PicUpTextAdepter(Add_Broad_Email_Activity.this, templateTextList, this);
        rv_direct_list.setAdapter(picUpTextAdepter);
    }

    private void IntentUI() {
        bottombar=findViewById(R.id.bottombar);
        mMainLayout = findViewById(R.id.mMainLayout);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Next");
        ev_subject = findViewById(R.id.ev_subject);
        edit_template = findViewById(R.id.edit_template);
        top_layout = findViewById(R.id.top_layout);
        tv_use_tamplet = findViewById(R.id.tv_use_tamplet);
        tv_use_tamplet.setOnClickListener(this);
        rv_direct_list = findViewById(R.id.rv_direct_list);
        add_new_contect = findViewById(R.id.add_new_contect);
        ev_from = findViewById(R.id.ev_from);
        iv_more = findViewById(R.id.iv_more);
        iv_more.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@SuppressLint("UnknownNullness") View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Global.hideKeyboard(Add_Broad_Email_Activity.this);
                if (ev_subject.getText().toString().equals("")) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, "Add Subject", false);
                } else if (edit_template.getText().toString().equals("")) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, getString(R.string.ComposeEmail), false);
                } else {
                    if (Global.isNetworkAvailable(Add_Broad_Email_Activity.this, mMainLayout)) {

                        broadcate_save_data.setFrom_ac(from_ac);
                        broadcate_save_data.setFrom_ac_id(from_ac_id);
                        broadcate_save_data.setTemplate_id(template_id_is);
                        broadcate_save_data.setContent_body(edit_template.getHtml().toString());
                        broadcate_save_data.setContent_header(ev_subject.getText().toString());
                        SessionManager.setBroadcate_save_data(getApplicationContext(),broadcate_save_data);

                         startActivity(new Intent(getApplicationContext(),Recuring_email_broadcast_activity.class));

                         //  StepData();
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
            case R.id.iv_more:
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
        bottomSheetDialog_templateList1 = new BottomSheetDialog(Add_Broad_Email_Activity.this, R.style.CoffeeDialog);
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

    private void bouttomSheet() {
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.template_list_dialog_item, null);
        bottomSheetDialog_templateList = new BottomSheetDialog(Add_Broad_Email_Activity.this, R.style.CoffeeDialog);
        bottomSheetDialog_templateList.setContentView(mView);
        LinearLayout layout_list_template = bottomSheetDialog_templateList.findViewById(R.id.layout_list_template);
        layout_list_template.setVisibility(View.VISIBLE);
        TextView tv_error = bottomSheetDialog_templateList.findViewById(R.id.tv_error);
        RecyclerView templet_list = bottomSheetDialog_templateList.findViewById(R.id.templet_list);
        templet_list.setVisibility(View.VISIBLE);
        try {
            if (Global.isNetworkAvailable(Add_Broad_Email_Activity.this, MainActivity.mMainLayout)) {
                Template_list(templet_list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        tv_error.setVisibility(View.GONE);

        bottomSheetDialog_templateList.show();
    }

    private void Template_list(RecyclerView templet_list) throws JSONException {
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Add_Broad_Email_Activity.this);
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
        retrofitCalls.Template_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Add_Broad_Email_Activity.this), Global.Device, new RetrofitCallback() {
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
                templateAdepter = new TemplateAdepter(getApplicationContext(), templateList, templateClick, edit_template, ev_subject);
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
        String Newtext = curenttext +" "+ s +" ";
        edit_template.setText(Newtext);
        edit_template.setSelection(edit_template.getText().length());
    }

    @Override
    public void OnClick(TemplateList.Template template) {
        ev_subject.setText(template.getContentHeader());
        edit_template.setText(template.getContentBody());
        edit_template.setSelection(edit_template.getText().length());
        template_id_is = String.valueOf(template.getId());
        bottomSheetDialog_templateList.dismiss();
    }

    public void showAlertDialogButtonClicked(View view, EditText edit_template) {

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
        EditText edt_template_name = customLayout.findViewById(R.id.editText);
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
        TextView tv_add = customLayout.findViewById(R.id.tv_add);
        AlertDialog dialog
                = builder.create();

        dialog.show();
        tv_add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SyntheticAccessor")
            @Override
            public void onClick(View v) {
                if (edt_template_name.getText().toString().equals("")) {
                    Global.Messageshow(getApplicationContext(), c_layout, "Enter template name ", false);
                } else if (edit_template.equals("")) {
                    Global.Messageshow(getApplicationContext(), c_layout, "Enter template Text ", false);

                } else {
                    try {
                        if (Global.isNetworkAvailable(Add_Broad_Email_Activity.this, Add_Broad_Email_Activity.mMainLayout)) {
                            if (isValidation(edit_template, ev_subject, edt_template_name))
                                CreateTemplate(edit_template, ev_subject, edt_template_name, dialog);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    public void StepData() {

        Intent inten = getIntent();
        Bundle bundle = inten.getExtras();
        String flag = bundle.getString("flag");

        JsonObject obj = new JsonObject();
        if (flag.equals("add")) {
            loadingDialog.showLoadingDialog();
            SignResponseModel user_data = SessionManager.getGetUserdata(this);

            try {
                if (!SessionManager.getTask(getApplicationContext()).equals(null)) {

                    CampaignTask main_data = SessionManager.getTask(getApplicationContext()).get(0);

                    int step = main_data.getStepNo() + 1;
                    step_no = String.valueOf(step);
                    day = Integer.parseInt(SessionManager.getCampaign_Day(getApplicationContext()));
                    minite = Integer.parseInt(SessionManager.getCampaign_minute(getApplicationContext()));

                    if (SessionManager.getTask(getApplicationContext()).get(0).getSequenceId().toString().equals("null")) {
                        sequence_id = "null";
                    } else {
                        sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId().toString();
                    }
                } else {
                    sequence_id = "null";
                }
            } catch (Exception e) {
                sequence_id = "null";
            }

            JsonObject paramObject = new JsonObject();
            paramObject.addProperty("content_body", edit_template.getHtml().toString());
            paramObject.addProperty("day", day);
            paramObject.addProperty("manage_by", SessionManager.getCampaign_type_name(getApplicationContext()));
            paramObject.addProperty("minute", minite);
            paramObject.addProperty("organization_id", 1);
            if (!template_id_is.equals("")) {
                paramObject.addProperty("template_id", template_id_is);

            }

            if (!sequence_id.equals("null")) {
                paramObject.addProperty("sequence_id", sequence_id);
            }
            paramObject.addProperty("content_header", ev_subject.getText().toString());
            paramObject.addProperty("step_no", step_no);
            paramObject.addProperty("team_id", 1);
            paramObject.addProperty("type", SessionManager.getCampaign_type(getApplicationContext()));
            paramObject.addProperty("time", Global.getCurrentTime());
            paramObject.addProperty("user_id", user_data.getUser().getId());
            paramObject.addProperty("from_ac", from_ac);
            paramObject.addProperty("from_ac_id", from_ac_id);

            obj.add("data", paramObject);
        } else {
            SignResponseModel user_data = SessionManager.getGetUserdata(this);
            String user_id = String.valueOf(user_data.getUser().getId());
            String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
            String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


            JsonObject paramObject = new JsonObject();
            paramObject.addProperty("content_body", edit_template.getHtml().toString());
            paramObject.addProperty("day", Integer.parseInt(SessionManager.getCampaign_Day(getApplicationContext())));
            paramObject.addProperty("manage_by", SessionManager.getCampaign_type_name(getApplicationContext()));
            paramObject.addProperty("minute", Integer.parseInt(SessionManager.getCampaign_minute(getApplicationContext())));
            paramObject.addProperty("organization_id", "1");
            Log.e("S_id", sequence_id);
            Log.e("S_task", seq_task_id);
            paramObject.addProperty("sequence_id", sequence_id);
            paramObject.addProperty("seq_task_id", seq_task_id);
            paramObject.addProperty("content_header", ev_subject.getText().toString());
            paramObject.addProperty("team_id", "1");
            paramObject.addProperty("type", SessionManager.getCampaign_type(getApplicationContext()));
            paramObject.addProperty("time", Global.getCurrentTime());
            paramObject.addProperty("user_id", user_id);
            paramObject.addProperty("step_no", step_no);
            paramObject.addProperty("from_ac", from_ac);
            paramObject.addProperty("from_ac_id", from_ac_id);
            if (!template_id_is.equals("")) {
                paramObject.addProperty("template_id", template_id_is);

            }
            obj.add("data", paramObject);

        }

        retrofitCalls.Task_store(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(Add_Broad_Email_Activity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                //Log.e("Response is",new Gson().toJson(response));

                loadingDialog.cancelLoading();

                if (response.body().getHttp_status() == 200) {

                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<List<CampaignTask>>() {
                    }.getType();
                    List<CampaignTask> user_model1 = new Gson().fromJson(headerString, listType);
                    Intent inten = getIntent();
                    Bundle bundle = inten.getExtras();
                    String flag = bundle.getString("flag");
                    if (!flag.equals("edit")) {
                        SessionManager.setTask(getApplicationContext(), user_model1);
                        startActivity(new Intent(getApplicationContext(), Campaign_Overview.class));
                    }
                    finish();

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

    private void CreateTemplate(EditText edit_template, EditText ev_subject, EditText edt_template_name, AlertDialog dialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Add_Broad_Email_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("template_name", edt_template_name.getText().toString().trim());
        paramObject.addProperty("content_header", ev_subject.getText().toString().trim());
        String template_slug = edt_template_name.getText().toString().toUpperCase().replace(" ", "_");
        paramObject.addProperty("template_slug", template_slug);
        paramObject.addProperty("content_body", edit_template.getText().toString().trim());
        paramObject.addProperty("type", "EMAIL");

        obj.add("data", paramObject);
        retrofitCalls.CreateTemplate(sessionManager, obj, loadingDialog, token, Global.getVersionname(Add_Broad_Email_Activity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Global.Messageshow(getApplicationContext(), mMainLayout,
                            response.body().getMessage(), true);
                    dialog.dismiss();
                    bottomSheetDialog_templateList.dismiss();
                } else {
                    dialog.dismiss();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    UservalidateModel user_model = new Gson().fromJson(headerString, listType);
                    if (user_model.getTemplate_slug() != null) {
                        Global.Messageshow(getApplicationContext(), mMainLayout,
                                "The template title has already been taken.", false);
                    }
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                dialog.dismiss();
                loadingDialog.cancelLoading();
            }


        });


    }

    private boolean isValidation(EditText editTemplate, EditText ev_subject, EditText edt_template_name) {
        if (edt_template_name.getText().toString().equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.template_name), false);
        }
        if (ev_subject.getText().toString().equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.template_subject), false);
        }
        if (editTemplate.getText().toString().equals("")) {
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
                    Newtext = curenttext+" "+newUrl;
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


    class EmailListAdepter extends RecyclerView.Adapter<EmailListAdepter.viewholder> {

        public Context mCtx;
        List<UserLinkedList.UserLinkedGmail> userLinkedGmailList;

        public EmailListAdepter(Context applicationContext, List<UserLinkedList.UserLinkedGmail> userLinkedGmailList) {
            this.mCtx = applicationContext;
            this.userLinkedGmailList = userLinkedGmailList;
        }

        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.email_select_layout, parent, false);
            return new viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull viewholder holder, int position) {

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

    class TemplateAdepter extends RecyclerView.Adapter<TemplateAdepter.viewholder> {

        public Context mCtx;
        List<TemplateList.Template> templateTextList1;
        TemplateClick interfaceClick;
        EditText edit_template, ev_subject;

        public TemplateAdepter(Context applicationContext, List<TemplateList.Template> templateTextList1,
                               TemplateClick interfaceClick, EditText edit_template, EditText ev_subject) {
            this.mCtx = applicationContext;
            this.templateTextList1 = templateTextList1;
            this.interfaceClick = interfaceClick;
            this.edit_template = edit_template;
            this.ev_subject = ev_subject;
        }

        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.select_templet_layout, parent, false);
            return new viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull viewholder holder, int position) {
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
                        showAlertDialogButtonClicked(view, edit_template);
                    } else {
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
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.template_text_selecte, parent, false);
            return new viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull viewholder holder, int position) {
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
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Add_Broad_Email_Activity.this,
                                R.style.DialogStyle);
                        bottomSheetDialog.setContentView(mView);
                        RecyclerView rv_image_card = bottomSheetDialog.findViewById(R.id.rv_image_card);
                        LinearLayout lay_no_list = bottomSheetDialog.findViewById(R.id.lay_no_list);

                        if(bizcardList.size()!=0){
                            rv_image_card.setVisibility(View.VISIBLE);
                            lay_no_list.setVisibility(View.GONE);
                        }else {
                            rv_image_card.setVisibility(View.GONE);
                            lay_no_list.setVisibility(View.VISIBLE);
                        }
                        rv_image_card.setLayoutManager(new LinearLayoutManager(Add_Broad_Email_Activity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        rv_image_card.setHasFixedSize(true);
                        cardListAdepter = new CardListAdepter(Add_Broad_Email_Activity.this, bizcardList,
                                bottomSheetDialog, interfaceClick);
                        rv_image_card.setAdapter(cardListAdepter);

                        bottomSheetDialog.show();
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

     //   loadingDialog.showLoadingDialog();

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Add_Broad_Email_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.BZcard_User_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Add_Broad_Email_Activity.this), Global.Device, new RetrofitCallback() {
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

}