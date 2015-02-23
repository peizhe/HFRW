package com.kol.recognition.enums;

import Jama.Matrix;
import com.google.common.collect.Multimap;
import com.kol.recognition.beans.Image;
import com.kol.recognition.recognition.NBC;
import com.kol.recognition.recognition.Recognizer;
import com.kol.recognition.recognition.LDA;
import com.kol.recognition.recognition.PCA;

public enum AnalysisAlgorithm {
    PCA("eigenface") {
        @Override
        public Recognizer get(final Multimap<String, Matrix> data, final int components, final int vecLength, final Multimap<String, Image> train) {
            return new PCA(data, components, vecLength, train);
        }
    },
    LDA("fisherface") {
        @Override
        public Recognizer get(final Multimap<String, Matrix> data, final int components, final int vecLength, final Multimap<String, Image> train) {
            return new LDA(data, components, vecLength, train);
        }
    },
    NBC("bayesface") {
        @Override
        public Recognizer get(Multimap<String, Matrix> data, int components, int vecLength, Multimap<String, Image> train) {
            return new NBC(data, components, vecLength, train);
        }
    };

    public abstract Recognizer get(final Multimap<String, Matrix> data, final int components, final int vecLength, final Multimap<String, Image> train);

    AnalysisAlgorithm(String name) {
        this.name = name;
    }

    private String name;

    public String getName(){
        return name;
    }
}