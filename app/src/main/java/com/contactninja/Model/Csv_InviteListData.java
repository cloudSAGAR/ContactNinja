package com.contactninja.Model;

import android.annotation.SuppressLint;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
@SuppressLint("UnknownNullness")

public class Csv_InviteListData implements Serializable {
    private int id;

    private String userImageURL;
    private String userName;
    private String userPhoneNumber;
    private String userDescription;
    private String  country;
    private String  city;
    private String  region;
    private String  street;
    private String  postcode;
    private String  postType;
    private String  note;
    private String  contect_email;
    private String  contect_type;
    private String  contect_type_work;
    private String  email_type_home;
    private String  email_type_work;
    private String  last_name;
    public Csv_InviteListData(String userName, String userPhoneNumber,
                              String contect_email,
                              String note,
                              String country, String city,
                              String region, String street,
                             /* String userImageURL, String userDescription,
                              String postcode, String postType,
                              String contect_type, String contect_type_work,*/
                              String last_name) {
        this.userImageURL = userImageURL;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userDescription=userDescription;
        this.country=country;
        this.city=city;
        this.region=region;
        this.street=street;
        this.postcode=postcode;
        this.postType=postType;
        this.note=note;
        this.contect_email=contect_email;
        this.contect_type=contect_type;
        this.contect_type_work=contect_type_work;
        this.email_type_home=email_type_home;
        this.email_type_work=email_type_work;
        this.last_name=last_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int compareTo(InviteListData lhs) {
        return 0;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getContect_email() {
        return contect_email;
    }

    public void setContect_email(String contect_email) {
        this.contect_email = contect_email;
    }

    public String getContect_type() {
        return contect_type;
    }

    public void setContect_type(String contect_type) {
        this.contect_type = contect_type;
    }

    public String getContect_type_work() {
        return contect_type_work;
    }

    public void setContect_type_work(String contect_type_work) {
        this.contect_type_work = contect_type_work;
    }

    public String getEmail_type_home() {
        return email_type_home;
    }

    public void setEmail_type_home(String email_type_home) {
        this.email_type_home = email_type_home;
    }

    public String getEmail_type_work() {
        return email_type_work;
    }

    public void setEmail_type_work(String email_type_work) {
        this.email_type_work = email_type_work;
    }
}
