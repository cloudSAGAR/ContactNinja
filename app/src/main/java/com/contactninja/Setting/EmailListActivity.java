package com.contactninja.Setting;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
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

import com.bumptech.glide.Glide;
import com.contactninja.MainActivity;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UserLinkedList;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class EmailListActivity extends AppCompatActivity implements View.OnClickListener,
        ConnectivityReceiver.ConnectivityReceiverListener,  SwipeRefreshLayout.OnRefreshListener {
    ImageView iv_back;
    private BroadcastReceiver mNetworkReceiver;
    public static LinearLayout mMainLayout;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RecyclerView rv_email_list;
   public static EmailAdepter emailAdepter;

    SwipeRefreshLayout swipeToRefresh;
    List<UserLinkedList.UserLinkedGmail> userLinkedGmailList=new ArrayList<>();
    LinearLayout lay_no_list,add_new_email;
    private long mLastClickTime=0;
    boolean CheckScreen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_list);
        mNetworkReceiver = new ConnectivityReceiver();
        retrofitCalls = new RetrofitCalls(EmailListActivity.this);
        loadingDialog=new LoadingDialog(EmailListActivity.this);
        sessionManager=new SessionManager(EmailListActivity.this);
        IntentUI();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(Global.isNetworkAvailable(EmailListActivity.this, MainActivity.mMainLayout)) {
                Mail_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void Mail_list() throws JSONException {
        if(!swipeToRefresh.isRefreshing()){
            loadingDialog.showLoadingDialog();
        }
        SignResponseModel signResponseModel= SessionManager.getGetUserdata(EmailListActivity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("include_smtp",1);
        obj.add("data", paramObject);
        retrofitCalls.Mail_list(sessionManager,obj, loadingDialog, token,Global.getVersionname(EmailListActivity.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                swipeToRefresh.setRefreshing(false);
                if (response.body().getHttp_status() == 200) {
                    userLinkedGmailList.clear();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UserLinkedList>() {
                    }.getType();
                    UserLinkedList userLinkedGmail=new Gson().fromJson(headerString, listType);

                    userLinkedGmailList=userLinkedGmail.getUserLinkedGmail();
                    if (userLinkedGmailList.size() == 0) {
                        if(!CheckScreen){
                            CheckScreen=true;
                            startActivity(new Intent(getApplicationContext(), Verification_web.class));
                        }
                    }else {
                        lay_no_list.setVisibility(View.GONE);
                        rv_email_list.setVisibility(View.VISIBLE);
                        sessionManager.setUserLinkedGmail(getApplicationContext(),userLinkedGmailList);
                    }
                    rv_email_list.setLayoutManager(new LinearLayoutManager(EmailListActivity.this, LinearLayoutManager.VERTICAL, false));
                    emailAdepter=new EmailAdepter(EmailListActivity.this,userLinkedGmailList);
                    rv_email_list.setAdapter(emailAdepter);

                }else {
                    if(!CheckScreen){
                        CheckScreen=true;
                        startActivity(new Intent(getApplicationContext(), Verification_web.class));
                    }
                    lay_no_list.setVisibility(View.VISIBLE);
                    rv_email_list.setVisibility(View.GONE);
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                swipeToRefresh.setRefreshing(false);
                loadingDialog.cancelLoading();
            }
        });


    }
    private void IntentUI() {
        lay_no_list = findViewById(R.id.lay_no_list);
        add_new_email = findViewById(R.id.add_new_email);
        rv_email_list = findViewById(R.id.rv_email_list);
        mMainLayout = findViewById(R.id.mMainLayout);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        add_new_email.setOnClickListener(this);




        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
                case R.id.add_new_email:
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    // Global.openEmailAuth(EmailListActivity.this);
                    Intent intent= new Intent(getApplicationContext(), Verification_web.class);
                    intent.putExtra("create","create");
                    startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onRefresh() {
        try {
            if(Global.isNetworkAvailable(EmailListActivity.this, MainActivity.mMainLayout)) {
                Mail_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(EmailListActivity.this, mMainLayout);
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

    public class EmailAdepter extends RecyclerView.Adapter<EmailAdepter.viewData> {

        public Context mCtx;
        List<UserLinkedList.UserLinkedGmail> userLinkedGmailList;
        public EmailAdepter(Context context,   List<UserLinkedList.UserLinkedGmail> userLinkedGmailList) {
            this.mCtx = context;
            this.userLinkedGmailList = userLinkedGmailList;
        }

        @NonNull
        @Override
        public EmailAdepter.viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_emaillist, parent, false);
            return new EmailAdepter.viewData(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EmailAdepter.viewData holder, int position) {
            UserLinkedList.UserLinkedGmail userLinkedGmail=userLinkedGmailList.get(position);

            Glide.with(mCtx)
                    .load(userLinkedGmail.getPicture())
                    .circleCrop()
                    .placeholder(Global.setplaceholder(mCtx))
                    .into(holder.iv_select_type);
            holder.tv_email_name.setText(userLinkedGmail.getUserEmail());
            if(userLinkedGmail.getIsDefault().equals(1)){
                holder.iv_is_default.setVisibility(View.VISIBLE);
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            }else {
                holder.iv_is_default.setVisibility(View.GONE);
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }
            holder.iv_unselected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    try {
                        if(Global.isNetworkAvailable(EmailListActivity.this, EmailListActivity.mMainLayout)) {
                            UpdateEmail_default(String.valueOf(userLinkedGmail.getId()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return userLinkedGmailList.size();
        }


        public class viewData extends RecyclerView.ViewHolder {
            TextView tv_email_name;
            LinearLayout layout_email;
            ImageView iv_is_default,iv_selected,iv_unselected;
            RoundedImageView iv_select_type;
            public viewData(@NonNull View itemView) {
                super(itemView);
                tv_email_name = itemView.findViewById(R.id.tv_email_name);
                layout_email = itemView.findViewById(R.id.layout_email);
                iv_select_type = itemView.findViewById(R.id.iv_select_type);
                iv_is_default = itemView.findViewById(R.id.iv_is_default);
                iv_selected = itemView.findViewById(R.id.iv_selected);
                iv_unselected = itemView.findViewById(R.id.iv_unselected);
            }
        }
    }

    void UpdateEmail_default(String email_id) throws JSONException {
        SignResponseModel signResponseModel= SessionManager.getGetUserdata(EmailListActivity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("model", "google_auth");
        paramObject.addProperty("is_default", "1");
        paramObject.addProperty("id", email_id);
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Mail_setDefault(sessionManager,obj, loadingDialog, token,Global.getVersionname(EmailListActivity.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    try {
                        if(Global.isNetworkAvailable(EmailListActivity.this, MainActivity.mMainLayout)) {
                            Mail_list();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }
}