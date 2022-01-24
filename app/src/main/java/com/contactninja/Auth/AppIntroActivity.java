package com.contactninja.Auth;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;
import com.google.android.material.tabs.TabLayout;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class AppIntroActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    ViewPager viewPager;
    int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    TextView slider_Text,tv_skip;
    TabLayout tab_layout;
    SessionManager sessionManager;

    private BroadcastReceiver mNetworkReceiver;
    RelativeLayout mMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);
        sessionManager=new SessionManager(AppIntroActivity.this);
        mNetworkReceiver = new ConnectivityReceiver();

        initUI();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0 || position == 1) {
                    tv_skip.setText(getResources().getString(R.string.skip));
                 //   slider_Text.setText(getResources().getString(R.string.in_1));
                } else {
                    tv_skip.setText(getResources().getString(R.string.next));
                    //slider_Text.setText(getResources().getString(R.string.in_3));
                }
            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.
            }
        });
    }

    private void initUI() {
        layouts = new int[]{R.layout.welcome_slide_1, R.layout.welcome_slide_2, R.layout.welcome_slide_3};

        mMainLayout = findViewById(R.id.mMainLayout);
        slider_Text = findViewById(R.id.tv_dis);
        tv_skip = findViewById(R.id.tv_skip);
        viewPager = findViewById(R.id.viewPager);
        tab_layout = findViewById(R.id.tab_layout);
        myViewPagerAdapter = new MyViewPagerAdapter(slider_Text);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setOffscreenPageLimit(layouts.length);
        tab_layout.setupWithViewPager(viewPager);

        tv_skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_skip:
                sessionManager.appIntro();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(AppIntroActivity.this, mMainLayout);
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

    class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        TextView slider_text;

        public MyViewPagerAdapter(TextView slider_text1) {
            slider_text = slider_text1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);


            container.addView(view);

            return view;
        }


        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}