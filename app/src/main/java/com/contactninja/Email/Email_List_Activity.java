package com.contactninja.Email;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contactninja.R;
import com.contactninja.Utils.SessionManager;

import java.util.ArrayList;

public class Email_List_Activity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout demo_layout,add_new_contect_layout;
    TextView tv_create;
    RecyclerView email_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_list2);
        intentView();
    }

    private void intentView() {
        demo_layout=findViewById(R.id.demo_layout);
        demo_layout.setVisibility(View.VISIBLE);
        demo_layout.setOnClickListener(this);
        tv_create=findViewById(R.id.tv_create);
        tv_create.setText(getString(R.string.email_txt));
        email_list=findViewById(R.id.email_list);
        add_new_contect_layout=findViewById(R.id.add_new_contect_layout);
        add_new_contect_layout.setOnClickListener(this);
        email_list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.add_new_contect_layout:
                SessionManager.setGroupList(getApplicationContext(), new ArrayList<>());

                startActivity(new Intent(getApplicationContext(),Email_Selction_Activity.class));
                //finish();
                break;
            case R.id.demo_layout:
                SessionManager.setGroupList(getApplicationContext(), new ArrayList<>());
                startActivity(new Intent(getApplicationContext(),Email_Selction_Activity.class));
              //  finish();
                break;
        }
    }
}