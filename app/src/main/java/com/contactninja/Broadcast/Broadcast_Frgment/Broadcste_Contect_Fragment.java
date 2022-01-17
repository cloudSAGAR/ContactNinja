package com.contactninja.Broadcast.Broadcast_Frgment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Addnewcontect_Activity;
import com.contactninja.Campaign.Fragment.Campaign_Contect_Fragment;
import com.contactninja.Group.GroupActivity;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.GroupListData;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitApiClient;
import com.contactninja.retrofit.RetrofitApiInterface;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class Broadcste_Contect_Fragment extends Fragment {
    BottomSheetDialog bottomSheetDialog_templateList1;
    String  group_flag="true";
    public static TopUserListDataAdapter topUserListDataAdapter;
    public static ArrayList<GroupListData> inviteListData = new ArrayList<>();
    public static List<GroupListData> select_inviteListData = new ArrayList<>();
    List<ContectListData.Contact> pre_seleact = new ArrayList<>();
    RecyclerView add_contect_list, contect_list_unselect;
    LinearLayoutManager layoutManager, layoutManager1;
    Cursor cursor;
    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    EditText contect_search;
    TextView add_new_contect, num_count;
    ImageView add_new_contect_icon;
    LinearLayout add_new_contect_layout;
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
    List<ContectListData.Contact> contectListData;
    List<ContectListData.Contact> select_contectListData;
    Activity activity;
    LinearLayout mMainLayout;

    public Broadcste_Contect_Fragment(Activity activity0) {
        this.activity = activity0;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_broadcste__contect_, container, false);
        IntentUI(view);
        sessionManager = new SessionManager(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        select_contectListData = new ArrayList<>();
        contect_list_unselect.setHasFixedSize(true);
        contect_list_unselect.setItemViewCacheSize(5000);
        contectListData = new ArrayList<>();
        //GetContactsIntoArrayList();
        groupContectAdapter = new GroupContectAdapter(getActivity());
        contect_list_unselect.setAdapter(groupContectAdapter);

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();



        contectListData.clear();
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
                    // ItemModel item = data.get(position);
                    FastScrollItemIndicator fastScrollItemIndicator = new FastScrollItemIndicator.Text(


                            contectListData.get(position).getFirstname().substring(0, 1)
                                    .substring(0, 1)
                                    .toUpperCase()// Grab the first letter and capitalize it
                    );
                    return fastScrollItemIndicator;
                }
        );


        topUserListDataAdapter = new TopUserListDataAdapter(getActivity(), getActivity(), select_contectListData);
        add_contect_list.setAdapter(topUserListDataAdapter);
        topUserListDataAdapter.notifyDataSetChanged();
        add_new_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupContectAdapter.addAll_item(contectListData);

            /*    Intent addnewcontect = new Intent(getActivity(), Addnewcontect_Activity.class);
                SessionManager.setContect_flag("save");
                startActivity(addnewcontect);*/
            }
        });
        add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupContectAdapter.addAll_item(contectListData);

               /* Intent addnewcontect = new Intent(getActivity(), Addnewcontect_Activity.class);
                SessionManager.setContect_flag("save");
                startActivity(addnewcontect);*/
            }
        });
        add_new_contect_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupContectAdapter.addAll_item(contectListData);
              /*  Intent addnewcontect = new Intent(getActivity(), Addnewcontect_Activity.class);
                SessionManager.setContect_flag("save");
                startActivity(addnewcontect);*/
                // splitdata(inviteListData);
            }
        });


        contect_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<ContectListData.Contact> temp = new ArrayList();
                for (ContectListData.Contact d : contectListData) {
                    if (d.getFirstname().toLowerCase().contains(s.toString().toLowerCase())) {
                        temp.add(d);
                        // Log.e("Same Data ",d.getUserName());
                    }
                }
                groupContectAdapter.updateList(temp);


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
                        try {
                            currentPage = currentPage + 1;
                            Log.e("Current Page is", String.valueOf(currentPage));
                            ContectEventnext();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        call_updatedata();

        return view;
    }


    public void call_updatedata() {
        if (sessionManager.getGroupList(getActivity()).size() != 0) {
            select_contectListData.clear();
            pre_seleact.clear();
            pre_seleact.addAll(sessionManager.getGroupList(getActivity()));
            select_contectListData.addAll(pre_seleact);
            topUserListDataAdapter.notifyDataSetChanged();


        }

    }

    private void IntentUI(View view) {
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        add_contect_list = view.findViewById(R.id.add_contect_list);
        mMainLayout = view.findViewById(R.id.mMainLayout);
        add_contect_list.setLayoutManager(layoutManager);
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

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void GetContactsIntoArrayList() {
        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        while (cursor.moveToNext()) {

            userName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            user_phone_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            user_image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
            user_des = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA2));

            try {
                contect_type = cursor.getString(cursor.getColumnIndex(String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)));
                contect_type_work = cursor.getString(cursor.getColumnIndex(String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_WORK)));
                contect_email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                email_type_home = cursor.getString(cursor.getColumnIndex(String.valueOf(ContactsContract.CommonDataKinds.Email.TYPE_HOME)));
                email_type_work = cursor.getString(cursor.getColumnIndex(String.valueOf(ContactsContract.CommonDataKinds.Email.TYPE_WORK)));


                country = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                // StructuredPostal.CITY == data7
                city = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                // StructuredPostal.REGION == data8
                region = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                // StructuredPostal.STREET == data4
                street = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                // StructuredPostal.POSTCODE == data9
                postcode = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                // StructuredPostal.TYPE == data2
                postType = String.valueOf(cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE)));
                note = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));


            } catch (Exception e) {
                e.printStackTrace();
            }

            String unik_key = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).substring(0, 1)
                    .substring(0, 1)
                    .toUpperCase();
            if (old_latter.equals("")) {
                old_latter = unik_key;

            } else if (old_latter.equals(unik_key)) {
                old_latter = "";
            } else if (!old_latter.equals(unik_key)) {
                old_latter = unik_key;
            }
            boolean found = inviteListData.stream().anyMatch(p -> p.getUserPhoneNumber().equals(user_phone_number));

            if (found) {

            } else {


                inviteListData.add(new GroupListData("" + userName.trim(),
                        user_phone_number.trim(),
                        user_image,
                        user_des,
                        old_latter.trim(), ""));
                // userListDataAdapter.notifyDataSetChanged();

            }

        }

        groupContectAdapter = new GroupContectAdapter(getActivity());
        contect_list_unselect.setAdapter(groupContectAdapter);
        cursor.close();

    }


    public class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";
            try {
                if(Global.isNetworkAvailable(getActivity(),mMainLayout)) {
                    ContectEvent();
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
        }

    }

    private void ContectEvent() throws JSONException {
        loadingDialog.showLoadingDialog();

        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("page", currentPage);
        paramObject.addProperty("perPage", 0);
        paramObject.addProperty("status", "A");
        paramObject.addProperty("q", "");
        paramObject.addProperty("orderBy", "firstname");
        paramObject.addProperty("order", "asc");
        obj.add("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        RetrofitApiInterface registerinfo = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<ApiResponse> call = registerinfo.Contect_List(RetrofitApiClient.API_Header, token, obj, Global.getVersionname(getActivity()), Global.Device);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                //Log.e("Reponse is", new Gson().toJson(response.body()));
                if (response.body().getStatus() == 200) {

                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ContectListData>() {
                    }.getType();
                    ContectListData contectListData1 = new Gson().fromJson(headerString, listType);
                    contectListData.addAll(contectListData1.getContacts());
                    groupContectAdapter.addAll(contectListData);
                    if (contectListData1.getContacts().size() == limit) {
                        if (currentPage <= TOTAL_PAGES) {
                            groupContectAdapter.addLoadingFooter();
                        } else isLastPage = true;
                    } else {
                        isLastPage = true;
                        isLoading = false;

                    }

                    num_count.setText("" + contectListData1.getTotal() + " Contacts");

                    totale_group = contectListData1.getTotal();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Log.e("Error is", throwable.getMessage());
                loadingDialog.cancelLoading();

            }
        });


    }

    private void ContectEventnext() throws JSONException {


        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("page", currentPage);
        paramObject.addProperty("perPage", limit);
        paramObject.addProperty("status", "");
        paramObject.addProperty("q", "");
        paramObject.addProperty("orderBy", "firstname");
        paramObject.addProperty("order", "asc");
        obj.add("data", paramObject);

        Log.e("Request data", new Gson().toJson(obj));


        RetrofitApiInterface registerinfo = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<ApiResponse> call = registerinfo.Contect_List(RetrofitApiClient.API_Header, token, obj, Global.getVersionname(getActivity()), Global.Device);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body().getStatus() == 200) {


                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ContectListData>() {
                    }.getType();
                    groupContectAdapter.removeLoadingFooter();
                    ContectListData group_model = new Gson().fromJson(headerString, listType);
                    contectListData.clear();
                    contectListData.addAll(group_model.getContacts());
                    groupContectAdapter.addAll(contectListData);

                    if (group_model.getContacts().size() == limit) {
                        if (currentPage != TOTAL_PAGES) groupContectAdapter.addLoadingFooter();
                        else isLastPage = true;
                    } else {
                        isLastPage = true;
                        isLoading = false;
                    }

                    num_count.setText("" + group_model.getTotal() + " Group");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Log.e("Error is", throwable.getMessage());

            }
        });


    }

    public class TopUserListDataAdapter extends RecyclerView.Adapter<TopUserListDataAdapter.InviteListDataclass> {

        private final Context mcntx;
        private final List<ContectListData.Contact> userDetailsfull;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<ContectListData.Contact> userDetails;


        public TopUserListDataAdapter(Activity Ctx, Context mCtx, List<ContectListData.Contact> userDetails) {
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
            ContectListData.Contact inviteUserDetails = userDetails.get(position);
            last_postion = position;
            holder.userName.setText(select_contectListData.get(position).getFirstname());
            holder.top_layout.setVisibility(View.VISIBLE);

            String first_latter = select_contectListData.get(position).getFirstname().substring(0, 1).toUpperCase();

            if (second_latter.equals("")) {
                current_latter = first_latter;
                second_latter = first_latter;

            } else if (second_latter.equals(first_latter)) {
                current_latter = second_latter;
            } else {

                current_latter = first_latter;
                second_latter = first_latter;
            }


            String file = "" + select_contectListData.get(position).getContactImage();
            if (file.equals("null")) {
                holder.no_image.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);
                String name = select_contectListData.get(position).getFirstname();
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
                image_url = select_contectListData.get(position).getContactImage();

               /* if (holder.profile_image.getDrawable() == null) {
                  */  Glide.with(mCtx).
                            load(select_contectListData.get(position).getContactImage())
                            .placeholder(R.drawable.shape_primary_circle)
                            .error(R.drawable.shape_primary_circle)
                            .into(holder.profile_image);
                    //Log.e("Image ","View "+position);
                /*} else {
                    holder.profile_image.setVisibility(View.GONE);
                    String name = select_contectListData.get(position).getFirstname();
                    String add_text = "";
                    String[] split_data = name.split(" ");
                    for (int i = 0; i < split_data.length; i++) {
                        if (i == 0) {
                            add_text = split_data[i].substring(0, 1);
                        } else {
                            add_text = add_text + split_data[i].charAt(0);
                        }
                    }
                    holder.no_image.setText(add_text);
                    holder.no_image.setVisibility(View.VISIBLE);
                }*/

            }

            holder.top_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < contectListData.size(); i++) {
                        if (contectListData.get(i).getId().equals(select_contectListData.get(position).getId())) {

                            groupContectAdapter.notifyItemChanged(i);
                        }
                    }
                    userDetails.remove(position);
                    topUserListDataAdapter.notifyDataSetChanged();

                    num_count.setText(userDetails.size() + " Contacts");


                }
            });

            if (userDetails.get(position).getFlag().equals("true")) {
                Log.e("Call", "Yes");
                holder.top_layout.setVisibility(View.GONE);


            } else {
                holder.top_layout.setVisibility(View.VISIBLE);

            }

        }

        @Override
        public int getItemCount() {
            return userDetails.size();
        }

        public void remove_item(int item, Integer id) {

            for (int j = 0; j < userDetails.size(); j++) {
                if (id.equals(userDetails.get(j).getId())) {
                    userDetails.remove(j);
                    notifyItemRemoved(j);
                }
            }


        }

        public void updateList(List<ContectListData.Contact> list) {
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

    public class GroupContectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int LOADING = 0;
        private static final int ITEM = 1;
        private final Context context;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<ContectListData.Contact> contacts;
        private boolean isLoadingAdded = false;

        public GroupContectAdapter(Context context) {
            this.context = context;
            contacts = new LinkedList<>();
        }

        public void setContactList(List<ContectListData.Contact> contacts) {
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
                    View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                    viewHolder = new GroupContectAdapter.LoadingViewHolder(viewLoading);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ContectListData.Contact Contact_data = contacts.get(position);
            Log.e("Selcete List Is", new Gson().toJson(select_contectListData));
            switch (getItemViewType(position)) {
                case ITEM:
                    GroupContectAdapter.MovieViewHolder holder1 = (GroupContectAdapter.MovieViewHolder) holder;

                    /*     try {*/
                    //contacts.get(position).setFlag("true");
                    contacts.get(position).setFlag(group_flag);
                   // Log.e("Postion is", String.valueOf(position));
                    if (contacts.get(position).getFlag().equals("false")) {
                        holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                        holder1.add_new_contect_icon.setVisibility(View.GONE);
                    } else {
                        holder1.remove_contect_icon.setVisibility(View.GONE);
                        holder1.add_new_contect_icon.setVisibility(View.VISIBLE);
                    }


                    Log.e("List is", new Gson().toJson(select_contectListData));
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
                    if (Contact_data.getContactImage() == null) {
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
                    } else {
                        Glide.with(context).
                                load(Contact_data.getContactImage())
                                .placeholder(R.drawable.shape_primary_circle)
                                .error(R.drawable.shape_primary_circle)
                                .into(holder1.profile_image);
                        holder1.no_image.setVisibility(View.GONE);
                        holder1.profile_image.setVisibility(View.VISIBLE);
                    }


                  //  holder1.add_new_contect_icon.setVisibility(View.VISIBLE);

                    if (sessionManager.getGroupList(getActivity()).size() != 0) {

                        List<ContectListData.Contact> contact1 = sessionManager.getGroupList(getActivity());

                        for (int i = 0; i < contact1.size(); i++) {
                            if (contact1.get(i).getId().equals(contacts.get(position).getId())) {

                                if (holder1.add_new_contect_icon.getVisibility() == View.VISIBLE) {
                                    holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                                    holder1.add_new_contect_icon.setVisibility(View.GONE);
                                } else {
                                    contacts.get(position).setFlag("true");
                                    holder1.remove_contect_icon.setVisibility(View.GONE);
                                    holder1.add_new_contect_icon.setVisibility(View.VISIBLE);
                                }

                            } else {

                                if (holder1.remove_contect_icon.getVisibility() == View.GONE) {
                                    holder1.remove_contect_icon.setVisibility(View.GONE);
                                    holder1.add_new_contect_icon.setVisibility(View.VISIBLE);
                                } else {
                                    holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                                    holder1.add_new_contect_icon.setVisibility(View.GONE);
                                }


                            }
                        }

                    }
                    holder1.add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                          /*  Log.e("Data is", new Gson().toJson(contacts.get(position)));
                            //userDetailsfull.get(position).setId(position);
                            holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                            holder1.add_new_contect_icon.setVisibility(View.GONE);
                            select_contectListData.add(contacts.get(position));
                            //userDetailsfull.get(position).setId(position);
                            topUserListDataAdapter.notifyDataSetChanged();
                            num_count.setText(select_contectListData.size() + " Contact Selcted");
                            contacts.get(position).setFlag("false");

*/



                            List<ContectListData.Contact.ContactDetail> detailList=new ArrayList<>();

                            detailList.clear();
                            for (int i=0;i<contacts.get(position).getContactDetails().size();i++)
                            {
                                if (contacts.get(position).getContactDetails().get(i).getType().equals("NUMBER") && !contacts.get(position).getContactDetails().get(i).getEmailNumber().equals(""))
                                {
                                    detailList.add(contacts.get(position).getContactDetails().get(i));
                                }
                                else {
                                    // detailList.add(contacts.get(position).getContactDetails().get(i));
                                }
                            }
                            if (detailList.size()==1)
                            {
                                holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                                holder1.add_new_contect_icon.setVisibility(View.GONE);
                                select_contectListData.add(contacts.get(position));
                                //userDetailsfull.get(position).setId(position);
                                topUserListDataAdapter.notifyDataSetChanged();
                                num_count.setText(select_contectListData.size() + " Contact Selcted");
                                contacts.get(position).setFlag("false");

                                SessionManager.setGroupList(getActivity(), new ArrayList<>());
                                SessionManager.setGroupList(getActivity(), select_contectListData);


                            }
                            else if (detailList.size()>=1)
                            {
                                for(int i=0;i<detailList.size();i++){
                                    if(detailList.get(i).getIsDefault()==1){
                                        detailList.get(i).setPhoneSelect(true);
                                        break;
                                    }
                                }
                                Phone_bouttomSheet(detailList,holder1,contacts,position);
                                Log.e("Size is","More ONE");
                            }

                        }
                    });
                    holder1.remove_contect_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("On Click Remove ", "Remove");

                            holder1.remove_contect_icon.setVisibility(View.GONE);
                            holder1.add_new_contect_icon.setVisibility(View.VISIBLE);
                            topUserListDataAdapter.remove_item(position, contacts.get(position).getId());

                            topUserListDataAdapter.notifyDataSetChanged();
                            Log.e("Size is", new Gson().toJson(select_contectListData));
                            num_count.setText(select_contectListData.size() + " Contact Selcted");
                            contacts.get(position).setFlag("true");


                        }
                    });



                    /*}
                    catch (Exception e)
                    {
                        contacts.remove(position);
                    }*/


                    break;

                case LOADING:
                    GroupContectAdapter.LoadingViewHolder loadingViewHolder = (GroupContectAdapter.LoadingViewHolder) holder;
                    loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        }

        public void updateList(List<ContectListData.Contact> list) {
            contacts = list;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            // Log.e("Size is :: ",contacts.size()+"");
            return contacts == null ? 0 : contacts.size();
        }


        public void addAll_item(List<ContectListData.Contact> contectListData)
        {
            select_contectListData.clear();
            contacts.clear();
            for (int i=0;i<contectListData.size();i++)
            {

                groupContectAdapter = new GroupContectAdapter(getContext());
                contect_list_unselect.setAdapter(groupContectAdapter);
                group_flag="false";
                contectListData.get(i).setFlag("false");
                groupContectAdapter.addAll(contectListData);
                groupContectAdapter.notifyDataSetChanged();
                select_contectListData.add(contectListData.get(i));
                topUserListDataAdapter.notifyDataSetChanged();
              //  save_button.setTextColor(getResources().getColor(R.color.purple_200));

            }

        }
        @Override
        public int getItemViewType(int position) {
            return (position == contacts.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }

        public void addLoadingFooter() {
            isLoadingAdded = true;
            add(new ContectListData.Contact());
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = contacts.size() - 1;
            ContectListData.Contact result = getItem(position);

            if (result != null) {
                contacts.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void add(ContectListData.Contact contact) {
            contacts.add(contact);
            notifyItemInserted(contacts.size() - 1);
            //  notifyDataSetChanged();
        }

        public void addAll(List<ContectListData.Contact> contact) {
            for (ContectListData.Contact result : contact) {

                add(result);
            }

        }

        public ContectListData.Contact getItem(int position) {
            return contacts.get(position);
        }


        public class MovieViewHolder extends RecyclerView.ViewHolder {
            TextView no_image;
            TextView userName, userNumber, first_latter;
            CircleImageView profile_image;
            LinearLayout top_layout;
            ImageView add_new_contect_icon, remove_contect_icon;

            public MovieViewHolder(View itemView) {
                super(itemView);
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


    private void Phone_bouttomSheet(List<ContectListData.Contact.ContactDetail> detailList, GroupContectAdapter.MovieViewHolder holder1, List<ContectListData.Contact> contacts, int position) {

        final View mView = getLayoutInflater().inflate(R.layout.email_bottom_sheet, null);
        bottomSheetDialog_templateList1 = new BottomSheetDialog(getActivity(), R.style.CoffeeDialog);
        bottomSheetDialog_templateList1.setContentView(mView);
        TextView tv_done = bottomSheetDialog_templateList1.findViewById(R.id.tv_done);
        TextView tv_txt=bottomSheetDialog_templateList1.findViewById(R.id.tv_txt);
        tv_txt.setText("Please select contact");
        RecyclerView email_list = bottomSheetDialog_templateList1.findViewById(R.id.email_list);
        email_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        PhoneListAdepter emailListAdepter = new PhoneListAdepter(getActivity(), detailList,holder1,contacts,position,
                tv_done);
        email_list.setAdapter(emailListAdepter);
        email_list.setVisibility(View.VISIBLE);


        bottomSheetDialog_templateList1.show();
    }

    class PhoneListAdepter extends RecyclerView.Adapter<PhoneListAdepter.viewholder> {

        public Context mCtx;
        List<ContectListData.Contact.ContactDetail> userLinkedGmailList;

        GroupContectAdapter.MovieViewHolder holder1;

        List<ContectListData.Contact> contacts;
        int s_position;
        TextView tv_done;


        public PhoneListAdepter(Context applicationContext, List<ContectListData.Contact.ContactDetail> userLinkedGmailList, GroupContectAdapter.MovieViewHolder holder1, List<ContectListData.Contact> contacts, int s_position, TextView tv_done) {
            this.mCtx = applicationContext;
            this.userLinkedGmailList = userLinkedGmailList;
            this.holder1=holder1;
            this.contacts=contacts;
            this.s_position=s_position;
            this.tv_done=tv_done;
        }

        @NonNull
        @Override
        public PhoneListAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.email_select_layout, parent, false);
            return new PhoneListAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhoneListAdepter.viewholder holder, int position) {

            userLinkedGmailList.get(position).setFlag("false");
            holder.tv_item.setText("Phone("+userLinkedGmailList.get(position).getLabel()+")");
            holder.tv_phone.setText(userLinkedGmailList.get(position).getEmailNumber());
            holder.tv_phone.setVisibility(View.VISIBLE);
            if (userLinkedGmailList.get(position).isPhoneSelect())
            {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            }
            else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }

            if (userLinkedGmailList.get(position).getIsDefault().toString().equals("1")) {
                holder.iv_dufult.setVisibility(View.VISIBLE);
            } else {
                holder.iv_dufult.setVisibility(View.GONE);
            }



            holder.layout_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for(int i=0; i<userLinkedGmailList.size();i++){
                        if (userLinkedGmailList.get(i).isPhoneSelect())
                        {
                            userLinkedGmailList.get(i).setPhoneSelect(false);
                            break;
                        }
                    }
                    userLinkedGmailList.get(position).setPhoneSelect(true);
                 /*   holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                    holder1.add_new_contect_icon.setVisibility(View.GONE);*/
                    tv_done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            for (int i=0;i<contacts.get(s_position).getContactDetails().size();i++)
                            {
                                if (contacts.get(s_position).getContactDetails().get(i).getType().equals("NUMBER") && !contacts.get(position).getContactDetails().get(i).getEmailNumber().equals(""))
                                {
                                    // detailList.add(contacts.get(position).getContactDetails().get(i));
                                }
                                else {
                                    userLinkedGmailList.add(contacts.get(s_position).getContactDetails().get(i));
                                    break;
                                }
                            }
                            List<ContectListData.Contact.ContactDetail> contactDetails=new ArrayList<>();
                            contactDetails.add(userLinkedGmailList.get(position));
                            contactDetails.add(userLinkedGmailList.get(userLinkedGmailList.size()-1));
                            //Log.e("contactDetails",new Gson().toJson(userLinkedGmailList));
                            contacts.get(s_position).setContactDetails(contactDetails);
                            select_contectListData.add(contacts.get(position));
                            //userDetailsfull.get(position).setId(position);
                            SessionManager.setGroupList(getActivity(), new ArrayList<>());
                            SessionManager.setGroupList(getActivity(), select_contectListData);
                            topUserListDataAdapter.notifyDataSetChanged();
                            num_count.setText(select_contectListData.size()+" Contact Selcted");
                            contacts.get(position).setFlag("false");
                            holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                            holder1.add_new_contect_icon.setVisibility(View.GONE);
                            bottomSheetDialog_templateList1.cancel();
                        }
                    });

                    holder.iv_selected.setVisibility(View.VISIBLE);
                    holder.iv_unselected.setVisibility(View.GONE);
                    notifyItemChanged(position);
                    userLinkedGmailList.get(position).setFlag("false");
                    notifyDataSetChanged();
                }
            });


        }

        @Override
        public int getItemCount() {
            return userLinkedGmailList.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item,tv_phone;
            View line_view;
            ImageView iv_dufult, iv_selected, iv_unselected;
            LinearLayout layout_select;

            public viewholder(View view) {
                super(view);
                tv_item = view.findViewById(R.id.tv_item);
                line_view = view.findViewById(R.id.line_view);
                iv_dufult = view.findViewById(R.id.iv_dufult);
                iv_selected = view.findViewById(R.id.iv_selected);
                layout_select = view.findViewById(R.id.layout_select);
                iv_unselected = view.findViewById(R.id.iv_unselected);
                tv_phone=view.findViewById(R.id.tv_phone);
            }
        }
    }


}