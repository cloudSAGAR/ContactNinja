package com.contactninja.Auth.PlanTyep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Auth.Thankyou_Screen;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Plan_Detail_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    TextView tv_main_text, tv_sub_titale, start_button;
    RecyclerView plan_condition;
    RecyclerView.LayoutManager layoutManager;
    List<Plandetail> plandetailslist;
    int flag = 0;
    String plan_product_id="";
    Plandetail plandetail_model;
    List<Plandetail.Plansublist> plansublists;
    ImageView tv_back;
    SessionManager sessionManager;

    private BroadcastReceiver mNetworkReceiver;
    ConstraintLayout mMainLayout;

    LoadingDialog loadingDialog;
    RetrofitCalls retrofitCalls;
    Integer user_id = 0;
    String token_api = "", organization_id = "", team_id = "";
    SignResponseModel user_data;
    @Override
    public void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail_screen);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        retrofitCalls = new RetrofitCalls(getApplicationContext());
        IntentUI();
        sessionManager = new SessionManager(getApplicationContext());
        plan_condition.setLayoutManager(layoutManager);
        Intent pre_data = getIntent();
        Bundle pre_bundle = pre_data.getExtras();
        flag = pre_bundle.getInt("flag");
        plan_product_id = pre_bundle.getString("plan_product_id");
        mainflagdata(flag);


        token_api = Global.getToken(sessionManager);
        user_data = SessionManager.getGetUserdata(getApplicationContext());
        user_id = user_data.getUser().getId();
        organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (Global.isNetworkAvailable(Plan_Detail_Screen.this,mMainLayout)) {
                        Add_trial_subscription(plan_product_id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void Add_trial_subscription(String plan_product_id) {
        loadingDialog.showLoadingDialog();
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("plan_product_id", plan_product_id);
        obj.add("data", paramObject);
        retrofitCalls.Add_Subscription(sessionManager, obj, loadingDialog, token_api, Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                Gson gson = new Gson();
                String headerString = gson.toJson(response.body());
                Type listType = new TypeToken<Subscription>() {
                }.getType();
                Subscription subscription = new Gson().fromJson(headerString, listType);
                if(subscription.getHttpStatus().equals(200)){
                    sessionManager.login();
                    startActivity(new Intent(getApplicationContext(), Thankyou_Screen.class));
                    finish();
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

    @SuppressLint("SetTextI18n")
    private void mainflagdata(int flag) {
        plandetail_model = new Plandetail();
        // plansublist=new Plandetail.Plansublist();
        Plandetail plandetail1 = new Plandetail();

        List<Plandetail.Plansublist> plansublists123 = new ArrayList<>();


        if (flag == 1) {

            tv_main_text.setText("Ninja Free Card");
            tv_sub_titale.setText("Join the thousands of users who use Contact Ninja to manage their connections and communicate quickly and easily with their network");
            for (int i = 0; i < 5; i++) {
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
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);

                } else if (i == 4) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("4");
                    plansublist.setCheck_text("Lead Tracking");
                    plansublists123.add(i, plansublist);

                } else if (i == 5) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("5");
                    plansublist.setCheck_text("Automated Campaigns");
                    plansublists123.add(i, plansublist);
                } else if (i == 6) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("6");
                    plansublist.setCheck_text("Calendar Integration");
                    plansublists123.add(i, plansublist);

                }


            }
            PlanDataAdapter planDataAdapter = new PlanDataAdapter(getApplicationContext(), plansublists123);
            plan_condition.setAdapter(planDataAdapter);


        } else if (flag == 2) {

            tv_main_text.setText("Ninja Bzcard");
            tv_sub_titale.setText("Join the thousands of users who use Contact Ninja to manage their connections and communicate quickly and easily with their network");
            for (int i = 0; i < 5; i++) {
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
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);

                } else if (i == 4) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("4");
                    plansublist.setCheck_text("Lead Tracking");
                    plansublists123.add(i, plansublist);

                } else if (i == 5) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("5");
                    plansublist.setCheck_text("Automated Campaigns");
                    plansublists123.add(i, plansublist);
                } else if (i == 6) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("6");
                    plansublist.setCheck_text("Calendar Integration");
                    plansublists123.add(i, plansublist);

                }


            }
            PlanDataAdapter planDataAdapter = new PlanDataAdapter(getApplicationContext(), plansublists123);
            plan_condition.setAdapter(planDataAdapter);

        } else if (flag == 3) {


            tv_main_text.setText("Ninja Text Master");
            tv_sub_titale.setText("Join the thousands of users who use Contact Ninja to manage their connections and communicate quickly and easily with their network");
            for (int i = 0; i < 5; i++) {
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

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);
                } else if (i == 3) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);

                } else if (i == 4) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("4");
                    plansublist.setCheck_text("Lead Tracking");
                    plansublists123.add(i, plansublist);

                } else if (i == 5) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("5");
                    plansublist.setCheck_text("Automated Campaigns");
                    plansublists123.add(i, plansublist);
                } else if (i == 6) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("false");
                    plansublist.setCheck_id("6");
                    plansublist.setCheck_text("Calendar Integration");
                    plansublists123.add(i, plansublist);

                }


            }
            PlanDataAdapter planDataAdapter = new PlanDataAdapter(getApplicationContext(), plansublists123);
            plan_condition.setAdapter(planDataAdapter);
        } else if (flag == 4) {

            tv_main_text.setText("ContactNinja");
            tv_sub_titale.setText("Join the thousands of users who use Contact Ninja to manage their connections and communicate quickly and easily with their network");
            for (int i = 0; i < 5; i++) {
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

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);
                } else if (i == 3) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();

                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("3");
                    plansublist.setCheck_text("Broadcasting");
                    plansublists123.add(i, plansublist);

                } else if (i == 4) {
                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("4");
                    plansublist.setCheck_text("Lead Tracking");
                    plansublists123.add(i, plansublist);

                } else if (i == 5) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("5");
                    plansublist.setCheck_text("Automated Campaigns");
                    plansublists123.add(i, plansublist);
                } else if (i == 6) {

                    Plandetail.Plansublist plansublist = new Plandetail.Plansublist();
                    plansublist.setCheck_flag("true");
                    plansublist.setCheck_id("6");
                    plansublist.setCheck_text("Calendar Integration");
                    plansublists123.add(i, plansublist);

                }


            }
            PlanDataAdapter planDataAdapter = new PlanDataAdapter(getApplicationContext(), plansublists123);
            plan_condition.setAdapter(planDataAdapter);

        }

    }

    private void IntentUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        tv_main_text = findViewById(R.id.tv_main_text);
        tv_sub_titale = findViewById(R.id.tv_sub_titale);
        plan_condition = findViewById(R.id.plan_condition);
        layoutManager = new LinearLayoutManager(this);
        plandetailslist = new ArrayList<>();
        plansublists = new ArrayList<>();
        start_button = findViewById(R.id.start_button);
        tv_back = findViewById(R.id.tv_back);

    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Plan_Detail_Screen.this, mMainLayout);
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


    public class PlanDataAdapter extends RecyclerView.Adapter<PlanDataAdapter.PlanDataclass> {

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
            View view = inflater.inflate(R.layout.plandetail_layout, parent, false);
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


        public class PlanDataclass extends RecyclerView.ViewHolder {

            ImageView plan_selected_or_not, plan_selected_or_not1;
            TextView check_text, check_text1;
            LinearLayout unselected_layout, selected_layout;

            public PlanDataclass(@NonNull View itemView) {
                super(itemView);
                plan_selected_or_not = findViewById(R.id.plan_selected_or_not);
                check_text = itemView.findViewById(R.id.check_text);
                plan_selected_or_not1 = findViewById(R.id.plan_selected_or_not1);
                check_text1 = itemView.findViewById(R.id.check_text1);
                selected_layout = itemView.findViewById(R.id.selected_layout);
                unselected_layout = itemView.findViewById(R.id.unselected_layout);


            }

        }

    }

}
