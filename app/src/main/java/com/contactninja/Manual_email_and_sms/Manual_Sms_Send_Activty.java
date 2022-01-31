package com.contactninja.Manual_email_and_sms;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Interface.TemplateClick;
import com.contactninja.Interface.TextClick;
import com.contactninja.MainActivity;
import com.contactninja.Model.ContecModel;
import com.contactninja.Model.HastagList;
import com.contactninja.Model.TemplateList;
import com.contactninja.Model.UserData.SignResponseModel;
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
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Manual_Sms_Send_Activty extends AppCompatActivity implements View.OnClickListener, TextClick, TemplateClick,ConnectivityReceiver.ConnectivityReceiverListener{
    public static final int PICKFILE_RESULT_CODE = 1;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    RelativeLayout mMainLayout;

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

    EditText edit_template,ev_task_title,ev_from;
    String p_number = "", id = "",task_name="",from_ac="",from_ac_id="";
    BottomSheetDialog bottomSheetDialog_templateList1;
    ImageView iv_more;
    int defult_id,temaplet_id=0;
    List<ContecModel.PhoneDatum> select_userLinkedGmailList = new ArrayList<>();
    List<ContecModel.PhoneDatum> userLinkedGmailList = new ArrayList<>();
    private int amountOfItemsSelected = 0;
    private BroadcastReceiver mNetworkReceiver;
    ImageView iv_down;
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
        String type=bundle.getString("type");
        task_name=bundle.getString("task_name");
        ev_task_title.setText(task_name);
        ev_from.setText(p_number);
        ev_from.setEnabled(false);

        try {
            if(Global.isNetworkAvailable(Manual_Sms_Send_Activty.this,mMainLayout)){
                Hastag_list();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(Global.isNetworkAvailable(Manual_Sms_Send_Activty.this,mMainLayout)){
                Contect_list();
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
        save_button.setText("Next");
        tv_use_tamplet = findViewById(R.id.tv_use_tamplet);
        tv_use_tamplet.setOnClickListener(this);
        rv_direct_list = findViewById(R.id.rv_direct_list);
        mMainLayout=findViewById(R.id.mMainLayout);
        edit_template=findViewById(R.id.ev_txt);
        iv_submit=findViewById(R.id.iv_submit);
        iv_submit.setOnClickListener(this);
        ev_task_title=findViewById(R.id.ev_task_title);
        ev_from=findViewById(R.id.ev_from);
        iv_down=findViewById(R.id.iv_down);
        iv_down.setOnClickListener(this);


    }

    private void Contect_list() throws JSONException {

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Manual_Sms_Send_Activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("q","");
        paramObject.addProperty("status","");
        paramObject.addProperty("orderBy","");
        paramObject.addProperty("order","");
        paramObject.addProperty("perPage","10");
        paramObject.addProperty("page","1");
        obj.add("data", paramObject);
        retrofitCalls.Contect_list(sessionManager, obj, loadingDialog, token,Global.getVersionname(Manual_Sms_Send_Activty.this),Global.Device, new RetrofitCallback() {
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


                            from_ac="USERSMS";
                            from_ac_id= String.valueOf(userLinkedGmailList.get(i).getId());


                        }
                    }
                    Log.e("List Is", new Gson().toJson(userLinkedGmailList));
                } else {

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
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Manual_Sms_Send_Activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Hastag_list(sessionManager, obj, loadingDialog, token,Global.getVersionname(Manual_Sms_Send_Activty.this),Global.Device, new RetrofitCallback() {
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
                    text1.setFile(R.drawable.ic_file);
                    text1.setSelect(false);
                    templateTextList.add(1, text1);


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
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Manual_Sms_Send_Activty.this, mMainLayout);
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
                showAlertDialogButtonClicked();
                break;
            case R.id.iv_submit:
              //  Log.e("Text is",edit_template.getText().toString());
                if (edit_template.getText().toString().equals(""))
                {
                    Global.Messageshow(getApplicationContext(),mMainLayout,"Add Message",false);
                }
                else {
                    if (SessionManager.getEmail_screen_name(getApplicationContext()).equals("only_sms"))
                    {
                       /* try {
                            SMSAPI(edit_template.getText().toString(), Integer.parseInt(id), p_number);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
*/

                        broadcast_manu();
                    }
                    else {
                        broadcast_manu();
                    }

                }

                break;
            case R.id.tv_use_tamplet:
                bouttomSheet();
                break;
            case R.id.iv_down:
                Email_bouttomSheet();
                break;


        }
    }

    private void Email_bouttomSheet() {
        final View mView = getLayoutInflater().inflate(R.layout.email_bottom_sheet, null);
        bottomSheetDialog_templateList1 = new BottomSheetDialog(Manual_Sms_Send_Activty.this, R.style.CoffeeDialog);
        bottomSheetDialog_templateList1.setContentView(mView);
        TextView tv_done = bottomSheetDialog_templateList1.findViewById(R.id.tv_done);
        RecyclerView email_list = bottomSheetDialog_templateList1.findViewById(R.id.email_list);


        for(int i=0;i<userLinkedGmailList.size();i++){
            if(userLinkedGmailList.get(i).getIsDefault()==1){
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
                if(select_userLinkedGmailList.size()!=0){
                  from_ac="USERSMS";
                  from_ac_id= String.valueOf(select_userLinkedGmailList.get(0).getId());
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

        TextView tv_title=customLayout.findViewById(R.id.tv_title);
        TextView tv_sub_titale=customLayout.findViewById(R.id.tv_sub_titale);
        TextView tv_ok = customLayout.findViewById(R.id.tv_ok);
        tv_title.setText("Are You Sure ?");
        tv_sub_titale.setText("Are you sure that you would like to back  home ?");
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
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Manual_Sms_Send_Activty.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        LinearLayout lay_sendnow=bottomSheetDialog.findViewById(R.id.lay_sendnow);
        LinearLayout lay_schedule=bottomSheetDialog.findViewById(R.id.lay_schedule);
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

                Intent intent=new Intent(getApplicationContext(),Manual_Sms_TaskActivity.class);
                intent.putExtra("text",edit_template.getText().toString());
                intent.putExtra("id",id);
                intent.putExtra("number",p_number);
                intent.putExtra("tem_id",String.valueOf(temaplet_id));
                intent.putExtra("task_name",task_name);
                intent.putExtra("from_ac",from_ac);
                intent.putExtra("from_ac_id",from_ac_id);
                startActivity(intent);
                finish();


            }
        });
        bottomSheetDialog.show();

    }
    private void SMSAPI(String text, int id, String email) throws JSONException {

        Log.e("Phone Number", email);
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
        paramObject.put("time", Global.getCurrentTime());
        paramObject.put("date", Global.getCurrentDate());
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
        paramObject.put("record_id","");
        paramObject.put("task_name",task_name);
        if (temaplet_id==0)
        {
            paramObject.put("template_id","");

        }
        else {
            paramObject.put("template_id",temaplet_id);
        }
        //paramObject.put("content_header","");
        paramObject.put("content_body",edit_template.getText().toString());
        paramObject.put("from_ac",from_ac);
        paramObject.put("from_ac_id",from_ac_id);
        obj.put("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is", new Gson().toJson(gsonObject));


        retrofitCalls.manual_task_store(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager),Global.getVersionname(this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {
                        loadingDialog.cancelLoading();
                        loadingDialog.cancelLoading();
                        Intent intent=new Intent(getApplicationContext(),Email_Tankyou.class);
                        intent.putExtra("s_name","add");
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

        retrofitCalls.Email_execute(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this),Global.Device,new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                edit_template.setText("");
                loadingDialog.cancelLoading();
                Intent intent=new Intent(getApplicationContext(),Email_Tankyou.class);
                intent.putExtra("s_name","add");
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
        bottomSheetDialog_templateList = new BottomSheetDialog(Manual_Sms_Send_Activty.this, R.style.CoffeeDialog);
        bottomSheetDialog_templateList.setContentView(mView);
        //  LinearLayout layout_list_template=bottomSheetDialog.findViewById(R.id.layout_list_template);
        TextView tv_error = bottomSheetDialog_templateList.findViewById(R.id.tv_error);
        RecyclerView templet_list = bottomSheetDialog_templateList.findViewById(R.id.templet_list);
        templet_list.setVisibility(View.VISIBLE);
        try {
            if (Global.isNetworkAvailable(Manual_Sms_Send_Activty.this, MainActivity.mMainLayout)) {
                Template_list(templet_list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        tv_error.setVisibility(View.GONE);

        bottomSheetDialog_templateList.show();
    }

    private void Template_list(RecyclerView templet_list) throws JSONException {
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Manual_Sms_Send_Activty.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Template_list(sessionManager, obj, loadingDialog, token,Global.getVersionname(Manual_Sms_Send_Activty.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {
                    loadingDialog.cancelLoading();
                    templateList.clear();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<TemplateList>() {
                    }.getType();
                    TemplateList list = new Gson().fromJson(headerString, listType);
                    String CurrentType = SessionManager.getCampaign_type(getApplicationContext());

                    TemplateList.Template template2 = new TemplateList.Template();
                    template2.setTemplateName("Please select template");
                    template2.setSelect(false);
                    templateList.add(0, template2);

                    for (int i = 0; i < list.getTemplate().size(); i++) {
                        if (CurrentType.equals("Email")) {
                            if (list.getTemplate().get(i).getType().equals("EMAIL")) {
                                TemplateList.Template template = new TemplateList.Template();
                                template.setId(list.getTemplate().get(i).getId());
                                template.setOrganizationId(list.getTemplate().get(i).getOrganizationId());
                                template.setTeamId(list.getTemplate().get(i).getTeamId());
                                template.setTemplateName(list.getTemplate().get(i).getTemplateName());
                                template.setTemplateSlug(list.getTemplate().get(i).getTemplateSlug());
                                template.setContentHeader(list.getTemplate().get(i).getContentHeader());
                                template.setType(list.getTemplate().get(i).getType());
                                template.setContentBody(list.getTemplate().get(i).getContentBody());
                                template.setStatus(list.getTemplate().get(i).getStatus());
                                template.setCreatedAt(list.getTemplate().get(i).getCreatedAt());
                                template.setUpdatedAt(list.getTemplate().get(i).getUpdatedAt());
                                templateList.add(template);
                            }
                        } else {
                            if (list.getTemplate().get(i).getType().equals("SMS")) {
                                TemplateList.Template template = new TemplateList.Template();
                                template.setId(list.getTemplate().get(i).getId());
                                template.setOrganizationId(list.getTemplate().get(i).getOrganizationId());
                                template.setTeamId(list.getTemplate().get(i).getTeamId());
                                template.setTemplateName(list.getTemplate().get(i).getTemplateName());
                                template.setTemplateSlug(list.getTemplate().get(i).getTemplateSlug());
                                template.setContentHeader(list.getTemplate().get(i).getContentHeader());
                                template.setType(list.getTemplate().get(i).getType());
                                template.setContentBody(list.getTemplate().get(i).getContentBody());
                                template.setStatus(list.getTemplate().get(i).getStatus());
                                template.setCreatedAt(list.getTemplate().get(i).getCreatedAt());
                                template.setUpdatedAt(list.getTemplate().get(i).getUpdatedAt());
                                templateList.add(template);
                            }
                        }
                    }


                    TemplateList.Template template1 = new TemplateList.Template();
                    template1.setTemplateName("Save Current as template");
                    template1.setSelect(true);
                    templateList.add(templateList.size(), template1);


                    templet_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    templateAdepter = new TemplateAdepter(getApplicationContext(), templateList, templateClick);
                    templet_list.setAdapter(templateAdepter);

                }
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
        EditText editText = customLayout.findViewById(R.id.editText);
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
        TextView tv_add = customLayout.findViewById(R.id.tv_add);
        AlertDialog dialog
                = builder.create();

        dialog.show();
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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
                    if (holder.tv_item.getText().toString().equals("Save Current as template")) {
                        showAlertDialogButtonClicked(view);
                    } else {
                        temaplet_id=item.getId();
                        interfaceClick.OnClick(item);
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
        public PicUpTextAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.template_text_selecte, parent, false);
            return new PicUpTextAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PicUpTextAdepter.viewholder holder, int position) {
            HastagList.TemplateText item = templateTextList.get(position);
            holder.tv_item.setText(item.getDescription());
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
                    if (position == 1) {
                        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                        chooseFile.setType("*/*");
                        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
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


            if (userLinkedGmailList.get(position).isEmailSelect())
            {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            }
            else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }


            holder.layout_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i=0; i<userLinkedGmailList.size();i++){
                        if (userLinkedGmailList.get(i).isEmailSelect())
                        {
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

    @Override
    public void onBackPressed() {
        showAlertDialogButtonClicked();
        //super.onBackPressed();
    }

}