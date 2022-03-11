
package com.contactninja.Model.Dashboard;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.contactninja.Model.UserData.LevelModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@SuppressLint("UnknownNullness")
public class Des_AffiliateInfo implements Serializable {
    @SerializedName("count_of_level_1")
    @Expose
    private Integer countOfLevel1=0;
    @SerializedName("count_of_level_2")
    @Expose
    private Integer countOfLevel2=0;
    @SerializedName("count_of_level_3")
    @Expose
    private Integer countOfLevel3=0;
    @SerializedName("count_of_level_4")
    @Expose
    private Integer countOfLevel4=0;
    @SerializedName("count_of_level_5")
    @Expose
    private Integer countOfLevel5=0;
    @SerializedName("level_1")
    @Expose
    private List<LevelModel> level1 = new ArrayList<>();
    @SerializedName("level_2")
    @Expose
    private List<LevelModel> level2 = new ArrayList<>();
    @SerializedName("level_3")
    @Expose
    private List<LevelModel> level3 = new ArrayList<>();
    @SerializedName("level_4")
    @Expose
    private List<LevelModel> level4 = new ArrayList<>();
    @SerializedName("level_5")
    @Expose
    private List<LevelModel> level5 = new ArrayList<>();

    public Integer getCountOfLevel1() {
        return countOfLevel1;
    }

    public void setCountOfLevel1(Integer countOfLevel1) {
        this.countOfLevel1 = countOfLevel1;
    }

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

    public Integer getCountOfLevel2() {
        return countOfLevel2;
    }

    public void setCountOfLevel2(Integer countOfLevel2) {
        this.countOfLevel2 = countOfLevel2;
    }

    public Integer getCountOfLevel3() {
        return countOfLevel3;
    }

    public void setCountOfLevel3(Integer countOfLevel3) {
        this.countOfLevel3 = countOfLevel3;
    }

    public Integer getCountOfLevel4() {
        return countOfLevel4;
    }

    public void setCountOfLevel4(Integer countOfLevel4) {
        this.countOfLevel4 = countOfLevel4;
    }

    public Integer getCountOfLevel5() {
        return countOfLevel5;
    }

    public void setCountOfLevel5(Integer countOfLevel5) {
        this.countOfLevel5 = countOfLevel5;
    }
}
