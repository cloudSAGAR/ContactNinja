package com.contactninja.Model;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZoomExists {

    @SerializedName("user_exists")
    @Expose
    private Boolean userExists = false;
    @SerializedName("revokedTokens")
    @Expose
    private Boolean revokedTokens = false;
    @SerializedName("user_timezone_name")
    @Expose
    private String userTimezoneName;
    @SerializedName("zoomUserEmail")
    @Expose
    private String zoomUserEmail;
    @SerializedName("zoom_url")
    @Expose
    private String zoom_url;
    @SerializedName("zoom_meeting_link_with_password")
    @Expose
    private String zoom_meeting_link_with_password;
    @SerializedName("zoom_meeting_password")
    @Expose
    private String zoom_meeting_password;
    @SerializedName("zoom_meeting_link")
    @Expose
    private String zoom_meeting_link;

    @SerializedName("data")
    @Expose
    private Zoom data;

    public String getZoom_meeting_link() {
        return zoom_meeting_link;
    }

    public void setZoom_meeting_link(String zoom_meeting_link) {
        this.zoom_meeting_link = zoom_meeting_link;
    }

    public String getZoom_meeting_password() {
        return zoom_meeting_password;
    }

    public void setZoom_meeting_password(String zoom_meeting_password) {
        this.zoom_meeting_password = zoom_meeting_password;
    }

    public String getZoom_meeting_link_with_password() {
        return zoom_meeting_link_with_password;
    }

    public void setZoom_meeting_link_with_password(String zoom_meeting_link_with_password) {
        this.zoom_meeting_link_with_password = zoom_meeting_link_with_password;
    }

    public Boolean getRevokedTokens() {
        return revokedTokens;
    }

    public void setRevokedTokens(Boolean revokedTokens) {
        this.revokedTokens = revokedTokens;
    }

    public Boolean getUserExists() {
        return userExists;
    }

    public void setUserExists(Boolean userExists) {
        this.userExists = userExists;
    }

    public String getUserTimezoneName() {
        return userTimezoneName;
    }

    public void setUserTimezoneName(String userTimezoneName) {
        this.userTimezoneName = userTimezoneName;
    }

    public String getZoomUserEmail() {
        return zoomUserEmail;
    }

    public void setZoomUserEmail(String zoomUserEmail) {
        this.zoomUserEmail = zoomUserEmail;
    }

    public String getZoom_url() {
        return zoom_url;
    }

    public void setZoom_url(String zoom_url) {
        this.zoom_url = zoom_url;
    }

    public Zoom getData() {
        return data;
    }

    public void setData(Zoom data) {
        this.data = data;
    }

    public class Zoom {
        @SerializedName("return_status")
        @Expose
        private int return_status;
        @SerializedName("userMeResultEmail")
        @Expose
        private String userMeResultEmail;

        public String getUserMeResultEmail() {
            return userMeResultEmail;
        }

        public void setUserMeResultEmail(String userMeResultEmail) {
            this.userMeResultEmail = userMeResultEmail;
        }

        public int getReturn_status() {
            return return_status;
        }

        public void setReturn_status(int return_status) {
            this.return_status = return_status;
        }
    }
}
