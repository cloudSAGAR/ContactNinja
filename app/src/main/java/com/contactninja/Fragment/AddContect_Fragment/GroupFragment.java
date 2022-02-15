package com.contactninja.Fragment.AddContect_Fragment;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

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
import android.widget.EditText;
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
import com.contactninja.Group.GroupActivity;
import com.contactninja.Group.SendBroadcast;
import com.contactninja.MainActivity;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.PaginationListener;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

@SuppressLint("UnknownNullness,SyntheticAccessor,SetTextI18n,StaticFieldLeak")
public class GroupFragment extends Fragment implements View.OnClickListener {
    private long mLastClickTime = 0;
    LinearLayout main_layout, add_new_contect_layout, group_name;
    SessionManager sessionManager;
    RecyclerView group_recyclerView;
    LinearLayoutManager layoutManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    TextView num_count;
    PaginationAdapter paginationAdapter;
    int currentPage = 1;
    int perPage = 20;
    boolean isLoading = false;
    boolean isLastPage = false;
    private List<Grouplist.Group> grouplists=new ArrayList<>();
    // private GroupAdapter groupAdapter;
    private ProgressBar loadingPB;
    LinearLayout mMainLayout1, demo_layout,mMainLayout;
    EditText ev_search;

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

        paginationAdapter = new PaginationAdapter(getActivity());
        group_recyclerView.setAdapter(paginationAdapter);

        add_new_contect_layout.setOnClickListener(this);
        group_name.setOnClickListener(this);
        demo_layout.setOnClickListener(this);


        group_recyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                try {
                    if (Global.isNetworkAvailable(getActivity(), MainActivity.mMainLayout)) {
                        GroupEvent();
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


        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("SyntheticAccessor")
            @Override
            public void onRefresh() {
                paginationAdapter = new PaginationAdapter(getActivity());
                group_recyclerView.setAdapter(paginationAdapter);

                ev_search.setText("");
                currentPage = PAGE_START;
                isLastPage = false;
                grouplists.clear();
                paginationAdapter.clear();

                try {
                    if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                        GroupEvent();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        ev_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                   Global.hideKeyboard(getActivity());
                    onResume();
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    private void IntentUI(View view) {

        /*   main_layout = view.findViewById(R.id.main_layout);*/
        add_new_contect_layout = view.findViewById(R.id.add_new_contect_layout);
        ev_search = view.findViewById(R.id.ev_search);
        group_recyclerView = view.findViewById(R.id.group_list);
        layoutManager = new LinearLayoutManager(getActivity());
        group_recyclerView.setLayoutManager(layoutManager);
        group_name = view.findViewById(R.id.group_name);
        num_count = view.findViewById(R.id.num_count);
        loadingPB = view.findViewById(R.id.idPBLoading);
        grouplists = new ArrayList<>();
        mMainLayout1 = view.findViewById(R.id.mMainLayout1);
        mMainLayout = view.findViewById(R.id.mMainLayout);
        demo_layout = view.findViewById(R.id.demo_layout);
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh);

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_contect_layout:
            case R.id.demo_layout:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (SessionManager.getContectList(getActivity()).size() != 0) {
                    SessionManager.setGroupList(getActivity(), new ArrayList<>());
                    SessionManager.setGroupData(getActivity(), new Grouplist.Group());
                    startActivity(new Intent(getActivity(), GroupActivity.class));
                } else {
                    Global.Messageshow(getActivity(), MainActivity.mMainLayout, getActivity().getResources().getString(R.string.add_contact), false);
                }

                /*  getActivity().finish();*/
                break;
            case R.id.group_name:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                startActivity(new Intent(getActivity(), SendBroadcast.class));
                break;


        }

    }

    @Override
    public void onResume() {
        super.onResume();
        SessionManager.setGroupList(getActivity(), new ArrayList<>());
        paginationAdapter = new PaginationAdapter(getActivity());
        group_recyclerView.setAdapter(paginationAdapter);
        //loadingDialog.showLoadingDialog();
        currentPage = PAGE_START;
        isLastPage = false;
        grouplists.clear();
        paginationAdapter.clear();
        try {
            if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                GroupEvent();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void GroupEvent() throws JSONException {

        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String token = Global.getToken(sessionManager);
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("page", currentPage);
        paramObject.put("perPage", perPage);
        paramObject.put("q", ev_search.getText().toString());
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Obbject data", new Gson().toJson(gsonObject));
        retrofitCalls.Group_List(sessionManager, gsonObject, loadingDialog, token, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                swipeToRefresh.setRefreshing(false);
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<Grouplist>() {
                    }.getType();
                    Grouplist group_model = new Gson().fromJson(headerString, listType);
                    grouplists.addAll(group_model.getGroups());

                    if (currentPage != PAGE_START) {
                        paginationAdapter.removeLoadingFooter();
                    }
                    paginationAdapter.addAll(grouplists);
                    // check weather is last page or not
                    if (group_model.getTotal() > paginationAdapter.getItemCount()) {
                        paginationAdapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                    isLoading = false;
                    num_count.setText(String.valueOf(group_model.getTotal() + " Group"));

                    demo_layout.setVisibility(View.GONE);
                    mMainLayout1.setVisibility(View.VISIBLE);
                } else {
                    num_count.setText(String.valueOf(0 + " Group"));
                    if(ev_search.getText().toString().equals("")){
                        demo_layout.setVisibility(View.VISIBLE);
                        mMainLayout1.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                swipeToRefresh.setRefreshing(false);
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

        public PaginationAdapter(@SuppressLint("UnknownNullness") Context context) {
            this.context = context;
            movieList = new LinkedList<>();
        }

        public void setMovieList(@SuppressLint("UnknownNullness") List<Grouplist.Group> movieList) {
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
                    View viewLoading = inflater.inflate(R.layout.item_loading, parent, false);
                    viewHolder = new LoadingViewHolder(viewLoading);
                    break;
            }
            return viewHolder;
        }

        @SuppressLint("SyntheticAccessor")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            Grouplist.Group Group_data = movieList.get(position);
            switch (getItemViewType(position)) {
                case ITEM:
                    MovieViewHolder movieViewHolder = (MovieViewHolder) holder;

                    movieViewHolder.group_name.setText(Group_data.getGroupName());


                    if (Group_data.getGroupImage() == null) {
                        String name = Group_data.getGroupName();
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


                        movieViewHolder.no_image.setText(add_text);
                        movieViewHolder.no_image.setVisibility(View.VISIBLE);
                        movieViewHolder.group_image.setVisibility(View.GONE);
                    } else {
                        Glide.with(context).
                                load(Group_data.getGroupImage()).
                                placeholder(R.drawable.shape_primary_back).
                                error(R.drawable.shape_primary_back).into(movieViewHolder.group_image);

                        movieViewHolder.no_image.setVisibility(View.GONE);
                        movieViewHolder.group_image.setVisibility(View.VISIBLE);
                    }


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
        public void clear() {
            movieList.clear();
            notifyDataSetChanged();
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

        @SuppressLint("NotifyDataSetChanged")
        public void addAll(List<Grouplist.Group> moveResults) {
            movieList.addAll(moveResults);
            notifyDataSetChanged();
        }

        public Grouplist.Group getItem(int position) {
            return movieList.get(position);
        }


        public class MovieViewHolder extends RecyclerView.ViewHolder {
            private final TextView group_name, no_image;
            private final CircleImageView group_image;
            LinearLayout group_layout;

            public MovieViewHolder(View itemView) {
                super(itemView);
                no_image = itemView.findViewById(R.id.no_image);
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

    public abstract static class PaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        public PaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
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






