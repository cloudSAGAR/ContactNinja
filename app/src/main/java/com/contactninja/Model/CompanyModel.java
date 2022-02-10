package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("UnknownNullness")
public class CompanyModel {


    @SerializedName("http_status")
    @Expose
    private Integer httpStatus;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("company")
    @Expose
    private List<Company> companies = new ArrayList<>();
    @SerializedName("data")
    @Expose
    private List<Company> companies_data = new ArrayList<>();

    @SerializedName("blocked")
    @Expose
    private List<Company> blocked_companies = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Company> getData() {
        return companies;
    }

    public void setData(List<Company> data) {
        this.companies = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Company> getCompanies_data() {
        return companies_data;
    }

    public void setCompanies_data(List<Company> companies_data) {
        this.companies_data = companies_data;
    }

    public List<Company> getBlocked_companies() {
        return blocked_companies;
    }

    public void setBlocked_companies(List<Company> blocked_companies) {
        this.blocked_companies = blocked_companies;
    }

    public static class Company {
        @SerializedName("id")
        @Expose
        private Integer id = 0;
        @SerializedName("organization_id")
        @Expose
        private Integer organizationId = 0;
        @SerializedName("team_id")
        @Expose
        private Integer teamId = 0;
        @SerializedName("name")
        @Expose
        private String name = "";
        @SerializedName("address")
        @Expose
        private String address = "";
        @SerializedName("description")
        @Expose
        private String description = "";
        @SerializedName("contact_number")
        @Expose
        private String contact_number = "";

        @SerializedName("is_blocked")
        @Expose
        private Integer is_blocked = 0;

        @SerializedName("email")
        @Expose
        private String email = "";
        @SerializedName("status")
        @Expose
        private String status = "";
        @SerializedName("created_at")
        @Expose
        private String createdAt = "";
        @SerializedName("updated_at")
        @Expose
        private String updatedAt = "";

        public Integer getIs_blocked() {
            return is_blocked;
        }

        public void setIs_blocked(Integer is_blocked) {
            this.is_blocked = is_blocked;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContact_number() {
            return contact_number;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }
    }

}
