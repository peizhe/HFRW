package com.kol.recognition.perceptualHash.bean;

import java.awt.image.BufferedImage;

public class Hash {

    private String hash;
    private BufferedImage image;

    public Hash(String hash, BufferedImage image) {
        this.hash = hash;
        this.image = image;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "hash='" + hash + "', image=" + image;
    }
}
