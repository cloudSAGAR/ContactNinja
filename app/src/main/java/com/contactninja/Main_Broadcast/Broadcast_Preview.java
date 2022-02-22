package com.contactninja.Main_Broadcast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contactninja.Campaign.Add_Camp_Tab_Select_Activity;
import com.contactninja.Main_Broadcast.List_And_show.List_Broadcast_activity;
import com.contactninja.Model.Broadcate_save_data;
import com.contactninja.Model.CampaignTask_overview;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Grouplist;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Broadcast_Preview extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    public static TopUserListDataAdapter topUserListDataAdapter;
    ImageView iv_back;
    TextView save_button, tv_start_broadcast, tv_repete_type;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    RecyclerView user_contect;
    /* List<String> stringList;*/
    int sequence_id, sequence_task_id;
    BottomSheetDialog bottomSheetDialog;
    TextView tv_name, tv_title, tv_detail;
    TextView contect_count, tv_date;
    ImageView iv_toolbar_manu, iv_manu;
    Toolbar toolbar;
    RelativeLayout contect_layout;
    ImageView add_icon, iv_camp_edit, tv_item_num;
    LinearLayout layout_toolbar_logo;
    ConstraintLayout mMainLayout;
    String Camp_name = "";
    LinearLayout layout_name, layout_email_subject;
    Broadcate_save_data broadcate_save_data = new Broadcate_save_data();
    List<ContectListData.Contact> Contect_List = new ArrayList<>();
    EditText ev_subject;
    private BroadcastReceiver mNetworkReceiver;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_preview);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        broadcate_save_data = SessionManager.getBroadcate_save_data(getApplicationContext());
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        Global.count = 1;
        toolbar.inflateMenu(R.menu.option_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_name.setText(broadcate_save_data.getBroadcastname());
        Contect_List = SessionManager.getGroupList(getApplicationContext());
        contect_count.setText(Contect_List.size() + "prospects");
        topUserListDataAdapter = new TopUserListDataAdapter(this, getApplicationContext(), Contect_List);
        user_contect.setAdapter(topUserListDataAdapter);
        tv_title.setText(SessionManager.getCampaign_type_name(getApplicationContext()) + " " + SessionManager.getCampaign_type(getApplicationContext()));
        ev_subject.setText(broadcate_save_data.getContent_header());
        tv_detail.setText(broadcate_save_data.getContent_body());

        if (SessionManager.getCampaign_type(getApplicationContext()).equals("SMS")) {

            tv_item_num.setImageDrawable(getResources().getDrawable(R.drawable.ic_message_select));
            layout_email_subject.setVisibility(View.GONE);
        } else {
            layout_email_subject.setVisibility(View.VISIBLE);
            tv_item_num.setImageDrawable(getResources().getDrawable(R.drawable.ic_email_mini));

        }
        tv_date.setText(broadcate_save_data.getDate() + " @ " + broadcate_save_data.getTime());
        tv_repete_type.setText(broadcate_save_data.getRecurrence());

        if (SessionManager.getBroadcast_flag(getApplicationContext()).equals("edit")) {
            iv_manu.setVisibility(View.VISIBLE);
            add_icon.setVisibility(View.VISIBLE);
            iv_camp_edit.setVisibility(View.VISIBLE);
        } else {
            iv_manu.setVisibility(View.GONE);
            add_icon.setVisibility(View.GONE);
            iv_camp_edit.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        //  Toast.makeText(getApplicationContext(), "Manu Clcik", Toast.LENGTH_LONG).show();
        //Log.e("Option Manu is Select", "Yes");
        switch (item.getItemId()) {
            case R.id.mv_save:
                startActivity(new Intent(getApplicationContext(), List_Broadcast_activity.class));
                finish();
                return true;
            case R.id.mv_edit:
                SessionManager.setBroadcast_flag("edit");
                Intent intent = new Intent(getApplicationContext(), Broadcast_Preview.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("WrongViewCast")
    private void IntentUI() {
        iv_manu = findViewById(R.id.iv_manu);
        iv_manu.setOnClickListener(this);
        tv_repete_type = findViewById(R.id.tv_repete_type);
        tv_date = findViewById(R.id.tv_date);
        layout_email_subject = findViewById(R.id.layout_email_subject);
        tv_item_num = findViewById(R.id.tv_item_num);
        tv_detail = findViewById(R.id.tv_detail);
        ev_subject = findViewById(R.id.ev_subject);
        tv_title = findViewById(R.id.tv_title);
        tv_start_broadcast = findViewById(R.id.tv_start_broadcast);
        tv_start_broadcast.setOnClickListener(this);
        iv_camp_edit = findViewById(R.id.iv_camp_edit);
        iv_camp_edit.setOnClickListener(this);
        layout_name = findViewById(R.id.layout_name);
        mMainLayout = findViewById(R.id.mMainLayout);
        add_icon = findViewById(R.id.add_icon);
        add_icon.setVisibility(View.GONE);
        add_icon.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Done");
        layout_toolbar_logo = findViewById(R.id.layout_toolbar_logo);
        layout_toolbar_logo.setVisibility(View.GONE);
        save_button.setVisibility(View.GONE);
        tv_name = findViewById(R.id.tv_name);
        layout_name.setOnClickListener(this);
        tv_name.setOnClickListener(this);
        user_contect = findViewById(R.id.user_contect);
        user_contect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        user_contect.setOnClickListener(this);
        iv_toolbar_manu = findViewById(R.id.iv_toolbar_manu);
        iv_toolbar_manu.setOnClickListener(this);

        contect_count = findViewById(R.id.contect_count);
        contect_count.setOnClickListener(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.option_menu);
        setSupportActionBar(toolbar);
        contect_layout = findViewById(R.id.contect_layout);
        contect_layout.setOnClickListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Broadcast_Preview.this, mMainLayout);
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
                onBackPressed();
                break;
            case R.id.tv_start_broadcast:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                try {
                    BroadcasteAPI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
/*                Intent intent=new Intent(getApplicationContext(),Brodcsast_Tankyou.class);
                intent.putExtra("s_name","final");
                startActivity(intent);*/
                break;

            case R.id.iv_toolbar_manu:

                break;
            case R.id.add_icon:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent broad_caste = new Intent(getApplicationContext(), Broadcast_Contact_Selction_Actvity.class);
                broad_caste.putExtra("Activty","Preview");
                startActivity(broad_caste);
                finish();
                break;
            case R.id.contect_count:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                break;

            case R.id.user_contect:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Global.hideKeyboard(Broadcast_Preview.this);


                break;

            case R.id.layout_name:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();


                break;

            case R.id.iv_camp_edit:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent caste = new Intent(getApplicationContext(), Broadcast_Name_Activity.class);
                caste.putExtra("Activty","Preview");
                startActivity(caste);
                finish();
                break;

            case R.id.iv_manu:
                if (SessionManager.getCampaign_type(getApplicationContext()).equals("SMS")) {
                    Intent new_task = new Intent(getApplicationContext(), Add_Broad_Text_Activity.class);
                    new_task.putExtra("flag", "add");
                    startActivity(new_task);
                    finish();
                } else {
                    Intent new_task = new Intent(getApplicationContext(), Add_Broad_Email_Activity.class);
                    new_task.putExtra("flag", "add");
                    startActivity(new_task);
                    finish();
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        showAlertDialogButtonClicked();
    }

    private void showAlertDialogButtonClicked() {

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
                Intent intent=new Intent(getApplicationContext(),List_Broadcast_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

               /* Broadcast_Name_Activity.Broadcast_Name.finish();
                Recuring_email_broadcast_activity.Recuring_email_broadcast.finish();
                Add_Broad_Text_Activity.Add_Broad_Text.finish();
                Add_Broad_Email_Activity.Add_Broad_Email.finish();
                Broadcast_Contact_Selction_Actvity.Broadcast_Contact_Selction.finish();
                Text_And_Email_Auto_Manual_Broadcast.Text_And_Email_Auto_Manual_Broadcast_Activity.finish();*/
                finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void BroadcasteAPI() throws JSONException {
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
        paramObject.put("start_time", broadcate_save_data.getTime());
        paramObject.put("start_date", broadcate_save_data.getDate());
        paramObject.put("assign_to", user_id);

        if (broadcate_save_data.getRecurrence().equals("Daily")) {
            paramObject.put("recurring_type", "D");
        } else if (broadcate_save_data.getRecurrence().equals("Weekly")) {
            paramObject.put("recurring_type", "W");
        } else if (broadcate_save_data.getRecurrence().equals("Monthly")) {
            paramObject.put("recurring_type", "M");
        }


        JSONArray jsonArray = new JSONArray();
        List<ContectListData.Contact> s = SessionManager.getGroupList(getApplicationContext());
        Log.e("Coontect Detail is", new Gson().toJson(s));
        for (int i = 0; i < s.size(); i++) {
            JSONObject paramObject1 = new JSONObject();
            for (int j = 0; j < s.get(i).getContactDetails().size(); j++) {

                paramObject1.put("prospect_id", s.get(i).getContactDetails().get(j).getContactId());
                if (s.get(i).getContactDetails().get(j).getType().equals("EMAIL")) {
                    paramObject1.put("email", s.get(i).getContactDetails().get(j).getEmailNumber());
                } else {
                    paramObject1.put("mobile", s.get(i).getContactDetails().get(j).getEmailNumber());
                }

                jsonArray.put(paramObject1);
            }
        }
        List<Grouplist.Group> group_list = SessionManager.getgroup_broadcste(getApplicationContext());
        JSONArray contect_array = new JSONArray();
        for (int i = 0; i < group_list.size(); i++) {
            contect_array.put(group_list.get(i).getId());
        }
        paramObject.put("contact_group_ids", contect_array);
        paramObject.put("prospect_id", jsonArray);
        paramObject.put("record_id", "");
        paramObject.put("broadcast_name", broadcate_save_data.getBroadcastname());
        if (broadcate_save_data.getTemplate_id().equals("")) {
            paramObject.put("template_id", "");

        } else {
            paramObject.put("template_id", broadcate_save_data.getTemplate_id());
        }

        paramObject.put("content_header", broadcate_save_data.getContent_header());
        paramObject.put("content_body", broadcate_save_data.getContent_body());
        paramObject.put("from_ac", broadcate_save_data.getFrom_ac());
        paramObject.put("from_ac_id", broadcate_save_data.getFrom_ac_id());

        JSONArray recurring_detail = new JSONArray();
        JSONObject repeat_every_obj = new JSONObject();
        repeat_every_obj.put("repeat_every", broadcate_save_data.getRepeat_every());
        recurring_detail.put(repeat_every_obj);

        if (broadcate_save_data.getRecurrence().equals("Weekly")) {
            JSONObject occurs_on_obj = new JSONObject();
            JSONArray coccurs_on_array = new JSONArray();
            coccurs_on_array.put(broadcate_save_data.getOccurs_weekly());
            occurs_on_obj.put("day_of_week", coccurs_on_array);
            recurring_detail.put(occurs_on_obj);

        } else if (broadcate_save_data.getRecurrence().equals("Monthly")) {
            JSONObject occurs_on_data = new JSONObject();
            JSONArray coccurs_on_data_array = new JSONArray();
            JSONObject day_of_month_data = new JSONObject();
            day_of_month_data.put("day_of_month", broadcate_save_data.getDay_of_month());
            coccurs_on_data_array.put(day_of_month_data);
            JSONObject every_week_no_data = new JSONObject();
            every_week_no_data.put("every_week_no", broadcate_save_data.getEvery_day());
            coccurs_on_data_array.put(every_week_no_data);

            JSONObject every_dayofweek_data = new JSONObject();
            every_dayofweek_data.put("every_dayofweek", broadcate_save_data.getEvery_second());
            coccurs_on_data_array.put(every_dayofweek_data);
            occurs_on_data.put("occurs_on", coccurs_on_data_array);
            recurring_detail.put(occurs_on_data);
        }

        paramObject.put("recurring_detail", recurring_detail);
        if (!broadcate_save_data.getId().equals(""))
        {
            paramObject.put("id",broadcate_save_data.getId());
        }

        obj.put("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is", new Gson().toJson(gsonObject));
        retrofitCalls.Broadcast_store(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(Broadcast_Preview.this), Global.Device, new RetrofitCallback() {

            public void success(Response<ApiResponse> response) {
                Log.e("Response is",new Gson().toJson(response.body()));
                if (response.body().getHttp_status() == 200) {
                    //  loadingDialog.cancelLoading();
                    loadingDialog.cancelLoading();
                    Intent intent=new Intent(getApplicationContext(),Brodcsast_Tankyou.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    loadingDialog.cancelLoading();
                }
            }

            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });

    }


    private void broadcast_manu(CampaignTask_overview.SequenceTask sequenceTask, int position) {
        final View mView = getLayoutInflater().inflate(R.layout.brodcaste_campaign_manu, null);
        bottomSheetDialog = new BottomSheetDialog(Broadcast_Preview.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);


        LinearLayout delete_layout = bottomSheetDialog.findViewById(R.id.delete_layout);
        LinearLayout edit_layout = bottomSheetDialog.findViewById(R.id.edit_layout);

        delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (sequenceTask.getType().equals("EMAIL")) {
                    if (SessionManager.getTask(getApplicationContext()).size() != 0) {
                        sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
                    } else {
                        Intent getintent = getIntent();
                        Bundle bundle = getintent.getExtras();
                        sequence_id = bundle.getInt("sequence_id");
                    }
                    Log.e("Sequence is is", String.valueOf(sequence_id));

                    Intent intent = new Intent(getApplicationContext(), Add_Camp_Tab_Select_Activity.class);
                    intent.putExtra("flag", "edit");
                    intent.putExtra("body", sequenceTask.getContentBody());
                    intent.putExtra("day", sequenceTask.getDay());
                    intent.putExtra("manage_by", sequenceTask.getManageBy());
                    intent.putExtra("seq_task_id", sequenceTask.getId());
                    intent.putExtra("sequence_id", sequence_id);
                    intent.putExtra("type", sequenceTask.getType());
                    intent.putExtra("minute", sequenceTask.getMinute());
                    intent.putExtra("header", sequenceTask.getContentHeader());
                    intent.putExtra("step", sequenceTask.getStepNo());
                    intent.putExtra("from_ac", sequenceTask.getMail_module());
                    intent.putExtra("from_ac_id", sequenceTask.getSent_tbl_id());
                    startActivity(intent);
                    finish();
                    // startActivity(new Intent(getActivity(),First_Step_Activity.class));
                    SessionManager.setCampaign_Day(String.valueOf(sequenceTask.getDay()));
                    SessionManager.setCampaign_minute(String.valueOf(sequenceTask.getMinute()));
                    SessionManager.setCampaign_type(String.valueOf(sequenceTask.getType()));
                    SessionManager.setCampaign_type_name(String.valueOf(sequenceTask.getManageBy()));

//                    finish();
                    bottomSheetDialog.cancel();

                } else {

                    Log.e("sequence id", String.valueOf(sequence_id));


                    Log.e("Sequence is is", String.valueOf(sequence_id));
                    if (SessionManager.getTask(getApplicationContext()).size() != 0) {
                        sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
                    } else {
                        Intent getintent = getIntent();
                        Bundle bundle = getintent.getExtras();
                        sequence_id = bundle.getInt("sequence_id");
                    }
                    Intent intent = new Intent(getApplicationContext(), Add_Camp_Tab_Select_Activity.class);
                    intent.putExtra("flag", "edit");
                    intent.putExtra("body", sequenceTask.getContentBody());
                    intent.putExtra("day", sequenceTask.getDay());
                    intent.putExtra("manage_by", sequenceTask.getManageBy());
                    intent.putExtra("seq_task_id", sequenceTask.getId());
                    intent.putExtra("sequence_id", sequence_id);
                    intent.putExtra("type", sequenceTask.getType());
                    intent.putExtra("minute", sequenceTask.getMinute());
                    intent.putExtra("step", sequenceTask.getStepNo());
                    intent.putExtra("from_ac", sequenceTask.getMail_module());
                    intent.putExtra("from_ac_id", sequenceTask.getSent_tbl_id());
                    startActivity(intent);
                    //  SessionManager.setTask(getActivity(),campaignTasks1);
                    SessionManager.setCampaign_Day(String.valueOf(sequenceTask.getDay()));
                    SessionManager.setCampaign_minute(String.valueOf(sequenceTask.getMinute()));
                    SessionManager.setCampaign_type(String.valueOf(sequenceTask.getType()));
                    SessionManager.setCampaign_type_name(String.valueOf(sequenceTask.getManageBy()));

                    //                finish();
                    bottomSheetDialog.cancel();
                }
                //              finish();
                bottomSheetDialog.cancel();

            }
        });
        bottomSheetDialog.show();

    }


    public void onResume() {
        loadingDialog.cancelLoading();
        super.onResume();
    }


    public static class TopUserListDataAdapter extends RecyclerView.Adapter<TopUserListDataAdapter.InviteListDataclass> {

        private final Context mcntx;
        private final List<ContectListData.Contact> userDetailsfull;
        private final List<ContectListData.Contact> userDetails;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";


        public TopUserListDataAdapter(Activity Ctx, Context mCtx, List<ContectListData.Contact> userDetails) {
            this.mcntx = mCtx;
            this.mCtx = Ctx;
            this.userDetails = userDetails;
            userDetailsfull = new ArrayList<>(userDetails);
        }

        @NonNull
        @Override
        public InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.top_user_details, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InviteListDataclass holder, int position) {
            ContectListData.Contact inviteUserDetails = userDetails.get(position);
            last_postion = position;
            holder.userName.setText(inviteUserDetails.getFirstname());
            holder.top_layout.setVisibility(View.VISIBLE);

            try {
                if (Global.IsNotNull(inviteUserDetails.getFirstname().toString()) || !inviteUserDetails.getFirstname().toString().equals("")) {
                    String first_latter = inviteUserDetails.getFirstname().substring(0, 1).toUpperCase();

                    if (second_latter.equals("")) {
                        current_latter = first_latter;
                        second_latter = first_latter;

                    } else if (second_latter.equals(first_latter)) {
                        current_latter = second_latter;
                    } else {

                        current_latter = first_latter;
                        second_latter = first_latter;
                    }


                }
                holder.no_image.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);
                String name = inviteUserDetails.getFirstname();
                holder.profile_image.setVisibility(View.GONE);
                String add_text = "";
                String[] split_data = name.split(" ");
                try {
                    for (int i = 0; i < split_data.length; i++) {
                        if (i == 0) {
                            add_text = split_data[i].substring(0, 1);
                        } else {
                            add_text = add_text + split_data[i].charAt(0);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                holder.no_image.setText(add_text);
                holder.no_image.setVisibility(View.VISIBLE);

            }
            catch (Exception e)
            {
                holder.no_image.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);
                String name = inviteUserDetails.getContactDetails().get(0).getEmailNumber();
                holder.userName.setText(name);
                holder.profile_image.setVisibility(View.GONE);
                String add_text = "";
                String[] split_data = name.split(" ");
                try {
                    for (int i = 0; i < split_data.length; i++) {
                        if (i == 0) {
                            add_text = split_data[i].substring(0, 1);
                        } else {
                            add_text = add_text + split_data[i].charAt(0);
                            break;
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }


                holder.no_image.setText(add_text);
                holder.no_image.setVisibility(View.VISIBLE);
            }







        }

        @Override
        public int getItemCount() {
            return userDetails.size();
        }

        public static class InviteListDataclass extends RecyclerView.ViewHolder {

            TextView no_image;
            TextView userName;
            CircleImageView profile_image;
            LinearLayout top_layout;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                userName = itemView.findViewById(R.id.username);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.main_layout);


            }

        }

    }

}
