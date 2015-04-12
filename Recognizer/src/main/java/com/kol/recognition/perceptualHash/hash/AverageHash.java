package com.kol.recognition.perceptualHash.hash;

import com.kol.RGBImage;
import com.kol.recognition.perceptualHash.Hash;
import com.kol.recognition.perceptualHash.bitsChain.BitsChainToString;
import com.kol.recognition.perceptualHash.monochrome.ToMonochrome;
import com.kol.recognition.perceptualHash.resize.ResizeImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public final class AverageHash extends PerceptualHash {

    public AverageHash(int smallWidth, int smallHeight,
                       ResizeImage resizeImage,
                       ToMonochrome toMonochrome,
                       BitsChainToString toString) {
        super(smallWidth, smallHeight, resizeImage, toMonochrome, toString);
    }

    @Override
    public Hash getHash(BufferedImage image) {
        final BufferedImage resize = resize(image, width, height);
        final BufferedImage monochrome = toMonochrome(resize);
        final RGBImage rgbImage = RGBImage.fromBufferedImage(monochrome);
        final double mean = meanPixelValue(rgbImage);
        final String bitsChain = toBitsChain(rgbImage, mean);
        return new Hash(bitsChainToString(bitsChain), monochrome);
    }
}