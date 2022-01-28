package com.contactninja.Manual_email_and_sms.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.contactninja.MainActivity;
import com.contactninja.Manual_email_and_sms.Email_Detail_activty;
import com.contactninja.Manual_email_and_sms.Manual_Email_Activity;
import com.contactninja.Manual_email_and_sms.Sms_And_Email_Auto_Manual;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Email_List_Fragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    LinearLayout demo_layout, add_new_contect_layout, mMainLayout,layout_search;
    TextView tv_create;
    RecyclerView rv_email_list;
    SwipeRefreshLayout swipeToRefresh;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    EmailAdepter emailAdepter;
    List<ManualTaskModel> manualTaskModelList = new ArrayList<>();
    ImageView iv_back;
    private BroadcastReceiver mNetworkReceiver;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_email__list_, container, false);
        intentView(view);
        mNetworkReceiver = new ConnectivityReceiver();
        retrofitCalls = new RetrofitCalls(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        sessionManager = new SessionManager(getActivity());
        try {
            Mail_list();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void intentView(View view) {
        layout_search=view.findViewById(R.id.layout_search);
        demo_layout = view.findViewById(R.id.demo_layout);
        mMainLayout = view.findViewById(R.id.mMainLayout);
        demo_layout.setOnClickListener(this);
        tv_create = view.findViewById(R.id.tv_create);
        tv_create.setText(getString(R.string.email_txt));
        rv_email_list = view.findViewById(R.id.email_list);
        add_new_contect_layout = view.findViewById(R.id.add_new_contect_layout);
        add_new_contect_layout.setOnClickListener(this);
        rv_email_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.add_new_contect_layout:
                SessionManager.setGroupList(getActivity(), new ArrayList<>());
               // startActivity(new Intent(getActivity(), Manual_Email_Activity.class));
                //finish();

                SessionManager.setMessage_number("");
                SessionManager.setMessage_type("");
                SessionManager.setMessage_id("");
                SessionManager.setEmail_screen_name("manual");
                SessionManager.setCampaign_type("");
                SessionManager.setCampaign_type_name("");
                SessionManager.setCampaign_Day("");
                SessionManager.setCampaign_minute("");
                Intent intent=new Intent(getActivity(), Sms_And_Email_Auto_Manual.class);
                intent.putExtra("flag","edit");
                intent.putExtra("type","EMAIL");
                startActivity(intent);
                break;
            case R.id.demo_layout:
                SessionManager.setMessage_number("");
                SessionManager.setMessage_type("");
                SessionManager.setMessage_id("");
                SessionManager.setEmail_screen_name("manual");
                SessionManager.setCampaign_type("");
                SessionManager.setCampaign_type_name("");
                SessionManager.setCampaign_Day("");
                SessionManager.setCampaign_minute("");
                Intent intent1=new Intent(getActivity(), Sms_And_Email_Auto_Manual.class);
                intent1.putExtra("flag","edit");
                intent1.putExtra("type","EMAIL");
                startActivity(intent1); //  finish();
                break;

        }
    }

    void Mail_list() throws JSONException {

        if (!swipeToRefresh.isRefreshing()) {
            loadingDialog.showLoadingDialog();
        }
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(getActivity());
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Mail_Activiy_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
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
                        if (emailActivityListModel.getManualTask().get(i).getType().toString().equals("EMAIL"))
                        {
                            manualTaskModelList.add(emailActivityListModel.getManualTask().get(i));
                        }
                    }
                    //manualTaskModelList = emailActivityListModel.getManualTask();

                   if (manualTaskModelList.size()==0)
                   {
                       layout_search.setVisibility(View.GONE);
                       demo_layout.setVisibility(View.VISIBLE);


                   }
                   else {
                       layout_search.setVisibility(View.VISIBLE);
                       rv_email_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                       emailAdepter = new EmailAdepter(getActivity(), manualTaskModelList);
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
            if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                Mail_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class EmailAdepter extends RecyclerView.Adapter<EmailAdepter.viewData> {

        public Context mCtx;
        List<ManualTaskModel> manualTaskModelList;

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
            holder.tv_status.setText(item.getStatus());
            try {
                String time =Global.getDate(item.getStartTime());
                holder.tv_time.setText(time);
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
            holder.no_image.setText(add_text);
            holder.no_image.setVisibility(View.VISIBLE);
            holder.profile_image.setVisibility(View.GONE);
                holder.layout_contec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("Item List is",new Gson().toJson(item));

                    SessionManager.setManualTaskModel(item);
                    Intent intent=new Intent(getActivity(), Email_Detail_activty.class);
                    startActivity(intent);
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
}