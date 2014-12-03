package com.trying.fe.enums;

import com.trying.fe.metric.CosineDissimilarity;
import com.trying.fe.metric.EuclideanDistance;
import com.trying.fe.metric.L1Distance;
import com.trying.fe.metric.Metric;

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