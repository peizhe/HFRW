package com.kol.recognition.perceptualHash.resize;

import java.awt.image.BufferedImage;

@FunctionalInterface
public interface ResizeImage {
    BufferedImage resize(BufferedImage src, int width, int height);
}
