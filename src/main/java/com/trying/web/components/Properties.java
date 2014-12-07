package com.trying.web.components;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class Properties {

    @Resource private Environment environment;

    public int faceNumber;
    public int imageWidth;
    public int imageHeight;
    public boolean useCache;
    public int eachFaceNumber;
    public int numOfKNNComponents;
    public int imageAsVectorLength;
    public int numOfImagesForTraining;
    public int principalComponentsCount;
    public int classLength;

    public String testType;
    public String classPrefix;
    public String trainingType;

    public Path pathToResources;
    public String testImages;
    public String components;
    public String trainingImages;

    @PostConstruct
    private void init() {
        faceNumber = environment.getRequiredProperty("face.number", Integer.class);
        imageWidth = environment.getRequiredProperty("face.image.width", Integer.class);
        imageHeight = environment.getRequiredProperty("face.image.height", Integer.class);
        eachFaceNumber = environment.getRequiredProperty("each.face.number", Integer.class);
        numOfKNNComponents = environment.getRequiredProperty("hfr.knn.count", Integer.class);
        numOfImagesForTraining = environment.getRequiredProperty("hfr.training.images.count", Integer.class);
        principalComponentsCount = environment.getRequiredProperty("hfr.principal.components.count", Integer.class);

        classLength = String.valueOf(faceNumber).length();
        imageAsVectorLength = imageHeight * imageWidth;

        useCache = environment.getRequiredProperty("hfr.use.cache", Boolean.class);
        classPrefix = environment.getRequiredProperty("hfr.class.prefix", String.class);
        testType = environment.getRequiredProperty("hfr.test.images.type", String.class);
        trainingType = environment.getRequiredProperty("hfr.training.images.type", String.class);

        pathToResources = Paths.get(environment.getRequiredProperty("hfr.path.to.resources", String.class));
        testImages = environment.getRequiredProperty("hfr.test.images", String.class);
        components = environment.getRequiredProperty("hfr.component.images", String.class);
        trainingImages = environment.getRequiredProperty("hfr.training.images", String.class);
    }
}