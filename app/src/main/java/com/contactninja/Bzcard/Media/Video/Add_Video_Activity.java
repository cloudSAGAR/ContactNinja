package com.contactninja.Bzcard.Media.Video;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.contactninja.Bzcard.Media.Select_Media_Activity;
import com.contactninja.Model.Bzcard_Fields_Model;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Add_Video_Activity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    ImageView iv_back, iv_Featured_star_on,iv_Featured_star_off,iv_Featured;
    TextView save_button,txt_Featured;
    RoundedImageView iv_video;
    private BroadcastReceiver mNetworkReceiver;
    RelativeLayout mMainLayout;
    String Link = "", Actvity = "";
    LinearLayout layout_replace, layout_Cancel, layout_featured;
    private long mLastClickTime = 0;
    EditText edt_video_title, edt_Add_description;
    SessionManager sessionManager;
    Bzcard_Fields_Model.BZ_media_information information;
    Integer is_featured = 0;
    List<Bzcard_Fields_Model.BZ_media_information> bzMediaInformationList = new ArrayList<>();
    Bzcard_Fields_Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        mNetworkReceiver = new ConnectivityReceiver();
        sessionManager = new SessionManager(this);
        model = SessionManager.getBzcard(Add_Video_Activity.this);
        bzMediaInformationList = model.getBzMediaInformationList();

        initUI();


        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            Link = bundle.getString("record");
            information = (Bzcard_Fields_Model.BZ_media_information) getIntent().getSerializableExtra("MyClass");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Global.IsNotNull(information)) {
            setVideo_info();
        } else {
            setVideo();
        }

    }

    private void setVideo_info() {

        edt_video_title.setText(information.getMedia_title());
        edt_Add_description.setText(information.getMedia_description());
        if(!Global.IsNotNull(Link)){
            Link = information.getMedia_url();
        }
        Glide.with(getApplicationContext())
                .load(Global.getYoutubeThumbnailUrlFromVideoUrl(Link))
                .into(iv_video);

        is_featured = information.getIs_featured();
        layout_featured.setVisibility(View.VISIBLE);
        layout_Cancel.setVisibility(View.VISIBLE);
        if(is_featured==1){
            iv_Featured.setVisibility(View.VISIBLE);
            txt_Featured.setText(getResources().getString(R.string.Remove_featured));
            iv_Featured_star_off.setVisibility(View.VISIBLE);
            iv_Featured_star_on.setVisibility(View.GONE);
        }else {
            iv_Featured.setVisibility(View.GONE);
            iv_Featured_star_on.setVisibility(View.VISIBLE);
            iv_Featured_star_off.setVisibility(View.GONE);
            txt_Featured.setText(getResources().getString(R.string.add_featured));
        }
    }

    private void setVideo() {

        Glide.with(getApplicationContext())
                .load(Global.getYoutubeThumbnailUrlFromVideoUrl(Link))
                .into(iv_video);

    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        iv_video = findViewById(R.id.iv_video);
        edt_video_title = findViewById(R.id.edt_video_title);
        edt_Add_description = findViewById(R.id.edt_Add_description);
        layout_replace = findViewById(R.id.layout_replace);
        layout_Cancel = findViewById(R.id.layout_Cancel);
        iv_Featured_star_on = findViewById(R.id.iv_Featured_star_on);
        iv_Featured_star_off = findViewById(R.id.iv_Featured_star_off);
        iv_Featured = findViewById(R.id.iv_Featured);
        layout_featured = findViewById(R.id.layout_featured);
        txt_Featured = findViewById(R.id.txt_Featured);
        iv_back = findViewById(R.id.iv_back);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        layout_replace.setOnClickListener(this);
        layout_Cancel.setOnClickListener(this);
        layout_featured.setOnClickListener(this);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Add_Video_Activity.this, mMainLayout);
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
            case R.id.layout_replace:
                 intent=new Intent(getApplicationContext(),Video_LinkAdd_Activity.class);
                intent.putExtra("MyClass", information);
                startActivity(intent);
                break;
            case R.id.layout_Cancel:
                for (int i = 0; i < bzMediaInformationList.size(); i++) {
                    if (bzMediaInformationList.get(i).getId().equals(information.getId())) {
                        bzMediaInformationList.remove(i);
                        model.setBzMediaInformationList(bzMediaInformationList);
                        SessionManager.setBzcard(Add_Video_Activity.this, model);
                        break;
                    }
                }
                intent = new Intent(getApplicationContext(), Select_Media_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.layout_featured:
                if(is_featured==0){
                    iv_Featured.setVisibility(View.VISIBLE);
                    txt_Featured.setText(getResources().getString(R.string.Remove_featured));
                    iv_Featured_star_on.setVisibility(View.GONE);
                    iv_Featured_star_off.setVisibility(View.VISIBLE);
                    for (int i = 0; i < bzMediaInformationList.size(); i++) {
                        if (bzMediaInformationList.get(i).getIs_featured()==1) {
                            bzMediaInformationList.get(i).setIs_featured(0);
                            break;
                        }
                    }
                    is_featured=1;
                }else {
                    iv_Featured.setVisibility(View.GONE);
                    is_featured=0;
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
                                information.setMedia_url(Link);
                                information.setMedia_title(edt_video_title.getText().toString().trim());
                                information.setMedia_description(edt_Add_description.getText().toString().trim());
                                information.setIs_featured(is_featured);
                                bzMediaInformationList.set(i, information);
                                model.setBzMediaInformationList(bzMediaInformationList);
                                SessionManager.setBzcard(Add_Video_Activity.this, model);

                                break;
                            }
                        }
                    } else {
                        Bzcard_Fields_Model.BZ_media_information information = new Bzcard_Fields_Model.BZ_media_information();
                        information.setId(bzMediaInformationList.size());
                        information.setMedia_type("video");
                        information.setMedia_url(Link);
                        information.setMedia_title(edt_video_title.getText().toString().trim());
                        information.setMedia_description(edt_Add_description.getText().toString().trim());
                        if (bzMediaInformationList.size() == 0) {
                            information.setIs_featured(1);
                        } else {
                            information.setIs_featured(0);
                        }
                        bzMediaInformationList.add(information);
                        model.setBzMediaInformationList(bzMediaInformationList);
                        SessionManager.setBzcard(Add_Video_Activity.this, model);
                    }
                    intent = new Intent(getApplicationContext(), Select_Media_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    private boolean Vlidaction() {
        if (edt_video_title.getText().toString().trim().equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.video_title_error), false);
        } else if (edt_Add_description.getText().toString().trim().equals("")) {
            Global.Messageshow(getApplicationContext(), mMainLayout, getResources().getString(R.string.video_description_error), false);
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