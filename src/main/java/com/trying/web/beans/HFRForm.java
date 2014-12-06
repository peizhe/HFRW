package com.trying.web.beans;

import com.trying.fe.KNNComponent;
import com.trying.fe.TrainingImage;
import com.trying.fe.enums.FeatureExtractionMode;
import com.trying.fe.enums.MetricType;

public class HFRForm {
    private String fileName;
    private MetricType metric;
    private FeatureExtractionMode algorithm;
    private Integer knnCount;
    private Integer trainingImageCount;
    private KNNComponent knnComponent;
    private TrainingImage trainingImage;
    private Integer principalComponentsCount;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MetricType getMetric() {
        return metric;
    }

    public void setMetric(MetricType metric) {
        this.metric = metric;
    }

    public FeatureExtractionMode getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(FeatureExtractionMode algorithm) {
        this.algorithm = algorithm;
    }

    public Integer getKnnCount() {
        return knnCount;
    }

    public void setKnnCount(Integer knnCount) {
        this.knnCount = knnCount;
    }

    public Integer getTrainingImageCount() {
        return trainingImageCount;
    }

    public void setTrainingImageCount(Integer trainingImageCount) {
        this.trainingImageCount = trainingImageCount;
    }

    public KNNComponent getKnnComponent() {
        return knnComponent;
    }

    public void setKnnComponent(KNNComponent knnComponent) {
        this.knnComponent = knnComponent;
    }

    public TrainingImage getTrainingImage() {
        return trainingImage;
    }

    public void setTrainingImage(TrainingImage trainingImage) {
        this.trainingImage = trainingImage;
    }

    public Integer getPrincipalComponentsCount() {
        return principalComponentsCount;
    }

    public void setPrincipalComponentsCount(Integer principalComponentsCount) {
        this.principalComponentsCount = principalComponentsCount;
    }
}
