package com.contactninja.Broadcast_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.contactninja.Fragment.Broadcast_Frgment.Broadcst_Activty;
import com.contactninja.R;
import com.contactninja.Utils.Global;

public class Broadcast_time_select extends AppCompatActivity {
    ImageView iv_back,iv_more;
    TextView save_button,selected_date;
    String time="";
    TimePicker timePicker;
    ConstraintLayout mMainLayout;
    String start_date="",end_date="",type="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_time_select);
        IntentUI();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        start_date=bundle.getString("start_date","");
        end_date=bundle.getString("end_date","");
        type=bundle.getString("type","");
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if (time.equals(""))
             {
                 Global.Messageshow(getApplicationContext(),mMainLayout,"Add Time",false);
             }
             else {

                 Intent Time_Selcet_Activity=new Intent(getApplicationContext(),Broadcast_Final_Activity.class);
                 Time_Selcet_Activity.putExtra("start_date",start_date);
                 Time_Selcet_Activity.putExtra("end_date",end_date);
                 Time_Selcet_Activity.putExtra("time",time);
                 Time_Selcet_Activity.putExtra("type",type);
                 startActivity(new Intent(getApplicationContext(), Broadcst_Activty.class));
                 startActivity(Time_Selcet_Activity);
             }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        timePicker = (TimePicker) findViewById(R.id.simpleTimePicker);
        timePicker.setIs24HourView(false); // used to display AM/PM mode
        // perform set on time changed listener event
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay>12) {
                    time=hourOfDay + " : " + minute+" PM";
                } else if(hourOfDay==12) {
                    time=hourOfDay + " : " + minute+" PM";
                } else if(hourOfDay<12) {
                    if(hourOfDay!=0) {
                        time=hourOfDay + " : " + minute+" AM";
                    } else {
                        time=hourOfDay + " : " + minute+" AM";
                    }
                }

            }
        });
    }

    private void IntentUI() {
        iv_back=findViewById(R.id.iv_back);
        save_button=findViewById(R.id.save_button);
        save_button.setVisibility(View.VISIBLE);
        save_button.setText("Next");
        iv_more=findViewById(R.id.iv_more);
        iv_more.setVisibility(View.GONE);
        selected_date=findViewById(R.id.selected_date);
        timePicker=findViewById(R.id.simpleTimePicker);
        mMainLayout=findViewById(R.id.mMainLayout);

    }
}