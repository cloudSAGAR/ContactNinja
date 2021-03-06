
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

    @SerializedName("trial_plan")
    @Expose
    private Integer trial_plan=0;

    @SerializedName("trial_expiry")
    @Expose
    private String trial_expiry="";
    @SerializedName("purchased_planid")
    @Expose
    private String purchased_planid="";

    @SerializedName("created_by")
    @Expose
    private String createdBy="";
    @SerializedName("referred_by")
    @Expose
    private String referredBy="";
    @SerializedName("referred_balance")
    @Expose
    private String referredBalance="";
    @SerializedName("timezone_id")
    @Expose
    private Integer timezoneId;
    @SerializedName("affiliate_json")
    @Expose
    private List<Integer> affiliateJson = null;
    @SerializedName("profile_pic")
    @Expose
    private String  profilePic;

    @SerializedName("status")
    @Expose
    private String status="";
    @SerializedName("created_at")
    @Expose
    private String createdAt="";
    @SerializedName("updated_at")
    @Expose
    private String updatedAt="";
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt="";
    @SerializedName("company_name")
    @Expose
    private String company_name="";
    @SerializedName("company_url")
    @Expose
    private String company_url="";
    @SerializedName("job_title")
    @Expose
    private String job_title="";
    @SerializedName("zoom_id")
    @Expose
    private String zoom_id="";


    @SerializedName("address")
    @Expose
    private String address="";
    @SerializedName("city")
    @Expose
    private String city="";
    @SerializedName("state")
    @Expose
    private String state="";
    @SerializedName("zipcode")
    @Expose
    private String zipcode="";



    @SerializedName("dob")
    @Expose
    private String dob="";
    @SerializedName("facebook_link")
    @Expose
    private String facebook_link="";
    @SerializedName("twitter_link")
    @Expose
    private String twitter_link="";
    @SerializedName("breakout_link")
    @Expose
    private String breakout_link="";

    @SerializedName("linkedin_link")
    @Expose
    private String linkedin_link="";
    @SerializedName("is_social")
    @Expose
    private String is_social="";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTrial_plan() {
        return trial_plan;
    }

    public void setTrial_plan(Integer trial_plan) {
        this.trial_plan = trial_plan;
    }

    public String getTrial_expiry() {
        return trial_expiry;
    }

    public void setTrial_expiry(String trial_expiry) {
        this.trial_expiry = trial_expiry;
    }

    public String getPurchased_planid() {
        return purchased_planid;
    }

    public void setPurchased_planid(String purchased_planid) {
        this.purchased_planid = purchased_planid;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public String getReferredBalance() {
        return referredBalance;
    }

    public void setReferredBalance(String referredBalance) {
        this.referredBalance = referredBalance;
    }

    public Integer getTimezoneId() {
        return timezoneId;
    }

    public void setTimezoneId(Integer timezoneId) {
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String  profilePic) {
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

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
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
