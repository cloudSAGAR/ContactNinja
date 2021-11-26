package com.intricare.test.Model;

import androidx.annotation.StyleableRes;
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

    public InviteListData( String userName, String userPhoneNumber,String userImageURL,String userDescription,String f_latter,String flag) {
        this.userImageURL = userImageURL;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userDescription=userDescription;
        this.f_latter=f_latter;
        this.flag=flag;
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
}
