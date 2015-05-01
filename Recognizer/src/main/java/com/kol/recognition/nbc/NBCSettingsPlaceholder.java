package com.kol.recognition.nbc;

import com.google.common.collect.Multimap;
import com.kol.recognition.general.Image;
import com.kol.recognition.general.SettingsPlaceholder;

import java.awt.image.BufferedImage;

public class NBCSettingsPlaceholder implements SettingsPlaceholder {

    private int width;
    private int height;
    private Multimap<String, BufferedImage> data;
    private Multimap<String, Image> train;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Multimap<String, BufferedImage> getData() {
        return data;
    }

    public void setData(Multimap<String, BufferedImage> data) {
        this.data = data;
    }

    public Multimap<String, Image> getTrain() {
        return train;
    }

    public void setTrain(Multimap<String, Image> train) {
        this.train = train;
    }
}
