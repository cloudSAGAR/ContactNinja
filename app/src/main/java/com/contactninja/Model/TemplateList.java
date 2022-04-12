package com.contactninja.Model;

import android.annotation.SuppressLint;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
@SuppressLint("UnknownNullness")

public class TemplateList implements Serializable {
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("Template")
    @Expose
    private List<Template> template = null;
    @SerializedName("0")
    @Expose
    private Template template1 = new Template();
    
    public Template getTemplate1() {
        return template1;
    }
    
    public void setTemplate1(Template template1) {
        this.template1 = template1;
    }
    
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Template> getTemplate() {
        return template;
    }

    public void setTemplate(List<Template> template) {
        this.template = template;
    }

    public static class Template implements Serializable  {
        boolean isSelect=true;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        @SerializedName("id")
        @Expose
        private Integer id=0;
        @SerializedName("organization_id")
        @Expose
        private Integer organizationId=0;
        @SerializedName("team_id")
        @Expose
        private Integer teamId=0;
        @SerializedName("template_name")
        @Expose
        private String templateName="";
        @SerializedName("template_slug")
        @Expose
        private String templateSlug="";
        @SerializedName("content_header")
        @Expose
        private String contentHeader="";
        @SerializedName("type")
        @Expose
        private String type="";
        @SerializedName("content_body")
        @Expose
        private String contentBody="";
        @SerializedName("status")
        @Expose
        private String status="";
        @SerializedName("created_at")
        @Expose
        private String createdAt="";
        @SerializedName("updated_at")
        @Expose
        private String updatedAt="";

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

        public String getTemplateName() {
            return templateName;
        }

        public void setTemplateName(String templateName) {
            this.templateName = templateName;
        }

        public String getTemplateSlug() {
            return templateSlug;
        }

        public void setTemplateSlug(String templateSlug) {
            this.templateSlug = templateSlug;
        }

        public String getContentHeader() {
            return contentHeader;
        }

        public void setContentHeader(String contentHeader) {
            this.contentHeader = contentHeader;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Spanned getContentBody() {
            return contentBody;
        }

        public void setContentBody(String contentBody) {
            this.contentBody = contentBody;
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
