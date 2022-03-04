package com.contactninja.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.contactninja.Fragment.AddContect_Fragment.Company_Fragment;
import com.contactninja.Fragment.AddContect_Fragment.ContectFragment;
import com.contactninja.Fragment.AddContect_Fragment.GroupFragment;
import com.contactninja.Fragment.Home.Broadcast_Fragment;
import com.contactninja.Fragment.Home.Campaign_Fragment;
import com.contactninja.Fragment.Home.Task_Fragment;
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

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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


    // we're going to display pie chart for smartphones martket shares
    private float[] yData = {5, 10, 15, 30, 40};
    private String[] xData = {"Sony", "Huawei", "LG", "Apple", "Samsung"};
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

        TimeZone tz = TimeZone.getDefault();
        if (!Global.IsNotNull(user_data.getUser().getWorkingHoursList()) || user_data.getUser().getWorkingHoursList().size() == 0) {
            try {
                if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                    Timezone(tz.getID());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        intentView(view);
        tabLayout.addTab(tabLayout.newTab().setText("Task"));
        tabLayout.addTab(tabLayout.newTab().setText("Broadcast"));
        tabLayout.addTab(tabLayout.newTab().setText("Campaign"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        Fragment fragment = new Task_Fragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, "Fragment");
        fragmentTransaction.commitAllowingStateLoss();


        mBarChart = view.findViewById(R.id.cubiclinechart);
        mBarChart.setOutlineAmbientShadowColor(Color.GREEN);
        mBarChart.startAnimation();
        loadData();


        mBarChart1 = view.findViewById(R.id.rkt_pie_chart);
        final int[] MY_COLORS = {Color.rgb(192, 0, 0), Color.rgb(255, 0, 0), Color.rgb(255, 192, 0), Color.rgb(127, 127, 127), Color.rgb(146, 208, 80), Color.rgb(0, 176, 80), Color.rgb(79, 129, 189)};

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : MY_COLORS) colors.add(c);

        mBarChart1.startAnimation();
        mBarChart1.addPieSlice(new PieModel("Freetime", 20, Color.parseColor("#F07676")));
        mBarChart1.addPieSlice(new PieModel("Sleep", 80, Color.parseColor("#FAAD64")));
        mBarChart1.startAnimation();
        return view;
    }

    @Override
    public void onResume() {
        TabSet();
        super.onResume();
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
                for (int i = 0; i < timezonDataList.size(); i++) {
                    if (id.equals(timezonDataList.get(i).getTzname())) {
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
        String version_name = Global.getVersionname(mainActivity);
        retrofitCalls.Working_hour(sessionManager, obj, loadingDialog, token_api, version_name, Global.Device, new RetrofitCallback() {
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