package com.contactninja.Bzcard.CreateBzcard;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transfermanager.MultipleFileUpload;
import com.amazonaws.mobileconnectors.s3.transfermanager.ObjectMetadataProvider;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.contactninja.AddContect.EmailSend_Activity;
import com.contactninja.Bzcard.Media.Image.Add_image_Activity;
import com.contactninja.Bzcard.Select_Bzcard_Activity;
import com.contactninja.Group.Final_Group;
import com.contactninja.MainActivity;
import com.contactninja.Manual_email_text.Manual_Auto_Task_Name_Activity;
import com.contactninja.Manual_email_text.Manual_Email_Contect_Activity;
import com.contactninja.Manual_email_text.Manual_Text_Contact_Activity;
import com.contactninja.Manual_email_text.Manual_Text_Send_Activty;
import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.Bzcard_Fields_Model;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.aws.AWSKeys;
import com.contactninja.aws.S3Uploader;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Title_bzcardActivity extends AppCompatActivity  implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    ImageView iv_back;
    TextView save_button, tv_remain_txt, tv_error;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    EditText ev_titale;
    private BroadcastReceiver mNetworkReceiver;
    ConstraintLayout mMainLayout;
    String sequence_Name = "";
    private long mLastClickTime = 0;
    S3Uploader s3uploaderObj;
    List<Bzcard_Fields_Model.BZ_media_information> bzMediaInformationList = new ArrayList<>();
    Bzcard_Fields_Model main_model;
    Bzcard_Fields_Model model;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_bzcard);
        mNetworkReceiver = new ConnectivityReceiver();

        main_model = SessionManager.getBzcard(this);
        model = SessionManager.getBzcard(this);
        bzMediaInformationList = main_model.getBzMediaInformationList();
        IntentUI();

        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        tv_remain_txt.setText("40 " + getResources().getString(R.string.camp_remaingn));
        ev_titale.requestFocus();

        if (Global.IsNotNull(sequence_Name)) {
            ev_titale.setText(sequence_Name);
            ev_titale.setSelection(ev_titale.getText().length());
        }

        ev_titale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() <= 40) {
                    int num = 40 - charSequence.toString().length();
                    tv_remain_txt.setText(num + " " + getResources().getString(R.string.camp_remaingn));
                    tv_error.setVisibility(View.GONE);
                } else if (charSequence.toString().length() == 0) {
                    tv_error.setVisibility(View.VISIBLE);
                } else {
                    tv_error.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void IntentUI() {

        mMainLayout = findViewById(R.id.mMainLayout);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Save and preview");
        ev_titale = findViewById(R.id.ev_titale);
        tv_remain_txt = findViewById(R.id.tv_remain_txt);
        tv_error = findViewById(R.id.tv_error);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Global.hideKeyboard(Title_bzcardActivity.this);
                //Add Api Call
                if (ev_titale.getText().toString().trim().equals("")) {
                    tv_error.setVisibility(View.VISIBLE);
                } else {
                    tv_error.setVisibility(View.GONE);
                    Log.e("Name ", SessionManager.getCampaign_type(getApplicationContext()));


                    try {
                        if (Global.isNetworkAvailable(Title_bzcardActivity.this, MainActivity.mMainLayout)) {
                            Data_Upload();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                break;

        }
    }

    void Data_Upload() throws JSONException {

        s3uploaderObj = new S3Uploader(Title_bzcardActivity.this);
        String Bzcard_image = s3uploaderObj.initUpload(bzMediaInformationList.get(1).getMedia_url(), "BZCard" + "/" + "image");
        s3uploaderObj.setOns3UploadDone(new S3Uploader.S3UploadInterface() {
            @Override
            public void onUploadSuccess(String response) {
                Log.e("Reppnse is", new Gson().toJson(response));
                if (response.equalsIgnoreCase("Success")) {
                    List<Bzcard_Fields_Model.BZ_media_information> List = new ArrayList<>();
                    for (int j = 0; j < List.size(); j++) {
                        if (bzMediaInformationList.get(1).getId().equals(List.get(j))) {
                            Bzcard_Fields_Model.BZ_media_information information = new Bzcard_Fields_Model.BZ_media_information();
                            information.setId(bzMediaInformationList.get(1).getId());
                            information.setMedia_type(bzMediaInformationList.get(1).getMedia_type());
                            information.setMedia_url(String.valueOf(Bzcard_image));
                            information.setMedia_title(bzMediaInformationList.get(1).getMedia_title());
                            information.setMedia_description(bzMediaInformationList.get(1).getMedia_description());
                            information.setIs_featured(bzMediaInformationList.get(1).getIs_featured());
                            bzMediaInformationList.set(1, information);
                            model.setBzMediaInformationList(bzMediaInformationList);
                            SessionManager.setBzcard(Title_bzcardActivity.this, model);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onUploadError(String response) {

                loadingDialog.cancelLoading();
                Toast.makeText(Title_bzcardActivity.this, new Gson().toJson(response), Toast.LENGTH_SHORT).show();

            }

        });





        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Title_bzcardActivity.this);
        String token = Global.getToken(sessionManager);
        JsonObject obj = new JsonObject();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("organization_id", 1);
        paramObject.addProperty("team_id", 1);
        paramObject.addProperty("user_id", signResponseModel.getUser().getId());



        obj.add("data", paramObject);




    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Title_bzcardActivity.this, mMainLayout);
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
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}