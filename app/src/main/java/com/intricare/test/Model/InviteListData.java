package com.intricare.test.Model;

public class InviteListData {
    private String userImageURL;
    private String userName;
    private String userPhoneNumber;

    public InviteListData( String userName, String userPhoneNumber) {
        this.userImageURL = userImageURL;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
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
}
