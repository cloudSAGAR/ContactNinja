package com.contactninja.Fragment.AddContect_Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contactninja.Group.GroupActivity;
import com.contactninja.Group.SendBroadcast;
import com.contactninja.R;
import com.contactninja.Utils.SessionManager;

import java.util.ArrayList;


public class GroupFragment extends Fragment implements View.OnClickListener {



    public GroupFragment() {
        // Required empty public constructor
    }
    LinearLayout main_layout,add_new_contect_layout,group_name;
    SessionManager sessionManager;
    RecyclerView group_list;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_group, container, false);
        IntentUI(view);
        sessionManager=new SessionManager(getActivity());

        sessionManager.setGroupList(getActivity(),new ArrayList<>());

        add_new_contect_layout.setOnClickListener(this);
        group_name.setOnClickListener(this);
        main_layout.setOnClickListener(this);
        return view;
    }
    private void IntentUI(View view) {

        main_layout=view.findViewById(R.id.main_layout);
        add_new_contect_layout=view.findViewById(R.id.add_new_contect_layout);
        group_list=view.findViewById(R.id.group_list);
        layoutManager=new LinearLayoutManager(getActivity());
        group_list.setLayoutManager(layoutManager);
        group_name=view.findViewById(R.id.group_name);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        { case R.id.add_new_contect_layout:
            startActivity(new Intent(getActivity(), GroupActivity.class));
            getActivity().finish();
            break;
            case R.id.group_name:
                startActivity(new Intent(getActivity(), SendBroadcast.class));
                break;
            case R.id.main_layout:
                startActivity(new Intent(getActivity(), GroupActivity.class));
                getActivity().finish();
                break;


        }

    }
}