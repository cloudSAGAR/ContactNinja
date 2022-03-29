package com.contactninja.Campaign.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Campaign.Add_Camp_Tab_Select_Activity;
import com.contactninja.Campaign.Campaign_Overview;
import com.contactninja.Model.CampaignTask;
import com.contactninja.Model.CampaignTask_overview;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Campaign_Step_Fragment extends Fragment implements View.OnClickListener {

    RecyclerView item_list;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    BottomSheetDialog bottomSheetDialog;
    int sequence_id,sequence_task_id;
    Campaign_OverviewAdapter campaign_overviewAdapter;
    List<CampaignTask_overview.SequenceTask> main_data =new ArrayList<>();
    private long mLastClickTime=0;
    String camp_flag="",start="";
    LinearLayout add_new_step;
    TextView tv_add_new_step;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_campaign__step_, container, false);
        IntentUI(view);
        loadingDialog = new LoadingDialog(getActivity());
        sessionManager = new SessionManager(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        campaign_overviewAdapter = new Campaign_OverviewAdapter(getContext());
        item_list.setAdapter(campaign_overviewAdapter);

        Global.count=1;
        //   StepData();
        return view;
    }

    private void IntentUI(View view) {
        item_list=view.findViewById(R.id.item_list);
        item_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        add_new_step=view.findViewById(R.id.add_new_step);
        tv_add_new_step =view.findViewById(R.id.tv_add_new_step);
        tv_add_new_step.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_new_step:
                SessionManager.setCampaign_type("");
                SessionManager.setCampaign_type_name("");
                SessionManager.setCampaign_minute("00");
                SessionManager.setCampaign_Day("1");

                CampaignTask campaignTask = new CampaignTask();
                //Toast.makeText(getApplicationContext(),"Step"+Data.getStepNo(),Toast.LENGTH_LONG).show();
                campaignTask.setSequenceId(sequence_id);
                campaignTask.setStepNo(0);
                List<CampaignTask> campaignTaskList = new ArrayList<>();
                campaignTaskList.add(campaignTask);
                SessionManager.setTask(getActivity(), campaignTaskList);
                SessionManager.setcamp_final_flag("final_edit");
                Intent newintent = new Intent(getActivity(), Add_Camp_Tab_Select_Activity.class);
                newintent.putExtra("flag", "new");
                startActivity(newintent);
                break;
        }
    }

    public class Campaign_OverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int LOADING = 0;
        private static final int ITEM = 1;
        private final Context context;
        private List<CampaignTask_overview.SequenceTask> movieList;
        private boolean isLoadingAdded = false;

        public Campaign_OverviewAdapter(Context context) {
            this.context = context;
            movieList = new LinkedList<>();
        }


        public void setMovieList(List<CampaignTask_overview.SequenceTask> movieList) {
            this.movieList = movieList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case ITEM:
                    View viewItem = inflater.inflate(R.layout.campign_item, parent, false);
                    viewHolder = new Campaign_OverviewAdapter.MovieViewHolder(viewItem);
                    break;
                case LOADING:
                    View viewLoading = inflater.inflate(R.layout.item_loading, parent, false);
                    viewHolder = new Campaign_OverviewAdapter.LoadingViewHolder(viewLoading);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            CampaignTask_overview.SequenceTask movieList_data = movieList.get(position);
            switch (getItemViewType(position)) {
                case ITEM:
                    Campaign_OverviewAdapter.MovieViewHolder movieViewHolder = (Campaign_OverviewAdapter.MovieViewHolder) holder;
                    movieViewHolder.add_new_step_layout.setVisibility(View.GONE);
                    movieViewHolder.tv_item_num.setText(String.valueOf(Global.count));
                    Global.count++;
                    if(position==(getItemCount()-1)){
                     movieViewHolder.cam_line.setVisibility(View.INVISIBLE);
                    }
                    if (position==0)
                    {
                        movieViewHolder.line_one.setVisibility(View.INVISIBLE);
                    movieViewHolder.run_time_layout.setVisibility(View.GONE);
                    }
                   /* else {
                        movieViewHolder.run_time_layout.setVisibility(View.VISIBLE);
                        movieViewHolder.edit_day.setEnabled(false);
                        movieViewHolder.edit_minutes.setEnabled(false);
                        movieViewHolder.line_one.setVisibility(View.VISIBLE);
                    }*/
                    if (movieList.get(position).getType().equals("SMS")) {
                        movieViewHolder.iv_email.setVisibility(View.GONE);
                        movieViewHolder.iv_message.setVisibility(View.VISIBLE);
                        movieViewHolder.tv_detail.setTypeface(null, Typeface.BOLD);

                    }
                    else {
                        movieViewHolder.iv_email.setVisibility(View.VISIBLE);
                        movieViewHolder.iv_message.setVisibility(View.GONE);
                    }
                    movieViewHolder.edit_minutes.setText(movieList.get(position).getHours().toString());
                    movieViewHolder.edit_day.setText(movieList_data.getDay().toString());
                    movieViewHolder.iv_manu.setVisibility(View.GONE);
                    movieViewHolder.iv_manu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            broadcast_manu(movieList.get(position),position);
                        }
                    });

                    if (camp_flag.equals("I") && !start.equals(""))
                    {
                        movieViewHolder.iv_manu.setVisibility(View.VISIBLE);
                        if (position == movieList.size() - 1)
                        {
                            if (movieList.size()<10)
                            {
                                movieViewHolder.add_new_step_layout.setVisibility(View.VISIBLE);
                                int num=movieList.size()+1;
                                movieViewHolder.tv_add_new_step_num.setText(String.valueOf(num));
                                movieViewHolder.cam_line.setVisibility(View.VISIBLE);
                            }
                            else {
                                movieViewHolder.cam_line.setVisibility(View.INVISIBLE);
                                movieViewHolder.add_new_step_layout.setVisibility(View.GONE);
                            }
                        }
                        else {
                            movieViewHolder.add_new_step_layout.setVisibility(View.GONE);
                        }
                    }
                    else {
                        movieViewHolder.iv_manu.setVisibility(View.GONE);
                    }

                    movieViewHolder.iv_manu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            broadcast_manu(movieList.get(position),position);
                        }
                    });

                    movieViewHolder.tv_title.setText("Step#"+movieList.get(position).getStepNo()+"("+movieList.get(position).getManageBy()+" "+movieList.get(position).getType()+")");

                    movieViewHolder.tv_detail.setText(Html.fromHtml(movieList.get(position).getContentBody().toString()));

                    movieViewHolder.tv_detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                         if (position!=0)
                         {
                             movieViewHolder.run_time_layout.setVisibility(View.VISIBLE);
                             movieViewHolder.edit_day.setEnabled(false);
                             movieViewHolder.edit_minutes.setEnabled(false);
                             movieViewHolder.line_one.setVisibility(View.VISIBLE);

                         }

                        }
                    });



                    movieViewHolder.tv_add_new_step.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();
                            SessionManager.setCampaign_type("");
                            SessionManager.setCampaign_type_name("");
                            SessionManager.setCampaign_minute("00");
                            SessionManager.setCampaign_Day("1");

                            if(position==(getItemCount()-1)){
                                CampaignTask campaignTask=new CampaignTask();
                                CampaignTask_overview.SequenceTask Data=movieList.get(position);
                                campaignTask.setId(Data.getId());
                                campaignTask.setDay(Data.getDay());
                                campaignTask.setStepNo(Data.getStepNo());
                                campaignTask.setType(Data.getType());
                                campaignTask.setPriority(Data.getPriority());
                                campaignTask.setMinute(Data.getHours());
                                campaignTask.setContentHeader(Data.getContentHeader());
                                campaignTask.setContentBody(Data.getContentBody());
                                campaignTask.setSequenceId(sequence_id);
                                campaignTask.setManageBy(Data.getManageBy());
                                List<CampaignTask> campaignTaskList=new ArrayList<>();
                                campaignTaskList.add(campaignTask);
                                SessionManager.setTask(getActivity(), campaignTaskList);
                            }
                            SessionManager.setcamp_final_flag("final_edit");
                            Intent intent=new Intent(getActivity(), Add_Camp_Tab_Select_Activity.class);
                            intent.putExtra("flag","new");
                            startActivity(intent);
                            //getActivity().finish();

                        }
                    });

                    break;

                case LOADING:
                    Campaign_OverviewAdapter.LoadingViewHolder loadingViewHolder = (Campaign_OverviewAdapter.LoadingViewHolder) holder;
                    loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return movieList == null ? 0 : movieList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == movieList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }

        public void addLoadingFooter() {
            isLoadingAdded = true;
            //add("");
        }


        public void remove_item(int position) {
            if (movieList.size()==1)
            {
                movieList.remove(position);
                notifyDataSetChanged();
                add_new_step.setVisibility(View.VISIBLE);
            }
            else {
                add_new_step.setVisibility(View.GONE);
                movieList.remove(position);
                notifyDataSetChanged();
            }

        }

        public void remove_all() {
            movieList.clear();
            notifyDataSetChanged();
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = movieList.size() - 1;
            CampaignTask_overview.SequenceTask result = getItem(position);

            if (result != null) {
                movieList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void add(CampaignTask_overview.SequenceTask movie) {
            movieList.add(movie);
            notifyItemInserted(movieList.size() - 1);
        }

        public void addAll(List<CampaignTask_overview.SequenceTask> moveResults) {
            for (CampaignTask_overview.SequenceTask result : moveResults) {
                add(result);
            }
        }

        public CampaignTask_overview.SequenceTask getItem(int position) {
            return movieList.get(position);
        }


        public class MovieViewHolder extends RecyclerView.ViewHolder {

            TextView tv_add_new_step_num,tv_item_num,tv_title,tv_detail,
                    tv_email_title,tv_email_detail,tv_add_new_step;
            View line_one,cam_line;
            LinearLayout add_new_step_layout,run_time_layout,email_layout,run_time_email_layout;
            ImageView iv_manu,iv_email_manu,iv_message,iv_email;
            EditText edit_day,edit_minutes,edit_email_day,edit_email_minutes;


            public MovieViewHolder(View itemView) {
                super(itemView);
                iv_message=itemView.findViewById(R.id.iv_message);
                iv_email=itemView.findViewById(R.id.iv_email);
                add_new_step_layout=itemView.findViewById(R.id.add_new_step);
                tv_add_new_step_num=itemView.findViewById(R.id.tv_add_new_step_num);
                tv_add_new_step=itemView.findViewById(R.id.tv_add_new_step);
                line_one=itemView.findViewById(R.id.line_one);
                tv_item_num=itemView.findViewById(R.id.tv_item_num);
                tv_title=itemView.findViewById(R.id.tv_title);
                iv_manu=itemView.findViewById(R.id.iv_manu);
                run_time_layout=itemView.findViewById(R.id.run_time_layout);
                tv_detail=itemView.findViewById(R.id.tv_detail);
                edit_day=itemView.findViewById(R.id.edit_day);
                edit_minutes=itemView.findViewById(R.id.edit_minutes);
                email_layout=itemView.findViewById(R.id.email_layout);
                tv_email_title=itemView.findViewById(R.id.tv_email_title);
                iv_email_manu=itemView.findViewById(R.id.iv_email_manu);
                tv_email_detail=itemView.findViewById(R.id.tv_email_detail);
                run_time_email_layout=itemView.findViewById(R.id.run_time_email_layout);
                edit_email_day=itemView.findViewById(R.id.edit_email_day);
                edit_email_minutes=itemView.findViewById(R.id.edit_email_minutes);
                cam_line=itemView.findViewById(R.id.cam_line);
            }
        }

        public class LoadingViewHolder extends RecyclerView.ViewHolder {

            private final ProgressBar progressBar;

            public LoadingViewHolder(View itemView) {
                super(itemView);
                progressBar = itemView.findViewById(R.id.idPBLoading);

            }
        }

    }
    private void broadcast_manu(CampaignTask_overview.SequenceTask sequenceTask, int position) {
        final View mView = getLayoutInflater().inflate(R.layout.brodcaste_campaign_manu, null);
        bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);


        LinearLayout delete_layout=bottomSheetDialog.findViewById(R.id.delete_layout);
        LinearLayout edit_layout=bottomSheetDialog.findViewById(R.id.edit_layout);

        delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveTaskData(sequenceTask.getId(),"SEQUENCE/TASK REMOVED",position);
            }
        });
        edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (sequenceTask.getType().equals("EMAIL"))
                {
                    if (SessionManager.getTask(getActivity()).size()!=0)
                    {
                        sequence_id = SessionManager.getTask(getActivity()).get(0).getSequenceId();
                    }
                    else {
                        Intent getintent=getActivity().getIntent();
                        Bundle bundle=getintent.getExtras();
                        sequence_id=bundle.getInt("sequence_id");
                    }
                //    Log.e("Sequence is is", String.valueOf(sequence_id));

                    Intent intent=new Intent(getActivity(), Add_Camp_Tab_Select_Activity.class);
                    intent.putExtra("flag","edit");
                    intent.putExtra("body",sequenceTask.getContentBody());
                    intent.putExtra("day",sequenceTask.getDay());
                    intent.putExtra("manage_by",sequenceTask.getManageBy());
                    intent.putExtra("seq_task_id",sequenceTask.getId());
                    intent.putExtra("sequence_id",sequence_id);
                    intent.putExtra("type",sequenceTask.getType());
                    intent.putExtra("minute",sequenceTask.getHours());
                    intent.putExtra("header",sequenceTask.getContentHeader());
                    intent.putExtra("step",sequenceTask.getStepNo());
                    intent.putExtra("from_ac",sequenceTask.getMail_module());
                    intent.putExtra("from_ac_id",sequenceTask.getSent_tbl_id());
                    startActivity(intent);
                    // startActivity(new Intent(getActivity(),First_Step_Activity.class));
                    SessionManager.setCampaign_Day(String.valueOf(sequenceTask.getDay()));
                    SessionManager.setCampaign_minute(String.valueOf(sequenceTask.getHours()));
                    SessionManager.setCampaign_type(String.valueOf(sequenceTask.getType()));
                    SessionManager.setCampaign_type_name(String.valueOf(sequenceTask.getManageBy()));

                    //finish();
                    bottomSheetDialog.cancel();

                }
                else {

                    Log.e("sequence id", String.valueOf(sequence_id));

                    Log.e("Sequence is is", String.valueOf(sequence_id));
                    if (SessionManager.getTask(getActivity()).size()!=0)
                    {
                        sequence_id = SessionManager.getTask(getActivity()).get(0).getSequenceId();
                    }
                    else {
                        Intent getintent=getActivity().getIntent();
                        Bundle bundle=getintent.getExtras();
                        sequence_id=bundle.getInt("sequence_id");
                    }
                    Intent intent=new Intent(getActivity(), Add_Camp_Tab_Select_Activity.class);
                    intent.putExtra("flag","edit");
                    intent.putExtra("body",sequenceTask.getContentBody());
                    intent.putExtra("day",sequenceTask.getDay());
                    intent.putExtra("manage_by",sequenceTask.getManageBy());
                    intent.putExtra("seq_task_id",sequenceTask.getId());
                    intent.putExtra("sequence_id",sequence_id);
                    intent.putExtra("type",sequenceTask.getType());
                    intent.putExtra("minute",sequenceTask.getHours());
                    intent.putExtra("step",sequenceTask.getStepNo());
                    intent.putExtra("from_ac",sequenceTask.getMail_module());
                    intent.putExtra("from_ac_id",sequenceTask.getSent_tbl_id());
                    startActivity(intent);
                    SessionManager.setCampaign_Day(String.valueOf(sequenceTask.getDay()));
                    SessionManager.setCampaign_minute(String.valueOf(sequenceTask.getHours()));
                    SessionManager.setCampaign_type(String.valueOf(sequenceTask.getType()));
                    SessionManager.setCampaign_type_name(String.valueOf(sequenceTask.getManageBy()));

                    bottomSheetDialog.cancel();
                }
                bottomSheetDialog.cancel();

            }
        });
        bottomSheetDialog.show();

    }

    public void RemoveTaskData(Integer task_id, String d, int position) {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


        if (SessionManager.getTask(getActivity()).size()!=0)
        {
            sequence_id = SessionManager.getTask(getActivity()).get(0).getSequenceId();
        }
        else {
            Intent getintent=getActivity().getIntent();
            Bundle bundle=getintent.getExtras();
            sequence_id=bundle.getInt("sequence_id");
        }
        Log.e("sequence_id", String.valueOf(sequence_id));
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("seq_task_id", task_id);
        paramObject.addProperty("sequence_id", sequence_id);
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("status", d);
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);
        retrofitCalls.Task_store(sessionManager, obj, loadingDialog,Global.getToken(sessionManager),
                Global.getVersionname(getActivity()),Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();

                        if (response.body().getHttp_status() == 200) {
                            Global.count=1;
                            campaign_overviewAdapter.remove_item(position);
                            bottomSheetDialog.cancel();

                        } else {

                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }


    public void StepData() {
       // loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


        if (SessionManager.getTask(getActivity()).size() != 0) {
            sequence_id = SessionManager.getTask(getActivity()).get(0).getSequenceId();
        } else {
            Intent getintent = getActivity().getIntent();
            Bundle bundle = getintent.getExtras();
            sequence_id = bundle.getInt("sequence_id");
        }
        Log.e("sequence_id", String.valueOf(sequence_id));

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("id", sequence_id);
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("is_preview","1");

        obj.add("data", paramObject);
        PackageManager pm = getActivity().getPackageManager();
        String pkgName = getActivity().getPackageName();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        retrofitCalls.Task_Data_Return(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();

                        if (response.body().getHttp_status() == 200) {

                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            Type listType = new TypeToken<CampaignTask_overview>() {
                            }.getType();

                            CampaignTask_overview user_model1 = new Gson().fromJson(headerString, listType);
                            //Log.e("User Model",new Gson().toJson(user_model1));
                            SessionManager.setCampaign_data(user_model1);

                            //CampaignTask_overview user_model1=SessionManager.getCampaign_data(getContext());
                             camp_flag= user_model1.get0().getStatus();
                             start=user_model1.get0().getStarted_on();
                             main_data.clear();
                             campaign_overviewAdapter.remove_all();
                             main_data=user_model1.getSequenceTask();
                             campaign_overviewAdapter.addAll(main_data);

                            main_data=  user_model1.getSequenceTask();

                            if (user_model1.get0().getStatus().equals("I") && !user_model1.get0().getStarted_on().equals(""))
                            {
                                if (main_data.size()==0)
                                {
                                    add_new_step.setVisibility(View.VISIBLE);
                                    item_list.setVisibility(View.GONE);
                                }
                                else {
                                    item_list.setVisibility(View.VISIBLE);
                                    add_new_step.setVisibility(View.GONE);
                                }
                            }

                        } else {
                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            // Global.Messageshow(getApplicationContext(), mMainLayout, headerString, false);

                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }

    public void onResume() {
        loadingDialog.cancelLoading();
        Global.count=1;
        StepData();
        super.onResume();
    }
}