package com.contactninja.Campaign;

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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Response;

import com.contactninja.Broadcast.Broadcast_Frgment.CardClick;
import com.contactninja.Broadcast.Broadcast_Schedule.Broadcast_to_repeat;
import com.contactninja.Broadcast.Brodcsast_Tankyou;
import com.contactninja.Campaign.Fragment.View_Contect_Fragment;
import com.contactninja.Model.Broadcast_Data;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Campaign_Viewcontect extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener  {
    TabLayout tabLayout;
    ViewPager viewPager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    EditText contect_search;
    String strtext = "";
    ViewpaggerAdapter adapter;
    ImageView search_icon;
    ImageView iv_back, iv_Setting;
    TextView save_button;
    private BroadcastReceiver mNetworkReceiver;
    LinearLayout main_layout;
    SessionManager sessionManager;
    int sequence_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_viewcontect);
        mNetworkReceiver = new ConnectivityReceiver();
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();
        sessionManager=new SessionManager(this);
        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
       // tabLayout.addTab(tabLayout.newTab().setText("Groups"));
        Fragment fragment = new View_Contect_Fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment, "Fragment");
        fragmentTransaction.commitAllowingStateLoss();

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

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contect_search.requestFocus();
            }
        });
    }

    private void IntentUI() {
        main_layout = findViewById(R.id.main_layout);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        contect_search = findViewById(R.id.contect_search);
        search_icon = findViewById(R.id.search_icon);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.GONE);
        iv_back.setOnClickListener(this);
        save_button.setOnClickListener(this);
        save_button.setVisibility(View.GONE);
        save_button.setText("Next");
        save_button.setTextColor(getResources().getColor(R.color.purple_200));
    }

    @Override
    protected void onResume() {
        StepData();
        super.onResume();
    }
    public void StepData() {
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
                Global.getVersionname(Campaign_Viewcontect.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {


                        if (response.body().getHttp_status() == 200) {

                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            Type listType = new TypeToken<CampaignTask_overview>() {
                            }.getType();

                            CampaignTask_overview user_model1 = new Gson().fromJson(headerString, listType);
                            //Log.e("User Model",new Gson().toJson(user_model1));
                            SessionManager.setCampaign_data(user_model1);
                            //  Log.e("Email Task",user_model1.getSequenceTask().get(0).getActiveTaskEmail().toString());
                            // Log.e("SMS",user_model1.getSequenceTask().get(0).getActiveTaskContactNumber().toString());

                            //  tv_email.setText(user_model1.getSequenceTask().get(0).getActiveTaskEmail().toString());
                            //tv_sms.setText(user_model1.getSequenceTask().get(0).getActiveTaskContactNumber().toString());
                            int sms_count=0;
                            int email_count=0;
                            for (int i=0;i<user_model1.getSequenceTask().size();i++)
                            {
                                if (user_model1.getSequenceTask().get(i).getType().equals("SMS"))
                                {
                                    sms_count=sms_count+1;
                                }
                                else {
                                    email_count=email_count+1;
                                }
                            }

                        } else {
                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            // Global.Messageshow(getApplicationContext(), mMainLayout, headerString, false);

                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {

                    }
                });
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();

                break;
            case R.id.save_button:

                break;
        }
    }
    static class ViewpaggerAdapter extends FragmentPagerAdapter {

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
                    View_Contect_Fragment contectFragment = new View_Contect_Fragment();
                    return contectFragment;

            /*    case 1:
                    Broadcast_Group_Fragment c_Fragment = new Broadcast_Group_Fragment();
                    return c_Fragment;*/
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
        Global.checkConnectivity(Campaign_Viewcontect.this, main_layout);
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



}