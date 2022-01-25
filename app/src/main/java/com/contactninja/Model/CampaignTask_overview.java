package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@SuppressLint("UnknownNullness")

public class CampaignTask_overview {


    @SerializedName("0")
    @Expose
    private _0 _0;
    @SerializedName("sequence_task")
    @Expose
    private List<SequenceTask> sequenceTask = null;
    @SerializedName("sequence_prospects")
    @Expose
    private List<SequenceProspect> sequenceProspects = null;
    @SerializedName("seq_prospect_count")
    @Expose
    private SeqProspectCount seqProspectCount;

    public _0 get0() {
        return _0;
    }

    public void set0(_0 _0) {
        this._0 = _0;
    }

    public List<SequenceTask> getSequenceTask() {
        return sequenceTask;
    }

    public void setSequenceTask(List<SequenceTask> sequenceTask) {
        this.sequenceTask = sequenceTask;
    }

    public List<SequenceProspect> getSequenceProspects() {
        return sequenceProspects;
    }

    public void setSequenceProspects(List<SequenceProspect> sequenceProspects) {
        this.sequenceProspects = sequenceProspects;
    }

    public SeqProspectCount getSeqProspectCount() {
        return seqProspectCount;
    }

    public void setSeqProspectCount(SeqProspectCount seqProspectCount) {
        this.seqProspectCount = seqProspectCount;
    }

   public static class SequenceTask {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("day")
        @Expose
        private Integer day;
        @SerializedName("step_no")
        @Expose
        private Integer stepNo;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("template_id")
        @Expose
        private String templateId;
        @SerializedName("content_header")
        @Expose
        private String contentHeader="";
        @SerializedName("content_body")
        @Expose
        private String contentBody="";
        @SerializedName("manage_by")
        @Expose
        private String manageBy;
        @SerializedName("minute")
        @Expose
        private Integer minute;
        @SerializedName("priority")
        @Expose
        private String priority;
        @SerializedName("active_task_id")
        @Expose
        private String activeTaskId;
        @SerializedName("active_task_email")
        @Expose
        private String activeTaskEmail;
        @SerializedName("active_task_contact_number")
        @Expose
        private String activeTaskContactNumber;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getDay() {
            return day;
        }

        public void setDay(Integer day) {
            this.day = day;
        }

        public Integer getStepNo() {
            return stepNo;
        }

        public void setStepNo(Integer stepNo) {
            this.stepNo = stepNo;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getManageBy() {
            return manageBy;
        }

        public void setManageBy(String manageBy) {
            this.manageBy = manageBy;
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

        public Object getActiveTaskId() {
            return activeTaskId;
        }

        public void setActiveTaskId(String activeTaskId) {
            this.activeTaskId = activeTaskId;
        }

        public Object getActiveTaskEmail() {
            return activeTaskEmail;
        }

        public void setActiveTaskEmail(String activeTaskEmail) {
            this.activeTaskEmail = activeTaskEmail;
        }

        public Object getActiveTaskContactNumber() {
            return activeTaskContactNumber;
        }

        public void setActiveTaskContactNumber(String activeTaskContactNumber) {
            this.activeTaskContactNumber = activeTaskContactNumber;
        }

    }


    public static class _0 {

        @SerializedName("manualtask")
        @Expose
        private Integer manualtask;
        @SerializedName("activetask")
        @Expose
        private Integer activetask;
        @SerializedName("prospect")
        @Expose
        private Integer prospect;
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
        @SerializedName("created_by_name")
        @Expose
        private String createdByName;

        public Integer getManualtask() {
            return manualtask;
        }

        public void setManualtask(Integer manualtask) {
            this.manualtask = manualtask;
        }

        public Integer getActivetask() {
            return activetask;
        }

        public void setActivetask(Integer activetask) {
            this.activetask = activetask;
        }

        public Integer getProspect() {
            return prospect;
        }

        public void setProspect(Integer prospect) {
            this.prospect = prospect;
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

        public String getCreatedByName() {
            return createdByName;
        }

        public void setCreatedByName(String createdByName) {
            this.createdByName = createdByName;
        }

    }


    public class SeqProspectCount {

        @SerializedName("total")
        @Expose
        private Integer total;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

    }

    public static class SequenceProspect {

        @SerializedName("contact_id")
        @Expose
        private Integer contactId;
        @SerializedName("firstname")
        @Expose
        private String firstname="";
        @SerializedName("lastname")
        @Expose
        private String lastname;
        @SerializedName("mcompleted_on")
        @Expose
        private String mcompletedOn;
        @SerializedName("acompleted_on")
        @Expose
        private String acompletedOn;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("mstep_no")
        @Expose
        private Integer mstepNo;
        @SerializedName("mseq_task_id")
        @Expose
        private Integer mseqTaskId;
        @SerializedName("mtype")
        @Expose
        private String mtype;
        @SerializedName("mmanage_by")
        @Expose
        private String mmanageBy;
        @SerializedName("mday")
        @Expose
        private Integer mday;
        @SerializedName("stage")
        @Expose
        private String stage;
        @SerializedName("astep_no")
        @Expose
        private String astepNo;
        @SerializedName("aseq_task_id")
        @Expose
        private String aseqTaskId;
        @SerializedName("atype")
        @Expose
        private String atype;
        @SerializedName("amanage_by")
        @Expose
        private String amanageBy;
        @SerializedName("aday")
        @Expose
        private String aday;

        public Integer getContactId() {
            return contactId;
        }

        public void setContactId(Integer contactId) {
            this.contactId = contactId;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getMcompletedOn() {
            return mcompletedOn;
        }

        public void setMcompletedOn(String mcompletedOn) {
            this.mcompletedOn = mcompletedOn;
        }

        public String getAcompletedOn() {
            return acompletedOn;
        }

        public void setAcompletedOn(String acompletedOn) {
            this.acompletedOn = acompletedOn;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getMstepNo() {
            return mstepNo;
        }

        public void setMstepNo(Integer mstepNo) {
            this.mstepNo = mstepNo;
        }

        public Integer getMseqTaskId() {
            return mseqTaskId;
        }

        public void setMseqTaskId(Integer mseqTaskId) {
            this.mseqTaskId = mseqTaskId;
        }

        public String getMtype() {
            return mtype;
        }

        public void setMtype(String mtype) {
            this.mtype = mtype;
        }

        public String getMmanageBy() {
            return mmanageBy;
        }

        public void setMmanageBy(String mmanageBy) {
            this.mmanageBy = mmanageBy;
        }

        public Integer getMday() {
            return mday;
        }

        public void setMday(Integer mday) {
            this.mday = mday;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public String getAstepNo() {
            return astepNo;
        }

        public void setAstepNo(String astepNo) {
            this.astepNo = astepNo;
        }

        public String getAseqTaskId() {
            return aseqTaskId;
        }

        public void setAseqTaskId(String aseqTaskId) {
            this.aseqTaskId = aseqTaskId;
        }

        public String getAtype() {
            return atype;
        }

        public void setAtype(String atype) {
            this.atype = atype;
        }

        public String getAmanageBy() {
            return amanageBy;
        }

        public void setAmanageBy(String amanageBy) {
            this.amanageBy = amanageBy;
        }

        public String getAday() {
            return aday;
        }

        public void setAday(String aday) {
            this.aday = aday;
        }

    }

}
