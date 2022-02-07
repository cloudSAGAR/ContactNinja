package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@SuppressLint("UnknownNullness")
public class ManualTaskDetailsModel {
        @SerializedName("0")
        @Expose
        private ManualDetails manualDetails;
        @SerializedName("TASK_TYPE")
        @Expose
        private List<String> taskType = null;

        public ManualDetails get_0() {
                return manualDetails;
        }

        public void set_0(ManualDetails manualDetails) {
                this.manualDetails = manualDetails;
        }

        public List<String> getTaskType() {
                return taskType;
        }

        public void setTaskType(List<String> taskType) {
                this.taskType = taskType;
        }

        public static class ManualDetails {

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
                @SerializedName("email")
                @Expose
                private String email;
                @SerializedName("contact_number")
                @Expose
                private String contactNumber;
                @SerializedName("task_name")
                @Expose
                private String taskName;
                @SerializedName("step_no")
                @Expose
                private String step_no="";
                @SerializedName("date")
                @Expose
                private String date;
                @SerializedName("time")
                @Expose
                private String time;
                @SerializedName("start_time")
                @Expose
                private Integer startTime;
                @SerializedName("assign_to")
                @Expose
                private Integer assignTo;
                @SerializedName("status")
                @Expose
                private String status;
                @SerializedName("created_at")
                @Expose
                private String createdAt;
                @SerializedName("created_by")
                @Expose
                private Integer createdBy;
                @SerializedName("updated_at")
                @Expose
                private String updatedAt;
                @SerializedName("template_id")
                @Expose
                private String templateId;
                @SerializedName("content_header")
                @Expose
                private String contentHeader;
                @SerializedName("content_body")
                @Expose
                private String contentBody;
                @SerializedName("mail_module")
                @Expose
                private String mailModule="";
                @SerializedName("sent_tbl_id")
                @Expose
                private Integer sentTblId=0;

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

                public String getTaskName() {
                        return taskName;
                }

                public void setTaskName(String taskName) {
                        this.taskName = taskName;
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

                public Integer getAssignTo() {
                        return assignTo;
                }

                public void setAssignTo(Integer assignTo) {
                        this.assignTo = assignTo;
                }

                public String getStatus() {
                        return status;
                }

                public void setStatus(String status) {
                        this.status = status;
                }

                public String getCreatedAt() {
                        return createdAt;
                }

                public void setCreatedAt(String createdAt) {
                        this.createdAt = createdAt;
                }

                public Integer getCreatedBy() {
                        return createdBy;
                }

                public void setCreatedBy(Integer createdBy) {
                        this.createdBy = createdBy;
                }

                public String getUpdatedAt() {
                        return updatedAt;
                }

                public void setUpdatedAt(String updatedAt) {
                        this.updatedAt = updatedAt;
                }

                public Object getTemplateId() {
                        return templateId;
                }

                public void setTemplateId(String templateId) {
                        this.templateId = templateId;
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

                public void setContentBody(String contentBody) {
                        this.contentBody = contentBody;
                }

                public String getMailModule() {
                        return mailModule;
                }

                public void setMailModule(String mailModule) {
                        this.mailModule = mailModule;
                }

                public Integer getSentTblId() {
                        return sentTblId;
                }

                public void setSentTblId(Integer sentTblId) {
                        this.sentTblId = sentTblId;
                }

                public String getStep_no() {
                        return step_no;
                }

                public void setStep_no(String step_no) {
                        this.step_no = step_no;
                }
        }
}
