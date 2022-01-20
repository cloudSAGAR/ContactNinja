package com.contactninja.Campaign.Fragment;

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

import androidx.fragment.app.Fragment;

import com.contactninja.R;
import com.contactninja.Utils.SessionManager;
import com.contactninja.Utils.YourFragmentInterface;
@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle,StaticFieldLeak,UseCompatLoadingForDrawables,SetJavaScriptEnabled")
public class Campaign_Email_Fragment extends Fragment implements View.OnClickListener, YourFragmentInterface {

    LinearLayout auto_layout, manual_layout;
    ImageView select_manual, select_automated,iv_back_image;
    int click = 0;
    SessionManager sessionManager;
    String c_name = "", c_type = "";
    LinearLayout layout_email_second_automated, layout_email_second_manual;
    EditText edit_day, edit_minutes, edit_day_manual, edit_minutes_manual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_campaign__email_, container, false);
        IntentUI(view);
        sessionManager = new SessionManager(getActivity());
        c_name = SessionManager.getCampaign_type_name(getActivity());
        c_type = SessionManager.getCampaign_type(getActivity());
        if (c_type.equals("EMAIL")) {
            if (c_name.equals("AUTO")) {
                select_automated.setVisibility(View.VISIBLE);
                select_manual.setVisibility(View.GONE);
            } else if (c_name.equals("MANUAL")) {
                select_automated.setVisibility(View.GONE);
                select_manual.setVisibility(View.VISIBLE);
            }

        }
        Intent intent=getActivity().getIntent();
        Bundle bundle=intent.getExtras();
        String flag=bundle.getString("flag");
        if (flag.equals("edit")) {

            int step= bundle.getInt("step");
            Log.e("Step", String.valueOf(bundle.getString("manage_by")));
            if (step >= 1) {
                String manege_by=bundle.getString("manage_by");
                if (manege_by.equals("AUTO"))
                {
                    //Log.e("Visible is","Yes");
                    layout_email_second_automated.setVisibility(View.VISIBLE);
                    edit_day.setText(String.valueOf(bundle.getInt("day")));
                    edit_minutes.setText(String.valueOf(bundle.getInt("minute")));

                    SessionManager.setCampaign_Day(edit_day.getText().toString());
                    SessionManager.setCampaign_minute(edit_minutes.getText().toString());
                    iv_back_image.setVisibility(View.GONE);

                }
                else {

                   // Log.e("Visible is","Yes");
                    edit_day_manual.setText(String.valueOf(bundle.getInt("day")));
                    edit_minutes_manual.setText(String.valueOf(bundle.getInt("minute")));

                    layout_email_second_manual.setVisibility(View.VISIBLE);
                    SessionManager.setCampaign_Day(edit_day_manual.getText().toString());
                    SessionManager.setCampaign_minute(edit_minutes_manual.getText().toString());
                    iv_back_image.setVisibility(View.GONE);
                }




            } else {
                Log.e("Visible is","No");
                layout_email_second_manual.setVisibility(View.GONE);
                layout_email_second_automated.setVisibility(View.GONE);
                iv_back_image.setVisibility(View.VISIBLE);
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
        auto_layout = view.findViewById(R.id.auto_layout);
        manual_layout = view.findViewById(R.id.manual_layout);
        select_manual = view.findViewById(R.id.select_manual);
        select_automated = view.findViewById(R.id.select_automated);
        layout_email_second_automated = view.findViewById(R.id.layout_email_second_automated);
        layout_email_second_manual = view.findViewById(R.id.layout_email_second_manual);
        edit_day = view.findViewById(R.id.edit_day);
        edit_minutes = view.findViewById(R.id.edit_minutes);
        edit_day_manual = view.findViewById(R.id.edit_day_manual);
        edit_minutes_manual = view.findViewById(R.id.edit_minutes_manual);
        iv_back_image=view.findViewById(R.id.iv_back_image);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.auto_layout:


                // startActivity(new Intent(getActivity(), Automated_Email_Activity.class));
                //Log.e("sessionManager",sessionManager.getCampaign_type(getActivity()));
                //Log.e("sessionManager",sessionManager.getCampaign_type_name(getActivity()));
                SessionManager.setCampaign_type("EMAIL");
                SessionManager.setCampaign_type_name("AUTO");
                select_automated.setVisibility(View.VISIBLE);
                select_manual.setVisibility(View.GONE);
                layout_email_second_manual.setVisibility(View.GONE);
                edit_day_manual.setText("1");
                edit_minutes_manual.setText("00");


                if (SessionManager.getTask(getActivity()) != null) {


                    if (SessionManager.getTask(getActivity()).size() >= 1) {
                        layout_email_second_automated.setVisibility(View.VISIBLE);
                        SessionManager.setCampaign_Day(edit_day.getText().toString());
                        SessionManager.setCampaign_minute(edit_minutes.getText().toString());
                        iv_back_image.setVisibility(View.GONE);
                    } else {
                        layout_email_second_automated.setVisibility(View.GONE);
                        iv_back_image.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case R.id.manual_layout:

                SessionManager.setCampaign_type("EMAIL");
                SessionManager.setCampaign_type_name("MANUAL");
                select_automated.setVisibility(View.GONE);
                select_manual.setVisibility(View.VISIBLE);
                layout_email_second_automated.setVisibility(View.GONE);
                edit_day.setText("1");
                edit_minutes.setText("00");
                if (SessionManager.getTask(getActivity()) != null) {


                    if (SessionManager.getTask(getActivity()).size() >= 1) {
                        Log.e("Visible is","Yes");
                        layout_email_second_manual.setVisibility(View.VISIBLE);
                        SessionManager.setCampaign_Day(edit_day_manual.getText().toString());
                        SessionManager.setCampaign_minute(edit_minutes_manual.getText().toString());
                        iv_back_image.setVisibility(View.GONE);
                    } else {
                        Log.e("Visible is","No");
                        layout_email_second_manual.setVisibility(View.GONE);
                        iv_back_image.setVisibility(View.VISIBLE);
                    }
                }

                break;
        }
    }

    @Override
    public void onResume() {
        c_name = SessionManager.getCampaign_type_name(getActivity());
        c_type = SessionManager.getCampaign_type(getActivity());
        // Log.e("Frgment Call","Yes");
        Log.e("c_name", c_name);
        Log.e("c_type", c_type);
        if (c_type.equals("EMAIL")) {
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
       // sessionManager = new SessionManager(getActivity());
        c_name = SessionManager.getCampaign_type_name(getActivity());
        c_type = SessionManager.getCampaign_type(getActivity());

        Log.e("c_name", c_name);
        Log.e("c_type", c_type);
        /// Log.e("Frgment Call","Yes");
        if (c_type.equals("EMAIL")) {
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
        }
    }
}