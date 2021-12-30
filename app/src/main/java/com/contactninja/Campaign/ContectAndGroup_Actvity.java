package com.contactninja.Campaign;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.contactninja.Campaign.Fragment.Campaign_Contect_Fragment;
import com.contactninja.Campaign.Fragment.Campaign_Group_Fragment;
import com.contactninja.Broadcast.Broadcast_Frgment.CardClick;
import com.contactninja.Model.Broadcast_image_list;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ContectAndGroup_Actvity extends AppCompatActivity implements View.OnClickListener ,CardClick,  ConnectivityReceiver.ConnectivityReceiverListener{
    TabLayout tabLayout;
    ViewPager viewPager;
    EditText contect_search;
    String strtext = "";
    ViewpaggerAdapter adapter;
    ImageView search_icon;
    ImageView iv_back, iv_Setting;
    TextView save_button;
    List<Broadcast_image_list> broadcast_image_list=new ArrayList<>();
    CardListAdepter cardListAdepter;
    private BroadcastReceiver mNetworkReceiver;

    LinearLayout main_layout;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect_and_group_actvity);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        sessionManager=new SessionManager(this);
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
        main_layout = findViewById(R.id.main_layout);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        contect_search = findViewById(R.id.contect_search);
        search_icon = findViewById(R.id.search_icon);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.GONE);
        iv_back.setOnClickListener(this);
        save_button.setOnClickListener(this);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText("Next");
        save_button.setTextColor(getResources().getColor(R.color.purple_200));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();

                break;
            case R.id.save_button:
                startActivity(new Intent(getApplicationContext(),Campaign_Name_Activity.class));
                break;
        }
    }

    @Override
    public void Onclick(Broadcast_image_list broadcastImageList) {
        for(int i=0;i<broadcast_image_list.size();i++){
            if(broadcastImageList.getId()==broadcast_image_list.get(i).getId()){
                broadcast_image_list.get(i).setScelect(true);
            }else {
                broadcast_image_list.get(i).setScelect(false);
            }
        }
        cardListAdepter.notifyDataSetChanged();
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
                    Campaign_Contect_Fragment contectFragment = new Campaign_Contect_Fragment(ContectAndGroup_Actvity.this);
                    return contectFragment;

                case 1:
                    Campaign_Group_Fragment c_Fragment = new Campaign_Group_Fragment();
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
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(ContectAndGroup_Actvity.this, main_layout);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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

}

class CardListAdepter extends RecyclerView.Adapter<CardListAdepter.cardListData> {

    Activity activity;
    List<Broadcast_image_list> broadcast_image_list;
    ImageView iv_selected;
    CardClick cardClick;
    CoordinatorLayout layout_select_image;


    public CardListAdepter(Activity activity, List<Broadcast_image_list> broadcast_image_list,
                           ImageView iv_selected,CardClick cardClick,CoordinatorLayout coordinatorLayout) {
        this.activity = activity;
        this.broadcast_image_list = broadcast_image_list;
        this.iv_selected = iv_selected;
        this.cardClick = cardClick;
        this.layout_select_image = coordinatorLayout;
    }


    @NonNull
    @Override
    public CardListAdepter.cardListData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
        return new CardListAdepter.cardListData(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull CardListAdepter.cardListData holder, int position) {
        Broadcast_image_list item = this.broadcast_image_list.get(position);


        int resID = activity.getResources().getIdentifier(item.getImagename()
                .replace(" ", "_").toLowerCase(), "drawable", activity.getPackageName());
        if (resID != 0) {
            Glide.with(activity.getApplicationContext()).load(resID).into(holder.iv_card);
        }
        holder.layout_select_image.setOnClickListener(v -> {
            cardClick.Onclick(item);
            item.setScelect(true);
            layout_select_image.setVisibility(View.VISIBLE);
            int resID1 = activity.getResources().getIdentifier(item.getImagename()
                    .replace(" ", "_").toLowerCase(), "drawable", activity.getPackageName());
            if (resID1 != 0) {
                Glide.with(activity.getApplicationContext()).load(resID1).into(iv_selected);
            }
        });
        if(item.isScelect()){
            holder.layout_select_image.setBackgroundResource(R.drawable.shape_blue_10);
        }else {
            holder.layout_select_image.setBackground(null);
        }
    }


    @Override
    public int getItemCount() {
        return broadcast_image_list.size();
    }

    public class cardListData extends RecyclerView.ViewHolder {

        ImageView iv_card;
        LinearLayout layout_select_image;

        public cardListData(@NonNull View itemView) {
            super(itemView);
            iv_card = itemView.findViewById(R.id.iv_card);
            layout_select_image = itemView.findViewById(R.id.layout_select_image);
        }
    }


}