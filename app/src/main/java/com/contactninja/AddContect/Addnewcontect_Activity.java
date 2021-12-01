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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.contactninja.Model.AddcontectModel;
import com.contactninja.R;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.contactninja.Fragment.AddContect_Fragment.BzcardFragment;
import com.contactninja.Fragment.AddContect_Fragment.ExposuresFragment;
import com.contactninja.Fragment.AddContect_Fragment.InformationFragment;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class Addnewcontect_Activity extends AppCompatActivity {
    private static final String TAG_HOME = "Addcontect";
    public static String CURRENT_TAG = TAG_HOME;
    ImageView iv_back,iv_more,pulse_icon;
    TextView save_button;
    TabLayout tabLayout;
    ViewPager viewPager;
    String fragment_name;
    EditText tv_name,tv_title;
    SessionManager sessionManager;
    String phone,phone_type,email,email_type,address,zip_code,zoom_id,note;
    public static final int RequestPermissionCode = 1;
    LinearLayout mMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewcontect);
        IntentUI();

        EnableRuntimePermission();
        sessionManager=new SessionManager(this);
        //Set Viewpagger
        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Bzcard"));
        tabLayout.addTab(tabLayout.newTab().setText("Exposures"));
        fragment_name="Info";

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ContectAdapter adapter = new ContectAdapter(this,getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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

                if (save_button.getText().toString().equals("Save Contact")) {
                    //Add Contect
                    save_button.setText("Edit Contact");
                    Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
                    long rowContactId = getRawContactId();

                    insertContactDisplayName(addContactsUri, rowContactId, tv_name.getText().toString());

                    insertContactPhoneNumber(addContactsUri, rowContactId, phone, phone_type);
                }
                else {
                    //Update Contect
                    save_button.setText("Save Contact");
                    updateContect(email,phone,phone_type);

                }
            }
        });

        tv_name.addTextChangedListener(new TextWatcher() {
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

        tv_title.addTextChangedListener(new TextWatcher() {
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
        viewPager = findViewById(R.id.viewPager);
        pulse_icon=findViewById(R.id.pulse_icon);
        tv_title=findViewById(R.id.tv_title);
        tv_name=findViewById(R.id.tv_name);
        mMainLayout=findViewById(R.id.frameContainer1);

    }


    //Set Adapter


    class ContectAdapter extends FragmentPagerAdapter {

        Context context;
        int totalTabs;
        String strtext1;
        public ContectAdapter(Context c, FragmentManager fm, int totalTabs) {
            super(fm);
            context = c;
            this.totalTabs = totalTabs;
            this.strtext1=strtext1;
        }
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                   /* if (fragment_name.equals("Info"))
                    {*/
                        InformationFragment informationFragment = new InformationFragment();
                        return informationFragment;
                   /*}
                    else {
                        EditContectFragment informationFragment = new EditContectFragment();
                        return informationFragment;
                    }
*/
                case 1:
                    BzcardFragment bzcardFragment = new BzcardFragment();
                    return bzcardFragment;
                case 2:
                    ExposuresFragment exposuresFragment = new ExposuresFragment();
                    return exposuresFragment;
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return totalTabs;
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
}