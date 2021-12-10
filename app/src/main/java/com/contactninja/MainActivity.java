package com.contactninja;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.contactninja.Model.ContectListData;
import com.contactninja.Utils.App;
import com.contactninja.Utils.DatabaseClient;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;
import com.contactninja.Fragment.Contect_main_Fragment;
import com.contactninja.Fragment.HomeFragment;
import com.contactninja.Fragment.SendFragment;
import com.contactninja.Fragment.UsetProgileFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    InstallStateUpdatedListener installStateUpdatedListener;
    private AppUpdateManager mAppUpdateManager;
    private static int RC_APP_UPDATE = 0;
    RelativeLayout mMainLayout;
    public static final int RequestPermissionCode = 1;
    ImageView llHome, llsend, llContact, llUser;
    FrameLayout frameLayout;
    private long mLastClickTime = 0;
    SessionManager sessionManager;
    //Declare Variabls for fragment
    public static int navItemIndex = 0;
    private boolean shouldLoadHomeFragOnBackPress = true;
    private static final String TAG_HOME = "home";
    private static final String TAG_SEND = "send";
    private static final String TAG_Contact = "contact";
    private static final String TAG_USER = "user";
    public static String CURRENT_TAG = TAG_HOME;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager=new SessionManager(this);
        sessionManager.login();
        IntentUI();
      //  FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
       // FirebaseCrashlytics.getInstance().recordException(new RuntimeException("Invalidtoken"));
        UpdateManageCheck();
        EnableRuntimePermission();

        navItemIndex = 0;
        CURRENT_TAG = TAG_HOME;
        displayView();
        ImageSetLight("Home");


      //  showAlertDialogButtonClicked();




    }



    public void showAlertDialogButtonClicked()
    {

        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.permision_dialog, null);
        builder.setView(customLayout);
        TextView tv_not=customLayout.findViewById(R.id.tv_not);
        TextView tv_ok=customLayout.findViewById(R.id.tv_ok);


        AlertDialog dialog
                = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.show();
    }

    public void EnableRuntimePermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {


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
                .setPermissions(Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS)
                .setRationaleConfirmText("OK")
                .check();




    }
/*

    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] PResult) {

        super.onRequestPermissionsResult(RC, per, PResult);
        if (RC == RequestPermissionCode) {
            if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                   // delete();
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
*/


    private void IntentUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        llHome = findViewById(R.id.llHome);
        llContact = findViewById(R.id.llContact);
        llsend = findViewById(R.id.llsend);
        llUser = findViewById(R.id.llUser);
        frameLayout = findViewById(R.id.frameContainer);
        llHome.setOnClickListener(this);
        llContact.setOnClickListener(this);
        llsend.setOnClickListener(this);
        llUser.setOnClickListener(this);
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
        sessionManager.setOneCotect_deatil(getApplicationContext(),new ContectListData.Contact());
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
                llsend.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_blitz_icon));
                llContact.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_contacts));
                llUser.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_user));
                break;
            case "Send":
                llHome.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_home));
                llsend.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_blitz_icon_select));
                llContact.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_contacts));
                llUser.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_user));
                break;
            case "Contact":
                llHome.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_home));
                llsend.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_blitz_icon));
                llContact.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_contacts_selece));
                llUser.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_user));
                break;
            case "User":
                llHome.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_home));
                llsend.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_blitz_icon));
                llContact.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_contacts));
                llUser.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_nav_user_select));
        }
    }

    @Override
    public void onBackPressed() {
        if (shouldLoadHomeFragOnBackPress) {
                if (navItemIndex != 0) {
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;
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

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        switch (v.getId()) {
            case R.id.llHome:
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                displayView();

                ImageSetLight("Home");


                break;
            case R.id.llsend:
                navItemIndex = 1;
                CURRENT_TAG = TAG_Contact;
                displayView();

                ImageSetLight("Send");


                break;
            case R.id.llContact:
                navItemIndex = 2;
                CURRENT_TAG = TAG_SEND;
                displayView();

                ImageSetLight("Contact");


                break;
            case R.id.llUser:
                navItemIndex = 3;
                CURRENT_TAG = TAG_USER;
                displayView();

                ImageSetLight("User");

                break;

        }

    }

    public void displayView() {

        Fragment fragment = null;
        switch (navItemIndex) {
            case 0:

                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new SendFragment();
                break;
            case 2:
                fragment = new Contect_main_Fragment();
                break;
            case 3:
                fragment = new UsetProgileFragment();
                break;
        }
        if (fragment != null) {



            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, fragment, CURRENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }

    }




    public void delete()
    {
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
                //   Log.e("Delete Task","Yes"+c);
                super.onPostExecute(aVoid);

            }
        }

        DeleteTask ut = new DeleteTask();
        ut.execute();
    }



}