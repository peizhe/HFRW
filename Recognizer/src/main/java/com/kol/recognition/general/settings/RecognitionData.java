package com.kol.recognition.general.settings;

import com.kol.recognition.components.metric.Metric;

public final class RecognitionData {

    private int knnCount;
    private Metric metric;

    public RecognitionData() {}

    public int getKnnCount() {
        return knnCount;
    }

    public void setKnnCount(int knnCount) {
        this.knnCount = knnCount;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }
}
