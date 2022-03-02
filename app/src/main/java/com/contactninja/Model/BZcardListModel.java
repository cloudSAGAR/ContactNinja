package com.contactninja.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BZcardListModel {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("Bizcard")
    @Expose
    private List<Bizcard> bizcard = new ArrayList<>();

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Bizcard> getBizcard() {
        return bizcard;
    }

    public void setBizcard(List<Bizcard> bizcard) {
        this.bizcard = bizcard;
    }

    public static class Bizcard {

            @SerializedName("id")
            @Expose
            private Integer id = 0;
            @SerializedName("card_name")
            @Expose
            private String cardName = "";
            @SerializedName("bzstore_image")
            @Expose
            private int bzstore_image = 0;
            @SerializedName("fields")
            @Expose
            private String fields = "";
            @SerializedName("created_at")
            @Expose
            private String createdAt = "";
            @SerializedName("created_by")
            @Expose
            private Integer createdBy = 0;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt = "";

            public Integer getId () {
                return id;
            }

            public void setId (Integer id){
                this.id = id;
            }

            public String getCardName () {
                return cardName;
            }

            public void setCardName (String cardName){
                this.cardName = cardName;
            }

            public String getFields () {
                return fields;
            }

            public void setFields (String fields){
                this.fields = fields;
            }

            public String getCreatedAt () {
                return createdAt;
            }

            public void setCreatedAt (String createdAt){
                this.createdAt = createdAt;
            }

            public Integer getCreatedBy () {
                return createdBy;
            }

            public void setCreatedBy (Integer createdBy){
                this.createdBy = createdBy;
            }

            public String getUpdatedAt () {
                return updatedAt;
            }

            public void setUpdatedAt (String updatedAt){
                this.updatedAt = updatedAt;
            }

        public int getBzstore_image() {
            return bzstore_image;
        }

        public void setBzstore_image(int bzstore_image) {
            this.bzstore_image = bzstore_image;
        }
    }
}
