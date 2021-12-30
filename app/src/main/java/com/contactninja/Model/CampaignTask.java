package com.contactninja.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CampaignTask {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("organization_id")
    @Expose
    private Integer organizationId;
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("seq_name")
    @Expose
    private String seqName;
    @SerializedName("seq_type")
    @Expose
    private String seqType;
    @SerializedName("working_hours_id")
    @Expose
    private Integer workingHoursId;
    @SerializedName("max_prospect")
    @Expose
    private Integer maxProspect;
    @SerializedName("allow_prospect_multiple_seq")
    @Expose
    private Integer allowProspectMultipleSeq;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("sequence_task")
    @Expose
    private List<SequenceTask> sequenceTask = null;

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

    public String getSeqName() {
        return seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    public String getSeqType() {
        return seqType;
    }

    public void setSeqType(String seqType) {
        this.seqType = seqType;
    }

    public Integer getWorkingHoursId() {
        return workingHoursId;
    }

    public void setWorkingHoursId(Integer workingHoursId) {
        this.workingHoursId = workingHoursId;
    }

    public Integer getMaxProspect() {
        return maxProspect;
    }

    public void setMaxProspect(Integer maxProspect) {
        this.maxProspect = maxProspect;
    }

    public Integer getAllowProspectMultipleSeq() {
        return allowProspectMultipleSeq;
    }

    public void setAllowProspectMultipleSeq(Integer allowProspectMultipleSeq) {
        this.allowProspectMultipleSeq = allowProspectMultipleSeq;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<SequenceTask> getSequenceTask() {
        return sequenceTask;
    }

    public void setSequenceTask(List<SequenceTask> sequenceTask) {
        this.sequenceTask = sequenceTask;
    }

    public class SequenceTask {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("organization_id")
        @Expose
        private Integer organizationId;
        @SerializedName("team_id")
        @Expose
        private Integer teamId;
        @SerializedName("sequence_id")
        @Expose
        private Integer sequenceId;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("content_header")
        @Expose
        private String contentHeader;
        @SerializedName("content_body")
        @Expose
        private String contentBody;
        @SerializedName("manage_by")
        @Expose
        private String manageBy;
        @SerializedName("day")
        @Expose
        private Integer day;
        @SerializedName("minute")
        @Expose
        private Integer minute;
        @SerializedName("priority")
        @Expose
        private String priority;
        @SerializedName("step_no")
        @Expose
        private Integer stepNo;
        @SerializedName("status")
        @Expose
        private String status;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getManageBy() {
            return manageBy;
        }

        public void setManageBy(String manageBy) {
            this.manageBy = manageBy;
        }

        public Integer getDay() {
            return day;
        }

        public void setDay(Integer day) {
            this.day = day;
        }

        public Integer getMinute() {
            return minute;
        }

        public void setMinute(Integer minute) {
            this.minute = minute;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public Integer getStepNo() {
            return stepNo;
        }

        public void setStepNo(Integer stepNo) {
            this.stepNo = stepNo;
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

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

}
