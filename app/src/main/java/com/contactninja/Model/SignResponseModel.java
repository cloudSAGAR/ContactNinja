package com.contactninja.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SignResponseModel {

        @SerializedName("token_type")
        @Expose
        private String tokenType;
        @SerializedName("access_token")
        @Expose
        private String accessToken;
        @SerializedName("refresh_token")
        @Expose
        private String refreshToken;
        @SerializedName("user")
        @Expose
        User user;

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
        public class User {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("first_name")
            @Expose
            private String firstName;
            @SerializedName("last_name")
            @Expose
            private String lastName;
            @SerializedName("contact_number")
            @Expose
            private Object contactNumber;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;
            @SerializedName("deleted_at")
            @Expose
            private Object deletedAt;
            @SerializedName("userprofile")
            @Expose
            private Userprofile userprofile;
            @SerializedName("working_hours_list")
            @Expose
            private List<Object> workingHoursList = null;
            @SerializedName("reference_code")
            @Expose
            private String referenceCode;
            @SerializedName("affiliate_info")
            @Expose
            private List<Object> affiliateInfo = null;

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

            public Object getContactNumber() {
                return contactNumber;
            }

            public void setContactNumber(Object contactNumber) {
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

            public Object getDeletedAt() {
                return deletedAt;
            }

            public void setDeletedAt(Object deletedAt) {
                this.deletedAt = deletedAt;
            }

            public Userprofile getUserprofile() {
                return userprofile;
            }

            public void setUserprofile(Userprofile userprofile) {
                this.userprofile = userprofile;
            }

            public List<Object> getWorkingHoursList() {
                return workingHoursList;
            }

            public void setWorkingHoursList(List<Object> workingHoursList) {
                this.workingHoursList = workingHoursList;
            }

            public String getReferenceCode() {
                return referenceCode;
            }

            public void setReferenceCode(String referenceCode) {
                this.referenceCode = referenceCode;
            }

            public List<Object> getAffiliateInfo() {
                return affiliateInfo;
            }

            public void setAffiliateInfo(List<Object> affiliateInfo) {
                this.affiliateInfo = affiliateInfo;
            }

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
                private Integer referredBy;
                @SerializedName("referred_balance")
                @Expose
                private Object referredBalance;
                @SerializedName("timezone_id")
                @Expose
                private Object timezoneId;
                @SerializedName("affiliate_json")
                @Expose
                private List<Integer> affiliateJson = null;
                @SerializedName("contact_number")
                @Expose
                private Object contactNumber;
                @SerializedName("profile_pic")
                @Expose
                private Object profilePic;
                @SerializedName("is_social")
                @Expose
                private String isSocial;
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

                public Integer getId() {
                    return id;
                }

                public void setId(Integer id) {
                    this.id = id;
                }

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

                public Integer getReferredBy() {
                    return referredBy;
                }

                public void setReferredBy(Integer referredBy) {
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

                public Object getContactNumber() {
                    return contactNumber;
                }

                public void setContactNumber(Object contactNumber) {
                    this.contactNumber = contactNumber;
                }

                public Object getProfilePic() {
                    return profilePic;
                }

                public void setProfilePic(Object profilePic) {
                    this.profilePic = profilePic;
                }

                public String getIsSocial() {
                    return isSocial;
                }

                public void setIsSocial(String isSocial) {
                    this.isSocial = isSocial;
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

            }

    }
}





