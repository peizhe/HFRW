package com.trying.web.beans;

import org.json.JSONObject;

public class PictureCropInfo {

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

    public static PictureCropInfo fromJson(final JSONObject selection) {
        final PictureCropInfo pictureCropInfo = new PictureCropInfo();
        pictureCropInfo.setStartX(selection.getInt("x1"));
        pictureCropInfo.setStartY(selection.getInt("y1"));
        pictureCropInfo.setWidth(selection.getInt("width"));
        pictureCropInfo.setHeight(selection.getInt("height"));
        return pictureCropInfo;
    }
}