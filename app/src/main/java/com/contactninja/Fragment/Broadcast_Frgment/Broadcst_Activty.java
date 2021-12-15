package com.contactninja.Fragment.Broadcast_Frgment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.contactninja.Fragment.AddContect_Fragment.GroupFragment;
import com.contactninja.Fragment.ContectFragment;
import com.contactninja.R;
import com.google.android.material.tabs.TabLayout;

public class Broadcst_Activty extends AppCompatActivity implements View.OnClickListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    EditText contect_search;
    String strtext = "";
    ViewpaggerAdapter adapter;
    ImageView search_icon;
    ImageView iv_back,iv_more;
    TextView save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcst_activty);
        IntentUI();


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
        tabLayout = findViewById(R.id.tabLayout);
        viewPager =findViewById(R.id.viewPager);
        contect_search = findViewById(R.id.contect_search);
        search_icon = findViewById(R.id.search_icon);
        iv_back=findViewById(R.id.iv_back);
        save_button=findViewById(R.id.save_button);
        iv_more=findViewById(R.id.iv_more);
        iv_more.setVisibility(View.GONE);
        iv_back.setOnClickListener(this);
        save_button.setOnClickListener(this);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText("Next");
        save_button.setTextColor(getResources().getColor(R.color.purple_200));
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
                    Broadcste_Contect_Fragment contectFragment = new Broadcste_Contect_Fragment();
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