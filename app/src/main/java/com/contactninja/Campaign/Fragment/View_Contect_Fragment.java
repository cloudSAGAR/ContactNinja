package com.contactninja.Campaign.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contactninja.Campaign.List_itm.Campaign_view_per_contect_Detail;
import com.contactninja.Campaign.ContectAndGroup_Actvity;
import com.contactninja.Model.CampaignTask_overview;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.GroupListData;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class View_Contect_Fragment extends Fragment implements View.OnClickListener {


    public static ArrayList<GroupListData> inviteListData = new ArrayList<>();
    List<ContectListData.Contact> pre_seleact=new ArrayList<>();

    public static List<GroupListData> select_inviteListData = new ArrayList<>();
    RecyclerView add_contect_list, contect_list_unselect;
    LinearLayoutManager layoutManager, layoutManager1;
    Cursor cursor;
    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    EditText contect_search;
    TextView add_new_contect, num_count;
    ImageView add_new_contect_icon,filter_icon;
    LinearLayout add_new_contect_layout,mMainLayout;
    LoadingDialog loadingDialog;
    String userName, user_phone_number, user_image, user_des, strtext = "", old_latter = "", contect_type = "", contect_email,
            contect_type_work = "", email_type_home = "", email_type_work = "", country = "", city = "", region = "", street = "",
            postcode = "", postType = "", note = "";
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    int page = 1, limit = 150, totale_group;
    GroupContectAdapter groupContectAdapter;
    int currentPage = 1, TOTAL_PAGES = 10;
    boolean isLoading = false;
    boolean isLastPage = false;
    Activity activity;
    BottomSheetDialog bottomSheetDialog_step,bottomSheetDialog_fillter;
    CampaignTask_overview contect_list_data;
    String s_type="";
    List<CampaignTask_overview.SequenceProspect> sequenceProspects=new ArrayList<>();
    private long mLastClickTime=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_view__contect_, container, false);

        IntentUI(view);
        sessionManager=new SessionManager(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());

        contect_list_unselect.setHasFixedSize(true);
        contect_list_unselect.setItemViewCacheSize(5000);


         contect_list_data=SessionManager.getCampaign_data(getActivity());
         sequenceProspects.addAll(contect_list_data.getSequenceProspects());

        groupContectAdapter = new GroupContectAdapter(getActivity());
        contect_list_unselect.setAdapter(groupContectAdapter);
        groupContectAdapter.addAll(sequenceProspects);
        Log.e("Seqence Data Is",new Gson().toJson(sequenceProspects));
        num_count.setText(contect_list_data.getSeqProspectCount().getTotal()+" Contacts");
        s_type=contect_list_data.getSequenceTask().get(0).getType();
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


                                sequenceProspects.get(position).getFirstname().substring(0, 1)
                                        .substring(0, 1)
                                        .toUpperCase()// Grab the first letter and capitalize it
                        );
                        return fastScrollItemIndicator;
                    }
                    catch (Exception e)
                    {

                        return null;
                    }

                }
        );


        add_new_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (contect_list_data.get0().getStatus().toString().equals("A"))
                {
                    Global.Messageshow(getActivity(),mMainLayout,"Pause the campaign before adding new prospects",false);
                }
                else {
                    showBottomSheetDialog_For_TimeZone();
                }

            }
        });
        add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (contect_list_data.get0().getStatus().toString().equals("A"))
                {
                    Global.Messageshow(getActivity(),mMainLayout,"Pause the campaign before adding new prospects",false);
                }
                else {
                    showBottomSheetDialog_For_TimeZone();
                }
            }
        });
        add_new_contect_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (contect_list_data.get0().getStatus().toString().equals("A"))
                {
                    Global.Messageshow(getActivity(),mMainLayout,"Pause the campaign before adding new prospects",false);
                }
                else {
                    showBottomSheetDialog_For_TimeZone();
                }
               /* Intent addnewcontect = new Intent(getActivity(), Addnewcontect_Activity.class);
                SessionManager.setContect_flag("save");
                startActivity(addnewcontect);
                // splitdata(inviteListData);*/
            }
        });





        contect_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<CampaignTask_overview.SequenceProspect> temp = new ArrayList();
                for(CampaignTask_overview.SequenceProspect d: sequenceProspects){
                    if(d.getFirstname().toLowerCase().contains(s.toString().toLowerCase())){
                        temp.add(d);
                        // Log.e("Same Data ",d.getUserName());
                    }
                }
                    /*groupContectAdapter = new GroupContectAdapter(getActivity());
                    contect_list_unselect.setAdapter(groupContectAdapter);*/
                    groupContectAdapter.updateList(temp);
                   //groupContectAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        contect_list_unselect.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItem = layoutManager1.getChildCount();
                int totalItem = layoutManager1.getItemCount();
                int firstVisibleItemPosition = layoutManager1.findFirstVisibleItemPosition();
                if (!isLoading && !isLastPage) {
                    if ((visibleItem + firstVisibleItemPosition) >= totalItem && firstVisibleItemPosition >= 0 && totalItem >= currentPage) {

                        currentPage=currentPage + 1;
                       /* try {

                            Log.e("Current Page is", String.valueOf(currentPage));
                      //      ContectEventnext();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                }
            }
        });



        return view;
    }


    private void IntentUI(View view) {
        mMainLayout=view.findViewById(R.id.mMainLayout);
        contect_list_unselect = view.findViewById(R.id.contect_list_unselect);
        layoutManager1 = new LinearLayoutManager(getActivity());
        contect_list_unselect.setLayoutManager(layoutManager1);
        fastscroller = view.findViewById(R.id.fastscroller);
        fastscroller_thumb = view.findViewById(R.id.fastscroller_thumb);
        contect_search = view.findViewById(R.id.contect_search);
        add_new_contect = view.findViewById(R.id.add_new_contect);
        num_count = view.findViewById(R.id.num_count);
        add_new_contect_icon = view.findViewById(R.id.add_new_contect_icon);
        add_new_contect_layout = view.findViewById(R.id.add_new_contect_layout);
        filter_icon=view.findViewById(R.id.filter_icon);
        filter_icon.setOnClickListener(this);

    }




    void showBottomSheetDialog_For_TimeZone() {
        bottomSheetDialog_step = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
        bottomSheetDialog_step.setContentView(R.layout.bottom_sheet_dialog_for_home);
        RecyclerView home_type_list = bottomSheetDialog_step.findViewById(R.id.home_type_list);
        TextView tv_item=bottomSheetDialog_step.findViewById(R.id.tv_item);
        tv_item.setText("Please select Timezone");
        tv_item.setVisibility(View.GONE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        home_type_list.setLayoutManager(layoutManager);

        List<CampaignTask_overview.SequenceTask> task_list=contect_list_data.getSequenceTask();

        TimezoneAdapter timezoneAdapter = new TimezoneAdapter(getActivity(), task_list);
        home_type_list.setAdapter(timezoneAdapter);

        bottomSheetDialog_step.show();
    }


    void showBottomSheetDialog_Filtter() {
        bottomSheetDialog_fillter = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
        bottomSheetDialog_fillter.setContentView(R.layout.bottom_sheet_dialog_for_fillter);
        TextView tv_clear=bottomSheetDialog_fillter.findViewById(R.id.tv_clear);
        TextView tv_done=bottomSheetDialog_fillter.findViewById(R.id.tv_done);
        RecyclerView home_type_list = bottomSheetDialog_fillter.findViewById(R.id.home_type_list);
        RecyclerView home_type_list_general=bottomSheetDialog_fillter.findViewById(R.id.home_type_list_general);
        home_type_list_general.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView home_type_list_sort=bottomSheetDialog_fillter.findViewById(R.id.home_type_list_sort);
        home_type_list_sort.setLayoutManager(new LinearLayoutManager(getActivity()));
        TextView tv_item=bottomSheetDialog_fillter.findViewById(R.id.tv_item);
        List<String> filtter_list=new ArrayList<>();
        filtter_list.add("General");
        filtter_list.add("Sort");


        List<String> filtter_list_normal=new ArrayList<>();
        filtter_list_normal.add("Finished");
        filtter_list_normal.add("Pending");
        filtter_list_normal.add("Replied");
        filtter_list_normal.add("Failed");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        home_type_list.setLayoutManager(layoutManager);


        FiltterAdapter timezoneAdapter = new FiltterAdapter(getActivity(), filtter_list);
        home_type_list.setAdapter(timezoneAdapter);


        Filtter_select_Adapter filtter_select_adapter= new Filtter_select_Adapter(getActivity(), filtter_list_normal);
        home_type_list_general.setAdapter(filtter_select_adapter);
        bottomSheetDialog_fillter.show();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.filter_icon) {
            showBottomSheetDialog_Filtter();
        }
    }


    public class TimezoneAdapter extends RecyclerView.Adapter<TimezoneAdapter.InviteListDataclass> {

        public Context mCtx;
        TextView phone_txt;
        Contactdetail item;
        private List<CampaignTask_overview.SequenceTask> timezoneModels;

        public TimezoneAdapter(Context context, List<CampaignTask_overview.SequenceTask> timezoneModels) {
            this.mCtx = context;
            this.timezoneModels = timezoneModels;
        }

        @NonNull
        @Override
        public TimezoneAdapter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.step_type_selecte, parent, false);
            return new TimezoneAdapter.InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TimezoneAdapter.InviteListDataclass holder, int position) {
            CampaignTask_overview.SequenceTask Data = timezoneModels.get(position);
            holder.tv_item.setText("Step#"+Data.getStepNo()+"("+Data.getManageBy()+Data.getType()+")");
            if (Data.getType().equals("SMS"))
            {
                holder.iv_email.setVisibility(View.GONE);
                holder.iv_message.setVisibility(View.VISIBLE);
            }
            else {

                holder.iv_email.setVisibility(View.VISIBLE);
                holder.iv_message.setVisibility(View.GONE);

            }
            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    SessionManager.setContect_flag("check");
                    SessionManager.setgroup_broadcste(getActivity(),new ArrayList<>());
                    SessionManager.setGroupList(getActivity(),new ArrayList<>());
                    Intent intent=new Intent(getActivity(), ContectAndGroup_Actvity.class);
                    intent.putExtra("sequence_id",contect_list_data.get0().getId());
                    intent.putExtra("seq_task_id",Data.getId());
                    startActivity(intent);
                    getActivity().finish();
                    bottomSheetDialog_step.cancel();

                }
            });

        }

        @Override
        public int getItemCount() {
            return timezoneModels.size();
        }

        public void updateList(List<CampaignTask_overview.SequenceTask> list) {
            timezoneModels = list;
            notifyDataSetChanged();
        }

        public class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_item;
            ImageView iv_message,iv_email;

            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                tv_item = itemView.findViewById(R.id.tv_item);
                iv_message=itemView.findViewById(R.id.iv_message);
                iv_email=itemView.findViewById(R.id.iv_email);
            }
        }

    }

    public class GroupContectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int LOADING = 0;
        private static final int ITEM = 1;
        private final Context context;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<CampaignTask_overview.SequenceProspect> contacts;
        private boolean isLoadingAdded = false;

        public GroupContectAdapter(Context context) {
            this.context = context;
            contacts = new LinkedList<>();
        }

        public void setContactList(List<CampaignTask_overview.SequenceProspect> contacts) {
            this.contacts = contacts;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case ITEM:
                    View viewItem = inflater.inflate(R.layout.campign_user_details, parent, false);
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

            CampaignTask_overview.SequenceProspect Contact_data = contacts.get(position);
            switch (getItemViewType(position)) {
                case ITEM:
                    GroupContectAdapter.MovieViewHolder holder1 = (GroupContectAdapter.MovieViewHolder) holder;
                  /*  contacts.get(position).setFlag("true");*/
                    holder1.userName.setText(Contact_data.getFirstname());
                    holder1.userNumber.setVisibility(View.GONE);

                    // Manual Ni
                    try {
                        if (!Contact_data.getMtype().toString().equals("null"))
                        {

                            if (Contact_data.getMtype().equals("SMS"))
                            {
                                holder1.iv_email.setVisibility(View.GONE);
                                holder1.iv_mesage.setVisibility(View.VISIBLE);
                            }
                            else {
                                holder1.iv_email.setVisibility(View.VISIBLE);
                                holder1.iv_mesage.setVisibility(View.GONE);
                            }
                            holder1.tv_step.setText("Step#"+Contact_data.getMstepNo());
                        }
                        //Auto
                        else {
                            if (Contact_data.getAtype().equals("SMS"))
                            {
                                holder1.iv_email.setVisibility(View.GONE);
                                holder1.iv_mesage.setVisibility(View.VISIBLE);

                            }
                            else {
                                holder1.iv_email.setVisibility(View.VISIBLE);
                                holder1.iv_mesage.setVisibility(View.GONE);
                            }
                            holder1.tv_step.setText("Step#"+Contact_data.getAstepNo());

                        }
                    }
                    catch (Exception e)
                    {
                        holder1.tv_step.setText("Step#"+Contact_data.getAstepNo());
                        try {
                            if (Contact_data.getAtype().equals("SMS"))
                            {
                                holder1.iv_email.setVisibility(View.GONE);
                                holder1.iv_mesage.setVisibility(View.VISIBLE);

                            }
                            else {
                                holder1.iv_email.setVisibility(View.VISIBLE);
                                holder1.iv_mesage.setVisibility(View.GONE);
                            }
                        }
                        catch (Exception e1)
                        {

                            holder1.tv_step.setText("Step#"+Contact_data.getStep_no());
                            if (s_type.equals("SMS"))
                            {
                                holder1.iv_email.setVisibility(View.GONE);
                                holder1.iv_mesage.setVisibility(View.VISIBLE);

                            }
                            else {
                                holder1.iv_email.setVisibility(View.VISIBLE);
                                holder1.iv_mesage.setVisibility(View.GONE);
                            }
                        }



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
                        holder1.main_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                    return;
                                }
                                mLastClickTime = SystemClock.elapsedRealtime();
                                Intent intent=new Intent(getActivity(), Campaign_view_per_contect_Detail.class);
                                intent.putExtra("position",String.valueOf(position));
                                startActivity(intent);
                            }
                        });
                        holder1.no_image.setText(add_text);
                        holder1.no_image.setVisibility(View.VISIBLE);
                        holder1.profile_image.setVisibility(View.GONE);

                    break;

                case LOADING:
                    GroupContectAdapter.LoadingViewHolder loadingViewHolder = (GroupContectAdapter.LoadingViewHolder) holder;
                    loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        }
        public void updateList(List<CampaignTask_overview.SequenceProspect> list) {
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
            add(new CampaignTask_overview.SequenceProspect());
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = contacts.size() - 1;
            CampaignTask_overview.SequenceProspect result = getItem(position);

            if (result != null) {
                contacts.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void add(CampaignTask_overview.SequenceProspect contact) {
            contacts.add(contact);
            notifyItemInserted(contacts.size() - 1);
            //  notifyDataSetChanged();
        }

        public void addAll(List<CampaignTask_overview.SequenceProspect> contact) {
            for (CampaignTask_overview.SequenceProspect result : contact) {

                add(result);
            }

        }

        public CampaignTask_overview.SequenceProspect getItem(int position) {
            return contacts.get(position);
        }


        public class MovieViewHolder extends RecyclerView.ViewHolder {
            TextView no_image;
            TextView userName, userNumber,tv_step;
            CircleImageView profile_image;
            ImageView iv_email,iv_mesage;
            RelativeLayout main_layout;
            public MovieViewHolder(View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                add_new_contect_icon.setVisibility(View.VISIBLE);
                tv_step=itemView.findViewById(R.id.tv_step);
                iv_email=itemView.findViewById(R.id.iv_email);
                iv_mesage=itemView.findViewById(R.id.iv_mesage);
                main_layout=itemView.findViewById(R.id.main_layout);
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


    public static class FiltterAdapter extends RecyclerView.Adapter<FiltterAdapter.InviteListDataclass> {

        public Context mCtx;
        TextView phone_txt;
        Contactdetail item;
        private List<String> timezoneModels;

        public FiltterAdapter(Context context, List<String> timezoneModels) {
            this.mCtx = context;
            this.timezoneModels = timezoneModels;
        }

        @NonNull
        @Override
        public FiltterAdapter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.step_type_selecte1, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FiltterAdapter.InviteListDataclass holder, int position) {
            String Data = timezoneModels.get(position);
            holder.tv_item.setText(Data);

            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

        }

        @Override
        public int getItemCount() {
            return timezoneModels.size();
        }

        public void updateList(List<String> list) {
            timezoneModels = list;
            notifyDataSetChanged();
        }

        public static class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_item;
            ImageView iv_message,iv_email;

            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                tv_item = itemView.findViewById(R.id.tv_item);
                iv_message=itemView.findViewById(R.id.iv_message);
                iv_email=itemView.findViewById(R.id.iv_email);
            }

        }

    }

    public static class Filtter_select_Adapter extends RecyclerView.Adapter<Filtter_select_Adapter.InviteListDataclass> {

        public Context mCtx;
        TextView phone_txt;
        Contactdetail item;
        private List<String> timezoneModels;

        public Filtter_select_Adapter(Context context, List<String> timezoneModels) {
            this.mCtx = context;
            this.timezoneModels = timezoneModels;
        }

        @NonNull
        @Override
        public Filtter_select_Adapter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.step_type_selecte_list, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Filtter_select_Adapter.InviteListDataclass holder, int position) {
            String Data = timezoneModels.get(position);
            holder.tv_item.setText(Data);

            holder.tv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
            holder.iv_selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.iv_unselected.setVisibility(View.VISIBLE);
                    holder.iv_selected.setVisibility(View.GONE);
                }
            });
            holder.iv_unselected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.iv_unselected.setVisibility(View.GONE);
                    holder.iv_selected.setVisibility(View.VISIBLE);
                }
            });

        }

        @Override
        public int getItemCount() {
            return timezoneModels.size();
        }

        public void updateList(List<String> list) {
            timezoneModels = list;
            notifyDataSetChanged();
        }

        public static class InviteListDataclass extends RecyclerView.ViewHolder {
            TextView tv_item;
            ImageView iv_unselected,iv_selected;

            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                tv_item = itemView.findViewById(R.id.tv_item);
                iv_unselected=itemView.findViewById(R.id.iv_unselected);
                iv_selected=itemView.findViewById(R.id.iv_selected);
            }

        }

    }

}