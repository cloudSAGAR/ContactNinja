package com.contactninja.Fragment.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contactninja.MainActivity;
import com.contactninja.Model.Dashboard.Dashboard;
import com.contactninja.Model.Dashboard.Des_Broadcast;
import com.contactninja.Model.Dashboard.Des_Sequence;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

public class Campaign_Fragment extends Fragment {

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
        try {
            if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                Api_Dashboard();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }
    private void IntentUI(View view) {

        iv_demo=view.findViewById(R.id.iv_demo);
        rv_campaign_list=view.findViewById(R.id.rv_campaign_list);

    }

    private void Api_Dashboard() throws JSONException {
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_id);

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
        Log.e("Gson Data is", new Gson().toJson(gsonObject));
        retrofitCalls.Dashboard(sessionManager, gsonObject, loadingDialog, token_api, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @SuppressLint("NewApi")
            @Override
            public void success(Response<ApiResponse> response) {


                Gson gson = new Gson();
                String headerString = gson.toJson(response.body().getData());
                Type listType = new TypeToken<Dashboard>() {
                }.getType();
                dashboard = new Gson().fromJson(headerString, listType);
                des_sequences = dashboard.getSequence();

                if(Global.IsNotNull(des_sequences)&&des_sequences.size()!=0){
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rv_campaign_list.setLayoutManager(layoutManager);
                    campaingListAdepter = new CampaingListAdepter(getActivity(), des_sequences);
                    rv_campaign_list.setAdapter(campaingListAdepter);
                    rv_campaign_list.setVisibility(View.VISIBLE);
                    iv_demo.setVisibility(View.GONE);
                }else {
                    rv_campaign_list.setVisibility(View.GONE);
                    iv_demo.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void error(Response<ApiResponse> response) {

            }
        });
    }


    public class CampaingListAdepter extends RecyclerView.Adapter<CampaingListAdepter.InviteListDataclass> {

        List<Des_Sequence> des_sequences;
        public Context mCtx;

        public CampaingListAdepter(Context context,List<Des_Sequence> des_sequences) {
            this.mCtx = context;
            this.des_sequences = des_sequences;
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

            Des_Sequence item = des_sequences.get(position);
            holder.tv_camp_name.setText(item.getSeqName());
            holder.tv_total_contact.setText(String.valueOf(item.getMaxProspect()));



            if(position==(getItemCount()-1)){
                holder.view_last_line.setVisibility(View.GONE);
            }else {
                holder.view_last_line.setVisibility(View.VISIBLE);
            }


        }

        @Override
        public int getItemCount() {
            return des_sequences.size();
        }



        public class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_camp_name,tv_total_contact ;


            View view_last_line;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                tv_camp_name = itemView.findViewById(R.id.tv_camp_name);
                tv_total_contact = itemView.findViewById(R.id.tv_total_contact);
                view_last_line = itemView.findViewById(R.id.view_last_line);


            }

        }

    }

}