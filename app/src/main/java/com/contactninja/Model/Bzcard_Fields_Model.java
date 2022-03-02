package com.contactninja.Model;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("UnknownNullness")
public class Bzcard_Fields_Model {
    int card_id =0;
    String cover_image="";
    String profile_image="";
    String first_name="";
    String last_name="";
    String contant_number="";
    String email="";
    String company_name="";
    String company_id="";
    String company_logo="";
    String company_url="";
    String jobtitle="";
    String addrees="";
    String zipcode="";
    String country_code="";

    String theme ="";
    String button1_name="";
    String button1_url="";
    String button2_name="";
    String button2_url="";
    String bio_head="";
    String bio_description="";
    String html="";

    String fb="";
    String twitter="";
    String youtube="";
    String breakout="";
    String instagram="";
    String linkedin="";
    String pintrest="";
    String venmo="";
    String skypay="";
    String tiktok="";
    String snapchat="";
    String other_filed="";
    String other_filed1="";
    String cover_url="";
    String profile_url="";
    String company_logo_url="";
    List<BZ_media_information> bzMediaInformationList=new ArrayList<>();


    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public List<BZ_media_information> getBzMediaInformationList() {
        return bzMediaInformationList;
    }

    public String getCompany_logo_url() {
        return company_logo_url;
    }

    public void setCompany_logo_url(String company_logo_url) {
        this.company_logo_url = company_logo_url;
    }

    public void setBzMediaInformationList(List<BZ_media_information> bzMediaInformationList) {
        this.bzMediaInformationList = bzMediaInformationList;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getContant_number() {
        return contant_number;
    }

    public void setContant_number(String contant_number) {
        this.contant_number = contant_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getCompany_url() {
        return company_url;
    }

    public void setCompany_url(String company_url) {
        this.company_url = company_url;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getAddrees() {
        return addrees;
    }

    public void setAddrees(String addrees) {
        this.addrees = addrees;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getBreakout() {
        return breakout;
    }

    public void setBreakout(String breakout) {
        this.breakout = breakout;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getPintrest() {
        return pintrest;
    }

    public void setPintrest(String pintrest) {
        this.pintrest = pintrest;
    }

    public String getVenmo() {
        return venmo;
    }

    public void setVenmo(String venmo) {
        this.venmo = venmo;
    }

    public String getSkypay() {
        return skypay;
    }

    public void setSkypay(String skypay) {
        this.skypay = skypay;
    }

    public String getTiktok() {
        return tiktok;
    }

    public void setTiktok(String tiktok) {
        this.tiktok = tiktok;
    }

    public String getSnapchat() {
        return snapchat;
    }

    public void setSnapchat(String snapchat) {
        this.snapchat = snapchat;
    }

    public String getOther_filed() {
        return other_filed;
    }

    public void setOther_filed(String other_filed) {
        this.other_filed = other_filed;
    }

    public String getOther_filed1() {
        return other_filed1;
    }

    public void setOther_filed1(String other_filed1) {
        this.other_filed1 = other_filed1;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getButton1_name() {
        return button1_name;
    }

    public void setButton1_name(String button1_name) {
        this.button1_name = button1_name;
    }

    public String getButton1_url() {
        return button1_url;
    }

    public void setButton1_url(String button1_url) {
        this.button1_url = button1_url;
    }

    public String getBio_head() {
        return bio_head;
    }

    public void setBio_head(String bio_head) {
        this.bio_head = bio_head;
    }

    public String getBio_description() {
        return bio_description;
    }

    public void setBio_description(String bio_description) {
        this.bio_description = bio_description;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getButton2_name() {
        return button2_name;
    }

    public void setButton2_name(String button2_name) {
        this.button2_name = button2_name;
    }

    public String getButton2_url() {
        return button2_url;
    }

    public void setButton2_url(String button2_url) {
        this.button2_url = button2_url;
    }

    public static class BZ_media_information implements Serializable {
        Integer id=0;
        String media_type="";
        String media_url="";
        String FileName="";
        String media_title="";
        String media_description="";
        Integer is_featured=0;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMedia_type() {
            return media_type;
        }

        public void setMedia_type(String media_type) {
            this.media_type = media_type;
        }

        public String getMedia_url() {
            return media_url;
        }

        public void setMedia_url(String media_url) {
            this.media_url = media_url;
        }

        public String getMedia_title() {
            return media_title;
        }

        public void setMedia_title(String media_title) {
            this.media_title = media_title;
        }

        public String getMedia_description() {
            return media_description;
        }

        public void setMedia_description(String media_description) {
            this.media_description = media_description;
        }

        public Integer getIs_featured() {
            return is_featured;
        }

        public void setIs_featured(Integer is_featured) {
            this.is_featured = is_featured;
        }

        public String getFileName() {
            return FileName;
        }

        public void setFileName(String fileName) {
            FileName = fileName;
        }
    }
    public static class Bz_color_Model {
        String ColorName="";
        boolean is_Select=false;

        public String getColorName() {
            return ColorName;
        }

        public void setColorName(String colorName) {
            ColorName = colorName;
        }

        public boolean isIs_Select() {
            return is_Select;
        }

        public void setIs_Select(boolean is_Select) {
            this.is_Select = is_Select;
        }
    }
}
