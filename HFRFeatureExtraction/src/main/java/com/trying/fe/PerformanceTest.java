package com.trying.fe;

import Jama.Matrix;
import com.google.common.collect.Lists;
import com.trying.fe.enums.FeatureExtractionMode;
import com.trying.fe.enums.MetricType;
import com.trying.fe.featureExtraction.FeatureExtraction;
import com.trying.fe.featureExtraction.ProjectedTrainingMatrix;
import com.trying.fe.metric.Metric;
import com.trying.fe.utils.ImageUtils;
import com.trying.fe.utils.KNN;
import com.trying.fe.utils.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PerformanceTest {

    public static final int FACE_NUMBER = 40;
    public static final int EACH_FACE_NUMBER = 10;
    public static final int FACE_IMAGE_WIDTH = 92;
    public static final int FACE_IMAGE_HEIGHT = 112;
    public static final int IMAGE_AS_VECTOR_LENGTH = 10304;

    public static void main(String args[]) {
        //Notice that the second parameter which is a measurement of energy percentage does not apply to LDA
//        test(MetricType.EUCLIDEAN, FeatureExtractionMode.PCA, 101, 3, 2);
        test(MetricType.EUCLIDEAN, FeatureExtractionMode.PCA, 101, 3, 2);
    }

    @SuppressWarnings("unused")
    private static void convertAllPGMFacesToBMP() throws IOException {
        for (int i = 1; i <= FACE_NUMBER; i++) {
            Files.createDirectory(Paths.get("faces/bmp/s" + i));
        }
        for (int i = 1; i <= FACE_NUMBER; i++) {
            final String label = "s" + i;
            for (int j = 1; j <= EACH_FACE_NUMBER; j++) {
                ImageUtils.convertPGMTo("faces/pgm/" + label + "/", "faces/bmp/" + label + "/", String.valueOf(j), "bmp");
            }
        }
    }

    /**
     * @param metricType:
     * 0: CosineDissimilarity
     * 1: L1Distance
     * 2: EuclideanDistance
     *
     * energyPercentage:
     * PCA: components = samples * energyPercentage
     * LDA: components = (c-1) * energyPercentage
     *
     * @param featureExtractionMode
     * 0: PCA
     * 1: LDA
     *
     * @param trainNumbers: how many numbers in 1..10 are assigned to be training faces
     * for each class, randomly generate the set
     *
     * @param knnCount: number of K for KNN algorithm
     */
    private static double test(final MetricType metricType, final FeatureExtractionMode featureExtractionMode, final int componentsRetained, final int trainNumbers, final int knnCount) {
        //determine which metric is used metric
        assert metricType != null : "metricType is wrong!";
        final Metric metric = metricType.getMetric();

        //set trainSet and testSet
        final Map<String, List<Integer>> trainMap = new HashMap<>();
        final Map<String, List<Integer>> testMap = new HashMap<>();
        for (int i = 1; i <= FACE_NUMBER; i++) {
            final String label = "s" + i;
            final List<Integer> train = generateTrainNumbers(trainNumbers);
            trainMap.put(label, train);
            testMap.put(label, generateTestNumbers(train));
        }

        //trainingSet & respective labels
        final List<Pair<String, Matrix>> trainingSet = getWorkingSetWithLabels(trainMap);

        //testingSet & respective true labels
        final List<Pair<String, Matrix>> testingSet = getWorkingSetWithLabels(testMap);

        //set featureExtraction
        try {
            final FeatureExtraction fe = featureExtractionMode.getInstance(trainingSet, componentsRetained, IMAGE_AS_VECTOR_LENGTH);

            ImageUtils.saveImagesToFiles(ImageUtils.convertMatricesToImage(fe.getW(), FACE_IMAGE_HEIGHT, FACE_IMAGE_WIDTH), featureExtractionMode.getName(), "bmp");

            //use test cases to validate testingSet trueLabels
            final List<ProjectedTrainingMatrix> projectedTrainingSet = fe.getProjectedTrainingSet();
            int accurateNum = 0;
            for (Pair<String, Matrix> aTestingSet : testingSet) {
                final Matrix testCase = fe.getW().transpose().times(aTestingSet.getSecond().minus(fe.getMeanMatrix()));
                final String result = KNN.assignLabel(projectedTrainingSet.toArray(new ProjectedTrainingMatrix[projectedTrainingSet.size()]), testCase, knnCount, metric);
                if (result.equals(aTestingSet.getFirst())) {
                    accurateNum++;
                }
            }
            final double accuracy = accurateNum / (double) testingSet.size();
            System.out.println("The accuracy is " + accuracy);
            return accuracy;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static List<Pair<String, Matrix>> getWorkingSetWithLabels(final Map<String, List<Integer>> map) {
        final List<Pair<String, Matrix>> trainingSet = new ArrayList<>();
        for (String label : map.keySet()) {
            trainingSet.addAll(map.get(label).stream()
                            .map(i -> new Pair<>(label, toVector(ImageUtils.convertToMatrix(ImageUtils.readImage("faces/bmp/" + label + "/" + i + ".bmp")))))
                .collect(Collectors.toList())
            );
        }
        return trainingSet;
    }

    private static List<Integer> generateTrainNumbers(final int trainNum) {
        /*final Random random = new Random();
        final List<Integer> result = new ArrayList<>();
        while (result.size() < trainNum) {
            int temp = random.nextInt(EACH_FACE_NUMBER) + 1;
            while (result.contains(temp)) {
                temp = random.nextInt(EACH_FACE_NUMBER) + 1;
            }
            result.add(temp);
        }*/
        return Lists.newArrayList(1,2,3);
    }

    private static List<Integer> generateTestNumbers(final List<Integer> trainSet) {
        final List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= EACH_FACE_NUMBER; i++) {
            if (!trainSet.contains(i)) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Convert a m by n matrix into a m*n by 1 matrix
     */
    private static Matrix toVector(final Matrix input) {
        final int m = input.getRowDimension();
        final int n = input.getColumnDimension();

        final Matrix result = new Matrix(m * n, 1);
        for (int p = 0; p < n; p++) {
            for (int q = 0; q < m; q++) {
                result.set(p * m + q, 0, input.get(q, p));
            }
        }
        return result;
    }
}