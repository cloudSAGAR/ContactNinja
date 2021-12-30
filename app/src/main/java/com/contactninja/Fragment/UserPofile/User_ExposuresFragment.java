package com.contactninja.Fragment.UserPofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.contactninja.R;


public class User_ExposuresFragment extends Fragment {

    public User_ExposuresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user_exposures, container, false);
        IntentUI(view);
        return view;
    }

    private void IntentUI(View view) {

    }
}