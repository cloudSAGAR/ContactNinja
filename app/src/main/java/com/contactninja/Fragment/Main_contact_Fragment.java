package com.contactninja.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.contactninja.Fragment.AddContect_Fragment.Company_Fragment;
import com.contactninja.Fragment.AddContect_Fragment.ContectFragment;
import com.contactninja.Fragment.AddContect_Fragment.GroupFragment;
import com.contactninja.R;
import com.google.android.material.tabs.TabLayout;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Main_contact_Fragment extends Fragment {

    TabLayout tabLayout;
   // ViewPager viewPager;
    EditText contect_search;
    String strtext = "";
   // ViewpaggerAdapter adapter;
    ImageView search_icon;
    LinearLayout layout_toolbar_logo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contect_main_, container, false);
        IntentUI(view);


        TabSet();


        return view;
    }

    private void TabSet() {
        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        tabLayout.addTab(tabLayout.newTab().setText("Groups"));
        tabLayout.addTab(tabLayout.newTab().setText("Company"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        Fragment fragment = new ContectFragment(getView(), getActivity());
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, "Fragment");
        fragmentTransaction.commitAllowingStateLoss();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new ContectFragment(getView(), getActivity());
                        break;
                    case 1:
                        fragment = new GroupFragment();
                        break;
                    case 2:
                        fragment = new Company_Fragment();
                        break;

                }
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment, "Fragment");
                    fragmentTransaction.commitAllowingStateLoss();
                }
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
        contect_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                strtext = s.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void IntentUI(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
     //   viewPager = view.findViewById(R.id.viewPager);
        contect_search = view.findViewById(R.id.contect_search);
        search_icon = view.findViewById(R.id.search_icon);
        layout_toolbar_logo = view.findViewById(R.id.layout_toolbar_logo);
        layout_toolbar_logo.setVisibility(View.VISIBLE);
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
                    ContectFragment contectFragment = new ContectFragment( getView(), getActivity());
                    return contectFragment;

                case 1:
                    GroupFragment c_Fragment = new GroupFragment();

                    return c_Fragment;
                case 2:
                    Company_Fragment company_Fragment = new Company_Fragment();

                    return company_Fragment;
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