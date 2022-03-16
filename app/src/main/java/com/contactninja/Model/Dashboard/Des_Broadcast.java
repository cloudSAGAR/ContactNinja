package com.contactninja.Model.Dashboard;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressLint("UnknownNullness")
public class Des_Broadcast {
    @SerializedName("broadcast_name")
    @Expose
    private String broadcastName;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("start_epoch")
    @Expose
    private String startEpoch;
    @SerializedName("recurring_type")
    @Expose
    private String recurringType;
    @SerializedName("recurring_detail")
    @Expose
    private Des_RecurringDetail recurringDetail;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("manage_by")
    @Expose
    private String manageBy;
    @SerializedName("content_header")
    @Expose
    private String contentHeader;
    @SerializedName("content_body")
    @Expose
    private String contentBody;

    public String getBroadcastName() {
        return broadcastName;
    }

    public void setBroadcastName(String broadcastName) {
        this.broadcastName = broadcastName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartEpoch() {
        return startEpoch;
    }

    public void setStartEpoch(String startEpoch) {
        this.startEpoch = startEpoch;
    }

    public String getRecurringType() {
        return recurringType;
    }

    public void setRecurringType(String recurringType) {
        this.recurringType = recurringType;
    }

    public Des_RecurringDetail getRecurringDetail() {
        return recurringDetail;
    }

    public void setRecurringDetail(Des_RecurringDetail recurringDetail) {
        this.recurringDetail = recurringDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManageBy() {
        return manageBy;
    }

    public void setManageBy(String manageBy) {
        this.manageBy = manageBy;
    }

    public String getContentHeader() {
        return contentHeader;
    }

    public void setContentHeader(String contentHeader) {
        this.contentHeader = contentHeader;
    }

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }
}
