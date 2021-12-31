package com.contactninja.Fragment;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Addnewcontect_Activity;
import com.contactninja.Fragment.AddContect_Fragment.BzcardFragment;
import com.contactninja.Fragment.AddContect_Fragment.ExposuresFragment;
import com.contactninja.Fragment.AddContect_Fragment.InformationFragment;
import com.contactninja.Fragment.UserPofile.User_BzcardFragment;
import com.contactninja.Fragment.UserPofile.User_ExposuresFragment;
import com.contactninja.Fragment.UserPofile.User_InformationFragment;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UserData.User;
import com.contactninja.Model.UservalidateModel;
import com.contactninja.R;
import com.contactninja.Setting.SettingActivity;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Response;

public class Main_userProfile_Fragment extends Fragment implements View.OnClickListener {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    public static final int RequestPermissionCode = 1;
    private static final String TAG_HOME = "Addcontect";
    public static String CURRENT_TAG = TAG_HOME;
    ImageView iv_Setting, pulse_icon;
    TextView save_button,tv_nameLetter;
    TabLayout tabLayout;
    String fragment_name, user_image_Url, File_name = "", File_extension = "";
    EditText edt_FirstName, edt_lastname;
    SessionManager sessionManager;
    String phone, phone_type, email, email_type,
            address, zip_code, zoom_id, note, f_name, l_name,
            city, state, olld_image = "";
    LinearLayout mMainLayout;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    FrameLayout frameContainer;
    RoundedImageView iv_user;
    LinearLayout layout_pulse;
    String option_type = "";
    private BroadcastReceiver mNetworkReceiver;
    LinearLayout layout_toolbar_logo;
    TextView edit_profile;


    // ListPhoneContactsActivity use this method to start this activity.
    public static void start(Context context) {
        Intent intent = new Intent(context, Addnewcontect_Activity.class);
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
    public View onCreateView(@SuppressLint("UnknownNullness") LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_profile_main, container, false);
        intentView(view);
        mNetworkReceiver = new ConnectivityReceiver();
        sessionManager=new SessionManager(getActivity());
        loadingDialog=new LoadingDialog(getActivity());
        //Global.checkConnectivity(getActivity(), mMainLayout);
        EnableRuntimePermission();
        sessionManager = new SessionManager(getActivity());
        save_button.setText("Save");
        option_type = "save";
        String flag = sessionManager.getContect_flag(getActivity());


        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


        User user_data_model=user_data.getUser();
        ContectListData.Contact set_contact=new ContectListData.Contact();
        set_contact.setFirstname(user_data_model.getFirstName());
        set_contact.setLastname(user_data_model.getLastName());
        List<ContectListData.Contact.ContactDetail> contactDetails_list=new ArrayList<>();
        ContectListData.Contact.ContactDetail contactDetail_model=new ContectListData.Contact.ContactDetail();
        contactDetail_model.setEmailNumber(user_data_model.getContactNumber());
        contactDetail_model.setLabel("");
        /*contactDetail_model.setEmailNumber(user_data_model.getEmail());*/
        contactDetail_model.setContactId(user_data_model.getId());
        contactDetail_model.setType("NUMBER");
        contactDetail_model.setIsDefault(1);
        contactDetail_model.setLabel("Home");
        contactDetails_list.add(0,contactDetail_model);

        ContectListData.Contact.ContactDetail contactDetail_model1=new ContectListData.Contact.ContactDetail();
        contactDetail_model1.setEmailNumber(user_data_model.getEmail());
        contactDetail_model1.setLabel("");
        /*contactDetail_model.setEmailNumber(user_data_model.getEmail());*/
        contactDetail_model1.setContactId(user_data_model.getId());
        contactDetail_model1.setType("EMAIL");
        contactDetail_model1.setIsDefault(1);
        contactDetail_model1.setLabel("Home");
        contactDetails_list.add(1,contactDetail_model1);
        set_contact.setContactDetails(contactDetails_list);






        SessionManager.setOneCotect_deatil(getActivity(),set_contact);

        if (flag.equals("edit")) {
            ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(getActivity());
            edt_FirstName.setText(Contect_data.getFirstname()+ " "+Contect_data.getLastname());
            edt_lastname.setText(Contect_data.getLastname());
            f_name = Contect_data.getFirstname();
            l_name = Contect_data.getLastname();
            if(Contect_data.getContactImage()==null){
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

                }
                tv_nameLetter.setText(add_text);

            }else {
                iv_user.setVisibility(View.VISIBLE);
                layout_pulse.setVisibility(View.GONE);
                Glide.with(getActivity()).
                        load(Contect_data.getContactImage())
                        .placeholder(R.drawable.shape_primary_back)
                        .error(R.drawable.shape_primary_back).
                        into(iv_user);
            }
            olld_image = Contect_data.getContactImage();

            save_button.setText("Save");



        } else if (flag.equals("read")) {
            edt_FirstName.setEnabled(false);
            edt_lastname.setEnabled(false);

            ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(getActivity());
            edt_FirstName.setText(Contect_data.getFirstname()+ " "+Contect_data.getLastname());
            edt_lastname.setText(Contect_data.getLastname());
            f_name = Contect_data.getFirstname();
            l_name = Contect_data.getLastname();
            if(Contect_data.getContactImage()==null){
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

                }
                tv_nameLetter.setText(add_text);

            }else {
                iv_user.setVisibility(View.VISIBLE);
                layout_pulse.setVisibility(View.GONE);
                Glide.with(getActivity()).
                        load(Contect_data.getContactImage())
                        .placeholder(R.drawable.shape_primary_back)
                        .error(R.drawable.shape_primary_back).
                        into(iv_user);
            }
            olld_image = Contect_data.getContactImage();

            save_button.setText("Edit");
        } else {
            Log.e("Null", "No Call");
        }
        retrofitCalls = new RetrofitCalls(getActivity());
        // loadingDialog = new LoadingDialog(this);
        //Set Viewpagger
        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Bzcard"));
        tabLayout.addTab(tabLayout.newTab().setText("Exposures"));
        fragment_name = "Info";

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        Fragment fragment = new User_InformationFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer123, fragment, "Fragment");
        fragmentTransaction.commitAllowingStateLoss();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new User_InformationFragment();
                        break;
                    case 1:
                        fragment = new User_BzcardFragment();
                        break;
                    case 2:
                        fragment = new User_ExposuresFragment();
                        break;

                }
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameContainer123, fragment, "Fragment");
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
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddcontectModel addcontectModel = SessionManager.getAdd_Contect_Detail(getActivity());
                //  AddcontectModel addcontectModel=new AddcontectModel();
                //    Log.e("Data is ", new Gson().toJson(addcontectModel));
                zip_code = addcontectModel.getZip_code();
                zoom_id = addcontectModel.getZoom_id();
                address = addcontectModel.getAddress();
                note = addcontectModel.getNote();
                f_name = edt_FirstName.getText().toString();
                l_name = edt_lastname.getText().toString();
                if ( sessionManager.getContect_flag(getActivity()).equals("edit"))
                {
                    if (f_name.equals("")) {
                        Global.Messageshow(getActivity(), mMainLayout, getString(R.string.invalid_first_name), false);

                    } else if (l_name.equals("")) {
                        Global.Messageshow(getActivity(), mMainLayout, getString(R.string.invalid_last_name), false);

                    }
                    else {
                        try {
                            if(Global.isNetworkAvailable(getActivity(),mMainLayout)) {
                                AddContect_Api1();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                else if (save_button.getText().toString().equals("Save")) {
                    //Add Contect.
                    if (f_name.equals("")) {
                        Global.Messageshow(getActivity(), mMainLayout, getString(R.string.invalid_first_name), false);

                    } else if (l_name.equals("")) {
                        Global.Messageshow(getActivity(), mMainLayout, getString(R.string.invalid_last_name), false);

                    } else if (addcontectModel.getContactdetails().size() == 0) {

                        Global.Messageshow(getActivity(), mMainLayout, getString(R.string.enter_phone), false);

                    } else {

                        try {
                            if(Global.isNetworkAvailable(getActivity(),mMainLayout)) {
                                AddContect_Api();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                } else {


                    String flag = sessionManager.getContect_flag(getActivity());
                    if (flag.equals("read")) {
                        SessionManager.setContect_flag("edit");
                        Intent addnewcontect = new Intent(getActivity(), Addnewcontect_Activity.class);
                        SessionManager.setContect_flag("edit");
                        startActivity(addnewcontect);
                        //finish();
                    } else {
                        SessionManager.setContect_flag("edit");
                        save_button.setText("Save");
                        edt_FirstName.setEnabled(true);
                        edt_lastname.setEnabled(true);
                        try {
                            if(Global.isNetworkAvailable(getActivity(),mMainLayout)) {
                                AddContect_Api1();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
        return view;
    }

    private void intentView(View view) {


        iv_Setting = view.findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.VISIBLE);
        tabLayout = view.findViewById(R.id.tabLayout);
        frameContainer = view.findViewById(R.id.frameContainer);
        pulse_icon = view.findViewById(R.id.pulse_icon);
        edt_lastname = view.findViewById(R.id.edt_lastname);
        edt_FirstName =view. findViewById(R.id.edt_FirstName);
        mMainLayout = view.findViewById(R.id.frameContainer1);
        layout_pulse = view.findViewById(R.id.layout_pulse);
        tv_nameLetter = view.findViewById(R.id.tv_nameLetter);

        iv_user = view.findViewById(R.id.iv_user);
        pulse_icon.setOnClickListener(this);
        iv_user.setOnClickListener(this);
        save_button=view.findViewById(R.id.save_button);
        save_button.setOnClickListener(this);
        save_button.setVisibility(View.VISIBLE);
        layout_toolbar_logo=view.findViewById(R.id.layout_toolbar_logo);
        layout_toolbar_logo.setVisibility(View.VISIBLE);
        edit_profile=view.findViewById(R.id.edit_profile);
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
                .setPermissions(Manifest.permission.WRITE_CONTACTS,Manifest.permission.SEND_SMS)
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
        switch (RC) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),
                            "Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),
                            "Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
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


      /*  //Note
        contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Note.DATA1, note)
                .withValue(ContactsContract.CommonDataKinds.Note.DATA1, ContactsContract.CommonDataKinds.Note.DATA1)
                .build());
        */


        try {
            ContentProviderResult[] results = getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, contact);
            Log.e("Contect Result", new Gson().toJson(results));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getRawContactId() {
        // Inser an empty contact.
        ContentValues contentValues = new ContentValues();
        Uri rawContactUri = getActivity().getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        // Get the newly created contact raw id.
        long ret = ContentUris.parseId(rawContactUri);
        return ret;
    }

    private void insertContactDisplayName(Uri addContactsUri, long rawContactId, String displayName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // Put contact display name value.
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName);
        getActivity(). getContentResolver().insert(addContactsUri, contentValues);
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
        getActivity().getContentResolver().insert(addContactsUri, contentValues);
    }



    public void AddContect_Api() throws JSONException {


        f_name = edt_FirstName.getText().toString().trim();
        l_name = edt_lastname.getText().toString().trim();
        AddcontectModel addcontectModel = SessionManager.getAdd_Contect_Detail(getActivity());
        zip_code = addcontectModel.getZip_code();
        zoom_id = addcontectModel.getZoom_id();
        address = addcontectModel.getAddress();
        note = addcontectModel.getNote();
        city = addcontectModel.getCity();
        state = addcontectModel.getState();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


        List<Contactdetail> contactdetails = new ArrayList<>();
        contactdetails.addAll(addcontectModel.getContactdetails());


        List<Contactdetail> contactdetails_email = new ArrayList<>();
        contactdetails_email.addAll(addcontectModel.getContactdetails_email());
        contactdetails.addAll(contactdetails_email);
      /* for(int j=0;j<2;j++){
           Contactdetail contactdetail=new Contactdetail();
           contactdetail.setId(0);
           contactdetail.setEmail_number("shirish@intericare.net");
           contactdetail.setIs_default(0);
           contactdetail.setLabel("Shirish");
           contactdetail.setType("Homme");
           contactdetails.add(j,contactdetail);
       }*/



        JSONObject obj = new JSONObject();

        JSONObject paramObject = new JSONObject();

        //Other Company Add
      if (addcontectModel.getCompany().equals(""))
        {
            paramObject.put("company_name", "");
            paramObject.put("company_id",  addcontectModel.getCompany_id());
        }
      else {
          paramObject.put("company_name", addcontectModel.getCompany());
          paramObject.put("company_id",  "");
       }
        paramObject.put("address", address);
        paramObject.put("breakout_link", addcontectModel.getBreakoutu());
        paramObject.put("city", city);


        paramObject.put("company_url", "");
        paramObject.put("dob", addcontectModel.getBirthday());
        paramObject.put("dynamic_fields_value", "");
        paramObject.put("facebook_link", addcontectModel.getFacebook());
        paramObject.put("firstname", edt_FirstName.getText().toString().trim());
        paramObject.put("lastname", l_name);
        paramObject.put("job_title", addcontectModel.getJob_title());
        paramObject.put("lastname", edt_lastname.getText().toString().trim());
        paramObject.put("linkedin_link", addcontectModel.getLinkedin());
        paramObject.put("organization_id", "1");
        paramObject.put("state", state);
        paramObject.put("team_id", "1");
        // addcontectModel.getTime()
        paramObject.put("timezone_id", addcontectModel.getTime());
        paramObject.put("twitter_link", addcontectModel.getTwitter());
        paramObject.put("user_id", user_id);
        paramObject.put("zipcode", zip_code);
        paramObject.put("zoom_id", zoom_id);
        paramObject.put("contact_image", user_image_Url);
        paramObject.put("image_extension", File_extension);
        paramObject.put("contact_image_name", File_name);
        paramObject.put("oldImage", olld_image);

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < contactdetails.size(); i++) {
            JSONObject paramObject1 = new JSONObject();
            if (contactdetails.get(i).getEmail_number().equals("")) {

            } else {
                if (contactdetails.get(i).getType().equals("NUMBER"))
                {
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

        Log.e("Final Data is", new Gson().toJson(gsonObject));
        retrofitCalls.Addcontect(sessionManager,gsonObject, loadingDialog, Global.getToken(sessionManager), new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getStatus() == 200) {
                    Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
                    long rowContactId = getRawContactId();
                    insertContactDisplayName(addContactsUri, rowContactId, edt_FirstName.getText().toString());
                    insertContactPhoneNumber(addContactsUri, rowContactId, phone, phone_type);


                } else {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    UservalidateModel uservalidateModel = new Gson().fromJson(headerString, listType);
                    try{
                        if(uservalidateModel.getFirstname().size()!=0){
                            Global.Messageshow(getActivity(), mMainLayout, uservalidateModel.getFirstname().get(0).toString(), false);
                        }
                    }catch (Exception e){
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

    public void AddContect_Api1() throws JSONException {

        loadingDialog.showLoadingDialog();
        ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(getActivity());
        f_name = edt_FirstName.getText().toString().trim();
        l_name = edt_lastname.getText().toString().trim();
        AddcontectModel addcontectModel = SessionManager.getAdd_Contect_Detail(getActivity());
        zip_code = addcontectModel.getZip_code();
        zoom_id = addcontectModel.getZoom_id();
        address = addcontectModel.getAddress();
        note = addcontectModel.getNote();
        city = addcontectModel.getCity();
        state = addcontectModel.getState();
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String user_id = String.valueOf(user_data.getUser().getId());
        List<Contactdetail> contactdetails = new ArrayList<>();
        contactdetails.addAll(addcontectModel.getContactdetails());


        List<Contactdetail> contactdetails_email = new ArrayList<>();
        contactdetails_email.addAll(addcontectModel.getContactdetails_email());
        contactdetails.addAll(contactdetails_email);


        JSONObject obj = new JSONObject();

        JSONObject paramObject = new JSONObject();
        paramObject.put("id", Contect_data.getId());
        paramObject.put("address", address);
        paramObject.put("breakout_link", addcontectModel.getBreakoutu());
        paramObject.put("city", city);
        paramObject.put("company_id", addcontectModel.getCompany_id());
        paramObject.put("company_name", addcontectModel.getCompany());
        paramObject.put("company_url", "");
        paramObject.put("dob", addcontectModel.getBirthday());
        paramObject.put("dynamic_fields_value", "");
        paramObject.put("facebook_link", addcontectModel.getFacebook());
        paramObject.put("firstname", edt_FirstName.getText().toString().trim());
        paramObject.put("lastname", l_name);
        paramObject.put("job_title", addcontectModel.getJob_title());
        paramObject.put("lastname", edt_lastname.getText().toString().trim());
        paramObject.put("linkedin_link", addcontectModel.getLinkedin());
        paramObject.put("organization_id", Contect_data.getOrganizationId());
        paramObject.put("state", state);
        paramObject.put("team_id", Contect_data.getTeamId());
        // addcontectModel.getTime()
        paramObject.put("timezone_id", addcontectModel.getTime());
        paramObject.put("twitter_link", addcontectModel.getTwitter());
        paramObject.put("user_id", user_id);
        paramObject.put("zipcode", zip_code);
        paramObject.put("zoom_id", zoom_id);
        paramObject.put("contact_image", user_image_Url);
        paramObject.put("image_extension", File_extension);
        paramObject.put("contact_image_name", File_name);
        paramObject.put("oldImage", olld_image);
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());

        Log.e("Final Data is", new Gson().toJson(gsonObject));
        retrofitCalls.Addcontect(sessionManager,gsonObject, loadingDialog, Global.getToken(sessionManager), new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getStatus() == 200) {
                    Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
                    long rowContactId = getRawContactId();
                    save_button.setText("Save");
                    /*           updateContect(edt_FirstName.getText().toString(),phone,phone_type);*/

                    updateContactPhoneByName(edt_FirstName.getText().toString(), edt_lastname.getText().toString(), phone, phone_type);

                } else {
                    Global.Messageshow(getActivity(), mMainLayout, response.body().getMessage(), false);
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


                if (checkAndRequestPermissions(getActivity())) {
                    captureimageDialog(false);

                }
                break;

            case R.id.iv_user:


                if (checkAndRequestPermissions(getActivity())) {
                    captureimageDialog(true);

                }
                break;
            case R.id.tv_nameLetter:
                if (checkAndRequestPermissions(getActivity())) {
                    captureimageDialog(true);

                }

                break;
        }
    }
    // Handled permission Result

    private void captureimageDialog(boolean remove) {
        final View mView = getLayoutInflater().inflate(R.layout.capture_userpicture_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.CoffeeDialog);
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

                iv_user.setVisibility(View.GONE);
                layout_pulse.setVisibility(View.VISIBLE);
                tv_nameLetter.setVisibility(View.GONE);
                bottomSheetDialog.dismiss();

            }
        });
        cameraId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

                bottomSheetDialog.dismiss();
            }
        });
        TextView galleryId = bottomSheetDialog.findViewById(R.id.galleryId);
        galleryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        iv_user.setImageBitmap(bitmap);
                        iv_user.setVisibility(View.VISIBLE);
                        layout_pulse.setVisibility(View.GONE);

                        File_name = "Image";

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();
                        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        user_image_Url = "data:image/JPEG;base64," + imageString;
                        File_extension = "JPEG";
                        Log.e("url is", user_image_Url);
                    }
                    break;
                case 1:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                user_image_Url = encodeFileToBase64Binary(picturePath);

                                iv_user.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                                iv_user.setVisibility(View.VISIBLE);
                                layout_pulse.setVisibility(View.GONE);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                                user_image_Url = "data:image/JPEG;base64," + imageString;
                                Log.e("url is", user_image_Url);
                                File_extension = "JPEG";


                                iv_user.setVisibility(View.VISIBLE);

                                File file = new File(selectedImage.getPath());
                                File_name = file.getName();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private long getRawContactIdByName(String givenName, String familyName) {
        ContentResolver contentResolver = getActivity().getContentResolver();

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

        ContentResolver contentResolver = getActivity().getContentResolver();

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



    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(getActivity(), mMainLayout);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void unregisterNetworkChanges() {
        try {
            getActivity().unregisterReceiver(mNetworkReceiver);
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





}