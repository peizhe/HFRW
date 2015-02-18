package com.kol.recognition.beans;

import org.json.JSONObject;

public class CropInfo {

    private Integer startX;
    private Integer startY;
    private Integer width;
    private Integer height;

    public Integer getStartX() {
        return startX;
    }

    public void setStartX(Integer startX) {
        this.startX = startX;
    }

    public Integer getStartY() {
        return startY;
    }

    public void setStartY(Integer startY) {
        this.startY = startY;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public static CropInfo fromJson(final JSONObject selection) {
        final CropInfo cropInfo = new CropInfo();
        cropInfo.setStartX(selection.getInt("x1"));
        cropInfo.setStartY(selection.getInt("y1"));
        cropInfo.setWidth(selection.getInt("width"));
        cropInfo.setHeight(selection.getInt("height"));
        return cropInfo;
    }
}