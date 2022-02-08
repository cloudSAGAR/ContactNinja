package com.contactninja.Broadcast;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.contactninja.Broadcast.Broadcast_Frgment.Broadcast_Group_Fragment;
import com.contactninja.Broadcast.Broadcast_Frgment.Broadcste_Contect_Fragment;
import com.contactninja.Broadcast.Broadcast_Frgment.CardClick;
import com.contactninja.Broadcast.Broadcast_Schedule.Broadcast_to_repeat;
import com.contactninja.Model.Broadcast_Data;
import com.contactninja.Model.Broadcast_image_list;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")

public class Broadcst_Activty extends AppCompatActivity implements View.OnClickListener , CardClick,  ConnectivityReceiver.ConnectivityReceiverListener {
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
        setContentView(R.layout.activity_broadcst_activty);
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
                if(SessionManager.getContectList(Broadcst_Activty.this).size()!=0) {
                    final View mView = getLayoutInflater().inflate(R.layout.brodcaste_link_dialog_item, null);
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Broadcst_Activty.this, R.style.DialogStyle);
                    bottomSheetDialog.setContentView(mView);
                    TextView tv_text_link = bottomSheetDialog.findViewById(R.id.tv_text_link);
                    ImageView iv_send = bottomSheetDialog.findViewById(R.id.iv_send);
                    ImageView iv_card_list = bottomSheetDialog.findViewById(R.id.iv_card_list);
                    ImageView iv_link_icon = bottomSheetDialog.findViewById(R.id.iv_link_icon);
                    ImageView iv_cancle_select_image = bottomSheetDialog.findViewById(R.id.iv_cancle_select_image);
                    ImageView iv_selected = bottomSheetDialog.findViewById(R.id.iv_selected);
                    LinearLayout lay_link_copy = bottomSheetDialog.findViewById(R.id.lay_link_copy);
                    LinearLayout lay_main_choose_send = bottomSheetDialog.findViewById(R.id.lay_main_choose_send);
                    RecyclerView rv_image_card = bottomSheetDialog.findViewById(R.id.rv_image_card);
                    EditText edit_message = bottomSheetDialog.findViewById(R.id.edit_message);
                    CoordinatorLayout layout_select_image = bottomSheetDialog.findViewById(R.id.layout_select_image);
                    LinearLayout lay_sendnow = bottomSheetDialog.findViewById(R.id.lay_sendnow);
                    LinearLayout lay_schedule = bottomSheetDialog.findViewById(R.id.lay_schedule);

                    lay_sendnow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent1 = new Intent(getApplicationContext(), Brodcsast_Tankyou.class);
                            intent1.putExtra("s_name", "default");
                            startActivity(intent1);

                        }
                    });
                    lay_schedule.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(), Broadcast_to_repeat.class));
                        }
                    });
                    edit_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iv_card_list.setImageResource(R.drawable.ic_card_blank);
                            rv_image_card.setVisibility(View.GONE);
                            iv_card_list.setSelected(false);
                        }
                    });

                    iv_send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
                            Broadcast_Data broadcast_data = new Broadcast_Data();
                            broadcast_data.setLink(tv_text_link.getText().toString());
                            broadcast_data.setLink_text(edit_message.getText().toString());
                            broadcast_data.setBroadcast_image_lists(broadcast_image_list);
                            sessionManager.setAdd_Broadcast_Data(broadcast_data);
                            lay_main_choose_send.setVisibility(View.VISIBLE);

                        }
                    });


                    broadcast_image_list.clear();
                    for (int i = 0; i <= 20; i++) {
                        Broadcast_image_list item = new Broadcast_image_list();
                        if (i % 2 == 0) {
                            item.setId(i);
                            item.setScelect(false);
                            item.setImagename("card_1");
                        } else {
                            item.setId(i);
                            item.setScelect(false);
                            item.setImagename("card_2");
                        }
                        broadcast_image_list.add(item);
                    }
                    rv_image_card.setLayoutManager(new LinearLayoutManager(Broadcst_Activty.this,
                            LinearLayoutManager.HORIZONTAL, false));
                    rv_image_card.setHasFixedSize(true);
                    cardListAdepter = new CardListAdepter(Broadcst_Activty.this, broadcast_image_list, iv_selected, this, layout_select_image);
                    rv_image_card.setAdapter(cardListAdepter);


                    lay_link_copy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            iv_link_icon.setImageResource(R.drawable.ic_link_dark);
                            tv_text_link.setTextColor(getResources().getColor(R.color.purple_200));
                            tv_text_link.setText(getResources().getString(R.string.link_click));
                        }
                    });
                    iv_card_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v.isSelected()) {


                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);

                                iv_card_list.setImageResource(R.drawable.ic_card_blank);
                                rv_image_card.setVisibility(View.GONE);
                                iv_card_list.setSelected(false);
                            } else {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);

                                iv_card_list.setImageResource(R.drawable.ic_card_fill);
                                rv_image_card.setVisibility(View.VISIBLE);
                                iv_card_list.setSelected(true);
                            }
                        }
                    });


                    iv_cancle_select_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_select_image.setVisibility(View.GONE);
                            for (int i = 0; i < broadcast_image_list.size(); i++) {
                                if (broadcast_image_list.get(i).isScelect()) {
                                    broadcast_image_list.get(i).setScelect(false);
                                    break;
                                }
                            }
                        }
                    });

                    bottomSheetDialog.show();
                   }else {
                    Global.Messageshow(Broadcst_Activty.this,main_layout,getResources().getString(R.string.add_contact),false);
                }
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
                    Broadcste_Contect_Fragment contectFragment = new Broadcste_Contect_Fragment(Broadcst_Activty.this);
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
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Broadcst_Activty.this, main_layout);
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
    public cardListData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list, parent, false);
        return new cardListData(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull cardListData holder, int position) {
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
            holder.layout_select_image.setBackgroundResource(R.drawable.shape_10_blue);
        }else {
            holder.layout_select_image.setBackground(null);
        }
    }


    @Override
    public int getItemCount() {
        return broadcast_image_list.size();
    }

    public static class cardListData extends RecyclerView.ViewHolder {

        ImageView iv_card;
        LinearLayout layout_select_image;

        public cardListData(@NonNull View itemView) {
            super(itemView);
            iv_card = itemView.findViewById(R.id.iv_card);
            layout_select_image = itemView.findViewById(R.id.layout_select_image);
        }
    }


}