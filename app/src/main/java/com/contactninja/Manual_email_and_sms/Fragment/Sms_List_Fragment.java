package com.contactninja.Manual_email_and_sms.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contactninja.R;


public class Sms_List_Fragment extends Fragment implements View.OnClickListener {

    LinearLayout demo_layout;
    TextView tv_create;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_sms__list_, container, false);
        intentView(view);
        return  view;

    }

    private void intentView(View view) {
        demo_layout=view.findViewById(R.id.demo_layout);
        tv_create=view.findViewById(R.id.tv_create);
        tv_create.setText(getString(R.string.add_task_email));
        demo_layout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.demo_layout:
                break;
        }
    }
}