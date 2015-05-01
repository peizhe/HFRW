package com.kol.recognition.controllers;

import com.kol.recognition.beans.entities.DBImage;
import com.kol.recognition.components.ImageManager;
import com.kol.recognition.components.PictureDAO;
import com.kol.recognition.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class MainController {

    @Autowired private PictureDAO dao;
    @Autowired private ImageManager imageManager;

    @Value("${face.image.width}") private int imageWidth;
    @Value("${system.user.username}") private String user;
    @Value("${face.image.height}") private int imageHeight;
    @Value("${hfr.test.images.type}") public String imageType;
    @Value("${hfr.recognition.images.type}") public String recognitionType;

    @RequestMapping("/")
    public String start(ModelMap map){
        map.put("width",imageWidth);
        map.put("height", imageHeight);
        map.put("recognitionType", recognitionType);
        return "index";
    }

    @RequestMapping(value = "importTrainingFaces")
    @ResponseBody
    public String importTrainingFaces() throws Exception {
        final URL url = ResourceUtils.getURL(SystemPropertyUtils.resolvePlaceholders("classpath:training_faces/bmp"));
        final Path path = Paths.get(url.toURI());
        for (int i = 1; i <= 40; i++) {
            final String className = Utils.leadingZeros("" + i, 2);
            final String folder = "s" + className;
            for (int j = 1; j <= 10; j++) {
                final Path imagePath = path.resolve(folder).resolve(j + "." + imageType);
                final BufferedImage image = ImageIO.read(Files.newInputStream(imagePath));
                final DBImage dbImage = imageManager.toDBImage(image, imageType);
                dbImage.setClazz(recognitionType + className);
                dao.saveImage(dbImage, user);
            }
        }
        return "ok";
    }
}
