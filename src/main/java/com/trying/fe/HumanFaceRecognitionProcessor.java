package com.trying.fe;

import Jama.Matrix;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.trying.fe.enums.FeatureExtractionMode;
import com.trying.fe.enums.MetricType;
import com.trying.fe.featureExtraction.FeatureExtraction;
import com.trying.fe.featureExtraction.ProjectedTrainingMatrix;
import com.trying.fe.utils.ImageUtils;
import com.trying.fe.utils.KNN;
import com.trying.fe.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class HumanFaceRecognitionProcessor {

    @Autowired private com.trying.web.components.Properties properties;
    private final Cache<FeatureExtractionMode, FeatureExtraction> cache = CacheBuilder.newBuilder().build();

    public String classifyFace(final BufferedImage image, final MetricType metricType, final FeatureExtractionMode featureExtractionMode, final int componentsRetained, final int knnCount) {
        final FeatureExtraction fe = getFeatureExtraction(featureExtractionMode, componentsRetained);
        final Matrix matrixImage = ImageUtils.convertToMatrix(image);

        final List<ProjectedTrainingMatrix> projectedTrainingSet = fe.getProjectedTrainingSet();

        final Matrix testCase = fe.getW().transpose().times(ImageUtils.toVector(matrixImage).minus(fe.getMeanMatrix()));
        return KNN.assignLabel(projectedTrainingSet.toArray(new ProjectedTrainingMatrix[projectedTrainingSet.size()]), testCase, knnCount, metricType.getMetric());
    }

    public void savePrincipalComponentImages(final FeatureExtractionMode featureExtractionMode, final int componentsRetained) throws IOException {
        final FeatureExtraction fe = getFeatureExtraction(featureExtractionMode, componentsRetained);

        ImageUtils.saveImagesToFiles(
                ImageUtils.convertMatricesToImage(fe.getW(), properties.imageHeight, properties.imageWidth),
                properties.pathToComponents.resolve(featureExtractionMode.getName()).toString(),
                properties.trainingType
        );
    }

    private FeatureExtraction trainingSystem(final FeatureExtractionMode featureExtractionMode, final int componentsRetained) {
        /** set trainSet and testSet **/
        final Map<String, List<Integer>> trainMap = new HashMap<>();
        for (int i = 1; i <= properties.faceNumber; i++) {
            trainMap.put(properties.classPrefix + i, generateTrainNumbers());
        }
        /** set featureExtraction **/
        return featureExtractionMode.getInstance(getWorkingSetWithLabels(trainMap), componentsRetained, properties.imageAsVectorLength);
    }

    private FeatureExtraction getFeatureExtraction(final FeatureExtractionMode featureExtractionMode, final int componentsRetained) {
        if(properties.useCache) {
            final FeatureExtraction ifPresent = cache.getIfPresent(featureExtractionMode);
            final FeatureExtraction fe;
            if (null == ifPresent) {
                fe = trainingSystem(featureExtractionMode, componentsRetained);
                cache.put(featureExtractionMode, fe);
            } else {
                fe = ifPresent;
            }
            return fe;
        } else {
            return trainingSystem(featureExtractionMode, componentsRetained);
        }
    }

    private List<Pair<String, Matrix>> getWorkingSetWithLabels(final Map<String, List<Integer>> map) {
        final List<Pair<String, Matrix>> trainingSet = new ArrayList<>();
        for (String label : map.keySet()) {
            trainingSet.addAll(map.get(label).stream()
                            .map(i -> new Pair<>(label, ImageUtils.toVector(
                                    ImageUtils.convertToMatrix(
                                            ImageUtils.readImage(
                                                    properties.pathToTrainingImages.resolve(properties.trainingType).resolve(label).resolve(i + "." + properties.trainingType).toString()
                                            )
                                    )
                            )))
                    .collect(Collectors.toList()));
        }
        return trainingSet;
    }

    private List<Integer> generateTrainNumbers() {
        final Random random = new Random();
        final List<Integer> result = new ArrayList<>();
        while (result.size() < properties.numOfImagesForTraining) {
            int temp = random.nextInt(properties.eachFaceNumber) + 1;
            while (result.contains(temp)) {
                temp = random.nextInt(properties.eachFaceNumber) + 1;
            }
            result.add(temp);
        }
        return result;
    }
}