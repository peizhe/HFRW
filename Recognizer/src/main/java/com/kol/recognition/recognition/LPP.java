package com.kol.recognition.recognition;


import java.util.List;
import java.util.stream.Collectors;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import com.google.common.collect.Multimap;
import com.kol.recognition.beans.ClassifySettings;
import com.kol.recognition.beans.Image;
import com.kol.recognition.beans.ProjectedTrainingMatrix;
import com.kol.recognition.utils.KNN;


public class LPP extends Recognizer {

    public LPP(Multimap<String, Matrix> data, int components, int vecLength, Multimap<String, Image> training, ClassifySettings settings) {
        super(data, components, vecLength, training, settings);
    }

    private Matrix constructNearestNeighborGraph(final List<ProjectedTrainingMatrix> input) {
        final int size = input.size();
        final Matrix nng = new Matrix(size, size);

        final ProjectedTrainingMatrix[] trainArray = input.toArray(new ProjectedTrainingMatrix[input.size()]);
        for (int i = 0; i < size; i++) {
            final ProjectedTrainingMatrix[] neighbors = KNN.findKNN(trainArray, input.get(i).getMatrix(), settings.getKnnCount(), settings.getMetric());
            for (ProjectedTrainingMatrix neighbor : neighbors) {
                if (!neighbor.equals(input.get(i))) {
//                    final double distance = metric.getDistance(neighbors[j].getMatrix(), input.get(i).getMatrix());
//                    double weight = Math.exp(0-distance*distance / 2);
                    final int index = input.indexOf(neighbor);
                    nng.set(i, index, 1);
                    nng.set(index, i, 1);
                }
            }
//            for(int j = 0; j < size; j ++){
//                if( i != j && input.get(i).label.equals(input.get(j).label)){
//                    nng.set(i, j, 1);
//                }
//           }
        }
        return nng;
    }

    private Matrix constructD(final Matrix sMatrix) {
        final int size = sMatrix.getRowDimension();
        final Matrix dMatrix = new Matrix(size, size);

        for (int i = 0; i < size; i++) {
            double temp = 0;
            for (int j = 0; j < size; j++) {
                temp += sMatrix.get(j, i);
            }
            dMatrix.set(i, i, temp);
        }
        return dMatrix;
    }

    private Matrix constructTrainingMatrix(final List<ProjectedTrainingMatrix> input) {
        final int row = input.get(0).getMatrix().getRowDimension();
        final int column = input.size();
        final Matrix matrix = new Matrix(row, column);
        for (int i = 0; i < column; i++) {
            matrix.setMatrix(0, row - 1, i, i, input.get(i).getMatrix());
        }
        return matrix;
    }

    @Override
    protected void init() {
        final int classSize = data.keySet().size();

        // process in PCA
        final PCA pca = new PCA(data, numberOfComponents, imageAsVectorLength, training, settings);

        //construct the nearest neighbor graph
        final Matrix nng = constructNearestNeighborGraph(pca.getProjectedTrainingSet());
        final Matrix dMatrix = constructD(nng);
        final Matrix lMatrix = dMatrix.minus(nng);

        //reconstruct the trainingSet into required xMatrix;
        final Matrix xMatrix = constructTrainingMatrix(pca.getProjectedTrainingSet());
        final Matrix xlxT = xMatrix.times(lMatrix).times(xMatrix.transpose());
        final Matrix xdxT = xMatrix.times(dMatrix).times(xMatrix.transpose());

        //calculate the eignevalues and eigenvectors of (xdxT)^-1 * (xlxT)
        final EigenvalueDecomposition feature = xdxT.inverse().times(xlxT).eig();

        final double[] d = feature.getRealEigenvalues();
        assert d.length >= classSize - 1 : "Ensure that the number of eigenvalues is larger than classSize - 1";
        final int[] indexes = getIndexesOfKEigenvalues(d, d.length);

        final Matrix eigenVectors = feature.getV();
        final Matrix selectedEigenVectors = eigenVectors.getMatrix(0, eigenVectors.getRowDimension() - 1, indexes);

        w = pca.getW().times(selectedEigenVectors);
        //Construct projectedTrainingMatrix
        meanMatrix = pca.getMeanMatrix();

        projectedTrainingSet = data.entries().stream()
                .map(dv -> new ProjectedTrainingMatrix(dv.getKey(), w.transpose().times(dv.getValue().minus(meanMatrix))))
                .collect(Collectors.toList());
    }

    @Override
    public String classify(final Matrix vector, final ClassifySettings data) {
        final Matrix testCase = getW().transpose().times(vector.minus(getMeanMatrix()));
        return KNN.assignLabel(projectedTrainingSet.toArray(new ProjectedTrainingMatrix[projectedTrainingSet.size()]), testCase, data.getKnnCount(), data.getMetric());
    }
}