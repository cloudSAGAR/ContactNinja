package com.contactninja.Email;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import com.contactninja.Model.EmailActivityListModel;
import com.contactninja.Model.ManualTaskModel;
import com.contactninja.Model.UserData.SignResponseModel;
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

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables")
public class Email_List_Activity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener, SwipeRefreshLayout.OnRefreshListener {
    LinearLayout demo_layout, add_new_contect_layout, mMainLayout;
    TextView tv_create;
    RecyclerView rv_email_list;
    SwipeRefreshLayout swipeToRefresh;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    EmailAdepter emailAdepter;
    List<ManualTaskModel> manualTaskModelList = new ArrayList<>();
    ImageView iv_back;
    private BroadcastReceiver mNetworkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_list2);
        mNetworkReceiver = new ConnectivityReceiver();
        retrofitCalls = new RetrofitCalls(Email_List_Activity.this);
        loadingDialog = new LoadingDialog(Email_List_Activity.this);
        sessionManager = new SessionManager(Email_List_Activity.this);
        intentView();
    }

    private void intentView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        demo_layout = findViewById(R.id.demo_layout);
        mMainLayout = findViewById(R.id.mMainLayout);
        demo_layout.setOnClickListener(this);
        tv_create = findViewById(R.id.tv_create);
        tv_create.setText(getString(R.string.email_txt));
        rv_email_list = findViewById(R.id.email_list);
        add_new_contect_layout = findViewById(R.id.add_new_contect_layout);
        add_new_contect_layout.setOnClickListener(this);
        rv_email_list.setLayoutManager(new LinearLayoutManager(this));
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.add_new_contect_layout:
                SessionManager.setGroupList(getApplicationContext(), new ArrayList<>());
                startActivity(new Intent(getApplicationContext(), Email_Selction_Activity.class));
                //finish();
                break;
            case R.id.demo_layout:
                SessionManager.setGroupList(getApplicationContext(), new ArrayList<>());
                startActivity(new Intent(getApplicationContext(), Email_Selction_Activity.class));
                //  finish();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (Global.isNetworkAvailable(Email_List_Activity.this, MainActivity.mMainLayout)) {
                Mail_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void Mail_list() throws JSONException {

        if (!swipeToRefresh.isRefreshing()) {
            loadingDialog.showLoadingDialog();
        }
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Email_List_Activity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Mail_Activiy_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(Email_List_Activity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                swipeToRefresh.setRefreshing(false);
                if (response.body().getStatus() == 200) {
                    manualTaskModelList.clear();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<EmailActivityListModel>() {
                    }.getType();
                    EmailActivityListModel emailActivityListModel = new Gson().fromJson(headerString, listType);

                    manualTaskModelList = emailActivityListModel.getManualTask();

                    rv_email_list.setLayoutManager(new LinearLayoutManager(Email_List_Activity.this, LinearLayoutManager.VERTICAL, false));
                    emailAdepter = new EmailAdepter(Email_List_Activity.this, manualTaskModelList);
                    rv_email_list.setAdapter(emailAdepter);


                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                swipeToRefresh.setRefreshing(false);
                loadingDialog.cancelLoading();
            }
        });

    }

    @Override
    public void onRefresh() {
        try {
            if (Global.isNetworkAvailable(Email_List_Activity.this, MainActivity.mMainLayout)) {
                Mail_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Email_List_Activity.this, mMainLayout);
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
        List<ManualTaskModel> manualTaskModelList;

        public EmailAdepter(Context context, List<ManualTaskModel> manualTaskModelList) {
            this.mCtx = context;
            this.manualTaskModelList = manualTaskModelList;
        }

        @NonNull
        @Override
        public EmailAdepter.viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_emailactivitylist, parent, false);
            return new EmailAdepter.viewData(view);
        }

        @SuppressLint("LogConditional")
        @Override
        public void onBindViewHolder(@NonNull EmailAdepter.viewData holder, int position) {
            ManualTaskModel item = manualTaskModelList.get(position);
            holder.tv_username.setText(item.getUserName());
            holder.tv_task_description.setText(item.getTaskDescription());
            try {
                String time =Global.getDate(item.getStartTime());
                holder.tv_time.setText(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String name =item.getUserName();
            String add_text="";
            String[] split_data=name.split(" ");
            try {
                for (int i=0;i<split_data.length;i++)
                {
                    if (i==0)
                    {
                        add_text=split_data[i].substring(0,1);
                    }
                    else {
                        add_text=add_text+split_data[i].substring(0,1);
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            holder.no_image.setText(add_text);
            holder.no_image.setVisibility(View.VISIBLE);
            holder.profile_image.setVisibility(View.GONE);

        }

        @Override
        public int getItemCount() {
            return manualTaskModelList.size();
        }


        public class viewData extends RecyclerView.ViewHolder {
            TextView tv_username, tv_task_description, tv_time,no_image;
            CircleImageView profile_image;
            public viewData(@NonNull View itemView) {
                super(itemView);
                tv_username = itemView.findViewById(R.id.tv_username);
                tv_task_description = itemView.findViewById(R.id.tv_task_description);
                tv_time = itemView.findViewById(R.id.tv_time);
                no_image = itemView.findViewById(R.id.no_image);
                profile_image = itemView.findViewById(R.id.profile_image);

            }
        }
    }

}