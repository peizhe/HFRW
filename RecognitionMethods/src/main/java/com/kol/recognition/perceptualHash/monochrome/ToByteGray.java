package com.kol.recognition.perceptualHash.monochrome;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class ToByteGray implements ToMonochrome {
    @Override
    public BufferedImage apply(final BufferedImage src) {
        final BufferedImage grayImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        final Graphics2D g = grayImage.createGraphics();
        g.drawImage(src, 0, 0, null);
        return grayImage;
    }
}
