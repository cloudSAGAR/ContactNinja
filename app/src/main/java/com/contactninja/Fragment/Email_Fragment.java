package com.contactninja.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contactninja.R;


public class Email_Fragment extends Fragment {

    LinearLayout demo_layout;
    TextView tv_create;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_email_, container, false);;
        intentView(view);
        return view;

    }

    private void intentView(View view) {

        demo_layout=view.findViewById(R.id.demo_layout);
        demo_layout.setVisibility(View.VISIBLE);
        tv_create=view.findViewById(R.id.tv_create);
        tv_create.setText(getString(R.string.email_txt));
    }

}