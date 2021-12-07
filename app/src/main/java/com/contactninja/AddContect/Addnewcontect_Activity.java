package com.contactninja.AddContect;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.contactninja.Auth.LoginActivity;
import com.contactninja.Auth.Phone_email_verificationActivity;
import com.contactninja.Auth.PlanTyep.PlanType_Screen;
import com.contactninja.Fragment.HomeFragment;
import com.contactninja.MainActivity;
import com.contactninja.Model.AddcontectModel;
import com.contactninja.Model.Contactdetail;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.tabs.TabLayout;
import com.contactninja.Fragment.AddContect_Fragment.BzcardFragment;
import com.contactninja.Fragment.AddContect_Fragment.ExposuresFragment;
import com.contactninja.Fragment.AddContect_Fragment.InformationFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class Addnewcontect_Activity extends AppCompatActivity {
    private static final String TAG_HOME = "Addcontect";
    public static String CURRENT_TAG = TAG_HOME;
    ImageView iv_back,iv_more,pulse_icon;
    TextView save_button;
    TabLayout tabLayout;
    String fragment_name;
    EditText edt_FirstName,edt_lastname;
    SessionManager sessionManager;
    String phone,phone_type,email,email_type,address,zip_code,zoom_id,note,f_name,l_name,city,state;
    public static final int RequestPermissionCode = 1;
    LinearLayout mMainLayout;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    FrameLayout frameContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewcontect);
        IntentUI();

        EnableRuntimePermission();

        sessionManager=new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        loadingDialog=new LoadingDialog(this);
        //Set Viewpagger
        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Bzcard"));
        tabLayout.addTab(tabLayout.newTab().setText("Exposures"));
        fragment_name="Info";

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        Fragment fragment = new InformationFragment();;
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, fragment, "Fragment");
            fragmentTransaction.commitAllowingStateLoss();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new InformationFragment();
                        break;
                    case 1:
                        fragment = new BzcardFragment();
                        break;
                    case 2:
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

        pulse_icon.setColorFilter(getColor(R.color.purple_200));
        save_button.setText("Save Contact");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddcontectModel addcontectModel = sessionManager.getAdd_Contect_Detail(getApplicationContext());
                //  AddcontectModel addcontectModel=new AddcontectModel();
                Log.e("Data is ", new Gson().toJson(addcontectModel));
                phone = addcontectModel.getMobile();
                phone_type = addcontectModel.getMobile_type();
                email = addcontectModel.getEmail();
                email_type = addcontectModel.getEmail_type();
                zip_code = addcontectModel.getZip_code();
                zoom_id = addcontectModel.getZoom_id();
                address = addcontectModel.getAddress();
                note = addcontectModel.getNote();
                f_name=edt_FirstName.getText().toString();
                l_name=edt_lastname.getText().toString();

                if (save_button.getText().toString().equals("Save Contact")) {
                    //Add Contect.
                    if (!Global.isValidPhoneNumber(phone))
                    {

                        Global.Messageshow(getApplicationContext(),mMainLayout,getString(R.string.invalid_phone),false);
                    }
                    else if (f_name.equals(""))
                    {
                        Global.Messageshow(getApplicationContext(),mMainLayout,getString(R.string.invalid_first_name),false);

                    }
                    else if (l_name.equals(""))
                    {
                        Global.Messageshow(getApplicationContext(),mMainLayout,getString(R.string.invalid_last_name),false);

                    }
                    else {



                        try {
                            AddContect_Api();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }
                else {
                    //Update Contect
                    save_button.setText("Save Contact");
                    updateContect(email,phone,phone_type);

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
        iv_back=findViewById(R.id.iv_back);
        save_button=findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        iv_more=findViewById(R.id.iv_more);
        iv_more.setVisibility(View.GONE);
        tabLayout = findViewById(R.id.tabLayout);
        frameContainer = findViewById(R.id.frameContainer);
        pulse_icon=findViewById(R.id.pulse_icon);
        edt_lastname=findViewById(R.id.edt_lastname);
        edt_FirstName=findViewById(R.id.edt_FirstName);
        mMainLayout=findViewById(R.id.frameContainer1);

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
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedTitle("Contactninja would like to access your contacts")
                .setDeniedMessage("Contact Ninja uses your contacts to improve your business’s marketing outreach by aggrregating your contacts.")
                .setGotoSettingButtonText("setting")
                .setPermissions(Manifest.permission.WRITE_CONTACTS)
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
                Global.Messageshow(getApplicationContext(), mMainLayout, "Permission Canceled, Now your application cannot access CONTACTS", false);
                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", getPackageName(), null)));
                // Toast.makeText(MainActivity.this, "Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void addContact(String given_name, String name, String mobile, String home, String email,String note) {

      Log.e("Name",name);
      Log.e("mobile",mobile);
      Log.e("home",home);
      Log.e("email",email);
      Log.e("Note",note);

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
            ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, contact);
            Log.e("Contect Result",new Gson().toJson(results));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getRawContactId()
    {
        // Inser an empty contact.
        ContentValues contentValues = new ContentValues();
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        // Get the newly created contact raw id.
        long ret = ContentUris.parseId(rawContactUri);
        return ret;
    }
    private void insertContactDisplayName(Uri addContactsUri, long rawContactId, String displayName)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // Put contact display name value.
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName);
        getContentResolver().insert(addContactsUri, contentValues);
    }



    private void insertContactPhoneNumber(Uri addContactsUri, long rawContactId, String phoneNumber, String phoneTypeStr)
    {
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
        if("home".equalsIgnoreCase(phoneTypeStr))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        }else if("mobile".equalsIgnoreCase(phoneTypeStr))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        }else if("work".equalsIgnoreCase(phoneTypeStr))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
        }
        // Put phone type value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType);
        // Insert new contact data into phone contact list.
        getContentResolver().insert(addContactsUri, contentValues);
    }
    // ListPhoneContactsActivity use this method to start this activity.
    public static void start(Context context)
    {
        Intent intent = new Intent(context, Addnewcontect_Activity.class);
        context.startActivity(intent);
    }


    public void updateContect(String email,String phone ,String type)
    {

        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        int phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        if("home".equalsIgnoreCase(type))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        }else if("mobile".equalsIgnoreCase(type))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        }else if("work".equalsIgnoreCase(type))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
        }
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email)

                .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE,
                        ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .putExtra(ContactsContract.Intents.Insert.PHONE, phone)
                .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .putExtra(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType);


        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }



    public void AddContect_Api() throws JSONException {


        /*JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("key", "value1");
            jsonObj_.put("key", "value2");
            jsonObj_.put("key", "value3");


            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());

            //print parameter


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);
*/
        f_name=edt_FirstName.getText().toString().trim();
        l_name=edt_lastname.getText().toString().trim();
        AddcontectModel addcontectModel = sessionManager.getAdd_Contect_Detail(getApplicationContext());
        phone = addcontectModel.getMobile();
        phone_type = addcontectModel.getMobile_type();
        email = addcontectModel.getEmail();
        email_type = addcontectModel.getEmail_type();
        zip_code = addcontectModel.getZip_code();
        zoom_id = addcontectModel.getZoom_id();
        address = addcontectModel.getAddress();
        note = addcontectModel.getNote();
        city=addcontectModel.getCity();
        state=addcontectModel.getState();

        SignResponseModel user_data = SessionManager.getGetUserdata(this);


        String user_id = String.valueOf(user_data.getUser().getId());
        String organization_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getId());
        String team_id = String.valueOf(user_data.getUser().getUserOrganizations().get(0).getTeamId());






       List<Contactdetail> contactdetails=new ArrayList<>();
       for(int j=0;j<2;j++){
           Contactdetail contactdetail=new Contactdetail();
           contactdetail.setId("0");
           contactdetail.setEmail_number("shirish@intericare.net");
           contactdetail.setIs_default("");
           contactdetail.setLabel("Shirish");
           contactdetail.setType("Homme");
           contactdetails.add(j,contactdetail);
       }

        JSONObject obj = new JSONObject();

        JSONObject paramObject = new JSONObject();

        paramObject.put("address", address);
        paramObject.put("breakout_link", "");
        paramObject.put("city", city);
        paramObject.put("company_id", "");
        paramObject.put("company_name", "");
        paramObject.put("company_url", "");
        paramObject.put("dob", "");
        paramObject.put("dynamic_fields_value", "");
        paramObject.put("facebook_link", "");
        paramObject.put("firstname", f_name);
        paramObject.put("job_title", "");
        paramObject.put("lastname", l_name);
        paramObject.put("linkedin_link", "");
        paramObject.put("organization_id", organization_id);
        paramObject.put("state", state);
        paramObject.put("team_id", team_id);
        paramObject.put("timezone_id", "");
        paramObject.put("twitter_link", "");
        paramObject.put("user_id", user_id);
        paramObject.put("zipcode", zoom_id);
        paramObject.put("zoom_id", zoom_id);

        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<contactdetails.size();i++){
            JSONObject paramObject1 = new JSONObject();
            paramObject1.put("email_number", contactdetails.get(i).getEmail_number());
            paramObject1.put("id",  contactdetails.get(i).getId());
            paramObject1.put("is_default",  contactdetails.get(i).getIs_default());
            paramObject1.put("label",  contactdetails.get(i).getLabel());
            paramObject1.put("type",  contactdetails.get(i).getType());
            jsonArray.put(paramObject1);
        }

        paramObject.put("contact_detail", jsonArray);

        obj.put("data", paramObject);
        retrofitCalls.Addcontect(obj, loadingDialog,Global.getToken(this), new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {

                loadingDialog.cancelLoading();
                if (response.body().getStatus() == 200) {
                    Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
                    long rowContactId = getRawContactId();
                    insertContactDisplayName(addContactsUri, rowContactId, edt_FirstName.getText().toString());
                    insertContactPhoneNumber(addContactsUri, rowContactId, phone, phone_type);
                    save_button.setText("Edit Contact");
                    finish();
                } else {
                    Global.Messageshow(getApplicationContext(),mMainLayout,response.body().getMessage(),false);
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });

    }
}