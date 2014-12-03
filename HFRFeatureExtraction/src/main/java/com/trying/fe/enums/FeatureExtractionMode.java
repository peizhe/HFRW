package com.trying.fe.enums;

import Jama.Matrix;
import com.trying.fe.utils.Pair;
import com.trying.fe.featureExtraction.FeatureExtraction;
import com.trying.fe.featureExtraction.LDA;
import com.trying.fe.featureExtraction.PCA;

import java.util.List;

public enum FeatureExtractionMode {
    PCA("eigenface") {
        @Override
        public FeatureExtraction getInstance(final List<Pair<String, Matrix>> data, final int numberOfComponents, final int imageAsVectorLength) {
            return new PCA(data, numberOfComponents, imageAsVectorLength);
        }
    },
    LDA("fisherface") {
        @Override
        public FeatureExtraction getInstance(final List<Pair<String, Matrix>> data, final int numberOfComponents, final int imageAsVectorLength) {
            return new LDA(data, numberOfComponents, imageAsVectorLength);
        }
    };

    public abstract FeatureExtraction getInstance(final List<Pair<String, Matrix>> data, final int numberOfComponents, final int imageAsVectorLength);

    FeatureExtractionMode(String name) {
        this.name = name;
    }

    private String name;

    public String getName(){
        return name;
    }
}