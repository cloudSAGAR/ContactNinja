package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressLint("UnknownNullness")
public class ManualTaskModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("organization_id")
    @Expose
    private Integer organizationId;
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("seq_id")
    @Expose
    private Integer seqId;
    @SerializedName("seq_task_id")
    @Expose
    private Integer seqTaskId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("prospect_id")
    @Expose
    private Integer prospectId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("start_time")
    @Expose
    private Integer startTime;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("assign_to")
    @Expose
    private Integer assignTo;
    @SerializedName("action_parameters")
    @Expose
    private String actionParameters;
    @SerializedName("task_description")
    @Expose
    private String taskDescription;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("contact_master_firstname")
    @Expose
    private String contactMasterFirstname="";
    @SerializedName("contact_master_lastname")
    @Expose
    private String contactMasterLastname="";
    @SerializedName("content_header")
    @Expose
    private String contentHeader;
    @SerializedName("content_body")
    @Expose
    private String contentBody;

    @SerializedName("task_name")
    @Expose
    private String task_name;


    @SerializedName("mail_module")
    @Expose
    private String mail_module;

    @SerializedName("sent_tbl_id")
    @Expose
    private String  sent_tbl_id;

    public String getMail_module() {
        return mail_module;
    }

    public void setMail_module(String mail_module) {
        this.mail_module = mail_module;
    }

    public String getSent_tbl_id() {
        return sent_tbl_id;
    }

    public void setSent_tbl_id(String sent_tbl_id) {
        this.sent_tbl_id = sent_tbl_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getSeqId() {
        return seqId;
    }

    public void setSeqId(Integer seqId) {
        this.seqId = seqId;
    }

    public Integer getSeqTaskId() {
        return seqTaskId;
    }

    public void setSeqTaskId(Integer seqTaskId) {
        this.seqTaskId = seqTaskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getProspectId() {
        return prospectId;
    }

    public void setProspectId(Integer prospectId) {
        this.prospectId = prospectId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Integer getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(Integer assignTo) {
        this.assignTo = assignTo;
    }

    public String getActionParameters() {
        return actionParameters;
    }

    public void setActionParameters(String actionParameters) {
        this.actionParameters = actionParameters;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getContentHeader() {
        return contentHeader;
    }

    public void setContentHeader(String contentHeader) {
        this.contentHeader = contentHeader;
    }

    public String getContentBody() {
        return contentBody;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }
}
