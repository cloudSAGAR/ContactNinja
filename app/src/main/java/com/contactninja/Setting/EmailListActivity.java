package com.contactninja.Setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.contactninja.AddContect.EmailSend_Activity;
import com.contactninja.Interface.TemplateClick;
import com.contactninja.MainActivity;
import com.contactninja.Model.TemplateList;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UserLinkedList;
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
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class EmailListActivity extends AppCompatActivity implements View.OnClickListener,
        ConnectivityReceiver.ConnectivityReceiverListener,  SwipeRefreshLayout.OnRefreshListener {
    ImageView iv_back;
    private BroadcastReceiver mNetworkReceiver;
    LinearLayout mMainLayout;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RecyclerView rv_email_list;
    EmailAdepter emailAdepter;

    SwipeRefreshLayout swipeToRefresh;
    List<UserLinkedList.UserLinkedGmail> userLinkedGmailList=new ArrayList<>();
    LinearLayout add_new_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_list);
        mNetworkReceiver = new ConnectivityReceiver();
        retrofitCalls = new RetrofitCalls(EmailListActivity.this);
        loadingDialog=new LoadingDialog(EmailListActivity.this);
        sessionManager=new SessionManager(EmailListActivity.this);
        IntentUI();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(Global.isNetworkAvailable(EmailListActivity.this, MainActivity.mMainLayout)) {
                Mail_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Mail_list() throws JSONException {
        if(!swipeToRefresh.isRefreshing()){
            loadingDialog.showLoadingDialog();
        }
        SignResponseModel signResponseModel= SessionManager.getGetUserdata(EmailListActivity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Mail_list(sessionManager,obj, loadingDialog, token, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                swipeToRefresh.setRefreshing(false);
                if (response.body().getStatus() == 200) {
                    userLinkedGmailList.clear();
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UserLinkedList>() {
                    }.getType();
                    UserLinkedList userLinkedGmail=new Gson().fromJson(headerString, listType);

                    userLinkedGmailList=userLinkedGmail.getUserLinkedGmail();

                    rv_email_list.setLayoutManager(new LinearLayoutManager(EmailListActivity.this, LinearLayoutManager.VERTICAL, false));
                    emailAdepter=new EmailAdepter(EmailListActivity.this,userLinkedGmailList);
                    rv_email_list.setAdapter(emailAdepter);


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
        add_new_email = findViewById(R.id.add_new_email);
        rv_email_list = findViewById(R.id.rv_email_list);
        mMainLayout = findViewById(R.id.mMainLayout);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        add_new_email.setOnClickListener(this);




        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
                case R.id.add_new_email:
                    Global.openEmailAuth(EmailListActivity.this);
                   // startActivity(new Intent(getApplicationContext(),Email_verification.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onRefresh() {
        try {
            if(Global.isNetworkAvailable(EmailListActivity.this, MainActivity.mMainLayout)) {
                Mail_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }
    public class EmailAdepter extends RecyclerView.Adapter<EmailAdepter.viewData> {

        public Context mCtx;
        List<UserLinkedList.UserLinkedGmail> userLinkedGmailList;
        public EmailAdepter(Context context,   List<UserLinkedList.UserLinkedGmail> userLinkedGmailList) {
            this.mCtx = context;
            this.userLinkedGmailList = userLinkedGmailList;
        }

        @NonNull
        @Override
        public EmailAdepter.viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_emaillist, parent, false);
            return new EmailAdepter.viewData(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EmailAdepter.viewData holder, int position) {
            UserLinkedList.UserLinkedGmail userLinkedGmail=userLinkedGmailList.get(position);

            Glide.with(mCtx)
                    .load(userLinkedGmail.getPicture())
                    .circleCrop()
                    .placeholder(Global.setplaceholder(mCtx))
                    .into(holder.iv_select_type);
            holder.tv_email_name.setText(userLinkedGmail.getUserEmail());
            if(userLinkedGmail.getIsDefault().equals(1)){
                holder.iv_is_default.setVisibility(View.VISIBLE);
            }else {
                holder.iv_is_default.setVisibility(View.GONE);

            }

        }

        @Override
        public int getItemCount() {
            return userLinkedGmailList.size();
        }


        public class viewData extends RecyclerView.ViewHolder {
            TextView tv_email_name;
            LinearLayout layout_email;
            ImageView iv_is_default;
            RoundedImageView iv_select_type;
            public viewData(@NonNull View itemView) {
                super(itemView);
                tv_email_name = itemView.findViewById(R.id.tv_email_name);
                layout_email = itemView.findViewById(R.id.layout_email);
                iv_select_type = itemView.findViewById(R.id.iv_select_type);
                iv_is_default = itemView.findViewById(R.id.iv_is_default);
            }
        }
    }
}