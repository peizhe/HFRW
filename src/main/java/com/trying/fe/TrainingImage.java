package com.trying.fe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum TrainingImage {
    ALL {
        @Override
        public List<Integer> generateTrainNumbers(final int numOfClassItems, final int numOfAllClassItems) {
            final List<Integer> result = new ArrayList<>();
            for (int i = 0; i < numOfAllClassItems; i++) {
                result.add(i);
            }
            return result;
        }
    },
    FIRST {
        @Override
        public List<Integer> generateTrainNumbers(final int numOfClassItems, final int numOfAllClassItems) {
            final List<Integer> result = new ArrayList<>();
            for (int i = 0; i < numOfClassItems; i++) {
                result.add(i);
            }
            return result;
        }
    },
    RANDOM {
        @Override
        public List<Integer> generateTrainNumbers(final int numOfClassItems, final int numOfAllClassItems) {
            final Random random = new Random();
            final List<Integer> result = new ArrayList<>();
            while (result.size() < numOfClassItems) {
                int temp = random.nextInt(numOfAllClassItems) + 1;
                while (result.contains(temp)) {
                    temp = random.nextInt(numOfAllClassItems) + 1;
                }
                result.add(temp);
            }
            return result;
        }
    };

    public abstract List<Integer> generateTrainNumbers(final int numOfClassItems, final int numOfAllClassItems);
}
