
package com.contactninja.Model.UserData;


import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressLint("UnknownNullness")
public class LevelModel implements Serializable {

    @SerializedName("user_id")
    @Expose
    private Integer userId;

    private String level_name;
    @SerializedName("referred_by")
    @Expose
    private Integer referredBy;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("referred_name")
    @Expose
    private String referredName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("reward_earned")
    @Expose
    private Double reward_earned=0.0;

    public Double getReward_earned() {
        return reward_earned;
    }

    public void setReward_earned(Double reward_earned) {
        this.reward_earned = reward_earned;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(Integer referredBy) {
        this.referredBy = referredBy;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getReferredName() {
        return referredName;
    }

    public void setReferredName(String referredName) {
        this.referredName = referredName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
