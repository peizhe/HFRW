package com.trying.web.controllers;

import com.trying.fe.HumanFaceRecognitionProcessor;
import com.trying.fe.enums.FeatureExtractionMode;
import com.trying.fe.enums.MetricType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class TestController {

    @Autowired HumanFaceRecognitionProcessor hfr;

    @RequestMapping("/")
    public String index() {
        return "model";
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

    /**
     * upload image from given link or create it from given base64
     */
    @RequestMapping(value = "uploadImage")
    @ResponseBody public String uploadImage(@RequestParam(value = "src", required = true) String src,
                              @RequestParam(value = "type", required = true) String type) throws IOException {
        final DateFormat df = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss");
        final Path file = Files.createFile(Paths.get("d:\\image_" + df.format(new Date()) + ".jpg"));
        if("base64".equalsIgnoreCase(type)){
            ByteArrayInputStream is = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(src));
            ImageIO.write(ImageIO.read(is), "jpg", Files.newOutputStream(file));
        } else {
            URL url = new URL(src);
            BufferedImage image = ImageIO.read(url);
            ImageIO.write(image, "jpg", Files.newOutputStream(file));
        }
        return file.getFileName().toString();
    }

    /**
     * load drag and drop test page
     */
    @RequestMapping(value = "dragAndDrop")
    public ModelAndView dragAndDrop() {
        return new ModelAndView("dragAndDrop");
    }
}