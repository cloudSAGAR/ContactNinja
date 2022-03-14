package com.contactninja.UserPofile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.contactninja.Fragment.Home.Task_Fragment;
import com.contactninja.MainActivity;
import com.contactninja.Model.Dashboard.Dashboard;
import com.contactninja.Model.Dashboard.Des_AffiliateInfo;
import com.contactninja.Model.Dashboard.Des_TaskCounter;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class User_GrowthFragment extends Fragment implements View.OnClickListener {
    TextView button_Affiliate_Report;
    ValueLineChart mBarChart;
    private long mLastClickTime = 0;
    Calendar calendar;
    CalendarView calendarView;
    ImageView prevMonth,nextMonth;
    TextView currentMonth;
    private Calendar _calendar;
    private int month, year,current_month,current_year;
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";
    private static final String ApidateTemplate = "yyyy-mm";
    String Selectdate="";


    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    Integer user_id = 0;
    String token_api = "", organization_id = "", team_id = "";
    SignResponseModel user_data;

    Dashboard dashboard = new Dashboard();
    Des_AffiliateInfo des_affiliateInfo = new Des_AffiliateInfo();

    TextView  tv_lavel_count_1, tv_lavel_count_2, tv_lavel_count_3, tv_lavel_count_4, tv_lavel_count_5, tv_total_lavel;



    public User_GrowthFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user_growth, container, false);
        retrofitCalls = new RetrofitCalls(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        sessionManager = new SessionManager(getActivity());
        token_api = Global.getToken(sessionManager);
        user_data = SessionManager.getGetUserdata(getActivity());
        user_id = user_data.getUser().getId();
        organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy");

        current_month= Integer.parseInt(dateFormat.format(date));
        current_year= Integer.parseInt(dateFormat1.format(date));

        Log.d("Month",dateFormat.format(date));
        IntentUI(view);
        setdata();




        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);

        setGridCellAdapterToDate(month, year);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 9);
        calendar.set(Calendar.YEAR, 2012);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.YEAR, 1);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String msg = "Selected date Day: " + i2 + " Month : " + (i1 + 1) + " Year " + i;
                //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @SuppressLint("NewApi")
    private void setdata() {


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

            button_Affiliate_Report.setOnClickListener(new View.OnClickListener() {
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
                button_Affiliate_Report.setEnabled(true);
            }else {
                button_Affiliate_Report.setEnabled(false);
            }


        }


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void IntentUI(View view) {
        mBarChart = view.findViewById(R.id.cubiclinechart);
        mBarChart.setOutlineAmbientShadowColor(Color.YELLOW);
        mBarChart.startAnimation();



        calendarView = view.findViewById(R.id.calendarView);
        prevMonth=view.findViewById(R.id.prevMonth);
        currentMonth=view.findViewById(R.id.currentMonth);
        nextMonth=view.findViewById(R.id.nextMonth);
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
        button_Affiliate_Report=view.findViewById(R.id.button_Affiliate_Report);
        button_Affiliate_Report.setOnClickListener(this);


        tv_lavel_count_5 = view.findViewById(R.id.tv_lavel_count_5);
        tv_lavel_count_4 = view.findViewById(R.id.tv_lavel_count_4);
        tv_lavel_count_3 = view.findViewById(R.id.tv_lavel_count_3);
        tv_lavel_count_2 = view.findViewById(R.id.tv_lavel_count_2);
        tv_lavel_count_1 = view.findViewById(R.id.tv_lavel_count_1);
        tv_total_lavel = view.findViewById(R.id.tv_total_lavel);

    }
    public class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";
            try {
                if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                    Api_Dashboard();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {

        }

    }

    private void Api_Dashboard() throws JSONException {
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_id);
        paramObject.put("month_year", Selectdate);


        JSONArray array = new JSONArray();


        array.put("TASK");

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

                SessionManager.setDes_Task(getActivity(),new ArrayList<>());

                Gson gson = new Gson();
                String headerString = gson.toJson(response.body().getData());
                Type listType = new TypeToken<Dashboard>() {
                }.getType();
                dashboard = new Gson().fromJson(headerString, listType);
                des_affiliateInfo = dashboard.getAffiliate();

                setdata();


            }

            @Override
            public void error(Response<ApiResponse> response) {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_Affiliate_Report:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(getActivity(),Affiliate_Report_LavelActivity.class));
                break;
            case R.id.prevMonth:
                prevMonth.setColorFilter(Color.parseColor("#5495EC"));
                nextMonth.setColorFilter(Color.parseColor("#AEA1A1"));

                if (current_month==month && current_year == year)
                {
                    nextMonth.setColorFilter(Color.parseColor("#AEA1A1"));
                }
                else {
                    nextMonth.setColorFilter(Color.parseColor("#5495EC"));

                }
                if (month <= 1)
                {
                    month = 12;
                    year--;
                }
                else
                {
                    month--;
                }
                setGridCellAdapterToDate(month, year);
                break;
            case R.id.nextMonth:

                if (current_month==month && current_year == year)
                {
                    nextMonth.setColorFilter(Color.parseColor("#AEA1A1"));
                }
                else {
                    nextMonth.setColorFilter(Color.parseColor("#5495EC"));
                    if (month > 11)
                    {
                        month = 1;
                        year++;
                    }
                    else
                    {
                        month++;
                    }
                    setGridCellAdapterToDate(month, year);
                }


                break;
        }
    }



    private void setGridCellAdapterToDate(int month, int year)
    {
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
        Selectdate= String.valueOf(dateFormatter.format(ApidateTemplate, _calendar.getTime()));
        MyAsyncTasks myAsyncTasks=new MyAsyncTasks();
        myAsyncTasks.execute();

    }
}