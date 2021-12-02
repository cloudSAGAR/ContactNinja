
package com.contactninja.Model.UserData;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Level3 {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("referred_by")
    @Expose
    private Integer referredBy;
    @SerializedName("first_name")
    @Expose
    private String firstName;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
