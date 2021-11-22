package com.intricare.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.intricare.test.Auth.VerificationActivity;
import com.intricare.test.Model.InviteListData;
import com.intricare.test.Utils.App;
import com.intricare.test.Utils.Global;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /*
     * in-app update
     * */
    InstallStateUpdatedListener installStateUpdatedListener;
    private AppUpdateManager mAppUpdateManager;
    private static int RC_APP_UPDATE = 0;
    RelativeLayout mMainLayout;
    public static final int RequestPermissionCode = 1;
    ImageView llHome, llsend, llContact, llUser;
    FrameLayout frameLayout;
    private long mLastClickTime = 0;


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
        IntentUI();
        UpdateManageCheck();
        EnableRuntimePermission();

        navItemIndex = 0;
        CURRENT_TAG = TAG_HOME;
        displayView();
        ImageSetLight("Home");
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
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_CONTACTS)
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
                llHome.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_home));
                llsend.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_blitz_icon));
                llContact.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_contacts));
                llUser.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_user));
                break;
            case "Send":
                llHome.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_home));
                llsend.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_blitz_icon));
                llContact.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_contacts));
                llUser.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_user));
                break;
            case "Contact":
                llHome.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_home));
                llsend.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_blitz_icon));
                llContact.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_contacts));
                llUser.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_user));
                break;
            case "User":
                llHome.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_home));
                llsend.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_blitz_icon));
                llContact.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_contacts));
                llUser.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_user));
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
                //fragment = new ExploreFragment();
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
}