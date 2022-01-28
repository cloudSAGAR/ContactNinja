package com.contactninja.Interface;

import android.annotation.SuppressLint;

import com.contactninja.Model.Campaign_List;
import com.contactninja.Model.WorkingHoursModel;

public interface TimeZoneClick {
    void OnClick(@SuppressLint("UnknownNullness") WorkingHoursModel.WorkingHour workingHour);
}
