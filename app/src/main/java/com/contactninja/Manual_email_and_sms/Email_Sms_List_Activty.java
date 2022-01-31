package com.contactninja.Manual_email_and_sms;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

import com.contactninja.MainActivity;
import com.contactninja.Manual_email_and_sms.Fragment.Email_List_Fragment;
import com.contactninja.Manual_email_and_sms.Fragment.Sms_List_Fragment;
import com.contactninja.Model.EmailActivityListModel;
import com.contactninja.Model.ManualTaskModel;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Email_Sms_List_Activty extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener,SwipeRefreshLayout.OnRefreshListener{
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back;
    TextView save_button;
    TabLayout tabLayout;
    TextView add_new_contect;

    ViewPager viewPager;
    LinearLayout mMainLayout;
    private BroadcastReceiver mNetworkReceiver;
    SampleFragmentPagerAdapter pagerAdapter;
    TabLayout.Tab tab;

    LinearLayout demo_layout, add_new_contect_layout,layout_search;
    TextView tv_create;
    RecyclerView rv_email_list;
    SwipeRefreshLayout swipeToRefresh;
    EditText ev_search;

    EmailAdepter emailAdepter;
    List<ManualTaskModel> manualTaskModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sms_list_activty);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog=new LoadingDialog(this);
        sessionManager=new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();

        pagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#4A4A4A"));
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
      /*  viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    */

        try {
            Mail_list();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ev_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<ManualTaskModel> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (ManualTaskModel item : manualTaskModelList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getContactMasterFirstname().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {

        } else {
           emailAdepter.filterList(filteredlist);
        }
    }


    private void IntentUI() {

        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button=findViewById(R.id.save_button);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        save_button.setVisibility(View.GONE);

        iv_back.setOnClickListener(this);
        save_button.setText("Next");
        add_new_contect=findViewById(R.id.add_new_contect);
        mMainLayout=findViewById(R.id.mMainLayout);


        layout_search=findViewById(R.id.layout_search);
        demo_layout = findViewById(R.id.demo_layout);
        mMainLayout = findViewById(R.id.mMainLayout);
        tv_create = findViewById(R.id.tv_create);
        tv_create.setText(getString(R.string.email_txt));
        rv_email_list = findViewById(R.id.email_list);
        add_new_contect_layout = findViewById(R.id.add_new_contect_layout);
        add_new_contect_layout.setOnClickListener(this);
        rv_email_list.setLayoutManager(new LinearLayoutManager(this));
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(this);
        ev_search=findViewById(R.id.ev_search);

    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Email_Sms_List_Activty.this, mMainLayout);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.iv_back:
                finish();
                break;
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
        private String tabTitles[] = new String[] { "SMS", "Email" };
        private int[] imageResId = {R.drawable.ic_message_tab, R.drawable.ic_email };
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
                    Sms_List_Fragment campaign_sms_fragment = new Sms_List_Fragment();

                    return campaign_sms_fragment;

                case 1:
                    Email_List_Fragment email_fragment = new Email_List_Fragment();
                    return email_fragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    void Mail_list() throws JSONException {

        if (!swipeToRefresh.isRefreshing()) {
            loadingDialog.showLoadingDialog();
        }
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Mail_Activiy_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                swipeToRefresh.setRefreshing(false);
                if (response.body().getHttp_status() == 200) {
                    manualTaskModelList.clear();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<EmailActivityListModel>() {
                    }.getType();
                    EmailActivityListModel emailActivityListModel = new Gson().fromJson(headerString, listType);

                    for (int i=0;i<emailActivityListModel.getManualTask().size();i++)
                    {
                        manualTaskModelList.add(emailActivityListModel.getManualTask().get(i));
                    }
                    //manualTaskModelList = emailActivityListModel.getManualTask();

                    if (manualTaskModelList.size()==0)
                    {
                        layout_search.setVisibility(View.GONE);
                        demo_layout.setVisibility(View.VISIBLE);


                    }
                    else {
                        layout_search.setVisibility(View.VISIBLE);
                        rv_email_list.setLayoutManager(new LinearLayoutManager(Email_Sms_List_Activty.this, LinearLayoutManager.VERTICAL, false));
                        emailAdepter = new EmailAdepter(Email_Sms_List_Activty.this, manualTaskModelList);
                        rv_email_list.setAdapter(emailAdepter);
                    }

                }
                else {
                    demo_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                swipeToRefresh.setRefreshing(false);
                loadingDialog.cancelLoading();
            }
        });
    }

    public void onRefresh() {
        try {
            if (Global.isNetworkAvailable(Email_Sms_List_Activty.this, MainActivity.mMainLayout)) {
                Mail_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class EmailAdepter extends RecyclerView.Adapter<EmailAdepter.viewData> {

        public Context mCtx;
        List<ManualTaskModel> manualTaskModelList;

        public void filterList(ArrayList<ManualTaskModel> filterllist) {
            // below line is to add our filtered
            // list in our course array list.
            manualTaskModelList = filterllist;
            // below line is to notify our adapter
            // as change in recycler view data.
            notifyDataSetChanged();
        }
        public EmailAdepter(Context context, List<ManualTaskModel> manualTaskModelList) {
            this.mCtx = context;
            this.manualTaskModelList = manualTaskModelList;
        }

        @NonNull
        @Override
        public EmailAdepter.viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_emailactivitylist, parent, false);
            return new EmailAdepter.viewData(view);
        }

        @SuppressLint("LogConditional")
        @Override
        public void onBindViewHolder(@NonNull EmailAdepter.viewData holder, int position) {
            ManualTaskModel item = manualTaskModelList.get(position);
            String conactname=item.getContactMasterFirstname()+" "+item.getContactMasterLastname();
            holder.tv_username.setText(conactname);
            holder.tv_task_description.setText(item.getContentBody());
            //   holder.tv_status.setText(item.getStatus());
            try {
                String time =Global.getDate(item.getStartTime());
                Log.e("Date is",time);

                String dt = covertTimeToText(time);

               // Log.e("Date is 1 ",dt);

                holder.tv_time.setText(dt);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String currentDateandTime = sdf.format(new Date());
                compareDates(currentDateandTime,time,holder.tv_status);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String name =conactname;
            String add_text="";
            String[] split_data=name.split(" ");
            try {
                for (int i=0;i<split_data.length;i++)
                {
                    if (i==0)
                    {
                        add_text=split_data[i].substring(0,1);
                    }
                    else {
                        add_text=add_text+split_data[i].substring(0,1);
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            holder.no_image.setText(add_text.toUpperCase());
            holder.no_image.setVisibility(View.VISIBLE);
            holder.profile_image.setVisibility(View.GONE);
            holder.layout_contec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (item.getType().toString().equals("SMS"))
                    {
                        SessionManager.setManualTaskModel(item);
                        Intent intent=new Intent(getApplicationContext(), Sms_Detail_Activty.class);
                        startActivity(intent);
                    }
                    else {

                        SessionManager.setManualTaskModel(item);
                        Intent intent=new Intent(getApplicationContext(), Email_Detail_activty.class);
                        startActivity(intent);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return manualTaskModelList.size();
        }


        public class viewData extends RecyclerView.ViewHolder {
            TextView tv_username, tv_task_description, tv_time,no_image,tv_status;
            CircleImageView profile_image;
            LinearLayout layout_contec;

            public viewData(@NonNull View itemView) {
                super(itemView);
                tv_username = itemView.findViewById(R.id.tv_username);
                tv_task_description = itemView.findViewById(R.id.tv_task_description);
                tv_time = itemView.findViewById(R.id.tv_time);
                no_image = itemView.findViewById(R.id.no_image);
                profile_image = itemView.findViewById(R.id.profile_image);
                tv_status=itemView.findViewById(R.id.tv_status);
                tv_status.setVisibility(View.VISIBLE);
                layout_contec=itemView.findViewById(R.id.layout_contec);
            }
        }
    }

    public String covertTimeToText(String dataDate) {
        String convTime = null;
        try {
            String prefix = "";
            String suffix = "Ago";


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date pasTime = dateFormat.parse(dataDate);
            Log.e("Time is ", String.valueOf(pasTime));

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second + " Sec " + suffix;
            } else if (minute < 60) {
                convTime = minute + " Min " + suffix;
            } else if (hour < 24) {
                convTime = hour + " Hours " + suffix;
            } else if (day >= 7) {
                if (day > 30) {
                    convTime = (day / 30) + " Months " + suffix;
                } else if (day > 360) {
                    convTime = (day / 360) + " Years " + suffix;
                } else {
                    convTime = (day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime = day + " Days " + suffix;
            }

        } catch (Exception e) {
            e.printStackTrace();
            //  Log.e("ConvTimeE", e.getMessage());
        }

        Log.e("convTime is",convTime);
        return convTime;
    }
    public static void compareDates(String d1, String d2, TextView tv_status)
    {
        try{
            // If you already have date objects then skip 1

            //1
            // Create 2 dates starts
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            Log.e("Date1",sdf.format(date1));
            Log.e("Date2",sdf.format(date2));

            // Create 2 dates ends
            //1

            // Date object is having 3 methods namely after,before and equals for comparing
            // after() will return true if and only if date1 is after date 2
            if(date1.after(date2)){
                tv_status.setText("Due");
                tv_status.setTextColor(Color.parseColor("#EC5454"));
                Log.e("","Date1 is after Date2");
            }
            // before() will return true if and only if date1 is before date2
            if(date1.before(date2)){

                tv_status.setText("Upcoming");
                tv_status.setTextColor(Color.parseColor("#2DA602"));
                Log.e("","Date1 is before Date2");
                System.out.println("Date1 is before Date2");
            }

            //equals() returns true if both the dates are equal
            if(date1.equals(date2)){
                tv_status.setText("Today");
                tv_status.setTextColor(Color.parseColor("#EC5454"));
                Log.e("","Date1 is equal Date2");
                System.out.println("Date1 is equal Date2");
            }

            System.out.println();
        }
        catch(ParseException ex){
            ex.printStackTrace();
        }
    }

}

