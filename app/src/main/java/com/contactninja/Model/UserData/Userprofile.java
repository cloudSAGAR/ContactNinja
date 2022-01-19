
package com.contactninja.Model.UserData;

import android.annotation.SuppressLint;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressLint("UnknownNullness")

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
    @SerializedName("company_name")
    @Expose
    private String company_name;
    @SerializedName("company_url")
    @Expose
    private String company_url;
    @SerializedName("job_title")
    @Expose
    private String job_title;
    @SerializedName("zoom_id")
    @Expose
    private String zoom_id;


    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("zipcode")
    @Expose
    private String zipcode;



    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("facebook_link")
    @Expose
    private String facebook_link;
    @SerializedName("twitter_link")
    @Expose
    private String twitter_link;
    @SerializedName("breakout_link")
    @Expose
    private String breakout_link;

    @SerializedName("linkedin_link")
    @Expose
    private String linkedin_link;
    @SerializedName("is_social")
    @Expose
    private String is_social;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @SerializedName("contact_details")
    @Expose
    private List<ContactDetail> contactDetails = null;

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

    public List<ContactDetail> getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(List<ContactDetail> contactDetails) {
        this.contactDetails = contactDetails;
    }

    public Object getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Object profilePic) {
        this.profilePic = profilePic;
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


    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_url() {
        return company_url;
    }

    public void setCompany_url(String company_url) {
        this.company_url = company_url;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getZoom_id() {
        return zoom_id;
    }

    public void setZoom_id(String zoom_id) {
        this.zoom_id = zoom_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFacebook_link() {
        return facebook_link;
    }

    public void setFacebook_link(String facebook_link) {
        this.facebook_link = facebook_link;
    }

    public String getTwitter_link() {
        return twitter_link;
    }

    public void setTwitter_link(String twitter_link) {
        this.twitter_link = twitter_link;
    }

    public String getBreakout_link() {
        return breakout_link;
    }

    public void setBreakout_link(String breakout_link) {
        this.breakout_link = breakout_link;
    }

    public String getLinkedin_link() {
        return linkedin_link;
    }

    public void setLinkedin_link(String linkedin_link) {
        this.linkedin_link = linkedin_link;
    }

    public String getIs_social() {
        return is_social;
    }

    public void setIs_social(String is_social) {
        this.is_social = is_social;
    }
}
