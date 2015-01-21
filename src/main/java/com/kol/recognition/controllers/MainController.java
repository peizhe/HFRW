package com.kol.recognition.controllers;

import com.kol.recognition.components.Properties;
import com.kol.recognition.utils.Utils;
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
import java.util.Base64;
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
                "INSERT INTO recognition_data (class, format, type, width, height, size, create_date, create_by, edit_date, edit_by, content) " +
                "VALUES (?,?,?,?,?,?,NOW(),?,NOW(),?,?)";
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
                            "face_" + clazz, prop.trainingType, "human_face", image.getWidth(), image.getHeight(), imageBytes.length, userId, userId,
                            Base64.getEncoder().encodeToString(imageBytes)
                    });
                }
            }
        }
        jdbc.batchUpdate(sql, data);
    }
}
