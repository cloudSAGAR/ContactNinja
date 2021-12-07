package com.contactninja.Model;

import java.util.ArrayList;
import java.util.List;

public class AddcontectModel {
    String mobile="";
    String email="";
    String address="";
    String city="";
    String state="";
    String email_type="";
    String mobile_type="";
    String zip_code="";
    String zoom_id="";
    String note="";

    List<Contactdetail> contactdetails =new ArrayList<>();

    public AddcontectModel()
    {

    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail_type() {
        return email_type;
    }

    public void setEmail_type(String email_type) {
        this.email_type = email_type;
    }

    public String getMobile_type() {
        return mobile_type;
    }

    public void setMobile_type(String mobile_type) {
        this.mobile_type = mobile_type;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getZoom_id() {
        return zoom_id;
    }

    public void setZoom_id(String zoom_id) {
        this.zoom_id = zoom_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Contactdetail> getContactdetails() {
        return contactdetails;
    }

    public void setContactdetails(List<Contactdetail> contactdetails) {
        this.contactdetails = contactdetails;
    }
}
