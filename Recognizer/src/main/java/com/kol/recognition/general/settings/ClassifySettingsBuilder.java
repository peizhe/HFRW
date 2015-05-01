package com.kol.recognition.general.settings;

import com.google.common.base.Strings;
import com.kol.recognition.components.RecognizerTrainType;
import com.kol.recognition.components.metric.MetricType;
import com.kol.recognition.components.metric.Metric;
import com.kol.recognition.general.RecognitionAlgorithm;
import com.kol.recognition.perceptualHash.distance.StringDistanceType;

public final class ClassifySettingsBuilder {

    private int knn;
    private int pca;
    private int images;
    private String type;
    private ClassifySettings settings;

    private ClassifySettingsBuilder(){
        this.settings = new ClassifySettings();
    }

    private ClassifySettingsBuilder(int knn, int pca, int images, String type) {
        this();
        this.knn = knn;
        this.pca = pca;
        this.images = images;
        this.type = type;
    }

    public static ClassifySettingsBuilder start(final int knn, final int pca, final int images, final String type) {
        return new ClassifySettingsBuilder(knn, pca, images, type);
    }

    public ClassifySettingsBuilder knn(final Integer value, final ComponentProperty cp) {
        if(ComponentProperty.DEFAULT.equals(cp) || null == value){
            settings.setKnnCount(knn);
        } else {
            settings.setKnnCount(value);
        }
        return this;
    }

    public ClassifySettingsBuilder metric(final MetricType value) {
        if(null != value) {
            settings.setMetric(value.get());
        }
        return this;
    }

    public ClassifySettingsBuilder algorithm(final RecognitionAlgorithm algorithm) {
        settings.setAlgorithm(algorithm);
        return this;
    }

    public ClassifySettingsBuilder pca(final Integer value, final ComponentProperty cp) {
        if(ComponentProperty.DEFAULT.equals(cp) || null == value){
            settings.setComponents(pca);
        } else {
            settings.setComponents(value);
        }
        return this;
    }

    public ClassifySettingsBuilder images(final Integer value) {
        if(null == value){
            settings.setNumberOfImages(images);
        } else {
            settings.setNumberOfImages(value);
        }
        return this;
    }

    public ClassifySettings result() {
        return settings;
    }

    public static ClassifySettingsBuilder start() {
        return new ClassifySettingsBuilder();
    }

    public ClassifySettingsBuilder knn(final int value) {
        settings.setKnnCount(value);
        return this;
    }

    public ClassifySettingsBuilder metric(final Metric metric) {
        settings.setMetric(metric);
        return this;
    }

    public ClassifySettingsBuilder type(final String value) {
        if(Strings.isNullOrEmpty(value)){
            settings.setType(type);
        } else {
            settings.setType(value);
        }
        return this;
    }

    public ClassifySettingsBuilder distance(final StringDistanceType distanceType) {
        settings.setDistanceType(distanceType);
        return this;
    }

    public ClassifySettingsBuilder recognizerTrainType(final RecognizerTrainType trainType) {
        settings.setTrainType(trainType);
        return this;
    }
}