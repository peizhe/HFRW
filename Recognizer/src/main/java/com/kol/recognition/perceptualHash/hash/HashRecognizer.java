package com.kol.recognition.perceptualHash.hash;

import com.google.common.collect.Multimap;
import com.kol.recognition.general.Algorithm;
import com.kol.recognition.general.Image;
import com.kol.recognition.perceptualHash.bean.Hash;
import com.kol.recognition.perceptualHash.distance.StringDistance;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

public class HashRecognizer implements Algorithm {

    private PerceptualHash hash;
    private StringDistance distance;
    private Multimap<String, Hash> data;
    private Multimap<String, Image> train;

    public HashRecognizer(PerceptualHash hash, StringDistance distance, Multimap<String, Hash> data, Multimap<String, Image> train) {
        this.hash = hash;
        this.distance = distance;
        this.data = data;
        this.train = train;
    }

    @Override
    public String classify(BufferedImage image) {
        final Hash imageHash = hash.getHash(image);
        double resultDistance = Double.MAX_VALUE;
        String resultClassName = null;
        for (String className : data.keySet()) {
            for (Hash hash : data.get(className)) {
                final double distance = this.distance.getDistance(hash.getHash(), imageHash.getHash());
                if(distance < resultDistance) {
                    resultDistance = distance;
                    resultClassName = className;
                }
            }
        }
        return resultClassName;
    }

    @Override
    public Multimap<String, Image> getTraining() {
        return train;
    }

    @Override
    public List<BufferedImage> getProcessedImages() {
        return data.values().stream().map(Hash::getImage).collect(Collectors.toList());
    }
}