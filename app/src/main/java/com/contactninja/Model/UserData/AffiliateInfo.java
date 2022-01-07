
package com.contactninja.Model.UserData;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressLint("UnknownNullness")
public class AffiliateInfo {

    @SerializedName("level 1")
    @Expose
    private List<LevelModel> level1 = new ArrayList<>();
    @SerializedName("level 2")
    @Expose
    private List<LevelModel> level2 = new ArrayList<>();
    @SerializedName("level 3")
    @Expose
    private List<LevelModel> level3 = new ArrayList<>();
    @SerializedName("level 4")
    @Expose
    private List<LevelModel> level4 = new ArrayList<>();
    @SerializedName("level 5")
    @Expose
    private List<LevelModel> level5 = new ArrayList<>();


    public List<LevelModel> getLevel1() {
        return level1;
    }

    public void setLevel1(List<LevelModel> level1) {
        this.level1 = level1;
    }

    public List<LevelModel> getLevel2() {
        return level2;
    }

    public void setLevel2(List<LevelModel> level2) {
        this.level2 = level2;
    }

    public List<LevelModel> getLevel3() {
        return level3;
    }

    public void setLevel3(List<LevelModel> level3) {
        this.level3 = level3;
    }

    public List<LevelModel> getLevel4() {
        return level4;
    }

    public void setLevel4(List<LevelModel> level4) {
        this.level4 = level4;
    }

    public List<LevelModel> getLevel5() {
        return level5;
    }

    public void setLevel5(List<LevelModel> level5) {
        this.level5 = level5;
    }
}
