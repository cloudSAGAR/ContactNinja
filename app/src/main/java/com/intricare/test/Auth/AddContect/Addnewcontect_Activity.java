package com.intricare.test.Auth.AddContect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.intricare.test.Fragment.AddContect_Fragment.BzcardFragment;
import com.intricare.test.Fragment.AddContect_Fragment.ExposuresFragment;
import com.intricare.test.Fragment.AddContect_Fragment.InformationFragment;
import com.intricare.test.Fragment.ContectFragment;
import com.intricare.test.Fragment.Contect_main_Fragment;
import com.intricare.test.Fragment.UsetProgileFragment;
import com.intricare.test.R;

public class Addnewcontect_Activity extends AppCompatActivity {
    private static final String TAG_HOME = "Addcontect";
    public static String CURRENT_TAG = TAG_HOME;
    ImageView iv_back,iv_more,pulse_icon;
    TextView save_button,tv_title,tv_name;
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewcontect);
        IntentUI();

        //Set Viewpagger
        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Bzcard"));
        tabLayout.addTab(tabLayout.newTab().setText("Exposures"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ContectAdapter adapter = new ContectAdapter(this,getSupportFragmentManager(),
                tabLayout.getTabCount());
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
        pulse_icon.setColorFilter(getColor(R.color.purple_200));
        save_button.setText("Save Contact");
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (save_button.getText().equals("Save Contact"))
                {
                    save_button.setText(getString(R.string.edit_text));
                }
                else {
                    save_button.setText(getString(R.string.save_text));
                }
            }
        });

    }



    private void IntentUI() {
        iv_back=findViewById(R.id.iv_back);
        save_button=findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        iv_more=findViewById(R.id.iv_more);
        iv_more.setVisibility(View.GONE);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        pulse_icon=findViewById(R.id.pulse_icon);
        tv_title=findViewById(R.id.tv_title);
        tv_name=findViewById(R.id.tv_name);

    }


    //Set Adapter


    class ContectAdapter extends FragmentPagerAdapter {

        Context context;
        int totalTabs;
        String strtext1;
        public ContectAdapter(Context c, FragmentManager fm, int totalTabs) {
            super(fm);
            context = c;
            this.totalTabs = totalTabs;
            this.strtext1=strtext1;
        }
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    InformationFragment informationFragment = new InformationFragment();
                    return informationFragment;
                case 1:
                    BzcardFragment bzcardFragment = new BzcardFragment();
                    return bzcardFragment;
                case 2:
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