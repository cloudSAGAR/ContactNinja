package com.contactninja.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.contactninja.R;
import com.contactninja.Setting.SettingActivity;
import com.contactninja.Utils.SessionManager;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfile_Main_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfile_Main_Fragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    LinearLayout layout_toolbar_logo;
    ImageView iv_Setting;

    public UserProfile_Main_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsetProgileFragment.
     */
    // TODO: Rename and change types and number of parameters
    @SuppressLint("UnknownNullness")
    public static UserProfile_Main_Fragment newInstance(String param1, String param2) {
        UserProfile_Main_Fragment fragment = new UserProfile_Main_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint({"UnknownNullness", "UseRequireInsteadOfGet"})
    @Override
    public View onCreateView(@SuppressLint("UnknownNullness") LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_profile_main, container, false);
        intentView(view);

        return view;
    }

    private void intentView(View view) {
        layout_toolbar_logo=view.findViewById(R.id.layout_toolbar_logo);
        layout_toolbar_logo.setVisibility(View.VISIBLE);
        iv_Setting=view.findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.VISIBLE);
        iv_Setting.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@SuppressLint("UnknownNullness") View v) {
        if (v.getId() == R.id.iv_Setting) {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        }
    }
}