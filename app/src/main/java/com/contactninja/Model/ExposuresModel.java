package com.contactninja.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExposuresModel {
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("mail_activity")
    @Expose
    private List<MailActivity> mailActivity = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<MailActivity> getMailActivity() {
        return mailActivity;
    }

    public void setMailActivity(List<MailActivity> mailActivity) {
        this.mailActivity = mailActivity;
    }

    public class MailActivity {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("contact_firstname")
        @Expose
        private String contactFirstname;
        @SerializedName("contact_lasttname")
        @Expose
        private String contactLasttname;
        @SerializedName("created_by_name")
        @Expose
        private String createdByName;
        @SerializedName("organization_id")
        @Expose
        private Integer organizationId;
        @SerializedName("team_id")
        @Expose
        private Integer teamId;
        @SerializedName("sequence_id")
        @Expose
        private Integer sequenceId;
        @SerializedName("seq_name")
        @Expose
        private String seqName;
        @SerializedName("active_task_id")
        @Expose
        private Object activeTaskId;
        @SerializedName("prospect_id")
        @Expose
        private Integer prospectId;
        @SerializedName("mail_module")
        @Expose
        private String mailModule;
        @SerializedName("sent_tbl_id")
        @Expose
        private Integer sentTblId;
        @SerializedName("content_json")
        @Expose
        private String contentJson;
        @SerializedName("response_json")
        @Expose
        private String responseJson;
        @SerializedName("comment")
        @Expose
        private Object comment;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("threadId")
        @Expose
        private Object threadId;
        @SerializedName("created_by")
        @Expose
        private Integer createdBy;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getContactFirstname() {
            return contactFirstname;
        }

        public void setContactFirstname(String contactFirstname) {
            this.contactFirstname = contactFirstname;
        }

        public String getContactLasttname() {
            return contactLasttname;
        }

        public void setContactLasttname(String contactLasttname) {
            this.contactLasttname = contactLasttname;
        }

        public String getCreatedByName() {
            return createdByName;
        }

        public void setCreatedByName(String createdByName) {
            this.createdByName = createdByName;
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

        public Integer getSequenceId() {
            return sequenceId;
        }

        public void setSequenceId(Integer sequenceId) {
            this.sequenceId = sequenceId;
        }

        public String getSeqName() {
            return seqName;
        }

        public void setSeqName(String seqName) {
            this.seqName = seqName;
        }

        public Object getActiveTaskId() {
            return activeTaskId;
        }

        public void setActiveTaskId(Object activeTaskId) {
            this.activeTaskId = activeTaskId;
        }

        public Integer getProspectId() {
            return prospectId;
        }

        public void setProspectId(Integer prospectId) {
            this.prospectId = prospectId;
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

        public String getContentJson() {
            return contentJson;
        }

        public void setContentJson(String contentJson) {
            this.contentJson = contentJson;
        }

        public String getResponseJson() {
            return responseJson;
        }

        public void setResponseJson(String responseJson) {
            this.responseJson = responseJson;
        }

        public Object getComment() {
            return comment;
        }

        public void setComment(Object comment) {
            this.comment = comment;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getThreadId() {
            return threadId;
        }

        public void setThreadId(Object threadId) {
            this.threadId = threadId;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

}
