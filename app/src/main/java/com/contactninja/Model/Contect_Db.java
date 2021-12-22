package com.contactninja.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Contect_Db implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "id1")
    private Integer id1;


    @ColumnInfo(name = "organization_id")
    private Integer organizationId;

    @ColumnInfo(name = "team_id")
    private Integer teamId;

    @ColumnInfo(name = "contact_id")
    private Integer contactId;


    @ColumnInfo(name = "label")
    private String label;

    @ColumnInfo(name = "first_name")
    private String first_name;

    @ColumnInfo(name = "last_name")
    private String last_name;

    @ColumnInfo(name = "flag")
    private String flag;

    @ColumnInfo(name = "contect_id")
    private String contect_id;


    @ColumnInfo(name = "email_number")
    private String emailNumber;


    @ColumnInfo(name = "country_code")
    private String countryCode;

    @ColumnInfo(name = "type")
    private String type;


    @ColumnInfo(name = "status")
    private String status;


    @ColumnInfo(name = "is_default")
    private Integer isDefault;

    @ColumnInfo(name = "is_blocked")
    private Integer isBlocked;


    @ColumnInfo(name = "created_by")
    private Integer createdBy;


    @ColumnInfo(name = "created_at")
    private String createdAt;

    @ColumnInfo(name = "updated_at")
    private String updatedAt;


    @ColumnInfo(name = "deleted_at")
    private String deletedAt;


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

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEmailNumber() {
        return emailNumber;
    }

    public void setEmailNumber(String emailNumber) {
        this.emailNumber = emailNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
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

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getContect_id() {
        return contect_id;
    }

    public void setContect_id(String contect_id) {
        this.contect_id = contect_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId1() {
        return id1;
    }

    public void setId1(Integer id1) {
        this.id1 = id1;
    }
}


