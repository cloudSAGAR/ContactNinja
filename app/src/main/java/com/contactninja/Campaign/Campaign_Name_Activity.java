package com.contactninja.Campaign;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.R;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;

public class Campaign_Name_Activity extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back;
    TextView save_button, tv_remain_txt, tv_error;
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    EditText ev_titale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_name);
        IntentUI();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        tv_remain_txt.setText("40 "+getResources().getString(R.string.camp_remaingn));
        ev_titale.requestFocus();
        ev_titale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length()<=40)
                {
                    int num=40-charSequence.toString().length();
                    tv_remain_txt.setText(num+" "+getResources().getString(R.string.camp_remaingn));
                    tv_error.setVisibility(View.GONE);
                }
                else if (charSequence.toString().length()==0)
                {
                    tv_error.setVisibility(View.VISIBLE);
                }
                else {
                    tv_error.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void IntentUI() {

        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        save_button.setText("Done");
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
                //Add Api Call
                startActivity(new Intent(getApplicationContext(), ContectAndGroup_Actvity.class));
                break;

        }
    }
}