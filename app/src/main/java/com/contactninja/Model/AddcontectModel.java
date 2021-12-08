package com.contactninja.Model;

import java.util.ArrayList;
import java.util.List;

public class AddcontectModel {

    String company="";
    String company_url="";
    String job_title="";
    String zoom_id="";
    String address="";
    String city="";
    String state="";
    String zip_code="";
    String time="";
    String birthday="";
    String note="";
    String facebook="";
    String twitter="";
    String Linkedin="";
    String breakoutu="";

    List<Contactdetail> contactdetails =new ArrayList<>();

    public AddcontectModel()
    {

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
