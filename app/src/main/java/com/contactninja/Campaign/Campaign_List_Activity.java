package com.contactninja.Campaign;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
        SwipeRefreshLayout.OnRefreshListener, CampaingClick, ConnectivityReceiver.ConnectivityReceiverListener  {
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back;
    TextView tv_create, sub_txt;
    LinearLayout demo_layout, add_campaign_layout, mMainLayout1,mMainLayout;
    EditText ev_search;
    SwipeRefreshLayout swipeToRefresh;
    RecyclerView rv_campaign_list;


    CampaingAdepter campaingAdepter;
    List<Campaign_List.Campaign> campaignList = new ArrayList<>();
    int perPage = 20;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    private BroadcastReceiver mNetworkReceiver;

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
                     onResume();
                    return true;
                }
                return false;
            }
        });
    }

    private void IntentUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        mMainLayout1 = findViewById(R.id.mMainLayout1);
        rv_campaign_list = findViewById(R.id.campaign_list);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        demo_layout = findViewById(R.id.demo_layout);
        tv_create = findViewById(R.id.tv_create);
        sub_txt = findViewById(R.id.sub_txt);
        ev_search = findViewById(R.id.ev_search);
        add_campaign_layout = findViewById(R.id.add_campaign_layout);
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
        currentPage = PAGE_START;
        isLastPage = false;
        campaingAdepter.clear();
        campaignList.clear();
        try {
            if (Global.isNetworkAvailable(Campaign_List_Activity.this, MainActivity.mMainLayout)) {
                Campaing_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Campaing_list() throws JSONException {
        if (!swipeToRefresh.isRefreshing()) {
            loadingDialog.showLoadingDialog();
        }

        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("q", ev_search.getText().toString());
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

                            if (campaignList.size() == 0) {
                                demo_layout.setVisibility(View.VISIBLE);
                                mMainLayout1.setVisibility(View.GONE);
                            } else {
                                demo_layout.setVisibility(View.GONE);
                                mMainLayout1.setVisibility(View.VISIBLE);
                            }


                            if (currentPage != PAGE_START) campaingAdepter.removeLoading();
                            campaingAdepter.addItems(campaignList);
                            swipeToRefresh.setRefreshing(false);
                            // check weather is last page or not
                            if (campaignList.size() >= perPage) {
                                campaingAdepter.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;

                        } else {
                            // Global.Messageshow(getApplicationContext(), mMainLayout, headerString, false);

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
            case R.id.demo_layout:
                SessionManager.setCampaign_type("");
                SessionManager.setCampaign_type_name("");
                SessionManager.setCampaign_Day("");
                SessionManager.setCampaign_minute("");
                Global.count = 1;
                SessionManager.setTask(getApplicationContext(), new ArrayList<>());
                Intent intent=new Intent(getApplicationContext(),First_Step_Activity.class);
                intent.putExtra("flag","new");
                startActivity(intent);
                //finish();
                break;
            case R.id.add_campaign_layout:
                SessionManager.setCampaign_type("");
                SessionManager.setCampaign_type_name("");
                SessionManager.setCampaign_Day("");
                SessionManager.setCampaign_minute("");
                Global.count = 1;
                SessionManager.setTask(getApplicationContext(), new ArrayList<>());
                Intent intent1=new Intent(getApplicationContext(),First_Step_Activity.class);
                intent1.putExtra("flag","new");
                startActivity(intent1);
                // finish();
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
        if (campaign.getStatus().equals("A"))
        {
            Intent intent = new Intent(getApplicationContext(), Campaign_Final_Start.class);
            intent.putExtra("sequence_id", campaign.getId());
            startActivity(intent);
        }else if (contect_list_count.equals("0")) {

            Intent intent = new Intent(getApplicationContext(), Campaign_Overview.class);
            intent.putExtra("sequence_id", campaign.getId());
            startActivity(intent);
         //   finish();
        } else {
            SessionManager.setCampign_flag("read");
            Intent intent = new Intent(getApplicationContext(), Campaign_Preview.class);
            intent.putExtra("sequence_id", campaign.getId());
            startActivity(intent);
            //finish();

        }

    }


    public static class CampaingAdepter extends RecyclerView.Adapter<CampaingAdepter.viewData> {
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
            holder.campaign_name.setText(campaign.getSeqName());
            setImage(campaign, holder);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    campaingClick.OnClick(campaign);
                }
            });

        }

        private void setImage(Campaign_List.Campaign campaign, viewData holder) {
            switch (campaign.getStatus()) {
                case "A":
                    holder.iv_hold.setVisibility(View.GONE);
                    holder.iv_play_icon.setVisibility(View.VISIBLE);
                    holder.iv_puse_icon.setVisibility(View.GONE);
                    break;
                case "I":
                    if (campaign.getStarted_on() != null) {
                        holder.iv_puse_icon.setVisibility(View.VISIBLE);
                        holder.iv_hold.setVisibility(View.GONE);
                    } else {
                        holder.iv_hold.setVisibility(View.VISIBLE);
                        holder.iv_puse_icon.setVisibility(View.GONE);
                    }
                    holder.iv_play_icon.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return campaignList.size();
        }


        public static class viewData extends RecyclerView.ViewHolder {
            TextView campaign_name;
            ImageView iv_hold, iv_puse_icon, iv_play_icon;

            public viewData(@NonNull View itemView) {
                super(itemView);
                campaign_name = itemView.findViewById(R.id.campaign_name);
                iv_hold = itemView.findViewById(R.id.iv_hold);
                iv_puse_icon = itemView.findViewById(R.id.iv_puse_icon);
                iv_play_icon = itemView.findViewById(R.id.iv_play_icon);
            }
        }

        public static class ProgressHolder extends viewData {
            ProgressHolder(View itemView) {
                super(itemView);
            }

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}