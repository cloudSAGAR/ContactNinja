
package com.contactninja.Model.UserData;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserOrganization {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("is_default")
    @Expose
    private Integer isDefault;
    @SerializedName("role_id")
    @Expose
    private Integer roleId;
    @SerializedName("organization_id")
    @Expose
    private Integer organizationId;
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("role_name")
    @Expose
    private String roleName;
    @SerializedName("team_name")
    @Expose
    private String teamName;
    @SerializedName("organization_name")
    @Expose
    private String organizationName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

}
