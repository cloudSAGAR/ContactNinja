package com.contactninja.Campaign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Model.CampaignTask;
import com.contactninja.Model.CampaignTask_overview;
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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Campaign_Preview extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    public static TopUserListDataAdapter topUserListDataAdapter;
    ImageView iv_back;
    TextView save_button;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    RecyclerView item_list, user_contect;
    Campaign_OverviewAdapter campaign_overviewAdapter;
    /* List<String> stringList;*/
    List<CampaignTask_overview.SequenceTask> campaignTasks = new ArrayList<>();
    int sequence_id, sequence_task_id;
    BottomSheetDialog bottomSheetDialog;
    EditText tv_name;
    TextView contect_count;
    ImageView iv_toolbar_manu;
    Toolbar toolbar;
    RelativeLayout contect_layout;
    ImageView add_icon;
    LinearLayout layout_toolbar_logo;
    ConstraintLayout mMainLayout;
    List<CampaignTask_overview.SequenceTask> main_data = new ArrayList<>();
    String Camp_name = "";
    private BroadcastReceiver mNetworkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_preview);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        Global.count = 1;

        if (SessionManager.getCampign_flag(getApplicationContext()).equals("read")) {
            // StepData();
            tv_name.setEnabled(false);
            campaign_overviewAdapter = new Campaign_OverviewAdapter(getApplicationContext());
            item_list.setAdapter(campaign_overviewAdapter);
            toolbar.inflateMenu(R.menu.option_menu);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else if (SessionManager.getCampign_flag(getApplicationContext()).equals("read_name")) {
            // StepData();
            campaign_overviewAdapter = new Campaign_OverviewAdapter(getApplicationContext());
            item_list.setAdapter(campaign_overviewAdapter);
            toolbar.inflateMenu(R.menu.option_menu);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            tv_name.setEnabled(true);
            save_button.setText("Done");
            save_button.setVisibility(View.VISIBLE);
            add_icon.setVisibility(View.VISIBLE);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            //StepData();
            campaign_overviewAdapter = new Campaign_OverviewAdapter(getApplicationContext());
            item_list.setAdapter(campaign_overviewAdapter);

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
                startActivity(new Intent(getApplicationContext(), Campaign_List_Activity.class));
                finish();
                return true;
            case R.id.mv_edit:
                SessionManager.setCampign_flag("edit");
                Intent intent = new Intent(getApplicationContext(), Campaign_Preview.class);
                intent.putExtra("sequence_id", sequence_id);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void IntentUI() {
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
        item_list = findViewById(R.id.item_list);
        item_list.setLayoutManager(new LinearLayoutManager(this));
        item_list.setItemViewCacheSize(500);
        tv_name = findViewById(R.id.tv_name);
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
        Global.checkConnectivity(Campaign_Preview.this, mMainLayout);
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
                onBackPressed();

            case R.id.iv_toolbar_manu:

                break;
            case R.id.add_icon:
                SessionManager.setContect_flag("edit");
                Intent intent_two = new Intent(getApplicationContext(), ContectAndGroup_Actvity.class);
                intent_two.putExtra("sequence_id", sequence_id);
                intent_two.putExtra("seq_task_id", sequence_task_id);
                startActivity(intent_two);
                break;
            case R.id.contect_count:
                SessionManager.setContect_flag("read");
                Intent intent1 = new Intent(getApplicationContext(), ContectAndGroup_Actvity.class);
                intent1.putExtra("sequence_id", sequence_id);
                intent1.putExtra("seq_task_id", sequence_task_id);
                startActivity(intent1);
                break;

            case R.id.user_contect:

                if (SessionManager.getTask(getApplicationContext()).size() != 0) {
                    sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
                } else {
                    Intent getintent = getIntent();
                    Bundle bundle = getintent.getExtras();
                    sequence_id = bundle.getInt("sequence_id");
                }
                SessionManager.setgroup_broadcste(getApplicationContext(), new ArrayList<>());
                SessionManager.setGroupList(this, new ArrayList<>());
                Intent intent = new Intent(getApplicationContext(), ContectAndGroup_Actvity.class);
                intent.putExtra("sequence_id", sequence_id);
                intent.putExtra("seq_task_id", sequence_task_id);
                startActivity(intent);
                break;
            case R.id.save_button:
                Global.hideKeyboard(Campaign_Preview.this);
                if (Camp_name.equals(tv_name.getText().toString())) {
                    SessionManager.setCampign_flag("read");
                    Intent in = new Intent(getApplicationContext(), Campaign_Preview.class);
                    in.putExtra("sequence_id", sequence_id);
                    startActivity(in);
                    finish();
                } else {
                    if(!tv_name.getText().toString().equals("")){
                        AddName();
                    }else {
                        Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.enter_campaign_name),false);
                    }
                }

                break;

        }
    }

    @Override
    public void onBackPressed() {
        if (SessionManager.getCampign_flag(getApplicationContext()).equals("read")) {

            startActivity(new Intent(getApplicationContext(), Campaign_List_Activity.class));
            finish();
        } else if (SessionManager.getCampign_flag(getApplicationContext()).equals("edit")) {
            startActivity(new Intent(getApplicationContext(), Campaign_List_Activity.class));
            finish();
        } else if (SessionManager.getCampign_flag(getApplicationContext()).equals("read_name")) {
            //  startActivity(new Intent(getApplicationContext(),Campaign_List_Activity.class));
            finish();
        } else {
            finish();
        }
        super.onBackPressed();
    }

    private void broadcast_manu(CampaignTask_overview.SequenceTask sequenceTask, int position) {
        final View mView = getLayoutInflater().inflate(R.layout.brodcaste_campaign_manu, null);
        bottomSheetDialog = new BottomSheetDialog(Campaign_Preview.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);


        LinearLayout delete_layout = bottomSheetDialog.findViewById(R.id.delete_layout);
        LinearLayout edit_layout = bottomSheetDialog.findViewById(R.id.edit_layout);

        delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveTaskData(sequenceTask.getId(), "D", position);
            }
        });
        edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             /*   if (sequenceTask.getType().equals("EMAIL"))
                {
                    if (SessionManager.getTask(getApplicationContext()).size()!=0)
                    {
                        sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
                    }
                    else {
                        Intent getintent=getIntent();
                        Bundle bundle=getintent.getExtras();
                        sequence_id=bundle.getInt("sequence_id");
                    }
                    Intent new_task=new Intent(getApplicationContext(),Automated_Email_Activity.class);
                    new_task.putExtra("flag","edit");
                    new_task.putExtra("body",sequenceTask.getContentBody());
                    new_task.putExtra("day",sequenceTask.getDay());
                    new_task.putExtra("manage_by",sequenceTask.getManageBy());
                    new_task.putExtra("seq_task_id",String.valueOf(sequenceTask.getId()));
                    new_task.putExtra("sequence_id",String.valueOf(sequence_id));
                    new_task.putExtra("type",sequenceTask.getType());
                    new_task.putExtra("minute",sequenceTask.getMinute());
                    new_task.putExtra("header",sequenceTask.getContentHeader());
                    new_task.putExtra("step",sequenceTask.getStepNo());
                    startActivity(new_task);

                }
                else {
                    if (SessionManager.getTask(getApplicationContext()).size()!=0)
                    {
                        sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
                    }
                    else {
                        Intent getintent=getIntent();
                        Bundle bundle=getintent.getExtras();
                        sequence_id=bundle.getInt("sequence_id");
                    }
                    Log.e("sequence id", String.valueOf(sequence_id));
                    Intent new_task=new Intent(getApplicationContext(),First_Step_Start_Activity.class);
                    new_task.putExtra("flag","edit");
                    new_task.putExtra("body",sequenceTask.getContentBody());
                    new_task.putExtra("day",sequenceTask.getDay());
                    new_task.putExtra("manage_by",sequenceTask.getManageBy());
                    new_task.putExtra("seq_task_id",String.valueOf(sequenceTask.getId()));
                    new_task.putExtra("sequence_id",String.valueOf(sequence_id));
                    new_task.putExtra("type",sequenceTask.getType());
                    new_task.putExtra("minute",sequenceTask.getMinute());
                    new_task.putExtra("step",sequenceTask.getStepNo());
                    startActivity(new_task);
                }*/

                if (sequenceTask.getType().equals("EMAIL")) {
                    if (SessionManager.getTask(getApplicationContext()).size() != 0) {
                        sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
                    } else {
                        Intent getintent = getIntent();
                        Bundle bundle = getintent.getExtras();
                        sequence_id = bundle.getInt("sequence_id");
                    }
                    Log.e("Sequence is is", String.valueOf(sequence_id));
                /*    Intent new_task=new Intent(getActivity(), Automated_Email_Activity.class);
                    new_task.putExtra("flag","edit");
                    new_task.putExtra("body",sequenceTask.getContentBody());
                    new_task.putExtra("day",sequenceTask.getDay());
                    new_task.putExtra("manage_by",sequenceTask.getManageBy());
                    new_task.putExtra("seq_task_id",String.valueOf(sequenceTask.getId()));
                    new_task.putExtra("sequence_id",String.valueOf(sequence_id));
                    new_task.putExtra("type",sequenceTask.getType());
                    new_task.putExtra("minute",sequenceTask.getMinute());
                    new_task.putExtra("header",sequenceTask.getContentHeader());
                    new_task.putExtra("step",sequenceTask.getStepNo());
                    startActivity(new_task);*/
                   /* List<CampaignTask> campaignTasks1=new ArrayList<>();
                    CampaignTask campaignTask=new CampaignTask();
                    campaignTask.setId(sequenceTask.getId());
                    campaignTask.setOrganizationId(1);
                    campaignTask.setTeamId(1);
                    campaignTask.setSequenceId(sequence_id);
                    campaignTask.setType(sequenceTask.getType());
                    campaignTask.setContentHeader(sequenceTask.getContentHeader());
                    campaignTask.setContentBody(sequenceTask.getContentBody());
                    campaignTask.setManageBy(sequenceTask.getManageBy());
                    campaignTask.setDay(sequenceTask.getDay());
                    campaignTask.setMinute(sequenceTask.getMinute());
                    campaignTask.setPriority(sequenceTask.getPriority());
                    campaignTask.setStepNo(sequenceTask.getStepNo());;

                    campaignTasks1.add(campaignTask);*/
                    Intent intent = new Intent(getApplicationContext(), Add_Camp_First_Step_Activity.class);
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
                    startActivity(intent);
                    // startActivity(new Intent(getActivity(),First_Step_Activity.class));
                    SessionManager.setCampaign_Day(String.valueOf(sequenceTask.getDay()));
                    SessionManager.setCampaign_minute(String.valueOf(sequenceTask.getMinute()));
                    SessionManager.setCampaign_type(String.valueOf(sequenceTask.getType()));
                    SessionManager.setCampaign_type_name(String.valueOf(sequenceTask.getManageBy()));

//                    finish();
                    bottomSheetDialog.cancel();

                } else {

                    Log.e("sequence id", String.valueOf(sequence_id));
                /*    Intent new_task=new Intent(getActivity(), First_Step_Start_Activity.class);
                    new_task.putExtra("flag","edit");
                    new_task.putExtra("body",sequenceTask.getContentBody());
                    new_task.putExtra("day",sequenceTask.getDay());
                    new_task.putExtra("manage_by",sequenceTask.getManageBy());
                    new_task.putExtra("seq_task_id",String.valueOf(sequenceTask.getId()));
                    new_task.putExtra("sequence_id",String.valueOf(sequence_id));
                    new_task.putExtra("type",sequenceTask.getType());
                    new_task.putExtra("minute",sequenceTask.getMinute());
                    new_task.putExtra("step",sequenceTask.getStepNo());
                    startActivity(new_task);*/
/*
                    List<CampaignTask> campaignTasks1=new ArrayList<>();
                    CampaignTask campaignTask=new CampaignTask();
                    campaignTask.setId(sequenceTask.getId());
                    campaignTask.setOrganizationId(1);
                    campaignTask.setTeamId(1);
                    campaignTask.setSequenceId(sequence_id);
                    campaignTask.setType(sequenceTask.getType());
                    campaignTask.setContentHeader(sequenceTask.getContentHeader());
                    campaignTask.setContentBody(sequenceTask.getContentBody());
                    campaignTask.setManageBy(sequenceTask.getManageBy());
                    campaignTask.setDay(sequenceTask.getDay());
                    campaignTask.setMinute(sequenceTask.getMinute());
                    campaignTask.setPriority(sequenceTask.getPriority());
                    campaignTask.setStepNo(sequenceTask.getStepNo());;

                    campaignTasks1.add(campaignTask);*/

                    Log.e("Sequence is is", String.valueOf(sequence_id));
                    if (SessionManager.getTask(getApplicationContext()).size() != 0) {
                        sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
                    } else {
                        Intent getintent = getIntent();
                        Bundle bundle = getintent.getExtras();
                        sequence_id = bundle.getInt("sequence_id");
                    }
                    Intent intent = new Intent(getApplicationContext(), Add_Camp_First_Step_Activity.class);
                    intent.putExtra("flag", "edit");
                    intent.putExtra("body", sequenceTask.getContentBody());
                    intent.putExtra("day", sequenceTask.getDay());
                    intent.putExtra("manage_by", sequenceTask.getManageBy());
                    intent.putExtra("seq_task_id", sequenceTask.getId());
                    intent.putExtra("sequence_id", sequence_id);
                    intent.putExtra("type", sequenceTask.getType());
                    intent.putExtra("minute", sequenceTask.getMinute());
                    intent.putExtra("step", sequenceTask.getStepNo());
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

    public void RemoveTaskData(Integer task_id, String d, int position) {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


        if (SessionManager.getTask(getApplicationContext()).size() != 0) {
            sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
        } else {
            Intent getintent = getIntent();
            Bundle bundle = getintent.getExtras();
            sequence_id = bundle.getInt("sequence_id");
        }
        Log.e("sequence_id", String.valueOf(sequence_id));
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("seq_task_id", task_id);
        paramObject.addProperty("sequence_id", sequence_id);
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("status", d);
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);
        retrofitCalls.Task_store(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(Campaign_Preview.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();

                        if (response.body().getHttp_status() == 200) {
                            Global.count = 1;
                            campaign_overviewAdapter.remove_item(position);
                            bottomSheetDialog.cancel();
                        } else {

                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }

    public void StepData() {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


        if (SessionManager.getTask(getApplicationContext()).size() != 0) {
            sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
        } else {
            Intent getintent = getIntent();
            Bundle bundle = getintent.getExtras();
            sequence_id = bundle.getInt("sequence_id");
        }
        Log.e("sequence_id", String.valueOf(sequence_id));

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("id", sequence_id);
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);
        PackageManager pm = getApplicationContext().getPackageManager();
        String pkgName = getApplicationContext().getPackageName();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        retrofitCalls.Task_Data_Return(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(Campaign_Preview.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();

                        if (response.body().getHttp_status() == 200) {

                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            Type listType = new TypeToken<CampaignTask_overview>() {
                            }.getType();

                            CampaignTask_overview user_model1 = new Gson().fromJson(headerString, listType);
                            contect_count.setText(user_model1.get0().getProspect() + " Prospects");
                            sequence_task_id = user_model1.getSequenceTask().get(0).getId();
                            main_data.clear();
                            campaign_overviewAdapter.remove_all();
                            main_data = user_model1.getSequenceTask();
                            campaign_overviewAdapter.addAll(main_data);
                            Camp_name = user_model1.get0().getSeqName();
                            tv_name.setText(user_model1.get0().getSeqName());
                            topUserListDataAdapter = new TopUserListDataAdapter(Campaign_Preview.this, getApplicationContext(), user_model1.getSequenceProspects());
                            user_contect.setAdapter(topUserListDataAdapter);
                            topUserListDataAdapter.notifyDataSetChanged();
                            SessionManager.setCampaign_data(user_model1);

                        } else {
                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());

                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }

    public void StartCampignApi() {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


        if (SessionManager.getTask(getApplicationContext()).size() != 0) {
            sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
        } else {
            Intent getintent = getIntent();
            Bundle bundle = getintent.getExtras();
            sequence_id = bundle.getInt("sequence_id");
        }
        Log.e("sequence_id", String.valueOf(sequence_id));
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("record_id", sequence_id);
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("status", "A");
        obj.add("data", paramObject);
        retrofitCalls.Sequence_settings(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(Campaign_Preview.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();

                        if (response.body().getHttp_status() == 200) {
                            onBackPressed();
                        } else {

                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }

    public void onResume() {
        loadingDialog.cancelLoading();
        Global.count = 1;
        StepData();
        super.onResume();
    }

    public void AddName() {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


        if (SessionManager.getTask(getApplicationContext()).size() != 0) {
            sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
        } else {
            Intent getintent = getIntent();
            Bundle bundle = getintent.getExtras();
            sequence_id = bundle.getInt("sequence_id");
        }
        Log.e("sequence_id", String.valueOf(sequence_id));
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("record_id", sequence_id);
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("seq_name", tv_name.getText().toString());
        obj.add("data", paramObject);
        retrofitCalls.Sequence_settings(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(Campaign_Preview.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();

                        if (response.body().getHttp_status() == 200) {
                            SessionManager.setCampign_flag("read");
                            Intent in = new Intent(getApplicationContext(), Campaign_Preview.class);
                            in.putExtra("sequence_id", sequence_id);
                            startActivity(in);
                            finish();
                        } else {

                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }

    public static class TopUserListDataAdapter extends RecyclerView.Adapter<TopUserListDataAdapter.InviteListDataclass> {

        private final Context mcntx;
        private final List<CampaignTask_overview.SequenceProspect> userDetailsfull;
        private final List<CampaignTask_overview.SequenceProspect> userDetails;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";


        public TopUserListDataAdapter(Activity Ctx, Context mCtx, List<CampaignTask_overview.SequenceProspect> userDetails) {
            this.mcntx = mCtx;
            this.mCtx = Ctx;
            this.userDetails = userDetails;
            userDetailsfull = new ArrayList<>(userDetails);
        }

        @NonNull
        @Override
        public TopUserListDataAdapter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.top_user_details, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TopUserListDataAdapter.InviteListDataclass holder, int position) {
            CampaignTask_overview.SequenceProspect inviteUserDetails = userDetails.get(position);
            last_postion = position;
            holder.userName.setText(inviteUserDetails.getFirstname());
            holder.top_layout.setVisibility(View.VISIBLE);

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

    public class Campaign_OverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int LOADING = 0;
        private static final int ITEM = 1;
        private final Context context;
        private List<CampaignTask_overview.SequenceTask> movieList;
        private boolean isLoadingAdded = false;

        public Campaign_OverviewAdapter(Context context) {
            this.context = context;
            movieList = new LinkedList<>();
        }


        public void setMovieList(List<CampaignTask_overview.SequenceTask> movieList) {
            this.movieList = movieList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case ITEM:
                    View viewItem = inflater.inflate(R.layout.campign_item, parent, false);
                    viewHolder = new Campaign_OverviewAdapter.MovieViewHolder(viewItem);
                    break;
                case LOADING:
                    View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                    viewHolder = new Campaign_OverviewAdapter.LoadingViewHolder(viewLoading);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            CampaignTask_overview.SequenceTask movieList_data = movieList.get(position);
            switch (getItemViewType(position)) {
                case ITEM:
                    Campaign_OverviewAdapter.MovieViewHolder movieViewHolder = (Campaign_OverviewAdapter.MovieViewHolder) holder;

                    if (SessionManager.getCampign_flag(Campaign_Preview.this).equals("read")) {
                        movieViewHolder.tv_add_new_step.setText(getString(R.string.txt_campaign));
                        movieViewHolder.iv_manu.setVisibility(View.GONE);
                    } else {
                        movieViewHolder.tv_add_new_step.setText("Add New Step");
                        movieViewHolder.iv_manu.setVisibility(View.VISIBLE);
                    }

                    if (position == movieList.size() - 1) {
                        movieViewHolder.add_new_step_layout.setVisibility(View.VISIBLE);
                        int num = movieList.size() + 1;
                        movieViewHolder.tv_add_new_step_num.setText(String.valueOf(num));
                    } else {
                        movieViewHolder.add_new_step_layout.setVisibility(View.GONE);
                    }
                    movieViewHolder.tv_item_num.setText(String.valueOf(Global.count));
                    Global.count++;

                    if (position == 0) {
                        movieViewHolder.run_time_layout.setVisibility(View.GONE);
                        movieViewHolder.line_one.setVisibility(View.VISIBLE);
                    } else {

                        movieViewHolder.line_one.setVisibility(View.VISIBLE);
                    }


                    if (movieList.get(position).getType().equals("SMS")) {
                        movieViewHolder.iv_email.setVisibility(View.GONE);
                        movieViewHolder.iv_message.setVisibility(View.VISIBLE);
                    } else {
                        movieViewHolder.iv_email.setVisibility(View.VISIBLE);
                        movieViewHolder.iv_message.setVisibility(View.GONE);
                    }
                    movieViewHolder.edit_minutes.setText(movieList.get(position).getMinute().toString());
                    movieViewHolder.edit_day.setText(movieList_data.getDay().toString());

                    movieViewHolder.tv_title.setText("Step#" + movieList.get(position).getStepNo() + "(" + movieList.get(position).getManageBy() + " " + movieList.get(position).getType() + ")");

                    movieViewHolder.tv_detail.setText(movieList.get(position).getContentBody());

                    if (SessionManager.getCampign_flag(getApplicationContext()).equals("edit")) {

                        movieViewHolder.iv_manu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                broadcast_manu(movieList.get(position), position);
                            }
                        });


                        movieViewHolder.tv_detail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (movieViewHolder.run_time_layout.getVisibility() == View.VISIBLE) {
                                    movieViewHolder.run_time_layout.setVisibility(View.GONE);
                                } else {
                                    movieViewHolder.run_time_layout.setVisibility(View.VISIBLE);
                                }

                            }
                        });

                    }
                    movieViewHolder.tv_add_new_step.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (SessionManager.getCampign_flag(Campaign_Preview.this).equals("read")) {

                                StartCampignApi();

                            } else {
                                SessionManager.setCampaign_type("");
                                SessionManager.setCampaign_type_name("");
                                SessionManager.setCampaign_minute("00");
                                SessionManager.setCampaign_Day("1");

                                if (position == (getItemCount() - 1)) {
                                    CampaignTask campaignTask = new CampaignTask();
                                    CampaignTask_overview.SequenceTask Data = movieList.get(position);
                                    //Toast.makeText(getApplicationContext(),"Step"+Data.getStepNo(),Toast.LENGTH_LONG).show();

                                    campaignTask.setId(Data.getId());
                                    campaignTask.setDay(Data.getDay());
                                    campaignTask.setStepNo(Data.getStepNo());
                                    campaignTask.setType(Data.getType());
                                    campaignTask.setPriority(Data.getPriority());
                                    campaignTask.setMinute(Data.getMinute());
                                    campaignTask.setContentHeader(Data.getContentHeader());
                                    campaignTask.setContentBody(Data.getContentBody());
                                    campaignTask.setSequenceId(sequence_id);
                                    campaignTask.setManageBy(Data.getManageBy());
                                    List<CampaignTask> campaignTaskList = new ArrayList<>();
                                    campaignTaskList.add(campaignTask);
                                    SessionManager.setTask(getApplicationContext(), campaignTaskList);
                                }
                                Intent intent = new Intent(getApplicationContext(), Add_Camp_First_Step_Activity.class);
                                intent.putExtra("flag", "new");
                                startActivity(intent);
                                finish();
                            }

                        }
                    });

                    break;

                case LOADING:
                    Campaign_OverviewAdapter.LoadingViewHolder loadingViewHolder = (Campaign_OverviewAdapter.LoadingViewHolder) holder;
                    loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return movieList == null ? 0 : movieList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == movieList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }

        public void addLoadingFooter() {
            isLoadingAdded = true;
        }


        public void remove_item(int position) {
            movieList.remove(position);
            notifyDataSetChanged();
        }

        public void remove_all() {
            movieList.clear();
            notifyDataSetChanged();
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = movieList.size() - 1;
            CampaignTask_overview.SequenceTask result = getItem(position);

            if (result != null) {
                movieList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void add(CampaignTask_overview.SequenceTask movie) {
            movieList.add(movie);
            notifyItemInserted(movieList.size() - 1);
        }

        public void addAll(List<CampaignTask_overview.SequenceTask> moveResults) {
            for (CampaignTask_overview.SequenceTask result : moveResults) {
                add(result);
            }
        }

        public CampaignTask_overview.SequenceTask getItem(int position) {
            return movieList.get(position);
        }


        public class MovieViewHolder extends RecyclerView.ViewHolder {

            TextView tv_add_new_step_num, tv_item_num, tv_title, tv_detail,
                    tv_email_title, tv_email_detail, tv_add_new_step;
            View line_one;
            LinearLayout add_new_step_layout, run_time_layout, email_layout, run_time_email_layout;
            ImageView iv_manu, iv_email_manu, iv_message, iv_email;
            EditText edit_day, edit_minutes, edit_email_day, edit_email_minutes;


            public MovieViewHolder(View itemView) {
                super(itemView);
                iv_message = itemView.findViewById(R.id.iv_message);
                iv_email = itemView.findViewById(R.id.iv_email);
                add_new_step_layout = itemView.findViewById(R.id.add_new_step);
                tv_add_new_step_num = itemView.findViewById(R.id.tv_add_new_step_num);
                tv_add_new_step = itemView.findViewById(R.id.tv_add_new_step);
                line_one = itemView.findViewById(R.id.line_one);
                tv_item_num = itemView.findViewById(R.id.tv_item_num);
                tv_title = itemView.findViewById(R.id.tv_title);
                iv_manu = itemView.findViewById(R.id.iv_manu);
                run_time_layout = itemView.findViewById(R.id.run_time_layout);
                tv_detail = itemView.findViewById(R.id.tv_detail);
                edit_day = itemView.findViewById(R.id.edit_day);
                edit_minutes = itemView.findViewById(R.id.edit_minutes);
                email_layout = itemView.findViewById(R.id.email_layout);
                tv_email_title = itemView.findViewById(R.id.tv_email_title);
                iv_email_manu = itemView.findViewById(R.id.iv_email_manu);
                tv_email_detail = itemView.findViewById(R.id.tv_email_detail);
                run_time_email_layout = itemView.findViewById(R.id.run_time_email_layout);
                edit_email_day = itemView.findViewById(R.id.edit_email_day);
                edit_email_minutes = itemView.findViewById(R.id.edit_email_minutes);
            }
        }

        public class LoadingViewHolder extends RecyclerView.ViewHolder {

            private final ProgressBar progressBar;

            public LoadingViewHolder(View itemView) {
                super(itemView);
                progressBar = itemView.findViewById(R.id.idPBLoading);

            }
        }

    }
}
