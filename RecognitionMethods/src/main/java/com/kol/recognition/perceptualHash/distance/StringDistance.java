package com.kol.recognition.perceptualHash.distance;

@FunctionalInterface
public interface StringDistance {

    double getDistance(String first, String second);
}
