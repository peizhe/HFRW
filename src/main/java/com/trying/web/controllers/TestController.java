package com.trying.web.controllers;

import com.trying.fe.HumanFaceRecognitionProcessor;
import com.trying.fe.enums.FeatureExtractionMode;
import com.trying.fe.enums.MetricType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class TestController {

    @Autowired HumanFaceRecognitionProcessor hfr;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/test")
    public ModelAndView test() throws IOException {
        final ModelAndView mav = new ModelAndView("index");
        hfr.savePrincipalComponentImages(FeatureExtractionMode.PCA, 45);

        final BufferedImage image = ImageIO.read(Files.newInputStream(Paths.get("D:/master/programming/java/HFRW/src/main/resources/10.bmp")));
        final String clazz = hfr.classifyFace(image, MetricType.EUCLIDEAN, FeatureExtractionMode.PCA, 45, 2);

        mav.addObject("clazz", clazz);
        return mav;
    }
}