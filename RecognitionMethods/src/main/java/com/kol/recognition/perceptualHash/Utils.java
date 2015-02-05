package com.kol.recognition.perceptualHash;

import Jama.Matrix;

import java.awt.image.BufferedImage;

public final class Utils {

    private Utils(){}

    public static int[][] getImagePixelData(final BufferedImage image) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        // read the image data
        final int[][] data2D = new int[height][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                data2D[j][i] = image.getRGB(i, j);
            }
        }
        return data2D;
    }

    /**
     * Convert BufferedImage to Matrix
     */
    public static Matrix convertToMatrix(final BufferedImage image) {
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

    public static String leadingZeros(final String str, final int length) {
        if (str.length() >= length) {
            return str;
        } else {
            return String.format("%0" + (length - str.length()) + "d%s", 0, str);
        }
    }
}
