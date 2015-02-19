package com.kol.recognition.utils;

import Jama.Matrix;
import com.kol.recognition.beans.ProjectedTrainingMatrix;
import com.kol.recognition.metric.Metric;

import java.util.HashMap;
import java.util.Map;

public final class KNN {

    public static String assignLabel(final ProjectedTrainingMatrix[] trainingSet, final Matrix testFace, final int k, final Metric metric) {
        return classify(findKNN(trainingSet, testFace, k, metric));
    }

    /**
     * testFace has been projected to the subspace
     */
    private static ProjectedTrainingMatrix[] findKNN(final ProjectedTrainingMatrix[] trainingSet, final Matrix testFace, final int k, final Metric metric) {
        final int NumOfTrainingSet = trainingSet.length;
        assert k <= NumOfTrainingSet : "k is lager than the length of trainingSet!";

        // initialization
        final ProjectedTrainingMatrix[] neighbors = new ProjectedTrainingMatrix[k];
        for (int i = 0; i < k; i++) {
            trainingSet[i].setDistance(metric.getDistance(trainingSet[i].getMatrix(), testFace));
//            System.out.println("index: " + i + " distance: " + trainingSet[i].getDistance());
            neighbors[i] = trainingSet[i];
        }

        // go through the remaining records in the trainingSet to find k nearest neighbors
        for (int i = k; i < NumOfTrainingSet; i++) {
            trainingSet[i].setDistance(metric.getDistance(trainingSet[i].getMatrix(), testFace));
//            System.out.println("index: " + i + " distance: " + trainingSet[i].getDistance());
            int maxIndex = 0;
            for (int j = 0; j < k; j++) {
                if (neighbors[j].getDistance() > neighbors[maxIndex].getDistance()) {
                    maxIndex = j;
                }
            }
            if (neighbors[maxIndex].getDistance() > trainingSet[i].getDistance()) {
                neighbors[maxIndex] = trainingSet[i];
            }
        }
        return neighbors;
    }

    /**
     * get the class label by using neighbors
     */
    private static String classify(final ProjectedTrainingMatrix[] neighbors) {
        final Map<String, Double> map = new HashMap<>();

        for (final ProjectedTrainingMatrix temp : neighbors) {
            final String key = temp.getLabel();
            if (!map.containsKey(key)) {
                map.put(key, 1.0 / temp.getDistance());
            } else {
                double value = map.get(key);
                value += 1.0 / temp.getDistance();
                map.put(key, value);
            }
        }

        // Find the most likely label
        double maxSimilarity = Double.MIN_VALUE;
        String returnLabel = null;
        for (String key : map.keySet()) {
            final double value = map.get(key);
            if (value > maxSimilarity) {
                maxSimilarity = value;
                returnLabel = key;
            }
        }
        return returnLabel;
    }
}