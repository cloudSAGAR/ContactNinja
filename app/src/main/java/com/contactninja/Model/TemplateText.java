package com.contactninja.Model;

public class TemplateText {
    String TemplateText="";
    boolean isSelect=false;
    int file=0;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getTemplateText() {
        return TemplateText;
    }

    public void setTemplateText(String templateText) {
        TemplateText = templateText;
    }

    public int getFile() {
        return file;
    }

    public void setFile(int file) {
        this.file = file;
    }
}
