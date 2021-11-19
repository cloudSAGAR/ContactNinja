package com.intricare.test.Auth.PlanTyep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.intricare.test.R;

public class PlanType_Screen extends AppCompatActivity {

    LinearLayout layout_free_card,layout_bz_card,layout_master,layout_contect;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_type_screen);
        IntentUI();
        layout_free_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Plan_Detail_Screen.class);
                intent.putExtra("flag",1);
                startActivity(intent);
              //  finish();
            }
        });
        layout_bz_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Plan_Detail_Screen.class);
                intent.putExtra("flag",2);
                startActivity(intent);
              //  finish();
            }
        });

        layout_master.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Plan_Detail_Screen.class);
                intent.putExtra("flag",3);
                startActivity(intent);
                //finish();
            }
        });


        layout_contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Plan_Detail_Screen.class);
                intent.putExtra("flag",4);
                startActivity(intent);
               // finish();
            }
        });
    }

    private void IntentUI() {
        layout_free_card=findViewById(R.id.layout_free_card);
        layout_bz_card=findViewById(R.id.layout_bz_card);
        layout_master=findViewById(R.id.layout_master);
        layout_contect=findViewById(R.id.layout_contect);
    }


}