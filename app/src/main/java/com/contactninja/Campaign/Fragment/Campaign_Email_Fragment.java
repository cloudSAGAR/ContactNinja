package com.contactninja.Campaign.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.contactninja.Campaign.Automated_Email_Activity;
import com.contactninja.R;
import com.contactninja.Utils.SessionManager;
import com.contactninja.Utils.YourFragmentInterface;

public class Campaign_Email_Fragment extends Fragment implements View.OnClickListener , YourFragmentInterface {

    LinearLayout auto_layout,manual_layout;
    ImageView select_manual,select_automated;
    int click=0;
    SessionManager sessionManager;
    String c_name="",c_type="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_campaign__email_, container, false);
        IntentUI(view);
        sessionManager=new SessionManager(getActivity());
        c_name=SessionManager.getCampaign_type_name(getActivity());
        c_type=SessionManager.getCampaign_type(getActivity());
       // Log.e("Frgment Call","Yes");
        Log.e("c_name",c_name);
        Log.e("c_type",c_type);
        if (c_type.equals("Email"))
        {
            if (c_name.equals("AUTO"))
            {
                select_automated.setVisibility(View.VISIBLE);
                select_manual.setVisibility(View.GONE);
            }
            else if (c_name.equals("MANUAL"))
            {
                select_automated.setVisibility(View.GONE);
                select_manual.setVisibility(View.VISIBLE);
            }

        }
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

                SessionManager.setCampaign_type("Email");
                SessionManager.setCampaign_type_name("AUTO");
                // startActivity(new Intent(getActivity(), Automated_Email_Activity.class));
                Log.e("sessionManager",sessionManager.getCampaign_type(getActivity()));
                Log.e("sessionManager",sessionManager.getCampaign_type_name(getActivity()));
                    select_automated.setVisibility(View.VISIBLE);
                    select_manual.setVisibility(View.GONE);


                break;
            case R.id.manual_layout:

                SessionManager.setCampaign_type("Email");
                SessionManager.setCampaign_type_name("MANUAL");
                //  startActivity(new Intent(getActivity(), Automated_Email_Activity.class));
                Log.e("sessionManager",sessionManager.getCampaign_type(getActivity()));
                Log.e("sessionManager",sessionManager.getCampaign_type_name(getActivity()));
                    select_automated.setVisibility(View.GONE);
                    select_manual.setVisibility(View.VISIBLE);


                break;
        }
    }

    @Override
    public void onResume() {
        c_name=sessionManager.getCampaign_type_name(getActivity());
        c_type=sessionManager.getCampaign_type(getActivity());
       // Log.e("Frgment Call","Yes");
        Log.e("c_name",c_name);
        Log.e("c_type",c_type);
        if (c_type.equals("Email"))
        {
            if (c_name.equals("AUTO"))
            {
                select_automated.setVisibility(View.VISIBLE);
                select_manual.setVisibility(View.GONE);
            }
            else if (c_name.equals("MANUAL"))
            {
                select_automated.setVisibility(View.GONE);
                select_manual.setVisibility(View.VISIBLE);
            }

        }
        super.onResume();
    }

    @Override
    public void fragmentBecameVisible() {
        sessionManager=new SessionManager(getActivity());
        c_name=sessionManager.getCampaign_type_name(getActivity());
        c_type=sessionManager.getCampaign_type(getActivity());

        Log.e("c_name",c_name);
        Log.e("c_type",c_type);
       /// Log.e("Frgment Call","Yes");
        if (c_type.equals("Email"))
        {
            if (c_name.equals("AUTO"))
            {
                select_automated.setVisibility(View.VISIBLE);
                select_manual.setVisibility(View.GONE);
            }
            else if (c_name.equals("MANUAL"))
            {
                select_automated.setVisibility(View.GONE);
                select_manual.setVisibility(View.VISIBLE);
            }
            else {
               // Log.e("Else Condition ","yes1");
                select_automated.setVisibility(View.GONE);
                select_manual.setVisibility(View.GONE);
            }

        }
        else {
            select_automated.setVisibility(View.GONE);
            select_manual.setVisibility(View.GONE);
        }
    }
}