package com.contactninja.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UservalidateModel {

    @SerializedName("email")
    @Expose
    private List<String> email = null;
    @SerializedName("contact_number")
    @Expose
    private List<String> contact_number = null;

    public List<String> getContact_number() {
        return contact_number;
    }

    public void setContact_number(List<String> contact_number) {
        this.contact_number = contact_number;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

}