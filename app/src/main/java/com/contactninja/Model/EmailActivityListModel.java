package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("UnknownNullness")

public class EmailActivityListModel {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("ManualTask")
    @Expose
    private List<ManualTaskModel> manualTask = new ArrayList<>();
    @SerializedName("all")
    @Expose
    private Integer all;
    @SerializedName("email")
    @Expose
    private Integer email;
    @SerializedName("call")
    @Expose
    private Integer call;
    @SerializedName("general")
    @Expose
    private Integer general;
    @SerializedName("sms")
    @Expose
    private Integer sms;
    @SerializedName("linkedin")
    @Expose
    private Integer linkedin;
    @SerializedName("twitter")
    @Expose
    private Integer twitter;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<ManualTaskModel> getManualTask() {
        return manualTask;
    }

    public void setManualTask(List<ManualTaskModel> manualTask) {
        this.manualTask = manualTask;
    }

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    public Integer getEmail() {
        return email;
    }

    public void setEmail(Integer email) {
        this.email = email;
    }

    public Integer getCall() {
        return call;
    }

    public void setCall(Integer call) {
        this.call = call;
    }

    public Integer getGeneral() {
        return general;
    }

    public void setGeneral(Integer general) {
        this.general = general;
    }

    public Integer getSms() {
        return sms;
    }

    public void setSms(Integer sms) {
        this.sms = sms;
    }

    public Integer getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(Integer linkedin) {
        this.linkedin = linkedin;
    }

    public Integer getTwitter() {
        return twitter;
    }

    public void setTwitter(Integer twitter) {
        this.twitter = twitter;
    }

}
