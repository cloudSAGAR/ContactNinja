package com.contactninja.Model;

import android.annotation.SuppressLint;

@SuppressLint("UnknownNullness")

public class WorkTypeData {
    private String id;
    private String titale;
    private String flag;


    public WorkTypeData(String id, String titale, String flag) {

        this.flag=flag;
        this.id=id;
        this.titale=titale;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitale() {
        return titale;
    }

    public void setTitale(String titale) {
        this.titale = titale;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
