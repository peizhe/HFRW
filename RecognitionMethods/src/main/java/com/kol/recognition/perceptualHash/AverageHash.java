package com.kol.recognition.perceptualHash;

import com.google.common.base.Splitter;
import com.kol.RGBImage;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public final class AverageHash implements PerceptualHash {

    public static final int BINARY_RADIX = 2;
    public static final int BITS_IN_ONE_STR_ELEMENT = 31;
    private int smallWidth;
    private int smallHeight;

    public AverageHash(int smallWidth, int smallHeight) {
        this.smallWidth = smallWidth;
        this.smallHeight = smallHeight;
    }

    @Override
    public BufferedImage toMonochrome(BufferedImage image) {
//        final int width = image.getWidth();
//        final int height = image.getHeight();
//        final BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
//        final Graphics2D g = dst.createGraphics();
//        g.drawImage(image, 0, 0, width, height, null);
//        g.dispose();
        return image;
    }

    @Override
    public BufferedImage resize(BufferedImage image, int width, int height) {
        return Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, width, height);
    }

    @Override
    public double meanPixelValue(BufferedImage image) {
        return RGBImage.fromBufferedImage(image).meanBlue();
    }

    @Override
    public String toBitsChain(BufferedImage image, final double meanPixelValue) {
        final StringBuilder bits = new StringBuilder();
        for (int val : RGBImage.fromBufferedImage(image).toBlue()) {
            if(val < meanPixelValue) {
                bits.append(0);
            } else {
                bits.append(1);
            }
        }
        return bits.toString();
    }

    @Override
    public String getHash(BufferedImage image) {
        final BufferedImage resize = resize(image, smallWidth, smallHeight);
        final BufferedImage monochrome = toMonochrome(resize);
        final double mean = meanPixelValue(monochrome);
        final String bitsChain = toBitsChain(monochrome, mean);
        return bitsChainToString(bitsChain);
    }

    @Override
    public String getHash(byte[] binaryData) {
        try {
            return getHash(ImageIO.read(new ByteArrayInputStream(binaryData)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String bitsChainToString(final String bitsChain) {
        final String bits;
        final int length = bitsChain.length();
        final int mod = length % BITS_IN_ONE_STR_ELEMENT;
        if(mod != 0) {
            bits = Utils.leadingZeros(bitsChain, length + (BITS_IN_ONE_STR_ELEMENT - mod));
        } else {
            bits = bitsChain;
        }

        final StringBuilder sb = new StringBuilder();
        final Iterable<String> iterable = Splitter.fixedLength(BITS_IN_ONE_STR_ELEMENT).split(bits);
        for (String el : iterable) {
            sb.append(Integer.toHexString(Integer.parseInt(el, BINARY_RADIX)));
        }
        return Utils.leadingZeros(sb.toString(), 60);
    }
}
