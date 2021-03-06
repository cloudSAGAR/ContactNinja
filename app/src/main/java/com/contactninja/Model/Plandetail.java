package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("UnknownNullness")

public class Plandetail {

    @SerializedName("plan_product_id")
    @Expose
    private String plan_product_id="";

    @SerializedName("plan_name")
    @Expose
    private String plan_name="";

    @SerializedName("plan_description")
    @Expose
    private String plan_description;

    @SerializedName("plan_free")
    @Expose
    private String plan_free;

    @SerializedName("subCategories")
    @Expose
    private List<Plansublist> plansublist = new ArrayList<>();

    public String getPlan_product_id() {
        return plan_product_id;
    }

    public void setPlan_product_id(String plan_product_id) {
        this.plan_product_id = plan_product_id;
    }

    public List<Plansublist> getPlansublist() {
        return plansublist;
    }

    public void setPlansublist(List<Plansublist> plansublist) {
        this.plansublist = plansublist;
    }

    public String getPlan_free() {
        return plan_free;
    }

    public void setPlan_free(String plan_free) {
        this.plan_free = plan_free;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getPlan_description() {
        return plan_description;
    }

    public void setPlan_description(String plan_description) {
        this.plan_description = plan_description;
    }

    public static class Plansublist{

        @SerializedName("check_id")
        @Expose
        private String check_id;

        @SerializedName("check_text")
        @Expose
        private String check_text;

        @SerializedName("check_flag")
        @Expose
        private String check_flag;

        /*public Plansublist(String check_id,String check_text,String check_flag)
        {
            this.check_flag=check_flag;
            this.check_id=check_id;
            this.check_text=check_text;

        }*/
        public String getCheck_id() {
            return check_id;
        }

        public void setCheck_id(String check_id) {
            this.check_id = check_id;
        }

        public String getCheck_text() {
            return check_text;
        }

        public void setCheck_text(String check_text) {
            this.check_text = check_text;
        }

        public String getCheck_flag() {
            return check_flag;
        }

        public void setCheck_flag(String check_flag) {
            this.check_flag = check_flag;
        }
    }

}
