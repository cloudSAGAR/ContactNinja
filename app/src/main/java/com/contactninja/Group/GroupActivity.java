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
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.GroupListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
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

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class GroupActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    public static TopUserListDataAdapter topUserListDataAdapter;
    public static ArrayList<GroupListData> inviteListData = new ArrayList<>();
    public static List<GroupListData> select_inviteListData = new ArrayList<>();
    private final int amountOfItemsSelected = 0;
    BottomSheetDialog bottomSheetDialog_templateList1;
    String group_flag = "true";
    LinearLayout layout_list_number_select;
    List<ContectListData.Contact> pre_seleact = new ArrayList<>();
    TextView save_button;
    ImageView iv_Setting, iv_back;
    RecyclerView add_contect_list, contect_list_unselect;
    LinearLayoutManager layoutManager, layoutManager1;
    Cursor cursor;
    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    EditText ev_search;
    TextView add_new_contect, num_count;
    ImageView add_new_contect_icon, add_new_contect_icon1, iv_cancle_search_icon;
    LinearLayout add_new_contect_layout, mMainLayout;
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
    List<ContectListData.Contact> contectListData = new ArrayList<>();
    List<ContectListData.Contact> select_contectListData = new ArrayList<>();
    //public static UserListDataAdapter userListDataAdapter;
    private long mLastClickTime = 0;
    private BroadcastReceiver mNetworkReceiver;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        mNetworkReceiver = new ConnectivityReceiver();
        inviteListData.clear();
        pre_seleact.clear();
        select_inviteListData.clear();
        contectListData.clear();
        select_contectListData.clear();
        IntentUI();
        Global.checkConnectivity(GroupActivity.this, mMainLayout);
        sessionManager = new SessionManager(this);
        loadingDialog = new LoadingDialog(GroupActivity.this);
        retrofitCalls = new RetrofitCalls(this);
        contect_list_unselect.setHasFixedSize(true);
        contect_list_unselect.setItemViewCacheSize(50000);
        add_contect_list.setItemViewCacheSize(50000);

        contectListData.clear();
        iv_Setting.setVisibility(View.GONE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText(getResources().getString(R.string.Next));
        save_button.setVisibility(View.VISIBLE);
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
                                contectListData.get(position).getFirstname().substring(0, 1)
                                        .substring(0, 1)
                                        .toUpperCase()// Grab the first letter and capitalize it
                        );
                        return fastScrollItemIndicator;
                    } catch (Exception e) {
                        return null;
                    }
                }
        );


        iv_cancle_search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contectListData.clear();
                groupContectAdapter.clear();
                contectListData.addAll(SessionManager.getContectList(getApplicationContext()).get(0).getContacts());
                onScrolledToBottom();
                iv_cancle_search_icon.setVisibility(View.GONE);
                ev_search.setText("");
            }
        });
        ev_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    iv_cancle_search_icon.setVisibility(View.VISIBLE);
                    List<ContectListData.Contact> temp = new ArrayList();
                    for (ContectListData.Contact d : contectListData) {
                        if (d.getFirstname().toLowerCase().contains(ev_search.getText().toString())) {
                            temp.add(d);
                        }

                    }
                    contect_list_unselect.setItemViewCacheSize(50000);
                    contectListData.clear();
                    contectListData.addAll(temp);
                    add_contect_list.setItemViewCacheSize(50000);
                    groupContectAdapter.clear();
                    onScrolledToBottom();
                    // groupContectAdapter.updateList(temp);
                  //  Log.e("Temp List Data is", new Gson().toJson(temp));
                    return true;
                }
                return false;
            }
        });


        groupContectAdapter = new GroupContectAdapter(this);


        if (SessionManager.getContectList(this).size() != 0) {
            //  GetContactsIntoArrayList();
            contect_list_unselect.setItemViewCacheSize(50000);
            contectListData.addAll(SessionManager.getContectList(this).get(0).getContacts());
            //    Log.e("contectListData", new Gson().toJson(contectListData));
            //   groupContectAdapter.addAll(contectListData);
            onScrolledToBottom();
        } else {
            // GetContactsIntoArrayList();
            contect_list_unselect.setItemViewCacheSize(50000);

            try {
                ContectEvent();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        add_new_contect_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }

                mLastClickTime = SystemClock.elapsedRealtime();


                if (SessionManager.getContectList(getApplicationContext()).size() != 0) {
                    contect_list_unselect.setItemViewCacheSize(50000);
                    add_contect_list.setItemViewCacheSize(50000);

                    if (add_new_contect_icon1.getVisibility() == View.GONE) {
                        Global.Please_wait(GroupActivity.this,mMainLayout,"Please Wait...",false);
                        Handler handler = new Handler();
                        Runnable r = new Runnable() {
                            @SuppressLint("NotifyDataSetChanged")
                            public void run() {
                                select_contectListData.clear();
                                add_new_contect_icon1.setVisibility(View.VISIBLE);
                                add_new_contect_icon.setVisibility(View.GONE);
                                groupContectAdapter.addAll_item(contectListData);
                                loadingDialog.cancelLoading();
                                add_new_contect.setText(getString(R.string.remove_new_contect1));
                                select_Contact(contectListData.size());
                            }
                        };
                        handler.postDelayed(r, 1000);
                    } else {
                        add_new_contect_icon1.setVisibility(View.GONE);
                        add_new_contect_icon.setVisibility(View.VISIBLE);
                        select_contectListData.clear();
                        group_flag = "true";
                        groupContectAdapter.addAll(contectListData);
                        groupContectAdapter.notifyDataSetChanged();
                        add_new_contect.setText(getString(R.string.add_new_contect1));
                        SessionManager.setGroupList(GroupActivity.this, new ArrayList<>());
                        onResume();
                        select_Contact(0);

                    }
                }
                else {

                    Global.Messageshow(getApplicationContext(),mMainLayout,getString(R.string.snk_contect),false);

                }

            }
        });

       /* contect_list_unselect.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom();

            }
        });*/


    }


    private void onScrolledToBottom() {

        if (groupContectAdapter.getItemCount() < contectListData.size()) {
            int x, y;
            if ((contectListData.size() - groupContectAdapter.getItemCount()) >= contectListData.size()) {
                x = groupContectAdapter.getItemCount();
                y = x + contectListData.size();
            } else {
                x = groupContectAdapter.getItemCount();
                y = x + contectListData.size() - groupContectAdapter.getItemCount();
            }
           for (int i = x; i < y; i++) {
               contectListData.get(i).setFlag("true");
               groupContectAdapter.add(contectListData.get(i));
            }
           // group_flag = "true";
         //   groupContectAdapter.addAll(contectListData.subList(x, y));
            //groupContectAdapter.notifyDataSetChanged();
        }

    }

    public void call_updatedata() {
        if (SessionManager.getGroupList(this).size() != 0) {
            contect_list_unselect.setItemViewCacheSize(50000);
            add_contect_list.setItemViewCacheSize(50000);

            select_contectListData.clear();
            pre_seleact.clear();
            pre_seleact.addAll(SessionManager.getGroupList(this));
            select_contectListData.addAll(pre_seleact);
            /*
             * set select contact count */
            select_Contact(select_contectListData.size());

        }
        /*
         * selected number list show
         * */
        if (select_contectListData.size() != 0) {
            layout_list_number_select.setVisibility(View.VISIBLE);
        } else {
            layout_list_number_select.setVisibility(View.GONE);
        }

    }

    private void select_Contact(int size) {
        if (size != 0) {
            num_count.setVisibility(View.VISIBLE);
            num_count.setText(size + " " + getResources().getString(R.string.selected));
        } else {
            num_count.setVisibility(View.GONE);
        }
    }

    private void IntentUI() {
        iv_cancle_search_icon = findViewById(R.id.iv_cancle_search_icon);
        layout_list_number_select = findViewById(R.id.layout_list_number_select);
        add_new_contect_icon1 = findViewById(R.id.add_new_contect_icon1);
        save_button = findViewById(R.id.save_button);
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        add_contect_list = findViewById(R.id.add_contect_list);
        add_contect_list.setLayoutManager(layoutManager);
        contect_list_unselect = findViewById(R.id.contect_list_unselect);
        layoutManager1 = new LinearLayoutManager(this);
        contect_list_unselect.setLayoutManager(layoutManager1);
        fastscroller = findViewById(R.id.fastscroller);
        fastscroller_thumb = findViewById(R.id.fastscroller_thumb);
        ev_search = findViewById(R.id.ev_search);
        add_new_contect = findViewById(R.id.add_new_contect);
        num_count = findViewById(R.id.num_count);
        add_new_contect_icon = findViewById(R.id.add_new_contect_icon);
        add_new_contect_layout = findViewById(R.id.add_new_contect_layout);
        mMainLayout = findViewById(R.id.mMainLayout);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                SessionManager.setGroupData(this, new Grouplist.Group());
                finish();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                // Log.e("Main Data ",new Gson().toJson(select_inviteListData));
                if (select_contectListData.size() != 0) {
                    SessionManager.setGroupList(this, new ArrayList<>());
                    Intent intent = new Intent(getApplicationContext(), Final_Group.class);
                    startActivity(intent);
                    finish();
                    SessionManager.setGroupList(getApplicationContext(), select_contectListData);
                } else {
                    Global.Messageshow(getApplicationContext(),
                            mMainLayout, getResources().getString(R.string.camp_select_contect), false);
                }
                break;
            default:

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

       // Log.e("Selcete List Is", new Gson().toJson(select_contectListData));
        select_contectListData.clear();
        add_contect_list.setItemViewCacheSize(50000);
        topUserListDataAdapter = new TopUserListDataAdapter(this, getApplicationContext(), select_contectListData);
        add_contect_list.setAdapter(topUserListDataAdapter);
        topUserListDataAdapter.notifyDataSetChanged();
        contect_list_unselect.setAdapter(groupContectAdapter);
        call_updatedata();
      /*  try {
            ContectEvent();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    private void ContectEvent() throws JSONException {
        //  loadingDialog.showLoadingDialog();

        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("page", currentPage);
        paramObject.addProperty("perPage", 0);
        paramObject.addProperty("status", "A");
        paramObject.addProperty("q", "");
        paramObject.addProperty("orderBy", "firstname");
        paramObject.addProperty("order", "asc");
        paramObject.addProperty("phone_book", 1);
        obj.add("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        RetrofitApiInterface registerinfo = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<ApiResponse> call = registerinfo.Contect_List(RetrofitApiClient.API_Header, token, obj, Global.getVersionname(GroupActivity.this), Global.Device);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                try {
                    loadingDialog.cancelLoading();
                    if (response.body().getHttp_status() == 200) {
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
                        /*
                         * set select contact count */
                        select_Contact(contectListData1.getTotal());

                        totale_group = contectListData1.getTotal();
                    }
                    else
                    {
                        sessionManager.setContectList(getApplicationContext(), new ArrayList<>());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
            //    Log.e("Error is", throwable.getMessage());
                loadingDialog.cancelLoading();

            }
        });


    }

    private void Phone_bouttomSheet(List<ContectListData.Contact.ContactDetail> detailList_phone,
                                    GroupContectAdapter.MovieViewHolder holder1, List<ContectListData.Contact> contacts,
                                    int position, List<ContectListData.Contact.ContactDetail> detailList1_email) {

    //    Log.e("Data list is", new Gson().toJson(detailList_phone));
        final View mView = getLayoutInflater().inflate(R.layout.phone_bottom_sheet, null);
        bottomSheetDialog_templateList1 = new BottomSheetDialog(GroupActivity.this, R.style.CoffeeDialog);
        bottomSheetDialog_templateList1.setContentView(mView);

        LinearLayout layout_phone = bottomSheetDialog_templateList1.findViewById(R.id.layout_phone);
        View v_phone = bottomSheetDialog_templateList1.findViewById(R.id.v_phone);
        View v_phone1 = bottomSheetDialog_templateList1.findViewById(R.id.v_phone1);
        LinearLayout layout_email = bottomSheetDialog_templateList1.findViewById(R.id.layout_email);
        LinearLayout bottom_sheet = bottomSheetDialog_templateList1.findViewById(R.id.bottom_sheet);
        View v_email = bottomSheetDialog_templateList1.findViewById(R.id.v_email);
        View v_email1 = bottomSheetDialog_templateList1.findViewById(R.id.v_email1);
        TextView tv_error = bottomSheetDialog_templateList1.findViewById(R.id.tv_error);
        TextView tv_error1 = bottomSheetDialog_templateList1.findViewById(R.id.tv_error1);
        TextView tv_done = bottomSheetDialog_templateList1.findViewById(R.id.tv_done);
        TextView tv_txt = bottomSheetDialog_templateList1.findViewById(R.id.tv_txt);
        tv_txt.setText("Pick contact details for " + contacts.get(position).getFirstname());
        RecyclerView phone_list = bottomSheetDialog_templateList1.findViewById(R.id.phone_list);
        phone_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        PhoneListAdepter emailListAdepter = new PhoneListAdepter(getApplicationContext(), detailList_phone, holder1, contacts, position,
                tv_done);
        phone_list.setAdapter(emailListAdepter);


        RecyclerView email_list = bottomSheetDialog_templateList1.findViewById(R.id.email_list);
        email_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        EmailListAdepter email_listAdepter = new EmailListAdepter(getApplicationContext(), detailList1_email, holder1, contacts, position,
                tv_done);
        email_list.setAdapter(email_listAdepter);


        layout_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        //        Log.e("Size is", String.valueOf(detailList_phone.size()));
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (detailList_phone.size() == 0) {

                    tv_error1.setVisibility(View.VISIBLE);
                    tv_error.setVisibility(View.GONE);
                    tv_error1.setText("seems like you don???t have any phone number ,  \n" +
                            "you can add that in contact section");
                    phone_list.setVisibility(View.GONE);
                } else {
                    phone_list.setVisibility(View.VISIBLE);
                    tv_error1.setVisibility(View.GONE);
                    tv_error1.setText("");
                    tv_error.setVisibility(View.GONE);
                }
                v_phone.setVisibility(View.GONE);
                v_phone1.setVisibility(View.VISIBLE);
                v_email1.setVisibility(View.GONE);
                v_email.setVisibility(View.VISIBLE);

                email_list.setVisibility(View.GONE);
            }
        });

        layout_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                v_phone.setVisibility(View.VISIBLE);
                v_phone1.setVisibility(View.GONE);
                v_email1.setVisibility(View.VISIBLE);
                v_email.setVisibility(View.GONE);
                phone_list.setVisibility(View.GONE);
        //        Log.e("Size is", String.valueOf(detailList1_email.size()));
                if (detailList1_email.size() == 0) {

                    tv_error.setVisibility(View.VISIBLE);
                    tv_error.setText("seems like you don???t have any \n" +
                            "email you can add that in contact section");
                    email_list.setVisibility(View.GONE);
                    tv_error1.setVisibility(View.GONE);
                } else {
                    email_list.setVisibility(View.VISIBLE);
                    tv_error.setVisibility(View.GONE);
                    tv_error.setText("");
                    tv_error1.setVisibility(View.GONE);

                }


            }
        });
        List<ContectListData.Contact.ContactDetail> userLinkedGmailList = new ArrayList<>();
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                List<ContectListData.Contact.ContactDetail> contactDetails = new ArrayList<>();
                contactDetails.clear();
                userLinkedGmailList.clear();

                if (detailList_phone.size() == 0) {

                    if (!tv_error.getText().equals("")) {
                        Global.Messageshow(GroupActivity.this, mMainLayout, tv_error.getText().toString(), false);
                        bottomSheetDialog_templateList1.cancel();

                    } else if (!tv_error1.getText().equals("")) {

                        Global.Messageshow(GroupActivity.this, mMainLayout, tv_error1.getText().toString(), false);
                        bottomSheetDialog_templateList1.cancel();
                    }
                } else {


                    for (int i = 0; i < detailList_phone.size(); i++) {
                        if (detailList_phone.get(i).isPhoneSelect()) {

                            userLinkedGmailList.add(detailList_phone.get(i));
                        }
                    }

                    for (int j = 0; j < detailList1_email.size(); j++) {
                        if (detailList1_email.get(j).isPhoneSelect()) {

                            userLinkedGmailList.add(detailList1_email.get(j));
                        }
                    }
                    contactDetails.addAll(userLinkedGmailList);
              //      Log.e("contactDetails", new Gson().toJson(userLinkedGmailList));
                    contacts.get(position).setContactDetails(contactDetails);
                    select_contectListData.add(contacts.get(position));
                    SessionManager.setGroupList(getApplicationContext(), select_contectListData);
                    topUserListDataAdapter.notifyDataSetChanged();
                    /*
                     * set select contact count */
                    select_Contact(select_contectListData.size());

                    contacts.get(position).setFlag("false");
                    holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                    holder1.add_new_contect_icon.setVisibility(View.GONE);
                    save_button.setTextColor(getResources().getColor(R.color.purple_200));
                    if (Global.IsNotNull(detailList_phone) && detailList_phone.size() != 0) {
                        for (int i = 0; i < detailList_phone.size(); i++) {
                            detailList_phone.get(i).setPhoneSelect(false);
                            if (!detailList_phone.get(i).getId().equals(userLinkedGmailList.get(0).getId())) {
                                contactDetails.add(detailList_phone.get(i));
                            }

                        }
                    }
                    /*
                     * selected number list show
                     * */
                    if (select_contectListData.size() != 0) {
                        layout_list_number_select.setVisibility(View.VISIBLE);
                    } else {
                        layout_list_number_select.setVisibility(View.GONE);
                    }

                    if (Global.IsNotNull(detailList1_email) && detailList1_email.size() != 0) {
                        for (int i = 0; i < detailList1_email.size(); i++) {
                            detailList1_email.get(i).setPhoneSelect(false);
                            for (int j = 0; j < contactDetails.size(); j++) {

                                if (!detailList1_email.get(i).getId().equals(contactDetails.get(j).getId())) {
                                    contactDetails.add(detailList1_email.get(i));
                                } else {
                                    contactDetails.remove(j);
                                }
                            }

                        }
                    }
                    bottomSheetDialog_templateList1.cancel();
                }
            }
        });

        bottomSheetDialog_templateList1.show();
    }

    @Override
    public void onBackPressed() {
        SessionManager.setGroupData(this, new Grouplist.Group());
        super.onBackPressed();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(GroupActivity.this, mMainLayout);
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
            holder.userName.setText(select_contectListData.get(position).getFirstname()+" "+select_contectListData.get(position).getLastname());
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
            if (file.equals("null") || file.equals("")) {
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

                Glide.with(mCtx).
                        load(select_contectListData.get(position).getContactImage())
                        .placeholder(R.drawable.shape_primary_circle)
                        .error(R.drawable.shape_primary_circle)
                        .into(holder.profile_image);

            }

            holder.top_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();


                        for (int i = 0; i < contectListData.size(); i++) {
                            if (contectListData.get(i).getId().equals(select_contectListData.get(position).getId())) {
                                group_flag = "false";
                                groupContectAdapter.notifyItemChanged(i);
                                contectListData.get(i).setFlag("false");

                            }
                        }
                        userDetails.remove(position);
                        topUserListDataAdapter.notifyDataSetChanged();
                        select_Contact(userDetails.size());



                }
            });

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
                    View viewLoading = inflater.inflate(R.layout.item_loading, parent, false);
                    viewHolder = new GroupContectAdapter.LoadingViewHolder(viewLoading);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ContectListData.Contact Contact_data = contacts.get(position);
            //  Log.e("Selcete List Is",new Gson().toJson(select_contectListData));
            switch (getItemViewType(position)) {
                case ITEM:
                    GroupContectAdapter.MovieViewHolder holder1 = (GroupContectAdapter.MovieViewHolder) holder;

                   contacts.get(position).setFlag(group_flag);
                    /*if (contacts.get(position).getFlag().equals("false")) {
                        holder1.add_new_contect_icon.setVisibility(View.GONE);
                        holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                    } else {
                        holder1.remove_contect_icon.setVisibility(View.GONE);
                        holder1.add_new_contect_icon.setVisibility(View.VISIBLE);
                    }*/
                    /*
                     * Contact already block not select */
                  //  Log.e("Group Flag",group_flag);
                  //  Log.e("Flag other",contacts.get(position).getFlag());
                    if (contacts.get(position).getIs_blocked().equals(1)) {
                        holder1.iv_block.setVisibility(View.VISIBLE);
                     /*   holder1.add_new_contect_icon.setVisibility(View.GONE);
                        holder1.remove_contect_icon.setVisibility(View.GONE);*/
                        holder1.userName.setTextColor(context.getResources().getColor(R.color.block_item));
                    } else {

                        holder1.iv_block.setVisibility(View.GONE);
                        holder1.userName.setTextColor(context.getResources().getColor(R.color.unblock_item));
                        if (contacts.get(position).getFlag().equals("false")) {
                          //  Log.e("Same","yes");
                            holder1.add_new_contect_icon.setVisibility(View.GONE);
                            holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                        } else {
                           // Log.e("Same","no");
                            holder1.remove_contect_icon.setVisibility(View.GONE);
                            holder1.add_new_contect_icon.setVisibility(View.VISIBLE);
                        }

                    }
                    holder1.userName.setText(Contact_data.getFirstname() + " " + Contact_data.getLastname());
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
                        holder1.first_latter.setVisibility(View.GONE);
                        holder1.top_layout.setVisibility(View.GONE);
                    } else {
                        current_latter = first_latter;
                        second_latter = first_latter;
                        holder1.first_latter.setVisibility(View.VISIBLE);
                        holder1.top_layout.setVisibility(View.VISIBLE);
                    }


                    if (Contact_data.getContactImage() == null || Contact_data.getContactImage().equals("")) {
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

                    //holder1.add_new_contect_icon.setVisibility(View.VISIBLE);

                    if (SessionManager.getGroupList(getApplicationContext()).size() != 0) {

                        List<ContectListData.Contact> contact1 = SessionManager.getGroupList(getApplicationContext());

                        for (int i = 0; i < contact1.size(); i++) {
                            if (contact1.get(i).getId().equals(contacts.get(position).getId())) {

                                if (holder1.add_new_contect_icon.getVisibility() == View.VISIBLE) {
                                    contacts.get(position).setFlag("false");
                                    holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                                    holder1.add_new_contect_icon.setVisibility(View.GONE);
                                } else {
                                    contacts.get(position).setFlag("true");
                                    holder1.remove_contect_icon.setVisibility(View.GONE);
                                    holder1.add_new_contect_icon.setVisibility(View.VISIBLE);
                                }

                            }
                            else {

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

                    /*
                     * select number */
                    holder1.layout_contact_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                                return;
                            }
                            mLastClickTime = SystemClock.elapsedRealtime();
                            if (contacts.get(position).getFlag().equals("true")) {
                                /*
                                 * this is a select item add in list*/

                                if (contacts.get(position).getIs_blocked().equals(1)) {
                                    Global.Messageshow(getApplicationContext(), mMainLayout, getString(R.string.contect_block), false);
                                } else {
                                    List<ContectListData.Contact.ContactDetail> detailList = new ArrayList<>();
                                    List<ContectListData.Contact.ContactDetail> detailList1 = new ArrayList<>();
                                    detailList.clear();
                                    detailList1.clear();
                                    for (int i = 0; i < contacts.get(position).getContactDetails().size(); i++) {
                                        if (contacts.get(position).getContactDetails().get(i).getType().equals("NUMBER") && !contacts.get(position).getContactDetails().get(i).getEmailNumber().equals("")) {
                                            detailList.add(contacts.get(position).getContactDetails().get(i));
                                        } else if (contacts.get(position).getContactDetails().get(i).getType().equals("EMAIL") && !contacts.get(position).getContactDetails().get(i).getEmailNumber().equals("") && !contacts.get(position).getContactDetails().get(i).getType().equals("NUMBER")) {

                                            detailList1.add(contacts.get(position).getContactDetails().get(i));
                                        }
                                    }
                                    if (detailList.size() == 1 && detailList1.size() == 0) {
                                        holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                                        holder1.add_new_contect_icon.setVisibility(View.GONE);
                                        select_contectListData.add(contacts.get(position));
                                        SessionManager.setGroupList(getApplicationContext(), select_contectListData);
                                        topUserListDataAdapter.notifyDataSetChanged();
                                        /*
                                         * set select contact count */
                                        select_Contact(select_contectListData.size());
                                        contacts.get(position).setFlag("false");
                                        save_button.setTextColor(getResources().getColor(R.color.purple_200));
                                        /*
                                         * selected number list show
                                         * */
                                        if (select_contectListData.size() != 0) {
                                            layout_list_number_select.setVisibility(View.VISIBLE);
                                        } else {
                                            layout_list_number_select.setVisibility(View.GONE);
                                        }
                                    }
                                    else if (detailList.size() == 1 && detailList1.size() == 1) {
                                        holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                                        holder1.add_new_contect_icon.setVisibility(View.GONE);
                                        select_contectListData.add(contacts.get(position));
                                        SessionManager.setGroupList(getApplicationContext(), select_contectListData);
                                        topUserListDataAdapter.notifyDataSetChanged();
                                        /*
                                         * set select contact count */
                                        select_Contact(select_contectListData.size());
                                        contacts.get(position).setFlag("false");
                                        save_button.setTextColor(getResources().getColor(R.color.purple_200));
                                        /*
                                         * selected number list show
                                         * */
                                        if (select_contectListData.size() != 0) {
                                            layout_list_number_select.setVisibility(View.VISIBLE);
                                        } else {
                                            layout_list_number_select.setVisibility(View.GONE);
                                        }
                                    }
                                    else if (detailList1.size() == 1 && detailList.size() == 0) {
                                        holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                                        holder1.add_new_contect_icon.setVisibility(View.GONE);
                                        select_contectListData.add(contacts.get(position));
                                        SessionManager.setGroupList(getApplicationContext(), select_contectListData);
                                        topUserListDataAdapter.notifyDataSetChanged();
                                        /*
                                         * set select contact count */
                                        select_Contact(select_contectListData.size());
                                        contacts.get(position).setFlag("false");
                                        save_button.setTextColor(getResources().getColor(R.color.purple_200));
                                        /*
                                         * selected number list show
                                         * */
                                        if (select_contectListData.size() != 0) {
                                            layout_list_number_select.setVisibility(View.VISIBLE);
                                        } else {
                                            layout_list_number_select.setVisibility(View.GONE);
                                        }
                                    } else if (detailList.size() >= 0) {
                                        for (int i = 0; i < detailList.size(); i++) {
                                            if (i == 0) {
                                                detailList.get(i).setPhoneSelect(true);
                                                break;
                                            }
                                        }
                                        for (int i = 0; i < detailList1.size(); i++) {
                                            if (i == 0) {
                                                detailList1.get(i).setPhoneSelect(true);
                                                break;
                                            }
                                        }
                                        Phone_bouttomSheet(detailList, holder1, contacts, position, detailList1);
                                    } else if (detailList1.size() >= 1) {
                                        for (int i = 0; i < detailList.size(); i++) {
                                            if (i == 0) {
                                                detailList.get(i).setPhoneSelect(true);
                                                break;
                                            }
                                        }
                                        for (int i = 0; i < detailList1.size(); i++) {
                                            if (i == 0) {
                                                detailList1.get(i).setPhoneSelect(true);
                                                break;
                                            }
                                        }
                                        Phone_bouttomSheet(detailList, holder1, contacts, position, detailList1);
                                    }


                                }
                            } else {
                                /*
                                 * this is a select item remove */

                                holder1.remove_contect_icon.setVisibility(View.GONE);
                                holder1.add_new_contect_icon.setVisibility(View.VISIBLE);
                                topUserListDataAdapter.remove_item(position, contacts.get(position).getId());

                                topUserListDataAdapter.notifyDataSetChanged();
                               // Log.e("Size is", new Gson().toJson(select_contectListData));
                                SessionManager.setGroupList(getApplicationContext(), select_contectListData);
                                /*
                                 * set select contact count */
                                select_Contact(select_contectListData.size());
                                contacts.get(position).setFlag("true");
                                if (select_contectListData.size() == 0) {
                                    save_button.setTextColor(getResources().getColor(R.color.black));
                                } else {
                                    save_button.setTextColor(getResources().getColor(R.color.purple_200));
                                }
                                /*
                                 * selected number list show
                                 * */
                                if (select_contectListData.size() != 0) {
                                    layout_list_number_select.setVisibility(View.VISIBLE);
                                } else {
                                    layout_list_number_select.setVisibility(View.GONE);
                                }

                            }

                        }
                    });

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

        public void clear() {
            contacts.clear();
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

        public void addAll_item(List<ContectListData.Contact> contectListData) {
            select_contectListData.clear();
            contacts.clear();

            int i = 0;
            for(ContectListData.Contact song : contectListData) {
                if (contectListData.get(i).getIs_blocked().equals(1)) {
                    group_flag = "true";
                    contectListData.get(i).setFlag("true");

                } else {
                    groupContectAdapter = new GroupContectAdapter(getApplicationContext());
                    contect_list_unselect.setAdapter(groupContectAdapter);
                    group_flag = "false";
                    contectListData.get(i).setFlag("false");
                    groupContectAdapter.addAll(contectListData);
                    groupContectAdapter.notifyDataSetChanged();
                    select_contectListData.add(contectListData.get(i));
                    add_contect_list.setItemViewCacheSize(5000);
                    topUserListDataAdapter.notifyDataSetChanged();
                    save_button.setTextColor(getResources().getColor(R.color.purple_200));

                }
                i++;
            }


          /*  for (int i = 0; i < contectListData.size(); i++) {


                    if (contectListData.get(i).getIs_blocked().equals(1)) {
                        group_flag = "true";
                        contectListData.get(i).setFlag("true");

                    } else {
                        groupContectAdapter = new GroupContectAdapter(getApplicationContext());
                        contect_list_unselect.setAdapter(groupContectAdapter);
                        group_flag = "false";
                        contectListData.get(i).setFlag("false");
                        groupContectAdapter.addAll(contectListData);
                        groupContectAdapter.notifyDataSetChanged();
                        select_contectListData.add(contectListData.get(i));
                        add_contect_list.setItemViewCacheSize(5000);
                        topUserListDataAdapter.notifyDataSetChanged();
                        save_button.setTextColor(getResources().getColor(R.color.purple_200));

                    }



            }*/
          /*  contect_list_unselect.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                  loadingDialog.cancelLoading();
                  contect_list_unselect.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });*/

            contacts.clear();
            contacts.addAll(contectListData);
            notifyDataSetChanged();
            /*
             * selected number list show
             * */
            if (select_contectListData.size() != 0) {
                layout_list_number_select.setVisibility(View.VISIBLE);
            } else {
                layout_list_number_select.setVisibility(View.GONE);
            }

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
            LinearLayout top_layout, layout_contec, layout_contact_item;
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
                layout_contact_item = itemView.findViewById(R.id.layout_contact_item);
                remove_contect_icon = itemView.findViewById(R.id.remove_contect_icon);
                add_new_contect_icon.setVisibility(View.VISIBLE);
                layout_contec = itemView.findViewById(R.id.layout_contec);
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
            this.holder1 = holder1;
            this.contacts = contacts;
            this.s_position = s_position;
            this.tv_done = tv_done;
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
            holder.tv_item.setText("Phone(" + userLinkedGmailList.get(position).getLabel() + ")");
            holder.tv_phone.setText(userLinkedGmailList.get(position).getEmailNumber());
            holder.tv_phone.setVisibility(View.VISIBLE);
            if (userLinkedGmailList.get(position).isPhoneSelect()) {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
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
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    for (int i = 0; i < userLinkedGmailList.size(); i++) {
                        if (userLinkedGmailList.get(i).isPhoneSelect()) {
                            userLinkedGmailList.get(i).setPhoneSelect(false);
                            break;
                        }
                    }
                    userLinkedGmailList.get(position).setPhoneSelect(true);
                    /*holder1.remove_contect_icon.setVisibility(View.VISIBLE);
                    holder1.add_new_contect_icon.setVisibility(View.GONE);*/

                   /* tv_done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                        }
                    });*/

                    holder.iv_selected.setVisibility(View.VISIBLE);
                    holder.iv_unselected.setVisibility(View.GONE);
                    notifyItemChanged(position);
                    userLinkedGmailList.get(position).setFlag("false");
                    notifyDataSetChanged();
                }
            });


        }

        public void removeall() {
            userLinkedGmailList.clear();
            notifyDataSetChanged();

        }

        @Override
        public int getItemCount() {
            return userLinkedGmailList.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item, tv_phone;
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
                tv_phone = view.findViewById(R.id.tv_phone);
            }
        }
    }

    class EmailListAdepter extends RecyclerView.Adapter<EmailListAdepter.viewholder> {

        public Context mCtx;
        List<ContectListData.Contact.ContactDetail> userLinkedGmailList1;

        GroupContectAdapter.MovieViewHolder holder1;

        List<ContectListData.Contact> contacts;
        int s_position;
        TextView tv_done;


        public EmailListAdepter(Context applicationContext, List<ContectListData.Contact.ContactDetail> userLinkedGmailList1, GroupContectAdapter.MovieViewHolder holder1, List<ContectListData.Contact> contacts, int s_position, TextView tv_done) {
            this.mCtx = applicationContext;
            this.userLinkedGmailList1 = userLinkedGmailList1;
            this.holder1 = holder1;
            this.contacts = contacts;
            this.s_position = s_position;
            this.tv_done = tv_done;
        }

        @NonNull
        @Override
        public EmailListAdepter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.email_select_layout, parent, false);
            return new EmailListAdepter.viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EmailListAdepter.viewholder holder, int position) {

            userLinkedGmailList1.get(position).setFlag("false");
            holder.tv_item.setText("Email(" + userLinkedGmailList1.get(position).getLabel() + ")");
            holder.tv_phone.setText(userLinkedGmailList1.get(position).getEmailNumber());
            holder.tv_phone.setVisibility(View.VISIBLE);
            if (userLinkedGmailList1.get(position).isPhoneSelect()) {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.iv_unselected.setVisibility(View.GONE);
            } else {
                holder.iv_selected.setVisibility(View.GONE);
                holder.iv_unselected.setVisibility(View.VISIBLE);
            }

            if (userLinkedGmailList1.get(position).getIsDefault().toString().equals("1")) {
                holder.iv_dufult.setVisibility(View.VISIBLE);
            } else {
                holder.iv_dufult.setVisibility(View.GONE);
            }


            holder.layout_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    for (int i = 0; i < userLinkedGmailList1.size(); i++) {
                        if (userLinkedGmailList1.get(i).isPhoneSelect()) {
                            userLinkedGmailList1.get(i).setPhoneSelect(false);
                            break;
                        }
                    }
                    userLinkedGmailList1.get(position).setPhoneSelect(true);
                    holder.iv_selected.setVisibility(View.VISIBLE);
                    holder.iv_unselected.setVisibility(View.GONE);
                    notifyItemChanged(position);
                    userLinkedGmailList1.get(position).setFlag("false");
                    notifyDataSetChanged();
                }
            });


        }

        @Override
        public int getItemCount() {
            return userLinkedGmailList1.size();
        }

        public class viewholder extends RecyclerView.ViewHolder {
            TextView tv_item, tv_phone;
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
                tv_phone = view.findViewById(R.id.tv_phone);
            }
        }
    }
}