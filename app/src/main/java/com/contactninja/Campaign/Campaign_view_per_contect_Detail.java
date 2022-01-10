package com.contactninja.Campaign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.contactninja.Model.CampaignTask_overview;
import com.contactninja.R;
import com.contactninja.Utils.LoadingDialog;
import com.contactninja.Utils.SessionManager;
import com.contactninja.retrofit.RetrofitCalls;

import java.util.ArrayList;
import java.util.List;

public class Campaign_view_per_contect_Detail extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back, iv_Setting;
    TextView save_button;
    RetrofitCalls retrofitCalls;
    LoadingDialog loadingDialog;
    SessionManager sessionManager;
    int position;
    TextView tv_nameLetter,tv_FirstName;
    List<CampaignTask_overview.SequenceProspect> sequenceProspects=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_view_per_contect_detail);
        IntentUI();
        loadingDialog = new LoadingDialog(this);
        sessionManager = new SessionManager(this);
        retrofitCalls = new RetrofitCalls(this);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        position=Integer.parseInt(bundle.getString("position"));
        CampaignTask_overview contect_list_data=SessionManager.getCampaign_data(getApplicationContext());
        sequenceProspects.addAll(contect_list_data.getSequenceProspects());
        tv_FirstName.setText(sequenceProspects.get(position).getFirstname());

        String name = sequenceProspects.get(position).getFirstname();
        String add_text = "";
        String[] split_data = name.split(" ");
        try {
            for (int i = 0; i < split_data.length; i++) {
                if (i == 0) {
                    add_text = split_data[i].substring(0, 1);
                } else {
                    add_text = add_text + split_data[i].charAt(0);
                    break;
                }
            }
        } catch (Exception e) {

        }
        tv_nameLetter.setText(add_text);

    }

    private void IntentUI() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        save_button = findViewById(R.id.save_button);
        iv_Setting = findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.GONE);
        iv_back.setOnClickListener(this);
        save_button.setOnClickListener(this);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText(getString(R.string.view_contect));
        save_button.setTextColor(getResources().getColor(R.color.purple_200));
        tv_nameLetter=findViewById(R.id.tv_nameLetter);
        tv_FirstName=findViewById(R.id.tv_FirstName);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.save_button:
                finish();
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}