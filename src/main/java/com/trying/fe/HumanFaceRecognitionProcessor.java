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
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class HumanFaceRecognitionProcessor {

    @Resource private Environment environment;

    private Integer faceNumber;
    private Integer imageWidth;
    private Integer imageHeight;
    private Integer eachFaceNumber;
    private Integer imageAsVectorLength;
    private Integer numOfImagesForTraining;

    private String type;
    private String classPrefix;
    private Path pathToTraining;
    private Path pathToPrincipalComponentImages;

    private final Cache<FeatureExtractionMode, FeatureExtraction> cache = CacheBuilder.newBuilder().build();

    @PostConstruct
    private void init() {
        faceNumber = environment.getRequiredProperty("face.number", Integer.class);
        imageWidth = environment.getRequiredProperty("face.image.width", Integer.class);
        imageHeight = environment.getRequiredProperty("face.image.height", Integer.class);
        eachFaceNumber = environment.getRequiredProperty("each.face.number", Integer.class);
        numOfImagesForTraining = environment.getRequiredProperty("number.of.images.for.training", Integer.class);

        imageAsVectorLength = imageHeight * imageWidth;

        classPrefix = environment.getRequiredProperty("each.class.prefix", String.class);
        type = environment.getRequiredProperty("type.of.training.face.image", String.class);

        final Path classpath = new File(getClass().getClassLoader().getResource("").getFile()).toPath();
        pathToTraining = classpath.resolve(environment.getRequiredProperty("path.to.training.faces", String.class));
        pathToPrincipalComponentImages = classpath.resolve(environment.getRequiredProperty("path.to.principal.component.images", String.class));
    }

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
                ImageUtils.convertMatricesToImage(fe.getW(), imageHeight, imageWidth),
                pathToPrincipalComponentImages.resolve(featureExtractionMode.getName()).toString(),
                type
        );
    }

    private FeatureExtraction trainingSystem(final FeatureExtractionMode featureExtractionMode, final int componentsRetained) {
        /** set trainSet and testSet **/
        final Map<String, List<Integer>> trainMap = new HashMap<>();
        for (int i = 1; i <= faceNumber; i++) {
            trainMap.put(classPrefix + i, generateTrainNumbers());
        }
        /** set featureExtraction **/
        return featureExtractionMode.getInstance(getWorkingSetWithLabels(trainMap), componentsRetained, imageAsVectorLength);
    }

    private FeatureExtraction getFeatureExtraction(final FeatureExtractionMode featureExtractionMode, final int componentsRetained) {
        final FeatureExtraction ifPresent = cache.getIfPresent(featureExtractionMode);
        final FeatureExtraction fe;
        if(null == ifPresent){
            fe = trainingSystem(featureExtractionMode, componentsRetained);
            cache.put(featureExtractionMode, fe);
        } else {
            fe = ifPresent;
        }
        return fe;
    }

    private List<Pair<String, Matrix>> getWorkingSetWithLabels(final Map<String, List<Integer>> map) {
        final List<Pair<String, Matrix>> trainingSet = new ArrayList<>();
        for (String label : map.keySet()) {
            trainingSet.addAll(map.get(label).stream()
                            .map(i -> new Pair<>(label, ImageUtils.toVector(
                                    ImageUtils.convertToMatrix(
                                            ImageUtils.readImage(
                                                    pathToTraining.resolve(type).resolve(label).resolve(i + "." + type).toString()
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
        while (result.size() < numOfImagesForTraining) {
            int temp = random.nextInt(eachFaceNumber) + 1;
            while (result.contains(temp)) {
                temp = random.nextInt(eachFaceNumber) + 1;
            }
            result.add(temp);
        }
        return result;
    }
}