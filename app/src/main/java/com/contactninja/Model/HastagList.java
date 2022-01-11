package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@SuppressLint("UnknownNullness")

public class HastagList {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("Hashtag")
    @Expose
    private List<TemplateText> hashtag = null;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<TemplateText> getHashtag() {
        return hashtag;
    }

    public void setHashtag(List<TemplateText> hashtag) {
        this.hashtag = hashtag;
    }

    public static class TemplateText {
        boolean isSelect=false;
        int file=0;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("organization_id")
        @Expose
        private Integer organizationId;
        @SerializedName("team_id")
        @Expose
        private Integer teamId;
        @SerializedName("hashtag")
        @Expose
        private String hashtag;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("type")
        @Expose
        private String type;
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

        public String getHashtag() {
            return hashtag;
        }

        public void setHashtag(String hashtag) {
            this.hashtag = hashtag;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStatus() {
            return status;
        }

        public int getFile() {
            return file;
        }

        public void setFile(int file) {
            this.file = file;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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
