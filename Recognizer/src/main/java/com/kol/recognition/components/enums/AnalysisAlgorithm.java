package com.kol.recognition.components.enums;

import Jama.Matrix;
import com.google.common.collect.Multimap;
import com.kol.recognition.components.beans.ClassifySettings;
import com.kol.recognition.components.beans.Image;
import com.kol.recognition.components.recognition.*;

public enum AnalysisAlgorithm {
    PCA("eigenface") {
        @Override
        public Recognizer get(final Multimap<String, Matrix> data, final int components, final int vecLength, final Multimap<String, Image> train, final ClassifySettings settings) {
            return new PCA(data, components, vecLength, train, settings);
        }
    },
    LDA("fisherface") {
        @Override
        public Recognizer get(final Multimap<String, Matrix> data, final int components, final int vecLength, final Multimap<String, Image> train, final ClassifySettings settings) {
            return new LDA(data, components, vecLength, train, settings);
        }
    },
    NBC("bayesface") {
        @Override
        public Recognizer get(final Multimap<String, Matrix> data, final int components, final int vecLength, final Multimap<String, Image> train, final ClassifySettings settings) {
            return new NBC(data, components, vecLength, train, settings);
        }
    },
    LPP("laplacianface") {
        @Override
        public Recognizer get(final Multimap<String, Matrix> data, final int components, final int vecLength, final Multimap<String, Image> train, final ClassifySettings settings) {
            return new LPP(data, components, vecLength, train, settings);
        }
    };

    public abstract Recognizer get(final Multimap<String, Matrix> data, final int components, final int vecLength, final Multimap<String, Image> train, final ClassifySettings settings);

    AnalysisAlgorithm(String name) {
        this.name = name;
    }

    private String name;

    public String getName(){
        return name;
    }
}