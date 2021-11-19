package com.intricare.test.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Plansublist{

    @SerializedName("check_id")
    @Expose
    private String check_id;

    @SerializedName("check_text")
    @Expose
    private String check_text;

    @SerializedName("check_flag")
    @Expose
    private String check_flag;
/*
    public Plansublist(String check_id,String check_text,String check_flag)
    {
        this.check_flag=check_flag;
        this.check_id=check_id;
        this.check_text=check_text;

    }*/
    public String getCheck_id() {
        return check_id;
    }

    public void setCheck_id(String check_id) {
        this.check_id = check_id;
    }

    public String getCheck_text() {
        return check_text;
    }

    public void setCheck_text(String check_text) {
        this.check_text = check_text;
    }

    public String getCheck_flag() {
        return check_flag;
    }

    public void setCheck_flag(String check_flag) {
        this.check_flag = check_flag;
    }
}
