package com.contactninja.Main_Broadcast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.contactninja.Model.Broadcate_save_data;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables")
public class Broadcast_Name_Activity extends AppCompatActivity implements View.OnClickListener{

    ImageView iv_back;
    TextView save_button, tv_remain_txt, tv_error;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    EditText ev_titale;
    int sequence_id, seq_task_id;
    private BroadcastReceiver mNetworkReceiver;
    ConstraintLayout mMainLayout;
    String sequence_Name = "",Activty_Back="";
    private long mLastClickTime = 0;
    Broadcate_save_data broadcate_save_data=new Broadcate_save_data();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_name);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        broadcate_save_data=SessionManager.getBroadcate_save_data(getApplicationContext());
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);

       // Global.ShowKeyboard(Broadcast_Name_Activity.this);
        ev_titale.requestFocus();
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
        if (SessionManager.getBroadcast_flag(getApplicationContext()).equals("edit"))
        {
            broadcate_save_data=SessionManager.getBroadcate_save_data(getApplicationContext());
            ev_titale.setText(broadcate_save_data.getBroadcastname());

        }
        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            Activty_Back = bundle.getString("Activty");
        }catch (Exception e){
            e.printStackTrace();
        }

        tv_remain_txt.setText("40 " + getResources().getString(R.string.camp_remaingn));
/*
        if (Global.IsNotNull(sequence_Name)) {
            ev_titale.setText(sequence_Name);
            ev_titale.setSelection(ev_titale.getText().length());
        }*/
    }

    private void IntentUI() {

        mMainLayout = findViewById(R.id.mMainLayout);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText(getResources().getString(R.string.Next));
        ev_titale = findViewById(R.id.ev_titale);
        tv_remain_txt = findViewById(R.id.tv_remain_txt);
        tv_error = findViewById(R.id.tv_error);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.save_button:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (ev_titale.getText().toString().equals(""))
                {
                    Global.Messageshow(getApplicationContext(),mMainLayout,getResources().getString(R.string.add_title),false);
                }
                else {
                   // SessionManager.setBroadcast_flag("edit");
                    SessionManager.setBroadcast_flag("add");
                    broadcate_save_data.setBroadcastname(ev_titale.getText().toString());
                    SessionManager.setBroadcate_save_data(getApplicationContext(),broadcate_save_data);
                    Intent broad_caste=new Intent(getApplicationContext(),Broadcast_Preview.class);
                    startActivity(broad_caste);
                }

                break;

        }
    }






    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Activty_Back.equals("Recurrence")){
            startActivity(new Intent(getApplicationContext(),Recuring_email_broadcast_activity.class));
            finish();
        }else if(Activty_Back.equals("Preview")){
            startActivity(new Intent(getApplicationContext(),Broadcast_Preview.class));
            finish();
        }
    }
}