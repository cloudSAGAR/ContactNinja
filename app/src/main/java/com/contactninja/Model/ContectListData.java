package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@SuppressLint("UnknownNullness")

public class ContectListData {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("contacts")
    @Expose
    private List<Contact> contacts = null;


    @SerializedName("contact")
    @Expose
    private List<Contact> contact = null;

    public List<Contact> getContact() {
        return contact;
    }

    public void setContact(List<Contact> contact) {
        this.contact = contact;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }



    public static class Contact {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("organization_id")
        @Expose
        private Integer organizationId;
        @SerializedName("team_id")
        @Expose
        private Integer teamId;
        @SerializedName("timezone_id")
        @Expose
        private Integer timezoneId;
        @SerializedName("company_name")
        @Expose
        private String companyName="";
        @SerializedName("contact_image")
        @Expose
        private String contactImage="";
        @SerializedName("address")
        @Expose
        private String address="";
        @SerializedName("city")
        @Expose
        private String city="";
        @SerializedName("zipcode")
        @Expose
        private String zipcode="";
        @SerializedName("zoom_id")
        @Expose
        private String zoomId="";
        @SerializedName("state")
        @Expose
        private String state="";
        @SerializedName("firstname")
        @Expose
        private String firstname="";
        @SerializedName("lastname")
        @Expose
        private String lastname="";
        @SerializedName("job_title")
        @Expose
        private String jobTitle="";
        @SerializedName("status")
        @Expose
        private String status="";
        @SerializedName("created_at")
        @Expose
        private String createdAt="";
        @SerializedName("updated_at")
        @Expose
        private String updatedAt="";
        @SerializedName("contact_details")
        @Expose
        private List<ContactDetail> contactDetails = null;
        @SerializedName("completed_task_details")
        @Expose
        private Object completedTaskDetails;
        @SerializedName("completed_mantask_details")
        @Expose
        private Object completedMantaskDetails;
        @SerializedName("contacted_at")
        @Expose
        private String contactedAt="";
        @SerializedName("contacted_at_utc")
        @Expose
        private Object contactedAtUtc;
        @SerializedName("contacted_at_user")
        @Expose
        private Object contactedAtUser;
        private String  flag ="true";
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
        @SerializedName("company_url")
        @Expose
        private String company_url="";
        @SerializedName("dob")
        @Expose
        private String dob="";
        @SerializedName("notes")
        @Expose
        private String notes="";
        @SerializedName("is_blocked")
        @Expose
        private Integer is_blocked=0;

        private String first_latter="";
        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public Integer getIs_blocked() {
            return is_blocked;
        }

        public void setIs_blocked(Integer is_blocked) {
            this.is_blocked = is_blocked;
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

        public String getCompany_url() {
            return company_url;
        }

        public void setCompany_url(String company_url) {
            this.company_url = company_url;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

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

        public String getFirst_latter() {
            return first_latter;
        }

        public void setFirst_latter(String first_latter) {
            this.first_latter = first_latter;
        }

        public Integer getTeamId() {
            return teamId;
        }

        public void setTeamId(Integer teamId) {
            this.teamId = teamId;
        }

        public Integer getTimezoneId() {
            return timezoneId;
        }

        public void setTimezoneId(Integer timezoneId) {
            this.timezoneId = timezoneId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getContactImage() {
            return contactImage;
        }

        public void setContactImage(String contactImage) {
            this.contactImage = contactImage;
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

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getZoomId() {
            return zoomId;
        }

        public void setZoomId(String zoomId) {
            this.zoomId = zoomId;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
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

        public Object getCompletedTaskDetails() {
            return completedTaskDetails;
        }

        public void setCompletedTaskDetails(Object completedTaskDetails) {
            this.completedTaskDetails = completedTaskDetails;
        }

        public Object getCompletedMantaskDetails() {
            return completedMantaskDetails;
        }

        public void setCompletedMantaskDetails(Object completedMantaskDetails) {
            this.completedMantaskDetails = completedMantaskDetails;
        }

        public String getContactedAt() {
            return contactedAt;
        }

        public void setContactedAt(String contactedAt) {
            this.contactedAt = contactedAt;
        }

        public Object getContactedAtUtc() {
            return contactedAtUtc;
        }

        public void setContactedAtUtc(Object contactedAtUtc) {
            this.contactedAtUtc = contactedAtUtc;
        }

        public Object getContactedAtUser() {
            return contactedAtUser;
        }

        public void setContactedAtUser(Object contactedAtUser) {
            this.contactedAtUser = contactedAtUser;
        }


        public static class ContactDetail {

            private boolean isPhoneSelect=false;

            public boolean isPhoneSelect() {
                return isPhoneSelect;
            }

            public void setPhoneSelect(boolean phoneSelect) {
                isPhoneSelect = phoneSelect;
            }

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("organization_id")
            @Expose
            private Integer organizationId;
            @SerializedName("team_id")
            @Expose
            private Integer teamId;
            @SerializedName("contact_id")
            @Expose
            private Integer contactId;
            @SerializedName("label")
            @Expose
            private String label;
            @SerializedName("email_number")
            @Expose
            private String emailNumber;
            @SerializedName("country_code")
            @Expose
            private String countryCode;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("is_default")
            @Expose
            private Integer isDefault;
            @SerializedName("is_blocked")
            @Expose
            private Integer isBlocked;
            @SerializedName("created_by")
            @Expose
            private Integer createdBy;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;
            @SerializedName("deleted_at")
            @Expose
            private Object deletedAt;

            @SerializedName("flag")
            @Expose
            private String flag;


            public String getFlag() {
                return flag;
            }

            public void setFlag(String flag) {
                this.flag = flag;
            }

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

            public Integer getTeamId() {
                return teamId;
            }

            public void setTeamId(Integer teamId) {
                this.teamId = teamId;
            }

            public Integer getContactId() {
                return contactId;
            }

            public void setContactId(Integer contactId) {
                this.contactId = contactId;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getEmailNumber() {
                return emailNumber;
            }

            public void setEmailNumber(String emailNumber) {
                this.emailNumber = emailNumber;
            }

            public String getCountryCode() {
                return countryCode;
            }

            public void setCountryCode(String countryCode) {
                this.countryCode = countryCode;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Integer getIsDefault() {
                return isDefault;
            }

            public void setIsDefault(Integer isDefault) {
                this.isDefault = isDefault;
            }

            public Integer getIsBlocked() {
                return isBlocked;
            }

            public void setIsBlocked(Integer isBlocked) {
                this.isBlocked = isBlocked;
            }

            public Integer getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(Integer createdBy) {
                this.createdBy = createdBy;
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

    }





}
