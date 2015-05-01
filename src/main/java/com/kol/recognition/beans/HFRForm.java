package com.kol.recognition.beans;

import com.kol.recognition.components.RecognizerTrainType;
import com.kol.recognition.components.metric.MetricType;
import com.kol.recognition.general.RecognitionAlgorithm;
import com.kol.recognition.general.settings.ComponentProperty;
import com.kol.recognition.perceptualHash.distance.StringDistanceType;

public class HFRForm {

    private String fileId;
    private String recognitionType;
    private RecognitionAlgorithm algorithm;

    private MetricType metricType;
    private StringDistanceType stringDistanceType;

    private Integer knnValue;
    private ComponentProperty knnType;

    private Integer trainingValue;
    private RecognizerTrainType trainingType;

    private Integer componentsValue;
    private ComponentProperty componentsType;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getRecognitionType() {
        return recognitionType;
    }

    public void setRecognitionType(String recognitionType) {
        this.recognitionType = recognitionType;
    }

    public RecognitionAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(RecognitionAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public StringDistanceType getStringDistanceType() {
        return stringDistanceType;
    }

    public void setStringDistanceType(StringDistanceType stringDistanceType) {
        this.stringDistanceType = stringDistanceType;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public Integer getKnnValue() {
        return knnValue;
    }

    public void setKnnValue(Integer knnValue) {
        this.knnValue = knnValue;
    }

    public ComponentProperty getKnnType() {
        return knnType;
    }

    public void setKnnType(ComponentProperty knnType) {
        this.knnType = knnType;
    }

    public Integer getTrainingValue() {
        return trainingValue;
    }

    public void setTrainingValue(Integer trainingValue) {
        this.trainingValue = trainingValue;
    }

    public RecognizerTrainType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(RecognizerTrainType trainingType) {
        this.trainingType = trainingType;
    }

    public Integer getComponentsValue() {
        return componentsValue;
    }

    public void setComponentsValue(Integer componentsValue) {
        this.componentsValue = componentsValue;
    }

    public ComponentProperty getComponentsType() {
        return componentsType;
    }

    public void setComponentsType(ComponentProperty componentsType) {
        this.componentsType = componentsType;
    }
}
