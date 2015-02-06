package com.kol.recognition.perceptualHash.monochrome;

import java.awt.image.BufferedImage;

@FunctionalInterface
public interface ToMonochrome {

    BufferedImage apply(BufferedImage src);
}
