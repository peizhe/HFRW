package com.trying.fe.enums;

import Jama.Matrix;
import com.trying.fe.utils.Pair;
import com.trying.fe.featureExtraction.FeatureExtraction;
import com.trying.fe.featureExtraction.LDA;
import com.trying.fe.featureExtraction.PCA;

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