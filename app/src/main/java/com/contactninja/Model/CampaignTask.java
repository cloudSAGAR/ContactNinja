package com.contactninja.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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


}
