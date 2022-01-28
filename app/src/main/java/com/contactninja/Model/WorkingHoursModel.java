package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@SuppressLint("UnknownNullness")
public class WorkingHoursModel {

    @SerializedName("user_timezone")
    @Expose
    private UserTimezone userTimezone;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("WorkingHours")
    @Expose
    private List<WorkingHour> workingHours = null;

    public UserTimezone getUserTimezone() {
        return userTimezone;
    }

    public void setUserTimezone(UserTimezone userTimezone) {
        this.userTimezone = userTimezone;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<WorkingHour> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(List<WorkingHour> workingHours) {
        this.workingHours = workingHours;
    }

    public static class WorkingHour {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("organization_id")
        @Expose
        private Integer organizationId;
        @SerializedName("team_id")
        @Expose
        private Integer teamId;
        @SerializedName("name")
        @Expose
        private String name="";
        @SerializedName("days")
        @Expose
        private String days;
        @SerializedName("is_default")
        @Expose
        private String isDefault;
        @SerializedName("start_time")
        @Expose
        private String startTime;
        @SerializedName("end_time")
        @Expose
        private String endTime;
        @SerializedName("start_time_iso")
        @Expose
        private String startTimeIso;
        @SerializedName("end_time_iso")
        @Expose
        private String endTimeIso;
        @SerializedName("server_start_time")
        @Expose
        private String serverStartTime;
        @SerializedName("server_end_time")
        @Expose
        private String serverEndTime;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("timezone_id")
        @Expose
        private Integer timezoneId;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("weekname")
        @Expose
        private String weekname;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStartTimeIso() {
            return startTimeIso;
        }

        public void setStartTimeIso(String startTimeIso) {
            this.startTimeIso = startTimeIso;
        }

        public String getEndTimeIso() {
            return endTimeIso;
        }

        public void setEndTimeIso(String endTimeIso) {
            this.endTimeIso = endTimeIso;
        }

        public String getServerStartTime() {
            return serverStartTime;
        }

        public void setServerStartTime(String serverStartTime) {
            this.serverStartTime = serverStartTime;
        }

        public String getServerEndTime() {
            return serverEndTime;
        }

        public void setServerEndTime(String serverEndTime) {
            this.serverEndTime = serverEndTime;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getTimezoneId() {
            return timezoneId;
        }

        public void setTimezoneId(Integer timezoneId) {
            this.timezoneId = timezoneId;
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

        public String getWeekname() {
            return weekname;
        }

        public void setWeekname(String weekname) {
            this.weekname = weekname;
        }
    }
    public static class UserTimezone{
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("zoneName")
        @Expose
        private String zoneName="";
        @SerializedName("utctime")
        @Expose
        private String utctime;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getZoneName() {
            return zoneName;
        }

        public void setZoneName(String zoneName) {
            this.zoneName = zoneName;
        }

        public String getUtctime() {
            return utctime;
        }

        public void setUtctime(String utctime) {
            this.utctime = utctime;
        }
    }
}
