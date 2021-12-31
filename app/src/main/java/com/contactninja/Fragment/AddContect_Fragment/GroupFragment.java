package com.contactninja.Fragment.AddContect_Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.contactninja.Auth.SignupActivity;
import com.contactninja.Group.GroupActivity;
import com.contactninja.Group.SendBroadcast;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Response;


public class GroupFragment extends Fragment implements View.OnClickListener {


    LinearLayout main_layout, add_new_contect_layout, group_name;
    SessionManager sessionManager;
    RecyclerView group_recyclerView;
    LinearLayoutManager layoutManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    TextView num_count;
    int page = 1, limit = 10, totale_group;
    PaginationAdapter paginationAdapter;
    int currentPage = 1, TOTAL_PAGES = 10;
    boolean isLoading = false;
    boolean isLastPage = false;
    private List<Grouplist.Group> grouplists;
    // private GroupAdapter groupAdapter;
    private ProgressBar loadingPB;
    LinearLayout mMainLayout1,demo_layout;
    LinearLayout mMainLayout;
    public GroupFragment() {
        // Required empty public constructor
    }

    SwipeRefreshLayout swipeToRefresh;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        IntentUI(view);
        sessionManager = new SessionManager(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        loadingDialog = new LoadingDialog(getActivity());


        SessionManager.setGroupList(getActivity(), new ArrayList<>());
       try {
           if(Global.isNetworkAvailable(getActivity(),mMainLayout)) {
               GroupEvent();
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        paginationAdapter = new PaginationAdapter(getActivity());
        group_recyclerView.setAdapter(paginationAdapter);

        SessionManager.setGroupList(getActivity(), new ArrayList<>());


        add_new_contect_layout.setOnClickListener(this);
        group_name.setOnClickListener(this);
        demo_layout.setOnClickListener(this);


        group_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItem = layoutManager.getChildCount();
                int totalItem = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (!isLoading && !isLastPage) {
                    if ((visibleItem + firstVisibleItemPosition) >= totalItem && firstVisibleItemPosition >= 0 && totalItem >= currentPage) {
                        try {
                            currentPage=currentPage + 1;
                            if(Global.isNetworkAvailable(getActivity(),mMainLayout)) {
                                GroupEvent1();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                paginationAdapter = new PaginationAdapter(getActivity());
                group_recyclerView.setAdapter(paginationAdapter);

                try {
                    if(Global.isNetworkAvailable(getActivity(),mMainLayout)) {
                        GroupEvent();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void IntentUI(View view) {

     /*   main_layout = view.findViewById(R.id.main_layout);*/
        add_new_contect_layout = view.findViewById(R.id.add_new_contect_layout);
        group_recyclerView = view.findViewById(R.id.group_list);
        layoutManager = new LinearLayoutManager(getActivity());
        group_recyclerView.setLayoutManager(layoutManager);
        group_name = view.findViewById(R.id.group_name);
        num_count = view.findViewById(R.id.num_count);
        loadingPB = view.findViewById(R.id.idPBLoading);
        grouplists = new ArrayList<>();
        mMainLayout1=view.findViewById(R.id.mMainLayout1);
        mMainLayout=view.findViewById(R.id.mMainLayout);
        demo_layout=view.findViewById(R.id.demo_layout);
        swipeToRefresh=view.findViewById(R.id.swipeToRefresh);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_contect_layout:
                SessionManager.setGroupList(getActivity(),new ArrayList<>());
                SessionManager.setGroupData(getActivity(),new Grouplist.Group());
                startActivity(new Intent(getActivity(), GroupActivity.class));
              /*  getActivity().finish();*/
                break;
            case R.id.group_name:
                startActivity(new Intent(getActivity(), SendBroadcast.class));
                break;
            case R.id.demo_layout:
                SessionManager.setGroupList(getActivity(),new ArrayList<>());
                SessionManager.setGroupData(getActivity(),new Grouplist.Group());
                startActivity(new Intent(getActivity(), GroupActivity.class));
                break;


        }

    }

    private void GroupEvent() throws JSONException {


        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        String token = Global.getToken(sessionManager);
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", "1");
        paramObject.put("team_id", "1");
        paramObject.put("user_id", user_id);
        paramObject.put("page", page);
        paramObject.put("perPage", limit);
        paramObject.put("q", "");
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Obbject data", new Gson().toJson(gsonObject));
        retrofitCalls.Group_List(sessionManager,gsonObject, loadingDialog, token,Global.getVersionname(getActivity()),Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                swipeToRefresh.setRefreshing(false);
                loadingDialog.cancelLoading();
                if (response.body().getStatus() == 200) {
                    Gson gson = new Gson();
                    grouplists.clear();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<Grouplist>() {
                    }.getType();
                    Grouplist group_model = new Gson().fromJson(headerString, listType);
                    grouplists.addAll(group_model.getGroups());
                    paginationAdapter.addAll(grouplists);
                    if (group_model.getGroups().size() == limit) {
                        if (currentPage <= TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                        else isLastPage = true;
                    } else {
                        isLastPage = true;
                        isLoading = false;

                    }

                    num_count.setText("" + group_model.getTotal() + " Group");

                    totale_group = group_model.getTotal();


                } else {
                    demo_layout.setVisibility(View.VISIBLE);
                    mMainLayout1.setVisibility(View.GONE);
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                swipeToRefresh.setRefreshing(false);
                loadingDialog.cancelLoading();
            }
        });


    }


    private void GroupEvent1() throws JSONException {
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        String token = Global.getToken(sessionManager);
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", "1");
        paramObject.put("team_id", "1");
        paramObject.put("user_id", user_id);
        paramObject.put("page", page);
        paramObject.put("perPage", limit);
        paramObject.put("q", "");
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        //Log.e("Obbject data", new Gson().toJson(gsonObject));
        retrofitCalls.Group_List(sessionManager,gsonObject, loadingDialog, token, Global.getVersionname(getActivity()),Global.Device,new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getStatus() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<Grouplist>() {
                    }.getType();

                    grouplists.clear();
                    paginationAdapter.removeLoadingFooter();
                    Grouplist group_model = new Gson().fromJson(headerString, listType);
                    grouplists.addAll(group_model.getGroups());
                    paginationAdapter.addAll(grouplists);
                    if (group_model.getGroups().size() == limit) {
                        if (currentPage != TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                        else isLastPage = true;
                    } else {
                        isLastPage = true;
                        isLoading = false;
                    }

                    num_count.setText("" + group_model.getTotal() + " Group");

                } else {

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }


    public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int LOADING = 0;
        private static final int ITEM = 1;
        private final Context context;
        private List<Grouplist.Group> movieList;
        private boolean isLoadingAdded = false;

        public PaginationAdapter(Context context) {
            this.context = context;
            movieList = new LinkedList<>();
        }

        public void setMovieList(List<Grouplist.Group> movieList) {
            this.movieList = movieList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case ITEM:
                    View viewItem = inflater.inflate(R.layout.group_list, parent, false);
                    viewHolder = new MovieViewHolder(viewItem);
                    break;
                case LOADING:
                    View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                    viewHolder = new LoadingViewHolder(viewLoading);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            Grouplist.Group Group_data = movieList.get(position);
            switch (getItemViewType(position)) {
                case ITEM:
                    MovieViewHolder movieViewHolder = (MovieViewHolder) holder;

                    movieViewHolder.group_name.setText(Group_data.getGroupName());
                    Glide.with(context).
                            load(Group_data.getGroupImage()).
                            placeholder(R.drawable.shape_primary_back).
                            error(R.drawable.shape_primary_back).into(movieViewHolder.group_image);
                    movieViewHolder.group_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SessionManager.setGroupData(context, Group_data);
                            startActivity(new Intent(getActivity(), SendBroadcast.class));
                            //getActivity().finish();
                        }
                    });
                    break;

                case LOADING:
                    LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
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
            add(new Grouplist.Group());
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = movieList.size() - 1;
            Grouplist.Group result = getItem(position);

            if (result != null) {
                movieList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void add(Grouplist.Group movie) {
            movieList.add(movie);
            notifyItemInserted(movieList.size() - 1);
        }

        public void addAll(List<Grouplist.Group> moveResults) {
            for (Grouplist.Group result : moveResults) {
                add(result);
            }
        }

        public Grouplist.Group getItem(int position) {
            return movieList.get(position);
        }


        public class MovieViewHolder extends RecyclerView.ViewHolder {
            private final TextView group_name;
            private final RoundedImageView group_image;
            LinearLayout group_layout;

            public MovieViewHolder(View itemView) {
                super(itemView);
                group_name = itemView.findViewById(R.id.group_name);
                group_layout = itemView.findViewById(R.id.group_layout);
                group_image = itemView.findViewById(R.id.group_image);
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

    public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        public PaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading() && !isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    loadMoreItems();
                }
            }
        }

        protected abstract void loadMoreItems();

        public abstract boolean isLastPage();

        public abstract boolean isLoading();

    }
}






