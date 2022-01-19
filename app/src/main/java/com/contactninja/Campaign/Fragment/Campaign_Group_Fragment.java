package com.contactninja.Campaign.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Campaign_Group_Fragment extends Fragment implements View.OnClickListener {

    LinearLayout main_layout, add_new_contect_layout, group_name;
    SessionManager sessionManager;
    RecyclerView group_recyclerView;
    LinearLayoutManager layoutManager,layoutManager1;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    TextView num_count;
    int page = 1, limit = 10, totale_group;
    PaginationAdapter paginationAdapter;
    int currentPage = 1, TOTAL_PAGES = 10;
    boolean isLoading = false;
    boolean isLastPage = false;
    private List<Grouplist.Group> grouplists;
    List<Grouplist.Group> select_contectListData;
    // private GroupAdapter groupAdapter;
    private ProgressBar loadingPB;
    RecyclerView add_contect_list;
    public static TopUserListDataAdapter topUserListDataAdapter;
    TextView tv_create;
    LinearLayout mMainLayout;
    ImageView add_new_contect_icon1,add_new_contect_icon;
    TextView add_new_contect;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_broadcast__group_, container, false);
        IntentUI(view);
        sessionManager = new SessionManager(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        loadingDialog = new LoadingDialog(getActivity());

        select_contectListData=new ArrayList<>();
        grouplists=new ArrayList<>();
        SessionManager.setGroupList(getActivity(), new ArrayList<>());
        try {
            if(Global.isNetworkAvailable(getActivity(),mMainLayout)) {
                GroupEvent();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        topUserListDataAdapter=new TopUserListDataAdapter(getActivity(),getActivity(),select_contectListData);
        add_contect_list.setAdapter(topUserListDataAdapter);
        topUserListDataAdapter.notifyDataSetChanged();
        paginationAdapter = new PaginationAdapter(getActivity());
        group_recyclerView.setAdapter(paginationAdapter);
        group_recyclerView.setHasFixedSize(true);
        group_recyclerView.setItemViewCacheSize(500);
        add_contect_list.setHasFixedSize(true);
        add_contect_list.setItemViewCacheSize(500);

        SessionManager.setGroupList(getActivity(), new ArrayList<>());

        add_new_contect_layout.setOnClickListener(this);
        group_name.setOnClickListener(this);
        main_layout.setOnClickListener(this);


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


        return view;
    }

    private void IntentUI(View view) {

        mMainLayout = view.findViewById(R.id.mMainLayout);
        main_layout = view.findViewById(R.id.demo_layout);
        add_new_contect_layout = view.findViewById(R.id.add_new_contect_layout);
        group_recyclerView = view.findViewById(R.id.group_list);
        layoutManager = new LinearLayoutManager(getActivity());
        group_recyclerView.setLayoutManager(layoutManager);
        group_name = view.findViewById(R.id.group_name);
        num_count = view.findViewById(R.id.num_count);
        loadingPB = view.findViewById(R.id.idPBLoading);
        grouplists = new ArrayList<>();
        add_contect_list=view.findViewById(R.id.add_contect_list);
        layoutManager1=new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        add_contect_list.setLayoutManager(layoutManager1);
        tv_create=view.findViewById(R.id.tv_create);
        add_new_contect_icon1=view.findViewById(R.id.add_new_contect_icon1);
        add_new_contect_icon=view.findViewById(R.id.add_new_contect_icon);
        add_new_contect=view.findViewById(R.id.add_new_contect);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_contect_layout:
                if (add_new_contect_icon1.getVisibility()==View.GONE)
                {
                    add_new_contect_icon1.setVisibility(View.VISIBLE);
                    add_new_contect_icon.setVisibility(View.GONE);
                    paginationAdapter.addAll_item(grouplists);
                    add_new_contect.setText(getString(R.string.remove_new_contect1));
                }
                else {
                    add_new_contect_icon1.setVisibility(View.GONE);
                    add_new_contect_icon.setVisibility(View.VISIBLE);
                    select_contectListData.clear();
                    topUserListDataAdapter = new TopUserListDataAdapter(getActivity(), getActivity(), select_contectListData);
                    add_contect_list.setAdapter(topUserListDataAdapter);
                    topUserListDataAdapter.notifyDataSetChanged();

                    paginationAdapter.notifyDataSetChanged();
                    add_new_contect.setText(getString(R.string.add_new_contect1));
                }

                break;
            case R.id.group_name:
                startActivity(new Intent(getActivity(), SendBroadcast.class));
                break;
            case R.id.main_layout:
                SessionManager.setGroupData(getActivity(),new Grouplist.Group());
                startActivity(new Intent(getActivity(), GroupActivity.class));
                /*getActivity().finish();*/
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

                loadingDialog.cancelLoading();
                if (response.body().getStatus() == 200) {
                    Gson gson = new Gson();
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
                    main_layout.setVisibility(View.GONE);


                } else {
                    tv_create.setText(getString(R.string.error_opps));
                    main_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                tv_create.setText(getString(R.string.error_opps));
                main_layout.setVisibility(View.VISIBLE);
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
                    if (Group_data.getGroupImage()==null)
                    {
                        String name =Group_data.getGroupName();
                        String add_text="";
                        String[] split_data=name.split(" ");
                        try {
                            for (int i=0;i<split_data.length;i++)
                            {
                                if (i==0)
                                {
                                    add_text=split_data[i].substring(0,1);
                                }
                                else {
                                    add_text=add_text+split_data[i].substring(0,1);
                                    break;
                                }
                            }
                        }
                        catch (Exception e)
                        {
                               e.printStackTrace();
                        }


                        movieViewHolder.no_image.setText(add_text);
                        movieViewHolder.no_image.setVisibility(View.VISIBLE);
                        movieViewHolder.group_image.setVisibility(View.GONE);
                    }
                    else {
                        Glide.with(context).
                                load(Group_data.getGroupImage()).
                                placeholder(R.drawable.shape_primary_back).
                                error(R.drawable.shape_primary_back).into(movieViewHolder.group_image);

                        movieViewHolder.no_image.setVisibility(View.GONE);
                        movieViewHolder.group_image.setVisibility(View.VISIBLE);
                    } movieViewHolder.group_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SessionManager.setGroupData(context, Group_data);
                            startActivity(new Intent(getActivity(), SendBroadcast.class));
                            getActivity().finish();
                        }
                    });



                    movieViewHolder.add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                          //  Log.e("Data is",new Gson().toJson(Group_data));
                            //userDetailsfull.get(position).setId(position);
                            movieViewHolder.remove_contect_icon.setVisibility(View.VISIBLE);
                            movieViewHolder.add_new_contect_icon.setVisibility(View.GONE);
                            select_contectListData.add(Group_data);
                            //userDetailsfull.get(position).setId(position);
                          //  topUserListDataAdapter.notifyDataSetChanged();
                            topUserListDataAdapter=new TopUserListDataAdapter(getActivity(),getActivity(),select_contectListData);
                            add_contect_list.setAdapter(topUserListDataAdapter);
                            num_count.setText(select_contectListData.size()+" Contact Selcted");
                            sessionManager.setgroup_broadcste(getActivity(),new ArrayList<>());
                            sessionManager.setgroup_broadcste(getActivity(),select_contectListData);

                        }
                    });
                    movieViewHolder.remove_contect_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  Log.e("On Click Remove ","Remove");
                            // Log.e("Data is",new Gson().toJson(contacts.get(position)));
                            //userDetailsfull.get(position).setId(0);
                            movieViewHolder.remove_contect_icon.setVisibility(View.GONE);
                            movieViewHolder.add_new_contect_icon.setVisibility(View.VISIBLE);
                            topUserListDataAdapter.remove_item(position);
                            topUserListDataAdapter=new TopUserListDataAdapter(getActivity(),getActivity(),select_contectListData);
                            add_contect_list.setAdapter(topUserListDataAdapter);
                           // Log.e("Postionis ",String.valueOf(position));

                            topUserListDataAdapter.notifyDataSetChanged();
                          //  Log.e("Size is",new Gson().toJson(select_contectListData));
                            num_count.setText(select_contectListData.size()+" Contact Selcted");
                            sessionManager.setgroup_broadcste(getActivity(),new ArrayList<>());
                            sessionManager.setgroup_broadcste(getActivity(),select_contectListData);

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
        public void addAll_item(List<Grouplist.Group> movieList1)
        {
            select_contectListData.clear();
            movieList.clear();
            for (int i=0;i<movieList1.size();i++)
            {

                paginationAdapter = new PaginationAdapter(getContext());
                group_recyclerView.setAdapter(paginationAdapter);
                //group_flag="false";
                //movieList1.get(i).set
                paginationAdapter.addAll(movieList1);
                paginationAdapter.notifyDataSetChanged();
                select_contectListData.add(movieList1.get(i));
                topUserListDataAdapter.notifyDataSetChanged();
                sessionManager.setgroup_broadcste(getActivity(),new ArrayList<>());
                sessionManager.setgroup_broadcste(getActivity(),select_contectListData);

                //  save_button.setTextColor(getResources().getColor(R.color.purple_200));

            }

        }

        public Grouplist.Group getItem(int position) {
            return movieList.get(position);
        }


        public class MovieViewHolder extends RecyclerView.ViewHolder {
            private final TextView group_name,no_image;
            private final CircleImageView group_image;
            LinearLayout group_layout;
            ImageView add_new_contect_icon,remove_contect_icon;

            public MovieViewHolder(View itemView) {
                super(itemView);
                no_image = itemView.findViewById(R.id.no_image);
                group_name = itemView.findViewById(R.id.group_name);
                group_layout = itemView.findViewById(R.id.group_layout);
                group_image = itemView.findViewById(R.id.group_image);
                add_new_contect_icon=itemView.findViewById(R.id.add_new_contect_icon);
                remove_contect_icon=itemView.findViewById(R.id.remove_contect_icon);
                add_new_contect_icon.setVisibility(View.VISIBLE);
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



   public class TopUserListDataAdapter extends RecyclerView.Adapter<TopUserListDataAdapter.InviteListDataclass>
    {

        private final Context mcntx;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<Grouplist.Group> userDetails;
        private final List<Grouplist.Group> userDetailsfull;


        public TopUserListDataAdapter(Activity Ctx, Context mCtx, List<Grouplist.Group> userDetails) {
            this.mcntx = mCtx;
            this.mCtx = Ctx;
            this.userDetails = userDetails;
            userDetailsfull = new ArrayList<>(userDetails);
        }

        @NonNull
        @Override
        public InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.top_user_details, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InviteListDataclass holder, int position) {
            Grouplist.Group inviteUserDetails = userDetails.get(position);
            last_postion = position;
            holder.userName.setText(select_contectListData.get(position).getGroupName());
            holder.top_layout.setVisibility(View.VISIBLE);

            String first_latter =select_contectListData.get(position).getGroupName().substring(0, 1).toUpperCase();

            if (second_latter.equals("")) {
                current_latter = first_latter;
                second_latter = first_latter;

            } else if (second_latter.equals(first_latter)) {
                current_latter = second_latter;
            } else {

                current_latter = first_latter;
                second_latter = first_latter;
            }



            String file = "" + select_contectListData.get(position).getGroupImage();
            if (file.equals("null")) {
                holder.no_image.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);
                String name = select_contectListData.get(position).getGroupName();
                holder.profile_image.setVisibility(View.GONE);
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

            } else {

                image_url = select_contectListData.get(position).getGroupImage();
                Glide.with(mCtx).
                            load(select_contectListData.get(position).getGroupImage())
                            .placeholder(R.drawable.shape_primary_circle)
                            .error(R.drawable.shape_primary_circle)
                            .into(holder.profile_image);

            }

            holder.top_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<grouplists.size();i++)
                    {
                        if (grouplists.get(i).getId().equals(select_contectListData.get(position).getId()))
                        {
                            paginationAdapter.notifyItemChanged(i);
                        }
                    }
                    userDetails.remove(position);
                    topUserListDataAdapter.notifyDataSetChanged();
                    sessionManager.setgroup_broadcste(getActivity(),new ArrayList<>());
                    sessionManager.setgroup_broadcste(getActivity(),select_contectListData);


                }
            });


        }

        @Override
        public int getItemCount() {
            return userDetails.size();
        }

        public void remove_item(int item)
        {
            try {
                userDetails.remove(item);
                notifyItemRemoved(item);
            }
            catch (Exception e)
            {
e.printStackTrace();
            }

        }

        public void updateList(List<Grouplist.Group> list) {
            userDetails = list;
            notifyDataSetChanged();
        }

        public class InviteListDataclass extends RecyclerView.ViewHolder {

            TextView no_image;
            TextView userName;
            CircleImageView profile_image;
            LinearLayout top_layout;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);

                userName = itemView.findViewById(R.id.username);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.main_layout);


            }

        }

    }

}

