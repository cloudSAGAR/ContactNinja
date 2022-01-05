package com.contactninja.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import kotlin.sequences.Sequence;

public class Campaign_List {
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("Sequence")
    @Expose
    private List<Campaign> campaignList = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Campaign> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(List<Campaign> campaignList) {
        this.campaignList = campaignList;
    }
    public static class Campaign{
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
        @SerializedName("seq_name")
        @Expose
        private String seqName;
        @SerializedName("seq_type")
        @Expose
        private String seqType;
        @SerializedName("max_prospect")
        @Expose
        private Integer maxProspect;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("started_on")
        @Expose
        private String started_on;
        @SerializedName("created_by_name")
        @Expose
        private String createdByName;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("finished_count")
        @Expose
        private Integer finishedCount;
        @SerializedName("active_count")
        @Expose
        private Integer activeCount;
        @SerializedName("due_count")
        @Expose
        private Integer dueCount;
        @SerializedName("total_count")
        @Expose
        private Integer totalCount;

        public String getStarted_on() {
            return started_on;
        }

        public void setStarted_on(String started_on) {
            this.started_on = started_on;
        }

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

        public Integer getMaxProspect() {
            return maxProspect;
        }

        public void setMaxProspect(Integer maxProspect) {
            this.maxProspect = maxProspect;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedByName() {
            return createdByName;
        }

        public void setCreatedByName(String createdByName) {
            this.createdByName = createdByName;
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

        public Integer getFinishedCount() {
            return finishedCount;
        }

        public void setFinishedCount(Integer finishedCount) {
            this.finishedCount = finishedCount;
        }

        public Integer getActiveCount() {
            return activeCount;
        }

        public void setActiveCount(Integer activeCount) {
            this.activeCount = activeCount;
        }

        public Integer getDueCount() {
            return dueCount;
        }

        public void setDueCount(Integer dueCount) {
            this.dueCount = dueCount;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }
    }
}
