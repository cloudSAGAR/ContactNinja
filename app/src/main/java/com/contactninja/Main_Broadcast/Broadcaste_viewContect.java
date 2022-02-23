package com.contactninja.Main_Broadcast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.contactninja.Campaign.Campaign_Preview;
import com.contactninja.Model.BroadcastActivityModel;
import com.contactninja.Model.CampaignTask_overview;
import com.contactninja.Model.ContectListData;
import com.contactninja.R;
import com.contactninja.Setting.WebActivity;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Broadcaste_viewContect extends AppCompatActivity  implements ConnectivityReceiver.ConnectivityReceiverListener {
    ImageView iv_back;
    TextView save_button;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    RecyclerView add_contect_list,contect_list_unselect;
    TopUserListDataAdapter topUserListDataAdapter;
    List<BroadcastActivityModel.BroadcastProspect> broadcastProspects;

    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    EditText ev_search;
    GroupContectAdapter groupContectAdapter;
    LinearLayout mMainLayout;
    private BroadcastReceiver mNetworkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcaste_view_contect);
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        mNetworkReceiver = new ConnectivityReceiver();

        IntentUI();
        broadcastProspects=SessionManager.getBroadcast_Contect(getApplicationContext());
        topUserListDataAdapter=new TopUserListDataAdapter(this,getApplicationContext(),broadcastProspects);
        add_contect_list.setAdapter(topUserListDataAdapter);
        groupContectAdapter = new GroupContectAdapter(this);
        contect_list_unselect.setAdapter(groupContectAdapter);
        add_contect_list.setItemViewCacheSize(50000);
        ev_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    List<BroadcastActivityModel.BroadcastProspect> temp = new ArrayList();
                    for (BroadcastActivityModel.BroadcastProspect d : broadcastProspects) {
                        if (d.getFirstname().toLowerCase().contains(ev_search.getText().toString().toLowerCase())) {
                            temp.add(d);
                        }
                    }
                    groupContectAdapter.updateList(temp);

                    return true;
                }
                return false;
            }
        });

        groupContectAdapter = new GroupContectAdapter(getApplicationContext());
        contect_list_unselect.setAdapter(groupContectAdapter);
        groupContectAdapter.addAll(broadcastProspects);
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
        fastscroller.setupWithRecyclerView(
                contect_list_unselect,
                (position) -> {

                    try {
                        // ItemModel item = data.get(position);
                        FastScrollItemIndicator fastScrollItemIndicator = new FastScrollItemIndicator.Text(
                                groupContectAdapter.getItem(position).getFirstname().substring(0, 1)
                                        .substring(0, 1)
                                        .toUpperCase()// Grab the first letter and capitalize it
                        );
                        return fastScrollItemIndicator;
                    } catch (Exception e) {
                        return null;
                    }


                }
        );
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Broadcaste_viewContect.this, mMainLayout);
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

    private void IntentUI() {
        contect_list_unselect = findViewById(R.id.contect_list_unselect);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);
        contect_list_unselect.setLayoutManager(layoutManager1);
        fastscroller = findViewById(R.id.fastscroller);
        fastscroller_thumb = findViewById(R.id.fastscroller_thumb);
        ev_search =findViewById(R.id.ev_search);
        mMainLayout =findViewById(R.id.mMainLayout);

        add_contect_list=findViewById(R.id.add_contect_list);
        add_contect_list.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.GONE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public static class TopUserListDataAdapter extends RecyclerView.Adapter<TopUserListDataAdapter.InviteListDataclass> {

        private final Context mcntx;
        private final List<BroadcastActivityModel.BroadcastProspect> userDetailsfull;
        private final List<BroadcastActivityModel.BroadcastProspect> userDetails;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";


        public TopUserListDataAdapter(Activity Ctx, Context mCtx, List<BroadcastActivityModel.BroadcastProspect> userDetails) {
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
            BroadcastActivityModel.BroadcastProspect inviteUserDetails = userDetails.get(position);
            last_postion = position;
            holder.userName.setText(inviteUserDetails.getFirstname());
            holder.top_layout.setVisibility(View.VISIBLE);

            if(Global.IsNotNull(inviteUserDetails.getFirstname())||!inviteUserDetails.getFirstname().equals("")){
                String first_latter =inviteUserDetails.getFirstname().substring(0, 1).toUpperCase();

                if (second_latter.equals("")) {
                    current_latter = first_latter;
                    second_latter = first_latter;

                } else if (second_latter.equals(first_latter)) {
                    current_latter = second_latter;
                } else {

                    current_latter = first_latter;
                    second_latter = first_latter;
                }
            }





            holder.no_image.setVisibility(View.VISIBLE);
            holder.profile_image.setVisibility(View.GONE);
            String name = inviteUserDetails.getFirstname();
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

        }

        @Override
        public int getItemCount() {
            return userDetails.size();
        }

        public static class InviteListDataclass extends RecyclerView.ViewHolder {

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


    public class GroupContectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int LOADING = 0;
        private static final int ITEM = 1;
        private final Context context;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<BroadcastActivityModel.BroadcastProspect> contacts;
        private boolean isLoadingAdded = false;

        public GroupContectAdapter(Context context) {
            this.context = context;
            contacts = new LinkedList<>();
        }

        public void setContactList(List<BroadcastActivityModel.BroadcastProspect> contacts) {
            this.contacts = contacts;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case ITEM:
                    View viewItem = inflater.inflate(R.layout.invite_user_details, parent, false);
                    viewHolder = new GroupContectAdapter.MovieViewHolder(viewItem);
                    break;
                case LOADING:
                    View viewLoading = inflater.inflate(R.layout.item_loading, parent, false);
                    viewHolder = new GroupContectAdapter.LoadingViewHolder(viewLoading);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            BroadcastActivityModel.BroadcastProspect Contact_data = contacts.get(position);
//            Log.e("Selcete List Is",new Gson().toJson(select_contectListData));
            switch (getItemViewType(position)) {
                case ITEM:
                    GroupContectAdapter.MovieViewHolder holder1 = (GroupContectAdapter.MovieViewHolder) holder;

                    holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                    holder1.add_new_contect_icon.setVisibility(View.GONE);
                    holder1.userName.setText(Contact_data.getFirstname());
                    holder1.userNumber.setVisibility(View.GONE);

                    holder1.first_latter.setVisibility(View.VISIBLE);
                    holder1.top_layout.setVisibility(View.VISIBLE);


                    String first_latter = Contact_data.getFirstname().substring(0, 1).toUpperCase();
                    holder1.first_latter.setText(first_latter);
                    if (second_latter.equals("")) {
                        current_latter = first_latter;
                        second_latter = first_latter;
                        holder1.first_latter.setVisibility(View.VISIBLE);
                        holder1.top_layout.setVisibility(View.VISIBLE);

                    } else if (second_latter.equals(first_latter)) {
                        current_latter = second_latter;
                        // inviteUserDetails.setF_latter("");
                        holder1.first_latter.setVisibility(View.GONE);
                        holder1.top_layout.setVisibility(View.GONE);

                    } else {

                        current_latter = first_latter;
                        second_latter = first_latter;
                        holder1.first_latter.setVisibility(View.VISIBLE);
                        holder1.top_layout.setVisibility(View.VISIBLE);


                    }


                        String name = Contact_data.getFirstname() + " " + Contact_data.getLastname();
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


                        holder1.no_image.setText(add_text);
                        holder1.no_image.setVisibility(View.VISIBLE);
                        holder1.profile_image.setVisibility(View.GONE);

                    //  holder1.add_new_contect_icon.setVisibility(View.VISIBLE);
                    break;

                case LOADING:
                    GroupContectAdapter.LoadingViewHolder loadingViewHolder = (GroupContectAdapter.LoadingViewHolder) holder;
                    loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        }

        public void updateList(List<BroadcastActivityModel.BroadcastProspect> list) {
            contacts = list;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            // Log.e("Size is :: ",contacts.size()+"");
            return contacts == null ? 0 : contacts.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == contacts.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }

        public void addLoadingFooter() {
            isLoadingAdded = true;
            add(new BroadcastActivityModel.BroadcastProspect());
        }




        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = contacts.size() - 1;
            BroadcastActivityModel.BroadcastProspect result = getItem(position);

            if (result != null) {
                contacts.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void add(BroadcastActivityModel.BroadcastProspect contact) {
            contacts.add(contact);
            notifyItemInserted(contacts.size() - 1);
            //  notifyDataSetChanged();
        }

        public void addAll(List<BroadcastActivityModel.BroadcastProspect> contact) {
            for (BroadcastActivityModel.BroadcastProspect result : contact) {

                add(result);
            }

        }

        public BroadcastActivityModel.BroadcastProspect getItem(int position) {
            return contacts.get(position);
        }


        public class MovieViewHolder extends RecyclerView.ViewHolder {
            TextView no_image;
            TextView userName, userNumber, first_latter;
            CircleImageView profile_image;
            LinearLayout top_layout;
            ImageView add_new_contect_icon, remove_contect_icon, iv_block;

            public MovieViewHolder(View itemView) {
                super(itemView);
                iv_block = itemView.findViewById(R.id.iv_block);
                first_latter = itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.top_layout);
                add_new_contect_icon = itemView.findViewById(R.id.add_new_contect_icon);
                remove_contect_icon = itemView.findViewById(R.id.remove_contect_icon);
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

}