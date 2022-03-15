package com.contactninja.Model.Dashboard;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("UnknownNullness")
public class Dashboard {
    @SerializedName("AFFILIATE")
    @Expose
    private Des_AffiliateInfo affiliate;
    @SerializedName("AFFILIATE_REWARDS")
    @Expose
    private Integer AFFILIATE_REWARDS=0;
    @SerializedName("BROADCAST")
    @Expose
    private List<Des_Broadcast> broadcast = new ArrayList<>();
    @SerializedName("BIZCARD")
    @Expose
    private List<Des_Bizcard> bizcard = new ArrayList<>();
    @SerializedName("TASK")
    @Expose
    private List<Des_Task> task = new ArrayList<>();
    @SerializedName("SEQUENCE")
    @Expose
    private List<Des_Sequence> sequence = new ArrayList<>();
    @SerializedName("TASK_COUNTER")
    @Expose
    private Des_TaskCounter taskCounter;

    public Integer getAFFILIATE_REWARDS() {
        return AFFILIATE_REWARDS;
    }

    public void setAFFILIATE_REWARDS(Integer AFFILIATE_REWARDS) {
        this.AFFILIATE_REWARDS = AFFILIATE_REWARDS;
    }

    public Des_AffiliateInfo getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(Des_AffiliateInfo affiliate) {
        this.affiliate = affiliate;
    }

    public List<Des_Broadcast> getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(List<Des_Broadcast> broadcast) {
        this.broadcast = broadcast;
    }

    public List<Des_Bizcard> getBizcard() {
        return bizcard;
    }

    public void setBizcard(List<Des_Bizcard> bizcard) {
        this.bizcard = bizcard;
    }

    public List<Des_Task> getTask() {
        return task;
    }

    public void setTask(List<Des_Task> task) {
        this.task = task;
    }

    public List<Des_Sequence> getSequence() {
        return sequence;
    }

    public void setSequence(List<Des_Sequence> sequence) {
        this.sequence = sequence;
    }

    public Des_TaskCounter getTaskCounter() {
        return taskCounter;
    }

    public void setTaskCounter(Des_TaskCounter taskCounter) {
        this.taskCounter = taskCounter;
    }
}
