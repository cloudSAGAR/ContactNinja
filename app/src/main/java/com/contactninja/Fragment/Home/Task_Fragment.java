package com.contactninja.Fragment.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contactninja.MainActivity;
import com.contactninja.Model.Dashboard.Dashboard;
import com.contactninja.Model.Dashboard.Des_Task;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;


public class Task_Fragment extends Fragment  {
    RecyclerView rv_Task_list;
    TaslListAdepter taslListAdepter;
    ImageView iv_demo;
    SessionManager sessionManager;
    Integer user_id = 0;
    String token_api = "", organization_id = "", team_id = "";
    SignResponseModel user_data;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    Dashboard dashboard = new Dashboard();
    List<Des_Task> des_tasks = new ArrayList<>();

    public Task_Fragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_dashboard, container, false);
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
        rv_Task_list=view.findViewById(R.id.rv_Task_list);

    }


    private void Api_Dashboard() throws JSONException {
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_id);

        JSONArray array = new JSONArray();


        array.put("TASK");


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
                des_tasks = dashboard.getTask();

                if(Global.IsNotNull(des_tasks)&&des_tasks.size()!=0){
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rv_Task_list.setLayoutManager(layoutManager);
                    taslListAdepter = new TaslListAdepter(getActivity(), des_tasks);
                    rv_Task_list.setAdapter(taslListAdepter);
                    rv_Task_list.setVisibility(View.VISIBLE);
                    iv_demo.setVisibility(View.GONE);
                }else {
                    rv_Task_list.setVisibility(View.GONE);
                    iv_demo.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void error(Response<ApiResponse> response) {

            }
        });
    }



    public class TaslListAdepter extends RecyclerView.Adapter<TaslListAdepter.InviteListDataclass> {

        List<Des_Task> taskList;
        public Context mCtx;
        LinearLayout layout_Add_email;

        public TaslListAdepter(Context context,List<Des_Task> taskList) {
            this.mCtx = context;
            this.taskList = taskList;
            this.layout_Add_email = layout_Add_email;
        }

        @NonNull
        @Override
        public TaslListAdepter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_emailactivitylist, parent, false);
            return new TaslListAdepter.InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaslListAdepter.InviteListDataclass holder, int position) {

            Des_Task item = taskList.get(position);
            if (Global.IsNotNull(item.getType()) && !item.getType().equals("")) {
                if (item.getType().equals("SMS")) {
                    holder.image_icon.setImageResource(R.drawable.ic_message_tab);
                } else {
                    holder.image_icon.setImageResource(R.drawable.ic_email);
                }


                if (Global.IsNotNull(item.getTaskFrom())) {
                    if(item.getTaskFrom()==1){
                        holder.iv_labal.setVisibility(View.VISIBLE);
                        holder.iv_labal.setImageResource(R.drawable.ic_campaning_icon);
                    }else if(item.getTaskFrom()==2){
                        holder.iv_labal.setVisibility(View.GONE);

                    } else if(item.getTaskFrom()==3){
                        holder.iv_labal.setVisibility(View.VISIBLE);
                        holder.iv_labal.setImageResource(R.drawable.ic_broadcast);
                    }
                } else {
                    holder.iv_labal.setVisibility(View.GONE);
                }

                String conactname = item.getContactMasterFirstname() + " " + item.getContactMasterLastname();
                holder.tv_username.setText(Global.setFirstLetter(conactname));
                holder.tv_task_description.setText(Global.setFirstLetter(item.getTaskName()));


                String name = conactname;
                String add_text = "";
                String[] split_data = name.split(" ");
                try {
                    for (int i = 0; i < split_data.length; i++) {
                        if (i == 0) {
                            add_text = split_data[i].substring(0, 1);
                        } else {
                            add_text = add_text + split_data[i].substring(0, 1);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.no_image.setText(add_text.toUpperCase());
                holder.no_image.setVisibility(View.VISIBLE);


                if(position==(getItemCount()-1)){
                   holder.view_last_line.setVisibility(View.GONE);
                }else {
                    holder.view_last_line.setVisibility(View.VISIBLE);
                }

            }

        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }



        public class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_username, tv_task_description, tv_time, no_image, tv_status;
            ImageView image_icon, iv_labal;
            View view_last_line;

            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                tv_username = itemView.findViewById(R.id.tv_username);
                tv_task_description = itemView.findViewById(R.id.tv_task_description);
                tv_time = itemView.findViewById(R.id.tv_time);
                no_image = itemView.findViewById(R.id.no_image);
                tv_status = itemView.findViewById(R.id.tv_status);
                image_icon = itemView.findViewById(R.id.image_icon);
                iv_labal = itemView.findViewById(R.id.iv_labal);
                view_last_line = itemView.findViewById(R.id.view_last_line);

            }

        }

    }
}


