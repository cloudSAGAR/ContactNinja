package com.contactninja.Model;

public class Contactdetail {
    String email_number;
    String id;
    String is_default;
    String label;
    String type;
    public Contactdetail()
    {

    }

    public String getEmail_number() {
        return email_number;
    }

    public void setEmail_number(String email_number) {
        this.email_number = email_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
