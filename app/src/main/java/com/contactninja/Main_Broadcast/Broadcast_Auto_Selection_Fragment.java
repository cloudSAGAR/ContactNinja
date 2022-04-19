package com.contactninja.Main_Broadcast;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.contactninja.R;
import com.contactninja.Utils.SessionManager;
import com.contactninja.Utils.YourFragmentInterface;

import androidx.fragment.app.Fragment;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Broadcast_Auto_Selection_Fragment extends Fragment implements View.OnClickListener, YourFragmentInterface {
    LinearLayout auto_layout, manual_layout;
    ImageView select_manual, select_automated;
    SessionManager sessionManager;

    String c_name = "", c_type = "";
    private long mLastClickTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manual_auto_selection, container, false);
        IntentUI(view);
        sessionManager = new SessionManager(getActivity());
        c_name = SessionManager.getCampaign_type_name(getActivity());
        c_type = SessionManager.getCampaign_type(getActivity());
        //  Log.e("Frgment Call","Yes");

        /// Log.e("c_name",c_name);
        // Log.e("c_type",c_type);
        if (c_type.equals("SMS")) {
            if (c_name.equals("AUTO")) {
                select_automated.setVisibility(View.VISIBLE);
                select_manual.setVisibility(View.GONE);
            } else if (c_name.equals("MANUAL")) {
                select_automated.setVisibility(View.GONE);
                select_manual.setVisibility(View.VISIBLE);
            }

        }
        auto_layout.setOnClickListener(this);
        manual_layout.setOnClickListener(this);



        return view;
    }

    private void IntentUI(View view) {
        auto_layout = view.findViewById(R.id.auto_layout);
        manual_layout = view.findViewById(R.id.manual_layout);
        select_manual = view.findViewById(R.id.select_manual);
        select_automated = view.findViewById(R.id.select_automated);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.auto_layout:
                select_automated.setVisibility(View.VISIBLE);
                select_manual.setVisibility(View.GONE);
                SessionManager.setCampaign_type("SMS");
                SessionManager.setCampaign_type_name("AUTO");
                break;
            case R.id.manual_layout:
                select_manual.setVisibility(View.VISIBLE);
                select_automated.setVisibility(View.GONE);
                SessionManager.setCampaign_type("SMS");
                SessionManager.setCampaign_type_name("MANUAL");
                break;
        }

    }

    @Override
    public void onResume() {

        c_name = SessionManager.getCampaign_type_name(getActivity());
        c_type = SessionManager.getCampaign_type(getActivity());
       // Log.e("c_name", c_name);
       // Log.e("c_type", c_type);
        //Log.e("Frgment Call","Yes");

        if (c_type.equals("SMS")) {
            if (c_name.equals("AUTO")) {
                select_automated.setVisibility(View.VISIBLE);
                select_manual.setVisibility(View.GONE);
            } else if (c_name.equals("MANUAL")) {
                select_automated.setVisibility(View.GONE);
                select_manual.setVisibility(View.VISIBLE);
            }

        }
        super.onResume();
    }

    @Override
    public void fragmentBecameVisible() {

        //  sessionManager=new SessionManager(getActivity());
        c_name = SessionManager.getCampaign_type_name(getActivity());
        c_type = SessionManager.getCampaign_type(getActivity());
        /*Log.e("c_name",c_name);
        Log.e("c_type",c_type);
        Log.e("Frgment Call","Yes");*/

        if (c_type.equals("SMS")) {
            if (c_name.equals("AUTO")) {
                select_automated.setVisibility(View.VISIBLE);
                select_manual.setVisibility(View.GONE);
            } else if (c_name.equals("MANUAL")) {
                select_automated.setVisibility(View.GONE);
                select_manual.setVisibility(View.VISIBLE);
            } else {
                // Log.e("Else Condition ","yes1");
                select_automated.setVisibility(View.GONE);
                select_manual.setVisibility(View.GONE);
            }

        } else {
            select_automated.setVisibility(View.GONE);
            select_manual.setVisibility(View.GONE);
            // Log.e("Else Condition ","yes");
        }
    }
}