package com.contactninja.Model;

public class Broadcast_image_list {
    int id=0;
    String Imagename="";
    boolean isScelect=false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isScelect() {
        return isScelect;
    }

    public void setScelect(boolean scelect) {
        isScelect = scelect;
    }

    public String getImagename() {
        return Imagename;
    }

    public void setImagename(String imagename) {
        Imagename = imagename;
    }
}
