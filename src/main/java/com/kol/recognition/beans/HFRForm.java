package com.kol.recognition.beans;

import com.kol.recognition.components.metric.MetricType;
import com.kol.recognition.general.RecognitionAlgorithm;
import com.kol.recognition.general.settings.ComponentProperty;
import com.kol.recognition.components.RecognizerTrainType;
import com.kol.recognition.perceptualHash.distance.StringDistanceType;

public class HFRForm {
    private String fileId;
    private String recognitionType;
    private RecognitionAlgorithm algorithm;

    private MetricType metric;
    private Integer trainingImageCount;
    private Integer knnCount;
    private ComponentProperty knnComponent;
    private Integer principalComponentsCount;
    private ComponentProperty principalComponents;
    private RecognizerTrainType recognizerTrainType;

    private StringDistanceType distanceType;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public MetricType getMetric() {
        return metric;
    }

    public void setMetric(MetricType metric) {
        this.metric = metric;
    }

    public RecognitionAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(RecognitionAlgorithm algorithm) {
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

    public ComponentProperty getKnnComponent() {
        return knnComponent;
    }

    public void setKnnComponent(ComponentProperty knnComponent) {
        this.knnComponent = knnComponent;
    }

    public RecognizerTrainType getRecognizerTrainType() {
        return recognizerTrainType;
    }

    public void setRecognizerTrainType(RecognizerTrainType recognizerTrainType) {
        this.recognizerTrainType = recognizerTrainType;
    }

    public Integer getPrincipalComponentsCount() {
        return principalComponentsCount;
    }

    public void setPrincipalComponentsCount(Integer principalComponentsCount) {
        this.principalComponentsCount = principalComponentsCount;
    }

    public ComponentProperty getPrincipalComponents() {
        return principalComponents;
    }

    public void setPrincipalComponents(ComponentProperty principalComponents) {
        this.principalComponents = principalComponents;
    }

    public String getRecognitionType() {
        return recognitionType;
    }

    public void setRecognitionType(String recognitionType) {
        this.recognitionType = recognitionType;
    }

    public StringDistanceType getDistanceType() {
        return distanceType;
    }

    public void setDistanceType(StringDistanceType distanceType) {
        this.distanceType = distanceType;
    }
}
