package com.kol.recognition.utils;

import com.kol.recognition.beans.ClassifySettings;
import com.kol.recognition.enums.AnalysisAlgorithm;
import com.kol.recognition.enums.ComponentProperty;
import com.kol.recognition.enums.MetricType;
import com.kol.recognition.interfaces.Metric;

public final class ClassifySettingsBuilder {

    private int knn;
    private int pca;
    private int images;
    private ClassifySettings settings;

    private ClassifySettingsBuilder(){
        this.settings = new ClassifySettings();
    }

    private ClassifySettingsBuilder(int knn, int pca, int images) {
        this();
        this.knn = knn;
        this.pca = pca;
        this.images = images;
    }

    public static ClassifySettingsBuilder start(final int knn, final int pca, final int images) {
        return new ClassifySettingsBuilder(knn, pca, images);
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
        settings.setMetric(value.get());
        return this;
    }

    public ClassifySettingsBuilder algorithm(final AnalysisAlgorithm algorithm) {
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
}