package com.contactninja.Campaign;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Interface.TimeZoneClick;
import com.contactninja.MainActivity;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.WorkingHoursModel;
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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables")
public class Campaign_Name_Activity extends AppCompatActivity implements View.OnClickListener,
        ConnectivityReceiver.ConnectivityReceiverListener, TimeZoneClick {
    ImageView iv_back;
    TextView save_button, topic_remainingCharacter, tv_error_title, tv_error_day, txt_Curent_time, txt_timezon;
    Integer WorkingHoursID = 0;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    EditText edt_titale, edt_day;
    int sequence_id = 0, seq_task_id = 0;
    ConstraintLayout mMainLayout;
    String sequence_Name = "";
    LinearLayout layout_time_zone;
    BottomSheetDialog bottomSheetDialog_time;
    List<WorkingHoursModel.WorkingHour> workingHourList = new ArrayList<>();
    TimeZoneAdepter timeZoneAdepter;
    private BroadcastReceiver mNetworkReceiver;
    int perPage = 20;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private long mLastClickTime = 0;
    String flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_name);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        edt_titale.requestFocus();
        timeZoneAdepter = new TimeZoneAdepter(Campaign_Name_Activity.this, new ArrayList<>(), this);
        try {
            Intent getintent = getIntent();
            Bundle bundle = getintent.getExtras();
            sequence_id = bundle.getInt("sequence_id");
            seq_task_id = bundle.getInt("seq_task_id");
            sequence_Name = bundle.getString("sequence_Name");
            flag = bundle.getString("flag");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Global.IsNotNull(sequence_Name)) {
            edt_titale.setText(sequence_Name);
            edt_titale.setSelection(edt_titale.getText().length());
        }
        edt_titale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                topic_remainingCharacter.setText(40 - editable.length() + " Characters Remaining.");
            }
        });

    }

    private void IntentUI() {

        mMainLayout = findViewById(R.id.mMainLayout);
        layout_time_zone = findViewById(R.id.layout_time_zone);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        layout_time_zone.setOnClickListener(this);
        save_button.setText(getResources().getString(R.string.Done));
        edt_titale = findViewById(R.id.edt_titale);
        topic_remainingCharacter = findViewById(R.id.topic_remainingCharacter);
        tv_error_title = findViewById(R.id.tv_error_title);
        tv_error_day = findViewById(R.id.tv_error_day);
        txt_Curent_time = findViewById(R.id.txt_Curent_time);
        txt_timezon = findViewById(R.id.txt_timezon);
        edt_day = findViewById(R.id.edt_day);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.layout_time_zone:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                showBottomSheetDialog_For_Time();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Global.hideKeyboard(Campaign_Name_Activity.this);
                //Add Api Call
                if (checkVelidaction()) {
                    tv_error_title.setVisibility(View.GONE);
                    tv_error_day.setVisibility(View.GONE);
                    if (Global.isNetworkAvailable(Campaign_Name_Activity.this, mMainLayout)) {
                        AddName();
                    }
                }
                break;

        }
    }

    private boolean checkVelidaction() {

        if (edt_titale.getText().toString().trim().equals("")) {
            tv_error_title.setVisibility(View.VISIBLE);
        } else if (edt_day.getText().toString().trim().equals("")) {
            tv_error_day.setVisibility(View.VISIBLE);
        } else {
            return true;
        }
        return false;
    }

    private void showBottomSheetDialog_For_Time() {
        bottomSheetDialog_time = new BottomSheetDialog(Campaign_Name_Activity.this, R.style.BottomSheetDialog);
        bottomSheetDialog_time.setContentView(R.layout.bottom_sheet_dialog_for_timezone);
        RecyclerView rv_time_list = bottomSheetDialog_time.findViewById(R.id.rv_time_list);
        TextView tv_item = bottomSheetDialog_time.findViewById(R.id.tv_item);
        tv_item.setText("Please select Timezone");
        tv_item.setVisibility(View.VISIBLE);

        ImageView search_icon = bottomSheetDialog_time.findViewById(R.id.search_icon);
        EditText ev_search = bottomSheetDialog_time.findViewById(R.id.ev_search);
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ev_search.requestFocus();
            }
        });

        rv_time_list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_time_list.setLayoutManager(layoutManager);
        rv_time_list.setAdapter(timeZoneAdepter);

        rv_time_list.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                try {
                    if (Global.isNetworkAvailable(Campaign_Name_Activity.this, MainActivity.mMainLayout)) {
                        Time_list();
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


        ev_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<WorkingHoursModel.WorkingHour> temp = new ArrayList();
                for (WorkingHoursModel.WorkingHour d : workingHourList) {
                    if (d.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        temp.add(d);
                        // Log.e("Same Data ",d.getUserName());
                    }
                }
                timeZoneAdepter.updateList(temp);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        bottomSheetDialog_time.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Campaign_Name_Activity.this, mMainLayout);
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

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = PAGE_START;
        isLastPage = false;
        workingHourList.clear();
        timeZoneAdepter.clear();
        try {
            if (Global.isNetworkAvailable(Campaign_Name_Activity.this, MainActivity.mMainLayout)) {

                Time_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void AddName() {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        if (SessionManager.getTask(getApplicationContext()).size() != 0) {
            sequence_id = SessionManager.getTask(getApplicationContext()).get(0).getSequenceId();
        } else {
            Intent getintent = getIntent();
            Bundle bundle = getintent.getExtras();
            sequence_id = bundle.getInt("sequence_id");
        }
      //  Log.e("sequence_id", String.valueOf(sequence_id));
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("record_id", sequence_id);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("seq_name", edt_titale.getText().toString());
        paramObject.addProperty("max_prospects", edt_day.getText().toString());
        paramObject.addProperty("working_hours_ids", WorkingHoursID);
        obj.add("data", paramObject);
        retrofitCalls.Sequence_settings(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(Campaign_Name_Activity.this), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {


                        if (response.body().getHttp_status() == 200) {

                            if (flag.equals("edit")) {
                                /*SessionManager.setCampign_flag("read_name");
                                Intent intent = new Intent(getApplicationContext(), Campaign_Preview.class);
                                intent.putExtra("sequence_id", sequence_id);
                                *//*intent.putExtra("seq_task_id",seq_task_id);*//*
                                startActivity(intent);*/
                                loadingDialog.cancelLoading();
                                finish();
                            } else {
                                SessionManager.setCampign_flag("read_name");
                                Intent intent = new Intent(getApplicationContext(), Campaign_Preview.class);
                                intent.putExtra("sequence_id", sequence_id);
                                /*intent.putExtra("seq_task_id",seq_task_id);*/
                                startActivity(intent);
                                finish();
                            }

                        } else {

                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }


    private void Time_list() throws JSONException {


        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("q", "");
        paramObject.addProperty("perPage", perPage);
        paramObject.addProperty("page", currentPage);
        obj.add("data", paramObject);

        retrofitCalls.working_hour(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(Campaign_Name_Activity.this), Global.Device, new RetrofitCallback() {
                    @SuppressLint("SyntheticAccessor")
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();

                        Gson gson = new Gson();
                        String headerString = gson.toJson(response.body().getData());
                        if (response.body().getHttp_status() == 200) {


                            Type listType = new TypeToken<WorkingHoursModel>() {
                            }.getType();
                            WorkingHoursModel workingHoursModel = new Gson().fromJson(headerString, listType);
                            workingHourList = workingHoursModel.getWorkingHours();
                            WorkingHoursModel.UserTimezone userTimezone = workingHoursModel.getUserTimezone();
                            txt_Curent_time.setText(String.valueOf(userTimezone.getZoneName()));
                            if (Global.IsNotNull(workingHourList) && workingHourList.size() != 0) {
                                for (int i = 0; i < workingHourList.size(); i++) {
                                    if (workingHourList.get(i).getIsDefault().equals("1")) {
                                        txt_timezon.setText(workingHourList.get(i).getName());
                                        WorkingHoursID = workingHourList.get(i).getId();
                                        break;
                                    } else {
                                        txt_timezon.setText(workingHourList.get(0).getName());
                                        WorkingHoursID = workingHourList.get(0).getId();
                                    }
                                }
                            }

                            if (currentPage != PAGE_START) timeZoneAdepter.removeLoading();
                            timeZoneAdepter.addItems(workingHourList);
                            // check weather is last page or not
                            if (workingHoursModel.getTotal() > timeZoneAdepter.getItemCount()) {
                                timeZoneAdepter.addLoading();
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;

                        } else {
                            // Global.Messageshow(getApplicationContext(), mMainLayout, headerString, false);

                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });


    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void OnClick(WorkingHoursModel.WorkingHour workingHour) {
        txt_timezon.setText(workingHour.getName());
        WorkingHoursID = workingHour.getId();
        bottomSheetDialog_time.dismiss();
    }


    public static class TimeZoneAdepter extends RecyclerView.Adapter<TimeZoneAdepter.viewData> {
        private static final int VIEW_TYPE_LOADING = 0;
        private static final int VIEW_TYPE_NORMAL = 1;
        public Context mCtx;
        TimeZoneClick timeZoneClick;
        private boolean isLoaderVisible = false;
        private List<WorkingHoursModel.WorkingHour> workingHourList = new ArrayList<>();

        public TimeZoneAdepter(Context context, List<WorkingHoursModel.WorkingHour> workingHourList, TimeZoneClick timeZoneClick) {
            this.mCtx = context;
            this.workingHourList = workingHourList;
            this.timeZoneClick = timeZoneClick;
        }

        @NonNull
        @Override
        public TimeZoneAdepter.viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            switch (viewType) {
                case VIEW_TYPE_NORMAL:
                    return new TimeZoneAdepter.viewData(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timezon_list, parent, false));
                case VIEW_TYPE_LOADING:
                    return new TimeZoneAdepter.ProgressHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
                default:
                    return null;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isLoaderVisible) {
                return position == workingHourList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
            } else {
                return VIEW_TYPE_NORMAL;
            }
        }

        public void addItems(List<WorkingHoursModel.WorkingHour> postItems) {
            workingHourList.addAll(postItems);
            notifyDataSetChanged();
        }

        public void addLoading() {
            isLoaderVisible = true;
            workingHourList.add(new WorkingHoursModel.WorkingHour());
            notifyItemInserted(workingHourList.size() - 1);
        }

        public void removeLoading() {
            isLoaderVisible = false;
            int position = workingHourList.size() - 1;
            WorkingHoursModel.WorkingHour item = getItem(position);
            if (item != null) {
                workingHourList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            workingHourList.clear();
            notifyDataSetChanged();
        }

        public void updateList(List<WorkingHoursModel.WorkingHour> list) {
            workingHourList = list;
            notifyDataSetChanged();
        }

        WorkingHoursModel.WorkingHour getItem(int position) {
            return workingHourList.get(position);
        }

        @Override
        public void onBindViewHolder(@NonNull TimeZoneAdepter.viewData holder, int position) {
            switch (getItemViewType(position)) {
                case VIEW_TYPE_NORMAL:

                    WorkingHoursModel.WorkingHour workingHour = workingHourList.get(position);
                    if (Global.IsNotNull(workingHour.getName())) {
                        holder.tv_time_name.setText(workingHour.getName()+"( "+workingHour.getStartTime()+" - "+workingHour.getEndTime()+" )");
                        if (Global.IsNotNull(workingHour.getIsDefault())) {
                            if (workingHour.getIsDefault().equals("1")) {
                                holder.iv_is_default.setVisibility(View.VISIBLE);
                            } else {
                                holder.iv_is_default.setVisibility(View.GONE);
                            }
                        }
                    }
                    holder.layout_time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            timeZoneClick.OnClick(workingHour);
                        }
                    });
                    break;
            }
        }


        @Override
        public int getItemCount() {
            return workingHourList.size();
        }


        public class viewData extends RecyclerView.ViewHolder {
            TextView tv_time_name;
            ImageView iv_is_default;
            LinearLayout layout_time;

            public viewData(@NonNull View itemView) {
                super(itemView);
                tv_time_name = itemView.findViewById(R.id.tv_time_name);
                layout_time = itemView.findViewById(R.id.layout_time);
                iv_is_default = itemView.findViewById(R.id.iv_is_default);

            }
        }

        public class ProgressHolder extends TimeZoneAdepter.viewData {
            ProgressHolder(View itemView) {
                super(itemView);
            }

        }
    }
}