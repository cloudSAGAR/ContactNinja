package com.contactninja.Campaign.List_itm;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.contactninja.Campaign.Add_Camp_Tab_Select_Activity;
import com.contactninja.Campaign.Campaign_Overview;
import com.contactninja.Campaign.Campaign_Preview;
import com.contactninja.Interface.CampaingClick;
import com.contactninja.MainActivity;
import com.contactninja.Model.Campaign_List;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.PaginationListener;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class Campaign_List_Activity extends AppCompatActivity implements View.OnClickListener,
                                                                                 SwipeRefreshLayout.OnRefreshListener, CampaingClick, ConnectivityReceiver.ConnectivityReceiverListener {
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back, iv_cancle_search_icon;
    TextView tv_create, sub_txt, txt_toolbar;
    LinearLayout demo_layout, mMainLayout1, mMainLayout, add_campaign_layout;
    EditText ev_search;
    SwipeRefreshLayout swipeToRefresh;
    RecyclerView rv_campaign_list;
    LinearLayout lay_no_list;
    
    
    CampaingAdepter campaingAdepter;
    List<Campaign_List.Campaign> campaignList = new ArrayList<>();
    int perPage = 20;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    
    private BroadcastReceiver mNetworkReceiver;
    private long mLastClickTime = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_list);
        mNetworkReceiver = new ConnectivityReceiver();
        retrofitCalls = new RetrofitCalls(Campaign_List_Activity.this);
        loadingDialog = new LoadingDialog(Campaign_List_Activity.this);
        sessionManager = new SessionManager(Campaign_List_Activity.this);
        
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();
        iv_back.setOnClickListener(this);
        demo_layout.setOnClickListener(this);
        add_campaign_layout.setOnClickListener(this);
        rv_campaign_list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_campaign_list.setLayoutManager(layoutManager);
        campaingAdepter = new CampaingAdepter(Campaign_List_Activity.this, new ArrayList<>(), this);
        rv_campaign_list.setAdapter(campaingAdepter);
        
        
        rv_campaign_list.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                try {
                    if (Global.isNetworkAvailable(Campaign_List_Activity.this, MainActivity.mMainLayout)) {
                        Campaing_list();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        
        ev_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    iv_cancle_search_icon.setVisibility(View.VISIBLE);
                    Global.hideKeyboard(Campaign_List_Activity.this);
                    onResume();
                    return true;
                }
                return false;
            }
        });
    }
    
    private void IntentUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        iv_cancle_search_icon = findViewById(R.id.iv_cancle_search_icon);
        iv_cancle_search_icon.setOnClickListener(this);
        mMainLayout1 = findViewById(R.id.mMainLayout1);
        rv_campaign_list = findViewById(R.id.rv_campaign_list);
        lay_no_list = findViewById(R.id.lay_no_list);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        demo_layout = findViewById(R.id.demo_layout);
        tv_create = findViewById(R.id.tv_create);
        sub_txt = findViewById(R.id.sub_txt);
        ev_search = findViewById(R.id.ev_search);
        add_campaign_layout = findViewById(R.id.add_campaign_layout);
        txt_toolbar = findViewById(R.id.txt_toolbar);
        txt_toolbar.setVisibility(View.VISIBLE);
        txt_toolbar.setText(getResources().getText(R.string.campaign));
        tv_create.setText(getString(R.string.campaign_alert_txt));
        sub_txt.setText(getString(R.string.campaign_alert_sub_txt));
        
        
        swipeToRefresh = findViewById(R.id.campaign_refresh);
        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(this);
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        currentPage = PAGE_START;
        isLastPage = false;
        campaignList.clear();
        campaingAdepter.clear();
        try {
            if (Global.isNetworkAvailable(Campaign_List_Activity.this, MainActivity.mMainLayout)) {
                if (!swipeToRefresh.isRefreshing()) {
                    loadingDialog.showLoadingDialog();
                }
                Campaing_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Campaign_List_Activity.this, mMainLayout);
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
    public void onRefresh() {
        ev_search.setText("");
        iv_cancle_search_icon.setVisibility(View.GONE);
        currentPage = PAGE_START;
        isLastPage = false;
        campaingAdepter.clear();
        campaignList.clear();
        try {
            if (Global.isNetworkAvailable(Campaign_List_Activity.this, MainActivity.mMainLayout)) {
                if (!swipeToRefresh.isRefreshing()) {
                    loadingDialog.showLoadingDialog();
                }
                Campaing_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    private void Campaing_list() throws JSONException {
        
        
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("q", ev_search.getText().toString());
        paramObject.addProperty("perPage", perPage);
        paramObject.addProperty("page", currentPage);
        paramObject.addProperty("orderBy","created_at");
       /* paramObject.addProperty("order","ASC");*/
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
                Global.getVersionname(Campaign_List_Activity.this), Global.Device, new RetrofitCallback() {
                    @SuppressLint("SyntheticAccessor")
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                        
                        Gson gson = new Gson();
                        String headerString = gson.toJson(response.body().getData());
                        if (response.body().getHttp_status() == 200) {
                            
                            
                            Type listType = new TypeToken<Campaign_List>() {
                            }.getType();
                            Campaign_List campaign = new Gson().fromJson(headerString, listType);
                            campaignList = campaign.getCampaignList();
                            
                            if (!ev_search.getText().toString().equals("")) {
                                if (campaignList.size() == 0) {
                                    rv_campaign_list.setVisibility(View.GONE);
                                    lay_no_list.setVisibility(View.VISIBLE);
                                } else {
                                    lay_no_list.setVisibility(View.GONE);
                                    rv_campaign_list.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (campaignList.size() == 0) {
                                    lay_no_list.setVisibility(View.GONE);
                                    mMainLayout1.setVisibility(View.GONE);
                                    demo_layout.setVisibility(View.VISIBLE);
                                } else {
                                    
                                    lay_no_list.setVisibility(View.GONE);
                                    demo_layout.setVisibility(View.GONE);
                                    rv_campaign_list.setVisibility(View.VISIBLE);
                                    mMainLayout1.setVisibility(View.VISIBLE);
                                }
                            }
                            
                            
                            if (currentPage != PAGE_START) campaingAdepter.removeLoading();
                            campaingAdepter.addItems(campaignList);
                            swipeToRefresh.setRefreshing(false);
                            // check weather is last page or not
                            if (campaign.getTotal() > campaingAdepter.getItemCount()) {
                                campaingAdepter.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;
                            
                        } else {
                            if (!ev_search.getText().toString().equals("")) {
                                rv_campaign_list.setVisibility(View.GONE);
                                lay_no_list.setVisibility(View.VISIBLE);
                            } else {
                                mMainLayout1.setVisibility(View.GONE);
                                demo_layout.setVisibility(View.VISIBLE);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cancle_search_icon:
                iv_cancle_search_icon.setVisibility(View.GONE);
                ev_search.setText("");
                onResume();
                break;
            case R.id.demo_layout:
            case R.id.add_campaign_layout:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (SessionManager.getContectList(Campaign_List_Activity.this).size() != 0) {
                    
                    
                    SessionManager.setCampaign_type("");
                    SessionManager.setCampaign_type_name("");
                    SessionManager.setCampaign_Day("");
                    SessionManager.setCampaign_minute("");
                    Global.count = 1;
                    SessionManager.setTask(getApplicationContext(), new ArrayList<>());
                    Intent intent = new Intent(getApplicationContext(), Add_Camp_Tab_Select_Activity.class);
                    intent.putExtra("flag", "new");
                    startActivity(intent);
                } else {
                    Global.Messageshow(Campaign_List_Activity.this, mMainLayout, getResources().getString(R.string.add_contact), false);
                }
                break;
        }
    }
    
    @Override
    public void OnClick(Campaign_List.Campaign campaign) {
        SessionManager.setCampaign_type("");
        SessionManager.setCampaign_type_name("");
        SessionManager.setCampaign_Day("");
        SessionManager.setCampaign_minute("");
        Global.count = 1;
        SessionManager.setTask(getApplicationContext(), new ArrayList<>());
        String contect_list_count = String.valueOf(campaign.getProspect());
        if (campaign.getStatus().equals("A")) {
            Intent intent = new Intent(getApplicationContext(), Campaign_Final_Start.class);
            intent.putExtra("sequence_id", campaign.getId());
            startActivity(intent);
        } else if (campaign.getStatus().equals("I")) {
            if (campaign.getStarted_on() != null && !campaign.getStarted_on().equals("") && campaign.getProspect() != 0) {
                Intent intent = new Intent(getApplicationContext(), Campaign_Final_Start.class);
                intent.putExtra("sequence_id", campaign.getId());
                startActivity(intent);
            } else {
                if (contect_list_count.equals("0")) {
                    Intent intent = new Intent(getApplicationContext(), Campaign_Overview.class);
                    intent.putExtra("sequence_id", campaign.getId());
                    startActivity(intent);
                    //   finish();
                } else {
                    SessionManager.setCampign_flag("read");
                    Intent intent = new Intent(getApplicationContext(), Campaign_Preview.class);
                    intent.putExtra("sequence_id", campaign.getId());
                    startActivity(intent);
                    finish();
                    
                }
            }
        }
        
    }
    
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    
    public void showAlertDialogButtonClicked(int sequence_id, int status,int position,Campaign_List.Campaign campaign) {
        Log.e("Postion is ",String.valueOf(position));
        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, R.style.MyDialogStyle);
        
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.campanign_aleart_dialog, null);
        builder.setView(customLayout);
        AlertDialog dialog
                = builder.create();
        
        TextView tv_message = customLayout.findViewById(R.id.tv_message);
        if (status == 1) {
            tv_message.setText("Are you sure you want to pause the campaign");
        } else {
            tv_message.setText("Are you sure you want to start the campaign");
        }
        TextView tv_ok = customLayout.findViewById(R.id.tv_ok);
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
                StartCampignApi(sequence_id, status, dialog,position,campaign);
            }
        });
        dialog.show();
    }
    
    public void StartCampignApi(int sequence_id, int status, AlertDialog dialog,int position,Campaign_List.Campaign campaign) {
        Log.e("Postion is ",String.valueOf(position));
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        
        
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("record_id", sequence_id);
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        if (status == 1) {
            paramObject.addProperty("status", "I");
        } else {
            paramObject.addProperty("status", "A");
        }
        obj.add("data", paramObject);
        retrofitCalls.Sequence_settings(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(Campaign_List_Activity.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                        
                        if (response.body().getHttp_status() == 200) {
                            //onResume();
                            if (status == 1) {
                               // paramObject.addProperty("status", "I");
                                campaign.setStatus("I");
                                campaingAdepter.notifyDataSetChanged();
                            } else {
                                campaign.setStatus("A");
                                campaingAdepter.notifyDataSetChanged();
                            }
                            dialog.dismiss();
                        } else if (response.body().getHttp_status() == 403) {
                            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.plan_validation), false);
                            dialog.dismiss();
                        } else {
                            Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), false);
                            dialog.dismiss();
                        }
                        
                    }
                    
                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                        dialog.dismiss();
                    }
                });
    }
    
    public class CampaingAdepter extends RecyclerView.Adapter<CampaingAdepter.viewData> {
        private static final int VIEW_TYPE_LOADING = 0;
        private static final int VIEW_TYPE_NORMAL = 1;
        public Context mCtx;
        CampaingClick campaingClick;
        private boolean isLoaderVisible = false;
        private List<Campaign_List.Campaign> campaignList = new ArrayList<>();
        
        public CampaingAdepter(Context context, List<Campaign_List.Campaign> campaignList, CampaingClick campaingClick) {
            this.mCtx = context;
            this.campaignList = campaignList;
            this.campaingClick = campaingClick;
        }
        
        @NonNull
        @Override
        public CampaingAdepter.viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            
            switch (viewType) {
                case VIEW_TYPE_NORMAL:
                    return new viewData(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_campaing, parent, false));
                case VIEW_TYPE_LOADING:
                    return new ProgressHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
                default:
                    return null;
            }
        }
        
        @Override
        public int getItemViewType(int position) {
            if (isLoaderVisible) {
                return position == campaignList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
            } else {
                return VIEW_TYPE_NORMAL;
            }
        }
        
        public void addItems(List<Campaign_List.Campaign> postItems) {
            campaignList.addAll(postItems);
            notifyDataSetChanged();
        }
        
        public void addLoading() {
            isLoaderVisible = true;
            campaignList.add(new Campaign_List.Campaign());
            notifyItemInserted(campaignList.size() - 1);
        }
        
        public void removeLoading() {
            isLoaderVisible = false;
            int position = campaignList.size() - 1;
            Campaign_List.Campaign item = getItem(position);
            if (item != null) {
                campaignList.remove(position);
                notifyItemRemoved(position);
            }
        }
        
        public void clear() {
            campaignList.clear();
            notifyDataSetChanged();
        }
        
        Campaign_List.Campaign getItem(int position) {
            return campaignList.get(position);
        }
        
        @Override
        public void onBindViewHolder(@NonNull CampaingAdepter.viewData holder, int position) {
            Campaign_List.Campaign campaign = campaignList.get(position);
            if (Global.IsNotNull(campaign.getSeqName())) {
                holder.campaign_name.setText(campaign.getSeqName());
                setImage(campaign, holder);
                
              /*  holder.layout_item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        switch (campaign.getStatus()) {
                            case "A":
                                showAlertDialogButtonClicked(campaign.getId(), 1);
                                break;
                            case "I":
                                if (campaign.getStarted_on() != null && !campaign.getStarted_on().equals("")) {
                                    showAlertDialogButtonClicked(campaign.getId(), 0);
                                } else {
                                    showAlertDialogButtonClicked(campaign.getId(), 3);
                                }
                                break;
                        }
                        return true;
                    }
                });*/
                
                holder.layout_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        campaingClick.OnClick(campaign);
                    }
                });
                holder.lay_btn_hold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showAlertDialogButtonClicked(campaign.getId(), 3,position,campaign);
                        
                    }
                });
                holder.lay_btn_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showAlertDialogButtonClicked(campaign.getId(), 0,position,campaign);
                    }
                });
                holder.lay_btn_pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showAlertDialogButtonClicked(campaign.getId(), 1,position,campaign);
                    }
                });
                
            }
            
            
        }

        private void setImage(Campaign_List.Campaign campaign, viewData holder) {
            switch (campaign.getStatus()) {
                case "A":
                    /* status active */
                    holder.tv_status.setText(mCtx.getResources().getString(R.string.Active));
                    holder.tv_status.setTextColor(mCtx.getResources().getColor(R.color.Active_green));
                    /* button paush show */
                    holder.lay_btn_pause.setVisibility(View.VISIBLE);
                    holder.lay_btn_hold.setVisibility(View.GONE);
                    holder.lay_btn_play.setVisibility(View.GONE);
                    break;
                case "I":
                    if (campaign.getStarted_on() != null && !campaign.getStarted_on().equals("")) {
                        /* status paused */
                        holder.tv_status.setText(mCtx.getResources().getString(R.string.Paused));
                        holder.tv_status.setTextColor(mCtx.getResources().getColor(R.color.Paused_yellow));
                        /* button start show */
                        holder.lay_btn_play.setVisibility(View.VISIBLE);
                        holder.lay_btn_hold.setVisibility(View.GONE);
                        holder.lay_btn_pause.setVisibility(View.GONE);
                    } else {
                        if(campaign.getProspect()!=0){
                            /* status not active */
                            holder.tv_status.setText(mCtx.getResources().getString(R.string.noactive));
                            holder.tv_status.setTextColor(mCtx.getResources().getColor(R.color.notactive));
                            /* button start show */
                            holder.lay_btn_play.setVisibility(View.VISIBLE);
                            holder.lay_btn_hold.setVisibility(View.GONE);
                            holder.lay_btn_pause.setVisibility(View.GONE);
                        }else {
                            /* status inactive */
                            holder.tv_status.setText(mCtx.getResources().getString(R.string.Inactive));
                            holder.tv_status.setTextColor(mCtx.getResources().getColor(R.color.Inactive_red));
                            /* button hold show */
                            holder.lay_btn_hold.setVisibility(View.VISIBLE);
                            holder.lay_btn_pause.setVisibility(View.GONE);
                            holder.lay_btn_play.setVisibility(View.GONE);
                        }
                       
                    }
                    break;
            }
        }
        
        @Override
        public int getItemCount() {
            return campaignList.size();
        }
        
        
        public class viewData extends RecyclerView.ViewHolder {
            TextView campaign_name,tv_status;
            LinearLayout layout_item, lay_btn_hold,lay_btn_pause,lay_btn_play;
            
            public viewData(@NonNull View itemView) {
                super(itemView);
                campaign_name = itemView.findViewById(R.id.campaign_name);
                tv_status = itemView.findViewById(R.id.tv_status);
                lay_btn_hold = itemView.findViewById(R.id.lay_btn_hold);
                lay_btn_pause = itemView.findViewById(R.id.lay_btn_pause);
                lay_btn_play = itemView.findViewById(R.id.lay_btn_play);
                layout_item = itemView.findViewById(R.id.layout_item);
            }
        }
        
        public class ProgressHolder extends viewData {
            ProgressHolder(View itemView) {
                super(itemView);
            }
            
        }
    }
}