package com.contactninja.Group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Addnewcontect_Activity;
import com.contactninja.Model.AddcontectModel;
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

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {

    //public static UserListDataAdapter userListDataAdapter;
    public static TopUserListDataAdapter  topUserListDataAdapter;
    public static ArrayList<GroupListData> inviteListData = new ArrayList<>();
    List<ContectListData.Contact> pre_seleact=new ArrayList<>();

    public static List<GroupListData> select_inviteListData = new ArrayList<>();
    TextView save_button;
    ImageView iv_more, iv_back;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        IntentUI();
        sessionManager=new SessionManager(this);
        loadingDialog = new LoadingDialog(this);
        retrofitCalls = new RetrofitCalls(this);
        select_contectListData=new ArrayList<>();
        contect_list_unselect.setHasFixedSize(true);
        contect_list_unselect.setItemViewCacheSize(500);
        contectListData = new ArrayList<>();

        contectListData.clear();
        iv_more.setVisibility(View.GONE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Next");
        save_button.setVisibility(View.VISIBLE);
        save_button.setTextColor(getColor(R.color.home_list_sub_data));
        loadingDialog = new LoadingDialog(this);
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
                        FastScrollItemIndicator fastScrollItemIndicator = new FastScrollItemIndicator.Text(
                                inviteListData.get(position).getUserName().substring(0, 1)
                                        .substring(0, 1)
                                        .toUpperCase()// Grab the first letter and capitalize it
                        );
                        return fastScrollItemIndicator;
                    }
                    catch (Exception e)
                    {
                      /*  FastScrollItemIndicator fastScrollItemIndicator = new FastScrollItemIndicator.Text(
                                inviteListData.get(position).getUserName().substring(0, 1)
                                        .substring(0, 1)
                                        .toUpperCase()// Grab the first letter and capitalize it
                        );*/
                        return null;
                    }

                }
        );




        topUserListDataAdapter=new TopUserListDataAdapter(this,getApplicationContext(),select_contectListData);
        add_contect_list.setAdapter(topUserListDataAdapter);
        topUserListDataAdapter.notifyDataSetChanged();
        GetContactsIntoArrayList();
        add_new_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnewcontect = new Intent(getApplicationContext(), Addnewcontect_Activity.class);
                SessionManager.setContect_flag("save");
                startActivity(addnewcontect);
            }
        });
        add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnewcontect = new Intent(getApplicationContext(), Addnewcontect_Activity.class);
                SessionManager.setContect_flag("save");
                startActivity(addnewcontect);
            }
        });
        add_new_contect_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnewcontect = new Intent(getApplicationContext(), Addnewcontect_Activity.class);
                SessionManager.setContect_flag("save");
                startActivity(addnewcontect);
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
                for(ContectListData.Contact d: contectListData){
                    if(d.getFirstname().contains(s.toString())){
                        temp.add(d);
                        // Log.e("Same Data ",d.getUserName());
                    }
                }
                groupContectAdapter = new GroupContectAdapter(GroupActivity.this);
                contect_list_unselect.setAdapter(groupContectAdapter);
                groupContectAdapter.notifyDataSetChanged();
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
                            currentPage=currentPage + 1;
                            Log.e("Current Page is", String.valueOf(currentPage));
                            ContectEventnext();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        if (SessionManager.getContectList(this).size() != 0) {
            GetContactsIntoArrayList();
            contectListData.addAll(SessionManager.getContectList(this).get(0).getContacts());
            groupContectAdapter.addAll(contectListData);
            num_count.setText(contectListData.size()+" Contacts");
            contect_list_unselect.setItemViewCacheSize(500);


        } else {
            GetContactsIntoArrayList();
            contect_list_unselect.setItemViewCacheSize(500);

            try {
                ContectEvent();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        call_updatedata();

    }

    public void call_updatedata()
    {
        if (sessionManager.getGroupList(this).size()!=0)
        {
            select_contectListData.clear();
            pre_seleact.clear();
            pre_seleact.addAll(sessionManager.getGroupList(this));
            select_contectListData.addAll(pre_seleact);
            topUserListDataAdapter.notifyDataSetChanged();



        }

    }

    private void IntentUI() {
        save_button = findViewById(R.id.save_button);
        iv_more = findViewById(R.id.iv_more);
        iv_back = findViewById(R.id.iv_back);
        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        add_contect_list = findViewById(R.id.add_contect_list);
        add_contect_list.setLayoutManager(layoutManager);
        contect_list_unselect = findViewById(R.id.contect_list_unselect);
        layoutManager1 = new LinearLayoutManager(this);
        contect_list_unselect.setLayoutManager(layoutManager1);
        fastscroller = findViewById(R.id.fastscroller);
        fastscroller_thumb = findViewById(R.id.fastscroller_thumb);
        contect_search = findViewById(R.id.contect_search);
        add_new_contect = findViewById(R.id.add_new_contect);
        num_count = findViewById(R.id.num_count);
        add_new_contect_icon = findViewById(R.id.add_new_contect_icon);
        add_new_contect_layout = findViewById(R.id.add_new_contect_layout);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void GetContactsIntoArrayList() {
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
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

        groupContectAdapter = new GroupContectAdapter(this);
        contect_list_unselect.setAdapter(groupContectAdapter);
        cursor.close();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:
               // Log.e("Main Data ",new Gson().toJson(select_inviteListData));

                sessionManager.setGroupList(this,new ArrayList<>());
                Intent intent=new Intent(getApplicationContext(),Final_Group.class);
                startActivity(intent);
                finish();
                sessionManager.setGroupList(getApplicationContext(), select_contectListData);
                break;
            default:


        }

    }




  /*  public class UserListDataAdapter extends RecyclerView.Adapter<UserListDataAdapter.InviteListDataclass>
            implements Filterable {

        private final Context mcntx;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<GroupListData> userDetails;
        private final List<GroupListData> userDetailsfull;
        private final Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<GroupListData> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(userDetailsfull);
                } else {
                    String userName = constraint.toString().toLowerCase().trim();
                    String userNumber = constraint.toString().toLowerCase().trim();
                    for (GroupListData item : userDetailsfull) {
                        if (item.getUserName().toLowerCase().contains(userName)
                                || item.getUserPhoneNumber().toLowerCase().contains(userNumber)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userDetails.clear();
                userDetails.addAll((List<GroupListData>) results.values);
                notifyDataSetChanged();
            }
        };

        public UserListDataAdapter(Activity Ctx, Context mCtx, ArrayList<GroupListData> userDetails) {
            this.mcntx = mCtx;
            this.mCtx = Ctx;
            this.userDetails = userDetails;
            userDetailsfull = new ArrayList<>(userDetails);
        }

        @NonNull
        @Override
        public UserListDataAdapter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.invite_user_details, parent, false);
            return new UserListDataAdapter.InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserListDataAdapter.InviteListDataclass holder, int position) {


            GroupListData inviteUserDetails = userDetailsfull.get(position);
            last_postion = position;
            Log.e("Size is", String.valueOf(select_inviteListData.size()));


            if (userDetailsfull.get(position).getFlag().equals("false"))
            {
                holder.remove_contect_icon.setVisibility(View.VISIBLE);
                holder.add_new_contect_icon.setVisibility(View.GONE);
            }
            else {
                holder.remove_contect_icon.setVisibility(View.GONE);
                holder.add_new_contect_icon.setVisibility(View.VISIBLE);
            }

            for (int i=0;i<select_inviteListData.size();i++)
            {

                if (inviteListData.get(select_inviteListData.get(i).getId()).getFlag().equals(select_inviteListData.get(i).getFlag()))
                {
                    Log.e("i is false",String.valueOf(i));

                    inviteListData.get(select_inviteListData.get(i).getId()).setFlag("false");


                }
                else {
                    Log.e("i is true",String.valueOf(i));
                    inviteListData.get(select_inviteListData.get(i).getId()).setFlag("true");
                    select_inviteListData.remove(select_inviteListData.get(i).getId());
                    topUserListDataAdapter.notifyDataSetChanged();
                }


            }
            holder.add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                       userDetailsfull.get(position).setId(position);
                        holder.remove_contect_icon.setVisibility(View.VISIBLE);
                        holder.add_new_contect_icon.setVisibility(View.GONE);
                        select_inviteListData.add(userDetailsfull.get(position));
                        userDetailsfull.get(position).setId(position);
                        topUserListDataAdapter.notifyDataSetChanged();
                        num_count.setText(select_contectListData.size()+" Contact Selcted");
                        userDetailsfull.get(position).setFlag("false");
                        save_button.setTextColor(getResources().getColor(R.color.purple_200));





                }
            });
            holder.remove_contect_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        userDetailsfull.get(position).setId(0);
                        holder.remove_contect_icon.setVisibility(View.GONE);
                        holder.add_new_contect_icon.setVisibility(View.VISIBLE);
                        select_inviteListData.remove(userDetailsfull.get(position));

                        topUserListDataAdapter.notifyDataSetChanged();
                        num_count.setText(select_contectListData.size()+" Contact Selcted");
                        userDetailsfull.get(position).setFlag("true");

                        if (select_inviteListData.size()==0)
                        {
                            save_button.setTextColor(getResources().getColor(R.color.black));
                        }
                        else {
                            save_button.setTextColor(getResources().getColor(R.color.purple_200));

                        }

                }
            });

            if (inviteUserDetails.getF_latter().equals("")) {
                holder.first_latter.setVisibility(View.GONE);
                holder.top_layout.setVisibility(View.GONE);
            } else {

                holder.first_latter.setVisibility(View.VISIBLE);
                holder.first_latter.setText(inviteUserDetails.getF_latter());
                holder.top_layout.setVisibility(View.VISIBLE);
                String first_latter = inviteUserDetails.getUserName().substring(0, 1).toUpperCase();

                if (second_latter.equals("")) {
                    current_latter = first_latter;
                    second_latter = first_latter;
                    holder.first_latter.setVisibility(View.VISIBLE);
                    holder.top_layout.setVisibility(View.VISIBLE);

                } else if (second_latter.equals(first_latter)) {
                    current_latter = second_latter;
                    inviteUserDetails.setF_latter("");
                    holder.first_latter.setVisibility(View.GONE);
                    holder.top_layout.setVisibility(View.GONE);

                } else {

                    current_latter = first_latter;
                    second_latter = first_latter;
                    holder.first_latter.setVisibility(View.VISIBLE);
                    holder.top_layout.setVisibility(View.VISIBLE);


                }
            }


            String file = "" + inviteUserDetails.getUserImageURL();
            if (file.equals("null")) {
                holder.no_image.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);
                String name = inviteUserDetails.getUserName();
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
                image_url = inviteUserDetails.getUserImageURL();

                if (holder.profile_image.getDrawable() == null) {
                    Glide.with(mCtx).
                            load(inviteUserDetails.getUserImageURL())
                            .placeholder(R.drawable.shape_primary_circle)
                            .error(R.drawable.shape_primary_circle)
                            .into(holder.profile_image);
                    //Log.e("Image ","View "+position);
                } else {
                    holder.profile_image.setVisibility(View.GONE);
                    String name = inviteUserDetails.getUserName();
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
                }

            }
            holder.userName.setText(inviteUserDetails.getUserName());
            holder.userNumber.setText(inviteUserDetails.getUserPhoneNumber());












        }

        @Override
        public int getItemCount() {
            return userDetails.size();
        }

        @Override
        public Filter getFilter() {
            Log.e("Fillter is", new Gson().toJson(exampleFilter));
            return exampleFilter;
        }

        public void updateList(List<GroupListData> list) {
            userDetails = list;
            notifyDataSetChanged();
        }

        public class InviteListDataclass extends RecyclerView.ViewHolder {

            TextView no_image;
            TextView userName, userNumber, first_latter;
            CircleImageView profile_image;
            LinearLayout top_layout;
            ImageView add_new_contect_icon,remove_contect_icon;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                first_latter = itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.top_layout);
                add_new_contect_icon=itemView.findViewById(R.id.add_new_contect_icon);
                remove_contect_icon=itemView.findViewById(R.id.remove_contect_icon);
                add_new_contect_icon.setVisibility(View.VISIBLE);

            }

        }

    }






*/


    public class TopUserListDataAdapter extends RecyclerView.Adapter<TopUserListDataAdapter.InviteListDataclass>
          {

        private final Context mcntx;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<ContectListData.Contact> userDetails;
        private final List<ContectListData.Contact> userDetailsfull;


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

            String first_latter =select_contectListData.get(position).getFirstname().substring(0, 1).toUpperCase();

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

                }


                holder.no_image.setText(add_text);
                holder.no_image.setVisibility(View.VISIBLE);

            } else {
                image_url = select_contectListData.get(position).getContactImage();

                if (holder.profile_image.getDrawable() == null) {
                    Glide.with(mCtx).
                            load(select_contectListData.get(position).getContactImage())
                            .placeholder(R.drawable.shape_primary_circle)
                            .error(R.drawable.shape_primary_circle)
                            .into(holder.profile_image);
                    //Log.e("Image ","View "+position);
                } else {
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
                }

            }

            holder.top_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<contectListData.size();i++)
                    {
                        if (contectListData.get(i).getId().equals(select_contectListData.get(position).getId()))
                        {
                            groupContectAdapter.notifyItemChanged(i);
                        }
                    }
                    userDetails.remove(position);
                    topUserListDataAdapter.notifyDataSetChanged();

                    num_count.setText(userDetails.size()+" Contacts");


                }
            });

            if (userDetails.get(position).getFlag().equals("true"))
            {
                Log.e("Call","Yes");
                holder.top_layout.setVisibility(View.GONE);


            }
            else {
                holder.top_layout.setVisibility(View.VISIBLE);

            }

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


    @Override
    protected void onResume() {
        super.onResume();
      /*  try {
            ContectEvent();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }



    private void ContectEvent() throws JSONException {
      //  loadingDialog.showLoadingDialog();

        SignResponseModel user_data = SessionManager.getGetUserdata(this);
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
        paramObject.addProperty("perPage", limit);
        paramObject.addProperty("status", "A");
        paramObject.addProperty("q", "");
        paramObject.addProperty("orderBy","firstname");
        paramObject.addProperty("order","asc");
        obj.add("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        RetrofitApiInterface registerinfo = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<ApiResponse> call = registerinfo.Contect_List(RetrofitApiClient.API_Header, token, obj);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                try {
                loadingDialog.cancelLoading();
                if (response.body().getStatus()==200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ContectListData>() {
                    }.getType();
                    ContectListData contectListData1 = new Gson().fromJson(headerString, listType);
                    contectListData.addAll(contectListData1.getContacts());
                    groupContectAdapter.addAll(contectListData);
                    if (contectListData1.getContacts().size() == limit) {
                        if (currentPage <= TOTAL_PAGES)
                        {
                            groupContectAdapter.addLoadingFooter();
                        }
                        else isLastPage = true;
                    } else {
                        isLastPage = true;
                        isLoading = false;

                    }

                    num_count.setText("" + contectListData1.getTotal() + " Contacts");

                    totale_group = contectListData1.getTotal();
                }
                }
                catch (Exception e)
                {

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


        SignResponseModel user_data = SessionManager.getGetUserdata(this);
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
        paramObject.addProperty("orderBy","firstname");
        paramObject.addProperty("order","asc");
        obj.add("data", paramObject);

        Log.e("Request data",new Gson().toJson(obj));


        RetrofitApiInterface registerinfo = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<ApiResponse> call = registerinfo.Contect_List(RetrofitApiClient.API_Header, token, obj);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body().getStatus()==200) {


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
            //Log.e("Postion is",String.valueOf(position));
            switch (getItemViewType(position)) {
                case ITEM:
                    GroupContectAdapter.MovieViewHolder holder1 = (GroupContectAdapter.MovieViewHolder) holder;


                    contacts.get(position).setFlag("true");





                    try {
                        Log.e("Postion is", String.valueOf(position));
                        if (contacts.get(position).getFlag().equals("false"))
                        {
                            holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                            holder1.add_new_contect_icon.setVisibility(View.GONE);
                        }
                        else {
                            holder1.remove_contect_icon.setVisibility(View.GONE);
                            holder1.add_new_contect_icon.setVisibility(View.VISIBLE);
                        }
                        Log.e("List is",new Gson().toJson(select_contectListData));
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


                        holder1.add_new_contect_icon.setVisibility(View.VISIBLE);

                        holder1.add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Log.e("Data is",new Gson().toJson(contacts.get(position)));
                                //userDetailsfull.get(position).setId(position);
                                holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                                holder1.add_new_contect_icon.setVisibility(View.GONE);
                                select_contectListData.add(contacts.get(position));
                                //userDetailsfull.get(position).setId(position);
                                topUserListDataAdapter.notifyDataSetChanged();
                                num_count.setText(select_contectListData.size()+" Contact Selcted");
                                contacts.get(position).setFlag("false");
                                save_button.setTextColor(getResources().getColor(R.color.purple_200));





                            }
                        });
                        holder1.remove_contect_icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("On Click Remove ","Remove");
                               // Log.e("Data is",new Gson().toJson(contacts.get(position)));
                                //userDetailsfull.get(position).setId(0);
                                holder1.remove_contect_icon.setVisibility(View.GONE);
                                holder1.add_new_contect_icon.setVisibility(View.VISIBLE);
                                topUserListDataAdapter.remove_item(position);

                                Log.e("Postionis ",String.valueOf(position));

                                topUserListDataAdapter.notifyDataSetChanged();
                                Log.e("Size is",new Gson().toJson(select_contectListData));
                                num_count.setText(select_contectListData.size()+" Contact Selcted");
                                contacts.get(position).setFlag("true");
                                if (select_contectListData.size()==0)
                                {
                                    save_button.setTextColor(getResources().getColor(R.color.black));
                                }
                                else {
                                    save_button.setTextColor(getResources().getColor(R.color.purple_200));

                                }

                            }
                        });

                        if (sessionManager.getGroupList(getApplicationContext()).size()!=0)
                        {

                            List<ContectListData.Contact> contact1= sessionManager.getGroupList(getApplicationContext());

                            for (int i=0;i<contact1.size();i++)
                            {
                                if (contact1.get(i).getId().equals(contacts.get(position).getId())) {

                                    if (holder1.add_new_contect_icon.getVisibility()==View.VISIBLE)
                                    {
                                        holder1.remove_contect_icon.setVisibility(View.GONE);
                                        holder1.add_new_contect_icon.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        contacts.get(position).setFlag("true");
                                        holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                                        holder1.add_new_contect_icon.setVisibility(View.GONE);
                                    }

                                }
                                else {

                                    if (holder1.remove_contect_icon.getVisibility()==View.GONE)
                                    {
                                        holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                                        holder1.add_new_contect_icon.setVisibility(View.GONE);
                                    }
                                    else {
                                        holder1.remove_contect_icon.setVisibility(View.GONE);
                                        holder1.add_new_contect_icon.setVisibility(View.VISIBLE);
                                    }


                                }
                            }

                        }

                    }
                    catch (Exception e)
                    {
                        contacts.remove(position);
                    }


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
            ImageView add_new_contect_icon,remove_contect_icon;

            public MovieViewHolder(View itemView) {
                super(itemView);
                first_latter = itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.top_layout);
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



}