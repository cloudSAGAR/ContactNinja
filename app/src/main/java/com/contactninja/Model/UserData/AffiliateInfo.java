
package com.contactninja.Model.UserData;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AffiliateInfo {

    @SerializedName("level 1")
    @Expose
    private List<Level1> level1 = null;
    @SerializedName("level 2")
    @Expose
    private List<Level2> level2 = null;
    @SerializedName("level 3")
    @Expose
    private List<Level3> level3 = null;
    @SerializedName("level 4")
    @Expose
    private List<Level4> level4 = null;
    @SerializedName("level 5")
    @Expose
    private List<Level5> level5 = null;

    public List<Level1> getLevel1() {
        return level1;
    }

    public void setLevel1(List<Level1> level1) {
        this.level1 = level1;
    }

    public List<Level2> getLevel2() {
        return level2;
    }

    public void setLevel2(List<Level2> level2) {
        this.level2 = level2;
    }

    public List<Level3> getLevel3() {
        return level3;
    }

    public void setLevel3(List<Level3> level3) {
        this.level3 = level3;
    }

    public List<Level4> getLevel4() {
        return level4;
    }

    public void setLevel4(List<Level4> level4) {
        this.level4 = level4;
    }

    public List<Level5> getLevel5() {
        return level5;
    }

    public void setLevel5(List<Level5> level5) {
        this.level5 = level5;
    }

}
