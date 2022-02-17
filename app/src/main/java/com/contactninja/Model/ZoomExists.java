package com.contactninja.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZoomExists {

    @SerializedName("user_exists")
    @Expose
    private Boolean userExists;
    @SerializedName("user_timezone_name")
    @Expose
    private String userTimezoneName;
    @SerializedName("zoomUserEmail")
    @Expose
    private String zoomUserEmail;
    @SerializedName("zoom_url")
    @Expose
    private String zoom_url;

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
}
