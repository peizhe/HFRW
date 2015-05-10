package com.kol.recognition.recognition;

import Jama.Matrix;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.kol.recognition.beans.entities.DBImage;
import com.kol.recognition.components.PictureDAO;
import com.kol.recognition.components.beans.AnalysisSettings;
import com.kol.recognition.components.beans.AnalysisSettingsPlaceholder;
import com.kol.recognition.general.Algorithm;
import com.kol.recognition.general.Image;
import com.kol.recognition.general.RecognitionAlgorithm;
import com.kol.recognition.general.settings.ClassifySettings;
import com.kol.recognition.nbc.NBCSettingsPlaceholder;
import com.kol.recognition.perceptualHash.bean.Hash;
import com.kol.recognition.perceptualHash.bean.HashSettingsPlaceholder;
import com.kol.recognition.perceptualHash.hash.AverageHash;
import com.kol.recognition.perceptualHash.hash.DCTHash;
import com.kol.recognition.perceptualHash.hash.PerceptualHash;
import com.kol.recognition.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class HumanFaceRecognitionProcessor {

    @Autowired private PictureDAO dao;

    @Value("${face.image.width}") private int width;
    @Value("${face.image.height}") private int height;
    @Value("${hfr.use.cache}") private boolean useCache;

    @Value("${hfr.hash.width}") private int hashWidth;
    @Value("${hfr.hash.height}") private int hashHeight;

    private final Cache<ClassifySettings, Algorithm> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES).build();

    public String classifyFace(final Algorithm algorithm, final BufferedImage image) {
        return algorithm.classify(image);
    }

    private Algorithm trainComponentAlgorithm(final ClassifySettings settings) {
        final Collection<String> classes = dao.getClasses(settings.getType());

        /** read images from database ang choose some of them (or all) for train the recognizer **/
        final Multimap<String, Image> training = ArrayListMultimap.create();
        classes.forEach(classCode -> training.putAll(
                classCode, settings.getTrainType().getTrainObjectIds(
                        settings.getNumberOfImages(),
                        dao.getImages(classCode, width, height).stream().map(DBImage::toImage).collect(Collectors.toList())
                )
        ));

        /** transform images, which were choose for training, into Jama.Matrix **/
        final Multimap<String, Matrix> data = ArrayListMultimap.create();
        for (String label : training.keySet()) {
            data.putAll(label, training.get(label).stream().map(ImageUtils::toVector).collect(Collectors.toList()));
        }
        /** get Recognizer based on exist data **/
        final AnalysisSettingsPlaceholder placeholder = new AnalysisSettingsPlaceholder();
        placeholder.setData(data);
        placeholder.setTrain(training);
        placeholder.setVecLength(width * height);
        placeholder.setComponents(settings.getComponents());

        final AnalysisSettings analysisSettings = new AnalysisSettings();
        analysisSettings.setKnnCount(settings.getKnnCount());
        analysisSettings.setMetric(settings.getMetric());
        analysisSettings.setWidth(width);
        analysisSettings.setHeight(height);
        placeholder.setSettings(analysisSettings);

        return settings.getAlgorithm().get(placeholder);
    }

    private Algorithm trainHashAlgorithm(final ClassifySettings settings) {
        final RecognitionAlgorithm algorithm = settings.getAlgorithm();

        final Collection<String> classes = dao.getClasses(settings.getType());
        final Multimap<String, Image> training = ArrayListMultimap.create();
        classes.forEach(classCode -> training.putAll(classCode,
                settings.getTrainType().getTrainObjectIds(
                        settings.getNumberOfImages(),
                        dao.getImages(classCode, width, height).stream().map(DBImage::toImage).collect(Collectors.toList())
                )
        ));

        final PerceptualHash hash = hash(algorithm);
        final Multimap<String, Hash> data = ArrayListMultimap.create();
        for (String label : training.keySet()) {
            data.putAll(label, training.get(label).stream().map(i -> hash.getHash(i.getContent())).collect(Collectors.toList()));
        }

        final HashSettingsPlaceholder placeholder = new HashSettingsPlaceholder();
        placeholder.setDistance(settings.getDistanceType().get());
        placeholder.setHeight(hashHeight);
        placeholder.setWidth(hashWidth);
        placeholder.setData(data);
        placeholder.setTrain(training);
        return algorithm.get(placeholder);
    }

    private Algorithm trainClassifyAlgorithm(final ClassifySettings settings) {
        final Collection<String> classes = dao.getClasses(settings.getType());

        /** read images from database ang choose some of them (or all) for train the recognizer **/
        final Multimap<String, Image> training = ArrayListMultimap.create();
        classes.forEach(classCode -> training.putAll(
                classCode, settings.getTrainType().getTrainObjectIds(
                        settings.getNumberOfImages(),
                        dao.getImages(classCode, width, height).stream().map(DBImage::toImage).collect(Collectors.toList())
                )
        ));

        /** transform images, which were choose for training, into Jama.Matrix **/
        final Multimap<String, BufferedImage> data = ArrayListMultimap.create();
        for (String label : training.keySet()) {
            data.putAll(label, training.get(label).stream().map(v -> ImageUtils.fromByteArray(v.getContent())).collect(Collectors.toList()));
        }
        /** get Recognizer based on exist data **/
        final NBCSettingsPlaceholder placeholder = new NBCSettingsPlaceholder();
        placeholder.setData(data);
        placeholder.setTrain(training);
        placeholder.setWidth(width);
        placeholder.setHeight(height);

        return settings.getAlgorithm().get(placeholder);
    }

    public Algorithm getAlgorithm(final ClassifySettings settings) {
        if(useCache) {
            final Algorithm ifPresent = cache.getIfPresent(settings);
            final Algorithm algorithm;
            if (null == ifPresent) {
                algorithm = algorithm(settings);
                cache.put(settings, algorithm);
            } else {
                algorithm = ifPresent;
            }
            return algorithm;
        } else {
            return algorithm(settings);
        }
    }

    private Algorithm algorithm(final ClassifySettings settings) {
        final RecognitionAlgorithm algorithm = settings.getAlgorithm();
        switch(algorithm.getType()) {
            case COMPONENT:
                return trainComponentAlgorithm(settings);
            case HASH:
                return trainHashAlgorithm(settings);
            case CLASSIFY:
                return trainClassifyAlgorithm(settings);
            default:
                throw new RuntimeException("Incorrect Recognition Algorithm Type: " + algorithm.getType());
        }
    }

    public PerceptualHash hash(final RecognitionAlgorithm algorithm) {
        switch(algorithm) {
            case AHASH:
                return new AverageHash(hashWidth, hashHeight);
            case DCT_HASH:
                return new DCTHash(hashWidth, hashHeight);
            default:
                throw new RuntimeException("Incorrect Perceptual Hash Algorithm selected: " + algorithm);
        }
    }
}