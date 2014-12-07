package com.trying.fe.featureExtraction;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import com.trying.fe.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class LDA extends FeatureExtraction {

    public LDA(List<Pair<String, Matrix>> data, int numberOfComponents, int imageAsVectorLength) {
        super(data, numberOfComponents, imageAsVectorLength);
    }

    @Override
    protected void init() {
        final int sampleSize = data.size();
        final int classSize = data.stream().map(Pair::getFirst).collect(Collectors.toSet()).size();

        assert numberOfComponents >= sampleSize - classSize : "the input components is smaller than sampleSize - classSize!";
        assert sampleSize >= 2 * classSize : "sampleSize is smaller than 2c!";

        // process in PCA
        final PCA pca = new PCA(data, sampleSize - classSize, imageAsVectorLength);

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
    }
}