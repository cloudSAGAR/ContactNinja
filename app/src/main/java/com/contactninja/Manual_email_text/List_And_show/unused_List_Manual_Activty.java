package com.contactninja.Manual_email_text.List_And_show;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.contactninja.MainActivity;
import com.contactninja.Manual_email_text.Text_And_Email_Auto_Manual;
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

import retrofit2.Response;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class unused_List_Manual_Activty extends AppCompatActivity implements View.OnClickListener,
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
    List<ManualTaskModel> manualTaskModelList = new ArrayList<>();
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
        setContentView(R.layout.activity_email_sms_list_activty);
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
        emailAdepter = new ListItemAdepter(unused_List_Manual_Activty.this, new ArrayList<>());
        rv_email_list.setAdapter(emailAdepter);
        rv_email_list.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                iv_filter_icon.setImageResource(R.drawable.ic_filter);
                Filter = "";
                isLoading = true;
                currentPage++;
                try {
                    if (Global.isNetworkAvailable(unused_List_Manual_Activty.this, MainActivity.mMainLayout)) {
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
                Intent intent1 = new Intent(getApplicationContext(), Text_And_Email_Auto_Manual.class);
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
                Intent intent1 = new Intent(getApplicationContext(), Text_And_Email_Auto_Manual.class);
                intent1.putExtra("flag", "add");
                startActivity(intent1);//  finish();
            }
        });


    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<ManualTaskModel> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (ManualTaskModel item : manualTaskModelList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getContactMasterFirstname().toLowerCase().contains(text.toLowerCase())) {
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
        manualTaskModelList.clear();
        emailAdepter.clear();
        try {
            if (Global.isNetworkAvailable(unused_List_Manual_Activty.this, MainActivity.mMainLayout)) {
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
        tv_create.setText(getString(R.string.email_txt));
        rv_email_list = findViewById(R.id.email_list);
        add_new_contect_layout = findViewById(R.id.add_new_contect_layout);

        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(this);
        ev_search = findViewById(R.id.ev_search);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(unused_List_Manual_Activty.this, mMainLayout);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_filter_icon:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                filter_manu();
                break;
        }
    }

    private void filter_manu() {
        /*
        Create By :- Paras
        Date:-1-2-22
        Chnage Date:- 4-2-22
        */

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.filter_solo_list, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(unused_List_Manual_Activty.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        CheckBox ch_today = bottomSheetDialog.findViewById(R.id.ch_today);
        CheckBox ch_upcoming = bottomSheetDialog.findViewById(R.id.ch_upcoming);
        CheckBox ch_due = bottomSheetDialog.findViewById(R.id.ch_due);
        CheckBox ch_complate = bottomSheetDialog.findViewById(R.id.ch_complate);
        CheckBox ch_skipped = bottomSheetDialog.findViewById(R.id.ch_skipped);
        CheckBox ch_Paused = bottomSheetDialog.findViewById(R.id.ch_Paused);
        CheckBox ch_all_task = bottomSheetDialog.findViewById(R.id.ch_all_task);


        switch (Filter) {
            case "today":
                ch_today.setChecked(true);
                break;
            case "upcoming":
                ch_upcoming.setChecked(true);

                break;
            case "due":
                ch_due.setChecked(true);

                break;
            case "finished":
                ch_complate.setChecked(true);

                break;
            case "skipped":
                ch_skipped.setChecked(true);

                break;
            case "paused":
                ch_Paused.setChecked(true);

                break;
        }


        String[] Filters = getResources().getStringArray(R.array.manual_filter);//FINISHED / TODAY / UPCOMING/ DUE/ SKIPPED / PAUSED

        ch_today.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = Filters[0];
                    refresf_api();
                }

            }
        });
        ch_upcoming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = Filters[1];
                    refresf_api();
                }

            }
        });
        ch_due.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = Filters[2];
                    refresf_api();
                }

            }
        });
        ch_complate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = Filters[3];
                    refresf_api();
                }

            }
        });
        ch_skipped.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = Filters[4];
                    refresf_api();
                }

            }
        });
        ch_Paused.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = Filters[5];
                    refresf_api();
                }

            }
        });
        ch_all_task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = "";
                    refresf_api();
                }

            }
        });


        bottomSheetDialog.show();
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
        /*
        Create By :- Paras
        Date:-1-2-22
        Chnage Date:- 4-2-22
        */
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
        retrofitCalls.Mail_Activiy_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                swipeToRefresh.setRefreshing(false);
                if (response.body().getHttp_status() == 200) {


                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    if (response.body().getHttp_status() == 200) {


                        Type listType = new TypeToken<EmailActivityListModel>() {
                        }.getType();
                        EmailActivityListModel emailActivityListModel = new Gson().fromJson(headerString, listType);
                        manualTaskModelList = emailActivityListModel.getManualTask();
                        if (!ev_search.getText().toString().equals("") || !Filter.equals("")) {
                            if (manualTaskModelList.size() == 0) {
                                swipeToRefresh.setVisibility(View.GONE);
                                lay_no_list.setVisibility(View.VISIBLE);
                            } else {
                                lay_no_list.setVisibility(View.GONE);
                                swipeToRefresh.setVisibility(View.VISIBLE);
                            }

                        } else {

                            if (manualTaskModelList.size() == 0) {
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
                        emailAdepter.addItems(manualTaskModelList);
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
        manualTaskModelList.clear();
        emailAdepter.clear();
        try {
            if (Global.isNetworkAvailable(unused_List_Manual_Activty.this, MainActivity.mMainLayout)) {
                if (!swipeToRefresh.isRefreshing()) {
                    loadingDialog.showLoadingDialog();
                }
                Mail_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void compareDates(String onlyDate, String FullDate, TextView tv_status, TextView tv_time, ManualTaskModel item) {
        try {


            Date date1 = Global.defoult_date_formate.parse(Global.getCurrentDate());
            Date date2 = Global.defoult_date_formate.parse(onlyDate);


            if (date1.after(date2)) {
                if (item.getStatus().equals("NOT_STARTED")) {
                    tv_status.setText("Due");
                    tv_status.setTextColor(Color.parseColor("#EC5454"));
                } else {
                    tv_status.setText(Global.setFirstLetter(item.getStatus()));
                    tv_status.setTextColor(Color.parseColor("#ABABAB"));
                }
                String formateChnage=Global.formateChange(FullDate);
                tv_time.setText(formateChnage.replace(" ", "\n"));

            } else if (date1.before(date2)) {
                if (item.getStatus().equals("NOT_STARTED")) {
                    tv_status.setText("Upcoming");
                    tv_status.setTextColor(Color.parseColor("#2DA602"));
                } else {
                    tv_status.setText(Global.setFirstLetter(item.getStatus()));
                    tv_status.setTextColor(Color.parseColor("#ABABAB"));
                }

                String formateChnage=Global.formateChange(FullDate);
                tv_time.setText(formateChnage.replace(" ", "\n"));

            } else if (date1.equals(date2)) {
                if (item.getStatus().equals("NOT_STARTED")) {
                    tv_status.setText("Today");
                    tv_status.setTextColor(Color.parseColor("#EC5454"));
                } else {
                    tv_status.setText(Global.setFirstLetter(item.getStatus()));
                    tv_status.setTextColor(Color.parseColor("#ABABAB"));
                }
                tv_time.setText(parseDate(FullDate));

            }

            System.out.println();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public static String parseDate(String timeAtMiliseconds) throws ParseException {
        String result = "now";

        Date CurrentDate = Global.defoult_date_time_formate.parse(Global.getCurrentTimeandDate());
        Date CreateDate = Global.defoult_date_time_formate.parse(timeAtMiliseconds);


        long different = Math.abs(CurrentDate.getTime() - CreateDate.getTime());

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        different = different % secondsInMilli;
        if (elapsedDays == 0) {
            if (elapsedHours == 0) {
                if (elapsedMinutes == 0) {
                    if (elapsedSeconds < 0) {
                        return "0" + " s";
                    } else {
                        if (elapsedDays > 0 && elapsedSeconds < 59) {
                            return "now";
                        }
                    }
                } else {
                    return String.valueOf(elapsedMinutes) + "m ago";
                }
            } else {
                return String.valueOf(elapsedHours) + "h ago";
            }

        }


        return result;
    }

    public class ListItemAdepter extends RecyclerView.Adapter<ListItemAdepter.viewData> {
        private static final int VIEW_TYPE_LOADING = 0;
        private static final int VIEW_TYPE_NORMAL = 1;
        public Context mCtx;
        List<ManualTaskModel> manualTaskModelList;
        private boolean isLoaderVisible = false;

        public ListItemAdepter(Context context, List<ManualTaskModel> manualTaskModelList) {
            this.mCtx = context;
            this.manualTaskModelList = manualTaskModelList;
        }

        public void filterList(ArrayList<ManualTaskModel> filterllist) {
            // below line is to add our filtered
            // list in our course array list.
            manualTaskModelList = filterllist;
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
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emailactivitylist, parent, false));
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
                return position == manualTaskModelList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
            } else {
                return VIEW_TYPE_NORMAL;
            }
        }

        public void addItems(List<ManualTaskModel> postItems) {
            manualTaskModelList.addAll(postItems);
            notifyDataSetChanged();
        }

        public void addLoading() {
            isLoaderVisible = true;
            manualTaskModelList.add(new ManualTaskModel());
            notifyItemInserted(manualTaskModelList.size() - 1);
        }

        public void removeLoading() {
            isLoaderVisible = false;
            int position = manualTaskModelList.size() - 1;
            ManualTaskModel item = getItem(position);
            if (item != null) {
                manualTaskModelList.remove(position);
                notifyItemRemoved(position);
            }
        }

        ManualTaskModel getItem(int position) {
            return manualTaskModelList.get(position);
        }

        public void clear() {
            manualTaskModelList.clear();
            notifyDataSetChanged();
        }

        @SuppressLint("LogConditional")
        @Override
        public void onBindViewHolder(@NonNull ListItemAdepter.viewData holder, int position) {
            ManualTaskModel item = manualTaskModelList.get(position);
            if (Global.IsNotNull(item.getType()) && !item.getType().equals("")) {
                if (item.getType().equals("SMS")) {
                    holder.image_icon.setImageResource(R.drawable.ic_message_tab);
                } else {
                    holder.image_icon.setImageResource(R.drawable.ic_email);
                }
                if (!Global.IsNotNull(item.getSeqId()) || item.getSeqId() != 0) {
                    holder.iv_camp.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_camp.setVisibility(View.GONE);
                }

                String conactname = item.getContactMasterFirstname() + " " + item.getContactMasterLastname();
                holder.tv_username.setText(Global.setFirstLetter(conactname));
                holder.tv_task_description.setText(Global.setFirstLetter(item.getTask_name()));

                String FullDate = item.getDate() + " " + item.getTime();
                compareDates(item.getDate(), FullDate, holder.tv_status, holder.tv_time, item);

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
                holder.layout_contec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (item.getType().toString().equals("SMS")) {
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();
                            Intent intent = new Intent(getApplicationContext(), Item_List_Text_Detail_Activty.class);
                            intent.putExtra("record_id", item.getId());
                            startActivity(intent);
                        } else {
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();
                            Intent intent = new Intent(getApplicationContext(), Item_List_Email_Detail_activty.class);
                            intent.putExtra("record_id", item.getId());
                            startActivity(intent);
                        }

                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return manualTaskModelList.size();
        }

        public class ProgressHolder extends ListItemAdepter.viewData {
            ProgressHolder(View itemView) {
                super(itemView);
            }

        }

        public class viewData extends RecyclerView.ViewHolder {
            TextView tv_username, tv_task_description, tv_time, no_image, tv_status;
            LinearLayout layout_contec;
            ImageView image_icon, iv_camp;

            public viewData(@NonNull View itemView) {
                super(itemView);
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

