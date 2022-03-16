package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("UnknownNullness")
public class Subscription {
    @SerializedName("http_status")
    @Expose
    private Integer httpStatus;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<Plan> data = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private String message="";

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Plan> getData() {
        return data;
    }

    public void setData(List<Plan> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Plan {

        @SerializedName("trial_plan")
        @Expose
        private Integer trialPlan=0;
        @SerializedName("trial_expiry")
        @Expose
        private String trialExpiry="";
        @SerializedName("purchased_planid")
        @Expose
        private String purchasedPlanid="";

        public Integer getTrialPlan() {
            return trialPlan;
        }

        public void setTrialPlan(Integer trialPlan) {
            this.trialPlan = trialPlan;
        }

        public String getTrialExpiry() {
            return trialExpiry;
        }

        public void setTrialExpiry(String trialExpiry) {
            this.trialExpiry = trialExpiry;
        }

        public String getPurchasedPlanid() {
            return purchasedPlanid;
        }

        public void setPurchasedPlanid(String purchasedPlanid) {
            this.purchasedPlanid = purchasedPlanid;
        }

    }
}
