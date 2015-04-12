package com.kol.recognition.perceptualHash.hash;

import com.kol.RGBImage;
import com.kol.Utils;
import com.kol.recognition.perceptualHash.Hash;
import com.kol.recognition.perceptualHash.bitsChain.BitsChainToString;
import com.kol.recognition.perceptualHash.dct.MatlabDCT;
import com.kol.recognition.perceptualHash.monochrome.ToMonochrome;
import com.kol.recognition.perceptualHash.resize.ResizeImage;

import java.awt.image.BufferedImage;

public final class DCTHash extends PerceptualHash {

    public DCTHash(int width, int height,
                   ResizeImage resizeImage,
                   ToMonochrome toMonochrome,
                   BitsChainToString bitsChainToString) {
        super(width, height, resizeImage, toMonochrome, bitsChainToString);
    }

    @Override
    public Hash getHash(final BufferedImage image) {
        final MatlabDCT dct = new MatlabDCT();
        final BufferedImage resize = resize(image, width, height);
        final BufferedImage monochrome = toMonochrome(resize);
        final RGBImage rgbImage = RGBImage.fromBufferedImage(monochrome);
        final double mean = Utils.mean(dct.dct(Utils.int2doubleArray(rgbImage.blue())));
        final String bitsChain = toBitsChain(rgbImage, mean);
        return new Hash(bitsChainToString(bitsChain), monochrome);
    }
}