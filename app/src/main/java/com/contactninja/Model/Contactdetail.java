package com.contactninja.Model;

public class Contactdetail {
    String email_number;
    int id;
    int is_default;
    String label;
    String type;
    String country_code;
    public Contactdetail()
    {

    }
    public String getEmail_number() {
        return email_number;
    }

    public void setEmail_number(String email_number) {
        this.email_number = email_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
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

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
}
