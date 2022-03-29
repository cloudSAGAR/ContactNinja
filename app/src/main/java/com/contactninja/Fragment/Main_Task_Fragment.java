package com.contactninja.Fragment;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.contactninja.MainActivity;
import com.contactninja.Manual_email_text.List_And_show.Item_List_Email_Detail_activty;
import com.contactninja.Manual_email_text.List_And_show.Item_List_Text_Detail_Activty;
import com.contactninja.Manual_email_text.Manual_Auto_Task_Name_Activity;
import com.contactninja.Manual_email_text.Manual_Shooz_Time_Date_Activity;
import com.contactninja.Manual_email_text.Text_And_Email_Auto_Manual;
import com.contactninja.Model.EmailActivityListModel;
import com.contactninja.Model.ManualTaskModel;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UserLinkedList;
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
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Main_Task_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Main_Task_Fragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Main_Task_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Main_Task_Fragment newInstance(String param1, String param2) {
        Main_Task_Fragment fragment = new Main_Task_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_filter_icon, iv_cancle_search_icon;
    TextView add_new_contect;

    LinearLayout mMainLayout,add_new_contect_layout;
    LinearLayout demo_layout, lay_no_list;
    LinearLayout layout_toolbar_logo;
    RelativeLayout lay_mainlayout;
    TextView tv_create;
    RecyclerView rv_Task_list;
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
    public boolean isLoaderVisible = false;


    private long mLastClickTime = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_send, container, false);
        mNetworkReceiver = new ConnectivityReceiver();
        loadingDialog = new LoadingDialog(getActivity());
        sessionManager = new SessionManager(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        IntentUI(view);

        ev_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Global.hideKeyboard(getActivity());
                    iv_cancle_search_icon.setVisibility(View.VISIBLE);
                    iv_filter_icon.setVisibility(View.GONE);
                    Filter = "";
                    onResume();
                    return true;
                }
                return false;
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_Task_list.setLayoutManager(layoutManager);
        emailAdepter = new ListItemAdepter(getActivity(), new ArrayList<>());
        rv_Task_list.setAdapter(emailAdepter);
        rv_Task_list.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                try {
                    if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
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
                Intent intent1 = new Intent(getActivity(), Text_And_Email_Auto_Manual.class);
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
                Intent intent1 = new Intent(getActivity(), Text_And_Email_Auto_Manual.class);
                intent1.putExtra("flag", "add");
                startActivity(intent1);//  finish();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //   Filter = "";
        //   iv_filter_icon.setImageResource(R.drawable.ic_filter);
        currentPage = PAGE_START;
        isLastPage = false;
        manualTaskModelList.clear();
        emailAdepter.clear();
        isLoaderVisible=false;
        try {
            if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                if (!swipeToRefresh.isRefreshing()) {
                    loadingDialog.showLoadingDialog();
                }
                Mail_list();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void IntentUI(View view) {

        lay_no_list = view.findViewById(R.id.lay_no_list);
        iv_filter_icon = view.findViewById(R.id.iv_filter_icon);
        iv_filter_icon.setOnClickListener(this);
        add_new_contect = view.findViewById(R.id.add_new_contect);
        mMainLayout = view.findViewById(R.id.mMainLayout);


        lay_mainlayout = view.findViewById(R.id.lay_mainlayout);
        demo_layout = view.findViewById(R.id.demo_layout);
        mMainLayout = view.findViewById(R.id.mMainLayout);
        tv_create = view.findViewById(R.id.tv_create);
        tv_create.setText(getString(R.string.txt_task));
        rv_Task_list = view.findViewById(R.id.rv_Task_list);
        add_new_contect_layout = view.findViewById(R.id.add_new_contect_layout);

        swipeToRefresh = view.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(this);
        ev_search = view.findViewById(R.id.ev_search);
        iv_cancle_search_icon = view.findViewById(R.id.iv_cancle_search_icon);
        iv_cancle_search_icon.setOnClickListener(this);

        layout_toolbar_logo = view.findViewById(R.id.layout_toolbar_logo);
        layout_toolbar_logo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_filter_icon:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                filter_manu();
                break;
            case R.id.iv_cancle_search_icon:
                ev_search.setText("");
                Filter = "";
                iv_cancle_search_icon.setVisibility(View.GONE);
                iv_filter_icon.setVisibility(View.VISIBLE);
                onResume();
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
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        CheckBox ch_today = bottomSheetDialog.findViewById(R.id.ch_today);
        CheckBox ch_upcoming = bottomSheetDialog.findViewById(R.id.ch_upcoming);
        CheckBox ch_due = bottomSheetDialog.findViewById(R.id.ch_due);
        CheckBox ch_complate = bottomSheetDialog.findViewById(R.id.ch_complate);
        CheckBox ch_skipped = bottomSheetDialog.findViewById(R.id.ch_skipped);
        CheckBox ch_Paused = bottomSheetDialog.findViewById(R.id.ch_Paused);
        CheckBox ch_auto_complate_task = bottomSheetDialog.findViewById(R.id.ch_auto_complate_task);
        CheckBox ch_auto_upcomimg_task = bottomSheetDialog.findViewById(R.id.ch_auto_upcomimg_task);
        CheckBox ch_all_task = bottomSheetDialog.findViewById(R.id.ch_all_task);


        switch (Filter) {
            case "TODAY":
                ch_today.setChecked(true);
                break;
            case "UPCOMING":
                ch_upcoming.setChecked(true);

                break;
            case "DUE":
                ch_due.setChecked(true);

                break;
            case "FINISHED":
                ch_complate.setChecked(true);

                break;
            case "SKIPPED":
                ch_skipped.setChecked(true);

                break;
            case "PAUSED":
                ch_Paused.setChecked(true);
                break;

            case "AUTO":
                ch_auto_upcomimg_task.setChecked(true);
                break;

            case "FINISHED_AUTO":
                ch_auto_complate_task.setChecked(true);
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
                } else {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter);
                    bottomSheetDialog.dismiss();
                    Filter = "";
                }
                refresf_api();

            }
        });
        ch_upcoming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = Filters[1];
                } else {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter);
                    bottomSheetDialog.dismiss();
                    Filter = "";
                }
                refresf_api();

            }
        });
        ch_due.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = Filters[2];
                } else {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter);
                    bottomSheetDialog.dismiss();
                    Filter = "";
                }
                refresf_api();
            }
        });
        ch_complate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = Filters[3];
                } else {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter);
                    bottomSheetDialog.dismiss();
                    Filter = "";
                }
                refresf_api();

            }
        });
        ch_skipped.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = Filters[4];
                } else {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter);
                    bottomSheetDialog.dismiss();
                    Filter = "";
                }
                refresf_api();

            }
        });
        ch_Paused.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = Filters[5];
                } else {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter);
                    bottomSheetDialog.dismiss();
                    Filter = "";
                }
                refresf_api();
            }
        });
        ch_auto_upcomimg_task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = Filters[6];
                } else {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter);
                    bottomSheetDialog.dismiss();
                    Filter = "";
                }
                refresf_api();
            }
        });
        ch_auto_complate_task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = Filters[7];
                } else {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter);
                    bottomSheetDialog.dismiss();
                    Filter = "";
                }
                refresf_api();
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

    void Mail_list() throws JSONException {
        /*
        Create By :- Paras
        Date:-1-2-22
        Chnage Date:- 4-2-22
        */
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(getActivity());
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
        retrofitCalls.Mail_Activiy_list(sessionManager, obj, loadingDialog, token, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
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
                                rv_Task_list.setVisibility(View.GONE);
                                lay_no_list.setVisibility(View.VISIBLE);
                            } else {
                                lay_no_list.setVisibility(View.GONE);
                                rv_Task_list.setVisibility(View.VISIBLE);
                            }

                        } else {

                            if (manualTaskModelList.size() == 0) {
                                lay_no_list.setVisibility(View.GONE);
                                lay_mainlayout.setVisibility(View.GONE);
                                demo_layout.setVisibility(View.VISIBLE);
                            } else {

                                lay_no_list.setVisibility(View.GONE);
                                demo_layout.setVisibility(View.GONE);
                                rv_Task_list.setVisibility(View.VISIBLE);
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
                        demo_layout.setVisibility(View.VISIBLE);
                    }
                }
                else if (response.body().getHttp_status()==403)
                {
                    Global.Messageshow(getActivity(),mMainLayout,getResources().getString(R.string.plan_validation),false);

                }

                else {
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
        ev_search.setText("");
        iv_cancle_search_icon.setVisibility(View.GONE);
        iv_filter_icon.setVisibility(View.VISIBLE);
        iv_filter_icon.setImageResource(R.drawable.ic_filter);
        Filter = "";

        refresf_api();
    }

    private void refresf_api() {
        currentPage = PAGE_START;
        isLastPage = false;
        manualTaskModelList.clear();
        emailAdepter.clear();
        isLoaderVisible=false;
        try {
            if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
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
                String formateChnage = Global.formateChange(FullDate);
                tv_time.setText(formateChnage.replace(" ", "\n"));

            } else if (date1.before(date2)) {
                if (item.getStatus().equals("NOT_STARTED")) {
                    tv_status.setText("Upcoming");
                    tv_status.setTextColor(Color.parseColor("#2DA602"));
                } else {
                    tv_status.setText(Global.setFirstLetter(item.getStatus()));
                    tv_status.setTextColor(Color.parseColor("#ABABAB"));
                }

                String formateChnage = Global.formateChange(FullDate);
                tv_time.setText(formateChnage.replace(" ", "\n"));

            } else if (date1.equals(date2)) {
                if (item.getStatus().equals("NOT_STARTED")) {
                    tv_status.setText("Today");
                } else {
                    tv_status.setText(Global.setFirstLetter(item.getStatus()));
                }
                tv_status.setTextColor(Color.parseColor("#ABABAB"));
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
        public Activity mCtx;
        List<ManualTaskModel> manualTaskModelList;

        public ListItemAdepter(Activity context, List<ManualTaskModel> manualTaskModelList) {
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
        public viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            switch (viewType) {
                case VIEW_TYPE_NORMAL:
                    return new viewData(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emailactivitylist, parent, false));
                case VIEW_TYPE_LOADING:
                    return new ProgressHolder(
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
        public void onBindViewHolder(@NonNull viewData holder, int position) {
            ManualTaskModel item = manualTaskModelList.get(position);
            if (Global.IsNotNull(item.getType()) && !item.getType().equals("")) {
                if (item.getType().equals("SMS")) {
                    holder.image_icon.setImageResource(R.drawable.ic_message_tab);
                } else {
                    holder.image_icon.setImageResource(R.drawable.ic_email);
                }


                if (Global.IsNotNull(item.getSequence_task_from())) {
                    if(item.getSequence_task_from()==1){
                        holder.iv_labal.setVisibility(View.VISIBLE);
                        holder.iv_labal.setImageResource(R.drawable.ic_campaning_icon);
                    }else if(item.getSequence_task_from()==2){
                        holder.iv_labal.setVisibility(View.GONE);

                    } else if(item.getSequence_task_from()==3){
                        holder.iv_labal.setVisibility(View.VISIBLE);
                        holder.iv_labal.setImageResource(R.drawable.ic_broadcast);
                    }
                } else {
                    holder.iv_labal.setVisibility(View.GONE);
                }

                String conactname = item.getContactMasterFirstname() + " " + item.getContactMasterLastname();
                holder.tv_username.setText(Global.setFirstLetter(conactname));
                holder.tv_task_description.setText(Global.setFirstLetter(item.getTask_name()));
                String FullDate="",curendate="";
                if(Filter.equals("FINISHED_AUTO")||Filter.equals("FINISHED")){
                    FullDate = item.getCompleted_on();
                    String currentString = item.getCompleted_on();
                    String[] separated = currentString.split(" ");
                    curendate = separated[0];
                }else {
                    FullDate = item.getDate() + " " + item.getTime();
                    curendate =item.getDate();
                }

                compareDates(curendate, FullDate, holder.tv_status, holder.tv_time, item);

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
                        if (!item.getManage_by().toString().equals("AUTO")||
                        !item.getStatus().equals("FINISHED")) {
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();


                                if (item.getType().toString().equals("SMS")) {
                                    Intent intent = new Intent(getActivity(), Item_List_Text_Detail_Activty.class);
                                    intent.putExtra("record_id", item.getId());
                                    startActivity(intent);
                                } else {
                                    try {
                                        if(Global.isNetworkAvailable(mCtx, MainActivity.mMainLayout)) {
                                            Mail_Checklist(item);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                   /* Intent intent = new Intent(getActivity(), Item_List_Email_Detail_activty.class);
                                    intent.putExtra("record_id", item.getId());
                                    startActivity(intent);*/
                                }



                        }else {
                            if(Global.IsNotNull(item.getStage())){
                                if(!item.getStage().equals("FINISHED")) {

                                    String[] selet_item = getResources().getStringArray(R.array.auto_Select);

                                    switch (item.getStage()) {
                                        case "PAUSED":
                                            showAlertDialogButtonClicked(getResources().getString(R.string.manual_aleart_paused),
                                                    getResources().getString(R.string.manual_aleart_paused_des), selet_item[1], item.getId());
                                            break;
                                        case "NOT_STARTED":
                                            showAlertDialogButtonClicked(getResources().getString(R.string.auto_aleart_Paused),
                                                    getResources().getString(R.string.auto_aleart_Paused_des), selet_item[0], item.getId());
                                            break;
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return manualTaskModelList.size();
        }

        public class ProgressHolder extends viewData {
            ProgressHolder(View itemView) {
                super(itemView);
            }

        }

        public class viewData extends RecyclerView.ViewHolder {
            TextView tv_username, tv_task_description, tv_time, no_image, tv_status;
            LinearLayout layout_contec;
            ImageView image_icon, iv_labal;

            public viewData(@NonNull View itemView) {
                super(itemView);
                tv_username = itemView.findViewById(R.id.tv_username);
                tv_task_description = itemView.findViewById(R.id.tv_task_description);
                tv_time = itemView.findViewById(R.id.tv_time);
                no_image = itemView.findViewById(R.id.no_image);
                tv_status = itemView.findViewById(R.id.tv_status);
                layout_contec = itemView.findViewById(R.id.layout_contec);
                image_icon = itemView.findViewById(R.id.image_icon);
                iv_labal = itemView.findViewById(R.id.iv_labal);
            }
        }
    }

     void Mail_Checklist (ManualTaskModel item) throws JSONException{
      //  loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel= SessionManager.getGetUserdata(getActivity());
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());
        paramObject.addProperty("include_smtp",1);
        obj.add("data", paramObject);
        retrofitCalls.Mail_list(sessionManager,obj, loadingDialog, token,Global.getVersionname(getActivity()),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    SessionManager.setUserLinkedGmail(getActivity(),new ArrayList<>());
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UserLinkedList>() {
                    }.getType();
                    UserLinkedList userLinkedGmail=new Gson().fromJson(headerString, listType);

                    List<UserLinkedList.UserLinkedGmail>  List=userLinkedGmail.getUserLinkedGmail();
                    SessionManager.setUserLinkedGmail(getActivity(),List);
                    if (List.size() == 0) {
                        Global.Messageshow(getActivity(),mMainLayout,getResources().getString(R.string.setting_mail),false);
                    }else {
                        if(List.size()==1){
                            if(List.get(0).getIsDefault()==1){
                            /*    if (item.getType().toString().equals("SMS")) {
                                    Intent intent = new Intent(getActivity(), Item_List_Text_Detail_Activty.class);
                                    intent.putExtra("record_id", item.getId());
                                    startActivity(intent);
                                } else {*/
                                    Intent intent = new Intent(getActivity(), Item_List_Email_Detail_activty.class);
                                    intent.putExtra("record_id", item.getId());
                                    startActivity(intent);
                               /* }*/
                            }else {
                                Global.Messageshow(getActivity(),mMainLayout,getResources().getString(R.string.setting_mail_defoult),false);
                            }
                        }else {
                           /* if (item.getType().toString().equals("SMS")) {

                                Intent intent = new Intent(getActivity(), Item_List_Text_Detail_Activty.class);
                                intent.putExtra("record_id", item.getId());
                                startActivity(intent);
                            } else {*/

                                Intent intent = new Intent(getActivity(), Item_List_Email_Detail_activty.class);
                                intent.putExtra("record_id", item.getId());
                                startActivity(intent);
                           /* }*/
                        }

                    }
                }else {
                    Global.Messageshow(getActivity(),mMainLayout,getResources().getString(R.string.setting_mail),false);
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }


    public void showAlertDialogButtonClicked(String title, String dis, String type,Integer id) {

        // Create an alert builder
        androidx.appcompat.app.AlertDialog.Builder builder
                = new androidx.appcompat.app.AlertDialog.Builder(getActivity(), R.style.MyDialogStyle);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.logout_dialog, null);
        builder.setView(customLayout);
        androidx.appcompat.app.AlertDialog dialog
                = builder.create();

        TextView tv_title = customLayout.findViewById(R.id.tv_title);
        TextView tv_sub_titale = customLayout.findViewById(R.id.tv_sub_titale);
        TextView tv_ok = customLayout.findViewById(R.id.tv_ok);
        tv_title.setText(title);
        tv_sub_titale.setText(dis);
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
                dialog.dismiss();
                try {

                    switch (type) {
                        case "PAUSED":
                            SMSAPI(type,id);
                            break;
                        case "NOT_STARTED":
                            SMSAPI(type,id);
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        dialog.show();
    }

    private void SMSAPI(String type,Integer id) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("team_id", 1);
        paramObject.put("organization_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("id", id);
        switch (type) {
            case "PAUSED":
                paramObject.put("status", "PAUSED");
                break;
            case "NOT_STARTED":
                paramObject.put("status", "NOT_STARTED");
                break;
        }
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Gson Data is", new Gson().toJson(gsonObject));


        retrofitCalls.active_task_update(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                if (response.body().getHttp_status() == 200) {
                    loadingDialog.cancelLoading();
                    ev_search.setText("");
                    iv_cancle_search_icon.setVisibility(View.GONE);
                    iv_filter_icon.setVisibility(View.VISIBLE);
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                  //  Filter = "";
                    refresf_api();

                }
                else if (response.body().getHttp_status()==403)
                {
                    Global.Messageshow(getActivity(),mMainLayout,getResources().getString(R.string.plan_validation),false);
                }


                else {
                    loadingDialog.cancelLoading();
                }


            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });
    }

}