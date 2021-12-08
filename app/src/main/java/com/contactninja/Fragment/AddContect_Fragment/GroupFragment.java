package com.contactninja.Fragment.AddContect_Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contactninja.Group.GroupActivity;
import com.contactninja.R;
import com.contactninja.Utils.SessionManager;

import java.util.ArrayList;


public class GroupFragment extends Fragment {



    public GroupFragment() {
        // Required empty public constructor
    }
    LinearLayout main_layout;
    SessionManager sessionManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_group, container, false);
        IntentUI(view);
        sessionManager=new SessionManager(getActivity());

        sessionManager.setGroupList(getActivity(),new ArrayList<>());

        main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getActivity(), GroupActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }
    private void IntentUI(View view) {
        main_layout=view.findViewById(R.id.main_layout);
    }




}