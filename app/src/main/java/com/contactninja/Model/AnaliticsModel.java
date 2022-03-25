package com.contactninja.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AnaliticsModel {
    @SerializedName("total_impressions")
    @Expose
    private Integer totalImpressions;
    @SerializedName("impression_data")
    @Expose
    private List<Impression> impressionData = new ArrayList<>();

    public Integer getTotalImpressions() {
        return totalImpressions;
    }

    public void setTotalImpressions(Integer totalImpressions) {
        this.totalImpressions = totalImpressions;
    }

    public List<Impression> getImpressionData() {
        return impressionData;
    }

    public void setImpressionData(List<Impression> impressionData) {
        this.impressionData = impressionData;
    }

    public class Impression {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_country")
        @Expose
        private String userCountry="";
        @SerializedName("user_agent")
        @Expose
        private String userAgent="";
        @SerializedName("created_at")
        @Expose
        private String createdAt="";

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUserCountry() {
            return userCountry;
        }

        public void setUserCountry(String userCountry) {
            this.userCountry = userCountry;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}
