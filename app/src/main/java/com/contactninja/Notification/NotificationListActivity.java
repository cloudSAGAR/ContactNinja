package com.contactninja.Notification;

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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.contactninja.Interface.NotificationClick;
import com.contactninja.MainActivity;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class NotificationListActivity extends AppCompatActivity implements View.OnClickListener ,
        ConnectivityReceiver.ConnectivityReceiverListener, SwipeRefreshLayout.OnRefreshListener, NotificationClick {
    ImageView iv_back;
    LinearLayout mMainLayout;
    private BroadcastReceiver mNetworkReceiver;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RecyclerView rv_notification_list;
    SwipeRefreshLayout swipeToRefresh;
    NotifiveAdepter notifiveAdepter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        IntentView();
        mNetworkReceiver = new ConnectivityReceiver();
        retrofitCalls = new RetrofitCalls(NotificationListActivity.this);
        loadingDialog=new LoadingDialog(NotificationListActivity.this);
        sessionManager=new SessionManager(NotificationListActivity.this);






        rv_notification_list.setLayoutManager(new LinearLayoutManager(NotificationListActivity.this, LinearLayoutManager.VERTICAL, false));
        notifiveAdepter= new NotifiveAdepter(NotificationListActivity.this,this);
        rv_notification_list.setAdapter(notifiveAdepter);

    }

    private void IntentView() {
        rv_notification_list=findViewById(R.id.rv_notification_list);
        mMainLayout=findViewById(R.id.mMainLayout);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        if(Global.isNetworkAvailable(NotificationListActivity.this, MainActivity.mMainLayout)) {
            // Mail_list();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(NotificationListActivity.this, mMainLayout);
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
    public void OnClick() {
        startActivity(new Intent(getApplicationContext(),Notification_task_acceptActivity.class));
    }

    public static class NotifiveAdepter extends RecyclerView.Adapter<NotifiveAdepter.viewData> {

        public Context mCtx;
        NotificationClick itemclick;
        public NotifiveAdepter(Context context,NotificationClick notificationClick) {
            this.mCtx = context;
            this.itemclick=notificationClick;
        }

        @NonNull
        @Override
        public NotifiveAdepter.viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_notificationlist, parent, false);
            return new viewData(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NotifiveAdepter.viewData holder, int position) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemclick.OnClick();
                }
            });

        }

        @Override
        public int getItemCount() {
            return 5;
        }


        public static class viewData extends RecyclerView.ViewHolder {

            public viewData(@NonNull View itemView) {
                super(itemView);

            }
        }
    }

}