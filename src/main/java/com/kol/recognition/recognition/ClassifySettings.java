package com.kol.recognition.recognition;

import com.kol.recognition.enums.AnalysisAlgorithm;
import com.kol.recognition.enums.MetricType;
import com.kol.recognition.forms.HFRForm;

public class ClassifySettings {

    private MetricType metric;
    private AnalysisAlgorithm algorithm;
    private RecognizerTrainType training;

    private int knnCount;
    private int components;
    private int numberOfImages;

    public MetricType getMetric() {
        return metric;
    }

    public void setMetric(MetricType metric) {
        this.metric = metric;
    }

    public RecognizerTrainType getTrainType() {
        return training;
    }

    public void setTraining(RecognizerTrainType training) {
        this.training = training;
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

    public static ClassifySettings getInstance(final int knnCount, final int pcaCount, final int trainingImages, final HFRForm form){
        final ClassifySettings cs = new ClassifySettings();
        if(ComponentProperty.DEFAULT.equals(form.getKnnComponent()) || null == form.getKnnCount()){
            cs.setKnnCount(knnCount);
        } else {
            cs.setKnnCount(form.getKnnCount());
        }
        cs.setMetric(form.getMetric());
        cs.setAlgorithm(form.getAlgorithm());
        cs.setTraining(form.getRecognizerTrainType());

        if(ComponentProperty.DEFAULT.equals(form.getPrincipalComponents()) || null == form.getPrincipalComponentsCount()){
            cs.setComponents(pcaCount);
        } else {
            cs.setComponents(form.getPrincipalComponentsCount());
        }
        if(null == form.getTrainingImageCount()){
            cs.setNumberOfImages(trainingImages);
        } else {
            cs.setNumberOfImages(form.getTrainingImageCount());
        }
        return cs;
    }
}
