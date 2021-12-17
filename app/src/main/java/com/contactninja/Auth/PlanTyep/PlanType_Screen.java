package com.contactninja.Auth.PlanTyep;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.contactninja.R;


public class    PlanType_Screen extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layout_free_card, layout_bz_card, layout_master, layout_contect;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_type_screen);
        IntentUI();

    }

    private void IntentUI() {
        layout_free_card = findViewById(R.id.layout_free_card);
        layout_bz_card = findViewById(R.id.layout_bz_card);
        layout_master = findViewById(R.id.layout_master);
        layout_contect = findViewById(R.id.layout_contect);

        layout_free_card.setOnClickListener(this);
        layout_bz_card.setOnClickListener(this);
        layout_master.setOnClickListener(this);
        layout_contect.setOnClickListener(this);

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), Plan_Detail_Screen.class);
        switch (v.getId()) {
            case R.id.layout_free_card:
                intent.putExtra("flag", 1);
                startActivity(intent);
                break;
            case R.id.layout_bz_card:
                intent.putExtra("flag", 2);
                startActivity(intent);
                break;
            case R.id.layout_master:
                intent.putExtra("flag", 3);
                startActivity(intent);
                break;
            case R.id.layout_contect:
                intent.putExtra("flag", 4);
                startActivity(intent);
                break;
        }
    }
}