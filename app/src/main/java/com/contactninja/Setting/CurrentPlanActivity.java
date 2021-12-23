package com.contactninja.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.contactninja.MainActivity;
import com.contactninja.R;
import com.dalong.francyconverflow.FancyCoverFlow;

import java.util.ArrayList;
import java.util.List;

public class CurrentPlanActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back;
    private FancyCoverFlow mfancyCoverFlow;
    private MyFancyCoverFlowAdapter mMyFancyCoverFlowAdapter;

    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_plan);
        IntentUI();
        ListShow();
    }

    private void ListShow() {
        List<PlanItem> mFancyCoverFlows=new ArrayList<>();
        for(int i=0;i<5;i++){
            PlanItem item=new PlanItem();
            item.setName((i+1)+"天");
            item.setSelected(false);
            mFancyCoverFlows.add(item);
        }
        mfancyCoverFlow = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);
        mMyFancyCoverFlowAdapter = new MyFancyCoverFlowAdapter(this, mFancyCoverFlows);
        mfancyCoverFlow.setAdapter(mMyFancyCoverFlowAdapter);
        mMyFancyCoverFlowAdapter.notifyDataSetChanged();
        mfancyCoverFlow.setUnselectedAlpha(0.5f);
        mfancyCoverFlow.setUnselectedSaturation(0.5f);
        mfancyCoverFlow.setUnselectedScale(0.3f);
        mfancyCoverFlow.setSpacing(0);
        mfancyCoverFlow.setMaxRotation(0);//设置最大旋转
        mfancyCoverFlow.setScaleDownGravity(0.5f);
        mfancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
        int num = Integer.MAX_VALUE / 2 % mFancyCoverFlows.size();
        int selectPosition = Integer.MAX_VALUE / 2 - num;
        mfancyCoverFlow.setSelection(selectPosition);
        mfancyCoverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PlanItem homeFancyCoverFlow = (PlanItem) mfancyCoverFlow.getSelectedItem();
                if (homeFancyCoverFlow != null) {
                    //Toast.makeText(MainActivity.this,homeFancyCoverFlow.getName(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void IntentUI() {
        iv_back=findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@SuppressLint("UnknownNullness") View v) {
        switch (v.getId()){

            case R.id.iv_back:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}