package com.contactninja.Campaign.List_itm;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.contactninja.Campaign.Fragment.Campaign_Stats_Fragment;
import com.contactninja.Campaign.Fragment.Campaign_Step_Fragment;
import com.contactninja.Model.Broadcast_image_list;
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
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Campaign_Final_Start extends AppCompatActivity  implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener{
    TabLayout tabLayout;
    ViewPager viewPager;
    String strtext = "";
    Campaign_Step_Fragment campaign_step_fragment;
    ViewpaggerAdapter adapter;
    ImageView iv_back, iv_Setting;
    TextView save_button;
    List<Broadcast_image_list> broadcast_image_list=new ArrayList<>();
    private BroadcastReceiver mNetworkReceiver;

    LinearLayout main_layout;
    SessionManager sessionManager;
    int sequence_id,seq_task_id;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    TextView tv_email,tv_sms,tv_contect,tv_pending,tv_contec_reach,tv_camp_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_final_start);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        Global.count=1;
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        Intent getintent=getIntent();
        Bundle bundle=getintent.getExtras();
        sequence_id=bundle.getInt("sequence_id");
        StepData();

        tabLayout.addTab(tabLayout.newTab().setText("Steps"));
        tabLayout.addTab(tabLayout.newTab().setText("Stats"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new ViewpaggerAdapter(getApplicationContext(), getSupportFragmentManager(),
                tabLayout.getTabCount(), strtext);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private void IntentUI() {
        main_layout = findViewById(R.id.main_layout);
        tv_camp_name = findViewById(R.id.tv_camp_name);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.GONE);
        iv_back.setOnClickListener(this);
        save_button.setOnClickListener(this);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText(getString(R.string.view_contect));
        save_button.setTextColor(getResources().getColor(R.color.purple_200));
        tv_email=findViewById(R.id.tv_email);
        tv_sms=findViewById(R.id.tv_sms);
        tv_contect=findViewById(R.id.tv_contect);
        tv_pending=findViewById(R.id.tv_pending);
        tv_contec_reach=findViewById(R.id.tv_contec_reach);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:
                Intent getintent=getIntent();
                Bundle bundle=getintent.getExtras();
                sequence_id=bundle.getInt("sequence_id");
                Intent intent = new Intent(getApplicationContext(), Campaign_Viewcontect.class);
                intent.putExtra("sequence_id", sequence_id);
                startActivity(intent);
                break;
        }
    }

    class ViewpaggerAdapter extends FragmentPagerAdapter {

        Context context;
        int totalTabs;
        String strtext1;

        public ViewpaggerAdapter(Context c, FragmentManager fm, int totalTabs, String strtext1) {
            super(fm);
            context = c;
            this.totalTabs = totalTabs;
            this.strtext1 = strtext1;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    campaign_step_fragment = new Campaign_Step_Fragment();
                    return campaign_step_fragment;
                case 1:
                    Campaign_Stats_Fragment c_Fragment = new Campaign_Stats_Fragment();
                    return c_Fragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return totalTabs;
        }
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Campaign_Final_Start.this, main_layout);
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



    public void StepData() {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);

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
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("id", sequence_id);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
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
                Global.getVersionname(Campaign_Final_Start.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();

                        if (response.body().getHttp_status() == 200) {

                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            Type listType = new TypeToken<CampaignTask_overview>() {
                            }.getType();

                            CampaignTask_overview campData = new Gson().fromJson(headerString, listType);
                            SessionManager.setCampaign_data(campData);
                              try {
                                String prospect_count=campData.getSeqProspectCount().getTotal().toString();
                                tv_contect.setText(prospect_count);
                                tv_camp_name.setText(campData.get0().getSeqName());
                                int sms_count=0;
                                int email_count=0;
                                for (int i=0;i<campData.getSequenceTask().size();i++)
                                {
                                    if (campData.getSequenceTask().get(i).getType().equals("SMS"))
                                    {
                                        sms_count=sms_count+1;
                                    }
                                    else {
                                        email_count=email_count+1;
                                    }
                                }
                                tv_sms.setText(String.valueOf(sms_count));
                                tv_email.setText(String.valueOf(email_count));


                            }catch (Exception e){
                                e.printStackTrace();
                            }


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


}