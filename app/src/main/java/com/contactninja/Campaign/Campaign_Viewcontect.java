package com.contactninja.Campaign;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Response;

import com.contactninja.Broadcast.Broadcast_Frgment.Broadcast_Group_Fragment;
import com.contactninja.Broadcast.Broadcast_Frgment.CardClick;
import com.contactninja.Broadcast.Broadcast_Schedule.Broadcast_to_repeat;
import com.contactninja.Broadcast.Brodcsast_Tankyou;
import com.contactninja.Campaign.Fragment.View_Contect_Fragment;
import com.contactninja.Fragment.AddContect_Fragment.InformationFragment;
import com.contactninja.Model.Broadcast_Data;
import com.contactninja.Model.Broadcast_image_list;
import com.contactninja.Model.CampaignTask_overview;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Campaign_Viewcontect extends AppCompatActivity implements View.OnClickListener , CardClick,  ConnectivityReceiver.ConnectivityReceiverListener  {
    TabLayout tabLayout;
    ViewPager viewPager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
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
    int sequence_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_viewcontect);
        mNetworkReceiver = new ConnectivityReceiver();
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();
        sessionManager=new SessionManager(this);
        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
       // tabLayout.addTab(tabLayout.newTab().setText("Groups"));
        Fragment fragment = new View_Contect_Fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment, "Fragment");
        fragmentTransaction.commitAllowingStateLoss();

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
        save_button.setVisibility(View.GONE);
        save_button.setText("Next");
        save_button.setTextColor(getResources().getColor(R.color.purple_200));
    }

    @Override
    protected void onResume() {
        StepData();
        super.onResume();
    }
    public void StepData() {
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


        if (SessionManager.getTask(getApplicationContext()).size() != 0) {
            sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
        } else {
            Intent getintent = getIntent();
            Bundle bundle = getintent.getExtras();
            sequence_id = bundle.getInt("sequence_id");
        }
        Log.e("sequence_id", String.valueOf(sequence_id));

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("id", sequence_id);
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);
        PackageManager pm = getApplicationContext().getPackageManager();
        String pkgName = getApplicationContext().getPackageName();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        retrofitCalls.Task_Data_Return(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(Campaign_Viewcontect.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {


                        if (response.body().getStatus() == 200) {

                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            Type listType = new TypeToken<CampaignTask_overview>() {
                            }.getType();

                            CampaignTask_overview user_model1 = new Gson().fromJson(headerString, listType);
                            //Log.e("User Model",new Gson().toJson(user_model1));
                            SessionManager.setCampaign_data(user_model1);
                            //  Log.e("Email Task",user_model1.getSequenceTask().get(0).getActiveTaskEmail().toString());
                            // Log.e("SMS",user_model1.getSequenceTask().get(0).getActiveTaskContactNumber().toString());

                            //  tv_email.setText(user_model1.getSequenceTask().get(0).getActiveTaskEmail().toString());
                            //tv_sms.setText(user_model1.getSequenceTask().get(0).getActiveTaskContactNumber().toString());
                            int sms_count=0;
                            int email_count=0;
                            for (int i=0;i<user_model1.getSequenceTask().size();i++)
                            {
                                if (user_model1.getSequenceTask().get(i).getType().equals("SMS"))
                                {
                                    sms_count=sms_count+1;
                                }
                                else {
                                    email_count=email_count+1;
                                }
                            }

                        } else {
                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            // Global.Messageshow(getApplicationContext(), mMainLayout, headerString, false);

                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {

                    }
                });
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();

                break;
            case R.id.save_button:
                final View mView = getLayoutInflater().inflate(R.layout.brodcaste_link_dialog_item, null);
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Campaign_Viewcontect.this, R.style.DialogStyle);
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
                CoordinatorLayout layout_select_image=bottomSheetDialog.findViewById(R.id.layout_select_image);
                LinearLayout lay_sendnow = bottomSheetDialog.findViewById(R.id.lay_sendnow);
                LinearLayout lay_schedule = bottomSheetDialog.findViewById(R.id.lay_schedule);
                lay_sendnow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1= new Intent(getApplicationContext(), Brodcsast_Tankyou.class);
                        intent1.putExtra("s_name","default");
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
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
                        Broadcast_Data broadcast_data=new Broadcast_Data();
                        broadcast_data.setLink(tv_text_link.getText().toString());
                        broadcast_data.setLink_text(edit_message.getText().toString());
                        broadcast_data.setBroadcast_image_lists(broadcast_image_list);
                        sessionManager.setAdd_Broadcast_Data(broadcast_data);
                        lay_main_choose_send.setVisibility(View.VISIBLE);

                    }
                });



                broadcast_image_list.clear();
                for (int i=0;i<=20;i++){
                    Broadcast_image_list item=new Broadcast_image_list();
                    if(i%2 == 0){
                        item.setId(i);
                        item.setScelect(false);
                        item.setImagename("card_1");
                    }else {
                        item.setId(i);
                        item.setScelect(false);
                        item.setImagename("card_2");
                    }
                    broadcast_image_list.add(item);
                }
                rv_image_card.setLayoutManager(new LinearLayoutManager(Campaign_Viewcontect.this,
                        LinearLayoutManager.HORIZONTAL, false));
                rv_image_card.setHasFixedSize(true);
                cardListAdepter = new CardListAdepter(Campaign_Viewcontect.this, broadcast_image_list,iv_selected,this,layout_select_image);
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


                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);

                            iv_card_list.setImageResource(R.drawable.ic_card_blank);
                            rv_image_card.setVisibility(View.GONE);
                            iv_card_list.setSelected(false);
                        }else {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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
                        for(int i=0;i<broadcast_image_list.size();i++){
                            if(broadcast_image_list.get(i).isScelect()){
                                broadcast_image_list.get(i).setScelect(false);
                                break;
                            }
                        }
                    }
                });

                bottomSheetDialog.show();


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

    static class ViewpaggerAdapter extends FragmentPagerAdapter {

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
                    View_Contect_Fragment contectFragment = new View_Contect_Fragment();
                    return contectFragment;

            /*    case 1:
                    Broadcast_Group_Fragment c_Fragment = new Broadcast_Group_Fragment();
                    return c_Fragment;*/
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
        Global.checkConnectivity(Campaign_Viewcontect.this, main_layout);
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