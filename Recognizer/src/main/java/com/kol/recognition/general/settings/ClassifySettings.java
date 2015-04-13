package com.kol.recognition.general.settings;

import com.kol.recognition.components.metric.Metric;
import com.kol.recognition.components.recognition.AnalysisAlgorithm;

public final class ClassifySettings {

    private Metric metric;
    private AnalysisAlgorithm algorithm;

    private int knnCount;
    private int components;
    private int numberOfImages;

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public AnalysisAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AnalysisAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public int getKnnCount() {
        return knnCount;
    }

    public void setKnnCount(int knnCount) {
        this.knnCount = knnCount;
    }

    public int getComponents() {
        return components;
    }

    public void setComponents(int components) {
        this.components = components;
    }

    public int getNumberOfImages() {
        return numberOfImages;
    }

    public void setNumberOfImages(int numberOfImages) {
        this.numberOfImages = numberOfImages;
    }
}
