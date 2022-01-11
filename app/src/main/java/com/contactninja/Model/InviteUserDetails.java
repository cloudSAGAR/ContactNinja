package com.contactninja.Model;

import android.annotation.SuppressLint;

@SuppressLint("UnknownNullness")

public class InviteUserDetails {

    private String userimageUrl;

    public InviteUserDetails(String userimageUrl) {
        this.userimageUrl = userimageUrl;
    }

    public String getUserimageUrl() {
        return userimageUrl;
    }

    public void setUserimageUrl(String userimageUrl) {
        this.userimageUrl = userimageUrl;
    }
}
