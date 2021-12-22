package com.contactninja.Model;

import java.util.List;

public class Broadcast_Data {
    String link;
    String link_text;
    List<Broadcast_image_list> broadcast_image_lists=null;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink_text() {
        return link_text;
    }

    public void setLink_text(String link_text) {
        this.link_text = link_text;
    }

    public List<Broadcast_image_list> getBroadcast_image_lists() {
        return broadcast_image_lists;
    }

    public void setBroadcast_image_lists(List<Broadcast_image_list> broadcast_image_lists) {
        this.broadcast_image_lists = broadcast_image_lists;
    }
}
