package com.trying.fe;

import Jama.Matrix;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.trying.fe.enums.FeatureExtractionMode;
import com.trying.fe.featureExtraction.FeatureExtraction;
import com.trying.fe.featureExtraction.ProjectedTrainingMatrix;
import com.trying.fe.utils.ImageUtils;
import com.trying.fe.utils.KNN;
import com.trying.fe.utils.Pair;
import com.trying.web.utils.Utils;
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
    private final Map<String, List<Integer>> trainMap = new HashMap<>();

    public String classifyFace(final BufferedImage image, final ClassifySettings settings) {
        final FeatureExtraction fe = getFeatureExtraction(settings);
        final Matrix matrixImage = ImageUtils.convertToMatrix(image);

        final List<ProjectedTrainingMatrix> projectedTrainingSet = fe.getProjectedTrainingSet();

        final Matrix testCase = fe.getW().transpose().times(ImageUtils.toVector(matrixImage).minus(fe.getMeanMatrix()));
        return KNN.assignLabel(projectedTrainingSet.toArray(new ProjectedTrainingMatrix[projectedTrainingSet.size()]), testCase, settings.getKnnCount(), settings.getMetric().getMetric());
    }

    public List<String> savePrincipalComponentImages(final ClassifySettings settings) throws IOException {
        final FeatureExtraction fe = getFeatureExtraction(settings);

        return ImageUtils.saveImagesToFiles(
                ImageUtils.convertMatricesToImage(fe.getW(), properties.imageHeight, properties.imageWidth),
                properties.pathToResources.resolve(properties.components).resolve(settings.getFeMode().getName()).toString(),
                properties.trainingType
        );
    }

    private FeatureExtraction trainingSystem(final ClassifySettings settings) {
        /** set trainSet and testSet **/
        trainMap.clear();
        for (int i = 1; i <= properties.faceNumber; i++) {
            trainMap.put(properties.classPrefix + Utils.leadingZeros(i, properties.classLength), settings.getTraining().generateTrainNumbers(settings.getNumberOfImages(), properties.eachFaceNumber));
        }
        /** set featureExtraction **/
        return settings.getFeMode().getInstance(getWorkingSetWithLabels(trainMap), settings.getComponents(), properties.imageAsVectorLength);
    }

    private FeatureExtraction getFeatureExtraction(final ClassifySettings settings) {
        if(properties.useCache) {
            final FeatureExtraction ifPresent = cache.getIfPresent(settings.getFeMode());
            final FeatureExtraction fe;
            if (null == ifPresent) {
                fe = trainingSystem(settings);
                cache.put(settings.getFeMode(), fe);
            } else {
                fe = ifPresent;
            }
            return fe;
        } else {
            return trainingSystem(settings);
        }
    }

    private List<Pair<String, Matrix>> getWorkingSetWithLabels(final Map<String, List<Integer>> map) {
        final List<Pair<String, Matrix>> trainingSet = new ArrayList<>();
        for (String label : map.keySet()) {
            trainingSet.addAll(map.get(label).stream()
                            .map(i -> new Pair<>(label, ImageUtils.toVector(
                                    ImageUtils.convertToMatrix(
                                            ImageUtils.readImage(
                                                    properties.pathToResources.resolve(properties.trainingImages).resolve(properties.trainingType).resolve(label).resolve(i + "." + properties.trainingType).toString()
                                            )
                                    )
                            )))
                    .collect(Collectors.toList()));
        }
        return trainingSet;
    }

    public Map<String, List<Integer>> getTrainMap() {
        return trainMap;
    }
}