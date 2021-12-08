package com.contactninja.Group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.contactninja.Fragment.AddContect_Fragment.GroupFragment;
import com.contactninja.Fragment.ContectFragment;
import com.contactninja.Fragment.GroupFragment.ExposuresFragment;
import com.contactninja.Fragment.GroupFragment.MembersFragment;
import com.contactninja.R;
import com.google.android.material.tabs.TabLayout;

public class SendBroadcast extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    TextView save_button;
    ImageView iv_more, iv_back;
    EditText add_detail;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewpaggerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_broadcast);
        IntentUI();
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Edit");
        save_button.setVisibility(View.VISIBLE);

        tabLayout.addTab(tabLayout.newTab().setText("Members"));
        tabLayout.addTab(tabLayout.newTab().setText("Exposures"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new ViewpaggerAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());

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

        viewPager.addOnPageChangeListener(this);
    }
    private void IntentUI() {
        save_button = findViewById(R.id.save_button);
        iv_more = findViewById(R.id.iv_more);
        iv_back = findViewById(R.id.iv_back);
        add_detail=findViewById(R.id.add_detail);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:
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


    class ViewpaggerAdapter extends FragmentPagerAdapter {

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

}