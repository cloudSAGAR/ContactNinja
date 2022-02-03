package com.contactninja.Fragment.AddContect_Fragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Add_Company_Activity;
import com.contactninja.AddContect.Addnewcontect_Activity;
import com.contactninja.Fragment.ContectFragment;
import com.contactninja.MainActivity;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.CampaignTask_overview;
import com.contactninja.Model.CompanyModel;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.PaginationListener;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitApiClient;
import com.contactninja.retrofit.RetrofitApiInterface;
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
import java.util.LinkedList;
import java.util.List;

import static com.contactninja.Utils.PaginationListener.PAGE_START;
public class Company_Fragment extends Fragment {
    ConstraintLayout mMainLayout;
    BottomSheetDialog bottomSheetDialog_fillter;
    Context mCtx;
    RecyclerView rvinviteuserdetails;
    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    SearchView contect_search;
    TextView add_new_contect, num_count;
    ImageView add_new_contect_icon,iv_filter_icon;
    View view1;
    FragmentActivity fragmentActivity;
    LinearLayout add_new_contect_layout;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    int currentPage = 1, TOTAL_PAGES = 10;
    boolean isLoading = false;
    boolean isLastPage = false;
    LinearLayoutManager layoutManager;
    SwipeRefreshLayout swipeToRefresh;
    EditText ev_search;
    CompanyAdapter companyAdapter;
    List<CompanyModel.Company> companyList=new ArrayList<>();
    int perPage = 20;

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
                SessionManager.setCampaign_data(new CampaignTask_overview());
                Intent intent=new Intent(getActivity(),Add_Company_Activity.class);
                intent.putExtra("flag","add");
                startActivity(intent);
            }
        });
        add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.setCampaign_data(new CampaignTask_overview());
                Intent intent=new Intent(getActivity(),Add_Company_Activity.class);
                intent.putExtra("flag","add");
                startActivity(intent);            }
        });
        add_new_contect_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.setCampaign_data(new CampaignTask_overview());
                Intent intent=new Intent(getActivity(),Add_Company_Activity.class);
                intent.putExtra("flag","add");
                startActivity(intent);            }
        });

        ev_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<CompanyModel.Company> temp = new ArrayList();
                for(CompanyModel.Company d: companyList){
                    if(d.getName().toLowerCase().contains(s.toString().toLowerCase())){
                        temp.add(d);
                        // Log.e("Same Data ",d.getUserName());
                    }
                }
                companyAdapter.updateList(temp);  }

            @Override
            public void afterTextChanged(Editable s) {

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

        return content_view;
    }


    private void IntentUI(View content_view) {
        iv_filter_icon=content_view.findViewById(R.id.iv_filter_icon);
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
             showBottomSheetDialog_Filtter();
            }
        });
    }


    void showBottomSheetDialog_Filtter() {
        bottomSheetDialog_fillter = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
        bottomSheetDialog_fillter.setContentView(R.layout.bottom_sheet_dialog_for_fillter_contact);
        TextView tv_clear=bottomSheetDialog_fillter.findViewById(R.id.tv_clear);
        TextView tv_done=bottomSheetDialog_fillter.findViewById(R.id.tv_done);
        TextView tv_item=bottomSheetDialog_fillter.findViewById(R.id.tv_item);
        TextView tv_blacklist=bottomSheetDialog_fillter.findViewById(R.id.tv_blacklist);
        TextView tv_allcompany=bottomSheetDialog_fillter.findViewById(R.id.tv_allcompany);
        tv_allcompany.setText(getString(R.string.company_all));
        tv_blacklist.setText(getString(R.string.company_blacklst));
        ImageView iv_unselect_blacklist=bottomSheetDialog_fillter.findViewById(R.id.iv_unselect_blacklist);
        ImageView iv_select_blacklist=bottomSheetDialog_fillter.findViewById(R.id.iv_select_blacklist);
        ImageView iv_unselect_all=bottomSheetDialog_fillter.findViewById(R.id.iv_unselect_all);
        ImageView iv_select_all=bottomSheetDialog_fillter.findViewById(R.id.iv_select_all);
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_unselect_blacklist.setVisibility(View.VISIBLE);
                iv_select_blacklist.setVisibility(View.GONE);
                iv_unselect_all.setVisibility(View.VISIBLE);
                iv_select_all.setVisibility(View.GONE);

            }
        });
        iv_unselect_blacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_unselect_blacklist.setVisibility(View.GONE);
                iv_select_blacklist.setVisibility(View.VISIBLE);
                bottomSheetDialog_fillter.cancel();
            }
        });
        iv_unselect_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_unselect_all.setVisibility(View.GONE);
                iv_select_all.setVisibility(View.VISIBLE);
                try {
                    companyAdapter.removeitem();
                    CompanyList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bottomSheetDialog_fillter.cancel();
            }
        });

        bottomSheetDialog_fillter.show();
    }

    private void CompanyList() throws JSONException {

        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("perPage", perPage);
        paramObject.addProperty("page", currentPage);
        obj.add("data", paramObject);
        retrofitCalls.CompanyList(sessionManager, obj, loadingDialog,  Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                //Log.e("Response is",new Gson().toJson(response));
                if(response.body().getHttp_status().equals(200)){
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    if (response.body().getHttp_status() == 200) {
                        //    sessionManager.setCompanylist(getActivity(), new ArrayList<>());
                        Type listType = new TypeToken<CompanyModel>() {
                        }.getType();
                        CompanyModel data = new Gson().fromJson(headerString, listType);
                        List<CompanyModel.Company> companyList=data.getData();
                        if (currentPage != PAGE_START) companyAdapter.removeLoading();
                        companyAdapter.addItems(companyList);
                        // check weather is last page or not
                        if (data.getTotal() > companyAdapter.getItemCount()) {
                            companyAdapter.addLoading();
                        } else {
                            isLastPage = true;
                        }
                        isLoading = false;
                        swipeToRefresh.setRefreshing(false);

                    } else {
                        // Global.Messageshow(getApplicationContext(), mMainLayout, headerString, false);

                    }

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
            }
        });
    }


    public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.viewData> {
        private static final int VIEW_TYPE_LOADING = 0;
        private static final int VIEW_TYPE_NORMAL = 1;
        private boolean isLoaderVisible = false;
        public Context mCtx;
        TextView phone_txt;
        Contactdetail item;
        String second_latter = "",current_latter = "";
        private List<CompanyModel.Company> companyList;

        public CompanyAdapter(Context context, List<CompanyModel.Company> companyList) {
            this.mCtx = context;
            this.companyList = companyList;
        }

        @NonNull
        @Override
        public CompanyAdapter.viewData  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
            notifyDataSetChanged();
        }

        public void addLoading() {
            isLoaderVisible = true;
            companyList.add(new CompanyModel.Company());
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
            if(Global.IsNotNull(WorkData)&&!WorkData.getName().equals("")){
                if (WorkData.getIs_blocked().equals("1"))
                {
                    holder.iv_block.setVisibility(View.VISIBLE);
                }
                else {
                    holder.iv_block.setVisibility(View.GONE);

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
                    holder.first_latter.setVisibility(View.VISIBLE);
                    holder.top_layout.setVisibility(View.VISIBLE);

                } else if (second_latter.equals(first_latter)) {
                    current_latter = second_latter;
                    // inviteUserDetails.setF_latter("");
                    holder.first_latter.setVisibility(View.GONE);
                    holder.top_layout.setVisibility(View.GONE);

                } else {

                    current_latter = first_latter;
                    second_latter = first_latter;
                    holder.first_latter.setVisibility(View.VISIBLE);
                    holder.top_layout.setVisibility(View.VISIBLE);


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
                        SessionManager.setCompnay_detail(WorkData);
                        Intent intent=new Intent(getActivity(),Add_Company_Activity.class);
                        intent.putExtra("flag","read");
                        startActivity(intent);
                    }
                });

               }

        }

        @Override
        public int getItemCount() {
            return companyList.size();
        }
        public void updateList(List<CompanyModel.Company> list) {
            companyList = list;
            notifyDataSetChanged();
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
                iv_block=itemView.findViewById(R.id.iv_block);
            }
        }

        public class ProgressHolder extends CompanyAdapter.viewData {
            ProgressHolder(View itemView) {
                super(itemView);
            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();
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
        View line_block=bottomSheetDialog.findViewById(R.id.line_block);
        View line_unblock=bottomSheetDialog.findViewById(R.id.line_unblock);
        TextView selected_un_block = bottomSheetDialog.findViewById(R.id.selected_unblock);
        TextView selected_delete=bottomSheetDialog.findViewById(R.id.selected_delete);
        selected_block.setText(getString(R.string.add_blacklist));
        selected_un_block.setText(getString(R.string.remove_blacklist));
        selected_delete.setText(getString(R.string.delete_contact));

        if (Company.getIs_blocked().equals("1"))
        {
            selected_block.setVisibility(View.GONE);
            line_block.setVisibility(View.GONE);
            selected_un_block.setVisibility(View.VISIBLE);
            line_unblock.setVisibility(View.VISIBLE);
        }
        else {
            line_block.setVisibility(View.VISIBLE);
            selected_block.setVisibility(View.VISIBLE);
            selected_un_block.setVisibility(View.GONE);
            line_unblock.setVisibility(View.GONE);
        }


        selected_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Block Contect

                try {
                    Contect_BLock(Company,"1",bottomSheetDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        selected_un_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Block Contect

                try {
                    Contect_BLock(Company,"0",bottomSheetDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        selected_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Block Contect

                try {
                    Company_Remove(Company,"0",bottomSheetDialog);
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
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", "1");
        paramObject.put("team_id", "1");
        paramObject.put("user_id", user_id);
        paramObject.put("is_block",block);
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
                        CompanyList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bottomSheetDialog.cancel();
                }
                else {
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



    public void Company_Remove(CompanyModel.Company Company, String block, BottomSheetDialog bottomSheetDialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", "1");
        paramObject.put("team_id", "1");
        paramObject.put("user_id", user_id);
        paramObject.put("id",Company.getId());
        paramObject.put("status","D");
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
                        CompanyList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bottomSheetDialog.cancel();
                }
                else {
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


}