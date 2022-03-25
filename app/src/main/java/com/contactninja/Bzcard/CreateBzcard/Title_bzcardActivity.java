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
import com.contactninja.Model.BZcardListModel;
import com.contactninja.Model.Bzcard_Fields_Model;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.aws.image_aws.AmazonUtil;
import com.contactninja.aws.image_aws.S3Uploader;
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
    ConstraintLayout mMainLayout;
    String card_Name = "";
    S3Uploader s3uploaderObj;
    List<Bzcard_Fields_Model.BZ_media_information> bzMediaInformationList = new ArrayList<>();
    List<Bzcard_Fields_Model.BZ_media_delete> deleteList = new ArrayList<>();
    BZcardListModel.Bizcard bzcard_model;
    int UploadCount = 0, TotalFileUpload = 0;
    private BroadcastReceiver mNetworkReceiver;
    private long mLastClickTime = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_bzcard);
        mNetworkReceiver = new ConnectivityReceiver();

        bzcard_model = SessionManager.getBzcard(this);
        bzMediaInformationList = bzcard_model.getBzcardFieldsModel().getBzMediaInformationList();
        deleteList = bzcard_model.getBzcardFieldsModel().getMedia_deletes();
        IntentUI();

        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        tv_remain_txt.setText("40 " + getResources().getString(R.string.camp_remaingn));
        ev_titale.requestFocus();

        card_Name = bzcard_model.getBzcardFieldsModel().getCard_name();

        if (Global.IsNotNull(card_Name)) {
            ev_titale.setText(card_Name);
            ev_titale.setSelection(ev_titale.getText().length());
        }

        ev_titale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {
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
            public void afterTextChanged (Editable editable) {

            }
        });

    }

    private void IntentUI () {

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
    public void onClick (View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
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
                    if (Global.IsNotNull(bzcard_model.getBzcardFieldsModel().getProfile_url())) {
                        TotalFileUpload++;
                    }
                    if (Global.IsNotNull(bzcard_model.getBzcardFieldsModel().getCover_url())) {
                        TotalFileUpload++;

                    }
                    if (Global.IsNotNull(bzcard_model.getBzcardFieldsModel().getCompany_logo_url())) {
                        TotalFileUpload++;
                    }

                    Uplod_inage_and_audio();


                }

                break;

        }
    }

    private void Uplod_inage_and_audio () {
        loadingDialog.showLoadingDialog();
        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Title_bzcardActivity.this);
        /*first delete image id is available in s3 bucket */



        if (Global.IsNotNull(bzcard_model.getBzcardFieldsModel().getProfile_url())) {
            if (!signResponseModel.getUser().getUserprofile().equals(bzcard_model.getBzcardFieldsModel().getProfile_image())) {
                if (!bzcard_model.getBzcardFieldsModel().getProfile_image().equals("")) {
                    try {
                        AmazonUtil.deleteS3Client(getApplicationContext(), bzcard_model.getBzcardFieldsModel().getProfile_image());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (Global.IsNotNull(bzcard_model.getBzcardFieldsModel().getCover_url())) {
            if (!bzcard_model.getBzcardFieldsModel().getCover_image().equals("")) {
                try {
                    AmazonUtil.deleteS3Client(getApplicationContext(), bzcard_model.getBzcardFieldsModel().getCover_image());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (Global.IsNotNull(bzcard_model.getBzcardFieldsModel().getCompany_logo_url())) {
            if (!bzcard_model.getBzcardFieldsModel().getCompany_logo().equals("")) {
                try {
                    AmazonUtil.deleteS3Client(getApplicationContext(), bzcard_model.getBzcardFieldsModel().getCompany_logo());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (bzcard_model.isEdit()) {
            if (Global.IsNotNull(bzMediaInformationList) && bzMediaInformationList.size() != 0) {

                for (int i = 0; i < bzMediaInformationList.size(); i++) {
                    switch (bzMediaInformationList.get(i).getMedia_type()) {
                        case "video": {
                            if (!bzMediaInformationList.get(i).getMedia_thumbnail().equals("")) {
                                AmazonUtil.deleteS3Client(getApplicationContext(), bzMediaInformationList.get(i).getMedia_thumbnail());
                            }
                            break;
                        }
                        case "image": {
                            if (!bzMediaInformationList.get(i).getMedia_url().equals("")) {
                                AmazonUtil.deleteS3Client(getApplicationContext(), bzMediaInformationList.get(i).getMedia_url());
                            }

                            break;
                        }
                        case "pdf": {
                            if (!bzMediaInformationList.get(i).getMedia_url().equals("")) {
                                AmazonUtil.deleteS3Client(getApplicationContext(), bzMediaInformationList.get(i).getMedia_url());
                            }
                            break;
                        }
                    }
                }
            }
            if (Global.IsNotNull(deleteList) && deleteList.size() != 0) {

                for (int i = 0; i < deleteList.size(); i++) {
                    switch (deleteList.get(i).getMedia_type()) {
                        case "video": {
                            if (!deleteList.get(i).getMedia_url().equals("")) {
                                AmazonUtil.deleteS3Client(getApplicationContext(), bzMediaInformationList.get(i).getMedia_url());
                            }
                            break;
                        }
                        case "image": {
                            if (!deleteList.get(i).getMedia_url().equals("")) {
                                AmazonUtil.deleteS3Client(getApplicationContext(), deleteList.get(i).getMedia_url());
                            }

                            break;
                        }
                        case "pdf": {
                            if (!deleteList.get(i).getMedia_url().equals("")) {
                                AmazonUtil.deleteS3Client(getApplicationContext(), deleteList.get(i).getMedia_url());
                            }
                            break;
                        }
                    }
                }
            }
        }





        /*Add image s3 bucket*/


        if (Global.IsNotNull(bzcard_model.getBzcardFieldsModel().getProfile_url())) {
                s3uploaderObj = new S3Uploader(Title_bzcardActivity.this);
                String Bzcard_profile = s3uploaderObj.BZcard_image_Upload(bzcard_model.getBzcardFieldsModel().getProfile_url(),
                        "bzcard" + "/" + "bzcard_" + signResponseModel.getUser().getId(), "pro_A");
                UploadS3();
                bzcard_model.getBzcardFieldsModel().setProfile_image(Bzcard_profile);
                SessionManager.setBzcard(Title_bzcardActivity.this, bzcard_model);


        }
        if (Global.IsNotNull(bzcard_model.getBzcardFieldsModel().getCover_url())) {

                s3uploaderObj = new S3Uploader(Title_bzcardActivity.this);
                String Bzcard_Cover_image = s3uploaderObj.BZcard_image_Upload(bzcard_model.getBzcardFieldsModel().getCover_url(),
                        "bzcard" + "/" + "bzcard_" + signResponseModel.getUser().getId(), "cov_A");
                UploadS3();
                bzcard_model.getBzcardFieldsModel().setCover_image(Bzcard_Cover_image);
                SessionManager.setBzcard(Title_bzcardActivity.this, bzcard_model);


        }
        if (Global.IsNotNull(bzcard_model.getBzcardFieldsModel().getCompany_logo_url())) {

                s3uploaderObj = new S3Uploader(Title_bzcardActivity.this);
                String Bzcard_Company_logo = s3uploaderObj.BZcard_image_Upload(bzcard_model.getBzcardFieldsModel().getCompany_logo_url(),
                        "bzcard" + "/" + "bzcard_" + signResponseModel.getUser().getId(), "com_A");
                UploadS3();
                bzcard_model.getBzcardFieldsModel().setCompany_logo(Bzcard_Company_logo);
                SessionManager.setBzcard(Title_bzcardActivity.this, bzcard_model);

        }
        if (Global.IsNotNull(bzMediaInformationList) && bzMediaInformationList.size() != 0) {

            for (int i = 0; i < bzMediaInformationList.size(); i++) {
                switch (bzMediaInformationList.get(i).getMedia_type()) {
                    case "video": {
                        if (!bzMediaInformationList.get(i).getMedia_filePath().equals("")) {
                            TotalFileUpload++;
                            Bzcard_Fields_Model.BZ_media_information information1 = bzMediaInformationList.get(i);
                            s3uploaderObj = new S3Uploader(Title_bzcardActivity.this);
                            String Bzcard_thumbnail = s3uploaderObj.BZcard_image_Upload(bzMediaInformationList.get(i).getMedia_filePath(),
                                    "bzcard" + "/" + "bzcard_" + signResponseModel.getUser().getId(), "thu_A");
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
                                bzcard_model.getBzcardFieldsModel().setBzMediaInformationList(bzMediaInformationList);
                                SessionManager.setBzcard(Title_bzcardActivity.this, bzcard_model);
                            }
                        }

                        break;
                    }
                    case "image": {
                        if (!bzMediaInformationList.get(i).getMedia_filePath().equals("")) {

                            TotalFileUpload++;
                            Bzcard_Fields_Model.BZ_media_information information1 = bzMediaInformationList.get(i);
                            s3uploaderObj = new S3Uploader(Title_bzcardActivity.this);

                            String Bzcard_image = s3uploaderObj.BZcard_image_Upload(bzMediaInformationList.get(i).getMedia_filePath(),
                                    "bzcard" + "/" + "bzcard_" + signResponseModel.getUser().getId(), "img_A");
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
                                bzcard_model.getBzcardFieldsModel().setBzMediaInformationList(bzMediaInformationList);
                                SessionManager.setBzcard(Title_bzcardActivity.this, bzcard_model);
                            }
                        }
                        break;
                    }
                    case "pdf": {
                        if (!bzMediaInformationList.get(i).getMedia_filePath().equals("")) {
                            TotalFileUpload++;
                            Bzcard_Fields_Model.BZ_media_information information1 = bzMediaInformationList.get(i);
                            s3uploaderObj = new S3Uploader(Title_bzcardActivity.this);

                            String Bzcard_image = s3uploaderObj.BZcard_pdf_Upload(bzMediaInformationList.get(i).getMedia_filePath(),
                                    "bzcard" + "/" + "bzcard_" + signResponseModel.getUser().getId(), "pdf_A");
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
                                bzcard_model.getBzcardFieldsModel().setBzMediaInformationList(bzMediaInformationList);
                                SessionManager.setBzcard(Title_bzcardActivity.this, bzcard_model);
                            }
                        }
                        break;
                    }
                }
            }
        }

        if(TotalFileUpload==0) {
            try {
                if (Global.isNetworkAvailable(Title_bzcardActivity.this, MainActivity.mMainLayout)) {
                    if(validaction()){
                        Data_Upload();
                    }else {
                        loadingDialog.cancelLoading();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean validaction() {
        if(bzcard_model.getCard_id()==1){
            if(bzcard_model.getBzcardFieldsModel().getFirst_name().equals("")){
                Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.invalid_first_name),false);
            }else  if(bzcard_model.getBzcardFieldsModel().getLast_name().equals("")){
                Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.invalid_last_name),false);
            }else if(bzcard_model.getBzcardFieldsModel().getContant_number().equals("")){
                Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.invalid_phone),false);
            }else if(bzcard_model.getBzcardFieldsModel().getEmail().equals("")){
                Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.invalid_email),false);
            }else {
                return true;
            }
        }else {
            if(bzcard_model.getBzcardFieldsModel().getFirst_name().equals("")){
                Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.invalid_first_name),false);
            }else  if(bzcard_model.getBzcardFieldsModel().getLast_name().equals("")){
                Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.invalid_last_name),false);
            }else if(bzcard_model.getBzcardFieldsModel().getContant_number().equals("")){
                Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.invalid_phone),false);
            }else if(bzcard_model.getBzcardFieldsModel().getEmail().equals("")){
                Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.invalid_email),false);
            }else {
                return true;
            }
        }

        return false;
    }

    void Data_Upload () throws JSONException {


        SignResponseModel signResponseModel = SessionManager.getGetUserdata(Title_bzcardActivity.this);
        String token = Global.getToken(sessionManager);
        JSONObject obj = new JSONObject();

        JSONObject paramObject = new JSONObject();
        if (bzcard_model.isEdit()) {
            paramObject.put("id", bzcard_model.getId());
        }
        paramObject.put("organization_id", 1);
        paramObject.put("team_id", 1);
        paramObject.put("user_id", signResponseModel.getUser().getId());
        paramObject.put("card_id", bzcard_model.getCard_id());


        JSONObject param = new JSONObject();
        /*Infirmaction key*/
        // if(Global.IsNotNull(main_model.getCover_image())){
        param.put("cover_image", bzcard_model.getBzcardFieldsModel().getCover_image());
        // }
        // if(Global.IsNotNull(main_model.getProfile_url())) {
        param.put("profile_image", bzcard_model.getBzcardFieldsModel().getProfile_image());
        // }
        // if(Global.IsNotNull(main_model.getFirst_name())) {
        param.put("first_name", bzcard_model.getBzcardFieldsModel().getFirst_name());
        //}
        //if(Global.IsNotNull(main_model.getLast_name())) {
        param.put("last_name", bzcard_model.getBzcardFieldsModel().getLast_name());
        //  }
        // if(Global.IsNotNull(main_model.getContant_number())) {
        param.put("phone_number", bzcard_model.getBzcardFieldsModel().getContant_number());
        //  }
        //  if(Global.IsNotNull(main_model.getEmail())) {
        param.put("email_address", bzcard_model.getBzcardFieldsModel().getEmail());
        //   }
        //   if(Global.IsNotNull(main_model.getCompany_name())) {
        param.put("company_name", bzcard_model.getBzcardFieldsModel().getCompany_name());
        //  }
        //   if(Global.IsNotNull(main_model.getCompany_logo_url())) {
        param.put("company_logo", bzcard_model.getBzcardFieldsModel().getCompany_logo());
        // }
        //   if(Global.IsNotNull(main_model.getCompany_url())) {
        param.put("company_url", bzcard_model.getBzcardFieldsModel().getCompany_url());
        // }
        // if(Global.IsNotNull(main_model.getJobtitle())) {
        param.put("job_title", bzcard_model.getBzcardFieldsModel().getJobtitle());
        //  }
        //   if(Global.IsNotNull(main_model.getAddrees())) {
        param.put("address", bzcard_model.getBzcardFieldsModel().getAddrees());
        //  }
        //  if(Global.IsNotNull(main_model.getZipcode())) {
        param.put("zipcode", bzcard_model.getBzcardFieldsModel().getZipcode());
        //  }
        /*media key*/
        //  if(Global.IsNotNull(main_model.getThemeColor())) {
        param.put("theme", "");
        //   }
        //   if(Global.IsNotNull(main_model.getThemeColorHash())) {
        if(Global.IsNotNull(bzcard_model.getBzcardFieldsModel().getThemeColorHash())){
            param.put("themeColorHash", bzcard_model.getBzcardFieldsModel().getThemeColorHash());
        }else {
            String[] color_list = getResources().getStringArray(R.array.Select_color);
            param.put("themeColorHash","#" + color_list[0]);
        }
        //  }
        //   if(Global.IsNotNull(main_model.getButton1_name())) {
        param.put("button1_name", bzcard_model.getBzcardFieldsModel().getButton1_name());
        //  }
        //   if(Global.IsNotNull(main_model.getButton1_url())) {
        param.put("button1_url", bzcard_model.getBzcardFieldsModel().getButton1_url());
        //  }
        // if(Global.IsNotNull(main_model.getButton2_name())) {
        param.put("button2_name", bzcard_model.getBzcardFieldsModel().getButton2_name());
        //  }
        //  if(Global.IsNotNull(main_model.getButton2_url())) {
        param.put("button2_url", bzcard_model.getBzcardFieldsModel().getButton2_url());
        //  }
        //  if(Global.IsNotNull(main_model.getBio_head())) {
        param.put("bio_head", bzcard_model.getBzcardFieldsModel().getBio_head());
        //  }
        //  if(Global.IsNotNull(main_model.getBio_description())) {
        param.put("bio_description", bzcard_model.getBzcardFieldsModel().getBio_description());
        //  }
        //  if(Global.IsNotNull(main_model.getHtml())) {
        param.put("html", bzcard_model.getBzcardFieldsModel().getHtml());
        //  }
        //  if(Global.IsNotNull(main_model.getAction_name())) {
        param.put("action_name", bzcard_model.getBzcardFieldsModel().getAction_name());
        //  }
        //  if(Global.IsNotNull(main_model.getAction_url())) {
        param.put("action_url", bzcard_model.getBzcardFieldsModel().getAction_url());
        //    }

        // if(Global.IsNotNull(bzMediaInformationList)||bzMediaInformationList.size()!=0){
        JSONArray jsonArray = new JSONArray();
        JSONObject media_ = null;
        for (int i = 0; i < bzMediaInformationList.size(); i++) {
            media_ = new JSONObject();
            if (Global.IsNotNull(bzMediaInformationList.get(i).getMedia_type())) {
                media_.put("media_type", bzMediaInformationList.get(i).getMedia_type());
            }
            if (Global.IsNotNull(bzMediaInformationList.get(i).getMedia_url())) {
                if(bzMediaInformationList.get(i).getMedia_type().equals("video")){
                    media_.put("media_url",Global.getEmbedURL(bzMediaInformationList.get(i).getMedia_url()));
                }else {
                    media_.put("media_url", bzMediaInformationList.get(i).getMedia_url());
                }
            }
            if (Global.IsNotNull(bzMediaInformationList.get(i).getMedia_thumbnail())) {
                media_.put("media_thumbnail", bzMediaInformationList.get(i).getMedia_thumbnail());
            }
            if (Global.IsNotNull(bzMediaInformationList.get(i).getMedia_title())) {
                media_.put("media_title", bzMediaInformationList.get(i).getMedia_title());
            }
            if (Global.IsNotNull(bzMediaInformationList.get(i).getMedia_description())) {
                media_.put("media_description", bzMediaInformationList.get(i).getMedia_description());
            }
            if (Global.IsNotNull(bzMediaInformationList.get(i).getIs_featured())) {
                media_.put("is_featured", bzMediaInformationList.get(i).getIs_featured());
            }

            jsonArray.put(media_);
        }

        param.put("media_information", jsonArray);
        // }



        /*social key*/
        JSONObject social = new JSONObject();
        //  if(Global.IsNotNull(main_model.getFb())) {
        social.put("facebook_url", bzcard_model.getBzcardFieldsModel().getSocial_links().getFacebook_url());
        //   }
        //  if(Global.IsNotNull(main_model.getTwitter())) {
        social.put("twitter_url", bzcard_model.getBzcardFieldsModel().getSocial_links().getTwitter_url());
        //  }
        //  if(Global.IsNotNull(main_model.getYoutube())) {
        social.put("youtube_url", bzcard_model.getBzcardFieldsModel().getSocial_links().getYoutube_url());
        //  }
        //   if(Global.IsNotNull(main_model.getBreakout())) {
        social.put("breakout_url", bzcard_model.getBzcardFieldsModel().getSocial_links().getBreakout_url());
        //  }
        //  if(Global.IsNotNull(main_model.getInstagram())) {
        social.put("instagram_url", bzcard_model.getBzcardFieldsModel().getSocial_links().getInstagram_url());
        //   }
        //    if(Global.IsNotNull(main_model.getLinkedin())) {
        social.put("linkedin_url", bzcard_model.getBzcardFieldsModel().getSocial_links().getLinkedin_url());
        //  }
        //  if(Global.IsNotNull(main_model.getProfile_url())) {
        social.put("pinterest_url", bzcard_model.getBzcardFieldsModel().getSocial_links().getPinterest_url());
        //  }
        // if(Global.IsNotNull(main_model.getVenmo())) {
        social.put("venmo_url", bzcard_model.getBzcardFieldsModel().getSocial_links().getVenmo_url());
        // }
        // if(Global.IsNotNull(main_model.getSkypay())) {
        social.put("skype_url", bzcard_model.getBzcardFieldsModel().getSocial_links().getSkype_url());
        //  }
        //  if(Global.IsNotNull(main_model.getTiktok())) {
        social.put("tiktok_url", bzcard_model.getBzcardFieldsModel().getSocial_links().getTiktok_url());
        //   }
        // if(Global.IsNotNull(main_model.getSnapchat())) {
        social.put("snapchat_url", bzcard_model.getBzcardFieldsModel().getSocial_links().getSnapchat_url());
        // }
        //  if(Global.IsNotNull(main_model.getOther_filed())) {
        social.put("other_1", bzcard_model.getBzcardFieldsModel().getSocial_links().getOther_1());
        //  }
        //  if(Global.IsNotNull(main_model.getOther_filed1())) {
        social.put("other_2", bzcard_model.getBzcardFieldsModel().getSocial_links().getOther_2());
        //  }
        param.put("social_links", social);
        param.put("card_name", ev_titale.getText().toString().trim());
        paramObject.put("fields_val", param);
        obj.put("data", paramObject);
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(obj.toString());
        retrofitCalls.BZcard_Add_Update(sessionManager, gsonObject, loadingDialog, token, Global.getVersionname(Title_bzcardActivity.this), Global.Device, new RetrofitCallback() {
            @Override
            public void success (Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
                if (response.body().getHttp_status() == 200) {
                    UploadCount = 0;
                    TotalFileUpload=0;
                    finish();
                }
            }

            @Override
            public void error (Response<ApiResponse> response) {
                loadingDialog.cancelLoading();
            }
        });

    }

    private void UploadS3 () {
        s3uploaderObj.setOns3UploadDone(new S3Uploader.S3UploadInterface() {
            @Override
            public void onUploadSuccess (String response) {
                Log.e("Reppnse is", new Gson().toJson(response));
                UploadCount++;

                if (UploadCount == TotalFileUpload) {
                    try {
                        if (Global.isNetworkAvailable(Title_bzcardActivity.this, MainActivity.mMainLayout)) {
                            if(validaction()) {
                                Data_Upload();
                            }else {
                                loadingDialog.cancelLoading();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onUploadError (String response) {

                loadingDialog.cancelLoading();
                Toast.makeText(Title_bzcardActivity.this, new Gson().toJson(response), Toast.LENGTH_SHORT).show();

            }

        });
    }

    @Override
    public void onNetworkConnectionChanged (boolean isConnected) {
        Global.checkConnectivity(Title_bzcardActivity.this, mMainLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkBroadcastForNougat () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void unregisterNetworkChanges () {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy () {
        super.onDestroy();
        unregisterNetworkChanges();
    }


    @Override
    public void onBackPressed () {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Add_New_Bzcard_Activity.class));
        finish();
    }
}