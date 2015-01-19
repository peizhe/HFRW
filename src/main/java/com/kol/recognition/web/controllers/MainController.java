package com.kol.recognition.web.controllers;

import com.kol.recognition.web.components.Properties;
import com.kol.recognition.web.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired Properties prop;
    @Autowired JdbcOperations jdbc;
    @Value("${system.user.id}") int userId;

    @RequestMapping("/")
    public String start(){
        return "index";
    }

    @RequestMapping("test")
    public void test() throws IOException {
        final String sql =
                "INSERT INTO recognition_data (class_code, image_format, width, height, size, image_content, create_date, create_by, edit_date, edit_by) " +
                "VALUES (?,?,?,?,?,?,NOW(),?,NOW(),?)";
        final List<Object[]> data = new ArrayList<>();
        for (int i = 1; i <= prop.faceNumber; i++) {
            final String clazz = prop.classPrefix + Utils.leadingZeros(i, prop.classLength);
            for (int j = 1; j <= prop.eachFaceNumber; j++) {
                final String fileName = prop.trainingImages + "/" + prop.trainingType + "/" + clazz + "/" + j + "." + prop.trainingType;
                final Path path = prop.resources.resolve(fileName);
                if(Files.exists(path)) {
                    final BufferedImage image = ImageIO.read(Files.newInputStream(path));
                    final ByteArrayOutputStream output = new ByteArrayOutputStream();
                    ImageIO.write(image, prop.testType, output);
                    final byte[] imageBytes = output.toByteArray();

                    data.add(new Object[]{
                            "face_" + i, prop.trainingType, image.getWidth(), image.getHeight(), imageBytes.length,
                            imageBytes, userId, userId
                    });
                }
            }
        }
        jdbc.batchUpdate(sql, data);
    }
}
