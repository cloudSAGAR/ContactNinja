package com.contactninja.Main_Broadcast.List_And_show;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.contactninja.MainActivity;
import com.contactninja.Main_Broadcast.Text_And_Email_Auto_Manual_Broadcast;
import com.contactninja.Model.BroadcastActivityListModel;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.PaginationListener;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class List_Broadcast_activity extends AppCompatActivity implements View.OnClickListener,
        ConnectivityReceiver.ConnectivityReceiverListener, SwipeRefreshLayout.OnRefreshListener {
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back, iv_cancle_search_icon;
    TextView save_button;

    LinearLayout mMainLayout,add_new_contect_layout;
    LinearLayout demo_layout,  lay_no_list;
    RelativeLayout lay_mainlayout;
    RecyclerView rv_Broadcast_list;
    SwipeRefreshLayout swipeToRefresh;
    EditText ev_search;
    ListItemAdepter broadCast_Adepter;
    List<BroadcastActivityListModel.Broadcast> broadcastActivityListModels = new ArrayList<>();
    int perPage = 20;
    private BroadcastReceiver mNetworkReceiver;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;


    private long mLastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_broadcast);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        IntentUI();


        ev_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    iv_cancle_search_icon.setVisibility(View.VISIBLE);
                    Global.hideKeyboard(List_Broadcast_activity.this);
                    onResume();
                    return true;
                }
                return false;
            }
        });

        rv_Broadcast_list.setLayoutManager(layoutManager);
        broadCast_Adepter = new ListItemAdepter(List_Broadcast_activity.this, new ArrayList<>());
        rv_Broadcast_list.setAdapter(broadCast_Adepter);
        rv_Broadcast_list.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                iv_cancle_search_icon.setVisibility(View.GONE);
                ev_search.setText("");
                isLoading = true;
                currentPage++;
                try {
                    if (Global.isNetworkAvailable(List_Broadcast_activity.this, MainActivity.mMainLayout)) {
                        Broadcast_list();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        demo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                SessionManager.setCampaign_Day("00");
                SessionManager.setCampaign_minute("00");
                SessionManager.setCampaign_type("");
                SessionManager.setCampaign_type_name("");
                SessionManager.setEmail_screen_name("");
                Intent intent1 = new Intent(getApplicationContext(), Text_And_Email_Auto_Manual_Broadcast.class);
                intent1.putExtra("flag", "add");
                startActivity(intent1);//  finish();
            }
        });
        add_new_contect_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                SessionManager.setCampaign_Day("00");
                SessionManager.setCampaign_minute("00");
                SessionManager.setCampaign_type("");
                SessionManager.setCampaign_type_name("");
                SessionManager.setEmail_screen_name("");
                Intent intent1 = new Intent(getApplicationContext(), Text_And_Email_Auto_Manual_Broadcast.class);
                intent1.putExtra("flag", "add");
                startActivity(intent1);//  finish();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        currentPage = PAGE_START;
        isLastPage = false;
        broadcastActivityListModels.clear();
        broadCast_Adepter.clear();
        try {
            if (Global.isNetworkAvailable(List_Broadcast_activity.this, MainActivity.mMainLayout)) {
                if (!swipeToRefresh.isRefreshing()) {
                    loadingDialog.showLoadingDialog();
                }
                Broadcast_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void IntentUI() {

        iv_cancle_search_icon = findViewById(R.id.iv_cancle_search_icon);
        iv_cancle_search_icon.setOnClickListener(this);
        lay_no_list = findViewById(R.id.lay_no_list);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.GONE);

        iv_back.setOnClickListener(this);
        save_button.setText("Next");
        mMainLayout = findViewById(R.id.mMainLayout);


        lay_mainlayout = findViewById(R.id.lay_mainlayout);
        demo_layout = findViewById(R.id.demo_layout);
        mMainLayout = findViewById(R.id.mMainLayout);
        rv_Broadcast_list = findViewById(R.id.rv_Broadcast_list);
        add_new_contect_layout = findViewById(R.id.add_new_contect_layout);

        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(this);
        ev_search = findViewById(R.id.ev_search);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(List_Broadcast_activity.this, mMainLayout);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cancle_search_icon:
                ev_search.setText("");
                iv_cancle_search_icon.setVisibility(View.GONE);
                onResume();
                break;
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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    void Broadcast_list() throws JSONException {

        SignResponseModel signResponseModel = SessionManager.getGetUserdata(this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("q", ev_search.getText().toString());
        paramObject.addProperty("orderBy", "created_at");
        paramObject.addProperty("order", "DESC");
        paramObject.addProperty("perPage", perPage);
        paramObject.addProperty("page", currentPage);
        obj.add("data", paramObject);
        retrofitCalls.Broadcast_Activiy_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                swipeToRefresh.setRefreshing(false);
                if (response.body().getHttp_status() == 200) {


                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    if (response.body().getHttp_status() == 200) {


                        Type listType = new TypeToken<BroadcastActivityListModel>() {
                        }.getType();
                        BroadcastActivityListModel emailActivityListModel = new Gson().fromJson(headerString, listType);
                        broadcastActivityListModels = emailActivityListModel.getBroadcast();
                        if (!ev_search.getText().toString().equals("")) {
                            if (broadcastActivityListModels.size() == 0) {
                                rv_Broadcast_list.setVisibility(View.GONE);
                                lay_no_list.setVisibility(View.VISIBLE);
                            } else {
                                lay_no_list.setVisibility(View.GONE);
                                rv_Broadcast_list.setVisibility(View.VISIBLE);
                            }

                        } else {

                            if (broadcastActivityListModels.size() == 0) {
                                lay_no_list.setVisibility(View.GONE);
                                lay_mainlayout.setVisibility(View.GONE);
                                demo_layout.setVisibility(View.VISIBLE);
                            } else {

                                lay_no_list.setVisibility(View.GONE);
                                demo_layout.setVisibility(View.GONE);
                                rv_Broadcast_list.setVisibility(View.VISIBLE);
                                lay_mainlayout.setVisibility(View.VISIBLE);
                            }
                        }


                        if (currentPage != PAGE_START) broadCast_Adepter.removeLoading();
                        broadCast_Adepter.addItems(broadcastActivityListModels);
                        // check weather is last page or not
                        if (emailActivityListModel.getTotal() > broadCast_Adepter.getItemCount()) {
                            broadCast_Adepter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;

                    } else {
                        // Global.Messageshow(getApplicationContext(), mMainLayout, headerString, false);
                        demo_layout.setVisibility(View.VISIBLE);
                    }


                } else {
                    if (!ev_search.getText().toString().equals("")) {
                        rv_Broadcast_list.setVisibility(View.GONE);
                        lay_no_list.setVisibility(View.VISIBLE);
                    } else {
                        lay_no_list.setVisibility(View.GONE);
                        lay_mainlayout.setVisibility(View.GONE);
                        demo_layout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                swipeToRefresh.setRefreshing(false);
                loadingDialog.cancelLoading();
                demo_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void onRefresh() {
        refresf_api();
    }

    private void refresf_api() {
        ev_search.setText("");
        iv_cancle_search_icon.setVisibility(View.GONE);
        currentPage = PAGE_START;
        isLastPage = false;
        broadcastActivityListModels.clear();
        broadCast_Adepter.clear();
        try {
            if (Global.isNetworkAvailable(List_Broadcast_activity.this, MainActivity.mMainLayout)) {
                if (!swipeToRefresh.isRefreshing()) {
                    loadingDialog.showLoadingDialog();
                }
                Broadcast_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ListItemAdepter extends RecyclerView.Adapter<ListItemAdepter.viewData> {
        private static final int VIEW_TYPE_LOADING = 0;
        private static final int VIEW_TYPE_NORMAL = 1;
        public Context mCtx;
        List<BroadcastActivityListModel.Broadcast> broadcastActivityListModels;
        private boolean isLoaderVisible = false;


        public ListItemAdepter(Context context, List<BroadcastActivityListModel.Broadcast> broadcastActivityListModels) {
            this.mCtx = context;
            this.broadcastActivityListModels = broadcastActivityListModels;
        }

        public void filterList(ArrayList<BroadcastActivityListModel.Broadcast> filterllist) {
            // below line is to add our filtered
            // list in our course array list.
            broadcastActivityListModels = filterllist;
            // below line is to notify our adapter
            // as change in recycler view data.
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ListItemAdepter.viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            switch (viewType) {
                case VIEW_TYPE_NORMAL:
                    return new ListItemAdepter.viewData(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brodadcastlist, parent, false));
                case VIEW_TYPE_LOADING:
                    return new ListItemAdepter.ProgressHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
                default:
                    return null;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isLoaderVisible) {
                return position == broadcastActivityListModels.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
            } else {
                return VIEW_TYPE_NORMAL;
            }
        }

        public void addItems(List<BroadcastActivityListModel.Broadcast> postItems) {
            broadcastActivityListModels.addAll(postItems);
            notifyDataSetChanged();
        }

        public void addLoading() {
            isLoaderVisible = true;
            broadcastActivityListModels.add(new BroadcastActivityListModel.Broadcast());
            notifyItemInserted(broadcastActivityListModels.size() - 1);
        }

        public void removeLoading() {
            isLoaderVisible = false;
            int position = broadcastActivityListModels.size() - 1;
            BroadcastActivityListModel.Broadcast item = getItem(position);
            if (item != null) {
                broadcastActivityListModels.remove(position);
                notifyItemRemoved(position);
            }
        }

        BroadcastActivityListModel.Broadcast getItem(int position) {
            return broadcastActivityListModels.get(position);
        }

        public void clear() {
            broadcastActivityListModels.clear();
            notifyDataSetChanged();
        }

        @SuppressLint("LogConditional")
        @Override
        public void onBindViewHolder(@NonNull ListItemAdepter.viewData holder, int position) {
            BroadcastActivityListModel.Broadcast item = broadcastActivityListModels.get(position);

            try {
                if (item.getType().equals("SMS")) {
                    holder.image_icon.setImageResource(R.drawable.ic_message_tab);
                } else {
                    holder.image_icon.setImageResource(R.drawable.ic_email);
                }


                setImage(item, holder);


                holder.layout_contec.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        switch (item.getStatus()) {
                            case "A":
                                showAlertDialogButtonClicked(item, 1);
                                break;
                            case "I":
                                if (item.getFirstActivated() != null && !item.getFirstActivated().equals("")) {
                                    showAlertDialogButtonClicked(item, 0);
                                } else {
                                    showAlertDialogButtonClicked(item, 3);
                                }
                                break;
                        }
                        return true;
                    }
                });

                switch (item.getStatus()) {
                    case "I":
                        holder.iv_hold.setVisibility(View.VISIBLE);
                        holder.tv_status.setText("Inactive");
                        holder.tv_status.setTextColor(getResources().getColor(R.color.red));

                        break;
                    case "P":
                        holder.iv_puse_icon.setVisibility(View.VISIBLE);
                        holder.tv_status.setText("Paused");
                        holder.tv_status.setTextColor(getResources().getColor(R.color.text_green));

                        break;
                    case "A":
                        holder.iv_play_icon.setVisibility(View.VISIBLE);
                        holder.tv_status.setText("Active");
                        holder.tv_status.setTextColor(getResources().getColor(R.color.tv_push_color));

                        break;
                }
                holder.iv_hold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showAlertDialogButtonClicked(item, 3);

                    }
                });

                holder.iv_play_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showAlertDialogButtonClicked(item, 1);

                    }
                });

                holder.iv_puse_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        showAlertDialogButtonClicked(item, 0);

                    }
                });
                String conactname = item.getBroadcastName();
                holder.tv_username.setText(conactname);
                switch (item.getRecurringType()) {
                    case "D":
                        holder.tv_task_time.setText("Daily - " + item.getStartTime());
                        break;
                    case "W":
                        holder.tv_task_time.setText("Weekly - " + item.getStartTime());

                        break;
                    case "M":
                        holder.tv_task_time.setText("Monthly - " + item.getStartTime());

                        break;
                }
                holder.tv_task_description.setText(item.getContentBody());

                holder.layout_contec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        //SessionManager.setBroadcate_List_Detail(getApplicationContext(), item);
                        Intent getintent = new Intent(getApplicationContext(), Broadcaste_Activity.class);
                        getintent.putExtra("id",item.getId());
                        startActivity(getintent);

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return broadcastActivityListModels.size();
        }

        public class ProgressHolder extends ListItemAdepter.viewData {
            ProgressHolder(View itemView) {
                super(itemView);
            }

        }

        public class viewData extends RecyclerView.ViewHolder {
            TextView tv_username, tv_task_description, tv_time, no_image, tv_status, tv_task_time;
            LinearLayout layout_contec;
            ImageView image_icon, iv_camp, iv_hold, iv_play_icon, iv_puse_icon;

            public viewData(@NonNull View itemView) {
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
                layout_contec = itemView.findViewById(R.id.layout_contec);
                image_icon = itemView.findViewById(R.id.image_icon);
                iv_camp = itemView.findViewById(R.id.iv_camp);
            }
        }

        private void setImage(BroadcastActivityListModel.Broadcast broadcast, viewData holder) {
            switch (broadcast.getStatus()) {
                case "A":
                    holder.iv_hold.setVisibility(View.GONE);
                    holder.iv_play_icon.setVisibility(View.VISIBLE);
                    holder.iv_puse_icon.setVisibility(View.GONE);
                    break;
                case "I":
                    if (broadcast.getFirstActivated() != null && !broadcast.getFirstActivated().equals("")) {
                        holder.iv_puse_icon.setVisibility(View.VISIBLE);
                        holder.iv_hold.setVisibility(View.GONE);
                    } else {
                        holder.iv_hold.setVisibility(View.VISIBLE);
                        holder.iv_puse_icon.setVisibility(View.GONE);
                    }
                    holder.iv_play_icon.setVisibility(View.GONE);
                    break;
            }
        }

    }

    public void showAlertDialogButtonClicked(BroadcastActivityListModel.Broadcast broadcast, int status) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, R.style.MyDialogStyle);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.campanign_aleart_dialog, null);
        builder.setView(customLayout);
        AlertDialog dialog
                = builder.create();

        TextView tv_message = customLayout.findViewById(R.id.tv_message);
        if (status == 1) {
            tv_message.setText("Are you sure you want to pause the broadcast");
        } else if (status == 0) {
            tv_message.setText("Are you sure you want to play the broadcast");
        } else {
            tv_message.setText("Are you sure you want to play the broadcast");
        }
        TextView tv_ok = customLayout.findViewById(R.id.tv_ok);
        TextView tv_cancel = customLayout.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartBroadCastApi(broadcast, status, dialog);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void StartBroadCastApi(BroadcastActivityListModel.Broadcast broadcast, int status, AlertDialog dialog) {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("id", broadcast.getId());
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        if (status == 1) {
            paramObject.addProperty("status", "I");
        } else if (status == 0) {
            paramObject.addProperty("status", "A");
        } else {
            paramObject.addProperty("status", "A");
        }
        obj.add("data", paramObject);
        retrofitCalls.Broadcast_store(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(List_Broadcast_activity.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                        if (response.body().getHttp_status()==200) {


                            currentPage = PAGE_START;
                            isLastPage = false;
                            broadcastActivityListModels.clear();
                            broadCast_Adepter.clear();
                            try {
                                if (Global.isNetworkAvailable(List_Broadcast_activity.this, MainActivity.mMainLayout)) {
                                    if (!swipeToRefresh.isRefreshing()) {
                                        loadingDialog.showLoadingDialog();
                                    }
                                    Broadcast_list();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (response.body().getHttp_status()==403)
                        {
                            Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.plan_validation),false);
                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }

}

