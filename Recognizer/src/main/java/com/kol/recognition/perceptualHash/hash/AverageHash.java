package com.kol.recognition.perceptualHash.hash;

import com.google.common.collect.Lists;
import com.kol.RGBImage;
import com.kol.recognition.perceptualHash.bean.Hash;
import com.kol.recognition.perceptualHash.bitsChain.BitsChainBigIntToString;
import com.kol.recognition.perceptualHash.bitsChain.BitsChainToString;
import com.kol.recognition.perceptualHash.monochrome.ToByteGray;
import com.kol.recognition.perceptualHash.monochrome.ToMonochrome;
import com.kol.recognition.perceptualHash.resize.ResizeImage;
import com.kol.recognition.perceptualHash.resize.ScalrResize;

import java.awt.image.BufferedImage;
import java.util.List;

public final class AverageHash extends PerceptualHash {

    public AverageHash(int smallWidth, int smallHeight,
                       ResizeImage resizeImage,
                       ToMonochrome toMonochrome,
                       BitsChainToString toString) {
        super(smallWidth, smallHeight, resizeImage, toMonochrome, toString);
    }

    public AverageHash(int smallWidth, int smallHeight) {
        super(smallWidth, smallHeight, new ScalrResize(), new ToByteGray(), new BitsChainBigIntToString());
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

    @Override
    public List<BufferedImage> getHashImage(BufferedImage image) {
        final BufferedImage resize = resize(image, width, height);
        final BufferedImage monochrome = toMonochrome(resize);
        final RGBImage rgbImage = RGBImage.fromBufferedImage(monochrome);
        final double mean = meanPixelValue(rgbImage);
        return Lists.newArrayList(toBitsChainImgContent(rgbImage, mean), monochrome);
    }
}