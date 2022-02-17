package com.contactninja.Main_Broadcast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contactninja.Model.Broadcast_image_list;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables")
public class Broadcast_Contact_Selction_Actvity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    EditText contect_search;
    String strtext = "";
    ViewpaggerAdapter adapter;
    ImageView search_icon;
    ImageView iv_back, iv_Setting;
    TextView save_button;
    LinearLayout main_layout;
    SessionManager sessionManager;
    String sequence_Name = "", Activty_Back="",Select_tab="";
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    private BroadcastReceiver mNetworkReceiver;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect_and_group_actvity);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);


        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            Activty_Back = bundle.getString("Activty");
            Select_tab = bundle.getString("type");
        }catch (Exception e){
            e.printStackTrace();
        }

        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        tabLayout.addTab(tabLayout.newTab().setText("Groups"));
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
        save_button.setVisibility(View.VISIBLE);
        save_button.setText("Next");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Global.hideKeyboard(Broadcast_Contact_Selction_Actvity.this);
                if(Activty_Back.equals("Preview")){
                    Intent intent=new Intent(getApplicationContext(), Broadcast_Preview.class);
                    intent.putExtra("Activty",Activty_Back);
                    startActivity(intent);
                    finish();
                }else {
                    if(SessionManager.getgroup_broadcste(this).size() != 0||
                            SessionManager.getGroupList(this).size()!=0){
                        if (SessionManager.getCampaign_type(getApplicationContext()).equals("SMS")) {
                            Intent new_task = new Intent(getApplicationContext(), Add_Broad_Text_Activity.class);
                            new_task.putExtra("flag", "add");
                            new_task.putExtra("Activity", "Contact");
                            startActivity(new_task);

                        } else {
                            Intent new_task = new Intent(getApplicationContext(), Add_Broad_Email_Activity.class);
                            new_task.putExtra("flag", "add");
                            new_task.putExtra("Activity", "Contact");
                            startActivity(new_task);

                        }
                    }else {
                        Global.Messageshow(getApplicationContext(),main_layout,getResources().getString(R.string.select_Contact_group),false);
                    }
                }
                //startActivity(new Intent(getApplicationContext(),Campaign_Name_Activity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent;
        if(Activty_Back.equals("Preview")){
            intent=new Intent(getApplicationContext(), Broadcast_Preview.class);
            intent.putExtra("Activty",Activty_Back);
            startActivity(intent);
        }
        finish();


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Broadcast_Contact_Selction_Actvity.this, main_layout);
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
                    Broadcast_Contect_Fragment contectFragment = new Broadcast_Contect_Fragment(Broadcast_Contact_Selction_Actvity.this);
                    return contectFragment;

                case 1:
                    Broadcast_Group_Fragment c_Fragment = new Broadcast_Group_Fragment();
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


}




