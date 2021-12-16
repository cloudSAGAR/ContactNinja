
package com.contactninja.Model.UserData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoleAccess {

    @SerializedName("is_view")
    @Expose
    private Integer isView;
    @SerializedName("is_add")
    @Expose
    private Integer isAdd;
    @SerializedName("is_update")
    @Expose
    private Integer isUpdate;
    @SerializedName("is_delete")
    @Expose
    private Integer isDelete;
    @SerializedName("role_name")
    @Expose
    private String roleName;
    @SerializedName("module_name")
    @Expose
    private String moduleName;

    public Integer getIsView() {
        return isView;
    }

    public void setIsView(Integer isView) {
        this.isView = isView;
    }

    public Integer getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(Integer isAdd) {
        this.isAdd = isAdd;
    }

    public Integer getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Integer isUpdate) {
        this.isUpdate = isUpdate;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

}
