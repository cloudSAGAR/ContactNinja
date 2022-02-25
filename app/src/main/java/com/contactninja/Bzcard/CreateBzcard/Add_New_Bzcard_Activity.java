package com.contactninja.Bzcard.CreateBzcard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.contactninja.Bzcard.CreateBzcard.Fragment.Information_Bzcard_Fragment;
import com.contactninja.Bzcard.CreateBzcard.Fragment.Media_Block_Bzcard_Fragment;
import com.contactninja.Bzcard.CreateBzcard.Fragment.Social_media_Bzcard_Fragment;
import com.contactninja.Model.Bzcard_Model;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class Add_New_Bzcard_Activity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    CoordinatorLayout mMainLayout;
    FrameLayout frameContainer;
    TabLayout tabLayout;
    ImageView iv_back, iv_dummy_cover_img,
            iv_cover_img, pulse_icon, iv_edit;
    LinearLayout layout_cover;
    int image_flag = 1;
    Uri mCapturedImageURI;
    Integer CAPTURE_IMAGE = 3;
    String cover_filePath = "",profile_filePath,cover_profile_image="";
    private BroadcastReceiver mNetworkReceiver;
    private long mLastClickTime = 0;
    CircleImageView iv_user;
    Bzcard_Model bzcard_model;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_bzcard);
        mNetworkReceiver = new ConnectivityReceiver();
        initUI();
        setTab();
        bzcard_model=new Bzcard_Model();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        tabLayout = findViewById(R.id.tabLayout);
        frameContainer = findViewById(R.id.frameContainer_bzcars);
        layout_cover = findViewById(R.id.layout_cover);
        iv_dummy_cover_img = findViewById(R.id.iv_dummy_cover_img);
        iv_cover_img = findViewById(R.id.iv_cover_img);
        pulse_icon = findViewById(R.id.pulse_icon);
        iv_user = findViewById(R.id.iv_user);
        iv_edit = findViewById(R.id.iv_edit);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        iv_dummy_cover_img.setOnClickListener(this);
        iv_cover_img.setOnClickListener(this);
        pulse_icon.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        iv_user.setOnClickListener(this);
    }

    private void setTab() {
        // loadingDialog = new LoadingDialog(this);
        //Set Viewpagger
        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Media Block"));
        tabLayout.addTab(tabLayout.newTab().setText("social media"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        Fragment fragment = new Information_Bzcard_Fragment();
        FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer_bzcars, fragment, "Fragment");
            fragmentTransaction.commitAllowingStateLoss();
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new Information_Bzcard_Fragment();

                        break;
                    case 1:
                        fragment = new Media_Block_Bzcard_Fragment();

                        break;
                    case 2:
                        fragment = new Social_media_Bzcard_Fragment();

                        break;

                }
                if (fragment != null) {
                    androidx.fragment.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frameContainer_bzcars, fragment, "Fragment");
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Add_New_Bzcard_Activity.this, mMainLayout);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_dummy_cover_img:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (checkAndRequestPermissions(Add_New_Bzcard_Activity.this)) {
                    cover_profile_image="cover";
                    captureimageDialog(false);
                }
                break;

            case R.id.pulse_icon:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (checkAndRequestPermissions(Add_New_Bzcard_Activity.this)) {
                    cover_profile_image="profile";
                    captureimageDialog(false);
                }
                break;

            case R.id.iv_cover_img:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (checkAndRequestPermissions(Add_New_Bzcard_Activity.this)) {
                    cover_profile_image="cover";
                    captureimageDialog(false);
                }
                break;
            case R.id.iv_edit:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (checkAndRequestPermissions(Add_New_Bzcard_Activity.this)) {
                    cover_profile_image="profile";
                    captureimageDialog(false);
                }
                break;

            case R.id.iv_user:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (checkAndRequestPermissions(Add_New_Bzcard_Activity.this)) {
                    cover_profile_image="profile";
                    captureimageDialog(false);
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void captureimageDialog(boolean remove) {
        final View mView = getLayoutInflater().inflate(R.layout.capture_userpicture_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Add_New_Bzcard_Activity.this, R.style.CoffeeDialog);
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
                bottomSheetDialog.dismiss();


            }
        });
        cameraId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                image_flag = 0;
                mLastClickTime = SystemClock.elapsedRealtime();
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
                image_flag = 0;
                mLastClickTime = SystemClock.elapsedRealtime();
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
        if (requestCode == 0) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    File file = new File(result.getUri().getPath());
                    Uri uri = Uri.fromFile(file);

                    if (cover_profile_image.equals("cover"))
                    {
                        cover_filePath = uri.getPath();
                        String profilePath = Global.getPathFromUri(getApplicationContext(), uri);
                        Glide.with(getApplicationContext()).
                                load(resultUri).
                                apply(RequestOptions.bitmapTransform(new RoundedCorners(30))).
                                into(iv_cover_img);
                        iv_cover_img.setVisibility(View.VISIBLE);
                        iv_dummy_cover_img.setVisibility(View.GONE);
                        bzcard_model= SessionManager.getBzcard(getApplicationContext());
                        bzcard_model.setCover_image(cover_filePath);
                        SessionManager.setBzcard(getApplicationContext(),bzcard_model);

                    }
                    else if (cover_profile_image.equals("profile")) {

                        profile_filePath= uri.getPath();
                        String profilePath = Global.getPathFromUri(getApplicationContext(), uri);
                        Glide.with(getApplicationContext()).
                                load(resultUri).
                                apply(RequestOptions.bitmapTransform(new RoundedCorners(30))).
                                into(iv_user);
                        iv_user.setVisibility(View.VISIBLE);
                        pulse_icon.setVisibility(View.GONE);
                        bzcard_model= SessionManager.getBzcard(getApplicationContext());
                        bzcard_model.setProfile_image(profile_filePath);
                        SessionManager.setBzcard(getApplicationContext(),bzcard_model);
                    }


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        } else if (requestCode == CAPTURE_IMAGE) {
            ImageCropFunctionCustom(mCapturedImageURI);
        } else if (requestCode == 203) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    File file = new File(result.getUri().getPath());
                    Uri uri = Uri.fromFile(file);
                    if (cover_profile_image.equals("cover"))
                    {
                        cover_filePath = uri.getPath();
                        String profilePath = Global.getPathFromUri(getApplicationContext(), uri);
                        Glide.with(getApplicationContext()).
                                load(resultUri).
                                apply(RequestOptions.bitmapTransform(new RoundedCorners(30))).
                                into(iv_cover_img);
                        iv_cover_img.setVisibility(View.VISIBLE);
                        iv_dummy_cover_img.setVisibility(View.GONE);
                        bzcard_model= SessionManager.getBzcard(getApplicationContext());
                        bzcard_model.setCover_image(cover_filePath);
                        SessionManager.setBzcard(getApplicationContext(),bzcard_model);
                    }
                    else if (cover_profile_image.equals("profile")) {

                        profile_filePath= uri.getPath();
                        String profilePath = Global.getPathFromUri(getApplicationContext(), uri);
                        Glide.with(getApplicationContext()).
                                load(resultUri).
                                apply(RequestOptions.bitmapTransform(new RoundedCorners(30))).
                                into(iv_user);
                        iv_user.setVisibility(View.VISIBLE);
                        pulse_icon.setVisibility(View.GONE);
                        bzcard_model= SessionManager.getBzcard(getApplicationContext());

                        bzcard_model.setProfile_image(profile_filePath);
                        SessionManager.setBzcard(getApplicationContext(),bzcard_model);

                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        } else {
            if (image_flag == 0) {
                image_flag = 1;
                try {
                    CropImage.activity(data.getData())
                            .start(this);
                } catch (Exception e) {

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


}