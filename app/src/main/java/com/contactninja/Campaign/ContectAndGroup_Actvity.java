package com.contactninja.Campaign;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contactninja.Campaign.Fragment.Campaign_Contect_Fragment;
import com.contactninja.Campaign.Fragment.Campaign_Group_Fragment;
import com.contactninja.Campaign.List_itm.Campaign_Final_Start;
import com.contactninja.Model.Broadcast_image_list;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables")
public class ContectAndGroup_Actvity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    EditText contect_search;
    String strtext = "";
    ViewpaggerAdapter adapter;
    ImageView search_icon;
    ImageView iv_back, iv_Setting;
    TextView save_button;
    List<Broadcast_image_list> broadcast_image_list = new ArrayList<>();
    LinearLayout main_layout;
    SessionManager sessionManager;
    int sequence_id, seq_task_id;
    String sequence_Name = "";
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    private BroadcastReceiver mNetworkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect_and_group_actvity);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        Intent getintent = getIntent();
        Bundle bundle = getintent.getExtras();
        sequence_id = bundle.getInt("sequence_id");
        seq_task_id = bundle.getInt("seq_task_id");
        sequence_Name = bundle.getString("sequence_Name");

        if (SessionManager.getContect_flag(getApplicationContext()).equals("edit")) {
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            Fragment fragment = new Campaign_Contect_Fragment(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, fragment, "Fragment");
            fragmentTransaction.commitAllowingStateLoss();

        } else if (SessionManager.getContect_flag(getApplicationContext()).equals("read")) {
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            Fragment fragment = new Campaign_Contect_Fragment(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, fragment, "Fragment");
            fragmentTransaction.commitAllowingStateLoss();
        } else if (SessionManager.getContect_flag(getApplicationContext()).equals("check")) {
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            Fragment fragment = new Campaign_Contect_Fragment(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, fragment, "Fragment");
            fragmentTransaction.commitAllowingStateLoss();
        } else {

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

        }

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
                Global.hideKeyboard(ContectAndGroup_Actvity.this);
                try {
                    if (Global.isNetworkAvailable(ContectAndGroup_Actvity.this, main_layout)) {
                        AddContectAndGroup(seq_task_id, sequence_id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //startActivity(new Intent(getApplicationContext(),Campaign_Name_Activity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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

    public void AddContectAndGroup(int seq_task_id, int sequence_id) throws JSONException {

        SignResponseModel user_data = SessionManager.getGetUserdata(this);

        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("seq_task_id", seq_task_id);
        paramObject.put("seq_id", sequence_id);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        List<ContectListData.Contact> contactdetails = SessionManager.getGroupList(this);
        JSONArray jsonArray = new JSONArray();
        Log.e("Contec List Size", String.valueOf(contactdetails.size()));
        for (int i = 0; i < contactdetails.size(); i++) {
            Log.e("Contec List Size", String.valueOf(contactdetails.get(0).getContactDetails().size()));
            JSONObject paramObject1 = new JSONObject();
            Log.e("Contect Detail is", new Gson().toJson(contactdetails.get(i).getContactDetails()));
            for (int j = 0; j < contactdetails.get(i).getContactDetails().size(); j++) {
                if (contactdetails.get(i).getContactDetails().get(j).getType().equals("NUMBER")) {
                    paramObject1.put("mobile", contactdetails.get(i).getContactDetails().get(j).getEmailNumber());
                    paramObject1.put("prospect_id", contactdetails.get(i).getContactDetails().get(j).getContactId());

                } else {
                    paramObject1.put("email", contactdetails.get(i).getContactDetails().get(j).getEmailNumber());
                    paramObject1.put("prospect_id", contactdetails.get(i).getContactDetails().get(j).getContactId());

                }
                //break;
            }

            jsonArray.put(paramObject1);
        }

        List<Grouplist.Group> group_list = SessionManager.getgroup_broadcste(getApplicationContext());
        JSONArray contect_array = new JSONArray();
        for (int i = 0; i < group_list.size(); i++) {
            contect_array.put(group_list.get(i).getId());
        }
        paramObject.put("prospect_ids", jsonArray);
        if ((jsonArray != null && jsonArray.length() == 0) && (contect_array != null && contect_array.length() == 0)) {
            Global.Messageshow(getApplicationContext(), main_layout, getString(R.string.camp_select_contect), false);
        } else {
            loadingDialog.showLoadingDialog();

            Log.e("Contect List is", new Gson().toJson(SessionManager.getGroupList(this)));
            Log.e("Group List is", new Gson().toJson(SessionManager.getgroup_broadcste(getApplicationContext())));

            if (SessionManager.getTask(getApplicationContext()).size() != 0) {
                this.sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
            } else {
                Intent getintent = getIntent();
                Bundle bundle = getintent.getExtras();
                this.sequence_id = bundle.getInt("sequence_id");
            }
            Log.e("sequence_id", String.valueOf(this.sequence_id));
            JSONObject obj = new JSONObject();

            paramObject.put("contact_group_ids", contect_array);
            obj.put("data", paramObject);
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
            Log.e("Gson Data", new Gson().toJson(gsonObject));
            retrofitCalls.Sequence_contact_store(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager),
                    Global.getVersionname(ContectAndGroup_Actvity.this), Global.Device, new RetrofitCallback() {
                        @Override
                        public void success(Response<ApiResponse> response) {
                            loadingDialog.cancelLoading();

                            if (response.body().getHttp_status() == 200) {

                                if (SessionManager.getContect_flag(getApplicationContext()).equals("check")) {
                                    Intent intent = new Intent(getApplicationContext(), Campaign_Final_Start.class);
                                    intent.putExtra("sequence_id", sequence_id);
                                    startActivity(intent);
                                    finish();
                                } else if (SessionManager.getContect_flag(getApplicationContext()).equals("edit")) {
                                    SessionManager.setCampign_flag("read");
                                    Intent intent = new Intent(getApplicationContext(), Campaign_Preview.class);
                                    intent.putExtra("sequence_id", sequence_id);
                                    startActivity(intent);
                                    //   finish();

                                } else {
                                    Intent intent = new Intent(getApplicationContext(), Campaign_Name_Activity.class);
                                    intent.putExtra("sequence_id", sequence_id);
                                    intent.putExtra("seq_task_id", seq_task_id);
                                    intent.putExtra("sequence_Name", sequence_Name);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                Global.Messageshow(getApplicationContext(), main_layout, response.body().getMessage(), false);

                            }
                        }

                        @Override
                        public void error(Response<ApiResponse> response) {
                            loadingDialog.cancelLoading();
                        }
                    });
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


}




