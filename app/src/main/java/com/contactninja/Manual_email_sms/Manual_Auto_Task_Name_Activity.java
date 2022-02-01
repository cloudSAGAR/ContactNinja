package com.contactninja.Manual_email_sms;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.contactninja.AddContect.EmailSend_Activity;
import com.contactninja.R;
import com.contactninja.Utils.ConnectivityReceiver;
import com.contactninja.Utils.Global;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;

public class Manual_Auto_Task_Name_Activity extends AppCompatActivity  implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener  {
    ImageView iv_back;
    TextView save_button, tv_remain_txt, tv_error;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    EditText ev_titale;
    int sequence_id, seq_task_id;
    private BroadcastReceiver mNetworkReceiver;
    ConstraintLayout mMainLayout;
    String sequence_Name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_auto_task_name);
        mNetworkReceiver = new ConnectivityReceiver();
        IntentUI();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        tv_remain_txt.setText("40 " + getResources().getString(R.string.camp_remaingn));
        ev_titale.requestFocus();

        if(Global.IsNotNull(sequence_Name)){
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

        mMainLayout= findViewById(R.id.mMainLayout);
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
                Global.hideKeyboard(Manual_Auto_Task_Name_Activity.this);
                //Add Api Call
                if (ev_titale.getText().toString().equals("")) {
                    tv_error.setVisibility(View.VISIBLE);
                } else {
                    tv_error.setVisibility(View.GONE);
                    Log.e("Name ",SessionManager.getCampaign_type(getApplicationContext()));

                    if (SessionManager.getEmail_screen_name(getApplicationContext()).equals("only_sms"))
                    {
                      //  Toast.makeText(getApplicationContext(),SessionManager.getMessage_id(getApplicationContext()),Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(Manual_Auto_Task_Name_Activity.this, Manual_Sms_Send_Activty.class);
                        intent.putExtra("number",SessionManager.getMessage_number(getApplicationContext()));
                        intent.putExtra("id",Integer.parseInt(SessionManager.getMessage_id(getApplicationContext())));
                        intent.putExtra("type",SessionManager.getMessage_tyep(getApplicationContext()));
                        intent.putExtra("task_name",ev_titale.getText().toString());
                        startActivity(intent);
                        finish();

                    }
                    else if (SessionManager.getEmail_screen_name(getApplicationContext()).equals("only_email"))
                    {
                        Intent intent = new Intent(getApplicationContext(), EmailSend_Activity.class);
                        intent.putExtra("email", SessionManager.getMessage_number(getApplicationContext()));
                        intent.putExtra("id", SessionManager.getMessage_id(getApplicationContext()));
                        intent.putExtra("task_name",ev_titale.getText().toString());
                        startActivity(intent);
                        finish();

                    }
                    else {
                        if (SessionManager.getCampaign_type(getApplicationContext()).equals("SMS")) {
                            Intent intent = new Intent(getApplicationContext(), Manual_Sms_Contact_Activity.class);
                            intent.putExtra("task_name", ev_titale.getText().toString());
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Manual_Email_Contect_Activity.class);
                            intent.putExtra("task_name", ev_titale.getText().toString());
                            startActivity(intent);
                            finish();
                        }

                    }
                }

                break;

        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Global.checkConnectivity(Manual_Auto_Task_Name_Activity.this, mMainLayout);
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