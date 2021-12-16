package com.contactninja.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Addnewcontect_Activity;
import com.contactninja.ContectListAdapter;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.Csv_InviteListData;
import com.contactninja.Model.InviteListData;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.DatabaseClient;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitApiClient;
import com.contactninja.retrofit.RetrofitApiInterface;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContectFragment extends Fragment {

    public static final int RequestPermissionCode = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private final static String[] DATA_COLS = {

            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.DATA1,//phone number
            ContactsContract.Data.CONTACT_ID
    };
    public static UserListDataAdapter userListDataAdapter;
    public static ArrayList<InviteListData> inviteListData = new ArrayList<>();
    ConstraintLayout mMainLayout;
    Context mCtx;
    Cursor cursor;
    RecyclerView rvinviteuserdetails;
    String userName = "", user_phone_number = "", user_image = "", user_des = "", strtext = "", old_latter = "", contect_type = "", contect_email = "",
            contect_type_work = "", email_type_home = "", email_type_work = "", country = "", city = "", region = "", street = "",
            postcode = "", postType = "", note = "";
    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    // ImageView iv_back,iv_more;
    SearchView contect_search;
    TextView add_new_contect, num_count;
    Handler mHandler = new Handler();
    ImageView add_new_contect_icon;
    View view1;
    FragmentActivity fragmentActivity;
    LinearLayout add_new_contect_layout;
    int c = 0;
    LoadingDialog loadingDialog;
    StringBuilder data;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    int page = 1, limit = 150, totale_group;
    ContectListAdapter paginationAdapter;
    int currentPage = 1, TOTAL_PAGES = 10;
    boolean isLoading = false;
    boolean isLastPage = false;
    LinearLayoutManager layoutManager;
    List<Csv_InviteListData> csv_inviteListData;
    private List<ContectListData.Contact> contectListData;


    public ContectFragment(String strtext, View view, FragmentActivity activity) {

        this.strtext = strtext;
        this.view1 = view;
        this.fragmentActivity = activity;
        // Log.e("View is ", String.valueOf(view1.getVisibility()));
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

                Log.d("Update Result", result.toString());
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
        csv_inviteListData = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        rvinviteuserdetails.setLayoutManager(layoutManager);
        rvinviteuserdetails.setHasFixedSize(true);
        contectListData = new ArrayList<>();
        SessionManager.setOneCotect_deatil(getActivity(), new ContectListData.Contact());
        GetContactsIntoArrayList();




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

        fastscroller.setupWithRecyclerView(
                rvinviteuserdetails,
                (position) -> {
                    FastScrollItemIndicator fastScrollItemIndicator = new FastScrollItemIndicator.Text(
                            inviteListData.get(position).getUserName().substring(0, 1)
                                    .substring(0, 1)
                                    .toUpperCase()// Grab the first letter and capitalize it
                    );
                    return fastScrollItemIndicator;
                }
        );

        //  getAllContect();
        add_new_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnewcontect = new Intent(getActivity(), Addnewcontect_Activity.class);
                SessionManager.setContect_flag("save");
                startActivity(addnewcontect);
            }
        });
        add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnewcontect = new Intent(getActivity(), Addnewcontect_Activity.class);
                SessionManager.setContect_flag("save");
                startActivity(addnewcontect);
            }
        });
        add_new_contect_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.setAdd_Contect_Detail(getActivity(), new AddcontectModel());
                SessionManager.setContect_flag("save");
                Intent addnewcontect = new Intent(getActivity(), Addnewcontect_Activity.class);
                startActivity(addnewcontect);

            }
        });

/*

        rvinviteuserdetails.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.e("Load More Call", "yes" + currentPage);
                isLoading = true;
                currentPage += 1;
                page +=1;
                try {
                    ContectEventnext();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

*/



        rvinviteuserdetails.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItem = layoutManager.getChildCount();
                int totalItem = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (!isLoading && !isLastPage) {
                    if ((visibleItem + firstVisibleItemPosition) >= totalItem && firstVisibleItemPosition >= 0 && totalItem >= currentPage) {
                        try {
                            currentPage=currentPage + 1;
                            ContectEventnext();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
      ///  EnableRuntimePermission();
        return content_view;

    }

    void filter(String text, View view, FragmentActivity activity) {


        if (!text.equals("")) {
            List<InviteListData> temp = new ArrayList();
            for (InviteListData d : inviteListData) {
                if (d.getUserName().contains(text)) {
                    temp.add(d);
                    // Log.e("Same Data ",d.getUserName());
                }
            }

        }

    }

    private void IntentUI(View content_view) {
        mMainLayout = content_view.findViewById(R.id.mMainLayout);
        rvinviteuserdetails = content_view.findViewById(R.id.contect_list);
        fastscroller = content_view.findViewById(R.id.fastscroller);
        fastscroller_thumb = content_view.findViewById(R.id.fastscroller_thumb);
        // iv_back=content_view.findViewById(R.id.iv_back);
        //iv_more=content_view.findViewById(R.id.iv_more);
        contect_search = content_view.findViewById(R.id.contect_search);
        add_new_contect = content_view.findViewById(R.id.add_new_contect);
        num_count = content_view.findViewById(R.id.num_count);
        add_new_contect_icon = content_view.findViewById(R.id.add_new_contect_icon);
        add_new_contect_layout = content_view.findViewById(R.id.add_new_contect_layout);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void GetContactsIntoArrayList() {
        String firstname = "", lastname = "";
        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
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
                Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(String.valueOf(getId())));
                String contactID = String.valueOf(Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY));
                inviteListData.add(new InviteListData("" + userName.trim(),
                        user_phone_number,
                        user_image,
                        user_des,
                        "", ""));
                //  userListDataAdapter.notifyDataSetChanged();

                try {
                    csv_inviteListData.add(new Csv_InviteListData("" + userName, user_phone_number, contect_email, note, country, city, region, street, "" + lastname));
                } catch (Exception e) {

                }

                getTasks(new InviteListData(userName, user_phone_number, user_image, user_des, old_latter, ""));


            }

        }

        if (sessionManager.getCsv_token())
        {
            //splitdata(csv_inviteListData);
            try {
                ContectEvent();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
           /* try {
                ContectEvent();
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            splitdata(csv_inviteListData);
        }


        cursor.close();

    }

    private void splitdata(List<Csv_InviteListData> response) {
        System.out.println("GET DATA IS " + response);

        // response will have a @ symbol so that we can split individual user data
        //  String res_data[] = response.split("@");
        //StringBuilder  to store the data
        data = new StringBuilder();

        data.append("Firstname,Lastname," +
                "Company Name,Company URL," +
                "Job Title,Notes," +
                "DOB,Address," +
                "City,State," +
                "Zipcode,Zoomid," +
                "Facebook Link,Twitter Link," +
                "Breakout Link,Linkedin Link," +
                "Email,Phone," +
                "Fax");

        for (int i = 0; i < response.size(); i++) {




            data.append("\n" + response.get(i).getUserName() +
                    "," + response.get(i).getLast_name() +
                    "," + "" +
                    "," + "" +
                    "," + "" +
                    "," + response.get(i).getNote() +
                    "," + "" +
                    "," + region + "" + response.get(i).getStreet() + " " + response.get(i).getCity() +
                    "," + response.get(i).getCity() +
                    "," + "" +
                    "," + "" +
                    "," + "" +
                    "," + "" +
                    "," + "" +
                    "," + "" +
                    "," + "" +
                    "," + response.get(i).getContect_email() +
                    "," + response.get(i).getUserPhoneNumber() +
                    "," + ""
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

     /*       Intent intent = new Intent(Intent.ACTION_SEND);
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

    public void EnableRuntimePermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }

        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionlistener)
                .setDeniedTitle("Contactninja would like to access your contacts")
                .setDeniedMessage("Contact Ninja uses your contacts to improve your business’s marketing outreach by aggrregating your contacts.")
                .setGotoSettingButtonText("setting")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .setRationaleConfirmText("OK")
                .check();


    }

    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] PResult) {

        super.onRequestPermissionsResult(RC, per, PResult);
        if (RC == RequestPermissionCode) {
            if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Global.Messageshow(getActivity(), mMainLayout, "Permission Canceled, Now your application cannot access CONTACTS", false);
                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", getActivity().getPackageName(), null)));
                // Toast.makeText(MainActivity.this, "Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void update(String strtext1, View view, FragmentActivity activity) {
        filter(strtext1, view, activity);

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
                if (tasks.size() == 0) {
                    loadingDialog.cancelLoading();
                    getAllContect();
                } else {
                    loadingDialog.cancelLoading();
                    num_count.setText(tasks.size() + " Contacts");
                  /*  userListDataAdapter = new UserListDataAdapter(getActivity(), getActivity(), (ArrayList<InviteListData>) tasks);
                    rvinviteuserdetails.setAdapter(userListDataAdapter);
                    userListDataAdapter.notifyDataSetChanged();*/
                    super.onPostExecute(tasks);
                }

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void getTasks(InviteListData inser_data) {


        class GetTasks extends AsyncTask<Void, Void, List<InviteListData>> {
            @Override
            protected List<InviteListData> doInBackground(Void... voids) {
                List<InviteListData> taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .taskDao()
                        .getvalue();

                return taskList;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void onPostExecute(List<InviteListData> tasks) {

                // if (tasks.size()==inviteListData.size()) {
                // Log.e("Size is Same ","Yse");
                boolean found = false;
                try {
                    found = tasks.stream().anyMatch(p -> p.getUserPhoneNumber().equals(inser_data.getUserPhoneNumber()));

                } catch (Exception e) {

                }
                if (found) {
                    if (tasks.size() == 1) {

                    } else {
                        getdataanme_mobile(inser_data);
                    }

                } else {
                    inser_data.setFlag("Add");
                    getTasks1(inser_data);

                }
                // }
             /*   else {
                    boolean found = tasks.stream().anyMatch(p -> p.getUserPhoneNumber().equals(inser_data.getUserPhoneNumber()));
                    if (found) {
                        delete();
                    }
                    else {
                        getTasks1(inser_data);
                    }


                }*/
                super.onPostExecute(tasks);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public void updatedata(InviteListData inviteListData) {
        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getContext()).getAppDatabase()
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
    }

    public void delete() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getContext()).getAppDatabase()
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
    }

    private void SetDatainDatabase(InviteListData inviteListData) {


        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .taskDao()
                        .insert(inviteListData);
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

    private void getTasks1(InviteListData inser_data) {
        class GetTasks extends AsyncTask<Void, Void, List<InviteListData>> {
            @Override
            protected List<InviteListData> doInBackground(Void... voids) {
                List<InviteListData> taskList = DatabaseClient
                        .getInstance(getActivity())
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

    private void getdataanme_mobile(InviteListData inser_data) {
        // c=c+1;
        // Log.e("C is ", String.valueOf(c));

        class GetTasks extends AsyncTask<Void, Void, List<InviteListData>> {
            @Override
            protected List<InviteListData> doInBackground(Void... voids) {
                List<InviteListData> taskList = DatabaseClient
                        .getInstance(getActivity())
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
                   /* boolean found = tasks.stream().anyMatch(p -> p.getUserPhoneNumber().equals(inser_data.getUserPhoneNumber()));
                    if (found)
                    {

                    }*/
                    delete();

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
                        .getInstance(getActivity())
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

    private void ContectEvent() throws JSONException {
        loadingDialog.showLoadingDialog();

        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        String token = Global.getToken(getActivity());
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("page", currentPage);
        paramObject.addProperty("perPage", limit);
        paramObject.addProperty("status", "A");
        paramObject.addProperty("q", "");
        paramObject.addProperty("orderBy","firstname");
        paramObject.addProperty("order","asc");
        obj.add("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        RetrofitApiInterface registerinfo = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<ApiResponse> call = registerinfo.Contect_List(RetrofitApiClient.API_Header, token, obj);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                Log.e("Reponse is", new Gson().toJson(response.body()));
                if (response.body().getStatus()==200) {

                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<ContectListData>() {
                    }.getType();
                    ContectListData contectListData1 = new Gson().fromJson(headerString, listType);
                    contectListData.addAll(contectListData1.getContacts());
                    paginationAdapter.addAll(contectListData);
                    if (contectListData1.getContacts().size() == limit) {
                        if (currentPage <= TOTAL_PAGES)
                        {
                            paginationAdapter.addLoadingFooter();
                        }
                        else isLastPage = true;
                    } else {
                        isLastPage = true;
                        isLoading = false;

                    }

                    num_count.setText("" + contectListData1.getTotal() + " Contacts");

                    totale_group = contectListData1.getTotal();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Log.e("Error is", throwable.getMessage());
                loadingDialog.cancelLoading();

            }
        });


    }

    private void ContectEventnext() throws JSONException {


        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());
        String token = Global.getToken(getActivity());
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", "1");
        paramObject.addProperty("team_id", "1");
        paramObject.addProperty("user_id", user_id);
        paramObject.addProperty("page", currentPage);
        paramObject.addProperty("perPage", limit);
        paramObject.addProperty("status", "");
        paramObject.addProperty("q", "");
        paramObject.addProperty("orderBy","firstname");
        paramObject.addProperty("order","asc");
        obj.add("data", paramObject);

        Log.e("Request data",new Gson().toJson(obj));


        RetrofitApiInterface registerinfo = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<ApiResponse> call = registerinfo.Contect_List(RetrofitApiClient.API_Header, token, obj);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                paginationAdapter.removeLoadingFooter();
                if (response.body().getStatus()==200) {


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
        retrofitCalls.Upload_csv(sessionManager,loadingDialog, Global.getToken(getActivity()), organization_id1, team_id1, user_id1, body, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                Log.e("Reponse is", new Gson().toJson(response.body()));
                if (response.body().getStatus()==200)
                {
                    loadingDialog.cancelLoading();
                    try {
                        ContectEvent();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sessionManager.setCsv_token();
                }
                else {
                    loadingDialog.cancelLoading();
                    try {
                        ContectEvent();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sessionManager.setCsv_token();
                }


            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }

        });


    }

    @Override
    public void onResume() {
        SessionManager.setAdd_Contect_Detail(getActivity(), new AddcontectModel());
        SessionManager.setOneCotect_deatil(getActivity(), new ContectListData.Contact());

        super.onResume();

    }

    public static class UserListDataAdapter extends RecyclerView.Adapter<UserListDataAdapter.InviteListDataclass>
            implements Filterable {

        private final Context mcntx;
        private final List<InviteListData> userDetailsfull;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<InviteListData> userDetails;
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
        public InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.invite_user_details, parent, false);
            return new InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InviteListDataclass holder, int position) {
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

            if (inviteUserDetails.getUserImageURL() == null) {
                String name = inviteUserDetails.getUserName();
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
                holder.profile_image.setVisibility(View.GONE);
            } else {
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


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                first_latter = itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.top_layout);
            }

        }

    }

   /* public class ContectListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int LOADING = 0;
        private static final int ITEM = 1;
        private final Context context;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<ContectListData.Contact> contacts;
        private boolean isLoadingAdded = false;

        public ContectListAdapter(Context context) {
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
                    viewHolder = new ContectListAdapter.MovieViewHolder(viewItem);
                    break;
                case LOADING:
                    View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                    viewHolder = new ContectListAdapter.LoadingViewHolder(viewLoading);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ContectListData.Contact Contact_data = contacts.get(position);
            Log.e("Postion is",String.valueOf(position));
            switch (getItemViewType(position)) {
                case ITEM:
                    ContectListAdapter.MovieViewHolder holder1 = (ContectListAdapter.MovieViewHolder) holder;
                    holder1.userName.setText(Contact_data.getFirstname());
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

                        }


                        holder1.no_image.setText(add_text);
                        holder1.no_image.setVisibility(View.VISIBLE);
                        holder1.profile_image.setVisibility(View.GONE);
                    } else {
                        Glide.with(mCtx).
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
                            SessionManager.setAdd_Contect_Detail(getActivity(), new AddcontectModel());
                            SessionManager.setOneCotect_deatil(getActivity(), Contact_data);
                            Intent addnewcontect = new Intent(getActivity(), Addnewcontect_Activity.class);
                            SessionManager.setContect_flag("read");
                            startActivity(addnewcontect);
                        }
                    });

                    break;

                case LOADING:
                    ContectListAdapter.LoadingViewHolder loadingViewHolder = (ContectListAdapter.LoadingViewHolder) holder;
                    loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
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
        }

        public void addAll(List<ContectListData.Contact> contact) {
            for (ContectListData.Contact result : contact) {

                Log.e("Result is ",new Gson().toJson(result));
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
            LinearLayout top_layout;
            RelativeLayout main_layout;

            public MovieViewHolder(View itemView) {
                super(itemView);
                first_latter = itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.top_layout);
                main_layout = itemView.findViewById(R.id.main_layout);
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
*/
    public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        public PaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading() && !isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    loadMoreItems();
                }
            }
        }

        protected abstract void loadMoreItems();

        public abstract boolean isLastPage();

        public abstract boolean isLoading();

    }


}

