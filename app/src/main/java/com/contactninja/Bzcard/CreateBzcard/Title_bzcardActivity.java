package com.contactninja.Bzcard.CreateBzcard;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.contactninja.MainActivity;
import com.contactninja.Model.Bzcard_Fields_Model;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.aws.AmazonUtil;
import com.contactninja.aws.S3Uploader;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Title_bzcardActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
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
    int UploadCount = 0, TotalFileUpload = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_bzcard);
        mNetworkReceiver = new ConnectivityReceiver();

        main_model = SessionManager.getBzcard(this);
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
                    if (Global.IsNotNull(main_model.getProfile_image())) {
                        TotalFileUpload++;
                    }
                    if (Global.IsNotNull(main_model.getCover_image())) {
                        TotalFileUpload++;

                    }
                    if (Global.IsNotNull(main_model.getCompany_logo())) {
                        TotalFileUpload++;
                    }
                    if (Global.IsNotNull(bzMediaInformationList)||bzMediaInformationList.size()!=0) {
                        TotalFileUpload=TotalFileUpload+bzMediaInformationList.size();
                    }
                    Uplod_inage_and_audio();


                }

                break;

        }
    }

    private void Uplod_inage_and_audio() {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Title_bzcardActivity.this);

        if (Global.IsNotNull(main_model.getProfile_image())) {
            s3uploaderObj = new S3Uploader(Title_bzcardActivity.this);
            String Bzcard_profile = s3uploaderObj.BZcard_image_Upload(main_model.getProfile_image(),
                    "bzcard" + "/" + "bzcard_" + signResponseModel.getUser().getId() + "_" + main_model.getCard_id(), "pro_A");
            UploadS3();
            main_model.setProfile_url(Bzcard_profile);
            SessionManager.setBzcard(Title_bzcardActivity.this, main_model);
        }
        if (Global.IsNotNull(main_model.getCover_image())) {
            s3uploaderObj = new S3Uploader(Title_bzcardActivity.this);
            String Bzcard_Cover_image = s3uploaderObj.BZcard_image_Upload(main_model.getCover_image(),
                    "bzcard" + "/" + "bzcard_" + signResponseModel.getUser().getId() + "_" + main_model.getCard_id(), "cov_A");
            UploadS3();
            main_model.setCover_url(Bzcard_Cover_image);
            SessionManager.setBzcard(Title_bzcardActivity.this, main_model);
        }
        if (Global.IsNotNull(main_model.getCompany_logo())) {
            s3uploaderObj = new S3Uploader(Title_bzcardActivity.this);
            String Bzcard_Company_logo = s3uploaderObj.BZcard_image_Upload(main_model.getCompany_logo(),
                    "bzcard" + "/" + "bzcard_" + signResponseModel.getUser().getId() + "_" + main_model.getCard_id(), "com_A");
            UploadS3();
            main_model.setCompany_logo_url(Bzcard_Company_logo);
            SessionManager.setBzcard(Title_bzcardActivity.this, main_model);
        }
        if (Global.IsNotNull(bzMediaInformationList)) {

            for (int i = 0; i < bzMediaInformationList.size(); i++) {
                switch (bzMediaInformationList.get(i).getMedia_type()) {
                    case "video": {
                        Bzcard_Fields_Model.BZ_media_information information1 = bzMediaInformationList.get(i);
                        s3uploaderObj = new S3Uploader(Title_bzcardActivity.this);
                        if (!bzMediaInformationList.get(i).getMedia_url().equals("")) {
                            AmazonUtil.deleteS3Client(getApplicationContext(), bzMediaInformationList.get(i).getMedia_url());
                        }
                        String Bzcard_thumbnail = s3uploaderObj.BZcard_image_Upload(bzMediaInformationList.get(i).getMedia_filePath(),
                                "bzcard" + "/" + "bzcard_" + signResponseModel.getUser().getId() + "_" + main_model.getCard_id(), "thu_A");
                        UploadS3();
                        if (bzMediaInformationList.get(i).getId().equals(information1.getId())) {
                            Bzcard_Fields_Model.BZ_media_information information = new Bzcard_Fields_Model.BZ_media_information();
                            information.setId(bzMediaInformationList.get(i).getId());
                            information.setMedia_type(bzMediaInformationList.get(i).getMedia_type());
                            information.setMedia_url(bzMediaInformationList.get(i).getMedia_url());
                            information.setMedia_thumbnail(Bzcard_thumbnail);
                            information.setMedia_filePath(bzMediaInformationList.get(i).getMedia_filePath());
                            information.setMedia_title(bzMediaInformationList.get(i).getMedia_title());
                            information.setMedia_description(bzMediaInformationList.get(i).getMedia_description());
                            information.setIs_featured(bzMediaInformationList.get(i).getIs_featured());
                            bzMediaInformationList.set(i, information);
                            main_model.setBzMediaInformationList(bzMediaInformationList);
                            SessionManager.setBzcard(Title_bzcardActivity.this, main_model);
                        }
                        break;
                    }
                    case "image": {
                        Bzcard_Fields_Model.BZ_media_information information1 = bzMediaInformationList.get(i);
                        s3uploaderObj = new S3Uploader(Title_bzcardActivity.this);
                        if (!bzMediaInformationList.get(i).getMedia_url().equals("")) {
                            AmazonUtil.deleteS3Client(getApplicationContext(), bzMediaInformationList.get(i).getMedia_url());
                        }
                        String Bzcard_image = s3uploaderObj.BZcard_image_Upload(bzMediaInformationList.get(i).getMedia_filePath(),
                                "bzcard" + "/" + "bzcard_" + signResponseModel.getUser().getId() + "_" + main_model.getCard_id(), "img_A");
                        UploadS3();
                        if (bzMediaInformationList.get(i).getId().equals(information1.getId())) {
                            Bzcard_Fields_Model.BZ_media_information information = new Bzcard_Fields_Model.BZ_media_information();
                            information.setId(bzMediaInformationList.get(i).getId());
                            information.setMedia_type(bzMediaInformationList.get(i).getMedia_type());
                            information.setMedia_url(Bzcard_image);
                            information.setMedia_filePath(bzMediaInformationList.get(i).getMedia_filePath());
                            information.setMedia_title(bzMediaInformationList.get(i).getMedia_title());
                            information.setMedia_description(bzMediaInformationList.get(i).getMedia_description());
                            information.setIs_featured(bzMediaInformationList.get(i).getIs_featured());
                            bzMediaInformationList.set(i, information);
                            main_model.setBzMediaInformationList(bzMediaInformationList);
                            SessionManager.setBzcard(Title_bzcardActivity.this, main_model);
                        }
                        break;
                    }
                    case "pdf": {
                        Bzcard_Fields_Model.BZ_media_information information1 = bzMediaInformationList.get(i);
                        s3uploaderObj = new S3Uploader(Title_bzcardActivity.this);
                        if (!bzMediaInformationList.get(i).getMedia_url().equals("")) {
                            AmazonUtil.deleteS3Client(getApplicationContext(), bzMediaInformationList.get(i).getMedia_url());
                        }
                        String Bzcard_image = s3uploaderObj.BZcard_pdf_Upload(bzMediaInformationList.get(i).getMedia_filePath(),
                                "bzcard" + "/" + "bzcard_" + signResponseModel.getUser().getId() + "_" + main_model.getCard_id(), "pdf_A");
                        UploadS3();
                        if (bzMediaInformationList.get(i).getId().equals(information1.getId())) {
                            Bzcard_Fields_Model.BZ_media_information information = new Bzcard_Fields_Model.BZ_media_information();
                            information.setId(bzMediaInformationList.get(i).getId());
                            information.setMedia_type(bzMediaInformationList.get(i).getMedia_type());
                            information.setMedia_url(Bzcard_image);
                            information.setMedia_filePath(bzMediaInformationList.get(i).getMedia_filePath());
                            information.setMedia_title(bzMediaInformationList.get(i).getMedia_title());
                            information.setMedia_description(bzMediaInformationList.get(i).getMedia_description());
                            information.setIs_featured(bzMediaInformationList.get(i).getIs_featured());
                            bzMediaInformationList.set(i, information);
                            main_model.setBzMediaInformationList(bzMediaInformationList);
                            SessionManager.setBzcard(Title_bzcardActivity.this, main_model);
                        }
                        break;
                    }
                }
            }
        } else {
            try {
                if (Global.isNetworkAvailable(Title_bzcardActivity.this, MainActivity.mMainLayout)) {
                    Data_Upload();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    void Data_Upload() throws JSONException {


        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Title_bzcardActivity.this);
        String token = Global.getToken(sessionManager);
        JSONObject obj = new JSONObject();

        JSONObject paramObject = new JSONObject();

        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", signResponseModel.getUser().getId());
        paramObject.put("card_id", main_model.getCard_id());


        JSONObject param = new JSONObject();
        /*Infirmaction key*/
       // if(Global.IsNotNull(main_model.getCover_image())){
            param.put("cover_image", main_model.getCover_image());
       // }
       // if(Global.IsNotNull(main_model.getProfile_url())) {
            param.put("profile_image", main_model.getProfile_url());
       // }
       // if(Global.IsNotNull(main_model.getFirst_name())) {
            param.put("first_name", main_model.getFirst_name());
        //}
        //if(Global.IsNotNull(main_model.getLast_name())) {
            param.put("last_name", main_model.getLast_name());
      //  }
      // if(Global.IsNotNull(main_model.getContant_number())) {
            param.put("phone_number", main_model.getContant_number());
      //  }
      //  if(Global.IsNotNull(main_model.getEmail())) {
            param.put("email_address", main_model.getEmail());
     //   }
     //   if(Global.IsNotNull(main_model.getCompany_name())) {
            param.put("company_name", main_model.getCompany_name());
      //  }
     //   if(Global.IsNotNull(main_model.getCompany_logo_url())) {
            param.put("company_logo", main_model.getCompany_logo_url());
       // }
     //   if(Global.IsNotNull(main_model.getCompany_url())) {
            param.put("company_url", main_model.getCompany_url());
       // }
       // if(Global.IsNotNull(main_model.getJobtitle())) {
            param.put("job_title", main_model.getJobtitle());
      //  }
     //   if(Global.IsNotNull(main_model.getAddrees())) {
            param.put("address", main_model.getAddrees());
      //  }
      //  if(Global.IsNotNull(main_model.getZipcode())) {
            param.put("zipcode", main_model.getZipcode());
      //  }
        /*media key*/
      //  if(Global.IsNotNull(main_model.getThemeColor())) {
            param.put("theme", "");
     //   }
     //   if(Global.IsNotNull(main_model.getThemeColorHash())) {
            param.put("themeColorHash", main_model.getThemeColorHash());
      //  }
     //   if(Global.IsNotNull(main_model.getButton1_name())) {
            param.put("button1_name", main_model.getButton1_name());
      //  }
     //   if(Global.IsNotNull(main_model.getButton1_url())) {
            param.put("button1_url", main_model.getButton1_url());
      //  }
       // if(Global.IsNotNull(main_model.getButton2_name())) {
            param.put("button2_name", main_model.getButton2_name());
      //  }
      //  if(Global.IsNotNull(main_model.getButton2_url())) {
            param.put("button2_url", main_model.getButton2_url());
      //  }
      //  if(Global.IsNotNull(main_model.getBio_head())) {
            param.put("bio_head", main_model.getBio_head());
      //  }
      //  if(Global.IsNotNull(main_model.getBio_description())) {
            param.put("bio_description", main_model.getBio_description());
      //  }
      //  if(Global.IsNotNull(main_model.getHtml())) {
            param.put("html", main_model.getHtml());
      //  }
      //  if(Global.IsNotNull(main_model.getAction_name())) {
            param.put("action_name", main_model.getAction_name());
      //  }
      //  if(Global.IsNotNull(main_model.getAction_url())) {
            param.put("action_url", main_model.getAction_url());
    //    }

       // if(Global.IsNotNull(bzMediaInformationList)||bzMediaInformationList.size()!=0){
            JSONArray jsonArray = new JSONArray();
            JSONObject media_ = null;
            for (int i = 0; i < bzMediaInformationList.size(); i++) {
                media_ = new JSONObject();
                if(Global.IsNotNull(bzMediaInformationList.get(i).getMedia_type())) {
                    media_.put("media_type", bzMediaInformationList.get(i).getMedia_type());
                }
                if(Global.IsNotNull(bzMediaInformationList.get(i).getMedia_url())) {
                    media_.put("media_url", bzMediaInformationList.get(i).getMedia_url());
                }
                if(Global.IsNotNull(bzMediaInformationList.get(i).getMedia_thumbnail())) {
                    media_.put("media_thumbnail", bzMediaInformationList.get(i).getMedia_thumbnail());
                }
                if(Global.IsNotNull(bzMediaInformationList.get(i).getMedia_title())) {
                    media_.put("media_title", bzMediaInformationList.get(i).getMedia_title());
                }
                if(Global.IsNotNull(bzMediaInformationList.get(i).getMedia_description())) {
                    media_.put("media_description", bzMediaInformationList.get(i).getMedia_description());
                }
                if(Global.IsNotNull(bzMediaInformationList.get(i).getIs_featured())) {
                    media_.put("is_featured", bzMediaInformationList.get(i).getIs_featured());
                }

                jsonArray.put(media_);
            }

            param.put("media_information", jsonArray);
       // }



        /*social key*/
        JSONObject social = new JSONObject();
      //  if(Global.IsNotNull(main_model.getFb())) {
            social.put("facebook_url", main_model.getFb());
     //   }
      //  if(Global.IsNotNull(main_model.getTwitter())) {
            social.put("twitter_url", main_model.getTwitter());
      //  }
      //  if(Global.IsNotNull(main_model.getYoutube())) {
            social.put("youtube_url", main_model.getYoutube());
      //  }
     //   if(Global.IsNotNull(main_model.getBreakout())) {
            social.put("breakout_url", main_model.getBreakout());
      //  }
      //  if(Global.IsNotNull(main_model.getInstagram())) {
            social.put("instagram_url", main_model.getInstagram());
     //   }
    //    if(Global.IsNotNull(main_model.getLinkedin())) {
            social.put("linkedin_url", main_model.getLinkedin());
      //  }
      //  if(Global.IsNotNull(main_model.getProfile_url())) {
            social.put("pinterest_url", main_model.getProfile_url());
      //  }
       // if(Global.IsNotNull(main_model.getVenmo())) {
            social.put("venmo_url", main_model.getVenmo());
       // }
       // if(Global.IsNotNull(main_model.getSkypay())) {
            social.put("skype_url", main_model.getSkypay());
      //  }
      //  if(Global.IsNotNull(main_model.getTiktok())) {
            social.put("tiktok_url", main_model.getTiktok());
     //   }
       // if(Global.IsNotNull(main_model.getSnapchat())) {
            social.put("snapchat_url", main_model.getSnapchat());
       // }
      //  if(Global.IsNotNull(main_model.getOther_filed())) {
            social.put("other_1", main_model.getOther_filed());
      //  }
      //  if(Global.IsNotNull(main_model.getOther_filed1())) {
            social.put("other_2", main_model.getOther_filed1());
      //  }
        param.put("social_links", social);
        param.put("first_name", ev_titale.getText().toString().trim());
        paramObject.put("fields_val", param);
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        retrofitCalls.BZcard_Add_Update(sessionManager, gsonObject, loadingDialog, token, Global.getVersionname(Title_bzcardActivity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void error(Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });

    }

    private void UploadS3() {
        s3uploaderObj.setOns3UploadDone(new S3Uploader.S3UploadInterface() {
            @Override
            public void onUploadSuccess(String response) {
                Log.e("Reppnse is", new Gson().toJson(response));
                UploadCount++;

                if (UploadCount == TotalFileUpload) {
                    try {
                        if (Global.isNetworkAvailable(Title_bzcardActivity.this, MainActivity.mMainLayout)) {
                            UploadCount=0;
                            Data_Upload();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onUploadError(String response) {

                loadingDialog.cancelLoading();
                Toast.makeText(Title_bzcardActivity.this, new Gson().toJson(response), Toast.LENGTH_SHORT).show();

            }

        });
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