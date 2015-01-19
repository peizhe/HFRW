package com.kol.recognition.web.controllers;

import com.kol.recognition.fe.ClassifySettings;
import com.kol.recognition.fe.HumanFaceRecognitionProcessor;
import com.kol.recognition.enums.FeatureExtractionMode;
import com.kol.recognition.featureExtraction.FeatureExtraction;
import com.kol.recognition.web.beans.HFRForm;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("hfr")
public class HFRController {

    @Autowired private HumanFaceRecognitionProcessor hfr;
    @Autowired private com.kol.recognition.web.components.Properties prop;

    @RequestMapping("classify")
    public String classify(@ModelAttribute HFRForm form) {
        final Path path = prop.resources.resolve(form.getFileName());
        final JSONObject answer = new JSONObject();
        try {
            if(Files.exists(path)) {
                final ClassifySettings settings = ClassifySettings.getInstance(prop, form);
                final FeatureExtraction fe = hfr.getFeatureExtraction(settings);
                final String clazz = hfr.classifyFace(fe, ImageIO.read(Files.newInputStream(path)), settings);
                answer.put("status", "ok");
                answer.put("class", clazz);
                answer.put("storedImages", getTrainingImages(clazz, fe));
            } else {
                answer.put("status", "error");
                answer.put("reason", "NoSuchFileException: " + path);
            }
        } catch (IOException e) {
            answer.put("status", "error");
            answer.put("reason", e.toString());
        }
        return answer.toString();
    }

    @RequestMapping("eigenVectors")
    public String eigenVectors(@ModelAttribute HFRForm form) {
        final Path path = prop.resources.resolve(form.getFileName());
        final JSONObject answer = new JSONObject();
        try {
            if(Files.exists(path)) {
                final ClassifySettings settings = ClassifySettings.getInstance(prop, form);
                final FeatureExtraction fe = hfr.getFeatureExtraction(settings);
                final List<String> names = hfr.savePrincipalComponentImages(fe, settings);
                answer.put("status", "ok");
                answer.put("storedImages", getEigenVectorImages(settings.getFeMode(), names));
            } else {
                answer.put("status", "error");
                answer.put("reason", "NoSuchFileException: " + path);
            }
        } catch (IOException e) {
            answer.put("status", "error");
            answer.put("reason", e.toString());
        }
        return answer.toString();
    }

    private JSONArray getTrainingImages(final String clazz, final FeatureExtraction fe) {
        final JSONArray storedImages = new JSONArray();
        fe.getTrainMap()
                .get(clazz)
                .forEach(c -> storedImages.put("/picture/getImage?file=" + prop.trainingImages + "/" + prop.trainingType + "/" + clazz + "/" + c + "." + prop.trainingType));
        return storedImages;
    }

    private JSONArray getEigenVectorImages(final FeatureExtractionMode fem, final List<String> names) throws IOException {
        final JSONArray storedImages = new JSONArray();
        names.stream().forEach(n -> storedImages.put("/picture/getImage?file=" + prop.components + "/" + fem.getName() + "/" + n));
        return storedImages;
    }
}