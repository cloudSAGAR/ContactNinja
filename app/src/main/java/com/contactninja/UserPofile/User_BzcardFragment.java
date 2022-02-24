package com.contactninja.UserPofile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.contactninja.Bzcard.Select_Bzcard_Activity;
import com.contactninja.R;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class User_BzcardFragment extends Fragment implements View.OnClickListener {

    LinearLayout demo_layout;
    TextView tv_create,sub_txt;
    private long mLastClickTime=0;

    public User_BzcardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_user_bzcard, container, false);
        IntentUI(view);
        return view;
    }

    private void IntentUI(View view) {
        demo_layout=view.findViewById(R.id.demo_layout);
        demo_layout.setOnClickListener(this);
        tv_create=view.findViewById(R.id.tv_create);
        sub_txt=view.findViewById(R.id.sub_txt);
        demo_layout.setVisibility(View.VISIBLE);
        tv_create.setText("Click to create\n" +
                " BZcard");
        sub_txt.setText("A digital business card that acts like a mini-website sharing your business information, work portfolio, customer testimonials, contact information, and so much more!\n");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.demo_layout:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                startActivity(new Intent(getActivity(), Select_Bzcard_Activity.class));
                break;
        }
    }
}