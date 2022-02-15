package com.contactninja.Model;

public class Broadcste_Coman_Model {
    String data;
    String num;
    private boolean isPhoneSelect=false;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean isPhoneSelect() {
        return isPhoneSelect;
    }

    public void setPhoneSelect(boolean phoneSelect) {
        isPhoneSelect = phoneSelect;
    }
}
