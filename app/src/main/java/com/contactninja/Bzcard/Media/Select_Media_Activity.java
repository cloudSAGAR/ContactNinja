package com.contactninja.Bzcard.Media;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.Bzcard.Media.Image.Add_image_Activity;
import com.contactninja.Bzcard.Media.Image.Image_List_Activity;
import com.contactninja.Bzcard.Media.PDF.Add_pdf_Activity;
import com.contactninja.Bzcard.Media.PDF.PDF_List_Activity;
import com.contactninja.Bzcard.Media.Video.Video_LinkAdd_Activity;
import com.contactninja.Bzcard.Media.Video.Video_List_Activity;
import com.contactninja.Model.Bzcard_Model;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class Select_Media_Activity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private BroadcastReceiver mNetworkReceiver;
    RelativeLayout mMainLayout;
    ImageView iv_back;

    RelativeLayout layout_video,layout_Image,layout_pdf;
    private long mLastClickTime = 0;
    SessionManager sessionManager;
    LinearLayout layout_count_video,layout_count_image,layout_count_pdf;
    TextView txt_count_video,txt_count_image,txt_count_pdf;
    List<Bzcard_Model.BZ_media_information> bzMediaInformationList=new ArrayList<>();
    int Video_count =0, Image_count =0, pdf_count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_media);
        mNetworkReceiver = new ConnectivityReceiver();
        sessionManager=new SessionManager(this);
        Bzcard_Model model= SessionManager.getBzcard(Select_Media_Activity.this);
        bzMediaInformationList=model.getBzMediaInformationList();

        initUI();
        setCount();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setCount() {
        Video_count =0;
        Image_count =0;
        pdf_count =0;
        if(Global.IsNotNull(bzMediaInformationList)&&bzMediaInformationList.size()!=0){
            for(int i=0;i<bzMediaInformationList.size();i++){
                switch (bzMediaInformationList.get(i).getMedia_type()) {
                    case "video":
                        Video_count++;
                        break;
                    case "image":
                        Image_count++;
                        break;
                    case "pdf":
                        pdf_count++;
                        break;
                }
            }
        }
        if(Video_count !=0){
            layout_count_video.setVisibility(View.VISIBLE);
            txt_count_video.setText(String.valueOf(Video_count));
        }else {
            layout_count_video.setVisibility(View.GONE);
        }
        if(Image_count !=0){
            layout_count_image.setVisibility(View.VISIBLE);
            txt_count_image.setText(String.valueOf(Image_count));
        }else {
            layout_count_image.setVisibility(View.GONE);
        }
        if(pdf_count !=0){
            layout_count_pdf.setVisibility(View.VISIBLE);
            txt_count_pdf.setText(String.valueOf(pdf_count));
        }else {
            layout_count_pdf.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUI() {
        mMainLayout = findViewById(R.id.mMainLayout);
        layout_video = findViewById(R.id.layout_video);
        layout_Image = findViewById(R.id.layout_Image);
        layout_pdf = findViewById(R.id.layout_pdf);
        layout_count_video = findViewById(R.id.layout_count_video);
        txt_count_video = findViewById(R.id.txt_count_video);
        layout_count_image = findViewById(R.id.layout_count_image);
        txt_count_image = findViewById(R.id.txt_count_image);
        layout_count_pdf = findViewById(R.id.layout_count_pdf);
        txt_count_pdf = findViewById(R.id.txt_count_pdf);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        layout_video.setOnClickListener(this);
        layout_Image.setOnClickListener(this);
        layout_pdf.setOnClickListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Select_Media_Activity.this, mMainLayout);
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
            case R.id.layout_video:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if(Video_count==0){
                    startActivity(new Intent(getApplicationContext(), Video_LinkAdd_Activity.class));
                }else {
                    if(bzMediaInformationList.size()>10){
                        startActivity(new Intent(getApplicationContext(), Video_List_Activity.class));
                    }else {
                        Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.max_media),false);
                    }
                }
                break;
                case R.id.layout_Image:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if(Image_count==0){
                    startActivity(new Intent(getApplicationContext(), Add_image_Activity.class));
                }else {
                    if(bzMediaInformationList.size()>10){
                        startActivity(new Intent(getApplicationContext(), Image_List_Activity.class));
                    }else {
                        Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.max_media),false);
                    }
                }
                break;
            case R.id.layout_pdf:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if(pdf_count==0){
                    startActivity(new Intent(getApplicationContext(), Add_pdf_Activity.class));
                }else {
                    if(bzMediaInformationList.size()>10){
                        startActivity(new Intent(getApplicationContext(), PDF_List_Activity.class));
                    }else {
                        Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.max_media),false);
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}