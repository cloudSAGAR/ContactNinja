package com.contactninja.Main_Broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contactninja.AddContect.EmailSend_Activity;
import com.contactninja.Campaign.Campaign_Preview;
import com.contactninja.Interface.TimeZoneClick;
import com.contactninja.MainActivity;
import com.contactninja.Manual_email_text.Manual_Auto_Task_Name_Activity;
import com.contactninja.Manual_email_text.Manual_Email_Contect_Activity;
import com.contactninja.Manual_email_text.Manual_Text_Contact_Activity;
import com.contactninja.Manual_email_text.Manual_Text_Send_Activty;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.Model.WorkingHoursModel;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.PaginationListener;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.ApiResponse;
import com.contactninja.retrofit.RetrofitCallback;
import com.contactninja.retrofit.RetrofitCalls;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Response;

import static com.contactninja.Utils.PaginationListener.PAGE_START;

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
    String sequence_Name = "";
    private long mLastClickTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_name);

        mNetworkReceiver = new ConnectivityReceiver();
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
        save_button.setText("Next");
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
                Intent broad_caste=new Intent(getApplicationContext(),Broadcast_Preview.class);
                startActivity(broad_caste);
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
        finish();
        super.onBackPressed();
    }
}