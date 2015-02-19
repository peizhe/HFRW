package com.kol.recognition.controllers;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.kol.recognition.beans.DBImage;
import com.kol.recognition.components.ImageManager;
import com.kol.recognition.dao.PictureDAO;
import com.kol.recognition.beans.CropInfo;
import com.kol.recognition.utils.NumberUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "picture")
public class PictureController {

    @Autowired private PictureDAO dao;
    @Autowired private ImageManager imageManager;
    @Value("${face.image.width}") private int imageWidth;
    @Value("${face.image.height}") private int imageHeight;

    @RequestMapping(value = "getImage/{fileId}")
    public void getImage(@PathVariable(value = "fileId") String fileId, HttpServletResponse response) throws IOException {
        final DBImage image = dao.get(NumberUtils.decode(fileId), DBImage.class);
        if(null != image) {
            response.setContentType("image/" + image.getFormat());
            response.setContentLength(image.getSize());
            try (OutputStream os = response.getOutputStream()) {
                os.write(image.getContent());
                os.flush();
                os.close();
            }
        }
    }

    @RequestMapping(value = "crop")
    public String crop(@RequestParam(value = "fileId", required = true) String fileId,
                       @RequestParam(value = "selection", required = true) String jsonSelection,
                       @RequestParam(value = "algorithm", required = true) int algorithm) throws JSONException {
        final JSONObject answer = new JSONObject();
        if (null != jsonSelection && !jsonSelection.isEmpty()) {
            final CropInfo cropInfo = CropInfo.fromJson(new JSONObject(jsonSelection));
            final DBImage image = dao.get(NumberUtils.decode(fileId), DBImage.class);
            if(null != image) {
                final BufferedImage templateImage = imageManager.resize(
                        imageManager.crop(image.getContent(), cropInfo),
                        imageWidth, imageHeight, algorithm
                );
                final DBImage dbImage = imageManager.toDBImage(templateImage, image.getFormat());
                dbImage.setClazz(ImageManager.IMAGE_CLASS_CROPPED_CODE);
                dbImage.setParentId(image.getId());
                dao.save(dbImage);

                final String id = NumberUtils.encode(dbImage.getId());
                answer.put("fileId", id);
                answer.put("status", "ok");
                answer.put("width", templateImage.getWidth());
                answer.put("height", templateImage.getHeight());
                answer.put("src", "./picture/getImage/" + id);
            } else {
                answer.put("status", "error");
            }
        } else {
            answer.put("status", "error");
        }
        return answer.toString();
    }

    @RequestMapping(value = "upload")
    public String upload(@RequestParam(value = "image", required = true) String jsonImage) throws JSONException {
        final JSONObject image = new JSONObject(jsonImage);
        final String type = image.getString("type");
        final String src = image.getString("src");
        final byte[] binaryData;
        if(ImageManager.BASE64_IMAGE_TYPE.equals(type)){
            binaryData = imageManager.fromBase64(src);
        } else {
            binaryData = imageManager.fromUrl(src);
        }
        final DBImage dbImage = imageManager.toDBImage(binaryData, ImageManager.DEFAULT_IMAGE_FORMAT);
        dbImage.setClazz(ImageManager.IMAGE_CLASS_UPLOADED_CODE);
        dao.save(dbImage);

        final JSONObject answer = new JSONObject();
        answer.put("status", "ok");
        answer.put("width", dbImage.getWidth());
        answer.put("height", dbImage.getHeight());
        answer.put("fileId", NumberUtils.encode(dbImage.getId()));
        answer.put("src", "./picture/getImage/" + NumberUtils.encode(dbImage.getId()));
        return answer.toString();
    }

    @RequestMapping(value = "storedImages")
    public String getAllStoredImages(@RequestParam(value = "type", required = true) String type){
        final List<DBImage> dbImages = dao.getImages(imageWidth, imageHeight, type);
        final JSONObject images = new JSONObject();
        final Multimap<String, DBImage> map = Multimaps.index(dbImages, DBImage::getClazz);
        for (String key : map.keySet()) {
            images.put(key, map.get(key).stream().map(image -> "./picture/getImage/" + NumberUtils.encode(image.getId())).collect(Collectors.toList()));
        }
        return images.toString();
    }
}