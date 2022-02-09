package com.contactninja;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Csv_InviteListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.InviteListData;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitApiClient;
import com.contactninja.retrofit.RetrofitApiInterface;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.TimeZone;
import java.util.stream.IntStream;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")

public class Contect_Demo extends AppCompatActivity  {

    private final static String[] DATA_COLS = {

            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.DATA1,//phone number
            ContactsContract.Data.CONTACT_ID
    };
    //Declare Variabls for fragment
    public static int navItemIndex = 0;
    public static ArrayList<InviteListData> inviteListData = new ArrayList<>();
    public static RelativeLayout mMainLayout;
    private static int RC_APP_UPDATE = 0;
    private final List<ContectListData.Contact> contectListData = new ArrayList<>();
    InstallStateUpdatedListener installStateUpdatedListener;
    ImageView llHome, llsend, llContact, llUser;
    FrameLayout frameLayout;
    SessionManager sessionManager;
    boolean doubleBackToExitPressedOnce = false;
    LinearLayout llCreate;
    LoadingDialog loadingDialog;
    List<Csv_InviteListData> csv_inviteListData = new ArrayList<>();
    List<Csv_InviteListData> csv_multiple_data = new ArrayList<>();
    int limit = 0;
    RetrofitCalls retrofitCalls;
    String userName = "", user_phone_number = "", user_image = "", user_des = "", strtext = "", old_latter = "", contect_type = "", contect_email = "",
            contect_type_work = "", email_type_home = "", email_type_work = "", country = "", city = "", region = "", street = "",
            postcode = "", postType = "", note = "";
    StringBuilder data;
    Cursor cursor;
    private AppUpdateManager mAppUpdateManager;
    private long mLastClickTime = 0;
    private boolean shouldLoadHomeFragOnBackPress = true;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private BroadcastReceiver mNetworkReceiver;
    RecyclerView rvinviteuserdetails;
    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    ContectListAdapter_demo paginationAdapter;
    int currentPage = 1, TOTAL_PAGES = 10;
    boolean isLoading = false;
    boolean isLastPage = false;
    LinearLayoutManager layoutManager;
    ImageView iv_back;
    TextView save_button;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect_demo);
        mNetworkReceiver = new ConnectivityReceiver();
        registerNetworkBroadcastForNougat();
        sessionManager = new SessionManager(this);
        sessionManager.login();
        loadingDialog = new LoadingDialog(this);
        retrofitCalls = new RetrofitCalls(getApplicationContext());
        SessionManager.setGroupData(getApplicationContext(), new Grouplist.Group());
        IntentUI();
        Calendar cal = Calendar.getInstance();
        TimeZone tz1 = cal.getTimeZone();
        Calendar calendar = Calendar.getInstance(tz1,
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        String offset = localTime.substring(0, 1);
        EnableRuntimePermission();
        navItemIndex = 0;

        layoutManager = new LinearLayoutManager(this);
        rvinviteuserdetails.setLayoutManager(layoutManager);
        rvinviteuserdetails.setHasFixedSize(true);
        rvinviteuserdetails.setItemViewCacheSize(5000);
        paginationAdapter = new ContectListAdapter_demo(this);
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

        save_button.setText("Upload All Contect");
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.showLoadingDialog();
                splitdata(csv_inviteListData);
            }
        });



    }



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
            FileOutputStream out = openFileOutput("CSV_Data_" + time + ".csv", Context.MODE_PRIVATE);

            //store the data in CSV file by passing String Builder data
            out.write(data.toString().getBytes());
            out.close();
            Context context = getApplicationContext();
            final File newFile = new File(Environment.getExternalStorageDirectory(), "SimpleCVS");
            if (!newFile.exists()) {
                newFile.mkdir();
            }
            File file = new File(context.getFilesDir(), "CSV_Data_" + time + ".csv");
            Uri path = FileProvider.getUriForFile(context, "com.contactninja", file);
            //once the file is ready a share option will pop up using which you can share
            // the same CSV from via Gmail or store in Google Drive


            if (Global.isNetworkAvailable(Contect_Demo.this, mMainLayout)) {
                Uploadcsv(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void Uploadcsv(File path) throws JSONException {
        Log.e("File is", String.valueOf(path));
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
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
                organization_id1, team_id1, user_id1, id, body, Global.getVersionname(Contect_Demo.this), Global.Device,imei, new RetrofitCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void success(Response<ApiResponse> response)  {
                        sessionManager.setcontectexits("1");
                        if (response.body().getHttp_status() == 200) {

                            SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
                            user_data.getUser().setIs_contact_exist(1);
                            SessionManager.setUserdata(getApplicationContext(), user_data);

                            loadingDialog.cancelLoading();
                            try {
                                GetContactsIntoArrayList();
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

    public void EnableRuntimePermission() {

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
        TedPermission.with(this)
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


    private void IntentUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        rvinviteuserdetails = findViewById(R.id.contact_list);
        fastscroller = findViewById(R.id.fastscroller);
        fastscroller_thumb = findViewById(R.id.fastscroller_thumb);
        mMainLayout = findViewById(R.id.mMainLayout);
        llHome = findViewById(R.id.llHome);
        llContact = findViewById(R.id.llContact);
        llsend = findViewById(R.id.llsend);
        llUser = findViewById(R.id.llUser);
        frameLayout = findViewById(R.id.frameContainer);
        llCreate = findViewById(R.id.llCreate);
        iv_back=findViewById(R.id.iv_back);
        save_button=findViewById(R.id.save_button);
      /*  llHome.setOnClickListener(this);
        llContact.setOnClickListener(this);
        llsend.setOnClickListener(this);
        llUser.setOnClickListener(this);
        llCreate.setOnClickListener(this);*/

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void GetContactsIntoArrayList() throws JSONException {

        loadingDialog.showLoadingDialog();
        String firstname = "", lastname = "";

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        while (cursor.moveToNext()) {
            userName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            user_phone_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            user_image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
            user_des = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA2));
            TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
            String country = tm.getNetworkCountryIso();
            int countryCode = 0;
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(Contect_Demo.this);
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
                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(String.valueOf(getTaskId())));
                    String contactID = String.valueOf(Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY));
                    inviteListData.add(new InviteListData("" + userName.trim(),
                            user_phone_number.replace(" ", ""),
                            user_image,
                            user_des,
                            "", ""));


                    try {
                        csv_inviteListData.add(new Csv_InviteListData("" + userName, user_phone_number, contect_email, note, country, city, region, street, "" + lastname));
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


        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String Is_contact_exist = String.valueOf(user_data.getUser().getIs_contact_exist());

        if (csv_inviteListData.size() == 0) {
            loadingDialog.cancelLoading();
            //Log.e("Csv Size is ","0");
        } else {
            List<ContectListData.Contact> main_store=new ArrayList<>();

            for (int i=0;i<csv_inviteListData.size();i++)
            {
                ContectListData.Contact contact=new ContectListData.Contact();
                contact.setFirstname(csv_inviteListData.get(i).getUserName());
                contact.setLastname(csv_inviteListData.get(i).getLast_name());
                contact.setState("I");

                String[] contet_data=csv_inviteListData.get(i).getUserPhoneNumber().toString().split(",");
                List<ContectListData.Contact.ContactDetail> main_contect=new ArrayList<>();
                for (int k=0;k<contet_data.length;k++)
                {
                    ContectListData.Contact.ContactDetail contactDetail=new ContectListData.Contact.ContactDetail();
                    contactDetail.setEmailNumber(contet_data[k]);
                    contactDetail.setType("NUMBER");
                    main_contect.add(contactDetail);
                }
                contact.setContactDetails(main_contect);
                main_store.add(contact);
          //      Log.e("Contect List Is ",new Gson().toJson(main_store));
            }


            if (SessionManager.getContectList(this).size() != 0) {
                Log.e("Contect Size is", String.valueOf(SessionManager.getContectList(this).get(0).getContacts().size()));
                Log.e("Main Size is", String.valueOf(main_store.size()));
                compareLists(SessionManager.getContectList(this).get(0).getContacts(),main_store);
                //ContectEvent(main_store);
            }
            else {
                ContectEvent(main_store);
            }


        }
        cursor.close();

    }




    private void ContectEvent(List<ContectListData.Contact> main_store) throws JSONException {

        SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_id);
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
        Call<ApiResponse> call = registerinfo.Contect_List(RetrofitApiClient.API_Header, token, obj, Global.getVersionname(Contect_Demo.this),
                Global.Device);
        call.enqueue(new Callback<ApiResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                loadingDialog.cancelLoading();

                if (response.body().getHttp_status() == 200) {

                    SessionManager.setContectList(getApplicationContext(), new ArrayList<>());
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ContectListData>() {
                    }.getType();
                    ContectListData contectListData1 = new Gson().fromJson(headerString, listType);
                    contectListData.addAll(contectListData1.getContacts());
                    Log.e("Contect Size is",String.valueOf(contectListData.size()));
                    List<ContectListData> contectListData_store = new ArrayList<>();
                    contectListData_store.add(contectListData1);
                    SessionManager.setContectList(getApplicationContext(), contectListData_store);
                    List<ContectListData.Contact> contacts=new ArrayList<>();
                    contacts.addAll(contectListData1.getContacts());
                    compareLists(contacts,main_store);
                }
                else {
                    loadingDialog.cancelLoading();
                    paginationAdapter.addAll(main_store);
                    paginationAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Log.e("Error is", throwable.getMessage());
                loadingDialog.cancelLoading();

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void compareLists(List<ContectListData.Contact> contacts, List<ContectListData.Contact> main_store) {
        for (int i=0;i<contacts.size();i++)
        {

            for (int j=0;j<main_store.size();j++)
            {

                if (contacts.get(i).getFirstname().toString().equals(main_store.get(j).getFirstname().toString()))
                {
                    main_store.remove(j);
                    main_store.add(j,contacts.get(i));
                    break;
                }
                else if (main_store.size()==j+1)
                {
                    main_store.add(main_store.size(),contacts.get(i));
                    break;
                }

            }
        }
        loadingDialog.cancelLoading();
        Collections.sort(main_store, new Comparator<ContectListData.Contact>() {
            @Override
            public int compare(ContectListData.Contact s1, ContectListData.Contact s2) {
                return s1.getFirstname().compareToIgnoreCase(s2.getFirstname());
            }
        });
        paginationAdapter.addAll(main_store);
        paginationAdapter.notifyDataSetChanged();
        Log.e("Model List Is Size",String.valueOf(main_store.size()));
        Log.e("Model List Is",new Gson().toJson(main_store));
    }

}