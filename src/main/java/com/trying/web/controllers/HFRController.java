package com.trying.web.controllers;

import com.trying.fe.ClassifySettings;
import com.trying.fe.HumanFaceRecognitionProcessor;
import com.trying.fe.enums.FeatureExtractionMode;
import com.trying.web.beans.HFRForm;
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
    @Autowired private com.trying.web.components.Properties properties;

    @RequestMapping("classify")
    public String classify(@ModelAttribute HFRForm form) {
        final Path path = properties.pathToResources.resolve(form.getFileName());
        final JSONObject answer = new JSONObject();
        try {
            if(Files.exists(path)) {
                final String clazz = hfr.classifyFace(ImageIO.read(Files.newInputStream(path)), ClassifySettings.getInstance(properties, form));
                answer.put("status", "ok");
                answer.put("class", clazz);
                answer.put("storedImages", getTrainingImages(clazz));
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
        final Path path = properties.pathToResources.resolve(form.getFileName());
        final JSONObject answer = new JSONObject();
        try {
            if(Files.exists(path)) {
                final ClassifySettings settings = ClassifySettings.getInstance(properties, form);
                final List<String> names = hfr.savePrincipalComponentImages(settings);
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

    private JSONArray getTrainingImages(final String clazz) {
        final JSONArray storedImages = new JSONArray();
        hfr.getTrainMap()
                .get(clazz)
                .forEach(c -> storedImages.put("/picture/getImage?file=" + properties.trainingImages + "/" + properties.trainingType + "/" + clazz + "/" + c + "." + properties.trainingType));
        return storedImages;
    }

    private JSONArray getEigenVectorImages(final FeatureExtractionMode fem, final List<String> names) throws IOException {
        final JSONArray storedImages = new JSONArray();
        names.stream().forEach(n -> storedImages.put("/picture/getImage?file=" + properties.components + "/" + fem.getName() + "/" + n));
        return storedImages;
    }
}