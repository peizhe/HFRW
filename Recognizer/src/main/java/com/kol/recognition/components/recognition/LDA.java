package com.kol.recognition.components.recognition;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import com.google.common.collect.Multimap;
import com.kol.recognition.components.beans.ClassifySettings;
import com.kol.recognition.components.beans.Image;
import com.kol.recognition.components.beans.ProjectedTrainingMatrix;
import com.kol.recognition.utils.KNN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class LDA extends Recognizer {

    public LDA(Multimap<String, Matrix> data, int components, int vecLength, Multimap<String, Image> training, ClassifySettings settings) {
        super(data, components, vecLength, training, settings);
    }

    @Override
    protected void init() {
        final int sampleSize = data.size();
        final int classSize = data.keySet().size();

        assert numberOfComponents >= sampleSize - classSize : "the input components is smaller than sampleSize - classSize!";
        assert sampleSize >= 2 * classSize : "sampleSize is smaller than 2c!";

        // process in PCA
        final PCA pca = new PCA(data, sampleSize - classSize, imageAsVectorLength, training, settings);

        // classify
        final Matrix meanTotal = new Matrix(sampleSize - classSize, 1);

        final Map<String, List<Matrix>> map = new HashMap<>();
        for (ProjectedTrainingMatrix pcaTrain : pca.getProjectedTrainingSet()) {
            final String key = pcaTrain.getLabel();
            meanTotal.plusEquals(pcaTrain.getMatrix());
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            final List<Matrix> temp = map.get(key);
            temp.add(pcaTrain.getMatrix());
            map.put(key, temp);
        }
        meanTotal.times(1.0 / sampleSize);

        // calculate sw, sb
        final Matrix sw = new Matrix(sampleSize - classSize, sampleSize - classSize);
        final Matrix sb = new Matrix(sampleSize - classSize, sampleSize - classSize);

        for (String key : map.keySet()) {
            final List<Matrix> matrixWithinThatClass = map.get(key);
            final Matrix meanOfCurrentClass = countMean(matrixWithinThatClass);
            for (Matrix matrix : matrixWithinThatClass) {
                final Matrix tmp = matrix.minus(meanOfCurrentClass);
                sw.plusEquals(tmp.times(tmp.transpose()));
            }
            final Matrix temp = meanOfCurrentClass.minus(meanTotal);
            sb.plusEquals(temp.times(temp.transpose()).times(matrixWithinThatClass.size()));
        }

        // calculate the eigenValues and vectors of sw^-1 * sb
        final Matrix targetForEigen = sw.inverse().times(sb);
        final EigenvalueDecomposition feature = targetForEigen.eig();

        final double[] d = feature.getRealEigenvalues();

        assert d.length >= classSize - 1 : "Ensure that the number of eigenValues is larger than classSize - 1";

        final int[] indexes = getIndexesOfKEigenvalues(d, classSize - 1);

        final Matrix eigenVectors = feature.getV();
        final Matrix selectedEigenVectors = eigenVectors.getMatrix(0, eigenVectors.getRowDimension() - 1, indexes);

        meanMatrix = pca.getMeanMatrix();
        w = pca.getW().times(selectedEigenVectors);

        this.projectedTrainingSet = data.entries().stream()
                .map(dv -> new ProjectedTrainingMatrix(dv.getKey(), w.transpose().times(dv.getValue().minus(meanMatrix))))
                .collect(Collectors.toList());
    }

    @Override
    public String classify(final Matrix vector, final ClassifySettings data) {
        final Matrix testCase = getW().transpose().times(vector.minus(getMeanMatrix()));
        return KNN.assignLabel(projectedTrainingSet.toArray(new ProjectedTrainingMatrix[projectedTrainingSet.size()]), testCase, data.getKnnCount(), data.getMetric());
    }
}