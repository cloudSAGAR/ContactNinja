package com.contactninja.Model.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Des_Bizcard {
    @SerializedName("card_name")
    @Expose
    private String card_name="";
    @SerializedName("impression")
    @Expose
    private Integer impression=0;

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public Integer getImpression() {
        return impression;
    }

    public void setImpression(Integer impression) {
        this.impression = impression;
    }
}
