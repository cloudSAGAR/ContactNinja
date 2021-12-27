package com.contactninja.Setting;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.contactninja.Campaign.Automated_Email_Activity;
import com.contactninja.Interface.TemplateClick;
import com.contactninja.MainActivity;
import com.contactninja.Model.TemplateList;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Response;

public class TemplateActivity extends AppCompatActivity implements View.OnClickListener,
        ConnectivityReceiver.ConnectivityReceiverListener,TemplateClick, SwipeRefreshLayout.OnRefreshListener {
    ImageView iv_back;
    LinearLayout demo_layout, add_new_Template;
    LinearLayout mMainLayout1;
    TextView tv_create;
    private BroadcastReceiver mNetworkReceiver;
    LinearLayout mMainLayout;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RecyclerView rv_template_list;
    TemplateAdepter templateAdepter;
    TemplateClick templateClick;
    SwipeRefreshLayout swipeToRefresh;

    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);


        mNetworkReceiver = new ConnectivityReceiver();
        retrofitCalls = new RetrofitCalls(TemplateActivity.this);
        loadingDialog=new LoadingDialog(TemplateActivity.this);
        sessionManager=new SessionManager(TemplateActivity.this);
        templateClick=this;
        IntentUI();

        try {
            if(Global.isNetworkAvailable(TemplateActivity.this, MainActivity.mMainLayout)) {
                Template_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Template_list() throws JSONException {
        if(!swipeToRefresh.isRefreshing()){
            loadingDialog.showLoadingDialog();
        }
        SignResponseModel signResponseModel= SessionManager.getGetUserdata(TemplateActivity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        Log.e("Tokem is ",new Gson().toJson(obj));
        retrofitCalls.Template_list(sessionManager,obj, loadingDialog, token, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                swipeToRefresh.setRefreshing(false);
                if (response.body().getStatus() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<TemplateList>() {
                    }.getType();
                    TemplateList templateList=new Gson().fromJson(headerString, listType);


                    if(templateList.getTemplate().size()==0){
                        demo_layout.setVisibility(View.VISIBLE);
                        mMainLayout1.setVisibility(View.GONE);
                    }else {
                        demo_layout.setVisibility(View.GONE);
                        mMainLayout1.setVisibility(View.VISIBLE);
                    }

                    rv_template_list.setLayoutManager(new LinearLayoutManager(TemplateActivity.this, LinearLayoutManager.VERTICAL, false));
                    templateAdepter=new TemplateAdepter(TemplateActivity.this,templateList.getTemplate(),templateClick);
                    rv_template_list.setAdapter(templateAdepter);


                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                swipeToRefresh.setRefreshing(false);
                loadingDialog.cancelLoading();
            }


        });


    }
    private void IntentUI() {
        rv_template_list = findViewById(R.id.template_list);
        mMainLayout = findViewById(R.id.mMainLayout);
        mMainLayout1 = findViewById(R.id.mMainLayout1);
        add_new_Template = findViewById(R.id.add_new_Template);
        add_new_Template.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        tv_create = findViewById(R.id.tv_create);
        tv_create.setText(getResources().getString(R.string.cratetemplate));
        demo_layout = findViewById(R.id.demo_layout);
        demo_layout.setOnClickListener(this);


        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(this);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@SuppressLint("UnknownNullness") View v) {
        switch (v.getId()) {
            case R.id.demo_layout:
            case R.id.add_new_Template:
                bouttomSheet();

                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }
    private void bouttomSheet() {
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.item_create_templete_select, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(TemplateActivity.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
          LinearLayout layout_SMS=bottomSheetDialog.findViewById(R.id.layout_SMS);
          LinearLayout layout_email=bottomSheetDialog.findViewById(R.id.layout_email);
        layout_SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(new Intent(getApplicationContext(), TemplateCreateActivity.class));
                i.putExtra("template_type","SMS");
                startActivity(i);
                bottomSheetDialog.dismiss();

            }
        });
        layout_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(new Intent(getApplicationContext(), TemplateCreateActivity.class));
                i.putExtra("template_type","EMAIL");
                startActivity(i);
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.show();
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(TemplateActivity.this, mMainLayout);
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

    @Override
    public void OnClick(TemplateList.Template template) {
        Intent i=new Intent(new Intent(getApplicationContext(), TemplateCreateActivity.class));
        i.putExtra("template",template);
        startActivity(i);

    }

    @Override
    public void onRefresh() {
        try {
            if(Global.isNetworkAvailable(TemplateActivity.this, MainActivity.mMainLayout)) {
                Template_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class TemplateAdepter extends RecyclerView.Adapter<TemplateAdepter.viewData> {

        public Context mCtx;
        private List<TemplateList.Template> templateList;
        TemplateClick templateClick;

        public TemplateAdepter(Context context, List<TemplateList.Template> templateList,TemplateClick templateClick) {
            this.mCtx = context;
            this.templateList = templateList;
            this.templateClick = templateClick;
        }

        @NonNull
        @Override
        public TemplateAdepter.viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_templatelist, parent, false);
            return new TemplateAdepter.viewData(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TemplateAdepter.viewData holder, int position) {
            TemplateList.Template template=templateList.get(position);
            holder.tv_template_name.setText(template.getTemplateName());
            holder.layout_template.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    templateClick.OnClick(template);
                }
            });
            if(template.getType().equals("EMAIL")){
                holder.iv_select_type.setBackgroundResource(R.drawable.ic_email);
            }else {
                holder.iv_select_type.setBackgroundResource(R.drawable.ic_message_tab);
            }

        }

        @Override
        public int getItemCount() {
            return templateList.size();
        }


        public class viewData extends RecyclerView.ViewHolder {
            TextView tv_template_name;
            LinearLayout layout_template;
            ImageView iv_select_type;
            public viewData(@NonNull View itemView) {
                super(itemView);
                tv_template_name = itemView.findViewById(R.id.tv_template_name);
                layout_template = itemView.findViewById(R.id.layout_template);
                iv_select_type = itemView.findViewById(R.id.iv_select_type);
            }
        }
    }
}