package com.contactninja.Manual_email_text;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contactninja.MainActivity;
import com.contactninja.Main_Broadcast.Add_Broad_Email_Activity;
import com.contactninja.Main_Broadcast.Broadcast_Contact_Selction_Actvity;
import com.contactninja.Model.CampaignTask;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UserLinkedList;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.Utils.YourFragmentInterface;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class Text_And_Email_Auto_Manual extends AppCompatActivity  implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener{
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back;
    TextView save_button;
    TabLayout tabLayout;
    TextView add_new_contect;

    ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_message_tab,
            R.drawable.ic_email,
    };
    RelativeLayout mMainLayout;
    private BroadcastReceiver mNetworkReceiver;
    SampleFragmentPagerAdapter pagerAdapter;
    TabLayout.Tab tab;
    private long mLastClickTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_and_email_auto_manual);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog=new LoadingDialog(this);
        sessionManager=new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();

        pagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#4A4A4A"));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int i, final float v, final int i2) {
            }
            @Override
            public void onPageSelected(final int i) {
                YourFragmentInterface fragment = (YourFragmentInterface) pagerAdapter.instantiateItem(viewPager, i);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
            }
            @Override
            public void onPageScrollStateChanged(final int i) {
            }
        });
        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String flag=bundle.getString("flag");
        if (flag.equals("edit"))
        {
          //  add_new_contect.setText(getString(R.string.campaign_step_one)+"#" + bundle.getInt("step"));
            String type=bundle.getString("type");
            if (type.equals("SMS"))
            {
                viewPager.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        viewPager.setCurrentItem(0);
                    }
                }, 10);

            }
            else {
                viewPager.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        viewPager.setCurrentItem(1);
                    }
                }, 10);

            }




            LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
            for(int i = 0; i < tabStrip.getChildCount(); i++) {
                tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

                viewPager.setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        return true;
                    }
                });
            }
        }
        else {
            if (SessionManager.getTask(getApplicationContext()).size() == 0) {
                String step_id = String.valueOf(SessionManager.getTask(getApplicationContext()).size() + 1);
                String stpe_tyep = SessionManager.getCampaign_type_name(getApplicationContext());
                add_new_contect.setText(getString(R.string.create_task));
            } else {
                List<CampaignTask> step=   SessionManager.getTask(getApplicationContext());
                int step_id = step.get(0).getStepNo() + 1;
                String stpe_tyep = SessionManager.getCampaign_type_name(getApplicationContext());
                add_new_contect.setText(getString(R.string.create_task) );

            }
        }



    }

    private void IntentUI() {
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button=findViewById(R.id.save_button);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Next");
        add_new_contect=findViewById(R.id.add_new_contect);
        mMainLayout=findViewById(R.id.mMainLayout);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (sessionManager.getCampaign_type_name(getApplicationContext()).equals(""))
                {
                    Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.select_type),false);
                }
                else if (sessionManager.getCampaign_type(getApplicationContext()).equals("EMAIL")){
                    try {
                        if(Global.isNetworkAvailable(Text_And_Email_Auto_Manual.this, MainActivity.mMainLayout)) {
                            Mail_list();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    SessionManager.setGroupList(getApplicationContext(),new ArrayList<>() );
                    startActivity(new Intent(getApplicationContext(),Manual_Auto_Task_Name_Activity.class));
                    finish();
                    }
                //
                break;

        }


    }
    void Mail_list() throws JSONException {

        SignResponseModel signResponseModel= SessionManager.getGetUserdata(Text_And_Email_Auto_Manual.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("include_smtp",1);
        obj.add("data", paramObject);
        retrofitCalls.Mail_list(sessionManager,obj, loadingDialog, token,Global.getVersionname(Text_And_Email_Auto_Manual.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    SessionManager.setUserLinkedGmail(Text_And_Email_Auto_Manual.this,new ArrayList<>());
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UserLinkedList>() {
                    }.getType();
                    UserLinkedList userLinkedGmail=new Gson().fromJson(headerString, listType);

                    List<UserLinkedList.UserLinkedGmail>  List=userLinkedGmail.getUserLinkedGmail();
                    if (List.size() == 0) {
                        Global.Messageshow(Text_And_Email_Auto_Manual.this,mMainLayout,getResources().getString(R.string.setting_mail),false);
                    }else {
                        SessionManager.setUserLinkedGmail(Text_And_Email_Auto_Manual.this,List);

                        SessionManager.setGroupList(getApplicationContext(),new ArrayList<>() );
                        startActivity(new Intent(getApplicationContext(),Manual_Auto_Task_Name_Activity.class));
                        finish();

                    }
                }else {
                    Global.Messageshow(Text_And_Email_Auto_Manual.this,mMainLayout,getResources().getString(R.string.setting_mail),false);
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Text_And_Email_Auto_Manual.this, mMainLayout);
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

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = getResources().getStringArray(R.array.Select_Tab);
        private int[] imageResId = { R.drawable.ic_message_tab,R.drawable.ic_email };
        final int PAGE_COUNT = 2;

        public SampleFragmentPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab_campagin, null);
            TextView tv = v.findViewById(R.id.tabContent);
            tv.setText(tabTitles[position]);
            ImageView img =  v.findViewById(R.id.image_view);
            img.setImageResource(imageResId[position]);
            return v;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Manual_Auto_Selection_Fragment campaign_sms_fragment = new Manual_Auto_Selection_Fragment();
                    return campaign_sms_fragment;
                case 1:
                    Manual_Auto_Selection_Email_Fragment campaign_email_fragment = new Manual_Auto_Selection_Email_Fragment();
                    return campaign_email_fragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }


}