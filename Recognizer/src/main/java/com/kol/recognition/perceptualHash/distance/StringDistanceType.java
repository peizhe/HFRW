package com.kol.recognition.perceptualHash.distance;

import com.kol.recognition.general.EnumType;

public enum StringDistanceType implements EnumType<StringDistance> {
    HAMMING {
        @Override
        public StringDistance get() {
            return new HammingDistance();
        }
    },
    JARO_WINKLER {
        @Override
        public StringDistance get() {
            return new JaroWinklerDistance();
        }
    },
    LEVENSTEIN {
        @Override
        public StringDistance get() {
            return new LevensteinDistance();
        }
    }
}