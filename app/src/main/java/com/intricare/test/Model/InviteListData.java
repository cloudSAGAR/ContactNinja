package com.intricare.test.Model;

import androidx.annotation.StyleableRes;

public class InviteListData {
    private String userImageURL;
    private String userName;
    private String userPhoneNumber;
    private String userDescription;
    private String f_latter;

    public InviteListData( String userName, String userPhoneNumber,String userImageURL,String userDescription,String f_latter) {
        this.userImageURL = userImageURL;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userDescription=userDescription;
        this.f_latter=f_latter;
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
}
