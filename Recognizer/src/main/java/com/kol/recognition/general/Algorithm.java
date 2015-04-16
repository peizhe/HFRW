package com.kol.recognition.general;

import com.google.common.collect.Multimap;

import java.awt.image.BufferedImage;
import java.util.List;

public interface Algorithm {

    String classify(BufferedImage image);

    Multimap<String, Image> getTraining();

    List<BufferedImage> getProcessedImages();
}