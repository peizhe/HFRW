package com.kol.recognition.perceptualHash.hash;

import com.kol.RGBImage;
import com.kol.recognition.perceptualHash.bean.Hash;
import com.kol.recognition.perceptualHash.bitsChain.BitsChainToString;
import com.kol.recognition.perceptualHash.monochrome.ToMonochrome;
import com.kol.recognition.perceptualHash.resize.ResizeImage;
import com.kol.recognition.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public abstract class PerceptualHash {

    private static final int WHITE = 255;
    private static final int BLACK = 0;

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

    public abstract List<BufferedImage> getHashImage(BufferedImage image);

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

    protected BufferedImage toBitsChainImgContent(final RGBImage monochrome, final double meanPixelValue) {
        final int[][] content = monochrome.content();
        final int[][] bitsImg = new int[monochrome.width()][monochrome.height()];
        for (int i = 0; i < monochrome.width(); i++) {
            for (int j = 0; j < monochrome.height(); j++) {
                if(content[i][j] < meanPixelValue) {
                    bitsImg[i][j] = BLACK;
                } else {
                    bitsImg[i][j] = WHITE;
                }
            }
        }
//        return ImageUtils.binaryImage(Utils.transpose(bitsImg), monochrome.height(), monochrome.width());
        return ImageUtils.binaryImage(bitsImg, monochrome.width(), monochrome.height());
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