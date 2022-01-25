package com.contactninja.Campaign;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
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
import android.util.Log;
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
import com.contactninja.Campaign.Fragment.View_Contect_Fragment;
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

import retrofit2.Response;
@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables")
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
    int sequence_id,seq_task_id;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect_and_group_actvity);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        Intent getintent=getIntent();
        Bundle bundle=getintent.getExtras();
        sequence_id=bundle.getInt("sequence_id");
        seq_task_id=bundle.getInt("seq_task_id");

        if (SessionManager.getContect_flag(getApplicationContext()).equals("edit"))
        {
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            Fragment fragment = new Campaign_Contect_Fragment(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, fragment, "Fragment");
            fragmentTransaction.commitAllowingStateLoss();
            /*tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
//            tabLayout.addTab(tabLayout.newTab().setText("Groups"));
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
*/
        }
        else if (SessionManager.getContect_flag(getApplicationContext()).equals("read"))
        {
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            Fragment fragment = new Campaign_Contect_Fragment(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, fragment, "Fragment");
            fragmentTransaction.commitAllowingStateLoss();
        }
        else if (SessionManager.getContect_flag(getApplicationContext()).equals("check"))
        {
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            Fragment fragment = new Campaign_Contect_Fragment(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, fragment, "Fragment");
            fragmentTransaction.commitAllowingStateLoss();
        }

        else {

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
                try {
                    AddContectAndGroup(seq_task_id,sequence_id);
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






    public void AddContectAndGroup(int seq_task_id, int sequence_id) throws JSONException {
        loadingDialog.showLoadingDialog();

        if (sessionManager.getGroupList(this).equals(null))
        {
            Global.Messageshow(getApplicationContext(),main_layout,getString(R.string.camp_select_contect).toString(),false);
        }
        else {

            SignResponseModel user_data = SessionManager.getGetUserdata(this);
            String user_id = String.valueOf(user_data.getUser().getId());
            String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
            String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

            Log.e("Contect List is",new Gson().toJson(sessionManager.getGroupList(this)));
            Log.e("Group List is",new Gson().toJson(SessionManager.getgroup_broadcste(getApplicationContext())));

            if (SessionManager.getTask(getApplicationContext()).size()!=0)
            {
                this.sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
            }
            else {
                Intent getintent=getIntent();
                Bundle bundle=getintent.getExtras();
                this.sequence_id =bundle.getInt("sequence_id");
            }
            Log.e("sequence_id", String.valueOf(this.sequence_id));
            JSONObject obj = new JSONObject();
            JSONObject paramObject = new JSONObject();
            paramObject.put("organization_id", "1");
            paramObject.put("seq_task_id", seq_task_id);
            paramObject.put("seq_id", sequence_id);
            paramObject.put("team_id", "1");
            paramObject.put("user_id", user_id);
            List<ContectListData.Contact> contactdetails=sessionManager.getGroupList(this);
            JSONArray jsonArray = new JSONArray();
            Log.e("Contec List Size",String.valueOf(contactdetails.size()));
            for (int i = 0; i < contactdetails.size(); i++) {
                Log.e("Contec List Size",String.valueOf(contactdetails.get(0).getContactDetails().size()));
                JSONObject paramObject1 = new JSONObject();
                paramObject1.put("prospect_id",contactdetails.get(i).getId());
               Log.e("Contect Detail is",new Gson().toJson(contactdetails.get(i).getContactDetails()));
                for (int j=0;j<contactdetails.get(i).getContactDetails().size();j++)
                {
                    if (contactdetails.get(i).getContactDetails().get(j).getType().equals("NUMBER"))
                    {
                        paramObject1.put("mobile",contactdetails.get(i).getContactDetails().get(j).getEmailNumber());
                    }
                    else {
                        paramObject1.put("email",contactdetails.get(i).getContactDetails().get(j).getEmailNumber());
                    }
                    //break;
                }

                jsonArray.put(paramObject1);
            }

            List<Grouplist.Group> group_list=SessionManager.getgroup_broadcste(getApplicationContext());
            JSONArray contect_array = new JSONArray();
            for (int i =0;i<group_list.size();i++)
            {
                contect_array.put(group_list.get(i).getId());
            }
            paramObject.put("prospect_ids", jsonArray);
            paramObject.put("contact_group_ids",contect_array);
            obj.put("data", paramObject);
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
            Log.e("Gson Data",new Gson().toJson(gsonObject));
            retrofitCalls.Sequence_contact_store(sessionManager, gsonObject, loadingDialog,Global.getToken(sessionManager),
                    Global.getVersionname(ContectAndGroup_Actvity.this),Global.Device, new RetrofitCallback() {
                        @Override
                        public void success(Response<ApiResponse> response) {
                            loadingDialog.cancelLoading();

                            if (response.body().getHttp_status() == 200) {

                               if (SessionManager.getContect_flag(getApplicationContext()).equals("check"))
                               {
                                   Intent intent = new Intent(getApplicationContext(), Campaign_Final_Start.class);
                                   intent.putExtra("sequence_id", sequence_id);
                                   startActivity(intent);
                                   finish();
                               }
                               else if (SessionManager.getContect_flag(getApplicationContext()).equals("edit"))
                               {
                                   SessionManager.setCampign_flag("read");
                                   Intent intent = new Intent(getApplicationContext(), Campaign_Preview.class);
                                   intent.putExtra("sequence_id", sequence_id);
                                   startActivity(intent);
                                //   finish();

                               }
                              else {
                                   Intent intent=new Intent(getApplicationContext(),Campaign_Name_Activity.class);
                                   intent.putExtra("sequence_id",sequence_id);
                                   intent.putExtra("seq_task_id",seq_task_id);
                                   startActivity(intent);
                                   finish();
                               }

                            } else {
                                Global.Messageshow(getApplicationContext(),main_layout,response.body().getMessage(),false);

                            }
                        }

                        @Override
                        public void error(Response<ApiResponse> response) {
                            loadingDialog.cancelLoading();
                        }
                    });


        }

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
        return new cardListData(view);
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



