package com.kol.recognition.controllers;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.kol.recognition.beans.DBImage;
import com.kol.recognition.dao.PictureDAO;
import com.kol.recognition.enums.FeatureExtractionMode;
import com.kol.recognition.beans.PictureCropInfo;
import com.kol.recognition.utils.NumberUtils;
import com.kol.recognition.utils.Utils;
import org.imgscalr.Scalr;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "picture")
public class PictureController {

    @Autowired private PictureDAO dao;
    private static final int BILINEAR = 0;
    private static final String BASE64_IMAGE_TYPE = "base64";
    private static final String TEST_IMAGES_FILE_NAME = "TEST_IMAGES";
    private static final DateFormat format = new SimpleDateFormat("yyy-MM-dd HH-mm-ss");

    @Autowired private com.kol.recognition.components.Properties prop;

    @PreDestroy
    private void stop() throws IOException {
        Files.walk(prop.resources.resolve(prop.testImages))
                .filter(filePath -> !filePath.toString().endsWith(TEST_IMAGES_FILE_NAME))
                .forEach(filePath -> {try {Files.delete(filePath);} catch (IOException ignored) {}});
        deleteImagesWithPrincipalComponents(prop.resources.resolve(prop.components), FeatureExtractionMode.PCA);
        deleteImagesWithPrincipalComponents(prop.resources.resolve(prop.components), FeatureExtractionMode.LDA);
    }

    @RequestMapping(value = "getImage")
    public void getImage(@RequestParam(value = "fileId", required = true) String fileId, HttpServletResponse response) throws IOException {
        final DBImage image = dao.getImage(NumberUtils.decode(fileId));
        if(null != image) {
            response.setContentType("image/" + image.getFormat());
            response.setContentLength(image.getSize());
            try (OutputStream os = response.getOutputStream()) {
                os.write(image.getContent());
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }
        }
    }

    @RequestMapping(value = "crop")
    public String crop(@RequestParam(value = "file", required = true) String fileName,
                       @RequestParam(value = "selection", required = true) String jsonSelection,
                       @RequestParam(value = "algorithm", required = true) Integer algorithm) throws JSONException, IOException {
        final JSONObject answer = new JSONObject();
        if (null != jsonSelection && !jsonSelection.isEmpty()) {
            final PictureCropInfo pictureCropInfo = PictureCropInfo.fromJson(new JSONObject(jsonSelection));
            final Path path = prop.resources.resolve(fileName);
            if(Files.exists(path)) {
                final BufferedImage src = ImageIO.read(Files.newInputStream(path));
                final BufferedImage templateImage = resizeImage(crop(src, pictureCropInfo), prop.imageWidth, prop.imageHeight, algorithm);

                final String file = saveImage(templateImage, "crop_image_");
                answer.put("status", "ok");
                answer.put("fileName", file);
                answer.put("width", templateImage.getWidth());
                answer.put("height", templateImage.getHeight());
                answer.put("src", "/picture/getImage?file=" + file);
            }
        } else {
            answer.put("status", "error");
        }
        return answer.toString();
    }


    public static BufferedImage crop(final BufferedImage src, final PictureCropInfo pictureCropInfo) throws IOException {
        final BufferedImage dst = new BufferedImage(pictureCropInfo.getWidth(), pictureCropInfo.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        final Graphics2D g = dst.createGraphics();
        g.drawImage(src, 0, 0, pictureCropInfo.getWidth(), pictureCropInfo.getHeight(), pictureCropInfo.getStartX(),
                pictureCropInfo.getStartY(), pictureCropInfo.getStartX()+pictureCropInfo.getWidth(), pictureCropInfo.getStartY()+pictureCropInfo.getHeight(), null);
        g.dispose();
        return dst;
    }

    public static BufferedImage resizeImage(final BufferedImage originalImage, final int width, final int height, final int algorithm){
        if(BILINEAR == algorithm){
            return Scalr.resize(originalImage, Scalr.Method.QUALITY, width, height);
        } else {
            return Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, width, height);
        }
    }

    @RequestMapping(value = "upload")
    public String upload(@RequestParam(value = "image", required = true) String jsonImage) throws JSONException, IOException {
        final JSONObject image = new JSONObject(jsonImage);
        final String type = image.getString("type");
        final String src = image.getString("src");
        final byte[] binaryData;
        if(BASE64_IMAGE_TYPE.equals(type)){
            binaryData = DatatypeConverter.parseBase64Binary(src);
        } else {
            try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                URLConnection urlConnection = new URL(src).openConnection();
                FileCopyUtils.copy(urlConnection.getInputStream(), baos);
                binaryData = baos.toByteArray();
            }
        }
        final BufferedImage renderImage = ImageIO.read(new ByteArrayInputStream(binaryData));
        final String fileName = saveImage(renderImage, "image_");

        final JSONObject answer = new JSONObject();
        answer.put("status", "ok");
        answer.put("fileName", fileName);
        answer.put("width", renderImage.getWidth());
        answer.put("height", renderImage.getHeight());
        answer.put("src", "/picture/getImage?file=" + fileName);
        return answer.toString();
    }

    private String saveImage(final BufferedImage image, final String name) throws IOException {
        final String fileName = name + format(Instant.now()) + "." + prop.testType;
        final Path path = prop.resources.resolve(prop.testImages + "/" + fileName);
        if(!Files.exists(path)){
            Files.createFile(path);
        }
        ImageIO.write(image, prop.testType, Files.newOutputStream(path));
        return prop.testImages + "/" + fileName;
    }

    private String format(final Instant instant){
        final Date date = new Date(instant.toEpochMilli());
        return format.format(date);
    }

    @RequestMapping(value = "storedImages")
    public String getAllStoredImages(@RequestParam(value = "type", required = true) String type){
        final List<DBImage> dbImages = dao.getImages(type, prop.imageWidth, prop.imageHeight);
        final JSONObject images = new JSONObject();
        final Multimap<String, DBImage> map = Multimaps.index(dbImages, DBImage::getName);
        for (String key : map.keySet()) {
            images.put(key, map.get(key).stream().map(image -> "/picture/getImage?fileId=" + NumberUtils.encode(image.getId())).collect(Collectors.toList()));
        }
        return images.toString();
    }

    private static void deleteImagesWithPrincipalComponents(final Path path, final FeatureExtractionMode fem) throws IOException {
        Files.walk(path.resolve(fem.getName()))
                .filter(filePath -> !filePath.toString().endsWith(fem.getName()))
                .forEach(filePath -> {try {Files.delete(filePath);} catch (IOException ignored) {}});
    }
}