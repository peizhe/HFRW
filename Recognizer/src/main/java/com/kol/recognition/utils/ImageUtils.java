package com.kol.recognition.utils;

import Jama.Matrix;
import com.kol.recognition.general.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class ImageUtils {

    /**
     * Convert Matrix to PGM with numbers of row and column
     */
    public static Matrix normalize(final Matrix input, final int height, final int width) {
        final int row = input.getRowDimension();

        for (int i = 0; i < row; i++) {
            input.set(i, 0, 0 - input.get(i, 0));
        }

        double max = input.get(0, 0);
        double min = input.get(0, 0);
        for (int i = 1; i < row; i++) {
            if (max < input.get(i, 0)) {
                max = input.get(i, 0);
            }
            if (min > input.get(i, 0)) {
                min = input.get(i, 0);
            }
        }

        final Matrix result = new Matrix(height, width);
        for (int p = 0; p < width; p++) {
            for (int q = 0; q < height; q++) {
                double value = input.get(p * height + q, 0);
                value = (value - min) * 255 / (max - min);
                result.set(q, p, value);
            }
        }
        return result;
    }

    /**
     * convert matrices to images
     */
    public static List<BufferedImage> convertMatricesToImage(final Matrix imageMatrix, final int height, final int width) {
        final int row = imageMatrix.getRowDimension();
        final int column = imageMatrix.getColumnDimension();
        final List<BufferedImage> images = new ArrayList<>();

        for (int i = 0; i < column; i++) {
            final Matrix eigen = normalize(imageMatrix.getMatrix(0, row - 1, i, i), height, width);
            final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            final WritableRaster raster = img.getRaster();

            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    raster.setSample(k, j, 0, (int) eigen.get(j, k));
                }
            }
            images.add(img);
        }
        return images;
    }

    public static BufferedImage convertMatrixToImage(final Matrix imageMatrix, final int height, final int width) {
        final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        final WritableRaster raster = img.getRaster();

        for (int j = 0; j < height; j++) {
            for (int k = 0; k < width; k++) {
                raster.setSample(k, j, 0, imageMatrix.get(j, k));
            }
        }
        return img;
    }

    public static void saveImageToFile(final BufferedImage image, final String name, final String format) throws IOException {
        final Path path = Paths.get(name + "." + format);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        ImageIO.write(image, format, Files.newOutputStream(path));
    }

    /**
     * Read PGM image from file and Convert to Matrix
     */
    public static Matrix convertPGMtoMatrix(final String path) throws IOException {
        final int picWidth;
        final int picHeight;
        try(FileInputStream imageFIS = new FileInputStream(path)) {
            final Scanner scan = new Scanner(imageFIS);
            // Discard the magic number
            scan.nextLine();
            // Read pic width, height and max value
            picWidth = scan.nextInt();
            picHeight = scan.nextInt();
        }

        final double[][] data2D = new double[picHeight][picWidth];
        // Now parse the file as binary data
        try(DataInputStream dis = new DataInputStream(new FileInputStream(path))) {
            // look for 4 lines (i.e.: the header) and discard them
            int numNewLines = 3;
            while (numNewLines > 0) {
                char c;
                do {
                    c = (char) (dis.readUnsignedByte());
                } while (c != '\n');
                numNewLines--;
            }
            // read the image data
            for (int row = 0; row < picHeight; row++) {
                for (int col = 0; col < picWidth; col++) {
                    data2D[row][col] = dis.readUnsignedByte();
                }
            }
        }
        return new Matrix(data2D);
    }

    /**
     * @param inPath - path to input PGM image
     * @param outPath - path to new image
     * @param fileName - name of input and output image
     * @param format - new format of image
     * @throws java.io.IOException
     */
    public static void convertPGMTo(final String inPath, final String outPath, final String fileName, final String format) throws IOException {
        final Matrix imageMatrix = convertPGMtoMatrix(inPath + fileName + ".pgm");
        ImageUtils.saveImageToFile(ImageUtils.convertMatrixToImage(imageMatrix, imageMatrix.getRowDimension(), imageMatrix.getColumnDimension()), outPath + fileName, format);
    }

    /**
     * Convert BufferedImage to Matrix
     */
    public static Matrix toMatrix(final BufferedImage image) {
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

    public static Matrix toMatrix(final Image image) {
        return toMatrix(fromByteArray(image.getContent()));
    }

    public static Matrix toVector(final BufferedImage image) {
        return toVector(toMatrix(image));
    }

    /**
     * Convert a m by n matrix into a m*n by 1 matrix
     */
    public static Matrix toVector(final Matrix input) {
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

    public static Matrix toVector(final Image input) {
        return toVector(toMatrix(input));
    }

    public static BufferedImage fromByteArray(final byte[] binaryData) {
        try {
            return ImageIO.read(new ByteArrayInputStream(binaryData));
        } catch (IOException e) {
            return null;
        }
    }

    public static BufferedImage binaryImage(final int[][] content, final int width, final int height) {
        final byte BLACK = (byte)0;
        final byte WHITE = (byte)255;
        final byte[] map = {BLACK, WHITE};
        final IndexColorModel icm = new IndexColorModel(1, map.length, map, map, map);
        // create checkered data
        final int[] data = new int[width*height];
        for(int i = 0; i < width; i++) {
            System.arraycopy(content[i], 0, data, i * height, height);
        }
        // create image from color model and data
        final WritableRaster raster = icm.createCompatibleWritableRaster(width, height);
        raster.setPixels(0, 0, width, height, data);
        return new BufferedImage(icm, raster, false, null);
    }
}