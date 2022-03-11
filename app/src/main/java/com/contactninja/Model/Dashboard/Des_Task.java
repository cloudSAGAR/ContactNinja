package com.contactninja.Model.Dashboard;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressLint("UnknownNullness")
public class Des_Task {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("task_from")
    @Expose
    private Integer taskFrom;
    @SerializedName("task_name")
    @Expose
    private String taskName;
    @SerializedName("contact_master_firstname")
    @Expose
    private String contactMasterFirstname;
    @SerializedName("contact_master_lastname")
    @Expose
    private String contactMasterLastname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTaskFrom() {
        return taskFrom;
    }

    public void setTaskFrom(Integer taskFrom) {
        this.taskFrom = taskFrom;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getContactMasterFirstname() {
        return contactMasterFirstname;
    }

    public void setContactMasterFirstname(String contactMasterFirstname) {
        this.contactMasterFirstname = contactMasterFirstname;
    }

    public String getContactMasterLastname() {
        return contactMasterLastname;
    }

    public void setContactMasterLastname(String contactMasterLastname) {
        this.contactMasterLastname = contactMasterLastname;
    }
}
