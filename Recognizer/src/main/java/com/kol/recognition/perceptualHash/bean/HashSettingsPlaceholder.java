package com.kol.recognition.perceptualHash.bean;

import com.google.common.collect.Multimap;
import com.kol.recognition.general.Image;
import com.kol.recognition.general.SettingsPlaceholder;
import com.kol.recognition.perceptualHash.distance.StringDistance;

public class HashSettingsPlaceholder implements SettingsPlaceholder {

    private int width;
    private int height;
    private StringDistance distance;

    private Multimap<String, Hash> data;
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

    public StringDistance getDistance() {
        return distance;
    }

    public void setDistance(StringDistance distance) {
        this.distance = distance;
    }

    public Multimap<String, Hash> getData() {
        return data;
    }

    public void setData(Multimap<String, Hash> data) {
        this.data = data;
    }

    public Multimap<String, Image> getTrain() {
        return train;
    }

    public void setTrain(Multimap<String, Image> train) {
        this.train = train;
    }
}
