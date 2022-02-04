package com.contactninja.Model;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("UnknownNullness")

public class Timezon {
    @SerializedName("http_status")
    @Expose
    private Integer httpStatus;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<TimezonData> data = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<TimezonData> getData() {
        return data;
    }

    public void setData(List<TimezonData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class TimezonData {

        @SerializedName("tzname")
        @Expose
        private String tzname;
        @SerializedName("tzhourdiff")
        @Expose
        private String tzhourdiff;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("value")
        @Expose
        private Integer value;

        public String getTzname() {
            return tzname;
        }

        public void setTzname(String tzname) {
            this.tzname = tzname;
        }

        public String getTzhourdiff() {
            return tzhourdiff;
        }

        public void setTzhourdiff(String tzhourdiff) {
            this.tzhourdiff = tzhourdiff;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }
}
