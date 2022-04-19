package com.contactninja.Group;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

import com.bumptech.glide.Glide;
import com.contactninja.Fragment.GroupFragment.MembersFragment;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.SigleGroupModel;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UservalidateModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class SendBroadcast extends AppCompatActivity implements View.OnClickListener,ConnectivityReceiver.ConnectivityReceiverListener {
    private long mLastClickTime = 0;
    TextView save_button;
    ImageView iv_Setting, iv_back,iv_toolbar_manu1;
    EditText add_detail, add_new_contect, ev_search;
    SessionManager sessionManager;
    RoundedImageView add_new_contect_icon;
    ConstraintLayout mMainLayout;
    TextView no_image, topic_remainingCharacter;

    private BroadcastReceiver mNetworkReceiver;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    int group_id;
    String group_name;

    private List<ContectListData.Contact> contectListData=new ArrayList<>();
    private List<ContectListData.Contact> selected_contectListData=new ArrayList<>();


    Context mCtx;
    Cursor cursor;
    public static UserListDataAdapter userListDataAdapter;
    public static ArrayList<SigleGroupModel.Group.ContactId> inviteListData=new ArrayList<>();
    RecyclerView rvinviteuserdetails;
    String userName, user_phone_number,user_image,user_des,strtext="",old_latter="",contect_type="",contect_email,
            contect_type_work="",email_type_home="",email_type_work="",country="",city="",region="",street="",
            postcode="",postType="",note="";

    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    ImageView iv_cancle_search_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_broadcast);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        Global.checkConnectivity(SendBroadcast.this, mMainLayout);
        sessionManager = new SessionManager(this);
        Grouplist.Group group_data = SessionManager.getGroupData(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        loadingDialog=new LoadingDialog(this);
        add_new_contect.setText(group_data.getGroupName());
        add_detail.setText(group_data.getDescription());
        if (add_detail.getText().toString().length() <= 100) {
            int num = 100 - add_detail.getText().toString().length();
            topic_remainingCharacter.setText(num + " " + getResources().getString(R.string.camp_remaingn));
        
        }
        group_id=group_data.getId();
        group_name=group_data.getGroupName();

        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Edit");
        save_button.setVisibility(View.GONE);
        iv_Setting.setVisibility(View.GONE);

        if (group_data.getGroupImage() == null) {
            String name = group_data.getGroupName();
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
            no_image.setText(add_text);
            no_image.setVisibility(View.VISIBLE);
            add_new_contect_icon.setVisibility(View.GONE);
        }
        else {
            Glide.with(getApplicationContext()).
                    load(group_data.getGroupImage()).
                    placeholder(R.drawable.shape_primary_back).
                    error(R.drawable.shape_primary_back).into(add_new_contect_icon);
            no_image.setVisibility(View.GONE);
            add_new_contect_icon.setVisibility(View.VISIBLE);
        }


        add_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               // Log.e("Test Clcik ", String.valueOf(charSequence));
                if (charSequence.toString().length() <= 100) {
                    int num = 100 - charSequence.toString().length();
                    topic_remainingCharacter.setText(num + " " + getResources().getString(R.string.camp_remaingn));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //    topic_remainingCharacter.setText(100 - editable.length() + " Characters Remaining.");
            }
        });


    /*    Fragment fragment = new MembersFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

*/
        mCtx=this;

        rvinviteuserdetails.setLayoutManager(new LinearLayoutManager(mCtx, LinearLayoutManager.VERTICAL, false));
        rvinviteuserdetails.setHasFixedSize(true);
        try {
            Single_group();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                            selected_contectListData.get(position).getFirstname().substring(0, 1)
                                    .substring(0, 1)
                                    .toUpperCase()// Grab the first letter and capitalize it
                    );
                    return  fastScrollItemIndicator;
                }
        );

        iv_cancle_search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ev_search.setText("");
                iv_cancle_search_icon.setVisibility(View.GONE);
                userListDataAdapter.updateList(selected_contectListData);
            }
        });
        ev_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                iv_cancle_search_icon.setVisibility(View.VISIBLE);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    List<ContectListData.Contact> temp = new ArrayList();
                    for (ContectListData.Contact d : selected_contectListData) {
                        if (d.getFirstname().toLowerCase().contains(ev_search.getText().toString())) {
                            temp.add(d);
                        }
                        userListDataAdapter.updateList(temp);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void IntentUI() {
        iv_toolbar_manu1=findViewById(R.id.iv_toolbar_manu_vertical);
        iv_toolbar_manu1.setVisibility(View.VISIBLE);
        iv_toolbar_manu1.setOnClickListener(this);
        save_button = findViewById(R.id.save_button);
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.VISIBLE);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        add_detail = findViewById(R.id.add_detail);
        add_new_contect_icon = findViewById(R.id.add_new_contect_icon);
        add_new_contect = findViewById(R.id.add_new_contect);
        mMainLayout = findViewById(R.id.mMainLayout);
        no_image = findViewById(R.id.no_image);
        topic_remainingCharacter = findViewById(R.id.topic_remainingCharacter);
        rvinviteuserdetails=findViewById(R.id.contact_list);
        fastscroller=findViewById(R.id.fastscroller);
        fastscroller_thumb=findViewById(R.id.fastscroller_thumb);
        ev_search =findViewById(R.id.ev_search);
        iv_cancle_search_icon=findViewById(R.id.iv_cancle_search_icon);
     
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_toolbar_manu_vertical:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (Global.isNetworkAvailable(this, mMainLayout)) {
                    broadcast_manu();
                }
                break;
            case R.id.iv_back:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                finish();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(getApplicationContext(), Final_Group.class));
                finish();
                break;
            default:


        }
    }

    private void broadcast_manu() {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.remove_group_layout, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);

        TextView selected_delete=bottomSheetDialog.findViewById(R.id.selected_delete);
        selected_delete.setText("Delete Group");
        TextView selected_edit=bottomSheetDialog.findViewById(R.id.selected_edit);
        selected_edit.setText("Edit Group");
        selected_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(getApplicationContext(), Final_Group.class));
                finish();
            }
        });
        selected_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    RemoveGroup();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bottomSheetDialog.dismiss();

            }
        });
        bottomSheetDialog.show();

    }


    private void RemoveGroup() throws JSONException {


          loadingDialog.showLoadingDialog();

        SignResponseModel user_data = sessionManager.getGetUserdata(this);
        JSONArray jsonArray = new JSONArray();

        // contect_array.put(3);
        String token = Global.getToken(sessionManager);
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("id", group_id);
        paramObject.put("status","D");
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        //Log.e("Data IS ",new Gson().toJson(obj));
        obj.put("data",paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        //  Log.e("Obbject data",new Gson().toJson(gsonObject));
        retrofitCalls.AddGroup(sessionManager, gsonObject, loadingDialog, token, Global.getVersionname(SendBroadcast.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    finish();
                } else {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Log.e("String is", response.body().getMessage());
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    UservalidateModel user_model = new Gson().fromJson(headerString, listType);
                    Global.Messageshow(getApplicationContext(), mMainLayout, user_model.getEmail().get(0), false);
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });





    }



    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(SendBroadcast.this, mMainLayout);
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


    public static class UserListDataAdapter extends RecyclerView.Adapter<UserListDataAdapter.InviteListDataclass>
            implements Filterable {

        int last_postion=0;
        public Activity mCtx;
        private final Context mcntx;
        private List<ContectListData.Contact> userDetails;
        private List<ContectListData.Contact> userDetailsfull;
        String second_latter="";
        String current_latter="",image_url="";
        private Filter exampleFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<ContectListData.Contact> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(userDetailsfull);
                } else {
                    String userName = constraint.toString().toLowerCase().trim();
                    String userNumber = constraint.toString().toLowerCase().trim();
                    for (ContectListData.Contact item : userDetailsfull) {
                        if (item.getFirstname().toLowerCase().contains(userName)
                                || item.getFirstname().toLowerCase().contains(userNumber)) {
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
                userDetails.addAll((List<ContectListData.Contact>) results.values);
                notifyDataSetChanged();
            }
        };

        public UserListDataAdapter(Activity Ctx, Context mCtx, List<ContectListData.Contact> userDetails) {
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
            ContectListData.Contact inviteUserDetails = userDetails.get(position);

            last_postion=position;




            holder.first_latter.setVisibility(View.VISIBLE);
            holder.top_layout.setVisibility(View.VISIBLE);
            String first_latter=inviteUserDetails.getFirstname().substring(0,1).toUpperCase();
            holder.first_latter.setText(first_latter);
          /*      if (second_latter.equals(""))
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


                }*/

            holder.add_new_contect_icon.setVisibility(View.GONE);
            holder.first_latter.setVisibility(View.GONE);
            holder.top_layout.setVisibility(View.GONE);
            if (!first_latter.equals(second_latter))
            {
                holder.first_latter.setText(first_latter);
                second_latter=first_latter;
                holder.first_latter.setVisibility(View.VISIBLE);
                holder.top_layout.setVisibility(View.VISIBLE);

            }


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
                e.printStackTrace();
            }


            holder.no_image.setText(add_text);
            holder.no_image.setVisibility(View.VISIBLE);
            holder.profile_image.setVisibility(View.GONE);
            holder.userName.setText(inviteUserDetails.getFirstname());
            //holder.userNumber.setText(inviteUserDetails.getMobile());
            holder.userNumber.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return userDetails.size();
        }

        @Override
        public Filter getFilter() {
            //Log.e("Fillter is",new Gson().toJson(exampleFilter));
            return exampleFilter;
        }

        public void updateList(List<ContectListData.Contact> list){
            userDetails=list;
            notifyDataSetChanged();
        }

        public static class InviteListDataclass extends RecyclerView.ViewHolder {

            TextView no_image;
            TextView userName, userNumber,first_latter;
            CircleImageView profile_image;
            LinearLayout top_layout;
            ImageView add_new_contect_icon;



            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                first_latter=itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image=itemView.findViewById(R.id.profile_image);
                no_image=itemView.findViewById(R.id.no_image);
                top_layout=itemView.findViewById(R.id.top_layout);
                add_new_contect_icon=itemView.findViewById(R.id.add_new_contect_icon);
            }

        }

    }




    private void
    Single_group() throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        Grouplist.Group group_data = SessionManager.getGroupData(getApplicationContext());
        String token = Global.getToken(sessionManager);
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("page", 1);
        paramObject.put("perPage", 10);
        paramObject.put("id", group_data.getId());
        paramObject.put("q", "");
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        //Log.e("Obbject data", new Gson().toJson(gsonObject));
        retrofitCalls.SingleGroup_List(sessionManager,gsonObject, loadingDialog, token, Global.getVersionname(this),Global.Device,new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<SigleGroupModel>() {
                    }.getType();
                    SigleGroupModel group_model = new Gson().fromJson(headerString, listType);

                    List<SigleGroupModel.Group> groups=new ArrayList<>();
                    groups.addAll(group_model.getGroups());
                    Log.e("Reponse is",new Gson().toJson(groups));


                    if (SessionManager.getContectList(getApplicationContext()).size() != 0) {
                        contectListData.addAll(SessionManager.getContectList(getApplicationContext()).get(0).getContacts());
                    }
                    for (int p=0;p<groups.get(0).getContactIds().size();p++)
                    {
                        String p_id= String.valueOf(groups.get(0).getContactIds().get(p).getProspectId());
                        OptionalInt indexOpt = IntStream.range(0, contectListData.size())
                                .filter(i -> p_id.equals(String.valueOf(contectListData.get(i).getId())))
                                .findFirst();
                        // Log.e("Matech Index", String.valueOf(indexOpt.getAsInt()));
                        try {
                            contectListData.get(indexOpt.getAsInt()).setFlag("false");
                            selected_contectListData.add(contectListData.get(indexOpt.getAsInt()));

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                    sessionManager.setGroupList(getApplicationContext(),selected_contectListData);
                    inviteListData.addAll(groups .get(0).getContactIds());


                    Collections.sort(selected_contectListData, new Comparator<ContectListData.Contact>() {
                        @Override
                        public int compare(ContectListData.Contact s1, ContectListData.Contact s2) {
                            return s1.getFirstname().compareToIgnoreCase(s2.getFirstname());
                        }
                    });
                    //  Log.e("Data Is ",new Gson().toJson(selected_contectListData));
                    rvinviteuserdetails.setItemViewCacheSize(5000);
                    userListDataAdapter = new UserListDataAdapter(SendBroadcast.this, getApplicationContext(), selected_contectListData);
                    rvinviteuserdetails.setAdapter(userListDataAdapter);
                    userListDataAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });


    }

}