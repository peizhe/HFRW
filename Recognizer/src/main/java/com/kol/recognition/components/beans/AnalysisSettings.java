package com.kol.recognition.components.beans;

import com.kol.recognition.components.metric.Metric;

public class AnalysisSettings {

    private int knnCount;
    private Metric metric;
    private int width;
    private int height;
    private boolean empty;

    public AnalysisSettings() {
        this.empty = false;
    }

    public AnalysisSettings(boolean empty) {
        this.empty = empty;
    }

    public int getKnnCount() {
        return knnCount;
    }

    public void setKnnCount(int knnCount) {
        this.knnCount = knnCount;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

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

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public static AnalysisSettings empty() {
        return new AnalysisSettings(true);
    }
}
