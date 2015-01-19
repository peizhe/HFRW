package com.kol.recognition.fe;

import Jama.Matrix;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kol.recognition.enums.FeatureExtractionMode;
import com.kol.recognition.featureExtraction.FeatureExtraction;
import com.kol.recognition.featureExtraction.ProjectedTrainingMatrix;
import com.kol.recognition.utils.ImageUtils;
import com.kol.recognition.utils.KNN;
import com.kol.recognition.utils.Pair;
import com.kol.recognition.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class HumanFaceRecognitionProcessor {

    @Autowired private com.kol.recognition.components.Properties prop;
    private final Cache<FeatureExtractionMode, FeatureExtraction> cache = CacheBuilder.newBuilder().build();

    public String classifyFace(final FeatureExtraction classifier, final BufferedImage image, final ClassifySettings settings) {
        final Matrix matrixImage = ImageUtils.convertToMatrix(image);

        final List<ProjectedTrainingMatrix> projectedTrainingSet = classifier.getProjectedTrainingSet();

        final Matrix testCase = classifier.getW().transpose().times(ImageUtils.toVector(matrixImage).minus(classifier.getMeanMatrix()));
        return KNN.assignLabel(projectedTrainingSet.toArray(new ProjectedTrainingMatrix[projectedTrainingSet.size()]), testCase, settings.getKnnCount(), settings.getMetric().getMetric());
    }

    public List<String> savePrincipalComponentImages(final FeatureExtraction classifier, final ClassifySettings settings) throws IOException {
        return ImageUtils.saveImagesToFiles(
                ImageUtils.convertMatricesToImage(classifier.getW(), prop.imageHeight, prop.imageWidth),
                prop.resources.resolve(prop.components).resolve(settings.getFeMode().getName()).toString(),
                prop.trainingType
        );
    }

    private FeatureExtraction trainingSystem(final ClassifySettings settings) {
        /** set trainSet and testSet **/
        final Map<String, List<Integer>> trainMap = new HashMap<>();
        for (int i = 1; i <= prop.faceNumber; i++) {
            trainMap.put(prop.classPrefix + Utils.leadingZeros(i, prop.classLength), settings.getTraining().generateTrainNumbers(settings.getNumberOfImages(), prop.eachFaceNumber));
        }
        /** set featureExtraction **/
        return settings.getFeMode().getInstance(getWorkingSetWithLabels(trainMap), settings.getComponents(), prop.imageAsVectorLength, trainMap);
    }

    public FeatureExtraction getFeatureExtraction(final ClassifySettings settings) {
        if(prop.useCache) {
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
                                                    prop.resources.resolve(prop.trainingImages).resolve(prop.trainingType).resolve(label).resolve(i + "." + prop.trainingType).toString()
                                            )
                                    )
                            )))
                    .collect(Collectors.toList()));
        }
        return trainingSet;
    }
}