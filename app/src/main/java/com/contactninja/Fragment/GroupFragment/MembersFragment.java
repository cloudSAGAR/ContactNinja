package com.contactninja.Fragment.GroupFragment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.contactninja.Fragment.ContectFragment;
import com.contactninja.Model.InviteListData;
import com.contactninja.R;
import com.contactninja.Utils.DatabaseClient;
import com.contactninja.Utils.LoadingDialog;
import com.google.gson.Gson;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MembersFragment extends Fragment {



    public MembersFragment() {
        // Required empty public constructor
    }
    Context mCtx;
    Cursor cursor;
    public static UserListDataAdapter userListDataAdapter;
    public static ArrayList<InviteListData> inviteListData=new ArrayList<>();
    RecyclerView rvinviteuserdetails;
    String userName, user_phone_number,user_image,user_des,strtext="",old_latter="",contect_type="",contect_email,
            contect_type_work="",email_type_home="",email_type_work="",country="",city="",region="",street="",
            postcode="",postType="",note="";

    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    EditText contect_search;
    LoadingDialog loadingDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_members, container, false);
        IntentUI(view);
        mCtx=getContext();
        loadingDialog=new LoadingDialog(getActivity());
        rvinviteuserdetails.setLayoutManager(new LinearLayoutManager(mCtx, LinearLayoutManager.VERTICAL, false));
        rvinviteuserdetails.setHasFixedSize(true);
        getAllContect();
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
                rvinviteuserdetails,
                (position) -> {
                    // ItemModel item = data.get(position);
                    FastScrollItemIndicator fastScrollItemIndicator= new FastScrollItemIndicator.Text(


                            inviteListData.get(position).getUserName().substring(0, 1)
                                    .substring(0, 1)
                                    .toUpperCase()// Grab the first letter and capitalize it
                    );
                    return  fastScrollItemIndicator;
                }
        );


        return view;
    }
    private void IntentUI(View view) {
        contect_search = view.findViewById(R.id.contect_search);
        rvinviteuserdetails=view.findViewById(R.id.contect_list);
        fastscroller=view.findViewById(R.id.fastscroller);
        fastscroller_thumb=view.findViewById(R.id.fastscroller_thumb);

    }
    private void getAllContect() {
        // loadingDialog.showLoadingDialog();
        class GetTasks extends AsyncTask<Void, Void, List<InviteListData>> {
            @Override
            protected List<InviteListData> doInBackground(Void... voids) {
                List<InviteListData> taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .taskDao()
                        .getvalue1();

                return taskList;
            }

            @Override
            protected void onPostExecute(List<InviteListData> tasks) {
                if (tasks.size()==0)
                {
                    loadingDialog.cancelLoading();
                   // getAllContect();
                }
                else {
                    loadingDialog.cancelLoading();
                    inviteListData.addAll(tasks);
                    userListDataAdapter = new UserListDataAdapter(getActivity(), getActivity(), inviteListData);
                    rvinviteuserdetails.setAdapter(userListDataAdapter);
                    userListDataAdapter.notifyDataSetChanged();
                    super.onPostExecute(tasks);
                }

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }


    public static class UserListDataAdapter extends RecyclerView.Adapter<UserListDataAdapter.InviteListDataclass>
            implements Filterable {

        int last_postion=0;
        public Activity mCtx;
        private final Context mcntx;
        private List<InviteListData> userDetails;
        private List<InviteListData> userDetailsfull;
        String second_latter="";
        String current_latter="",image_url="";
        private Filter exampleFilter = new Filter() {
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

            last_postion=position;


            if (inviteUserDetails.getF_latter().equals(""))
            {
                holder.first_latter.setVisibility(View.GONE);
                holder.top_layout.setVisibility(View.GONE);
            }
            else {

                holder.first_latter.setVisibility(View.VISIBLE);
                holder.first_latter.setText(inviteUserDetails.getF_latter());
                holder.top_layout.setVisibility(View.VISIBLE);
                String first_latter=inviteUserDetails.getUserName().substring(0,1).toUpperCase();

                if (second_latter.equals(""))
                {
                    current_latter=first_latter;
                    second_latter=first_latter;
                    holder.first_latter.setVisibility(View.VISIBLE);
                    holder.top_layout.setVisibility(View.VISIBLE);

                }
                else if (second_latter.equals(first_latter))
                {
                    current_latter=second_latter;
                    inviteUserDetails.setF_latter("");
                    holder.first_latter.setVisibility(View.GONE);
                    holder.top_layout.setVisibility(View.GONE);

                }
                else {

                    current_latter=first_latter;
                    second_latter=first_latter;
                    holder.first_latter.setVisibility(View.VISIBLE);
                    holder.top_layout.setVisibility(View.VISIBLE);


                }
            }

            if (inviteUserDetails.getUserImageURL()==null)
            {
                String name =inviteUserDetails.getUserName();
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

                }


                holder.no_image.setText(add_text);
                holder.no_image.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);
            }
            else {
                Glide.with(mCtx).
                        load(inviteUserDetails.getUserImageURL())
                        .placeholder(R.drawable.shape_primary_circle)
                        .error(R.drawable.shape_primary_circle)
                        .into(holder.profile_image);
                holder.no_image.setVisibility(View.GONE);
                holder.profile_image.setVisibility(View.VISIBLE);
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
            Log.e("Fillter is",new Gson().toJson(exampleFilter));
            return exampleFilter;
        }

        public void updateList(List<InviteListData> list){
            userDetails=list;
            notifyDataSetChanged();
        }

        public  class InviteListDataclass extends RecyclerView.ViewHolder {

            TextView no_image;
            TextView userName, userNumber,first_latter;
            CircleImageView profile_image;
            LinearLayout top_layout;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                first_latter=itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image=itemView.findViewById(R.id.profile_image);
                no_image=itemView.findViewById(R.id.no_image);
                top_layout=itemView.findViewById(R.id.top_layout);
            }

        }

    }
}