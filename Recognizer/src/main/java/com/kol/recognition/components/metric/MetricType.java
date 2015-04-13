package com.kol.recognition.components.metric;

import com.kol.recognition.general.EnumType;

public enum MetricType implements EnumType<Metric> {
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
    }
}