package com.intricare.test.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.intricare.test.MainActivity;
import com.intricare.test.R;
import com.intricare.test.Utils.OnButtonPressListener;


public class Contect_main_Fragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    SearchView contect_search;
    OnButtonPressListener onButtonPressListener;
    String strtext="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view=inflater.inflate(R.layout.fragment_contect_main_, container, false);
        IntentUI(view);
        onButtonPressListener = (OnButtonPressListener) getActivity();
        strtext = ""+getArguments().getString("data");
        if (strtext.equals(""))
        {

            strtext="";
        }
        else {
            //Log.e("Data IS",strtext);
            strtext=""+getArguments().getString("data");

            //filter(strtext.trim());

        }

        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        tabLayout.addTab(tabLayout.newTab().setText("Groups"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
         ViewpaggerAdapter adapter = new ViewpaggerAdapter(getActivity(),getActivity().getSupportFragmentManager(),
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

        contect_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onButtonPressListener.onButtonPressed(query.trim());


              /*  Bundle bundle = new Bundle();
                bundle.putString("data", query);
                Fragment fragment = null;
                fragment = new ContectFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameContainer, fragment, MainActivity.CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
*/
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });





        return view;
    }

    private void IntentUI(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        contect_search=view.findViewById(R.id.contect_search);

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