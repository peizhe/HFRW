package com.kol.recognition.perceptualHash;

import java.awt.image.BufferedImage;

public interface PerceptualHash {

    BufferedImage toMonochrome(BufferedImage image);

    BufferedImage resize(BufferedImage image, int width, int height);

    double meanPixelValue(BufferedImage image);

    String toBitsChain(BufferedImage image, double meanPixelValue);

    String getHash(BufferedImage image);

    String getHash(byte[] binaryData);

    String bitsChainToString(String bitsChain);
}