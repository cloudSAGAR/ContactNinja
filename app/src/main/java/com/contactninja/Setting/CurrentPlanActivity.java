package com.contactninja.Setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.contactninja.Model.Plandetail;
import com.contactninja.Model.Subscription;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class CurrentPlanActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    ImageView iv_back;
    int select_plan_type;
    ViewPager2 viewPager2;
    List<Plandetail> plandetailslist = new ArrayList<>();
    List<Plandetail> plandetailslist_new = new ArrayList<>();
    TextView tv_save;
    private BroadcastReceiver mNetworkReceiver;
    RelativeLayout mMainLayout;
    DotsIndicator dots_indicator;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    RetrofitCalls retrofitCalls;
    Integer user_id = 0;
    String token_api = "", organization_id = "", team_id = "";
    SignResponseModel user_data;
    String CurentPlan="";
    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_plan);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        retrofitCalls = new RetrofitCalls(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());

        token_api = Global.getToken(sessionManager);
        user_data = SessionManager.getGetUserdata(getApplicationContext());
        user_id = user_data.getUser().getId();
        organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        IntentUI();
        try {
            if (Global.isNetworkAvailable(CurrentPlanActivity.this,mMainLayout)) {
                Subscription();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_save.getText().toString().equals(getResources().getString(R.string.Upgrade_Plan)))
                {
                    Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.upgrade_plane),true);
                }
            }
        });

    }
    private void Subscription() {
        loadingDialog.showLoadingDialog();
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);
        retrofitCalls.Active_Subscription(sessionManager, obj, loadingDialog, token_api, Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                Gson gson = new Gson();
                String headerString = gson.toJson(response.body());
                Type listType = new TypeToken<Subscription>() {
                }.getType();
                Subscription subscription = new Gson().fromJson(headerString, listType);
                List<Subscription.Plan> plans=subscription.getData();
                if(subscription.getHttpStatus().equals(200)){
                    if(Global.IsNotNull(plans.get(0).getPurchasedPlanid())){
                        CurentPlan=plans.get(0).getPurchasedPlanid();
                    }
                    ListShow();
                }else {
                    Global.Messageshow(getApplicationContext(),mMainLayout,subscription.getMessage(),false);
                }

            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }
    private void ListShow() {
        // set adapter on viewpager
        if (plandetailslist.size() != 0) {
            plandetailslist.clear();
        }

        for (int k = 0; k <= 4; k++) {


            if (k == 2) {
                List<Plandetail.Plansublist> plansublists123 = new ArrayList<>();
                Plandetail plan = new Plandetail();
                plan.setPlan_product_id(getResources().getString(R.string.plan_39));
                plan.setPlan_name("Ninja Text Master");
                plan.setPlan_description("Master text marketing with broadcasts and lead tracking to multiply the chances of your messages to be opened and converted.\n");
                plan.setPlan_free("$39.95/Monthly");
                for (int i = 0; i <= 5; i++) {
                    if (i == 0) {
                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("1");
                        plansublist.setCheck_text("Contact Aggregation");
                        plansublists123.add(i, plansublist);
                    } else if (i == 1) {
                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("2");
                        plansublist.setCheck_text("5 BZcard  ");
                        plansublists123.add(i, plansublist);

                    } else if (i == 2) {

                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("3");
                        plansublist.setCheck_text("Broadcasting");
                        plansublists123.add(i, plansublist);
                    } else if (i == 3) {

                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("3");
                        plansublist.setCheck_text("Lead Tracking");
                        plansublists123.add(i, plansublist);

                    } else if (i == 4) {
                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                        plansublist.setCheck_flag("false");
                        plansublist.setCheck_id("4");
                        plansublist.setCheck_text("Automated Campaigns");
                        plansublists123.add(i, plansublist);

                    } else if (i == 5) {

                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                        plansublist.setCheck_flag("false");
                        plansublist.setCheck_id("5");
                        plansublist.setCheck_text("Calendar Integration");
                        plansublists123.add(i, plansublist);
                    }
                    plan.setPlansublist(plansublists123);

                }
                plandetailslist.add(plan);

            } else if (k == 3) {
                List<Plandetail.Plansublist> plansublists123 = new ArrayList<>();
                Plandetail plan = new Plandetail();
                plan.setPlan_product_id(getResources().getString(R.string.plan_69));
                plan.setPlan_name("ContactNinja");
                plan.setPlan_description("Join the thousands of users who use \n" +
                        "Contact Ninja to manage their connections and communicate quickly and easily with their network.\n");
                plan.setPlan_free("$69.95/Monthly");
                for (int i = 0; i <= 5; i++) {
                    if (i == 0) {
                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("1");
                        plansublist.setCheck_text("Contact Aggregation");


                        plansublists123.add(i, plansublist);
                    } else if (i == 1) {
                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("2");
                        plansublist.setCheck_text("5 BZcard  ");
                        plansublists123.add(i, plansublist);


                    } else if (i == 2) {

                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("3");
                        plansublist.setCheck_text("Broadcasting");
                        plansublists123.add(i, plansublist);
                    } else if (i == 3) {

                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("3");
                        plansublist.setCheck_text("Lead Tracking");
                        plansublists123.add(i, plansublist);

                    } else if (i == 4) {
                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("4");
                        plansublist.setCheck_text("Automated Campaigns");
                        plansublists123.add(i, plansublist);

                    } else if (i == 5) {

                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("5");
                        plansublist.setCheck_text("Calendar Integration");
                        plansublists123.add(i, plansublist);
                    }
                    plan.setPlansublist(plansublists123);

                }
                plandetailslist.add(plan);

            } else if (k == 1) {
                List<Plandetail.Plansublist> plansublists123 = new ArrayList<>();
                Plandetail plan = new Plandetail();
                plan.setPlan_product_id(getResources().getString(R.string.plan_9));
                plan.setPlan_name("Ninja BZcard");
                plan.setPlan_description("Master text marketing with broadcasts and lead tracking to multiply the chances of your messages to be opened and converted.\n");
                plan.setPlan_free("$9.95/Monthly");
                for (int i = 0; i <= 5; i++) {
                    if (i == 0) {
                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("1");
                        plansublist.setCheck_text("Contact Aggregation");
                        plansublists123.add(i, plansublist);

                    } else if (i == 1) {
                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("2");
                        plansublist.setCheck_text("3 BZcard  ");
                        plansublists123.add(i, plansublist);

                    } else if (i == 2) {

                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                        plansublist.setCheck_flag("false");
                        plansublist.setCheck_id("3");
                        plansublist.setCheck_text("Broadcasting");
                        plansublists123.add(i, plansublist);
                    } else if (i == 3) {
                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                        plansublist.setCheck_flag("false");
                        plansublist.setCheck_id("3");
                        plansublist.setCheck_text("Lead Tracking");
                        plansublists123.add(i, plansublist);

                    } else if (i == 4) {
                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                        plansublist.setCheck_flag("false");
                        plansublist.setCheck_id("4");
                        plansublist.setCheck_text("Automated Campaigns");
                        plansublists123.add(i, plansublist);

                    } else if (i == 5) {

                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                        plansublist.setCheck_flag("false");
                        plansublist.setCheck_id("5");
                        plansublist.setCheck_text("Calendar Integration");
                        plansublists123.add(i, plansublist);
                    }
                    plan.setPlansublist(plansublists123);


                }
                plandetailslist.add(plan);
            } else if (k == 0) {
                List<Plandetail.Plansublist> plansublists123 = new ArrayList<>();

                Plandetail plan = new Plandetail();

                plan.setPlan_name("Ninja BZcard");
                plan.setPlan_description("Master text marketing with broadcasts and lead tracking to multiply the chances of your messages to be opened and converted.\n");
                plan.setPlan_free("Free");
                for (int i = 0; i <= 5; i++) {
                    if (i == 0) {
                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("1");
                        plansublist.setCheck_text("Contact Aggregation");


                        plansublists123.add(i, plansublist);
                    } else if (i == 1) {
                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                        plansublist.setCheck_flag("true");
                        plansublist.setCheck_id("2");
                        plansublist.setCheck_text("1 BZcard  ");
                        plansublists123.add(i, plansublist);


                    } else if (i == 2) {

                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                        plansublist.setCheck_flag("false");
                        plansublist.setCheck_id("3");
                        plansublist.setCheck_text("Broadcasting");
                        plansublists123.add(i, plansublist);
                    } else if (i == 3) {

                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                        plansublist.setCheck_flag("false");
                        plansublist.setCheck_id("3");
                        plansublist.setCheck_text("Lead Tracking");
                        plansublists123.add(i, plansublist);

                    } else if (i == 4) {
                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                        plansublist.setCheck_flag("false");
                        plansublist.setCheck_id("4");
                        plansublist.setCheck_text("Automated Campaigns");
                        plansublists123.add(i, plansublist);

                    } else if (i == 5) {

                        Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                        plansublist.setCheck_flag("false");
                        plansublist.setCheck_id("5");
                        plansublist.setCheck_text("Calendar Integration");
                        plansublists123.add(i, plansublist);
                    }
                    plan.setPlansublist(plansublists123);

                }
                try {
                    plandetailslist.add(plan);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

        }



        for(int i=0;i<plandetailslist.size();i++){

           // Log.e("CurentPlan",CurentPlan);
          //  Log.e("Planid",plandetailslist.get(i).getPlan_product_id());
            if(CurentPlan.equals(plandetailslist.get(i).getPlan_product_id())){
                tv_save.setText(getResources().getString(R.string.Current_Plan));

                select_plan_type= i;
            }
            else if (CurentPlan.equals("0"))
            {
                select_plan_type= plandetailslist.size()-1;
                tv_save.setText(getResources().getString(R.string.Current_Plan));

            }
           
        }


        //Log.e("plan Type is",new Gson().toJson(plandetailslist_new));

        viewPager2.setAdapter(new ViewPageAdepter(getApplicationContext(), plandetailslist));


        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        for(int i=0;i<plandetailslist.size();i++){
            if(CurentPlan.equals(plandetailslist.get(i).getPlan_product_id())){
                viewPager2.setCurrentItem(i);
               // tv_save.setText(getResources().getString(R.string.Current_Plan));

            }
            else if (CurentPlan.equals("0"))
            {
                viewPager2.setCurrentItem(plandetailslist.size());
             //   tv_save.setText(getResources().getString(R.string.Current_Plan));

            }
            else {
                // plandetailslist_new.add(plandetailslist.size(),plandetailslist.get(i));
            }
        }
        dots_indicator.setViewPager2(viewPager2);
        CompositePageTransformer compositePagerTransformer = new CompositePageTransformer();
        compositePagerTransformer.addTransformer(new MarginPageTransformer(40));
        compositePagerTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);

              //  Log.e("Item is ", String.valueOf(viewPager2.getCurrentItem()));
                if(viewPager2.getCurrentItem()==select_plan_type){
                        tv_save.setText(getResources().getString(R.string.Current_Plan));
                }else {
                    tv_save.setText(getResources().getString(R.string.Upgrade_Plan));
                }
            }
        });
        viewPager2.setPageTransformer(compositePagerTransformer);
    }

    private void IntentUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        tv_save = findViewById(R.id.tv_save);
        viewPager2 = findViewById(R.id.viewpager);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        dots_indicator = findViewById(R.id.dots_indicator);
        iv_back.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@SuppressLint("UnknownNullness") View v) {
        if (v.getId() == R.id.iv_back) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(CurrentPlanActivity.this, mMainLayout);
    }

    @SuppressLint("ObsoleteSdkInt")
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

    public static class ViewPageAdepter extends RecyclerView.Adapter<ViewPageAdepter.viewholder> {

        public Context mCtx;
        List<Plandetail> plandetailslist;
        Plandetail plandetail;

        public ViewPageAdepter(Context applicationContext, List<Plandetail> plandetailslist) {
            this.mCtx = applicationContext;
            this.plandetailslist = plandetailslist;
        }

        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.plan_item_select, parent, false);
            return new viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull viewholder holder, int position) {
            plandetail = plandetailslist.get(position);
            holder.tv_title.setText(plandetail.getPlan_name());
            holder.tv_prise.setText(plandetail.getPlan_free());
            holder.tv_text.setText(plandetail.getPlan_description());

            holder.plan_condition.setLayoutManager(new LinearLayoutManager(mCtx));
            PlanDataAdapter planDataAdapter = new PlanDataAdapter(mCtx, plandetail.getPlansublist());
            holder.plan_condition.setAdapter(planDataAdapter);


        }

        @Override
        public int getItemCount() {
            return plandetailslist.size();
        }

        public static class viewholder extends RecyclerView.ViewHolder {
            RecyclerView plan_condition;
            TextView tv_title, tv_prise, tv_text;

            public viewholder(View view) {
                super(view);
                plan_condition = view.findViewById(R.id.plan_condition);
                tv_title = view.findViewById(R.id.tv_title);
                tv_prise = view.findViewById(R.id.tv_prise);
                tv_text = view.findViewById(R.id.tv_text);

            }
        }

        public static class PlanDataAdapter extends RecyclerView.Adapter<PlanDataAdapter.PlanDataclass> {

            public Activity mCtx;
            private final Context mcntx;
            private final List<Plandetail.Plansublist> planDetails;


            public PlanDataAdapter(Context mCtx, List<Plandetail.Plansublist> planDetails) {
                this.mcntx = mCtx;
                this.planDetails = planDetails;
            }


            @NonNull
            @Override
            public PlanDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.plandetail_layout_user, parent, false);
                return new PlanDataclass(view);
            }

            @Override
            public void onBindViewHolder(@NonNull PlanDataclass holder, int position) {
                holder.check_text.setText(planDetails.get(position).getCheck_text());
                holder.check_text1.setText(planDetails.get(position).getCheck_text());


                String plan_check = planDetails.get(position).getCheck_flag();
                if (plan_check.equals("true")) {
                    holder.unselected_layout.setVisibility(View.GONE);
                    holder.selected_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.unselected_layout.setVisibility(View.VISIBLE);
                    holder.selected_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public int getItemCount() {
                return planDetails.size();
            }


            public static class PlanDataclass extends RecyclerView.ViewHolder {

                ImageView plan_selected_or_not, plan_selected_or_not1;
                TextView check_text, check_text1;
                LinearLayout unselected_layout, selected_layout;

                public PlanDataclass(@NonNull View itemView) {
                    super(itemView);
                    check_text = itemView.findViewById(R.id.check_text);
                    check_text1 = itemView.findViewById(R.id.check_text1);
                    selected_layout = itemView.findViewById(R.id.selected_layout);
                    unselected_layout = itemView.findViewById(R.id.unselected_layout);
                }

            }

        }
    }
}