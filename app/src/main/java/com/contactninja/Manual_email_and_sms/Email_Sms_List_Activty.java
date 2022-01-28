package com.contactninja.Manual_email_and_sms;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.contactninja.Manual_email_and_sms.Fragment.Email_List_Fragment;
import com.contactninja.Manual_email_and_sms.Fragment.Sms_List_Fragment;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.tabs.TabLayout;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Email_Sms_List_Activty extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener{
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back;
    TextView save_button;
    TabLayout tabLayout;
    TextView add_new_contect;

    ViewPager viewPager;
    LinearLayout mMainLayout;
    private BroadcastReceiver mNetworkReceiver;
    SampleFragmentPagerAdapter pagerAdapter;
    TabLayout.Tab tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sms_list_activty);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog=new LoadingDialog(this);
        sessionManager=new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();

        pagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#4A4A4A"));
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
      /*  viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int i, final float v, final int i2) {
            }
            @Override
            public void onPageSelected(final int i) {
                YourFragmentInterface fragment = (YourFragmentInterface) pagerAdapter.instantiateItem(viewPager, i);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
            }
            @Override
            public void onPageScrollStateChanged(final int i) {
            }
        });
    */
    }
    private void IntentUI() {
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button=findViewById(R.id.save_button);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        save_button.setVisibility(View.GONE);

        iv_back.setOnClickListener(this);
        save_button.setText("Next");
        add_new_contect=findViewById(R.id.add_new_contect);
        mMainLayout=findViewById(R.id.mMainLayout);



    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Email_Sms_List_Activty.this, mMainLayout);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.iv_back:
                finish();
                break;
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

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[] { "SMS", "Email" };
        private int[] imageResId = {R.drawable.ic_message_tab, R.drawable.ic_email };
        final int PAGE_COUNT = 2;

        public SampleFragmentPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab_campagin, null);
            TextView tv = v.findViewById(R.id.tabContent);
            tv.setText(tabTitles[position]);
            ImageView img =  v.findViewById(R.id.image_view);
            img.setImageResource(imageResId[position]);
            return v;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Sms_List_Fragment campaign_sms_fragment = new Sms_List_Fragment();

                    return campaign_sms_fragment;

                case 1:
                    Email_List_Fragment email_fragment = new Email_List_Fragment();
                    return email_fragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}