package com.kol.recognition.forms;

import com.kol.recognition.enums.ComponentProperty;
import com.kol.recognition.recognition.RecognizerTrainType;
import com.kol.recognition.enums.AnalysisAlgorithm;
import com.kol.recognition.enums.MetricType;

public class HFRForm {
    private String fileId;
    private MetricType metric;
    private AnalysisAlgorithm algorithm;
    private Integer knnCount;
    private Integer trainingImageCount;
    private ComponentProperty knnComponent;
    private ComponentProperty principalComponents;
    private RecognizerTrainType recognizerTrainType;
    private Integer principalComponentsCount;

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

    public AnalysisAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AnalysisAlgorithm algorithm) {
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
}
