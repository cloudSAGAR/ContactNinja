package com.contactninja.Campaign.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.contactninja.R;

public class Campaign_Email_Fragment extends Fragment implements View.OnClickListener {

    LinearLayout auto_layout,manual_layout;
    ImageView select_manual,select_automated;
    int click=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_campaign__email_, container, false);
        IntentUI(view);

        auto_layout.setOnClickListener(this);
        manual_layout.setOnClickListener(this);
        return view;
    }

    private void IntentUI(View view) {
        auto_layout=view.findViewById(R.id.auto_layout);
        manual_layout=view.findViewById(R.id.manual_layout);
        select_manual=view.findViewById(R.id.select_manual);
        select_automated=view.findViewById(R.id.select_automated);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.auto_layout:

                    click=1;
                    select_automated.setVisibility(View.VISIBLE);
                    select_manual.setVisibility(View.GONE);


                break;
            case R.id.manual_layout:

                    click=1;
                    select_automated.setVisibility(View.GONE);
                    select_manual.setVisibility(View.VISIBLE);
                break;
        }
    }
}