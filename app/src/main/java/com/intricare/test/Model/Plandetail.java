package com.intricare.test.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Plandetail {

    @SerializedName("plan_name")
    @Expose
    private String plan_name;

    @SerializedName("plan_description")
    @Expose
    private String plan_description;

    @SerializedName("subCategories")
    @Expose
    private List<Plansublist> plansublist = null;

    public List<Plansublist> getPlansublist() {
        return plansublist;
    }

    public void setPlansublist(List<Plansublist> subCategories) {
        this.plansublist = plansublist;
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
