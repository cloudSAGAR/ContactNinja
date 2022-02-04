
package com.contactninja.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ContecModel {
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("phoneData")
    @Expose
    private List<PhoneDatum> phoneData = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<PhoneDatum> getPhoneData() {
        return phoneData;
    }

    public void setPhoneData(List<PhoneDatum> phoneData) {
        this.phoneData = phoneData;
    }

    public static class PhoneDatum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("organization_id")
        @Expose
        private Integer organizationId;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("team_id")
        @Expose
        private Integer teamId;
        @SerializedName("phone_number")
        @Expose
        private String phoneNumber;
        @SerializedName("country_code")
        @Expose
        private String countryCode;
        @SerializedName("phone_id")
        @Expose
        private String phoneId;
        @SerializedName("response")
        @Expose
        private String response;
        @SerializedName("is_default")
        @Expose
        private Integer isDefault;
        @SerializedName("status")
        @Expose
        private Object status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        private boolean isEmailSelect=false;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getOrganizationId() {
            return organizationId;
        }

        public void setOrganizationId(Integer organizationId) {
            this.organizationId = organizationId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getTeamId() {
            return teamId;
        }

        public void setTeamId(Integer teamId) {
            this.teamId = teamId;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getPhoneId() {
            return phoneId;
        }

        public void setPhoneId(String phoneId) {
            this.phoneId = phoneId;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public Integer getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(Integer isDefault) {
            this.isDefault = isDefault;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
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
        public boolean isEmailSelect() {
            return isEmailSelect;
        }

        public void setEmailSelect(boolean EmailSelect) {
            isEmailSelect = EmailSelect;
        }


    }

}
