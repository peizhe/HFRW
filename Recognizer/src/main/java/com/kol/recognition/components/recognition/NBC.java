package com.kol.recognition.components.recognition;

import Jama.Matrix;
import com.google.common.collect.Multimap;
import com.kol.nbc.NBLA;
import com.kol.nbc.NaiveBayesLearningAlgorithm;
import com.kol.recognition.general.settings.ClassifySettings;
import com.kol.recognition.general.Image;

import java.util.ArrayList;
import java.util.List;

public final class NBC extends Recognizer {
    private NaiveBayesLearningAlgorithm<double[]> nbc;

    public NBC(Multimap<String, Matrix> data, int components, int vecLength, Multimap<String, Image> training, ClassifySettings settings) {
        super(data, components, vecLength, training, settings);
    }

    /*@Override
    protected void init() {
        final List<Matrix> faces = new ArrayList<>(data.values());
        meanMatrix = countMean(faces);
        nbc = NBLA.doubleArray(8);

        final PCA pca = new PCA(data, numberOfComponents, imageAsVectorLength, training);
        final Multimap<String, Matrix> pcaSpace = ArrayListMultimap.create();
        data.entries().forEach(d -> pcaSpace.put(d.getKey(), pca.getW().transpose().times(d.getValue().minus(meanMatrix)).transpose()));
        this.w = pca.w;

        pcaSpace.entries().forEach(e -> nbc.addExample(e.getValue().getArray()[0], e.getKey()));
    }

    @Override
    public String classify(Matrix vector, ClassifySettings data) {
        return nbc.classifier().classify(w.transpose().times(vector.minus(meanMatrix)).transpose().getArray()[0]);
    }*/

    @Override
    protected void init() {
        final List<Matrix> faces = new ArrayList<>(data.values());
        meanMatrix = countMean(faces);
        nbc = NBLA.doubleArray(8);
        data.entries().forEach(e -> nbc.addExample(e.getValue().minus(meanMatrix).getArray()[0], e.getKey()));
    }

    @Override
    public String classify(Matrix vector, ClassifySettings data) {
        return nbc.classifier().classify(vector.minus(meanMatrix).getArray()[0]);
    }
}
