package com.kol.recognition.components;

import com.kol.recognition.beans.CropInfo;
import com.kol.recognition.beans.entities.DBImage;
import com.kol.recognition.utils.ImageUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

@Component
public class ImageManager {

    public static final int BILINEAR = 0;
    public static final String DEFAULT_IMAGE_FORMAT = "bmp";
    public static final String BASE64_IMAGE_TYPE = "base64";

    public byte[] fromBase64(final String src) {
        return DatatypeConverter.parseBase64Binary(src);
    }

    public byte[] fromUrl(final String url) {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            URLConnection urlConnection = new URL(url).openConnection();
            FileCopyUtils.copy(urlConnection.getInputStream(), baos);
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public BufferedImage crop(final BufferedImage src, final CropInfo cropInfo) {
        final BufferedImage dst = new BufferedImage(cropInfo.getWidth(), cropInfo.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        final Graphics2D g = dst.createGraphics();
        g.drawImage(src, 0, 0, cropInfo.getWidth(), cropInfo.getHeight(), cropInfo.getStartX(),
                cropInfo.getStartY(), cropInfo.getStartX() + cropInfo.getWidth(), cropInfo.getStartY() + cropInfo.getHeight(), null);
        g.dispose();
        return dst;
    }

    public BufferedImage crop(final byte[] binaryData, final CropInfo cropInfo) {
        final BufferedImage src = ImageUtils.fromByteArray(binaryData);
        final BufferedImage dst = new BufferedImage(cropInfo.getWidth(), cropInfo.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        final Graphics2D g = dst.createGraphics();
        g.drawImage(src, 0, 0, cropInfo.getWidth(), cropInfo.getHeight(), cropInfo.getStartX(),
                cropInfo.getStartY(), cropInfo.getStartX() + cropInfo.getWidth(), cropInfo.getStartY() + cropInfo.getHeight(), null);
        g.dispose();
        return dst;
    }

    public BufferedImage resize(final BufferedImage originalImage, final int width, final int height, final int algorithm) {
        if(BILINEAR == algorithm){
            return Scalr.resize(originalImage, Scalr.Method.QUALITY, width, height);
        } else {
            return Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, width, height);
        }
    }

    public byte[] toByteArray(final BufferedImage image, final String format) {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, format, output);
        } catch (IOException ignored) {}
        return output.toByteArray();
    }

    public DBImage toDBImage(final BufferedImage image, final String format) {
        final DBImage dbImage = new DBImage();
        final byte[] binaryData = toByteArray(image, format);
        dbImage.setFormat(format);
        dbImage.setWidth(image.getWidth());
        dbImage.setHeight(image.getHeight());
        dbImage.setSize(binaryData.length);
        dbImage.setContentFromBytes(binaryData);
        return dbImage;
    }

    public DBImage toDBImage(final byte[] binaryData, final String format) {
        return toDBImage(ImageUtils.fromByteArray(binaryData), format);
    }

    public static byte[] stringToByte(final String content) {
        return Base64.getDecoder().decode(content.getBytes());
    }

    public static String byteToString(final byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes));
    }
}
