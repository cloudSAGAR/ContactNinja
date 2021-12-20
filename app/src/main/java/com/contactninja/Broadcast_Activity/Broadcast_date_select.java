package com.contactninja.Broadcast_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.contactninja.R;

public class Broadcast_date_select extends AppCompatActivity {
    ImageView iv_back,iv_more;
    TextView save_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_date_select);
        IntentUI();
    }

    private void IntentUI() {
        iv_back=findViewById(R.id.iv_back);
        save_button=findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText("Next");
        iv_more=findViewById(R.id.iv_more);
        iv_more.setVisibility(View.GONE);
    }
}