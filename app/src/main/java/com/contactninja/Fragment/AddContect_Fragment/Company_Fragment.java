package com.contactninja.Fragment.AddContect_Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

import com.contactninja.AddContect.Add_Company_Activity;
import com.contactninja.MainActivity;
import com.contactninja.Model.CampaignTask_overview;
import com.contactninja.Model.CompanyModel;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
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
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class Company_Fragment extends Fragment {
    ConstraintLayout mMainLayout;
    TextView tv_create;
    BottomSheetDialog bottomSheetDialog_fillter;
    Context mCtx;
    RecyclerView rvinviteuserdetails;
    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    SearchView contect_search;
    TextView add_new_contect, num_count,txt_nolist;
    ImageView add_new_contect_icon, iv_filter_icon,iv_cancle_search_icon;
    View view1;
    FragmentActivity fragmentActivity;
    LinearLayout add_new_contect_layout;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    int currentPage = 1;
    int perPage = 20;
    boolean isLoading = false;
    boolean isLastPage = false;
    LinearLayoutManager layoutManager;
    SwipeRefreshLayout swipeToRefresh;
    EditText ev_search;
    CompanyAdapter companyAdapter;
    List<CompanyModel.Company> companyList = new ArrayList<>();
    String Filter = "";//BLOCK / ALL
    LinearLayout demo_layout, linearLayout3;
    private long mLastClickTime = 0;
    LinearLayout lay_no_list,layout_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View content_view = inflater.inflate(R.layout.fragment_company_, container, false);
        IntentUI(content_view);
        mCtx = getContext();
        sessionManager = new SessionManager(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        companyAdapter = new CompanyAdapter(getActivity(), new ArrayList<>());

        rvinviteuserdetails.setLayoutManager(layoutManager);
        rvinviteuserdetails.setHasFixedSize(true);
        rvinviteuserdetails.setAdapter(companyAdapter);
        rvinviteuserdetails.setItemViewCacheSize(50000);
        fastscroller_thumb.setupWithFastScroller(fastscroller);
        fastscroller.setUseDefaultScroller(false);

        fastscroller.getItemIndicatorSelectedCallbacks().add(
                new FastScrollerView.ItemIndicatorSelectedCallback() {
                    @Override
                    public void onItemIndicatorSelected(
                            FastScrollItemIndicator indicator,
                            int indicatorCenterY,
                            int itemPosition
                    ) {

                    }
                }
        );
        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter);
                    iv_cancle_search_icon.setVisibility(View.GONE);
                    iv_filter_icon.setVisibility(View.VISIBLE);
                    ev_search.setText("");
                    currentPage = PAGE_START;
                    isLastPage = false;
                    companyList.clear();
                    companyAdapter.clear();
                    try {
                        CompanyList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        rvinviteuserdetails.setItemViewCacheSize(50000);
        rvinviteuserdetails.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                try {
                    if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                        CompanyList();
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
        add_new_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                SessionManager.setCampaign_data(new CampaignTask_overview());
                Intent intent = new Intent(getActivity(), Add_Company_Activity.class);
                intent.putExtra("flag", "add");
                startActivity(intent);
            }
        });
        demo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                SessionManager.setCampaign_data(new CampaignTask_overview());
                Intent intent = new Intent(getActivity(), Add_Company_Activity.class);
                intent.putExtra("flag", "add");
                startActivity(intent);
            }
        });
        add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                SessionManager.setCampaign_data(new CampaignTask_overview());
                Intent intent = new Intent(getActivity(), Add_Company_Activity.class);
                intent.putExtra("flag", "add");
                startActivity(intent);
            }
        });
        add_new_contect_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                SessionManager.setCampaign_data(new CampaignTask_overview());
                Intent intent = new Intent(getActivity(), Add_Company_Activity.class);
                intent.putExtra("flag", "add");
                startActivity(intent);
            }
        });

        ev_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Global.hideKeyboard(getActivity());
                    iv_cancle_search_icon.setVisibility(View.VISIBLE);
                    iv_filter_icon.setVisibility(View.GONE);
                    onResume();
                    return true;
                }
                return false;
            }
        });

        fastscroller.setupWithRecyclerView(
                rvinviteuserdetails,
                (position) -> {

                    try {
                        FastScrollItemIndicator fastScrollItemIndicator = new FastScrollItemIndicator.Text(
                                companyAdapter.getItem(position).getName().substring(0, 1)
                                        .substring(0, 1)
                                        .toUpperCase()// Grab the first letter and capitalize it
                        );
                        return fastScrollItemIndicator;
                    } catch (Exception e) {
                        return null;
                    }

                }
        );

      /*  if (companyAdapter.getItemCount() == 0) {
            linearLayout3.setVisibility(View.GONE);
            demo_layout.setVisibility(View.VISIBLE);
            layout_common.setVisibility(View.VISIBLE);

        }*/

        return content_view;
    }


    private void IntentUI(View content_view) {
        iv_cancle_search_icon = content_view.findViewById(R.id.iv_cancle_search_icon);
        txt_nolist = content_view.findViewById(R.id.txt_nolist);
        layout_list = content_view.findViewById(R.id.layout_list);
        lay_no_list = content_view.findViewById(R.id.lay_no_list);
        linearLayout3 = content_view.findViewById(R.id.linearLayout3);
        tv_create = content_view.findViewById(R.id.tv_create);
        tv_create.setText(getResources().getString(R.string.create_company));
        demo_layout = content_view.findViewById(R.id.demo_layout);
        iv_filter_icon = content_view.findViewById(R.id.iv_filter_icon);
        mMainLayout = content_view.findViewById(R.id.mMainLayout);
        rvinviteuserdetails = content_view.findViewById(R.id.contact_list);
        fastscroller = content_view.findViewById(R.id.fastscroller);
        fastscroller_thumb = content_view.findViewById(R.id.fastscroller_thumb);
        contect_search = content_view.findViewById(R.id.contect_search);
        add_new_contect = content_view.findViewById(R.id.add_new_contect);
        num_count = content_view.findViewById(R.id.num_count);
        add_new_contect_icon = content_view.findViewById(R.id.add_new_contect_icon);
        add_new_contect_layout = content_view.findViewById(R.id.add_new_contect_layout);
        swipeToRefresh = content_view.findViewById(R.id.swipeToRefresh);
        ev_search = content_view.findViewById(R.id.ev_search);
        iv_filter_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                showBottomSheetDialog_Filtter();
            }
        });
        iv_cancle_search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ev_search.setText("");
                iv_cancle_search_icon.setVisibility(View.GONE);
                iv_filter_icon.setVisibility(View.VISIBLE);
                onResume();
            }
        });

    }


    void showBottomSheetDialog_Filtter() {
        /*
        Change By :- Paras
        Chnage Date:- 4-2-22
        */
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.fillter_contact_block_unblock, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);


        CheckBox ch_block = bottomSheetDialog.findViewById(R.id.ch_block);
        CheckBox ch_all = bottomSheetDialog.findViewById(R.id.ch_all);
        switch (Filter) {
            case "BLOCK":
                ch_block.setChecked(true);
                break;
            case "ALL":
                ch_all.setChecked(true);
                break;

        }
        ch_block.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    Filter = "BLOCK";
                    RefreshList();
//
                }

            }
        });

        ch_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bottomSheetDialog.dismiss();
                    iv_filter_icon.setImageResource(R.drawable.ic_filter);
                    Filter = "All";
                    RefreshList();

                }

            }
        });


        bottomSheetDialog.show();

    }

    private void CompanyList() throws JSONException {

        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("q", ev_search.getText().toString());
        paramObject.addProperty("perPage", perPage);
        paramObject.addProperty("page", currentPage);
        paramObject.addProperty("orderBy", "name");
        paramObject.addProperty("order", "ASC");
        if (Filter.equals("BLOCK")) {
            paramObject.addProperty("is_blocked", 1);
        }
        obj.add("data", paramObject);
        retrofitCalls.CompanyList(sessionManager, obj, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                //   Log.e("Response is",new Gson().toJson(response));
                if (response.body().getHttp_status().equals(200)) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    //    sessionManager.setCompanylist(getActivity(), new ArrayList<>());
                    Type listType = new TypeToken<CompanyModel>() {
                    }.getType();
                    CompanyModel data = new Gson().fromJson(headerString, listType);
                    num_count.setText(String.valueOf(data.getTotal() + " Company"));
                    List<CompanyModel.Company> companyList;
                    if (Filter.equals("BLOCK")) {
                        companyList = data.getBlocked_companies();

                    } else {

                        companyList = data.getData();
                        lay_no_list.setVisibility(View.GONE);
                        layout_list.setVisibility(View.VISIBLE);

                    }

                    Collections.sort(companyList, new Comparator<CompanyModel.Company>() {
                        @Override
                        public int compare(CompanyModel.Company s1, CompanyModel.Company s2) {
                            return s1.getName().compareToIgnoreCase(s2.getName());
                        }
                    });
                    if (currentPage != PAGE_START)
                        companyAdapter.removeLoading();
                        companyAdapter.addItems(companyList);
                        rvinviteuserdetails.setItemViewCacheSize(50000);

                    // check weather is last page or not
                    if (data.getTotal() > companyAdapter.getItemCount()) {
                        companyAdapter.addLoading();
                    } else {
                        isLastPage = true;
                    }
                    isLoading = false;
                    swipeToRefresh.setRefreshing(false);
                    linearLayout3.setVisibility(View.VISIBLE);
                    demo_layout.setVisibility(View.GONE);

                } else {
                    if (Filter.equals("") && ev_search.getText().toString().equals("")) {
                        linearLayout3.setVisibility(View.GONE);
                        demo_layout.setVisibility(View.VISIBLE);
                    }

                    if (Filter.equals("BLOCK")) {
                        if (companyList.size() == 0) {
                            txt_nolist.setText(getResources().getString(R.string.no_block_company));
                            lay_no_list.setVisibility(View.VISIBLE);
                            layout_list.setVisibility(View.GONE);
                        } else {
                            lay_no_list.setVisibility(View.GONE);
                            layout_list.setVisibility(View.VISIBLE);
                        }
                    }else {
                        if (companyList.size() == 0) {
                            txt_nolist.setText(getResources().getString(R.string.no_company));
                            lay_no_list.setVisibility(View.VISIBLE);
                            layout_list.setVisibility(View.GONE);
                        }else {
                            lay_no_list.setVisibility(View.GONE);
                            layout_list.setVisibility(View.VISIBLE);
                        }
                    }
                    num_count.setText(String.valueOf(0 + " Company"));
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                linearLayout3.setVisibility(View.GONE);
                demo_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Filter = "";
        iv_filter_icon.setImageResource(R.drawable.ic_filter);
        RefreshList();
    }

    private void RefreshList() {
        currentPage = PAGE_START;
        isLastPage = false;
        companyList.clear();
        companyAdapter.clear();
        try {
            if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                CompanyList();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void broadcast_manu(CompanyModel.Company Company) {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.remove_block_layout, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        TextView selected_block = bottomSheetDialog.findViewById(R.id.selected_block);
        View line_block = bottomSheetDialog.findViewById(R.id.line_block);
        View line_unblock = bottomSheetDialog.findViewById(R.id.line_unblock);
        TextView selected_un_block = bottomSheetDialog.findViewById(R.id.selected_unblock);
        TextView selected_delete = bottomSheetDialog.findViewById(R.id.selected_delete);
        selected_block.setText(getString(R.string.add_blacklist));
        selected_un_block.setText(getString(R.string.remove_blacklist));
        selected_delete.setText(getString(R.string.delete_company));

        if (Company.getIs_blocked().equals(1)) {
            selected_block.setVisibility(View.GONE);
            line_block.setVisibility(View.GONE);
            selected_un_block.setVisibility(View.VISIBLE);
            line_unblock.setVisibility(View.VISIBLE);
        } else {
            line_block.setVisibility(View.VISIBLE);
            selected_block.setVisibility(View.VISIBLE);
            selected_un_block.setVisibility(View.GONE);
            line_unblock.setVisibility(View.GONE);
        }


        selected_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                //Block Contect

                try {
                    Contect_BLock(Company, "1", bottomSheetDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        selected_un_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                //Block Contect

                try {
                    Contect_BLock(Company, "0", bottomSheetDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        selected_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                //Block Contect

                try {
                    Company_Remove(Company, "0", bottomSheetDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        bottomSheetDialog.show();

    }

    public void Contect_BLock(CompanyModel.Company Company, String block, BottomSheetDialog bottomSheetDialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("is_block", block);
        JSONArray block_array = new JSONArray();
        block_array.put(Company.getId());
        paramObject.put("blockCompanyIds", block_array);
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Main Data is ", new Gson().toJson(gsonObject));
        retrofitCalls.Block_Company(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Global.Messageshow(getActivity(), mMainLayout, response.body().getMessage(), true);
                    try {
                        companyAdapter.removeitem();
                        currentPage = 1;
                        CompanyList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Global.Messageshow(getActivity(), mMainLayout, response.body().getMessage(), false);
                }
                bottomSheetDialog.cancel();
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                bottomSheetDialog.cancel();
            }
        });

    }

    public void Company_Remove(CompanyModel.Company Company, String block, BottomSheetDialog bottomSheetDialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("id", Company.getId());
        paramObject.put("status", "D");
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Main Data is ", new Gson().toJson(gsonObject));
        retrofitCalls.Company_add(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Global.Messageshow(getActivity(), mMainLayout, response.body().getMessage(), false);
                    try {
                        companyAdapter.removeitem();
                        currentPage = 1;
                        CompanyList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bottomSheetDialog.cancel();
                } else {
                    Global.Messageshow(getActivity(), mMainLayout, response.body().getMessage(), false);
                    bottomSheetDialog.cancel();
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                bottomSheetDialog.cancel();
            }
        });

    }

    public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.viewData> {
        private static final int VIEW_TYPE_LOADING = 0;
        private static final int VIEW_TYPE_NORMAL = 1;
        public Context mCtx;
        TextView phone_txt;
        Contactdetail item;
        String second_latter = "", current_latter = "";
        private boolean isLoaderVisible = false;
        private List<CompanyModel.Company> companyList;

        public CompanyAdapter(Context context, List<CompanyModel.Company> companyList) {
            this.mCtx = context;
            this.companyList = companyList;
        }

        @NonNull
        @Override
        public CompanyAdapter.viewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_NORMAL:
                    return new CompanyAdapter.viewData(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.comany_item_layout, parent, false));
                case VIEW_TYPE_LOADING:
                    return new CompanyAdapter.ProgressHolder(
                            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
                default:
                    return null;
            }

        }

        @Override
        public int getItemViewType(int position) {
            if (isLoaderVisible) {
                return position == companyList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
            } else {
                return VIEW_TYPE_NORMAL;
            }
        }

        public void addItems(List<CompanyModel.Company> postItems) {
            companyList.addAll(postItems);
            rvinviteuserdetails.setItemViewCacheSize(50000);
            notifyDataSetChanged();


        }

        public void addLoading() {
            isLoaderVisible = true;
            companyList.add(new CompanyModel.Company());
            rvinviteuserdetails.setItemViewCacheSize(50000);
            notifyItemInserted(companyList.size() - 1);
        }

        public void removeLoading() {
            isLoaderVisible = false;
            int position = companyList.size() - 1;
            CompanyModel.Company item = getItem(position);
            if (item != null) {
                companyList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            companyList.clear();
            notifyDataSetChanged();
        }


        CompanyModel.Company getItem(int position) {
            return companyList.get(position);
        }

        @Override
        public void onBindViewHolder(@NonNull CompanyAdapter.viewData holder, int position) {
            CompanyModel.Company WorkData = companyList.get(position);
            if (Global.IsNotNull(WorkData) && !WorkData.getName().equals("")) {
                try {
                    if (WorkData.getIs_blocked().equals(1)) {
                        holder.iv_block.setVisibility(View.VISIBLE);
                        holder.userName.setTextColor(mCtx.getResources().getColor(R.color.block_item));
                    } else {
                        holder.iv_block.setVisibility(View.GONE);
                        holder.userName.setTextColor(mCtx.getResources().getColor(R.color.unblock_item));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                holder.userName.setText(WorkData.getName());
                holder.userNumber.setVisibility(View.GONE);

                holder.first_latter.setVisibility(View.VISIBLE);
                holder.top_layout.setVisibility(View.VISIBLE);


                String first_latter = WorkData.getName().substring(0, 1).toUpperCase();
                holder.first_latter.setText(first_latter);
                if (second_latter.equals("")) {
                    current_latter = first_latter;
                    second_latter = first_latter;
                    holder.first_latter.setVisibility(View.GONE);
                    holder.top_layout.setVisibility(View.GONE);

                } else if (second_latter.equals(first_latter)) {
                    current_latter = second_latter;
                    // inviteUserDetails.setF_latter("");
                    holder.first_latter.setVisibility(View.GONE);
                    holder.top_layout.setVisibility(View.GONE);

                } else {

                    current_latter = first_latter;
                    second_latter = first_latter;
                    holder.first_latter.setVisibility(View.GONE);
                    holder.top_layout.setVisibility(View.GONE);


                }


                String name = WorkData.getName();
                String add_text = "";
                String[] split_data = name.split(" ");
                try {
                    for (int i = 0; i < split_data.length; i++) {
                        if (i == 0) {
                            add_text = split_data[i].substring(0, 1);
                        } else {
                            add_text = add_text + split_data[i].charAt(0);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                holder.no_image.setText(add_text);
                holder.no_image.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);


                holder.main_layout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        broadcast_manu(WorkData);
                        return false;
                    }
                });
                holder.main_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        Intent intent = new Intent(getActivity(), Add_Company_Activity.class);
                        intent.putExtra("flag", "read");
                        intent.putExtra("id", WorkData.getId());
                        startActivity(intent);
                    }
                });

            }

        }

        @Override
        public int getItemCount() {
            return companyList.size();
        }

        public void removeitem() {
            companyList.clear();
            notifyDataSetChanged();
        }

        public class viewData extends RecyclerView.ViewHolder {
            TextView no_image;
            TextView userName, userNumber, first_latter;
            CircleImageView profile_image;
            LinearLayout top_layout;
            RelativeLayout main_layout;
            ImageView iv_block;


            public viewData(@NonNull View itemView) {
                super(itemView);
                first_latter = itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.top_layout);
                main_layout = itemView.findViewById(R.id.main_layout);
                iv_block = itemView.findViewById(R.id.iv_block);
            }
        }

        public class ProgressHolder extends CompanyAdapter.viewData {
            ProgressHolder(View itemView) {
                super(itemView);
            }

        }
    }


}