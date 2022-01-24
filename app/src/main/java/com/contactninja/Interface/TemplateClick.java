package com.contactninja.Interface;

import android.annotation.SuppressLint;

import com.contactninja.Model.TemplateList;

public interface TemplateClick {
    void OnClick(@SuppressLint("UnknownNullness") TemplateList.Template template);
}
