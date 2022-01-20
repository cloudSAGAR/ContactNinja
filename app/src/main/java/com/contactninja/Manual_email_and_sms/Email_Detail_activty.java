package com.contactninja.Manual_email_and_sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.contactninja.Model.ManualTaskModel;
import com.contactninja.R;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;

public class Email_Detail_activty extends AppCompatActivity implements View.OnClickListener {
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back,iv_body;
    TextView save_button,bt_done;
    CoordinatorLayout mMainLayout;
    EditText ev_from,ev_to,ev_subject,edit_compose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_detail_activty);
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);

        IntentUI();
        setData();
    }

    private void setData() {
        ManualTaskModel Data= SessionManager.getManualTaskModel(getApplicationContext());
        ev_from.setText(Data.getContactMasterFirstname()+" "+Data.getContactMasterLastname());
        ev_to.setText(Data.getEmail());
        ev_subject.setText(Data.getContentHeader());
        edit_compose.setText(Data.getContentBody());

    }


    private void IntentUI() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Cancel");
        mMainLayout = findViewById(R.id.mMainLayout);
        bt_done=findViewById(R.id.bt_done);

        ev_from=findViewById(R.id.ev_from);
        ev_to=findViewById(R.id.ev_to);
        ev_subject=findViewById(R.id.ev_subject);
        edit_compose=findViewById(R.id.edit_compose);
        iv_body=findViewById(R.id.iv_body);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.save_button:
                 finish();
                  break;
            case R.id.iv_back:
                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}