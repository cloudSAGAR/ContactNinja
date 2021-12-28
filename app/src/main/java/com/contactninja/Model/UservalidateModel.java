package com.contactninja.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UservalidateModel {

    @SerializedName("email")
    @Expose
    private List<String> email = null;
    @SerializedName("firstname")
    @Expose
    private List<String> firstname = null;
    @SerializedName("contact_number")
    @Expose
    private List<String> contact_number = null;

    @SerializedName("password")
    @Expose
    private List<String> password = null;

    @SerializedName("template_slug")
    @Expose
    private List<String> template_slug = null;

    public List<String> getTemplate_slug() {
        return template_slug;
    }

    public void setTemplate_slug(List<String> template_slug) {
        this.template_slug = template_slug;
    }

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

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }

    public List<String> getFirstname() {
        return firstname;
    }

    public void setFirstname(List<String> firstname) {
        this.firstname = firstname;
    }


}