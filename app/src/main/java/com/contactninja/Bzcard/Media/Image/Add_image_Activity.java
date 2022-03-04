package com.contactninja.Bzcard.Media.Image;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.contactninja.Bzcard.Media.Select_Media_Activity;
import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.Bzcard_Fields_Model;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("SimpleDateFormat,StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Add_image_Activity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private BroadcastReceiver mNetworkReceiver;
    RelativeLayout mMainLayout, layout_Image_add;
    ImageView iv_back, iv_Featured_star_on, iv_Featured_star_off, iv_Featured;
    TextView save_button, txt_Featured;
    SessionManager sessionManager;
    List<Bzcard_Fields_Model.BZ_media_information> bzMediaInformationList = new ArrayList<>();
    BZcardListModel.Bizcard model;
    Bzcard_Fields_Model.BZ_media_information information;
    EditText edt_image_title, edt_Add_description;
    RoundedImageView iv_image;
    Integer is_featured = 0;
    LinearLayout layout_replace, layout_Cancel, layout_featured;
    private long mLastClickTime = 0;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    String olld_image = "", File_name = "",SelectImagePath="";
    Uri mCapturedImageURI;
    Integer CAPTURE_IMAGE = 3;
    int image_flag = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        mNetworkReceiver = new ConnectivityReceiver();
        sessionManager = new SessionManager(this);
        model = SessionManager.getBzcard(Add_image_Activity.this);
        bzMediaInformationList = model.getBzcardFieldsModel().getBzMediaInformationList();
        initUI();
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            information = (Bzcard_Fields_Model.BZ_media_information) getIntent().getSerializableExtra("MyClass");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Global.IsNotNull(information)) {
            setImage_info();
        }

    }

    private void setImage_info() {
        layout_Image_add.setVisibility(View.GONE);

        edt_image_title.setText(information.getMedia_title());
        edt_Add_description.setText(information.getMedia_description());
        Glide.with(getApplicationContext())
                .load(information.getMedia_filePath())
                .into(iv_image);
        SelectImagePath=information.getMedia_filePath();
        olld_image=information.getMedia_filePath();
        is_featured = information.getIs_featured();
        layout_featured.setVisibility(View.VISIBLE);
        layout_Cancel.setVisibility(View.VISIBLE);
        if (is_featured == 1) {
            iv_Featured.setVisibility(View.VISIBLE);
            txt_Featured.setText(getResources().getString(R.string.Remove_featured));
            iv_Featured_star_off.setVisibility(View.VISIBLE);
            iv_Featured_star_on.setVisibility(View.GONE);
        } else {
            iv_Featured.setVisibility(View.GONE);
            iv_Featured_star_on.setVisibility(View.VISIBLE);
            iv_Featured_star_off.setVisibility(View.GONE);
            txt_Featured.setText(getResources().getString(R.string.add_featured));
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        edt_image_title = findViewById(R.id.edt_image_title);
        edt_Add_description = findViewById(R.id.edt_Add_description);
        iv_image = findViewById(R.id.iv_image);
        layout_replace = findViewById(R.id.layout_replace);
        layout_Cancel = findViewById(R.id.layout_Cancel);
        iv_Featured_star_on = findViewById(R.id.iv_Featured_star_on);
        iv_Featured_star_off = findViewById(R.id.iv_Featured_star_off);
        iv_Featured = findViewById(R.id.iv_Featured);
        layout_featured = findViewById(R.id.layout_featured);
        layout_Image_add = findViewById(R.id.layout_Image_add);
        txt_Featured = findViewById(R.id.txt_Featured);


        layout_replace.setOnClickListener(this);
        layout_Cancel.setOnClickListener(this);
        layout_featured.setOnClickListener(this);
        layout_Image_add.setOnClickListener(this);
        iv_image.setOnClickListener(this);

        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Add_image_Activity.this, mMainLayout);
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
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.layout_Cancel:
                for (int i = 0; i < bzMediaInformationList.size(); i++) {
                    if (bzMediaInformationList.get(i).getId().equals(information.getId())) {
                        bzMediaInformationList.remove(i);
                        model.getBzcardFieldsModel().setBzMediaInformationList(bzMediaInformationList);
                        SessionManager.setBzcard(Add_image_Activity.this, model);
                        break;
                    }
                }
                intent = new Intent(getApplicationContext(), Select_Media_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.layout_featured:
                if (is_featured == 0) {
                    iv_Featured.setVisibility(View.VISIBLE);
                    txt_Featured.setText(getResources().getString(R.string.Remove_featured));
                    iv_Featured_star_on.setVisibility(View.GONE);
                    iv_Featured_star_off.setVisibility(View.VISIBLE);
                    for (int i = 0; i < bzMediaInformationList.size(); i++) {
                        if (bzMediaInformationList.get(i).getIs_featured() == 1) {
                            bzMediaInformationList.get(i).setIs_featured(0);
                            break;
                        }
                    }
                    is_featured = 1;
                } else {
                    iv_Featured.setVisibility(View.GONE);
                    is_featured = 0;
                    iv_Featured_star_on.setVisibility(View.VISIBLE);
                    iv_Featured_star_off.setVisibility(View.GONE);
                    txt_Featured.setText(getResources().getString(R.string.add_featured));
                }

                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (Vlidaction()) {

                    if (Global.IsNotNull(information)) {
                        for (int i = 0; i < bzMediaInformationList.size(); i++) {
                            if (bzMediaInformationList.get(i).getId().equals(information.getId())) {
                                Bzcard_Fields_Model.BZ_media_information information = new Bzcard_Fields_Model.BZ_media_information();
                                information.setId(bzMediaInformationList.get(i).getId());
                                information.setMedia_type(bzMediaInformationList.get(i).getMedia_type());
                                information.setMedia_filePath(SelectImagePath);
                                information.setMedia_title(edt_image_title.getText().toString().trim());
                                information.setMedia_description(edt_Add_description.getText().toString().trim());
                                information.setIs_featured(is_featured);
                                bzMediaInformationList.set(i, information);
                                model.getBzcardFieldsModel().setBzMediaInformationList(bzMediaInformationList);
                                SessionManager.setBzcard(Add_image_Activity.this, model);

                                break;
                            }
                        }
                    } else {
                        Bzcard_Fields_Model.BZ_media_information information = new Bzcard_Fields_Model.BZ_media_information();
                        information.setId(bzMediaInformationList.size());
                        information.setMedia_type("image");
                        information.setMedia_filePath(SelectImagePath);
                        information.setMedia_title(edt_image_title.getText().toString().trim());
                        information.setMedia_description(edt_Add_description.getText().toString().trim());
                        if (bzMediaInformationList.size() == 0) {
                            information.setIs_featured(1);
                        } else {
                            information.setIs_featured(0);
                        }
                        bzMediaInformationList.add(information);
                        model.getBzcardFieldsModel().setBzMediaInformationList(bzMediaInformationList);
                        SessionManager.setBzcard(Add_image_Activity.this, model);
                    }
                    intent = new Intent(getApplicationContext(), Select_Media_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.layout_Image_add:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (checkAndRequestPermissions(Add_image_Activity.this)) {
                    captureimageDialog(false);
                }
                break;
            case R.id.layout_replace:
            case R.id.iv_image:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (checkAndRequestPermissions(Add_image_Activity.this)) {
                    if (olld_image != null && !olld_image.equals("") || SelectImagePath != null && !SelectImagePath.equals("")) {
                        captureimageDialog(true);
                    } else {
                        captureimageDialog(false);
                    }

                }

                break;
        }
    }

    private void captureimageDialog(boolean remove) {
        final View mView = getLayoutInflater().inflate(R.layout.capture_userpicture_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Add_image_Activity.this, R.style.CoffeeDialog);
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
                iv_image.setVisibility(View.GONE);
                layout_Image_add.setVisibility(View.VISIBLE);

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
               /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 0);*/
                image_flag = 0;
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
                mLastClickTime = SystemClock.elapsedRealtime();
/*                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);*/


                image_flag = 0;
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

        if (requestCode == 0) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();


                    File_name = "Image";
                    File file = new File(result.getUri().getPath());
                    Uri uri = Uri.fromFile(file);
                    SelectImagePath = resultUri.getPath();
                    String profilePath = Global.getPathFromUri(getApplicationContext(), uri);
                    iv_image.setVisibility(View.VISIBLE);
                    layout_Image_add.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(resultUri).into(iv_image);


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
                    SelectImagePath = resultUri.getPath();
                    iv_image.setVisibility(View.VISIBLE);
                    layout_Image_add.setVisibility(View.GONE);
                    Glide.with(getApplicationContext()).load(resultUri).into(iv_image);
                    // uploadImageTos3(filePath1);


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
                    e.printStackTrace();
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

    private boolean Vlidaction() {
        if (edt_image_title.getText().toString().trim().equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.image_title_error), false);
        } else if (edt_Add_description.getText().toString().trim().equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.image_description_error), false);
        } else if (SelectImagePath.equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.select_image), false);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}