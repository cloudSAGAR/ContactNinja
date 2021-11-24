package com.intricare.test.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.intricare.test.MainActivity;
import com.intricare.test.R;
import com.intricare.test.Utils.OnButtonPressListener;


public class Contect_main_Fragment extends Fragment implements ViewPager.OnPageChangeListener {

    TabLayout tabLayout;
    ViewPager viewPager;
    EditText contect_search;
    String strtext="";
    ViewpaggerAdapter adapter;
    ImageView search_icon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_contect_main_, container, false);
        IntentUI(view);
        Log.e("Contect Main ","Call");

        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        tabLayout.addTab(tabLayout.newTab().setText("Groups"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
          adapter = new ViewpaggerAdapter(getActivity(),getActivity().getSupportFragmentManager(),
                tabLayout.getTabCount(),strtext);

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
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              contect_search.requestFocus();
            }
        });
        contect_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                strtext=s.toString().trim();
                onPageSelected(1);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void IntentUI(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        contect_search=view.findViewById(R.id.contect_search);
        search_icon=view.findViewById(R.id.search_icon);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Fragment fragment= adapter.getItem(position);
        if(fragment instanceof ContectFragment ){
            ((ContectFragment)fragment).update(strtext,getView(),getActivity());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class ViewpaggerAdapter extends FragmentPagerAdapter {

        Context context;
        int totalTabs;
        String strtext1;
        public ViewpaggerAdapter(Context c, FragmentManager fm, int totalTabs,String strtext1) {
            super(fm);
            context = c;
            this.totalTabs = totalTabs;
            this.strtext1=strtext1;
        }
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    ContectFragment contectFragment = new ContectFragment(strtext1);
                    return contectFragment;
                case 1:

                    ContectFragment c_Fragment = new ContectFragment(strtext1);

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