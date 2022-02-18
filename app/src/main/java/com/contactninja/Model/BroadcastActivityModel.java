package com.contactninja.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BroadcastActivityModel {

        @SerializedName("0")
        @Expose
        private _0 _0;
        @SerializedName("broadcast_prospect")
        @Expose
        private List<BroadcastProspect> broadcastProspect = null;

        public _0 get0() {
            return _0;
        }

        public void set0(_0 _0) {
            this._0 = _0;
        }

        public List<BroadcastProspect> getBroadcastProspect() {
            return broadcastProspect;
        }

        public void setBroadcastProspect(List<BroadcastProspect> broadcastProspect) {
            this.broadcastProspect = broadcastProspect;
        }


    public class _0 {

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
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("manage_by")
        @Expose
        private String manageBy;
        @SerializedName("content_header")
        @Expose
        private String contentHeader;
        @SerializedName("content_body")
        @Expose
        private String contentBody;
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

        public class RecurringDetail {

            @SerializedName("repeat_every")
            @Expose
            private String repeatEvery;
            @SerializedName("occurs_on")
            @Expose
            private List<OccursOn> occursOn = null;

            public String getRepeatEvery() {
                return repeatEvery;
            }

            public void setRepeatEvery(String repeatEvery) {
                this.repeatEvery = repeatEvery;
            }

            public List<OccursOn> getOccursOn() {
                return occursOn;
            }

            public void setOccursOn(List<OccursOn> occursOn) {
                this.occursOn = occursOn;
            }

        }

        public class OccursOn {

            @SerializedName("day_of_month")
            @Expose
            private String dayOfMonth;
            @SerializedName("every_week_no")
            @Expose
            private String everyWeekNo;
            @SerializedName("every_dayofweek")
            @Expose
            private String everyDayofweek;

            public String getDayOfMonth() {
                return dayOfMonth;
            }

            public void setDayOfMonth(String dayOfMonth) {
                this.dayOfMonth = dayOfMonth;
            }

            public String getEveryWeekNo() {
                return everyWeekNo;
            }

            public void setEveryWeekNo(String everyWeekNo) {
                this.everyWeekNo = everyWeekNo;
            }

            public String getEveryDayofweek() {
                return everyDayofweek;
            }

            public void setEveryDayofweek(String everyDayofweek) {
                this.everyDayofweek = everyDayofweek;
            }

        }


    }


    public static class BroadcastProspect {

        @SerializedName("contact_id")
        @Expose
        private Integer contactId;
        @SerializedName("email")
        @Expose
        private Object email;
        @SerializedName("contact_number")
        @Expose
        private String contactNumber;
        @SerializedName("group_detail")
        @Expose
        private Object groupDetail;
        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("lastname")
        @Expose
        private String lastname;

        public Integer getContactId() {
            return contactId;
        }

        public void setContactId(Integer contactId) {
            this.contactId = contactId;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public Object getGroupDetail() {
            return groupDetail;
        }

        public void setGroupDetail(Object groupDetail) {
            this.groupDetail = groupDetail;
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

    }


}
