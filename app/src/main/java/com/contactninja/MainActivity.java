package com.contactninja;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
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

import com.contactninja.Fragment.Broadcast_Frgment.Broadcst_Activty;
import com.contactninja.Fragment.Contect_main_Fragment;
import com.contactninja.Fragment.Home_Main_Fragment;
import com.contactninja.Fragment.Send_Main_Fragment;
import com.contactninja.Fragment.UserProfile_Main_Fragment;
import com.contactninja.Model.Broadcast_Data;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Contect_Db;
import com.contactninja.Model.Csv_InviteListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.InviteListData;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Utils.App;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Declare Variabls for fragment
    public static int navItemIndex = 0;
    private static int RC_APP_UPDATE = 0;
    InstallStateUpdatedListener installStateUpdatedListener;
    RelativeLayout mMainLayout;
    ImageView llHome, llsend, llContact, llUser;
    FrameLayout frameLayout;
    SessionManager sessionManager;
    boolean doubleBackToExitPressedOnce = false;
    private AppUpdateManager mAppUpdateManager;
    private long mLastClickTime = 0;
    private boolean shouldLoadHomeFragOnBackPress = true;
    LinearLayout llCreate;
    LoadingDialog loadingDialog;
    List<Csv_InviteListData> csv_inviteListData=new ArrayList<>();
    private List<ContectListData.Contact> contectListData=new ArrayList<>();

    int limit=0;
    RetrofitCalls retrofitCalls;

    public static ArrayList<InviteListData> inviteListData = new ArrayList<>();
    String userName = "", user_phone_number = "", user_image = "", user_des = "", strtext = "", old_latter = "", contect_type = "", contect_email = "",
            contect_type_work = "", email_type_home = "", email_type_work = "", country = "", city = "", region = "", street = "",
            postcode = "", postType = "", note = "";
    StringBuilder data;
    Cursor cursor;
    private final static String[] DATA_COLS = {

            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.DATA1,//phone number
            ContactsContract.Data.CONTACT_ID
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        sessionManager.login();
        loadingDialog=new LoadingDialog(this);
        retrofitCalls = new RetrofitCalls(getApplicationContext());
        SessionManager.setGroupData(getApplicationContext(),new Grouplist.Group());
        IntentUI();


        Calendar cal = Calendar.getInstance();
        TimeZone tz1 = cal.getTimeZone();
        Calendar calendar = Calendar.getInstance(tz1,
                Locale.getDefault());

        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("Z");
        String localTime = date.format(currentLocalTime);
       String  offset = localTime.substring(0, 1);
       Log.e("offset",offset);
        Log.e("Show Local ",localTime);
        Log.e("GMT offset is %s hours",""+ TimeUnit.MINUTES.convert(tz1.getRawOffset(), TimeUnit.MILLISECONDS));
        UpdateManageCheck();
        EnableRuntimePermission();

        navItemIndex = 0;
        displayView();
        ImageSetLight("Home");

    }


    public void EnableRuntimePermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPermissionGranted() {

               // loadingDialog.showLoadingDialog();
                GetContactsIntoArrayList();

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
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS)
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
        llCreate=findViewById(R.id.llCreate);
        llHome.setOnClickListener(this);
        llContact.setOnClickListener(this);
        llsend.setOnClickListener(this);
        llUser.setOnClickListener(this);
        llCreate.setOnClickListener(this);

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void GetContactsIntoArrayList() {

        String firstname = "", lastname = "";
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


                firstname = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                lastname = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));

            } catch (Exception e) {

            }

            String unik_key = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)).substring(0, 1)
                    .substring(0, 1)
                    .toUpperCase();

            boolean found = false;
            try {
                found = inviteListData.stream().anyMatch(p -> p.getUserPhoneNumber().equals(user_phone_number));

            } catch (Exception e) {

            }

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

                }

                if (csv_inviteListData.size() != 0) {
                    OptionalInt indexOpt = IntStream.range(0, csv_inviteListData.size())
                            .filter(i -> userName.equals(csv_inviteListData.get(i).getUserName()))
                            .findFirst();

                    // Log.e("Size is", String.valueOf(indexOpt.getAsInt()));
                    if (!csv_inviteListData.get(indexOpt.getAsInt()).getUserPhoneNumber().replace(" ", "").equals(user_phone_number.replace(" ", ""))) {
                        // Log.e("postion is", String.valueOf(indexOpt.getAsInt()+1));

                        csv_inviteListData.get(indexOpt.getAsInt()).setUserPhoneNumber(csv_inviteListData.get(indexOpt.getAsInt()).getUserPhoneNumber() + "," + user_phone_number);

                        csv_inviteListData.remove(indexOpt.getAsInt() + 1);
                    }


                    //   Log.e("Data Is",new Gson().toJson(csv_inviteListData));

                }





            }

        }


        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        String Is_contact_exist = String.valueOf(user_data.getUser().getIs_contact_exist());
        Log.e("is exits",Is_contact_exist);
        Log.e("Global.getcontectexits(sessionManager)",Global.getcontectexits(sessionManager));

        if (Global.getcontectexits(sessionManager).equals("0"))
        {
            if (Is_contact_exist.equals("0"))
            {
                limit = csv_inviteListData.size();
                Log.e("Csv List",""+limit);
                splitdata(csv_inviteListData);
            }
            else {
                // limit = csv_inviteListData.size();
                // splitdata(csv_inviteListData);

                getTasks();

            }
        }
        else {

            getTasks();

        }

     /*   if (SessionManager.getContectList(getApplicationContext()).size() != 0) {
            if(SessionManager.getContectList(getApplicationContext()).get(0).getContacts().size()!=
                    csv_inviteListData.size()){
                limit = csv_inviteListData.size();
                splitdata(csv_inviteListData);
            }else {
                contectListData.addAll(SessionManager.getContectList(getApplicationContext()).get(0).getContacts());
            }
        } else {
            limit = csv_inviteListData.size();
            splitdata(csv_inviteListData);
        }*/
        //limit=csv_inviteListData.size();
        // splitdata(csv_inviteListData);

        cursor.close();

    }


    private void splitdata(List<Csv_InviteListData> response) {

        System.out.println("GET DATA IS " + response);

        // response will have a @ symbol so that we can split individual user data
        //  String res_data[] = response.split("@");
        //StringBuilder  to store the data
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
                    ',' + response.get(i).getNote() +
                    ',' + ' ' +
                    ',' + region + ' ' + response.get(i).getStreet() + ' ' + response.get(i).getCity() +
                    ',' + response.get(i).getCity() +
                    ',' + ' ' +
                    ',' + ' ' +
                    ',' + ' ' +
                    ',' + ' ' +
                    ',' + ' ' +
                    ',' + ' ' +
                    ',' + ' ' +
                    ',' + response.get(i).getContect_email() +
                    ',' + '"' + response.get(i).getUserPhoneNumber() + '"' +
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

          /*  Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            intent.putExtra(Intent.EXTRA_STREAM, path);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Excel Data"));*/
            Uploadcsv(file);
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
        retrofitCalls.Upload_csv(sessionManager, loadingDialog, Global.getToken(sessionManager), organization_id1, team_id1, user_id1, body, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                sessionManager.setcontectexits("1");
                Log.e("Reponse is", new Gson().toJson(response.body()));
                if (response.body().getStatus() == 200) {

                    SignResponseModel user_data = SessionManager.getGetUserdata(getApplicationContext());
                    user_data.getUser().setIs_contact_exist(1);
                    SessionManager.setUserdata(getApplicationContext(),user_data);

                    loadingDialog.cancelLoading();
                    try {
                        limit = csv_inviteListData.size();
                        ContectEvent();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sessionManager.setCsv_token();
                } else {
                    loadingDialog.cancelLoading();
                  /* try {
                        ContectEvent();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    sessionManager.setCsv_token();
                }


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
        Call<ApiResponse> call = registerinfo.Contect_List(RetrofitApiClient.API_Header, token, obj);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                loadingDialog.cancelLoading();

                if (response.body().getStatus() == 200) {

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

                    SetDatainDatabase(contectListData);
                    delete();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Log.e("Error is", throwable.getMessage());
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
    private void ImageSetLight(String imageName) {
        switch (imageName) {
            case "Home":
                llHome.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_home_select));
                llsend.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_chat_icon));
                llContact.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_contacts));
                llUser.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_user));
                break;
            case "Send":
                llHome.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_home));
                llsend.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_chat_icon_select));
                llContact.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_contacts));
                llUser.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_user));
                break;
            case "Contact":
                llHome.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_home));
                llsend.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_chat_icon));
                llContact.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_contacts_selece));
                llUser.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_user));
                break;
            case "User":
                llHome.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_home));
                llsend.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_chat_icon));
                llContact.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_contacts));
                llUser.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_user_select));
                break;

            case "Add":
                llHome.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_home));
                llsend.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_chat_icon));
                llContact.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_contacts));
                llUser.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_user));
                break;

        }
    }

    @Override
    public void onBackPressed() {
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                displayView();
                ImageSetLight("Home");
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

                ImageSetLight("Home");


                break;
            case R.id.llsend:
                navItemIndex = 1;
                displayView();

                ImageSetLight("Send");


                break;
            case R.id.llContact:
                navItemIndex = 2;
                displayView();

                ImageSetLight("Contact");


                break;
            case R.id.llUser:
                navItemIndex = 3;
                displayView();

                ImageSetLight("User");

                break;
            case R.id.llCreate:
                navItemIndex = 4;
                displayView();

                ImageSetLight("Add");
                break;

        }

    }

    public void displayView() {

        Fragment fragment = null;
        switch (navItemIndex) {
            case 0:

                fragment = new Home_Main_Fragment();
                shouldLoadHomeFragOnBackPress=false;
                break;
            case 1:
                fragment = new Send_Main_Fragment();
                shouldLoadHomeFragOnBackPress=true;
                break;
            case 2:
                fragment = new Contect_main_Fragment();
                shouldLoadHomeFragOnBackPress=true;
                break;
            case 3:
                fragment = new UserProfile_Main_Fragment();
                shouldLoadHomeFragOnBackPress=true;
                break;
            case 4:
                Log.e("Brodcaste Call","Yes");
                SessionManager.setgroup_broadcste(getApplicationContext(),new ArrayList<>());
                SessionManager.setContectList_broadcste(getApplicationContext(),new ArrayList<>());
                Broadcast_Data broadcast_data=new Broadcast_Data();
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
        TextView selected_broadcast = bottomSheetDialog.findViewById(R.id.selected_broadcast);
        selected_broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Broadcst_Activty.class);
                startActivity(intent);
                //finish();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();

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

                Log.e("ContectList", String.valueOf(contect_list.size()));
                Log.e("Store", String.valueOf(csv_inviteListData.size()));
                if (contect_list.size()==csv_inviteListData.size())
                {

                    Log.e("Same Data","Yes");

                }
                else {
                    Log.e("Same Data","No");
                    getUser_check(csv_inviteListData);
                }

                super.onPostExecute(contect_list);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void getUser_check(List<Csv_InviteListData> csv_inviteListData) {
        for (int i=0;i<csv_inviteListData.size();i++)
        {
            check_list(csv_inviteListData.get(i).getUserName(),csv_inviteListData.get(i).getLast_name(),csv_inviteListData.get(i).getUserPhoneNumber());
        }



    }
    public void check_list(String userName, String last_name, String userPhoneNumber)
    {
        class GetTasks extends AsyncTask<Void, Void, List<Contect_Db>> {
            @Override
            protected List<Contect_Db> doInBackground(Void... voids) {
                List<Contect_Db> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getSameValue(userName,last_name,userPhoneNumber);
                if (taskList.size()==0)
                {
                    List<Csv_InviteListData> csv_inviteListData1=new ArrayList<>();
                    csv_inviteListData1.add(new Csv_InviteListData(userName,userPhoneNumber,"","","","","","",last_name));
                    splitdata(csv_inviteListData1);
                }
                else if (taskList.size()!=1)
                {
                    delete();
                }
               else if (taskList.size()==1)
                {
                    Log.e("Name is ",userName+" "+last_name+" "+userPhoneNumber);
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


    public void delete() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .deleteDuplicates();
                //.DeleteData(inviteListData.getUserPhoneNumber());
                // .RemoveData();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                 Log.e("Delete Task","Yes");
                super.onPostExecute(aVoid);

            }
        }

        DeleteTask ut = new DeleteTask();
        ut.execute();
    }

    /*
        private void getdataanme_mobile(InviteListData inser_data) {
            // c=c+1;
            // Log.e("C is ", String.valueOf(c));

            class GetTasks extends AsyncTask<Void, Void, List<InviteListData>> {
                @Override
                protected List<InviteListData> doInBackground(Void... voids) {
                    List<InviteListData> taskList = DatabaseClient
                            .getInstance(getApplicationContext())
                            .getAppDatabase()
                            .taskDao()
                            .getTaskUpdate(inser_data.getUserPhoneNumber(), inser_data.getUserName());

                    return taskList;
                }

                @Override
                protected void onPostExecute(List<InviteListData> tasks) {

                    if (tasks.size() == 0) {
                        inser_data.setFlag("Update");
                        getdataanme_mobile1(inser_data);
                        Log.e("Event Update ", "Call");
                    }
                    //One more then Remove Contect
                    else if (tasks.size() < 2) {
                       *//* boolean found = tasks.stream().anyMatch(p -> p.getUserPhoneNumber().equals(inser_data.getUserPhoneNumber()));
                    if (found)
                    {

                    }*//*
                    delete();

                }


                super.onPostExecute(tasks);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }


    private void getTasks1(InviteListData inser_data) {
        class GetTasks extends AsyncTask<Void, Void, List<InviteListData>> {
            @Override
            protected List<InviteListData> doInBackground(Void... voids) {
                List<InviteListData> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getTask(inser_data.getUserPhoneNumber());

                return taskList;
            }

            @Override
            protected void onPostExecute(List<InviteListData> tasks) {
                // Log.e("Insert Task list Size ", String.valueOf(tasks.size()));
                if (tasks.size() == 0) {
                    SetDatainDatabase(inser_data);
                }

                super.onPostExecute(tasks);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void getdataanme_mobile1(InviteListData inser_data) {

        class GetTasks extends AsyncTask<Void, Void, List<InviteListData>> {
            @Override
            protected List<InviteListData> doInBackground(Void... voids) {
                List<InviteListData> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getTaskUpdate1(inser_data.getUserPhoneNumber(), inser_data.getUserName());

                return taskList;
            }

            @Override
            protected void onPostExecute(List<InviteListData> tasks) {
                Log.e("Update Data Event ", "CAll" + tasks.size());
                if (tasks.size() == 0) {
                    inser_data.setFlag("Add");
                    Log.e("Insert Flag", inser_data.getFlag());
                    SetDatainDatabase(inser_data);
                } else {
                    inser_data.setFlag("Update");
                    updatedata(inser_data);
                }


                super.onPostExecute(tasks);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public void delete() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .deleteDuplicates();
                //.DeleteData(inviteListData.getUserPhoneNumber());
                // .RemoveData();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //   Log.e("Delete Task","Yes"+c);
                super.onPostExecute(aVoid);

            }
        }

        DeleteTask ut = new DeleteTask();
        ut.execute();
    }*/
    private void SetDatainDatabase(List<ContectListData.Contact> contectListData) {


        for (int i=0;i<contectListData.size();i++)
        {
            Contect_Db contect_db=new Contect_Db();
            for (int j=0;j<contectListData.get(i).getContactDetails().size();j++)
            {
                ContectListData.Contact.ContactDetail data=contectListData.get(i).getContactDetails().get(j);
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
            save_data(contect_db);
        }


    }

    public void save_data(Contect_Db contect_db)
    {
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
                Log.e("Insert Data","Yes");
                super.onPostExecute(aVoid);
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    /*public void updatedata(InviteListData inviteListData) {
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .updatevalue(inviteListData.getUserName(), inviteListData.getUserPhoneNumber(), inviteListData.getFlag());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }*/
}