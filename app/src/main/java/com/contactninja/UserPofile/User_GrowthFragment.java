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
import java.util.HashMap;
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

    TextView  tv_lavel_count_1, tv_lavel_count_2, tv_lavel_count_3, tv_lavel_count_4, tv_lavel_count_5,
            tv_total_lavel,tv_rat_total, tv_rat_1, tv_rat_2, tv_rat_3, tv_rat_4, tv_rat_5;

    ImageView iv_all_up, iv_1_up, iv_2_up, iv_3_up, iv_4_up, iv_5_up;
    private int Weekofday=7;


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

        try {
            if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                Api_Dashboard();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }





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
    private void setdata(HashMap<String, String> map) {

        /**
         *
         * Set data AffiliateInfo
         * */
        if (Global.IsNotNull(des_affiliateInfo)) {
            int Total_Affiliate = 0;
            int Total_rat = 0;
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel1()) && des_affiliateInfo.getCountOfLevel1() != 0) {
               try {
                   tv_lavel_count_1.setText(String.valueOf(des_affiliateInfo.getCountOfLevel1()));
                   tv_rat_1.setText(String.valueOf(des_affiliateInfo.getRatiooflevel1())+"%");
                   Total_rat = des_affiliateInfo.getRatiooflevel1();
                   Total_Affiliate = des_affiliateInfo.getCountOfLevel1();
                   if (des_affiliateInfo.getRatiooflevel1() > 0) {
                       iv_1_up.setImageResource(R.drawable.ic_home_grow_up);
                   } else if (des_affiliateInfo.getRatiooflevel1() < 0) {
                       iv_1_up.setImageResource(R.drawable.ic_home_grow_down);
                   }
               }
               catch (Exception e)
               {

               }


            }
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel2()) && des_affiliateInfo.getCountOfLevel2() != 0) {
                tv_lavel_count_2.setText(String.valueOf(des_affiliateInfo.getCountOfLevel2()));
                tv_rat_2.setText(String.valueOf(des_affiliateInfo.getRatiooflevel2())+"%");
                Total_rat = Total_rat + des_affiliateInfo.getRatiooflevel2();
                Total_Affiliate = Total_Affiliate + des_affiliateInfo.getCountOfLevel2();
                if (des_affiliateInfo.getRatiooflevel2() > 0) {
                    iv_2_up.setImageResource(R.drawable.ic_home_grow_up);
                } else if (des_affiliateInfo.getRatiooflevel2() < 0) {
                    iv_2_up.setImageResource(R.drawable.ic_home_grow_down);
                }
            }
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel3()) && des_affiliateInfo.getCountOfLevel3() != 0) {
                tv_lavel_count_3.setText(String.valueOf(des_affiliateInfo.getCountOfLevel3()));
                tv_rat_3.setText(String.valueOf(des_affiliateInfo.getRatiooflevel3())+"%");
                Total_rat = Total_rat + des_affiliateInfo.getRatiooflevel3();
                Total_Affiliate = Total_Affiliate + des_affiliateInfo.getCountOfLevel3();
                if (des_affiliateInfo.getRatiooflevel3() > 0) {
                    iv_3_up.setImageResource(R.drawable.ic_home_grow_up);
                } else if (des_affiliateInfo.getRatiooflevel3() < 0) {
                    iv_3_up.setImageResource(R.drawable.ic_home_grow_down);
                }
            }
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel4()) && des_affiliateInfo.getCountOfLevel4() != 0) {
                tv_lavel_count_4.setText(String.valueOf(des_affiliateInfo.getCountOfLevel4()));
                tv_rat_4.setText(String.valueOf(des_affiliateInfo.getRatiooflevel4())+"%");
                Total_rat = Total_rat + des_affiliateInfo.getRatiooflevel4();
                Total_Affiliate = Total_Affiliate + des_affiliateInfo.getCountOfLevel4();
                if (des_affiliateInfo.getRatiooflevel4() > 0) {
                    iv_4_up.setImageResource(R.drawable.ic_home_grow_up);
                } else if (des_affiliateInfo.getRatiooflevel4() < 0) {
                    iv_4_up.setImageResource(R.drawable.ic_home_grow_down);
                }
            }
            if (Global.IsNotNull(des_affiliateInfo.getCountOfLevel5()) && des_affiliateInfo.getCountOfLevel5() != 0) {
                tv_lavel_count_5.setText(String.valueOf(des_affiliateInfo.getCountOfLevel5()));
                tv_rat_5.setText(String.valueOf(des_affiliateInfo.getRatiooflevel5())+"%");
                Total_rat = Total_rat + des_affiliateInfo.getRatiooflevel5();
                Total_Affiliate = Total_Affiliate + des_affiliateInfo.getCountOfLevel5();
                if (des_affiliateInfo.getRatiooflevel5() > 0) {
                    iv_5_up.setImageResource(R.drawable.ic_home_grow_up);
                } else if (des_affiliateInfo.getRatiooflevel5() < 0) {
                    iv_5_up.setImageResource(R.drawable.ic_home_grow_down);
                }
            }
            tv_total_lavel.setText(String.valueOf(Total_Affiliate));
            tv_rat_total.setText(String.valueOf(Total_rat)+"%");

            if (Total_rat > 0) {
                iv_all_up.setImageResource(R.drawable.ic_home_grow_up);
            } else if (Total_rat < 0) {
                iv_all_up.setImageResource(R.drawable.ic_home_grow_down);
            }

            button_Affiliate_Report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Global.IsNotNull(des_affiliateInfo)) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(getActivity(), Affiliate_Report_LavelActivity.class);
                        getActivity().startActivity(intent);
                    }


                }
            });
            mBarChart.setOutlineAmbientShadowColor(Color.GREEN);
            mBarChart.startAnimation();
            loadData(map);


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
        paramObject.put("date_time", Global.getCurrentTimeandDate());


        JSONArray array = new JSONArray();
        array.put("AFFILIATE");

        try {
            // Add the JSONArray to the JSONObject
            paramObject.put("data_type", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
     //   Log.e("Gson Data is", new Gson().toJson(gsonObject));
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


            }

            @Override
            public void error(Response<ApiResponse> response) {

            }
        });
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