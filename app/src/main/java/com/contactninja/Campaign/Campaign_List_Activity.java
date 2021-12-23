package com.contactninja.Campaign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contactninja.R;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;

public class Campaign_List_Activity extends AppCompatActivity implements View.OnClickListener {
    SessionManager sessionManager;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    ImageView iv_back;
    TextView tv_create,sub_txt;
    LinearLayout demo_layout,add_campaign_layout;
    EditText ev_search;
    RecyclerView campaign_list;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_list);
        loadingDialog=new LoadingDialog(this);
        sessionManager=new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        IntentUI();
        iv_back.setOnClickListener(this);
        demo_layout.setOnClickListener(this);
        add_campaign_layout.setOnClickListener(this);
    }
    private void IntentUI() {
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        demo_layout=findViewById(R.id.demo_layout);
        tv_create=findViewById(R.id.tv_create);
        sub_txt=findViewById(R.id.sub_txt);
        ev_search=findViewById(R.id.ev_search);
        add_campaign_layout=findViewById(R.id.add_campaign_layout);
        campaign_list=findViewById(R.id.campaign_list);
        layoutManager=new LinearLayoutManager(this);
        campaign_list.setLayoutManager(layoutManager);
        tv_create.setText(getString(R.string.campaign_alert_txt));
        sub_txt.setText(getString(R.string.campaign_alert_sub_txt));
        demo_layout.setVisibility(View.VISIBLE);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.demo_layout:
                startActivity(new Intent(getApplicationContext(),First_Step_Activity.class));
                break;
            case R.id.add_campaign_layout:
                startActivity(new Intent(getApplicationContext(),First_Step_Activity.class));
                break;
        }
    }
}