package com.contactninja.Model.Dashboard;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressLint("UnknownNullness")

public class Des_TaskCounter {
    @SerializedName("SEQUENCE")
    @Expose
    private Integer sequence;
    @SerializedName("SMS")
    @Expose
    private Integer sms;
    @SerializedName("EMAIL")
    @Expose
    private Integer email;
    @SerializedName("BROADCAST")
    @Expose
    private Integer broadcast;
    @SerializedName("MANUAL")
    @Expose
    private Integer manual;
    @SerializedName("AUTO")
    @Expose
    private Integer auto;

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getSms() {
        return sms;
    }

    public void setSms(Integer sms) {
        this.sms = sms;
    }

    public Integer getEmail() {
        return email;
    }

    public void setEmail(Integer email) {
        this.email = email;
    }

    public Integer getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(Integer broadcast) {
        this.broadcast = broadcast;
    }

    public Integer getManual() {
        return manual;
    }

    public void setManual(Integer manual) {
        this.manual = manual;
    }

    public Integer getAuto() {
        return auto;
    }

    public void setAuto(Integer auto) {
        this.auto = auto;
    }
}
