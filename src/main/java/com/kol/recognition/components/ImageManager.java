package com.kol.recognition.components;

import Jama.Matrix;
import com.kol.recognition.beans.CropInfo;
import com.kol.recognition.beans.DBImage;
import com.kol.recognition.interfaces.ByteData;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Component
public class ImageManager {

    public static final int BILINEAR = 0;
    public static final String DEFAULT_IMAGE_FORMAT = "bmp";
    public static final String BASE64_IMAGE_TYPE = "base64";
    public static final String IMAGE_CLASS_CROPPED_CODE = "CRPD";
    public static final String IMAGE_CLASS_UPLOADED_CODE = "UPLD";

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

    public BufferedImage fromByteArray(final byte[] binaryData) {
        try {
            return ImageIO.read(new ByteArrayInputStream(binaryData));
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
        final BufferedImage src = fromByteArray(binaryData);
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
        dbImage.setContent(binaryData);
        return dbImage;
    }

    public DBImage toDBImage(final byte[] binaryData, final String format) {
        return toDBImage(fromByteArray(binaryData), format);
    }

    /**
     * Convert BufferedImage to Matrix
     */
    public Matrix toMatrix(final BufferedImage image) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        // read the image data
        final double[][] data2D = new double[height][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                data2D[j][i] = image.getRGB(i, j);
            }
        }
        return new Matrix(data2D);
    }

    public Matrix toMatrix(final ByteData image) {
        return toMatrix(fromByteArray(image.getContent()));
    }

    /**
     * Convert a m by n matrix into a m*n by 1 matrix
     */
    public Matrix toVector(final Matrix input) {
        final int m = input.getRowDimension();
        final int n = input.getColumnDimension();

        final Matrix result = new Matrix(m * n, 1);
        for (int p = 0; p < n; p++) {
            for (int q = 0; q < m; q++) {
                result.set(p * m + q, 0, input.get(q, p));
            }
        }
        return result;
    }

    public Matrix toVector(final ByteData input) {
        return toVector(toMatrix(input));
    }
}
