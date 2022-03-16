package com.contactninja.AddContect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.contactninja.Fragment.AddContect_Fragment.ExposuresFragment;
import com.contactninja.Fragment.AddContect_Fragment.InformationFragment;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UservalidateModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.Utils.YourFragmentInterface;
import com.contactninja.aws.image_aws.AmazonUtil;
import com.contactninja.aws.image_aws.S3Uploader;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class Add_Newcontect_Activity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener, YourFragmentInterface {
    S3Uploader s3uploaderObj;
    private long mLastClickTime = 0;
    Integer CAPTURE_IMAGE = 3;
    String urlFromS3 = null;
    String Validation="";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    int image_flag=1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    public static final int RequestPermissionCode = 1;
    private static final String TAG_HOME = "Addcontect";
    public static String CURRENT_TAG = TAG_HOME;
    ImageView iv_back, iv_Setting, pulse_icon;
    TextView save_button, tv_nameLetter;
    TabLayout tabLayout;
    Uri mCapturedImageURI;
    String fragment_name = "", user_image_Url = "", File_name = "", File_extension = "";
    EditText edt_FirstName, edt_lastname;
    SessionManager sessionManager;
    String phone, phone_type, email, email_type,
            address, zip_code, zoom_id, note, f_name, l_name,
            city, state, olld_image = "";
    LinearLayout mMainLayout;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    FrameLayout frameContainer;
    CircleImageView iv_user;
    LinearLayout layout_pulse;
    String option_type = "";
    private BroadcastReceiver mNetworkReceiver;
    ImageView iv_toolbar_manu_vertical, iv_block;
    String filePath1="";


    // ListPhoneContactsActivity use this method to start this activity.
    public static void start(Context context) {
        Intent intent = new Intent(context, Add_Newcontect_Activity.class);
        SessionManager.setContect_flag("save");
        context.startActivity(intent);
    }

    // function to check permission
    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public static String encodeFileToBase64Binary(String str) {
        try {
            return new String(Base64.decode(str, 0), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException unused) {
            return "";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewcontect);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();

        s3uploaderObj = new S3Uploader(this);
        sessionManager = new SessionManager(this);
        loadingDialog = new LoadingDialog(this);
        Global.checkConnectivity(Add_Newcontect_Activity.this, mMainLayout);
        sessionManager = new SessionManager(this);
        save_button.setText("Save Contact");
        option_type = "save";
        String flag = sessionManager.getContect_flag(this);




        if (flag.equals("edit")) {
            ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(this);
            edt_FirstName.setText(Contect_data.getFirstname().toString().trim());
            edt_lastname.setText(Contect_data.getLastname().toString().trim());
            f_name = Contect_data.getFirstname().toString().trim();
            l_name = Contect_data.getLastname().toString().trim();
            iv_user.setOnClickListener(this);
            if (Contect_data.getIs_blocked().equals(1)) {
                iv_block.setVisibility(View.VISIBLE);
            } else {
                iv_block.setVisibility(View.GONE);
            }
            if (Contect_data.getContactImage() == null || Contect_data.getContactImage().equals("")) {
                iv_user.setVisibility(View.GONE);

                layout_pulse.setVisibility(View.VISIBLE);
                pulse_icon.setVisibility(View.GONE);
                tv_nameLetter.setVisibility(View.VISIBLE);
                tv_nameLetter.setOnClickListener(this);

                String name = Contect_data.getFirstname();
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
                tv_nameLetter.setText(add_text);

            } else {
                iv_user.setVisibility(View.VISIBLE);
                layout_pulse.setVisibility(View.GONE);
                Log.e("Image is",Contect_data.getContactImage());
                Glide.with(getApplicationContext()).
                        load(Contect_data.getContactImage())
                        .placeholder(R.drawable.shape_primary_back)
                        .error(R.drawable.shape_primary_back).
                        into(iv_user);
            }
            user_image_Url = Contect_data.getContactImage();
            save_button.setText("Save Contact");



        } else if (flag.equals("read")) {
            edt_FirstName.setEnabled(false);
            edt_lastname.setEnabled(false);
            iv_user.setOnClickListener(null);

            ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(this);
            edt_FirstName.setText(Contect_data.getFirstname().toString().trim());
            edt_lastname.setText(Contect_data.getLastname().toString().trim());
            f_name = Contect_data.getFirstname().toString().trim();
            l_name = Contect_data.getLastname().toString().trim();
            if (Contect_data.getIs_blocked().equals(1)) {
                iv_block.setVisibility(View.VISIBLE);
            } else {
                iv_block.setVisibility(View.GONE);
            }
            if (Contect_data.getContactImage() == null || Contect_data.getContactImage().equals("")) {
                iv_user.setVisibility(View.GONE);
                layout_pulse.setVisibility(View.VISIBLE);
                pulse_icon.setVisibility(View.GONE);
                tv_nameLetter.setVisibility(View.VISIBLE);
                String name = Contect_data.getFirstname();
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
                tv_nameLetter.setText(add_text);

            } else {
                iv_user.setVisibility(View.VISIBLE);
                layout_pulse.setVisibility(View.GONE);
                Log.e("Image is",Contect_data.getContactImage());
                Glide.with(getApplicationContext()).
                        load(Contect_data.getContactImage())
                        .placeholder(R.drawable.shape_primary_back)
                        .error(R.drawable.shape_primary_back).
                        into(iv_user);
            }
            user_image_Url = Contect_data.getContactImage();

            save_button.setText("Edit Contact");
        } else {
            iv_toolbar_manu_vertical.setVisibility(View.GONE);
            Log.e("Null", "No Call");
        }
        retrofitCalls = new RetrofitCalls(this);
        // loadingDialog = new LoadingDialog(this);
        //Set Viewpagger
        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        //tabLayout.addTab(tabLayout.newTab().setText("Bzcard"));
        tabLayout.addTab(tabLayout.newTab().setText("Exposures"));

        fragment_name = "Info";

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        Fragment fragment = new InformationFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment, "Fragment");
        fragmentTransaction.commitAllowingStateLoss();
        if (flag.equals("edit"))
        {

            LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
            for(int i = 0; i < tabStrip.getChildCount(); i++) {
                tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
            }

        }
        else if (flag.equals("save"))
        {

            LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
            for(int i = 0; i < tabStrip.getChildCount(); i++) {
                tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
            }

        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        if (flag.equals("edit"))
                        {
                            LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
                            for(int i = 0; i < tabStrip.getChildCount(); i++) {
                                tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        return true;
                                    }
                                });
                            }
                        }
                        else if (flag.equals("save"))
                        {
                            LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
                            for(int i = 0; i < tabStrip.getChildCount(); i++) {
                                tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                        return true;
                                    }
                                });
                            }
                        }
                        fragment = new InformationFragment();
                      break;
                    case 1:
                        fragment = new ExposuresFragment();

                        break;

                }
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameContainer, fragment, "Fragment");
                    fragmentTransaction.commitAllowingStateLoss();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pulse_icon.setColorFilter(getResources().getColor(R.color.purple_200));

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                onBackPressed();
            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                AddcontectModel addcontectModel = SessionManager.getAdd_Contect_Detail(getApplicationContext());
                zip_code = addcontectModel.getZip_code();
                zoom_id = addcontectModel.getZoom_id();
                address = addcontectModel.getAddress();
                note = addcontectModel.getNote();
                f_name = edt_FirstName.getText().toString();
                l_name = edt_lastname.getText().toString();
                if (sessionManager.getContect_flag(getApplicationContext()).equals("edit")) {
                    if (f_name.equals("")) {
                        Global.Messageshow(getApplicationContext(), mMainLayout, getString(R.string.invalid_first_name), false);

                    } else {

                            if (Global.isNetworkAvailable(Add_Newcontect_Activity.this, mMainLayout)) {
                                //AddContect_Update();


                                AddcontectModel save_data = SessionManager.getAdd_Contect_Detail(getApplicationContext());
                                List<Contactdetail> contect_list=save_data.getContactdetails();
                                List<Contactdetail> email_list=save_data.getContactdetails_email();

                                if (email_list.size()==0 && contect_list.size()==0)
                                {

                                    Global.Messageshow(getApplicationContext(),mMainLayout,"Add Contect Or Emaail",false);
                                }
                                else {
                                    for (int i=0;i<contect_list.size();i++)
                                    {
                                        if (contect_list.get(i).getEmail_number().equals(""))
                                        {
                                            contect_list.remove(i);
                                        }
                                    }

                                    for (int i=0;i<email_list.size();i++)
                                    {
                                        if (email_list.get(i).getEmail_number().equals(""))
                                        {
                                            email_list.remove(i);
                                        }
                                    }
                                    save_data.setContactdetails(contect_list);
                                    save_data.setContactdetails_email(email_list);
                                    SessionManager.setAdd_Contect_Detail(getApplicationContext(),save_data);
                                    uploadImageTos3(filePath1,"update");
                                }

                            }


                    }

                } else if (save_button.getText().toString().equals("Save Contact")) {
                    //Add Contect.
                    if (f_name.equals("")) {
                        Global.Messageshow(getApplicationContext(), mMainLayout, getString(R.string.invalid_first_name), false);

                    } else if (addcontectModel.getContactdetails().size() == 0) {

                        Global.Messageshow(getApplicationContext(), mMainLayout, getString(R.string.enter_phone), false);

                    } else {


                            if (Global.isNetworkAvailable(Add_Newcontect_Activity.this, mMainLayout)) {


                                AddcontectModel save_data = SessionManager.getAdd_Contect_Detail(getApplicationContext());
                                List<Contactdetail> contect_list=save_data.getContactdetails();
                                List<Contactdetail> email_list=save_data.getContactdetails_email();

                                if (email_list.size()==0 && contect_list.size()==0)
                                {

                                    Global.Messageshow(getApplicationContext(),mMainLayout,"Add Contect Or Emaail",false);
                                }
                                else {
                                    for (int i = 0; i < contect_list.size(); i++) {
                                        if (contect_list.get(i).getEmail_number().equals("")) {
                                            contect_list.remove(i);
                                        }
                                    }

                                    for (int i = 0; i < email_list.size(); i++) {
                                        if (email_list.get(i).getEmail_number().equals("")) {
                                            email_list.remove(i);
                                        }
                                    }
                                    save_data.setContactdetails(contect_list);
                                    save_data.setContactdetails_email(email_list);
                                    SessionManager.setAdd_Contect_Detail(getApplicationContext(), save_data);
                                    uploadImageTos3(filePath1,"add");
                                }


                                //AddContect_Api();
                            }


                    }


                } else {


                    String flag = sessionManager.getContect_flag(getApplicationContext());
                    if (flag.equals("read")) {
                        SessionManager.setContect_flag("edit");
                        Intent addnewcontect = new Intent(getApplicationContext(), Add_Newcontect_Activity.class);
                        startActivity(addnewcontect);
                        finish();
                    } else {
                        SessionManager.setContect_flag("edit");
                        save_button.setText("Save Contact");
                        edt_FirstName.setEnabled(true);
                        edt_lastname.setEnabled(true);
                            if (Global.isNetworkAvailable(Add_Newcontect_Activity.this, mMainLayout)) {
                                AddcontectModel save_data = SessionManager.getAdd_Contect_Detail(getApplicationContext());
                                List<Contactdetail> contect_list=save_data.getContactdetails();
                                List<Contactdetail> email_list=save_data.getContactdetails_email();

                                if (email_list.size()==0 && contect_list.size()==0)
                                {

                                    Global.Messageshow(getApplicationContext(),mMainLayout,"Add Contect Or Emaail",false);
                                }
                                else {
                                    for (int i = 0; i < contect_list.size(); i++) {
                                        if (contect_list.get(i).getEmail_number().equals("")) {
                                            contect_list.remove(i);
                                        }
                                    }

                                    for (int i = 0; i < email_list.size(); i++) {
                                        if (email_list.get(i).getEmail_number().equals("")) {
                                            email_list.remove(i);
                                        }
                                    }
                                    save_data.setContactdetails(contect_list);
                                    save_data.setContactdetails_email(email_list);
                                    SessionManager.setAdd_Contect_Detail(getApplicationContext(), save_data);
                                    uploadImageTos3(filePath1, "update");
                                }
                            }

                    }


                }
            }
        });

        edt_FirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sessionManager.setContect_Name(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sessionManager.setContect_Type(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void IntentUI() {
        iv_block = findViewById(R.id.iv_block);
        iv_toolbar_manu_vertical = findViewById(R.id.iv_toolbar_manu_vertical);
        iv_toolbar_manu_vertical.setVisibility(View.VISIBLE);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.GONE);
        tabLayout = findViewById(R.id.tabLayout);
        frameContainer = findViewById(R.id.frameContainer);
        pulse_icon = findViewById(R.id.pulse_icon);
        edt_lastname = findViewById(R.id.edt_lastname);
        edt_FirstName = findViewById(R.id.edt_FirstName);
        mMainLayout = findViewById(R.id.frameContainer1);
        layout_pulse = findViewById(R.id.layout_pulse);
        tv_nameLetter = findViewById(R.id.tv_nameLetter);

        iv_user = findViewById(R.id.iv_user);
        pulse_icon.setOnClickListener(this);
        iv_user.setOnClickListener(this);
        iv_toolbar_manu_vertical.setOnClickListener(this);


    }


    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] PResult) {

        super.onRequestPermissionsResult(RC, per, PResult);
        if (RC == RequestPermissionCode) {
            if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Global.Messageshow(getApplicationContext(), mMainLayout, "Permission Canceled, Now your application cannot access CONTACTS", false);
                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", getPackageName(), null)));
            }
        }
        if (RC == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(Add_Newcontect_Activity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "Requires Access to Camara.", Toast.LENGTH_SHORT)
                        .show();
            } else if (ContextCompat.checkSelfPermission(Add_Newcontect_Activity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "Requires Access to Your Storage.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addContact(String given_name, String name, String mobile, String home, String email, String note) {

        Log.e("Name", name);
        Log.e("mobile", mobile);
        Log.e("home", home);
        Log.e("email", email);
        Log.e("Note", note);

        ArrayList<ContentProviderOperation> contact = new ArrayList<ContentProviderOperation>();
        contact.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // first and last names
        contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, given_name)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, name)
                .build());

        // Contact No Mobile
        contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobile)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        // Contact Home
        contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, home)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());

        // Email    `
        contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());


        try {
            ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, contact);
            Log.e("Contect Result", new Gson().toJson(results));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getRawContactId() {
        // Inser an empty contact.
        try {
            ContentValues contentValues = new ContentValues();
            Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
            // Get the newly created contact raw id.
            long ret = ContentUris.parseId(rawContactUri);
            return ret;
        }
        catch (Exception e)
        {
            return  0;
        }

    }

    private void insertContactDisplayName(Uri addContactsUri, long rawContactId, String displayName) {

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
            contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            // Put contact display name value.
            contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName);
            getContentResolver().insert(addContactsUri, contentValues);

        }
        catch (Exception e)
        {

        }
    }

    private void insertContactPhoneNumber(Uri addContactsUri, long rawContactId, String phoneNumber, String phoneTypeStr) {
        // Create a ContentValues object.
        ContentValues contentValues = new ContentValues();
        // Each contact must has an id to avoid java.lang.IllegalArgumentException: raw_contact_id is required error.
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // Each contact must have a mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        // Put phone number value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        // Calculate phone type by user selection.
        int phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        if ("home".equalsIgnoreCase(phoneTypeStr)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        } else if ("mobile".equalsIgnoreCase(phoneTypeStr)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        } else if ("work".equalsIgnoreCase(phoneTypeStr)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
        }
        // Put phone type value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType);
        // Insert new contact data into phone contact list.
        getContentResolver().insert(addContactsUri, contentValues);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void AddContect_Api() throws JSONException {

        //loadingDialog.showLoadingDialog();

        f_name = edt_FirstName.getText().toString().trim();
        l_name = edt_lastname.getText().toString().trim();
        AddcontectModel addcontectModel = SessionManager.getAdd_Contect_Detail(getApplicationContext());
        zip_code = addcontectModel.getZip_code();
        zoom_id = addcontectModel.getZoom_id();
        address = addcontectModel.getAddress();
        note = addcontectModel.getNote();
        city = addcontectModel.getCity();
        state = addcontectModel.getState();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
        List<Contactdetail> contactdetails = new ArrayList<>();
        contactdetails.addAll(addcontectModel.getContactdetails());
        List<Contactdetail> contactdetails_email = new ArrayList<>();
        contactdetails_email.addAll(addcontectModel.getContactdetails_email());
        contactdetails.addAll(contactdetails_email);
        JSONObject obj = new JSONObject();
        JSONObject paramObject = new JSONObject();
        //Other Company Add
        if (addcontectModel.getCompany().equals("")) {
            paramObject.put("company_name", "");
            paramObject.put("company_id", addcontectModel.getCompany_id());
        } else {
            paramObject.put("company_name", addcontectModel.getCompany().toString().trim());
            paramObject.put("company_id", "");
        }
        paramObject.put("address", address);
        paramObject.put("breakout_link", addcontectModel.getBreakoutu().toString().trim());
        paramObject.put("city", city.toString().trim());
       /* if (olld_image != null) {
            paramObject.put("oldImage", olld_image);
        } else {
            paramObject.put("oldImage", "");
        }*/

        paramObject.put("company_url", "");
        if (addcontectModel.getBirthday().equals("0000-00-00"))
        {
            paramObject.put("dob","");
        }
        else {
            paramObject.put("dob",addcontectModel.getBirthday());
        }
        paramObject.put("dynamic_fields_value", "");
        paramObject.put("facebook_link", addcontectModel.getFacebook().toString().trim());
        paramObject.put("firstname", edt_FirstName.getText().toString().trim());
        paramObject.put("lastname", l_name.toString().trim());
        paramObject.put("job_title", addcontectModel.getJob_title().toString().trim());
        paramObject.put("lastname", edt_lastname.getText().toString().trim());
        paramObject.put("linkedin_link", addcontectModel.getLinkedin().toString().trim());
        paramObject.put("organization_id", 1);
        paramObject.put("state", state.toString().trim());
        paramObject.put("team_id", 1);
        // addcontectModel.getTime()
        paramObject.put("timezone_id", addcontectModel.getTime().toString().trim());
        paramObject.put("twitter_link", addcontectModel.getTwitter().toString().trim());
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("zipcode", zip_code.toString().trim());
        paramObject.put("zoom_id", zoom_id.toString().trim());

        paramObject.put("imei", Global.imei);
        paramObject.put("contact_image",user_image_Url.toString().trim());
      ///  paramObject.put("contact_image", user_image_Url);
        //paramObject.put("image_extension", File_extension);
       // paramObject.put("contact_image_name", File_name);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < contactdetails.size(); i++) {
            JSONObject paramObject1 = new JSONObject();
            if (contactdetails.get(i).getEmail_number().equals("")) {
            } else {
                if (contactdetails.get(i).getType().equals("NUMBER")) {
                    phone = contactdetails.get(i).getEmail_number();
                }
                phone_type = contactdetails.get(i).getLabel();
                paramObject1.put("email_number", contactdetails.get(i).getEmail_number());
                paramObject1.put("id", contactdetails.get(i).getId());
                paramObject1.put("is_default", contactdetails.get(i).getIs_default());
                paramObject1.put("label", contactdetails.get(i).getLabel());
                paramObject1.put("type", contactdetails.get(i).getType());
            }
            jsonArray.put(paramObject1);
        }


        paramObject.put("contact_detail", jsonArray);


        obj.put("data", paramObject);

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());

        Log.e("Main Data is ", new Gson().toJson(gsonObject));
        retrofitCalls.Addcontect(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(Add_Newcontect_Activity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
                    long rowContactId = getRawContactId();
                    insertContactDisplayName(addContactsUri, rowContactId, edt_FirstName.getText().toString());
                    try {
                        insertContactPhoneNumber(addContactsUri, rowContactId, phone, phone_type);

                    }
                    catch (Exception e)
                    {

                    }
                    save_button.setText("Edit Contact");
                    finish();
                } else {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    UservalidateModel uservalidateModel = new Gson().fromJson(headerString, listType);
                    try {
                        String message = "";
                        if (uservalidateModel.getEmail_number().size() != 0) {
                            message = uservalidateModel.getEmail_number().get(0).toString();
                        }
                        if (uservalidateModel.getFirstname().size() != 0) {
                            message = uservalidateModel.getFirstname().get(0).toString();
                        }
                        Global.Messageshow(getApplicationContext(), mMainLayout, message, false);

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


    public void AddContect_Update() throws JSONException {
        //loadingDialog.showLoadingDialog();
        f_name = edt_FirstName.getText().toString().trim();
        l_name = edt_lastname.getText().toString().trim();
        ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(this);
        AddcontectModel addcontectModel = SessionManager.getAdd_Contect_Detail(getApplicationContext());
        zip_code = addcontectModel.getZip_code();
        zoom_id = addcontectModel.getZoom_id();
        address = addcontectModel.getAddress();
        note = addcontectModel.getNote();
        city = addcontectModel.getCity();
        state = addcontectModel.getState();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);


        List<Contactdetail> contactdetails = new ArrayList<>();
        contactdetails.addAll(addcontectModel.getContactdetails());


        List<Contactdetail> contactdetails_email = new ArrayList<>();
        contactdetails_email.addAll(addcontectModel.getContactdetails_email());
        contactdetails.addAll(contactdetails_email);
        JSONObject obj = new JSONObject();

        JSONObject param_data = new JSONObject();
        param_data.put("organization_id", 1);
        param_data.put("team_id", 1);
        param_data.put("user_id", user_data.getUser().getId());
        JSONArray jsonArray_contect = new JSONArray();


        JSONObject paramObject = new JSONObject();
        //Other Company Add
        if (addcontectModel.getCompany().trim().equalsIgnoreCase("")) {
            paramObject.put("company_name", "");
            paramObject.put("company_id", addcontectModel.getCompany_id().toString().trim());
        } else {
            paramObject.put("company_name", addcontectModel.getCompany().toString().trim());
            paramObject.put("company_id", "");
        }
        paramObject.put("id", Contect_data.getId());
        paramObject.put("address", address.toString().trim());
        paramObject.put("breakout_link", addcontectModel.getBreakoutu().toString().trim());
        paramObject.put("city", city);


        paramObject.put("company_url", "");
        if (addcontectModel.getBirthday().equals("0000-00-00"))
        {
            paramObject.put("dob","");
        }
        else {
            paramObject.put("dob",addcontectModel.getBirthday());
        }


        paramObject.put("dynamic_fields_value", "");
        paramObject.put("facebook_link", addcontectModel.getFacebook().toString().trim());
        paramObject.put("firstname", edt_FirstName.getText().toString().trim());
        paramObject.put("lastname", l_name.toString().trim());
        paramObject.put("job_title", addcontectModel.getJob_title().toString().trim());
        paramObject.put("lastname", edt_lastname.getText().toString().trim());
        paramObject.put("linkedin_link", addcontectModel.getLinkedin().toString().trim());
        paramObject.put("organization_id", 1);
        paramObject.put("state", state.toString().trim());
        paramObject.put("team_id", 1);
        // addcontectModel.getTime()
        paramObject.put("timezone_id", addcontectModel.getTime().toString().trim());
        paramObject.put("twitter_link", addcontectModel.getTwitter().toString().trim());
        paramObject.put("user_id", user_data.getUser().getId());
        paramObject.put("zipcode", zip_code.toString().trim());
        paramObject.put("zoom_id", zoom_id.toString().trim());
       /* if (!user_image_Url.equals("")) {
            paramObject.put("contact_image", user_image_Url);
            paramObject.put("contact_image_name", File_name);
            paramObject.put("image_extension", File_extension);
            if (olld_image != null) {
                paramObject.put("oldImage", olld_image);
            } else {
                paramObject.put("oldImage", "");
            }

        } else {
            paramObject.put("contact_image", "");
            //   paramObject.put("contact_image", "");
            //   paramObject.put("contact_image_name", "");
        }*/
        paramObject.put("contact_image",user_image_Url.toString().trim());
        paramObject.put("notes", addcontectModel.getNote().toString().trim());
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < contactdetails.size(); i++) {
            JSONObject paramObject1 = new JSONObject();
            if (contactdetails.get(i).getEmail_number().equals("")) {

            } else {
                if (contactdetails.get(i).getType().equals("NUMBER")) {
                    phone = contactdetails.get(i).getEmail_number();
                }
                phone_type = contactdetails.get(i).getLabel();
                paramObject1.put("email_number", contactdetails.get(i).getEmail_number());

                if (contactdetails.get(i).getContect_id() != 0) {
                    paramObject1.put("id", contactdetails.get(i).getId());
                }
                paramObject1.put("is_default", contactdetails.get(i).getIs_default());
                paramObject1.put("label", contactdetails.get(i).getLabel());
                paramObject1.put("type", contactdetails.get(i).getType());
                paramObject1.put("contact_id", Contect_data.getId());
                param_data.put("team_id", 1);
            }
            jsonArray.put(paramObject1);
        }
        paramObject.put("contact_details", jsonArray);
        jsonArray_contect.put(paramObject);
        param_data.put("contact_update", jsonArray_contect);

        obj.put("data", param_data);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());

        Log.e("Final Data is", new Gson().toJson(gsonObject));
        retrofitCalls.Updatecontect(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(Add_Newcontect_Activity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
                    long rowContactId = getRawContactId();
                    insertContactDisplayName(addContactsUri, rowContactId, edt_FirstName.getText().toString());
                   try {
                       insertContactPhoneNumber(addContactsUri, rowContactId, phone, phone_type);
                   }
                   catch (Exception e)

                   {

                   }
                    save_button.setText("Edit Contact");
                    SessionManager.setContect_edit(true);
                    finish();
                } else {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    UservalidateModel uservalidateModel = new Gson().fromJson(headerString, listType);
                    try {
                        String message = "";
                        if (uservalidateModel.getEmail_number().size() != 0) {
                            message = uservalidateModel.getEmail_number().get(0).toString();
                        }
                        if (uservalidateModel.getFirstname().size() != 0) {
                            message = uservalidateModel.getFirstname().get(0).toString();
                        }
                        Global.Messageshow(getApplicationContext(), mMainLayout, message, false);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pulse_icon:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (checkAndRequestPermissions(Add_Newcontect_Activity.this)) {
                    captureimageDialog(false);
                }
                break;
            case R.id.iv_user:
            case R.id.tv_nameLetter:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (checkAndRequestPermissions(Add_Newcontect_Activity.this)) {
                    if (olld_image != null && !olld_image.equals("") || user_image_Url != null && !user_image_Url.equals("")) {
                        captureimageDialog(true);
                    } else {
                        captureimageDialog(false);
                    }

                }

                break;
            case R.id.iv_toolbar_manu_vertical:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(this);
                broadcast_manu(Contect_data);
                break;

        }
    }
    // Handled permission Result

    private void captureimageDialog(boolean remove) {
        final View mView = getLayoutInflater().inflate(R.layout.capture_userpicture_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Add_Newcontect_Activity.this, R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);

        TextView cameraId = bottomSheetDialog.findViewById(R.id.cameraId);
        TextView tv_remove = bottomSheetDialog.findViewById(R.id.tv_remove);
        if (remove) {
            tv_remove.setVisibility(View.VISIBLE);
        } else {
            tv_remove.setVisibility(View.GONE);
        }
        tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                iv_user.setVisibility(View.GONE);
                layout_pulse.setVisibility(View.VISIBLE);
                tv_nameLetter.setVisibility(View.GONE);
                if(Global.IsNotNull(user_image_Url)){
                    AmazonUtil.deleteS3Client(getApplicationContext(),user_image_Url);
                    user_image_Url="";
                    Glide.with(getApplicationContext()).load(user_image_Url).into(iv_user);
                }
                bottomSheetDialog.dismiss();


            }
        });
        cameraId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                image_flag=0;
                mLastClickTime = SystemClock.elapsedRealtime();
               /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 0);*/
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String fileName = "temp.jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    mCapturedImageURI = getContentResolver()
                            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);
                    takePictureIntent
                            .putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE);



                bottomSheetDialog.dismiss();
            }
        });
        TextView galleryId = bottomSheetDialog.findViewById(R.id.galleryId);
        galleryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                image_flag=0;
                mLastClickTime = SystemClock.elapsedRealtime();
/*                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);*/


                Intent takePictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                String fileName = "temp.jpg";
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                mCapturedImageURI = getContentResolver()
                        .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values);
                takePictureIntent
                        .putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                startActivityForResult(takePictureIntent, 1);

                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

     //   Log.e("requestCode", String.valueOf(requestCode));
      //  Log.e("resultCode",String.valueOf(resultCode));

     if (requestCode==0)
     {
         if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
             CropImage.ActivityResult result = CropImage.getActivityResult(data);
             if (resultCode == RESULT_OK) {
                 Uri resultUri = result.getUri();
                 File_name = "Image";
                 File file=new File(result.getUri().getPath());
                 Uri uri = Uri.fromFile(file);
                 filePath1 = uri.getPath();
                 String profilePath = Global.getPathFromUri(getApplicationContext(), uri);
                 iv_user.setVisibility(View.VISIBLE);
                 layout_pulse.setVisibility(View.GONE);
                 Glide.with(getApplicationContext()).load(resultUri).into(iv_user);

                 //    uploadImageTos3(filePath1);


             } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                 Exception error = result.getError();
             }
         }
     }
     else if (requestCode == CAPTURE_IMAGE) {
         ImageCropFunctionCustom(mCapturedImageURI);
     }
     else if (requestCode==203)
     {
         if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
         {
             CropImage.ActivityResult result = CropImage.getActivityResult(data);
             if (resultCode == RESULT_OK) {
                 Uri resultUri = result.getUri();
                 File file=new File(result.getUri().getPath());
                 Uri uri = Uri.fromFile(file);
                 filePath1 = uri.getPath();
                 iv_user.setVisibility(View.VISIBLE);
                 layout_pulse.setVisibility(View.GONE);
                 Glide.with(getApplicationContext()).load(resultUri).into(iv_user);
                // uploadImageTos3(filePath1);


             } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                 Exception error = result.getError();
             }
         }
     }
     else {
         if (image_flag==0)
         {
             image_flag=1;
             try {
                 Log.e("Data is", String.valueOf(data.getData()));
                 CropImage.activity(data.getData())
                         .start(this);
             }
             catch (Exception e)
             {

             }

         }

     }


    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void uploadImageTos3(String imageUri, String flag) {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Add_Newcontect_Activity.this);

        if (!imageUri.toString().equals("")) {
            olld_image=user_image_Url;
            String contect_url=s3uploaderObj.initUpload(imageUri,"contact_image", Integer.valueOf(signResponseModel.getUser().getId()));
            s3uploaderObj.setOns3UploadDone(new S3Uploader.S3UploadInterface() {
                @Override
                public void onUploadSuccess(String response) {
                    Log.e("Reppnse is",new Gson().toJson(response));

                    if (response.equalsIgnoreCase("Success")) {
                        user_image_Url=contect_url;
                        if(Global.IsNotNull(olld_image)){
                            AmazonUtil.deleteS3Client(getApplicationContext(),olld_image);
                        }
                        if (flag.equals("add"))
                        {
                            if (Global.isNetworkAvailable(Add_Newcontect_Activity.this, mMainLayout)) {

                                try {
                                    AddContect_Api();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else if (flag.equals("update"))
                        {

                            if (Global.isNetworkAvailable(Add_Newcontect_Activity.this, mMainLayout)) {

                                try {
                                    AddContect_Update();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                    }
                }

                @Override
                public void onUploadError(String response) {
                    if (flag.equals("add"))
                    {
                        if (Global.isNetworkAvailable(Add_Newcontect_Activity.this, mMainLayout)) {

                            try {
                                AddContect_Api();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else if (flag.equals("update"))
                    {

                        if (Global.isNetworkAvailable(Add_Newcontect_Activity.this, mMainLayout)) {

                            try {
                                AddContect_Update();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    Log.e("Error", "Error Uploading");

                }
            });
        }else{
            if (flag.equals("add"))
            {
                if (Global.isNetworkAvailable(Add_Newcontect_Activity.this, mMainLayout)) {

                    try {
                        AddContect_Api();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (flag.equals("update"))
            {

                if (Global.isNetworkAvailable(Add_Newcontect_Activity.this, mMainLayout)) {

                    try {
                        AddContect_Update();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public void ImageCropFunctionCustom(Uri uri) {
        Intent intent = CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(this);
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private long getRawContactIdByName(String givenName, String familyName) {
        ContentResolver contentResolver = getContentResolver();

        // Query raw_contacts table by display name field ( given_name family_name ) to get raw contact id.

        // Create query column array.
        String[] queryColumnArr = {ContactsContract.RawContacts._ID};

        // Create where condition clause.
        String displayName = givenName + " " + familyName;
        String whereClause = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY + " = '" + displayName + "'";

        // Query raw contact id through RawContacts uri.
        Uri rawContactUri = ContactsContract.RawContacts.CONTENT_URI;

        // Return the query cursor.
        Cursor cursor = contentResolver.query(rawContactUri, queryColumnArr, whereClause, null, null);

        long rawContactId = -1;

        if (cursor != null) {
            // Get contact count that has same display name, generally it should be one.
            int queryResultCount = cursor.getCount();
            // This check is used to avoid cursor index out of bounds exception. android.database.CursorIndexOutOfBoundsException
            if (queryResultCount > 0) {
                // Move to the first row in the result cursor.
                cursor.moveToFirst();
                // Get raw_contact_id.
                rawContactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.RawContacts._ID));
            }
        }

        return rawContactId;
    }

    private int updateContactPhoneByName(String givenName, String familyName, String phone, String type) {
        int ret = 0;

        ContentResolver contentResolver = getContentResolver();

        // Get raw contact id by display name.
        long rawContactId = getRawContactIdByName(givenName, familyName);

        // Update data table phone number use contact raw contact id.
        if (rawContactId > -1) {
            // Update mobile phone number.
            updatePhoneNumber(contentResolver, rawContactId, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, phone, type);

            // Update work mobile phone number.
            //updatePhoneNumber(contentResolver, rawContactId, ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE, phone);

            // Update home phone number.
            // updatePhoneNumber(contentResolver, rawContactId, ContactsContract.CommonDataKinds.Phone.TYPE_HOME, phone);

            ret = 1;
        } else {
            ret = 0;
        }

        return ret;
    }


    private void updatePhoneNumber(ContentResolver contentResolver, long rawContactId, int phoneType, String newPhoneNumber, String phoneTypeStr) {
        // Create content values object.
        ContentValues contentValues = new ContentValues();

        // Put new phone number value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhoneNumber);
        int phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        if ("home".equalsIgnoreCase(phoneTypeStr)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        } else if ("mobile".equalsIgnoreCase(phoneTypeStr)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        } else if ("work".equalsIgnoreCase(phoneTypeStr)) {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
        }
        // Put phone type value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType);

        // Create query condition, query with the raw contact id.
        StringBuffer whereClauseBuf = new StringBuffer();

        // Specify the update contact id.
        whereClauseBuf.append(ContactsContract.Data.RAW_CONTACT_ID);
        whereClauseBuf.append("=");
        whereClauseBuf.append(rawContactId);

        // Specify the row data mimetype to phone mimetype( vnd.android.cursor.item/phone_v2 )
        whereClauseBuf.append(" and ");
        whereClauseBuf.append(ContactsContract.Data.MIMETYPE);
        whereClauseBuf.append(" = '");
        String mimetype = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE;
        whereClauseBuf.append(mimetype);
        whereClauseBuf.append("'");

        // Specify phone type.
        whereClauseBuf.append(" and ");
        whereClauseBuf.append(ContactsContract.CommonDataKinds.Phone.TYPE);
        whereClauseBuf.append(" = ");
        whereClauseBuf.append(phoneType);

        // Update phone info through Data uri.Otherwise it may throw java.lang.UnsupportedOperationException.
        Uri dataUri = ContactsContract.Data.CONTENT_URI;

        // Get update data count.
        int updateCount = contentResolver.update(dataUri, contentValues, whereClauseBuf.toString(), null);
        Log.e("Count is", String.valueOf(updateCount));

    }

    @Override
    protected void onResume() {
        Global.getInstance().setConnectivityListener(this);
        super.onResume();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Add_Newcontect_Activity.this, mMainLayout);
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

    @Override
    public void fragmentBecameVisible() {

    }

    private void broadcast_manu(ContectListData.Contact contact_item) {

        @SuppressLint("InflateParams") final View mView = getLayoutInflater().inflate(R.layout.remove_block_layout, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.CoffeeDialog);
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
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                //Block Contect

                try {
                    if (Global.isNetworkAvailable(Add_Newcontect_Activity.this, mMainLayout)) {
                        Contect_BLock(contact_item, 1, bottomSheetDialog);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        selected_un_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                //Block Contect

                try {
                    if (Global.isNetworkAvailable(Add_Newcontect_Activity.this, mMainLayout)) {
                        Contect_BLock(contact_item, 0, bottomSheetDialog);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        selected_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                //Block Contect

                try {
                    if (Global.isNetworkAvailable(Add_Newcontect_Activity.this, mMainLayout)) {
                        Contect_Remove(contact_item, bottomSheetDialog);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        bottomSheetDialog.show();

    }


    public void Contect_BLock(ContectListData.Contact contact_data, int block, BottomSheetDialog bottomSheetDialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
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
        Log.e("Main Data is ", new Gson().toJson(gsonObject));
        retrofitCalls.Block_contact(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    finish();
                    if (block == 1) {
                        iv_block.setVisibility(View.VISIBLE);
                    } else {
                        iv_block.setVisibility(View.GONE);
                    }

                    contact_data.setIs_blocked(block);
                    SessionManager.setOneCotect_deatil(getApplicationContext(), contact_data);
                    SessionManager.setContect_edit(true);

                } else {
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), false);
                }
                bottomSheetDialog.cancel();
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                bottomSheetDialog.cancel();
            }
        });

    }


    public void Contect_Remove(ContectListData.Contact contact_data, BottomSheetDialog bottomSheetDialog) throws JSONException {
        loadingDialog.showLoadingDialog();
        SignResponseModel user_data = SessionManager.getGetUserdata(this);
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
        Log.e("Main Data is ", new Gson().toJson(gsonObject));
        retrofitCalls.Addcontect(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(this), Global.Device, new RetrofitCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    SessionManager.setContect_edit(true);
                    finish();
                } else {
                    Global.Messageshow(getApplicationContext(), mMainLayout, response.body().getMessage(), false);
                }
                bottomSheetDialog.cancel();
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                bottomSheetDialog.cancel();
            }
        });

    }

}