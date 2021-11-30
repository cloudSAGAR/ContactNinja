package com.contactninja.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UservalidateModel {

        @SerializedName("contact_number")
        @Expose
        private List<String> contactNumber = null;

        public List<String> getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(List<String> contactNumber) {
            this.contactNumber = contactNumber;
        }

}