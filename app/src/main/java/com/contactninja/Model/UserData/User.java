package com.contactninja.Model.UserData;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressLint("UnknownNullness")
public class User {
    @SerializedName("id")
    @Expose
    private Integer id=0;
    @SerializedName("is_contact_exist")
    @Expose
    private Integer is_contact_exist=0;
    @SerializedName("first_name")
    @Expose
    private String firstName="";
    @SerializedName("last_name")
    @Expose
    private String lastName="";
    @SerializedName("contact_number")
    @Expose
    private String contactNumber="";
    @SerializedName("email")
    @Expose
    private String email="";
    @SerializedName("created_at")
    @Expose
    private String createdAt="";
    @SerializedName("updated_at")
    @Expose
    private String updatedAt="";
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt="";
    @SerializedName("userprofile")
    @Expose
    private Userprofile userprofile=new Userprofile();
    @SerializedName("working_hours_list")
    @Expose
    private List<WorkingHours> workingHoursList = new ArrayList<>();
    @SerializedName("reference_code")
    @Expose
    private String referenceCode="";
    @SerializedName("role_id")
    @Expose
    private Integer roleId=0;
    @SerializedName("role_name")
    @Expose
    private String roleName="";
    @SerializedName("user_organizations")
    @Expose
    private List<UserOrganization> userOrganizations = new ArrayList<>();
    @SerializedName("role_access")
    @Expose
    private List<RoleAccess> roleAccess = new ArrayList<>();
    @SerializedName("affiliate_info")
    @Expose
    private AffiliateInfo affiliateInfo = new AffiliateInfo();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Userprofile getUserprofile() {
        return userprofile;
    }

    public void setUserprofile(Userprofile userprofile) {
        this.userprofile = userprofile;
    }

    public List<WorkingHours> getWorkingHoursList() {
        return workingHoursList;
    }

    public void setWorkingHoursList(List<WorkingHours> workingHoursList) {
        this.workingHoursList = workingHoursList;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<UserOrganization> getUserOrganizations() {
        return userOrganizations;
    }

    public void setUserOrganizations(List<UserOrganization> userOrganizations) {
        this.userOrganizations = userOrganizations;
    }

    public List<RoleAccess> getRoleAccess() {
        return roleAccess;
    }

    public void setRoleAccess(List<RoleAccess> roleAccess) {
        this.roleAccess = roleAccess;
    }

    public AffiliateInfo getAffiliateInfo() {
        return affiliateInfo;
    }

    public void setAffiliateInfo(AffiliateInfo affiliateInfo) {
        this.affiliateInfo = affiliateInfo;
    }

    public Integer getIs_contact_exist() {
        return is_contact_exist;
    }

    public void setIs_contact_exist(Integer is_contact_exist) {
        this.is_contact_exist = is_contact_exist;
    }
}
