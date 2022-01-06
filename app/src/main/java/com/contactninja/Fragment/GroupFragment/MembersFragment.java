package com.contactninja.Fragment.GroupFragment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
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
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.InviteListData;
import com.contactninja.Model.SigleGroupModel;
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
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;


public class MembersFragment extends Fragment {



    public MembersFragment() {
        // Required empty public constructor
    }
    Context mCtx;
    Cursor cursor;
    public static UserListDataAdapter userListDataAdapter;
    public static ArrayList<SigleGroupModel.Group.ContactDetail> inviteListData=new ArrayList<>();
    RecyclerView rvinviteuserdetails;
    String userName, user_phone_number,user_image,user_des,strtext="",old_latter="",contect_type="",contect_email,
            contect_type_work="",email_type_home="",email_type_work="",country="",city="",region="",street="",
            postcode="",postType="",note="";

    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    EditText contect_search;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_members, container, false);
        IntentUI(view);
        mCtx=getContext();

        loadingDialog=new LoadingDialog(getActivity());
        sessionManager = new SessionManager(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        rvinviteuserdetails.setLayoutManager(new LinearLayoutManager(mCtx, LinearLayoutManager.VERTICAL, false));
        rvinviteuserdetails.setHasFixedSize(true);
        try {
            Single_group();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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


                            inviteListData.get(position).getFirstname().substring(0, 1)
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
        rvinviteuserdetails=view.findViewById(R.id.contact_list);
        fastscroller=view.findViewById(R.id.fastscroller);
        fastscroller_thumb=view.findViewById(R.id.fastscroller_thumb);

    }
    private void getAllContect() {
        // loadingDialog.showLoadingDialog();
       /* class GetTasks extends AsyncTask<Void, Void, List<InviteListData>> {
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
        gt.execute();*/
    }


    public static class UserListDataAdapter extends RecyclerView.Adapter<UserListDataAdapter.InviteListDataclass>
            implements Filterable {

        int last_postion=0;
        public Activity mCtx;
        private final Context mcntx;
        private List<SigleGroupModel.Group.ContactDetail> userDetails;
        private List<SigleGroupModel.Group.ContactDetail> userDetailsfull;
        String second_latter="";
        String current_latter="",image_url="";
        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<SigleGroupModel.Group.ContactDetail> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(userDetailsfull);
                } else {
                    String userName = constraint.toString().toLowerCase().trim();
                    String userNumber = constraint.toString().toLowerCase().trim();
                    for (SigleGroupModel.Group.ContactDetail item : userDetailsfull) {
                        if (item.getFirstname().toLowerCase().contains(userName)
                                || item.getEmailNumber().toLowerCase().contains(userNumber)) {
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
                userDetails.addAll((List<SigleGroupModel.Group.ContactDetail>) results.values);
                notifyDataSetChanged();
            }
        };

        public UserListDataAdapter(Activity Ctx, Context mCtx, ArrayList<SigleGroupModel.Group.ContactDetail> userDetails) {
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
            SigleGroupModel.Group.ContactDetail inviteUserDetails = userDetails.get(position);

            last_postion=position;




                holder.first_latter.setVisibility(View.VISIBLE);
                holder.top_layout.setVisibility(View.VISIBLE);
                String first_latter=inviteUserDetails.getFirstname().substring(0,1).toUpperCase();

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
                    holder.first_latter.setVisibility(View.GONE);
                    holder.top_layout.setVisibility(View.GONE);

                }
                else {

                    current_latter=first_latter;
                    second_latter=first_latter;
                    holder.first_latter.setVisibility(View.VISIBLE);
                    holder.top_layout.setVisibility(View.VISIBLE);


                }


            if (inviteUserDetails.getContactImage()==null)
            {
                String name =inviteUserDetails.getFirstname();
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
                        load(inviteUserDetails.getContactImage())
                        .placeholder(R.drawable.shape_primary_circle)
                        .error(R.drawable.shape_primary_circle)
                        .into(holder.profile_image);
                holder.no_image.setVisibility(View.GONE);
                holder.profile_image.setVisibility(View.VISIBLE);
            }
            holder.userName.setText(inviteUserDetails.getFirstname());
            holder.userNumber.setText(inviteUserDetails.getEmailNumber());

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

        public void updateList(List<SigleGroupModel.Group.ContactDetail> list){
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




    private void Single_group() throws JSONException {
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        Grouplist.Group group_data = SessionManager.getGroupData(getActivity());
        String token = Global.getToken(sessionManager);
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", "1");
        paramObject.put("team_id", "1");
        paramObject.put("user_id", user_id);
        paramObject.put("page", 1);
        paramObject.put("perPage", 10);
        paramObject.put("id", group_data.getId());
        paramObject.put("q", "");
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        //Log.e("Obbject data", new Gson().toJson(gsonObject));
        retrofitCalls.SingleGroup_List(sessionManager,gsonObject, loadingDialog, token, Global.getVersionname(getActivity()),Global.Device,new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getStatus() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<SigleGroupModel.Group>() {
                    }.getType();
                    SigleGroupModel.Group group_model = new Gson().fromJson(headerString, listType);

                    Log.e("Group List is",new Gson().toJson(group_model.getContactIds()));
                    List<ContectListData.Contact> groupModel=new ArrayList<>();
                    ContectListData.Contact contact=new ContectListData.Contact();
                    for (int i=0;i<group_model.getContactIds().size();i++)
                    {
                        contact.setFirstname(group_model.getContactIds().get(i).getMobile());
                        contact.setLastname(group_model.getContactIds().get(i).getMobile());
                        //contact.setContactImage(group_model.getContactDetails().get(i).getContactImage());
                        List<ContectListData.Contact.ContactDetail> contactDetails=new ArrayList<>();
                        ContectListData.Contact.ContactDetail contactDetail=new ContectListData.Contact.ContactDetail();
                        contactDetail.setContactId(group_model.getContactIds().get(i).getProspectId());
                        contactDetail.setEmailNumber(group_model.getContactIds().get(i).getMobile());
                        contactDetails.add(contactDetail);
                        contact.setContactDetails(contactDetails);

                    }
                    groupModel.add(contact);
                    sessionManager.setGroupList(getActivity(),groupModel);
                 /*   inviteListData.addAll(group_model.getContactDetails());
                    userListDataAdapter = new UserListDataAdapter(getActivity(), getActivity(), inviteListData);
                    rvinviteuserdetails.setAdapter(userListDataAdapter);
                    userListDataAdapter.notifyDataSetChanged();*/
                    // userListDataAdapter.addAll(grouplists);

                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }

}