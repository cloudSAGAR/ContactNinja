package com.intricare.test.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.intricare.test.R;


public class AppIntroActivity extends AppCompatActivity {
    ViewPager viewPager;
    int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    TextView slider_Text;
    TabLayout tab_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);
        initUI();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    slider_Text.setText("ContactNinja is a “Multi-Function CRM Tool” providing contact management, outreach support, lead and follow-up tracking, and flexible communications management.");
                } else if (position == 1) {
                    slider_Text.setText("Use the most effective form of communication marketing to outperform your competition with contactninja Broadcasts");
                } else {
                    slider_Text.setText("Share your mission when you bring your business contact info to life on a virtual BZcard");
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
        viewPager = findViewById(R.id.viewPager);
        tab_layout = findViewById(R.id.tab_layout);
        myViewPagerAdapter = new MyViewPagerAdapter(slider_Text);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setOffscreenPageLimit(layouts.length);
        tab_layout.setupWithViewPager(viewPager);

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