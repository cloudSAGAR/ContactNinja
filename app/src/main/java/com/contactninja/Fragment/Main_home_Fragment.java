package com.contactninja.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.contactninja.Fragment.Home.Affiliate_Groth_Fragment;
import com.contactninja.Fragment.Home.Contact_Growth_Fragment;
import com.contactninja.Fragment.Home.Dashboard_Fragment;
import com.contactninja.MainActivity;
import com.contactninja.Model.Timezon;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Notification.NotificationListActivity;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Main_home_Fragment extends Fragment implements View.OnClickListener {


    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    ImageView  iv_toolbar_notification;
    LinearLayout layout_toolbar_logo;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewpaggerAdapter adapter;
    Integer user_id=0;
    String token_api = "",  organization_id = "", team_id = "";
    SignResponseModel user_data;
    MainActivity mainActivity;

    ValueLineChart mBarChart;
    public Main_home_Fragment(MainActivity mainActivity) {
        this.mainActivity=mainActivity;
    }
    private long mLastClickTime = 0;
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
        user_id =user_data.getUser().getId();
        organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        TimeZone tz = TimeZone.getDefault();
        Log.e("offset", tz.getID());
        if (!Global.IsNotNull(user_data.getUser().getWorkingHoursList())||user_data.getUser().getWorkingHoursList().size() == 0) {
            try {
                if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                    Timezone( tz.getID());
                }
            } catch (Exception e) {
            e.printStackTrace();
            }
        }

        intentView(view);
        tabLayout.addTab(tabLayout.newTab().setText("Dashboard"));
        tabLayout.addTab(tabLayout.newTab().setText("Affiliate Groth"));
        tabLayout.addTab(tabLayout.newTab().setText("Contact Growth"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new ViewpaggerAdapter(getActivity(), getChildFragmentManager(),
                tabLayout.getTabCount(), "");

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



         mBarChart = (ValueLineChart)view.findViewById(R.id.cubiclinechart);
         mBarChart.setOutlineAmbientShadowColor(Color.GREEN);
         mBarChart.startAnimation();
        loadData();
        PieView pieView = (PieView)view.findViewById(R.id.pie_view);
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
        pieView.setDate(pieHelperArrayList);
        pieView.selectedPie(2); //optional
        pieView.showPercentLabel(false); //optional

        return view;
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
        viewPager = view.findViewById(R.id.viewPager);
        iv_toolbar_notification.setOnClickListener(this);
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
                    Dashboard_Fragment dashboard_fragment = new Dashboard_Fragment();
                    return dashboard_fragment;
                case 1:
                    Affiliate_Groth_Fragment affiliate_groth_fragment = new Affiliate_Groth_Fragment();
                    return affiliate_groth_fragment;
                case 2:
                    Contact_Growth_Fragment contact_growth_fragment = new Contact_Growth_Fragment();
                    return contact_growth_fragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return totalTabs;
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


    private void Timezone(String id) throws JSONException {
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);
        retrofitCalls.Timezone(sessionManager, obj, loadingDialog, token_api, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {


                Gson gson = new Gson();
                String headerString = gson.toJson(response.body().getData());
                Type listType = new TypeToken<ArrayList<Timezon.TimezonData>>() {
                }.getType();
                List<Timezon.TimezonData> timezonDataList = new Gson().fromJson(headerString, listType);
                    for (int i=0;i<timezonDataList.size();i++){
                        if(id.equals(timezonDataList.get(i).getTzname())){
                            Working_hour(timezonDataList.get(i).getValue());
                            break;
                        }
                    }
            }

            @Override
            public void error(Response<ApiResponse> response) {
            }
        });
    }

    private void loadData() {


        ValueLineSeries series = new ValueLineSeries();
        series.setColor(Color.parseColor("#79D2DE"));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(3.2f));
        series.addPoint(new ValueLinePoint(2.6f));
        series.addPoint(new ValueLinePoint(5.0f));
        series.addPoint(new ValueLinePoint(3.5f));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(0.4f));
        series.addPoint(new ValueLinePoint(3.4f));
        series.addPoint(new ValueLinePoint(2.5f));
        series.addPoint(new ValueLinePoint(1.4f));
        series.addPoint(new ValueLinePoint(4.4f));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(3.2f));
        series.addPoint(new ValueLinePoint(2.6f));
        series.addPoint(new ValueLinePoint(5.0f));
        series.addPoint(new ValueLinePoint(3.5f));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(0.4f));
        series.addPoint(new ValueLinePoint(3.4f));
        series.addPoint(new ValueLinePoint(2.5f));
        series.addPoint(new ValueLinePoint(1.0f));
        series.addPoint(new ValueLinePoint(4.4f));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(3.2f));
        series.addPoint(new ValueLinePoint(2.6f));
        series.addPoint(new ValueLinePoint(5.0f));
        series.addPoint(new ValueLinePoint(3.5f));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(0.4f));
        series.addPoint(new ValueLinePoint(3.4f));
        series.addPoint(new ValueLinePoint(2.5f));
        series.addPoint(new ValueLinePoint(1.0f));
        series.addPoint(new ValueLinePoint(4.2f));
        series.addPoint(new ValueLinePoint(2.4f));
        series.addPoint(new ValueLinePoint(3.6f));
        series.addPoint(new ValueLinePoint(1.0f));
        series.addPoint(new ValueLinePoint(2.5f));
        series.addPoint(new ValueLinePoint(2.0f));
        series.addPoint(new ValueLinePoint(1.4f));
        mBarChart.addSeries(series);

    }
    private void Working_hour(Integer value) {
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("timezone_id", value);
        paramObject.addProperty("is_default", "1");
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);
        String version_name=Global.getVersionname(mainActivity);
        retrofitCalls.Working_hour(sessionManager, obj, loadingDialog, token_api,version_name , Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                //Log.e("Response is",new Gson().toJson(response));
            }

            @Override
            public void error(Response<ApiResponse> response) {
            }
        });

    }
}