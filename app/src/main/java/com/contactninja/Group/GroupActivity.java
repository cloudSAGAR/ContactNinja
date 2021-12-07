package com.contactninja.Group;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Addnewcontect_Activity;
import com.contactninja.Fragment.ContectFragment;
import com.contactninja.Model.InviteListData;
import com.contactninja.R;
import com.contactninja.Utils.LoadingDialog;
import com.google.gson.Gson;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {

    public static UserListDataAdapter userListDataAdapter;
    public static TopUserListDataAdapter  topUserListDataAdapter;
    public static ArrayList<InviteListData> inviteListData = new ArrayList<>();

    public static ArrayList<InviteListData> select_inviteListData = new ArrayList<>();
    TextView save_button;
    ImageView iv_more, iv_back;
    RecyclerView add_contect_list, contect_list_unselect;
    RecyclerView.LayoutManager layoutManager, layoutManager1;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        IntentUI();
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
                    // ItemModel item = data.get(position);
                    FastScrollItemIndicator fastScrollItemIndicator = new FastScrollItemIndicator.Text(


                            inviteListData.get(position).getUserName().substring(0, 1)
                                    .substring(0, 1)
                                    .toUpperCase()// Grab the first letter and capitalize it
                    );
                    return fastScrollItemIndicator;
                }
        );

        GetContactsIntoArrayList();
        add_new_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnewcontect = new Intent(getApplicationContext(), Addnewcontect_Activity.class);
                startActivity(addnewcontect);
            }
        });
        add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnewcontect = new Intent(getApplicationContext(), Addnewcontect_Activity.class);
                startActivity(addnewcontect);
            }
        });
        add_new_contect_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnewcontect = new Intent(getApplicationContext(), Addnewcontect_Activity.class);
                startActivity(addnewcontect);
                // splitdata(inviteListData);
            }
        });

        userListDataAdapter = new UserListDataAdapter(this, getApplicationContext(), inviteListData);
        contect_list_unselect.setAdapter(userListDataAdapter);
        userListDataAdapter.notifyDataSetChanged();


        topUserListDataAdapter=new TopUserListDataAdapter(this,getApplicationContext(),select_inviteListData);
        add_contect_list.setAdapter(topUserListDataAdapter);
        userListDataAdapter.notifyDataSetChanged();



        contect_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<InviteListData> temp = new ArrayList();
                for(InviteListData d: inviteListData){
                    if(d.getUserName().contains(s.toString())){
                        temp.add(d);
                        // Log.e("Same Data ",d.getUserName());
                    }
                }
                userListDataAdapter = new UserListDataAdapter(GroupActivity.this, getApplicationContext(), inviteListData);
                contect_list_unselect.setAdapter(userListDataAdapter);
                userListDataAdapter.notifyDataSetChanged();
                userListDataAdapter.updateList(temp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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


               inviteListData.add(new InviteListData("" + userName.trim(),
                        user_phone_number.trim(),
                        user_image,
                        user_des,
                        old_latter.trim(), ""));
                //userListDataAdapter.notifyDataSetChanged();

            }

        }


        cursor.close();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:

                break;
            default:


        }

    }


    public class UserListDataAdapter extends RecyclerView.Adapter<UserListDataAdapter.InviteListDataclass>
            implements Filterable {

        private final Context mcntx;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<InviteListData> userDetails;
        private final List<InviteListData> userDetailsfull;
        private final Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<InviteListData> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(userDetailsfull);
                } else {
                    String userName = constraint.toString().toLowerCase().trim();
                    String userNumber = constraint.toString().toLowerCase().trim();
                    for (InviteListData item : userDetailsfull) {
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
                userDetails.addAll((List<InviteListData>) results.values);
                notifyDataSetChanged();
            }
        };

        public UserListDataAdapter(Activity Ctx, Context mCtx, ArrayList<InviteListData> userDetails) {
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
            InviteListData inviteUserDetails = userDetails.get(position);

            last_postion = position;


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

           holder.add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.remove_contect_icon.setVisibility(View.VISIBLE);
                    holder.add_new_contect_icon.setVisibility(View.GONE);
                    select_inviteListData.add(userDetails.get(position));
                    topUserListDataAdapter.notifyDataSetChanged();
                }
            });
           holder.remove_contect_icon.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   holder.remove_contect_icon.setVisibility(View.GONE);
                   holder.add_new_contect_icon.setVisibility(View.VISIBLE);
                   select_inviteListData.remove(userDetails.get(position));
                   topUserListDataAdapter.notifyDataSetChanged();
               }
           });

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

        public void updateList(List<InviteListData> list) {
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









    public class TopUserListDataAdapter extends RecyclerView.Adapter<TopUserListDataAdapter.InviteListDataclass>
            implements Filterable {

        private final Context mcntx;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<InviteListData> userDetails;
        private final List<InviteListData> userDetailsfull;
        private final Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<InviteListData> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(userDetailsfull);
                } else {
                    String userName = constraint.toString().toLowerCase().trim();
                    String userNumber = constraint.toString().toLowerCase().trim();
                    for (InviteListData item : userDetailsfull) {
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
                userDetails.addAll((List<InviteListData>) results.values);
                notifyDataSetChanged();
            }
        };

        public TopUserListDataAdapter(Activity Ctx, Context mCtx, ArrayList<InviteListData> userDetails) {
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
            InviteListData inviteUserDetails = userDetails.get(position);
            Log.e("Data is",new Gson().toJson(userDetails));

            last_postion = position;
            holder.userName.setText(inviteUserDetails.getUserName());
            Log.e("User Name is",holder.userName.getText().toString());
            holder.top_layout.setVisibility(View.VISIBLE);

            if (inviteUserDetails.getF_latter().equals("")) {
                  // holder.top_layout.setVisibility(View.GONE);
            } else {


                //holder.top_layout.setVisibility(View.VISIBLE);
                String first_latter = inviteUserDetails.getUserName().substring(0, 1).toUpperCase();

                if (second_latter.equals("")) {
                    current_latter = first_latter;
                    second_latter = first_latter;
                    //holder.top_layout.setVisibility(View.VISIBLE);

                } else if (second_latter.equals(first_latter)) {
                    current_latter = second_latter;
                    inviteUserDetails.setF_latter("");

                  //  holder.top_layout.setVisibility(View.GONE);

                } else {

                    current_latter = first_latter;
                    second_latter = first_latter;
                  //  holder.top_layout.setVisibility(View.VISIBLE);


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

            holder.top_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userDetails.remove(position);
                    topUserListDataAdapter.notifyDataSetChanged();
                }
            });

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

        public void updateList(List<InviteListData> list) {
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