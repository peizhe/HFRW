package com.kol.recognition.components.enums;

import com.kol.recognition.components.metric.CosineDissimilarity;
import com.kol.recognition.components.metric.EuclideanDistance;
import com.kol.recognition.components.metric.L1Distance;
import com.kol.recognition.components.interfaces.Metric;

public enum MetricType {
    COSINE {
        @Override
        public Metric get() {
            return new CosineDissimilarity();
        }
    },
    L1D {
        @Override
        public Metric get() {
            return new L1Distance();
        }
    },
    EUCLIDEAN {
        @Override
        public Metric get() {
            return new EuclideanDistance();
        }
    };

    public abstract Metric get();
}