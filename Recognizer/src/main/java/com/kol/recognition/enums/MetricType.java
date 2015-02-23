package com.kol.recognition.enums;

import com.kol.recognition.metric.CosineDissimilarity;
import com.kol.recognition.metric.EuclideanDistance;
import com.kol.recognition.metric.L1Distance;
import com.kol.recognition.interfaces.Metric;

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