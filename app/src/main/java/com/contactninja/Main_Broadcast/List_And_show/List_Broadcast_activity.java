package com.contactninja.Main_Broadcast.List_And_show;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contactninja.MainActivity;
import com.contactninja.Main_Broadcast.Text_And_Email_Auto_Manual_Broadcast;
import com.contactninja.Manual_email_text.List_And_show.Item_List_Email_Detail_activty;
import com.contactninja.Manual_email_text.List_And_show.Item_List_Text_Detail_Activty;
import com.contactninja.Manual_email_text.List_And_show.List_Manual_Activty;
import com.contactninja.Manual_email_text.Text_And_Email_Auto_Manual;
import com.contactninja.Model.BroadcastActivityListModel;
import com.contactninja.Model.EmailActivityListModel;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


        ev_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        rv_email_list.setLayoutManager(layoutManager);
        emailAdepter = new ListItemAdepter(List_Broadcast_activity.this, new ArrayList<>());
        rv_email_list.setAdapter(emailAdepter);
        rv_email_list.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
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
        if (filteredlist.isEmpty()) {

        } else {
            emailAdepter.filterList(filteredlist);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        paramObject.addProperty("filter_by", Filter);
        paramObject.addProperty("user_datetime", Global.getCurrentTimeandDate());
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

    public static void compareDates(String d2, TextView tv_status, TextView tv_time, ManualTaskModel item) {
        try {
            // If you already have date objects then skip 1

            //1
            // Create 2 dates starts

            Date date1 = Global.defoult_date_time_formate.parse(Global.getCurrentTimeandDate());
            Date date2 = Global.defoult_date_time_formate.parse(d2);

            // Create 2 dates ends
            //1

            // Date object is having 3 methods namely after,before and equals for comparing
            // after() will return true if and only if date1 is after date 2


        /*    String convTime = null;
            try {
                String prefix = "";
                String suffix = "Ago";


                long dateDiff = date2.getTime() - date1.getTime();
                    Log.e("dateDiff", String.valueOf(dateDiff));
                    long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
                    long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
                    long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
                    long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

                    Log.e("Second", String.valueOf(second));
                    Log.e("Minute", String.valueOf(minute));
                    Log.e("hour", String.valueOf(hour));
                    Log.e("day", String.valueOf(day));
                    if (second < 60) {
                        convTime = second + " Sec " + suffix;
                    } else if (minute < 60) {
                        convTime = minute + " Min " + suffix;
                    } else if (hour < 24) {
                        convTime = hour + " Hours " + suffix;
                    } else {
                        convTime = d2;
                    }

                tv_time.setText(convTime);

            } catch (Exception e) {
                e.printStackTrace();
                //  Log.e("ConvTimeE", e.getMessage());
            }*/


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

              /*  if (item.getType().equals("SMS")) {
                    holder.image_icon.setImageResource(R.drawable.ic_message_tab);
                } else {
                    holder.image_icon.setImageResource(R.drawable.ic_email);
                }*/
               /* if (!Global.IsNotNull(item.getSeqId()) || item.getSeqId() != 0) {
                    holder.iv_camp.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_camp.setVisibility(View.GONE);
                }*/

                String conactname = item.getBroadcastName() ;
                holder.tv_username.setText(Global.setFirstLetter(conactname));
               // holder.tv_task_description.setText(Global.setFirstLetter(item.getTask_name()));
              //  String time = item.getDate() + " " + item.getTime();
               // compareDates(time, holder.tv_status, holder.tv_time, item);

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
            TextView tv_username, tv_task_description, tv_time, no_image, tv_status,tv_task_time;
            LinearLayout layout_contec;
            ImageView image_icon, iv_camp,iv_hold,iv_play_icon,iv_puse_icon;

            public viewData(@NonNull View itemView) {
                super(itemView);
                iv_puse_icon=itemView.findViewById(R.id.iv_puse_icon);
                iv_play_icon=itemView.findViewById(R.id.iv_play_icon);
                iv_hold=itemView.findViewById(R.id.iv_hold);
                tv_task_time=itemView.findViewById(R.id.tv_task_time);
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
    }

}

