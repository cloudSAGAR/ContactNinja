
package com.contactninja.Model.UserData;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Userprofile {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("created_by")
    @Expose
    private Object createdBy;
    @SerializedName("referred_by")
    @Expose
    private Object referredBy;
    @SerializedName("referred_balance")
    @Expose
    private Object referredBalance;
    @SerializedName("timezone_id")
    @Expose
    private Object timezoneId;
    @SerializedName("affiliate_json")
    @Expose
    private List<Integer> affiliateJson = null;
    @SerializedName("profile_pic")
    @Expose
    private Object profilePic;
    @SerializedName("is_social")
    @Expose
    private String isSocial;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(Object referredBy) {
        this.referredBy = referredBy;
    }

    public Object getReferredBalance() {
        return referredBalance;
    }

    public void setReferredBalance(Object referredBalance) {
        this.referredBalance = referredBalance;
    }

    public Object getTimezoneId() {
        return timezoneId;
    }

    public void setTimezoneId(Object timezoneId) {
        this.timezoneId = timezoneId;
    }

    public List<Integer> getAffiliateJson() {
        return affiliateJson;
    }

    public void setAffiliateJson(List<Integer> affiliateJson) {
        this.affiliateJson = affiliateJson;
    }

    public Object getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Object profilePic) {
        this.profilePic = profilePic;
    }

    public String getIsSocial() {
        return isSocial;
    }

    public void setIsSocial(String isSocial) {
        this.isSocial = isSocial;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

}
