package com.contactninja;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.contactninja.Campaign.List_itm.Campaign_List_Activity;
import com.contactninja.Fragment.Main_Task_Fragment;
import com.contactninja.Fragment.Main_contact_Fragment;
import com.contactninja.Fragment.Main_home_Fragment;
import com.contactninja.Fragment.Main_userProfile_Fragment;
import com.contactninja.Group.SendBroadcast;
import com.contactninja.Main_Broadcast.List_And_show.List_Broadcast_activity;
import com.contactninja.Manual_email_text.Text_And_Email_Auto_Manual;
import com.contactninja.Model.Broadcast_Data;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Contect_Db;
import com.contactninja.Model.Csv_InviteListData;
import com.contactninja.Model.EmailModel;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.InviteListData;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Utils.App;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.DatabaseClient;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitApiClient;
import com.contactninja.retrofit.RetrofitApiInterface;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.TimeZone;
import java.util.stream.IntStream;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private final static String[] DATA_COLS = {

            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.DATA1,//phone number
            ContactsContract.Data.CONTACT_ID
    };
    //Declare Variabls for fragment
    public static int navItemIndex = 0;
    public static ArrayList<InviteListData> inviteListData = new ArrayList<>();
    public static RelativeLayout mMainLayout;
    public static MainActivity activity;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = MainActivity.this;
        mNetworkReceiver = new ConnectivityReceiver();
        registerNetworkBroadcastForNougat();
        sessionManager = new SessionManager(this);
        sessionManager.login();
    /*    Intent intent=new Intent(getApplicationContext(),Contect_Demo.class);
        startActivity(intent);*/
        loadingDialog = new LoadingDialog(this);
        retrofitCalls = new RetrofitCalls(getApplicationContext());
        SessionManager.setGroupData(getApplicationContext(), new Grouplist.Group());
        IntentUI();
        Calendar cal = Calendar.getInstance();
        TimeZone tz1 = cal.getTimeZone();
        Calendar calendar = Calendar.getInstance(tz1, Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
        String offset = localTime.substring(0, 1);

        UpdateManageCheck();
        EnableRuntimePermission();
        navItemIndex = 0;
        displayView();
        ImageSetLight(getResources().getString(R.string.select_Home));
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
                    ContectEvent();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (SessionManager.getContectList(getApplicationContext()).size() == 0) {
                    loadingDialog.showLoadingDialog();
                }
                MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                myAsyncTasks.execute();

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
              //  EnableRuntimePermission();
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
        llHome = findViewById(R.id.llHome);
        llContact = findViewById(R.id.llContact);
        llsend = findViewById(R.id.llsend);
        llUser = findViewById(R.id.llUser);
        frameLayout = findViewById(R.id.frameContainer);
        llCreate = findViewById(R.id.llCreate);
        llHome.setOnClickListener(this);
        llContact.setOnClickListener(this);
        llsend.setOnClickListener(this);
        llUser.setOnClickListener(this);
        llCreate.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void GetContactsIntoArrayList() {

        List<EmailModel> emailModels = new ArrayList<>();

        String firstname = "", lastname = "";

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        Cursor cursor1 = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        while (cursor.moveToNext()) {

            userName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            while (cursor1.moveToNext()) {
                contect_email = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                String name = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                EmailModel emailModel = new EmailModel();
                emailModel.setId(name);
                emailModel.setName(contect_email);
                emailModels.add(emailModel);
            }

            user_phone_number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
            String country = tm.getNetworkCountryIso();
            int countryCode = 0;
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(MainActivity.this);
            try {
                // phone must begin with '+'
                Phonenumber.PhoneNumber numberProto = phoneUtil.parse(user_phone_number, country.toUpperCase());
                countryCode = numberProto.getCountryCode();
                user_phone_number = user_phone_number.replace(" ", "");
                user_phone_number = user_phone_number.replace("-", "");
                if (!user_phone_number.contains("+")) {
                    user_phone_number = "+" + countryCode + user_phone_number;
                }
            } catch (NumberParseException e) {
               e.printStackTrace();
            }

            String unik_key = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).substring(0, 1)
                    .substring(0, 1)
                    .toUpperCase();

            boolean found = false;
            try {
                found = inviteListData.stream().anyMatch(p -> p.getUserPhoneNumber().equals(user_phone_number));


                if (!found) {
                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(String.valueOf(getTaskId())));
                    String contactID = String.valueOf(Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY));
                    inviteListData.add(new InviteListData("" + userName.trim(),
                            user_phone_number.replace(" ", ""),
                            user_image,
                            user_des,
                            "", ""));
                    String email = "";
                    for (int i = 0; i < emailModels.size(); i++) {

                        if (userName.equals(emailModels.get(i).getId())) {
                            email = emailModels.get(i).getName();
                        }
                    }

                    try {
                        csv_inviteListData.add(new Csv_InviteListData("" + userName, user_phone_number,
                                email, note, country, city, region, street, "" + lastname));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (csv_inviteListData.size() != 0) {
                        OptionalInt indexOpt = IntStream.range(0, csv_inviteListData.size())
                                .filter(i -> userName.equals(csv_inviteListData.get(i).getUserName()))
                                .findFirst();
                        if (!csv_inviteListData.get(indexOpt.getAsInt()).getUserPhoneNumber().replace(" ", "").equals(user_phone_number.replace(" ", ""))) {
                            csv_multiple_data.add(new Csv_InviteListData("" + csv_inviteListData.get(indexOpt.getAsInt()).getUserName(), csv_inviteListData.get(indexOpt.getAsInt()).getUserPhoneNumber() + "," + user_phone_number, contect_email, note, country, city, region, street, "" + lastname));
                            csv_inviteListData.get(indexOpt.getAsInt()).setUserPhoneNumber(csv_inviteListData.get(indexOpt.getAsInt()).getUserPhoneNumber() + "," + user_phone_number);
                            csv_inviteListData.remove(indexOpt.getAsInt() + 1);
                        }

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String Is_contact_exist = String.valueOf(user_data.getUser().getIs_contact_exist());

        if (csv_inviteListData.size() == 0) {
            try {
                ContectEvent();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            String isContact = SessionManager.getcontectexits();
            if (isContact.equals("0")) {
                //Not Upload Contect Then If Call
                if (Is_contact_exist.equals("0")) {
                    limit = csv_inviteListData.size();
                    splitdata(csv_inviteListData);
                } else {
                    getTasks();
                }
            } else {
                getTasks();

            }
        }
        cursor.close();

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
            data.append('\n' + response.get(i).getUserName().replaceAll("[-+.^:,]","")+
                    ',' + response.get(i).getLast_name().replaceAll("[-+.^:,]","")+
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
     /*   Intent intent = ShareCompat.IntentBuilder.from(this)
                    .setType("application/pdf")
                    .setStream(path)
                    .setChooserTitle("Choose bar")
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(intent);
*/
            if (Global.isNetworkAvailable(MainActivity.this, mMainLayout)) {
                Uploadcsv(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Uploadcsv(File path) throws JSONException {


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
                organization_id1, team_id1, user_id1, id, body, Global.getVersionname(MainActivity.this), Global.Device,imei, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        sessionManager.setcontectexits("1");
                        if (response.body().getHttp_status() == 200) {

                            SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
                            user_data.getUser().setIs_contact_exist(1);
                            SessionManager.setUserdata(getApplicationContext(), user_data);

                            //   loadingDialog.cancelLoading();
                            try {
                                limit = csv_inviteListData.size();
                                ContectEvent();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {

                            try {
                                ContectEvent();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        sessionManager.setCsv_token();


                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }

                });


    }

    private void ContectEvent() throws JSONException {

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
        Call<ApiResponse> call = registerinfo.Contect_List(RetrofitApiClient.API_Header, token, obj, Global.getVersionname(MainActivity.this),
                Global.Device);
        call.enqueue(new Callback<ApiResponse>() {
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
                    List<ContectListData> contectListData_store = new ArrayList<>();
                    contectListData_store.add(contectListData1);
                    SessionManager.setContectList(getApplicationContext(), contectListData_store);


                    delete(contectListData);

                    /*Duplicate_remove();*/
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                loadingDialog.cancelLoading();

            }
        });


    }

    private void UpdateManageCheck() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Global.IsNotNull(info)) {
            RC_APP_UPDATE = info.versionCode;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        SessionManager.setOneCotect_deatil(getApplicationContext(), new ContectListData.Contact());
        Global.getInstance().setConnectivityListener(MainActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        installStateUpdatedListener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                if (mAppUpdateManager != null) {
                    mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                }
            }
        };
        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE /*AppUpdateType.FLEXIBLE*/)) {

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE /*AppUpdateType.FLEXIBLE*/, MainActivity.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            }
        });
    }

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.mMainLayout),
                        getString(R.string.appUpdate),
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction(getString(R.string.install), view -> {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.completeUpdate();
            }
        });


        snackbar.setActionTextColor(getResources().getColor(R.color.green));
        snackbar.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void ImageSetLight(String imageName) {
        switch (imageName) {
            case "Home":
                llHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_home_select));
                llsend.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_task));
                llContact.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_contacts));
                llUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_user));
                break;
            case "Send":
                llHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_home));
                llsend.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_task_select));
                llContact.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_contacts));
                llUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_user));
                break;
            case "Contact":
                llHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_home));
                llsend.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_task));
                llContact.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_contacts_selece));
                llUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_user));
                break;
            case "User":
                llHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_home));
                llsend.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_task));
                llContact.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_contacts));
                llUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_user_select));
                break;


        }
    }

    @Override
    public void onBackPressed() {
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                displayView();
                ImageSetLight(getResources().getString(R.string.select_Home));
                return;
            }

        }
        if (doubleBackToExitPressedOnce) {
            App.isFirstTime = true;
            super.onBackPressed();
            return;
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@SuppressLint("UnknownNullness") View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        switch (v.getId()) {
            case R.id.llHome:
                navItemIndex = 0;
                displayView();

                ImageSetLight(getResources().getString(R.string.select_Home));


                break;
            case R.id.llsend:
                navItemIndex = 1;
                displayView();

                ImageSetLight(getResources().getString(R.string.select_Send));


                break;
            case R.id.llContact:
                navItemIndex = 2;
                displayView();

                ImageSetLight(getResources().getString(R.string.select_Contact));


                break;
            case R.id.llUser:
                SessionManager.setContect_flag("read");
                navItemIndex = 3;
                displayView();

                ImageSetLight(getResources().getString(R.string.select_User));

                break;
            case R.id.llCreate:
                navItemIndex = 4;
                displayView();
                break;

        }

    }

    public void displayView() {

        Fragment fragment = null;
        switch (navItemIndex) {
            case 0:

                fragment = new Main_home_Fragment(MainActivity.this);
                shouldLoadHomeFragOnBackPress = false;
                break;
            case 1:
                fragment = new Main_Task_Fragment();
                shouldLoadHomeFragOnBackPress = true;
                break;
            case 2:
                SessionManager.setContect_flag("read");
                fragment = new Main_contact_Fragment();
                shouldLoadHomeFragOnBackPress = true;
                break;
            case 3:
                fragment = new Main_userProfile_Fragment();
                shouldLoadHomeFragOnBackPress = true;
                break;
            case 4:
                SessionManager.setgroup_broadcste(getApplicationContext(), new ArrayList<>());
                SessionManager.setContectList_broadcste(getApplicationContext(), new ArrayList<>());
                Broadcast_Data broadcast_data = new Broadcast_Data();
                SessionManager.setAdd_Broadcast_Data(broadcast_data);
                broadcast_manu();
                break;
        }
        if (fragment != null) {


            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, fragment, "Fragment");
            fragmentTransaction.commitAllowingStateLoss();
        }

    }

    private void broadcast_manu() {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.brodcaste_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);
        TextView selected_campaign = bottomSheetDialog.findViewById(R.id.selected_campaign);

        TextView selected_broadcast = bottomSheetDialog.findViewById(R.id.selected_broadcast);
        TextView selected_task = bottomSheetDialog.findViewById(R.id.selected_task);
        selected_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                 SessionManager.setGroupList(getApplicationContext(),new ArrayList<>() );

                if (Global.IsNotNull(SessionManager.getContectList(getApplicationContext()))) {
                    SessionManager.setCampaign_Day("00");
                    SessionManager.setCampaign_minute("00");
                    SessionManager.setCampaign_type("");
                    SessionManager.setCampaign_type_name("");
                    SessionManager.setEmail_screen_name("");
                    Intent intent1 = new Intent(getApplicationContext(), Text_And_Email_Auto_Manual.class);
                    intent1.putExtra("flag", "add");
                    startActivity(intent1);//  finish();
                }else {
                    EnableRuntimePermission();
                }
                bottomSheetDialog.dismiss();
            }
        });

        selected_broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (Global.IsNotNull(SessionManager.getContectList(getApplicationContext()))) {
                    SessionManager.setGroupList(getApplicationContext(), new ArrayList<>());
                    SessionManager.setgroup_broadcste(getApplicationContext(), new ArrayList<>());
                    SessionManager.setCampaign_Day("00");
                    SessionManager.setCampaign_minute("00");
                    SessionManager.setCampaign_type("");
                    SessionManager.setCampaign_type_name("");
                    Intent intent = new Intent(getApplicationContext(),List_Broadcast_activity.class);
                    startActivity(intent);
                }else {
                    EnableRuntimePermission();
                }
                bottomSheetDialog.dismiss();
            }
        });
        selected_campaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (Global.IsNotNull(SessionManager.getContectList(getApplicationContext()))) {
                    Intent intent = new Intent(getApplicationContext(), Campaign_List_Activity.class);
                    startActivity(intent);
                }else {
                    EnableRuntimePermission();
                }
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.show();

    }

    public void delete(List<ContectListData.Contact> contectListData) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        //.deleteDuplicates();
                        //.DeleteData(inviteListData.getUserPhoneNumber());
                        .RemoveData();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                SetDatainDatabase(contectListData);
                super.onPostExecute(aVoid);

            }
        }

        DeleteTask ut = new DeleteTask();
        ut.execute();
    }

    public void Duplicate_remove() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        //.deleteDuplicates();
                        //.DeleteData(inviteListData.getUserPhoneNumber());
                        .deleteDuplicates();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }

        DeleteTask ut = new DeleteTask();
        ut.execute();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(MainActivity.this, mMainLayout);
    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<Contect_Db>> {
            @Override
            protected List<Contect_Db> doInBackground(Void... voids) {
                List<Contect_Db> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getvalue();
                return taskList;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void onPostExecute(List<Contect_Db> contect_list) {


                //Get All Contect Locale Room Database  No Data Then Upload Csv Code Call
                if (contect_list.size() == 0) {
                    splitdata(csv_inviteListData);
                } else if (contect_list.size() == csv_inviteListData.size()) {
                    //Phone Book And Local Data Size Same then Update Call

                    for (int i = 0; i < csv_inviteListData.size(); i++) {
                        String num = csv_inviteListData.get(i).getUserPhoneNumber();
                        String f_name = csv_inviteListData.get(i).getUserName();
                        boolean found = contect_list.stream().anyMatch(p -> p.getEmailNumber().equals(num));
                        boolean found1 = contect_list.stream().anyMatch(p -> p.getFirst_name().equals(f_name));

                        if (found == true && found1 == false) {
                            check_list_for_Update(csv_inviteListData.get(i).getUserName(), csv_inviteListData.get(i).getLast_name(), csv_inviteListData.get(i).getUserPhoneNumber());
                        }
                    }

                } else {
                    //Call Phone Number Exits OR N0t
                    getUser_check(csv_inviteListData);
                }

                super.onPostExecute(contect_list);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void getUser_check(List<Csv_InviteListData> csv_inviteListData) {
        //Get One By One Model Data
        for (int i = 0; i < csv_inviteListData.size(); i++) {
            check_list(csv_inviteListData.get(i).getUserName(), csv_inviteListData.get(i).getLast_name(), csv_inviteListData.get(i).getUserPhoneNumber());
        }


    }

    public void check_list(String userName, String last_name, String userPhoneNumber) {
        class GetTasks extends AsyncTask<Void, Void, List<Contect_Db>> {
            @Override
            protected List<Contect_Db> doInBackground(Void... voids) {
                List<Contect_Db> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getSameValue(userName, last_name, userPhoneNumber);
                if (taskList.size() == 0) {
                    //Update Call
                    check_list_for_Update(userName, last_name, userPhoneNumber);

                } else if (taskList.size() != 1) {
                    loadingDialog.cancelLoading();   //Multiple Same Data Then Remove
                    Duplicate_remove();
                } else if (taskList.size() == 1) {
                    loadingDialog.cancelLoading();
                } else {
                    loadingDialog.cancelLoading();
                }
                return taskList;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void onPostExecute(List<Contect_Db> contect_list) {
                super.onPostExecute(contect_list);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public void check_list_for_Update(String userName, String last_name, String userPhoneNumber) {
        class GetTasks extends AsyncTask<Void, Void, List<Contect_Db>> {
            @Override
            protected List<Contect_Db> doInBackground(Void... voids) {
                List<Contect_Db> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getSameValue_Firstorlastname(userPhoneNumber);

                if (taskList.size() == 0) {
                    //No Data Then Add Contect
                    List<Csv_InviteListData> csv_inviteListData1 = new ArrayList<>();
                    csv_inviteListData1.add(new Csv_InviteListData(userName, userPhoneNumber, "", "", "", "", "", "", last_name));
                    splitdata(csv_inviteListData1);
                } else {
                    //Update Contect Api Call
                    try {
                        if (Global.isNetworkAvailable(MainActivity.this, mMainLayout)) {

                            AddContect_Api1(userName, last_name, userPhoneNumber, taskList.get(0).getContect_id(), taskList.get(0).getContactId());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return taskList;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void onPostExecute(List<Contect_Db> contect_list) {
                //Log.e("Contect List Update Size 1 "," : "+contect_list.size());
                super.onPostExecute(contect_list);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public void AddContect_Api1(String userName, String last_name, String userPhoneNumber, String contect_id, Integer contactId) throws JSONException {

//        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String user_id = String.valueOf(user_data.getUser().getId());
        JSONObject obj = new JSONObject();

        JSONObject paramObject = new JSONObject();

        paramObject.put("id", contect_id);
        paramObject.put("firstname", userName);
        if (!last_name.equals("")) {
            paramObject.put("lastname", last_name);
        }
        paramObject.put("user_id", user_id);
        paramObject.put("organization_id", "1");
        paramObject.put("team_id", "1");

        paramObject.put("imei", Global.imei);
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());

        retrofitCalls.Addcontect(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(MainActivity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    try {
                        ContectEvent();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), false);
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });

    }

    private void SetDatainDatabase(List<ContectListData.Contact> contectListData) {
        //Log.e("List is", new Gson().toJson(contectListData));


        for (int i = 0; i < contectListData.size(); i++) {
            Contect_Db contect_db = new Contect_Db();
            for (int j = 0; j < contectListData.get(i).getContactDetails().size(); j++) {
                ContectListData.Contact.ContactDetail data = contectListData.get(i).getContactDetails().get(j);
                if (!data.getEmailNumber().equals(" ")) {
                    contect_db.setId1(data.getId());
                    contect_db.setContect_id(String.valueOf(contectListData.get(i).getId()));
                    contect_db.setCreatedAt(data.getCreatedAt());
                    contect_db.setOrganizationId(contectListData.get(i).getOrganizationId());
                    contect_db.setTeamId(contectListData.get(i).getTeamId());
                    contect_db.setFirst_name(contectListData.get(i).getFirstname());
                    contect_db.setLast_name(contectListData.get(i).getLastname());
                    contect_db.setFlag("csv");
                    contect_db.setEmailNumber(data.getEmailNumber());
                    contect_db.setCountryCode(data.getCountryCode());
                    contect_db.setType(data.getType());
                    contect_db.setStatus(data.getStatus());
                    contect_db.setDeletedAt(String.valueOf(data.getDeletedAt()));
                    contect_db.setCreatedBy(data.getCreatedBy());
                    contect_db.setIsBlocked(data.getIsBlocked());
                    contect_db.setCreatedAt(data.getCreatedAt());
                    contect_db.setUpdatedAt(data.getUpdatedAt());
                }
            }
            save_data(contect_db);
        }


    }

    public void save_data(Contect_Db contect_db) {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .insert(contect_db);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";
            try {
                if (Global.isNetworkAvailable(MainActivity.this, mMainLayout)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        GetContactsIntoArrayList();
                    }
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
}