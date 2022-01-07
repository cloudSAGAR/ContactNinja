
package com.contactninja.Model.UserData;


import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressLint("UnknownNullness")
public class Level1 {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("referred_by")
    @Expose
    private Integer referredBy;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("referred_name")
    @Expose
    private String referredName;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReferredName() {
        return referredName;
    }

    public void setReferredName(String referredName) {
        this.referredName = referredName;
    }
}
