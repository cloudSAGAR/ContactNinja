package com.contactninja.Broadcast_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.contactninja.R;

public class Broadcast_to_repeat extends AppCompatActivity {

    ImageView iv_back,iv_more;
    TextView save_button;
    Spinner day_spinner,time_spinner;
    String day_txt="",time_txt="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_to_repeat);
        IntentUI();

        setSpinner_data();
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (day_txt.equals(""))
                {

                }
               else if (day_txt.equals("Daily"))
                {
                    Intent intent=new Intent(getApplicationContext(),Broadcast_date_select.class);
                    intent.putExtra("type",day_txt);
                    startActivity(intent);
                   // finish();
                }
                else if (day_txt.equals("Weekly"))
                {
                    Intent intent=new Intent(getApplicationContext(),Repeat_weekly_Activity.class);
                    intent.putExtra("type",day_txt);
                    startActivity(intent);

                }
                else if (day_txt.equals("Monthly"))
                {
                    Intent intent=new Intent(getApplicationContext(),Repeat_Month_Activity.class);
                    intent.putExtra("type",day_txt);
                    startActivity(intent);

                }
                else if (day_txt.equals("Does Not Repeat"))
                {

                }
                else {

                }

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setSpinner_data() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.broadcast_repeat_list));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_spinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> TimeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.broadcste_time_list));
        TimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_spinner.setAdapter(TimeAdapter);

        day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                day_txt=String.valueOf(day_spinner.getSelectedItem());
                   Log.e("Text is ",day_txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time_txt=String.valueOf(time_spinner.getSelectedItem());
                Log.e("Text is ",time_txt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        day_spinner=findViewById(R.id.day_spinner);
        time_spinner=findViewById(R.id.time_spinner);

    }
}