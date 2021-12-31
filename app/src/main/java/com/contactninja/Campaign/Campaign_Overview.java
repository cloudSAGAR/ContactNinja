package com.contactninja.Campaign;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Campaign_Overview extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back;
    TextView save_button;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    RecyclerView item_list;
    Campaign_OverviewAdapter campaign_overviewAdapter;
   /* List<String> stringList;*/
    List<CampaignTask_overview.SequenceTask> campaignTasks=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_overview);

        IntentUI();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        StepData();
        campaign_overviewAdapter = new Campaign_OverviewAdapter(getApplicationContext());
        item_list.setAdapter(campaign_overviewAdapter);

    }

    private void IntentUI() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Next");
        item_list = findViewById(R.id.item_list);
        item_list.setLayoutManager(new LinearLayoutManager(this));
        item_list.setItemViewCacheSize(500);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
         onBackPressed();
                break;
            case R.id.save_button:
                //Add Api Call
                startActivity(new Intent(getApplicationContext(),ContectAndGroup_Actvity.class));
                break;

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),First_Step_Activity.class));
        finish();
        super.onBackPressed();
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

                    if (position == movieList.size()-1)
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
                    }

                    if (position==0)
                    {
                        movieViewHolder.line_one.setVisibility(View.INVISIBLE);
                    }
                    else {

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
                                broadcast_manu();
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
                                Intent intent=new Intent(getApplicationContext(),First_Step_Activity.class);
                                startActivity(intent);
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

    private void broadcast_manu() {
        final View mView = getLayoutInflater().inflate(R.layout.brodcaste_campaign_manu, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Campaign_Overview.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);


       LinearLayout delete_layout=bottomSheetDialog.findViewById(R.id.delete_layout);
       LinearLayout edit_layout=bottomSheetDialog.findViewById(R.id.edit_layout);
       bottomSheetDialog.show();

    }


    public void StepData() {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        int sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
        Log.e("sequence_id", String.valueOf(sequence_id));

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("id", sequence_id);
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        obj.add("data", paramObject);
        PackageManager pm = getApplicationContext().getPackageManager();
        String pkgName = getApplicationContext().getPackageName();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        retrofitCalls.Task_Data_Return(sessionManager, obj, loadingDialog,Global.getToken(sessionManager),
                Global.getVersionname(Campaign_Overview.this),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();

                if (response.body().getStatus() == 200) {

                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<CampaignTask_overview>() {
                    }.getType();
                    CampaignTask_overview user_model1 = new Gson().fromJson(headerString, listType);
                    campaign_overviewAdapter.addAll(user_model1.getSequenceTask());
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