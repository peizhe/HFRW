package com.kol.recognition.controllers;

import com.kol.recognition.beans.entities.DBImage;
import com.kol.recognition.components.ImageManager;
import com.kol.recognition.components.PictureDAO;
import com.kol.recognition.perceptualHash.bean.Hash;
import com.kol.recognition.perceptualHash.distance.HammingDistance;
import com.kol.recognition.perceptualHash.distance.JaroWinklerDistance;
import com.kol.recognition.perceptualHash.distance.LevensteinDistance;
import com.kol.recognition.perceptualHash.hash.PerceptualHash;
import com.kol.recognition.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class MainController {

    @Autowired private PictureDAO dao;
    @Autowired private ImageManager imageManager;

    @Value("${system.user.username}") private String user;
    @Value("${hfr.test.images.type}") public String imageType;
    @Value("${hfr.recognition.images.type}") public String recognitionType;

    @RequestMapping("/")
    public String start(){
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

    /*@RequestMapping("test")
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
                    ImageIO.write(image, prop.imageType, output);
                    final byte[] imageBytes = output.toByteArray();

                    data.add(new Object[]{
                            "face_" + clazz, prop.trainingType, "human_face", image.getWidth(), image.getHeight(), imageBytes.length, userId, userId,
                            Base64.getEncoder().encodeToString(imageBytes)
                    });
                }
            }
        }
        jdbc.batchUpdate(sql, data);
    }*/

    @RequestMapping(value = "test")
    public void test() throws Exception {
//        final double[][] dctm = DCT.dct(new double[][]{{1, 6, 7}, {5, 3, 7}, {1.2, 5.6, 8}});
//        System.out.println(Arrays.deepToString(dctm));
//        final double[][] dct = DCT.dctm(new double[][]{{1, 6, 7}, {5, 3, 7}, {1.2, 5.6, 8}});
//        System.out.println(Arrays.deepToString(dct));
//
//        final int hw = 64;
//        System.out.println("Average Hash");
//        test(new AverageHash(hw, hw));
//        System.out.println("\n\nDCT Hash");
//        test(new DCTHash(hw, hw));
    }

    private void test(final PerceptualHash hash) throws IOException {
        final BufferedImage im1 = ImageIO.read(Files.newInputStream(Paths.get("D:\\1.jpg")));
        final BufferedImage im2 = ImageIO.read(Files.newInputStream(Paths.get("D:\\2.jpg")));
        final BufferedImage im3 = ImageIO.read(Files.newInputStream(Paths.get("D:\\3.jpg")));


        final Hash hash1 = hash.getHash(im1);
        final Hash hash2 = hash.getHash(im2);
        final Hash hash3 = hash.getHash(im3);

        System.out.println(hash1);
        System.out.println(hash2);
        System.out.println(hash3);

        System.out.println("1 - 2");
        System.out.println("Hamming = " + new HammingDistance().getDistance(hash1.getHash(), hash2.getHash()));
        System.out.println("Jaro = " + new JaroWinklerDistance().getDistance(hash1.getHash(), hash2.getHash()));
        System.out.println("Levenstein = " + new LevensteinDistance().getDistance(hash1.getHash(), hash2.getHash()));

        System.out.println("1 - 3");
        System.out.println("Hamming = " + new HammingDistance().getDistance(hash1.getHash(), hash3.getHash()));
        System.out.println("Jaro = " + new JaroWinklerDistance().getDistance(hash1.getHash(), hash3.getHash()));
        System.out.println("Levenstein = " + new LevensteinDistance().getDistance(hash1.getHash(), hash3.getHash()));

        System.out.println("2 - 3");
        System.out.println("Hamming = " + new HammingDistance().getDistance(hash2.getHash(), hash3.getHash()));
        System.out.println("Jaro = " + new JaroWinklerDistance().getDistance(hash2.getHash(), hash3.getHash()));
        System.out.println("Levenstein = " + new LevensteinDistance().getDistance(hash2.getHash(), hash3.getHash()));

        tmpSave(hash1.getImage(), "im_1_.bmp");
        tmpSave(hash2.getImage(), "im_2_.bmp");
        tmpSave(hash3.getImage(), "im_3_.bmp");
    }

    private static void tmpSave(final BufferedImage image, final String fileName) throws IOException {
        final Path path = Paths.get("D:\\").resolve(fileName);
        if(!Files.exists(path)){
            Files.createFile(path);
        }
        ImageIO.write(image, "bmp", Files.newOutputStream(path));
    }
}
