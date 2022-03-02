
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
        @SerializedName("first_activated")
        @Expose
        private Object firstActivated;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("last_run_time")
        @Expose
        private Object lastRunTime;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("manage_by")
        @Expose
        private String manageBy;
        @SerializedName("content_header")
        @Expose
        private Object contentHeader;
        @SerializedName("content_body")
        @Expose
        private String contentBody;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getManageBy() {
            return manageBy;
        }

        public void setManageBy(String manageBy) {
            this.manageBy = manageBy;
        }

        public Object getContentHeader() {
            return contentHeader;
        }

        public void setContentHeader(Object contentHeader) {
            this.contentHeader = contentHeader;
        }

        public String getContentBody() {
            return contentBody;
        }

        public void setContentBody(String contentBody) {
            this.contentBody = contentBody;
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


    }









