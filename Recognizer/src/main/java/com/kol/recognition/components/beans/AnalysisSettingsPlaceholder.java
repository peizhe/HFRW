package com.kol.recognition.components.beans;

import Jama.Matrix;
import com.google.common.collect.Multimap;
import com.kol.recognition.general.Image;
import com.kol.recognition.general.SettingsPlaceholder;

public class AnalysisSettingsPlaceholder implements SettingsPlaceholder {

    private Multimap<String, Matrix> data;
    private int components;
    private int vecLength;
    private Multimap<String, Image> train;
    private AnalysisSettings settings;

    public Multimap<String, Matrix> getData() {
        return data;
    }

    public void setData(Multimap<String, Matrix> data) {
        this.data = data;
    }

    public int getComponents() {
        return components;
    }

    public void setComponents(int components) {
        this.components = components;
    }

    public int getVecLength() {
        return vecLength;
    }

    public void setVecLength(int vecLength) {
        this.vecLength = vecLength;
    }

    public Multimap<String, Image> getTrain() {
        return train;
    }

    public void setTrain(Multimap<String, Image> train) {
        this.train = train;
    }

    public AnalysisSettings getSettings() {
        return settings;
    }

    public void setSettings(AnalysisSettings settings) {
        this.settings = settings;
    }
}
