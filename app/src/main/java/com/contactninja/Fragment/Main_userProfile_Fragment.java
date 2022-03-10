package com.contactninja.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
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

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.bumptech.glide.Glide;
import com.contactninja.AddContect.Add_Newcontect_Activity;
import com.contactninja.Bzcard.Select_Bzcard_Activity;
import com.contactninja.MainActivity;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UserData.User;
import com.contactninja.Model.UservalidateModel;
import com.contactninja.R;
import com.contactninja.Setting.SettingActivity;
import com.contactninja.UserPofile.User_BzcardFragment;
import com.contactninja.UserPofile.User_GrowthFragment;
import com.contactninja.UserPofile.User_InformationFragment;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.aws.AWSKeys;
import com.contactninja.aws.AmazonUtil;
import com.contactninja.aws.S3Uploader;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")

public class Main_userProfile_Fragment extends Fragment implements View.OnClickListener {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    public static final int RequestPermissionCode = 1;
    private static final String TAG_HOME = "Addcontect";
    public static String CURRENT_TAG = TAG_HOME;
    public static TransferUtility transferUtility;
    int image_flag = 1;
    String filePath1="";
    Uri mCapturedImageURI;
    Integer CAPTURE_IMAGE = 3;
    CoordinatorLayout user_image;
    ImageView iv_Setting, pulse_icon, iv_back, iv_edit,iv_Bzcard;
    TextView save_button, tv_nameLetter;
    TabLayout tabLayout;
    TabLayout.Tab tab;
    String fragment_name, user_image_Url = "", File_name = "", File_extension = "";
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
    LinearLayout layout_toolbar_logo;
    TextView edit_profile;
    AmazonS3Client s3Client;
    S3Uploader s3uploaderObj;
    private long mLastClickTime = 0;
    private BroadcastReceiver mNetworkReceiver;
    String flag;
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

    public View onCreateView(@SuppressLint("UnknownNullness") LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile_main, container, false);
        intentView(view);
        Log.e("On Click Method ","2");
        SessionManager.setOneCotect_deatil(getActivity(), new ContectListData.Contact());

        mNetworkReceiver = new ConnectivityReceiver();
        sessionManager = new SessionManager(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        retrofitCalls = new RetrofitCalls(getActivity());
        sessionManager = new SessionManager(getActivity());
        s3uploaderObj = new S3Uploader(getActivity());
        // Create an S3 client
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getActivity(),
                "us-east-2:a47e1f07-8030-4bce-b50b-075713507665", // Identity pool ID
                Regions.US_EAST_2 // Region
        );
        option_type = "save";



        setTab();
        setdata();



        pulse_icon.setColorFilter(getResources().getColor(R.color.purple_200));
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                AddcontectModel addcontectModel = SessionManager.getAdd_Contect_Detail(getActivity());
                zip_code = addcontectModel.getZip_code();
                zoom_id = addcontectModel.getZoom_id();
                address = addcontectModel.getAddress();
                note = addcontectModel.getNote();
                f_name = edt_FirstName.getText().toString();
                l_name = edt_lastname.getText().toString();
                if (SessionManager.getContect_flag(getActivity()).equals("edit")) {
                    if (f_name.equals("")) {
                        Global.Messageshow(getActivity(), mMainLayout, getString(R.string.invalid_first_name), false);

                    } else if (l_name.equals("")) {
                        Global.Messageshow(getActivity(), mMainLayout, getString(R.string.invalid_last_name), false);

                    } else {
                        uploadImageTos3(filePath1);
                    }

                } else {


                    String flag = SessionManager.getContect_flag(getActivity());
                    if (flag.equals("read")) {
                        SessionManager.setContect_flag("edit");
                        Intent addnewcontect = new Intent(getActivity(), Add_Newcontect_Activity.class);
                        SessionManager.setContect_flag("edit");
                        startActivity(addnewcontect);
                        //finish();
                    } else {
                        SessionManager.setContect_flag("edit");
                        save_button.setText("Save");
                        edt_FirstName.setEnabled(true);
                        edt_lastname.setEnabled(true);
                        uploadImageTos3(filePath1);
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

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();

        return view;
    }
    private void Userinfo() throws JSONException {
        //  loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(getActivity());
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("api", "user view");
        obj.add("data", paramObject);
        retrofitCalls.Userinfo(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        if (response.body().getHttp_status() == 200) {
                            loadingDialog.cancelLoading();
                            SessionManager.setUserdata(getActivity(), new SignResponseModel());

                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            Type listType = new TypeToken<SignResponseModel>() {
                            }.getType();
                            SignResponseModel user_model = new Gson().fromJson(headerString, listType);
                            SessionManager.setUserdata(getActivity(), user_model);
                            Log.e("Main Data Is ", new Gson().toJson(user_model));

                            // setTab();


                        } else {
                            loadingDialog.cancelLoading();
                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }

    private void Userinfo1() throws JSONException {
        //  loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(getActivity());
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("api", "user view");
        obj.add("data", paramObject);
        retrofitCalls.Userinfo(sessionManager, obj, loadingDialog, Global.getToken(sessionManager),
                Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
                    @Override
                    public void success(Response<ApiResponse> response) {
                        if (response.body().getHttp_status() == 200) {
                            SessionManager.setUserdata(getActivity(), new SignResponseModel());

                            Gson gson = new Gson();
                            String headerString = gson.toJson(response.body().getData());
                            Type listType = new TypeToken<SignResponseModel>() {
                            }.getType();
                            SignResponseModel user_model = new Gson().fromJson(headerString, listType);
                            SessionManager.setUserdata(getActivity(), user_model);
                            setdata();

                            Fragment fragment = new User_InformationFragment();
                            FragmentManager fragmentManager = getFragmentManager();

                            if (fragmentManager != null) {
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.frameContainer123, fragment, "Fragment");
                                fragmentTransaction.commitAllowingStateLoss();
                            }

                            loadingDialog.cancelLoading();


                        } else {
                            loadingDialog.cancelLoading();
                        }
                    }

                    @Override
                    public void error(Response<ApiResponse> response) {
                        loadingDialog.cancelLoading();
                    }
                });
    }

    private void setTab() {
        // loadingDialog = new LoadingDialog(this);
        //Set Viewpagger
        flag = SessionManager.getContect_flag(getActivity());
        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Bzcard"));
        tabLayout.addTab(tabLayout.newTab().setText("Growth"));
        fragment_name = "Info";


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        Fragment fragment = new User_InformationFragment();
        FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer123, fragment, "Fragment");
            fragmentTransaction.commitAllowingStateLoss();
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        if (!flag.equals("edit")) {
                            edt_FirstName.setEnabled(false);
                            edt_lastname.setEnabled(false);

                        }
                        fragment = new User_InformationFragment();
                        break;
                    case 1:
                        if (flag.equals("edit")) {
                            layout_toolbar_logo.setVisibility(View.VISIBLE);
                            iv_back.setVisibility(View.GONE);
                            SessionManager.setContect_flag("read");
                            save_button.setVisibility(View.GONE);
                            iv_Setting.setVisibility(View.VISIBLE);
                            save_button.setText("Save");
                            iv_edit.setVisibility(View.GONE);
                            edt_lastname.setVisibility(View.GONE);
                            edit_profile.setVisibility(View.VISIBLE);
                            edt_FirstName.setEnabled(false);
                            //  setdata();
                        }
                        fragment = new User_BzcardFragment();
                        break;
                    case 2:
                        if (flag.equals("edit")) {
                            layout_toolbar_logo.setVisibility(View.VISIBLE);
                            iv_back.setVisibility(View.GONE);
                            SessionManager.setContect_flag("read");
                            save_button.setVisibility(View.GONE);
                            iv_Setting.setVisibility(View.VISIBLE);
                            save_button.setText("Save");
                            iv_edit.setVisibility(View.GONE);
                            edt_lastname.setVisibility(View.GONE);
                            edit_profile.setVisibility(View.VISIBLE);
                            // setdata();
                            edt_FirstName.setEnabled(false);
                        }
                        fragment = new User_GrowthFragment();
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


    }

    @SuppressLint("SetTextI18n")
    private void setdata() {
        flag = SessionManager.getContect_flag(getActivity());
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        if(Global.IsNotNull(user_data.getUser().getId())) {


            String user_id = String.valueOf(user_data.getUser().getId());
            String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
            String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());


            User user_data_model = user_data.getUser();
            ContectListData.Contact set_contact = new ContectListData.Contact();
            set_contact.setFirstname(user_data_model.getFirstName());
            set_contact.setLastname(user_data_model.getLastName());
            List<ContectListData.Contact.ContactDetail> contactDetails_list = new ArrayList<>();
            ContectListData.Contact.ContactDetail contactDetail_model = new ContectListData.Contact.ContactDetail();
            contactDetail_model.setEmailNumber(user_data_model.getContactNumber());
            contactDetail_model.setLabel("");
            /*contactDetail_model.setEmailNumber(user_data_model.getEmail());*/
            contactDetail_model.setContactId(user_data_model.getId());
            contactDetail_model.setType("NUMBER");
            contactDetail_model.setIsDefault(1);
            contactDetail_model.setLabel("Home");
            contactDetails_list.add(0, contactDetail_model);

            ContectListData.Contact.ContactDetail contactDetail_model1 = new ContectListData.Contact.ContactDetail();
            contactDetail_model1.setEmailNumber(user_data_model.getEmail());
            contactDetail_model1.setLabel("");
            /*contactDetail_model.setEmailNumber(user_data_model.getEmail());*/
            contactDetail_model1.setContactId(user_data_model.getId());
            contactDetail_model1.setType("EMAIL");
            contactDetail_model1.setIsDefault(1);
            contactDetail_model1.setLabel("Home");
            contactDetails_list.add(1, contactDetail_model1);
            set_contact.setContactDetails(contactDetails_list);
            SessionManager.setOneCotect_deatil(getActivity(), set_contact);

            if (flag.equals("edit")) {
                pulse_icon.setEnabled(true);
                iv_user.setEnabled(true);
                tv_nameLetter.setEnabled(true);
                iv_edit.setVisibility(View.VISIBLE);
                edt_FirstName.setEnabled(true);
                edt_lastname.setEnabled(true);
                ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(getActivity());
                edt_FirstName.setText(Contect_data.getFirstname());
                edt_lastname.setText(Contect_data.getLastname());
                f_name = Contect_data.getFirstname();
                l_name = Contect_data.getLastname();

                if (user_data.getUser().getUserprofile().getProfilePic() == null) {
                    iv_user.setVisibility(View.GONE);
                    layout_pulse.setVisibility(View.VISIBLE);
                    user_image.setVisibility(View.GONE);
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
                    Glide.with(getActivity()).
                            load(user_data.getUser().getUserprofile().getProfilePic())
                            .placeholder(R.drawable.shape_primary_back)
                            .error(R.drawable.shape_primary_back).
                            into(iv_user);
                }
                user_image_Url = Contect_data.getContactImage();

                save_button.setText("Save");


            } else if (flag.equals("read")) {

                iv_user.setEnabled(false);
                pulse_icon.setEnabled(false);
                tv_nameLetter.setEnabled(false);
                save_button.setVisibility(View.GONE);
                edt_FirstName.setEnabled(false);
                edt_lastname.setEnabled(false);


                ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(getActivity());
                edt_FirstName.setText(Contect_data.getFirstname() + " " + Contect_data.getLastname());
                edt_lastname.setText(Contect_data.getLastname());
                f_name = Contect_data.getFirstname();
                l_name = Contect_data.getLastname();
                if (user_data.getUser().getUserprofile().getProfilePic() == null) {
                    iv_user.setVisibility(View.GONE);
                    layout_pulse.setVisibility(View.VISIBLE);
                    user_image.setVisibility(View.GONE);
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
                    try {
                        Glide.with(getActivity()).
                                load(user_data.getUser().getUserprofile().getProfilePic())
                                .placeholder(R.drawable.shape_primary_back)
                                .error(R.drawable.shape_primary_back).
                                into(iv_user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                user_image_Url = Contect_data.getContactImage();

            } else {
                pulse_icon.setEnabled(false);
                tv_nameLetter.setEnabled(false);
               // Log.e("Null", "No Call");
            }

        }



    }

    private void intentView(View view) {
        iv_edit = view.findViewById(R.id.iv_edit);
        iv_Setting = view.findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.VISIBLE);
        iv_Setting.setOnClickListener(this);
        iv_Bzcard = view.findViewById(R.id.iv_Bzcard);
        iv_Bzcard.setOnClickListener(this);
        iv_Bzcard.setVisibility(View.VISIBLE);
        iv_back = view.findViewById(R.id.iv_back);
        tabLayout = view.findViewById(R.id.tabLayout);
        frameContainer = view.findViewById(R.id.frameContainer);
        pulse_icon = view.findViewById(R.id.pulse_icon);
        user_image = view.findViewById(R.id.user_image);
        edt_lastname = view.findViewById(R.id.edt_lastname);
        edt_FirstName = view.findViewById(R.id.edt_FirstName);
        mMainLayout = view.findViewById(R.id.frameContainer1);
        layout_pulse = view.findViewById(R.id.layout_pulse);
        tv_nameLetter = view.findViewById(R.id.tv_nameLetter);
        iv_user = view.findViewById(R.id.iv_user);
        pulse_icon.setOnClickListener(this);
        iv_user.setOnClickListener(this);
        save_button = view.findViewById(R.id.save_button);
        save_button.setOnClickListener(this);
        layout_toolbar_logo = view.findViewById(R.id.layout_toolbar_logo);
        layout_toolbar_logo.setVisibility(View.VISIBLE);
        edit_profile = view.findViewById(R.id.edit_profile);
        edit_profile.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] PResult) {

        super.onRequestPermissionsResult(RC, per, PResult);
        if (RC == RequestPermissionCode) {
            if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Global.Messageshow(getActivity(), mMainLayout, "Permission Canceled, Now your application cannot access CONTACTS", false);
                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", getActivity().getPackageName(), null)));
                // Toast.makeText(MainActivity.this, "Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();
            }
        }
        if (RC == REQUEST_ID_MULTIPLE_PERMISSIONS) {
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
        getActivity().getContentResolver().insert(addContactsUri, contentValues);
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

    public void AddContect_Update() throws JSONException {
        f_name = edt_FirstName.getText().toString().trim();
        l_name = edt_lastname.getText().toString().trim();
        //  ContectListData.Contact Contect_data = SessionManager.getOneCotect_deatil(getActivity());
        AddcontectModel addcontectModel = SessionManager.getAdd_Contect_Detail(getActivity());
        zip_code = addcontectModel.getZip_code();
        zoom_id = addcontectModel.getZoom_id();
        address = addcontectModel.getAddress();
        note = addcontectModel.getNote();
        city = addcontectModel.getCity();
        state = addcontectModel.getState();

        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());
        String email_address = user_data.getUser().getEmail();

        String rol_id = user_data.getUser().getRoleId().toString();
        String contect_number = user_data.getUser().getContactNumber();

        List<Contactdetail> contactdetails = new ArrayList<>();
        contactdetails.clear();
        List<Contactdetail> contactdetails1 = new ArrayList<>();
        contactdetails1.clear();
        contactdetails.addAll(addcontectModel.getContactdetails());


        List<Contactdetail> contactdetails_email = new ArrayList<>();
        contactdetails_email.addAll(addcontectModel.getContactdetails_email());
        contactdetails.addAll(contactdetails_email);
        JSONObject obj = new JSONObject();

        JSONObject param_data = new JSONObject();
        param_data.put("organization_id", 1);
        param_data.put("team_id", 1);
        param_data.put("user_id", user_data.getUser().getId());
        param_data.put("role_id", rol_id);
        param_data.put("contact_number", contect_number.toString().trim());
        param_data.put("email", email_address.toString().trim());


        // JSONArray jsonArray_contect = new JSONArray();


        // JSONObject paramObject = new JSONObject();
        //Other Company Add
        if (addcontectModel.getCompany().trim().equalsIgnoreCase("")) {
            param_data.put("company_name", addcontectModel.getCompany().toString().trim());
            //param_data.put("company_id",  addcontectModel.getCompany_id());
        } else {
            param_data.put("company_name", addcontectModel.getCompany().toString().trim());
            // param_data.put("company_id",   addcontectModel.getCompany_id());
        }
        param_data.put("address", address.toString().trim());
        param_data.put("breakout_link", addcontectModel.getBreakoutu().toString().trim());
        param_data.put("city", city.toString().trim());


        param_data.put("company_url", addcontectModel.getCompany_url().toString().trim());

        if (addcontectModel.getBirthday().equals("0000-00-00"))
        {
            param_data.put("dob", "");
        }
        else
        {
            param_data.put("dob", addcontectModel.getBirthday());
        }
        param_data.put("dynamic_fields_value", "");
        param_data.put("facebook_link", addcontectModel.getFacebook().toString().trim());
        param_data.put("first_name", edt_FirstName.getText().toString().trim());
        param_data.put("last_name", edt_lastname.getText().toString().toString().trim());
        param_data.put("job_title", addcontectModel.getJob_title().toString().trim());
        param_data.put("linkedin_link", addcontectModel.getLinkedin().toString().trim());
        param_data.put("organization_id", "1");
        param_data.put("state", state.toString().trim());
        param_data.put("team_id", "1");
        // addcontectModel.getTime()
        param_data.put("timezone_id", addcontectModel.getTime().toString().trim());
        param_data.put("twitter_link", addcontectModel.getTwitter().toString().trim());
        param_data.put("user_id", user_data.getUser().getId());
        param_data.put("zipcode", addcontectModel.getZip_code().toString().trim());
        param_data.put("zoom_id", addcontectModel.getZoom_id().toString().trim());

        /*if (!user_image_Url.equals("")) {
            param_data.put("profile_pic", user_image_Url);
            param_data.put("pic_name", File_name);
            param_data.put("pic_extension", File_extension);
            if (olld_image != null) {
                param_data.put("old_pic_name", olld_image);
            } else {
                param_data.put("old_pic_name", "");
            }

        } else {
            param_data.put("profile_pic", olld_image);
            //   paramObject.put("contact_image", "");
            //   paramObject.put("contact_image_name", "");
        }*/
        param_data.put("user_img", user_image_Url.toString().trim());

        param_data.put("notes", addcontectModel.getNote().toString().trim());


        for (int i = 0; i < contactdetails.size(); i++) {

            if (contactdetails.get(i).getEmail_number().equals("")) {

            } else {


                if (contactdetails.get(i).getEmail_number().equals(user_data.getUser().getContactNumber()) || contactdetails.get(i).getEmail_number().equals(user_data.getUser().getEmail())) {

                } else {
                    if (contactdetails.get(i).getType().equals("NUMBER")) {
                        phone = contactdetails.get(i).getEmail_number();
                    }
                    phone_type = contactdetails.get(i).getLabel();
                    contactdetails1.add(contactdetails.get(i));
                }
            }
        }


        JSONArray jsonArray = new JSONArray();
        JSONObject paramObject1 = null;

        Log.e("Size is ", String.valueOf(contactdetails1.size()));
        contactdetails1 = removeDuplicates((ArrayList<Contactdetail>) contactdetails1);
        for (int i = 0; i < contactdetails1.size(); i++) {
            paramObject1 = new JSONObject();
            if (contactdetails1.get(i).getEmail_number().equals("")) {

            } else {
                if (contactdetails1.get(i).getType().equals("NUMBER")) {
                    phone = contactdetails1.get(i).getEmail_number();
                }
                phone_type = contactdetails1.get(i).getLabel();
                paramObject1.put("email_number", contactdetails1.get(i).getEmail_number());
                paramObject1.put("label", contactdetails1.get(i).getLabel());
                paramObject1.put("type", contactdetails1.get(i).getType());

            }

            jsonArray.put(paramObject1);
        }
        param_data.put("contact_details", jsonArray);

        obj.put("data", param_data);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());

        Log.e("Final Data is", new Gson().toJson(gsonObject));


        retrofitCalls.UpdateUser_Profile(sessionManager, gsonObject, loadingDialog, Global.getToken(sessionManager), Global.getVersionname(getActivity()), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    layout_toolbar_logo.setVisibility(View.VISIBLE);
                    iv_back.setVisibility(View.GONE);
                    SessionManager.setContect_flag("read");
                    save_button.setVisibility(View.GONE);
                    iv_Setting.setVisibility(View.VISIBLE);
                    save_button.setText("Save");
                    edit_profile.setVisibility(View.VISIBLE);
                    edt_lastname.setVisibility(View.GONE);
                    edt_FirstName.setEnabled(false);
                    edt_lastname.setEnabled(false);
                    iv_edit.setVisibility(View.GONE);
                    try {
                        Userinfo1();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.body().getHttp_status() == 404) {
                    Gson gson = new Gson();
                    String headerString = gson.toJson(response.body().getData());
                    Type listType = new TypeToken<UservalidateModel>() {
                    }.getType();
                    UservalidateModel uservalidateModel = new Gson().fromJson(headerString, listType);
                    try {
                        if (uservalidateModel.getLast_name().size() != 0) {
                            Global.Messageshow(getActivity(), mMainLayout, uservalidateModel.getLast_name().get(0), false);
                        } else if (uservalidateModel.getFirstname().size() != 0) {
                            Global.Messageshow(getActivity(), mMainLayout, uservalidateModel.getFirstname().get(0), false);
                        }
                    } catch (Exception e) {
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
                            Global.Messageshow(getActivity(), mMainLayout, uservalidateModel.getFirstname().get(0), false);
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

    public ArrayList<Contactdetail> removeDuplicates(ArrayList<Contactdetail> list) {
        Set<Contactdetail> set = new TreeSet(new Comparator<Contactdetail>() {

            @Override
            public int compare(Contactdetail o1, Contactdetail o2) {
                if (o1.getEmail_number().equalsIgnoreCase(o2.getEmail_number())) {
                    return 0;
                }
                return 1;
            }
        });
        set.addAll(list);

        final ArrayList newList = new ArrayList(set);
        return newList;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Fragment fragment;
        FragmentManager fragmentManager ;
        switch (v.getId()) {
            case R.id.iv_Bzcard:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(getActivity(), Select_Bzcard_Activity.class));
                break;
            case R.id.pulse_icon:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (checkAndRequestPermissions(getActivity())) {
                    captureimageDialog(false);

                }
                break;


            case R.id.iv_user:
            case R.id.tv_nameLetter:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                if (checkAndRequestPermissions(getActivity())) {
                    captureimageDialog(true);
                }
                break;

            case R.id.iv_Setting:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                Intent i = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(i);
                break;

            case R.id.edit_profile:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }


                layout_toolbar_logo.setVisibility(View.GONE);
                iv_back.setVisibility(View.VISIBLE);
                SessionManager.setContect_flag("edit");
                save_button.setVisibility(View.VISIBLE);
                iv_Setting.setVisibility(View.GONE);
                save_button.setText("Save");
                edit_profile.setVisibility(View.GONE);
                edt_lastname.setVisibility(View.VISIBLE);
                edt_FirstName.setEnabled(true);
                edt_lastname.setEnabled(true);
                setdata();

                tab=tabLayout.getTabAt(0);
                tab.select();

                 fragment = new User_InformationFragment();
                 fragmentManager = getFragmentManager();

                if (fragmentManager != null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameContainer123, fragment, "Fragment");
                    fragmentTransaction.commitAllowingStateLoss();
                }



                break;
            case R.id.iv_back:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                layout_toolbar_logo.setVisibility(View.VISIBLE);
                iv_back.setVisibility(View.GONE);
                SessionManager.setContect_flag("read");
                save_button.setVisibility(View.GONE);
                iv_Setting.setVisibility(View.VISIBLE);
                save_button.setText("Save");
                iv_edit.setVisibility(View.GONE);
                edt_lastname.setVisibility(View.GONE);
                edit_profile.setVisibility(View.VISIBLE);
                setdata();

                 fragment = new User_InformationFragment();
                 fragmentManager = getFragmentManager();

                if (fragmentManager != null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameContainer123, fragment, "Fragment");
                    fragmentTransaction.commitAllowingStateLoss();
                }
        }
    }

    private void captureimageDialog(boolean remove) {
        final View mView = getLayoutInflater().inflate(R.layout.capture_userpicture_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.CoffeeDialog);
        bottomSheetDialog.setContentView(mView);

        TextView cameraId = bottomSheetDialog.findViewById(R.id.cameraId);
        TextView tv_remove = bottomSheetDialog.findViewById(R.id.tv_remove);
        if (remove) {
            tv_remove.setVisibility(View.GONE);
        } else {
            tv_remove.setVisibility(View.VISIBLE);
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
                    AmazonUtil.deleteS3Client(getActivity(),user_image_Url);
                    user_image_Url="";
                    Glide.with(getActivity()).load(user_image_Url).into(iv_user);
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
                mLastClickTime = SystemClock.elapsedRealtime();
               /* Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
*/
                image_flag = 0;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String fileName = "temp.jpg";
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                mCapturedImageURI = getActivity().getContentResolver()
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
                mCapturedImageURI = getActivity().getContentResolver()
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
    // Handled permission Result

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {
                    iv_user.setVisibility(View.VISIBLE);
                    layout_pulse.setVisibility(View.GONE);
                    tv_nameLetter.setVisibility(View.GONE);
                    Uri resultUri = result.getUri();
                    File_name = "Image";
                    File file=new File(result.getUri().getPath());
                    Uri uri = Uri.fromFile(file);
                    filePath1 = uri.getPath();
                    Glide.with(getActivity()).load(resultUri).into(iv_user);

                    //  uploadImageTos3(profilePath);

                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        } else if (requestCode == CAPTURE_IMAGE) {
            ImageCropFunctionCustom(mCapturedImageURI);
        }
        else if (requestCode == 203) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result1 = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {
                    iv_user.setVisibility(View.VISIBLE);
                    layout_pulse.setVisibility(View.GONE);
                    tv_nameLetter.setVisibility(View.GONE);
                    Uri resultUri = result1.getUri();
                    File_name = "Image";
                    File file=new File(result1.getUri().getPath());
                    Uri uri = Uri.fromFile(file);
                    filePath1 = uri.getPath();
                    Glide.with(getActivity()).load(resultUri).into(iv_user);
                    //uploadImageTos3(filePath1);
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result1.getError();
                }
            }
        } else {
            if (image_flag == 0) {
                image_flag = 1;
                ImageCropFunctionCustom(data.getData());
            }

        }
    }

    private void uploadImageTos3(String imageUri) {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(getActivity());
        if (!imageUri.equals("")) {
            //String[] nameList = imageUri.split("/");
            // String uploadFileName = nameList[nameList.length - 1];
            olld_image = user_image_Url;
            String contect_group = s3uploaderObj.initUpload(imageUri, "user_profile", Integer.valueOf(signResponseModel.getUser().getId()));
            s3uploaderObj.setOns3UploadDone(new S3Uploader.S3UploadInterface() {
                @Override
                public void onUploadSuccess(String response) {
                    Log.e("Reppnse is", new Gson().toJson(response));
                    Toast.makeText(getActivity(), new Gson().toJson(response), Toast.LENGTH_SHORT).show();

                    if (response.equalsIgnoreCase("Success")) {
                        user_image_Url = contect_group;
                        try {
                            if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                                AddContect_Update();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onUploadError(String response) {
                    try {
                        if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                            AddContect_Update();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("Error", "Error Uploading");

                }
            });
        } else {
            try {
                if (Global.isNetworkAvailable(getActivity(), mMainLayout)) {
                    AddContect_Update();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void ImageCropFunctionCustom(Uri uri) {
        Intent intent = CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(getActivity());
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
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
                    Userinfo();
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