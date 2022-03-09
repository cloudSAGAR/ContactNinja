package com.contactninja.Fragment.AddContect_Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Add_Newcontect_Activity;
import com.contactninja.Contect.Contact;
import com.contactninja.Contect.ContactFetcher;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Csv_InviteListData;
import com.contactninja.Model.InviteListData;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UservalidateModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitApiClient;
import com.contactninja.retrofit.RetrofitApiInterface;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged")
public class ContectFragment extends Fragment {
    private final static String[] DATA_COLS = {

            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.DATA1,//phone number
            ContactsContract.Data.CONTACT_ID
    };
    ArrayList<Contact> listContacts;
    List<ContectListData.Contact> main_store = new ArrayList<>();
    public static ArrayList<InviteListData> inviteListData = new ArrayList<>();
    ConstraintLayout mMainLayout;
    Context mCtx;
    RecyclerView rvinviteuserdetails;
    String strtext = "";
    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    TextView add_new_contect, num_count,txt_nolist;
    ImageView add_new_contect_icon, iv_filter_icon,iv_cancle_search_icon;
    View view1;
    FragmentActivity fragmentActivity;
    LinearLayout add_new_contect_layout,layout_list_data,lay_no_list;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    int limit = 0, totale_group;
    ContectListAdapter paginationAdapter;
    int currentPage = 1;
    boolean isLoading = false;
    boolean isLastPage = false;
    LinearLayoutManager layoutManager;
    SwipeRefreshLayout swipeToRefresh;
    EditText ev_search;
    private List<ContectListData.Contact> contectListData;
    private List<ContectListData.Contact> main_contectListData=new ArrayList<>();

    String Filter = "";//BLOCK / ALL

    String userName = "", user_phone_number = "", user_image = "", user_des = "", old_latter = "", contect_type = "", contect_email = "",
            contect_type_work = "", email_type_home = "", email_type_work = "", country = "", city = "", region = "", street = "",
            postcode = "", postType = "", note = "";
    StringBuilder data;
    Cursor cursor;
    List<Csv_InviteListData> csv_inviteListData = new ArrayList<>();
    List<Csv_InviteListData> csv_multiple_data = new ArrayList<>();
    private long mLastClickTime = 0;

    public ContectFragment(View view, FragmentActivity activity) {

        this.view1 = view;
        this.fragmentActivity = activity;
    }


    TextView tv_upload;
    String fillter_text="";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View content_view = inflater.inflate(R.layout.fragment_contect, container, false);
        IntentUI(content_view);
        setAllData();
        return content_view;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void setAllData()
    {
        mCtx = getContext();
        sessionManager = new SessionManager(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        rvinviteuserdetails.setLayoutManager(layoutManager);
        rvinviteuserdetails.setHasFixedSize(true);
        contectListData = new ArrayList<>();
        SessionManager.setOneCotect_deatil(getActivity(), new ContectListData.Contact());

        paginationAdapter = new ContectListAdapter(getActivity());
        rvinviteuserdetails.setAdapter(paginationAdapter);


        //inviteListData.clear();


        //Faste View Code
        fastscroller_thumb.setupWithFastScroller(fastscroller);
        fastscroller.setUseDefaultScroller(true);

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

        if (SessionManager.getContectList(getActivity()).size() != 0) {
            contectListData.addAll(SessionManager.getContectList(getActivity()).get(0).getContacts());
            // main_contectListData=contectListData.subList(0,50);
            paginationAdapter.addAll(main_contectListData);
            num_count.setText(contectListData.size() + " Contacts");
            onScrolledToBottom();
        }

      /*  rvinviteuserdetails.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1))
                       onScrolledToBottom();

            }
        });*/
        // EnableRuntimePermission();

        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout_list_data.setVisibility(View.VISIBLE);
                lay_no_list.setVisibility(View.GONE);
                ev_search.setText("");
                iv_filter_icon.setVisibility(View.VISIBLE);
                iv_filter_icon.setImageResource(R.drawable.ic_filter);
                iv_cancle_search_icon.setVisibility(View.GONE);
                try {
                    try {
                        //  GetContactsIntoArrayList();
                        if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                            fillter_text="";
                            ContectEvent();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        add_new_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent addnewcontect = new Intent(getActivity(), Add_Newcontect_Activity.class);
                SessionManager.setContect_flag("save");
                startActivity(addnewcontect);
            }
        });
        add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Intent addnewcontect = new Intent(getActivity(), Add_Newcontect_Activity.class);
                SessionManager.setContect_flag("save");
                startActivity(addnewcontect);
            }
        });
        add_new_contect_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                SessionManager.setAdd_Contect_Detail(getActivity(), new AddcontectModel());
                SessionManager.setContect_flag("save");
                Intent addnewcontect = new Intent(getActivity(), Add_Newcontect_Activity.class);
                startActivity(addnewcontect);

            }
        });
        tv_upload.setVisibility(View.VISIBLE);
        tv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                    loadingDialog.showLoadingDialog();
                EnableRuntimePermission();
                // splitdata(csv_inviteListData);
            }
        });
        ev_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Global.hideKeyboard(getActivity());
                    iv_cancle_search_icon.setVisibility(View.VISIBLE);
                    iv_filter_icon.setVisibility(View.GONE);
                    fillter_text=ev_search.getText().toString().trim();
                    try {
                        loadingDialog.showLoadingDialog();
                        ContectEvent();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        fastscroller.setupWithRecyclerView(
                rvinviteuserdetails,
                (position) -> {
                    try {
                        FastScrollItemIndicator fastScrollItemIndicator = new FastScrollItemIndicator.Text(
                                paginationAdapter.getItem(position).getFirstname().substring(0, 1)
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

    private void onScrolledToBottom() {

        if (paginationAdapter.getItemCount() < contectListData.size()) {
            int x, y;
            if ((contectListData.size() - paginationAdapter.getItemCount()) >= contectListData.size()) {
                x = paginationAdapter.getItemCount();
                y = x + contectListData.size();
            } else {
                x = paginationAdapter.getItemCount();
                y = x + contectListData.size() - paginationAdapter.getItemCount();
            }
          /*  for (int i = x; i < y; i++) {
               // main_contectListData.add(contectListData.get(i));
                paginationAdapter.add(contectListData.get(i));
            }*/
            paginationAdapter.addAll(contectListData.subList(x,y));
            //paginationAdapter.notifyDataSetChanged();
        }

    }
    //Update Contect Number Direct
    public static boolean updateNameAndNumber(final Context context, String number, String newName, String newNumber) {

        if (context == null || number == null || number.trim().isEmpty()) return false;

        if (newNumber != null && newNumber.trim().isEmpty()) newNumber = null;

        if (newNumber == null) return false;


        String contactId = getContactId(context, number);

        if (contactId == null) return false;

        //selection for name
        String where = String.format(
                "%s = '%s' AND %s = ?",
                DATA_COLS[0], //mimetype
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                DATA_COLS[2]/*contactId*/);

        String[] args = {contactId};

        ArrayList<ContentProviderOperation> operations = new ArrayList<>();

        operations.add(
                ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, args)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, newName)
                        .build()
        );

        //change selection for number
        where = String.format(
                "%s = '%s' AND %s = ?",
                DATA_COLS[0],//mimetype
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                DATA_COLS[1]/*number*/);

        //change args for number
        args[0] = number;

        operations.add(
                ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, args)
                        .withValue(DATA_COLS[1]/*number*/, newNumber)
                        .build()
        );

        try {

            ContentProviderResult[] results = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);

            for (ContentProviderResult result : results) {
             //   Log.e("Upadte Contect", result.toString());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getContactId(Context context, String number) {

        if (context == null) return null;

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.NUMBER + "=?",
                new String[]{number},
                null
        );

        if (cursor == null || cursor.getCount() == 0) return null;

        cursor.moveToFirst();

        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

        cursor.close();
        return id;
    }
/*
    private void filter1(String text) {
        // creating a new array list to filter our data.
        ArrayList<ContectListData.Contact> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (ContectListData.Contact item : contectListData) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getFirstname().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {

        } else {
            paginationAdapter.filterList(filteredlist);
        }
    }
*/

    private void splitdata(ArrayList<Contact> response) {
        data = new StringBuilder();
        data.append("Firstname" +
                "," + "Lastname" +
                "," + "Company Name" +
                "," + "Company URL" +
                "," + "Job Title" + "," + "Notes" + "," +
                "DOB" + "," + "Address" + "," +
                "City" + "," + "State" + "," +
                "Zipcode" + "," + "Zoomid" + "," +
                "Facebook Link" + "," + "Twitter Link" + "," +
                "Breakout Link" + "," + "Linkedin Link" + "," +
                "Email" + "," + "Phone" + "," +
                "Fax");

        for (int i = 0; i < response.size(); i++) {
            if(Global.IsNotNull(response.get(i).name)&& !response.get(i).name.equals("null") &&
                    Global.IsNotNull(response.get(i).numbers)&& !response.get(i).numbers.equals("null")) {
                String email = "";
                String number = "";
                for (int j = 0; j < response.get(i).emails.size(); j++) {

                    if (email.equals("")) {
                        email = response.get(i).emails.get(j).address;
                    } else {
                        email = email + "," + response.get(i).emails.get(j).address;
                    }

                }

                for (int j = 0; j < response.get(i).numbers.size(); j++) {

                    if (number.equals("")) {
                        number = response.get(i).numbers.get(j).number;
                    } else {
                        number = number + "," + response.get(i).numbers.get(j).number;
                    }

                }

                data.append('\n' + response.get(i).name.replaceAll("[-+.^:,]","") +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + ' ' +
                        ',' + '"' + email + ',' + '"' +
                        ',' + '"' + number + ',' + '"' +
                        ',' + ' '
                );

            }
        }
        // Log.e("Data Is", String.valueOf(data));
        CreateCSV(data);
    }

    private void CreateCSV(StringBuilder data) {
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        try {
            //
            FileOutputStream out = getActivity().openFileOutput("CSV_Data_" + time + ".csv", Context.MODE_PRIVATE);

            //store the data in CSV file by passing String Builder data
            out.write(data.toString().getBytes());
            out.close();
            Context context = getActivity();
            final File newFile = new File(Environment.getExternalStorageDirectory(), "SimpleCVS");
            if (!newFile.exists()) {
                newFile.mkdir();
            }
            File file = new File(context.getFilesDir(), "CSV_Data_" + time + ".csv");
            Uri path = FileProvider.getUriForFile(context, "com.contactninja", file);
            //once the file is ready a share option will pop up using which you can share
            // the same CSV from via Gmail or store in Google Drive


            if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                Uploadcsv(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void Uploadcsv(File path) throws JSONException {
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("csv"),
                        path
                );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("import_file", path.getName(), requestFile);

        RequestBody user_id1 = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody organization_id1 = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody team_id1 = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody imei = RequestBody.create(MediaType.parse("text/plain"), Global.imei);

        retrofitCalls.Upload_csv(sessionManager, loadingDialog, Global.getToken(sessionManager),
                organization_id1, team_id1, user_id1, id, body, Global.getVersionname(getActivity()), Global.Device, imei, new RetrofitCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void success(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                        sessionManager.setcontectexits("1");
                        if (response.body().getHttp_status() == 200) {

                            SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
                            user_data.getUser().setIs_contact_exist(1);
                            SessionManager.setUserdata(getActivity(), user_data);

                            loadingDialog.cancelLoading();
                            try {
                                ContectEvent1(main_store);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loadingDialog.cancelLoading();
                        }
                        sessionManager.setCsv_token();


                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }

                });


    }


    public void EnableRuntimePermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPermissionGranted() {
                listContacts = new ContactFetcher(getActivity()).fetchAll();

                SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
                String Is_contact_exist = String.valueOf(user_data.getUser().getIs_contact_exist());

                if (listContacts.size() == 0) {
                    try {
                        fillter_text="";
                        ContectEvent();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    limit = listContacts.size();
                    splitdata(listContacts);

                }


            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //  EnableRuntimePermission();
            }

        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedTitle("Contactninja would like to access your contacts")
                .setDeniedMessage("Contact Ninja uses your contacts to improve your business’s marketing outreach by aggrregating your contacts.")
                .setGotoSettingButtonText("setting")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.SEND_SMS
                )
                .setRationaleConfirmText("OK")
                .check();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)


    private void ContectEvent1(List<ContectListData.Contact> main_store) throws JSONException {


        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("page", "1");
        paramObject.addProperty("perPage", 0);
        paramObject.addProperty("status", "A");
        paramObject.addProperty("q", "");
        paramObject.addProperty("orderBy", "firstname");
        paramObject.addProperty("order", "asc");
        obj.add("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        RetrofitApiInterface registerinfo = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<ApiResponse> call = registerinfo.Contect_List(RetrofitApiClient.API_Header, token, obj, Global.getVersionname(getActivity()),
                Global.Device);
        call.enqueue(new Callback<ApiResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                //  loadingDialog.cancelLoading();
                swipeToRefresh.setRefreshing(false);
                if (response.body().getHttp_status() == 200) {

                    SessionManager.setContectList(getActivity(), new ArrayList<>());
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ContectListData>() {
                    }.getType();
                    ContectListData contectListData1 = new Gson().fromJson(headerString, listType);
                    contectListData.addAll(contectListData1.getContacts());
                    num_count.setText("" + contectListData1.getTotal() + " Contacts");

                    totale_group = contectListData1.getTotal();

                    List<ContectListData> contectListData_store = new ArrayList<>();
                    contectListData_store.add(contectListData1);
                    SessionManager.setContectList(getActivity(), contectListData_store);
                    List<ContectListData.Contact> contacts = new ArrayList<>();
                    contacts.addAll(contectListData1.getContacts());
                    compareLists(contacts, main_store);
                } else {
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                //loadingDialog.cancelLoading();
                swipeToRefresh.setRefreshing(false);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void compareLists(List<ContectListData.Contact> contacts, List<ContectListData.Contact> main_store) {
        for (int i = 0; i < contacts.size(); i++) {

            for (int j = 0; j < main_store.size(); j++) {

                if (contacts.get(i).getFirstname().toString().equals(main_store.get(j).getFirstname().toString())) {
                    main_store.remove(j);
                    main_store.add(j, contacts.get(i));
                    break;
                } else if (main_store.size() == j + 1) {
                    main_store.add(main_store.size(), contacts.get(i));
                    break;
                }

            }
        }

        Collections.sort(main_store, new Comparator<ContectListData.Contact>() {
            @Override
            public int compare(ContectListData.Contact s1, ContectListData.Contact s2) {
                return s1.getFirstname().compareToIgnoreCase(s2.getFirstname());
            }
        });


        loadingDialog.cancelLoading();
        paginationAdapter.removeloist();
        rvinviteuserdetails.setItemViewCacheSize(50000);
        paginationAdapter.addAll(main_store);
        paginationAdapter.notifyDataSetChanged();
    }


    void filter(String text, View view, FragmentActivity activity) {
        if (!text.equals("")) {
            List<ContectListData.Contact> temp = new ArrayList();
            for (ContectListData.Contact d : contectListData) {
                if (d.getFirstname().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);

                    contectListData.addAll(temp);
                    paginationAdapter.notifyDataSetChanged();

                }
            }

        }

    }

    private void IntentUI(View content_view) {
        iv_filter_icon = content_view.findViewById(R.id.iv_filter_icon);
        txt_nolist = content_view.findViewById(R.id.txt_nolist);
        layout_list_data = content_view.findViewById(R.id.layout_list_data);
        lay_no_list = content_view.findViewById(R.id.lay_no_list);
        mMainLayout = content_view.findViewById(R.id.mMainLayout);
        rvinviteuserdetails = content_view.findViewById(R.id.contact_list);
        fastscroller = content_view.findViewById(R.id.fastscroller);
        fastscroller_thumb = content_view.findViewById(R.id.fastscroller_thumb);
        add_new_contect = content_view.findViewById(R.id.add_new_contect);
        num_count = content_view.findViewById(R.id.num_count);
        add_new_contect_icon = content_view.findViewById(R.id.add_new_contect_icon);
        add_new_contect_layout = content_view.findViewById(R.id.add_new_contect_layout);
        swipeToRefresh = content_view.findViewById(R.id.swipeToRefresh);
        ev_search = content_view.findViewById(R.id.ev_search);
        iv_cancle_search_icon = content_view.findViewById(R.id.iv_cancle_search_icon);
        tv_upload = content_view.findViewById(R.id.tv_upload);
        iv_filter_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                showBottomSheetDialog_Filtter();
            }
        });
        iv_cancle_search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ev_search.setText("");
                iv_cancle_search_icon.setVisibility(View.GONE);
                iv_filter_icon.setVisibility(View.VISIBLE);
                try {
                    loadingDialog.showLoadingDialog();
                    fillter_text="";
                    ContectEvent();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                layout_list_data.setVisibility(View.VISIBLE);
                lay_no_list.setVisibility(View.GONE);
            }
        });
    }


    public void update(String strtext1, View view, FragmentActivity activity) {
        filter(strtext1, view, activity);

    }


    private void ContectEvent() throws JSONException {

        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_data.getUser().getId());
        paramObject.addProperty("page", currentPage);
        paramObject.addProperty("perPage", 0);
        paramObject.addProperty("status", "A");
        paramObject.addProperty("q", fillter_text);
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


                if (response.body().getHttp_status() == 200) {
                    try {
                        if (swipeToRefresh.isRefreshing()) {
                            contectListData.clear();
                            paginationAdapter.removeloist();
                            paginationAdapter.notifyDataSetChanged();
                            rvinviteuserdetails.setItemViewCacheSize(50000);
                            paginationAdapter = new ContectListAdapter(getActivity());
                            rvinviteuserdetails.setAdapter(paginationAdapter);

                            sessionManager.setContectList(getActivity(), new ArrayList<>());

                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            Type listType = new TypeToken<ContectListData>() {
                            }.getType();
                            ContectListData contectListData1 = new Gson().fromJson(headerString, listType);
                            contectListData.addAll(contectListData1.getContacts());
                           // paginationAdapter.addAll(contectListData);
                            //paginationAdapter.notifyDataSetChanged();
                            List<ContectListData> contectListData_store = new ArrayList<>();
                            contectListData_store.add(contectListData1);
                            sessionManager.setContectList(getActivity(), contectListData_store);
                            num_count.setText("" + contectListData1.getTotal() + " Contacts");
                            totale_group = contectListData1.getTotal();
                            contectListData.addAll(contectListData_store.get(0).getContacts());
                            onScrolledToBottom();
                        } else{
                            contectListData.clear();
                            paginationAdapter.removeloist();
                            sessionManager.setContectList(getActivity(), new ArrayList<>());
                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            Type listType = new TypeToken<ContectListData>() {
                            }.getType();
                            ContectListData contectListData1 = new Gson().fromJson(headerString, listType);
                            contectListData.addAll(contectListData1.getContacts());
                            List<ContectListData> contectListData_store = new ArrayList<>();
                            contectListData_store.add(contectListData1);
                            sessionManager.setContectList(getActivity(), contectListData_store);
                            //contectListData.addAll(contectListData_store.get(0).getContacts());
                            rvinviteuserdetails.setItemViewCacheSize(50000);
                            paginationAdapter = new ContectListAdapter(getActivity());
                            rvinviteuserdetails.setAdapter(paginationAdapter);

                            onScrolledToBottom();

                        }
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                }
                loadingDialog.cancelLoading();
                swipeToRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                swipeToRefresh.setRefreshing(false);
                loadingDialog.cancelLoading();

            }
        });


    }

    /* private void ContectEventnext() throws JSONException {


         SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
         String token = Global.getToken(sessionManager);
         JsonObject obj = new JsonObject();
         JsonObject paramObject = new JsonObject();
         paramObject.addProperty("organization_id", 1);
         paramObject.addProperty("team_id", 1);
         paramObject.addProperty("user_id", user_data.getUser().getId());
         paramObject.addProperty("page", currentPage);
         paramObject.addProperty("perPage", 0);
         paramObject.addProperty("status", "");
         paramObject.addProperty("q", "");
         paramObject.addProperty("orderBy", "firstname");
         paramObject.addProperty("order", "asc");
         obj.add("data", paramObject);


         RetrofitApiInterface registerinfo = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
         Call<ApiResponse> call = registerinfo.Contect_List(RetrofitApiClient.API_Header, token, obj, Global.getVersionname(getActivity()), Global.Device);
         call.enqueue(new Callback<ApiResponse>() {
             @Override
             public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                 paginationAdapter.removeLoadingFooter();
                 if (response.body().getHttp_status() == 200) {


                     Gson gson = new Gson();
                     String headerString = gson.toJson(response.body().getData());
                     Type listType = new TypeToken<ContectListData>() {
                     }.getType();
                     paginationAdapter.removeLoadingFooter();
                     ContectListData group_model = new Gson().fromJson(headerString, listType);
                     contectListData.clear();
                     contectListData.addAll(group_model.getContacts());
                     paginationAdapter.addAll(contectListData);

                     if (group_model.getContacts().size() == limit) {
                         if (currentPage != TOTAL_PAGES) paginationAdapter.addLoadingFooter();
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

             }
         });


     }
 */
    @Override
    public void onResume() {
        super.onResume();
        ev_search.setText("");
        Filter = "";
        SessionManager.setAdd_Contect_Detail(getActivity(), new AddcontectModel());
        SessionManager.setOneCotect_deatil(getActivity(), new ContectListData.Contact());
        if(SessionManager.getContect_edit(getActivity())){
            loadingDialog.showLoadingDialog();
            SessionManager.setContect_edit(false);
        }
        try {
            if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                fillter_text="";
                ContectEvent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

/*
@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak")
public class ContectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LOADING = 0;
    private static final int ITEM = 1;
    Context context;
    String second_latter = "";
    String current_latter = "", image_url = "";
    private List<ContectListData.Contact> contacts;
    private boolean isLoadingAdded = false;

    public void filterList(ArrayList<ContectListData.Contact> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
       contacts = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    public ContectListAdapter(@SuppressLint("UnknownNullness") Context context) {
        this.context = context;
        contacts = new LinkedList<>();
    }

    public void setContactList(@SuppressLint("UnknownNullness") List<ContectListData.Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.invite_user_details1, parent, false);
                viewHolder = new MovieViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_loading, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);

                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ContectListData.Contact Contact_data = contacts.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                ContectListAdapter.MovieViewHolder holder1 = (ContectListAdapter.MovieViewHolder) holder;
              try {
                if (Contact_data.getState().equals("I"))
                  {
                      holder1.add_new_contect_icon.setVisibility(View.GONE);
                  }
                  else {
                      holder1.add_new_contect_icon.setVisibility(View.GONE);
                  }

                  if (Contact_data.getLastname().equals("null"))
                  {
                      holder1.userName.setText(Contact_data.getFirstname());
                  }
                else {
                      holder1.userName.setText(Contact_data.getFirstname()+" "+Contact_data.getLastname().toString());

                  }
                  holder1.userNumber.setVisibility(View.GONE);


                  holder1.first_latter.setVisibility(View.GONE);
                  holder1.top_layout.setVisibility(View.GONE);
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
                      String name = Contact_data.getFirstname();
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

                    */
/*  CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context.getApplicationContext());
                      circularProgressDrawable.setStrokeWidth(6f);
                      circularProgressDrawable.setCenterRadius(20f);
                      circularProgressDrawable.setColorSchemeColors(context.getResources().getColor(R.color.purple_200),context.getResources().getColor(R.color.purple_200),context.getResources().getColor(R.color.purple_200));
                      circularProgressDrawable.start();*//*

                      Glide.with(context).
                              load(Contact_data.getContactImage())
                              .placeholder(R.drawable.shape_primary_circle)
                              .error(R.drawable.shape_primary_circle)
                              .into(holder1.profile_image);
                      holder1.no_image.setVisibility(View.GONE);
                      holder1.profile_image.setVisibility(View.VISIBLE);
                  }


                  holder1.main_layout.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          if (Contact_data.getState().equals("I"))
                          {
                              holder1.add_new_contect_icon.setVisibility(View.VISIBLE);

                              try {
                                  AddContect_Api(Contact_data);
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }
                          else {
                              SessionManager.setAdd_Contect_Detail(context, new AddcontectModel());
                              SessionManager.setOneCotect_deatil(context, Contact_data);
                              Intent addnewcontect = new Intent(context, Addnewcontect_Activity.class);
                              SessionManager.setContect_flag("read");
                              context.startActivity(addnewcontect);
                          }


                      }
                  });
              }
              catch (Exception e)
              {
                  contacts.remove(position);
           */
    /*       notifyDataSetChanged();*//*


              }


                break;

            case LOADING:
                ContectListAdapter.LoadingViewHolder loadingViewHolder = (ContectListAdapter.LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
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
    public void updateList(List<ContectListData.Contact> list) {
        contacts = list;
        notifyDataSetChanged();
    }

    public void removeloist() {
        contacts.clear();
        notifyDataSetChanged();
    }

    public ContectListData.Contact getItem(int position) {
        return contacts.get(position);
    }


    public  class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView no_image;
        TextView userName, userNumber, first_latter;
        CircleImageView profile_image;
        LinearLayout top_layout;
        RelativeLayout main_layout;
        ImageView add_new_contect_icon;


        public MovieViewHolder(View itemView) {
            super(itemView);
            first_latter = itemView.findViewById(R.id.first_latter);
            userName = itemView.findViewById(R.id.username);
            userNumber = itemView.findViewById(R.id.user_number);
            profile_image = itemView.findViewById(R.id.profile_image);
            no_image = itemView.findViewById(R.id.no_image);
            top_layout = itemView.findViewById(R.id.top_layout);
            main_layout = itemView.findViewById(R.id.main_layout);
            add_new_contect_icon=itemView.findViewById(R.id.add_new_contect_icon);
        }
    }

    public  class LoadingViewHolder extends RecyclerView.ViewHolder {

        private final ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.idPBLoading);

        }
    }

}

*/


    public void AddContect_Api(ContectListData.Contact contact_data) throws JSONException {

        loadingDialog.showLoadingDialog();


        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());

        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();

        //Other Company Add

        paramObject.put("company_name", "");
        paramObject.put("company_id", "");

        paramObject.put("address", "");
        paramObject.put("breakout_link", "");
        paramObject.put("city", "");
        paramObject.put("oldImage", "");

        paramObject.put("company_url", "");
        paramObject.put("dob", "");
        paramObject.put("dynamic_fields_value", "");
        paramObject.put("facebook_link", "");
        paramObject.put("firstname", contact_data.getFirstname());
        paramObject.put("lastname", contact_data.getLastname());
        paramObject.put("job_title", "");
        paramObject.put("lastname", contact_data.getLastname());
        paramObject.put("linkedin_link", "");
        paramObject.put("organization_id", 1);
        paramObject.put("state", "");
        paramObject.put("team_id", 1);
        // addcontectModel.getTime()
        paramObject.put("timezone_id", "");
        paramObject.put("twitter_link", "");
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("zipcode", "");
        paramObject.put("zoom_id", "");
        paramObject.put("contact_image", "");
        paramObject.put("image_extension", "");
        paramObject.put("contact_image_name", "");

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < contact_data.getContactDetails().size(); i++) {
            JSONObject paramObject1 = new JSONObject();
            if (contact_data.getContactDetails().get(i).getEmailNumber().equals("")) {
            } else {

                paramObject1.put("email_number", contact_data.getContactDetails().get(i).getEmailNumber());
                paramObject1.put("id", contact_data.getContactDetails().get(i).getId());
                paramObject1.put("is_default", contact_data.getContactDetails().get(i).getIsDefault());
                paramObject1.put("label", contact_data.getContactDetails().get(i).getLabel());
                paramObject1.put("type", contact_data.getContactDetails().get(i).getType());
            }
            jsonArray.put(paramObject1);
        }

        paramObject.put("imei", Global.imei);
        paramObject.put("contact_detail", jsonArray);


        obj.put("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());

        retrofitCalls.Addcontect(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {

                } else {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    UservalidateModel uservalidateModel = new Gson().fromJson(headerString, listType);
                    try {
                        if (uservalidateModel.getFirstname().size() != 0) {
                            Global.Messageshow(getActivity(), mMainLayout, uservalidateModel.getFirstname().get(0).toString(), false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });

    }


    public class ContectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int LOADING = 0;
        private static final int ITEM = 1;
        private final Context context;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<ContectListData.Contact> contacts;
        private boolean isLoadingAdded = false;

        public ContectListAdapter(@SuppressLint("UnknownNullness") Context context) {
            this.context = context;
            contacts = new LinkedList<>();
        }

        public void setContactList(@SuppressLint("UnknownNullness") List<ContectListData.Contact> contacts) {
            this.contacts = contacts;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case ITEM:
                    View viewItem = inflater.inflate(R.layout.invite_user_details1, parent, false);
                    viewHolder = new MovieViewHolder(viewItem);
                    break;
                case LOADING:
                    View viewLoading = inflater.inflate(R.layout.item_loading, parent, false);
                    viewHolder = new LoadingViewHolder(viewLoading);

                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ContectListData.Contact Contact_data = contacts.get(position);
            switch (getItemViewType(position)) {
                case ITEM:
                    MovieViewHolder holder1 = (MovieViewHolder) holder;
                    try {

                        if (Contact_data.getIs_blocked().equals(1)) {
                            holder1.iv_block.setVisibility(View.VISIBLE);
                            holder1.userName.setTextColor(mCtx.getResources().getColor(R.color.block_item));

                        } else {
                            holder1.userName.setTextColor(mCtx.getResources().getColor(R.color.unblock_item));
                            holder1.iv_block.setVisibility(View.GONE);

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
                            // inviteUserDetails.setF_latter("");
                            holder1.first_latter.setVisibility(View.GONE);
                            holder1.top_layout.setVisibility(View.GONE);

                        } else {

                            current_latter = first_latter;
                            second_latter = first_latter;
                            holder1.first_latter.setVisibility(View.VISIBLE);
                            holder1.top_layout.setVisibility(View.VISIBLE);


                        }

                     //   Log.e("Contect Image is",Contact_data.getContactImage());

                        if (Contact_data.getContactImage() == null || Contact_data.getContactImage().equals("")) {
                            String name = Contact_data.getFirstname();
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

                    /*  CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context.getApplicationContext());
                      circularProgressDrawable.setStrokeWidth(6f);
                      circularProgressDrawable.setCenterRadius(20f);
                      circularProgressDrawable.setColorSchemeColors(context.getResources().getColor(R.color.purple_200),context.getResources().getColor(R.color.purple_200),context.getResources().getColor(R.color.purple_200));
                      circularProgressDrawable.start();*/
                            Glide.with(context).
                                    load(Contact_data.getContactImage())
                                    .placeholder(R.drawable.shape_primary_circle)
                                    .error(R.drawable.shape_primary_circle)
                                    .into(holder1.profile_image);
                            holder1.no_image.setVisibility(View.GONE);
                            holder1.profile_image.setVisibility(View.VISIBLE);
                        }


                        holder1.main_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                    return;
                                }
                                mLastClickTime = SystemClock.elapsedRealtime();

                                SessionManager.setAdd_Contect_Detail(context, new AddcontectModel());
                                SessionManager.setOneCotect_deatil(context, Contact_data);
                                Intent addnewcontect = new Intent(context, Add_Newcontect_Activity.class);
                                SessionManager.setContect_flag("read");
                                context.startActivity(addnewcontect);
                            }
                        });

                        holder1.main_layout.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {

                                broadcast_manu(Contact_data);
                                return false;
                            }
                        });

                    } catch (Exception e) {
                        contacts.remove(position);
                        /*       notifyDataSetChanged();*/

                    }


                    break;

                case LOADING:
                    LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                    loadingViewHolder.progressBar.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public int getItemCount() {
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

        public void updateList(List<ContectListData.Contact> list) {
            contacts = list;
            notifyDataSetChanged();
        }

        public void removeloist() {
            contacts.clear();
            notifyDataSetChanged();
        }

        public ContectListData.Contact getItem(int position) {
            return contacts.get(position);
        }


        public class MovieViewHolder extends RecyclerView.ViewHolder {
            TextView no_image;
            TextView userName, userNumber, first_latter;
            CircleImageView profile_image;
            LinearLayout top_layout;
            RelativeLayout main_layout;
            ImageView iv_block;

            public MovieViewHolder(View itemView) {
                super(itemView);
                first_latter = itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.top_layout);
                main_layout = itemView.findViewById(R.id.main_layout);
                iv_block = itemView.findViewById(R.id.iv_block);
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


    private void broadcast_manu(ContectListData.Contact contact_item) {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.remove_block_layout, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        TextView selected_block = bottomSheetDialog.findViewById(R.id.selected_block);
        View line_block = bottomSheetDialog.findViewById(R.id.line_block);
        View line_unblock = bottomSheetDialog.findViewById(R.id.line_unblock);
        TextView selected_un_block = bottomSheetDialog.findViewById(R.id.selected_unblock);
        TextView selected_delete = bottomSheetDialog.findViewById(R.id.selected_delete);
        selected_block.setText(getString(R.string.add_blacklist));
        selected_un_block.setText(getString(R.string.remove_blacklist));
        selected_delete.setText(getString(R.string.delete_contact));

        if (contact_item.getIs_blocked().equals(1)) {
            selected_block.setVisibility(View.GONE);
            line_block.setVisibility(View.GONE);
            selected_un_block.setVisibility(View.VISIBLE);
            line_unblock.setVisibility(View.VISIBLE);
        } else {
            line_block.setVisibility(View.VISIBLE);
            selected_block.setVisibility(View.VISIBLE);
            selected_un_block.setVisibility(View.GONE);
            line_unblock.setVisibility(View.GONE);
        }


        selected_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Block Contect
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                try {
                    Contect_BLock(contact_item, "1", bottomSheetDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        selected_un_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Block Contect
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                try {
                    Contect_BLock(contact_item, "0", bottomSheetDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        selected_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Block Contect
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                try {
                    Contect_Remove(contact_item, "0", bottomSheetDialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        bottomSheetDialog.show();

    }


    public void Contect_BLock(ContectListData.Contact contact_data, String block, BottomSheetDialog bottomSheetDialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("is_block", block);
        JSONArray block_array = new JSONArray();
        block_array.put(contact_data.getId());
        paramObject.put("blockContactIds", block_array);
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        retrofitCalls.Block_contact(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {


                if (response.body().getHttp_status() == 200) {

                    Global.Messageshow(getActivity(), mMainLayout, response.body().getMessage(), true);
                    try {
                        fillter_text="";

                        ContectEvent();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bottomSheetDialog.cancel();
                } else {
                    loadingDialog.cancelLoading();
                    Global.Messageshow(getActivity(), mMainLayout, response.body().getMessage(), false);
                    bottomSheetDialog.cancel();
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                bottomSheetDialog.cancel();
            }
        });

    }


    public void Contect_Remove(ContectListData.Contact contact_data, String block, BottomSheetDialog bottomSheetDialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("id", contact_data.getId());
        paramObject.put("status", "D");

        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        retrofitCalls.Addcontect(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {


                if (response.body().getHttp_status() == 200) {
                    Global.Messageshow(getActivity(), mMainLayout, response.body().getMessage(), false);
                    try {
                        fillter_text="";
                       // loadingDialog.showLoadingDialog();
                        ContectEvent();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bottomSheetDialog.cancel();
                } else {
                    loadingDialog.cancelLoading();
                    Global.Messageshow(getActivity(), mMainLayout, response.body().getMessage(), false);
                    bottomSheetDialog.cancel();
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                bottomSheetDialog.cancel();
            }
        });

    }


    void showBottomSheetDialog_Filtter() {

          /*
        Change By :- Paras
        Chnage Date:- 4-2-22
        */
        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.fillter_contact_block_unblock, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);


        CheckBox ch_block = bottomSheetDialog.findViewById(R.id.ch_block);
        CheckBox ch_all = bottomSheetDialog.findViewById(R.id.ch_all);

        switch (Filter) {
            case "BLOCK":
                ch_block.setChecked(true);
                break;
            case "ALL":
                ch_all.setChecked(true);
                break;

        }
        ch_block.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    bottomSheetDialog.dismiss();
                    List<ContectListData.Contact> block_data = SessionManager.getContectList(getActivity()).get(0).getContacts();
                    List<ContectListData.Contact> block__list_data = new ArrayList<>();
                    for (int i = 0; i < block_data.size(); i++) {
                        if (block_data.get(i).getIs_blocked().equals(1)) {
                            block__list_data.add(block_data.get(i));
                        }
                    }
                    contectListData.clear();
                    paginationAdapter.removeloist();
                    contectListData.addAll(block__list_data);
                    paginationAdapter.addAll(contectListData);
                    num_count.setText(contectListData.size() + " Contacts");
                    Filter = "BLOCK";

                    if(block__list_data.size()==0){
                        txt_nolist.setText(mCtx.getResources().getString(R.string.no_block_contact));
                        lay_no_list.setVisibility(View.VISIBLE);
                        layout_list_data.setVisibility(View.GONE);
                    }else {
                        layout_list_data.setVisibility(View.VISIBLE);
                        lay_no_list.setVisibility(View.GONE);
                    }
                }
                else {
                    Filter = "";
                    contectListData.clear();
                    contectListData.addAll(SessionManager.getContectList(getActivity()).get(0).getContacts());
                    iv_filter_icon.setImageResource(R.drawable.ic_filter);
                    paginationAdapter.removeloist();
                    onScrolledToBottom();
                    bottomSheetDialog.dismiss();
                }

            }
        });

        ch_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bottomSheetDialog.dismiss();
                    iv_filter_icon.setImageResource(R.drawable.ic_filter);
                    contectListData.clear();
                    paginationAdapter.removeloist();
                    contectListData.addAll(SessionManager.getContectList(getActivity()).get(0).getContacts());
                    paginationAdapter.addAll(contectListData);
                    num_count.setText(contectListData.size() + " Contacts");
                    Filter = "All";
                    layout_list_data.setVisibility(View.VISIBLE);
                    lay_no_list.setVisibility(View.GONE);
                }

            }
        });


        bottomSheetDialog.show();
    }
}

