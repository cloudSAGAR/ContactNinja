package com.contactninja.Model.Dashboard;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressLint("UnknownNullness")
public class Des_RecurringDetail {
    @SerializedName("repeat_every")
    @Expose
    private String repeatEvery;
    @SerializedName("occurs_on")
    @Expose
    private Des_OccursOn occursOn;

    public String getRepeatEvery() {
        return repeatEvery;
    }

    public void setRepeatEvery(String repeatEvery) {
        this.repeatEvery = repeatEvery;
    }
}
