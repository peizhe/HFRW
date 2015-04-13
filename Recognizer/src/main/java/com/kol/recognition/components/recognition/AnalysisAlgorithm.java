package com.kol.recognition.components.recognition;

import Jama.Matrix;
import com.google.common.collect.Multimap;
import com.kol.recognition.general.Image;
import com.kol.recognition.general.settings.ClassifySettings;

public enum AnalysisAlgorithm {
    PCA {
        @Override
        public Recognizer get(final Multimap<String, Matrix> data, final int components, final int vecLength, final Multimap<String, Image> train, final ClassifySettings settings) {
            return new PCA(data, components, vecLength, train, settings);
        }
    },
    LDA {
        @Override
        public Recognizer get(final Multimap<String, Matrix> data, final int components, final int vecLength, final Multimap<String, Image> train, final ClassifySettings settings) {
            return new LDA(data, components, vecLength, train, settings);
        }
    },
    NBC {
        @Override
        public Recognizer get(final Multimap<String, Matrix> data, final int components, final int vecLength, final Multimap<String, Image> train, final ClassifySettings settings) {
            return new NBC(data, components, vecLength, train, settings);
        }
    },
    LPP {
        @Override
        public Recognizer get(final Multimap<String, Matrix> data, final int components, final int vecLength, final Multimap<String, Image> train, final ClassifySettings settings) {
            return new LPP(data, components, vecLength, train, settings);
        }
    };

    public abstract Recognizer get(final Multimap<String, Matrix> data, final int components, final int vecLength, final Multimap<String, Image> train, final ClassifySettings settings);
}