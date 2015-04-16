package com.kol.recognition.perceptualHash.hash;

import com.kol.RGBImage;
import com.kol.recognition.perceptualHash.bean.Hash;
import com.kol.recognition.perceptualHash.bitsChain.BitsChainToString;
import com.kol.recognition.perceptualHash.monochrome.ToMonochrome;
import com.kol.recognition.perceptualHash.resize.ResizeImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public abstract class PerceptualHash {

    protected int width;
    protected int height;
    protected ResizeImage resizeImage;
    protected ToMonochrome toMonochrome;
    protected BitsChainToString bitsChainToString;

    public PerceptualHash(int width, int height,
                          ResizeImage resizeImage,
                          ToMonochrome toMonochrome,
                          BitsChainToString bitsChainToString) {
        this.width = width;
        this.height = height;
        this.resizeImage = resizeImage;
        this.toMonochrome = toMonochrome;
        this.bitsChainToString = bitsChainToString;
    }

    public abstract Hash getHash(BufferedImage image);

    public Hash getHash(byte[] binaryData) {
        try {
            return getHash(ImageIO.read(new ByteArrayInputStream(binaryData)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String toBitsChain(final RGBImage monochrome, final double meanPixelValue) {
        final StringBuilder bits = new StringBuilder();
        for (int val : monochrome.vectorContent()) {
            if(val < meanPixelValue) {
                bits.append(0);
            } else {
                bits.append(1);
            }
        }
        return bits.toString();
    }

    protected double meanPixelValue(final RGBImage monochrome) {
        return monochrome.mean();
    }

    protected BufferedImage toMonochrome(final BufferedImage image) {
        return toMonochrome.apply(image);
    }

    protected BufferedImage resize(final BufferedImage image, final int width, final int height) {
        return resizeImage.resize(image, width, height);
    }

    protected String bitsChainToString(final String bitsChain) {
        return bitsChainToString.toString(bitsChain);
    }
}