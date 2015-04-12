package com.kol.recognition.perceptualHash.resize;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

public final class ScalrResize implements ResizeImage {
    @Override
    public BufferedImage resize(final BufferedImage src, final int width, final int height) {
        return Scalr.resize(src, Scalr.Method.ULTRA_QUALITY, width, height);
    }
}
