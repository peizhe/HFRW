package com.trying.web.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class Properties {

    @Resource private Environment environment;

    @Value("${hfr.use.cache}") public boolean useCache;

    @Value("${hfr.knn.count}") public int numOfKNNComponents;
    @Value("${hfr.training.images.count}") public int numOfImagesForTraining;
    @Value("${hfr.principal.components.count}") public int principalComponentsCount;

    @Value("${hfr.test.images}") public String testImages;
    @Value("${hfr.class.prefix}") public String classPrefix;
    @Value("${hfr.test.images.type}") public String testType;
    @Value("${hfr.component.images}") public String components;
    @Value("${hfr.training.images}") public String trainingImages;
    @Value("${hfr.training.images.type}") public String trainingType;

    @Value("${face.number}") public int faceNumber;
    @Value("${face.image.width}") public int imageWidth;
    @Value("${face.image.height}") public int imageHeight;
    @Value("${each.face.number}") public int eachFaceNumber;

    public int classLength;
    public Path resources;
    public int imageAsVectorLength;

    @PostConstruct
    private void init() {
        imageAsVectorLength = imageHeight * imageWidth;
        classLength = String.valueOf(faceNumber).length();
        resources = Paths.get(environment.getRequiredProperty("hfr.path.to.resources", String.class));
    }
}