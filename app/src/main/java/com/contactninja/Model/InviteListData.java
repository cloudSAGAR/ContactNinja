package com.contactninja.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class InviteListData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "userImageURL")
    private String userImageURL;
    @ColumnInfo(name = "userName")
    private String userName;
    @ColumnInfo(name ="userPhoneNumber")
    private String userPhoneNumber;
    @ColumnInfo(name = "userDescription")
    private String userDescription;
    @ColumnInfo(name = "f_latter")
    private String f_latter;
    @ColumnInfo(name = "flag")
    private String  flag;
   /* @ColumnInfo(name = "country")
    private String  country;

    @ColumnInfo(name = "city")
    private String  city;
    @ColumnInfo(name = "region")
    private String  region;
    @ColumnInfo(name = "street")
    private String  street;
    @ColumnInfo(name = "postcode")
    private String  postcode;
    @ColumnInfo(name = "postType")
    private String  postType;
    @ColumnInfo(name = "note")
    private String  note;
    @ColumnInfo(name = "contect_email")
    private String  contect_email;

    @ColumnInfo(name = "contect_type")
    private String  contect_type;

    @ColumnInfo(name = "contect_type_work")
    private String  contect_type_work;


    @ColumnInfo(name = "email_type_home")
    private String  email_type_home;


    @ColumnInfo(name = "email_type_work")
    private String  email_type_work;*/

    public InviteListData( String userName, String userPhoneNumber,String userImageURL,String userDescription,String f_latter,String flag
             /*              String country,String city,String region,String street,String postcode,
                           String postType,String note,String contect_email,String contect_type,
                           String contect_type_work,String email_type_home,String email_type_work*/) {
        this.userImageURL = userImageURL;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userDescription=userDescription;
        this.f_latter=f_latter;
        this.flag=flag;
       /* this.country=country;
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
        this.email_type_work=email_type_work;*/
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

    public String getF_latter() {
        return f_latter;
    }

    public void setF_latter(String f_latter) {
        this.f_latter = f_latter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int compareTo(InviteListData lhs) {
        return 0;
    }

    /*public String getCountry() {
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
    }*/
}
