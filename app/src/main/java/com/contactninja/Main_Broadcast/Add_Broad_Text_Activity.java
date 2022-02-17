package com.contactninja.Main_Broadcast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import com.contactninja.Interface.CardClick;
import com.contactninja.Interface.TemplateClick;
import com.contactninja.Interface.TextClick;
import com.contactninja.MainActivity;
import com.contactninja.Model.Broadcast_image_list;
import com.contactninja.Model.Broadcate_save_data;
import com.contactninja.Model.CampaignTask;
import com.contactninja.Model.ContecModel;
import com.contactninja.Model.HastagList;
import com.contactninja.Model.TemplateList;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UservalidateModel;
import com.contactninja.R;
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
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak")
public class Add_Broad_Text_Activity extends AppCompatActivity implements View.OnClickListener, TextClick, TemplateClick, ConnectivityReceiver.ConnectivityReceiverListener, CardClick {
    public String template_id_is = "";
    List<Broadcast_image_list> broadcast_image_list = new ArrayList<>();
    CardListAdepter cardListAdepter;
    ImageView iv_back, iv_down;
    BottomSheetDialog bottomSheetDialog_templateList1;
    TextView save_button, tv_use_tamplet, tv_step;
    List<ContecModel.PhoneDatum> select_userLinkedGmailList = new ArrayList<>();
    List<ContecModel.PhoneDatum> userLinkedGmailList = new ArrayList<>();
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    RecyclerView rv_direct_list;
    PicUpTextAdepter picUpTextAdepter;
    TemplateAdepter templateAdepter;
    EditText edit_template, edit_template_name;
    List<HastagList.TemplateText> templateTextList = new ArrayList<>();
    List<TemplateList.Template> templateList = new ArrayList<>();
    CoordinatorLayout mMainLayout;
    String step_no = "1", time = "09:00", sequence_id = "", seq_task_id = "", from_ac = "", from_ac_id = "";
    int minite = 00, day = 1, defult_id;
    TemplateClick templateClick;
    BottomSheetDialog bottomSheetDialog_templateList;
    EditText ev_from;
    private BroadcastReceiver mNetworkReceiver;
    private static long mLastClickTime=0;
    Broadcate_save_data broadcate_save_data=new Broadcate_save_data();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_text);
        mNetworkReceiver = new ConnectivityReceiver();
        templateClick = Add_Broad_Text_Activity.this;
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();
        try {
            if (Global.isNetworkAvailable(Add_Broad_Text_Activity.this, mMainLayout)) {
                Contect_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (Global.isNetworkAvailable(Add_Broad_Text_Activity.this, MainActivity.mMainLayout)) {
                Hastag_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        edit_template.requestFocus();
        if (SessionManager.getTask(getApplicationContext()).size() == 0) {
            String step_id = String.valueOf(SessionManager.getTask(getApplicationContext()).size() + 1);
            String stpe_tyep = SessionManager.getCampaign_type_name(getApplicationContext());
            tv_step.setText("Step#" + step_id + "(" + stpe_tyep + " " + SessionManager.getCampaign_type(getApplicationContext()) + ")");
        } else {
            List<CampaignTask> step = SessionManager.getTask(getApplicationContext());

            int step_id = step.get(0).getStepNo() + 1;
            String stpe_tyep = SessionManager.getCampaign_type_name(getApplicationContext());
            tv_step.setText("Step#" + step_id + "(" + stpe_tyep + " " + SessionManager.getCampaign_type(getApplicationContext()) + ")");

        }

        Intent inten = getIntent();
        Bundle bundle = inten.getExtras();
        String flag = bundle.getString("flag");
        if (flag.equals("edit")) {
            edit_template.setText(bundle.getString("body"));

            seq_task_id = String.valueOf(bundle.getInt("seq_task_id"));
            sequence_id = String.valueOf(bundle.getInt("sequence_id"));

            step_no = String.valueOf(bundle.getInt("step"));

            //  SessionManager.setCampaign_type(bundle.getString("type"));
            //SessionManager.setCampaign_type_name(bundle.getString("manage_by"));

            String stpe_tyep = SessionManager.getCampaign_type_name(getApplicationContext());
            tv_step.setText("Step#" + step_no + "(" + stpe_tyep + " " + SessionManager.getCampaign_type(getApplicationContext()) + ")");


        }
        if (SessionManager.getBroadcast_flag(getApplicationContext()).equals("edit"))
        {
            broadcate_save_data=SessionManager.getBroadcate_save_data(getApplicationContext());
            edit_template.setText(broadcate_save_data.getContent_body());
            from_ac=broadcate_save_data.getFrom_ac();
            from_ac_id=broadcate_save_data.getFrom_ac_id();
            template_id_is=broadcate_save_data.getTemplate_id();
        }

    }


    private void Contect_list() throws JSONException {

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Add_Broad_Text_Activity.this);
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
        retrofitCalls.Contect_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Add_Broad_Text_Activity.this), Global.Device, new RetrofitCallback() {
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


                    Log.e("Size is", "" + new Gson().toJson(userLinkedGmailList));

                    ContecModel.PhoneDatum phoneDatum = new ContecModel.PhoneDatum();
                    phoneDatum.setId(0);
                    phoneDatum.setIsDefault(1);
                    phoneDatum.setPhoneNumber("System Assigned");
                    userLinkedGmailList.add(userLinkedGmailList.size(), phoneDatum);
                    Collections.reverse(userLinkedGmailList);
                    if (userLinkedGmailList.size() == 1) {
                        iv_down.setVisibility(View.GONE);
                    } else if (userLinkedGmailList.size() == 1) {
                        iv_down.setVisibility(View.GONE);
                    } else {
                        iv_down.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < userLinkedGmailList.size(); i++) {

                        Intent inten = getIntent();
                        Bundle bundle = inten.getExtras();
                        String flag = bundle.getString("flag");
                        if (flag.equals("edit")) {
                            if (userLinkedGmailList.get(i).getId().toString().equals(bundle.getString("from_ac_id"))) {
                                ev_from.setText(userLinkedGmailList.get(i).getPhoneNumber());
                                defult_id = userLinkedGmailList.get(i).getId();
                                select_userLinkedGmailList.add(userLinkedGmailList.get(i));
                                from_ac = "USERSMS";
                                from_ac_id = String.valueOf(userLinkedGmailList.get(i).getId());
                            }
                        } else {
                            if (userLinkedGmailList.get(i).getIsDefault().toString().equals("1")) {
                                ev_from.setText(userLinkedGmailList.get(i).getPhoneNumber());
                                defult_id = userLinkedGmailList.get(i).getId();
                                select_userLinkedGmailList.add(userLinkedGmailList.get(i));
                                from_ac = "USERSMS";
                                from_ac_id = String.valueOf(userLinkedGmailList.get(i).getId());
                            }
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


    private void bouttomSheet() {
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.template_list_dialog_item, null);
        bottomSheetDialog_templateList = new BottomSheetDialog(Add_Broad_Text_Activity.this, R.style.CoffeeDialog);
        bottomSheetDialog_templateList.setContentView(mView);
        LinearLayout layout_list_template = bottomSheetDialog_templateList.findViewById(R.id.layout_list_template);
        layout_list_template.setVisibility(View.VISIBLE);
        TextView tv_error = bottomSheetDialog_templateList.findViewById(R.id.tv_error);
        RecyclerView templet_list = bottomSheetDialog_templateList.findViewById(R.id.templet_list);
        templet_list.setVisibility(View.VISIBLE);

        try {
            if (Global.isNetworkAvailable(Add_Broad_Text_Activity.this, MainActivity.mMainLayout)) {
                Template_list(templet_list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        tv_error.setVisibility(View.GONE);


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Add_Broad_Text_Activity.this, mMainLayout);
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


    private void Template_list(RecyclerView templet_list) throws JSONException {
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Add_Broad_Text_Activity.this);
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
        retrofitCalls.Template_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Add_Broad_Text_Activity.this), Global.Device, new RetrofitCallback() {
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
                bottomSheetDialog_templateList.show();
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }


        });


    }

    private void Hastag_list() throws JSONException {

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Add_Broad_Text_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Hastag_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Add_Broad_Text_Activity.this), Global.Device, new RetrofitCallback() {
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


                  /*  HastagList.TemplateText text = new HastagList.TemplateText();
                    text.setFile(R.drawable.ic_a);
                    text.setSelect(false);
                    templateTextList.add(0, text);*/

                    HastagList.TemplateText text1 = new HastagList.TemplateText();
                    text1.setFile(R.drawable.ic_card_blank);
                    text1.setSelect(false);
                    templateTextList.add(0, text1);

                    HastagList.TemplateText text2 = new HastagList.TemplateText();
                    text2.setFile(R.drawable.ic_video);
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
        picUpTextAdepter = new PicUpTextAdepter(getApplicationContext(), templateTextList, this);
        rv_direct_list.setAdapter(picUpTextAdepter);
    }

    private void IntentUI() {
        ev_from = findViewById(R.id.ev_from);
        iv_down = findViewById(R.id.iv_down);
        iv_down.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Next");

        edit_template_name = findViewById(R.id.edit_template_name);
        rv_direct_list = findViewById(R.id.rv_direct_list);
        tv_use_tamplet = findViewById(R.id.tv_use_tamplet);
        edit_template = findViewById(R.id.edit_template);
        tv_use_tamplet.setOnClickListener(this);
        mMainLayout = findViewById(R.id.mMainLayout);
        tv_step = findViewById(R.id.tv_step);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                //Add Api Call
                Global.hideKeyboard(Add_Broad_Text_Activity.this);
                Global.count = 1;
                String body = edit_template.getText().toString();
                if (body.equals("")) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.add_message), false);

                } else {

                    broadcate_save_data.setFrom_ac(from_ac);
                    broadcate_save_data.setFrom_ac_id(from_ac_id);
                    broadcate_save_data.setTemplate_id(template_id_is);
                    broadcate_save_data.setContent_body(edit_template.getText().toString());
                    SessionManager.setBroadcate_save_data(getApplicationContext(),broadcate_save_data);
                    Intent intent=new Intent(getApplicationContext(),Recuring_email_broadcast_activity.class);
                    startActivity(intent);

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
        bottomSheetDialog_templateList1 = new BottomSheetDialog(Add_Broad_Text_Activity.this, R.style.CoffeeDialog);
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
                    from_ac = "USERSMS";
                    from_ac_id = String.valueOf(select_userLinkedGmailList.get(0).getId());
                    ev_from.setText(select_userLinkedGmailList.get(0).getPhoneNumber());
                }
            }
        });

        bottomSheetDialog_templateList1.show();
    }

    @Override
    public void Onclick(Broadcast_image_list broadcastImageList) {
        for (int i = 0; i < broadcast_image_list.size(); i++) {
            if (broadcastImageList.getId() == broadcast_image_list.get(i).getId()) {
                broadcast_image_list.get(i).setScelect(true);
            } else {
                broadcast_image_list.get(i).setScelect(false);
            }
        }
        cardListAdepter.notifyDataSetChanged();
    }

    public void OnClick(@SuppressLint("UnknownNullness") String s) {
        String curenttext = edit_template.getText().toString();
        String Newtext = curenttext + s;
        edit_template.setText(Newtext);
        edit_template.setSelection(edit_template.getText().length());
    }

    @Override
    public void OnClick(TemplateList.Template template) {
        edit_template.setText(template.getContentBody());
        edit_template.setSelection(edit_template.getText().length());
        template_id_is = String.valueOf(template.getId());
        bottomSheetDialog_templateList.dismiss();
    }

    public void showAlertDialogButtonClicked(View view, String body_text) {

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
                } else if (body_text.equals("")) {
                    Global.Messageshow(getApplicationContext(), c_layout, "Enter template Text ", false);

                } else {
                    try {
                        dialog.dismiss();
                        CreateTemplate(body_text, editText.getText().toString(), dialog);

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



    private void CreateTemplate(String body_text, String s, AlertDialog dialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Add_Broad_Text_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("template_name", s);
        String template_slug = s.toUpperCase().replace(" ", "_");
        paramObject.addProperty("template_slug", template_slug);
        paramObject.addProperty("content_body", body_text);
        paramObject.addProperty("type", "SMS");

        obj.add("data", paramObject);
        retrofitCalls.CreateTemplate(sessionManager, obj, loadingDialog, token, Global.getVersionname(Add_Broad_Text_Activity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Global.Messageshow(getApplicationContext(), mMainLayout,
                            response.body().getMessage(), true);
                    dialog.dismiss();
                    bottomSheetDialog_templateList.dismiss();
                } else {
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
                loadingDialog.cancelLoading();
            }
        });


    }

    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();
    }

    static class CardListAdepter extends RecyclerView.Adapter<CardListAdepter.cardListData> {

        Activity activity;
        List<Broadcast_image_list> broadcast_image_list;
        CardClick cardClick;
        BottomSheetDialog bottomSheetDialog;
        TextClick interfaceClick;

        public CardListAdepter(Activity activity, List<Broadcast_image_list> broadcast_image_list,
                               CardClick cardClick, BottomSheetDialog bottomSheetDialog, TextClick interfaceClick) {
            this.activity = activity;
            this.broadcast_image_list = broadcast_image_list;
            this.cardClick = cardClick;
            this.bottomSheetDialog = bottomSheetDialog;
            this.interfaceClick = interfaceClick;
        }


        @NonNull
        @Override
        public cardListData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
            return new cardListData(view);
        }

        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
        @Override
        public void onBindViewHolder(@NonNull cardListData holder, int position) {
            Broadcast_image_list item = this.broadcast_image_list.get(position);


            int resID = activity.getResources().getIdentifier(item.getImagename()
                    .replace(" ", "_").toLowerCase(), "drawable", activity.getPackageName());
            if (resID != 0) {
                Glide.with(activity.getApplicationContext()).load(resID).into(holder.iv_card);
            }
            holder.layout_select_image.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                cardClick.Onclick(item);
                item.setScelect(true);
                bottomSheetDialog.dismiss();
                interfaceClick.OnClick("BzczrdLink");
            });
            if (item.isScelect()) {
                holder.layout_select_image.setBackgroundResource(R.drawable.shape_10_blue);
            } else {
                holder.layout_select_image.setBackground(null);
            }
        }


        @Override
        public int getItemCount() {
            return broadcast_image_list.size();
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
        List<ContecModel.PhoneDatum> userLinkedGmailList;

        public EmailListAdepter(Context applicationContext, List<ContecModel.PhoneDatum> userLinkedGmailList) {
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

            holder.tv_item.setText(userLinkedGmailList.get(position).getPhoneNumber());
            if (holder.iv_selected.isSelected() == false) {
                holder.iv_unselected.setVisibility(View.VISIBLE);
                holder.iv_selected.setVisibility(View.GONE);


            } else if (holder.iv_selected.isSelected() == true) {
                holder.iv_unselected.setVisibility(View.GONE);
                holder.iv_selected.setVisibility(View.VISIBLE);

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
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
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

    class PicUpTextAdepter extends RecyclerView.Adapter<PicUpTextAdepter.viewholder> {

        public Context mCtx;
        List<HastagList.TemplateText> templateTextList;
        TextClick interfaceClick;

        public PicUpTextAdepter(Context applicationContext, List<HastagList.TemplateText> templateTextList, TextClick interfaceClick) {
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

            holder.im_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 0) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        final View mView = getLayoutInflater().inflate(R.layout.bzcart_list_dialog_item, null);
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Add_Broad_Text_Activity.this,
                                R.style.DialogStyle);
                        bottomSheetDialog.setContentView(mView);
                        RecyclerView rv_image_card = bottomSheetDialog.findViewById(R.id.rv_image_card);


                        broadcast_image_list.clear();
                        for (int i = 0; i <= 20; i++) {
                            Broadcast_image_list item = new Broadcast_image_list();
                            if (i % 2 == 0) {
                                item.setId(i);
                                item.setScelect(false);
                                item.setImagename("card_1");
                            } else {
                                item.setId(i);
                                item.setScelect(false);
                                item.setImagename("card_2");
                            }
                            broadcast_image_list.add(item);
                        }
                        rv_image_card.setLayoutManager(new LinearLayoutManager(Add_Broad_Text_Activity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        rv_image_card.setHasFixedSize(true);
                        cardListAdepter = new CardListAdepter(Add_Broad_Text_Activity.this, broadcast_image_list,
                                Add_Broad_Text_Activity.this, bottomSheetDialog, interfaceClick);
                        rv_image_card.setAdapter(cardListAdepter);


                        bottomSheetDialog.show();
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
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.select_templet_layout, parent, false);
            return new viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull viewholder holder, int position) {
            TemplateList.Template item = templateTextList1.get(position);
            holder.tv_item.setText(item.getTemplateName());
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
                        showAlertDialogButtonClicked(view, edit_template.getText().toString());
                        bottomSheetDialog_templateList.cancel();
                    } else {
                        interfaceClick.OnClick(item);

                        bottomSheetDialog_templateList.cancel();
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

}