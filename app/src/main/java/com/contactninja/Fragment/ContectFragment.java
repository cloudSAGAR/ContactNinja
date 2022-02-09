package com.contactninja.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Addnewcontect_Activity;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Csv_InviteListData;
import com.contactninja.Model.EmailModel;
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
import java.util.OptionalInt;
import java.util.stream.IntStream;

import de.hdodenhof.circleimageview.CircleImageView;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged")
public class ContectFragment extends Fragment  {
    private final static String[] DATA_COLS = {

            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.DATA1,//phone number
            ContactsContract.Data.CONTACT_ID
    };
    List<ContectListData.Contact> main_store = new ArrayList<>();
    public static ArrayList<InviteListData> inviteListData = new ArrayList<>();
    ConstraintLayout mMainLayout;
    Context mCtx;
    RecyclerView rvinviteuserdetails;
    String strtext = "";
    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    SearchView contect_search;
    TextView add_new_contect, num_count;
    ImageView add_new_contect_icon,iv_filter_icon;
    View view1;
    FragmentActivity fragmentActivity;
    LinearLayout add_new_contect_layout;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    int limit = 0, totale_group;
    ContectListAdapter paginationAdapter;
    int currentPage = 1, TOTAL_PAGES = 10;
    boolean isLoading = false;
    boolean isLastPage = false;
    LinearLayoutManager layoutManager;
    SwipeRefreshLayout swipeToRefresh;
    EditText ev_search;
    private List<ContectListData.Contact> contectListData;
    String Filter="";//BLOCK / ALL

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
        // Log.e("View is ", String.valueOf(view1.getVisibility()));
    }



    TextView tv_upload;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View content_view = inflater.inflate(R.layout.fragment_contect, container, false);
        IntentUI(content_view);
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


    if (SessionManager.getContectList(getActivity()).size() != 0) {
            contectListData.addAll(SessionManager.getContectList(getActivity()).get(0).getContacts());
            paginationAdapter.addAll(contectListData);
            num_count.setText(contectListData.size() + " Contacts");
        }
      // EnableRuntimePermission();

        swipeToRefresh.setColorSchemeResources(R.color.purple_200);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ev_search.setText("");

                try {
                    try {
        //                GetContactsIntoArrayList();
                        if(Global.isNetworkAvailable(getActivity(),mMainLayout)){
                            ContectEvent();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    try {
                        if(Global.isNetworkAvailable(getActivity(),mMainLayout)) {
                            ContectEvent();
                        }
                   //     GetContactsIntoArrayList();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
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

                Intent addnewcontect = new Intent(getActivity(), Addnewcontect_Activity.class);
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

                Intent addnewcontect = new Intent(getActivity(), Addnewcontect_Activity.class);
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
                Intent addnewcontect = new Intent(getActivity(), Addnewcontect_Activity.class);
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

            //    loadingDialog.showLoadingDialog();
                EnableRuntimePermission();
               // splitdata(csv_inviteListData);
            }
        });

        ev_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // filter1(s.toString());
                List<ContectListData.Contact> temp = new ArrayList();
                for (ContectListData.Contact d : contectListData) {
                    if (d.getFirstname().toLowerCase().contains(s.toString().toLowerCase())) {
                        temp.add(d);
                        // Log.e("Same Data ",d.getUserName());
                    }
                }
                paginationAdapter.updateList(temp);
                //groupContectAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

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


        return content_view;

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
                Log.e("Upadte Contect", result.toString());
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

    private void splitdata(List<Csv_InviteListData> response) {

        System.out.println("GET DATA IS " + response);

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
            data.append('\n' + response.get(i).getUserName() +
                    ',' + response.get(i).getLast_name() +
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
                    ',' + response.get(i).getContect_email() +
                    ',' + '"' + response.get(i).getUserPhoneNumber() + ',' + '"' +
                    ',' + ' '
            );


        }
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
        Log.e("File is", String.valueOf(path));
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

        retrofitCalls.Upload_csv(sessionManager, loadingDialog, Global.getToken(sessionManager),
                organization_id1, team_id1, user_id1, id, body, Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void success(Response<ApiResponse> response)  {
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



    public void EnableRuntimePermission()
    {

        PermissionListener permissionlistener = new PermissionListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPermissionGranted() {
                try {
                    GetContactsIntoArrayList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                EnableRuntimePermission();
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


    public void GetContactsIntoArrayList() throws JSONException {
        List<EmailModel> emailModels = new ArrayList<>();
        loadingDialog.showLoadingDialog();
        String firstname = "", lastname = "";

        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        Cursor cursor1 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        while (cursor.moveToNext()) {
            userName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            while (cursor1.moveToNext()) {
                contect_email = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                Log.e("Email is ", contect_email);
                String name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                EmailModel emailModel = new EmailModel();
                emailModel.setId(name);
                emailModel.setName(contect_email);
                emailModels.add(emailModel);
            }
            user_phone_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            user_image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
            user_des = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA2));
            TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
            String country = tm.getNetworkCountryIso();
            int countryCode = 0;
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(getActivity());
            try {
                // phone must begin with '+'
                Phonenumber.PhoneNumber numberProto = phoneUtil.parse(user_phone_number, country.toUpperCase());
                countryCode = numberProto.getCountryCode();
                user_phone_number = user_phone_number.replace(" ", "");
                user_phone_number = user_phone_number.replace("-", "");
                if (!user_phone_number.contains("+")) {
                    user_phone_number = String.valueOf("+" + countryCode + user_phone_number);
                }
            } catch (NumberParseException e) {
                System.err.println("NumberParseException was thrown: " + e.toString());
            }
            try {
                contect_email = "";
                region = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DATA8));
                firstname = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                lastname = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String unik_key = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).substring(0, 1)
                    .substring(0, 1)
                    .toUpperCase();

            boolean found = false;
            try {
                found = inviteListData.stream().anyMatch(p -> p.getUserPhoneNumber().equals(user_phone_number));


                if (found) {

                } else {
                    //String  contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(String.valueOf(getActivity().getTaskId())));
                    String contactID = String.valueOf(Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY));
                    inviteListData.add(new InviteListData("" + userName.trim(),
                            user_phone_number.replace(" ", ""),
                            user_image,
                            user_des,
                            "", ""));

                    String email = "";
                    for (int i = 0; i < emailModels.size(); i++) {
                        //             Log.e("Phone ID", userName);
                        //               Log.e("Email Id", emailModels.get(i).getId());
                        if (userName.equals(emailModels.get(i).getId())) {
                            email = emailModels.get(i).getName();
                        } else {
                            // contect_email="";
                        }

                    }
                    try {
                        csv_inviteListData.add(new Csv_InviteListData("" + userName, user_phone_number, email, note, country, city, region, street, "" + lastname));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (csv_inviteListData.size() != 0) {
                        OptionalInt indexOpt = IntStream.range(0, csv_inviteListData.size())
                                .filter(i -> userName.equals(csv_inviteListData.get(i).getUserName()))
                                .findFirst();
                        if (!csv_inviteListData.get(indexOpt.getAsInt()).getUserPhoneNumber().replace(" ", "").equals(user_phone_number.replace(" ", ""))) {
                            // Log.e("postion is", String.valueOf(indexOpt.getAsInt()+1));
                            csv_multiple_data.add(new Csv_InviteListData("" + csv_inviteListData.get(indexOpt.getAsInt()).getUserName(), csv_inviteListData.get(indexOpt.getAsInt()).getUserPhoneNumber() + "," + user_phone_number, contect_email, note, country, city, region, street, "" + lastname));
                            csv_inviteListData.get(indexOpt.getAsInt()).setUserPhoneNumber(csv_inviteListData.get(indexOpt.getAsInt()).getUserPhoneNumber() + "," + user_phone_number);
                            csv_inviteListData.remove(indexOpt.getAsInt() + 1);
                        }
                        //Log.e("Data Is",new Gson().toJson(csv_inviteListData));

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String Is_contact_exist = String.valueOf(user_data.getUser().getIs_contact_exist());

        if (csv_inviteListData.size() == 0) {
            loadingDialog.cancelLoading();
            //Log.e("Csv Size is ","0");
        } else {
            loadingDialog.cancelLoading();

            splitdata(csv_inviteListData);
         /*   main_store = new ArrayList<>();

            for (int i = 0; i < csv_inviteListData.size(); i++) {
                ContectListData.Contact contact = new ContectListData.Contact();
                contact.setFirstname(csv_inviteListData.get(i).getUserName());
                contact.setLastname(csv_inviteListData.get(i).getLast_name());
                contact.setState("I");

                String[] contet_data = csv_inviteListData.get(i).getUserPhoneNumber().toString().split(",");
                List<ContectListData.Contact.ContactDetail> main_contect = new ArrayList<>();
                for (int k = 0; k < contet_data.length; k++) {
                    ContectListData.Contact.ContactDetail contactDetail = new ContectListData.Contact.ContactDetail();
                    contactDetail.setEmailNumber(contet_data[k]);
                    contactDetail.setType("NUMBER");
                    main_contect.add(contactDetail);
                }
                contact.setContactDetails(main_contect);
                main_store.add(contact);
                //      Log.e("Contect List Is ",new Gson().toJson(main_store));
            }
            */
/*

            if (SessionManager.getContectList(getActivity()).size() != 0) {

               *//* num_count.setText(""+SessionManager.getContectList(getActivity()).get(0).getContacts().size() + " Contacts");
                compareLists(SessionManager.getContectList(getActivity()).get(0).getContacts(),main_store);
*//*
               // ContectEvent1(main_store);
            }
            else {
               // ContectEvent1(main_store);
            }*/


        }
        cursor.close();

    }


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
                    Log.e("Contect Size is", String.valueOf(contectListData.size()));
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
                Log.e("Error is", throwable.getMessage());
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
        rvinviteuserdetails.setItemViewCacheSize(5000);
        paginationAdapter.addAll(main_store);
        paginationAdapter.notifyDataSetChanged();
    }


    void filter(String text, View view, FragmentActivity activity) {
        Log.e("Text is", text);
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
        iv_filter_icon=content_view.findViewById(R.id.iv_filter_icon);
        mMainLayout = content_view.findViewById(R.id.mMainLayout);
        rvinviteuserdetails = content_view.findViewById(R.id.contact_list);
        fastscroller = content_view.findViewById(R.id.fastscroller);
        fastscroller_thumb = content_view.findViewById(R.id.fastscroller_thumb);
        contect_search = content_view.findViewById(R.id.contect_search);
        add_new_contect = content_view.findViewById(R.id.add_new_contect);
        num_count = content_view.findViewById(R.id.num_count);
        add_new_contect_icon = content_view.findViewById(R.id.add_new_contect_icon);
        add_new_contect_layout = content_view.findViewById(R.id.add_new_contect_layout);
        swipeToRefresh = content_view.findViewById(R.id.swipeToRefresh);
        ev_search = content_view.findViewById(R.id.ev_search);
        tv_upload=content_view.findViewById(R.id.tv_upload);
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
    }


    public void update(String strtext1, View view, FragmentActivity activity) {
        filter(strtext1, view, activity);

    }


    private void ContectEvent() throws JSONException {
        //  loadingDialog.showLoadingDialog();
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
                swipeToRefresh.setRefreshing(false);


                Log.e("Reponse is", new Gson().toJson(response.body()));
                try {
                    //   if (response.body().getStatus() == 200) {.

                    contectListData.clear();
                    paginationAdapter.removeloist();
                    paginationAdapter.notifyDataSetChanged();
                    rvinviteuserdetails.setItemViewCacheSize(5000);
                    paginationAdapter = new ContectListAdapter(getActivity());
                    rvinviteuserdetails.setAdapter(paginationAdapter);

                    sessionManager.setContectList(getActivity(), new ArrayList<>());


                    SessionManager.setContectList(getActivity(), new ArrayList<>());
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ContectListData>() {
                    }.getType();
                    ContectListData contectListData1 = new Gson().fromJson(headerString, listType);
                    contectListData.addAll(contectListData1.getContacts());
                    paginationAdapter.addAll(contectListData);
                    paginationAdapter.notifyDataSetChanged();
                    List<ContectListData> contectListData_store = new ArrayList<>();
                    contectListData_store.add(contectListData1);
                    SessionManager.setContectList(getActivity(), contectListData_store);
                    if (contectListData1.getContacts().size() == limit) {
                        if (currentPage <= TOTAL_PAGES) {
                            paginationAdapter.addLoadingFooter();
                        } else isLastPage = true;
                    } else {
                        isLastPage = true;
                        isLoading = false;

                    }
                    num_count.setText("" + contectListData1.getTotal() + " Contacts");

                    totale_group = contectListData1.getTotal();
                    //    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Log.e("Error is", throwable.getMessage());
                swipeToRefresh.setRefreshing(false);
                loadingDialog.cancelLoading();

            }
        });


    }

    private void ContectEventnext() throws JSONException {


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

        Log.e("Request data", new Gson().toJson(obj));


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
                Log.e("Error is", throwable.getMessage());

            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        SessionManager.setAdd_Contect_Detail(getActivity(), new AddcontectModel());
        SessionManager.setOneCotect_deatil(getActivity(), new ContectListData.Contact());
     try {
            MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
            myAsyncTasks.execute();

        } catch (Exception e) {
            MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
            myAsyncTasks.execute();

       }
        ev_search.setText("");
        Filter="";

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
                if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
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
        //Log.e("Postion is",String.valueOf(position));
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
                              Log.e("List Of Contec is",new Gson().toJson(Contact_data));
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
        paramObject.put("zoom_id","");
        paramObject.put("contact_image", "");
        paramObject.put("image_extension", "");
        paramObject.put("contact_image_name","");

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
        String str = android.os.Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;
        paramObject.put("imei",str+" "+versionRelease);
        paramObject.put("contact_detail", jsonArray);


        obj.put("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());

        Log.e("Main Data is ", new Gson().toJson(gsonObject));
        retrofitCalls.Addcontect(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            GetContactsIntoArrayList();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
            //Log.e("Postion is",String.valueOf(position));
            switch (getItemViewType(position)) {
                case ITEM:
                    MovieViewHolder holder1 = (MovieViewHolder) holder;
                    try {

                        if (Contact_data.getIs_blocked().equals(1))
                        {
                            holder1.iv_block.setVisibility(View.VISIBLE);
                            holder1.userName.setTextColor(Color.parseColor("#ABABAB"));

                        }
                        else {
                            holder1.userName.setTextColor(Color.parseColor("#4A4A4A"));
                            holder1.iv_block.setVisibility(View.GONE);

                        }
                        holder1.userName.setText(Contact_data.getFirstname()+" "+Contact_data.getLastname());
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
                                Log.e("List Of Contec is",new Gson().toJson(Contact_data));
                                SessionManager.setOneCotect_deatil(context, Contact_data);
                                Intent addnewcontect = new Intent(context, Addnewcontect_Activity.class);
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

                    }
                    catch (Exception e)
                    {
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
                iv_block=itemView.findViewById(R.id.iv_block);
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


    private void broadcast_manu(ContectListData.Contact contact_item) {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.remove_block_layout, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        TextView selected_block = bottomSheetDialog.findViewById(R.id.selected_block);
        View line_block=bottomSheetDialog.findViewById(R.id.line_block);
        View line_unblock=bottomSheetDialog.findViewById(R.id.line_unblock);
        TextView selected_un_block = bottomSheetDialog.findViewById(R.id.selected_unblock);
        TextView selected_delete=bottomSheetDialog.findViewById(R.id.selected_delete);
        selected_block.setText(getString(R.string.add_blacklist));
        selected_un_block.setText(getString(R.string.remove_blacklist));
        selected_delete.setText(getString(R.string.delete_contact));

        if (contact_item.getIs_blocked().equals(1))
        {
            selected_block.setVisibility(View.GONE);
            line_block.setVisibility(View.GONE);
            selected_un_block.setVisibility(View.VISIBLE);
            line_unblock.setVisibility(View.VISIBLE);
        }
        else {
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
                    Contect_BLock(contact_item,"1",bottomSheetDialog);
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
                    Contect_BLock(contact_item,"0",bottomSheetDialog);
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
                    Contect_Remove(contact_item,"0",bottomSheetDialog);
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
        paramObject.put("is_block",block);
        JSONArray block_array = new JSONArray();
        block_array.put(contact_data.getId());
        paramObject.put("blockContactIds", block_array);
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Main Data is ", new Gson().toJson(gsonObject));
        retrofitCalls.Block_contact(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Global.Messageshow(getActivity(), mMainLayout, response.body().getMessage(), true);
                    try {
                        ContectEvent();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bottomSheetDialog.cancel();
                }
                else {
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
        paramObject.put("id",contact_data.getId());
        paramObject.put("status","D");

        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        Log.e("Main Data is ", new Gson().toJson(gsonObject));
        retrofitCalls.Addcontect(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Global.Messageshow(getActivity(), mMainLayout, response.body().getMessage(), false);
                    try {
                        ContectEvent();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bottomSheetDialog.cancel();
                }
                else {
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
                    List<ContectListData.Contact> block_data=SessionManager.getContectList(getActivity()).get(0).getContacts();
                    List<ContectListData.Contact> block__list_data=new ArrayList<>();
                    for (int i=0;i<block_data.size();i++)
                    {
                        if (block_data.get(i).getIs_blocked().equals(1))
                        {
                            block__list_data.add(block_data.get(i));
                        }
                    }
                    contectListData.clear();
                    paginationAdapter.removeloist();
                    contectListData.addAll(block__list_data);
                    paginationAdapter.addAll(contectListData);
                    num_count.setText(contectListData.size() + " Contacts");
                    Filter="BLOCK";
                }

            }
        });

        ch_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bottomSheetDialog.dismiss();
                    iv_filter_icon.setImageResource(R.drawable.ic_filter_on);
                    contectListData.clear();
                    paginationAdapter.removeloist();
                    contectListData.addAll(SessionManager.getContectList(getActivity()).get(0).getContacts());
                    paginationAdapter.addAll(contectListData);
                    num_count.setText(contectListData.size() + " Contacts");
                    Filter="All";
                }

            }
        });


        bottomSheetDialog.show();
    }
}

