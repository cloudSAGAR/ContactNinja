package com.intricare.test.Fragment.AddContect_Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.intricare.test.R;


public class ExposuresFragment extends Fragment {

    public ExposuresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_exposures, container, false);
        IntentUI(view);
        return view;
    }

    private void IntentUI(View view) {

    }
}