package com.contactninja.Campaign;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Model.CampaignTask;
import com.contactninja.Model.CampaignTask_overview;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
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

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak")
public class Campaign_Overview extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    Toolbar toolbar;
    ImageView iv_back;
    TextView save_button;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    RecyclerView item_list;
    int seq_prospect_count=0;
    Campaign_OverviewAdapter campaign_overviewAdapter;
   /* List<String> stringList;*/
    List<CampaignTask_overview.SequenceTask> campaignTasks=new ArrayList<>();
    int sequence_id,sequence_task_id;
    String sequence_Name="";

    BottomSheetDialog bottomSheetDialog;
    private BroadcastReceiver mNetworkReceiver;
    ConstraintLayout mMainLayout;
    ImageView iv_toolbar_manu;
    List<CampaignTask_overview.SequenceTask> main_data=new ArrayList<>();
    private long mLastClickTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_overview);
        mNetworkReceiver = new ConnectivityReceiver();

        IntentUI();
        Global.count=1;
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);

        toolbar.inflateMenu(R.menu.option_menu1);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
       // StepData();
        campaign_overviewAdapter = new Campaign_OverviewAdapter(getApplicationContext());
        item_list.setAdapter(campaign_overviewAdapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(final MenuItem item) {

        //  Toast.makeText(getApplicationContext(), "Manu Clcik", Toast.LENGTH_LONG).show();
        //Log.e("Option Manu is Select", "Yes");

        switch (item.getItemId()) {
            case R.id.mv_save:
                //startActivity(new Intent(getApplicationContext(), Campaign_List_Activity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void IntentUI() {
        iv_toolbar_manu = findViewById(R.id.iv_toolbar_manu);
        iv_toolbar_manu.setOnClickListener(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.option_menu);
        setSupportActionBar(toolbar);
        mMainLayout = findViewById(R.id.mMainLayout);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Add prospects");
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
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (SessionManager.getTask(getApplicationContext()).size()!=0)
                {
                    sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
                }
                else {
                    Intent getintent=getIntent();
                    Bundle bundle=getintent.getExtras();
                    sequence_id=bundle.getInt("sequence_id");
                }
                SessionManager.setContect_flag("");
                SessionManager.setgroup_broadcste(getApplicationContext(),new ArrayList<>());
                SessionManager.setGroupList(this,new ArrayList<>());

               if (seq_prospect_count==0)
               {
                   Intent intent=new Intent(getApplicationContext(),ContectAndGroup_Actvity.class);
                   intent.putExtra("sequence_id",sequence_id);
                   intent.putExtra("seq_task_id",sequence_task_id);
                   intent.putExtra("sequence_Name",sequence_Name);
                   startActivity(intent);
                   finish();
               }
               else {
                   SessionManager.setCampign_flag("read");
                   Intent intent = new Intent(getApplicationContext(), Campaign_Preview.class);
                   intent.putExtra("sequence_id", sequence_id);
                   /*intent.putExtra("seq_task_id",seq_task_id);*/
                   startActivity(intent);
                   finish();
               }

                //startActivity(new Intent(getApplicationContext(),ContectAndGroup_Actvity.class));
                break;

        }
    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(getApplicationContext(),First_Step_Activity.class));
        finish();
        super.onBackPressed();
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Campaign_Overview.this, mMainLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
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

                    if (position == movieList.size() - 1)
                    {
                        movieViewHolder.add_new_step_layout.setVisibility(View.VISIBLE);
                        int num=movieList.size()+1;
                        movieViewHolder.tv_add_new_step_num.setText(String.valueOf(num));
                    }
                    else {
                        movieViewHolder.add_new_step_layout.setVisibility(View.GONE);
                    }
                    movieViewHolder.tv_item_num.setText(String.valueOf(Global.count));
                    Global.count++;

                    if (position==0)
                    {
                        movieViewHolder.line_one.setVisibility(View.INVISIBLE);
                        movieViewHolder.run_time_layout.setVisibility(View.GONE);
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
                                broadcast_manu(movieList.get(position),position);
                            }
                        });

                        movieViewHolder.tv_title.setText("Step#"+movieList.get(position).getStepNo()+"("+movieList.get(position).getManageBy()+" "+movieList.get(position).getType()+")");

                        movieViewHolder.tv_detail.setText(movieList.get(position).getContentBody().toString());

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
                                    //Toast.makeText(getApplicationContext(),"Step"+Data.getStepNo(),Toast.LENGTH_LONG).show();

                                    campaignTask.setId(Data.getId());
                                    campaignTask.setDay(Data.getDay());
                                    campaignTask.setStepNo(Data.getStepNo());
                                    campaignTask.setType(Data.getType());
                                    campaignTask.setPriority(Data.getPriority());
                                    campaignTask.setMinute(Data.getMinute());
                                    campaignTask.setContentHeader(Data.getContentHeader());
                                    campaignTask.setContentBody(Data.getContentBody());
                                    campaignTask.setSequenceId(sequence_id);
                                    campaignTask.setManageBy(Data.getManageBy());
                                    List<CampaignTask> campaignTaskList=new ArrayList<>();
                                    campaignTaskList.add(campaignTask);
                                    SessionManager.setTask(getApplicationContext(), campaignTaskList);
                                }
                                Intent intent=new Intent(getApplicationContext(), Add_Camp_Tab_Select_Activity.class);
                                intent.putExtra("flag","new");
                                startActivity(intent);
                                finish();

                            }
                        });

                      /*  movieViewHolder.tv_detail.setOnClickListener(new View.OnClickListener() {
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

        public void remove_all() {
            movieList.clear();
            notifyDataSetChanged();
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

        public void removeall()
        {
            movieList.clear();
            notifyDataSetChanged();
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
        bottomSheetDialog = new BottomSheetDialog(Campaign_Overview.this, R.style.CoffeeDialog);
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
                    if (SessionManager.getTask(getApplicationContext()).size()!=0)
                    {
                        sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
                    }
                    else {
                        Intent getintent=getIntent();
                        Bundle bundle=getintent.getExtras();
                        sequence_id=bundle.getInt("sequence_id");
                    }
                    Log.e("Sequence is is", String.valueOf(sequence_id));

                    Intent intent=new Intent(getApplicationContext(), Add_Camp_Tab_Select_Activity.class);
                    intent.putExtra("flag","edit");
                    intent.putExtra("body",sequenceTask.getContentBody());
                    intent.putExtra("day",sequenceTask.getDay());
                    intent.putExtra("manage_by",sequenceTask.getManageBy());
                    intent.putExtra("seq_task_id",sequenceTask.getId());
                    intent.putExtra("sequence_id",sequence_id);
                    intent.putExtra("type",sequenceTask.getType());
                    intent.putExtra("minute",sequenceTask.getMinute());
                    intent.putExtra("header",sequenceTask.getContentHeader());
                    intent.putExtra("step",sequenceTask.getStepNo());
                    intent.putExtra("from_ac",sequenceTask.getMail_module());
                    intent.putExtra("from_ac_id",sequenceTask.getSent_tbl_id());
                    startActivity(intent);
                    // startActivity(new Intent(getActivity(),First_Step_Activity.class));
                    SessionManager.setCampaign_Day(String.valueOf(sequenceTask.getDay()));
                    SessionManager.setCampaign_minute(String.valueOf(sequenceTask.getMinute()));
                    SessionManager.setCampaign_type(String.valueOf(sequenceTask.getType()));
                    SessionManager.setCampaign_type_name(String.valueOf(sequenceTask.getManageBy()));

                 //finish();
                    bottomSheetDialog.cancel();

                }
                else {

                    Log.e("sequence id", String.valueOf(sequence_id));

                    Log.e("Sequence is is", String.valueOf(sequence_id));
                    if (SessionManager.getTask(getApplicationContext()).size()!=0)
                    {
                        sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
                    }
                    else {
                        Intent getintent=getIntent();
                        Bundle bundle=getintent.getExtras();
                        sequence_id=bundle.getInt("sequence_id");
                    }
                    Intent intent=new Intent(getApplicationContext(), Add_Camp_Tab_Select_Activity.class);
                    intent.putExtra("flag","edit");
                    intent.putExtra("body",sequenceTask.getContentBody());
                    intent.putExtra("day",sequenceTask.getDay());
                    intent.putExtra("manage_by",sequenceTask.getManageBy());
                    intent.putExtra("seq_task_id",sequenceTask.getId());
                    intent.putExtra("sequence_id",sequence_id);
                    intent.putExtra("type",sequenceTask.getType());
                    intent.putExtra("minute",sequenceTask.getMinute());
                    intent.putExtra("step",sequenceTask.getStepNo());
                    intent.putExtra("from_ac",sequenceTask.getMail_module());
                    intent.putExtra("from_ac_id",sequenceTask.getSent_tbl_id());
                    startActivity(intent);
                    SessionManager.setCampaign_Day(String.valueOf(sequenceTask.getDay()));
                    SessionManager.setCampaign_minute(String.valueOf(sequenceTask.getMinute()));
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
        SignResponseModel user_data = SessionManager.getGetUserdata(this);

        if (SessionManager.getTask(getApplicationContext()).size()!=0)
        {
            sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
        }
        else {
            Intent getintent=getIntent();
            Bundle bundle=getintent.getExtras();
            sequence_id=bundle.getInt("sequence_id");
        }
        Log.e("sequence_id", String.valueOf(sequence_id));
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("seq_task_id", task_id);
        paramObject.addProperty("sequence_id", sequence_id);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("status", d);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        obj.add("data", paramObject);
        retrofitCalls.Task_store(sessionManager, obj, loadingDialog,Global.getToken(sessionManager),
                Global.getVersionname(Campaign_Overview.this),Global.Device, new RetrofitCallback() {
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
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        if (SessionManager.getTask(getApplicationContext()).size()!=0)
        {
            sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
        }
        else {
            Intent getintent=getIntent();
            Bundle bundle=getintent.getExtras();
            sequence_id=bundle.getInt("sequence_id");
        }
        Log.e("sequence_id", String.valueOf(sequence_id));

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("id", sequence_id);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
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

                Gson gson = new Gson();
                String headerString = gson.toJson(response.body().getData());
                if (response.body().getHttp_status() == 200) {

                    Type listType = new TypeToken<CampaignTask_overview>() {
                    }.getType();

                    CampaignTask_overview user_model1 = new Gson().fromJson(headerString, listType);
                    if(user_model1.getSequenceProspects().size()!=0){
                        if (user_model1.getSeqProspectCount()==null)
                        {
                            seq_prospect_count=0;

                        }
                        else {
                            seq_prospect_count=user_model1.getSeqProspectCount().getTotal();

                        }
                    }
                    main_data.clear();
                    campaign_overviewAdapter.removeall();
                    main_data=  user_model1.getSequenceTask();
                    sequence_task_id=user_model1.getSequenceTask().get(0).getId();
                    sequence_Name=user_model1.get0().getSeqName();
                    campaign_overviewAdapter.addAll(main_data);
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }
    public void StepData1() {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);


        if (SessionManager.getTask(getApplicationContext()).size()!=0)
        {
            sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
        }
        else {
            Intent getintent=getIntent();
            Bundle bundle=getintent.getExtras();
            sequence_id=bundle.getInt("sequence_id");
        }
        Log.e("sequence_id", String.valueOf(sequence_id));

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("id", sequence_id);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
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

                        Gson gson = new Gson();
                        String headerString = gson.toJson(response.body().getData());
                        if (response.body().getHttp_status() == 200) {

                            Type listType = new TypeToken<CampaignTask_overview>() {
                            }.getType();

                            CampaignTask_overview user_model1 = new Gson().fromJson(headerString, listType);
                            if (user_model1.getSeqProspectCount()==null)
                            {
                                seq_prospect_count=0;

                            }
                            else {
                                seq_prospect_count=user_model1.getSeqProspectCount().getTotal();
                            }
                            sequence_task_id=user_model1.getSequenceTask().get(0).getId();
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