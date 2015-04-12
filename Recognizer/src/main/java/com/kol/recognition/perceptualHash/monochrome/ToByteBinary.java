package com.kol.recognition.perceptualHash.monochrome;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class ToByteBinary implements ToMonochrome {
    /**
     * This application renders a color image on a pure black-and-white image created from scratch.
     * The JAI API is not used in this example.
     */
    @Override
    public BufferedImage apply(final BufferedImage src) {
        final BufferedImage im = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        // Get the graphics context for the black-and-white image.
        final Graphics2D g2d = im.createGraphics();
        // Render the input image on it.
        g2d.drawImage(src, 0, 0, null);
        // Store the resulting image using the PNG format.
        return im;
    }
}
