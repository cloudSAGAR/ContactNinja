package com.contactninja.UserPofile;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.contactninja.Fragment.AddContect_Fragment.GroupFragment;
import com.contactninja.Group.GroupActivity;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class Affiliate_Report_LavelActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private BroadcastReceiver mNetworkReceiver;

    LinearLayout mMainLayout;
    ImageView iv_back;

    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;

    RecyclerView lavel_list;
    LavelAdapter lavelAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliate_report_lavel);
        sessionManager = new SessionManager(Affiliate_Report_LavelActivity.this);
        retrofitCalls = new RetrofitCalls(Affiliate_Report_LavelActivity.this);
        loadingDialog = new LoadingDialog(Affiliate_Report_LavelActivity.this);

        IntentView();

        lavelAdapter = new LavelAdapter(Affiliate_Report_LavelActivity.this);
        lavel_list.setAdapter(lavelAdapter);


        mNetworkReceiver = new ConnectivityReceiver();


    }

    private void IntentView() {
        lavel_list=findViewById(R.id.lavel_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Affiliate_Report_LavelActivity.this);
        lavel_list.setLayoutManager(layoutManager);
        mMainLayout=findViewById(R.id.mMainLayout);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Affiliate_Report_LavelActivity.this, mMainLayout);
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




}
class LavelAdapter extends RecyclerView.Adapter<LavelAdapter.viewholder> {

    public Context mCtx;



    public LavelAdapter(Context applicationContext) {
        this.mCtx = applicationContext;

    }

    @NonNull
    @Override
    public LavelAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.lavel_item, parent, false);
        return new LavelAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LavelAdapter.viewholder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tv_user_name;


        public viewholder(View view) {
            super(view);
            tv_user_name = view.findViewById(R.id.tv_user_name);

        }
    }
}