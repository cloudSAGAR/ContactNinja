package com.contactninja.Bzcard;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.contactninja.MainActivity;
import com.contactninja.Model.AnaliticsModel;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Bz_card_Analytics extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, SwipeRefreshLayout.OnRefreshListener {
    LinearLayout mMainLayout, lay_no_list, layout_list;
    ImageView iv_back;
    RecyclerView rv_Affiliate_list;
    TextView tv_analytics;
    SwipeRefreshLayout swipeToRefresh;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    List<AnaliticsModel.Impression> impressionList = new ArrayList<>();
    AnaliticsAdepter analiticsAdepter;
    int Bz_card_id_ = 0, total_Analytic = 0;
    private BroadcastReceiver mNetworkReceiver;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bz_card_analytics);
        mNetworkReceiver = new ConnectivityReceiver();
        retrofitCalls = new RetrofitCalls(Bz_card_Analytics.this);
        loadingDialog = new LoadingDialog(Bz_card_Analytics.this);
        sessionManager = new SessionManager(Bz_card_Analytics.this);
        initUI();
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bz_card_id_ = extras.getInt("Bzcard_id");
        }
        
        try {
            if (Global.isNetworkAvailable(Bz_card_Analytics.this, MainActivity.mMainLayout)) {
                Analytics_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    void Analytics_list() throws JSONException {
        if (!swipeToRefresh.isRefreshing()) {
            loadingDialog.showLoadingDialog();
        }
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Bz_card_Analytics.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("usercard_id", Bz_card_id_);
        obj.add("data", paramObject);
        retrofitCalls.Analitycs_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Bz_card_Analytics.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                swipeToRefresh.setRefreshing(false);
                if (response.body().getHttp_status() == 200) {
                    impressionList.clear();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<AnaliticsModel>() {
                    }.getType();
                    AnaliticsModel analiticsModel = new Gson().fromJson(headerString, listType);
                    
                    impressionList = analiticsModel.getImpressionData();
                    total_Analytic = analiticsModel.getTotalImpressions();
                    tv_analytics.setText(String.valueOf(total_Analytic));
                    
                    if (Global.IsNotNull(impressionList) && impressionList.size() != 0) {
                        lay_no_list.setVisibility(View.GONE);
                        layout_list.setVisibility(View.VISIBLE);
                        rv_Affiliate_list.setLayoutManager(new LinearLayoutManager(Bz_card_Analytics.this, LinearLayoutManager.VERTICAL, false));
                        analiticsAdepter = new AnaliticsAdepter(Bz_card_Analytics.this, impressionList);
                        rv_Affiliate_list.setAdapter(analiticsAdepter);
                    } else {
                        lay_no_list.setVisibility(View.VISIBLE);
                        layout_list.setVisibility(View.GONE);
                    }
                    
                }
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
                swipeToRefresh.setRefreshing(false);
                loadingDialog.cancelLoading();
            }
        });
        
        
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        layout_list = findViewById(R.id.layout_list);
        rv_Affiliate_list = findViewById(R.id.rv_Affiliate_list);
        lay_no_list = findViewById(R.id.lay_no_list);
        tv_analytics = findViewById(R.id.tv_analytics);
        
        
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(this);
        
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Bz_card_Analytics.this, mMainLayout);
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
        try {
            if (Global.isNetworkAvailable(Bz_card_Analytics.this, MainActivity.mMainLayout)) {
                Analytics_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    
    public class AnaliticsAdepter extends RecyclerView.Adapter<AnaliticsAdepter.viewData> {
        
        public Context mCtx;
        List<AnaliticsModel.Impression> impressionList;
        
        public AnaliticsAdepter(Context context, List<AnaliticsModel.Impression> impressionList) {
            this.mCtx = context;
            this.impressionList = impressionList;
        }
        
        @NonNull
        @Override
        public AnaliticsAdepter.viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_analitics, parent, false);
            return new AnaliticsAdepter.viewData(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull AnaliticsAdepter.viewData holder, int position) {
            AnaliticsModel.Impression impression = impressionList.get(position);
            
            
            holder.tv_date.setText(String.valueOf(Global_Time.parseDateToddMMyyyy(impression.getCreatedAt())));
            holder.tv_time.setText(String.valueOf(Global_Time.parsetime(impression.getCreatedAt())));
            if (Global.IsNotNull(impression.getUserCountry())) {
                holder.tv_country.setText(String.valueOf(impression.getUserCountry()));
            }
            holder.tv_browser.setText(String.valueOf(Global.parseSpaceFirst(impression.getUserAgent())));
            
        }
        
        @Override
        public int getItemCount() {
            return impressionList.size();
        }
        
        
        public class viewData extends RecyclerView.ViewHolder {
            TextView tv_browser, tv_country, tv_time, tv_date;
            
            public viewData(@NonNull View itemView) {
                super(itemView);
                tv_browser = itemView.findViewById(R.id.tv_browser);
                tv_country = itemView.findViewById(R.id.tv_country);
                tv_time = itemView.findViewById(R.id.tv_time);
                tv_date = itemView.findViewById(R.id.tv_date);
                
            }
        }
    }
}