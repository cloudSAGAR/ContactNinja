package com.contactninja.Unuse.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.contactninja.MainActivity;
import com.contactninja.Manual_email_sms.Sms_And_Email_Auto_Manual;
import com.contactninja.Manual_email_sms.Sms_Detail_Activty;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Sms_List_Fragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    LinearLayout demo_layout, add_new_contect_layout, mMainLayout;
    TextView tv_create;
    RecyclerView rv_email_list;
    SwipeRefreshLayout swipeToRefresh;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    EmailAdepter emailAdepter;
    List<ManualTaskModel> manualTaskModelList = new ArrayList<>();
    private BroadcastReceiver mNetworkReceiver;
    LinearLayout layout_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_sms__list_, container, false);
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
        return  view;

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
                SessionManager.setMessage_number("");
                SessionManager.setMessage_type("");
                SessionManager.setMessage_id("");
                SessionManager.setCampaign_type("");
                SessionManager.setCampaign_type_name("");
                SessionManager.setCampaign_Day("");
                SessionManager.setCampaign_minute("");
                SessionManager.setEmail_screen_name("");
                Intent intent1=new Intent(getActivity(), Sms_And_Email_Auto_Manual.class);
                intent1.putExtra("flag","edit");
                intent1.putExtra("type","SMS");
                startActivity(intent1); //  finish();
                break;
            case R.id.demo_layout:
                SessionManager.setMessage_number("");
                SessionManager.setMessage_type("");
                SessionManager.setMessage_id("");
                SessionManager.setCampaign_type("");
                SessionManager.setCampaign_type_name("");
                SessionManager.setCampaign_Day("");
                SessionManager.setCampaign_minute("");
                SessionManager.setEmail_screen_name("");
                Intent intent=new Intent(getActivity(), Sms_And_Email_Auto_Manual.class);
                intent.putExtra("flag","edit");
                intent.putExtra("type","SMS");
                startActivity(intent);
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
                        if (!emailActivityListModel.getManualTask().get(i).getType().toString().equals("EMAIL"))
                        {
                            manualTaskModelList.add(emailActivityListModel.getManualTask().get(i));
                        }
                    }
                    //manualTaskModelList = emailActivityListModel.getManualTask();

                    if (manualTaskModelList.size()==0)
                    {
                        demo_layout.setVisibility(View.VISIBLE);
                        layout_search.setVisibility(View.GONE);
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
            holder.tv_username.setText(item.getUserName());
            holder.tv_task_description.setText(item.getContentBody());
           // holder.tv_status.setText(item.getStatus());
            try {
                String time =Global.getDate(item.getStartTime());
                holder.tv_time.setText(time);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String currentDateandTime = sdf.format(new Date());
                compareDates(currentDateandTime,time,holder.tv_status);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String name =item.getUserName();
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
                    SessionManager.setManualTaskModel(item);
                    Intent intent=new Intent(getActivity(), Sms_Detail_Activty.class);
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