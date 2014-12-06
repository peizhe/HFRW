package com.trying.web.controllers;

import com.trying.fe.HumanFaceRecognitionProcessor;
import com.trying.fe.enums.FeatureExtractionMode;
import com.trying.fe.enums.MetricType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("hfr")
public class HFRController {

    @Autowired private HumanFaceRecognitionProcessor hfr;
    @Autowired private com.trying.web.components.Properties properties;

    @RequestMapping("classify")
    public String classify(@RequestParam(value = "fileName", required = true) String fileName,
                           @RequestParam(value = "metric", required = true) MetricType metric,
                           @RequestParam(value = "algorithm", required = true) FeatureExtractionMode algorithm,
                           @RequestParam(value = "nnCount", required = false, defaultValue = "2") Integer nnCount,
                           @RequestParam(value = "pcCount", required = false, defaultValue = "42") Integer pcCount) {

        final Path path = properties.pathToTestImages.resolve(fileName);
        final JSONObject answer = new JSONObject();
        try {
            if(Files.exists(path)) {
                final String clazz = hfr.classifyFace(ImageIO.read(Files.newInputStream(path)), metric, algorithm, pcCount, nnCount);
                answer.put("status", "ok");
                answer.put("class", clazz);
                answer.put("storedImages", getTrainingImages(clazz));
            } else {
                answer.put("status", "error");
                answer.put("reason", "NoSuchFileException: " + path);
            }
        } catch (IOException e) {
            answer.put("status", "error");
            answer.put("reason", e.getMessage());
        }
        return answer.toString();
//        hfr.savePrincipalComponentImages(FeatureExtractionMode.PCA, 45);
    }

    private JSONArray getTrainingImages(final String clazz) {
        final JSONArray storedImages = new JSONArray();
        hfr.getTrainMap()
                .get(clazz)
                .forEach(c -> storedImages.put(
                                properties.webTrainingImages
                                        .resolve(properties.trainingType)
                                        .resolve(clazz)
                                        .resolve(c + "." + properties.trainingType)
                        )
                );
        return storedImages;
    }
}