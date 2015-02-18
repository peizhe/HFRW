package com.kol.recognition.controllers;

import com.kol.recognition.beans.DBImage;
import com.kol.recognition.components.ImageManager;
import com.kol.recognition.dao.PictureDAO;
import com.kol.recognition.fe.ClassifySettings;
import com.kol.recognition.fe.HumanFaceRecognitionProcessor;
import com.kol.recognition.featureExtraction.FeatureExtraction;
import com.kol.recognition.forms.HFRForm;
import com.kol.recognition.utils.ImageUtils;
import com.kol.recognition.utils.NumberUtils;
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
    @Value("${face.image.height}") private int imageHeight;
    @Value("${hfr.principal.components.count}") public int pcaCount;
    @Value("${hfr.training.images.count}") public int trainingImages;

    @RequestMapping(value = "classify")
    public String classify(@ModelAttribute final HFRForm form) {
        final DBImage dbImage = dao.get(NumberUtils.decode(form.getFileId()), DBImage.class);
        final JSONObject answer = new JSONObject();
        if(null != dbImage) {
            final ClassifySettings settings = ClassifySettings.getInstance(knnCount, pcaCount, trainingImages, form);
            final FeatureExtraction fe = hfr.getFeatureExtraction(settings);
            final String className = hfr.classifyFace(fe, imageManager.fromByteArray(dbImage.getContent()), settings);
            answer.put("status", "ok");
            answer.put("class", className);
            answer.put("storedImages", getTrainingImages(className, fe));
        } else {
            answer.put("status", "error");
            answer.put("reason", "NoSuchFileException");
        }
        return answer.toString();
    }

    @RequestMapping(value = "eigenVectors")
    public String eigenVectors(@ModelAttribute final HFRForm form) {
        final DBImage dbImage = dao.get(NumberUtils.decode(form.getFileId()), DBImage.class);
        final JSONObject answer = new JSONObject();
        if(null != dbImage) {
            final ClassifySettings settings = ClassifySettings.getInstance(knnCount, pcaCount, trainingImages, form);
            final FeatureExtraction fe = hfr.getFeatureExtraction(settings);
            final Collection<DBImage> names = savePrincipalComponentImages(fe, settings.getFeMode().getName());
            answer.put("status", "ok");
            answer.put("storedImages", getEigenVectorImages(names));
        } else {
            answer.put("status", "error");
            answer.put("reason", "NoSuchFileException");
        }
        return answer.toString();
    }

    private JSONArray getTrainingImages(final String className, final FeatureExtraction fe) {
        final JSONArray storedImages = new JSONArray();
        fe.getTrainMap()
                .get(className)
                .forEach(c -> storedImages.put("./picture/getImage?fileId="));
        return storedImages;
    }

    private JSONArray getEigenVectorImages(final Collection<DBImage> images) {
        final JSONArray storedImages = new JSONArray();
        images.stream().forEach(im -> storedImages.put("./picture/getImage?fileId=" + NumberUtils.encode(im.getId())));
        return storedImages;
    }

    private Collection<DBImage> savePrincipalComponentImages(final FeatureExtraction classifier, final String className) {
        final List<BufferedImage> images = ImageUtils.convertMatricesToImage(classifier.getW(), imageHeight, imageWidth);
        final Collection<DBImage> dbImages = images.stream()
                .map(image -> {
                    final DBImage dbImage = imageManager.toDBImage(image, ImageManager.DEFAULT_IMAGE_FORMAT);
                    dbImage.setClazz(className);
                    return dbImage;
                }).collect(Collectors.toList());
        dao.save(dbImages);
        return dbImages;
    }
}