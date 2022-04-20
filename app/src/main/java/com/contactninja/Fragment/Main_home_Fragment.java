package com.contactninja.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Fragment.Home.Broadcast_Fragment;
import com.contactninja.Fragment.Home.Campaign_Fragment;
import com.contactninja.Fragment.Home.Task_Fragment;
import com.contactninja.MainActivity;
import com.contactninja.Model.Dashboard.Dashboard;
import com.contactninja.Model.Dashboard.Des_AffiliateInfo;
import com.contactninja.Model.Dashboard.Des_Bizcard;
import com.contactninja.Model.Dashboard.Des_TaskCounter;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Notification.NotificationListActivity;
import com.contactninja.R;
import com.contactninja.UserPofile.Affiliate_Report_LevelActivity;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.Global_Time;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.Utils.WrapContentViewPager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Main_home_Fragment extends Fragment implements View.OnClickListener {
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    ImageView iv_toolbar_notification;
    LinearLayout layout_toolbar_logo, layout_level;
    TabLayout tabLayout;
    Integer user_id = 0;
    String token_api = "", organization_id = "", team_id = "";
    SignResponseModel user_data;
    MainActivity mainActivity;
    ValueLineChart mBarChart;
    PieChart mBarChart1;
    RecyclerView rv_bzcardlist;
    BzcardlistAdepter bzcardlistAdepter;
    Dashboard dashboard = new Dashboard();
    Des_TaskCounter des_taskCounter = new Des_TaskCounter();
    Des_AffiliateInfo des_affiliateInfo = new Des_AffiliateInfo();
    List<Des_Bizcard> des_bizcardList = new ArrayList<>();
    TextView tv_campaign, tv_text, tv_email, tv_broadcast, tv_lavel_count_1, tv_lavel_count_2, tv_lavel_count_3, tv_lavel_count_4, tv_lavel_count_5, tv_total_lavel,
            btn_view_affilate_detail, tv_autometed_task, tv_manual_task, tv_rat_total, tv_rat_1, tv_rat_2, tv_rat_3, tv_rat_4, tv_rat_5, tv_total_reeard, tv_name_user;
    LinearLayout layout_Affiliate, layout_Bz_card, layout_connected_email;
    ImageView iv_all_up, iv_1_up, iv_2_up, iv_3_up, iv_4_up, iv_5_up;
    WrapContentViewPager viewPager;
    ViewpaggerAdapter adapter;
    NestedScrollView scrollView;
    private long mLastClickTime = 0;
    private int Weekofday = 7;
    public Main_home_Fragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    public Main_home_Fragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_home, container, false);

        retrofitCalls = new RetrofitCalls(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        sessionManager = new SessionManager(getActivity());
        token_api = Global.getToken(sessionManager);
        user_data = SessionManager.getGetUserdata(getActivity());
        user_id = user_data.getUser().getId();
        organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        intentView(view);


        try {
            if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                Api_Dashboard();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        tabLayout.addTab(tabLayout.newTab().setText("Tasks"));
        tabLayout.addTab(tabLayout.newTab().setText("Broadcast"));
        tabLayout.addTab(tabLayout.newTab().setText("Campaign"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabSet();

        return view;
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setdata(HashMap<String, String> map) {
        tv_name_user.setText(user_data.getUser().getFirstName());


        /**
         *
         * Set data TaskCounter
         * */
        if (Global.IsNotNull(des_taskCounter)) {
            tv_campaign.setText(String.valueOf(des_taskCounter.getSequence()));
            tv_text.setText(String.valueOf(des_taskCounter.getSms()));
            tv_email.setText(String.valueOf(des_taskCounter.getEmail()));
            tv_broadcast.setText(String.valueOf(des_taskCounter.getBroadcast()));

            mBarChart1.startAnimation();
            mBarChart1.addPieSlice(new PieModel("Automated", des_taskCounter.getAuto(), Color.parseColor("#F07676")));
            mBarChart1.addPieSlice(new PieModel("Manual", des_taskCounter.getManual(), Color.parseColor("#FAAD64")));
            mBarChart1.startAnimation();


            Integer total = des_taskCounter.getAuto() + des_taskCounter.getManual();
            Double A = ((double) des_taskCounter.getAuto() / total) * 100;
            Double m = ((double) des_taskCounter.getManual() / total) * 100;

            long AA = Math.round(A);
            long MM = Math.round(m);
            String Auto = Integer.toString((int) AA);
            String manual = Integer.toString((int) MM);

            tv_autometed_task.setText(String.valueOf(Auto + "%" + " Automated tasks"));
            tv_manual_task.setText(String.valueOf(manual + "%" + " Manual tasks"));


        }

        tv_total_reeard.setText(String.valueOf(Integer.parseInt(dashboard.getAFFILIATE_REWARDS().toString())));

        if (Global.IsNotNull(des_affiliateInfo)) {

            int Total_Affiliate = 0;
            double Total_rat = 0;

            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel1()) && des_affiliateInfo.getCountOfLevel1() != 0) {

                setLavel(tv_lavel_count_1, tv_rat_1, des_affiliateInfo.getCountOfLevel1(),
                        des_affiliateInfo.getRatiooflevel1(), iv_1_up);
                Total_rat = des_affiliateInfo.getRatiooflevel1();
                Total_Affiliate = des_affiliateInfo.getCountOfLevel1();


            }
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel2()) && des_affiliateInfo.getCountOfLevel2() != 0) {
                Total_rat = Total_rat + des_affiliateInfo.getRatiooflevel2();
                Total_Affiliate = Total_Affiliate + des_affiliateInfo.getCountOfLevel2();
                setLavel(tv_lavel_count_2, tv_rat_2, des_affiliateInfo.getCountOfLevel2(),
                        des_affiliateInfo.getRatiooflevel2(), iv_2_up);

            }
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel3()) && des_affiliateInfo.getCountOfLevel3() != 0) {

                Total_rat = Total_rat + des_affiliateInfo.getRatiooflevel3();
                Total_Affiliate = Total_Affiliate + des_affiliateInfo.getCountOfLevel3();

                setLavel(tv_lavel_count_3, tv_rat_3, des_affiliateInfo.getCountOfLevel3(),
                        des_affiliateInfo.getRatiooflevel3(), iv_3_up);

            }
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel4()) && des_affiliateInfo.getCountOfLevel4() != 0) {
                Total_rat = Total_rat + des_affiliateInfo.getRatiooflevel4();
                Total_Affiliate = Total_Affiliate + des_affiliateInfo.getCountOfLevel4();

                setLavel(tv_lavel_count_4, tv_rat_4, des_affiliateInfo.getCountOfLevel4(),
                        des_affiliateInfo.getRatiooflevel4(), iv_4_up);

            }
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel5()) && des_affiliateInfo.getCountOfLevel5() != 0) {
                setLavel(tv_lavel_count_5, tv_rat_5, des_affiliateInfo.getCountOfLevel5(),
                        des_affiliateInfo.getRatiooflevel5(), iv_5_up);


                Total_rat = Total_rat + des_affiliateInfo.getRatiooflevel5();
                Total_Affiliate = Total_Affiliate + des_affiliateInfo.getCountOfLevel5();

            }
            tv_total_lavel.setText(String.valueOf(Total_Affiliate));
            if (Total_Affiliate != 0) {
                layout_level.setVisibility(View.VISIBLE);
            } else {
                layout_level.setVisibility(View.GONE);
            }
            if (Total_rat > 0) {
                iv_all_up.setImageResource(R.drawable.ic_home_grow_up);
                tv_rat_total.setTextColor(getActivity().getResources().getColor(R.color.green_rate));
                tv_rat_total.setText("+" + String.valueOf(Total_rat) + "%");
            } else if (Total_rat < 0) {
                iv_all_up.setImageResource(R.drawable.ic_home_grow_down);
                tv_rat_total.setTextColor(getActivity().getResources().getColor(R.color.red));
                tv_rat_total.setText("-" + String.valueOf(Total_rat) + "%");
            }

            btn_view_affilate_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Global.IsNotNull(des_affiliateInfo)) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(getActivity(), Affiliate_Report_LevelActivity.class);
                        getActivity().startActivity(intent);
                    }


                }
            });
            try {
                mBarChart.setOutlineAmbientShadowColor(Color.GREEN);
                mBarChart.startAnimation();
                loadData(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (Global.IsNotNull(des_bizcardList) && des_bizcardList.size() != 0) {
            bzcardlistAdepter.clear();
            layout_Bz_card.setVisibility(View.VISIBLE);
            bzcardlistAdepter.add(des_bizcardList);
        } else {
            layout_Bz_card.setVisibility(View.GONE);
        }


    }

    private void setLavel(TextView text_lavelNumber, TextView text_lavelRate, Integer countOfLevel,
                          double Ratiooflevel, ImageView image_up_down) {

        text_lavelNumber.setText(String.valueOf(countOfLevel));

        if (Ratiooflevel > 0) {
            image_up_down.setImageResource(R.drawable.ic_home_grow_up);
            text_lavelRate.setTextColor(getActivity().getResources().getColor(R.color.green_rate));
            text_lavelRate.setText("+" + String.valueOf(Ratiooflevel) + "%");

        } else if (Ratiooflevel < 0 || Ratiooflevel == 0) {
            image_up_down.setImageResource(R.drawable.ic_home_grow_down);
            text_lavelRate.setTextColor(getActivity().getResources().getColor(R.color.red));
            text_lavelRate.setText("-" + String.valueOf(Ratiooflevel) + "%");
        }

    }

    @Override
    public void onResume() {
        //Log.e("Update Contect is",new Gson().toJson(SessionManager.getupdateContect(getActivity())));
        super.onResume();
    }

    private void Api_Dashboard() throws JSONException {
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_id);
        paramObject.put("date_time", Global_Time.getCurrentTimeandDate_24());

        JSONArray array = new JSONArray();

        // With put() you can add a value to the array.
        array.put("TASK_COUNTER");
        array.put("AFFILIATE");
        // array.put("TASK");
        array.put("BIZCARD");

// Create a new instance of a JSONObject

        try {
            // Add the JSONArray to the JSONObject
            paramObject.put("data_type", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        //Log.e("Gson Data is", new Gson().toJson(gsonObject));
        retrofitCalls.Dashboard(sessionManager, gsonObject, loadingDialog, token_api, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @SuppressLint("NewApi")
            @Override
            public void success(Response<ApiResponse> response) {

                try {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<Dashboard>() {
                    }.getType();
                    dashboard = new Gson().fromJson(headerString, listType);
                    des_taskCounter = dashboard.getTaskCounter();
                    des_affiliateInfo = dashboard.getAffiliate();
                    des_bizcardList = dashboard.getBizcard();

                    JSONObject jsonRootObject, json, graph = null;
                    HashMap<String, String> map = new HashMap<String, String>();//Creating HashMap
                    try {
                        jsonRootObject = new JSONObject(headerString);
                        json = jsonRootObject.getJSONObject("AFFILIATE");
                        graph = json.getJSONObject("graph");
                        for (int i = 0; i < graph.names().length(); i++) {
                            map.put(graph.names().getString(i), String.valueOf(graph.get(graph.names().getString(i))));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    setdata(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void error(Response<ApiResponse> response) {

            }
        });
    }

    private void TabSet() {

        adapter = new ViewpaggerAdapter(getActivity(), getChildFragmentManager(),
                tabLayout.getTabCount(), "");

        viewPager.setAdapter(adapter);
        viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(1);
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

    private void intentView(View view) {
        scrollView = view.findViewById(R.id.nest_scrollview);
        scrollView.setFillViewport(true);
        iv_toolbar_notification = view.findViewById(R.id.iv_toolbar_notification);
        iv_toolbar_notification.setVisibility(View.GONE);
        viewPager = view.findViewById(R.id.viewPager);

        layout_toolbar_logo = view.findViewById(R.id.layout_toolbar_logo);
        layout_toolbar_logo.setVisibility(View.VISIBLE);
        tabLayout = view.findViewById(R.id.tabLayout);
        iv_toolbar_notification.setOnClickListener(this);
        mBarChart = view.findViewById(R.id.cubiclinechart);
        mBarChart1 = view.findViewById(R.id.rkt_pie_chart);
        layout_level = view.findViewById(R.id.layout_level);


        tv_rat_total = view.findViewById(R.id.tv_rat_total);
        tv_rat_1 = view.findViewById(R.id.tv_rat_1);
        tv_rat_2 = view.findViewById(R.id.tv_rat_2);
        tv_rat_3 = view.findViewById(R.id.tv_rat_3);
        tv_rat_4 = view.findViewById(R.id.tv_rat_4);
        tv_rat_5 = view.findViewById(R.id.tv_rat_5);


        iv_all_up = view.findViewById(R.id.iv_all_up);
        iv_1_up = view.findViewById(R.id.iv_1_up);
        iv_2_up = view.findViewById(R.id.iv_2_up);
        iv_3_up = view.findViewById(R.id.iv_3_up);
        iv_4_up = view.findViewById(R.id.iv_4_up);
        iv_5_up = view.findViewById(R.id.iv_5_up);


        tv_campaign = view.findViewById(R.id.tv_campaign);
        tv_text = view.findViewById(R.id.tv_text);
        tv_email = view.findViewById(R.id.tv_email);
        tv_broadcast = view.findViewById(R.id.tv_broadcast);
        tv_autometed_task = view.findViewById(R.id.tv_autometed_task);
        tv_manual_task = view.findViewById(R.id.tv_manual_task);
        tv_total_reeard = view.findViewById(R.id.tv_total_reeard);
        tv_name_user = view.findViewById(R.id.tv_name_user);

        tv_lavel_count_5 = view.findViewById(R.id.tv_lavel_count_5);
        tv_lavel_count_4 = view.findViewById(R.id.tv_lavel_count_4);
        tv_lavel_count_3 = view.findViewById(R.id.tv_lavel_count_3);
        tv_lavel_count_2 = view.findViewById(R.id.tv_lavel_count_2);
        tv_lavel_count_1 = view.findViewById(R.id.tv_lavel_count_1);
        tv_total_lavel = view.findViewById(R.id.tv_total_lavel);
        btn_view_affilate_detail = view.findViewById(R.id.btn_view_affilate_detail);


        layout_Affiliate = view.findViewById(R.id.layout_Affiliate);
        layout_Bz_card = view.findViewById(R.id.layout_Bz_card);

        rv_bzcardlist = view.findViewById(R.id.rv_bzcardlist);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_bzcardlist.setLayoutManager(layoutManager);
        bzcardlistAdepter = new BzcardlistAdepter(getActivity(), new ArrayList<>());
        rv_bzcardlist.setAdapter(bzcardlistAdepter);
        rv_bzcardlist.setVisibility(View.VISIBLE);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_toolbar_notification:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent intent = new Intent(getActivity(), NotificationListActivity.class);
                startActivity(intent);
                break;


        }
    }

    private void loadData(HashMap<String, String> map) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#79D2DE"));

        mBarChart.setLayerPaint(paint);

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(Color.parseColor("#79D2DE"));
        for (HashMap.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (Global.IsNotNull(value)) {
                series.addPoint(new ValueLinePoint(Float.parseFloat(value)));
            } else {
                series.addPoint(new ValueLinePoint(0));
            }
            // ...
        }
        int a = map.size();
        int b = Weekofday - a;
        for (int i = 0; i < b; i++) {
            series.addPoint(new ValueLinePoint(0));
        }

        mBarChart.addSeries(series);

    }


    public static class BzcardlistAdepter extends RecyclerView.Adapter<BzcardlistAdepter.InviteListDataclass> {

        public Context mCtx;
        List<Des_Bizcard> des_bizcardList;

        public BzcardlistAdepter(Context context, List<Des_Bizcard> des_bizcardList) {
            this.mCtx = context;
            this.des_bizcardList = des_bizcardList;
        }

        @NonNull
        @Override
        public BzcardlistAdepter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_bzlist, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BzcardlistAdepter.InviteListDataclass holder, int position) {

            Des_Bizcard item = des_bizcardList.get(position);
            holder.tv_bzname.setText(String.valueOf(item.getCard_name()));
            holder.tv_count.setText(String.valueOf(item.getImpression()));


        }

        @Override
        public int getItemCount() {
            return des_bizcardList.size();
        }

        public void add(List<Des_Bizcard> des_tasks) {
            des_bizcardList = des_tasks;
            notifyDataSetChanged();
        }

        public void clear() {
            des_bizcardList.clear();
            notifyDataSetChanged();
        }


        public static class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_bzname, tv_count;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                tv_bzname = itemView.findViewById(R.id.tv_bzname);
                tv_count = itemView.findViewById(R.id.tv_count);


            }

        }

    }


    class ViewpaggerAdapter extends FragmentPagerAdapter {

        Context context;
        int totalTabs;
        String strtext1;

        public ViewpaggerAdapter(Context c, FragmentManager fm, int totalTabs, String strtext1) {
            super(fm);
            context = c;
            context = c;
            this.totalTabs = totalTabs;
            this.strtext1 = strtext1;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Task_Fragment fragment = new Task_Fragment();

                    return fragment;
                case 1:
                    Broadcast_Fragment c_Fragment = new Broadcast_Fragment();
                    return c_Fragment;
                case 2:
                    Campaign_Fragment campaign_fragment = new Campaign_Fragment();

                    return campaign_fragment;


                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Log.e("Tab Count", String.valueOf(totalTabs));
            return totalTabs;
        }
    }


}

