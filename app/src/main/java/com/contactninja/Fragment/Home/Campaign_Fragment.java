package com.contactninja.Fragment.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
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

import com.contactninja.Campaign.Campaign_Overview;
import com.contactninja.Campaign.Campaign_Preview;
import com.contactninja.Campaign.List_itm.Campaign_Final_Start;
import com.contactninja.Interface.Des_CampaingClick;
import com.contactninja.MainActivity;
import com.contactninja.Model.Dashboard.Dashboard;
import com.contactninja.Model.Dashboard.Des_Sequence;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.Global_Time;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Campaign_Fragment extends Fragment implements Des_CampaingClick {
    
    RecyclerView rv_campaign_list;
    CampaingListAdepter campaingListAdepter;
    ImageView iv_demo;
    SessionManager sessionManager;
    Integer user_id = 0;
    String token_api = "", organization_id = "", team_id = "";
    SignResponseModel user_data;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    Dashboard dashboard = new Dashboard();
    List<Des_Sequence> des_sequences = new ArrayList<>();
    private long mLastClickTime = 0;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_campaign_deshboard, container, false);
        retrofitCalls = new RetrofitCalls(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        sessionManager = new SessionManager(getActivity());
        token_api = Global.getToken(sessionManager);
        user_data = SessionManager.getGetUserdata(getActivity());
        user_id = user_data.getUser().getId();
        IntentUI(view);
        
        
        des_sequences = SessionManager.getDes_Sequence(getActivity());
        if (des_sequences.size() == 0) {
            rv_campaign_list.setVisibility(View.GONE);
            iv_demo.setVisibility(View.VISIBLE);
        } else {
            rv_campaign_list.setVisibility(View.VISIBLE);
            iv_demo.setVisibility(View.GONE);
            campaingListAdepter.add(des_sequences);
        }
        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();
        
        
        return view;
    }
    
    private void IntentUI(View view) {
        
        iv_demo = view.findViewById(R.id.iv_demo);
        rv_campaign_list = view.findViewById(R.id.rv_campaign_list);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_campaign_list.setLayoutManager(layoutManager);
        campaingListAdepter = new CampaingListAdepter(getActivity(), new ArrayList<>(), this);
        rv_campaign_list.setAdapter(campaingListAdepter);
        
    }
    
    @Override
    public void OnClick(Des_Sequence des_sequence) {
        SessionManager.setCampaign_type("");
        SessionManager.setCampaign_type_name("");
        SessionManager.setCampaign_Day("");
        SessionManager.setCampaign_minute("");
        Global.count = 1;
        SessionManager.setTask(getActivity(), new ArrayList<>());
        String contect_list_count = String.valueOf(des_sequence.getProspect());
        if (des_sequence.getStatus().equals("A")) {
            Intent intent = new Intent(getActivity(), Campaign_Final_Start.class);
            intent.putExtra("sequence_id", des_sequence.getId());
            startActivity(intent);
        } else if (des_sequence.getStatus().equals("I")) {
            if (des_sequence.getStartedOn() != null && !des_sequence.getStartedOn().equals("") && des_sequence.getProspect() != 0) {
                Intent intent = new Intent(getActivity(), Campaign_Final_Start.class);
                intent.putExtra("sequence_id", des_sequence.getId());
                startActivity(intent);
            } else {
                if (contect_list_count.equals("0")) {
                    Intent intent = new Intent(getActivity(), Campaign_Overview.class);
                    intent.putExtra("sequence_id", des_sequence.getId());
                    startActivity(intent);
                    //   finish();
                } else {
                    SessionManager.setCampign_flag("read");
                    Intent intent = new Intent(getActivity(), Campaign_Preview.class);
                    intent.putExtra("sequence_id", des_sequence.getId());
                    startActivity(intent);
                    
                }
            }
        }
        
        
    }
    
    private void Api_Dashboard() throws JSONException {
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_id);
        paramObject.put("date_time", Global_Time.getCurrentTimeandDate_24());
        
        JSONArray array = new JSONArray();
        
        
        array.put("SEQUENCE");
        
        
        try {
            // Add the JSONArray to the JSONObject
            paramObject.put("data_type", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        //  Log.e("Gson Data is", new Gson().toJson(gsonObject));
        retrofitCalls.Dashboard(sessionManager, gsonObject, loadingDialog, token_api, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @SuppressLint("NewApi")
            @Override
            public void success(Response<ApiResponse> response) {
                
                SessionManager.setDes_Sequence(getActivity(), new ArrayList<>());
                Gson gson = new Gson();
                String headerString = gson.toJson(response.body().getData());
                Type listType = new TypeToken<Dashboard>() {
                }.getType();
                dashboard = new Gson().fromJson(headerString, listType);
                des_sequences = dashboard.getSequence();
                SessionManager.setDes_Sequence(getActivity(), des_sequences);
                
                if (Global.IsNotNull(des_sequences) && des_sequences.size() != 0) {
                    campaingListAdepter.clear();
                    campaingListAdepter.add(des_sequences);
                    rv_campaign_list.setVisibility(View.VISIBLE);
                    iv_demo.setVisibility(View.GONE);
                } else {
                    rv_campaign_list.setVisibility(View.GONE);
                    iv_demo.setVisibility(View.VISIBLE);
                }
                
                
            }
            
            @Override
            public void error(Response<ApiResponse> response) {
            
            }
        });
    }
    
    @SuppressLint("StaticFieldLeak")
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
    
    public class CampaingListAdepter extends RecyclerView.Adapter<CampaingListAdepter.InviteListDataclass> {
        
        public Context mCtx;
        List<Des_Sequence> desSequenceList;
        Des_CampaingClick des_campaingClick;
        
        public CampaingListAdepter(Context context, List<Des_Sequence> desSequenceList, Des_CampaingClick des_campaingClick) {
            this.mCtx = context;
            this.desSequenceList = desSequenceList;
            this.des_campaingClick = des_campaingClick;
        }
        
        @NonNull
        @Override
        public CampaingListAdepter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_campaning_beshbord, parent, false);
            return new CampaingListAdepter.InviteListDataclass(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull CampaingListAdepter.InviteListDataclass holder, int position) {
            
            Des_Sequence item = desSequenceList.get(position);
            holder.tv_camp_name.setText(item.getSeqName());
            holder.tv_total_contact.setText(String.valueOf(item.getProspect()));
            
            setImage(item, holder);
            
            
            if (position == (getItemCount() - 1)) {
                holder.view_last_line.setVisibility(View.GONE);
            } else {
                holder.view_last_line.setVisibility(View.VISIBLE);
            }
            
            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    des_campaingClick.OnClick(item);
                }
            });
        }
        
        @Override
        public int getItemCount() {
            return desSequenceList.size();
        }
        
        public void add(List<Des_Sequence> des_sequences) {
            desSequenceList = des_sequences;
            notifyDataSetChanged();
        }
        
        public void clear() {
            desSequenceList.clear();
            notifyDataSetChanged();
            
        }
        
        private void setImage(Des_Sequence campaign, CampaingListAdepter.InviteListDataclass holder) {
            switch (campaign.getStatus()) {
                case "A":
                    /* status active */
                    holder.tv_status.setText(mCtx.getResources().getString(R.string.Active));
                    holder.tv_status.setTextColor(mCtx.getResources().getColor(R.color.Active_green));
                    
                    break;
                case "I":
                    if (campaign.getStartedOn() != null && !campaign.getStartedOn().equals("")) {
                        /* status paused */
                        holder.tv_status.setText(mCtx.getResources().getString(R.string.Paused));
                        holder.tv_status.setTextColor(mCtx.getResources().getColor(R.color.Paused_yellow));
                        
                    } else {
                        if (campaign.getProspect() != 0) {
                            /* status not active */
                            holder.tv_status.setText(mCtx.getResources().getString(R.string.noactive));
                            holder.tv_status.setTextColor(mCtx.getResources().getColor(R.color.notactive));
                            
                        } else {
                            /* status inactive */
                            holder.tv_status.setText(mCtx.getResources().getString(R.string.Inactive));
                            holder.tv_status.setTextColor(mCtx.getResources().getColor(R.color.Inactive_red));
                        }
                        
                    }
                    break;
            }
        }
        
        public class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_camp_name, tv_total_contact, tv_status;
            LinearLayout layout_item;
            
            View view_last_line;
            
            
            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                
                tv_camp_name = itemView.findViewById(R.id.tv_camp_name);
                tv_status = itemView.findViewById(R.id.tv_status);
                tv_total_contact = itemView.findViewById(R.id.tv_total_contact);
                view_last_line = itemView.findViewById(R.id.view_last_line);
                layout_item = itemView.findViewById(R.id.layout_item);
                
                
            }
            
        }
        
    }
    
}