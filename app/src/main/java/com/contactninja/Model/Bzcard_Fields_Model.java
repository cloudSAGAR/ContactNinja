package com.contactninja.Model;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("UnknownNullness")
public class Bzcard_Fields_Model {
/*
    int card_id =0;
*/
    String card_name="";
    String cover_image="";
    String profile_image="";
    String first_name="";
    String last_name="";
    String phone_number="";
    String email_address="";
    String company_name="";
    String company_id="";
    String company_logo="";
    String company_url="";
    String jobtitle="";
    String addrees="";
    String zipcode="";
    String country_code="";

    String themeColor ="";
    String themeColorHash ="";
    String button1_name="";
    String button1_url="";
    String button2_name="";
    String button2_url="";
    String action_name="";
    String action_url="";
    String bio_head="";
    String bio_description="";
    String html="";

    String cover_url="";
    String profile_url="";
    String company_logo_url="";
    List<BZ_media_information> media_information=new ArrayList<>();
    List<BZ_media_delete> media_deletes=new ArrayList<>();

    Social_links social_links= new Social_links();

    Integer custom_btn_limit=0;
    boolean action_btn_flag=false;

    public List<BZ_media_delete> getMedia_deletes() {
        return media_deletes;
    }

    public void setMedia_deletes(List<BZ_media_delete> media_deletes) {
        this.media_deletes = media_deletes;
    }

/*
    public int getCard_id() {
        return card_id;
    }
*/

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

 /*   public void setCard_id(int card_id) {
        this.card_id = card_id;
    }
*/
    public List<BZ_media_information> getBzMediaInformationList() {
        return media_information;
    }

    public String getCompany_logo_url() {
        return company_logo_url;
    }

    public void setCompany_logo_url(String company_logo_url) {
        this.company_logo_url = company_logo_url;
    }

    public void setBzMediaInformationList(List<BZ_media_information> bzMediaInformationList) {
        this.media_information = bzMediaInformationList;
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
        return phone_number;
    }

    public void setContant_number(String contant_number) {
        this.phone_number = contant_number;
    }

    public String getEmail() {
        return email_address;
    }

    public void setEmail(String email) {
        this.email_address = email;
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



    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getThemeColorHash() {
        return themeColorHash;
    }

    public void setThemeColorHash(String themeColorHash) {
        this.themeColorHash = themeColorHash;
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

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public String getAction_url() {
        return action_url;
    }

    public void setAction_url(String action_url) {
        this.action_url = action_url;
    }

    public Integer getCustom_btn_limit() {
        return custom_btn_limit;
    }

    public void setCustom_btn_limit(Integer custom_btn_limit) {
        this.custom_btn_limit = custom_btn_limit;
    }

    public boolean isAction_btn_flag() {
        return action_btn_flag;
    }

    public void setAction_btn_flag(boolean action_btn_flag) {
        this.action_btn_flag = action_btn_flag;
    }

    public Social_links getSocial_links() {
        return social_links;
    }

    public void setSocial_links(Social_links social_links) {
        this.social_links = social_links;
    }

    public static class BZ_media_information implements Serializable {
        Integer id=0;
        String media_type="";
        String media_filePath ="";
        String media_url="";
        String media_thumbnail="";
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

        public String getMedia_filePath() {
            return media_filePath;
        }

        public void setMedia_filePath(String media_filePath) {
            this.media_filePath = media_filePath;
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

        public String getMedia_url() {
            return media_url;
        }

        public void setMedia_url(String media_url) {
            this.media_url = media_url;
        }

        public String getMedia_thumbnail() {
            return media_thumbnail;
        }

        public void setMedia_thumbnail(String media_thumbnail) {
            this.media_thumbnail = media_thumbnail;
        }
    }
    public static class BZ_media_delete implements Serializable {
        String media_type="";
        String media_url="";


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

    public static class Social_links {
        String facebook_url="";
        String twitter_url="";
        String youtube_url="";
        String breakout_url="";
        String instagram_url="";
        String linkedin_url="";
        String pinterest_url="";
        String venmo_url="";
        String skype_url="";
        String tiktok_url="";
        String snapchat_url="";
        String other_1="";
        String other_2="";

        public String getFacebook_url() {
            return facebook_url;
        }

        public void setFacebook_url(String facebook_url) {
            this.facebook_url = facebook_url;
        }

        public String getTwitter_url() {
            return twitter_url;
        }

        public void setTwitter_url(String twitter_url) {
            this.twitter_url = twitter_url;
        }

        public String getYoutube_url() {
            return youtube_url;
        }

        public void setYoutube_url(String youtube_url) {
            this.youtube_url = youtube_url;
        }

        public String getBreakout_url() {
            return breakout_url;
        }

        public void setBreakout_url(String breakout_url) {
            this.breakout_url = breakout_url;
        }

        public String getInstagram_url() {
            return instagram_url;
        }

        public void setInstagram_url(String instagram_url) {
            this.instagram_url = instagram_url;
        }

        public String getLinkedin_url() {
            return linkedin_url;
        }

        public void setLinkedin_url(String linkedin_url) {
            this.linkedin_url = linkedin_url;
        }

        public String getPinterest_url() {
            return pinterest_url;
        }

        public void setPinterest_url(String pinterest_url) {
            this.pinterest_url = pinterest_url;
        }

        public String getVenmo_url() {
            return venmo_url;
        }

        public void setVenmo_url(String venmo_url) {
            this.venmo_url = venmo_url;
        }

        public String getSkype_url() {
            return skype_url;
        }

        public void setSkype_url(String skype_url) {
            this.skype_url = skype_url;
        }

        public String getTiktok_url() {
            return tiktok_url;
        }

        public void setTiktok_url(String tiktok_url) {
            this.tiktok_url = tiktok_url;
        }

        public String getSnapchat_url() {
            return snapchat_url;
        }

        public void setSnapchat_url(String snapchat_url) {
            this.snapchat_url = snapchat_url;
        }

        public String getOther_1() {
            return other_1;
        }

        public void setOther_1(String other_1) {
            this.other_1 = other_1;
        }

        public String getOther_2() {
            return other_2;
        }

        public void setOther_2(String other_2) {
            this.other_2 = other_2;
        }
    }
}
