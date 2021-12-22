package com.contactninja.Broadcast_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.contactninja.Auth.LoginActivity;
import com.contactninja.MainActivity;
import com.contactninja.R;

public class Brodcsast_Tankyou extends AppCompatActivity {

    TextView tv_sub_titale;
    String s_name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brodcsast_tankyou);
        tv_sub_titale=findViewById(R.id.tv_sub_titale);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        s_name=bundle.getString("s_name","");
        if (s_name.equals("final"))
        {
            tv_sub_titale.setText(getString(R.string.brod_message));
        }
        else {

        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        }, 2000);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();

    }
}