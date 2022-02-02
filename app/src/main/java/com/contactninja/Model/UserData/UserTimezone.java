
package com.contactninja.Model.UserData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserTimezone {

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
