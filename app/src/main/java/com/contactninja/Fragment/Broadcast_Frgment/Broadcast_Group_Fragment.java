package com.contactninja.Fragment.Broadcast_Frgment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.contactninja.Fragment.AddContect_Fragment.GroupFragment;
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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class Broadcast_Group_Fragment extends Fragment implements View.OnClickListener {

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
            GroupEvent();
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
                            GroupEvent1();
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


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_contect_layout:
                SessionManager.setGroupData(getActivity(),new Grouplist.Group());
                startActivity(new Intent(getActivity(), GroupActivity.class));
                /*  getActivity().finish();*/
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
        String token = Global.getToken(getActivity());
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
        retrofitCalls.Group_List(sessionManager,gsonObject, loadingDialog, token, new RetrofitCallback() {
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


                } else {

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }


    private void GroupEvent1() throws JSONException {
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        String token = Global.getToken(getActivity());
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
        retrofitCalls.Group_List(sessionManager,gsonObject, loadingDialog, token, new RetrofitCallback() {
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
                    viewHolder = new PaginationAdapter.MovieViewHolder(viewItem);
                    break;
                case LOADING:
                    View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                    viewHolder = new PaginationAdapter.LoadingViewHolder(viewLoading);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            Grouplist.Group Group_data = movieList.get(position);
            switch (getItemViewType(position)) {
                case ITEM:
                    PaginationAdapter.MovieViewHolder movieViewHolder = (PaginationAdapter.MovieViewHolder) holder;

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

                        }
                    });

                    break;

                case LOADING:
                   PaginationAdapter.LoadingViewHolder loadingViewHolder = (PaginationAdapter.LoadingViewHolder) holder;
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
            ImageView add_new_contect_icon,remove_contect_icon;

            public MovieViewHolder(View itemView) {
                super(itemView);
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
        public TopUserListDataAdapter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.top_user_details, parent, false);
            return new TopUserListDataAdapter.InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TopUserListDataAdapter.InviteListDataclass holder, int position) {
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




                }
            });


        }

        @Override
        public int getItemCount() {
            return userDetails.size();
        }

        public void remove_item(int item)
        {
            userDetails.remove(item);
            notifyItemRemoved(item);

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

