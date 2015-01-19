package com.kol.recognition.fe;

import com.kol.recognition.enums.FeatureExtractionMode;
import com.kol.recognition.enums.MetricType;
import com.kol.recognition.forms.HFRForm;
import com.kol.recognition.components.Properties;

public class ClassifySettings {

    private MetricType metric;
    private TrainingImage training;
    private FeatureExtractionMode feMode;

    private int knnCount;
    private int components;
    private int numberOfImages;

    public MetricType getMetric() {
        return metric;
    }

    public void setMetric(MetricType metric) {
        this.metric = metric;
    }

    public TrainingImage getTraining() {
        return training;
    }

    public void setTraining(TrainingImage training) {
        this.training = training;
    }

    public FeatureExtractionMode getFeMode() {
        return feMode;
    }

    public void setFeMode(FeatureExtractionMode feMode) {
        this.feMode = feMode;
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

    public static ClassifySettings getInstance(final Properties prop, final HFRForm form){
        final ClassifySettings cs = new ClassifySettings();
        if(ComponentProperty.DEFAULT.equals(form.getKnnComponent()) || null == form.getKnnCount()){
            cs.setKnnCount(prop.numOfKNNComponents);
        } else {
            cs.setKnnCount(form.getKnnCount());
        }
        cs.setMetric(form.getMetric());
        cs.setFeMode(form.getAlgorithm());
        cs.setTraining(form.getTrainingImage());

        if(ComponentProperty.DEFAULT.equals(form.getPrincipalComponents()) || null == form.getPrincipalComponentsCount()){
            cs.setComponents(prop.principalComponentsCount);
        } else {
            cs.setComponents(form.getPrincipalComponentsCount());
        }
        if(null == form.getTrainingImageCount()){
            cs.setNumberOfImages(prop.numOfImagesForTraining);
        } else {
            cs.setNumberOfImages(form.getTrainingImageCount());
        }
        return cs;
    }
}
