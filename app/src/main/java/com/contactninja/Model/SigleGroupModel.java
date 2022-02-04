package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@SuppressLint("UnknownNullness")

public class SigleGroupModel {

    @SerializedName("groups")
    @Expose
    private List<Group> groups = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }


    public class Group {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("group_name")
        @Expose
        private String groupName;
        @SerializedName("group_image")
        @Expose
        private Object groupImage;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("organization_id")
        @Expose
        private Integer organizationId;
        @SerializedName("team_id")
        @Expose
        private Integer teamId;
        @SerializedName("contact_ids")
        @Expose
        private List<ContactId> contactIds = null;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("contact_details")
        @Expose
        private List<ContactDetail> contactDetails = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public Object getGroupImage() {
            return groupImage;
        }

        public void setGroupImage(Object groupImage) {
            this.groupImage = groupImage;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getOrganizationId() {
            return organizationId;
        }

        public void setOrganizationId(Integer organizationId) {
            this.organizationId = organizationId;
        }

        public Integer getTeamId() {
            return teamId;
        }

        public void setTeamId(Integer teamId) {
            this.teamId = teamId;
        }

        public List<ContactId> getContactIds() {
            return contactIds;
        }

        public void setContactIds(List<ContactId> contactIds) {
            this.contactIds = contactIds;
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

        public List<ContactDetail> getContactDetails() {
            return contactDetails;
        }

        public void setContactDetails(List<ContactDetail> contactDetails) {
            this.contactDetails = contactDetails;
        }

        public class ContactDetail {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("email_number")
            @Expose
            private String emailNumber;
            @SerializedName("firstname")
            @Expose
            private String firstname;
            @SerializedName("lastname")
            @Expose
            private Object lastname;
            @SerializedName("contact_image")
            @Expose
            private Object contactImage;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getEmailNumber() {
                return emailNumber;
            }

            public void setEmailNumber(String emailNumber) {
                this.emailNumber = emailNumber;
            }

            public String getFirstname() {
                return firstname;
            }

            public void setFirstname(String firstname) {
                this.firstname = firstname;
            }

            public Object getLastname() {
                return lastname;
            }

            public void setLastname(Object lastname) {
                this.lastname = lastname;
            }

            public Object getContactImage() {
                return contactImage;
            }

            public void setContactImage(Object contactImage) {
                this.contactImage = contactImage;
            }

        }


        public class ContactId {

            @SerializedName("prospect_id")
            @Expose
            private Integer prospectId;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("mobile")
            @Expose
            private String mobile;

            public Integer getProspectId() {
                return prospectId;
            }

            public void setProspectId(Integer prospectId) {
                this.prospectId = prospectId;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

        }


    }


}