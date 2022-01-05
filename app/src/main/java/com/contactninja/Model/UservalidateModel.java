package com.contactninja.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UservalidateModel {
    @SerializedName("email")
    @Expose
    private List<String> email = new ArrayList<>();
    @SerializedName("email_number")
    @Expose
    private List<String> email_number = new ArrayList<>();
    @SerializedName("firstname")
    @Expose
    private List<String> firstname = new ArrayList<>();
    @SerializedName("contact_number")
    @Expose
    private List<String> contact_number = new ArrayList<>();

    @SerializedName("password")
    @Expose
    private List<String> password = new ArrayList<>();

    @SerializedName("template_slug")
    @Expose
    private List<String> template_slug = new ArrayList<>();

    public List<String> getEmail_number() {
        return email_number;
    }

    public void setEmail_number(List<String> email_number) {
        this.email_number = email_number;
    }

    @SerializedName("error")
    @Expose
    private List<String> error = null;

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

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }
}