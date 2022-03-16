package com.contactninja.Model;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("UnknownNullness")

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
    String company_id="";
    String other_company="";
    String referenceCode="";

    List<Contactdetail> contactdetails =new ArrayList<>();
    List<Contactdetail> contactdetails_email =new ArrayList<>();

    public AddcontectModel()
    {

    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getOther_company() {
        return other_company;
    }

    public void setOther_company(String other_company) {
        this.other_company = other_company;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
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

    public List<Contactdetail> getContactdetails_email() {
        return contactdetails_email;
    }

    public void setContactdetails_email(List<Contactdetail> contactdetails_email) {
        this.contactdetails_email = contactdetails_email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany_url() {
        return company_url;
    }

    public void setCompany_url(String company_url) {
        this.company_url = company_url;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return Linkedin;
    }

    public void setLinkedin(String linkedin) {
        Linkedin = linkedin;
    }

    public String getBreakoutu() {
        return breakoutu;
    }

    public void setBreakoutu(String breakoutu) {
        this.breakoutu = breakoutu;
    }
}
