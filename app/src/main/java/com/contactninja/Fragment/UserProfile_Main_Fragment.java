package com.contactninja.Fragment;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.contactninja.R;
import com.contactninja.Utils.SessionManager;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btn_logout;
    SessionManager sessionManager;
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
    public static UserProfile_Main_Fragment newInstance(String param1, String param2) {
        UserProfile_Main_Fragment fragment = new UserProfile_Main_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_profile_main, container, false);
        sessionManager= new SessionManager(getActivity());
        intentView(view);

        return view;
    }

    private void intentView(View view) {
        layout_toolbar_logo=view.findViewById(R.id.layout_toolbar_logo);
        layout_toolbar_logo.setVisibility(View.VISIBLE);
        iv_Setting=view.findViewById(R.id.iv_Setting);
        iv_Setting.setVisibility(View.VISIBLE);
        btn_logout=view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:

                sessionManager.logoutUser();
                getActivity().finish();

                break;
        }
    }
}