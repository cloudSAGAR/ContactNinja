package com.contactninja.Campaign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.contactninja.Fragment.AddContect_Fragment.GroupFragment;
import com.contactninja.Fragment.ContectFragment;
import com.contactninja.Fragment.Contect_main_Fragment;
import com.contactninja.R;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.tabs.TabLayout;

public class First_Step_Activity extends AppCompatActivity implements View.OnClickListener {
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back;
    TextView save_button;
    TabLayout tabLayout;
    ViewPager viewPager;
    String strtext = "";
    ViewpaggerAdapter adapter;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_step);
        loadingDialog=new LoadingDialog(this);
        sessionManager=new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();
        tabLayout.addTab(tabLayout.newTab().setText("Email").setIcon(getDrawable(R.drawable.ic_email)));
        tabLayout.addTab(tabLayout.newTab().setText("SMS").setIcon(getDrawable(R.drawable.ic_message)));
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
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button=findViewById(R.id.save_button);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:
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
                    GroupFragment c_Fragment = new GroupFragment();
                    return c_Fragment;

                case 1:
                    GroupFragment c_Fragment1 = new GroupFragment();

                    return c_Fragment1;
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