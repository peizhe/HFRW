package com.trying.fe.featureExtraction;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import com.trying.fe.utils.Pair;

import java.util.List;
import java.util.stream.Collectors;

public final class PCA extends FeatureExtraction {

    public PCA(List<Pair<String, Matrix>> data, int numberOfComponents, int imageAsVectorLength) {
        super(data, numberOfComponents, imageAsVectorLength);
    }

    @Override
    protected void init() {
        final List<Matrix> faces = data.stream().map(Pair::getSecond).collect(Collectors.toList());
        this.meanMatrix = countMean(faces);
        this.w = getFeature(faces, numberOfComponents);
    }

    private Matrix getFeature(final List<Matrix> input, final int k) {
        final int row = input.get(0).getRowDimension();
        final int column = input.size();
        final Matrix x = new Matrix(row, column);

        for (int i = 0; i < column; i++) {
            x.setMatrix(0, row - 1, i, i, input.get(i).minus(meanMatrix));
        }

        // get eigenvalues and eigenvectors
        final EigenvalueDecomposition feature = x.transpose().times(x).eig();
        final double[] d = feature.getRealEigenvalues();

        assert d.length >= k : "number of eigenvalues is less than k";

        final int[] indexes = this.getIndexesOfKEigenvalues(d, k);

        final Matrix eigenVectors = x.times(feature.getV());
        final Matrix selectedEigenVectors = eigenVectors.getMatrix(0, eigenVectors.getRowDimension() - 1, indexes);

        // normalize the eigenvectors
        final int normRow = selectedEigenVectors.getRowDimension();
        final int normColumn = selectedEigenVectors.getColumnDimension();
        for (int i = 0; i < normColumn; i++) {
            double temp = 0;
            for (int j = 0; j < normRow; j++) {
                temp += Math.pow(selectedEigenVectors.get(j, i), 2);
            }
            temp = Math.sqrt(temp);

            for (int j = 0; j < normRow; j++) {
                selectedEigenVectors.set(j, i, selectedEigenVectors.get(j, i) / temp);
            }
        }
        return selectedEigenVectors;
    }
}