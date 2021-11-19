package com.intricare.test.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.intricare.test.Auth.PlanTyep.PlanType_Screen;
import com.intricare.test.R;

public class Thankyou_Screen extends AppCompatActivity {

    TextView start_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou_screen);

        IntentUI();
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), PlanType_Screen.class));
            }
        });

    }

    private void IntentUI() {
        start_button=findViewById(R.id.start_button);

    }
}