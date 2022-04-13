package com.contactninja.Fragment.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.MainActivity;
import com.contactninja.Model.Dashboard.Dashboard;
import com.contactninja.Model.Dashboard.Des_Broadcast;
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
public class Broadcast_Fragment extends Fragment {
    RecyclerView rv_broadcast_list;
    BroadcastListAdepter broadcastListAdepter;
    ImageView iv_demo;
    SessionManager sessionManager;
    Integer user_id = 0;
    String token_api = "", organization_id = "", team_id = "";
    SignResponseModel user_data;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    Dashboard dashboard = new Dashboard();
    List<Des_Broadcast> des_broadcasts = new ArrayList<>();
    private long mLastClickTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_droadcast_deshboard, container, false);
        retrofitCalls = new RetrofitCalls(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        sessionManager = new SessionManager(getActivity());
        token_api = Global.getToken(sessionManager);
        user_data = SessionManager.getGetUserdata(getActivity());
        user_id = user_data.getUser().getId();
        IntentUI(view);

        des_broadcasts = SessionManager.getDes_Broadcast(getActivity());
        if (des_broadcasts.size() == 0) {
            rv_broadcast_list.setVisibility(View.GONE);
            iv_demo.setVisibility(View.VISIBLE);
        } else {
            rv_broadcast_list.setVisibility(View.VISIBLE);
            iv_demo.setVisibility(View.GONE);
            broadcastListAdepter.add(des_broadcasts);
        }
        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();


        return view;
    }

    private void IntentUI(View view) {

        iv_demo = view.findViewById(R.id.iv_demo);
        rv_broadcast_list = view.findViewById(R.id.rv_broadcast_list);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_broadcast_list.setLayoutManager(layoutManager);
        broadcastListAdepter = new BroadcastListAdepter(getActivity(), new ArrayList<>());
        rv_broadcast_list.setAdapter(broadcastListAdepter);
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

    private void Api_Dashboard() throws JSONException {
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_id);

        JSONArray array = new JSONArray();


        array.put("BROADCAST");


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


                try {
                    SessionManager.setDes_Broadcast(getActivity(), new ArrayList<>());
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<Dashboard>() {
                    }.getType();
                    dashboard = new Gson().fromJson(headerString, listType);
                    des_broadcasts = dashboard.getBroadcast();
                    SessionManager.setDes_Broadcast(getActivity(), des_broadcasts);

                    if (Global.IsNotNull(des_broadcasts) && des_broadcasts.size() != 0) {
                        broadcastListAdepter.clear();
                        broadcastListAdepter.add(des_broadcasts);

                        rv_broadcast_list.setVisibility(View.VISIBLE);
                        iv_demo.setVisibility(View.GONE);
                    } else {
                        rv_broadcast_list.setVisibility(View.GONE);
                        iv_demo.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void error(Response<ApiResponse> response) {

            }
        });
    }

    static class BroadcastListAdepter extends RecyclerView.Adapter<BroadcastListAdepter.InviteListDataclass> {

        List<Des_Broadcast> broadcastList;
        public Context mCtx;

        public BroadcastListAdepter(Context context, List<Des_Broadcast> broadcastList) {
            this.mCtx = context;
            this.broadcastList = broadcastList;
        }

        @NonNull
        @Override
        public BroadcastListAdepter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_brodadcastlist, parent, false);
            return new InviteListDataclass(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull BroadcastListAdepter.InviteListDataclass holder, int position) {

            Des_Broadcast item = broadcastList.get(position);

            try {
                if (item.getType().equals("SMS")) {
                    holder.image_icon.setImageResource(R.drawable.ic_message_tab);
                    //holder.tv_task_description.setTypeface(null, Typeface.BOLD);
                } else {
                    holder.image_icon.setImageResource(R.drawable.ic_email);
                }


                String conactname = item.getBroadcastName();
                holder.tv_username.setText(conactname);
                switch (item.getRecurringType()) {
                    case "D":
                        holder.tv_task_time.setText("Daily - " + Global_Time.TimeFormateAMPM(item.getStartTime()));
                        break;
                    case "W":
                        holder.tv_task_time.setText("Weekly - " + Global_Time.TimeFormateAMPM(item.getStartTime()));
                        break;
                    case "M":
                        holder.tv_task_time.setText("Monthly - " + Global_Time.TimeFormateAMPM(item.getStartTime()));

                        break;
                }
                holder.tv_task_description.setText(Html.fromHtml(item.getContentBody()));


                holder.iv_logo.setVisibility(View.GONE);


                if (position == (getItemCount() - 1)) {
                    holder.view_last_line.setVisibility(View.GONE);
                } else {
                    holder.view_last_line.setVisibility(View.VISIBLE);
                }


                switch (item.getStatus()) {
                    case "I":
                        if(Global.IsNotNull(item.getFirstActivated())){
                            holder.iv_puse_icon.setVisibility(View.VISIBLE);
                            holder.tv_status.setText("Paused");
                            holder.tv_status.setTextColor(mCtx.getResources().getColor(R.color.tv_push_color));
    
                        }else {
                            holder.iv_hold.setVisibility(View.VISIBLE);
                            holder.tv_status.setText("Inactive");
                            holder.tv_status.setTextColor(mCtx.getResources().getColor(R.color.red));
                        }
                        break;
                    case "A":
                        holder.iv_play_icon.setVisibility(View.VISIBLE);
                        holder.tv_status.setText("Active");
                        holder.tv_status.setTextColor(mCtx.getResources().getColor(R.color.text_green));

                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            return broadcastList.size();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void add(List<Des_Broadcast> des_broadcasts) {
            broadcastList = des_broadcasts;
            notifyDataSetChanged();
        }

        @SuppressLint("NotifyDataSetChanged")
        public void clear() {
            broadcastList.clear();
            notifyDataSetChanged();
        }


        public static class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_username, tv_task_description, tv_time, no_image, tv_status, tv_task_time;
            ImageView image_icon, iv_camp, iv_hold, iv_play_icon, iv_puse_icon;
            RelativeLayout iv_logo;
            LinearLayout layout_contec;
            View view_last_line;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                iv_puse_icon = itemView.findViewById(R.id.iv_puse_icon);
                iv_play_icon = itemView.findViewById(R.id.iv_play_icon);
                iv_hold = itemView.findViewById(R.id.iv_hold);
                tv_task_time = itemView.findViewById(R.id.tv_task_time);
                tv_username = itemView.findViewById(R.id.tv_username);
                tv_task_description = itemView.findViewById(R.id.tv_task_description);
                tv_time = itemView.findViewById(R.id.tv_time);
                no_image = itemView.findViewById(R.id.no_image);
                tv_status = itemView.findViewById(R.id.tv_status);
                image_icon = itemView.findViewById(R.id.image_icon);
                iv_camp = itemView.findViewById(R.id.iv_camp);
                iv_logo = itemView.findViewById(R.id.iv_logo);
                view_last_line = itemView.findViewById(R.id.view_last_line);
                layout_contec = itemView.findViewById(R.id.layout_contec);

            }

        }

    }
}