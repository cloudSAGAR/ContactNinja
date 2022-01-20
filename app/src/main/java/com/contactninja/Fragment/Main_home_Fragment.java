package com.contactninja.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.contactninja.Email.Email_List_Activity;
import com.contactninja.Fragment.Home.Affiliate_Groth_Fragment;
import com.contactninja.Fragment.Home.Contact_Growth_Fragment;
import com.contactninja.Fragment.Home.Dashboard_Fragment;
import com.contactninja.MainActivity;
import com.contactninja.Manual_email_and_sms.Email_Sms_List_Activty;
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

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Main_home_Fragment extends Fragment implements View.OnClickListener {


    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    ImageView iv_toolbar_mail, iv_toolbar_select,iv_toolbar_notification;
    LinearLayout layout_toolbar_logo;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewpaggerAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_home, container, false);

        retrofitCalls = new RetrofitCalls(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        sessionManager = new SessionManager(getActivity());
     /*   try {
            if(Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                Refreess_token();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        try {
            if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                TimeZooneUpdate();
            }
        } catch (JSONException e) {
            e.printStackTrace();
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



        return view;
    }

    private void intentView(View view) {
        iv_toolbar_notification = view.findViewById(R.id.iv_toolbar_notification);
        iv_toolbar_notification.setVisibility(View.VISIBLE);
        iv_toolbar_mail = view.findViewById(R.id.iv_toolbar_mail);
        iv_toolbar_mail.setVisibility(View.VISIBLE);
        iv_toolbar_mail.setOnClickListener(this);
        iv_toolbar_select = view.findViewById(R.id.iv_toolbar_select);
        iv_toolbar_select.setVisibility(View.VISIBLE);
        iv_toolbar_select.setOnClickListener(this);
        layout_toolbar_logo = view.findViewById(R.id.layout_toolbar_logo);
        layout_toolbar_logo.setVisibility(View.VISIBLE);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        iv_toolbar_notification.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_toolbar_notification:
                Intent intent=new Intent(getActivity(),NotificationListActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_toolbar_mail:
                startActivity(new Intent(getActivity(), Email_List_Activity.class));
                //getActivity().finish();
                break;
            case R.id.iv_toolbar_select:
                startActivity(new Intent(getActivity(), Email_Sms_List_Activty.class));

                break;

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
                    Contact_Growth_Fragment contact_growth_fragment= new Contact_Growth_Fragment();
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


    private void TimeZooneUpdate() throws JSONException {

        Calendar cal = Calendar.getInstance();
        TimeZone tz1 = cal.getTimeZone();
        Calendar calendar = Calendar.getInstance(tz1,
                Locale.getDefault());

        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        String offset = localTime.substring(0, 1);

//      //  Log.e("GMT offset is %s hours",""+ TimeUnit.MINUTES.convert(tz1.getRawOffset(), TimeUnit.MILLISECONDS));

        String time = offset + TimeUnit.MINUTES.convert(tz1.getRawOffset(), TimeUnit.MILLISECONDS);
        Log.e("offset", time);
        String token = Global.getToken(sessionManager);
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        //String workin_time=user_data.getUser().getWorkingHoursList();
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

//        Log.e("Size is",new Gson().toJson(user_data.getUser().getWorkingHoursList()));
        if (user_data.getUser().getWorkingHoursList().size() == 0) {
            JsonObject obj = new JsonObject();
            JsonObject paramObject = new JsonObject();
            paramObject.addProperty("diff_minutes", time);
            paramObject.addProperty("is_default", "1");
            paramObject.addProperty("organization_id", "1");
            paramObject.addProperty("team_id", "1");
            paramObject.addProperty("user_id", user_id);
            obj.add("data", paramObject);
            retrofitCalls.Working_hour(sessionManager, obj, loadingDialog, token, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
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
}