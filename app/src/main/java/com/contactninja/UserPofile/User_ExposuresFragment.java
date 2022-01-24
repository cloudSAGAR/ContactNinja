package com.contactninja.UserPofile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.contactninja.Model.UserData.AffiliateInfo;
import com.contactninja.Model.UserData.SignResponseModel;
import com.contactninja.R;
import com.contactninja.Utils.SessionManager;

@SuppressLint("StaticFieldLeak,UnknownNullness,SetTextI18n,SyntheticAccessor,NotifyDataSetChanged,NonConstantResourceId,InflateParams,Recycle")
public class User_ExposuresFragment extends Fragment implements View.OnClickListener {
    TextView button_Affiliate_Report;
    public User_ExposuresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user_exposures, container, false);
        IntentUI(view);
        setdata();
        return view;
    }

    private void setdata() {
        SignResponseModel user_data = SessionManager.getGetUserdata(getActivity());

      AffiliateInfo affiliateInfo = user_data.getUser().getAffiliateInfo();
        if(affiliateInfo.getLevel1().size()!=0||
                affiliateInfo.getLevel2().size()!=0||
                affiliateInfo.getLevel3().size()!=0||
                affiliateInfo.getLevel4().size()!=0||
                affiliateInfo.getLevel5().size()!=0){
            button_Affiliate_Report.setVisibility(View.VISIBLE);
        }else {
            button_Affiliate_Report.setVisibility(View.GONE);
        }
    }

    private void IntentUI(View view) {
        button_Affiliate_Report=view.findViewById(R.id.button_Affiliate_Report);
        button_Affiliate_Report.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_Affiliate_Report:
                startActivity(new Intent(getActivity(),Affiliate_Report_LavelActivity.class));
                break;
        }
    }
}