package com.contactninja.Group;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.contactninja.Fragment.GroupFragment.ExposuresFragment;
import com.contactninja.Fragment.GroupFragment.MembersFragment;
import com.contactninja.Model.Grouplist;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class SendBroadcast extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener  , ConnectivityReceiver.ConnectivityReceiverListener {
    TextView save_button;
    ImageView iv_Setting, iv_back;
    EditText add_detail,add_new_contect;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewpaggerAdapter adapter;
    SessionManager sessionManager;
    RoundedImageView add_new_contect_icon;
    ConstraintLayout mMainLayout;
    TextView no_image;

    private BroadcastReceiver mNetworkReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_broadcast);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        Global.checkConnectivity(SendBroadcast.this, mMainLayout);
        sessionManager=new SessionManager(this);
        Grouplist.Group group_data = SessionManager.getGroupData(this);

        add_new_contect.setText(group_data.getGroupName());
        add_detail.setText(group_data.getDescription());

        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Edit");
        save_button.setVisibility(View.VISIBLE);
        iv_Setting.setVisibility(View.GONE);
        tabLayout.addTab(tabLayout.newTab().setText("Members"));
        tabLayout.addTab(tabLayout.newTab().setText("Exposures"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new ViewpaggerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
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

        if (group_data.getGroupImage()==null)
        {
            String name =group_data.getGroupName();
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
            no_image.setText(add_text);
            no_image.setVisibility(View.VISIBLE);
            add_new_contect_icon.setVisibility(View.GONE);
        }
        else {
            Glide.with(getApplicationContext()).
                    load(group_data.getGroupImage()).
                    placeholder(R.drawable.shape_primary_back).
                    error(R.drawable.shape_primary_back).into(add_new_contect_icon);
            no_image.setVisibility(View.GONE);
            add_new_contect_icon.setVisibility(View.VISIBLE);
        }




        viewPager.addOnPageChangeListener(this);
    }
    private void IntentUI() {
        save_button = findViewById(R.id.save_button);
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.VISIBLE);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        add_detail=findViewById(R.id.add_detail);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        add_new_contect_icon=findViewById(R.id.add_new_contect_icon);
        add_new_contect=findViewById(R.id.add_new_contect);
        mMainLayout=findViewById(R.id.mMainLayout);
        no_image=findViewById(R.id.no_image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:
                startActivity(new Intent(getApplicationContext(),Final_Group.class));
                finish();
                break;
            default:


        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }




    static class ViewpaggerAdapter extends FragmentPagerAdapter {

        Context context;
        int totalTabs;

        public ViewpaggerAdapter(Context c, FragmentManager fm, int totalTabs) {
            super(fm);
            context = c;
            this.totalTabs = totalTabs;

        }
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                   MembersFragment membersFragment = new MembersFragment();
                    return membersFragment;

                case 1:
                    ExposuresFragment exposuresFragment = new ExposuresFragment();
                    return exposuresFragment;
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
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(SendBroadcast.this, mMainLayout);
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