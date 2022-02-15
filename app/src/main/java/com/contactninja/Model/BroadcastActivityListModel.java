
package com.contactninja.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class BroadcastActivityListModel
{

        @SerializedName("total")
        @Expose
        private Integer total;
        @SerializedName("Broadcast")
        @Expose
        private List<Broadcast> broadcast = null;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<Broadcast> getBroadcast() {
            return broadcast;
        }

        public void setBroadcast(List<Broadcast> broadcast) {
            this.broadcast = broadcast;
        }


    public static class Broadcast {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("organization_id")
        @Expose
        private Integer organizationId;
        @SerializedName("team_id")
        @Expose
        private Integer teamId;
        @SerializedName("broadcast_name")
        @Expose
        private String broadcastName;
        @SerializedName("start_date")
        @Expose
        private String startDate;
        @SerializedName("start_time")
        @Expose
        private String startTime;
        @SerializedName("start_epoch")
        @Expose
        private String startEpoch;
        @SerializedName("recurring_type")
        @Expose
        private String recurringType;
        @SerializedName("recurring_detail")
        @Expose
        private List<RecurringDetail> recurringDetail = null;
        @SerializedName("first_activated")
        @Expose
        private Object firstActivated;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("last_run_time")
        @Expose
        private Object lastRunTime;
        @SerializedName("created_by")
        @Expose
        private Integer createdBy;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

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

        public String getBroadcastName() {
            return broadcastName;
        }

        public void setBroadcastName(String broadcastName) {
            this.broadcastName = broadcastName;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStartEpoch() {
            return startEpoch;
        }

        public void setStartEpoch(String startEpoch) {
            this.startEpoch = startEpoch;
        }

        public String getRecurringType() {
            return recurringType;
        }

        public void setRecurringType(String recurringType) {
            this.recurringType = recurringType;
        }

        public List<RecurringDetail> getRecurringDetail() {
            return recurringDetail;
        }

        public void setRecurringDetail(List<RecurringDetail> recurringDetail) {
            this.recurringDetail = recurringDetail;
        }

        public Object getFirstActivated() {
            return firstActivated;
        }

        public void setFirstActivated(Object firstActivated) {
            this.firstActivated = firstActivated;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getLastRunTime() {
            return lastRunTime;
        }

        public void setLastRunTime(Object lastRunTime) {
            this.lastRunTime = lastRunTime;
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

    }

    public class RecurringDetail {

        @SerializedName("repeat_every")
        @Expose
        private Integer repeatEvery;
        @SerializedName("occurs_on")
        @Expose
        private List<Integer> occursOn = null;

        public Integer getRepeatEvery() {
            return repeatEvery;
        }

        public void setRepeatEvery(Integer repeatEvery) {
            this.repeatEvery = repeatEvery;
        }

        public List<Integer> getOccursOn() {
            return occursOn;
        }

        public void setOccursOn(List<Integer> occursOn) {
            this.occursOn = occursOn;
        }

    }

}
