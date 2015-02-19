package com.kol.recognition.enums;

import com.kol.recognition.metric.CosineDissimilarity;
import com.kol.recognition.metric.EuclideanDistance;
import com.kol.recognition.metric.L1Distance;
import com.kol.recognition.metric.Metric;

public enum MetricType {
    COSINE {
        @Override
        public Metric getMetric() {
            return new CosineDissimilarity();
        }
    },
    L1D {
        @Override
        public Metric getMetric() {
            return new L1Distance();
        }
    },
    EUCLIDEAN {
        @Override
        public Metric getMetric() {
            return new EuclideanDistance();
        }
    };

    public abstract Metric getMetric();
}