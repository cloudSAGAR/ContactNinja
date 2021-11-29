package com.intricare.test.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.intricare.test.R;
import com.intricare.test.Utils.SessionManager;


public class AppIntroActivity extends AppCompatActivity implements View.OnClickListener {
    ViewPager viewPager;
    int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    TextView slider_Text,tv_skip;
    TabLayout tab_layout;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);
        sessionManager=new SessionManager(AppIntroActivity.this);

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