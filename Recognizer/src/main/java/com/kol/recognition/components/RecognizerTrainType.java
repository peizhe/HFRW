package com.kol.recognition.components;

import java.util.Collection;
import java.util.stream.Collectors;

public enum RecognizerTrainType {
    ALL {
        @Override
        public <T> Collection<T> getTrainObjectIds(final int requiredCount, final Collection<T> list) {
            return list;
        }
    },
    FIRST {
        @Override
        public <T> Collection<T> getTrainObjectIds(final int requiredCount, final Collection<T> list) {
            return list.stream().sorted().limit(requiredCount).collect(Collectors.toList());
        }
    },
    RANDOM {
        @Override
        public <T> Collection<T> getTrainObjectIds(final int requiredCount, final Collection<T> list) {
            return list.stream().unordered().limit(requiredCount).collect(Collectors.toList());
        }
    };

    public abstract <T> Collection<T> getTrainObjectIds(final int requiredCount, final Collection<T> list);
}
