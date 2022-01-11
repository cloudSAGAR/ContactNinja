package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
@SuppressLint("UnknownNullness")

public class UserLinkedList implements Serializable {
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("UserLinkedGmail")
    @Expose
    private List<UserLinkedGmail> userLinkedGmail = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<UserLinkedGmail> getUserLinkedGmail() {
        return userLinkedGmail;
    }

    public void setUserLinkedGmail(List<UserLinkedGmail> userLinkedGmail) {
        this.userLinkedGmail = userLinkedGmail;
    }

    public class UserLinkedGmail implements Serializable {
        private boolean isEmailSelect=false;

        public boolean isEmailSelect() {
            return isEmailSelect;
        }

        public void setEmailSelect(boolean EmailSelect) {
            isEmailSelect = EmailSelect;
        }

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("organization_id")
        @Expose
        private Integer organizationId;
        @SerializedName("is_default")
        @Expose
        private Integer isDefault;
        @SerializedName("team_id")
        @Expose
        private Integer teamId;
        @SerializedName("user_email")
        @Expose
        private String userEmail;
        @SerializedName("picture")
        @Expose
        private String picture;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

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

        public Integer getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(Integer isDefault) {
            this.isDefault = isDefault;
        }

        public Integer getTeamId() {
            return teamId;
        }

        public void setTeamId(Integer teamId) {
            this.teamId = teamId;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
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

    }
}
