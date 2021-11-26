package com.intricare.test.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class SignModel {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {



        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("email")
        @Expose
        private String email;


        @SerializedName("contact_number")
        @Expose
        private String  contact_number;
        @SerializedName("otp")
        @Expose
        private String otp;
        @SerializedName("login_type")
        @Expose
        private String loginType;
        @SerializedName("referred_by")
        @Expose
        private String referredBy;
        public Data()
        {

        }

       /* public Data(String firstName, String last_name, String email, String contact_number, String otp, String login_type, String referred_by)
        {
            this.firstName=firstName;
            this.lastName=last_name;
            this.contact_number=contact_number;
            this.email=email;
            this.otp=otp;
            this.loginType=login_type;
            this.referredBy=referred_by;

        }*/

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getLoginType() {
            return loginType;
        }

        public void setLoginType(String loginType) {
            this.loginType = loginType;
        }

        public String getReferredBy() {
            return referredBy;
        }

        public void setReferredBy(String referredBy) {
            this.referredBy = referredBy;
        }

        public String getContact_number() {
            return contact_number;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }
    }

}
