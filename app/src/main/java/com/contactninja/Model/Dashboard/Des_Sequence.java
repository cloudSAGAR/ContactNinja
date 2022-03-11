package com.contactninja.Model.Dashboard;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressLint("UnknownNullness")
public class Des_Sequence {
    @SerializedName("manualtask")
    @Expose
    private Integer manualtask;
    @SerializedName("activetask")
    @Expose
    private Integer activetask;
    @SerializedName("prospect")
    @Expose
    private Integer prospect;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("seq_name")
    @Expose
    private String seqName;
    @SerializedName("seq_type")
    @Expose
    private String seqType;
    @SerializedName("max_prospect")
    @Expose
    private Integer maxProspect;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("started_on")
    @Expose
    private String startedOn;
    @SerializedName("created_by_name")
    @Expose
    private String createdByName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getManualtask() {
        return manualtask;
    }

    public void setManualtask(Integer manualtask) {
        this.manualtask = manualtask;
    }

    public Integer getActivetask() {
        return activetask;
    }

    public void setActivetask(Integer activetask) {
        this.activetask = activetask;
    }

    public Integer getProspect() {
        return prospect;
    }

    public void setProspect(Integer prospect) {
        this.prospect = prospect;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSeqName() {
        return seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    public String getSeqType() {
        return seqType;
    }

    public void setSeqType(String seqType) {
        this.seqType = seqType;
    }

    public Integer getMaxProspect() {
        return maxProspect;
    }

    public void setMaxProspect(Integer maxProspect) {
        this.maxProspect = maxProspect;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(String startedOn) {
        this.startedOn = startedOn;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
