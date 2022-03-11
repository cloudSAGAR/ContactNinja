package com.contactninja.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.contactninja.Fragment.Home.Broadcast_Fragment;
import com.contactninja.Fragment.Home.Campaign_Fragment;
import com.contactninja.Fragment.Home.Task_Fragment;
import com.contactninja.MainActivity;
import com.contactninja.Model.Dashboard.Dashboard;
import com.contactninja.Model.Dashboard.Des_AffiliateInfo;
import com.contactninja.Model.Dashboard.Des_Task;
import com.contactninja.Model.Dashboard.Des_TaskCounter;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Notification.NotificationListActivity;
import com.contactninja.R;
import com.contactninja.UserPofile.Affiliate_Report_LavelActivity;
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
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Main_home_Fragment extends Fragment implements View.OnClickListener {
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    ImageView iv_toolbar_notification;
    LinearLayout layout_toolbar_logo;
    TabLayout tabLayout;
    Integer user_id = 0;
    String token_api = "", organization_id = "", team_id = "";
    SignResponseModel user_data;
    MainActivity mainActivity;
    ValueLineChart mBarChart;
    PieChart mBarChart1;
    private long mLastClickTime = 0;
    Dashboard dashboard = new Dashboard();
    Des_TaskCounter des_taskCounter = new Des_TaskCounter();
    Des_AffiliateInfo des_affiliateInfo = new Des_AffiliateInfo();

    TextView tv_campaign, tv_text, tv_email, tv_broadcast, tv_lavel_count_1, tv_lavel_count_2, tv_lavel_count_3, tv_lavel_count_4, tv_lavel_count_5, tv_total_lavel,
            btn_view_affilate_detail,tv_autometed_task,tv_manual_task;
    LinearLayout layout_Affiliate;

    public Main_home_Fragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
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


        tabLayout.addTab(tabLayout.newTab().setText("Task"));
        tabLayout.addTab(tabLayout.newTab().setText("Broadcast"));
        tabLayout.addTab(tabLayout.newTab().setText("Campaign"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setdata() {
        Fragment fragment = new Task_Fragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, "Fragment");
        fragmentTransaction.commitAllowingStateLoss();

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

            tv_autometed_task.setText(String.valueOf(des_taskCounter.getAuto()+" Automated tasks"));
            tv_manual_task.setText(String.valueOf(des_taskCounter.getManual()+" Manaul tasks"));
        }

        /**
         *
         * Set data AffiliateInfo
         * */
        if (Global.IsNotNull(des_affiliateInfo)) {
            int Total_Affiliate = 0;
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel1()) && des_affiliateInfo.getCountOfLevel1() != 0) {
                tv_lavel_count_1.setText(String.valueOf(des_affiliateInfo.getCountOfLevel1()));
                Total_Affiliate = des_affiliateInfo.getCountOfLevel1();
            }
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel2()) && des_affiliateInfo.getCountOfLevel2() != 0) {
                tv_lavel_count_2.setText(String.valueOf(des_affiliateInfo.getCountOfLevel2()));
                Total_Affiliate = Total_Affiliate + des_affiliateInfo.getCountOfLevel2();
            }
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel3()) && des_affiliateInfo.getCountOfLevel3() != 0) {
                tv_lavel_count_3.setText(String.valueOf(des_affiliateInfo.getCountOfLevel3()));
                Total_Affiliate = Total_Affiliate + des_affiliateInfo.getCountOfLevel3();
            }
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel4()) && des_affiliateInfo.getCountOfLevel4() != 0) {
                tv_lavel_count_4.setText(String.valueOf(des_affiliateInfo.getCountOfLevel4()));
                Total_Affiliate = Total_Affiliate + des_affiliateInfo.getCountOfLevel4();
            }
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel5()) && des_affiliateInfo.getCountOfLevel5() != 0) {
                tv_lavel_count_5.setText(String.valueOf(des_affiliateInfo.getCountOfLevel5()));
                Total_Affiliate = Total_Affiliate + des_affiliateInfo.getCountOfLevel5();
            }
            tv_total_lavel.setText(String.valueOf(Total_Affiliate));

            btn_view_affilate_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Global.IsNotNull(des_affiliateInfo)) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent=new Intent(getActivity(), Affiliate_Report_LavelActivity.class);
                        intent.putExtra("list",des_affiliateInfo);
                        getActivity().startActivity(intent);
                    }


                }
            });
            mBarChart.setOutlineAmbientShadowColor(Color.GREEN);
            mBarChart.startAnimation();
            loadData(des_affiliateInfo);



            if(des_affiliateInfo.getLevel1().size()!=0||
                    des_affiliateInfo.getLevel2().size()!=0||
                    des_affiliateInfo.getLevel3().size()!=0||
                    des_affiliateInfo.getLevel4().size()!=0||
                    des_affiliateInfo.getLevel5().size()!=0){
                btn_view_affilate_detail.setEnabled(true);
            }else {
                btn_view_affilate_detail.setEnabled(false);
            }


        }





     /*   final int[] MY_COLORS = {Color.rgb(192, 0, 0), Color.rgb(255, 0, 0), Color.rgb(255, 192, 0), Color.rgb(127, 127, 127), Color.rgb(146, 208, 80), Color.rgb(0, 176, 80), Color.rgb(79, 129, 189)};

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : MY_COLORS) colors.add(c);*/



    }

    @Override
    public void onResume() {
        TabSet();
        super.onResume();
    }

    private void Api_Dashboard() throws JSONException {
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_id);

        JSONArray array = new JSONArray();

        // With put() you can add a value to the array.
        array.put("TASK_COUNTER");
        array.put("AFFILIATE");
        array.put("TASK");

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
        Log.e("Gson Data is", new Gson().toJson(gsonObject));
        retrofitCalls.Dashboard(sessionManager, gsonObject, loadingDialog, token_api, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @SuppressLint("NewApi")
            @Override
            public void success(Response<ApiResponse> response) {


                Gson gson = new Gson();
                String headerString = gson.toJson(response.body().getData());
                Type listType = new TypeToken<Dashboard>() {
                }.getType();
                dashboard = new Gson().fromJson(headerString, listType);
                des_taskCounter = dashboard.getTaskCounter();
                des_affiliateInfo = dashboard.getAffiliate();





                setdata();

            }

            @Override
            public void error(Response<ApiResponse> response) {

            }
        });
    }

    private void TabSet() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new Task_Fragment();
                        break;
                    case 1:
                        fragment = new Broadcast_Fragment();
                        break;
                    case 2:
                        fragment = new Campaign_Fragment();
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

    }

    private void intentView(View view) {
        iv_toolbar_notification = view.findViewById(R.id.iv_toolbar_notification);
        iv_toolbar_notification.setVisibility(View.VISIBLE);

      /*  iv_toolbar_select = view.findViewById(R.id.iv_toolbar_select);
        iv_toolbar_select.setVisibility(View.VISIBLE);
        iv_toolbar_select.setOnClickListener(this);*/
        layout_toolbar_logo = view.findViewById(R.id.layout_toolbar_logo);
        layout_toolbar_logo.setVisibility(View.VISIBLE);
        tabLayout = view.findViewById(R.id.tabLayout);
        iv_toolbar_notification.setOnClickListener(this);
        mBarChart = view.findViewById(R.id.cubiclinechart);
        mBarChart1 = view.findViewById(R.id.rkt_pie_chart);


        tv_campaign = view.findViewById(R.id.tv_campaign);
        tv_text = view.findViewById(R.id.tv_text);
        tv_email = view.findViewById(R.id.tv_email);
        tv_broadcast = view.findViewById(R.id.tv_broadcast);
        tv_autometed_task = view.findViewById(R.id.tv_autometed_task);
        tv_manual_task = view.findViewById(R.id.tv_manual_task);

        tv_lavel_count_5 = view.findViewById(R.id.tv_lavel_count_5);
        tv_lavel_count_4 = view.findViewById(R.id.tv_lavel_count_4);
        tv_lavel_count_3 = view.findViewById(R.id.tv_lavel_count_3);
        tv_lavel_count_2 = view.findViewById(R.id.tv_lavel_count_2);
        tv_lavel_count_1 = view.findViewById(R.id.tv_lavel_count_1);
        tv_total_lavel = view.findViewById(R.id.tv_total_lavel);
        btn_view_affilate_detail = view.findViewById(R.id.btn_view_affilate_detail);


        layout_Affiliate = view.findViewById(R.id.layout_Affiliate);

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
           /* case R.id.iv_toolbar_select:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(Global.IsNotNull(SessionManager.getContectList(getActivity()))){
                    startActivity(new Intent(getActivity(), List_Manual_Activty.class));
                }
                break;*/

        }
    }


    private void Refreess_token() throws JSONException {


        String token = sessionManager.getAccess_token();
        String Refresh_token = sessionManager.getRefresh_token();
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("refresh_token", sessionManager.getRefresh_token());
        obj.add("data", paramObject);
        Log.e("Tokem is ", new Gson().toJson(obj));
        retrofitCalls.Refress_Token(sessionManager, obj, loadingDialog, token, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                ApiResponse apiResponse = response.body();
                try {
                    if (apiResponse.getHttp_status() == 200) {
                        Gson gson = new Gson();
                        String headerString = gson.toJson(response.body().getData());
                        Type listType = new TypeToken<SignResponseModel>() {
                        }.getType();
                        SignResponseModel data = new Gson().fromJson(headerString, listType);
                        sessionManager.setRefresh_token(data.getRefreshToken());
                        sessionManager.setAccess_token(data.getTokenType() + " " + data.getAccessToken());

                        Log.e("Access_token", data.getTokenType() + " " + data.getAccessToken());
                        Log.e("Refresh_token", data.getRefreshToken());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }


        });


    }


    private void loadData(Des_AffiliateInfo des_affiliateInfo) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#79D2DE"));

        mBarChart.setLayerPaint(paint);

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(Color.parseColor("#79D2DE"));
        if(Global.IsNotNull(des_affiliateInfo.getCountOfLevel1())){
            series.addPoint(new ValueLinePoint(des_affiliateInfo.getCountOfLevel1()));
        }else {
            series.addPoint(new ValueLinePoint(0));
        }
        if(Global.IsNotNull(des_affiliateInfo.getCountOfLevel2())){
            series.addPoint(new ValueLinePoint(0));
        }
        if(Global.IsNotNull(des_affiliateInfo.getCountOfLevel2())){
            series.addPoint(new ValueLinePoint(0));
        }
        if(Global.IsNotNull(des_affiliateInfo.getCountOfLevel3())){
            series.addPoint(new ValueLinePoint(0));
        }
        if(Global.IsNotNull(des_affiliateInfo.getCountOfLevel4())){
            series.addPoint(new ValueLinePoint(0));
        }


        mBarChart.addSeries(series);

    }


}