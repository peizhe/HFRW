package com.kol.recognition.enums;

import Jama.Matrix;
import com.kol.recognition.utils.Pair;
import com.kol.recognition.featureExtraction.FeatureExtraction;
import com.kol.recognition.featureExtraction.LDA;
import com.kol.recognition.featureExtraction.PCA;

import java.util.List;
import java.util.Map;

public enum FeatureExtractionMode {
    PCA("eigenface") {
        @Override
        public FeatureExtraction getInstance(final List<Pair<String, Matrix>> data, final int numberOfComponents, final int imageAsVectorLength, final Map<String, List<Integer>> trainingMap) {
            return new PCA(data, numberOfComponents, imageAsVectorLength, trainingMap);
        }
    },
    LDA("fisherface") {
        @Override
        public FeatureExtraction getInstance(final List<Pair<String, Matrix>> data, final int numberOfComponents, final int imageAsVectorLength, final Map<String, List<Integer>> trainingMap) {
            return new LDA(data, numberOfComponents, imageAsVectorLength, trainingMap);
        }
    };

    public abstract FeatureExtraction getInstance(final List<Pair<String, Matrix>> data, final int numberOfComponents, final int imageAsVectorLength, final Map<String, List<Integer>> trainingMap);

    FeatureExtractionMode(String name) {
        this.name = name;
    }

    private String name;

    public String getName(){
        return name;
    }
}