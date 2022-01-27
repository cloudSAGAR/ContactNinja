package com.contactninja.Manual_email_and_sms.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
public class Manual_Auto_Selection_Fragment extends Fragment implements View.OnClickListener, YourFragmentInterface {
    LinearLayout auto_layout,manual_layout;
    ImageView select_manual,select_automated,iv_back_image;
    SessionManager sessionManager;

    String c_name="",c_type="";
    LinearLayout layout_sms_second_automated,layout_sms_second_manual;
    EditText edit_day,edit_minutes,edit_day_manual,edit_minutes_manual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_manual_auto_selection, container, false);
        IntentUI(view);
        sessionManager=new SessionManager(getActivity());
        c_name=SessionManager.getCampaign_type_name(getActivity());
        c_type=SessionManager.getCampaign_type(getActivity());
      //  Log.e("Frgment Call","Yes");

       /// Log.e("c_name",c_name);
       // Log.e("c_type",c_type);
        if (c_type.equals("SMS"))
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

        edit_day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SessionManager.setCampaign_Day(edit_day.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edit_minutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SessionManager.setCampaign_minute(edit_minutes.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edit_day_manual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SessionManager.setCampaign_Day(edit_day_manual.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edit_minutes_manual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SessionManager.setCampaign_minute(edit_minutes_manual.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        return view;
    }
    private void IntentUI(View view) {
        auto_layout=view.findViewById(R.id.auto_layout);
        manual_layout=view.findViewById(R.id.manual_layout);
        select_manual=view.findViewById(R.id.select_manual);
        select_automated=view.findViewById(R.id.select_automated);
        layout_sms_second_automated=view.findViewById(R.id.layout_sms_second_automated);
        layout_sms_second_manual=view.findViewById(R.id.layout_sms_second_manual);

        edit_day=view.findViewById(R.id.edit_day);
        edit_minutes=view.findViewById(R.id.edit_minutes);

        edit_day_manual=view.findViewById(R.id.edit_day_manual);
        edit_minutes_manual=view.findViewById(R.id.edit_minutes_manual);
        iv_back_image=view.findViewById(R.id.iv_back_image);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.auto_layout:
                SessionManager.setCampaign_type("SMS");
                SessionManager.setCampaign_type_name("AUTO");
                Log.e("sessionManager",sessionManager.getCampaign_type(getActivity()));
                Log.e("sessionManager",sessionManager.getCampaign_type_name(getActivity()));
                select_automated.setVisibility(View.VISIBLE);
                select_manual.setVisibility(View.GONE);
                layout_sms_second_manual.setVisibility(View.GONE);
                edit_day_manual.setText("1");
                edit_minutes_manual.setText("00");
                if (SessionManager.getTask(getActivity())!=null) {


                    if (SessionManager.getTask(getActivity()).size() >= 1) {
                        layout_sms_second_automated.setVisibility(View.VISIBLE);
                        SessionManager.setCampaign_Day(edit_day.getText().toString());
                        SessionManager.setCampaign_minute(edit_minutes.getText().toString());
                        iv_back_image.setVisibility(View.GONE);
                    } else {
                        layout_sms_second_automated.setVisibility(View.GONE);
                        iv_back_image.setVisibility(View.VISIBLE);

                    }
                }

                break;
            case R.id.manual_layout:

                SessionManager.setCampaign_type("SMS");
                SessionManager.setCampaign_type_name("MANUAL");
                Log.e("sessionManager",sessionManager.getCampaign_type(getActivity()));
                Log.e("sessionManager",sessionManager.getCampaign_type_name(getActivity()));
                select_automated.setVisibility(View.GONE);
                select_manual.setVisibility(View.VISIBLE);
                layout_sms_second_automated.setVisibility(View.GONE);
                edit_day.setText("1");
                edit_minutes.setText("00");
                if (SessionManager.getTask(getActivity())!=null) {


                    if (SessionManager.getTask(getActivity()).size() >= 1) {

                        layout_sms_second_manual.setVisibility(View.VISIBLE);
                        SessionManager.setCampaign_Day(edit_day_manual.getText().toString());
                        SessionManager.setCampaign_minute(edit_minutes_manual.getText().toString());
                        iv_back_image.setVisibility(View.GONE);
                    } else {

                        layout_sms_second_manual.setVisibility(View.GONE);
                        iv_back_image.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }

    }

    @Override
    public void onResume() {

        c_name=SessionManager.getCampaign_type_name(getActivity());
        c_type=SessionManager.getCampaign_type(getActivity());
        Log.e("c_name",c_name);
        Log.e("c_type",c_type);
        //Log.e("Frgment Call","Yes");

        if (c_type.equals("SMS"))
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

      //  sessionManager=new SessionManager(getActivity());
        c_name=SessionManager.getCampaign_type_name(getActivity());
        c_type=SessionManager.getCampaign_type(getActivity());
        /*Log.e("c_name",c_name);
        Log.e("c_type",c_type);
        Log.e("Frgment Call","Yes");*/

        if (c_type.equals("SMS"))
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
           // Log.e("Else Condition ","yes");
        }
    }
}