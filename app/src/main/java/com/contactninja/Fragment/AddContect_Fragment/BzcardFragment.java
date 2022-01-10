package com.contactninja.Fragment.AddContect_Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.contactninja.R;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")

public class BzcardFragment extends Fragment {



    LinearLayout demo_layout;
    TextView tv_create;
    public BzcardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_bzcard, container, false);
        IntentUI(view);
        return view;
    }

    private void IntentUI(View view) {
        demo_layout=view.findViewById(R.id.demo_layout);
        tv_create=view.findViewById(R.id.tv_create);
        demo_layout.setVisibility(View.VISIBLE);
        tv_create.setText("Click to create\n" +
                " BZcard");
    }
}