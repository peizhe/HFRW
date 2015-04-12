package com.kol.recognition.controllers;

import com.kol.recognition.components.beans.ClassifySettings;
import com.kol.recognition.beans.entities.DBImage;
import com.kol.recognition.beans.entities.RecognitionDataClass;
import com.kol.recognition.components.ImageManager;
import com.kol.recognition.dao.PictureDAO;
import com.kol.recognition.recognition.HumanFaceRecognitionProcessor;
import com.kol.recognition.components.recognition.Recognizer;
import com.kol.recognition.forms.HFRForm;
import com.kol.recognition.utils.ClassifySettingsBuilder;
import com.kol.recognition.utils.ImageUtils;
import com.kol.recognition.utils.NumberUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "hfr")
public class HFRController {

    @Autowired private PictureDAO dao;
    @Autowired private ImageManager imageManager;
    @Autowired private HumanFaceRecognitionProcessor hfr;

    @Value("${hfr.knn.count}") public int knnCount;
    @Value("${face.image.width}") private int imageWidth;
    @Value("${face.image.height}") private int imageHeight;
    @Value("${hfr.principal.components.count}") public int pcaCount;
    @Value("${hfr.training.images.count}") public int trainingImages;

    @RequestMapping(value = "classify")
    public String classify(@ModelAttribute final HFRForm form,
                           @RequestParam(required = false, defaultValue = "HMF") final String type) {
        final DBImage dbImage = dao.getImage(NumberUtils.decode(form.getFileId()));
        final JSONObject answer = new JSONObject();
        if(null != dbImage) {
            final ClassifySettings settings = getSettings(form);
            final Recognizer recognizer = hfr.getRecognizer(settings, type, form.getRecognizerTrainType());
            final String className = hfr.classifyFace(recognizer, imageManager.fromByteArray(dbImage.getByteContent()), settings);
            answer.put("status", "ok");
            answer.put("class", className);
            answer.put("storedImages", getTrainingImages(className, recognizer));
        } else {
            answer.put("status", "error");
            answer.put("reason", "NoSuchFileException");
        }
        return answer.toString();
    }

    @RequestMapping(value = "eigenVectors")
    public String eigenVectors(@ModelAttribute final HFRForm form,
                               @RequestParam(required = false, defaultValue = "HMF") final String type) {
        final DBImage dbImage = dao.getImage(NumberUtils.decode(form.getFileId()));
        final JSONObject answer = new JSONObject();
        if(null != dbImage) {
            final ClassifySettings settings = getSettings(form);
            final Recognizer fe = hfr.getRecognizer(settings, type, form.getRecognizerTrainType());
            final Collection<DBImage> names = savePrincipalComponentImages(fe, settings.getAlgorithm().name());
            answer.put("status", "ok");
            answer.put("storedImages", getEigenVectorImages(names));
        } else {
            answer.put("status", "error");
            answer.put("reason", "NoSuchFileException");
        }
        return answer.toString();
    }

    private JSONArray getTrainingImages(final String className, final Recognizer recognizer) {
        final JSONArray storedImages = new JSONArray();
        recognizer.getTraining()
                .get(className)
                .forEach(c -> storedImages.put("./picture/getImage/" + c.getId()));
        return storedImages;
    }

    private JSONArray getEigenVectorImages(final Collection<DBImage> images) {
        final JSONArray storedImages = new JSONArray();
        images.stream().forEach(im -> storedImages.put("./picture/getImage/" + NumberUtils.encode(im.getId())));
        return storedImages;
    }

    private Collection<DBImage> savePrincipalComponentImages(final Recognizer classifier, final String classCode) {
        final RecognitionDataClass clazz = dao.getClassByCode(classCode);
        final List<BufferedImage> images = ImageUtils.convertMatricesToImage(classifier.getW(), imageHeight, imageWidth);
        final Collection<DBImage> dbImages = images.stream()
                .map(image -> {
                    final DBImage dbImage = imageManager.toDBImage(image, ImageManager.DEFAULT_IMAGE_FORMAT);
                    dbImage.setClazz(clazz);
                    return dbImage;
                }).collect(Collectors.toList());
        dao.saveImages(dbImages);
        return dbImages;
    }

    private ClassifySettings getSettings(final HFRForm form) {
        return ClassifySettingsBuilder
                .start(knnCount, pcaCount, trainingImages)
                .knn(form.getKnnCount(), form.getKnnComponent())
                .pca(form.getPrincipalComponentsCount(), form.getPrincipalComponents())
                .metric(form.getMetric())
                .images(form.getTrainingImageCount())
                .algorithm(form.getAlgorithm())
                .result();
    }
}