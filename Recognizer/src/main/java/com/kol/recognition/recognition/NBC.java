package com.kol.recognition.recognition;

import Jama.Matrix;
import com.google.common.collect.Multimap;
import com.kol.nbc.NBLA;
import com.kol.nbc.NaiveBayesLearningAlgorithm;
import com.kol.recognition.beans.ClassifySettings;
import com.kol.recognition.beans.Image;

import java.util.ArrayList;
import java.util.List;

public final class NBC extends Recognizer {
    private NaiveBayesLearningAlgorithm<double[]> nbc;

    public NBC(Multimap<String, Matrix> data, int components, int vecLength, Multimap<String, Image> training) {
        super(data, components, vecLength, training);
    }

    @Override
    protected void init() {
        final List<Matrix> faces = new ArrayList<>(data.values());
        meanMatrix = countMean(faces);
        nbc = NBLA.doubleArray(8);
        data.entries().forEach(e -> nbc.addExample(e.getValue().minus(meanMatrix).getArray()[0], e.getKey()));
    }

    @Override
    public String classify(Matrix vector, ClassifySettings data) {
        return nbc.classifier().classify(vector.minus(getMeanMatrix()).getArray()[0]);
    }
}
