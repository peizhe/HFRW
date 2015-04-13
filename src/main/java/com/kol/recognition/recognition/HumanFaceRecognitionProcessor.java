package com.kol.recognition.recognition;

import Jama.Matrix;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.kol.recognition.beans.entities.DBImage;
import com.kol.recognition.general.settings.ClassifySettings;
import com.kol.recognition.general.Image;
import com.kol.recognition.components.recognition.Recognizer;
import com.kol.recognition.components.PictureDAO;
import com.kol.recognition.components.recognition.AnalysisAlgorithm;
import com.kol.recognition.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class HumanFaceRecognitionProcessor {

    @Autowired private PictureDAO dao;

    @Value("${face.image.width}") private int width;
    @Value("${face.image.height}") private int height;
    @Value("${hfr.use.cache}") private boolean useCache;

    private final Cache<AnalysisAlgorithm, Recognizer> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES).build();

    public String classifyFace(final Recognizer classifier, final BufferedImage image, final ClassifySettings settings) {
        return classifier.classify(ImageUtils.toVector(ImageUtils.toMatrix(image)), settings);
    }

    private Recognizer train(final ClassifySettings settings, final String type, final RecognizerTrainType trainType) {
        final Collection<String> classes = dao.getClasses(type);

        /** read images from database ang choose some of them (or all) for train the recognizer **/
        final Multimap<String, Image> training = ArrayListMultimap.create();
        classes.forEach(classCode -> training.putAll(
                classCode, trainType.getTrainObjectIds(
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
        return settings.getAlgorithm().get(data, settings.getComponents(), width*height, training, settings);
    }

    public Recognizer getRecognizer(final ClassifySettings settings, final String type, final RecognizerTrainType trainType) {
        if(useCache) {
            final Recognizer ifPresent = cache.getIfPresent(settings.getAlgorithm());
            final Recognizer recognizer;
            if (null == ifPresent) {
                recognizer = train(settings, type, trainType);
                cache.put(settings.getAlgorithm(), recognizer);
            } else {
                recognizer = ifPresent;
            }
            return recognizer;
        } else {
            return train(settings, type, trainType);
        }
    }
}