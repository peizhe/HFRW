package com.kol.recognition.controllers;

import com.kol.recognition.beans.HFRForm;
import com.kol.recognition.beans.entities.DBImage;
import com.kol.recognition.components.ImageManager;
import com.kol.recognition.components.PictureDAO;
import com.kol.recognition.general.Algorithm;
import com.kol.recognition.general.RecognitionAlgorithm;
import com.kol.recognition.general.settings.ClassifySettings;
import com.kol.recognition.general.settings.ClassifySettingsBuilder;
import com.kol.recognition.recognition.HumanFaceRecognitionProcessor;
import com.kol.recognition.utils.ImageUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @Value("${system.user.username}") private String user;
    @Value("${face.image.height}") private int imageHeight;
    @Value("${hfr.recognition.images.type}") public String type;
    @Value("${hfr.principal.components.count}") public int pcaCount;
    @Value("${hfr.training.images.count}") public int trainingImages;

    @RequestMapping(value = "classify")
    public String classify(@ModelAttribute final HFRForm form) {
        final DBImage dbImage = dao.getImage(form.getFileId());
        final JSONObject answer = new JSONObject();
        if(null != dbImage) {
            final Algorithm algorithm = hfr.getAlgorithm(getSettings(form));
            final String className = hfr.classifyFace(algorithm, ImageUtils.fromByteArray(dbImage.getByteContent()));
            answer.put("status", "ok");
            answer.put("class", className);
            answer.put("storedImages", getTrainingImages(className, algorithm));
        } else {
            answer.put("status", "error");
            answer.put("reason", "NoSuchFileException");
        }
        return answer.toString();
    }

    @RequestMapping(value = "eigenVectors")
    public String eigenVectors(@ModelAttribute final HFRForm form) {
        final DBImage dbImage = dao.getImage(form.getFileId());
        final JSONObject answer = new JSONObject();
        if(null != dbImage) {
            final ClassifySettings settings = getSettings(form);
            final Algorithm algorithm = hfr.getAlgorithm(settings);
            final Collection<DBImage> names = savePrincipalComponentImages(algorithm, settings.getAlgorithm());
            answer.put("status", "ok");
            answer.put("storedImages", getEigenVectorImages(names));
        } else {
            answer.put("status", "error");
            answer.put("reason", "NoSuchFileException");
        }
        return answer.toString();
    }

    private JSONArray getTrainingImages(final String className, final Algorithm algorithm) {
        return new JSONArray(algorithm.getTraining().get(className).stream().map(c -> "./picture/getImage/" + c.getId()).collect(Collectors.toList()));
    }

    private JSONArray getEigenVectorImages(final Collection<DBImage> images) {
        return new JSONArray(images.stream().map(im -> "./picture/getImage/" + im.getId()).collect(Collectors.toList()));
    }

    private Collection<DBImage> savePrincipalComponentImages(final Algorithm classifier, final RecognitionAlgorithm algorithm) {
        final List<BufferedImage> images = classifier.getProcessedImages();
        final Collection<DBImage> dbImages = images.stream()
                .map(image -> imageManager.toDBImage(image, ImageManager.DEFAULT_IMAGE_FORMAT))
                .peek(image -> image.setClazz(algorithm.name()))
                .collect(Collectors.toList());
        dao.saveImages(dbImages, user);
        return dbImages;
    }

    private ClassifySettings getSettings(final HFRForm form) {
        return ClassifySettingsBuilder
                .start(knnCount, pcaCount, trainingImages, type)
                .knn(form.getKnnValue(), form.getKnnType())
                .pca(form.getComponentsValue(), form.getComponentsType())
                .metric(form.getMetricType())
                .images(form.getTrainingValue())
                .algorithm(form.getAlgorithm())
                .type(form.getRecognitionType())
                .distance(form.getStringDistanceType())
                .recognizerTrainType(form.getTrainingType())
                .result();
    }
}