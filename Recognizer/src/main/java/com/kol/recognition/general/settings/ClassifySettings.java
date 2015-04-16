package com.kol.recognition.general.settings;

import com.kol.recognition.components.RecognizerTrainType;
import com.kol.recognition.components.metric.Metric;
import com.kol.recognition.general.RecognitionAlgorithm;
import com.kol.recognition.perceptualHash.distance.StringDistanceType;

public final class ClassifySettings {

    private Metric metric;
    private RecognitionAlgorithm algorithm;

    private int knnCount;
    private int components;
    private int numberOfImages;

    private String type;
    private RecognizerTrainType trainType;
    private StringDistanceType distanceType;

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public RecognitionAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(RecognitionAlgorithm algorithm) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RecognizerTrainType getTrainType() {
        return trainType;
    }

    public void setTrainType(RecognizerTrainType trainType) {
        this.trainType = trainType;
    }

    public StringDistanceType getDistanceType() {
        return distanceType;
    }

    public void setDistanceType(StringDistanceType distanceType) {
        this.distanceType = distanceType;
    }
}
