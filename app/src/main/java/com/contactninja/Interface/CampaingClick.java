package com.contactninja.Interface;

import android.annotation.SuppressLint;

import com.contactninja.Model.Campaign_List;
import com.contactninja.Model.TemplateList;

public interface CampaingClick {
    void OnClick(@SuppressLint("UnknownNullness") Campaign_List.Campaign campaign);
}
