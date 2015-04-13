package com.kol.recognition.perceptualHash.hash;

import com.kol.RGBImage;
import com.kol.Utils;
import com.kol.dct.DCT;
import com.kol.recognition.perceptualHash.Hash;
import com.kol.recognition.perceptualHash.bitsChain.BitsChainBigIntToString;
import com.kol.recognition.perceptualHash.bitsChain.BitsChainToString;
import com.kol.recognition.perceptualHash.monochrome.ToByteGray;
import com.kol.recognition.perceptualHash.monochrome.ToMonochrome;
import com.kol.recognition.perceptualHash.resize.ResizeImage;
import com.kol.recognition.perceptualHash.resize.ScalrResize;

import java.awt.image.BufferedImage;

public final class DCTHash extends PerceptualHash {

    public DCTHash(int width, int height,
                   ResizeImage resizeImage,
                   ToMonochrome toMonochrome,
                   BitsChainToString bitsChainToString) {
        super(width, height, resizeImage, toMonochrome, bitsChainToString);
    }

    public DCTHash(int smallWidth, int smallHeight) {
        super(smallWidth, smallHeight, new ScalrResize(), new ToByteGray(), new BitsChainBigIntToString());
    }

    @Override
    public Hash getHash(final BufferedImage image) {
        final BufferedImage resize = resize(image, width, height);
        final BufferedImage monochrome = toMonochrome(resize);
        final RGBImage rgbImage = RGBImage.fromBufferedImage(monochrome);
        final double mean = Utils.mean(DCT.dct(Utils.toDouble(rgbImage.content())));
        final String bitsChain = toBitsChain(rgbImage, mean);
        return new Hash(bitsChainToString(bitsChain), monochrome);
    }
}