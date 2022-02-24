package com.contactninja.Group;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.contactninja.Model.ContectListData;
import com.contactninja.Model.GroupListData;
import com.contactninja.Model.Grouplist;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.UservalidateModel;
import com.contactninja.R;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import com.reddit.indicatorfastscroll.FastScrollItemIndicator;
import com.reddit.indicatorfastscroll.FastScrollerThumbView;
import com.reddit.indicatorfastscroll.FastScrollerView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Final_Group extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    public static UserListDataAdapter userListDataAdapter;
    public static List<ContectListData.Contact> inviteListData = new ArrayList<>();
    Uri mCapturedImageURI;
    String filePath1="";
    int image_flag = 1;
    Integer CAPTURE_IMAGE = 3;
    GroupListData groupListData;
    TextView save_button;
    ImageView iv_Setting, iv_back;
    String fragment_name, user_image_Url;
    EditText add_new_contect, add_detail, ev_search;
    LinearLayout add_new_member;
    RecyclerView contect_list_unselect;
    RecyclerView.LayoutManager layoutManager;
    FastScrollerView fastscroller;
    FastScrollerThumbView fastscroller_thumb;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    RoundedImageView iv_user;
    ImageView iv_dummy;
    String group_name, group_description, File_name, File_extension;
    LinearLayout mMainLayout;
    RetrofitCalls retrofitCalls;
    String old_image = "", group_id = "";
    TextView topic_remainingCharacter;
    private long mLastClickTime = 0;
    private BroadcastReceiver mNetworkReceiver;

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
            return new String(Base64.decode(str, 0), "UTF-8");
        } catch (UnsupportedEncodingException | IllegalArgumentException unused) {
            return "";
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static TransferUtility transferUtility;
    AmazonS3Client s3Client;
    S3Uploader s3uploaderObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_group);
        inviteListData.clear();
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        Global.checkConnectivity(Final_Group.this, mMainLayout);
        s3uploaderObj = new S3Uploader(this);
        // Create an S3 client
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-2:a47e1f07-8030-4bce-b50b-075713507665", // Identity pool ID
                Regions.US_EAST_2 // Region
        );

        s3Client = new AmazonS3Client(credentialsProvider);
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_2));
        transferUtility = new TransferUtility(s3Client, getApplicationContext());

        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        if (SessionManager.getGroupData(this) != null) {
            Grouplist.Group group_data = SessionManager.getGroupData(this);
            add_new_contect.setText(group_data.getGroupName());
            add_detail.setText(group_data.getDescription());
            if (group_data.getGroupImage() != null) {
                iv_user.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).
                        load(group_data.getGroupImage()).
                        placeholder(R.drawable.shape_primary_back).
                        error(R.drawable.shape_primary_back).into(iv_user);
                iv_dummy.setVisibility(View.GONE);
            } else {
                iv_user.setVisibility(View.GONE);
                iv_dummy.setVisibility(View.VISIBLE);
            }


            user_image_Url = group_data.getGroupImage();

            group_id = String.valueOf(group_data.getId());
            Log.e("Group id is", group_id);
            if (group_id.equals("")) {
                group_id = "";
            } else if (group_id.equals("null")) {
                group_id = "";
            } else {
                group_id = group_id;
            }


        }

        iv_user.setOnClickListener(this);
        iv_Setting.setVisibility(View.GONE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Save");
        save_button.setVisibility(View.VISIBLE);
        inviteListData.addAll(sessionManager.getGroupList(this));
        //   Log.e("Data Is ",new Gson().toJson(sessionManager.getGroupList(this)));
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

        add_new_member.setOnClickListener(this);

        fastscroller.setupWithRecyclerView(
                contect_list_unselect,
                (position) -> {
                    // ItemModel item = data.get(position);
                    try {
                        FastScrollItemIndicator fastScrollItemIndicator = new FastScrollItemIndicator.Text(
                                inviteListData.get(position).getFirstname().substring(0, 1)
                                        .substring(0, 1)
                                        .toUpperCase()// Grab the first letter and capitalize it
                        );
                        return fastScrollItemIndicator;
                    } catch (Exception e) {

                        return null;

                    }

                }
        );

        Collections.sort(inviteListData, new Comparator<ContectListData.Contact>() {
            public int compare(ContectListData.Contact obj1, ContectListData.Contact obj2) {
                return obj1.getFirstname().compareToIgnoreCase(obj1.getFirstname());
            }
        });
        userListDataAdapter = new UserListDataAdapter(this, getApplicationContext(), inviteListData);
        contect_list_unselect.setAdapter(userListDataAdapter);
        loadingDialog = new LoadingDialog(this);

        ev_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Global.hideKeyboard(Final_Group.this);
                    List<ContectListData.Contact> temp = new ArrayList();
                    for (ContectListData.Contact d : inviteListData) {
                        if (d.getFirstname().contains(ev_search.getText().toString())) {
                            temp.add(d);
                            //Log.e("Same Data ",d.getFirstname());
                        }
                    }

                    userListDataAdapter = new UserListDataAdapter(Final_Group.this, getApplicationContext(), inviteListData);
                    contect_list_unselect.setAdapter(userListDataAdapter);
                    userListDataAdapter.notifyDataSetChanged();
                    userListDataAdapter.updateList(temp);
                    return true;
                }
                return false;
            }
        });
        deleteCache(this);

        add_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.e("Test Clcik ", String.valueOf(charSequence));
                if (charSequence.toString().length() <= 100) {
                    int num = 100 - charSequence.toString().length();
                    topic_remainingCharacter.setText(num + " " + getResources().getString(R.string.camp_remaingn));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //    topic_remainingCharacter.setText(100 - editable.length() + " Characters Remaining.");

            }
        });
    }
    // Handled permission Result

    private void IntentUI() {
        topic_remainingCharacter = findViewById(R.id.topic_remainingCharacter);
        save_button = findViewById(R.id.save_button);
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        add_new_contect = findViewById(R.id.add_new_contect);
        add_detail = findViewById(R.id.add_detail);
        add_new_member = findViewById(R.id.add_new_member);
        layoutManager = new LinearLayoutManager(this);
        contect_list_unselect = findViewById(R.id.contect_list_unselect);
        contect_list_unselect.setLayoutManager(layoutManager);
        fastscroller = findViewById(R.id.fastscroller);
        fastscroller_thumb = findViewById(R.id.fastscroller_thumb);
        ev_search = findViewById(R.id.ev_search);
        iv_user = findViewById(R.id.iv_user);
        iv_dummy = findViewById(R.id.iv_dummy);
        iv_dummy.setOnClickListener(this);
        mMainLayout = findViewById(R.id.mMainLayout);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                SessionManager.setGroupData(getApplicationContext(), new Grouplist.Group());
                finish();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                group_name = add_new_contect.getText().toString().trim();
                group_description = add_detail.getText().toString();
                if (group_name.equals("")) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, getString(R.string.add_group_txt), false);
                } else if (group_description.equals("")) {
                    Global.Messageshow(getApplicationContext(), mMainLayout, getString(R.string.add_group_description), false);
                } else {
                    uploadImageTos3(filePath1);
                }
                break;
            case R.id.add_new_member:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                sessionManager.setGroupList(getApplicationContext(), inviteListData);
                startActivity(new Intent(getApplicationContext(), GroupActivity.class));
                finish();

                break;
            case R.id.iv_dummy:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (checkAndRequestPermissions(Final_Group.this)) {
                    captureimageDialog(false);
                }


                break;

            case R.id.iv_user:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if (checkAndRequestPermissions(Final_Group.this)) {
                    captureimageDialog(true);
                }


                break;
            default:


        }
    }

    private void SaveEvent() throws JSONException {


         //   loadingDialog.showLoadingDialog();

            SignResponseModel user_data = sessionManager.getGetUserdata(this);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < inviteListData.size(); i++) {
                Log.e("Contec List Size", String.valueOf(inviteListData.get(0).getContactDetails().size()));
                JSONObject paramObject1 = new JSONObject();

                for (int j = 0; j < inviteListData.get(i).getContactDetails().size(); j++) {
                    if (inviteListData.get(i).getContactDetails().get(j).getType().equals("NUMBER")) {
                        paramObject1.put("mobile", inviteListData.get(i).getContactDetails().get(j).getEmailNumber());
                        paramObject1.put("prospect_id", inviteListData.get(i).getContactDetails().get(j).getContactId());
                    } else {
                        if (!inviteListData.get(i).getContactDetails().get(j).getEmailNumber().equals(" ")) {
                            paramObject1.put("email", inviteListData.get(i).getContactDetails().get(j).getEmailNumber());
                            paramObject1.put("prospect_id", inviteListData.get(i).getContactDetails().get(j).getContactId());
                        }
                    }
                    //break;
                }

                jsonArray.put(paramObject1);
            }

            // contect_array.put(3);
            String token = Global.getToken(sessionManager);
            JSONObject obj = new JSONObject();
            JSONObject paramObject = new JSONObject();
            if (!group_id.equals("")) {
                paramObject.put("id", group_id);
            }

            paramObject.put("group_name", group_name);
          /*  if(old_image!=null){
                paramObject.put("oldImage", old_image);
            }*/

            paramObject.put("group_image", user_image_Url);
            paramObject.put("organization_id", 1);
            paramObject.put("team_id", 1);
            paramObject.put("user_id", user_data.getUser().getId());
            paramObject.put("contact_ids", jsonArray);
            paramObject.put("description", group_description);
            obj.put("data", paramObject);
            //Log.e("Data IS ",new Gson().toJson(obj));

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
            //  Log.e("Obbject data",new Gson().toJson(gsonObject));
            retrofitCalls.AddGroup(sessionManager, gsonObject, loadingDialog, token, Global.getVersionname(Final_Group.this), Global.Device, new RetrofitCallback() {
                @Override
                public void success(Response<ApiResponse> response) {

                    loadingDialog.cancelLoading();
                    if (response.body().getHttp_status() == 200) {
                        finish();
                    } else {
                        Gson gson = new Gson();
                        String headerString = gson.toJson(response.body().getData());
                        Log.e("String is", response.body().getMessage());
                        Type listType = new TypeToken<UservalidateModel>() {
                        }.getType();
                        UservalidateModel user_model = new Gson().fromJson(headerString, listType);
                        Global.Messageshow(getApplicationContext(), mMainLayout, user_model.getEmail().get(0), false);
                    }
                }

                @Override
                public void error(Response<ApiResponse> response) {
                    loadingDialog.cancelLoading();
                }
            });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Glide.with(getApplicationContext()).load(resultUri).into(iv_user);
                    iv_user.setVisibility(View.VISIBLE);
                    iv_dummy.setVisibility(View.GONE);
                    File_name = "Image";
                    File file = new File(result.getUri().getPath());
                    Uri uri = Uri.fromFile(file);
                    filePath1 = uri.getPath();

                  //  uploadImageTos3(filePath1);


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        } else if (requestCode == CAPTURE_IMAGE) {
            ImageCropFunctionCustom(mCapturedImageURI);
        }
        else if (requestCode == 203) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    File file = new File(result.getUri().getPath());
                    Uri uri = Uri.fromFile(file);
                    Glide.with(getApplicationContext()).load(resultUri).into(iv_user);
                    iv_user.setVisibility(View.VISIBLE);
                    iv_dummy.setVisibility(View.GONE);
                    filePath1 = uri.getPath();
                    String profilePath = Global.getPathFromUri(getApplicationContext(), uri);
                    //uploadImageTos3(filePath1);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }
        else {
            if (image_flag == 0) {
                image_flag = 1;
                CropImage.activity(data.getData())
                        .start(this);
            }

        }

    }
    private void uploadImageTos3(String imageUri) {
        //   final String path = getRealPathFromURI(imageUri);
        loadingDialog.showLoadingDialog();
        if (!imageUri.equals("")) {
            //String[] nameList = imageUri.split("/");
            // String uploadFileName = nameList[nameList.length - 1];
            old_image=user_image_Url;
            String contect_group=s3uploaderObj.initUpload(imageUri,"contact_group");
            s3uploaderObj.setOns3UploadDone(new S3Uploader.S3UploadInterface() {
                @Override
                public void onUploadSuccess(String response) {
                    Log.e("Reppnse is",new Gson().toJson(response));
                    Toast.makeText(Final_Group.this, new Gson().toJson(response), Toast.LENGTH_SHORT).show();

                    if (response.equalsIgnoreCase("Success")) {
                        user_image_Url=contect_group;
                        try {
                            if (Global.isNetworkAvailable(Final_Group.this, mMainLayout)) {
                                SaveEvent();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onUploadError(String response) {

                    try {
                        if (Global.isNetworkAvailable(Final_Group.this, mMainLayout)) {
                            SaveEvent();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }else{
          //  Toast.makeText(this, "Null Path", Toast.LENGTH_SHORT).show();
            try {
                if (Global.isNetworkAvailable(Final_Group.this, mMainLayout)) {
                    SaveEvent();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void ImageCropFunctionCustom(Uri uri) {
        Intent intent = CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(this);
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void captureimageDialog(boolean remove) {
        final View mView = getLayoutInflater().inflate(R.layout.capture_userpicture_dialog_item, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Final_Group.this, R.style.CoffeeDialog);
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
                iv_dummy.setVisibility(View.VISIBLE);
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
                mLastClickTime = SystemClock.elapsedRealtime();
               /* Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
*/
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
               /* Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
               */
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        SessionManager.setGroupList(getApplicationContext(), new ArrayList<>());
        SessionManager.setGroupData(getApplicationContext(), new Grouplist.Group());
        super.onBackPressed();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Final_Group.this, mMainLayout);
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

    public class UserListDataAdapter extends RecyclerView.Adapter<UserListDataAdapter.InviteListDataclass> {

        private final Context mcntx;
        private final List<ContectListData.Contact> userDetailsfull;
        public Activity mCtx;
        int last_postion = 0;
        String second_latter = "";
        String current_latter = "", image_url = "";
        private List<ContectListData.Contact> userDetails;

        public UserListDataAdapter(Activity Ctx, Context mCtx, List<ContectListData.Contact> userDetails) {
            this.mcntx = mCtx;
            this.mCtx = Ctx;
            this.userDetails = userDetails;
            userDetailsfull = new ArrayList<>(userDetails);
        }

        @NonNull
        @Override
        public UserListDataAdapter.InviteListDataclass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.invite_user_details, parent, false);
            return new UserListDataAdapter.InviteListDataclass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserListDataAdapter.InviteListDataclass holder, int position) {
            ContectListData.Contact inviteUserDetails = userDetailsfull.get(position);
            last_postion = position;
            //Log.e("Size is", String.valueOf(userDetailsfull.size()));

            holder.remove_contect_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    //  Log.e("Posirion is", String.valueOf(position));
                    userDetails.get(position).setFlag("true");
                    removeite(position);
                    //  Log.e("Main Data Is",new Gson().toJson(inviteListData));
                    /* holder.main_layout.setVisibility(View.GONE);
                     */

                }
            });
               /* if (userDetailsfull.get(position).getFlag().toString().equals("null"))
                {
                    holder.remove_contect_icon.setVisibility(View.VISIBLE);
                    holder.add_new_contect_icon.setVisibility(View.GONE);
                }
                else */
         /*   if (userDetailsfull.get(position).getFlag().equals("false")) {
                holder.remove_contect_icon.setVisibility(View.VISIBLE);
                holder.add_new_contect_icon1.setVisibility(View.GONE);
            } else {
                holder.remove_contect_icon.setVisibility(View.GONE);
                holder.add_new_contect_icon1.setVisibility(View.VISIBLE);
            }*/

            holder.remove_contect_icon.setVisibility(View.VISIBLE);
            holder.add_new_contect_icon1.setVisibility(View.GONE);




/*
            holder.add_new_contect_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.remove_contect_icon.setVisibility(View.VISIBLE);
                    holder.add_new_contect_icon.setVisibility(View.GONE);
                    userDetailsfull.get(position).setFlag("false");
                    save_button.setTextColor(getColor(R.color.purple_200));
                    inviteListData.add(userDetails.get(position));

                }
            });
*/


            holder.first_latter.setVisibility(View.VISIBLE);
            holder.top_layout.setVisibility(View.VISIBLE);
            String first_latter = inviteUserDetails.getFirstname().substring(0, 1).toUpperCase();
            holder.first_latter.setText(first_latter);

            if (second_latter.equals("")) {
                current_latter = first_latter;
                second_latter = first_latter;
                holder.first_latter.setVisibility(View.VISIBLE);
                holder.top_layout.setVisibility(View.VISIBLE);

            } else if (second_latter.equals(first_latter)) {
                current_latter = second_latter;
                //inviteUserDetails.setF_latter("");
                holder.first_latter.setVisibility(View.GONE);
                holder.top_layout.setVisibility(View.GONE);

            } else {

                current_latter = first_latter;
                second_latter = first_latter;
                holder.first_latter.setVisibility(View.VISIBLE);
                holder.top_layout.setVisibility(View.VISIBLE);


            }


            String file = "" + inviteUserDetails.getContactImage();
            if (file.equals("null")) {
                holder.no_image.setVisibility(View.VISIBLE);
                holder.profile_image.setVisibility(View.GONE);
                String name = inviteUserDetails.getFirstname();
                holder.profile_image.setVisibility(View.GONE);
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


                holder.no_image.setText(add_text);
                holder.no_image.setVisibility(View.VISIBLE);

            } else {
                image_url = inviteUserDetails.getContactImage();

                if (holder.profile_image.getDrawable() == null) {
                    Glide.with(mCtx).
                            load(inviteUserDetails.getContactImage())
                            .placeholder(R.drawable.shape_primary_circle)
                            .error(R.drawable.shape_primary_circle)
                            .into(holder.profile_image);
                    //Log.e("Image ","View "+position);
                } else {
                    holder.profile_image.setVisibility(View.GONE);
                    String name = inviteUserDetails.getFirstname();
                    String add_text = "";
                    String[] split_data = name.split(" ");
                    for (int i = 0; i < split_data.length; i++) {
                        if (i == 0) {
                            add_text = split_data[i].substring(0, 1);
                        } else {
                            add_text = add_text + split_data[i].charAt(0);
                        }
                    }
                    holder.no_image.setText(add_text);
                    holder.no_image.setVisibility(View.VISIBLE);
                }

            }
            try {
                if (inviteUserDetails.getLastname().equals("")) {
                    holder.userName.setText(inviteUserDetails.getFirstname());
                } else {
                    holder.userName.setText(inviteUserDetails.getFirstname() + " " + inviteUserDetails.getLastname());
                }
            } catch (Exception e) {
                holder.userName.setText(inviteUserDetails.getFirstname());
            }

            // holder.userNumber.setText();


        }

        @Override
        public int getItemCount() {
            return userDetails.size();
        }


        public void updateList(List<ContectListData.Contact> list) {
            userDetails = list;
            notifyDataSetChanged();
        }

        public void removeite(int value) {
            inviteListData.remove(value);
            userListDataAdapter = new UserListDataAdapter(Final_Group.this, getApplicationContext(), inviteListData);
            contect_list_unselect.setAdapter(userListDataAdapter);
            userListDataAdapter.notifyDataSetChanged();


        }

        public class InviteListDataclass extends RecyclerView.ViewHolder {

            TextView no_image;
            TextView userName, userNumber, first_latter;
            CircleImageView profile_image;
            LinearLayout top_layout;
            ImageView add_new_contect_icon1, remove_contect_icon;
            RelativeLayout main_layout;


            public InviteListDataclass(@NonNull View itemView) {
                super(itemView);
                first_latter = itemView.findViewById(R.id.first_latter);
                userName = itemView.findViewById(R.id.username);
                userNumber = itemView.findViewById(R.id.user_number);
                profile_image = itemView.findViewById(R.id.profile_image);
                no_image = itemView.findViewById(R.id.no_image);
                top_layout = itemView.findViewById(R.id.top_layout);
                add_new_contect_icon1 = itemView.findViewById(R.id.add_new_contect_icon);
                remove_contect_icon = itemView.findViewById(R.id.remove_contect_icon);
                add_new_contect_icon1.setVisibility(View.VISIBLE);
                main_layout = itemView.findViewById(R.id.main_layout);

            }

        }

    }

}