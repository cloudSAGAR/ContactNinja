package com.contactninja.Campaign.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

import com.contactninja.Campaign.Automated_Email_Activity;
import com.contactninja.Campaign.First_Step_Activity;
import com.contactninja.Campaign.First_Step_Start_Activity;
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
import java.util.LinkedList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Campaign_Step_Fragment extends Fragment {

    RecyclerView item_list;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    BottomSheetDialog bottomSheetDialog;
    int sequence_id,sequence_task_id;
    Campaign_OverviewAdapter campaign_overviewAdapter;

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
        StepData();
        return view;
    }

    private void IntentUI(View view) {
        item_list=view.findViewById(R.id.item_list);
        item_list.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                    View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
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
                  /*  if (position == movieList.size() - 1)
                    {
                        movieViewHolder.add_new_step_layout.setVisibility(View.VISIBLE);
                        int num=movieList.size()+1;
                        movieViewHolder.tv_add_new_step_num.setText(String.valueOf(num));
                        movieViewHolder.tv_item_num.setText(String.valueOf(Global.count));
                        Global.count++;
                    }
                    else {

                        movieViewHolder.add_new_step_layout.setVisibility(View.GONE);
                        movieViewHolder.tv_item_num.setText(String.valueOf(Global.count));
                        Global.count++;
                    }*/

                    if (position==0)
                    {
                        movieViewHolder.line_one.setVisibility(View.INVISIBLE);
                    }
                    else {
                        movieViewHolder.run_time_layout.setVisibility(View.VISIBLE);
                        movieViewHolder.edit_day.setEnabled(false);
                        movieViewHolder.edit_minutes.setEnabled(false);
                        movieViewHolder.line_one.setVisibility(View.VISIBLE);
                    }



                    if (movieList.get(position).getType().equals("SMS")) {
                        movieViewHolder.iv_email.setVisibility(View.GONE);
                        movieViewHolder.iv_message.setVisibility(View.VISIBLE);
                    }
                    else {
                        movieViewHolder.iv_email.setVisibility(View.VISIBLE);
                        movieViewHolder.iv_message.setVisibility(View.GONE);
                    }
                    movieViewHolder.edit_minutes.setText(movieList.get(position).getMinute().toString());
                    movieViewHolder.edit_day.setText(movieList_data.getDay().toString());
                    movieViewHolder.iv_manu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            broadcast_manu(movieList.get(position),position);
                        }
                    });

                    movieViewHolder.tv_title.setText("Step#"+movieList.get(position).getStepNo()+"("+movieList.get(position).getManageBy()+" "+movieList.get(position).getType()+")");

                    movieViewHolder.tv_detail.setText(movieList.get(position).getContentBody().toString());

                    movieViewHolder.tv_add_new_step.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SessionManager.setCampaign_type("");
                            SessionManager.setCampaign_type_name("");
                            SessionManager.setCampaign_minute("00");
                            SessionManager.setCampaign_Day("1");
                            Intent intent=new Intent(getContext(), First_Step_Activity.class);
                            startActivity(intent);
                        }
                    });

                   /* movieViewHolder.tv_detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (movieViewHolder.run_time_layout.getVisibility()==View.VISIBLE)
                            {
                                movieViewHolder.run_time_layout.setVisibility(View.GONE);
                            }
                            else {
                                movieViewHolder.run_time_layout.setVisibility(View.VISIBLE);
                            }

                        }
                    });
*/

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
            movieList.remove(position);
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
            View line_one;
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
                RemoveTaskData(sequenceTask.getId(),"D",position);
            }
        });
        edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    Intent new_task=new Intent(getActivity(), Automated_Email_Activity.class);
                    new_task.putExtra("flag","edit");
                    new_task.putExtra("body",sequenceTask.getContentBody());
                    new_task.putExtra("day",sequenceTask.getDay());
                    new_task.putExtra("manage_by",sequenceTask.getManageBy());
                    new_task.putExtra("seq_task_id",String.valueOf(sequenceTask.getId()));
                    new_task.putExtra("sequence_id",String.valueOf(sequence_id));
                    new_task.putExtra("type",sequenceTask.getType());
                    new_task.putExtra("minute",sequenceTask.getMinute());
                    new_task.putExtra("header",sequenceTask.getContentHeader());
                    new_task.putExtra("step",sequenceTask.getStepNo());
                    startActivity(new_task);
                    getActivity().finish();
                    bottomSheetDialog.cancel();

                }
                else {
                    if (SessionManager.getTask(getActivity()).size()!=0)
                    {
                        sequence_id = SessionManager.getTask(getActivity()).get(0).getSequenceId();
                    }
                    else {
                        Intent getintent=getActivity().getIntent();
                        Bundle bundle=getintent.getExtras();
                        sequence_id=bundle.getInt("sequence_id");
                    }
                    Log.e("sequence id", String.valueOf(sequence_id));
                    Intent new_task=new Intent(getActivity(), First_Step_Start_Activity.class);
                    new_task.putExtra("flag","edit");
                    new_task.putExtra("body",sequenceTask.getContentBody());
                    new_task.putExtra("day",sequenceTask.getDay());
                    new_task.putExtra("manage_by",sequenceTask.getManageBy());
                    new_task.putExtra("seq_task_id",String.valueOf(sequenceTask.getId()));
                    new_task.putExtra("sequence_id",String.valueOf(sequence_id));
                    new_task.putExtra("type",sequenceTask.getType());
                    new_task.putExtra("minute",sequenceTask.getMinute());
                    new_task.putExtra("step",sequenceTask.getStepNo());
                    startActivity(new_task);
                    getActivity().finish();
                    bottomSheetDialog.cancel();
                }

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

                        if (response.body().getStatus() == 200) {
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

                        if (response.body().getStatus() == 200) {

                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            Type listType = new TypeToken<CampaignTask_overview>() {
                            }.getType();

                            CampaignTask_overview user_model1 = new Gson().fromJson(headerString, listType);
                            //Log.e("User Model",new Gson().toJson(user_model1));
                            SessionManager.setCampaign_data(user_model1);

                            //CampaignTask_overview user_model1=SessionManager.getCampaign_data(getContext());
                            campaign_overviewAdapter.addAll(user_model1.getSequenceTask());

                            //  Log.e("Email Task",user_model1.getSequenceTask().get(0).getActiveTaskEmail().toString());
                            // Log.e("SMS",user_model1.getSequenceTask().get(0).getActiveTaskContactNumber().toString());

                            //  tv_email.setText(user_model1.getSequenceTask().get(0).getActiveTaskEmail().toString());
                            //tv_sms.setText(user_model1.getSequenceTask().get(0).getActiveTaskContactNumber().toString());




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


}