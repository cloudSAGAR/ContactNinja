package com.contactninja.Main_Broadcast.List_And_show;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
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

import com.contactninja.Campaign.List_itm.Campaign_List_Activity;
import com.contactninja.MainActivity;
import com.contactninja.Main_Broadcast.Text_And_Email_Auto_Manual_Broadcast;
import com.contactninja.Model.BroadcastActivityListModel;
import com.contactninja.Model.Campaign_List;
import com.contactninja.Model.ManualTaskModel;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Response;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

public class List_Broadcast_activity extends AppCompatActivity implements View.OnClickListener,
        ConnectivityReceiver.ConnectivityReceiverListener, SwipeRefreshLayout.OnRefreshListener {
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back, iv_filter_icon;
    TextView save_button;
    TextView add_new_contect;

    LinearLayout mMainLayout;
    LinearLayout demo_layout, add_new_contect_layout, lay_no_list;
    RelativeLayout lay_mainlayout;
    TextView tv_create;
    RecyclerView rv_email_list;
    SwipeRefreshLayout swipeToRefresh;
    EditText ev_search;
    ListItemAdepter emailAdepter;
    List<BroadcastActivityListModel.Broadcast> broadcastActivityListModels = new ArrayList<>();
    int perPage = 20;
    String Filter = "";//FINISHED / TODAY / UPCOMING/ DUE/ SKIPPED / PAUSED
    private BroadcastReceiver mNetworkReceiver;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;


    private long mLastClickTime = 0;

    public static void compareDates(String d2, TextView tv_status, TextView tv_time, ManualTaskModel item) {
        try {


            Date date1 = Global.defoult_date_time_formate.parse(Global.getCurrentTimeandDate());
            Date date2 = Global.defoult_date_time_formate.parse(d2);


            if (date1.after(date2)) {
                if (item.getStatus().equals("NOT_STARTED")) {
                    tv_status.setText("Due");
                    tv_status.setTextColor(Color.parseColor("#EC5454"));
                } else {
                    tv_status.setText(Global.setFirstLetter(item.getStatus()));
                    tv_status.setTextColor(Color.parseColor("#ABABAB"));
                }
                tv_time.setText(d2);
                //    Log.e("","Date1 is after Date2");
            }
            // before() will return true if and only if date1 is before date2
            if (date1.before(date2)) {
                if (item.getStatus().equals("NOT_STARTED")) {
                    tv_status.setText("Upcoming");
                    tv_status.setTextColor(Color.parseColor("#2DA602"));
                } else {
                    tv_status.setText(Global.setFirstLetter(item.getStatus()));
                    tv_status.setTextColor(Color.parseColor("#ABABAB"));
                }

                tv_time.setText(d2);
                //  Log.e("","Date1 is before Date2");
                //System.out.println("Date1 is before Date2");
            }

            //equals() returns true if both the dates are equal
            if (date1.equals(date2)) {
                if (item.getStatus().equals("NOT_STARTED")) {
                    tv_status.setText("Today");
                    tv_status.setTextColor(Color.parseColor("#EC5454"));
                } else {
                    tv_status.setText(Global.setFirstLetter(item.getStatus()));
                    tv_status.setTextColor(Color.parseColor("#ABABAB"));
                }

                tv_time.setText(d2);

                //Log.e("","Date1 is equal Date2");
                //System.out.println("Date1 is equal Date2");
            }

            System.out.println();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

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
                    filter(ev_search.getText().toString());
                    return true;
                }
                return false;
            }
        });
        rv_email_list.setLayoutManager(layoutManager);
        emailAdepter = new ListItemAdepter(List_Broadcast_activity.this, new ArrayList<>());
        rv_email_list.setAdapter(emailAdepter);
        rv_email_list.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                ev_search.setText("");
                iv_filter_icon.setImageResource(R.drawable.ic_filter);
                Filter = "";
                isLoading = true;
                currentPage++;
                try {
                    if (Global.isNetworkAvailable(List_Broadcast_activity.this, MainActivity.mMainLayout)) {
                        Mail_list();
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

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<BroadcastActivityListModel.Broadcast> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (BroadcastActivityListModel.Broadcast item : broadcastActivityListModels) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getBroadcastName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (!filteredlist.isEmpty()) {
            emailAdepter.filterList(filteredlist);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ev_search.setText("");
        Filter = "";
        iv_filter_icon.setImageResource(R.drawable.ic_filter);
        currentPage = PAGE_START;
        isLastPage = false;
        broadcastActivityListModels.clear();
        emailAdepter.clear();
        try {
            if (Global.isNetworkAvailable(List_Broadcast_activity.this, MainActivity.mMainLayout)) {
                if (!swipeToRefresh.isRefreshing()) {
                    loadingDialog.showLoadingDialog();
                }
                Mail_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void IntentUI() {

        lay_no_list = findViewById(R.id.lay_no_list);
        iv_filter_icon = findViewById(R.id.iv_filter_icon);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.GONE);

        iv_back.setOnClickListener(this);
        iv_filter_icon.setOnClickListener(this);
        save_button.setText("Next");
        add_new_contect = findViewById(R.id.add_new_contect);
        mMainLayout = findViewById(R.id.mMainLayout);


        lay_mainlayout = findViewById(R.id.lay_mainlayout);
        demo_layout = findViewById(R.id.demo_layout);
        mMainLayout = findViewById(R.id.mMainLayout);
        tv_create = findViewById(R.id.tv_create);
        tv_create.setText(getString(R.string.add_brodcaste));
        rv_email_list = findViewById(R.id.email_list);
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
            case R.id.iv_filter_icon:

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

    void Mail_list() throws JSONException {

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
                        if (!ev_search.getText().toString().equals("") || !Filter.equals("")) {
                            if (broadcastActivityListModels.size() == 0) {
                                swipeToRefresh.setVisibility(View.GONE);
                                lay_no_list.setVisibility(View.VISIBLE);
                            } else {
                                lay_no_list.setVisibility(View.GONE);
                                swipeToRefresh.setVisibility(View.VISIBLE);
                            }

                        } else {

                            if (broadcastActivityListModels.size() == 0) {
                                lay_no_list.setVisibility(View.GONE);
                                lay_mainlayout.setVisibility(View.GONE);
                                demo_layout.setVisibility(View.VISIBLE);
                            } else {

                                lay_no_list.setVisibility(View.GONE);
                                demo_layout.setVisibility(View.GONE);
                                swipeToRefresh.setVisibility(View.VISIBLE);
                                lay_mainlayout.setVisibility(View.VISIBLE);
                            }
                        }


                        if (currentPage != PAGE_START) emailAdepter.removeLoading();
                        emailAdepter.addItems(broadcastActivityListModels);
                        // check weather is last page or not
                        if (emailActivityListModel.getTotal() > emailAdepter.getItemCount()) {
                            emailAdepter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;

                    } else {
                        // Global.Messageshow(getApplicationContext(), mMainLayout, headerString, false);
                        demo_layout.setVisibility(View.VISIBLE);
                    }


                } else {
                    demo_layout.setVisibility(View.VISIBLE);
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
        iv_filter_icon.setImageResource(R.drawable.ic_filter);
        Filter = "";
        refresf_api();
    }

    private void refresf_api() {
        currentPage = PAGE_START;
        isLastPage = false;
        broadcastActivityListModels.clear();
        emailAdepter.clear();
        try {
            if (Global.isNetworkAvailable(List_Broadcast_activity.this, MainActivity.mMainLayout)) {
                if (!swipeToRefresh.isRefreshing()) {
                    loadingDialog.showLoadingDialog();
                }
                Mail_list();
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

            if (item.getType().equals("SMS")) {
                holder.image_icon.setImageResource(R.drawable.ic_message_tab);
            } else {
                holder.image_icon.setImageResource(R.drawable.ic_email);
            }

            setImage(item,holder);
            holder.layout_contec.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    switch (item.getStatus()) {
                        case "A":
                            showAlertDialogButtonClicked(item,1);
                            break;
                        case "I":
                            if (item.getFirstActivated() != null&&!item.getFirstActivated().equals("")) {
                                showAlertDialogButtonClicked(item,0);
                            } else {
                                showAlertDialogButtonClicked(item,3);
                            }
                            break;
                    }
                    return true;
                }
            });

            if (item.getStatus().equals("I")) {
                holder.iv_hold.setVisibility(View.VISIBLE);
                holder.tv_status.setText("Inactive");
                holder.tv_status.setTextColor(getResources().getColor(R.color.red));

            } else if (item.getStatus().equals("P")) {
                holder.iv_puse_icon.setVisibility(View.VISIBLE);
                holder.tv_status.setText("Active");
                holder.tv_status.setTextColor(getResources().getColor(R.color.text_green));

            } else if (item.getStatus().equals("A")) {
                holder.iv_play_icon.setVisibility(View.VISIBLE);
                holder.tv_status.setText("Paused");
                holder.tv_status.setTextColor(getResources().getColor(R.color.tv_push_color));

            }
            holder.iv_hold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    showAlertDialogButtonClicked(item,3);

                }
            });

            holder.iv_play_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    showAlertDialogButtonClicked(item,1);

                }
            });

            holder.iv_puse_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    showAlertDialogButtonClicked(item,0);

                }
            });
            String conactname = item.getBroadcastName();
            holder.tv_username.setText(conactname);
            if (item.getRecurringType().equals("D")) {
                holder.tv_task_time.setText("Daily - " + item.getStartTime());
            } else if (item.getRecurringType().equals("W")) {
                holder.tv_task_time.setText("Weekly - " + item.getStartTime());

            } else if (item.getRecurringType().equals("M")) {
                holder.tv_task_time.setText("Monthly - " + item.getStartTime());

            }
            holder.tv_task_description.setText(item.getContentBody());

            holder.layout_contec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    SessionManager.setBroadcate_List_Detail(getApplicationContext(),item);
                    Intent getintent=new Intent(getApplicationContext(),Broadcaste_Activity.class);
                    startActivity(getintent);

                }
            });

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
                    if (broadcast.getFirstActivated() != null&&!broadcast.getFirstActivated().equals("")) {
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

    public void showAlertDialogButtonClicked(BroadcastActivityListModel.Broadcast broadcast,int status) {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, R.style.MyDialogStyle);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.campanign_aleart_dialog, null);
        builder.setView(customLayout);
        AlertDialog dialog
                = builder.create();

        TextView tv_message = customLayout.findViewById(R.id.tv_message);
        if(status==1){
            tv_message.setText("Are you sure you want to pause the broadcast");
        }else if(status==0) {
            tv_message.setText("Are you sure you want to play the broadcast");
        }else {
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
                StartBroadCastApi(broadcast,status,dialog);
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
        if(status==1){
            paramObject.addProperty("status", "I");
        }else if(status==0){
            paramObject.addProperty("status", "A");
        }else {
            paramObject.addProperty("status", "A");
        }
        obj.add("data", paramObject);
        retrofitCalls.Broadcast_store(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(List_Broadcast_activity.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                        onResume();
                    }
                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }

}

