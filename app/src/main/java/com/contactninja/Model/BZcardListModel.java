package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("UnknownNullness")
public class BZcardListModel {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("user_total")
    @Expose
    private Integer user_total;
    @SerializedName("Bizcard")
    @Expose
    private List<Bizcard> bizcard = new ArrayList<>();
    @SerializedName("Bizcarduser")
    @Expose
    private List<Bizcard> bizcardList_user = new ArrayList<>();

    public Integer getUser_total() {
        return user_total;
    }

    public void setUser_total(Integer user_total) {
        this.user_total = user_total;
    }

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

    public List<Bizcard> getBizcardList_user() {
        return bizcardList_user;
    }

    public void setBizcardList_user(List<Bizcard> bizcardList_user) {
        this.bizcardList_user = bizcardList_user;
    }


    public static class Bizcard {
        boolean isEdit = false;

        @SerializedName("id")
        @Expose
        private Integer id = 0;
        @SerializedName("card_name")
        @Expose
        private String cardName = "";
        @SerializedName("fields")
        @Expose
        private Bzcard_Fields_Model bzcardFieldsModel1 = new Bzcard_Fields_Model();
        @SerializedName("fields_val")
        @Expose
        private Bzcard_Fields_Model bzcardFieldsModel = new Bzcard_Fields_Model();
        @SerializedName("created_at")
        @Expose
        private String createdAt = "";
        @SerializedName("created_by")
        @Expose
        private Integer createdBy = 0;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt = "";


        public boolean isEdit() {
            return isEdit;
        }

        public void setEdit(boolean edit) {
            isEdit = edit;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public Bzcard_Fields_Model getBzcardFieldsModel() {
            return bzcardFieldsModel;
        }

        public void setBzcardFieldsModel(Bzcard_Fields_Model bzcardFieldsModel) {
            this.bzcardFieldsModel = bzcardFieldsModel;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Bzcard_Fields_Model getBzcardFieldsModel1() {
            return bzcardFieldsModel1;
        }

        public void setBzcardFieldsModel1(Bzcard_Fields_Model bzcardFieldsModel1) {
            this.bzcardFieldsModel1 = bzcardFieldsModel1;
        }
    }
}
