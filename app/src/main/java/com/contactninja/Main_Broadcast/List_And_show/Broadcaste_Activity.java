package com.contactninja.Main_Broadcast.List_And_show;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.Main_Broadcast.Broadcast_Preview;
import com.contactninja.Main_Broadcast.Broadcaste_viewContect;
import com.contactninja.Model.BroadcastActivityModel;
import com.contactninja.Model.Broadcate_save_data;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Broadcaste_Activity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    ImageView iv_back, iv_Setting, image_icon, image_step, iv_toolbar_manu_vertical;
    TextView save_button;
    List<BroadcastActivityModel.BroadcastProspect> broadcastProspects;
    LinearLayout main_layout;
    SessionManager sessionManager;
    int sequence_id, seq_task_id;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    TextView tv_email, tv_sms, tv_contect, tv_pending, tv_contec_reach, tv_camp_name;
    BroadcastActivityModel._0 broadcasteda;
    TextView tv_contect1, tv_exposure, tv_status, tv_title,
            tv_date, tv_repete_type, ev_subject, tv_detail;
    LinearLayout layout_email_subject;
    Broadcate_save_data broadcate_save_data;
    int Broadcate_ID = 0;
    private long mLastClickTime = 0;
    private BroadcastReceiver mNetworkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcaste);
        mNetworkReceiver = new ConnectivityReceiver();
        broadcate_save_data = new Broadcate_save_data();
        // broadcasteda=SessionManager.getBroadcate_List_Detail(this);
        Log.e("Broadcaste Data", new Gson().toJson(broadcasteda));


        Intent inten = getIntent();
        Bundle bundle = inten.getExtras();
        Broadcate_ID = bundle.getInt("id");

        IntentUI();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);


    }

    public void setData() {

        if (broadcasteda.getType().equals("SMS")) {
            image_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_message_tab));
            tv_status.setText(broadcasteda.getBroadcastName());
            image_step.setImageDrawable(getResources().getDrawable(R.drawable.ic_message_tab));
            layout_email_subject.setVisibility(View.GONE);
           // tv_detail.setTypeface(null, Typeface.BOLD);
        } else {
            layout_email_subject.setVisibility(View.VISIBLE);
            image_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_email));
            image_step.setImageDrawable(getResources().getDrawable(R.drawable.ic_email));
            tv_status.setText(broadcasteda.getBroadcastName());
        }
        String manageby= "";
        if(broadcasteda.getManageBy().equals("AUTO")){
            manageby="Automated";
        }else {
            manageby="Manual";
        }
        switch (broadcasteda.getStatus()) {
            case "A":
                tv_title.setText("Active - "+manageby
                        + " " + broadcasteda.getType().toLowerCase());
                tv_title.setTextColor(getResources().getColor(R.color.text_green));
                break;
            case "I":
                if (Global.IsNotNull(broadcasteda.getFirstActivated())) {
                    tv_title.setText("Paused - "+manageby
                            + " " + broadcasteda.getType().toLowerCase());
                    tv_title.setTextColor(getResources().getColor(R.color.tv_push_color));
                } else {
                    tv_title.setText("Inactive - "+manageby
                            + " " + broadcasteda.getType().toLowerCase());
                    tv_title.setTextColor(getResources().getColor(R.color.red));
                }
                break;
        }

        tv_date.setText(Global_Time.DateFormateMonth(broadcasteda.getStartDate()) + " @ " + Global_Time.TimeFormateAMPM(broadcasteda.getStartTime()));

        switch (broadcasteda.getRecurringType()) {
            case "D":
                tv_repete_type.setText("Daily");
                break;
            case "W":
                tv_repete_type.setText("Weekly");
                break;
            case "M":
                tv_repete_type.setText("Monthly");
                break;
        }
        ev_subject.setText(broadcasteda.getContentHeader());
        tv_detail.setText(Html.fromHtml(broadcasteda.getContentBody()));
    }

    private void IntentUI() {
        iv_toolbar_manu_vertical = findViewById(R.id.iv_toolbar_manu_vertical);
        iv_toolbar_manu_vertical.setOnClickListener(this);
        iv_toolbar_manu_vertical.setVisibility(View.VISIBLE);

        layout_email_subject = findViewById(R.id.layout_email_subject);
        tv_detail = findViewById(R.id.tv_detail);
        ev_subject = findViewById(R.id.ev_subject);
        tv_repete_type = findViewById(R.id.tv_repete_type);
        tv_date = findViewById(R.id.tv_date);
        tv_title = findViewById(R.id.tv_title);
        image_step = findViewById(R.id.image_step);
        tv_status = findViewById(R.id.tv_status);
        image_icon = findViewById(R.id.image_icon);
        tv_exposure = findViewById(R.id.tv_exposure);
        tv_contect1 = findViewById(R.id.tv_contect1);
        main_layout = findViewById(R.id.main_layout);
        tv_camp_name = findViewById(R.id.tv_camp_name);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setEnabled(
                false
        );
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.GONE);
        iv_back.setOnClickListener(this);
        save_button.setOnClickListener(this);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText(getString(R.string.view_contect));
        save_button.setTextColor(getResources().getColor(R.color.purple_200));
        tv_email = findViewById(R.id.tv_email);
        tv_sms = findViewById(R.id.tv_sms);
        tv_contect = findViewById(R.id.tv_contect);
        tv_pending = findViewById(R.id.tv_pending);
        tv_contec_reach = findViewById(R.id.tv_contec_reach);
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
                Intent intent = new Intent(getApplicationContext(), Broadcaste_viewContect.class);
                startActivity(intent);
                break;
            case R.id.iv_toolbar_manu_vertical:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                broadcast_manu();
                break;
        }
    }

    private void broadcast_manu() {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.brodcaste_broadcaste_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Broadcaste_Activity.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        TextView selected_campaign = bottomSheetDialog.findViewById(R.id.selected_campaign);
        TextView selected_broadcast = bottomSheetDialog.findViewById(R.id.selected_broadcast);
        TextView selected_task = bottomSheetDialog.findViewById(R.id.selected_task);
        TextView romove_task = bottomSheetDialog.findViewById(R.id.romove_task);

        if (!broadcasteda.getStatus().equals("A")) {
            romove_task.setVisibility(View.VISIBLE);

        }
        try {
            switch (broadcasteda.getStatus()) {
                case "A":
                    selected_broadcast.setText("Pause Broadcast");
                    selected_broadcast.setVisibility(View.VISIBLE);
                    break;
                case "I":
                    selected_broadcast.setText("Active Broadcast");
                    selected_broadcast.setVisibility(View.VISIBLE);
                    break;

            }
            selected_task.setText("Edit Broadcast");
            selected_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    if (broadcasteda.getStatus().equals("A")) {

                        Global.Messageshow(getApplicationContext(), main_layout, getResources().getString(R.string.push_broadcast), false);
                    } else {
                        SessionManager.setCampaign_type_name(broadcasteda.getManageBy());
                        SessionManager.setCampaign_type(broadcasteda.getType());
                        SessionManager.setBroadcast_flag("edit");
                        broadcate_save_data.setBroadcastname(broadcasteda.getBroadcastName());
                        broadcate_save_data.setContent_body(broadcasteda.getContentBody());
                        broadcate_save_data.setContent_header(broadcasteda.getContentHeader());
                        broadcate_save_data.setTime(broadcasteda.getStartTime());
                        broadcate_save_data.setDate(broadcasteda.getStartDate());
                        switch (broadcasteda.getRecurringType()) {
                            case "M":
                                broadcate_save_data.setRecurrence("Monthly");

                                BroadcastActivityModel._0.OccursOn r_data = broadcasteda.getRecurringDetail().getOccursOn();
                                try {
                                    if (!r_data.getDay_of_month().equals(null)) {
                                        broadcate_save_data.setDay_of_month(r_data.getDay_of_month());
                                        broadcate_save_data.setOccurs_monthly("Day");
                                    } else {
                                        broadcate_save_data.setEvery_second(r_data.getEveryWeekNo());
                                        broadcate_save_data.setEvery_day(r_data.getEveryDayofweek());
                                        broadcate_save_data.setOccurs_monthly("Every");

                                    }
                                } catch (Exception e) {
                                    broadcate_save_data.setEvery_second(r_data.getEveryWeekNo());
                                    broadcate_save_data.setEvery_day(r_data.getEveryDayofweek());
                                    broadcate_save_data.setOccurs_monthly("Every");

                                }


                                //  broadcate_save_data.setOccurs_weekly(r_data.get(i).getEveryWeekNo());


                                break;
                            case "W":
                                broadcate_save_data.setRecurrence("Weekly");
                                String data = "";
                                for (int i = 0; i < broadcasteda.getRecurringDetail().getOccursOn().getDayOfWeek().size(); i++) {
                                    if (data.equals("")) {

                                        data = broadcasteda.getRecurringDetail().getOccursOn().getDayOfWeek().get(i).toString();

                                    } else {
                                        data = data + "," + broadcasteda.getRecurringDetail().getOccursOn().getDayOfWeek().get(i).toString();

                                    }

                                }
                                broadcate_save_data.setOccurs_weekly(data);

                                break;
                            case "D":
                                broadcate_save_data.setRecurrence("Daily");

                                break;
                        }
                        broadcate_save_data.setRepeat_every(broadcasteda.getRecurringDetail().getRepeatEvery());


                        List<ContectListData.Contact> Contect_List = new ArrayList<>();

                        for (int i = 0; i < broadcastProspects.size(); i++) {
                            ContectListData.Contact contact = new ContectListData.Contact();
                            contact.setFirstname(broadcastProspects.get(i).getFirstname());
                            contact.setId(broadcastProspects.get(i).getContactId());
                            contact.setLastname(broadcastProspects.get(i).getLastname());
                            List<ContectListData.Contact.ContactDetail> contactDetails = new ArrayList<>();
                            ContectListData.Contact.ContactDetail contactDetail = new ContectListData.Contact.ContactDetail();
                            contact.setFlag("false");
                            contactDetail.setContactId(broadcastProspects.get(0).getContactId());
                            contactDetail.setEmailNumber(broadcastProspects.get(0).getContactNumber());
                            contactDetail.setType("NUMBER");
                            contactDetails.add(contactDetail);
                            contact.setContactDetails(contactDetails);
                            Contect_List.add(contact);

                        }
                        broadcate_save_data.setFrom_ac_id(broadcasteda.getSent_tbl_id());
                        broadcate_save_data.setFrom_ac(broadcasteda.getMail_module());
                        broadcate_save_data.setId(String.valueOf(broadcasteda.getId()));
                        SessionManager.setBroadcate_save_data(getApplicationContext(), broadcate_save_data);
                        SessionManager.setGroupList(getApplicationContext(), Contect_List);
                        Intent broad_caste_preview = new Intent(getApplicationContext(), Broadcast_Preview.class);
                        startActivity(broad_caste_preview);
                        finish();
                    }


                    bottomSheetDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        romove_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartBroadCastApi(broadcasteda, 4);
                bottomSheetDialog.cancel();

            }
        });


        selected_broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                switch (broadcasteda.getStatus()) {
                    case "A":
                        StartBroadCastApi(broadcasteda, 1);
                        break;
                    case "I":
                        if (broadcasteda.getFirstActivated() != null && !broadcasteda.getFirstActivated().equals("")) {
                            StartBroadCastApi(broadcasteda, 0);
                        } else {
                            StartBroadCastApi(broadcasteda, 3);
                        }
                        break;
                }
                bottomSheetDialog.dismiss();
            }
        });
        selected_campaign.setVisibility(View.GONE);
        bottomSheetDialog.show();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Broadcaste_Activity.this, main_layout);
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
    protected void onResume() {

        super.onResume();
        try {
            Mail_list();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    void Mail_list() throws JSONException {

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("id", Broadcate_ID);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("q", "");
        paramObject.addProperty("orderBy", "created_at");
        paramObject.addProperty("order", "DESC");
        paramObject.addProperty("perPage", "1");
        paramObject.addProperty("page", "1");
        obj.add("data", paramObject);
        retrofitCalls.Broadcast_Activiy_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {


                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    if (response.body().getHttp_status() == 200) {
                        try {
                            Type listType = new TypeToken<BroadcastActivityModel>() {
                            }.getType();
                            BroadcastActivityModel emailActivityListModel = new Gson().fromJson(headerString, listType);
                            broadcasteda = emailActivityListModel.get0();
                            try {
                                setData();
                                int totale=Integer.parseInt(emailActivityListModel.getBroadcast_success())+Integer.parseInt(emailActivityListModel.getBroadcast_fail());
                                tv_contect.setText(String.valueOf(totale));
                                int sms_count = 0;
                                int email_count = 0;

                                if (emailActivityListModel.get0().getType().equals("SMS")) {
                                    sms_count = sms_count + 1;
                                } else {
                                    email_count = email_count + 1;
                                }
                                tv_sms.setText(emailActivityListModel.getBroadcast_success());
                                tv_email.setText(emailActivityListModel.getBroadcast_fail());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            broadcastProspects = emailActivityListModel.getBroadcastProspect();
                            SessionManager.setBroadcast_Contect(getApplicationContext(), broadcastProspects);

                            save_button.setEnabled(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                } else {

                }

            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }


    public void StartBroadCastApi(BroadcastActivityModel._0 broadcast, int status) {
        //  loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("id", broadcast.getId());
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        if (status == 1) {
            paramObject.addProperty("status", "I");
        } else if (status == 0) {
            paramObject.addProperty("status", "A");
        } else if (status == 4) {
            paramObject.addProperty("status", "D");
        } else {
            paramObject.addProperty("status", "A");
        }
        obj.add("data", paramObject);
        retrofitCalls.Broadcast_store(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(Broadcaste_Activity.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        //                loadingDialog.cancelLoading();
                        if (response.body().getHttp_status() == 200) {
                            if (status == 4) {
                                finish();
                            } else {
                                try {
                                    Mail_list();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else if (response.body().getHttp_status() == 403) {
                            Global.Messageshow(getApplicationContext(), main_layout, getResources().getString(R.string.plan_validation), false);
                        } else {
                            Global.Messageshow(getApplicationContext(), main_layout, response.body().getMessage(), false);
                        }

                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }

}